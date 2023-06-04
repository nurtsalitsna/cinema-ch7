package com.spring.binar.challenge_5.repos;

import com.spring.binar.challenge_5.models.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface FilmRepository extends JpaRepository<Film, Integer> {
}
