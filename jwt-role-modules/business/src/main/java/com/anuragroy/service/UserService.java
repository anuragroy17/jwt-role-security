package com.anuragroy.service;

import com.anuragroy.dto.UserDto;
import com.anuragroy.model.User;

import java.util.List;

public interface UserService {

    User save(UserDto user);
    List<User> findAll();
    void delete(long id);
    User findOne(String username);

    User findById(Long id);
}
