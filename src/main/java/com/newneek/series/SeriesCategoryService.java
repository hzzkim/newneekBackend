package com.newneek.series;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeriesCategoryService {

    @Autowired
    private SeriesCategoryRepository seriesCategoryRepository;

    // 모든 카테고리 조회
    public List<SeriesCategoryDto> getAllCategories() {
        return seriesCategoryRepository.findAll().stream()
                .map(category -> new SeriesCategoryDto(category.getSeriesCategoryId(), category.getName()))
                .collect(Collectors.toList());
    }

}
