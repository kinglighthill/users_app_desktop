package com.scholarly.utme.ui.cellFactories;

import com.scholarly.utme.data.model.listItems.TriviaChallengeItem;
import com.scholarly.utme.ui.listcells.TriviaChallengeListItemCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class TriviaChallengeListCellFactory implements Callback<ListView<TriviaChallengeItem>, ListCell<TriviaChallengeItem>> {

    @Override
    public ListCell<TriviaChallengeItem> call(ListView<TriviaChallengeItem> triviaChallengeItemListView) {
        return new TriviaChallengeListItemCell();
    }
}
