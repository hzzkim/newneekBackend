package com.newneek.series;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "series")
@Getter
@Setter
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seriesId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "series_category_id", nullable = false)
    private int seriesCategoryId;

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Column(name = "about", nullable = false, length = 500)
    private String about;

    @Column(name = "profile", length = 200)
    private String profile; // 이미지 파일 경로 저장

}
