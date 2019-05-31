package com.pz.auth.controller;

import com.pz.auth.dto.AuthDto;
import com.pz.auth.dto.UserDto;
import com.pz.auth.model.User;
import com.pz.auth.repository.UserRepository;
import com.pz.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("users/")
@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AuthDto> createUser(@RequestBody UserDto user) {
        return new ResponseEntity<>(userService.registerNewUserAccount(user), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AuthDto> getUserByLogin(@RequestBody UserDto user) {
        return new ResponseEntity<>(userService.loginUser(user), HttpStatus.OK);
    }

    @RequestMapping(value = "/auth", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AuthDto> authenitcate(@RequestParam(value = "authToken") String authToken) {
        return new ResponseEntity<>(userService.authenticateUser(authToken), HttpStatus.OK);
    }
}
