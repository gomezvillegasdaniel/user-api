package com.example.demo.data.validator;

import com.example.demo.data.constraint.UniqueUserConstraint;
import com.example.demo.data.model.User;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.example.demo.constant.SecurityConstant.USER_ALREADY_EXISTS_MESSAGE;

public class UniqueUserConstraintValidator implements ConstraintValidator<UniqueUserConstraint, User> {

    private final UserService userService;

    public UniqueUserConstraintValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        boolean existsByUsername = userService.findByUserName(user.getUsername()).isPresent();
        boolean existsByEmail = userService.findByEmail(user.getEmail()).isPresent();
        boolean existsByPhone = userService.findByPhone(user.getPhone()).isPresent();
        if (existsByUsername || existsByEmail || existsByPhone) {
            throw new UserAlreadyExistsException(USER_ALREADY_EXISTS_MESSAGE);
        }
        return true;
    }
}
