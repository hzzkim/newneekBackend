package com.newneek.series;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/series")
public class SeriesController {

    @Autowired
    private SeriesService seriesService;

    @PostMapping
    public ResponseEntity<Series> createSeries(
            @RequestParam("seriesCategoryId") int seriesCategoryId,
            @RequestParam("userId") String userId,
            @RequestParam("about") String about,
            @RequestParam(value = "profile", required = false) MultipartFile profile
    ) {
        SeriesDto seriesDto = new SeriesDto();
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
}
