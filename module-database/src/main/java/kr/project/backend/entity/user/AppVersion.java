package kr.project.backend.entity.user;

import jakarta.persistence.*;
import kr.project.backend.converter.BooleanToYNConverter;
import kr.project.backend.entity.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AppVersion extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "앱버전키값")
    private Long appVersionId;

    @Comment(value = "앱 OS")
    @Column(length = 2)
    private String appOs;

    @Comment(value = "최소버전")
    @Column(length = 6)
    private String minimumVersion;

    @Comment(value = "강제업데이트 여부")
    @Column(columnDefinition = "VARCHAR(1) default 'N'")
    @Convert(converter = BooleanToYNConverter.class)
    private boolean hardUpdateYn;

    @Comment(value = "강제업데이트 url")
    @Column(length = 100)
    private String hardUpdateUrl;

}
