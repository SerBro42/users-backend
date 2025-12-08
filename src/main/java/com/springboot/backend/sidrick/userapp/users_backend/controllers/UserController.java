package com.springboot.backend.sidrick.userapp.users_backend.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.springboot.backend.sidrick.userapp.users_backend.entities.User;
import com.springboot.backend.sidrick.userapp.users_backend.models.UserRequest;
import com.springboot.backend.sidrick.userapp.users_backend.services.UserService;

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;


//The @CrossOrigin annotation is added to fix the CORS policy blocking error in our front-end.
@CrossOrigin(origins = {"http://localhost:4200"})
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

    //After implementing Spring Security, an explicit path variable name had to be added ("page")
    @GetMapping("/page/{page}")
    public Page<User> listPageable(@PathVariable("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 5);
        return service.findAll(pageable);
    }
    
    //Converts our DTO into a JSON
    //In our UserService, the findById() method returns an optional, so we must create an additional check for null objects.
    //We use ResponsEntity so that it returns a 200 or 400 response, depending if the object was found.
    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable("id") Long id) {
        Optional<User> userOptional = service.findById(id);
        if(userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(userOptional.orElseThrow());
        }
        //There are 2 variants for the return: the first returns 404 with no body. The second, a JSON body with an error message, including 404.
        //return ResponseEntity.notFound().build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "no user was found with id: "+id));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
        if(result.hasErrors()) {
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }
    
    //This function includes a check whether the user being edited exists at all.
    //As seen during Postman testing, every column must be included in the request body. Whichever column is not included, the corresponding result will be 
    //null in the table.
    //Following the "clean code" principle, we extracted several lines of userDb.set... from here and put it in UserServiceImpl, so that every class has a definite purpose.
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody UserRequest user, BindingResult result, @PathVariable("id") Long id) {

        if(result.hasErrors()) {
            return validation(result);
        }

        Optional<User> userOptional = service.update(user, id);

        if(userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        Optional<User> userOptional = service.findById(id);
        if(userOptional.isPresent()) {
            
            return ResponseEntity.ok(service.deleteById(id));
        }
        return ResponseEntity.notFound().build();
    }
    
    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), "The field " + error.getField() +" "+ error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
