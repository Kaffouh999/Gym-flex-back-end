package com.example.GymInTheBack.services.upload;

import com.example.GymInTheBack.entities.Equipment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UploadService implements IUploadService{

    @Override
    public String handleFileUpload(String name, String folderUrl, MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        try {
            File uploadDir = new File("/home/youssef/Documents/GYmFlexDocuments"+folderUrl);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            Path dest = uploadDir.toPath().resolve(name+"."+extension);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, dest, StandardCopyOption.REPLACE_EXISTING);
            }

            return dest.getFileName().toString();
        } catch (IOException e) {

            return null;
        }    }
    @Override
    public String updateFileUpload(String documentUrl,String folderUrl, MultipartFile file) {
        int lastIndex = documentUrl.lastIndexOf("/");
        String name = documentUrl.substring(lastIndex + 1);

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        try {
            File uploadDir = new File("/home/youssef/Documents/GYmFlexDocuments"+folderUrl);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            Path dest = uploadDir.toPath().resolve(name);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, dest, StandardCopyOption.REPLACE_EXISTING);
            }

            return dest.getFileName().toString();
        } catch (IOException e) {

            return null;
        }
    }

    @Override
    public void deleteDocument(String urlFolderDocument, String urlDocument) {

                int lastIndex = urlDocument.lastIndexOf("/");
                String fileName = urlDocument.substring(lastIndex + 1);

                String folderPath = "/home/youssef/Documents/GYmFlexDocuments"+urlFolderDocument;

                String filePath = folderPath + fileName ;
                // Create a File object representing the image file
                File documentFile = new File(filePath);

                // Delete the image file
                if (documentFile.delete()) {
                    System.out.println("File deleted successfully.");
                } else {
                    System.out.println("Failed to delete the file.");
                }
            }


}
