package com.newneek.series;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "series_category")
@Getter
@Setter
public class SeriesCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "series_category_id") // 데이터베이스에 있는 실제 컬럼명
    private int seriesCategoryId; // 엔티티에서 사용할 필드명

    @Column(nullable = false)
    private String name;
}
