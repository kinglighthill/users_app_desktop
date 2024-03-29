package com.scholarly.ui.cellFactories;

import com.scholarly.data.model.listItems.VideoItem;
import com.scholarly.ui.listcells.VideoListItemCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class VideoListCellFactory implements Callback<ListView<VideoItem>, ListCell<VideoItem>> {

    @Override
    public ListCell<VideoItem> call(ListView<VideoItem> videoItemListView) {
        return new VideoListItemCell();
    }
}
