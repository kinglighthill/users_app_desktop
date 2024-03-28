package com.scholarly.ui.listcells;

import com.scholarly.data.model.listItems.UnorderedListItem;
import com.scholarly.ui.utils.FontUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class UnorderedListItemCell extends ListCell<UnorderedListItem> {
    public Label itemText;
    public Circle dot;
    private String subjectColor;

    public UnorderedListItemCell(String subjectColor) {
        loadFXML();
        setPrefWidth(300);
        this.subjectColor = subjectColor;
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/list_items/unordered_list_item.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(UnorderedListItem item, boolean empty) {
        super.updateItem(item, empty);

        if(empty || item == null) {
            setText(null);
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        else {
            dot.setFill(Paint.valueOf(subjectColor));
            itemText.setText(item.getText());
            itemText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
