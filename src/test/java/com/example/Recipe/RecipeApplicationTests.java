package com.example.Recipe;

import com.example.Recipe.Controller.MainController;
import com.example.Recipe.Controller.RecipeController;
import com.example.Recipe.Controller.UserController;

import com.example.Recipe.Repositories.RecipeRepository;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@ContextConfiguration(classes= Application.class)
@SpringBootTest
class RecipeApplicationTests {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webapplicationContext;

	@Autowired
	RecipeController recipeController;
	@Autowired
	MainController mainController;

	@Autowired
	RecipeRepository recipeRepository;
	@Autowired
	UserController userController;

	@Test
	void contextLoads() {
	}

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webapplicationContext).apply(springSecurity()).build();
	}
	/*
	in order to make the test pass need to use @WithMockUser("spring") with logged-in user and have data in it
	 */

	///////////////////////////////////////////////// Main controller testing /////////////////////////////////////////////////
	@Test
	@DisplayName("log in page test") //pass
	public void testLoginPage() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/login")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("login"));


	}

	@Test
	@DisplayName("sign up page test") // pass
	public void testSignupPage() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/signup")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("signup"));


	}

	@WithMockUser("admin")
	@Test
	@DisplayName("Root page test") // pass
	public void testRootPage() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("home"))
				.andExpect(content().string(Matchers.containsString("Delicious Recipes")));
	}

	///////////////////////////////////////////////// User controller testing /////////////////////////////////////////////////

	@WithMockUser("admin")
	@Test
	@DisplayName("users page test")  //pass
	public void testUserPage() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/users"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("users"));
	}

	@WithMockUser("admin")
	@Test
	@DisplayName("user recipe page test") //pass
	public void testUserRecipePage() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/user/recipes"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("userRecipe"));
	}

	@WithMockUser("admin")
	@Test
	@DisplayName("User My Profile page test")  //pass
	public void testMyProfilePage() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/myprofile"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("myprofile"));
	}

	@WithMockUser("admin")
	@Test
	@DisplayName(" post new recipe test")  //pass
	public void testUserNewRecipe() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/user/newRecipe").param("name","flafel").param("description","flafel").param("ingredientModels","ing").param("instructionModels","ins"))
				.andDo(print())
				.andExpect(status().is(302));

	}

	@WithMockUser("admin")
	@Test
	@DisplayName(" post delete own recipe test")  //pass
	public void testDeleteRecipe() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/recipe/delete").param("userRecipe_id","48"))
				.andDo(print())
				.andExpect(status().is(302));

	}

	@WithMockUser("admin")
	@Test
	@DisplayName(" post follow user test")  //pass
	public void testUsersFollow() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/users/follow").param("user_id","6"))
				.andDo(print())
				.andExpect(status().is(302));
	}

	@WithMockUser("admin")
	@Test
	@DisplayName(" post unfollow users test")  //pass
	public void testUsersUnFollow() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/users/unfollow").param("user_id","6"))
				.andDo(print())
				.andExpect(status().is(302));
	}

	@WithMockUser("admin")
	@Test
	@DisplayName(" post delete from favorite list User rout test")  //pass
	public void testDeleteFavorite() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/recipe/favorite/delete").param("Recipe_id","4"))
				.andDo(print())
				.andExpect(status().is(302));
	}



	@WithMockUser("admin")
	@Test
	@DisplayName("User account  page test")  //pass
	public void testAccountPage() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/user/account/{id}",1)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("myprofile"));
	}



	@WithMockUser("admin")
	@Test
	@DisplayName("User followers  page test")  //pass
	public void testFollowers() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/followers"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("users"));
	}


	@WithMockUser("admin")
	@Test
	@DisplayName("User following  page test")  //pass
	public void testFollowing() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/following"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("users"));
	}

	@WithMockUser("admin")
	@Test
	@DisplayName("User update recipe page test")  //pass
	public void testUpdatePost() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/recipe/update").param("recipe_id","48").param("name","Ali").param("description","Ali").param("ingredientModels","ing").param("instructionModels","ins"))
				.andDo(print())
				.andExpect(status().is(302));
	}


	///////////////////////////////////////////////// Recipe controller testing /////////////////////////////////////////////////

	@WithMockUser("admin")
	@Test
	@DisplayName("recipe page test")   // pass
	public void testRecipePage() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/recipe").param("id", "10"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("recipeinfo"));
	}

	// the mockUser must have comment to make the test pass else will fail
	@WithMockUser("admin")
	@Test
	@DisplayName("delete comment test")   // pass
	public void testDeleteRoute() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/deletecomment").param("id", "5").param("rid","10").param("cUid","3"))
				.andDo(print())
				.andExpect(status().is(302));
	}

	// the mockUser must log in to see the result
	@WithMockUser("admin")
	@Test
	@DisplayName("Add comment test")   // pass
	public void testAddRoute() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/recipe/comment").param("text", "hello world").param("id","10"))
				.andDo(print())
				.andExpect(status().is(302));
	}

	// the mockUser must log in to see the result
	@WithMockUser("admin")
	@Test
	@DisplayName("Add to favorite test")   // pass
	public void testFavoriteRoute() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/favorite").param("id", "4"))
				.andDo(print())
				.andExpect(status().is(302));
	}



}