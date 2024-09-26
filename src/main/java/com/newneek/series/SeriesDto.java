package com.newneek.series;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class SeriesDto {

    private String name;
    private int seriesCategoryId;
    private String userId;
    private String about;
    private MultipartFile profile; // 이미지 파일

}
