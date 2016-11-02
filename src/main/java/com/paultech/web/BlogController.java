package com.paultech.web;

import com.paultech.domain.Blog;
import com.paultech.domain.BlogComment;
import com.paultech.domain.User;
import com.paultech.service.BlogCommentRepo;
import com.paultech.service.BlogRepo;
import com.paultech.web.exceptions.ItemNotFoundException;
import com.paultech.web.exceptions.UnauthorizedException;
import com.paultech.web.helpers.IUserHelper;
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

    @Autowired
    private BlogCommentRepo blogCommentRepo;

    @Autowired
    private IUserHelper userHelper;

    @GetMapping
    public String displayAllBlog(Model model) {
        List<Blog> blogList = blogRepo.findAll();
        model.addAttribute("blogList", blogList);
        model.addAttribute("displayAddBlogLink", false);
        return "blog";
    }

    @GetMapping(value = "/my")
    public String displayMyBlog(Model model) throws UnauthorizedException {
        User user = userHelper.getUserFromAuthentication();
        if (null == user) throw new UnauthorizedException();
        List<Blog> blogList = blogRepo.findByUserEmail(user.getEmail());
        model.addAttribute("blogList", blogList);
        model.addAttribute("displayAddBlogLink", true);
        return "blog";
    }

    @GetMapping(value = "/{blogId}")
    public String displayBlogDetail(@PathVariable long blogId, Model model) throws ItemNotFoundException {
        Blog blog = blogRepo.findOne(blogId);
        if (null == blog) throw new ItemNotFoundException();
        model.addAttribute("blog", blog);
        model.addAttribute("blogComment", new BlogComment());
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
    public String createBlog(@ModelAttribute @Valid Blog blog, BindingResult result, Model model) throws UnauthorizedException {
        if (result.hasErrors()) {
            model.addAttribute("isAdding", true);
            return "addModifyBlog";
        }
        User user = userHelper.getUserFromAuthentication();
        if (null == user) throw new UnauthorizedException();

        blog.setUser(user);
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

    @GetMapping(value = "/{blogId}/delete")
    public String deleteBlog(@PathVariable long blogId) {
        blogRepo.delete(blogId);
        return "redirect:/blog";
    }

    @PostMapping(value = "/{blogId}/comment")
    public String createComment(@PathVariable long blogId, @ModelAttribute @Valid BlogComment blogComment, BindingResult result, Model model) throws ItemNotFoundException, UnauthorizedException {
        Blog blog = blogRepo.findOne(blogId);
        if (null == blog) throw new ItemNotFoundException();
        if (result.hasErrors()) {
            model.addAttribute("blog", blog);
            model.addAttribute("blogComment", blogComment);
            return "blogDetail";
        }

        User user = userHelper.getUserFromAuthentication();
        if (null == user) throw new UnauthorizedException();

        blogComment.setCommenter(user);
        blogComment.setCommentedBlog(blog);
        blogComment.setCommentDate(new Date());
        blogCommentRepo.save(blogComment);
        return "redirect:/blog/" + blogId;
    }


    @GetMapping(value = "/{blogId}/comment/{commentId}/delete")
    public String deleteComment(@PathVariable long blogId, @PathVariable long commentId) {
        blogCommentRepo.delete(commentId);
        return "redirect:/blog/" + blogId;
    }

}
