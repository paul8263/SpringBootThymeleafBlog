package com.paultech;

import com.paultech.domain.Blog;
import com.paultech.domain.BlogComment;
import com.paultech.domain.User;
import com.paultech.service.BlogCommentRepo;
import com.paultech.service.BlogRepo;
import com.paultech.service.UserRepo;
import com.paultech.web.helpers.IUserHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Sample user and blog are created by testDataLoader
 */

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
	private IUserHelper userHelper;

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(wac).apply(SecurityMockMvcConfigurers.springSecurity()).build();
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
		mvc.perform(get("/")).andExpect(status().is(302)).andExpect(redirectedUrl("/blog?page=0&size=10"));
		mvc.perform(post("/")).andExpect(status().is4xxClientError());

		mvc.perform(get("/about")).andExpect(status().isOk()).andExpect(view().name("about"));
		mvc.perform(post("/about")).andExpect(status().is4xxClientError());

		mvc.perform(get("/signup")).andExpect(status().isOk()).andExpect(view().name("signUp"));
		mvc.perform(post("/signup")).andExpect(status().is4xxClientError());
		mvc.perform(post("/logout")).andExpect(status().is4xxClientError());

		mvc.perform(get("/login")).andExpect(status().isOk()).andExpect(view().name("login"));

		mvc.perform(get("/blog")).andExpect(status().isOk()).andExpect(view().name("blog"));
	}

	@Test
	public void blogAccessShouldDenyTest() throws Exception {
		mvc.perform(get("/blog/my")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("http://localhost/login"));
		mvc.perform(get("/blog/1/modify")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("http://localhost/login"));
		mvc.perform(get("/blog/new")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("http://localhost/login"));
		mvc.perform(post("/blog/new").param("title", "New Blog").param("content", "Content")).andExpect(status().is4xxClientError());
		mvc.perform(get("/blog/1/delete")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	@WithMockUser(username = "anotherUser")
	public void modifyDeleteBlogShouldDenyTest() throws Exception {
		mvc.perform(get("/blog/1/delete")).andExpect(status().isOk()).andExpect(view().name("errorPages/unauthorized"));
		mvc.perform(post("/blog/1/modify").param("title", "New title").param("content", "New content").with(csrf())).andExpect(status().isOk()).andExpect(view().name("errorPages/unauthorized"));
	}

	@Test
	@WithMockUser
	public void blogAccessUserNotFoundTest() throws Exception {
		mvc.perform(get("/blog/my")).andExpect(status().isOk()).andExpect(view().name("errorPages/unauthorized"));
	}

	@Test
	@WithMockUser(username = "123@123", password = "123456")
	public void viewMyBlogTest() throws Exception {
		mvc.perform(get("/blog/my")).andExpect(status().isOk()).andExpect(view().name("blog"));
	}

	@Test
	@WithMockUser
	public void blogNotFoundTest() throws Exception {
		mvc.perform(get("/blog/4000")).andExpect(status().isOk()).andExpect(view().name("errorPages/itemNotFound"));
	}

	@Test
	@WithMockUser(username = "123@123", password = "123456")
	public void addBlogTest() throws Exception {
//		System.out.println(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
//		User user = userRepo.findByEmail("123@123");
//		Assert.assertNotNull(user);
//		Assert.assertEquals(user.getEmail(), "123@123");
		Assert.assertNotNull(userHelper);
		Assert.assertNotNull(userHelper.getUserFromAuthentication());
		mvc.perform(get("/blog/new")).andExpect(status().isOk()).andExpect(view().name("addModifyBlog"));
		mvc.perform(post("/blog/new").param("title", "New Blog").param("content", "Blog Content").param("id", "0").with(csrf())).andExpect(redirectedUrl("/blog"));
		mvc.perform(post("/blog/new").param("title", "New Blog").param("content", "Blog Content").param("id", "0").with(csrf().useInvalidToken())).andExpect(status().is4xxClientError());
		mvc.perform(post("/blog/new").param("title", "New title").param("content", "").with(csrf())).andExpect(view().name("addModifyBlog"));
		mvc.perform(post("/blog/new").param("title", "").param("content", "New Content").with(csrf())).andExpect(view().name("addModifyBlog"));
		mvc.perform(post("/blog/1/modify").param("title", "New title").param("content", "New content").with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/blog/1"));
		mvc.perform(post("/blog/1/modify").param("title", "New title").param("content", "New content").with(csrf().useInvalidToken())).andExpect(status().is4xxClientError());
		mvc.perform(post("/blog/0/modify").param("title", "New title").param("content", "New content").with(csrf())).andExpect(status().isOk()).andExpect(view().name("errorPages/itemNotFound"));
	}

	@Test
	public void addCommentFailTest() throws Exception {
		mvc.perform(post("/blog/1/comment").param("content", "comment")).andExpect(status().is4xxClientError());
		mvc.perform(get("/blog/1/comment/delete")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	@WithMockUser(username = "123@123", password = "123456")
	public void addCommentTest() throws Exception {
		mvc.perform(post("/blog/1/comment").param("content", "comment").with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/blog/1"));
		mvc.perform(post("/blog/1/comment").param("content", "comment").with(csrf().useInvalidToken())).andExpect(status().is4xxClientError());
		mvc.perform(post("/blog/1/comment").param("content", "").with(csrf())).andExpect(status().isOk()).andExpect(view().name("blogDetail"));
		mvc.perform(get("/blog/1/comment/1/delete")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/blog/1"));
	}

	@Test
	public void userRegistrationTest() throws Exception {
//		User email already exists
		mvc.perform(post("/user").param("email", "123@123").param("password", "password").param("password2", "password").with(csrf())).andExpect(view().name("signUp"));

		mvc.perform(post("/user").param("email", "another@123").param("password", "password").param("password2", "password").with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/blog"));
		mvc.perform(post("/user").param("email", "another2@123").param("password", "password").param("password2", "password").with(csrf().useInvalidToken())).andExpect(status().is4xxClientError());
		mvc.perform(post("/user").param("email", "not a email").param("password", "password").param("password2", "password").with(csrf())).andExpect(status().isOk()).andExpect(view().name("signUp"));
		mvc.perform(post("/user").param("email", "another2@123").param("password", "pass").param("password2", "pass").with(csrf())).andExpect(status().isOk()).andExpect(view().name("signUp"));
		mvc.perform(post("/user").param("email", "another2@123").param("password", "passwordpasswordpasswordpassword").param("password2", "passwordpasswordpasswordpassword").with(csrf())).andExpect(status().isOk()).andExpect(view().name("signUp"));
		mvc.perform(post("/user").param("email", "another2@123").param("password", "password").param("password2", "difpassword").with(csrf())).andExpect(status().isOk()).andExpect(view().name("signUp"));
	}

	@Test
	public void changeUserPasswordFormShouldRedirectTest() throws Exception {
		mvc.perform(get("/user/settings/password")).andExpect(status().is3xxRedirection()).andExpect(view().name("http://localhost/login"));
	}

	@Test
	@WithMockUser(username = "123@123", password = "123456")
	public void changeUserPasswordTest() throws Exception {
		mvc.perform(post("/user/settings/password").param("oldPassword", "123456").param("newPassword", "654321").param("newPassword2", "654321").with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/settings"));
		mvc.perform(post("/user/settings/password").param("oldPassword", "123456").param("newPassword", "654321").param("newPassword2", "654321").with(csrf().useInvalidToken())).andExpect(status().is4xxClientError());
		mvc.perform(post("/user/settings/password").param("oldPassword", "wrongpassword").param("newPassword", "654321").param("newPassword2", "654321").with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/settings/password?error=old"));
		mvc.perform(post("/user/settings/password").param("oldPassword", "123456").param("newPassword", "short").param("newPassword2", "short").with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/settings/password?error=new"));
		mvc.perform(post("/user/settings/password").param("oldPassword", "123456").param("newPassword", "longlonglonglonglongpassword").param("newPassword2", "longlonglonglonglongpassword").with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/settings/password?error=new"));
		mvc.perform(post("/user/settings/password").param("oldPassword", "123456").param("newPassword", "654321").param("newPassword2", "different").with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/settings/password?error=confirm"));
	}
}
