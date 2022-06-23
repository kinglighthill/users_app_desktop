package com.scholarly.utme.ui.cellFactories;

import com.scholarly.utme.data.model.listItems.TriviaParticipantItem;
import com.scholarly.utme.ui.listcells.TriviaRankingGridItemCell;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

public class TriviaRankingGridCellFactory implements Callback<GridView<TriviaParticipantItem>, GridCell<TriviaParticipantItem>> {

    @Override
    public GridCell<TriviaParticipantItem> call(GridView<TriviaParticipantItem> triviaParticipantItemGridView) {
        return new TriviaRankingGridItemCell();
    }
}
