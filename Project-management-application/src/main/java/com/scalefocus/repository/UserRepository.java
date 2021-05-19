package com.scalefocus.repository;

import com.scalefocus.model.User;
import com.scalefocus.util.PasswordConverter;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository extends DataRepository<User>{

    public UserRepository() {
        super("users");
    }

    @Override
    public User buildEntity(ResultSet rs) throws SQLException {
        return new User(rs.getInt("id"), rs.getString("username"),
                rs.getString("password"),
                rs.getString("first_name"), rs.getString("last_name"), rs.getString("privilege"));
    }

    public User findUserByUsername(String username) throws SQLException {
        q.select(null).from("users").where(String.format(" username = '%s'",username));
        preparedStatement = connection.prepareStatement(q.getQuery());
        preparedStatement.execute();
        ResultSet rs = preparedStatement.getResultSet();
        rs.next();
        return buildEntity(rs);
    }
}
