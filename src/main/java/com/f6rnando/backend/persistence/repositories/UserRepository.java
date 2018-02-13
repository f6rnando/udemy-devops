package com.f6rnando.backend.persistence.repositories;

import com.f6rnando.backend.persistence.domain.backend.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/************************************
 Created by f6rnando@gmail.com
 2018-02-07
 *************************************/

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Returns an User given an username or null if not found
     * @param username
     * @return User/Null given an username
     */
    User findByUsername(String username);

    /**
     * Returns an User fot the given email - Null is not found
     * @param email
     * @return an User fot the given email - Null is not found
     */
    User findByEmail(String email);

    @Modifying
    @Query("update User u set u.password = :password where u.id = :userId")
    void updateUserPassword(@Param("userId") long userId, @Param("password") String password);
}
