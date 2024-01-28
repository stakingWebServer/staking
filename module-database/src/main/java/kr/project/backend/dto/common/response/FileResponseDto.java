package kr.project.backend.dto.common.response;

import lombok.Data;

@Data
public class FileResponseDto {

    private String groupFileId;

    private String fileId;

    private String fileName;

    private String filePath;

    private String fileUrl;
}
