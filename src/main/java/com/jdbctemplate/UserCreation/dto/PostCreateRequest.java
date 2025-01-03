package com.jdbctemplate.UserCreation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostCreateRequest {

    private Long id;
    private String title;
    private String description;
    private Long userId;
}