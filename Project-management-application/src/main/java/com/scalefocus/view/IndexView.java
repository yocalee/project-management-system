package com.scalefocus.view;

import com.scalefocus.repository.UserRepository;
import com.scalefocus.service.UserService;
import com.scalefocus.util.Printer;
import java.util.Scanner;

public class IndexView {
    private static View view;
    private static UserService userService;
    private static final Scanner sc = new Scanner(System.in);

    public static void login(boolean isLoggedOut) {
        userService = new UserService(new UserRepository());

        if (!isLoggedOut) {
            Printer.greetings();
        }
        userService.init();

        String role = login();
        if (role.equalsIgnoreCase("admin")) {
            System.out.println("Welcome, administrator.");
            view = new AdminView(userService);
        } else {
            view = new UserView();
        }
        menu();
    }

    public static void menu() {
        while (!view.exit) {
            view.menu(false);
        }
        return;
    }

    public static String login() {
        System.out.println("Enter credentials to login...");
        while (UserService.user == null) {
            System.out.println("Enter username... ");
            String username = sc.nextLine();
            System.out.println("Enter password... ");
            String password = sc.nextLine();

            UserService.user = userService.getUserByUsernameAndPassword(username, password);
            if (UserService.user == null) {
                System.out.println("Try again.");
            } else {
                break;
            }
        }
        return UserService.user.getPrivilege();
    }
}
