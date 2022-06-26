package com.scholarly.utme.ui.listcells;

import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.ui.utils.FontUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.io.IOException;

public class PracticeSubjectListItemCell extends ListCell<Subject> {

    public Label subjectName;
    public Panel subjectBox;
    public RadioButton radioButton;

    public PracticeSubjectListItemCell() {
        loadFxml();

        selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {
                subjectBox.setStyle("-fx-background-color: #12AF20; -fx-background-radius: 5;");
                subjectName.setTextFill(Paint.valueOf("#FFFFFF"));
                radioButton.setSelected(true);
            }else {
                subjectBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 5;");
                subjectName.setTextFill(Paint.valueOf("#053500"));
                radioButton.setSelected(false);
            }
        }));
    }

    private void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/list_items/practice_subject_list_item.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(Subject item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }else {
            subjectName.setText(item.getSubjectName());
            subjectName.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));

            radioButton.setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

}
