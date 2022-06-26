package com.scholarly.utme.ui.cellFactories;

import com.scholarly.utme.data.model.listItems.OrderedListItem;
import com.scholarly.utme.ui.listcells.OrderedListItemCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class OrderedListCellFactory implements Callback<ListView<OrderedListItem>, ListCell<OrderedListItem>> {

    @Override
    public ListCell<OrderedListItem> call(ListView<OrderedListItem> param) {
        return new OrderedListItemCell();
    }
}
