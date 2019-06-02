package com.pz.auth.validation;

import com.pz.auth.dto.UserDto;
import com.pz.auth.exception.ConflictingUserCredentialsException;
import com.pz.auth.exception.InvalidCredentialsException;
import com.pz.auth.exception.WeakPasswordException;
import com.pz.auth.model.User;
import com.pz.auth.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

public class UserDataValidatorTest {

    private static final String LOGIN = "login";
    private static final String EXISTING_LOGIN = "existingLogin";

    @Mock
    UserRepository userRepository;

    UserDataValidator userDataValidator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        userDataValidator = new UserDataValidator(userRepository);
        when(userRepository.findByLogin(EXISTING_LOGIN)).thenReturn(new User());
    }

    @Test(expected = InvalidCredentialsException.class)
    public void shouldThrowInvalidCredentialsExceptionWhenPasswordIsEmpty() {
        // GIVEN
        UserDto user =  new UserDto(LOGIN, "");

        // WHEN
        userDataValidator.validate(user);
    }

    @Test(expected = InvalidCredentialsException.class)
    public void shouldThrowInvalidCredentialsExceptionWhenLoginIsEmpty() {
        // GIVEN
        UserDto user =  new UserDto(null, "password");

        // WHEN
        userDataValidator.validate(user);
    }

    @Test(expected = ConflictingUserCredentialsException.class)
    public void shouldThrowConflictingUserCredentialsExceptionWhenLoginAlreadyExists() {
        // GIVEN
        UserDto user =  new UserDto(EXISTING_LOGIN, "password");

        // WHEN
        userDataValidator.validate(user);
    }

    @Test(expected = WeakPasswordException.class)
    public void shouldThrowWeakPasswordExceptionWhenPasswordIsTooShort() {
        // GIVEN
        UserDto user =  new UserDto(LOGIN, "Tst1");

        // WHEN
        userDataValidator.validate(user);
    }

    @Test(expected = WeakPasswordException.class)
    public void shouldThrowWeakPasswordExceptionWhenDoesNotContainNumbers() {
        // GIVEN
        UserDto user =  new UserDto(LOGIN, "password");

        // WHEN
        userDataValidator.validate(user);
    }

    @Test(expected = WeakPasswordException.class)
    public void shouldThrowWeakPasswordExceptionWhenDoesNotContainLetters() {
        // GIVEN
        UserDto user =  new UserDto(LOGIN, "1234567");

        // WHEN
        userDataValidator.validate(user);
    }

    @Test
    public void passValidationForValidCredentials() {
        // GIVEN
        UserDto user =  new UserDto(LOGIN, "password1");

        // WHEN
        userDataValidator.validate(user);
    }
}