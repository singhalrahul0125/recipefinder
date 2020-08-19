package com.recipefinder.service;

import com.recipefinder.client.model.RecipeResult;
import com.recipefinder.client.model.Result;
import com.recipefinder.client.model.SearchResponse;
import com.recipefinder.persistence.model.SavedRecipe;
import com.recipefinder.persistence.model.UserAccount;
import com.recipefinder.persistence.respository.SavedRecipeRepository;
import com.recipefinder.persistence.respository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class RecipeFinderService {

    final RestTemplate restTemplate;
    final UserAccountRepository userAccountRepository;
    final SavedRecipeRepository savedRecipeRepository;
    @Value("${sourceApi.apikey}")
    private String apiKey;
    @Value("${sourceApi.url.searchrecipe}")
    private String searchRecipeUrl;

    @Autowired
    public RecipeFinderService(RestTemplate restTemplate, UserAccountRepository userAccountRepository,
                               SavedRecipeRepository savedRecipeRepository) {
        this.restTemplate = restTemplate;
        this.userAccountRepository = userAccountRepository;
        this.savedRecipeRepository = savedRecipeRepository;
    }

    public List<RecipeResult> findRecipe(String recipeName) {
        String url = addApiKeyToUrl(searchRecipeUrl, recipeName);
        ResponseEntity<SearchResponse> responseEntity = restTemplate.getForEntity(url, SearchResponse.class);
        SearchResponse response = responseEntity.getBody();
        List<RecipeResult> recipeResultList = new ArrayList<>();

        if (response.getResults().size() == 0) {
            throw new IllegalArgumentException("Recipe with this name doesn't exist");
        }

        for (int i = 0; i < response.getResults().size(); i++) {
            Result result = response.getResults().get(i);
            RecipeResult recipeResult = new RecipeResult(result.getTitle(), result.getSourceUrl(),
                    result.getReadyInMinutes());
            recipeResultList.add(recipeResult);
        }
        return recipeResultList;
    }

    private String addApiKeyToUrl(String url, String recipeName) {
        return url + "?apiKey=" + apiKey + "&query=" + recipeName + "&number=5";
    }


    @Transactional(value = Transactional.TxType.REQUIRED)
    public String createUserAccount(String userName, String password) {
        List<UserAccount> userAccounts = userAccountRepository.findByUsername(userName.toUpperCase());

        if (userAccounts != null && userAccounts.size() == 0) {
            UserAccount account = new UserAccount();
            account.setUsername(userName.toUpperCase());
            account.setPassword(password);
            account.setApiKey(UUID.randomUUID().toString());
            userAccountRepository.save(account);
            return account.getApiKey();

        } else {
            throw new IllegalArgumentException("User already exists!");
        }

    }

    public String saveRecipes(String apiKey, String recipeName, String recipeURL, Integer rating) {
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Rating should be between 0-5. Recipe is not saved.");
        }
        List<UserAccount> userAccount = userAccountRepository.findByapiKey(apiKey);
        if (userAccount != null && userAccount.size() == 0) {
            throw new IllegalArgumentException("ApiKey doesn't exist or Invalid");
        } else {
            SavedRecipe savedRecipe = new SavedRecipe();
            savedRecipe.setRating(rating);
            savedRecipe.setRecipeName(recipeName);
            savedRecipe.setRecipeURL(recipeURL);
            savedRecipe.setUserAccount(userAccount.get(0));
            savedRecipeRepository.save(savedRecipe);
            return "Recipe Saved";
        }
    }


    public List<UserRecipe> getUserRecipe(String apikey) {
        List<UserAccount> userAccounts = userAccountRepository.findByapiKey(apikey);
        if (userAccounts.size() == 0) {
            throw new IllegalArgumentException("Error: apiKey not correct or doesn't exist");
        } else {
            List<SavedRecipe> userSavedRecipes = savedRecipeRepository.findByUserAccount(userAccounts.get(0));
            List<UserRecipe> userRecipesList = new ArrayList<>();

            for (int i = 0; i < userSavedRecipes.size(); i++) {

                UserRecipe userRecipe = new UserRecipe();
                userRecipe.setRating(userSavedRecipes.get(i).getRating());
                userRecipe.setRecipeName(userSavedRecipes.get(i).getRecipeName());
                userRecipe.setRecipeUrl(userSavedRecipes.get(i).getRecipeURL());
                userRecipesList.add(userRecipe);
            }
            return userRecipesList;
        }
    }

    public String recoverApiKey(String userName, String password) {
        List<UserAccount> userAccounts = userAccountRepository.findByUsername(userName.toUpperCase());

        if (userAccounts.size() == 0) {
            throw new IllegalArgumentException("Invalid username or password!");
        } else if (userAccounts.get(0).getPassword().equals(password)) {
            return userAccounts.get(0).getApiKey();
        } else {
            throw new IllegalArgumentException("Invalid username or password!");
        }
    }
}
