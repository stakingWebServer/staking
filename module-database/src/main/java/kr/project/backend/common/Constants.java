package kr.project.backend.common;

public class Constants {

    /** 회원상태 코드 */
    public static final class USER_STATE {

        public static final String CODE = "USER_STATE";

        public static final String ACTIVE_USER = "01"; //정상

        public static final String REST_USER = "02"; //휴면

        public static final String LOCK_USER = "03"; //잠김

        public static final String DROP_USER = "04"; //탈퇴
    }

    /** 회원가입 구분 코드 */
    public static final class USER_JOIN_SNS_KIND {

        public static final String CODE = "USER_JOIN_SNS_KIND";

        public static final String KAKAO = "01"; //카카오

        public static final String NAVER = "02"; //네이버

        public static final String GOOGLE = "03"; //구글

        public static final String APPLE = "04"; //애플
    }

    /** 회원가입 os 구분 코드 */
    public static final class USER_JOIN_OS_KIND {

        public static final String CODE = "USER_JOIN_OS_KIND";

        public static final String AOS = "01"; //안드로이드

        public static final String IOS = "02"; //애플

        public static final String WINDOW = "03"; //윈도우

        public static final String MAC = "04"; //맥
    }

    /** 이용약관 상태 코드 */
    public static final class USE_CLAUSE_STATE {

        public static final String CODE = "USE_CLAUSE_STATE";

        public static final String STAY = "01"; //대기중

        public static final String NOTICE = "02"; //공지중

        public static final String APPLY = "03"; //적용중

        public static final String END = "04"; //종료
    }

    /** 이용약관 구분 코드 */
    public static final class USE_CLAUSE_KIND {

        public static final String CODE = "USE_CLAUSE_KIND";

        public static final String USE_CLAUSE = "01"; //이용약관

        public static final String INDIVIDUAL_INFO = "02"; //개인정보처리방침

        public static final String ADVERTISEMENT_PUSH = "03"; //광고성푸시
    }

}
