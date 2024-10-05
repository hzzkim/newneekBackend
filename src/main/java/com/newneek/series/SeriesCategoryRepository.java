package com.newneek.series;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesCategoryRepository extends JpaRepository<SeriesCategory, Integer> {
}
