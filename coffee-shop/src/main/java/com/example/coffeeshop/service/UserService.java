package com.example.coffeeshop.service;

import com.example.coffeeshop.dto.UserDto;
import com.example.coffeeshop.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);
    User findUserByEmail(String email);
    List<UserDto> findAllUsers();
}
