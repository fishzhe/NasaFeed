package com.nasafeed.domain;

import android.graphics.Bitmap;

/**
 * Created by Zhe Yu on 2015/10/18.
 */
public class ImageContainer {
    private Bitmap image;
    private String title;
    private String pubDate;
    private String description;

    public ImageContainer() {
        image = null;
        title = "";
        pubDate = "";
        description = "";
    }

    public ImageContainer(Bitmap image, String title, String pubDate, String description) {
        this.image = image;
        this.title = title;
        this.pubDate = pubDate;
        this.description = description;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
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
