package com.recipefinder.service;
import com.recipefinder.client.model.RecipeResult;
import com.recipefinder.controller.RecipeFinderController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;

@WebMvcTest(RecipeFinderController.class)
public class RecipeFinderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeFinderService recipeFinderService;

    @Test
    public void testFindRecipe() throws Exception{
        List<RecipeResult> recipeResultList = new ArrayList<>();
        when(recipeFinderService.findRecipe("Pasta")).thenReturn(recipeResultList);
        this.mockMvc.perform(get("/recipefinder/find?recipeName=pasta")).andExpect(status().isOk());
    }

    @Test
    public void testCreateUserAccount() throws Exception{
        when(recipeFinderService.createUserAccount(anyString(),anyString())).thenReturn("RandomKey");
        this.mockMvc.perform(post("/recipefinder/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"username\": \"Rahul\", \"password\": \"1234\" }"))
                .andExpect(status().isOk());
        verify(recipeFinderService).createUserAccount("Rahul", "1234");
    }

    @Test
    public void testSaveRecipes() throws Exception{
        when(recipeFinderService.saveRecipes(anyString(),anyString(),anyString(),anyInt())).thenReturn("Randomkey");
        this.mockMvc.perform(post("/recipefinder/myrecipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"recipeName\": \"Pasta\", \"recipeUrl\": \"AnyUrl\", \"recipeRating\": \"5\" }"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetRecipes() throws Exception{
        List<UserRecipe> userRecipes = new ArrayList<>();
        when(recipeFinderService.getUserRecipe(anyString())).thenReturn(userRecipes);
        this.mockMvc.perform(get("/recipefinder/myrecipes?apiKey=12345")).andExpect(status().isOk());
    }

    @Test
    public void testRecoverApiKey() throws Exception{
        when(recipeFinderService.recoverApiKey(anyString(),anyString())).thenReturn("RandomKey");
        this.mockMvc.perform(post("/recipefinder/recoverapikey")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"userName\": \"Rahul\", \"password\": \"1234\" }"))
                .andExpect(status().isOk());
    }

}
