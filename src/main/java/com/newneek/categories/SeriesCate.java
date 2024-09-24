package com.newneek.categories;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table( name = "series_category" )
@Getter
@Setter
public class SeriesCate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "series_category_id")
	private int seriesCategoryId;
	private String name;
}
