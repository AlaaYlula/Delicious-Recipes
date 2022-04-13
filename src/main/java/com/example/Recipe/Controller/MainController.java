package com.example.Recipe.Controller;

import com.example.Recipe.Models.Ingredient;
import com.example.Recipe.Models.InstructionModel;
import com.example.Recipe.Models.RecipeModel;
import com.example.Recipe.Models.UserApp;
import com.example.Recipe.Recipe.*;
import com.example.Recipe.Repositories.IngredientRepository;
import com.example.Recipe.Repositories.InstructionRepository;
import com.example.Recipe.Repositories.RecipeRepository;
import com.example.Recipe.Repositories.UserAppRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;

@Controller
public class MainController {


    @Autowired
    private PasswordEncoder passwordEncoder;
    private final UserAppRepository userAppRepository;
    private final InstructionRepository instructionRepository;
   private final RecipeRepository recipeRepository;
   private final IngredientRepository ingredientRepository;

    public MainController(UserAppRepository userAppRepository,
                          InstructionRepository instructionRepository,
                          RecipeRepository recipeRepository,
                          IngredientRepository ingredientRepository) {
        this.userAppRepository = userAppRepository;
        this.instructionRepository = instructionRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }


    @GetMapping("/signup")
    public String signupPage(){
        return "signup";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

//    @GetMapping("/")
//    public String getMainPage(){
//        return "index";
//    }

    @PostMapping("/signup")
    public String getSignupPage(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String firstname,
            @RequestParam String lastname,
            @RequestParam Date dateOfBirth,
            @RequestParam String nationality,
            @RequestParam String bio
    ){

        UserApp userApp = new UserApp(username,passwordEncoder.encode(password),firstname,lastname,dateOfBirth,nationality,bio);
        userAppRepository.save(userApp);
        return "login";
    }



    @GetMapping("/")
    public String getHomePage(){

        if(recipeRepository.findAll().size() == 0){
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
                                Ingredient ingredient = new Ingredient(c.ingredient.name);
                                ingredient.setRecipes_ingredient(recipeModel);
                                ingredientRepository.save(ingredient);

                            }
                        }

                    }
                }

            }
        }


        return "index";
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

