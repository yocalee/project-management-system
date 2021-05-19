package com.scalefocus.repository;

import com.scalefocus.model.Project;
import com.scalefocus.repository.connection.DBConnection;
import org.junit.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class ProjectRepositoryTest {
    private static ProjectRepository projectRepository;

    @BeforeClass
    public static void init() throws SQLException {
        DBConnection.setConnection(null, null);
        projectRepository = new ProjectRepository();
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
    public void saveShouldThrowExceptionWhenSavingNull() throws SQLException {
        projectRepository.save(null, null);
    }

    @Test
    public void saveShouldReturnProject() throws SQLException {
        Project saved = projectRepository.save("name, creator_id".split(", "),
                new Object[]{"test project", 2});
        assertNotNull(saved);
        assertEquals(2, saved.getCreator());
    }

    @Test
    public void updateShouldReturnUpdatedProject() throws SQLException {
        Project updated = projectRepository.update("name".split(", "),
                new Object[]{"updated project name"}, " id = 1");
        assertNotNull(updated);
        assertEquals("updated project name", updated.getName());
    }

    @Test(expected = NullPointerException.class)
    public void updateShouldThrowExceptionWhenUpdatingWithNull() throws SQLException {
        projectRepository.update(null, null, "");
    }

    @Test
    public void deleteShouldReturnProject() throws SQLException {
        Project saved = projectRepository.save("name, creator_id".split(", "),
                new Object[]{"test project", 5});
        Project deleted = projectRepository.delete(saved.getId());
        assertNotNull(deleted);
        assertEquals(5, deleted.getCreator());
    }

    @Test(expected = SQLException.class)
    public void deleteShouldThrowExceptionWhenDeletingWithInvalidId() throws SQLException {
        projectRepository.delete(9);
    }

    @Test
    public void findAllShouldReturnListOfProjects() throws SQLException {
        projectRepository.save("name, creator_id".split(", "),
                new Object[]{"test project 2", 5});
        List<Project> all = projectRepository.findAll();
        assertEquals(3, all.size());
    }

    @Test
    public void findProjectsRelatedToUserShouldReturnEmptyList() throws SQLException {
        List<Project> projectsRelatedToUser = projectRepository.findProjectsRelatedToUser(15);
        assertEquals(0, projectsRelatedToUser.size());
    }

    @Test
    public void saveInCommonTable() throws SQLException {
        projectRepository.saveInCommonTable("projects_teams", "project_id, team_id".split(", "),
                new Object[]{1, 2});
        List<Project> projects = projectRepository.findInCommonTable("projects_teams", "project_id = 1 and team_id = 2", "project_id");
        assertNotNull(projects);
        assertEquals(1, projects.size());
    }


}



