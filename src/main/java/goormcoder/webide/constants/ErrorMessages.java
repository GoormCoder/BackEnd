package goormcoder.webide.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {

    //404 NOT_FOUND
    MEMBER_NOT_FOUND("해당하는 사용자가 존재하지 않습니다."),
    QUESTION_NOT_FOUND("해당하는 문제가 존재하지 않습니다."),
    BOARD_NOT_FOUND("해당하는 게시글이 존재하지 않습니다."),
    COMMENT_NOT_FOUND("해당하는 댓글이 존재하지 않습니다."),
    LIKE_NOT_FOUND("해당하는 좋아요가 존재하지 않습니다."),
    TESTCASE_NOT_FOUND("해당하는 테스트케이스가 존재하지 않습니다."),
    BATTLE_WAIT_NOT_FOUND("해당하는 대기방이 존재하지 않습니다."),
    SOLVE_NOT_FOUND("해당하는 제출이 존재하지 않습니다."),
    CHATROOM_NOT_FOUND("해당하는 채팅방이 존재하지 않습니다."),
    FRIEND_REQUEST_NOT_FOUND("해당하는 친구요청이 존재하지 않습니다."),
    TAG_NOT_FOUND("해당하는 태그가 존재하지 않습니다."),
    WRONG_INPUT_DATA("잘못된 입력입니다."),
    
    //409 CONFLICT
    LIKE_CONFLICT("이미 좋아요를 누르셨습니다."),
    BATTLE_MEMBER_CONFLICT("같은 사용자가 두 명의 역할을 맡을 수 없습니다."),
    CHATROOM_CONFLICT("해당 사용자와의 채팅방이 이미 존재합니다."),

    //401 UNAUTHORIZED
    JWT_UNAUTHORIZED_EXCEPTION("사용자 검증에 실패했습니다."),
    JWT_FORBIDDEN_ACCESS("접근 권한이 없습니다."),
    JWT_USER_NOT_FOUND_EXCEPTION("존재하지 않는 사용자입니다."),
    JWT_BAD_CREDENTIAL_EXCEPTION("아이디 또는 비밀번호가 잘못 되었습니다."),
    TOKEN_NOT_FOUND("존재하지 않는 토큰입니다."),
    TOKEN_EXPIRED("만료된 토큰입니다."),

    //403 FORBIDDEN
    FORBIDDEN_ACCESS("해당 요청에 대한 접근 권한이 없습니다."),
    FORBIDDEN_SOLVE_ACCESS("해당 제출에 대한 접근 권한이 없습니다."),
    FORBIDDEN_FRIEND_REQUEST_ACCESS("해당 친구 요청에 대한 접근 권한이 없습니다."),
    FORBIDDEN_CHATROOM_ACCESS("해당 채팅방에 대한 접근 권한이 없습니다."),

    //400 BAD_REQUEST
    BAD_REQUEST_INVITED_ID("본인과의 채팅방은 생성할 수 없습니다."),
    UNAVAILABLE_LANGUAGE("지원하지 않는 언어입니다."),
  
    // 500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR("서버 오류입니다."),

    ;

    private final String message;

}
