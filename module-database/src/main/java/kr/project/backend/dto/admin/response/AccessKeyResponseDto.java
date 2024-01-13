package kr.project.database.dto.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class AccessKeyResponseDto implements Serializable {

    /** accessKey */
    private String accessKey;
}
