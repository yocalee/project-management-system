package com.scalefocus.util;

import com.scalefocus.model.User;

public class Printer {
    public static void greetings(){
        System.out.println("Welcome to Project management system. Here you can join in team and work with your colleges on projects.");
        System.out.println("Also you can create work logs in order to track tracks.");
        System.out.println("Have fun! ");
        System.out.println();
    }

    public static void editUserMenu() {
        System.out.println("-----> Press \n-----  ::U:: for editing username\n-----  ::P:: for editing password\n" +
                "-----  ::R:: for editing privilege");
    }

    public static void adminMenuUser() {
        System.out.println("You entered in user panel.");
        System.out.println("-----> Press \n-----  ::R:: for register user\n-----  ::E:: for editing user\n-----  ::D:: for deleting user\n"+
                "-----  ::V:: to view all users\n-----  ::B:: for back");

    }

    public static String userDetails(User u) {
        return u.toString();
    }

    public static void taskMenu() {
        System.out.println("You entered in task panel.");
        System.out.println("-----> Press \n-----  ::C:: for creating new task\n-----  ::E:: for editing task\n-----  ::D:: for deleting task\n"+
                "-----  ::V:: to view all tasks\n-----  ::B:: for back");
    }

    public static void adminMenu() {
        System.out.println("-----> Press \n-----  ::U:: for entering user panel\n-----  ::T:: for entering team panel\n-----  ::P:: for entering project panel" +
                "\n-----  ::A:: for entering task panel\n-----  ::W:: for entering work log panel\n-----  ::O:: to logout\n-----  ::X:: to exit the application");
    }

    public static void userMenu() {
        System.out.println("-----> Press \n-----  ::P:: for entering project panel" +
                "\n-----  ::A:: for entering task panel\n-----  ::T:: for entering team panel\n-----  ::W:: for entering work log panel\n-----  ::O:: to logout\n-----  ::X:: to exit the application");
    }
    public static void exitApplication() {
        System.out.println("Exiting application...");
    }

    public static void adminTeamMenu() {
        System.out.println("-----> Press \n-----  ::A:: to view all teams in the system\n-----  ::S:: to view teams you are assigned to" +
                "\n-----  ::C:: to create team\n-----  ::E:: to edit team\n-----  ::D:: to delete team" +
                "\n-----  ::B:: back");
    }

    public static void userTeamMenu() {
        System.out.println("-----> Press \n-----  ::A:: to view all teams in the system\n-----  ::S:: to view teams you are assigned to" +
                "\n-----  ::B:: back");
    }

    public static void editTeamView() {
        System.out.println("-----> Press \n-----  ::N:: to change team's name\n-----  ::A:: to add user to team" +
                "\n-----  ::R:: to remove user from team");
    }

    public static void editTask() {
        System.out.println("-----> Press \n-----  ::S:: to change status\n-----  ::N:: to change name" +
                "\n-----  ::U:: to remove/add users to task");
    }

    public static void projectMenu() {
        System.out.println("You entered in project panel.");
        System.out.println("-----> Press \n-----  ::C:: for creating new project\n-----  ::E:: for editing project\n-----  ::D:: for deleting project\n"+
                "-----  ::V:: to view all projects\n-----  ::B:: for back");
    }

    public static void editProjectMenu() {
        System.out.println("-----> Press \n-----  ::N:: for changing project name\n-----  ::A:: to add team\n-----  ::R:: to remove team");
    }

    public static void workLogMenu() {
        System.out.println("You entered in work log panel.");
        System.out.println("-----> Press \n-----  ::C:: for creating new work log\n-----  ::E:: for editing work log\n"+
                "-----  ::V:: to view all work logs\n-----  ::B:: for back");
    }

    public static void workLogEditMenu() {
        System.out.println("-----> Press \n-----  ::T:: for editing time\n-----  ::D:: for editing description");
    }
}
