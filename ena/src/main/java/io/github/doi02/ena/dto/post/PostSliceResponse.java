package io.github.doi02.ena.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostSliceResponse<T> {
    private List<T> content;     // 실제 데이터 리스트
    private boolean hasNext;     // 다음 페이지가 있는지 여부 (무한 스크롤용)
    private int currentPage;     // 현재 페이지 번호

    public static <T> PostSliceResponse<T> from(Slice<T> slice) {
        return new PostSliceResponse<>(
                slice.getContent(),
                slice.hasNext(),
                slice.getNumber()
        );
    }
}
