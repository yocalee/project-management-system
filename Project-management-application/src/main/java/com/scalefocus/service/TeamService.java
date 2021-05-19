package com.scalefocus.service;

import com.scalefocus.model.Team;
import com.scalefocus.repository.TeamRepository;

public class TeamService extends AbstractService<Team> {
    public TeamService(TeamRepository teamRepository) {
        super(teamRepository);
    }

}
