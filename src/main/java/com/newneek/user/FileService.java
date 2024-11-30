package com.newneek.user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
    @Value("${file.upload-dir:/absolute/path/to/uploads/}")
    private String uploadDir;

    public String saveProfileImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir, fileName);

        // 디렉토리가 없으면 생성
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }

        // 디버깅 메시지
        System.out.println("저장 경로: " + path.toAbsolutePath().toString());
        System.out.println("디렉토리 존재 여부: " + Files.exists(path.getParent()));

        // 파일 저장
        try {
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            System.err.println("파일 저장 실패: " + e.getMessage());
            throw e;
        }
        return fileName; // 저장된 파일 이름 반환
    }
}
