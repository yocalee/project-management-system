package com.scalefocus.util;

import com.scalefocus.exception.InvalidCredentials;
import com.scalefocus.model.*;
import com.scalefocus.repository.UserRepository;
import com.scalefocus.service.*;

import java.sql.SQLException;
import java.util.List;

public class Validation {

    private static void validateInput(String field, String input) {
        if (input.trim().equals("")) {
            throw new InvalidCredentials(field + " cannot be null or empty.");
        }
    }

    public static class UserValidation {
        private final UserRepository userRepository;

        public UserValidation(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        public boolean validateLogin(String username, String password) throws InvalidCredentials {
            if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
                throw new InvalidCredentials("Username and password must not be empty or null.");
            }
            try {
                User user = userRepository.findUserByUsername(username);

                if (user == null) {
                    throw new InvalidCredentials("Incorrect username or password.");
                }
                if (!PasswordConverter.matches(password, user.getPassword())) {
                    throw new InvalidCredentials("Password incorrect.");
                }

                return true;
            } catch (SQLException ignored) {
                return false;
            }
        }

        public boolean validateInput(Object[] input, String[] columns) throws InvalidCredentials {
            for (int i = 0; i < input.length; i++) {
                Validation.validateInput((String) input[i], columns[i]);
            }
            return true;
        }

        public void validateUsername(String username) throws InvalidCredentials {
            User user = null;
            try {
                Validation.validateInput(username, "Username");

                user = userRepository.findUserByUsername(username);
            } catch (SQLException | NullPointerException ignored) {
            }
            if (user != null) {
                throw new InvalidCredentials("This username is already registered.");
            }
        }

        public boolean validateId(int userId) {
            try {
                userRepository.findById(userId);
                return true;
            } catch (SQLException e) {
                return false;
            }
        }

    }

    public static class TeamValidation {
        private final TeamService teamService;

        public TeamValidation(TeamService teamService) {
            this.teamService = teamService;
        }

        public boolean validId(int teamId) {
            Team team = teamService.findById(teamId);

            return team != null;
        }

        public boolean validUserId(int userId, int teamId) {
                List<Team> list = teamService.findInCommonTable("users_teams",
                        String.format(" user_id = %d and team_id = %d", userId, teamId), "team_id");
                return !list.isEmpty();
        }

    }

    public static class ProjectValidation {
        private final ProjectService projectService;

        public ProjectValidation (ProjectService projectService) {
            this.projectService = projectService;
        }

        public boolean validateCreator(int projectId, int id) {
            Project project = projectService.findById(projectId);
            return project.getCreator() == id;
        }

        public boolean validateTeam(int projectId, int teamId) {
            List<Project> inCommonTable = projectService.findInCommonTable("projects_teams",
                    String.format(" project_id = %d and team_id = %d ", projectId, teamId), "project_id");
            return inCommonTable.isEmpty();
        }
    }

    public static class TaskValidation {
        private final TaskService taskService;

        public TaskValidation(TaskService taskService){
            this.taskService = taskService;
        }

        public boolean validateUserInTask(int userId, int taskId) {
            List<Task> validated = taskService.findInCommonTable("users_tasks",
                    String.format(" user_id = %d and task_id = %d", userId, taskId), "task_id");
            return validated.isEmpty();
        }
    }
}


