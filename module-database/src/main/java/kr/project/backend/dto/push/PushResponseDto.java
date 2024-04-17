package kr.project.backend.dto.push;

import kr.project.backend.entity.push.Push;
import lombok.Data;

import java.io.Serializable;


@Data
public class PushResponseDto implements Serializable {
    private String pushTitle;
    private String pushContent;

    public PushResponseDto(Push push) {
        this.pushTitle = push.getPushTitle();
        this.pushContent = push.getPushContent();
    }

}
