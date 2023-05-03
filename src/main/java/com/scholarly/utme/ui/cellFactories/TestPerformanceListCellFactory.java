package com.scholarly.utme.ui.cellFactories;

import com.scholarly.utme.controller.practice_screens.ResultScreenController;
import com.scholarly.utme.ui.listcells.TestPerformanceListItemCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class TestPerformanceListCellFactory implements Callback<ListView<ResultScreenController.InitialData>, ListCell<ResultScreenController.InitialData>> {

    @Override
    public ListCell<ResultScreenController.InitialData> call(ListView<ResultScreenController.InitialData> testPerformanceItemListView) {
        return new TestPerformanceListItemCell();
    }
}
