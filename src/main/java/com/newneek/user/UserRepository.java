package com.newneek.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
	public boolean existsByEmailAndPw(String email,String pw);
}
