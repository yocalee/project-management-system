package com.scalefocus.model;

public class Project extends BaseEntity{
    private String name;
    private int creator;

    public Project(int id, String name, int creator) {
        super(id);
        this.name = name;
        this.creator = creator;
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

    @Override
    public String toString() {
        return String.format("Project %s with id %d is created by user with id %d.", name, getId(), creator);
    }
}
