package com.jdbctemplate.UserCreation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDto {

    private Long id;
    private String title;
    private String description;
    private Long userId;
    private LocalDateTime createdAt;
    public PostDto(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
    @Override
    public String toString() {
        return "PostDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}