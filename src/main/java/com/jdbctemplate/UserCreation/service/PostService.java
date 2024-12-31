package com.jdbctemplate.UserCreation.service;

import com.jdbctemplate.UserCreation.entity.PostEntity;
import com.jdbctemplate.UserCreation.repos.PostRepos;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {
    private final PostRepos postRepos;
    private final Map<String, List<PostEntity>> cache = new HashMap<>();


    public void createPost(PostEntity postEntity) {
        postRepos.save(postEntity);
    }


    public void deletePost(Long postId) {
        postRepos.delete(postId);
    }


    public List<PostEntity> getLastPosts() {
        String cacheKey = "latest_posts";
        List<PostEntity> cachedPosts = cache.get(cacheKey);

        if (cachedPosts != null) {
            log.info("Using cached latest posts...");
            return cachedPosts;
        }

        List<PostEntity> latestPosts = postRepos.findLastPost(10); // Предполагается, что этот метод уже возвращает List<PostEntity>
        cache.put(cacheKey, latestPosts);

        return latestPosts;
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


    public List<PostEntity> pages(int page, int size) {
        String cacheKey = "pages_" + page;

        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        List<PostEntity> latestPosts = postRepos.findPostsByPage(page, size);
        cache.put(cacheKey, latestPosts);

        return latestPosts;
    }
}
