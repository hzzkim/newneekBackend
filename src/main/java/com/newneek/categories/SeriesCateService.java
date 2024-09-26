package com.newneek.categories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

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
	    List<SeriesCate> seriesCateList = seriesCateRepository.findAll();
	    List<SeriesCateDTO> seriesCateDtoList = new ArrayList<>();
	    
	    for (SeriesCate seriesCate : seriesCateList) {
	        SeriesCateDTO seriesCateDto = new SeriesCateDTO(seriesCate.getSeriesCategoryId(), seriesCate.getName());
	        seriesCateDtoList.add(seriesCateDto);
	    }
	    return seriesCateDtoList;
	}

	public SeriesCateDTO updateSeriesCategory(int id, SeriesCateDTO seriesCateDto) {
	    SeriesCate seriesCate = seriesCateRepository.findById(id)
	        .orElseThrow(() -> new EntityNotFoundException("SeriesCate not found with id: " + id));
	    
	    seriesCate.setName(seriesCateDto.getName());
	    
	    SeriesCate updatedSeriesCategory = seriesCateRepository.save(seriesCate);
	    return new SeriesCateDTO(updatedSeriesCategory.getSeriesCategoryId(), updatedSeriesCategory.getName());
	}
	 
}// end
