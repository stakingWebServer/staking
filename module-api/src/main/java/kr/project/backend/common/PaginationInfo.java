package kr.project.backend.common;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationInfo {

    /** 페이징 계산에 필요한 파라미터들이 담긴 클래스 */
    private PagingCriteria pagingCriteria;

    /** 전체 데이터 개수 */
    private int totalRecordCount;

    /** 전체 페이지 개수 */
    private int totalPageCount;

    /** 페이지 리스트의 첫 페이지 번호 */
    private int firstPage;

    /** 페이지 리스트의 마지막 페이지 번호 */
    private int lastPage;

    /** SQL의 조건절에 사용되는 첫 RNUM */
    private int firstRecordIndex;

    /** SQL의 조건절에 사용되는 마지막 RNUM */
    private int lastRecordIndex;

    /** 이전 페이지 존재 여부 */
    private boolean hasPreviousPage;

    /** 다음 페이지 존재 여부 */
    private boolean hasNextPage;

    public PaginationInfo(PagingCriteria pagingCriteria) {
        if (pagingCriteria.getCurrentPageNo() < 1) {
            pagingCriteria.setCurrentPageNo(1);
        }
        if (pagingCriteria.getRecordsPerPage() < 1 || pagingCriteria.getRecordsPerPage() > 100) {
            pagingCriteria.setRecordsPerPage(10);
        }
        if (pagingCriteria.getPageSize() < 5 || pagingCriteria.getPageSize() > 20) {
            pagingCriteria.setPageSize(10);
        }

        this.pagingCriteria = pagingCriteria;
    }

    public void setTotalRecordCount(int totalRecordCount) {
        this.totalRecordCount = totalRecordCount;

        if (totalRecordCount > 0) {
            calculation();
        }
    }

    private void calculation() {

        /* 전체 페이지 수 (현재 페이지 번호가 전체 페이지 수보다 크면 현재 페이지 번호에 전체 페이지 수를 저장) */
        totalPageCount = ((totalRecordCount - 1) / pagingCriteria.getRecordsPerPage()) + 1;
        if (pagingCriteria.getCurrentPageNo() > totalPageCount) {
            pagingCriteria.setCurrentPageNo(totalPageCount);
        }

        /* 페이지 리스트의 첫 페이지 번호 */
        firstPage = ((pagingCriteria.getCurrentPageNo() - 1) / pagingCriteria.getPageSize()) * pagingCriteria.getPageSize() + 1;

        /* 페이지 리스트의 마지막 페이지 번호 (마지막 페이지가 전체 페이지 수보다 크면 마지막 페이지에 전체 페이지 수를 저장) */
        lastPage = firstPage + pagingCriteria.getPageSize() - 1;
        if (lastPage > totalPageCount) {
            lastPage = totalPageCount;
        }

        /* SQL의 조건절에 사용되는 첫 RNUM */
        firstRecordIndex = (pagingCriteria.getCurrentPageNo() - 1) * pagingCriteria.getRecordsPerPage();

        /* SQL의 조건절에 사용되는 마지막 RNUM */
        lastRecordIndex = pagingCriteria.getCurrentPageNo() * pagingCriteria.getRecordsPerPage();

        /* 이전 페이지 존재 여부 */
        hasPreviousPage = firstPage != 1;

        /* 다음 페이지 존재 여부 */
        hasNextPage = (lastPage * pagingCriteria.getRecordsPerPage()) < totalRecordCount;
    }

}
