package kr.project.backend.results;
import kr.project.backend.common.ApiResponseMessage;
import kr.project.backend.common.PaginationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Slf4j
public class ListResult {

    /**
     * 리스트 타입 ApiResult
     * @param resultListName 리스트 객체 이름
     * @param resultList 리스트 객체
     * @param paginationInfo 페이징 정보
     * @return
     */
    public static ResponseEntity<?> build(String resultListName, List<?> resultList, PaginationInfo paginationInfo) {

        ApiResult apiResult = ApiResult.blank()
                .add("totalRecordCount", paginationInfo.getTotalRecordCount())
                .add("totalPageCount", paginationInfo.getTotalPageCount())
                .add("firstPage", paginationInfo.getFirstPage())
                .add("lastPage", paginationInfo.getLastPage())
                .add("currentPageNo", paginationInfo.getPagingCriteria().getCurrentPageNo())
                .add("recordsPerPage", paginationInfo.getPagingCriteria().getRecordsPerPage())
                .add("pageSize", paginationInfo.getPagingCriteria().getPageSize())
                .add(resultListName, resultList);
        return Result.ok(new ApiResponseMessage(apiResult));
    }

    /**
     * 리스트 타입 ApiResult
     * root object는 하나만 나와야 하기 때문에 해당 메소드를 deprecated 처리함, build(List resultList)를 사용한다.
     * @param resultListName 리스트 객체 이름
     * @param resultList 리스트 객체
     * @return ResponseEntity
     */
    @Deprecated
    public static ResponseEntity<?> build(String resultListName, List<?> resultList) {

        ApiResult apiResult = ApiResult.blank()
                .add(resultListName, resultList);
        return Result.ok(new ApiResponseMessage(apiResult));
    }

    /**
     * 리스트 타입 ApiResult
     * @param resultList 리스트 객체
     * @return ResponseEntity
     */
    public static ResponseEntity<?> build(List<?> resultList) {
        return Result.ok(new ApiResponseMessage(resultList));
    }

    /**
     * 리스트 타입 ApiResult
     * @param resultList 리스트 객체
     * @return ResponseEntity
     */
    public static ResponseEntity<?> build(Page<?> resultList) {
        return Result.ok(new ApiResponseMessage(resultList));
    }
}
