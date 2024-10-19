package com.sakhee.finman.expenso.service;

import java.util.List;

import com.sakhee.finman.expenso.dto.UserDto;
import com.sakhee.finman.expenso.entity.Role;
import com.sakhee.finman.expenso.entity.User;

public interface UserService {
	
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    List<UserDto> findAllUsers();

	Role findRoleByName(String role);
}

