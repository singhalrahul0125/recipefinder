package com.recipefinder.client.model;

public class RecipeResult {
    private String title;
    private String sourceUrl;
    private Integer readyInMinutes;

    public RecipeResult() {

    }

    public RecipeResult(String title, String sourceUrl, Integer readyInMinutes) {
        this.title = title;
        this.sourceUrl = sourceUrl;
        this.readyInMinutes = readyInMinutes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public Integer getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(Integer readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }
}
