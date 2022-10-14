package com.happiergore.stopvinesgrowing.SQLite;

import com.happiergore.stopvinesgrowing.main;
import com.happiergore.stopvinesgrowing.data.OldVineData;
import com.happiergore.stopvinesgrowing.data.ParentMDown;
import com.happiergore.stopvinesgrowing.data.ParentMUp;
import com.happiergore.stopvinesgrowing.data.VineJBDC;
import com.happiergore.stopvinesgrowing.events.OnVineGrowing;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;

/**
 *
 * @author HappieGore
 */
public class ImportData {

    /**
     * @author HappierGore
     */
    public static String path;
    public static final String TABLE = "vines";
    public static final String DBNAME = "SavedVines.db";

    private static final String SELECT_SQL = "SELECT * FROM " + TABLE;
    private static final String DELETE_SQL = "DELETE FROM " + TABLE + " WHERE world = ? AND x = ? AND y = ? AND z = ?";

    public static Set<OldVineData> oldVineData = new HashSet<>();

    public static boolean initialize() {
        try {
            try {
                Class.forName("org.sqlite.JDBC").newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                return false;
            }
        } catch (ClassNotFoundException ex) {
            return false;
        }

        File dataFolder = main.getPlugin(main.class).getDataFolder();

        path = "jdbc:sqlite:" + dataFolder.getAbsolutePath().replace('\\', '/') + "/" + DBNAME;

        File dataBase = new File(dataFolder.getAbsolutePath() + "/" + DBNAME);

        if (dataBase.exists()) {
            importData();
        }

        return true;
    }

    private static boolean remove(OldVineData vain) {
        try ( Connection conn = connect();  PreparedStatement db = conn.prepareStatement(DELETE_SQL)) {
            db.setString(1, vain.getWorld().getName());
            db.setInt(2, vain.getX());
            db.setInt(3, vain.getY());
            db.setInt(4, vain.getZ());

            db.executeUpdate();
            db.close();

        } catch (SQLException e) {
            System.out.println("Error from remove: " + e.getMessage());
            return false;
        }
        return true;
    }

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(path);
        } catch (SQLException e) {
            System.out.println("Error from connect: " + e.getMessage());
        }
        return conn;
    }

    private static boolean importData() {

        main.console.warnMsg("&6We found previous data saved using databases. We'll try to update them to the new format.");

        try ( Connection conn = connect();  PreparedStatement pstmt = conn.prepareStatement(SELECT_SQL); // set the corresponding param
                  ResultSet rs = pstmt.executeQuery()) {

            boolean corruptedData = false;

            while (rs.next()) {
                String world = rs.getString("world");
                int x = rs.getInt("x");
                int y = rs.getInt("y");
                int z = rs.getInt("z");
                int id = rs.getInt("id");
                if (world == null || world.isBlank() || world.equalsIgnoreCase("NULL")) {
                    main.console.errorMsg("&cThere's a record with nullish value in world on record &a" + id + "&c. Skiping it...");
                    corruptedData = true;
                    continue;
                }

                oldVineData.add(new OldVineData(world, x, y, z, id));
            }
            if (corruptedData) {
                main.console.infoMsg("&eThis may be corrupted data. Please, erase the database after finish importing.");
            }

            pstmt.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error from select: " + e.getMessage());
        }

        if (oldVineData.isEmpty()) {
            main.console.warnMsg("&6The database is empty o doesn't have more valid records. "
                    + "Please, erase this file to avoid this messsage. &r\n&ePath: &a" + path);
            return false;
        }

        if (main.debugMode) {
            main.console.infoMsg("Old vine data: " + oldVineData.toString());
        }

        oldVineData.forEach(oldVine -> {
            Block block = Bukkit.getWorld(oldVine.getWorld().getName()).getBlockAt(oldVine.getLocation());
            if (main.debugMode) {
                main.console.infoMsg("&eBlock: &r" + block.toString() + " &ein location: &r" + block.getLocation().toString());
            }
            if (OnVineGrowing.materialsDown.contains(block.getType().toString())) {
                if (remove(oldVine) && main.debugMode) {
                    main.console.infoMsg("&eThe next entry has been removed from database:&r\n" + oldVine.toString());
                }
                ParentMDown parent = new ParentMDown(block.getLocation(), block.getType().toString());
                VineJBDC.saveOne(parent);
                if (main.debugMode) {
                    main.console.infoMsg("&aA parent block has been imported successfully. &r\n" + parent.toString());
                } else {
                    main.console.infoMsg("&aA parent block has been imported successfully");
                }
            } else if (OnVineGrowing.materialsUP.contains(block.getType().toString())) {
                if (remove(oldVine) && main.debugMode) {
                    main.console.infoMsg("&eThe next entry has been removed from database:&r\n" + oldVine.toString());
                }
                ParentMUp parent = new ParentMUp(block.getLocation(), block.getType().toString());
                VineJBDC.saveOne(parent);
                if (main.debugMode) {
                    main.console.infoMsg("&aA parent block has been imported successfully. &r\n" + parent.toString());
                } else {
                    main.console.infoMsg("&aA parent block has been imported successfully");
                }
            } else {
                main.console.errorMsg("&cWe cannot import the object with data &r" + oldVine.toString() + " &cbecause the item type"
                        + " doesn't exists into materials list (From config.yml)");
            }
        });

        return true;
    }
}
