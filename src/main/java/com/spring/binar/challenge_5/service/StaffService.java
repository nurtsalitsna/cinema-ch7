package com.spring.binar.challenge_5.service;

import com.spring.binar.challenge_5.dto.StaffRequestDTO;
import com.spring.binar.challenge_5.dto.StaffResponseDto;
import com.spring.binar.challenge_5.models.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StaffService {
    Page<StaffResponseDto> findAll(Pageable pageable);

    List<StaffResponseDto> findAll();

    StaffResponseDto findById(int id);

    StaffResponseDto save(StaffRequestDTO request);

    StaffResponseDto save(Staff staff);

    StaffResponseDto save(MultipartFile file, Integer staffId);

    StaffResponseDto update(Staff updatedStaff);

    void delete(int id);
}
