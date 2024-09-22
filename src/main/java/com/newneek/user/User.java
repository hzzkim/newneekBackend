package com.newneek.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	@Id
	@Column(name = "user_id", columnDefinition = "VARCHAR(36)", nullable = false)
	private String userId;
	
	@Column(name = "email", columnDefinition = "VARCHAR(50)", nullable = false)
	private String email;
	
	@Column(name = "pw", columnDefinition = "VARCHAR(20)", nullable = false)
	private String password;
}
