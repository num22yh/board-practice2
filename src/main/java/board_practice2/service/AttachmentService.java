package board_practice2.service;

import board_practice2.entity.Attachment;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    @Value("${file.dir}")
    private String uploadDir;

    public Attachment saveFile(MultipartFile file) throws IOException{

        if (file.isEmpty()){
            return null;
        }
        String originalFileName = file.getOriginalFilename();
        String storedFileName = createStoreFileName(originalFileName);
        Path storedPath = Paths.get(uploadDir, storedFileName);

        Files.createDirectories(storedPath.getParent());
        file.transferTo(storedPath);

        Attachment attachment = new Attachment();
        attachment.setOriginalName(originalFileName);
        attachment.setStoredName(storedFileName);
        //TODO : 질문 - logicalPath 어떻게 해야하는 건지 잘 이해 안감
        attachment.setLogicalPath("/uploads/"+ storedFileName);
        attachment.setPhysicalPath(storedPath.toString());
        attachment.setSize(file.getSize());

        return attachment;

    }

    private String createStoreFileName(String originalFilename){
        String extension = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "_"+originalFilename+"." + extension;
    }

    private String extractExt(String originalFilename){
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos+1);
    }
}
