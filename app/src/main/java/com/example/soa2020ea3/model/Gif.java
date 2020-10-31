package com.example.soa2020ea3.model;

import com.google.gson.annotations.SerializedName;

public class Gif {
    @SerializedName("title")
    private String title;

    @SerializedName("images")
    private Image image;

    public Gif(String title, Image image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public class Image {
        @SerializedName("downsized_medium")
        private Medium medium;

        public Image(Medium medium) {
            this.medium = medium;
        }

        public Medium getMedium() {
            return medium;
        }

        public void setMedium(Medium medium) {
            this.medium = medium;
        }
    }

    public class Medium {
        @SerializedName("url")
        private String url;

        public Medium(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}