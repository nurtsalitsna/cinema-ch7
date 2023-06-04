package com.spring.binar.challenge_5.service.implementation;

import com.spring.binar.challenge_5.dto.StaffRequestDTO;
import com.spring.binar.challenge_5.dto.StaffResponseDto;
import com.spring.binar.challenge_5.models.Staff;
import com.spring.binar.challenge_5.repos.StaffRepository;
import com.spring.binar.challenge_5.repos.UserRepository;
import com.spring.binar.challenge_5.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {
    private final StaffRepository staffRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final Logger logger = LoggerFactory.getLogger(StaffServiceImpl.class);

    @Override
    public Page<StaffResponseDto> findAll(Pageable pageable) {
        return staffRepository.findAll(pageable).map(Staff::convertToStaffDto);
    }

    @Override
    public List<StaffResponseDto> findAll() {
        return staffRepository.findAll()
                .stream()
                .map(Staff::convertToStaffDto)
                .toList();
    }

    @Override
    public StaffResponseDto findById(int id) {
        var staff = staffRepository.findById(id).orElseThrow(() -> new RuntimeException("Could not find staff"));

        return staff.convertToStaffDto();
    }
    @Override
    public StaffResponseDto save(StaffRequestDTO request){
        if (request.getName()==null|| request.getName().isEmpty()||request.getIdCard().length()!=10)
            throw new RuntimeException("Data Staff is not valid");
        var user = userRepository.findById(request.getUserId()).orElseThrow(()->new RuntimeException("User is not Exist"));
        var staff = request.toStaff(user);
        return staffRepository.save(staff).convertToStaffDto();
    }

    @Override
    public StaffResponseDto save(Staff staff) {
        if (staff.getName() == null || staff.getName().isEmpty()
                || staff.getIdCard() == null || staff.getIdCard().length() != 10
        )  throw new RuntimeException("Data staff is not valid");

        staff.setStaffId(0);
        staff.setLastUpdate(System.currentTimeMillis());

        return staffRepository.save(staff).convertToStaffDto();
    }

    @Override
    public StaffResponseDto save(MultipartFile file, Integer staffId) {
        if(file == null || staffId == null) throw new RuntimeException("Please check again your input, it can't empty");

        if(!Objects.requireNonNull(file.getContentType()).startsWith("image")) throw new RuntimeException("Your file is not an image, please check again.");
        logger.info("file name: {}", file.getOriginalFilename());

        var staff       = staffRepository.findById(staffId).orElseThrow(() -> new RuntimeException("Data costumer is not exist"));
        String publicId = staff.getName().toLowerCase().replace(' ', '_') + "_s_" + staff.getStaffId();

        if(staff.getPhotoUrl() != null)
            cloudinaryService.deleteFile(publicId);

        logger.info("staff: {}", staff);

        String url = cloudinaryService.uploadFile(file, publicId);
        logger.info("photo url: {}", url);

        staff.setPhotoUrl(url);

        return staffRepository.save(staff).convertToStaffDto();
    }

    @Override
    public StaffResponseDto update(Staff updatedStaff) {
        var result = staffRepository.findById(updatedStaff.getStaffId()).orElseThrow(() -> new RuntimeException("Could not find staff"));;

        result.setIdCard(updatedStaff.getIdCard());
        result.setName(updatedStaff.getName());
        result.setLastUpdate(System.currentTimeMillis());

        return staffRepository.save(result).convertToStaffDto();
    }

    @Override
    public void delete(int id) {
        var staff = staffRepository.findById(id);
        if(staff.isEmpty()) throw new RuntimeException("Data staff id: " + id + " is not exist.");
        staffRepository.delete(staff.get());
    }
}
