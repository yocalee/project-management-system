package com.scalefocus.service;

import com.scalefocus.model.Project;
import com.scalefocus.repository.ProjectRepository;
import com.scalefocus.repository.connection.DBConnection;
import org.junit.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class ProjectServiceTest {
    private static ProjectService projectService;
    private static ProjectRepository projectRepository;

    @BeforeClass
    public static void init() throws SQLException {
        DBConnection.setConnection(null, null);
        projectRepository = new ProjectRepository();
        projectService = new ProjectService(projectRepository);
        try {
            projectRepository.init();
        } catch (SQLException ignored) {
        }
        projectRepository.save("name, creator_id".split(", "),
                new Object[]{"test project", 4});
    }

    @AfterClass
    public static void dropSchema() throws SQLException {
        PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("drop schema project_management_system;");
        preparedStatement.execute();
    }

    @Test(expected = NullPointerException.class)
    public void saveShouldThrowExceptionWhenSavingNull() {
        projectService.save(null, null);
    }

    @Test
    public void saveShouldReturnProject() {
        Project saved = projectService.save("name, creator_id".split(", "),
                new Object[]{"test project", 2});
        assertNotNull(saved);
        assertEquals(2, saved.getCreator());
    }

    @Test
    public void updateShouldReturnUpdatedProject() {
        Project updated = projectService.update("name".split(", "),
                new Object[]{"updated project name"}, " id = 1");
        assertNotNull(updated);
        assertEquals("updated project name", updated.getName());
    }

    @Test(expected = NullPointerException.class)
    public void updateShouldThrowExceptionWhenUpdatingWithNull() {
        projectService.update(null, null, "");
    }

    @Test
    public void deleteShouldReturnProject() {
        Project saved = projectService.save("name, creator_id".split(", "),
                new Object[]{"test project", 5});
        Project deleted = projectService.delete(saved.getId());
        assertNotNull(deleted);
        assertEquals(5, deleted.getCreator());
    }

    @Test
    public void deleteShouldReturnNullForInvalidId() {
        Project deleted = projectService.delete(9);
        assertNull(deleted);
    }

    @Test
    public void findAllShouldReturnListOfProjects() {
        projectService.save("name, creator_id".split(", "),
                new Object[]{"test project 2", 5});
        List<Project> all = projectService.findAll();
        assertEquals(3, all.size());
    }

    @Test
    public void findProjectsRelatedToUserShouldReturnEmptyList() {
        List<Project> projectsRelatedToUser = projectService.findProjectsRelatedToUser(15);
        assertEquals(0, projectsRelatedToUser.size());
    }

    @Test
    public void saveInCommonTable() {
        projectService.saveInCommonTable("projects_teams", "project_id, team_id".split(", "),
                new Object[]{1, 2});
        List<Project> projects = projectService.findInCommonTable("projects_teams", "project_id = 1 and team_id = 2", "project_id");
        assertNotNull(projects);
        assertEquals(1, projects.size());
    }

    @Test
    public void findByIdShouldReturnProject() {
        Project project = projectService.findById(1);
        assertNotNull(project);
    }

    @Test
    public void findByIdShouldReturnNullWhenInvalidId() {
        Project project = projectService.findById(123);
        assertNull(project);
    }
}