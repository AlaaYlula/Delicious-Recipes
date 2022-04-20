package com.example.Recipe;
import com.example.Recipe.Models.*;

import com.example.Recipe.Models.Ingredient;
import com.example.Recipe.Recipe.*;
import com.example.Recipe.Repositories.*;

import com.github.javafaker.Faker;

import com.google.gson.Gson;
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

import org.slf4j.Logger;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.transaction.Transactional;

//@EnableAdminServer
@SpringBootApplication
public class RecipeApplication {


	private static final Logger log = LoggerFactory.getLogger(RecipeApplication.class);
	private final RecipeRepository recipeRepository;
	private final CommentRepository commentRepository;
	private final IngredientRepository ingredientRepository;
	private final InstructionRepository instructionRepository;
	private final UserAppRepository userAppRepository;
	private final RoleRepository roleRepository;

	public RecipeApplication(RecipeRepository recipeRepository, CommentRepository commentRepository, IngredientRepository ingredientRepository, InstructionRepository instructionRepository, UserAppRepository userAppRepository, RoleRepository roleRepository) {
		this.recipeRepository = recipeRepository;
		this.commentRepository = commentRepository;
		this.ingredientRepository = ingredientRepository;
		this.instructionRepository = instructionRepository;
		this.userAppRepository = userAppRepository;
		this.roleRepository = roleRepository;
	}

	public static void main(String[] args) throws IOException {
		SpringApplication.run(RecipeApplication.class, args);

	}

	@Transactional
	@Bean
	CommandLineRunner initDatabase(RecipeRepository recipeRepository, IngredientRepository ingredientRepository ,

								   InstructionRepository instructionRepository , UserAppRepository userAppRepository,RoleRepository roleRepository
			,CommentRepository commentRepository) {

		return args -> {
			// initiate the Role
			if (roleRepository.findAll().size() == 0 )
			{
				log.info("Preloading " + roleRepository.save(new Role("ADMIN")));
				log.info("Preloading " + roleRepository.save(new Role("USERS")));
			}
			// create an ADMIN
			if (userAppRepository.findByUsername("admin") == null) {
				Role role = roleRepository.getById(1L);
				String firstName = "admin";
				String lastName = "admin";
				String username = "admin";
				Date dateOfBirth = new Date(1991, 6, 15);
				String password = "1234";
				String nationality = "Jordanian";
				String bio = "Lorem ipsum dolor sit amet";
				String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
				UserApp admin = new UserApp(username, hashedPassword, firstName, lastName, dateOfBirth, nationality, bio);

				admin.setRole(role);

				userAppRepository.save(admin);
			}

			// Write On DATABASE
			if(recipeRepository.findAll().size() == 0)
			{

				Recipe recipe = ReadJsonFile("recipe.json");
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
			Add Maximum 6 users
			 */
			if(userAppRepository.findAll().size() < 6) {
				// Create 3 users
				for (int i = 0; i < 3; i++) {
					GetUser();
				}

			}
		};
	}
	////////////////////////////////////// Initiate  DATA ////////////////////////////////////////
	public void GetUser (){
		Role role = roleRepository.getById(2L);

		Faker faker = new Faker();
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
		//////
		SetData(user);

	}
	public void SetData(UserApp user){
		log.info("Preloading" + userAppRepository.save(user));

		// create recipe
		RecipeModel recipe = createRecipes(user);

		log.info("Preloading" + recipeRepository.save(recipe));

		// Create Instruction and Ingredient
		createInstructionAndIngredient(user,recipe);
		// Add comment For this recipe
		getComment(user,recipe);

		// Add Favorite Recipe For user
		SetFavRecipes(user,recipeRepository.findAll(),1);

		log.info("Preloading" + userAppRepository.save(user));
	}
	@Transactional
	public  RecipeModel createRecipes(UserApp user){
		Faker faker = new Faker();
		RecipeModel recipe = new RecipeModel();
		// For each Recipe
		String name = faker.food().dish();
		String description = faker.food().dish();
		recipe.setName(name);
		recipe.setDescription(description);

		return recipe;
	}
	public void createInstructionAndIngredient(UserApp user , RecipeModel recipe){
		Faker faker = new Faker();
		// For each Recipe

		// Ingredients
		String ingredient1 = faker.food().ingredient();
		Ingredient ingredientObj1 = new Ingredient(ingredient1);
		ingredientObj1.setRecipes_ingredient(recipe);
		ingredientRepository.save(ingredientObj1);
		List<Ingredient> ingredients = new ArrayList<>();
		ingredients.add(ingredientObj1);
		recipe.setIngredientModels(ingredients);


		// Instructions
		String instruction1 = faker.food().measurement();
		String instruction2 = faker.food().measurement();

		InstructionModel instructionObj1 = new InstructionModel(1,instruction1);
		instructionObj1.setRecipes_instruction(recipe);
		instructionRepository.save(instructionObj1);
		InstructionModel instructionObj2 = new InstructionModel(2,instruction2);
		instructionObj2.setRecipes_instruction(recipe);
		instructionRepository.save(instructionObj2);

		List<InstructionModel> instructions  = new ArrayList<>();
		instructions.add(instructionObj1);
		instructions.add(instructionObj2);
		recipe.setInstructionModels(instructions);

		recipe.setUserOwnRecipe(user);
	}
	//Write comments on the users Recipe
	public  void getComment(UserApp user,RecipeModel recipe) {
		Comment comment = new Comment("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
		comment.setRecipeComments(recipe);
		comment.setUserComments(user);
		commentRepository.save(comment);
		recipeRepository.save(recipe);


	}
	public  void SetFavRecipes(UserApp user , List<RecipeModel> recipes , int i) {

		RecipeModel recipe = recipes.get(i);

		List<RecipeModel> favList= new ArrayList<>();
		favList.add(recipe);
		user.setFavoriteRecipeModels(favList);
		userAppRepository.save(user);

		recipe.getUserFavRecipe().add(user);
		recipe.setUserFavRecipe(recipe.getUserFavRecipe());
		recipeRepository.save(recipe);

		getComment(user,recipe);

	}

	///////////////////////////////////////////// DATABASE //////////////////////////////////////////////////////

		// We used this at First To Get the data and write it on the File
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




