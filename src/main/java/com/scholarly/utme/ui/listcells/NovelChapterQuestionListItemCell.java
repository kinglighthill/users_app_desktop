package com.scholarly.utme.ui.listcells;

import com.scholarly.utme.data.model.novels.NovelObjectiveQuestion;
import com.scholarly.utme.data.util.QuestionOption;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NovelChapterQuestionListItemCell extends ListCell<NovelObjectiveQuestion> {

    public VBox footerVBox;

    public Label questionLabel, optionA, optionB, optionC, optionD, optionE, explanationLabel, explanationText;

    public ImageView shareIcon, micIcon, bookmarkIcon, reportIcon, explanationIcon;

    private List<QuestionOption> options;

    private List<Label> optionLabels;

    boolean isExplanationShowing = false;

    public NovelChapterQuestionListItemCell() {
        loadFxml();

        options = new ArrayList<>();
        options.add(new QuestionOption(0, optionA.getText()));
        options.add(new QuestionOption(1, optionB.getText()));
        options.add(new QuestionOption(2, optionC.getText()));
        options.add(new QuestionOption(3, optionD.getText()));
        options.add(new QuestionOption(4, optionE.getText()));

        optionLabels = new ArrayList<>();
        optionLabels.add(optionA);
        optionLabels.add(optionB);
        optionLabels.add(optionC);
        optionLabels.add(optionD);
        optionLabels.add(optionE);

        footerVBox.getChildren().remove(explanationText);

    }

    private void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/list_items/novel_chapter_question_list_item.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(NovelObjectiveQuestion item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }else {
            questionLabel.setText(item.getQuestion());

            optionA.setText("(A)  " + item.getOptionA().getText());
            optionB.setText("(B)  " + item.getOptionB().getText());
            optionC.setText("(C)  " + item.getOptionC().getText());
            optionD.setText("(D)  " + item.getOptionD().getText());
            optionE.setText("(E)  " + item.getOptionE().getText());

            explanationText.setText(item.getQuestionAnswer().getExplanation());

            // Set green text color on the Option Answer
            for (int i = 0; i < options.size(); i++) {
                optionLabels.get(i).setTextFill(Paint.valueOf("#053500"));
                if (item.getQuestionAnswer().getId() == options.get(i).getId()) {
                    optionLabels.get(i).setTextFill(Paint.valueOf("#12AF20"));
                }
            }

            shareIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/novel_question_share.png").toString()));
            micIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/novel_question_mic.png").toString()));
            bookmarkIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/novel_question_bookmark.png").toString()));
            reportIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/novel_question_report.png").toString()));
            explanationIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/novel_explanation_icon.png").toString()));


            explanationLabel.setOnMouseClicked(event -> {
                if (isExplanationShowing) {
                    footerVBox.getChildren().remove(explanationText);
                    isExplanationShowing = false;
                } else {
                    footerVBox.getChildren().add(explanationText);
                    isExplanationShowing = true;
                }
            });

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        }
    }
}
