package com.springboot.backend.sidrick.userapp.users_backend.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.springboot.backend.sidrick.userapp.users_backend.entities.User;
import com.springboot.backend.sidrick.userapp.users_backend.services.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/users")
public class UserController {

    //The service implementation must be as generic as possible, in order to decouple our class from any 
    //particular service implementation. In order to do that, we call the interface, rather than a concrete class.
    @Autowired
    private UserService service;

    @GetMapping
    public List<User> list() {
        return service.findAll();
    }
    
    //Converts our DTO into a JSON
    //In our UserService, the findById() method returns an optional, so we must create an additional check for null objects.
    //We use ResponsEntity so that it returns a 200 or 400 response, depending if the object was found.
    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Optional<User> userOptional = service.findById(id);
        if(userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(userOptional.orElseThrow());
        }
        //There are 2 variants for the return: the first returns 404 with no body. The second, a JSON body with an error message, including 404.
        //return ResponseEntity.notFound().build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "no user was found with id: "+id));
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }
    
    //This function includes a check whether the user being edited exists at all
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        Optional<User> userOptional = service.findById(id);
        if(userOptional.isPresent()) {
            User userDb = userOptional.get();
            userDb.setEmail(user.getEmail());
            userDb.setLastName(user.getLastName());
            userDb.setName(user.getName());
            userDb.setPassword(user.getPassword());
            userDb.setUsername(user.getUsername());
            return ResponseEntity.ok(service.save(userDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<User> userOptional = service.findById(id);
        if(userOptional.isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
