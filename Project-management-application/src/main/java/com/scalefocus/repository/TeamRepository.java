package com.scalefocus.repository;

import com.scalefocus.model.Team;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TeamRepository extends DataRepository<Team> {

    public TeamRepository() {
        super("teams");
    }

    @Override
    public Team buildEntity(ResultSet rs) throws SQLException {
        return new Team(rs.getInt("id"), rs.getString("name"));
    }
}
