package com.paultech.service;

import com.paultech.domain.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by paulzhang on 27/10/2016.
 */
@Service
public interface BlogRepo extends JpaRepository<Blog, Long> {
    List<Blog> findByUserEmail(String email);
    Page<Blog> findAll(Pageable pageable);
}
