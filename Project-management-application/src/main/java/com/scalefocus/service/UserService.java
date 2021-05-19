package com.scalefocus.service;

import com.scalefocus.exception.InvalidCredentials;
import com.scalefocus.model.User;
import com.scalefocus.repository.UserRepository;
import com.scalefocus.util.Validation;

import java.sql.SQLException;


public class UserService extends AbstractService<User> {
    private final UserRepository userRepository;
    private final Validation.UserValidation userValidation;
    public static User user;

    public UserService(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
        userValidation = new Validation.UserValidation(userRepository);
        user = null;
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        try {
            userValidation.validateLogin(username, password);
            user = userRepository.findUserByUsername(username);
        } catch (SQLException | InvalidCredentials e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    public void init() {
        try {
            this.userRepository.init();
        } catch (SQLException ignored) {
        }
    }
}
