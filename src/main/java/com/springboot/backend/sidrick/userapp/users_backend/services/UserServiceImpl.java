package com.springboot.backend.sidrick.userapp.users_backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.backend.sidrick.userapp.users_backend.entities.User;
import com.springboot.backend.sidrick.userapp.users_backend.repositories.UserRepository;

import io.micrometer.common.lang.NonNull;

@Service
public class UserServiceImpl implements UserService{

    //Autowiring the repository is an option. The other option is to create a constructor
    //@Autowired
    private UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
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

    @Override
    @Transactional
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
