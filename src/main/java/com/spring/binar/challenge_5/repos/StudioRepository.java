package com.spring.binar.challenge_5.repos;

import com.spring.binar.challenge_5.models.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface StudioRepository extends JpaRepository<Studio, Integer> {
}
