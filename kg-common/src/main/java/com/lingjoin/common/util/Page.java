package com.lingjoin.common.util;

import java.util.List;

public class Page {

    private List<?> pageList;
    private Integer totalCount;
    private Integer pageSize=10;
    private Integer pageNo=0;
    private Integer totalPage;
    private Integer start;
    public Page(Integer totalCount, Integer pageSize) {
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        setTotalPage();
    }

    public Integer getTotalCount() {

        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }



    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public List<?> getPageList() {
        return pageList;
    }

    public void setPageList(List<?> pageList) {
        this.pageList = pageList;
    }



    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    private void setTotalPage() {
        this.totalPage = totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1;
    }

    public Page() {

    }

    @Override
    public String toString() {
        return "Page{" +
                "pageList=" + pageList +
                ", totalCount=" + totalCount +
                ", pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                ", totalPage=" + totalPage +
                '}';
    }
}
