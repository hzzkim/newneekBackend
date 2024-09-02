package com.newneek.series;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table( name = "article")
@Getter
@Setter
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int articleId;
    private int seriesId;
    private String userId;
    @Column(length = 40)
    private String title;
    @Lob //큰 텍스트 데이터를 저장할 때 사용하는 어노테이션
    private String content;
    private String image;
    private LocalDateTime date;

}
