package com.scalefocus.repository;

import com.scalefocus.model.User;
import com.scalefocus.repository.connection.DBConnection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class UserRepositoryTest {
    private static UserRepository userRepository;

    @BeforeClass
    public static void init() throws SQLException {
        DBConnection.setConnection(null, null);
        userRepository = new UserRepository();
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
    public void saveShouldReturnUser() throws SQLException {
        User saved = userRepository.save(new String[]{"privilege", "username", "password", "first_name", "last_name"},
                new Object[]{"USER", "test", "1234", "Test", "Testov"});
        assertNotNull(saved);
        assertEquals(4, saved.getId());
    }

    @Test(expected = NullPointerException.class)
    public void saveShouldThrowExceptionWhenSavingNull() throws SQLException {
        userRepository.save(null, null);
    }

    @Test
    public void updateShouldReturnUser() throws SQLException {
        User updated = userRepository.update("privilege".split(", "), new Object[]{"ADMIN"}, " id = 2");
        assertNotNull(updated);
        assertEquals("ADMIN", updated.getPrivilege());
    }

    @Test(expected = NullPointerException.class)
    public void updateShouldThrowExceptionWhenInvalidId() throws SQLException {
        userRepository.update(null, null, " id = 109");
    }

    @Test
    public void deleteShouldReturnUser() throws SQLException {
        User saved = userRepository.save(new String[]{"privilege", "username", "password", "first_name", "last_name"},
                new Object[]{"USER", "test", "1234", "Test", "Testov"});
        User deleted = userRepository.delete(saved.getId());

        assertNotNull(deleted);
        assertEquals(saved.toString(), deleted.toString());
    }

    @Test(expected = SQLException.class)
    public void deleteShouldThrowExceptionWhenInvalidId() throws SQLException {
        userRepository.delete(109);
    }

    @Test
    public void findAllShouldReturnListOfUsers() throws SQLException {
        List<User> all = userRepository.findAll();
        assertNotNull(all);
        assertEquals(2, all.size());
    }

    @Test
    public void findUserByUserNameReturnUser() throws SQLException {
        User admin = userRepository.findUserByUsername("admin");
        assertNotNull(admin);
        assertEquals(1, admin.getId());
    }

    @Test(expected = SQLException.class)
    public void findUserByUsernameShouldThrowExceptionWhenInvalidUsername() throws SQLException {
        userRepository.findUserByUsername("lorem ipsum");
    }

    @Test
    public void saveInCommonTable() throws SQLException {
        userRepository.saveInCommonTable("users_teams", "user_id, team_id".split(", "),
                new Object[]{2, 1});
        List<User> users = userRepository.findInCommonTable("users_teams", "user_id = 2", "user_id");
        assertNotNull(users);
        assertEquals(1, users.size());
    }

    @Test
    public void deleteInCommonTable() throws SQLException {
        userRepository.saveInCommonTable("users_teams", "user_id, team_id".split(", "),
                new Object[]{3, 2});
        userRepository.deleteInCommonTable("users_teams", " user_id = 3 and team_id = 2");
    }
}