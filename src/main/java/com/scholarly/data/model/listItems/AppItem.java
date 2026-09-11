package com.scholarly.data.model.listItems;

import com.google.gson.annotations.SerializedName;
import com.scholarly.util.Helper;
import javafx.scene.image.Image;

import java.util.Objects;

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

    public Image getImage() {
        Image image = Helper.loadWebpFromUrl(imageUrl);

        return Objects.requireNonNullElseGet(image, () -> new Image(getClass().getResource("/drawable/app_screen_images/scholarly_logo.png").toString()));
    }

    public String getDownloadLink() {
        return downloadLink;
    }
}
