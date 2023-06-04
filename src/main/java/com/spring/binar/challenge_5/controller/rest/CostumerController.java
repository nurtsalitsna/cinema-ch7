package com.spring.binar.challenge_5.controller.rest;

import com.spring.binar.challenge_5.models.Costumer;
import com.spring.binar.challenge_5.service.CostumerService;
import com.spring.binar.challenge_5.utils.ResponseHandler;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.spring.binar.challenge_5.utils.Constants.SUCCESS_EDIT_MSG;
import static com.spring.binar.challenge_5.utils.Constants.SUCCESS_RETRIEVE_MSG;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CostumerController {

    private final CostumerService costumerService;

    @GetMapping("/costumer")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<Object> findAll(
            @Nullable @RequestParam Integer page,
            @Nullable @RequestParam Integer size
    ){
        if(page == null || size == null){
            return ResponseHandler.generateResponse(SUCCESS_RETRIEVE_MSG, HttpStatus.OK,costumerService.findAll());
        }else{
            Pageable pageable = PageRequest.of(page, size);
            var filmList = costumerService.findAll(pageable);
            return ResponseHandler.generatePagingResponse(SUCCESS_RETRIEVE_MSG, HttpStatus.OK,filmList);
        }
    }

    @GetMapping("/costumer/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") int id){
        var costumer = costumerService.findById(id);
        return ResponseHandler.generateResponse(SUCCESS_RETRIEVE_MSG, HttpStatus.OK,costumer);
    }

    @PostMapping("/costumer")
    public ResponseEntity<Object> save(@RequestBody Costumer film){
        costumerService.save(film);
        return ResponseHandler.generateResponse(SUCCESS_EDIT_MSG, HttpStatus.OK,film);
    }

    @PostMapping(value = "/costumer/{costumerId}/photo", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Object> savePhoto(
            @RequestParam("photo") MultipartFile file,
            @PathVariable("costumerId") Integer costumerId
    ){
        return ResponseHandler.generateResponse(SUCCESS_EDIT_MSG, HttpStatus.OK,costumerService.save(file, costumerId));
    }

    @PutMapping("/costumer")
    public ResponseEntity<Object> update(@RequestBody Costumer costumer ) {
        costumerService.update(costumer);
        return ResponseHandler.generateResponse(SUCCESS_EDIT_MSG, HttpStatus.OK, costumer);
    }
    @DeleteMapping("/costumer/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id ) {
        costumerService.delete(id);
        return ResponseHandler.generateResponse(SUCCESS_EDIT_MSG, HttpStatus.OK, id);
    }
}
