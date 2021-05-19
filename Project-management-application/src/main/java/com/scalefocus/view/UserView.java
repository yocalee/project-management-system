package com.scalefocus.view;

import com.scalefocus.service.UserService;
import com.scalefocus.util.Printer;

public class UserView extends View{
    private final ProjectView projectView;
    private final TaskView taskView;
    private final WorkLogView workLogView;
    private final TeamView teamView;

    public UserView() {
        projectView = new ProjectView();
        taskView = new TaskView();
        workLogView = new WorkLogView();
        teamView = new TeamView();
    }

    @Override
    public void menu(boolean isLoggedOut) {
        Printer.userMenu();
        String option = sc.nextLine().toUpperCase();
        switch (option){
            case "P":
                projectView.menu(false);
                break;
            case "A":
                taskView.menu(false);
                break;
            case "W":
                workLogView.menu(false);
                break;
            case "T":
                teamView.menu(false);
                break;
            case "O":
                IndexView.login(true);
                UserService.user = null;
                return;
            case "X":
                exit = true;
                return;
        }
    }
}
