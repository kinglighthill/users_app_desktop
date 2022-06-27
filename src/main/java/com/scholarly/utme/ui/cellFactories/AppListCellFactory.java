package com.scholarly.utme.ui.cellFactories;

import com.scholarly.utme.data.model.listItems.AppItem;
import com.scholarly.utme.ui.listcells.AppListItemCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class AppListCellFactory implements Callback<ListView<AppItem>, ListCell<AppItem>> {

    @Override
    public ListCell<AppItem> call(ListView<AppItem> appItemListView) {
        return new AppListItemCell();
    }
}
