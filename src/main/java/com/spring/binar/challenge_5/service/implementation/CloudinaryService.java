package com.spring.binar.challenge_5.service.implementation;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinaryConfig;
    private static final Logger logger = LogManager.getLogger(CloudinaryService.class);


    public String uploadFile(MultipartFile file, String publicId){
        try {
            File uploadFile = new File(convertMultipartToFile(file).toUri());
            logger.info("Uploading file : {}", uploadFile);
            var uploadResult = cloudinaryConfig.uploader().upload(uploadFile, ObjectUtils.asMap("public_id", publicId));
            logger.info("upload to cloudinary: {}", uploadResult);
            boolean isDeleted = uploadFile.delete();

            if (isDeleted)
                logger.info("File successfully deleted");
            else
                logger.error("File doesn't exist");

            return uploadResult.get("url").toString();
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new  RuntimeException(e);
        }
    }

    public void deleteFile(String publicId){
        try {
            var result = cloudinaryConfig
                    .uploader()
                    .destroy(publicId, ObjectUtils.asMap("resource_type","image"));
            logger.info("is deleted in cloudinary: {}", result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public void deleteFileFromLocal(File file) throws IOException {
//        Files.delete(file.toPath());
//    }

    private Path convertMultipartToFile(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        Path path = Paths.get("C:/Users/Dwika/Documents/Dwika File/Temporary/",file.getOriginalFilename());
        var convPath = Files.write(path, bytes);
        logger.info("converted path: {}", convPath);
        return convPath;
    }
}
