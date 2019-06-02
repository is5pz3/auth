package com.pz.auth.service;

import com.pz.auth.dto.AuthDto;
import com.pz.auth.dto.UserDto;
import com.pz.auth.exception.FailedAuthenticationAttempt;
import com.pz.auth.exception.FailedLoginAttempt;
import com.pz.auth.jwt.Jwt;
import com.pz.auth.model.User;
import com.pz.auth.repository.UserRepository;
import com.pz.auth.validation.UserDataValidator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Calendar;
import java.util.Date;

import static org.mockito.Mockito.when;

public class UserServiceTest {

    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String TOKEN = "token";

    @Mock
    UserRepository userRepository;

    @Mock
    UserDataValidator userDataValidator;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    Jwt jwt;

    UserService userService;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        userService = new UserService(userRepository, userDataValidator, passwordEncoder, jwt);
    }

    @Test
    public void shouldRegisterNewUserAccount() {
        // GIVEN
        UserDto userDto = new UserDto(LOGIN, PASSWORD);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(jwt.createJWT(LOGIN)).thenReturn(TOKEN);
        Date expirationDate = new Date();
        when(jwt.getJWTExpireDate(TOKEN)).thenReturn(expirationDate);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        // WHEN
        AuthDto auth = userService.registerNewUserAccount(userDto);

        // THEN
        Mockito.verify(userDataValidator).validate(userDto);
        Mockito.verify(userRepository).save(captor.capture());
        User user = captor.getValue();
        Assert.assertEquals(LOGIN, user.getLogin());
        Assert.assertEquals(ENCODED_PASSWORD, user.getPassword());
        Assert.assertEquals(LOGIN, auth.getLogin());
        Assert.assertEquals(TOKEN, auth.getToken());
        Assert.assertEquals(expirationDate, auth.getExpirationDate());
    }

    @Test(expected = FailedLoginAttempt.class)
    public void shouldThrowExceptionWhenLoginDoesNotExist() {
        // GIVEN
        UserDto userDto = new UserDto(LOGIN, PASSWORD);

        // WHEN
        userService.loginUser(userDto);
    }

    @Test(expected = FailedLoginAttempt.class)
    public void shouldThrowExceptionWhenPasswordsDoNotMarch() {
        // GIVEN
        UserDto userDto = new UserDto(LOGIN, PASSWORD);
        User user = new User();
        user.setPassword(ENCODED_PASSWORD);
        when(userRepository.findByLogin(LOGIN)).thenReturn(user);
        when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(false);

        // WHEN
        userService.loginUser(userDto);
    }

    @Test
    public void shouldLogInUserSuccessfully() {
        // GIVEN
        UserDto userDto = new UserDto(LOGIN, PASSWORD);
        User user = new User();
        user.setPassword(ENCODED_PASSWORD);
        when(userRepository.findByLogin(LOGIN)).thenReturn(user);
        when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
        when(jwt.createJWT(LOGIN)).thenReturn(TOKEN);
        Date expirationDate = new Date();
        when(jwt.getJWTExpireDate(TOKEN)).thenReturn(expirationDate);

        // WHEN
        AuthDto auth = userService.loginUser(userDto);

        // THEN
        Assert.assertEquals(LOGIN, auth.getLogin());
        Assert.assertEquals(TOKEN, auth.getToken());
        Assert.assertEquals(expirationDate, auth.getExpirationDate());
    }

    @Test(expected = FailedAuthenticationAttempt.class)
    public void shouldThrowExceptionWhenJwtTokenIsInvalid() {
        // GIVEN
        when(jwt.decodeJWT(TOKEN)).thenThrow(new RuntimeException());

        // WHEN
        userService.authenticateUser(TOKEN);
    }

    @Test(expected = FailedAuthenticationAttempt.class)
    public void shouldThrowExceptionWhenEncodedUserDoesNotExist() {
        // GIVEN
        Claims claims = new DefaultClaims();
        claims.setSubject(LOGIN);
        Date expirationDate = new Date();
        claims.setExpiration(expirationDate);
        when(jwt.decodeJWT(TOKEN)).thenReturn(claims);

        // WHEN
        userService.authenticateUser(TOKEN);
    }

    @Test(expected = FailedAuthenticationAttempt.class)
    public void shouldThrowExceptionWhenTokenIsExpired() {
        // GIVEN
        Claims claims = new DefaultClaims();
        claims.setSubject(LOGIN);
        Date expirationDate = new Date();
        claims.setExpiration(expirationDate);
        when(jwt.decodeJWT(TOKEN)).thenReturn(claims);
        when(userRepository.findByLogin(LOGIN)).thenReturn(new User());

        // WHEN
        userService.authenticateUser(TOKEN);
    }

    @Test
    public void shouldAuthenticateUserSuccessfullyForActiveToken() {
        // GIVEN
        Claims claims = new DefaultClaims();
        claims.setSubject(LOGIN);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date expirationDate = calendar.getTime();
        claims.setExpiration(expirationDate);
        when(jwt.decodeJWT(TOKEN)).thenReturn(claims);
        when(userRepository.findByLogin(LOGIN)).thenReturn(new User());
        when(jwt.getJWTExpireDate(TOKEN)).thenReturn(expirationDate);

        // WHEN
        AuthDto auth = userService.authenticateUser(TOKEN);

        // THEN
        Assert.assertEquals(LOGIN, auth.getLogin());
        Assert.assertEquals(TOKEN, auth.getToken());
        Assert.assertEquals(expirationDate, auth.getExpirationDate());
    }
}