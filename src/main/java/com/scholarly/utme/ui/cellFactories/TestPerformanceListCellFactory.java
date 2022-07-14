package com.scholarly.utme.ui.cellFactories;

import com.scholarly.utme.data.model.listItems.TestPerformanceItem;
import com.scholarly.utme.ui.listcells.TestPerformanceListItemCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.util.List;

public class TestPerformanceListCellFactory implements Callback<ListView<TestPerformanceItem>, ListCell<TestPerformanceItem>> {

    @Override
    public ListCell<TestPerformanceItem> call(ListView<TestPerformanceItem> testPerformanceItemListView) {
        return new TestPerformanceListItemCell();
    }
}
