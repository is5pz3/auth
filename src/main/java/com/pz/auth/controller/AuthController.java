package com.pz.auth.controller;

import com.pz.auth.dto.AuthDto;
import com.pz.auth.dto.UserDto;
import com.pz.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("users/")
@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AuthDto> createUser(@RequestBody UserDto user) {
        return new ResponseEntity<>(userService.registerNewUserAccount(user), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AuthDto> loginUser(@RequestBody UserDto user) {
        return new ResponseEntity<>(userService.loginUser(user), HttpStatus.OK);
    }

    @RequestMapping(value = "/auth", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AuthDto> authenitcateUser(@RequestParam(value = "authToken") String authToken) {
        return new ResponseEntity<>(userService.authenticateUser(authToken), HttpStatus.OK);
    }
}
