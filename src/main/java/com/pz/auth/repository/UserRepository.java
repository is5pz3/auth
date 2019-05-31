package com.pz.auth.repository;

import com.pz.auth.model.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Long> {
    User findByLogin(String login);
}
