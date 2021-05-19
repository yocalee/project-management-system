package com.scalefocus.service;

import com.scalefocus.model.WorkingLog;
import com.scalefocus.repository.WorkingLogRepository;

public class WorkLogService extends AbstractService<WorkingLog> {

    public WorkLogService(WorkingLogRepository workingLogRepository) {
        super(workingLogRepository);
    }

}
