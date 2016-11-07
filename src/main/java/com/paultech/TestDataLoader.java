package com.paultech;

import com.paultech.domain.Blog;
import com.paultech.domain.User;
import com.paultech.service.BlogRepo;
import com.paultech.service.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.IntStream;

/**
 * Created by paulzhang on 28/10/2016.
 */
@Component
public class TestDataLoader implements ApplicationRunner {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BlogRepo blogRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private boolean testing = true;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        if (testing) {
            User user = new User();
            user.setEmail("123@123");
            user.setPassword(passwordEncoder.encode("123456"));
            userRepo.save(user);
            IntStream.range(0, 30).mapToObj(value -> {
                Blog blog = new Blog();
                blog.setTitle("Title:" + value);
                blog.setContent("Content:" + value);
                blog.setModifyDate(new Date());
                blog.setCreateDate(new Date());
                blog.setUser(user);
                return blog;
            }).forEach(blogRepo::save);
        }
    }
}
