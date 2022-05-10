package com.scholarly.utme.ui.listcells;

import com.scholarly.utme.data.model.listItems.UnorderedListItem;
import com.scholarly.utme.ui.utils.FontUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;

import java.io.IOException;

public class UnorderedListItemCell extends ListCell<UnorderedListItem> {
    public Label itemText;

    public UnorderedListItemCell() {
        loadFXML();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/unordered_list_item.fxml"));
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
            itemText.setText(item.getText());
            itemText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
