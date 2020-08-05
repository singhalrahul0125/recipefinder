package com.recipefinder.persistence.respository;

import com.recipefinder.persistence.model.SavedRecipe;
import com.recipefinder.persistence.model.UserAccount;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, Long>{

    List<UserAccount> findByUsername(String username);

    List<UserAccount> findByapiKey(String apiKey);

}
