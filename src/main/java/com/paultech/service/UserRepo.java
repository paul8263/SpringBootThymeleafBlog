package com.paultech.service;

import com.paultech.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by paulzhang on 27/10/2016.
 */
@Service
public interface UserRepo extends JpaRepository<User, Long> {
    List<User> findByName(String name);
    User findByEmail(String email);
}
