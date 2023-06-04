package com.spring.binar.challenge_5.service;

import com.spring.binar.challenge_5.dto.CostumerRequestDTO;
import com.spring.binar.challenge_5.dto.CostumerResponseDto;
import com.spring.binar.challenge_5.models.Costumer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface CostumerService {

    Page<Costumer> findAll(Pageable pageable);

    List<CostumerResponseDto> findAll();

    CostumerResponseDto findById(int id);

    CostumerResponseDto save(CostumerRequestDTO request);

    CostumerResponseDto save(Costumer costumer);

    CostumerResponseDto save(MultipartFile file, Integer costumerId);

    CostumerResponseDto update(Costumer updatedCostumer);

    void delete(int id);
}
