package com.pz.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT, reason="Provided login/email is already in use.")
public class ConflictingUserCredentialsException extends InvalidCredentialsException{
}
