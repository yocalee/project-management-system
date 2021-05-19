package com.scalefocus.model;

import com.scalefocus.util.PasswordConverter;

public class User extends BaseEntity{
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String privilege;

    public User(int id, String username, String password, String firstName, String lastName, String privilege) {
        super(id);
        this.username = username;
        setPassword(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.privilege = privilege;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    @Override
    public String toString() {
        return String.format("User (%d) with username %s and password %s has %s privileges.", super.getId(), username, password, privilege);
    }
}
