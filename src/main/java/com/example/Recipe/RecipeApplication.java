package com.example.Recipe;

import com.example.Recipe.Models.Recipe;
import com.example.Recipe.Models.Result;
import com.example.Recipe.Models.Role;
import com.example.Recipe.Models.UserApp;
import com.example.Recipe.Repositories.RoleRepository;
import com.example.Recipe.Repositories.UserAppRepository;
import com.google.gson.Gson;
import org.ietf.jgss.Oid;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
//
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
import java.util.logging.Logger;


@SpringBootApplication
public class RecipeApplication {

	//private static final Logger log = (Logger) LoggerFactory.getLogger(RecipeApplication.class);

	public static void main(String[] args) throws IOException {
		SpringApplication.run(RecipeApplication.class, args);

		String dataJson = ReadFromAPI();
		WritOnDatabase();
	}

	/*
	When Run the App :
	 */
	/*
	@Bean
	CommandLineRunner initDatabase(RoleRepository roleRepository, UserAppRepository userAppRepository) {
		//Date date = new Date(1985,8,1);
		Calendar calendar = Calendar.getInstance();
		calendar.set(1985, 11, 31);
		Date date = (Date) calendar.getTime();
		return args -> {
			log.info("Preloading " + roleRepository.save(new Role("ADMIN")));
			log.info("Preloading " + roleRepository.save(new Role("CUSTOMER")));
			// encoder.encode(password)
			log.info("Preloading " + userAppRepository.save(new UserApp
					("admin jason", "1234", "jason", "jones", date, "amirican", "hi",
							new Role("ADMIN"))));
		};
	}
*/
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

//		// Get the Recipes Array
//		List<Recipe> RecipesArray = new ArrayList<>();
//		for (Recipe recipe :
//				RecipesResult.getResults()) {
//			RecipesArray.add(recipe);
//		}
		//////////////////////////////////////////////////////////////////////////////
		// Write the Array to the File :
		File localFile = new File("recipe.json");///////////////////////////// heek done terminal
		try (FileWriter localFileWriter = new FileWriter(localFile)) {
			//gson.toJson(RecipesArray, localFileWriter);
			gson.toJson(RecipesResult, localFileWriter); // Update here

		}
	}
	/*
	Write on the Database
	 */

	public static void WritOnDatabase() {
		//Creating a JSONParser object
		JSONParser jsonParser = new JSONParser();
		try {
			//Parsing the contents of the JSON file
			JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("recipe.json"));
			//Retrieving the array
			JSONArray jsonArray = (JSONArray) jsonObject.get("results");
			Connection con = Connect(); // Function down
			//Insert a row into the recipe table
			PreparedStatement pstmt = con.prepareStatement("INSERT INTO recipe values (?, ?,?, ?,?)");
			//Long id = 0L;
			for (Object object : jsonArray) {
				JSONObject record = (JSONObject) object;
				Long id = (Long)record.get("id");
				//id = id+1;
				String name = (String) record.get("name");
				String description = (String) record.get("description");
				String thumbnail_url = (String) record.get("thumbnail_url");
				String original_video_url = (String) record.get("original_video_url");

				pstmt.setLong(1, id);
				pstmt.setString(2, name);
				pstmt.setString(3, description);
				pstmt.setString(4, thumbnail_url);
				pstmt.setString(5, original_video_url);

				pstmt.executeUpdate();
			}
			System.out.println("Records inserted.....");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	Connect To Data base
	 */
	static public Connection Connect() {

		String url ="jdbc:postgresql://localhost:5432/recipedata";;
		String user = "alaa";
		String password = "0000";
		Connection con = null;
		try {
			con = DriverManager.getConnection(url, user, password);
			System.out.println("Connected to the PostgreSQL server successfully.");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return con;
	}


}



