package com.example.ApplicationsManager.db.DbAccess;

import java.sql.*;
import java.util.logging.Logger;

public class ConnectionFactory {


    private static final Logger logger = Logger.getLogger(ConnectionFactory.class.getName());
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String dburl = "jdbc:mysql://localhost:3306/utcn30234?useSSL=false";
    private static final String user = "root";
    private static final String pass = "";

    private static ConnectionFactory singleInstance = new ConnectionFactory();

    private ConnectionFactory() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return singleInstance.createConnection();
    }

    public static void close(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void close(Statement stm) {
        if (stm != null) {
            try {
                stm.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void close(ResultSet rst) {
        if (rst != null) {
            try {
                rst.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private Connection createConnection() {
        try {
            Connection con = DriverManager.getConnection(dburl, user, pass);
            return con;
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return null;
    }
}
