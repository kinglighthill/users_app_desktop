package com.scholarly.utme.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import io.reactivex.rxjava3.annotations.Nullable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;

import javax.imageio.ImageIO;

public class Helper {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(SerializationFeature.INDENT_OUTPUT);

    public static String toString(Object obj) {
        String result;
        try {
            result = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            result = "The object can not be serialized by Jackson JSON mapper because: " + e.getMessage();
        }
        return result;
    }

    public static @Nullable Image loadWebpFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            BufferedImage img = ImageIO.read(url);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            if (ImageIO.write(img, "png", outputStream)) {
                byte[] bytes = outputStream.toByteArray();
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                return new Image(inputStream);
            }

            outputStream.close();
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static String loadPQImageUrl(
            Class<?> mClass, ImageView imageView, WebView webView, String imageUrl
    ) {
        int startIndexOfImg = imageUrl.indexOf("<img");
        int endIndexOfImg = imageUrl.indexOf("'100%'>", startIndexOfImg);

        int startIndexOfImgPath = imageUrl.indexOf("/android_asset", startIndexOfImg);
        int endIndexOfImgPath = imageUrl.indexOf("' width", startIndexOfImg);

        String imagePath = imageUrl.substring(startIndexOfImgPath, endIndexOfImgPath);
        String newImagePath = imagePath.replace("android_asset/images", "assets/images/pq");

        imageView.setImage(new Image(mClass.getResource(newImagePath).toString()));

        String imageQuestion = imageUrl.substring(imageUrl.lastIndexOf(">")+1);
        webView.getEngine().loadContent(imageQuestion);

        StringBuilder builder = new StringBuilder(newImagePath);

        URL url = mClass.getResource(newImagePath);
        String img = "<img src='"+url+"' width='100%'>";

        builder.replace(startIndexOfImg, (endIndexOfImg + 7), img);
        return builder.toString();
    }

    public static String parsePQImageUrl(Class<?> mClass, String imageUrl) {
        int startIndexOfImg = imageUrl.indexOf("<img");
        int endIndexOfImg = imageUrl.indexOf("'100%'>", startIndexOfImg);

        int startIndexOfImgPath = imageUrl.indexOf("/android_asset", startIndexOfImg);
        int endIndexOfImgPath = imageUrl.indexOf("' width", startIndexOfImg);

        String imagePath = imageUrl.substring(startIndexOfImgPath, endIndexOfImgPath);
        String newImagePath = imagePath.replace("android_asset/images", "assets/images/pq");

        StringBuilder builder = new StringBuilder(imageUrl);

        URL url = mClass.getResource(newImagePath);
        String img = "<img src='"+url+"' width='100%'>";

        builder.replace(startIndexOfImg, (endIndexOfImg + 7), img);
        return builder.toString();
    }
}
