package io.github.doi02.ena.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 공통 에러
    INVALID_INPUT_VALUE(400, "C001", "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(405, "C002", "허용되지 않은 메서드입니다."),
    INTERNAL_SERVER_ERROR(500, "C003", "서버 내부에 오류가 발생했습니다."),

    // 게시글(Post) 관련 에러
    POST_NOT_FOUND(404, "P001", "해당 게시글을 찾을 수 없습니다."),
    POST_DELETE_NOT_ALLOWED(404, "P002", "해당 사용자는 게시글을 삭제할 수 없습니다."),
    NOT_POST_OWNER(403, "P003", "해당 사용자는 게시글을 수정할 권한이 없습니다."),
    // 사용자(User) 관련 에러
    USER_NOT_FOUND(404, "U001", "해당 사용자를 찾을 수 없습니다."),

    // 게임(Game) 관련 에러
    GAME_NOT_FOUND(404, "G001", "해당 게임을 찾을 수 없습니다.");

    private final int status;
    private final String code;
    private final String message;
}
