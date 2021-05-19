package com.scalefocus.service;

import com.scalefocus.model.WorkingLog;
import com.scalefocus.repository.WorkingLogRepository;
import com.scalefocus.repository.connection.DBConnection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class WorkLogServiceTest {
    private static WorkingLogRepository workingLogRepository;
    private static WorkLogService workLogService;

    @BeforeClass
    public static void init() throws SQLException {
        DBConnection.setConnection(null, null);
        workingLogRepository = new WorkingLogRepository();
        workLogService = new WorkLogService(workingLogRepository);
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
    public void saveShouldReturnWorkingLog() {
        WorkingLog saved = workLogService.save("time, description, user_id, task_id".split(", "),
                new Object[]{"2021-05-17 03:30:00", "test working log 1", 1, 1});
        assertNotNull(saved);
        assertEquals(3, saved.getId());
    }

    @Test(expected = NullPointerException.class)
    public void saveShouldThrowExceptionWhenSavingNull() {
        workLogService.save(null, null);
    }

    @Test
    public void updateShouldReturnWorkingLog() {
        WorkingLog updated = workLogService.update("description".split(", "),
                new Object[]{"updated description"}, "id = 1");
        assertNotNull(updated);
        assertEquals("updated description", updated.getDescription());
    }

    @Test(expected = NullPointerException.class)
    public void updateShouldThrowExceptionWhenUpdatingNull() {
        workLogService.update(null, null, "");
    }

    @Test
    public void deleteShouldReturnWorkingLog() {
        WorkingLog saved = workLogService.save("time, description, user_id, task_id".split(", "),
                new Object[]{"2021-05-17 03:30:00", "test working log 2", 1, 1});
        WorkingLog deleted = workLogService.delete(saved.getId());
        assertNotNull(deleted);
        assertEquals(saved.toString(), deleted.toString());
    }

    @Test
    public void deleteShouldReturnNullWhenInvalidId() {
        WorkingLog deleted = workLogService.delete(109);
        assertNull(deleted);
    }

    @Test
    public void findByIdShouldReturnProject() {
        WorkingLog workingLog = workLogService.findById(1);
        assertNotNull(workingLog);
    }

    @Test
    public void findByIdShouldReturnNullWhenInvalidId() {
        WorkingLog workingLog = workLogService.findById(123);
        assertNull(workingLog);
    }
}