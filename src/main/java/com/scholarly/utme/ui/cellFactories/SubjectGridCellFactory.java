package com.scholarly.utme.ui.cellFactories;

import com.scholarly.utme.data.model.newDb.FavoriteSubject;
import com.scholarly.utme.ui.listcells.SubjectGridItemCell;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

public class SubjectGridCellFactory implements Callback<GridView<FavoriteSubject>, GridCell<FavoriteSubject>> {
    @Override
    public GridCell<FavoriteSubject> call(GridView<FavoriteSubject> param) {
        return new SubjectGridItemCell();
    }
}
