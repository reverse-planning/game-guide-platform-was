package io.github.doi02.ena.service;

import io.github.doi02.ena.common.exception.BusinessException;
import io.github.doi02.ena.common.exception.ErrorCode;
import io.github.doi02.ena.dto.post.PostCreateRequest;
import io.github.doi02.ena.dto.post.PostDetailResponse;
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
    @Transactional
    public Long createPost(PostCreateRequest request) {
        Game game = gameRepository.findByName(request.getGame())
                .orElseThrow(() -> new BusinessException(ErrorCode.GAME_NOT_FOUND));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Post post = Post.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .user(user)
                .game(game)
                .updatedAt(new Date())
                .build();

        return postRepository.save(post).getId();
    }

    // 게시글 조회
    public PostDetailResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new BusinessException(ErrorCode.POST_NOT_FOUND));
        return PostDetailResponse.from(post);
    }

    //게시글 수정 (더티체크)
    @Transactional
    public void updatePost(Long postId, Long userId ,PostCreateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NOT_POST_OWNER);
        }
        // 데이터가 있을 때만 수정
        if (request.getTitle() != null) post.setTitle(request.getTitle());
        if (request.getBody() != null) post.setBody(request.getBody());

        post.setUpdatedAt(new Date()); // 수정 시간 갱신
    }

    //게시글 삭제
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
        if (!post.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NOT_POST_OWNER);
        }
        postRepository.deleteById(postId);
    }

    // 게시글 검색 (GET)
    public Slice<PostListResponse> searchGuides(String keyword, Pageable pageable) {
        return postRepository.findByKeyword(keyword, pageable)
                .map(this::convertToResponse);
    }

    // 게시글 목록 (무한 스크롤)
    public Slice<PostListResponse> getGuides(Pageable pageable) {
        return postRepository.findAllByOrderByUpdatedAtDesc(pageable)
                .map(this::convertToResponse);
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
}
