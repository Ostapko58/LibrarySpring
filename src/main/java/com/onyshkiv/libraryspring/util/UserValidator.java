package com.onyshkiv.libraryspring.util;

import com.onyshkiv.libraryspring.DTO.UserDTO;
import com.onyshkiv.libraryspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO user  = (UserDTO) target;
        if (userService.getUserByLogin(user.getLogin()).isPresent()){
            errors.rejectValue("login","","User login already exist");
        }
    }
}
