package kr.project.backend.dto.admin.response;

import lombok.Data;

@Data
public class PageViewDto {
    private String viewName;
    private String pageViewDate;
    private Long pageView;

    public PageViewDto(String viewName,String pageViewDate,Long pageView){
        this.viewName = viewName;
        this.pageViewDate = pageViewDate;
        this.pageView = pageView;
    }
}
