package com.newneek.categories;

import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class SeriesCateController {
	
	@Autowired
	private SeriesCateService seriesCateService;
	
	//생성
	@PostMapping("/series/add")
	public ResponseEntity<SeriesCate> createSeriesCategory(@RequestBody SeriesCate seriesCate) {
		
		SeriesCate savedSeriesCategory = seriesCateService.createSeriesCate(seriesCate);
		return new ResponseEntity<>(savedSeriesCategory, HttpStatus.CREATED);
	}
	
	
	//조회
	@GetMapping("/series/list")
	public List<SeriesCateDTO> getAllSeriesCategory() {
		return seriesCateService.getAllSeriesCategory();
	}
	
	
	
	//수정
	@PutMapping("/series/update/{id}")
	public ResponseEntity<SeriesCateDTO> updateSeriesCategory(@PathVariable int id, SeriesCateDTO seriesCateDto) {
		SeriesCateDTO updatedSeriesCategory = seriesCateService.updateSeriesCategory(id, seriesCateDto);
		return ResponseEntity.ok(updatedSeriesCategory);
	}
	
	
	
	//삭제
	@DeleteMapping("/series/delete/{id}")
	public void deleteSeriesCategory(@PathVariable int id) {
		boolean deleted = seriesCateService.deleteSeriesCategory(id);
		if (!deleted) {
			throw new RuntimeException("시리즈 카테고리 삭제 실패");
		}
	}
	
	
}// end
