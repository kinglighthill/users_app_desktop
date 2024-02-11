package com.scholarly.utme.async;

import com.scholarly.utme.MainApplication;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewModel;
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