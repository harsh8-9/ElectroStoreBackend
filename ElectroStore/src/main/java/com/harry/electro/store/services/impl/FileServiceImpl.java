package com.harry.electro.store.services.impl;

import com.harry.electro.store.exceptions.BadApiRequestException;
import com.harry.electro.store.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private Logger logger = LoggerFactory.getLogger(FileService.class);

    @Override
    public String uploadFile(MultipartFile file, String filePath) throws IOException {
        String originalFilename = file.getOriginalFilename();
        logger.info("FILE : FileName - {}", originalFilename);
        String fileName = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension = fileName + extension;
        String fullPathWithFileName = filePath + fileNameWithExtension;
        List<String> allowedExtension = List.of(".png", ".jpg", ".jpeg");
        if (allowedExtension.contains(extension.toLowerCase())) {
//          file save
            logger.info("Full Path with File Name : {}",fullPathWithFileName);
            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdirs();
                logger.info("FILE : Directory Created !!! ");
            }
            // upload file
            Files.copy(file.getInputStream(), Path.of(fullPathWithFileName));
        } else {
            throw new BadApiRequestException("File with the extension (" + extension + ") is not allowed!");
        }
        return fileNameWithExtension;
    }

    @Override
    public InputStream getResource(String filePath, String fileName) throws FileNotFoundException {

        String fullPathWithFileName = filePath + fileName;
        InputStream inputStream = new FileInputStream(fullPathWithFileName);
        return inputStream;
    }
}
