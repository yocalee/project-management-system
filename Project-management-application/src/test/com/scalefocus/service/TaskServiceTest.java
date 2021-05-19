package com.scalefocus.service;

import com.scalefocus.model.Task;
import com.scalefocus.repository.TaskRepository;
import com.scalefocus.repository.connection.DBConnection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class TaskServiceTest {
    private static TaskRepository taskRepository;
    private static TaskService taskService;

    @BeforeClass
    public static void init() throws SQLException {
        DBConnection.setConnection(null, null);
        taskRepository = new TaskRepository();
        taskService = new TaskService(taskRepository);
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
    public void saveShouldReturnTask() {
        Task save = taskService.save(new String[]{"name", "status", "creator_id", "project_id"},
                new Object[]{"test task 2", "PENDING", 1, 1});
        assertNotNull(save);
        assertEquals(3, save.getId());
    }

    @Test(expected = NullPointerException.class)
    public void saveShouldThrowExceptionWhenSavingNull() {
        taskService.save(null, null);
    }

    @Test
    public void updateShouldReturnTask() {
        Task updated = taskService.update("status".split(", "), new Object[]{"COMPLETED"}, " id = 1");
        Task task = taskService.findById(updated.getId());
        assertNotNull(updated);
        assertEquals("COMPLETED", task.getStatus());
    }

    @Test(expected = NullPointerException.class)
    public void updateShouldThrowExceptionWhenUpdatingNullValues() {
        taskService.update(null, null, "");
    }

    @Test
    public void deleteShouldReturnTask() {
        Task save = taskService.save(new String[]{"name", "status", "creator_id", "project_id"},
                new Object[]{"test task 3", "PENDING", 1, 1});
        Task delete = taskService.delete(save.getId());
        assertNotNull(delete);
        assertEquals("PENDING", delete.getStatus());
        assertEquals("test task 3", delete.getName());
    }

    @Test
    public void deleteShouldReturnNullWhenInvalidId() {
        Task deleted = taskService.delete(9);
        assertNull(deleted);
    }

    @Test
    public void findAllShouldReturnListOfTasks() {
        List<Task> all = taskService.findAll();
        assertNotNull(all);
        assertEquals(1, all.size());
    }

    @Test
    public void findTaskByUserIdShouldReturnEmptyList() {
        List<Task> list = taskService.findTasksByUserId(1);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void saveInCommonTable() {
        taskService.saveInCommonTable("users_tasks", "user_id, task_id".split(", "),
                new Object[]{3, 1});
        List<Task> tasks = taskService.findInCommonTable("users_tasks", "user_id = 3 and task_id = 1", "task_id");
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
    }

    @Test
    public void deleteInCommonTable() {
        taskService.saveInCommonTable("users_tasks", "user_id, task_id".split(", "),
                new Object[]{3, 1});
        taskService.deleteInCommonTable("users_tasks", "task_id = 1 and user_id = 3");
    }

    @Test
    public void findByIdShouldReturnProject() {
        Task task = taskService.findById(1);
        assertNotNull(task);
    }

    @Test
    public void findByIdShouldReturnNullWhenInvalidId() {
        Task task = taskService.findById(123);
        assertNull(task);
    }
}