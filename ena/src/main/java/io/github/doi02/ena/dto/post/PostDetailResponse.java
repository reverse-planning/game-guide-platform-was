package io.github.doi02.ena.dto.post;

import io.github.doi02.ena.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
@Builder
public class PostDetailResponse {
    private Long id;
    private String title;
    private String body;
    private String game;
    private String author;
    private Date updatedAt;
    private Long viewCount;

    public static PostDetailResponse from(Post post) {
        return PostDetailResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .game(post.getGame().getName())
                .author(post.getUser().getNickname())
                .updatedAt(post.getUpdatedAt())
                .viewCount(post.getViewCount())
                .build();
    }
}
