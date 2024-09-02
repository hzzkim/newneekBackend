package com.newneek.series;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }
}
