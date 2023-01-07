package com.ake.ckey.view;

import com.ake.ckey.controller.AlertController;
import com.ake.ckey.model.StructGraphicModel;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 有变化结构的地方
 */
public class StructureView {

    /** 结构画板 */
    private Pane structPane;
    private StructGraphicModel snapshot;

    public StructureView(){
        this.structPane = new Pane();
        this.structPane.setMinHeight(500);
        this.structPane.setMinWidth(500);

        this.structPane.widthProperty().addListener((obs, oldVal, newVal) -> refreshLayout());
        this.structPane.heightProperty().addListener((obs, oldVal, newVal) -> refreshLayout());
    }

    public void refreshLayout(){
        this.updateView(this.snapshot);
    }

    public void updateView(StructGraphicModel model){
        this.snapshot = model;
        if (model == null) return;

        this.structPane.setBorder(new Border(
                new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID,
                        new CornerRadii(2), BorderStroke.THICK)));
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

        // 4. 添加label提示对应的编码，十六进制
        FlowPane hBox = new FlowPane(Orientation.HORIZONTAL);
        hBox.setMaxWidth(500);
        String regularVerticalBits = getRegularCode(model.getRows(), model.getVerticalBits());
        String regularHorizontalBits = getRegularCode(model.getRows(), model.getHorizontalBits());
        Label label = new Label();
        label.setText("最终编码为: ");
        hBox.getChildren().add(label);

        Label labelVerticalBits = new Label();
        labelVerticalBits.setText(regularVerticalBits);
        labelVerticalBits.setBackground(Background.fill(Color.GREEN));
        hBox.getChildren().add(labelVerticalBits);

        Label labelHorizontalBits = new Label();
        labelHorizontalBits.setBackground(Background.fill(Color.RED));
        labelHorizontalBits.setText(regularHorizontalBits);
        hBox.getChildren().add(labelHorizontalBits);

        String finalBits = regularVerticalBits + regularHorizontalBits;

        BigInteger resultNumber = finalBits.isBlank() ? BigInteger.ZERO : new BigInteger(finalBits, 2);
        Label label1 = new Label();
        label1.setText("最终表示的整数为： " + resultNumber);
        label1.setBackground(Background.fill(Color.YELLOWGREEN));
        label1.setMinWidth(400);
        hBox.getChildren().add(label1);

        TextField label2 = new TextField();
        label2.setText("对应的十六进制编码为 0x" + bin2hex(finalBits));
        label2.setBackground(Background.fill(Color.BISQUE));
        label2.setEditable(false);
        label2.setPrefWidth(450);
        hBox.getChildren().add(label2);

        hBox.setLayoutX(10);
        hBox.setLayoutY(10);

        this.structPane.getChildren().add(hBox);
    }

    private String bin2hex(String input) {
        if (input.isBlank()) return "";
        StringBuilder sb = new StringBuilder();
        int len = input.length();
        if (input.length()%8 != 0) {
            // 需要前面补0
            int leftBits = 8 - input.length()%8;
            List<String> bitList = Arrays.stream(input.split("")).collect(Collectors.toList());
            for (int i = 0; i < leftBits; i++) {
                bitList.add(0, "0");
            }
            input = String.join("", bitList);
            len = input.length();
        }

        for (int i = 0; i < len / 4; i++){
            //每4个二进制位转换为1个十六进制位
            String temp = input.substring(i * 4, (i + 1) * 4);
            int tempInt = Integer.parseInt(temp, 2);
            String tempHex = Integer.toHexString(tempInt).toUpperCase();
            sb.append(tempHex);
        }

        return sb.toString();
    }

    private String getRegularCode(int rows, String bits){
        if (rows < 2) {
            return "";
        }
        if (bits == null) {
            bits = "";
        }
        bits = bits.trim();
        int maxBitSize = rows * (rows - 1);
        if (bits.length() > maxBitSize) {
            return bits.substring(0, maxBitSize);
        } else {
            // 这边需要后边补0
            int leftBits = maxBitSize - bits.length();
            StringBuilder tmpResult = new StringBuilder(bits);
            for (int i = 0; i < leftBits; i++) {
                tmpResult.append('0');
            }
            return tmpResult.toString();
        }
    }

    public Pane getStructPane() {
        return structPane;
    }
}
