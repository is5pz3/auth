package com.pz.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Invalid or expired token.")
public class FailedAuthenticationAttempt extends InvalidCredentialsException {
}
