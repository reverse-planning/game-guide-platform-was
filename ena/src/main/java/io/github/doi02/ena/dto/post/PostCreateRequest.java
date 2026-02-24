package io.github.doi02.ena.dto.post;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCreateRequest {
    private String title;
    private String body;
    private String game;
}
