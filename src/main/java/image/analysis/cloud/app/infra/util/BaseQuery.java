package image.analysis.cloud.app.infra.util;

public class BaseQuery {
    private Integer pageSize = 10;
    private Long pageNo = 1L;
    private String sortColumn;
    private String sortDesc;


    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageNo() {
        return pageNo;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public String getSortDesc() {
        return sortDesc;
    }

    public void setSortDesc(String sortDesc) {
        this.sortDesc = sortDesc;
    }
}
