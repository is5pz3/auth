package com.pz.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Provided password is too weak. Should be longer than 5 and contain at least one number and character.")
public class WeakPasswordException extends InvalidCredentialsException{
}
