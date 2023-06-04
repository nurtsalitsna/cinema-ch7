package com.spring.binar.challenge_5.controller.rest;

import com.spring.binar.challenge_5.models.Studio;
import com.spring.binar.challenge_5.service.StudioService;
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
public class StudioController {

    private final StudioService studioService;

    @GetMapping("/studio")
    public ResponseEntity<Object> findAll(
            @RequestParam(defaultValue ="0") int page,
            @RequestParam(defaultValue ="10") int size
    ){
        Page<Studio> filmList;
        Pageable pageable = PageRequest.of(page, size);
        filmList = studioService.findAll(pageable);

        return ResponseHandler.generatePagingResponse(SUCCESS_RETRIEVE_MSG, HttpStatus.OK,filmList);
    }

    @PostMapping("/studio")
    public ResponseEntity<Object> save(@RequestBody Studio studio){
        studioService.save(studio);
        return ResponseHandler.generateResponse(SUCCESS_EDIT_MSG, HttpStatus.OK,studio);
    }

    @GetMapping("/studio/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") int id){
        var studio = studioService.findById(id);
        return ResponseHandler.generateResponse(SUCCESS_RETRIEVE_MSG, HttpStatus.OK,studio);
    }

    @PutMapping("/studio")
    public ResponseEntity<Object> update(@RequestBody Studio film ) {
        studioService.update(film);
        return ResponseHandler.generateResponse(SUCCESS_EDIT_MSG, HttpStatus.OK, film);
    }
    @DeleteMapping("/studio/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id ) {
        studioService.delete(id);
        return ResponseHandler.generateResponse(SUCCESS_EDIT_MSG, HttpStatus.OK, id);
    }

}
