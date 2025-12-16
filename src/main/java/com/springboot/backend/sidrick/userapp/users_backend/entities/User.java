package com.springboot.backend.sidrick.userapp.users_backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.backend.sidrick.userapp.users_backend.models.IUser;

import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

//By convention, the tables in databases are called in plural, whereas the Java Entity class is called in
//singular, because the Entity is an instance.
@Entity
@Table(name = "users")
public class User implements IUser{

    //GenerationType IDENTITY means that the primary key is auto-incrementing.
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    //It is important that the front-end names for the variable are written the same as in the back-end.
    //In this case, we chose to write the same name in both places. Othewise, you could use @Column(...) annotation.
    //By default, Hibernate uses a naming strategy that converts camelCase to snake_case, so we had to change the column name in the database to last_name. Using
    //the @Column(name="lastName") annotation does NOT override this naming convention.
    @NotBlank
    private String lastName;

    @NotEmpty
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min=4)
    @Column(unique = true)
    private String username;

    //The annotation Transient means that this field is not part of the database, it is merely an attribute of the class.
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean admin;

    @NotBlank
    private String password;

    //Since we've just created the Role Entity, now we must indicate that User entity is linked to it somehow. A user can
    //have one or more Roles, and each Role can have one or more user, so the relationoship is many-to-many. In order to join
    //the User table and the Roles table, we had do create an intermediate table called users_roles. User entity is related to 
    //users_roles through the 'user_id' column (hence the joinColumns) and the Roles entity does the same thing through the 'role_id', 
    //(hence the inverseJoinColumns).
    //@JsonIgnorePoperties ignores garbage properties that can't be converted to JSON
    //@ManyToMany annotation specifies that FetchType is Lazy in order not to retrieve every single table relationship that the User
    //might have, unless those relationships are explicitly requested.
    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name="users_roles",
        joinColumns = {@JoinColumn(name="user_id")},
        inverseJoinColumns = {@JoinColumn(name="role_id")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role_id"})}
    )
    private List<Role> roles;

    public User() {
        this.roles = new ArrayList<>();
    }

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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    
}
