package com.newneek.series;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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
