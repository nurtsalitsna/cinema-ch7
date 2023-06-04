package com.spring.binar.challenge_5.controller.rest;

import com.spring.binar.challenge_5.models.Seat;
import com.spring.binar.challenge_5.service.SeatService;
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
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/seat")
    public ResponseEntity<Object> findAll(
            @RequestParam(defaultValue ="0") int page,
            @RequestParam(defaultValue ="10") int size
    ){
        Page<Seat> seatList;
        Pageable pageable = PageRequest.of(page, size);
        seatList = seatService.findAll(pageable);

        return ResponseHandler.generatePagingResponse(SUCCESS_RETRIEVE_MSG, HttpStatus.OK,seatList);
    }
    @GetMapping("/seat/{id}")
    public ResponseEntity<Object> findAll(@PathVariable("id") int id){

        var seats = seatService.findById(id);

        return ResponseHandler.generateResponse(SUCCESS_RETRIEVE_MSG, HttpStatus.OK,seats);
    }
    @PostMapping("/seat")
    public ResponseEntity<Object> save(@RequestBody Seat seat){
        var seats = seatService.save(seat);
        return ResponseHandler.generateResponse(SUCCESS_EDIT_MSG, HttpStatus.OK, seats);
    }
    @PutMapping("/seat")
    public ResponseEntity<Object> update(@RequestBody Seat seat ) {
        seatService.update(seat);
        return ResponseHandler.generateResponse(SUCCESS_EDIT_MSG, HttpStatus.OK, seat);
    }
    @DeleteMapping("/seat/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id ) {
        seatService.delete(id);
        return ResponseHandler.generateResponse(SUCCESS_EDIT_MSG, HttpStatus.OK, id);
    }
}
