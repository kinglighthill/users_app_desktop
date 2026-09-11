package com.scholarly.utme.ui.cellFactories;

import com.scholarly.utme.data.model.listItems.NotificationItem;
import com.scholarly.utme.ui.listcells.NotificationListItemCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class NotificationListCellFactory implements Callback<ListView<NotificationItem>, ListCell<NotificationItem>> {

    @Override
    public ListCell<NotificationItem> call(ListView<NotificationItem> notificationItemListView) {
        return new NotificationListItemCell();
    }
}
