package com.spring.binar.challenge_5.service;

import com.spring.binar.challenge_5.dto.ScheduleRequestDTO;
import com.spring.binar.challenge_5.dto.ScheduleResponseDTO;
import com.spring.binar.challenge_5.models.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ScheduleService {
    Page<Schedule> findAll(Pageable pageable);

    List<ScheduleResponseDTO> findAll();

    ScheduleResponseDTO findById(int id);

    ScheduleResponseDTO save(ScheduleRequestDTO schedule);

    ScheduleResponseDTO update(ScheduleRequestDTO updatedSchedule);

    void delete(int id);
}
