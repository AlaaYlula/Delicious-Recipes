package com.example.Recipe;

import com.example.Recipe.Models.*;
import com.example.Recipe.Repositories.InstructionRepository;
import com.example.Recipe.Repositories.RecipeRepository;
import com.example.Recipe.Repositories.RoleRepository;
import com.example.Recipe.Repositories.UserAppRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.ietf.jgss.Oid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Text;


//
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;

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

//	@Bean
//	CommandLineRunner initDatabase(RecipeRepository recipeRepository, InstructionRepository instructionRepository, RoleRepository roleRepository, UserAppRepository userAppRepository) {
//		//Date date = new Date(1985,8,1);
////		Calendar calendar = Calendar.getInstance();
////		calendar.set(1985, 11, 31);
////		Date date = (Date) calendar.getTime();
//		return args -> {
//             //Creating a JSONParser object
//			JSONParser jsonParser = new JSONParser();
//			try {
//				//Parsing the contents of the JSON file
//				JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("recipe.json"));
//				//Retrieving the array
//				JSONArray jsonArray = (JSONArray) jsonObject.get("results");
//
//				for (Object recipe : jsonArray) {
//					JSONObject record = (JSONObject) recipe;
//					//System.out.println(record);
//					System.out.println("****************************");
//					String name = (String) record.get("name");
//					String description = (String) record.get("description");
//					String thumbnail_url = (String) record.get("thumbnail_url");
//					String original_video_url = (String) record.get("original_video_url");
//					List<Instruction> instructionList =(List)record.get("instructions");
////					if(instructionList !=null) {
////						for (Instruction ins :
////								instructionList) {
////							System.out.println(ins);
////						}
////					}
//					Recipe recipeObj = new Recipe(name,description,thumbnail_url,original_video_url,instructionList);
//					//System.out.println(recipeObj);
//					//log.info("Preloading " + recipeRepository.save(recipeObj));
////					for (Instruction in :
////							instructionList) {
////						Instruction instruction = new Instruction(in.getPosition(),in.getDisplay_text());
////						instruction.setRecipe(recipeObj);
////						log.info("Preloading " + instructionRepository.save(instruction));
////
////					}
//				}
//				System.out.println("Records inserted.....");
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (ParseException e) {
//				e.printStackTrace();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
////			log.info("Preloading " + roleRepository.save(new Role("ADMIN")));
////			log.info("Preloading " + roleRepository.save(new Role("CUSTOMER")));
////			// encoder.encode(password)
////			log.info("Preloading " + userAppRepository.save(new UserApp
////					("admin jason", "1234", "jason", "jones", date, "amirican", "hi",
////							new Role("ADMIN"))));
//		};
//	}

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
		//System.out.println(RecipesResult);
		//////////////////////////////////////////////////////////////////////////////
		// Write the Array to the File :
		File localFile = new File("recipe.json");///////////////////////////// heek done terminal
		try (FileWriter localFileWriter = new FileWriter(localFile)) {
			//gson.toJson(RecipesArray, localFileWriter);
			gson.toJson(RecipesResult, localFileWriter); // Update here

		}
	}

	public static Result ReadJsonFile(String filename) { ///////////////////////////////////// Read Json File
		FileReader filereader = null;

		try {
			filereader = new FileReader(filename);

		} catch (IOException exception) {
			exception.printStackTrace();
		}
		Gson gson = new Gson();
		Result results = gson.fromJson(filereader, Result.class); // all recipes
		for (Recipe recipe:// each recipe
			 results.getResults()) {
			for (Section section:
				 recipe.getSections()) {
				System.out.println(section);
				System.out.println("////////////////////////");
			}
		}

		return results;
	}
	}






