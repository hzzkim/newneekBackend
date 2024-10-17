package com.newneek.post;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
	public PostService() {
        System.out.println("-----PostService() 객체 생성");
    }
	
	@Autowired
    private PostRepository postRepository;

	// 게시글 저장
    public Post savePost(Post post) {
        post.setDate(LocalDateTime.now());  // 생성 시간 설정
        return postRepository.save(post);
    }
}
