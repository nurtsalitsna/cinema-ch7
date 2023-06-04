package com.spring.binar.challenge_5.service.implementation;

import com.spring.binar.challenge_5.dto.CostumerRequestDTO;
import com.spring.binar.challenge_5.dto.CostumerResponseDto;
import com.spring.binar.challenge_5.models.Costumer;
import com.spring.binar.challenge_5.repos.CostumerRepository;
import com.spring.binar.challenge_5.repos.UserRepository;
import com.spring.binar.challenge_5.service.CostumerService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CostumerServiceImpl implements CostumerService {

    private final CostumerRepository costumerRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private static final Logger logger = LogManager.getLogger(CostumerServiceImpl.class);

    @Override
    public Page<Costumer> findAll(Pageable pageable) {
        return costumerRepository.findAll(pageable);
    }

    @Override
    public List<CostumerResponseDto> findAll() {
        return costumerRepository
                .findAll()
                .stream()
                .map(Costumer::convertToCostumerDto)
                .toList();
    }

    @Override
    public CostumerResponseDto findById(int id) {
        var costumer = costumerRepository.findById(id).orElseThrow(() -> new RuntimeException("Data costumer is not exist."));

        return costumer.convertToCostumerDto();
    }
    @Override
    public CostumerResponseDto save(CostumerRequestDTO request){
        if(request.getFirstName()==null || request.getLastName().isEmpty()
                || request.getEmail() == null || request.getEmail().isEmpty())
            throw new RuntimeException("Data Costumer is Not Valid");
        var user = userRepository.findById(request.getUserId()).orElseThrow(()-> new RuntimeException("User not exist"));
        var costumer = request.toCostumer(user);
        return costumerRepository.save(costumer).convertToCostumerDto();
    }

    @Override
    public CostumerResponseDto save(Costumer costumer) {
        if (costumer.getFirstName() == null || costumer.getLastName().isEmpty()
                || costumer.getEmail() == null || costumer.getEmail().isEmpty()
        )  throw new RuntimeException("Data costumer is not valid");

        costumer.setCostumerId(0);

        var date = Calendar.getInstance();
        costumer.setLastUpdate(date.getTime().getTime());

        return costumerRepository.save(costumer).convertToCostumerDto();
    }

    @Override
    public CostumerResponseDto save(MultipartFile file, Integer costumerId) {

        if(file == null || costumerId == null) throw new RuntimeException("Please check again your input, it can't empty");

        if(!Objects.requireNonNull(file.getContentType()).startsWith("image")) throw new RuntimeException("Your file is not an image, please check again.");
        logger.info("file name: {}", file.getOriginalFilename());

        var costumer    = costumerRepository.findById(costumerId).orElseThrow(() -> new RuntimeException("Data costumer is not exist"));
        String publicId = costumer.getFirstName().toLowerCase() + "_" + costumer.getLastName().toLowerCase() + "_" + costumer.getCostumerId();

        if(costumer.getPhotoUrl() != null)
            cloudinaryService.deleteFile(publicId);

        logger.info(costumer);

        String url = cloudinaryService.uploadFile(file, publicId);
        logger.info("photo url: {}", url);

        costumer.setPhotoUrl(url);

        return costumerRepository.save(costumer).convertToCostumerDto();
    }

    @Override
    public CostumerResponseDto update(Costumer updatedCostumer) {
        var result = costumerRepository.findById(updatedCostumer.getCostumerId());

        if(result.isEmpty())
            throw new RuntimeException("Data costumer id: " + updatedCostumer.getCostumerId() + " is not exist.");

        var costumer = result.get();
        costumer.setEmail(updatedCostumer.getEmail());
        costumer.setFirstName(updatedCostumer.getFirstName());
        return costumerRepository.save(costumer).convertToCostumerDto();
    }

    @Override
    public void delete(int id) {
        var costumer = costumerRepository.findById(id);
        if(costumer.isEmpty()) throw new RuntimeException("Data costumer id: " + id + " is not exist.");
        costumerRepository.delete(costumer.get());
    }
}
