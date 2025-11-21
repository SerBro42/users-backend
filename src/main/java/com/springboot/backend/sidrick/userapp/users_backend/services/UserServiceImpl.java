package com.springboot.backend.sidrick.userapp.users_backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.backend.sidrick.userapp.users_backend.entities.Role;
import com.springboot.backend.sidrick.userapp.users_backend.entities.User;
import com.springboot.backend.sidrick.userapp.users_backend.models.IUser;
import com.springboot.backend.sidrick.userapp.users_backend.models.UserRequest;
import com.springboot.backend.sidrick.userapp.users_backend.repositories.RoleRepository;
import com.springboot.backend.sidrick.userapp.users_backend.repositories.UserRepository;

import io.micrometer.common.lang.NonNull;

@Service
public class UserServiceImpl implements UserService{

    //Autowiring the repository is an option. The other option is to create a constructor
    //@Autowired
    private UserRepository repository;
    private RoleRepository roleRepository;

    //Adding another variable that will be part of the constructor.
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    //ReadOnly = true is used when this query is only to consult data, never to edit it.
    //findAll() returns an iterable, but we need a list. So, we cast it as a list.
    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List) this.repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(@NonNull Long id) {
        return repository.findById(id);
    }

    //Now, when we create a new User, we give it ROLE_USER by default
    @Override
    @Transactional
    public User save(User user) {

        user.setRoles(getRoles(user));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    //We restructure our code by adding an update method in this Class by copying (and later cutting) the code from UserController.
    @Override
    public Optional<User> update(UserRequest user, Long id) {
        Optional<User> userOptional = repository.findById(id);

        if(userOptional.isPresent()) {
            User userDb = userOptional.get();
            userDb.setEmail(user.getEmail());
            userDb.setLastName(user.getLastName());
            userDb.setName(user.getName());
            //This line for password editing has been removed, because there is a separate Security function for that purpose specifically.
            userDb.setUsername(user.getUsername());

            userDb.setRoles(getRoles(user));
            return Optional.of(repository.save(userDb));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    //We refactor our code by extracting these common lines from both the save() and the update() method, and we create
    //the IUser interface specifically to make this method compatible with both methods.
    private List<Role> getRoles(IUser user) {
        List<Role> roles = new ArrayList<>();
        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        //Callback with method reference: roles::add, as an alternative to 'role -> roles.add(role)'
        optionalRoleUser.ifPresent(roles::add);

        if(user.isAdmin()) {
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roles::add);
        }
        return roles;
    }

}
