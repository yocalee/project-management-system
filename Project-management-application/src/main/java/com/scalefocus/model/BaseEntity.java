package com.scalefocus.model;

public abstract class BaseEntity {

    private int id;

    public BaseEntity(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
