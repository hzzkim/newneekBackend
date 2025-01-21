package com.newneek.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,String> {
	public boolean existsByEmailAndPw(String email,String pw);

	Optional<User> findByEmail(String email);
	
	boolean existsByUserId(String userId);
}
