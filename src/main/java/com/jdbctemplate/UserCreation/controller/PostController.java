package com.jdbctemplate.UserCreation.controller;

import com.jdbctemplate.UserCreation.dto.PostCreateRequest;
import com.jdbctemplate.UserCreation.dto.PostDto;
import com.jdbctemplate.UserCreation.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;


    @GetMapping("/last")
    public ArrayList<PostDto> lastPost() {
        return postService.getLastPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") Long id) {
        PostDto post = postService.getPostById(id);
        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @PostMapping()
    public ResponseEntity<String> createPost(@RequestBody PostCreateRequest post,
                                             @RequestHeader("UserData") String userId) {


        Long userIdLong = Long.valueOf(userId);
        post.setUserId(userIdLong);

        postService.createPost(post);

        return ResponseEntity.ok("Post created successfully!");
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
