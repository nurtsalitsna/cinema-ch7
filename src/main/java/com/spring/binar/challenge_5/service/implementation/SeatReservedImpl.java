package com.spring.binar.challenge_5.service.implementation;

import com.spring.binar.challenge_5.models.SeatReserved;
import com.spring.binar.challenge_5.repos.SeatReservedRepository;
import com.spring.binar.challenge_5.service.SeatReservedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatReservedImpl implements SeatReservedService {

    @Override
    public List<SeatReserved> findAll() {
        return null;
    }

    @Override
    public SeatReserved findById(int id) {
        return null;
    }

    @Override
    public SeatReserved save(SeatReserved seatReserved) {
        return null;
    }

    @Override
    public SeatReserved update(SeatReserved updatedSeatReserved) {
        return null;
    }

    @Override
    public void delete(int id) {

    }
}
