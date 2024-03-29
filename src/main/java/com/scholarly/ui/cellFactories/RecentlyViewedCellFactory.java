package com.scholarly.ui.cellFactories;

import com.scholarly.data.model.Course;
import com.scholarly.ui.listcells.RecentlyViewedItemCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class RecentlyViewedCellFactory implements Callback<ListView<Course>, ListCell<Course>> {
    @Override
    public ListCell<Course> call(ListView<Course> param) {
        return new RecentlyViewedItemCell();
    }
}
