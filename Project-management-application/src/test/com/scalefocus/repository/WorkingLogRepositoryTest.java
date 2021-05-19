package com.scalefocus.repository;

import com.scalefocus.model.WorkingLog;
import com.scalefocus.repository.connection.DBConnection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.*;


public class WorkingLogRepositoryTest {
    private static WorkingLogRepository workingLogRepository;

    @BeforeClass
    public static void init() throws SQLException {
        DBConnection.setConnection(null, null);
        workingLogRepository = new WorkingLogRepository();
        try {
            workingLogRepository.init();
        } catch (SQLException ignored) {
        }
        workingLogRepository.save("time, description, user_id, task_id".split(", "),
                new Object[]{"2021-05-17 03:30:00", "test working log", 1, 1});
    }

    @AfterClass
    public static void dropSchema() throws SQLException {
        PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("drop schema project_management_system;");
        preparedStatement.executeUpdate();
    }

    @Test
    public void saveShouldReturnWorkingLog() throws SQLException {
        WorkingLog saved = workingLogRepository.save("time, description, user_id, task_id".split(", "),
                new Object[]{"2021-05-17 03:30:00", "test working log 1", 1, 1});
        assertNotNull(saved);
        assertEquals(3, saved.getId());
    }

    @Test(expected = NullPointerException.class)
    public void saveShouldThrowExceptionWhenSavingNull() throws SQLException {
        workingLogRepository.save(null, null);
    }

    @Test
    public void updateShouldReturnWorkingLog() throws SQLException {
        WorkingLog updated = workingLogRepository.update("description".split(", "),
                new Object[]{"updated description"}, "id = 1");
        assertNotNull(updated);
        assertEquals("updated description", updated.getDescription());
    }

    @Test(expected = NullPointerException.class)
    public void updateShouldThrowExceptionWhenUpdatingNull() throws SQLException {
        workingLogRepository.update(null, null, "");
    }

    @Test
    public void deleteShouldReturnWorkingLog() throws SQLException {
        WorkingLog saved = workingLogRepository.save("time, description, user_id, task_id".split(", "),
                new Object[]{"2021-05-17 03:30:00", "test working log 2", 1, 1});
        WorkingLog deleted = workingLogRepository.delete(saved.getId());
        assertNotNull(deleted);
        assertEquals(saved.toString(), deleted.toString());
    }

    @Test(expected = SQLException.class)
    public void deleteShouldThrowExceptionWhenInvalidId() throws SQLException {
        workingLogRepository.delete(109);
    }
}