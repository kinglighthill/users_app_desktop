package com.scholarly.ui.cellFactories;

import com.scholarly.data.model.novels.NovelObjectiveQuestion;
import com.scholarly.ui.listcells.NovelChapterQuestionListItemCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class NovelChapterQuestionListCellFactory implements Callback<ListView<NovelObjectiveQuestion>, ListCell<NovelObjectiveQuestion>> {

    @Override
    public ListCell<NovelObjectiveQuestion> call(ListView<NovelObjectiveQuestion> param) {
        return new NovelChapterQuestionListItemCell();
    }
}
