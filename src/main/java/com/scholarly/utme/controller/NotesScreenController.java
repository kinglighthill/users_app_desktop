package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.Section;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.Topic;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.NotesScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.transform.Shear;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


@FxmlPath("/layouts/NotesScreen.fxml")
public class NotesScreenController implements FxmlView<NotesScreenVM>, Initializable {


    @InjectViewModel
    private NotesScreenVM viewModel;

    @FXML
    private ListView<Topic> topicList;

    @FXML
    private VBox contentLayout;

    @FXML
    private Label pageTitle, subjectLabel, topicLabel, topicTitle;

    @FXML
    private ImageView notesImage;

    @FXML
    private Button backButton, prevButton, nextButton, practiceTopicButton;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel.initialize(getInitialData());

        topicList.setItems(viewModel.getTopics());

        topicList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super Topic>) c -> {
            if (c.getList().size() == 1) {
                viewModel.setSelectedTopic(c.getList().get(0));
                renderNote(c.getList().get(0));
                topicLabel.setText(c.getList().get(0).getTitle());
            } else {
                viewModel.setSelectedTopic(null);
            }
        });

        topicList.getSelectionModel().select(viewModel.getSelectedTopic());

        notesImage.setImage(new Image(getClass().getResource("/drawable/notes.png").toString()));

        ImageView view = new ImageView(new Image(getClass().getResource("/drawable/back_button_white.png").toString()));
        view.setFitHeight(25);
        view.setPreserveRatio(true);

        backButton.setGraphic(view);

        ImageView nextView = new ImageView(new Image(getClass().getResource("/drawable/next_icon.png").toString()));
        nextView.setFitHeight(20);
        nextView.setPreserveRatio(true);

        nextButton.setGraphic(nextView);

        ImageView prevView = new ImageView(new Image(getClass().getResource("/drawable/prev_icon.png").toString()));
        prevView.setFitHeight(20);
        prevView.setPreserveRatio(true);

        prevButton.setGraphic(prevView);
        backButton.setBackground(Background.EMPTY);

        pageTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 24));
        subjectLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 18));
        topicLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        topicTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 16));

        practiceTopicButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 14));

        prevButton.setOnAction(event -> {
            if (topicList.getSelectionModel().getSelectedIndex() > 0) {
                topicList.getSelectionModel().select(topicList.getSelectionModel().getSelectedIndex() - 1);
            }
        });

        nextButton.setOnAction(event -> {
            if (topicList.getSelectionModel().getSelectedIndex() < topicList.getItems().size() - 1) {
                topicList.getSelectionModel().select(topicList.getSelectionModel().getSelectedIndex() + 1);
            }
        });

        backButton.setOnAction(event -> {
            ViewSwitcher.showScreen(View.SELECT_NOTE_SCREEN);
        });
    }

    private void renderNote(Topic topic) {
        List<Node> contentElements = new ArrayList<>();


        contentElements.addAll(
                viewModel.getSections()
                        .stream()
                        .filter(section -> section.getTopicId() == topic.getId())
                        .map(section -> {
                            if (section.getContent() != null) {
                                Label header = new Label();
                                header.setText(section.getContent());
                                header.setWrapText(true);
                                header.setUnderline(true);
                                header.setLineSpacing(8);
                                header.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 20));

                                return header;
                            } else {
                                ImageView image = new ImageView(new Image(getClass().getResource("/drawable/dummy_image.jpg").toString()));
                                image.setFitHeight(240);
                                image.setPreserveRatio(true);

                                return image;
                            }
                        })
                        .collect(Collectors.toList())
        );



        for (int i = 0; i < viewModel.getSections().size(); i++) {
            Section section = viewModel.getSections().get(i);
            if (section.getTopicId() == topic.getId()) {
                contentElements.addAll(
                        viewModel.getSectionsSubSections().get(section.getId())
                                .stream()
                                .map(subSection -> {
                                    if (subSection.getContent() != null) {
                                        Label header = new Label();
                                        header.setText(subSection.getContent());
                                        header.setWrapText(true);
                                        header.setLineSpacing(5);
                                        header.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));

                                        return header;
                                    } else {
                                        ImageView image = new ImageView(new Image(getClass().getResource("/drawable/dummy_image.jpg").toString()));
                                        image.setFitHeight(240);
                                        image.setPreserveRatio(true);

                                        return image;
                                    }
                                })
                                .collect(Collectors.toList())
                );
            }
        }

        contentLayout.getChildren().clear();
        contentLayout.getChildren().addAll(contentElements);
    }

    private InitialData getInitialData() {
        InitialData data = (InitialData) ViewSwitcher.retrieveData();
        System.out.println("Got data -> " + data);
        return data;
    }

    public static class InitialData {
        private Subject subject;
        private List<Topic> topics;
        private Topic selectedTopic;

        public InitialData(Subject subject, List<Topic> topics, Topic selectedTopic) {
            this.subject = subject;
            this.topics = topics;
            this.selectedTopic = selectedTopic;
        }

        public Subject getSubject() {
            return subject;
        }

        public Topic getSelectedTopic() {
            return selectedTopic;
        }

        public List<Topic> getTopics() {
            return topics;
        }
    }
}
