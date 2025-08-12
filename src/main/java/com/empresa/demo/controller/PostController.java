package com.empresa.demo.controller;

import com.empresa.demo.dto.PostDto;
import com.empresa.demo.network.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/external")
public class PostController {
    
    private final PostService postService;
    
    public PostController(PostService postService) {
        this.postService = postService;
    }
    
    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getPosts() {
        List<PostDto> posts = postService.getPosts();
        return ResponseEntity.ok(posts);
    }
} 