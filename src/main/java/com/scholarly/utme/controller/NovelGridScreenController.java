package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.ui.cellFactories.NovelGridCellFactory;
import com.scholarly.utme.viewmodels.NovelGridScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import org.controlsfx.control.cell.ImageGridCell;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/NovelGridScreen.fxml")
public class NovelGridScreenController implements FxmlView<NovelGridScreenVM>, Initializable {

    @FXML
    private GridView<Novel> novelGridView;

    @InjectViewModel
    private NovelGridScreenVM viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        novelGridView.setCellFactory(new NovelGridCellFactory());
        novelGridView.setItems(viewModel.getNovels());
    }
}
