package com.ake.ckey.view;

import com.ake.ckey.controller.AlertController;
import com.ake.ckey.model.StructGraphicModel;
import javafx.geometry.Insets;
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
            double borderStartX = startx - model.getBorderWidth();
            double borderStartY = starty - model.getBorderWidth();

            Rectangle borderRect = new Rectangle(borderStartX, borderStartY, structWidth + 2*model.getBorderWidth(), structWidth + 2*model.getBorderWidth());
            borderRect.setFill(model.getBorderColor());
            borderRect.setStroke(model.getBorderColor());
            borderRect.setArcWidth(5);
            borderRect.setArcHeight(5);

            this.structPane.getChildren().add(borderRect);
        }

        // 画矩形
        Rectangle rectangle = new Rectangle(startx, starty, structWidth, structWidth);
        rectangle.setFill(model.getBackgroundColor());
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
            line.setStroke(model.getLineBackground());
            line.setStrokeWidth(model.getLineSegmentWidth());
            this.structPane.getChildren().add(line);

            // 初始化对应的短线段
            double shortLineStartX = startx;
            for (int j = 0; j < model.getRows(); j++) {
                double sx = shortLineStartX;
                if (j > 0) {
                    // 非起始位置，需要做偏移
                    sx = shortLineStartX - model.getLineSegmentWidth();
                }
                Line shortLine = new Line(sx, lineStartY, shortLineStartX + model.getCellWidth(), lineStartY);
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
            line.setStroke(model.getLineBackground());
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

        // 5. 添加label提示对应的编码，十六进制
        FlowPane hBox = new FlowPane(Orientation.HORIZONTAL);
        hBox.setMinWidth(450);
        String regularVerticalBits = getRegularCode(model.getRows(), model.getVerticalBits());
        String regularHorizontalBits = getRegularCode(model.getRows(), model.getHorizontalBits());
        Label label = new Label();
        label.setText("最终编码V：");
        label.setPadding(new Insets(0, 0, 0, 10));
        hBox.getChildren().add(label);

        TextField labelVerticalBits = new TextField();
        labelVerticalBits.setText(regularVerticalBits);
        labelVerticalBits.setBackground(Background.fill(Color.GREEN));
        labelVerticalBits.setEditable(false);
        labelVerticalBits.setMinWidth(350);
        hBox.getChildren().add(labelVerticalBits);

        Label labelH = new Label();
        labelH.setText("最终编码H：");
        labelH.setPadding(new Insets(0, 0, 0, 10));
        hBox.getChildren().add(labelH);

        TextField labelHorizontalBits = new TextField();
        labelHorizontalBits.setBackground(Background.fill(Color.RED));
        labelHorizontalBits.setText(regularHorizontalBits);
        labelHorizontalBits.setEditable(false);
        labelHorizontalBits.setMinWidth(350);
        hBox.getChildren().add(labelHorizontalBits);

        String finalBits = regularVerticalBits + regularHorizontalBits;

        BigInteger resultNumber = finalBits.isBlank() ? BigInteger.ZERO : new BigInteger(finalBits, 2);
        Label label1 = new Label();
        label1.setText("对应整数：");
        label1.setPadding(new Insets(0, 0, 0, 10));
        hBox.getChildren().add(label1);

        TextField resultNumberCom = new TextField();
        resultNumberCom.setBackground(Background.fill(Color.YELLOWGREEN));
        resultNumberCom.setText(resultNumber.toString());
        resultNumberCom.setEditable(false);
        resultNumberCom.setMinWidth(350);
        hBox.getChildren().add(resultNumberCom);

        Label label2 = new Label();
        label2.setText("对应十六进制：");
        label2.setPadding(new Insets(0, 0, 0, 10));
        hBox.getChildren().add(label2);

        TextField hexNumberCom = new TextField();
        hexNumberCom.setBackground(Background.fill(Color.DODGERBLUE));
        hexNumberCom.setText(bin2hex(finalBits));
        hexNumberCom.setEditable(false);
        hexNumberCom.setMinWidth(320);
        hBox.getChildren().add(hexNumberCom);

        hBox.setLayoutX(10);
        hBox.setLayoutY(10);

        this.structPane.getChildren().add(hBox);

        // 4. 开始画对应的短线
        int regularBitLength = regularVerticalBits.length();
        if (null != model.getHorizontalBits()  && !model.getHorizontalBits().isBlank()) {
            // 画横线，找出对应的横线段二进制输入文本
            String[] horizontalBitArray = model.getHorizontalBits().split("");
            for (int i = 0; i < horizontalBitArray.length; i++) {
                if (i == regularBitLength) break;
                // 通过位运算来画线段， (10001010 >> (regularBitLength - i - 1)) & 00000001
                BigInteger bitVal = resultNumber.shiftRight(regularBitLength - i - 1).and(BigInteger.ONE);
                if (bitVal.equals(BigInteger.ONE)) {
                    this.structPane.getChildren().add(horizontalShortLineList.get(i));
                }
            }
        }
        if (null != model.getVerticalBits() && !model.getVerticalBits().isBlank()) {
            // 画竖线，获取对应竖线段二进制输入文本
            String[] verticalBitArray = model.getVerticalBits().split("");
            for (int i = 0; i < verticalBitArray.length; i++) {
                if (i == regularBitLength) break;
                BigInteger bitVal = resultNumber.shiftRight(regularBitLength + (regularBitLength - i - 1)).and(BigInteger.ONE);
                if (bitVal.equals(BigInteger.ONE)) {
                    this.structPane.getChildren().add(verticalShortLineList.get(i));
                }
            }
        }
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

    /**
     * 针对于输入框中的二进制文本，转换成规范的二进制文本，
     * 二进制的位数是跟对应的宫格数有关系的，计算公式为：
     * bitLength(二进制长度) = rows(宫格数) * (rows - 1)
     *
     * 如果输入的产出规定的长度，就直接截取，如果少于规定长度，就直接尾部补0
     *
     * @param rows 宫格数
     * @param bits 原始文本框输入的数据
     * @return 规范的二进制数
     */
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
            return bits + "0".repeat(leftBits);
        }
    }

    public Pane getStructPane() {
        return structPane;
    }
}
