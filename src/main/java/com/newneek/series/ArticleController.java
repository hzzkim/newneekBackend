package com.newneek.series;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        // 현재 시간을 아티클의 날짜로 설정 (생성 시점)
        article.setDate(LocalDateTime.now());
        Article savedArticle = articleService.createArticle(article);
        return new ResponseEntity<>(savedArticle, HttpStatus.CREATED);
    }
}
