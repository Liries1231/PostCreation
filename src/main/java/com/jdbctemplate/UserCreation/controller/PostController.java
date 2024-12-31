package com.jdbctemplate.UserCreation.controller;

import com.jdbctemplate.UserCreation.dto.PostCreateRequest;
import com.jdbctemplate.UserCreation.dto.PostDto;
import com.jdbctemplate.UserCreation.entity.PostEntity;
import com.jdbctemplate.UserCreation.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;


    @GetMapping("/last")
    public ArrayList<PostDto> lastPost() {
        return postService.getLastPosts();
    }



    @PostMapping
    public PostDto createPost(@RequestBody PostCreateRequest postCreateRequest) {
        return postService.createPost(postCreateRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody PostCreateRequest postEntity) {

        postService.updatePost(id, postEntity);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
