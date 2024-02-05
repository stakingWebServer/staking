package kr.project.backend.dto.admin.response;

import lombok.Data;

@Data//ㅇ
public class PageViewDto {
    private String viewName;
    private Long pageView;

    public PageViewDto(String viewName,Long pageView){
        this.viewName = viewName;
        this.pageView = pageView;
    }
}
