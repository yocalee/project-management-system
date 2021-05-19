package com.scalefocus.repository;

import com.scalefocus.model.BaseEntity;
import com.scalefocus.repository.connection.DBConnection;
import java.sql.*;
import java.util.*;

public abstract class DataRepository <E extends BaseEntity>{
    protected Connection connection;
    protected Query q = new Query();
    private String tableName;
    protected PreparedStatement preparedStatement;

    public DataRepository(String tableName) {
        connection = DBConnection.getConnection();
        this.tableName = tableName;
    }

    public void init() throws SQLException {
        connection.prepareStatement("use project_management_system;").execute();

        q.createTable("users", "id,privilege,username,password,first_name,last_name".split(","),
                ("int not null auto_increment unique primary key, varchar(15) not null, varchar(120) not null unique, varchar(150) not null," +
                        " varchar(50) not null, varchar(50) not null")
                        .split(", "));
        executeSimpleQuery(q.getQuery());
        q.insert("users").params("privilege, username, password, first_name, last_name".split(", "))
                .values(new Object[]{"ADMIN", "admin", "[97, 100, 109, 105, 110, 112, 97, 115, 115]", "Admin", "Admin"});
        executeSimpleQuery(q.getQuery());
        q.createTable("projects_teams", "project_id, team_id".split(", "),
                                "int not null, int not null".split(", "));
        executeSimpleQuery(q.getQuery());
        q.createTable("teams", "id, name".split(", "),
                        "int not null auto_increment unique primary key, varchar(45) not null".split(", "));
        executeSimpleQuery(q.getQuery());
        q.createTable("working_logs", "id, time, description, user_id, task_id".split(", "),
                        "int not null auto_increment unique primary key, datetime not null, varchar(150) not null, int not null, int not null".split(", "));
        executeSimpleQuery(q.getQuery());
        q.createTable("tasks", "id, name, status, creator_id, project_id".split(", "),
                        "int not null auto_increment unique primary key, varchar(45) not null, varchar(10) not null, int not null, int not null".split(", "));
        executeSimpleQuery(q.getQuery());
        q.createTable("projects", "id, name, creator_id".split(", "),
                        "int not null auto_increment unique primary key, varchar(45) not null, int not null".split(", "));
        executeSimpleQuery(q.getQuery());
        q.createTable("users_teams", "id, user_id, team_id".split(", "),
                        "int not null auto_increment unique primary key, int not null, int not null".split(", "));
        executeSimpleQuery(q.getQuery());
        q.createTable("users_tasks", "id, user_id, task_id".split(", "),
                        "int not null auto_increment unique primary key, int not null, int not null".split(", "));
        executeSimpleQuery(q.getQuery());
    }

    private void executeSimpleQuery(String q) throws SQLException {
        preparedStatement = connection.prepareStatement(q);
        preparedStatement.executeUpdate();
    }

    public E save(String[] params, Object[] values) throws SQLException {
        q.insert(tableName).params(params).values(values);
        executeSimpleQuery(q.getQuery());
        List<E> all = findAll();
        return all.get(all.size() - 1);
    }

    public E update(String[] columns, Object[] values, String requirement) throws SQLException {
        q.update(tableName).set(columns, values).where(requirement);
        executeSimpleQuery(q.getQuery());
        q.select(null).from(tableName).where(requirement);
        preparedStatement = connection.prepareStatement(q.getQuery());
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet();
        resultSet.next();
        return buildEntity(resultSet);
    }

    public E delete(int id) throws SQLException {
        E entity = findById(id);
        q.delete(tableName).where("id = " + id);
        executeSimpleQuery(q.getQuery());
        return entity;
    }

    public void deleteInCommonTable(String tableName, String requirement) throws SQLException {
        q.delete(tableName).where(requirement);
        executeSimpleQuery(q.getQuery());
    }

    public void saveInCommonTable(String tableName, String[] columns, Object[] values) throws SQLException {
        q.insert(tableName).params(columns).values(values);
        executeSimpleQuery(q.getQuery());
    }

    public List<E> findAll() throws SQLException {
        q.select(null).from(tableName);
        preparedStatement = connection.prepareStatement(q.getQuery());
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet();
        List<E> all = new ArrayList<>();

        while (resultSet.next()) {
            all.add(buildEntity(resultSet));
        }
        q = new Query();
        return all;
    }

    public E findById(int id) throws SQLException {
        q.select(null).from(tableName).where("id = " + id);
        preparedStatement = connection.prepareStatement(q.getQuery());
        preparedStatement.execute();
        ResultSet rs = preparedStatement.getResultSet();
        rs.next();
        return buildEntity(rs);
    }

    public abstract E buildEntity(ResultSet resultSet) throws SQLException;

    public void updateInCommonTable(String tableName, String[] columns, Object[] values, String requirement) throws SQLException {
        q.update(tableName).set(columns, values).where(requirement);
        executeSimpleQuery(q.getQuery());
    }

    public List<E> findInCommonTable(String tableName, String requirement, String columnId) throws SQLException {
        q.select(null).from(tableName).where(requirement);
        preparedStatement = connection.prepareStatement(q.getQuery());
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet();
        List<E> entities = new ArrayList<>();
        while (resultSet.next()){
            int id = resultSet.getInt(columnId.trim());
            entities.add(findById(id));
        }
        return entities;
    }
}
