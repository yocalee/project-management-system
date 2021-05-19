package com.scalefocus.repository.connection;

import java.util.Scanner;
import java.sql.*;

public class DBConnection {
    private static final Scanner sc = new Scanner(System.in);

    private static String CONNECTION_STR = "jdbc:mysql://localhost:3306/project_management_system?" +
            "createDatabaseIfNotExist=true&amp;autoReconnect=true&useSSL=false&amp;";
    private static Connection connection;

    public static void setConnection(String username, String password) throws SQLException {
        connection = DriverManager.getConnection(CONNECTION_STR, username, password);
    }

    public static void connection() throws SQLException {
        System.out.println("Before starting the application, please enter your MySQL server's username in order to connect with the server: ");
        String username = sc.nextLine();
        System.out.println("Great! Now enter the password...");
        String password = sc.nextLine();
        setConnection(username, password);
    }

    public static Connection getConnection(){
        return connection;
    }
}
