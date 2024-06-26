package com.binarybrothers.gymflexapi.services.upload;

import org.springframework.web.multipart.MultipartFile;

public interface IUploadService {
    public String updateFileUpload(String documentUrl,String folderUrl, MultipartFile file);

    public String handleFileUpload(String name ,String folderUrl ,MultipartFile file);
    public void deleteDocument(String cheminFolderDocument, String urlDocument);
}
