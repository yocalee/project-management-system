package com.scalefocus.model;

public class Task extends BaseEntity{
    private String name;
    private int creator;
    private int project;
    private String status;

    public Task(int id, String name, int creator, int project, String status) {
        super(id);
        this.name = name;
        this.creator = creator;
        this.project = project;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public int getProject() {
        return project;
    }

    public void setProject(int project) {
        this.project = project;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Task %s with id %d has status %s and creator with id %d.", name, getId(), status, creator);
    }
}
