package com.jdbctemplate.UserCreation.ServiceIntegrationTest;

import com.jdbctemplate.UserCreation.AbstractIntegrationTest;
import com.jdbctemplate.UserCreation.entity.PostEntity;
import com.jdbctemplate.UserCreation.repos.PostRepos;
import com.jdbctemplate.UserCreation.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Commit

public class PostServiceIntegrationTest extends AbstractIntegrationTest {


    @Autowired
    private PostService postService;

    @Autowired
    private PostRepos postRepos;

    @Test
    void createPost() {
        PostEntity postEntity1 = new PostEntity();
        postEntity1.setTitle("Post 1");
        postEntity1.setDescription("Description 1");
        postEntity1.setUserId(1L);

        postService.createPost(postEntity1);

        PostEntity createdPost = postRepos.findById(postEntity1.getId());
        assertNotNull(createdPost);
        assertEquals("Post 1", createdPost.getTitle());
        assertEquals("Description 1", createdPost.getDescription());

        PostEntity postEntity2 = new PostEntity();
        postEntity2.setTitle("Post 2");
        postEntity2.setDescription("Description 2");
        postEntity2.setUserId(2L);
        postService.createPost(postEntity2);

        PostEntity createdPost2 = postRepos.findById(postEntity2.getId());
        assertNotNull(createdPost2);
        assertEquals("Post 2", createdPost2.getTitle());
        assertEquals("Description 2", createdPost2.getDescription());

        postEntity1.setTitle("Updated Post 1");
        postEntity1.setDescription("Updated Description 1");
        postService.updatePost(postEntity1.getId(), postEntity1);

        PostEntity updatedPost = postRepos.findById(postEntity1.getId());
        assertNotNull(updatedPost);
        assertEquals("Updated Post 1", updatedPost.getTitle());
        assertEquals("Updated Description 1", updatedPost.getDescription());

        postService.deletePost(postEntity1.getId());

        PostEntity deletedPost = postRepos.findById(postEntity1.getId());
        assertNull(deletedPost);
    }
}