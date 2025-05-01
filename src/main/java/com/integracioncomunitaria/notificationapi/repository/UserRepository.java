package com.integracioncomunitaria.notificationapi.repository;

import com.integracioncomunitaria.notificationapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

     @Query("""
      SELECT u 
      FROM User u 
      LEFT JOIN FETCH u.profile p 
      WHERE u.email = :email
    """)
    Optional<User> findByEmailWithProfile(@Param("email") String email);
}

