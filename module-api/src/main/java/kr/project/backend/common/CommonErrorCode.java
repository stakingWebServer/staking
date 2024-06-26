package kr.project.backend.common;


public enum CommonErrorCode {
    // common
    SUCCESS("SUCCESS", "정상처리"),
    FAIL("FAIL", "실패"),
    COMMON_FAIL("A000", "실패"),
    NULL_DATA("A001","데이터가 없습니다."),
    NOT_ALLOW_FILE("A002","허용되지 않는 파일입니다."),
    NOT_FOUND_FILE("A003","파일을 찾을 수 없습니다."),
    $_NOT_FOUND_FILE("A004","[$fileId] 파일을 찾을 수 없습니다."),

    // token
    EXPIRED_TOKEN("B000","만료된 토큰입니다."),
    WRONG_TOKEN("B001","형식이 잘못된 토큰입니다."),
    NOT_EXIST_TOKEN("B002","존재하지 않는 토큰입니다."),
    NOT_FOUND_TOKEN("B003","토큰정보를 찾을 수 없습니다."),

    // user
    NOT_FOUND_USER("C000","회원을 찾을 수 없습니다."),
    ALREADY_DROP_USER("C001","이미 탈퇴된 회원입니다."),
    JOIN_TERM_DATE("C002","탈퇴 30일후 재가입 가능합니다."),
    ALREADY_JOIN_USER("C003","이미 가입된 회원입니다."),
    NOT_FOUND_FAVORITE("C004","없는 즐겨찾기 정보입니다."),
    NOT_JOIN_USER("C005","회원가입이 필요한 회원입니다."),
    NOT_FOUND_STAKING_DATA("C006","마이 스테이킹 데이터를 찾을수 없습니다."),
    NOT_FOUND_USE_CLAUSE_DATA("C007","이용약관을 찾을 수 없습니다. 약관ID를 확인해주세요."),
    NOT_AGREE_ESSENTIAL_USE_CLAUSE("C008","필수 이용약관은 반드시 동의해야 합니다."),
    NOT_FOUND_USE_CLAUSE("C009","존재하지 않는 항목입니다."),
    NOT_FOUND_USER_USE_CLAUSE("C010","선택한 항목이 아닙니다."),
    NOT_FOUND_NOTICE("C011","공지사항을 찾을 수 없습니다."),
    NOT_FOUND_REPLY("C012","답변을 찾을 수 없습니다."),
    NOT_FOUND_BEFORE_USE_CLAUSE("C013","이전 약관을 찾을 수 없습니다."),
    ALREADY_REPLY("C014","이미 답변된 문의입니다."),

    // coin
    NOT_FOUND_COIN("D000","없는 코인정보입니다."),
    NOT_INPUT_COIN("D001","보상주기가 없는 코인은 수량을 입력할 수 없습니다."),
    CHECK_MIN_INPUT_COIN("D002","최소 신청 수량을 확인해주세요."),

    // favorite
    ALREADY_EXIST_STAKING_DATA("E000","이미 존재하는 마이스테이킹데이터 입니다."),

    // question
    NOT_FOUND_QUESTION("F000","해당 문의를 찾을수 없습니다."),
    // push
    FAIL_PUSH("H000","푸쉬 전송이 실패 하였습니다."),
    // time
    NOT_SEND_TIME("G000","발송불가능한 시간입니다.")
    ;





    private final String code;
    private final String message;

    CommonErrorCode(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
