package kr.project.backend.entity.user;

import jakarta.persistence.*;
import kr.project.backend.converter.BooleanToYNConverter;
import kr.project.backend.dto.user.request.UseClauseDto;
import kr.project.backend.entity.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "varchar(38)")
    @Comment(value = "공지사항ID")
    private String noticeId;

    @Comment(value = "공지사항 제목")
    @Column(length = 100)
    private String noticeTitle;

    @Comment(value = "공지사항 내용")
    private String noticeContent;

    @OneToMany(mappedBy = "notice")
    private List<UserNoticeRead> userNoticeReads;

}
