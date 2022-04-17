package com.example.Recipe;
import com.example.Recipe.Models.Ingredient;
import com.example.Recipe.Models.InstructionModel;
import com.example.Recipe.Models.RecipeModel;

import com.example.Recipe.Models.UserApp;

import com.example.Recipe.Models.Role;

import com.example.Recipe.Recipe.*;
import com.example.Recipe.Repositories.IngredientRepository;
import com.example.Recipe.Repositories.InstructionRepository;
import com.example.Recipe.Repositories.RecipeRepository;

import com.example.Recipe.Repositories.UserAppRepository;
import com.github.javafaker.Faker;

import com.example.Recipe.Repositories.RoleRepository;

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
import com.github.javafaker.Faker;
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
								   ) {

		return args -> {
			if (roleRepository.findAll().size() == 0)
			{
				log.info("Preloading " + roleRepository.save(new Role("ADMIN")));
				log.info("Preloading " + roleRepository.save(new Role("USERS")));

//				Role role = roleRepository.getById(1L);
//				UserApp admin = new UserApp("admin","admin","admin","admin",'3891-07-15',"admin","admin");
//				admin.setRole(role);

			}
			if(recipeRepository.findAll().size() == 0)
			{
				Recipe recipe = ReadJsonFile("recipe.json");
				System.out.println(recipe);

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


			if(userAppRepository.findAll().size() < 8) {



				/*              **************************************                          */

				Faker faker = new Faker();
				String firstName = faker.name().firstName();
				String lastName = faker.name().lastName();
				String username = faker.name().username();
				Date dateOfBirth = new Date(1991, 6, 15);
				String password = "1234";
				String nationality = "Jordanian";
				String bio = "Hello welcome 1";
				String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
				UserApp user1 = new UserApp(username, hashedPassword, firstName, lastName, dateOfBirth, nationality, bio);
				Role role = roleRepository.getById(2L);
				user1.setRole(role);
				///////////////////////////////////////////////
				String firstName2 = faker.name().firstName();
				String lastName2 = faker.name().lastName();
				String username2 = faker.name().username();
				Date dateOfBirth2 = new Date(1991, 6, 15);
				String password2 = "1234";
				String nationality2 = "Jordanian";
				String bio2 = "Hello welcome 2";
				String hashedPassword2 = BCrypt.hashpw(password, BCrypt.gensalt(12));
				UserApp user2 = new UserApp(username2, hashedPassword2, firstName2, lastName2, dateOfBirth2, nationality2, bio2);
				user2.setRole(role);



				String firstName3 = faker.name().firstName();
				String lastName3 = faker.name().lastName();
				String username3 = faker.name().username();
				Date dateOfBirth3 = new Date(1991, 6, 15);
//				String image3 = faker.avatar().image();
//				System.out.println("*********************************************"+image3);
				String password3 = "1234";
				String nationality3 = "Jordanian";
				String bio3 = "Hello welcome 3";
				String hashedPassword3 = BCrypt.hashpw(password, BCrypt.gensalt(12));
				UserApp user3 = new UserApp(username3, hashedPassword3, firstName3, lastName3, dateOfBirth3, nationality3, bio3);
				user3.setRole(role);


				///////////////////////////////////////
				List<UserApp> user1Following = new ArrayList<>();
				user1Following.add(user2);
				user1.setFollowing(user1Following);
				List<UserApp> user2Followers = new ArrayList<>();
				user2Followers.add(user1);
				user2.setFollowers(user2Followers);
				log.info("Preloading" + userAppRepository.save(user1));
				log.info("Preloading" + userAppRepository.save(user2));
				log.info("Preloading" + userAppRepository.save(user3));
///////////////////////////////////////
			}
		};
	}


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






