package com.paultech.web;

import com.paultech.domain.Blog;
import com.paultech.domain.BlogComment;
import com.paultech.domain.User;
import com.paultech.service.BlogCommentRepo;
import com.paultech.service.BlogRepo;
import com.paultech.web.exceptions.ItemNotFoundException;
import com.paultech.web.exceptions.UnauthorizedException;
import com.paultech.web.helpers.IUserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

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
    public String displayAllBlog(@PageableDefault(direction = Sort.Direction.DESC, sort = "modifyDate") Pageable pageable, Model model) {
        Page<Blog> blogList = blogRepo.findAll(pageable);
        model.addAttribute("blogList", blogList);
        model.addAttribute("displayAddBlogLink", false);
        return "blog";
    }

    @GetMapping(value = "/my")
    public String displayMyBlog(@PageableDefault(direction = Sort.Direction.DESC, sort = "modifyDate") Pageable pageable, Model model) throws UnauthorizedException {
        User user = userHelper.getUserFromAuthentication();

        Page<Blog> blogList = blogRepo.findAll(pageable);
        model.addAttribute("blogList", blogList);
        model.addAttribute("displayAddBlogLink", true);
        return "blog";
    }

    @GetMapping(value = "/{blogId}")
    public String displayBlogDetail(@PathVariable("blogId") Blog blog, Model model) throws ItemNotFoundException {
        if (null == blog) throw new ItemNotFoundException();
        model.addAttribute("blog", blog);
        model.addAttribute("blogComment", new BlogComment());
        return "blogDetail";
    }

    @GetMapping(value = "/{blogId}/modify")
    public String displayModifyBlog(@PathVariable("blogId") Blog blog, Model model) throws ItemNotFoundException, UnauthorizedException {
        if (null == blog) throw new ItemNotFoundException();
        User user = userHelper.getUserFromAuthentication();
        if (blog.getUser() != user) throw new UnauthorizedException();
        model.addAttribute("blog", blog);
        model.addAttribute("isAdding", false);
        return "addModifyBlog";
    }

    @PostMapping(value = "/{blogId}/modify")
    public String modifyBlog(@ModelAttribute("blog") @Valid Blog newBlog, @PathVariable("blogId") Blog oldBlog, BindingResult result, Model model) throws UnauthorizedException, ItemNotFoundException {
        if (result.hasErrors()) {
            model.addAttribute("isAdding", false);
            return "addModifyBlog";
        }
        if (null == oldBlog) throw new ItemNotFoundException();
        User user = userHelper.getUserFromAuthentication();
        if (oldBlog.getUser() != user) throw new UnauthorizedException();

        oldBlog.setTitle(newBlog.getTitle());
        oldBlog.setContent(newBlog.getContent());
        oldBlog.setModifyDate(new Date());
        blogRepo.save(oldBlog);
        return "redirect:/blog/" + oldBlog.getId();
    }

    @PostMapping(value = "/new")
    public String createBlog(@ModelAttribute @Valid Blog blog, BindingResult result, Model model) throws UnauthorizedException {
        if (result.hasErrors()) {
            model.addAttribute("isAdding", true);
            return "addModifyBlog";
        }
        User user = userHelper.getUserFromAuthentication();

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
    public String deleteBlog(@PathVariable("blogId") Blog blog) throws ItemNotFoundException, UnauthorizedException {
        if (null == blog) throw new ItemNotFoundException();
        User user = userHelper.getUserFromAuthentication();
        if (blog.getUser() != user) throw new UnauthorizedException();
        blogRepo.delete(blog);
        return "redirect:/blog";
    }

    @PostMapping(value = "/{blogId}/comment")
    public String createComment(@PathVariable("blogId") Blog blog, @ModelAttribute @Valid BlogComment blogComment, BindingResult result, Model model) throws ItemNotFoundException, UnauthorizedException {
        if (null == blog) throw new ItemNotFoundException();
        if (result.hasErrors()) {
            model.addAttribute("blog", blog);
            model.addAttribute("blogComment", blogComment);
            return "blogDetail";
        }

        User user = userHelper.getUserFromAuthentication();

        blogComment.setCommenter(user);
        blogComment.setCommentedBlog(blog);
        blogComment.setCommentDate(new Date());
        blogCommentRepo.save(blogComment);
        return "redirect:/blog/" + blog.getId();
    }


    @GetMapping(value = "/{blogId}/comment/{commentId}/delete")
    public String deleteComment(@PathVariable long blogId, @PathVariable("commentId") BlogComment blogComment) throws ItemNotFoundException, UnauthorizedException {
        if (null == blogComment) throw new ItemNotFoundException();
        User user = userHelper.getUserFromAuthentication();
        if (blogComment.getCommenter() != user) throw new UnauthorizedException();
        blogCommentRepo.delete(blogComment);
        return "redirect:/blog/" + blogId;
    }

}
