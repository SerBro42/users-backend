package com.springboot.backend.sidrick.userapp.users_backend.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

//This class is created specifically for PUT HTTP requests, because POST has its own, separate functionality. This request does not include the Password field, because the latter will be dealt with separately
//just in PUT requests.
public class UserRequest implements IUser{

    @NotBlank
    private String name;

    @NotBlank
    private String lastName;

    @NotEmpty
    @Email
    private String email;

    @NotBlank
    @Size(min=4)
    private String username;

    private boolean admin;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
