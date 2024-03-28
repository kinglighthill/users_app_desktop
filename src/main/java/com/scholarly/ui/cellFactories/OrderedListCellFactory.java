package com.scholarly.ui.cellFactories;

import com.scholarly.data.model.listItems.OrderedListItem;
import com.scholarly.ui.listcells.OrderedListItemCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class OrderedListCellFactory implements Callback<ListView<OrderedListItem>, ListCell<OrderedListItem>> {

    @Override
    public ListCell<OrderedListItem> call(ListView<OrderedListItem> param) {
        return new OrderedListItemCell();
    }
}
