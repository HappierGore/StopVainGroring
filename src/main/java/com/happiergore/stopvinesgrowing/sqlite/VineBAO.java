package com.happiergore.stopvinesgrowing.sqlite;

import com.happiergore.stopvinesgrowing.data.VineData;
import static com.happiergore.stopvinesgrowing.main.console;
import static com.happiergore.stopvinesgrowing.main.debugMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author HappierGore
 */
public class VineBAO extends SQLite {

    public static Set<VineData> vainsData = new HashSet<>();

    private static final String SELECT_SQL = "SELECT * FROM " + TABLE;
    private static final String DELETE_SQL = "DELETE FROM " + TABLE + " WHERE world = ? AND x = ? AND y = ? AND z = ?";
    private static final String INSERT_SQL = "INSERT INTO " + TABLE + "(world, x, y, z) VALUES(?, ?, ? ,?)";

    //Create 
    public static boolean insert(VineData vain) {

        try ( Connection conn = connect();  PreparedStatement db = conn.prepareStatement(INSERT_SQL)) {
            db.setString(1, vain.getWorld().getName());
            db.setInt(2, vain.getX());
            db.setInt(3, vain.getY());
            db.setInt(4, vain.getZ());

            db.executeUpdate();

            vainsData.add(vain);

            db.close();

        } catch (SQLException e) {
            System.out.println("Error from insert: " + e);
        }
        return true;
    }

    public static boolean remove(VineData vain) {

        try ( Connection conn = connect();  PreparedStatement db = conn.prepareStatement(DELETE_SQL)) {

            db.setString(1, vain.getWorld().getName());
            db.setInt(2, vain.getX());
            db.setInt(3, vain.getY());
            db.setInt(4, vain.getZ());

            db.executeUpdate();
            vainsData.clear();
            select();

            db.close();

        } catch (SQLException e) {

            System.out.println("Error from remove: " + e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean select() {

        try ( Connection conn = connect();  PreparedStatement pstmt = conn.prepareStatement(SELECT_SQL); // set the corresponding param
                  ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String world = rs.getString("world");
                int x = rs.getInt("x");
                int y = rs.getInt("y");
                int z = rs.getInt("z");
                vainsData.add(new VineData(world, x, y, z));
            }

            pstmt.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error from select: " + e.getMessage());
        }
        return true;
    }

    public static boolean compareWithDB(VineData data) {

        if (debugMode) {
            console.infoMsg("Entries saved into DB\n" + vainsData.toString());
            console.infoMsg("Detailed info:");
            for (VineData entry : vainsData) {
                console.infoMsg("Entry: " + entry.toString());
            }
        }
        boolean result = false;
        try {
            for (VineData entry : vainsData) {
                if (debugMode) {
                    console.infoMsg("Object to compare: " + data.toString());
                    console.infoMsg("Comparing with entry: " + entry.toString());
                }
                boolean worldEqual = entry.getWorld().getName().equals(data.getWorld().getName());
                boolean xEqual = entry.getX() == data.getX();
                boolean yEqual = entry.getY() == data.getY();
                boolean zEqual = entry.getZ() == data.getZ();
                if (worldEqual && xEqual && yEqual && zEqual) {
                    result = true;
                }
            }
        } catch (Exception ex) {
            console.errorMsg("There was an error when trying to compare an entry to database. Please report it using debug"
                    + "mode (Enable it in config.yml) and send the log into my discord channel (Included in config.yml)");
        }
        return result;
    }

}
