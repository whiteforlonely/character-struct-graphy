package com.ake.ckey.view;

import com.ake.ckey.controller.AlertController;
import com.ake.ckey.model.StructGraphicModel;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

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

        List<Line> verticalShortLineList = new ArrayList<>();
        List<Line> horizontalShortLineList = new ArrayList<>();

        // 画网格线
        // 1. 横的网格线
        double lineStartY = starty;
        for (int i = 0; i < model.getRows() - 1; i++) {
            lineStartY += model.getCellWidth();
            Line line = new Line(startx, lineStartY, startx + structWidth, lineStartY);
            line.setStroke(Color.rgb(255,255,255,.2));
            line.setStrokeWidth(model.getLineSegmentWidth());
            this.structPane.getChildren().add(line);

            // 初始化对应的短线段
            double shortLineStartX = startx;
            for (int j = 0; j < model.getRows(); j++) {
                Line shortLine = new Line(shortLineStartX - model.getLineSegmentWidth(), lineStartY, shortLineStartX + model.getCellWidth(), lineStartY);
                shortLine.setStrokeWidth(model.getLineSegmentWidth());
                shortLine.setStroke(model.getLineSegmentColor());
                horizontalShortLineList.add(shortLine);
                shortLineStartX += model.getCellWidth();
                shortLineStartX += model.getLineSegmentWidth();
            }

            lineStartY += model.getLineSegmentWidth();
        }
        // 2. 竖的网格线
        double lineStartX = startx;
        for (int i = 0; i < model.getRows() - 1; i ++) {
            lineStartX += model.getCellWidth();
            Line line = new Line(lineStartX, starty, lineStartX, starty + structWidth);
            line.setStroke(Color.rgb(255,255,255,.2));
            line.setStrokeWidth(model.getLineSegmentWidth());
            this.structPane.getChildren().add(line);

            // 初始化对应的短线段
            double shortLineStartY = starty;
            for (int j = 0; j < model.getRows(); j++) {
                Line shortLine = new Line(lineStartX, shortLineStartY, lineStartX, shortLineStartY + model.getCellWidth());
                shortLine.setStrokeWidth(model.getLineSegmentWidth());
                shortLine.setStroke(model.getLineSegmentColor());
                verticalShortLineList.add(shortLine);
                shortLineStartY += model.getCellWidth();
                shortLineStartY += model.getLineSegmentWidth();
            }

            lineStartX += model.getLineSegmentWidth();
        }

        // 3. 开始画对应的短线
        if (null != model.getHorizontalBits()  && !model.getHorizontalBits().isBlank()) {
            String[] horizontalBitArray = model.getHorizontalBits().split("");
            for (int i = 0; i < horizontalBitArray.length; i++) {
                if (i == horizontalShortLineList.size()) break;
                if ("1".equals(horizontalBitArray[i])) {
                    this.structPane.getChildren().add(horizontalShortLineList.get(i));
                }
            }
        }
        if (null != model.getVerticalBits() && !model.getVerticalBits().isBlank()) {
            String[] verticalBitArray = model.getVerticalBits().split("");
            for (int i = 0; i < verticalBitArray.length; i++) {
                if (i == verticalShortLineList.size()) break;
                if ("1".equals(verticalBitArray[i])) {
                    this.structPane.getChildren().add(verticalShortLineList.get(i));
                }
            }
        }
    }

    public Pane getStructPane() {
        return structPane;
    }
}
