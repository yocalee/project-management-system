package com.scalefocus;

import com.scalefocus.repository.connection.DBConnection;
import com.scalefocus.util.Printer;
import com.scalefocus.view.IndexView;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        DBConnection.connection();
        IndexView.login(false);
        Printer.exitApplication();
    }
}
