package com.recipefinder.service;

import com.recipefinder.client.model.RecipeResult;
import com.recipefinder.client.model.Result;
import com.recipefinder.client.model.SearchResponse;
import com.recipefinder.persistence.model.SavedRecipe;
import com.recipefinder.persistence.model.UserAccount;
import com.recipefinder.persistence.respository.SavedRecipeRepository;
import com.recipefinder.persistence.respository.UserAccountRepository;
import org.apache.catalina.User;
import org.apache.http.protocol.HTTP;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RecipeFinderServiceTest {

    RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
    UserAccountRepository userAccountRepository = Mockito.mock(UserAccountRepository.class);
    SavedRecipeRepository savedRecipeRepository= Mockito.mock(SavedRecipeRepository.class);
    ResponseEntity<Object> responseEntity = Mockito.mock(ResponseEntity.class);

    RecipeFinderService recipeFinderService = new RecipeFinderService(restTemplate, userAccountRepository, savedRecipeRepository);

    @Test
    void testRecoverApiKeyWhenUsernameDoesNotMatchesThrowsException(){
        List<UserAccount> userAccounts = new ArrayList<>();
        String username = "ABC";
        Mockito.when(userAccountRepository.findByUsername(username)).thenReturn(userAccounts);
        try{
            recipeFinderService.recoverApiKey(username, "123");
            Assertions.failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        }
        catch (IllegalArgumentException e){
            assertThat(e.getMessage()).isEqualTo("Invalid username or password!");
        }
    }


    @Test
    void testRecoverApiKeyWhenPasswordMatchesThenReturnsApiKey() {
        String password = "123";
        String apiKey = "API123";
        String username = "ABC";
        UserAccount userAccount = new UserAccount();
        userAccount.setPassword(password);
        userAccount.setApiKey(apiKey);
        List<UserAccount> userAccounts = Collections.singletonList(userAccount);
        Mockito.when(userAccountRepository.findByUsername(username)).thenReturn(userAccounts);

        String actualApiKey = recipeFinderService.recoverApiKey(username, password);
        assertThat(actualApiKey).isEqualTo(apiKey);
    }

    @Test
    void testRecoverApiKeyWhenPasswordDoesNotMatchesThrowsException() {
        String password = "123";
        String apiKey = "API123";
        String username = "ABC";
        UserAccount userAccount = new UserAccount();
        userAccount.setPassword("365");
        userAccount.setApiKey(apiKey);
        List<UserAccount> userAccounts = Collections.singletonList(userAccount);
        Mockito.when(userAccountRepository.findByUsername(username)).thenReturn(userAccounts);

        try{
            recipeFinderService.recoverApiKey(username, password);
            Assertions.failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        }
        catch (IllegalArgumentException e){
            assertThat(e.getMessage()).isEqualTo("Invalid username or password!");
        }
    }

    @Test
    void testCreateUserAccountWhenUserAccountDoesNotExistThenCreatesUserAccount() {
        String username = "Rahul";
        String password = "123456";
        List<UserAccount> userAccounts = new ArrayList<>();
        UserAccount account = new UserAccount();

        Mockito.when(userAccountRepository.findByUsername(username)).thenReturn(userAccounts);
        Mockito.when(userAccountRepository.save(Mockito.any())).thenReturn(account);

        String apiKey = recipeFinderService.createUserAccount(username, password);
        assertThat(apiKey).isNotBlank();
    }

    @Test
    void testCreateUserAccountWhenUserAccountExistThenThrowException() {
        String username = "Rahul";
        String password = "123456";
        UserAccount account = new UserAccount();
        account.setUsername(username);
        account.setPassword(password);
        List<UserAccount> userAccounts = Collections.singletonList(account);

        Mockito.when(userAccountRepository.findByUsername(username.toUpperCase())).thenReturn(userAccounts);

        try{
            recipeFinderService.createUserAccount(username,password);
            Assertions.failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        }
        catch (IllegalArgumentException e){
            assertThat(e.getMessage()).isEqualTo("User already exists!");
        }
    }

    @Test
    void testSaveRecipesWhenApiKeyIsNotValidThenThrowsException(){
        String apiKey = "Anything";
        String recipeName = "AnyName";
        String recipeUrl = "AnyUrl";
        Integer rating =  100;
        List<UserAccount> userAccount = new ArrayList<>();

        Mockito.when(userAccountRepository.findByapiKey(Mockito.any())).thenReturn(userAccount);

        try{
            recipeFinderService.saveRecipes(apiKey,recipeName,recipeUrl,rating);
            Assertions.failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        }
        catch (IllegalArgumentException e){
            assertThat(e.getMessage()).isEqualTo("ApiKey doesn't exist or Invalid");
        }
    }

    @Test
    void testSaveRecipesWhenApiKeyIsValidThenSaveRecipe(){
        String apiKey = "1234";
        String recipeName = "Pasta";
        String recipeUrl = "Url";
        Integer rating =  4;
        UserAccount userAccount = new UserAccount();
        SavedRecipe savedRecipe = new SavedRecipe();
        userAccount.setApiKey(apiKey);

        List<UserAccount> userAccountList = new ArrayList<>();
        userAccountList.add(userAccount);

        Mockito.when(userAccountRepository.findByapiKey(Mockito.any())).thenReturn(userAccountList);
        Mockito.when(userAccountRepository.save(Mockito.any())).thenReturn(savedRecipe);

        String Message = recipeFinderService.saveRecipes(apiKey,recipeName,recipeUrl,rating);
        assertThat(Message).isEqualTo("Recipe Saved");
    }

    @Test
    void testGetUserRecipeWhenApiKeyIsNotValidThenThrowsException(){
        List<UserAccount> userAccount = new ArrayList<>();

        Mockito.when(userAccountRepository.findByapiKey(Mockito.any())).thenReturn(userAccount);

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> recipeFinderService.getUserRecipe("1234"),
                "Expected getUserRecipe() to throw, but it didn't"
        );

        assertThat(thrown.getMessage()).isEqualTo("Error: apiKey not correct or doesn't exist");

    }

    @Test
    void testGetUserRecipeWhenApiKeyIsValidThenReturnUserSavedRecipe(){
        List<UserAccount> userAccounts = new ArrayList<>();
        String apiKey = "1234";
        UserAccount userAccount = new UserAccount();
        userAccount.setApiKey(apiKey);
        userAccounts.add(userAccount);

        List<SavedRecipe> savedRecipesList = new ArrayList<>();
        SavedRecipe savedRecipe = new SavedRecipe();
        savedRecipe.setRating(4);
        savedRecipe.setRecipeName("myRecipe");
        savedRecipe.setRecipeURL("http");
        savedRecipesList.add(savedRecipe);

        Mockito.when(userAccountRepository.findByapiKey(Mockito.any())).thenReturn(userAccounts);
        Mockito.when(savedRecipeRepository.findByUserAccount(userAccounts.get(0))).thenReturn(savedRecipesList);

        List<UserRecipe> userRecipes = recipeFinderService.getUserRecipe(apiKey);

        assertThat(userRecipes.get(0).getRating()).isEqualTo(4);
        assertThat(userRecipes.get(0).getRecipeName()).isEqualTo("myRecipe");
        assertThat(userRecipes.get(0).getRecipeUrl()).isEqualTo("http");

    }

    @Test
    void testFindRecipeWhenRecipeNameIsValidThenReturnRecipesList() {
        String recipeName = "Pasta";
        SearchResponse response = new SearchResponse();
        //ResponseEntity<Object> responseEntity = new ResponseEntity<Object>(HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.any())).thenReturn(responseEntity);
        Mockito.when(responseEntity.getBody()).thenReturn(response);

        Result result = new Result();
        result.setId(1234);
        result.setServings(4);

        List<Result> resultsList = new ArrayList<>();
        resultsList.add(0,result);
        response.setResults(resultsList);
        assertThat(recipeFinderService.findRecipe(recipeName)).isNotEmpty();
    }

    @Test
    void testFindRecipeWhenRecipeNameIsNotValidThenReturnRecipesList(){
        String recipeName = "NoRecipe";
        SearchResponse response = new SearchResponse();
        List<Result> resultsList = new ArrayList<>();
        response.setResults(resultsList);
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.any())).thenReturn(responseEntity);
        Mockito.when(responseEntity.getBody()).thenReturn(response);
        try{
            recipeFinderService.findRecipe(recipeName);
            Assertions.failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        }
        catch (IllegalArgumentException e){
            assertThat(e.getMessage()).isEqualTo("Recipe with this name does't exist");
        }

    }
}