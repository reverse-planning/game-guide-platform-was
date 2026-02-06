package io.github.doi02.ena.dto.post;

import lombok.Getter;
import lombok.AllArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
public class PostListResponse {
    private Long id;
    private String title;
    private String excerpt;
    private String game;
    private String author;
    private Date updatedAt;
}
