package com.springboot.backend.sidrick.userapp.users_backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.*;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

//By convention, the tables in databases are called in plural, whereas the Java Entity class is called in
//singular, because the Entity is an instance.
@Entity
@Table(name = "users")
public class User {

    //GenerationType IDENTITY means that the primary key is auto-incrementing.
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    //It is important that the front-end names for the variable are written the same as in the back-end.
    //In this case, we chose to write the same name in both places. Othewise, you could use @Column(...) annotation.
    //By default, Hibernate uses a naming strategy that converts camelCase to snake_case, so we had to change the column name in the database to last_name. Using
    //the @Column(name="lastName") annotation does NOT override this naming convention.
    @NotBlank
    private String lastName;

    @NotEmpty
    @Email
    private String email;

    @NotBlank
    @Size(min=4)
    private String username;

    @NotBlank
    private String password;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
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
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
}
