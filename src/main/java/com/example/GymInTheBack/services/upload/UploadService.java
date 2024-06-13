package com.example.GymInTheBack.services.upload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.logging.Logger;

@Service
public class UploadService implements IUploadService {

    Logger logger = Logger.getLogger(UploadService.class.getName());

    @Value("${FILE_UPLOAD_DIRECTORY}")
    String FILE_UPLOAD_DIRECTORY;

    @Override
    public String handleFileUpload(String name, String folderUrl, MultipartFile file) {
        try {
            File uploadDir = createDirectoryIfNotExists(folderUrl);
            String fileName = getFileName(file);
            Path dest = uploadDir.toPath().resolve(name + "." + fileName);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, dest, StandardCopyOption.REPLACE_EXISTING);
            }

            return dest.getFileName().toString();
        } catch (IOException e) {
            logger.severe("Failed to handle file upload: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String updateFileUpload(String documentUrl, String folderUrl, MultipartFile file) {
        try {
            File uploadDir = createDirectoryIfNotExists(folderUrl);
            int lastIndex = documentUrl.lastIndexOf("/");
            String name = documentUrl.substring(lastIndex + 1);
            Path dest = uploadDir.toPath().resolve(name);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, dest, StandardCopyOption.REPLACE_EXISTING);
            }

            return dest.getFileName().toString();
        } catch (IOException e) {
            logger.severe("Failed to update file upload: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteDocument(String urlFolderDocument, String urlDocument) {
        int lastIndex = urlDocument.lastIndexOf("/");
        String fileName = urlDocument.substring(lastIndex + 1);

        String folderPath = FILE_UPLOAD_DIRECTORY + urlFolderDocument;

        String filePath = folderPath + fileName;
        // Create a File object representing the image file
        File documentFile = new File(filePath);

        // Delete the image file
        if (documentFile.delete()) {
            logger.info("File deleted successfully.");
        } else {
            logger.severe("Failed to delete the file.");
        }
    }

    private File createDirectoryIfNotExists(String folderUrl) {
        File directory = new File(FILE_UPLOAD_DIRECTORY + folderUrl);
        if (!directory.exists()) {
            boolean result = directory.mkdirs();
            if (result) {
                logger.info("Directory was created!");
            } else {
                logger.severe("Failed to create directory!");
            }
        }
        return directory;
    }

    private String getFileName(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}