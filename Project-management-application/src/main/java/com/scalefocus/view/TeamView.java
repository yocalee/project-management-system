package com.scalefocus.view;

import com.scalefocus.model.Team;
import com.scalefocus.repository.*;
import com.scalefocus.service.*;
import com.scalefocus.util.*;
import java.util.List;

public class TeamView extends View {
    private final TeamService teamService;
    private final Validation.TeamValidation teamValidation;
    private final Validation.UserValidation userValidation;

    public TeamView() {
        this.teamService = new TeamService(new TeamRepository());
        teamValidation = new Validation.TeamValidation(teamService);
        userValidation = new Validation.UserValidation(new UserRepository());
    }

    @Override
    public void menu(boolean isAdmin) {
        if (!isAdmin) {
            Printer.userTeamMenu();
        } else {
            Printer.adminTeamMenu();
        }
        String option = sc.nextLine().toUpperCase();

        switch (option) {
            case "A":
                teamService.findAll().forEach(t -> System.out.println(t.toString()));
                break;
            case "S":
                List<Team> teams = teamService.findInCommonTable("users_teams",
                        " user_id = " + UserService.user.getId(), " team_id");
                System.out.println("You are assigned to " + teams.size() + " teams.");
                teams.forEach(t -> System.out.println(t.toString()));
                break;
            case "C":
                if (!isAdmin) {
                    return;
                }
                System.out.println("Enter team name...");
                String teamName = sc.nextLine();
                Team team = teamService.save("name".split(", "), new Object[]{teamName});
                System.out.printf("Successfully created team %s with id %d.%n", teamName, team.getId());
                break;
            case "D": {
                if (!isAdmin) {
                    return;
                }
                int teamId = validateTeamId();
                teamService.delete(teamId);
                teamService.deleteInCommonTable("users_teams", " team_id = " + teamId);
                teamService.deleteInCommonTable("projects_teams", " team_id = " + teamId);
                System.out.println("Successfully deleted team with id " + teamId);
                break;
            }
            case "E": {
                if (!isAdmin) {
                    return;
                }
                int teamId = validateTeamId();
                Printer.editTeamView();
                String opt = sc.nextLine().toUpperCase();
                if (opt.equals("N")) {
                    System.out.println("Enter new name...");
                    String newName = sc.nextLine();
                    teamService.update("name".split(", "), new Object[]{newName}, " id = " + teamId);
                    System.out.println("Successfully edited team with id " + teamId);
                    return;
                }

                System.out.println("Enter user id...");
                int userId = Integer.parseInt(sc.nextLine());
                while (!userValidation.validateId(userId)) {
                    System.out.println("Enter valid user id...");
                    userId = Integer.parseInt(sc.nextLine());
                }
                switch (opt) {
                    case "A":
                        teamService.saveInCommonTable("users_teams",
                                "user_id, team_id".split(", "), new Object[]{userId, teamId});
                        System.out.printf("Successfully added user with id %d to the team.%n", userId);
                        break;
                    case "R":
                        while (!teamValidation.validUserId(userId, teamId)) {
                            System.out.println("Enter valid user id...");
                            userId = Integer.parseInt(sc.nextLine());
                        }
                        teamService.deleteInCommonTable("users_teams", " user_id = " + userId);
                        System.out.printf("Successfully removed user with id %d from the team.%n", userId);
                        break;
                }

                break;
            }
            case "B":
                return;
        }
    }

    private int validateTeamId() {
        System.out.println("Enter team id...");
        int teamId = Integer.parseInt(sc.nextLine());
        while (!teamValidation.validId(teamId)) {
            System.out.println("Enter valid team id...");
            teamId = Integer.parseInt(sc.nextLine());
        }
        return teamId;
    }


}
