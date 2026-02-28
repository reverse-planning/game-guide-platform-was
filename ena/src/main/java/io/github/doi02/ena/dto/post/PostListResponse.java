package io.github.doi02.ena.dto.post;

import io.github.doi02.ena.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
@Builder
public class PostListResponse {
    private Long id;
    private String title;
    private String excerpt;
    private String game;
    private String author;
    private Date updatedAt;
    private Long viewCount;

    public static PostListResponse from(Post post) {
        return PostListResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .excerpt(post.getBody().substring(0, Math.min(post.getBody().length(), 100)))
                .game(post.getGame().getName())
                .author(post.getUser().getNickname())
                .updatedAt(post.getUpdatedAt())
                .viewCount(post.getViewCount())
                .build();
    }
}
