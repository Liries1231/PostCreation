package com.jdbctemplate.UserCreation.service;

import com.jdbctemplate.UserCreation.dto.PostCreateRequest;
import com.jdbctemplate.UserCreation.dto.PostDto;
import com.jdbctemplate.UserCreation.entity.PostEntity;

public class PostMapper {
    public static PostEntity toEntity(PostCreateRequest request) {
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(request.getTitle());
        postEntity.setDescription(request.getDescription());
        postEntity.setUserId(request.getUserId());
        return postEntity;
    }

    public static PostDto toDto(PostEntity entity) {
        PostDto postDto = new PostDto();
        postDto.setId(entity.getId());
        postDto.setTitle(entity.getTitle());
        postDto.setDescription(entity.getDescription());
        postDto.setUserId(entity.getUserId());
        return postDto;
    }

}
