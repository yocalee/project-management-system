package com.scalefocus.service;

import com.scalefocus.model.Project;
import com.scalefocus.repository.ProjectRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectService extends AbstractService<Project> {
    private ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        super(projectRepository);
        this.projectRepository = projectRepository;
    }

    public List<Project> findProjectsRelatedToUser(int id) {
        try {
            return this.projectRepository.findProjectsRelatedToUser(id);
        }catch (SQLException e){
            return new ArrayList<>();
        }
    }
}
