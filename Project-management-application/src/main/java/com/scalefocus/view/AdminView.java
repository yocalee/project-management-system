package com.scalefocus.view;

import com.scalefocus.exception.InvalidCredentials;
import com.scalefocus.model.User;
import com.scalefocus.repository.*;
import com.scalefocus.service.*;
import com.scalefocus.util.*;

public class AdminView extends View {
    private final UserService userService;
    private final ProjectService projectService;
    private final TeamService teamService;
    private final TaskService taskService;
    private final TaskView taskView;
    private final TeamView teamView;
    private final ProjectView projectView;
    private final WorkLogView workLogView;
    private final Validation.UserValidation userValidation;

    public AdminView(UserService userService) {
        this.userService = userService;
        taskService = new TaskService(new TaskRepository());
        projectService = new ProjectService(new ProjectRepository());
        teamService = new TeamService(new TeamRepository());
        taskView = new TaskView();
        projectView = new ProjectView();
        teamView = new TeamView();
        workLogView = new WorkLogView();
        userValidation = new Validation.UserValidation(new UserRepository());
    }

    @Override
    public void menu(boolean isLoggedOut) {
        Printer.adminMenu();
        String opt = sc.nextLine().toUpperCase();
        switch (opt) {
            case "U":
                adminUserMenu();
                break;
            case "T":
                teamView.menu(true);
                break;
            case "P":
                projectView.menu(true);
                break;
            case "A":
                taskView.menu(true);
                break;
            case "W":
                workLogView.menu(true);
                break;
            case "O":
                IndexView.login(true);
                UserService.user = null;
                break;
            case "X":
                exit = true;
                return;
        }
    }

    private void adminUserMenu() {
        Printer.adminMenuUser();
        String option = sc.nextLine().toUpperCase();
        switch (option) {
            case "R":
                register();
                break;
            case "E":
                edit();
                break;
            case "D":
                delete();
                break;
            case "V":
                view();
                break;
            case "B":
                return;
        }
        adminUserMenu();
    }

    private void view() {
        userService.findAll().forEach(u -> System.out.println(Printer.userDetails((User) u)));
    }

    private void delete() {
        System.out.println("Enter user id...");
        int userId = Integer.parseInt(sc.nextLine());

        if (!userValidation.validateId(userId)) {
            System.out.println("Invalid user id.");
            return;
        }

        userService.delete(userId);
        teamService.updateInCommonTable("users_teams", "user_id".split(", "), new Object[]{1}, " user_id = " + userId);
        taskService.deleteInCommonTable("users_tasks", "user_id = " + userId);
        projectService.update("creator_id".split(", "), new Object[]{1}, " creator_id = " + userId);
        taskService.update("status, creator_id".split(", "), new Object[]{"COMPLETED", 1}, " creator_id = " + userId);

        System.out.println("Successfully deleted user with id " + userId);
    }

    private void edit() {
        System.out.println("Enter user id...");
        int userId = Integer.parseInt(sc.nextLine());

        if (!userValidation.validateId(userId)) {
            System.out.println("Invalid user id.");
            return;
        }

        Printer.editUserMenu();
        String option = sc.nextLine().toUpperCase();
        String[] columns = new String[1];
        Object[] values = new Object[1];
        switch (option) {
            case "U":
                System.out.println("Enter new username...");
                columns[0] = "username";
                String username = sc.nextLine();
                try {
                    userValidation.validateUsername(username);
                } catch (InvalidCredentials e) {
                    System.out.println(e.getMessage());
                    return;
                }
                values[0] = username;
                break;
            case "P":
                System.out.println("Enter new password...");
                columns[0] = "password";
                String password = sc.nextLine();
                try {
                    values[0] = password;
                    userValidation.validateInput(values, columns);
                } catch (InvalidCredentials e) {
                    System.out.println(e.getMessage());
                    return;
                }
                values[0] = PasswordConverter.encodePassword(password);
                break;
            case "R":
                System.out.println("Enter new privilege (ADMIN | USER) ...");
                columns[0] = "privilege";
                values[0] = sc.nextLine();
                try {
                    userValidation.validateInput(values, columns);
                } catch (InvalidCredentials e) {
                    System.out.println(e.getMessage());
                    return;
                }
                break;
        }
        userService.update(columns, values, " id = " + userId);
        System.out.printf("Successfully edited user with id %d.%n", userId);
    }

    private void register() {
        String[] values = new String[5];
        String[] columns = {"privilege", "username", "password", "first_name", "last_name"};
        System.out.println("Enter privilege...");
        values[0] = sc.nextLine().toUpperCase();
        System.out.println("Enter username...");
        while (true) {
            try {
                String username = sc.nextLine();
                userValidation.validateUsername(username);
                values[1] = username;
                break;
            } catch (InvalidCredentials e) {
                System.out.println("This username is already registered.");
            }
        }
        System.out.println("Enter password...");
        values[2] = PasswordConverter.encodePassword(sc.nextLine());
        System.out.println("Enter first name...");
        values[3] = sc.nextLine();
        System.out.println("Enter last name...");
        values[4] = sc.nextLine();

        try {
            userValidation.validateInput(columns, values);
        } catch (InvalidCredentials e) {
            System.out.println(e.getMessage());
            register();
            return;
        }
        User registered = userService.save(columns, values);
        System.out.printf("Successfully registered user with id %d.%n", registered.getId());
    }
}
