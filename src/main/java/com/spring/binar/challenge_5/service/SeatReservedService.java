package com.spring.binar.challenge_5.service;

import com.spring.binar.challenge_5.models.SeatReserved;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SeatReservedService {

    List<SeatReserved> findAll();

    SeatReserved findById(int id);

    SeatReserved save(SeatReserved seatReserved);

    SeatReserved update(SeatReserved updatedSeatReserved);

    void delete(int id);
}
