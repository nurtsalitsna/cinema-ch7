package com.spring.binar.challenge_5.service.implementation;

import com.spring.binar.challenge_5.dto.ScheduleRequestDTO;
import com.spring.binar.challenge_5.dto.ScheduleResponseDTO;
import com.spring.binar.challenge_5.models.Schedule;
import com.spring.binar.challenge_5.repos.*;
import com.spring.binar.challenge_5.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    ModelMapper modelMapper;
    private final ScheduleRepository scheduleRepository;
    private final StudioRepository studioRepository;
    private final FilmRepository filmRepository;
    private final SeatRepository seatRepository;

    @Override
    public Page<Schedule> findAll(Pageable pageable) {
        return scheduleRepository.findAll(pageable);
    }

    @Override
    public List<ScheduleResponseDTO> findAll() {
        var schedules = scheduleRepository.findAll();
        if(schedules.isEmpty()) return new ArrayList<>();

        return schedules.stream().map(schedule -> {
            var availableSeats = seatRepository.findAvailableSeats(schedule.getScheduleId(), schedule.getStudio().getStudioId());
            System.out.println("Avalable seats" + availableSeats);
            return schedule.convertToResponse(availableSeats);
        }).toList();
    }

    @Override
    public ScheduleResponseDTO findById(int id) {
        var schedule = scheduleRepository.findById(id);

        if(schedule.isEmpty()) throw new RuntimeException("No Schedule found");

        var availableSeats = seatRepository.findAvailableSeats(schedule.get().getScheduleId(), schedule.get().getStudio().getStudioId());

        return schedule.get().convertToResponse(availableSeats);
    }

    @Override
    public ScheduleResponseDTO save(ScheduleRequestDTO request) {

        if(request.getFromDate() > request.getToDate())
            throw new RuntimeException("Date range not valid");

        var studio = studioRepository.findById(request.getStudioId()).orElseThrow(()-> new RuntimeException("Studio or Film Not Found "));
        var film = filmRepository.findById(request.getFilmId()).orElseThrow(()-> new RuntimeException("Studio or Film Not Found "));

        var schedule = request.toSchedule(studio,film);
        var result = scheduleRepository.save(schedule);
        var seats = seatRepository.findByStudioStudioId(result.getStudio().getStudioId());

        return result.convertToResponse(seats);
    }

    /*
    * permasalahan ketika schedule di update studio maka payment yang sudah terjadi perlu overwrite id seat yang ada agar sesuai
    * id seat yang sesuai pada studio.
    * Jadi update hanya dibatasi pada perubahan film, price, dan tanggal
    * */
    @Override
    public ScheduleResponseDTO update(ScheduleRequestDTO updatedSchedule) {
        var request = scheduleRepository.findById(updatedSchedule.getScheduleId());

        if(request.isEmpty())
            throw new RuntimeException("No Schedule found");
        
        var schedule = request.get();

        var film = filmRepository.findById(updatedSchedule.getFilmId());
        if(film.isEmpty())
            throw new RuntimeException("No film found");

        if(updatedSchedule.getFromDate() > updatedSchedule.getToDate())
            throw new RuntimeException("Date range not valid");

        schedule.setPrice(updatedSchedule.getPrice());
        schedule.setFromDate(updatedSchedule.getFromDate());
        schedule.setToDate(updatedSchedule.getToDate());
        schedule.setFilm(film.get());

        var result = scheduleRepository.save(schedule);
        var seats = seatRepository.findByStudioStudioId(result.getStudio().getStudioId());

        return result.convertToResponse(seats);
    }

    @Override
    public void delete(int id) {
        var result = scheduleRepository.findById(id);

        if(result.isEmpty()) throw new RuntimeException("No Schedule found");

        scheduleRepository.delete(result.get());
    }
}
