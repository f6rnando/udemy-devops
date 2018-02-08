package com.f6rnando.backend.persistence.repositories;

import com.f6rnando.backend.persistence.domain.backend.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/************************************
 Created by f6rnando@gmail.com
 2018-02-07
 *************************************/

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Returns an User given an username or null if not found
     * @param username
     * @return User/Null given an username
     */
    public User findByUsername(String username);
}
