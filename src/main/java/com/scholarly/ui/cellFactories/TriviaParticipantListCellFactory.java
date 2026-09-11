package com.scholarly.ui.cellFactories;

import com.scholarly.data.model.listItems.TriviaParticipantItem;
import com.scholarly.ui.listcells.TriviaParticipantListItemCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class TriviaParticipantListCellFactory implements Callback<ListView<TriviaParticipantItem>, ListCell<TriviaParticipantItem>> {

    @Override
    public ListCell<TriviaParticipantItem> call(ListView<TriviaParticipantItem> triviaParticipantItemListView) {
        return new TriviaParticipantListItemCell();
    }
}
