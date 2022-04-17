package com.example.Recipe;
import com.example.Recipe.Models.*;

import com.example.Recipe.Models.Ingredient;
import com.example.Recipe.Recipe.*;
import com.example.Recipe.Repositories.*;

import com.github.javafaker.Faker;

import com.google.gson.Gson;
import org.apache.catalina.User;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.security.crypto.bcrypt.BCrypt;

//@EnableAdminServer
@SpringBootApplication
public class RecipeApplication {


	private static final Logger log = LoggerFactory.getLogger(RecipeApplication.class);


	public static void main(String[] args) throws IOException {
		SpringApplication.run(RecipeApplication.class, args);

	}


	@Bean
	CommandLineRunner initDatabase(RecipeRepository recipeRepository, IngredientRepository ingredientRepository ,

								   InstructionRepository instructionRepository , UserAppRepository userAppRepository,RoleRepository roleRepository
								   ,CommentRepository commentRepository) {

		return args -> {
			if (roleRepository.findAll().size() == 0)
			{
				log.info("Preloading " + roleRepository.save(new Role("ADMIN")));
				log.info("Preloading " + roleRepository.save(new Role("USERS")));
			}
			if(recipeRepository.findAll().size() == 0)
			{
				Recipe recipe = ReadJsonFile("recipe.json");
				//System.out.println(recipe);

				for (Result r:recipe.results
				) {
					RecipeModel recipeModel = new RecipeModel(r.name,r.description,r.thumbnail_url);
					recipeRepository.save(recipeModel);

					if(r.instructions != null){
						for (Instruction i: r.instructions
						) {
							InstructionModel instructionModel = new InstructionModel(i.position,i.display_text);
							instructionModel.setRecipes_instruction(recipeModel);
							instructionRepository.save(instructionModel);

						}
					}
					if(r.sections !=null){
						for (Sections s:r.sections
						) {
							if(s.components != null){
								for (Component c:s.components
								) {
									com.example.Recipe.Models.Ingredient ingredient = new Ingredient(c.ingredient.name);
									ingredient.setRecipes_ingredient(recipeModel);
									ingredientRepository.save(ingredient);

								}
							}

						}
					}

				}

			}
			/*
			Add 8 users
			 */
			if(userAppRepository.findAll().size() < 6) {

				Role role = roleRepository.getById(2L);
//				List<UserApp> users = getUsers(role);

				Faker faker = new Faker();
				////////////////// USER 1 /////////////////////////////
				String firstName = faker.name().firstName();
				String lastName = faker.name().lastName();
				String username = faker.name().username();
				Date dateOfBirth = new Date(1991, 6, 15);
				String password = "1234";
				String nationality = "Jordanian";
				String bio = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incid";
				String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
				UserApp user1 = new UserApp(username, hashedPassword, firstName, lastName, dateOfBirth, nationality, bio);

				user1.setRole(role);
				// create 3 own recipes
				List<RecipeModel> recipes = createRecipes(user1);
				user1.setOwnRecipes(recipes);


//				List<RecipeModel> favList = SetFavRecipes(user1,recipeRepository.findAll());
//				for (RecipeModel re:
//					 favList) {
//					recipeRepository.save(re);
//				}
				////////////////// USER 2 /////////////////////////////
				String firstName2 = faker.name().firstName();
				String lastName2 = faker.name().lastName();
				String username2 = faker.name().username();
				Date dateOfBirth2 = new Date(1991, 6, 15);
				String password2 = "1234";
				String nationality2 = "Jordanian";
				String bio2 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incid";
				String hashedPassword2 = BCrypt.hashpw(password, BCrypt.gensalt(12));
				UserApp user2 = new UserApp(username2, hashedPassword2, firstName2, lastName2, dateOfBirth2, nationality2, bio2);
				user2.setRole(role);

				// create 3 own recipes
				List<RecipeModel> recipes2 = createRecipes(user2);
				user2.setOwnRecipes(recipes2);

				////////////////// USER 3 /////////////////////////////

				String firstName3 = faker.name().firstName();
				String lastName3 = faker.name().lastName();
				String username3 = faker.name().username();
				Date dateOfBirth3 = new Date(1991, 6, 15);
//				String image3 = faker.avatar().image();
//				System.out.println("*********************************************"+image3);
				String password3 = "1234";
				String nationality3 = "Jordanian";
				String bio3 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incid";
				String hashedPassword3 = BCrypt.hashpw(password, BCrypt.gensalt(12));
				UserApp user3 = new UserApp(username3, hashedPassword3, firstName3, lastName3, dateOfBirth3, nationality3, bio3);
				user3.setRole(role);

				// create 3 own recipes
				List<RecipeModel> recipes3 = createRecipes(user3);
				user3.setOwnRecipes(recipes3);

				///////////////// Follow ////////////////////
				List<UserApp> user1Following = new ArrayList<>();
				user1Following.add(user2);
				user1.setFollowing(user1Following);
				List<UserApp> user2Followers = new ArrayList<>();
				user2Followers.add(user1);
				user2.setFollowers(user2Followers);
				///////////////////////////////////////
				// Add Comment to Recipe
//				List<Comment> comments = getComments(user1,recipeRepository.getById(1));
//				for (Comment comment:
//					 comments) {
//					commentRepository.save(comment);
//				}
//				List<Comment> comments2 = getComments(user2,recipeRepository.getById(2));
//				for (Comment comment:
//						comments2) {
//					commentRepository.save(comment);
//				}

				log.info("Preloading" + userAppRepository.save(user1));
				log.info("Preloading" + userAppRepository.save(user2));
				log.info("Preloading" + userAppRepository.save(user3));


///////////////////////////////////////
			}
		};
	}

	public static List<UserApp> getUsers(Role role){
		List<UserApp> users = new ArrayList<>();
		Faker faker = new Faker();

		for (int i = 0; i < 3; i++) {
			String firstName = faker.name().firstName();
			String lastName = faker.name().lastName();
			String username = faker.name().username();
			Date dateOfBirth = new Date(1991, 6, 15);
			String password = "1234";
			String nationality = "Jordanian";
			String bio = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incid";
			String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

			UserApp user = new UserApp(username, hashedPassword, firstName, lastName, dateOfBirth, nationality, bio);

			user.setRole(role);
			// create 3 own recipes
			List<RecipeModel> recipes = createRecipes(user);
			user.setOwnRecipes(recipes);

		}
		return users;
	}
	public static List<RecipeModel> createRecipes(UserApp user){
		Faker faker = new Faker();
		List<RecipeModel> recipes = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			RecipeModel recipe = new RecipeModel();
			// For each Recipe
			String name = faker.food().dish();
			String description = faker.food().dish();
			// Ingredients
			String ingredient1 = faker.food().ingredient();
			String ingredient2= faker.food().ingredient();
			String ingredient3 = faker.food().ingredient();

			Ingredient ingredientObj1 = new Ingredient(ingredient1);
			ingredientObj1.setRecipes_ingredient(recipe);
			Ingredient ingredientObj2 = new Ingredient(ingredient2);
			ingredientObj1.setRecipes_ingredient(recipe);
			Ingredient ingredientObj3 = new Ingredient(ingredient3);
			ingredientObj1.setRecipes_ingredient(recipe);

			List<Ingredient> ingredients = new ArrayList<>();
			ingredients.add(ingredientObj1);
			ingredients.add(ingredientObj2);
			ingredients.add(ingredientObj3);
			// Instructions
			String instruction1 = faker.food().measurement();
			String instruction2 = faker.food().measurement();

			InstructionModel instructionObj1 = new InstructionModel(1,instruction1);
			instructionObj1.setRecipes_instruction(recipe);
			InstructionModel instructionObj2 = new InstructionModel(2,instruction2);
			instructionObj2.setRecipes_instruction(recipe);

			List<InstructionModel> instructions  = new ArrayList<>();
			instructions.add(instructionObj1);
			instructions.add(instructionObj2);

			List<Comment> comments = getComments(user,recipe);
			recipe.setComments(comments);

			recipe.setName(name);
			recipe.setDescription(description);
			recipe.setIngredientModels(ingredients);
			recipe.setInstructionModels(instructions);

			recipe.setUserOwnRecipe(user);

			recipes.add(recipe);
		}
		return recipes;
	}
	//Write comments on the users Recipe
	public static List<Comment> getComments(UserApp user,RecipeModel recipe) {
		Faker faker = new Faker();
		List<Comment> comments = new ArrayList<>();
		Comment comment = new Comment("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
		comment.setRecipeComments(recipe);
		comment.setUserComments(user);
		for (int i = 0; i < 3; i++) {
			comments.add(comment);
		}
	return comments;
	}
	public static List<RecipeModel> SetFavRecipes(UserApp user , List<RecipeModel> recipes) {
		List<RecipeModel> recipesFavResult = recipes.stream().limit(3).collect(Collectors.toList());
		user.setFavoriteRecipeModels(recipesFavResult);
		for (RecipeModel recipe:
			 recipesFavResult) {
			recipe.getUserFavRecipe().add(user);
			recipe.setUserFavRecipe(recipe.getUserFavRecipe());
		}
		return recipesFavResult;
	}
	///////////////////////////////////////////// DATABASE //////////////////////////////////////////////////////
		public static String ReadFromAPI() throws FileNotFoundException {
		String dataJson = "";
		try {
			// Make connection
			URL recipesURL = new URL("https://tasty.p.rapidapi.com/recipes/list?rapidapi-key=e9759040abmsh24f8c728a714d78p1bff50jsncd148a5fba3b&from=0&size=100");

			HttpURLConnection recipeURLConnection = (HttpURLConnection) recipesURL.openConnection();
			// send GET request
			recipeURLConnection.setRequestMethod("GET");
			recipeURLConnection.connect(); // added
			// Read the Response
			InputStreamReader recipeURLReader = new InputStreamReader(recipeURLConnection.getInputStream());
			BufferedReader recipeURLBuffered = new BufferedReader(recipeURLReader);
			// Get the data
			dataJson = recipeURLBuffered.readLine();
			WriteOnJSonFile(dataJson);

		} catch (Exception e) {
			// Read From File If OFFLINE //
		}
		return dataJson;
	}
	/*
       Write the Recipes on the File
  */
	public static void WriteOnJSonFile(String dataJson) throws IOException {
		//  Convert To Object From JSON Format
		Gson gson = new Gson();
		//Get the results Object
		RecipeModel recipesResult = gson.fromJson(dataJson, RecipeModel.class);
		// Write the Array to the File :
		File localFile = new File("recipe.json");
		try (FileWriter localFileWriter = new FileWriter(localFile)) {
			gson.toJson(recipesResult, localFileWriter);
		}
	}
	/*
  Read Json File , Return the Recipes
   */
	public static Recipe ReadJsonFile(String filename) {
		FileReader filereader = null;
		try {
			filereader = new FileReader(filename);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		Gson gson = new Gson();
		Recipe results = gson.fromJson(filereader, Recipe.class); // all recipes

		return results;
	}
}






