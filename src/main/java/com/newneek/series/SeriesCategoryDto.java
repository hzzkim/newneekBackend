package com.newneek.series;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeriesCategoryDto {
    private int id;
    private String name;

    public SeriesCategoryDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // 기본 생성자 추가 (필수)
    public SeriesCategoryDto() {
    }
}
