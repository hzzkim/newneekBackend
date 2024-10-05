package com.newneek.series;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeriesDto {

    private int seriesCategoryId;
    private String userId;
    private String about;
    private MultipartFile profile; // 이미지 파일

}
