package com.jdbctemplate.UserCreation.service;

import com.jdbctemplate.UserCreation.dto.PostCreateRequest;
import com.jdbctemplate.UserCreation.dto.PostDto;
import com.jdbctemplate.UserCreation.entity.PostEntity;
import com.jdbctemplate.UserCreation.repos.PostRepos;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {
    private final PostRepos postRepos;
    private final List<PostDto> cache = new ArrayList<>();


    public PostDto createPost(PostCreateRequest postCreateRequest) {
        PostEntity postEntity = PostMapper.toEntity(postCreateRequest);
        PostEntity savedPost = postRepos.save(postEntity);
        return PostMapper.toDto(savedPost);
    }

    public void deletePost(Long postId) {
        postRepos.delete(postId);
    }


    public ArrayList<PostDto> getLastPosts() {
        if (!cache.isEmpty()) {
            log.info("Using cached latest posts...");
            return new ArrayList<>(cache);
        }

        List<PostEntity> latestPosts = postRepos.findLastPost(10);
        ArrayList<PostDto> postDtos = latestPosts.stream()
                .map(PostMapper::toDto)
                .collect(Collectors.toCollection(ArrayList::new));

        cache.clear();
        cache.addAll(postDtos);

        return postDtos;
    }

    public void updatePost(Long id, PostCreateRequest postEntity) {
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

