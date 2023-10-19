package com.scholarly.utme.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.scholarly.utme.MainApplication;
import com.scholarly.utme.data.model.ObjectiveQuestion;
import com.scholarly.utme.data.model.TheoryQuestion;
import io.reactivex.rxjava3.annotations.Nullable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Labeled;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;

import javax.imageio.ImageIO;

import static com.scholarly.utme.util.Constants.BASE_URL;

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
            Class<?> mClass,
            ImageView imageView,
            WebView webView,
            String imageUrl
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

    public static ImageView getImageFromText(
            Class<?> mClass,
            String imageUrl
    ) {
        int startIndexOfImg = imageUrl.indexOf("<img");
        int endIndexOfImg = imageUrl.indexOf("'100%'>", startIndexOfImg);

        int startIndexOfImgPath = imageUrl.indexOf("/android_asset", startIndexOfImg);
        int endIndexOfImgPath = imageUrl.indexOf("' width", startIndexOfImg);

        String imagePath = imageUrl.substring(startIndexOfImgPath, endIndexOfImgPath);
        String newImagePath = imagePath.replace("android_asset/images", "assets/images/pq");

        Image image = new Image(Objects.requireNonNull(mClass.getResource(newImagePath)).toString());
        return new ImageView(image);
    }

    public static String loadPQImageUrl(
            Class<?> mClass,
            ImageView imageView,
            WebView webView,
            String imageUrl,
            boolean isWebView
    ) {
        int startIndexOfImg = imageUrl.indexOf("<img");
        int endIndexOfImg = imageUrl.indexOf("'100%'>", startIndexOfImg);

        int startIndexOfImgPath = imageUrl.indexOf("/android_asset", startIndexOfImg);
        int endIndexOfImgPath = imageUrl.indexOf("' width", startIndexOfImg);

        String imagePath = imageUrl.substring(startIndexOfImgPath, endIndexOfImgPath);
        String newImagePath = imagePath.replace("android_asset/images", "assets/images/pq");

        imageView.setImage(new Image(mClass.getResource(newImagePath).toString()));

        String imageQuestion = imageUrl.substring(imageUrl.lastIndexOf(">")+1);
        if (isWebView) {
            imageQuestion = loadLatex(mClass, imageQuestion);
        }
        webView.getEngine().loadContent(imageQuestion);

        StringBuilder builder = new StringBuilder(newImagePath);

        URL url = mClass.getResource(newImagePath);
        String img = "<img src='"+url+"' width='100%'>";

        builder.replace(startIndexOfImg, (endIndexOfImg + 7), img);
        return builder.toString();
    }

    public static void loadOption(Class<?> mClass, Labeled optionButton, String option, String optionLabel, String backgroundColor) {
        if (option.contains("<img")) {
            optionButton.setText(optionLabel);
            ImageView imageView = Helper.getImageFromText(mClass, option);
            imageView.setFitHeight(44.0);
            optionButton.setGraphic(imageView);
            optionButton.setContentDisplay(ContentDisplay.RIGHT);
            optionButton.setPrefHeight(64.0);
        } else {
            if (Helper.isWebView(option)) {
                WebView webView = new WebView();
                webView.setPrefHeight(44.0);
                String content = "<html><body style='background-color: " + backgroundColor +"'>"
                        + option + "</body></html>";
                webView.getEngine().loadContent(content);

                optionButton.setText(optionLabel);
                optionButton.setGraphic(webView);
                optionButton.setContentDisplay(ContentDisplay.RIGHT);
                optionButton.setMinHeight(64.0);
            } else {
                optionButton.setGraphic(null);
                optionButton.setText(optionLabel + option);
                optionButton.setPrefHeight(24.0);
            }
        }
    }

    public static boolean isWebView(String input) {
        String pattern = "<.*?>|&.*?;";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(input);

        return matcher.find();
    }

    public static boolean isWebView(ObjectiveQuestion question, boolean checkQuestion) {
        String input;
        if (checkQuestion) {
            input = question.getQuestion();
        } else {
            input = question.getQuestionAnswer().getExplanation();
        }

        String pattern = "<.*?>|&.*?;";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(input);

        int inputCheck;
        if (checkQuestion) {
            inputCheck = question.getIsQuestionWebView();
        } else {
            inputCheck = question.getIsExplanationWebView();
        }

        return inputCheck == 1 || matcher.find();
    }

    public static boolean isWebView(TheoryQuestion question) {
        return question.getIsQuestionWebView() == 1;
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

    public static String loadLatex(Class mClass, String content) {
        String cssPath = Objects.requireNonNull(mClass.getResource("/assets/katex/katex.min.css")).toExternalForm();
        String jsPath = Objects.requireNonNull(mClass.getResource("/assets/katex/katex.min.js")).toExternalForm();
        String autoRenderJsPath = Objects.requireNonNull(mClass.getResource("/assets/katex/contrib/auto-render.min.js")).toExternalForm();

        String latexContent = """
                <!DOCTYPE html>
                <html>
                    <head>
                       <link rel="stylesheet" href="{cssPath}">
                       <script defer src="{jsPath}"></script>
                       <script defer src="{autoRenderJsPath}"
                           onload="renderMathInElement(document.body, {
                                delimiters: [
                                     {left: '$$', right: '$$', display: false},
                                     {left: '$', right: '$', display: false},
                                ],
                                throwOnError : false
                           });"></script>
                       <style type='text/css'>body {margin: 0px;padding: 0px;font-size:16px; } </style>
                     </head>
                    <body>
                        <div>{formula}</div>
                    </body>
                </html>""";
        latexContent = latexContent.replace("{cssPath}", cssPath)
                .replace("{jsPath}", jsPath)
                .replace("{autoRenderJsPath}", autoRenderJsPath);
        return latexContent.replace("{formula}", content);
    }

    public static boolean checkNetworkConnectivity() {
        try {
            URL url = new URL(BASE_URL);
            URLConnection connection = url.openConnection();
            connection.connect();
            return true;
        } catch (IOException e) {
            MainApplication.log(e);
            return false;
        }
    }
}
