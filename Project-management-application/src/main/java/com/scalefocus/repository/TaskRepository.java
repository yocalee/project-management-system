package com.scalefocus.repository;

import com.scalefocus.model.Task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository extends DataRepository<Task> {

    public TaskRepository(){
        super("tasks");
    }
    @Override
    public Task buildEntity(ResultSet rs) throws SQLException {
        return new Task(rs.getInt("id"), rs.getString("name"), rs.getInt("creator_id"),
                rs.getInt("project_id"), rs.getString("status"));
    }

    public List<Task> findTasksByUserId(int userId) throws SQLException {
        preparedStatement = connection.prepareStatement("select * from tasks\n" +
                "join projects_teams pt\n" +
                "on tasks.project_id = pt.project_id\n" +
                "join users_teams ut\n" +
                "on pt.team_id = ut.team_id\n" +
                "where ut.user_id = ?;");
        preparedStatement.setInt(1, userId);
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet();
        List<Task> tasks = new ArrayList<>();
        while (resultSet.next()){
            tasks.add(buildEntity(resultSet));
        }
        return tasks;
    }
}
