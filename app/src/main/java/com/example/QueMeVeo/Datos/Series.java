
package com.example.QueMeVeo.Datos;

import java.util.List;
//////import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//////@Generated("jsonschema2pojo")
public class Series {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<ResultSeries> results = null;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Series() {
    }

    /**
     * 
     * @param totalResults
     * @param totalPages
     * @param page
     * @param results
     */
    public Series(Integer page, List<ResultSeries> results, Integer totalPages, Integer totalResults) {
        super();
        this.page = page;
        this.results = results;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<ResultSeries> getResults() {
        return results;
    }

    public void setResults(List<ResultSeries> results) {
        this.results = results;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

}
