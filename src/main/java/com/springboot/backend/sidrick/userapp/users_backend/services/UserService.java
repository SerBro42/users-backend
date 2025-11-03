package com.springboot.backend.sidrick.userapp.users_backend.services;

import java.util.List;
import java.util.Optional;

import com.springboot.backend.sidrick.userapp.users_backend.entities.User;

import io.micrometer.common.lang.NonNull;

public interface UserService {

    List<User> findAll();

    //We use optional in order to avoid the null pointer exception
    Optional<User> findById(@NonNull Long id);

    User save(User user);

    void deleteById(Long id);
}
