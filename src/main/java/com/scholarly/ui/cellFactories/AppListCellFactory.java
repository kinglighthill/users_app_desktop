package com.scholarly.ui.cellFactories;

import com.scholarly.data.model.listItems.AppItem;
import com.scholarly.ui.listcells.AppListItemCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class AppListCellFactory implements Callback<ListView<AppItem>, ListCell<AppItem>> {

    @Override
    public ListCell<AppItem> call(ListView<AppItem> appItemListView) {
        return new AppListItemCell();
    }
}
