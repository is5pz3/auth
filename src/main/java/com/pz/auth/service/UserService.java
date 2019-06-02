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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDataValidator userDataValidator;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Jwt jwt;

    public UserService(UserRepository userRepository, UserDataValidator userDataValidator,
                       PasswordEncoder passwordEncoder, Jwt jwt) {
        this.userRepository = userRepository;
        this.userDataValidator =userDataValidator;
        this.passwordEncoder = passwordEncoder;
        this.jwt = jwt;
    }

    public AuthDto registerNewUserAccount(UserDto userDto) {
        userDataValidator.validate(userDto);
        String login = userDto.getLogin();

        userRepository.save(new User(login, passwordEncoder.encode(userDto.getPassword())));
        String authToken = jwt.createJWT(login);
        return new AuthDto(login, authToken, jwt.getJWTExpireDate(authToken));
    }

    public AuthDto loginUser(UserDto userDto) {
        User user = userRepository.findByLogin(userDto.getLogin());
        if (user == null || !passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new FailedLoginAttempt();
        }

        String login = userDto.getLogin();
        String authToken = jwt.createJWT(login);
        return new AuthDto(login, authToken, jwt.getJWTExpireDate(authToken));
    }

    public AuthDto authenticateUser(String authToken) {
        try {
            Claims claims = jwt.decodeJWT(authToken);
            String login = claims.getSubject();
            Date expireDate = claims.getExpiration();
            User user = userRepository.findByLogin(login);

            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            if (user == null || expireDate.before(now)) {
                throw new RuntimeException();
            }
            return new AuthDto(login, authToken, jwt.getJWTExpireDate(authToken));

        } catch (Exception e) {
            throw new FailedAuthenticationAttempt();
        }
    }
}
