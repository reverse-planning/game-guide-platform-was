package io.github.doi02.ena.controller;

import io.github.doi02.ena.common.config.LoginUser;
import io.github.doi02.ena.dto.post.*;
import io.github.doi02.ena.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Post", description = "게시글 관련 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 작성
    @Operation(
            summary = "게시글 작성",
            description = "사용자가 게시판에 글을 작성합니다."
    )
    @ApiResponse(responseCode = "201", description = "작성 성공")
    @ApiResponse(responseCode = "404", description = "게임을 찾을 수 없음")
    @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
    @PostMapping("/guides")
    public ResponseEntity<Long> createGuide(
            @Parameter(hidden = true) @LoginUser Long userId,
            @RequestBody PostCreateRequest request) {
        Long postId = postService.createPost(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(postId);
    }

    // 게시글 상세 페이지
    @Operation(
            summary = "게시글 상세 페이지 요청",
            description = "사용자가 게시글의 상세를 확인합니다."
    )
    @ApiResponse(responseCode = "200", description = "작성 성공")
    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    //@ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터")
    @GetMapping("/guides/{id}")
    public ResponseEntity<PostDetailResponse> getGuide(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    // 게시글 수정
    @Operation(
            summary = "게시글 수정",
            description = "사용자가 자신의 게시글을 수정합니다."
    )
    @ApiResponse(responseCode = "200", description = "수정 성공")
    @ApiResponse(responseCode = "403", description = "다른 사용자가 게시글 수정을 시도함")
    //@ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터")
    //@ApiResponse(responseCode = "400", description = "존재하지 않는 게시글")
    @PatchMapping("/guides/{id}")
    public ResponseEntity<Void> updateGuide(
            @PathVariable(name = "id") Long postId,
            @Parameter(hidden = true) @LoginUser Long userId,
            @RequestBody PostCreateRequest request) {

        postService.updatePost(postId, userId, request);
        return ResponseEntity.ok().build();
    }

    // 게시글 삭제
    @Operation(
            summary = "게시글 삭제",
            description = "사용자가 자신의 게시글을 삭제합니다."
    )
    @ApiResponse(responseCode = "204", description = "삭제 성공")
    @ApiResponse(responseCode = "403", description = "다른 사용자가 게시글 삭제를 시도함")
    //@ApiResponse(responseCode = "400", description = "존재하지 않는 게시글")
    @DeleteMapping("/guides/{id}")
    public ResponseEntity<Void> deleteGuide(
            @PathVariable Long id,
            @Parameter(hidden = true) @LoginUser Long userId) {
        postService.deletePost(id, userId);
        return ResponseEntity.noContent().build();
    }

    // 게시글 검색
    @Operation(
            summary = "게시글 검색",
            description = "사용자가 게시글을 검색합니다. 호출 예시: /api/guide?query=키워드"
    )
    @ApiResponse(responseCode = "200", description = "검색 성공")
    //@ApiResponse(responseCode = "204", description = "아무 게시글도 존재하지 않음")
    //@ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터")
    //@ApiResponse(responseCode = "400", description = "검색 실패")
    @GetMapping("/guides")
    public ResponseEntity<PostSliceResponse<PostListResponse>> searchGuides(
            @RequestParam(value = "query", defaultValue = "") String query,
            Pageable pageable) {
        Slice<PostListResponse> result = postService.searchGuides(query, pageable);
        return ResponseEntity.ok(PostSliceResponse.from(result));
    }
}
