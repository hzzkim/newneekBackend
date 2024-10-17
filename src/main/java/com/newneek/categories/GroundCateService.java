package com.newneek.categories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class GroundCateService {

	@Autowired
	private GroundCateRepository groundCateRepository;
	
	public GroundCate createGroundCate(GroundCate groundCate) {
		return groundCateRepository.save(groundCate);
	}
	
	public boolean deleteGroundCategory(int id) {
		if(groundCateRepository.existsById(id)) {
			groundCateRepository.deleteById(id);
			return true;
		}
		return false;
	}
	
	public List<GroundCateDTO> getAllGroundCategory() {
	    List<GroundCate> groundCateList = groundCateRepository.findAll();
	    List<GroundCateDTO> groundCateDtoList = new ArrayList<>();
	    
	    for (GroundCate groundCate : groundCateList) {
	        GroundCateDTO groundCateDto = new GroundCateDTO(groundCate.getGroundCategoryId(), groundCate.getName());
	        groundCateDtoList.add(groundCateDto);
	    }
	    return groundCateDtoList;
	}
	
	public GroundCateDTO updateGroundCategory(int id, GroundCateDTO groundCateDto) {
	    GroundCate groundCate = groundCateRepository.findById(id)
	        .orElseThrow(() -> new EntityNotFoundException("GroundCate not found with id: " + id));
	    
	    groundCate.setName(groundCateDto.getName());
	    
	    GroundCate updatedGroundCategory = groundCateRepository.save(groundCate);
	    return new GroundCateDTO(updatedGroundCategory.getGroundCategoryId(), updatedGroundCategory.getName());
	}
	
}// end
