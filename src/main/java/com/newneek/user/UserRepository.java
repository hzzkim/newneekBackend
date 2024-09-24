package com.newneek.user;

import org.springframework.data.jpa.repository.JpaRepository;
import com.newneek.user.User;

public interface UserRepository extends JpaRepository<User,Long> {
	
}
