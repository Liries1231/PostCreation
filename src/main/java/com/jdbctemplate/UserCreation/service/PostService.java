package com.jdbctemplate.UserCreation.service;

import com.jdbctemplate.UserCreation.entity.PostEntity;
import com.jdbctemplate.UserCreation.repos.PostRepos;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepos postRepos;
    private final Map<String, List<Map<String, Object>>> cache = new HashMap<>();

    public void createPost(PostEntity postEntity) {
        postRepos.save(postEntity);
    }

    public void deletePost(Long postId) {
        postRepos.delete(postId);
    }

    public List<Map<String, Object>> getLastPosts() {
        String cacheKey = "latest_posts";
        List<Map<String, Object>> cachedPosts = cache.get(cacheKey);

        if (cachedPosts != null) {
            System.out.println("Using cached latest posts...");
            return cachedPosts;
        }
        List<Map<String, Object>> latestPosts = postRepos.findLastPost(10);

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

    public List<Map<String, Object>> pages(int page, int size) {
        String cacheKey = "pages_" + page;
        List<Map<String, Object>> latestPosts = postRepos.findPostsByPage(page, size);
        cache.put(cacheKey, latestPosts);
        return latestPosts;
    }
}


