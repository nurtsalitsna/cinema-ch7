package com.spring.binar.challenge_5.service;

import com.spring.binar.challenge_5.models.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilmService {

    Page<Film> findAll(Pageable pageable);

    Film findById(int id);

    Film save(Film film);

    Film update(Film updatedFilm);

    void delete(int id);

}
