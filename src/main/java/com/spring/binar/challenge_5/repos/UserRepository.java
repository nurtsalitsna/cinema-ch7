package com.spring.binar.challenge_5.repos;

import com.spring.binar.challenge_5.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByPhoneNumber(String phoneNumber);
}
