package com.ecommerce.project.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {


    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        
        //file names of the current file
        String originalFilename = file.getOriginalFilename();


        //generate a random file name for dealing with name conflicts
        String randomId = UUID.randomUUID().toString();
        String fileName =randomId.concat(originalFilename.substring(originalFilename.lastIndexOf('.')));

        //check if path exists and create directory
        String filePath= path + File.separator + fileName;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));
        //returning file name

        return fileName;
    }
}
