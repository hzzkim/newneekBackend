package com.newneek.categories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeriesCateService {
	
	@Autowired
	private SeriesCateRepository seriesCateRepository;
	
	public SeriesCate createSeriesCate(SeriesCate seriesCate) {
		return seriesCateRepository.save(seriesCate);
	}
	
	
	public boolean deleteSeriesCategory(int id) {
		if (seriesCateRepository.existsById(id)) {
			seriesCateRepository.deleteById(id);
			return true;
		}
		return false;
	}
	
	public List<SeriesCateDTO> getAllSeriesCategory() {
		List<SeriesCate> seriesCate2 = seriesCateRepository.findAll();
		List<SeriesCateDTO> seriesCateDto2 = new ArrayList<>();
		for (SeriesCate seriesCate : seriesCate2) {
			SeriesCateDTO seriesCateDto = new SeriesCateDTO(0, null);
			seriesCateDto2.add(seriesCateDto);
		}
		return seriesCateDto2;
	}
	
	public SeriesCateDTO updateSeriesCategory(int id, SeriesCateDTO seriesCateDto) {
		SeriesCate seriesCate = seriesCateRepository.findById(id).orElseThrow(() -> new RuntimeException("시리즈 카테고리를 찾을 수 없습니다"));
		seriesCate.setName(seriesCateDto.getName());
		
		SeriesCate updatedSeriesCategory = seriesCateRepository.save(seriesCate);
		return new SeriesCateDTO(updatedSeriesCategory.getSeriesCategoryId(), updatedSeriesCategory.getName());
	}
	 
}// end
