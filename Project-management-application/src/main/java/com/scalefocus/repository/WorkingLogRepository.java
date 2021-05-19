package com.scalefocus.repository;

import com.scalefocus.model.WorkingLog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class WorkingLogRepository extends DataRepository<WorkingLog> {

    public WorkingLogRepository(){
        super("working_logs");
    }
    @Override
    public WorkingLog buildEntity(ResultSet rs) throws SQLException {
        return new WorkingLog(rs.getInt("id"),
                rs.getTimestamp("time").toLocalDateTime(),
                rs.getString("description"),
                rs.getInt("user_id"),
                rs.getInt("task_id"));
    }
}
