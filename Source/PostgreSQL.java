package com.gokhanyildiz9535;
import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class PostgreSQL {

    /* ------------------------------------------------- */
    /* Statics */

    public static final Dotenv dotenv = Dotenv.load();
    public static final String PGSQL_DRIVER = "org.postgresql.Driver";
    public Connection conn;
    public Statement stmt;
    public ResultSet rs;
    public static final String PGSQL_CONNECTION = "jdbc:postgresql://" +
            dotenv.get("DB_HOST") + ":" + dotenv.get("DB_PORT") + "/" + dotenv.get("DB_NAME");
    public static final String DB_USERNAME = dotenv.get("DB_USERNAME");
    public static final String DB_PASSWORD = dotenv.get("DB_PASSWORD");

    /* ------------------------------------------------- */

    public PostgreSQL () {

    }

    public void connect_POSTGRESQL() {
        try {

            Class.forName(PGSQL_DRIVER);
            conn = DriverManager.getConnection(PGSQL_CONNECTION, DB_USERNAME, DB_PASSWORD);
            conn.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
