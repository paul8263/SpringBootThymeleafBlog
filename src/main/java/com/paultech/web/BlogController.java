package com.paultech.web;

import com.paultech.domain.Blog;
import com.paultech.domain.BlogComment;
import com.paultech.service.BlogRepo;
import com.paultech.web.exceptions.ItemNotFoundException;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Created by paulzhang on 28/10/2016.
 */
@Controller
@RequestMapping(value = "/blog")
public class BlogController {

    @Autowired
    private BlogRepo blogRepo;

    @GetMapping
    public String displayBlog(Model model) {
        List<Blog> blogList = blogRepo.findAll();
        model.addAttribute("blogList", blogList);
        return "blog";
    }

    @GetMapping(value = "/{blogId}")
    public String displayBlogDetail(@PathVariable long blogId, Model model) {
        Blog blog = blogRepo.findOne(blogId);
        model.addAttribute("blog", blog);
        return "blogDetail";
    }

    @GetMapping(value = "/{blogId}/modify")
    public String modifyBlog(@PathVariable long blogId, Model model) throws ItemNotFoundException {
        Blog blog = blogRepo.findOne(blogId);
        if (null == blog) throw new ItemNotFoundException();
        model.addAttribute("blog", blog);
        model.addAttribute("isAdding", false);
        return "addModifyBlog";
    }

    @PostMapping(value = "/{blogId}/modify")
    public String modifyBlog(@ModelAttribute @Valid Blog blog, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isAdding", false);
            return "addModifyBlog";
        }
        blog.setModifyDate(new Date());
        blogRepo.save(blog);
        return "redirect:/blog";
    }

    @PostMapping(value = "/new")
    public String createBlog(@ModelAttribute @Valid Blog blog, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isAdding", true);
            return "addModifyBlog";
        }
        blog.setCreateDate(new Date());
        blog.setModifyDate(new Date());
        blogRepo.save(blog);
        return "redirect:/blog";
    }

    @GetMapping(value = "/new")
    public String showBlogCreationPage(Model model) {
        model.addAttribute("isAdding", true);
        model.addAttribute("blog", new Blog());
        return "addModifyBlog";
    }


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
