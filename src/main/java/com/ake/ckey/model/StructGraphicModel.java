package com.ake.ckey.model;

import javafx.scene.paint.Color;

/**
 * 结构参数信息
 */
public class StructGraphicModel {

    /** 宫格数量 */
    private int rows = 5;

    /**
     * 结构编码
     * （对应二进制数文本，由竖线段和横线短拼接，
     * 竖线高位，横线低位，统一通过此转换成对应
     * 的数字或者其他形式的编码）
     * */
    private String structCode;

    /** 竖线段的显示标志 */
    private String verticalBits;

    /** 横的显示标志 */
    private String horizontalBits;

    /** 每一个的宽度 */
    private double cellWidth = 50;

    /** 小线段的宽度 */
    private double lineSegmentWidth = 2;

    /** 小线段的颜色 */
    private Color lineSegmentColor = Color.GRAY;

    /** 结构图背景色 */
    private Color backgroundColor = Color.BLACK;

    /** 是否有边框 */
    private boolean hasBorder = true;

    public Color getLineSegmentColor() {
        return lineSegmentColor;
    }

    public void setLineSegmentColor(Color lineSegmentColor) {
        this.lineSegmentColor = lineSegmentColor;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getStructCode() {
        return structCode;
    }

    public void setStructCode(String structCode) {
        this.structCode = structCode;
    }

    public String getVerticalBits() {
        return verticalBits;
    }

    public void setVerticalBits(String verticalBits) {
        this.verticalBits = verticalBits;
    }

    public String getHorizontalBits() {
        return horizontalBits;
    }

    public void setHorizontalBits(String horizontalBits) {
        this.horizontalBits = horizontalBits;
    }

    public double getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(double cellWidth) {
        this.cellWidth = cellWidth;
    }

    public double getLineSegmentWidth() {
        return lineSegmentWidth;
    }

    public void setLineSegmentWidth(double lineSegmentWidth) {
        this.lineSegmentWidth = lineSegmentWidth;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isHasBorder() {
        return hasBorder;
    }

    public void setHasBorder(boolean hasBorder) {
        this.hasBorder = hasBorder;
    }

    @Override
    public String toString() {
        return "StructGraphicModel{" +
                "rows=" + rows +
                ", structCode='" + structCode + '\'' +
                ", verticalBits='" + verticalBits + '\'' +
                ", horizontalBits='" + horizontalBits + '\'' +
                ", cellWidth=" + cellWidth +
                ", lineSegmentWidth=" + lineSegmentWidth +
                ", lineSegmentColor=" + lineSegmentColor +
                ", backgroundColor=" + backgroundColor +
                ", hasBorder=" + hasBorder +
                '}';
    }
}

