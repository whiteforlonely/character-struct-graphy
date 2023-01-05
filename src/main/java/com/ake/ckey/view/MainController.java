package com.ake.ckey.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * 专门生产组件的地方
 */
public class MainController {

    private StructureView structureView;
    private ParamControlView paramControlView;

    public Scene initWindow(){
        structureView = new StructureView();
        paramControlView = new ParamControlView();
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.BASELINE_LEFT);
        vBox.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(1), BorderStroke.THIN)));
        for (ParamView paramView : paramControlView.getParamViews()) {
            vBox.getChildren().add(paramView.getRoot());
        }
        HBox window = new HBox(vBox, structureView.getStructPane());
        structureView.getStructPane().setBorder(new Border(
                new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID,
                        new CornerRadii(2), BorderStroke.THICK)));

        paramControlView.setListener(dataModel -> this.structureView.updateView(dataModel));

        return new Scene(window);
    }

    public void refreshView(){
        this.paramControlView.invokeListener();
    }
}
