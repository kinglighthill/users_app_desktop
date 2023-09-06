package com.scholarly.utme.network.model;

import com.google.gson.annotations.SerializedName;

public class BaseResponse2 {
    private String bitlink;
    private String app_name;
    private String keywords;
    private String download_link;

    @SerializedName("image_url")
    private byte[] imageUrl;

    public BaseResponse2(String bitlink, String app_name, String keywords, String download_link, byte[] imageUrl) {
        this.bitlink = bitlink;
        this.app_name = app_name;
        this.keywords = keywords;
        this.download_link = download_link;
        this.imageUrl = imageUrl;
    }

    public String getBitLink() {
        return bitlink;
    }

    public String getAppName() {
        return app_name;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getDownload_link() {
        return download_link;
    }

    public byte[] getImageUrl() {
        return imageUrl;
    }
}