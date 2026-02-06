package io.github.doi02.ena.controller;

import io.github.doi02.ena.dto.post.PostCreateRequest;
import io.github.doi02.ena.dto.post.PostListResponse;
import io.github.doi02.ena.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 공략 게시글 작성
    @PostMapping("/guide")
    public ResponseEntity<Long> createGuide(@RequestBody PostCreateRequest request) {
        Long postId = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(postId);
    }

    // 게시글 수정
    @PatchMapping("/guide/{id}")
    public ResponseEntity<Void> updateGuide(@PathVariable Long id, @RequestBody PostCreateRequest request) {
        postService.updatePost(id, request);
        return ResponseEntity.ok().build();
    }

    // 게시글 삭제
    @DeleteMapping("/guide/{id}")
    public ResponseEntity<Void> deleteGuide(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    // 게시글 검색
    // 호출 예시: /api/guide?query=키워드
    @GetMapping("/guide")
    public ResponseEntity<Slice<PostListResponse>> searchGuides(
            @RequestParam(value = "query", defaultValue = "") String query,
            Pageable pageable) {
        return ResponseEntity.ok(postService.searchGuides(query, pageable));
    }

    // 무한 스크롤 게시글 요청
    // 호출 예시: /api/guides?page=0&size=10
    @GetMapping("/guides")
    public ResponseEntity<Slice<PostListResponse>> getGuides(Pageable pageable) {
        return ResponseEntity.ok(postService.getGuides(pageable));
    }
}
