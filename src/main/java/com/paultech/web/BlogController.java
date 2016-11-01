package com.paultech.web;

import com.paultech.domain.Blog;
import com.paultech.domain.BlogComment;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Created by paulzhang on 28/10/2016.
 */
@Controller
@RequestMapping(value = "/blog")
public class BlogController {

    @GetMapping(value = "/{blogId}")
    public String displayBlogDetail(@PathVariable long blogId) {
        return "blogDetail";
    }

//    @PostMapping
//    public String createBlog(@ModelAttribute Blog blog, BindingResult result) {
//
//    }
//
//    @PostMapping(value = "/{blogId}")
//    public String modifyBlog(@ModelAttribute Blog blog, BindingResult result) {
//
//    }
//
//    @GetMapping(value = "/{blogId}/delete")
//    public String deleteBlog(@PathVariable long blogId) {
//
//    }
//    @PostMapping(value = "/{blogId}/comment")
//    public String createComment(@ModelAttribute BlogComment blogComment, @PathVariable long BlogId, BindingResult result) {
//
//    }
//
//    @PostMapping(value = "/{blogId}/comment/{commentId}")
//    public String modifyComment(@ModelAttribute BlogComment blogComment, @PathVariable long BlogId, @PathVariable long commentId, BindingResult result) {
//
//    }
//
//    @GetMapping(value = "/{blogId}/comment/{commentId}/delete")
//    public String deleteComment(@PathVariable long BlogId, @PathVariable long commentId) {
//
//    }

}
