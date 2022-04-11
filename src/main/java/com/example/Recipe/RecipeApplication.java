package com.example.Recipe;

import com.example.Recipe.Models.*;
import com.example.Recipe.Repositories.*;
import com.google.gson.Gson;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.slf4j.Logger;


@SpringBootApplication
public class RecipeApplication {

	private static final Logger log = LoggerFactory.getLogger(RecipeApplication.class);


	public static void main(String[] args) throws IOException {
		SpringApplication.run(RecipeApplication.class, args);
		//String dataJson = ReadFromAPI();
		System.out.println(ReadJsonFile("recipe.json"));
	}

	/*
	When Run the App :
	 */

	@Bean
	CommandLineRunner initDatabase(RecipeRepository recipeRepository, InstructionRepository instructionRepository,
								   IngredientRepository ingredientRepository,SectionRepository sectionRepository,
								   RoleRepository roleRepository, UserAppRepository userAppRepository) {
		//Date date = new Date(1985,8,1);
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(1985, 11, 31);
//		Date date = (Date) calendar.getTime();
		return args -> {
			Result results = ReadJsonFile("recipe.json");
			for (Recipe recipe:// each recipe
					results.getResults()) {
				// Recipe
				Recipe recipe1 = new Recipe(recipe.getName(),recipe.getDescription(),recipe.getThumbnail_url(),recipe.getOriginal_video_url());
				if (recipe.getInstructions() != null) {
					//Each Instruction
					for (Instruction instruction :
							recipe.getInstructions()) {
						instructionRepository.save(instruction);
						System.out.println(instruction);
						System.out.println("//////////////////////");
					}
				}
				////// save instruction /////////
				recipe1.setInstructions(instructionRepository.findAll());
				///////////////
				if (recipe.getSections() != null) {
					// Each Section
					for (Section section :
							recipe.getSections()) {
						if(section.getComponents()!=null) {
							// Each Component
							for (Component component:
							 section.getComponents()){
								if(component.ingredient != null){
									// get Ingredient
									Ingredient ingredient = new Ingredient(component.ingredient.getName());
									ingredient.setComponent(component);
									ingredientRepository.save(component.ingredient);
								}
								component.setSection(section);
							}
							section.setRecipeSections(recipe1);
						}

					}
				}
				recipe1.setSections(sectionRepository.findAll());
				recipeRepository.save(recipe1);
			}
				System.out.println("Records inserted.....");

//			log.info("Preloading " + roleRepository.save(new Role("ADMIN")));
//			log.info("Preloading " + roleRepository.save(new Role("CUSTOMER")));
//			// encoder.encode(password)
//			log.info("Preloading " + userAppRepository.save(new UserApp
//					("admin jason", "1234", "jason", "jones", date, "amirican", "hi",
//							new Role("ADMIN"))));
		};
	}

	 /*
	 Method Make the connection and get the data as json Format And called Method WriteOnJsonFile
    */

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
		Result RecipesResult = gson.fromJson(dataJson, Result.class);
		// Write the Array to the File :
		File localFile = new File("recipe.json");
		try (FileWriter localFileWriter = new FileWriter(localFile)) {
			gson.toJson(RecipesResult, localFileWriter);
		}
	}

	/*
	Read Json File , Return the Recipes
	 */
	public static Result ReadJsonFile(String filename) {
		FileReader filereader = null;
		try {
			filereader = new FileReader(filename);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		Gson gson = new Gson();
		Result results = gson.fromJson(filereader, Result.class); // all recipes

		return results;
	}
}






