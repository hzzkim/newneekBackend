package com.newneek.series;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class SeriesService {

    @Autowired
    private SeriesRepository seriesRepository;

    public Series createSeries(SeriesDto seriesDto) throws IOException {
        Series series = new Series();
        series.setName(seriesDto.getName());
        series.setSeriesCategoryId(seriesDto.getSeriesCategoryId());
        series.setUserId(seriesDto.getUserId());
        series.setAbout(seriesDto.getAbout());

        // 프로필 이미지 처리
        String fileName = saveImage(seriesDto.getProfile());
        series.setProfile(fileName);

        return seriesRepository.save(series);
    }

    // 파일 저장 디렉토리 경로 설정
    private final String uploadDir = System.getProperty("user.dir") + "/uploads";

    private String saveImage(MultipartFile image) {
        try {

            // 업로드 폴더가 존재하지 않으면 생성
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath); // 디렉토리 생성
            }

            // 파일 이름 생성
            String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            // 파일을 지정된 경로에 저장 (임시 파일을 사용하지 않음)
            Files.write(filePath, image.getBytes()); // 파일을 직접 디스크에 기록

            // 파일의 상대 URL 반환 (예: /uploads/파일이름)
            String fileUrl = "/uploads/" + fileName;
            return fileUrl;
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }

    // 모든 시리즈 리스트를 가져오는 메서드
    public List<Series> getAllSeries() {
        return seriesRepository.findAll();
    }

}
