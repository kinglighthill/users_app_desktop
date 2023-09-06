package com.scholarly.utme.data.model.listItems;

import com.google.gson.annotations.SerializedName;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class AppItem {
    public String bitlink;
    @SerializedName("app_name")
    public String name;
    public String keywords;
    @SerializedName("image_url")
    public byte[] imageUrl;
    @SerializedName("download_link")
    public String downloadLink;

    public AppItem(String bitlink, String name, String keywords, byte[] imageUrl, String downloadLink) {
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
        return new String(imageUrl, StandardCharsets.UTF_8);
    }

    public Image getImage() {
        byte[] data = imageUrl;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        return new Image(byteArrayInputStream);
    }

    public String getDownloadLink() {
        return downloadLink;
    }
}
