package com.newneek.series;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/series-categories")
public class SeriesCategoryController {

    @Autowired
    private SeriesCategoryService seriesCategoryService;

    // 모든 시리즈 카테고리 조회
    @GetMapping
    public ResponseEntity<List<SeriesCategoryDto>> getAllCategories() {
        List<SeriesCategoryDto> categories = seriesCategoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}
