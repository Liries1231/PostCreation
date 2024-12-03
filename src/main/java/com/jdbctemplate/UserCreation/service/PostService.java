package com.jdbctemplate.UserCreation.service;

import com.jdbctemplate.UserCreation.entity.PostEntity;
import com.jdbctemplate.UserCreation.repos.PostRepos;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepos postRepos;

    public void createPost(PostEntity postEntity) {
        postRepos.save(postEntity);
    }

    public void deletePost(Long postId) {
        postRepos.delete(postId);
    }

    public void updatePost(Long id, PostEntity postEntity) {
        PostEntity post = postRepos.findById(id);
        if (post != null) {
            post.setDescription(postEntity.getDescription());
            post.setTitle(postEntity.getTitle());
            post.setUserId(postEntity.getUserId());
            postRepos.update(post);
        } else {
            throw new EntityNotFoundException("Post with id " + id + " not found");
        }
    }

}
