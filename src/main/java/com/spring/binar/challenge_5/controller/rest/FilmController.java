package com.spring.binar.challenge_5.controller.rest;


import com.spring.binar.challenge_5.models.Film;
import com.spring.binar.challenge_5.service.FilmService;
import com.spring.binar.challenge_5.utils.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.spring.binar.challenge_5.utils.Constants.SUCCESS_EDIT_MSG;
import static com.spring.binar.challenge_5.utils.Constants.SUCCESS_RETRIEVE_MSG;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FilmController {

    private final FilmService filmService;

    @GetMapping("/film")
    public ResponseEntity<Object> findAll(
            @RequestParam(defaultValue ="0") int page,
            @RequestParam(defaultValue ="10") int size
    ){
        Page<Film> filmList;
        Pageable pageable = PageRequest.of(page, size);
        filmList = filmService.findAll(pageable);

        return ResponseHandler.generatePagingResponse(SUCCESS_RETRIEVE_MSG, HttpStatus.OK,filmList);
    }

    @GetMapping("/film/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") int id){
        var film = filmService.findById(id);
        return ResponseHandler.generateResponse(SUCCESS_RETRIEVE_MSG, HttpStatus.OK,film);
    }

    @PostMapping("/film")
    public ResponseEntity<Object> save(@RequestBody Film film){
        filmService.save(film);
        return ResponseHandler.generateResponse(SUCCESS_EDIT_MSG, HttpStatus.OK,film);
    }

    @PutMapping("/film")
    public ResponseEntity<Object> update(@RequestBody Film film ) {
        filmService.update(film);
        return ResponseHandler.generateResponse(SUCCESS_EDIT_MSG, HttpStatus.OK, film);
    }
    @DeleteMapping("/film/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id ) {
        filmService.delete(id);
        return ResponseHandler.generateResponse(SUCCESS_EDIT_MSG, HttpStatus.OK, id);
    }


}
