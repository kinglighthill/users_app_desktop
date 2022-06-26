package com.scholarly.utme.ui.cellFactories;

import com.scholarly.utme.data.model.listItems.VideoItem;
import com.scholarly.utme.ui.listcells.VideoListItemCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class VideoListCellFactory implements Callback<ListView<VideoItem>, ListCell<VideoItem>> {

    @Override
    public ListCell<VideoItem> call(ListView<VideoItem> videoItemListView) {
        return new VideoListItemCell();
    }
}
