package com.scholarly.utme.async;

import com.scholarly.utme.MainApplication;
import com.scholarly.utme.controller.SubjectListViewController;
import com.scholarly.utme.viewmodels.SubjectListViewVM;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.util.Pair;

public class PQScreenTask<V extends FxmlView<? extends ViewModel>> extends Task<Pair<V, Parent>> {
    private final Class<V> controller;

    public PQScreenTask(Class<V> controller) {
        this.controller = controller;
    }

    @Override
    protected Pair<V, Parent> call() {
        /*ViewTuple<SubjectListViewController, SubjectListViewVM> subjectListViewTuple = FluentViewLoader.fxmlView(SubjectListViewController.class).load();
        var subjectListController = subjectListViewTuple.getCodeBehind();
        var subjectListView = subjectListViewTuple.getView();*/

        /*var viewTuple = FluentViewLoader.fxmlView(controller).load();
        var mController = viewTuple.getCodeBehind();
        var view = viewTuple.getView();
        MainApplication.timeTakenTo("load " + controller.getSimpleName());
        return view;*/

        var viewTuple = FluentViewLoader.fxmlView(controller).load();
        V controllerObject = viewTuple.getCodeBehind();
        Parent view = viewTuple.getView();
        MainApplication.timeTakenTo("load " + controller.getSimpleName());
        return new Pair<>(controllerObject, view);
    }

    @Override
    protected void updateValue(Pair<V, Parent> value) {
        super.updateValue(value);
    }
}