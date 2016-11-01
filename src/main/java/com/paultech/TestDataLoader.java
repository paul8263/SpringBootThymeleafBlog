package com.paultech;

import com.paultech.domain.User;
import com.paultech.service.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Created by paulzhang on 28/10/2016.
 */
@Component
public class TestDataLoader implements ApplicationRunner {
    @Autowired
    private UserRepo userRepo;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
//        User user = new User();
//        user.setName("Paul");
//        user.setEmail("paul8263@gmail.com");
//        User user2 = new User();
//        user2.setName("Kate");
//        user2.setEmail("kate1234@gmail.com");
//        userRepo.save(user);
//        userRepo.save(user2);
//        System.out.println("Runner executed");
    }
}
