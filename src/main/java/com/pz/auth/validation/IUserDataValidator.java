package com.pz.auth.validation;

import com.pz.auth.dto.UserDto;

public interface IUserDataValidator {
    void validate(UserDto userDto);
}
