package com.spring.binar.challenge_5.repos;

import com.spring.binar.challenge_5.models.Costumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CostumerRepository extends JpaRepository<Costumer, Integer> {

//    @Query("select new com.spring.binar.challenge_5.Costumer from costumer where photoUrl = ? and costumerId = ?")
    Optional<Costumer> findByCostumerIdAndPhotoUrl(String photoUrl, Integer costumerId);
}
