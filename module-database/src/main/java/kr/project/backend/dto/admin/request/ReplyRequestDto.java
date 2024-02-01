package kr.project.backend.dto.admin.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReplyRequestDto implements Serializable {
    private String content;
}
