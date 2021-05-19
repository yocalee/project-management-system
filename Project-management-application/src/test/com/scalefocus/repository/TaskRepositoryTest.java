package com.scalefocus.repository;

import com.scalefocus.model.Task;
import com.scalefocus.repository.connection.DBConnection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class TaskRepositoryTest {
    private static TaskRepository taskRepository;

    @BeforeClass
    public static void init() throws SQLException {
        DBConnection.setConnection(null, null);
        taskRepository = new TaskRepository();
        try {
            taskRepository.init();
        } catch (SQLException ignored) {
        }
        taskRepository.save(new String[]{"name", "status", "creator_id", "project_id"},
                new Object[]{"test task", "PENDING", 1, 1});
    }

    @AfterClass
    public static void dropSchema() throws SQLException {
        PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("drop schema project_management_system;");
        preparedStatement.executeUpdate();
    }

    @Test
    public void saveShouldReturnTask() throws SQLException {
        Task save = taskRepository.save(new String[]{"name", "status", "creator_id", "project_id"},
                new Object[]{"test task 2", "PENDING", 1, 1});
        assertNotNull(save);
        assertEquals(3, save.getId());
    }

    @Test(expected = NullPointerException.class)
    public void saveShouldThrowExceptionWhenSavingNull() throws SQLException {
        taskRepository.save(null, null);
    }

    @Test
    public void updateShouldReturnTask() throws SQLException {
        Task updated = taskRepository.update("status".split(", "), new Object[]{"COMPLETED"}, " id = 1");
        Task task = taskRepository.findById(updated.getId());
        assertNotNull(updated);
        assertEquals("COMPLETED", task.getStatus());
    }

    @Test(expected = NullPointerException.class)
    public void updateShouldThrowExceptionWhenUpdatingNullValues() throws SQLException {
        taskRepository.update(null, null, "");
    }

    @Test
    public void deleteShouldReturnTask() throws SQLException {
        Task save = taskRepository.save(new String[]{"name", "status", "creator_id", "project_id"},
                new Object[]{"test task 3", "PENDING", 1, 1});
        Task delete = taskRepository.delete(save.getId());
        assertNotNull(delete);
        assertEquals("PENDING", delete.getStatus());
        assertEquals("test task 3", delete.getName());
    }

    @Test(expected = SQLException.class)
    public void deleteShouldThrowExceptionWhenInvalidId() throws SQLException {
        taskRepository.delete(9);
    }

    @Test
    public void findAllShouldReturnListOfTasks() throws SQLException {
        List<Task> all = taskRepository.findAll();
        assertNotNull(all);
        assertEquals(1, all.size());
    }

    @Test
    public void findTaskByUserIdShouldReturnEmptyList() throws SQLException {
        List<Task> list = taskRepository.findTasksByUserId(1);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void saveInCommonTable() throws SQLException {
        taskRepository.saveInCommonTable("users_tasks", "user_id, task_id".split(", "),
                new Object[]{3, 1});
        List<Task> tasks = taskRepository.findInCommonTable("users_tasks", "user_id = 3 and task_id = 1", "task_id");
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
    }

    @Test
    public void deleteInCommonTable() throws SQLException {
        taskRepository.saveInCommonTable("users_tasks", "user_id, task_id".split(", "),
                new Object[]{3, 1});
        taskRepository.deleteInCommonTable("users_tasks", "task_id = 1 and user_id = 3");
    }

}