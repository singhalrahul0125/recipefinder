package com.recipefinder.persistence.respository;

import com.recipefinder.persistence.model.SavedRecipe;
import com.recipefinder.persistence.model.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedRecipeRepository extends CrudRepository<SavedRecipe, Long>{
    List<SavedRecipe> findByUserAccount(UserAccount userAccount);

}
