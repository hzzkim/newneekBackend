package com.newneek.series;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/series")
public class SeriesController {

    @Autowired
    private SeriesService seriesService;

    @PostMapping
    public ResponseEntity<Series> createSeries(
            @RequestParam("name") String name,
            @RequestParam("seriesCategoryId") int seriesCategoryId,
            @RequestParam("userId") String userId,
            @RequestParam("about") String about,
            @RequestParam(value = "profile", required = false) MultipartFile profile
    ) {
        SeriesDto seriesDto = new SeriesDto();
        seriesDto.setName(name);
        seriesDto.setSeriesCategoryId(seriesCategoryId);
        seriesDto.setUserId(userId);
        seriesDto.setAbout(about);
        seriesDto.setProfile(profile);

        try {
            Series createdSeries = seriesService.createSeries(seriesDto);
            return new ResponseEntity<>(createdSeries, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 시리즈 리스트 조회 API
    @GetMapping("/lists")
    public ResponseEntity<List<Series>> getAllSeries() {
        System.out.println("시리즈 목록 조회 요청");
        List<Series> seriesList = seriesService.getAllSeries();
        return ResponseEntity.ok(seriesList);
    }
}
