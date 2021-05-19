package com.scalefocus.util;

import com.scalefocus.exception.InvalidCredentials;
import com.scalefocus.repository.ProjectRepository;
import com.scalefocus.repository.TaskRepository;
import com.scalefocus.repository.TeamRepository;
import com.scalefocus.repository.UserRepository;
import com.scalefocus.repository.connection.DBConnection;
import com.scalefocus.service.ProjectService;
import com.scalefocus.service.TaskService;
import com.scalefocus.service.TeamService;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class ValidationTest {
    private static Validation.UserValidation userValidation;
    private static Validation.ProjectValidation projectValidation;
    private static Validation.TaskValidation taskValidation;
    private static Validation.TeamValidation teamValidation;

    @BeforeClass
    public static void init() throws SQLException {
        DBConnection.setConnection(null, null);
        UserRepository userRepository = new UserRepository();
        userValidation = new Validation.UserValidation(userRepository);
        projectValidation = new Validation.ProjectValidation(new ProjectService(new ProjectRepository()));
        taskValidation = new Validation.TaskValidation(new TaskService(new TaskRepository()));
        teamValidation = new Validation.TeamValidation(new TeamService(new TeamRepository()));
        try {
            userRepository.init();
        } catch (Exception ignored) {
        }
    }


    @Test
    public void validateInputShouldReturnTrue() {
        assertTrue(userValidation.validateInput(new Object[]{"dsfg", "edf", "dfd"}, new String[]{"column1", "column2", "column3"}));
    }

    @Test(expected = InvalidCredentials.class)
    public void validateInputShouldThrowException() {
        userValidation.validateInput(new Object[]{"", ""}, new String[]{"", "", ""});
    }

    @Test
    public void validateLoginShouldReturnTrue() {
        assertTrue(userValidation.validateLogin("admin", "adminpass"));
    }

    @Test(expected = InvalidCredentials.class)
    public void validateLoginShouldThrowException() {
        userValidation.validateLogin(null, null);
    }

    @Test(expected = InvalidCredentials.class)
    public void validateUniqueUsernameShouldThrowException() {
        userValidation.validateUsername("admin");
    }

    @Test
    public void validateUserIdShouldReturnTrue() {
        assertTrue(userValidation.validateId(1));
    }

    @Test
    public void validateUserIdShouldReturnFalse() {
        assertFalse(userValidation.validateId(109));
    }

    @Test
    public void validateTeamIdShouldReturnFalse() {
        assertFalse(teamValidation.validId(10));
    }

    @Test
    public void validateUserInTeamShouldReturnTrue() {
        assertFalse(teamValidation.validUserId(1, 1));
    }

    @Test(expected = NullPointerException.class)
    public void validateCreatorOfProjectShouldThrowExceptionWhenNoProjectsInDB() {
        projectValidation.validateCreator(1, 1);
    }

    @Test
    public void validateProjectTeamShouldReturnTrue() {
        assertTrue(projectValidation.validateTeam(1, 1));
    }

    @Test
    public void validateUserInTaskShouldReturnTrue() {
        assertTrue(taskValidation.validateUserInTask(1,1));
    }


}