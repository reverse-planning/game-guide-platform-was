package io.github.doi02.ena.repsository;

import io.github.doi02.ena.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p join fetch p.game join fetch p.user")
    Slice<Post> findAllByOrderByUpdatedAtDesc(Pageable pageable);

    // 제목, 본문, 작성자 닉네임 중 하나라도 키워드를 포함하면 조회
    @Query("select p from Post p " +
            "join fetch p.game " +
            "join fetch p.user " +
            "where p.title like %:kw% " +
            "or p.body like %:kw% " +
            "or p.user.nickname like %:kw%")
    Slice<Post> findByKeyword(@Param("kw") String kw, Pageable pageable);
}