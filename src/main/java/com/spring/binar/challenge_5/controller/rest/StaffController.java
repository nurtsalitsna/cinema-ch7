package com.spring.binar.challenge_5.controller.rest;

import com.spring.binar.challenge_5.models.Staff;
import com.spring.binar.challenge_5.service.StaffService;
import com.spring.binar.challenge_5.utils.ResponseHandler;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.spring.binar.challenge_5.utils.Constants.SUCCESS_EDIT_MSG;
import static com.spring.binar.challenge_5.utils.Constants.SUCCESS_RETRIEVE_MSG;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StaffController {

    private final StaffService staffService;

    @GetMapping("/staff")
    public ResponseEntity<Object> findAll(
            @Nullable @RequestParam Integer page,
            @Nullable @RequestParam Integer size
    ){
        if(page == null || size == null){
            return ResponseHandler.generateResponse(SUCCESS_RETRIEVE_MSG, HttpStatus.OK,staffService.findAll());
        }else{
            Pageable pageable = PageRequest.of(page, size);
            var filmList = staffService.findAll(pageable);
            return ResponseHandler.generatePagingResponse(SUCCESS_RETRIEVE_MSG, HttpStatus.OK,filmList);
        }
    }

    @GetMapping("/staff/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") int id){
        var staff = staffService.findById(id);
        return ResponseHandler.generateResponse(SUCCESS_RETRIEVE_MSG, HttpStatus.OK,staff);
    }

    @PostMapping("/staff")
    public ResponseEntity<Object> save(@RequestBody Staff film){
        staffService.save(film);
        return ResponseHandler.generateResponse(SUCCESS_EDIT_MSG, HttpStatus.OK,film);
    }

    @PostMapping(value = "/staff/{staffId}/photo", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Object> savePhoto(
            @RequestParam("photo") MultipartFile file,
            @PathVariable("staffId") Integer staffId
    ){
        return ResponseHandler.generateResponse(SUCCESS_EDIT_MSG, HttpStatus.OK,staffService.save(file, staffId));
    }

    @PutMapping("/staff")
    public ResponseEntity<Object> update(@RequestBody Staff film ) {
        staffService.update(film);
        return ResponseHandler.generateResponse(SUCCESS_EDIT_MSG, HttpStatus.OK, film);
    }
    @DeleteMapping("/staff/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id ) {
        staffService.delete(id);
        return ResponseHandler.generateResponse(SUCCESS_EDIT_MSG, HttpStatus.OK, id);
    }
}
