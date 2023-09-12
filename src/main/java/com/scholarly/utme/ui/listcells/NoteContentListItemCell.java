package com.scholarly.utme.ui.listcells;

import com.sandec.mdfx.MarkdownView;
import com.scholarly.utme.data.model.ObjectiveQuestion;
import com.scholarly.utme.data.model.listItems.UnorderedListItem;
import com.scholarly.utme.data.model.newDb.ContentViewTypes;
import com.scholarly.utme.data.model.newDb.NoteSection;
import com.scholarly.utme.data.model.newDb.contentType.ContentViewType;
import com.scholarly.utme.data.model.newDb.contentViewType.*;
import com.scholarly.utme.ui.cellFactories.UnorderedListCellFactory;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.NoSelectionModel;
import com.scholarly.utme.viewmodels.note_screens.NotesScreenVM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.web.HTMLEditor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoteContentListItemCell extends ListCell<NoteSection> {
    private static final String TAG = "NoteContentListItemCell: ";

    Parser markdownParser = Parser.builder().build();
    HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();

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
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setPadding(new Insets(5, 10, 5, 5));

//            System.out.println(TAG + "Got Section -> " + Helper.toString(item));
            Parent node = renderNote(item);
//            System.out.println(TAG + "Before setGraphic -> " + node);
            setOnMouseClicked(event -> {
                if (node instanceof Label) {
                    ((Label) node).setUnderline(!((Label) node).isUnderline());
                }
            });
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
                label.setLineSpacing(20);
                label.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
                if (section.getSubtopicId() != 0 || section.getMainSectionOrder() != 0) {
                    label.setPadding(new Insets(10, 0, 0, 0));
                    label.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
                    label.setLineSpacing(20);
                }

                contentElement = label;
//                System.out.println(TAG + "HeaderViewType text -> " + label.getText());
            }

        } else if (contentViewType instanceof ParagraphViewType paragraphViewType) {
//            System.out.println(TAG + "ContentViewType -> ParagraphViewType");

            if (paragraphViewType.getText() != null) {
                Document doc = Jsoup.parse(paragraphViewType.getText());
                String formattedText = doc.body().text();
                Label label = new Label(formattedText);
                label.setWrapText(true);
                label.setLineSpacing(20);
                label.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
                if (section.getSubtopicId() != 0 || section.getMainSectionOrder() != 0) {
                    label.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
                    label.setPadding(new Insets(10, 0, 0, 0));
                    label.setLineSpacing(20);
                }

                contentElement = label;
//                System.out.println(TAG + "ParagraphViewType text -> " + label.getText());
            }
        } else if (contentViewType instanceof CBTViewType cbtViewType) {
//            System.out.println(TAG + "ContentViewType -> CBTViewType");

            int yearId = cbtViewType.getYearId();
            int questionNum = cbtViewType.getQuestionId();

            ObjectiveQuestion question = viewModel.getQuestion(yearId, questionNum);

            VBox cbtVBox = new VBox();
            cbtVBox.setPadding(new Insets(20, 20, 30, 20));
            cbtVBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-color: #51C46B; -fx-border-radius: 10;");
            cbtVBox.setSpacing(10);
            VBox.setMargin(cbtVBox, new Insets(20, 0, 0, 0));

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
            optionAButton.setStyle("-fx-border-color: #233D2C; -fx-border-radius: 50;");
            optionAButton.setPadding(new Insets(10));

            RadioButton optionBButton = new RadioButton();
            optionBButton.setText(question.getOptionB().getText());
            optionBButton.setAlignment(Pos.CENTER);
            optionBButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
            optionBButton.setStyle("-fx-border-color: #233D2C; -fx-border-radius: 50;");
            optionBButton.setPadding(new Insets(10));

            RadioButton optionCButton = new RadioButton();
            optionCButton.setText(question.getOptionC().getText());
            optionCButton.setAlignment(Pos.CENTER);
            optionCButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
            optionCButton.setStyle("-fx-border-color: #233D2C; -fx-border-radius: 50;");
            optionCButton.setPadding(new Insets(10));

            RadioButton optionDButton = new RadioButton();
            optionDButton.setText(question.getOptionD().getText());
            optionDButton.setAlignment(Pos.CENTER);
            optionDButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
            optionDButton.setStyle("-fx-border-color: #233D2C; -fx-border-radius: 50;");
            optionDButton.setPadding(new Insets(10));


            ToggleGroup optionsToggle = new ToggleGroup();
            optionsToggle.getToggles().addAll(optionAButton, optionBButton, optionCButton, optionDButton);

            optionAButton.setOnAction(event -> {
                if (question.getQuestionAnswer().getId() == 0) {
                    optionAButton.setStyle("-fx-border-color: #51C42B; -fx-border-radius: 50;");
                } else {
                    optionAButton.setStyle("-fx-border-color: #E90000; -fx-border-radius: 50;");

                }
            });
            optionBButton.setOnAction(event -> {
                if (question.getQuestionAnswer().getId() == 1) {
                    optionBButton.setStyle("-fx-border-color: #51C42B; -fx-border-radius: 50;");
                } else {
                    optionBButton.setStyle("-fx-border-color: #E90000; -fx-border-radius: 50;");
                }
            });
            optionCButton.setOnAction(event -> {
                if (question.getQuestionAnswer().getId() == 2) {
                    optionCButton.setStyle("-fx-border-color: #51C42B; -fx-border-radius: 50;");
                } else {
                    optionCButton.setStyle("-fx-border-color: #E90000; -fx-border-radius: 50;");
                }
            });
            optionDButton.setOnAction(event -> {
                if (question.getQuestionAnswer().getId() == 3) {
                    optionDButton.setStyle("-fx-border-color: #51C42B; -fx-border-radius: 50;");
                } else {
                    optionDButton.setStyle("-fx-border-color: #E90000; -fx-border-radius: 50;");
                }
            });

            HBox hBox = new HBox();
            VBox.setMargin(hBox, new Insets(15, 0, 0, 5));
            Label seeExplanation = new Label("See explanation");
            seeExplanation.setAlignment(Pos.CENTER_LEFT);
            seeExplanation.setTextFill(Paint.valueOf("#51C46B"));
            seeExplanation.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
            ImageView showExplanation = new ImageView(new Image(getClass().getResource("/drawable/cbtview_show_explanation_icon_1x.png").toString()));
            showExplanation.setFitWidth(15);
            showExplanation.setFitHeight(15);
            showExplanation.setPreserveRatio(true);
            showExplanation.setPickOnBounds(true);
            ImageView hideExplanation = new ImageView(new Image(getClass().getResource("/drawable/cbtview_hide_explanation_icon_1x.png").toString()));
            hideExplanation.setFitWidth(15);
            hideExplanation.setFitHeight(15);
            hideExplanation.setPreserveRatio(true);
            hideExplanation.setPickOnBounds(true);
            Button showHideExplanation = new Button();
            showHideExplanation.setBackground(Background.EMPTY);
            showHideExplanation.setGraphic(showExplanation);
            HBox.setMargin(showHideExplanation, new Insets(0, 0, 0, 10));

            hBox.getChildren().addAll(seeExplanation, showHideExplanation);

            Label explanationText = new Label();
            explanationText.setText(question.getQuestionAnswer().getExplanation());
            explanationText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));
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

            cbtVBox.getChildren().addAll(questionBox, optionAButton, optionBButton, optionCButton, optionDButton, hBox);

            showHideExplanation.setOnAction(event -> {
                if (showHideExplanation.getGraphic() == showExplanation){
                    showHideExplanation.setGraphic(hideExplanation);
                    seeExplanation.setText("Hide explanation");
                    cbtVBox.getChildren().add(explanationText);
                }else {
                    showHideExplanation.setGraphic(showExplanation);
                    seeExplanation.setText("See explanation");
                    cbtVBox.getChildren().remove(explanationText);
                }
            });

            VBox.setMargin(cbtVBox, new Insets(0, 100, 0, 0));

            contentElement = cbtVBox;

        } else if (contentViewType instanceof LatexMathViewType latexMathViewType) {
            if (latexMathViewType.getKatex() != null) {
//                org.commonmark.node.Node document = markdownParser.parse(latexMathViewType.getKatex());
//                String htmlKatex = htmlRenderer.render(document);
//
//                Document doc = Jsoup.parse(htmlKatex);
//                String formattedText = doc.body().text();

//                Label label = new Label(formattedText);
//                label.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
//                label.setWrapText(true);

                Label label1 = new Label(section.getContent());
                Label label2 = new Label(latexMathViewType.getKatex());
                label1.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
                label1.setWrapText(true);
                label2.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
                label2.setWrapText(true);

//                contentElement = label;
                MarkdownView mdfx = new MarkdownView(latexMathViewType.getKatex());
                contentElement = formatMarkdown(latexMathViewType.getKatex());
            }
        } else if (contentViewType instanceof ListViewType listViewType) {
//            System.out.println(TAG + "ContentViewType -> ListViewType");

            if (listViewType.getStyle().equalsIgnoreCase("unordered")) {

                ArrayList<UnorderedListItem> contentItems = new ArrayList<>();

                for (int i = 0; i < listViewType.getItems().size(); i++) {
                    UnorderedListItem unorderedListItem = new UnorderedListItem(listViewType.getItems().get(i));
                    contentItems.add(unorderedListItem);
                }

                ObservableList<UnorderedListItem> items = FXCollections.observableArrayList(contentItems);

                ListView<UnorderedListItem> contentList = new ListView<>();
                contentList.setItems(items);
                contentList.setBackground(Background.EMPTY);
                contentList.setCellFactory(new UnorderedListCellFactory());
                contentList.setSelectionModel(new NoSelectionModel<>());
                contentList.setPadding(new Insets(0, 0, 0, 10));

                contentElement = contentList;
            }

        } else if (contentViewType instanceof ReferenceViewType referenceViewType) {
//            System.out.println(TAG + "ContentViewType -> ReferenceViewType");

            if (referenceViewType.getText() != null) {
                Document doc = Jsoup.parse(referenceViewType.getText());
                String formattedText = doc.body().text();

                Label label = new Label(formattedText);
                label.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
                label.setWrapText(true);

                contentElement = label;
            }
        } else if (contentViewType instanceof TableViewType tableViewType) {
//            System.out.println(TAG + "ContentViewType -> TableViewType");

            GridPane tableGrid = new GridPane();
            tableGrid.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #F4D242");

            List<List<String>> content = tableViewType.getContent();

            for (int row = 0; row < content.size(); row++) {

//                System.out.println("Row Content -> " + content.get(row));
                for (int col = 0; col < content.get(row).size(); col++) {
//                    System.out.println("Column content -> " + content.get(row).get(col));
                    Label grid = new Label(content.get(row).get(col));
                    grid.setWrapText(true);
                    grid.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
                    grid.setPadding(new Insets(5, 5, 5, 5));

                    tableGrid.add(grid, col, row);
                }
            }

            contentElement = tableGrid;

        }
//        System.out.println(TAG + "ContentElement returned -> " + contentElement);
        return contentElement;
    }

    private Parent formatMarkdown(String content) {
//        List<Extension> DEFAULT_EXTENSIONS = ImmutableList.<Extension>builder()
//                .add(AbbreviationExtension.create())
//                .add(AutolinkExtension.create())
//                .add(AutoLinkRendererExtension.create())
//                .add(EscapedCharacterExtension.create())
//                .add(TaskListExtension.create())
//                .add(StrikethroughExtension.create())
//                .add(TablesExtension.create())
//                .add(TaskListRendererExtension.create())
//                .add(InsExtension.create())
//                .add(SuperscriptExtension.create())
//                .add(TocExtension.create())
//                .add(JekyllFrontMatterExtension.create())
//                .build();

        MutableDataSet options = new MutableDataSet();

        // uncomment to set optional extensions
//        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));

        // uncomment to convert soft-breaks to hard breaks
//        options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        // You can re-use parser and renderer instances
        Node document = parser.parse(content);
        String html = renderer.render(document);  // "<p>This is <em>Sparta</em></p>\n"
        System.out.println(html);

//        final SwingNode swingNode = new SwingNode();
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                swingNode.setContent(new JButton("Click me!"));
//            }
//        });

        HTMLEditorKit kit = new HTMLEditorKit();
        JTextPane chatPane = new JTextPane();
        chatPane.setEditable(false);
        chatPane.setContentType("text/html");
        chatPane.setEditorKit(kit);
        chatPane.setText(html);

        SwingNode swingNode = new SwingNode();
//        try {
//            SwingUtilities.invokeAndWait(() -> swingNode.setContent(chatPane));
//        } catch (InterruptedException | InvocationTargetException e) {
//            throw new RuntimeException(e);
//        }

        StackPane pane = new StackPane();
        pane.getChildren().add(swingNode);

        return pane;
    }
}
