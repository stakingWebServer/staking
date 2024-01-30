package kr.project.backend.entity.common;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InOutLog extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "varchar(38)")
    @Comment(value = "로그ID")
    private String logId;

    @Comment(value = "유저 키값")
    @Column(length = 38)
    private String userId;

    @Comment(value = "헤더")
    @Column(columnDefinition = "TEXT")
    private String header;

    @Comment(value = "uri")
    private String uri;

    @Comment(value = "요청 파라미터")
    @Column(columnDefinition = "TEXT")
    private String param;

    @Comment(value = "ip")
    private String ip;

    @Comment(value = "응답결과")
    @Column(columnDefinition = "TEXT")
    private String response;

    public InOutLog(String userId, String header, String uri, String param, String ip, String response){
        this.userId = userId;
        this.header = header;
        this.uri = uri;
        this.param = param;
        this.ip = ip;
        this.response = response;
    }

}
