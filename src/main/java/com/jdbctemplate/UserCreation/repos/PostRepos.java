package com.jdbctemplate.UserCreation.repos;

import com.jdbctemplate.UserCreation.entity.PostEntity;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class PostRepos {

    private final JdbcTemplate jdbcTemplate;

    public void save(PostEntity post) {
        String sql = "INSERT INTO posts (title, description, user_id) VALUES (?, ?, ?) RETURNING id";
        Long generatedId = jdbcTemplate.queryForObject(sql, new Object[]{post.getTitle(), post.getDescription(), post.getUserId()}, Long.class);
        post.setId(generatedId);
    }
    public List<Map<String, Object>> findPostsByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;

        String sql = "SELECT id, title, created_at FROM posts ORDER BY created_at DESC LIMIT ? OFFSET ?";

        return jdbcTemplate.queryForList(sql, pageSize, offset);
    }
    public void update(PostEntity post) {
        String sql = "UPDATE posts SET title = ?, description = ?, user_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, post.getTitle(), post.getDescription(), post.getUserId(), post.getId());
    }

     public List<Map<String,Object>> findLastPost(int limit) {
         String sql = "SELECT id, title, created_at FROM posts ORDER BY created_at DESC LIMIT ?";

         return jdbcTemplate.queryForList(sql,limit);
     }


    public void delete(Long id) {
        String sql = "DELETE FROM posts WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
    public PostEntity findById(Long id) {
        String sql = "SELECT id, title, description, user_id FROM posts WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<>(PostEntity.class));
        } catch (EmptyResultDataAccessException e) {
            return  null;
        }
    }



}
