package com.group1;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionToVS {
    public Connection databaseLink;

    public Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/student";
        String user = "root";
        String password = "Heng012631355";
        try {
            databaseLink = DriverManager.getConnection(url, user, password);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return databaseLink;
    }
}
