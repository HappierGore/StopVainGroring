package com.happiergore.stopvinesgrowing.sqlite;

import com.happiergore.stopvinesgrowing.main;
import static com.happiergore.stopvinesgrowing.IOHelper.ExportResource;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;

/**
 * @author HappierGore
 */
public abstract class SQLite {

    public static String path;
    public static final String TABLE = "vines";
    public static final String DBNAME = "SavedVines.db";

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

        if (!dataFolder.exists()) {
            dataFolder.mkdir();
            //Generar base de datos en caso de que no exista
        }

        generateDB(dataFolder.getAbsolutePath());

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

    private static void generateDB(String dataFolderPath) {
        File dataBase = new File(dataFolderPath + "/" + DBNAME);

        if (!dataBase.exists()) {
            try {
                ExportResource("/" + DBNAME, dataFolderPath);
            } catch (Exception ex) {
                System.out.println("An error occured when trying to export the database" + ex);
            }
        }
    }

}
