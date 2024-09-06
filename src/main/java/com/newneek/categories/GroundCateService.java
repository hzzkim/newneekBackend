package com.newneek.categories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		List<GroundCate> groundCate2 = groundCateRepository.findAll();
		List<GroundCateDTO> groundCateDto2 = new ArrayList<>();
		for (GroundCate groundCate : groundCate2) {
			GroundCateDTO groundCateDto = new GroundCateDTO(0, null);
			groundCateDto2.add(groundCateDto);
		}
		return groundCateDto2;
	}
	
	public GroundCateDTO updateGroundCategory(int id, GroundCateDTO groundCateDto) {
		GroundCate groundCate = groundCateRepository.findById(id).orElseThrow(() -> new RuntimeException());
		groundCate.setName(groundCateDto.getName());
		
		GroundCate updatedGroundCategory = groundCateRepository.save(groundCate);
		return new GroundCateDTO(updatedGroundCategory.getGroundCategoryId(), updatedGroundCategory.getName());
	}
	
}// end
