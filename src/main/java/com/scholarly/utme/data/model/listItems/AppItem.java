package com.scholarly.utme.data.model.listItems;

import com.google.gson.annotations.SerializedName;

public class AppItem {
    public String bitlink;
    @SerializedName("app_name")
    public String name;
    public String keywords;
    @SerializedName("image_url")
    public String imageUrl;
    @SerializedName("download_link")
    public String downloadLink;

    public AppItem(String bitlink, String name, String keywords, String imageUrl, String downloadLink) {
        this.bitlink = bitlink;
        this.name = name;
        this.keywords = keywords;
        this.imageUrl = imageUrl;
        this.downloadLink = downloadLink;
    }

    public String getBitlink() {
        return bitlink;
    }

    public String getName() {
        return name;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDownloadLink() {
        return downloadLink;
    }
}
