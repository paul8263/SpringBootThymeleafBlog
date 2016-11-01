package com.paultech;

import com.paultech.domain.Blog;
import com.paultech.domain.BlogComment;
import com.paultech.domain.User;
import com.paultech.service.BlogCommentRepo;
import com.paultech.service.BlogRepo;
import com.paultech.service.UserRepo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JavaBlogApplicationTests {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private BlogRepo blogRepo;

	@Autowired
	private BlogCommentRepo blogCommentRepo;

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void UserTest() {
		User user = new User();
		user.setName("Paul");
		user.setEmail("paul8263@gmail.com");
		User user2 = new User();
		user2.setName("Kate");
		user2.setEmail("kate1234@gmail.com");
		userRepo.save(user);
		userRepo.save(user2);
		List<User> userList = userRepo.findByName("Kate");
		Assert.assertEquals(userList.size(), 1);
		Assert.assertEquals(userList.get(0).getEmail(), "kate1234@gmail.com");
		User retrievedUser = userRepo.findByEmail("paul8263@gmail.com");
		Assert.assertEquals(retrievedUser.getName(), "Paul");
	}

	@Test
	public void BlogTest() {
		User user = new User();
		user.setName("Paul");
		user.setEmail("paul8263@gmail.com");
		userRepo.save(user);
		Blog blog1 = new Blog();
		blog1.setUser(user);
		blog1.setTitle("Hello World");
		blogRepo.save(blog1);
		Blog blog2 = new Blog();
		blog2.setUser(user);
		blog2.setTitle("2nd Hello");
		blogRepo.save(blog2);
		List<Blog> blogList = blogRepo.findByUserEmail("paul8263@gmail.com");
		Assert.assertEquals(blogList.size(), 2);
		Assert.assertEquals(blogList.get(0).getTitle(), "Hello World");
	}

	@Test
	public void blogCommentTest() {
		User user = new User();
		user.setName("Paul");
		user.setEmail("paul8263@gmail.com");
		User user2 = new User();
		user2.setName("Kate");
		user2.setEmail("kate1234@gmail.com");
		userRepo.save(user);
		userRepo.save(user2);
		Blog blog1 = new Blog();
		blog1.setUser(user);
		blog1.setTitle("Hello World");
		blogRepo.save(blog1);
		BlogComment blogComment1 = new BlogComment();
		blogComment1.setContent("Awesome");
		blogComment1.setCommenter(user);
		blogComment1.setCommentedBlog(blog1);
		blogCommentRepo.save(blogComment1);
		BlogComment blogComment2 = new BlogComment();
		blogComment2.setContent("Good");
		blogComment2.setCommenter(user);
		blogComment2.setCommentedBlog(blog1);
		blogCommentRepo.save(blogComment2);
		List<BlogComment> blogCommentList = blogCommentRepo.findByCommenterEmail("paul8263@gmail.com");
		Assert.assertEquals(blogCommentList.size(), 2);
		Assert.assertEquals(blogCommentList.get(0).getContent(), "Awesome");
		Assert.assertEquals(blogCommentList.get(1).getContent(), "Good");
	}

	@Test
	public void mainControllerTest() throws Exception {
		mvc.perform(get("/")).andExpect(status().is(200)).andExpect(view().name("index"));
	}

}
