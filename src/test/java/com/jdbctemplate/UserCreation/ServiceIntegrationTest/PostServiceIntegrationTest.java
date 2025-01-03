package com.jdbctemplate.UserCreation.ServiceIntegrationTest;

import com.jdbctemplate.UserCreation.AbstractIntegrationTest;
import com.jdbctemplate.UserCreation.dto.PostCreateRequest;
import com.jdbctemplate.UserCreation.dto.PostDto;
import com.jdbctemplate.UserCreation.entity.PostEntity;
import com.jdbctemplate.UserCreation.repos.PostRepos;
import com.jdbctemplate.UserCreation.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Commit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Commit
@Slf4j
public class PostServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final Map<String, List<Map<String, Object>>> cache = new HashMap<>();

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepos postRepos;

//    @Test
//    void createPost() {
//        PostCreateRequest postCreateRequest1 = new PostCreateRequest();
//        postCreateRequest1.setTitle("Post 1");
//        postCreateRequest1.setDescription("Description 1");
//        postCreateRequest1.setUserId(1L);
//
//        PostDto createResponse1 = postService.createPost(postCreateRequest1);
//
//        PostEntity createdPost = postRepos.findById(createResponse1.getId());
//        assertNotNull(createdPost);
//        assertEquals("Post 1", createdPost.getTitle());
//        assertEquals("Description 1", createdPost.getDescription());
//
//        PostCreateRequest postCreateRequest2 = new PostCreateRequest();
//        postCreateRequest2.setTitle("Post 2");
//        postCreateRequest2.setDescription("Description 2");
//        postCreateRequest2.setUserId(2L);
//
//        PostDto createResponse2 = postService.createPost(postCreateRequest2);
//
//        PostEntity createdPost2 = postRepos.findById(createResponse2.getId());
//        assertNotNull(createdPost2);
//        assertEquals("Post 2", createdPost2.getTitle());
//        assertEquals("Description 2", createdPost2.getDescription());
//
//        postCreateRequest1.setTitle("Updated Post 1");
//        postCreateRequest1.setDescription("Updated Description 1");
//
//        postService.updatePost(createResponse1.getId(), postCreateRequest1);
//
//        PostEntity updatedPost = postRepos.findById(createResponse1.getId());
//        assertNotNull(updatedPost);
//        assertEquals("Updated Post 1", updatedPost.getTitle());
//        assertEquals("Updated Description 1", updatedPost.getDescription());
//
//        postService.deletePost(createResponse1.getId());
//
//        PostEntity deletedPost = postRepos.findById(createResponse1.getId());
//        assertNull(deletedPost);
//    }


    @Test
    void latest() {
        String cacheKey1 = "latest_posts1";
        String cacheKey2 = "latest_posts2";
        String cacheKey3 = "latest_posts3";


        log.info("First search:");
        testLatestPostsCaching(cacheKey1);

        log.info("Second search:");
        testLatestPostsCaching(cacheKey2);

        log.info("Third search:");

        PostEntity postEntity1 = new PostEntity();
        postEntity1.setTitle("Post 321312");
        postEntity1.setDescription("Description 321312");
        postEntity1.setUserId(77777L);
        String sqlInsert = "INSERT INTO post (title, description, user_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlInsert, postEntity1.getTitle(), postEntity1.getDescription(), postEntity1.getUserId());
        testLatestPostsCaching(cacheKey3);
        testLatestPostsCaching(cacheKey3);
        testLatestPostsCaching(cacheKey3);
        testLatestPostsCaching(cacheKey3);
        testLatestPostsCaching(cacheKey3);
        testLatestPostsCaching(cacheKey3);

        log.debug("Cache content: {}", cache);


    }

    public void testLatestPostsCaching(String cacheKey) {
        List<Map<String, Object>> cachedPosts = cache.get(cacheKey);
        if (cachedPosts != null) {
            log.info("Using cached latest posts...");
            log.debug("Total size (cached): {}", cachedPosts.size());
        } else {
            String sql = "SELECT id, title, created_at FROM post ORDER BY created_at DESC LIMIT 10";
            long start = System.currentTimeMillis();
            List<Map<String, Object>> posts = jdbcTemplate.queryForList(sql);
            long finish = System.currentTimeMillis();
            log.debug("Time " + (finish - start) + " ms");
            log.debug(cacheKey, posts);


        }


    }

    @Test
    void testFindPostsByTitleTwice() {
        String search1 = "5";
        String search2 = "1";

        if (log.isInfoEnabled()) {
            log.info("First search:");
        }
        findPostsByTitle(search1);

        log.info("Second search:");
        findPostsByTitle(search2);
    }

    void findPostsByTitle(String search) {


        List<Map<String, Object>> cachedPosts = cache.get(search);

        if (cachedPosts != null) {
            log.info("Using cached latest posts...");
            log.debug("Total size (cached): {}", cachedPosts.size());


        } else {
            String sql = "SELECT id, title, created_at FROM post ORDER BY created_at DESC LIMIT 10";

            long start = System.currentTimeMillis();
            List<Map<String, Object>> posts = jdbcTemplate.queryForList(sql);

            List<Map<String, Object>> filteredPosts = posts.stream()
                    .filter(post -> post.get("title").toString().contains(search))
                    .toList();
            long finish = System.currentTimeMillis();

            assertThat(posts).isNotEmpty();

            // posts.forEach(post -> System.out.println("Found post: " + filteredPosts));
            log.info("Time {} ms", finish - start);
            log.info("Total size: {}", filteredPosts.size());
            cache.put(search, filteredPosts);
            log.info("cache: {}", cache);


        }


    }
}