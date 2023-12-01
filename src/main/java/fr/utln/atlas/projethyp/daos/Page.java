package fr.utln.atlas.projethyp.daos;

import java.util.List;

public class Page<T> {
    private int pageNumber;
    private int pageSize;
    private List<T> resultList;

    public Page(int pageNumber, int pageSize, List<T> resultList) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.resultList = resultList;
    }

    // Getters
    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<T> getResultList() {
        return resultList;
    }

    // Setters (if needed)
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setResultList(List<T> resultList) {
        this.resultList = resultList;
    }
}