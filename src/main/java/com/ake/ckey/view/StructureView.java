package com.ake.ckey.view;

import com.ake.ckey.controller.AlertController;
import com.ake.ckey.model.StructGraphicModel;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 * 有变化结构的地方
 */
public class StructureView {

    /** 结构画板 */
    private Pane structPane;

    public StructureView(){
        this.structPane = new Pane();
        this.structPane.setMinHeight(1000);
        this.structPane.setMinWidth(1000);
    }

    public void updateView(StructGraphicModel model){
        this.structPane.getChildren().clear();

        double width = structPane.getWidth();
        double height = structPane.getHeight();

        double structWidth = model.getRows()*model.getCellWidth() + (model.getRows()-1) * model.getLineSegmentWidth();

        // 确认矩形的起始位置
        if (width <= structWidth) {
            AlertController.exception("设置宽度超出限制", "画板当前宽度为："+width+", 可以减少宫格数或者方格宽度");
            return;
        }
        if (height <= structWidth) {
            AlertController.exception("设置高度超出限制", "画板当前高度为："+height+", 可以减少宫格数或者方格宽度");
            return;
        }
        double startx = (width - structWidth)/2;
        double starty = (height - structWidth)/2;

        if (model.isHasBorder()) {
            // 画边框
            double borderStartX = startx - 2;
            double borderStartY = starty - 2;

            Rectangle borderRect = new Rectangle(borderStartX, borderStartY, structWidth + 4, structWidth + 4);
            borderRect.setStroke(Color.YELLOWGREEN);
            borderRect.setArcWidth(5);
            borderRect.setArcHeight(5);

            this.structPane.getChildren().add(borderRect);
        }

        // 画矩形
        Rectangle rectangle = new Rectangle(startx, starty, structWidth, structWidth);
        rectangle.setStroke(model.getBackgroundColor());
        rectangle.setArcWidth(5);
        rectangle.setArcHeight(5);

        this.structPane.getChildren().add(rectangle);

        // 画网格线
        // 1. 横的网格线
        double lineStartY = starty;
        for (int i = 0; i < model.getRows() - 1; i++) {
            lineStartY += model.getCellWidth();
            Line line = new Line(startx, lineStartY, startx + structWidth, lineStartY);
            line.setStroke(Color.ALICEBLUE);
            line.setStrokeWidth(model.getLineSegmentWidth());
            this.structPane.getChildren().add(line);
            lineStartY += model.getLineSegmentWidth();
        }
        // 2. 竖的网格线
        double lineStartX = startx;
        for (int i = 0; i < model.getRows() - 1; i ++) {
            lineStartX += model.getCellWidth();
            Line line = new Line(lineStartX, starty, lineStartX, starty + structWidth);
            line.setStroke(Color.ALICEBLUE);
            line.setStrokeWidth(model.getLineSegmentWidth());
            this.structPane.getChildren().add(line);
            lineStartX += model.getLineSegmentWidth();
        }
    }

    public Pane getStructPane() {
        return structPane;
    }
}
