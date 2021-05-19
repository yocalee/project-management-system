package com.scalefocus.model;

public class Team extends BaseEntity{
    private String name;

    public Team(int id, String name){
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Team %s has id %d.", name, getId());
    }
}
