package com.recipefinder.service;

import com.recipefinder.persistence.model.UserAccount;
import com.recipefinder.persistence.respository.SavedRecipeRepository;
import com.recipefinder.persistence.respository.UserAccountRepository;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecipeFinderServiceTest {

    RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
    UserAccountRepository userAccountRepository = Mockito.mock(UserAccountRepository.class);
    SavedRecipeRepository savedRecipeRepository= Mockito.mock(SavedRecipeRepository.class);

    RecipeFinderService recipeFinderService = new RecipeFinderService(restTemplate, userAccountRepository, savedRecipeRepository);

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
        Assertions.assertThat(actualApiKey).isEqualTo(apiKey);
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
            Assertions.assertThat(e.getMessage()).isEqualTo("Invalid username or password!");
        }
    }

}