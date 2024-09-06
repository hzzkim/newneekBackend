package com.newneek.categories;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table( name = "ground_category" )
@Getter
@Setter
public class GroundCate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ground_category_id")
	private int groundCategoryId;
	private String name;
}
