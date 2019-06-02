package com.pz.auth.validation;

import com.pz.auth.dto.UserDto;
import com.pz.auth.exception.ConflictingUserCredentialsException;
import com.pz.auth.exception.InvalidCredentialsException;
import com.pz.auth.exception.WeakPasswordException;
import com.pz.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class UserDataValidator implements IUserDataValidator {

    private static final String AT_LEAST_ONE_ALPHA = ".*[a-zA-Z]+.*";
    private static final String AT_LEAST_ONE_NUMERIC = ".*[0-9]+.*";
    private static final int MIN_PASSWORD_LENGTH = 5;

    @Autowired
    private UserRepository userRepository;

    public UserDataValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validate(UserDto user) {
        if (StringUtils.isEmpty(user.getLogin()) || StringUtils.isEmpty(user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        if (userRepository.findByLogin(user.getLogin()) != null) {
            throw new ConflictingUserCredentialsException();
        }

        String password = user.getPassword();
        boolean atLeastOneAlpha = password.matches(AT_LEAST_ONE_ALPHA);
        boolean atLeastOneNumeric = password.matches(AT_LEAST_ONE_NUMERIC);
        if (user.getPassword().length() < MIN_PASSWORD_LENGTH || !atLeastOneAlpha || !atLeastOneNumeric) {
            throw new WeakPasswordException();
        }
    }
}
