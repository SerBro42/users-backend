package com.springboot.backend.sidrick.userapp.users_backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.springboot.backend.sidrick.userapp.users_backend.entities.User;

//In Spring, repositories implement the CrudRepository, which already includes most of the
//functionalities that we'll need for the repository. We must include as a generic the Entity that
//we're using the repository for, along with the data type for its primary key (id Long in this case).
//Ctrl + click to see all the methods available in CrudRepository
public interface UserRepository extends CrudRepository<User, Long>{

}
