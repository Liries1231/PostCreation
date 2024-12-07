package com.jdbctemplate.UserCreation.controller;

import com.jdbctemplate.UserCreation.entity.PostEntity;
import com.jdbctemplate.UserCreation.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;


    @GetMapping("/last")
    public List<Map<String, Object>> lastPost() {
        return postService.getLastPosts();
    }

    @GetMapping("/page/{page}")
    public ResponseEntity<List<Map<String, Object>>> getPosts(@PathVariable int page,
                                                              @RequestParam(defaultValue = "10") int pageSize) {
        List<Map<String, Object>> posts = postService.pages(page, pageSize);

        return ResponseEntity.ok(posts);
    }


    @PostMapping
    public ResponseEntity<PostEntity> createPost(@RequestBody PostEntity postEntity) {
        postService.createPost(postEntity);
        return new ResponseEntity<>(postEntity, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody PostEntity postEntity) {

        postService.updatePost(id, postEntity);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
