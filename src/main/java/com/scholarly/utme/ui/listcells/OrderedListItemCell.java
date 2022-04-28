package com.scholarly.utme.ui.listcells;

import com.scholarly.utme.data.model.OrderedListItem;
import com.scholarly.utme.ui.utils.FontUtil;
import de.saxsys.mvvmfx.FxmlPath;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


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
