package com.scalefocus.service;

import com.scalefocus.model.Task;
import com.scalefocus.repository.TaskRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskService extends AbstractService<Task> {
    private TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        super(taskRepository);
        this.taskRepository = taskRepository;
    }

    public List<Task> findTasksByUserId(int id) {
        try {
            return taskRepository.findTasksByUserId(id);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }
}
