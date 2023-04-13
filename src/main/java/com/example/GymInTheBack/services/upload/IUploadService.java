package com.example.GymInTheBack.services.upload;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadService {
    public String updateFileUpload(String documentUrl,String folderUrl, MultipartFile file);

    public String handleFileUpload(String name ,String folderUrl ,MultipartFile file);
    public void deleteDocument(String cheminFolderDocument, String urlDocument);
}
