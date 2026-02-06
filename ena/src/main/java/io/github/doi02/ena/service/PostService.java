package io.github.doi02.ena.service;

import io.github.doi02.ena.dto.post.PostCreateRequest;
import io.github.doi02.ena.dto.post.PostListResponse;
import io.github.doi02.ena.entity.Game;
import io.github.doi02.ena.entity.Post;
import io.github.doi02.ena.entity.User;
import io.github.doi02.ena.repsository.GameRepository;
import io.github.doi02.ena.repsository.PostRepository;
import io.github.doi02.ena.repsository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    // 게시글 작성
    public Long createPost(PostCreateRequest request) {
        Game game = gameRepository.findById(request.getGameId())
                .orElseThrow(() -> new IllegalArgumentException("게임이 존재하지 않습니다."));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        Post post = Post.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .user(user)
                .updatedAt(new Date())
                .build();

        return postRepository.save(post).getId();
    }

    //게시글 수정 (더티체크)
    @Transactional
    public void updatePost(Long id, PostCreateRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));

        // 데이터가 있을 때만 수정
        if (request.getTitle() != null) post.setTitle(request.getTitle());
        if (request.getBody() != null) post.setBody(request.getBody());

        post.setUpdatedAt(new Date()); // 수정 시간 갱신
    }

    //게시글 삭제
    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    // 게시글 검색 (GET)
    public Slice<PostListResponse> searchGuides(String keyword, Pageable pageable) {
        return postRepository.findByKeyword(keyword, pageable)
                .map(this::convertToResponse); // 중복되는 변환 로직은 메서드로 분리
    }
    private PostListResponse convertToResponse(Post p) {
        return new PostListResponse(
                p.getId(),
                p.getTitle(),
                p.getBody().substring(0, Math.min(p.getBody().length(), 100)),
                p.getGame().getName(),
                p.getUser().getNickname(),
                p.getUpdatedAt()
        );
    }

    // 게시글 목록 (무한 스크롤)
    public Slice<PostListResponse> getGuides(Pageable pageable) {
        return postRepository.findAllByOrderByUpdatedAtDesc(pageable)
                .map(p -> new PostListResponse(
                        p.getId(),
                        p.getTitle(),
                        p.getBody().substring(0, Math.min(p.getBody().length(), 100)), // 100자 요약
                        p.getGame().getName(),
                        p.getUser().getNickname(),
                        p.getUpdatedAt()
                ));
    }
}
