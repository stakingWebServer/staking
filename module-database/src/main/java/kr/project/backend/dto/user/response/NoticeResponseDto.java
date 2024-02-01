package kr.project.backend.dto.user.response;

import kr.project.backend.entity.user.Notice;
import lombok.Data;

@Data
public class NoticeResponseDto {

    private String noticeId;

    private String noticeTitle;

    private String noticeContent;

    private String noticeDate;

    private String noticeReadYn;

    public NoticeResponseDto(Notice notice, String noticeReadYn) {
        this.noticeId = notice.getNoticeId();
        this.noticeTitle = notice.getNoticeTitle();
        this.noticeContent = notice.getNoticeContent();
        this.noticeDate = notice.getCreatedDate().substring(0,10);
        this.noticeReadYn = noticeReadYn;
    }
}
