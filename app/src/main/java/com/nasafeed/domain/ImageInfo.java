package com.nasafeed.domain;

/**
 * Created by Zhe Yu on 2015/10/18.
 */
public class ImageInfo {
    private String url;
    private String title;
    private String pubDate;
    private String description;

    public ImageInfo() {
        url = "";
        title = "";
        pubDate = "";
        description = "";
    }

    public ImageInfo(String url, String title, String pubDate, String description) {
        this.url = url;
        this.title = title;
        this.pubDate = pubDate;
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
