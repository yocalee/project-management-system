package com.scalefocus.repository;

import com.scalefocus.model.Team;
import com.scalefocus.repository.connection.DBConnection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class TeamRepositoryTest {
    private static TeamRepository teamRepository;

    @BeforeClass
    public static void init() throws SQLException {
        DBConnection.setConnection(null, null);
        teamRepository = new TeamRepository();
        try {
            teamRepository.init();
        } catch (SQLException ignored) {
        }
        teamRepository.save("name".split(", "), new Object[]{"test team 1"});
    }

    @AfterClass
    public static void dropSchema() throws SQLException {
        PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("drop schema project_management_system;");
        preparedStatement.executeUpdate();
    }

    @Test
    public void saveShouldReturnTeam() throws SQLException {
        Team saved = teamRepository.save("name".split(", "), new Object[]{"test team 2"});
        assertNotNull(saved);
        assertEquals(3, saved.getId());
    }

    @Test(expected = NullPointerException.class)
    public void saveShouldThrowExceptionWhenSavingNull() throws SQLException {
        teamRepository.save(null, null);
    }

    @Test
    public void updateShouldReturnTeam() throws SQLException {
        Team updated = teamRepository.update("name".split(", "),
                new Object[]{"updated team name"}, " id = 1");
        assertNotNull(updated);
        assertEquals("updated team name", updated.getName());
    }

    @Test(expected = NullPointerException.class)
    public void updateShouldThrowExceptionWhenUpdatingWithNull() throws SQLException {
        teamRepository.update(null, null, "");
    }

    @Test
    public void deleteShouldReturnTeam() throws SQLException {
        Team saved = teamRepository.save("name".split(", "), new Object[]{"test to delete 2"});
        Team deleted = teamRepository.delete(saved.getId());
        assertNotNull(deleted);
        assertEquals(saved.toString(), deleted.toString());
    }

    @Test(expected = SQLException.class)
    public void deleteShouldThrowExceptionWhenInvalidId() throws SQLException {
        teamRepository.delete(109);
    }

    @Test
    public void findAllShouldReturnListOfTeams() throws SQLException {
        List<Team> all = teamRepository.findAll();
        assertNotNull(all);
        assertEquals(1, all.size());
    }

    @Test
    public void insertInCommonTableShouldReturnOneTeam() throws SQLException {
        teamRepository.saveInCommonTable("users_teams", "user_id, team_id".split(", "),
                new Object[]{1, 1});
        List<Team> teams = teamRepository.findInCommonTable("users_teams", " team_id = 1", "team_id");
        assertNotNull(teams);
        assertEquals(1, teams.size());
    }

    @Test
    public void deleteInCommonTableShouldNotReturnTeam() throws SQLException {
        teamRepository.saveInCommonTable("users_teams", "user_id, team_id".split(", "),
                new Object[]{1, 2});
        teamRepository.deleteInCommonTable("users_teams", " team_id = 2");
        List<Team> teams = teamRepository.findInCommonTable("users_teams", " team_id = 2", "team_id");
        assertNotNull(teams);
        assertEquals(0, teams.size());
    }
}