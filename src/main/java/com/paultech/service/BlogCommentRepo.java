package com.paultech.service;

import com.paultech.domain.BlogComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by paulzhang on 27/10/2016.
 */
@Service
public interface BlogCommentRepo extends JpaRepository<BlogComment, Long> {
    List<BlogComment> findByCommenterEmail(String email);
    List<BlogComment> findByCommenterId(long id);
    List<BlogComment> findByCommentedBlogId(long id);
}
