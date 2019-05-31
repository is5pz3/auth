package com.pz.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Provided users credentials are invalid.")
public class InvalidCredentialsException extends AuthApplicationException{
}
