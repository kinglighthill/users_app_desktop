package com.scholarly.utme.ui.listcells;

import com.sandec.mdfx.MarkdownView;
import com.scholarly.utme.data.dao.SubjectDao;
import com.scholarly.utme.data.model.ObjectiveQuestion;
import com.scholarly.utme.data.model.listItems.UnorderedListItem;
import com.scholarly.utme.data.model.newDb.ContentViewTypes;
import com.scholarly.utme.data.model.newDb.NoteSection;
import com.scholarly.utme.data.model.newDb.contentType.ContentViewType;
import com.scholarly.utme.data.model.newDb.contentViewType.*;
import com.scholarly.utme.ui.cellFactories.UnorderedListCellFactory;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.NoSelectionModel;
import com.scholarly.utme.util.Helper;
import com.scholarly.utme.viewmodels.note_screens.NotesScreenVM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
//import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
//import org.commonmark.parser.Parser;
//import org.commonmark.renderer.html.HtmlRenderer;
//import org.commonmark.Extension;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NoteContentListItemCell extends ListCell<NoteSection> {
    private static final String TAG = "NoteContentListItemCell: ";

    Parser markdownParser = Parser.builder().build();
    HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();

    public static final String NORMAL_OPTION_STYLE = "-fx-border-color: #233D2C; -fx-border-radius: 50; -fx-cursor: hand;";
    private static final String CORRECT_OPTION_STYLE = "-fx-border-color: #51C42B; -fx-border-radius: 50; -fx-cursor: hand;";
    private static final String INCORRECT_OPTION_STYLE = "-fx-border-color: #E90000; -fx-border-radius: 50; -fx-cursor: hand;";

    private final NotesScreenVM viewModel;
    public NoteContentListItemCell() {
        loadFxml();
        viewModel = new NotesScreenVM();
        setPrefWidth(400);
        setOnMouseClicked(event -> {
            System.out.println(TAG + "Section clicked!");
        });
    }

    private void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/list_items/note_content_list_item.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(NoteSection item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
//            System.out.println(TAG + "Got Section -> " + Helper.toString(item));
            Parent node = renderNote(item);
//            System.out.println(TAG + "Before setGraphic -> " + node);
            if (node instanceof VBox) {
                ((VBox) node).setPadding(new Insets(0, 180, 0, 180));
            }

            if (node instanceof Label) {
                ((Label) node).setLineSpacing(10);
                ((Label) node).setPadding(new Insets(5, 180, 5, 180));
                ((Label) node).setTextAlignment(TextAlignment.JUSTIFY);

                if (node.getUserData() != null && node.getUserData().equals("Level 1")) {
                    ((Label) node).setPadding(new Insets(20, 180, 5, 180));
                }
            }

            setOnMouseClicked(event -> {
                if (node instanceof Label) {
//                    ((Label) node).setUnderline(!((Label) node).isUnderline());
                }
            });

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(node);

        }
    }

    private Parent renderNote(NoteSection section) {
        ContentViewType contentViewType = ContentViewTypes.convert(section);
        Parent contentElement = null;
        if (contentViewType instanceof HeaderViewType headerViewType) {
//            System.out.println(TAG + "ContentViewType -> HeaderViewType");

            if (headerViewType.getText() != null) {
                Document doc = Jsoup.parse(headerViewType.getText());
                String formattedText = doc.body().text();
                Label label = new Label(formattedText);
                label.setWrapText(true);
                label.setTextFill(Paint.valueOf(SubjectDao.getNoteSubjectColor(section.getSubjectId())));

                if (headerViewType.getLevel() == 1) {
                    label.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 22));
                    label.setUserData("Level 1");
                } else if (headerViewType.getLevel() == 2) {
                    label.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
                } else {
                    label.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
                }

//                if (section.getSubtopicId() != 0 || section.getMainSectionOrder() != 0) {
//                    label.setPadding(new Insets(10, 0, 0, 0));
//                    label.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
//                }

                contentElement = label;
//                System.out.println(TAG + "HeaderViewType text -> " + label.getText());
            }

        } else if (contentViewType instanceof ParagraphViewType paragraphViewType) {
            if (paragraphViewType.getText() != null) {
                String contentText = paragraphViewType.getText();
                if (!Helper.isWebView(contentText)) {
                    String formattedText = paragraphViewType.getText().replaceAll("<br>", System.lineSeparator());
                    Label label = new Label(formattedText);
                    label.setWrapText(true);
                    label.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
                    if (section.getSubtopicId() != 0 || section.getMainSectionOrder() != 0) {
                        label.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
                        label.setPadding(new Insets(10, 0, 0, 0));
                    }
                    contentElement = label;
                } else {
                    String content = Helper.loadLatex(getClass(), contentText);
                    WebView webView = new WebView();
                    webView.setPrefHeight(200);
                    WebEngine webEngine = webView.getEngine();
                    webEngine.loadContent(content);

                    VBox vBox = new VBox();
                    vBox.getChildren().addAll(webView);
                    contentElement = vBox;
                }
            }
        } else if (contentViewType instanceof CBTViewType cbtViewType) {
            int subjectId = cbtViewType.getSubjectId();
            int yearId = cbtViewType.getYearId();
            int questionNum = cbtViewType.getQuestionId();

            ObjectiveQuestion question = viewModel.getQuestion(subjectId, yearId, questionNum);

            VBox cbtVBox = new VBox();
            cbtVBox.setPadding(new Insets(20, 50, 30, 50));
            cbtVBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10;");
            cbtVBox.setSpacing(10);
            VBox.setMargin(cbtVBox, new Insets(0, 130, 0, 130));

            Document doc = Jsoup.parse(question.getQuestion());
            String formattedText = doc.body().text();
            Label questionBox = new Label(formattedText);
            questionBox.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
            questionBox.setWrapText(true);
            questionBox.setPadding(new Insets(10));
            questionBox.setStyle("-fx-background-color: #E7F7E9; -fx-border-color: #034801; -fx-border-radius: 5; ");

            RadioButton optionAButton = new RadioButton();
            optionAButton.setText(question.getOptionA().getText());
            optionAButton.setAlignment(Pos.CENTER);
            optionAButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
            optionAButton.setStyle(NORMAL_OPTION_STYLE);
            optionAButton.setPadding(new Insets(10));

            RadioButton optionBButton = new RadioButton();
            optionBButton.setText(question.getOptionB().getText());
            optionBButton.setAlignment(Pos.CENTER);
            optionBButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
            optionBButton.setStyle(NORMAL_OPTION_STYLE);
            optionBButton.setPadding(new Insets(10));

            RadioButton optionCButton = new RadioButton();
            optionCButton.setText(question.getOptionC().getText());
            optionCButton.setAlignment(Pos.CENTER);
            optionCButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
            optionCButton.setStyle(NORMAL_OPTION_STYLE);
            optionCButton.setPadding(new Insets(10));

            RadioButton optionDButton = new RadioButton();
            optionDButton.setText(question.getOptionD().getText());
            optionDButton.setAlignment(Pos.CENTER);
            optionDButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
            optionDButton.setStyle(NORMAL_OPTION_STYLE);
            optionDButton.setPadding(new Insets(10));


            ToggleGroup optionsToggle = new ToggleGroup();
            optionsToggle.getToggles().addAll(optionAButton, optionBButton, optionCButton, optionDButton);

            optionAButton.setOnAction(event -> {
                if (question.getQuestionAnswer().getId() == 0) {
                    optionAButton.setStyle(CORRECT_OPTION_STYLE);
                } else {
                    optionAButton.setStyle(INCORRECT_OPTION_STYLE);

                }
            });
            optionBButton.setOnAction(event -> {
                if (question.getQuestionAnswer().getId() == 1) {
                    optionBButton.setStyle(CORRECT_OPTION_STYLE);
                } else {
                    optionBButton.setStyle(INCORRECT_OPTION_STYLE);
                }
            });
            optionCButton.setOnAction(event -> {
                if (question.getQuestionAnswer().getId() == 2) {
                    optionCButton.setStyle(CORRECT_OPTION_STYLE);
                } else {
                    optionCButton.setStyle(INCORRECT_OPTION_STYLE);
                }
            });
            optionDButton.setOnAction(event -> {
                if (question.getQuestionAnswer().getId() == 3) {
                    optionDButton.setStyle(CORRECT_OPTION_STYLE);
                } else {
                    optionDButton.setStyle(INCORRECT_OPTION_STYLE);
                }
            });


            ImageView showExplanationIcon = new ImageView(new Image(getClass().getResource("/drawable/cbtview_show_explanation_icon_1x.png").toString()));
            showExplanationIcon.setFitWidth(15);
            showExplanationIcon.setFitHeight(15);
            showExplanationIcon.setPreserveRatio(true);
            showExplanationIcon.setPickOnBounds(true);

            ImageView hideExplanationIcon = new ImageView(new Image(getClass().getResource("/drawable/cbtview_hide_explanation_icon_1x.png").toString()));
            hideExplanationIcon.setFitWidth(15);
            hideExplanationIcon.setFitHeight(15);
            hideExplanationIcon.setPreserveRatio(true);
            hideExplanationIcon.setPickOnBounds(true);

            ToggleButton showHideExplanationButton = new ToggleButton("See Explanation");
            showHideExplanationButton.setTextFill(Paint.valueOf("#51C46B"));
            showHideExplanationButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
            showHideExplanationButton.setGraphic(showExplanationIcon);
            showHideExplanationButton.setGraphicTextGap(10);
            showHideExplanationButton.setContentDisplay(ContentDisplay.RIGHT);
            showHideExplanationButton.setBackground(Background.EMPTY);
            showHideExplanationButton.setStyle("-fx-cursor: hand;");
            HBox.setMargin(showHideExplanationButton, new Insets(0, 0, 0, 10));

            Label explanationText = new Label();
            explanationText.setText(question.getQuestionAnswer().getExplanation());
            explanationText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
            explanationText.setWrapText(true);
            explanationText.setPadding(new Insets(10));
            explanationText.setStyle("-fx-border-color: #034801; -fx-border-radius: 5; ");

            cbtVBox.widthProperty().addListener(((observable, oldValue, newValue) -> {
                optionAButton.setPrefWidth((Double) newValue);
                optionBButton.setPrefWidth((Double) newValue);
                optionCButton.setPrefWidth((Double) newValue);
                optionDButton.setPrefWidth((Double) newValue);
                questionBox.setPrefWidth((Double) newValue);
                explanationText.setPrefWidth((Double) newValue);
            }));

            cbtVBox.getChildren().addAll(questionBox, optionAButton, optionBButton, optionCButton, optionDButton, showHideExplanationButton);

            showHideExplanationButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue) {
                    showHideExplanationButton.setGraphic(hideExplanationIcon);
                    showHideExplanationButton.setText("Hide explanation");
                    cbtVBox.getChildren().add(explanationText);
                } else {
                    showHideExplanationButton.setGraphic(showExplanationIcon);
                    showHideExplanationButton.setText("See explanation");
                    cbtVBox.getChildren().remove(explanationText);
                }
            }));

            showHideExplanationButton.setOnMouseEntered(e -> showHideExplanationButton.setUnderline(true));
            showHideExplanationButton.setOnMouseExited(e -> showHideExplanationButton.setUnderline(false));

            contentElement = cbtVBox;

        } else if (contentViewType instanceof LatexMathViewType latexMathViewType) {
            if (latexMathViewType.getKatex() != null) {
                String content = Helper.loadLatex(getClass(), latexMathViewType.getKatex());
                WebView webView = new WebView();
                webView.setPrefHeight(200);
                WebEngine webEngine = webView.getEngine();
                webEngine.loadContent(content);

                VBox vBox = new VBox();
                vBox.getChildren().addAll(webView);
                contentElement = vBox;
            }
        } else if (contentViewType instanceof ListViewType listViewType) {
            if (listViewType.getStyle().equalsIgnoreCase("unordered")) {

                ArrayList<UnorderedListItem> contentItems = new ArrayList<>();

                VBox vBox = new VBox(10);
                for (int i = 0; i < listViewType.getItems().size(); i++) {
                    UnorderedListItem unorderedListItem = new UnorderedListItem(listViewType.getItems().get(i));
                    contentItems.add(unorderedListItem);

                    Circle dot = new Circle(3.5, Paint.valueOf(SubjectDao.getNoteSubjectColor(section.getSubjectId())));
                    HBox.setMargin(dot, new Insets(5, 0, 0, 0));
                    Label label = new Label(unorderedListItem.getText());
                    label.setWrapText(true);
                    label.setTextAlignment(TextAlignment.JUSTIFY);
                    label.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
                    HBox hBox = new HBox(dot, label);
                    hBox.setAlignment(Pos.TOP_LEFT);
                    hBox.setSpacing(13);
                    vBox.getChildren().addAll(hBox);
                }

                ObservableList<UnorderedListItem> items = FXCollections.observableArrayList(contentItems);


                ListView<UnorderedListItem> contentList = new ListView<>();
                contentList.setItems(items);
                contentList.setBackground(Background.EMPTY);
                contentList.setCellFactory(new UnorderedListCellFactory(SubjectDao.getNoteSubjectColor(section.getSubjectId())));
                contentList.setSelectionModel(new NoSelectionModel<>());
                contentList.setPadding(new Insets(0, 120, 0, 120));

                contentElement = vBox;
            }

        } else if (contentViewType instanceof ReferenceViewType referenceViewType) {
            if (referenceViewType.getText() != null) {
                String contentText = referenceViewType.getText();
                if (!Helper.isWebView(contentText)) {
                    String formattedText = referenceViewType.getText().replaceAll("<br>", System.lineSeparator());

                    Label label = new Label(formattedText);
                    label.setWrapText(true);
                    label.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
                    if (formattedText.contains("www") || formattedText.contains(".com")) {
                        label.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM_ITALIC, 14));
                    }

                    contentElement = label;
                } else {
                    String content = Helper.loadLatex(getClass(), contentText);
                    WebView webView = new WebView();
                    webView.setPrefHeight(200);
                    WebEngine webEngine = webView.getEngine();
                    webEngine.loadContent(content);

                    VBox vBox = new VBox();
                    vBox.getChildren().addAll(webView);
                    contentElement = vBox;
                }
            }
        } else if (contentViewType instanceof TableViewType tableViewType) {
            GridPane tableGrid = new GridPane();
            tableGrid.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #F4D242");

            List<List<String>> content = tableViewType.getContent();

            for (int row = 0; row < content.size(); row++) {
                for (int col = 0; col < content.get(row).size(); col++) {
                    Label grid = new Label(content.get(row).get(col));
                    grid.setWrapText(true);
                    grid.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
                    grid.setPadding(new Insets(5, 5, 5, 5));

                    tableGrid.add(grid, col, row);
                }
            }

            contentElement = tableGrid;

        } else if (contentViewType instanceof SimpleImageViewType simpleImageViewType) {
            System.out.println(TAG + "ContentViewType -> SimpleImageViewType");
            String imageUrl = simpleImageViewType.getUrl();
            System.out.println(TAG + "Image Url -> " + imageUrl);
            StringBuilder builder = new StringBuilder(imageUrl);
            builder.insert(0, "/assets/images/");
            URL url = getClass().getResource(builder.toString());
            if (url != null) {
                System.out.println(TAG + "Note Image Url -> " + url);
                ImageView imageView = new ImageView(new Image(url.toString()));

                contentElement = new StackPane(imageView);
            } else {
                System.out.println(TAG + "IMAGE WITH URL NOT FOUND -> " + imageUrl);
            }
        }
        return contentElement;
    }
}
