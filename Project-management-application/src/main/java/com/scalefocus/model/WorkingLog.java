package com.scalefocus.model;

import java.time.LocalDateTime;

public class WorkingLog extends BaseEntity{
    private LocalDateTime time;
    private String description;
    private int user;
    private int task;

    public WorkingLog(int id, LocalDateTime time, String description, int user, int task) {
        super(id);
        this.time = time;
        this.description = description;
        this.user = user;
        this.task = task;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getTask() {
        return task;
    }

    public void setTask(int task) {
        this.task = task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        StringBuilder month = new StringBuilder();
        String[] temp = time.getMonth().toString().split("");

        for (int i = 0; i < temp.length; i++) {
            month.append(i > 0 ? temp[i].toLowerCase() : temp[i]);
        }

        return String.format("User with id %d %s for %d hours and %d minutes (on %d of %s %d year) for task with id %d.",
                user, description, time.getHour(), time.getMinute(), time.getDayOfMonth(),
                month.toString(), time.getYear(), task);
    }
}
