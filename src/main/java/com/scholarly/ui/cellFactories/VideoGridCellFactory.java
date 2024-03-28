package com.scholarly.ui.cellFactories;

import com.scholarly.data.model.listItems.VideoItem;
import com.scholarly.ui.listcells.VideoGridItemCell;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

public class VideoGridCellFactory implements Callback<GridView<VideoItem>, GridCell<VideoItem>> {

    @Override
    public GridCell<VideoItem> call(GridView<VideoItem> videoItemGridView) {
        return new VideoGridItemCell();
    }
}
