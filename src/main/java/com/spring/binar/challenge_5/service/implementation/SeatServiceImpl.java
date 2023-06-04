package com.spring.binar.challenge_5.service.implementation;

import com.spring.binar.challenge_5.models.Seat;
import com.spring.binar.challenge_5.repos.SeatRepository;
import com.spring.binar.challenge_5.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {
    private final SeatRepository seatRepository;

    @Override
    public Page<Seat> findAll(Pageable pageable) {
        return seatRepository.findAll(pageable);
    }
    @Override
    public Seat findById(int id) {
        var seat = seatRepository.findById(id);
        if(seat.isEmpty()) throw new RuntimeException("Seat id: " + id + " tidak ditemukan.");

        return seat.get();
    }
    @Override
    public Seat save(Seat seat) {
        if (seat.getRow() != ' ' || seat.getNumber() > 0
        ) throw new RuntimeException("Seat tidak valid ");

        seat.setSeatId(0);

        return seatRepository.save(seat);
    }
    @Override
    public Seat update(Seat updatedSeat) {
        Optional<Seat> result = seatRepository.findById(updatedSeat.getSeatId());
        Seat seat;

        if (result.isPresent()) {
            seat = result.get();
            seat.setRow(updatedSeat.getRow());
            seat.setNumber(updatedSeat.getNumber());
            return seatRepository.save(seat);
        } else {
            throw new RuntimeException("Data Seat tidak ditemukan");
        }
    }
    @Override
    public void delete(int id) {
        Optional<Seat> result = seatRepository.findById(id);
        if (result.isEmpty()) throw new RuntimeException("Data seat tidak ditemukan");
        seatRepository.delete(result.get());
    }

}
