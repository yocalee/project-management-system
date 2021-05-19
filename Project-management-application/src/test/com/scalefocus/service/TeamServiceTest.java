package com.scalefocus.service;

import com.scalefocus.model.Team;
import com.scalefocus.repository.TeamRepository;
import com.scalefocus.repository.connection.DBConnection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class TeamServiceTest {
    private static TeamRepository teamRepository;
    private static TeamService teamService;

    @BeforeClass
    public static void init() throws SQLException {
        DBConnection.setConnection(null, null);
        teamRepository = new TeamRepository();
        teamService = new TeamService(teamRepository);
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
    public void saveShouldReturnTeam() {
        Team saved = teamService.save("name".split(", "), new Object[]{"test team 2"});
        assertNotNull(saved);
        assertEquals(3, saved.getId());
    }

    @Test(expected = NullPointerException.class)
    public void saveShouldThrowExceptionWhenSavingNull() {
        teamService.save(null, null);
    }

    @Test
    public void updateShouldReturnTeam() {
        Team updated = teamService.update("name".split(", "),
                new Object[]{"updated team name"}, " id = 1");
        assertNotNull(updated);
        assertEquals("updated team name", updated.getName());
    }

    @Test(expected = NullPointerException.class)
    public void updateShouldThrowExceptionWhenUpdatingWithNull() {
        teamService.update(null, null, "");
    }

    @Test
    public void deleteShouldReturnTeam() {
        Team saved = teamService.save("name".split(", "), new Object[]{"test to delete 2"});
        Team deleted = teamService.delete(saved.getId());
        assertNotNull(deleted);
        assertEquals(saved.toString(), deleted.toString());
    }

    @Test
    public void deleteShouldReturnNullWhenInvalidId() {
        Team deleted = teamService.delete(109);
        assertNull(deleted);
    }

    @Test
    public void findAllShouldReturnListOfTeams() {
        List<Team> all = teamService.findAll();
        assertNotNull(all);
        assertEquals(1, all.size());
    }

    @Test
    public void insertInCommonTableShouldReturnOneTeam() {
        teamService.saveInCommonTable("users_teams", "user_id, team_id".split(", "),
                new Object[]{1, 1});
        List<Team> teams = teamService.findInCommonTable("users_teams", " team_id = 1", "team_id");
        assertNotNull(teams);
        assertEquals(1, teams.size());
    }

    @Test
    public void deleteInCommonTableShouldNotReturnTeam() {
        teamService.saveInCommonTable("users_teams", "user_id, team_id".split(", "),
                new Object[]{1, 2});
        teamService.deleteInCommonTable("users_teams", " team_id = 2");
        List<Team> teams = teamService.findInCommonTable("users_teams", " team_id = 2", "team_id");
        assertNotNull(teams);
        assertEquals(0, teams.size());
    }

    @Test
    public void findByIdShouldReturnProject() {
        Team team = teamService.findById(1);
        assertNotNull(team);
    }

    @Test
    public void findByIdShouldReturnNullWhenInvalidId() {
        Team team = teamService.findById(123);
        assertNull(team);
    }
}