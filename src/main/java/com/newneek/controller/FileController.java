package com.newneek.controller;

import com.newneek.entity.FileEntity;
import com.newneek.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final FileRepository fileRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        System.out.println("파일 업로드 요청 수신: " + file.getOriginalFilename());
        try {
            // 파일 저장 경로 생성
            Path path = Paths.get(uploadDir).toAbsolutePath().normalize();
            System.out.println("저장 경로: " + path.toString());
            Files.createDirectories(path);
            
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String uuid = UUID.randomUUID().toString();
            String filename = uuid + "_" + originalFileName;
            
            // 파일 저장
            Path targetLocation = path.resolve(filename);
            System.out.println("파일 저장 위치: " + targetLocation.toString());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // 파일 메타데이터 저장
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFilename(filename); // UUID를 포함한 파일 이름 저장
            fileEntity.setFilePath(targetLocation.toString());
            fileRepository.save(fileEntity);
            System.out.println("파일 메타데이터 저장 완료: " + fileEntity.toString());

            String fileDownloadUri = "/api/download/" + filename;  // 파일의 접근 가능한 URL 경로
            System.out.println("파일 다운로드 URL: " + fileDownloadUri);
            return ResponseEntity.ok(fileDownloadUri);
        } catch (IOException e) {
            System.err.println("파일 업로드 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패: " + e.getMessage());
        }
    }
    
    // 다운로드 엔드포인트
    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        System.out.println("파일 다운로드 요청 수신: " + filename);
        try {
            Path filePath = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(filename);
            System.out.println("파일 경로: " + filePath.toString());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                System.out.println("파일 다운로드 가능: " + resource.getFilename());
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                System.err.println("파일을 읽을 수 없거나 존재하지 않습니다: " + filename);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (IOException e) {
            System.err.println("파일 다운로드 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
