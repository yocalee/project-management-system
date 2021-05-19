package com.scalefocus.service;

import com.scalefocus.model.User;
import com.scalefocus.repository.UserRepository;
import com.scalefocus.repository.connection.DBConnection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class UserServiceTest {
    private static UserRepository userRepository;
    private static UserService userService;

    @BeforeClass
    public static void init() throws SQLException {
        DBConnection.setConnection(null, null);
        userRepository = new UserRepository();
        userService = new UserService(userRepository);
        try {
            userRepository.init();
        } catch (SQLException ignored) {
        }
        userRepository.save(new String[]{"privilege", "username", "password", "first_name", "last_name"},
                new Object[]{"USER", "test123", "1234", "Test", "Testov"});
    }

    @AfterClass
    public static void dropSchema() throws SQLException {
        PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("drop schema project_management_system;");
        preparedStatement.executeUpdate();
    }

    @Test
    public void saveShouldReturnUser() {
        User saved = userService.save(new String[]{"privilege", "username", "password", "first_name", "last_name"},
                new Object[]{"USER", "test", "1234", "Test", "Testov"});
        assertNotNull(saved);
        assertEquals(4, saved.getId());
    }

    @Test(expected = NullPointerException.class)
    public void saveShouldThrowExceptionWhenSavingNull() {
        userService.save(null, null);
    }

    @Test
    public void updateShouldReturnUser() {
        User updated = userService.update("privilege".split(", "), new Object[]{"ADMIN"}, " id = 2");
        assertNotNull(updated);
        assertEquals("ADMIN", updated.getPrivilege());
    }

    @Test(expected = NullPointerException.class)
    public void updateShouldThrowExceptionWhenInvalidId() {
        userService.update(null, null, " id = 109");
    }

    @Test
    public void deleteShouldReturnUser() {
        User saved = userService.save(new String[]{"privilege", "username", "password", "first_name", "last_name"},
                new Object[]{"USER", "test", "1234", "Test", "Testov"});
        User deleted = userService.delete(saved.getId());

        assertNotNull(deleted);
        assertEquals(saved.toString(), deleted.toString());
    }

    @Test
    public void deleteShouldReturnNullWhenInvalidId() {
        User deleted = userService.delete(109);
        assertNull(deleted);
    }

    @Test
    public void findAllShouldReturnListOfUsers() {
        List<User> all = userService.findAll();
        assertNotNull(all);
        assertEquals(2, all.size());
    }

    @Test
    public void findUserByUserNameReturnUser() {
        User admin = userService.getUserByUsernameAndPassword("admin", "adminpass");
        assertNotNull(admin);
        assertEquals(1, admin.getId());
    }

    @Test
    public void findUserByUsernameShouldReturnNullWhenInvalidUsername() {
        User user = userService.getUserByUsernameAndPassword("lorem ipsum", "wdesghj");
        assertNull(user);
    }

    @Test
    public void saveInCommonTable() {
        userService.saveInCommonTable("users_teams", "user_id, team_id".split(", "),
                new Object[]{2, 1});
        List<User> users = userService.findInCommonTable("users_teams", "user_id = 2", "user_id");
        assertNotNull(users);
        assertEquals(1, users.size());
    }

    @Test
    public void deleteInCommonTable() {
        userService.saveInCommonTable("users_teams", "user_id, team_id".split(", "),
                new Object[]{3, 2});
        userService.deleteInCommonTable("users_teams", " user_id = 3 and team_id = 2");
    }

    @Test
    public void findByIdShouldReturnProject() {
        User user = userService.findById(1);
        assertNotNull(user);
    }

    @Test
    public void findByIdShouldReturnNullWhenInvalidId() {
        User user = userService.findById(123);
        assertNull(user);
    }
}