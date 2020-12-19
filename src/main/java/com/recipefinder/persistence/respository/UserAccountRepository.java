package com.recipefinder.persistence.respository;

import com.recipefinder.persistence.model.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    List<UserAccount> findByUsername(String username);
    List<UserAccount> findByapiKey(String apiKey);
}
