package com.newneek.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostController {
	
	public PostController() {
		System.out.println("-----PostController() 객체생성");
	}
	
	@Autowired
	private PostService postSevice;
	
	@PostMapping
	public ResponseEntity<Post> createPost(@RequestBody Post post) {
       System.out.println("요청 포스트"+ post);
		Post savedPost = postSevice.savePost(post);
        return ResponseEntity.ok(savedPost);

        
    }

}
