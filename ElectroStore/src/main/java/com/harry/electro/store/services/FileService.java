package com.harry.electro.store.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/*
 *  @author :-
 *       Harshal Bafna
 */
public interface FileService {

    String uploadFile(MultipartFile file, String filePath) throws IOException;

    InputStream getResource(String filePath, String fileName) throws FileNotFoundException;
}
