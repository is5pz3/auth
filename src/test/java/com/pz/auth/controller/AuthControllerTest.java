package com.pz.auth.controller;

import com.pz.auth.dto.AuthDto;
import com.pz.auth.dto.UserDto;
import com.pz.auth.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;

public class AuthControllerTest {

    private static final String TOKEN = "token";

    @Mock
    UserService userService;

    AuthController authController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        authController = new AuthController(userService);
    }

    @Test
    public void shouldReturnCreatedStatusOnSuccessfulUserCreation() {
        // GIVEN
        UserDto userDto = new UserDto();
        AuthDto authDto = new AuthDto();
        when(userService.registerNewUserAccount(userDto)).thenReturn(authDto);

        // WHEN
        ResponseEntity<AuthDto> response = authController.createUser(userDto);

        // THEN
        Assert.assertEquals(authDto, response.getBody());
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void shouldReturnOkStatusOnSuccessfulLoginAttempt() {
        // GIVEN
        UserDto userDto = new UserDto();
        AuthDto authDto = new AuthDto();
        when(userService.loginUser(userDto)).thenReturn(authDto);

        // WHEN
        ResponseEntity<AuthDto> response = authController.loginUser(userDto);

        // THEN
        Assert.assertEquals(authDto, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldReturnOkStatusOnSuccessfulAuthentication() {
        // GIVEN
        AuthDto authDto = new AuthDto();
        when(userService.authenticateUser(TOKEN)).thenReturn(authDto);

        // WHEN
        ResponseEntity<AuthDto> response = authController.authenitcateUser(TOKEN);

        // THEN
        Assert.assertEquals(authDto, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}