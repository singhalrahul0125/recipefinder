package com.recipefinder.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class SearchResponse {

    @JsonProperty("results")
    private List<Result> results = null;
    @JsonProperty("baseUri")
    private String baseUri;
    @JsonProperty("offset")
    private Integer offset;
    @JsonProperty("number")
    private Integer number;
    @JsonProperty("totalResults")
    private Integer totalResults;
    @JsonProperty("processingTimeMs")
    private Integer processingTimeMs;
    @JsonProperty("expires")
    private Long expires;

    @JsonProperty("results")
    public List<Result> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(List<Result> results) {
        this.results = results;
    }

    @JsonProperty("baseUri")
    public String getBaseUri() {
        return baseUri;
    }

    @JsonProperty("baseUri")
    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    @JsonProperty("offset")
    public Integer getOffset() {
        return offset;
    }

    @JsonProperty("offset")
    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @JsonProperty("number")
    public Integer getNumber() {
        return number;
    }

    @JsonProperty("number")
    public void setNumber(Integer number) {
        this.number = number;
    }

    @JsonProperty("totalResults")
    public Integer getTotalResults() {
        return totalResults;
    }

    @JsonProperty("totalResults")
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    @JsonProperty("processingTimeMs")
    public Integer getProcessingTimeMs() {
        return processingTimeMs;
    }

    @JsonProperty("processingTimeMs")
    public void setProcessingTimeMs(Integer processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }

    @JsonProperty("expires")
    public Long getExpires() {
        return expires;
    }

    @JsonProperty("expires")
    public void setExpires(Long expires) {
        this.expires = expires;
    }

}
