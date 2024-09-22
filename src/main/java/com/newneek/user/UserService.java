package com.newneek.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public UserDto createMember(UserDto userDto) {
		User user = convertToEntity(userDto);
		
		user = userRepository.save(user);
		return convertToDto(user);
	}
	
	private UserDto convertToDto(User user) {
		return new UserDto(
				user.getUserId(),
				user.getEmail(),
				user.getPassword()
				);
	}
	
	private User convertToEntity(UserDto userDto) {
		User user = new User();
		user.setUserId(userDto.getUserId());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		return user;
	}
}
