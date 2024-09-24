package com.newneek.categories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/categories")
public class GroundCateController {
	
	@Autowired
	private GroundCateService groundCateService;
	
	//생성
	@PostMapping("/ground/add")
	public ResponseEntity<GroundCate> createGroundCategory(@RequestBody GroundCate groundCate) {
		
		GroundCate savedGroundCategory = groundCateService.createGroundCate(groundCate);
		return new ResponseEntity<>(savedGroundCategory, HttpStatus.CREATED);
	}
	
	
	//조회
	@GetMapping("/ground/list")
	public List<GroundCateDTO> getAllGroundCategory() {
		return groundCateService.getAllGroundCategory();
	}
	
	
	//수정
	@PutMapping("/ground/update/{id}")
	public ResponseEntity<GroundCateDTO> updateGroundCategory(@PathVariable("id") int id, GroundCateDTO groundCateDto) {
		GroundCateDTO updatedGroundCategory = groundCateService.updateGroundCategory(id, groundCateDto);
		return ResponseEntity.ok(updatedGroundCategory);
	}
	
	//삭제
	@DeleteMapping("/ground/delete/{id}")
	public void deleteGroundCategory(@PathVariable("id") int id) {
		boolean deleted = groundCateService.deleteGroundCategory(id);
		if (!deleted) {
			throw new RuntimeException("그라운드 카테고리 삭제 실패");
		}
	}
	
}// end
