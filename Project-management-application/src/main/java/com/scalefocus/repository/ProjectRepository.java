package com.scalefocus.repository;

import com.scalefocus.model.Project;
import com.scalefocus.model.Team;
import com.scalefocus.service.UserService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectRepository extends DataRepository<Project> {

    public ProjectRepository(){
        super("projects");
    }

    @Override
    public Project buildEntity(ResultSet rs) throws SQLException {
        return new Project(rs.getInt("id"), rs.getString("name"), rs.getInt("creator_id"));
    }

    public List<Project> findProjectsRelatedToUser(int id) throws SQLException{
        preparedStatement = connection.prepareStatement("select projects.id, name, creator_id from projects\n" +
                "join projects_teams pt\n" +
                "on projects.id = pt.project_id\n" +
                "join users_teams ut\n" +
                "on pt.team_id = ut.team_id\n" +
                "where ut.user_id = ?;");
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet();
        List<Project> projects = new ArrayList<>();
        while (resultSet.next()){
            projects.add(buildEntity(resultSet));
        }
        return projects;
    }
}
