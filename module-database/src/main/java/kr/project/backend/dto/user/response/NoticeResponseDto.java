package kr.project.backend.dto.user.response;

import kr.project.backend.entity.user.Notice;
import lombok.Data;

@Data
public class NoticeResponseDto {

    private String noticeId;

    private String noticeTitle;

    private String noticeContent;

    private String noticeDate;

    public NoticeResponseDto(Notice notice) {
        this.noticeId = notice.getNoticeId();
        this.noticeTitle = notice.getNoticeTitle();
        this.noticeContent = notice.getNoticeContent();
        this.noticeDate = notice.getCreatedDate().substring(0,10);
    }
}
