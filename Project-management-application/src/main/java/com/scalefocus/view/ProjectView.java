package com.scalefocus.view;

import com.scalefocus.model.Project;
import com.scalefocus.model.Team;
import com.scalefocus.repository.*;
import com.scalefocus.service.*;
import com.scalefocus.util.Printer;
import com.scalefocus.util.Validation;

import java.util.List;

public class ProjectView extends View {
    private final ProjectService projectService;
    private final TaskService taskService;
    private final TeamService teamService;
    private final Validation.ProjectValidation projectValidation;

    public ProjectView() {
        this.projectService = new ProjectService(new ProjectRepository());
        this.taskService = new TaskService(new TaskRepository());
        this.teamService = new TeamService(new TeamRepository());
        projectValidation = new Validation.ProjectValidation(projectService);
    }

    @Override
    public void menu(boolean isAdmin) {
        Printer.projectMenu();
        String option = sc.nextLine().toUpperCase();
        switch (option) {
            case "C": {
                System.out.println("Enter project name...");
                String name = sc.nextLine();
                Project created = projectService.save("name, creator_id".split(", "),
                        new Object[]{name, UserService.user.getId()});
                System.out.println("Successfully created project with id " + created.getId());
                break;
            }
            case "E": {
                int projectId = enterProjectId(isAdmin);
                if (projectId == -1) {
                    return;
                }
                Printer.editProjectMenu();
                String opt = sc.nextLine().toUpperCase();

                if (opt.equals("N")) {
                    System.out.println("Enter new name...");
                    String newName = sc.nextLine();
                    projectService.update("name".split(", "), new Object[]{newName}, " id = " + projectId);
                } else {
                    System.out.println("Enter team id to add...");
                    int teamId = Integer.parseInt(sc.nextLine());
                    if (opt.equals("A")) {
                        if (projectValidation.validateTeam(projectId, teamId)) {
                            projectService.saveInCommonTable("projects_teams",
                                    "project_id, team_id".split(", "), new Object[]{projectId, teamId});
                            System.out.println("Successfully added team with id " + teamId + " to the project.");
                            return;
                        }
                        System.out.println("Invalid team id.");
                        return;
                    } else if (opt.equals("R")) {
                        if (!projectValidation.validateTeam(projectId, teamId)) {
                            projectService.deleteInCommonTable("projects_teams",
                                    String.format(" project_id = %d and team_id = %d", projectId, teamId));
                            System.out.println("Successfully removed team with id " + teamId + " from the project.");
                            return;
                        }
                        System.out.println("Invalid team id.");
                        return;
                    }
                }
            }
            break;
            case "D": {
                int projectId = enterProjectId(isAdmin);
                if (projectId == -1) {
                    return;
                }
                projectService.update("creator_id".split(", "), new Object[]{1}, " id = " + projectId);
                taskService.update("status".split(", "), new Object[]{"COMPLETED"}, " project_id = " + projectId);
                projectService.deleteInCommonTable("projects_teams", " project_id = " + projectId);
                System.out.println("Successfully deleted project with id " + projectId);
            }
            break;
            case "V":
                if (!isAdmin) {
                    List<Project> owner = projectService.findInCommonTable("projects",
                            " creator_id = " + UserService.user.getId(), "id");
                    System.out.println("You are creator of " + owner.size() + " projects.");
                    for (Project project : owner) {
                        System.out.println(project.toString());
                        List<Team> teams = teamService.findInCommonTable("projects_teams", " project_id", "team_id");
                        System.out.println("Teams assigned to this project: ");
                        teams.forEach(t -> System.out.println(t.toString()));
                    }
                    System.out.println();
                    System.out.println("You are related to projects: ");
                    List<Project> projects = projectService.findProjectsRelatedToUser(UserService.user.getId());
                    if (projects != null) {
                        projects.forEach(p -> System.out.println(p.toString()));
                        System.out.println();
                    }
                    return;
                }
                List<Project> all = projectService.findAll();
                all.forEach(p -> System.out.println(p.toString()));
                break;
            case "B":
                return;
        }
    }

    private int enterProjectId(boolean isAdmin) {
        System.out.println("Enter project id...");
        int projectId = Integer.parseInt(sc.nextLine());
        Project project = projectService.findById(projectId);

        if (project == null) {
            System.out.println("Invalid project id.");
            return -1;
        }
        if (!isAdmin) {
            if (project.getCreator() != UserService.user.getId()) {
                System.out.println("Only the creator of the project may edit it.");
                return -1;
            }
        }
        return projectId;
    }

}
