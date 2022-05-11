package com.scholarly.utme.ui.listcells;

import com.scholarly.utme.data.model.listItems.OrderedListItem;
import com.scholarly.utme.ui.utils.FontUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;

import java.io.IOException;


public class OrderedListItemCell extends ListCell<OrderedListItem> {

    public Label itemIndex;
    public Label itemText;

    public OrderedListItemCell() {
        loadFxml();
    }

    private void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/ordered_list_item.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
           // System.out.println("OrderedList Loader loaded successfully");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(OrderedListItem item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }else {
            itemIndex.setText(item.getIndex() + ".");
            itemText.setText(item.getText());

            itemIndex.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
            itemText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
