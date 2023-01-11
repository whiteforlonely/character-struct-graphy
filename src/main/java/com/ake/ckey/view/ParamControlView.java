package com.ake.ckey.view;

import com.ake.ckey.model.StructGraphicModel;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 左边栏
 */
public class ParamControlView {

    /**
     * 约定
     * 参数1： n - 几格 3，4，5，6，7，，，100
     * 参数2：0b101010 - 竖线段标识，从上到下，总做到右，二进制文本，
     * 参数3：0b111111 - 横线段标识，从上到下，从左到右，二进制文本，
     * 参数4： 方块宽度
     * 参数5： 线段宽度
     * 参数6： 线段颜色
     * 参数7： 线段背景色
     * 参数8： 全局背景色
     * 参数9： 是否有边框
     * 参数10： 边框宽度
     * 参数11： 边框颜色
     */
    public List<ParamView> paramViews;
    private StructGraphicModel dataModel;
    private ParamChangeListener listener;

    public ParamControlView(){
        paramViews = new ArrayList<>();
        paramViews.add(initParam(TextField.class, "宫格数") );
        paramViews.add(initParam(TextField.class, "竖线段(1101)") );
        paramViews.add(initParam(TextField.class, "横线段(1101)") );
        paramViews.add(initParam(TextField.class, "小方块宽度"));
        paramViews.add(initParam(TextField.class, "线段宽度"));
        paramViews.add(initParam(TextField.class, "线段颜色"));
        paramViews.add(initParam(TextField.class, "线段背景色"));
        paramViews.add(initParam(TextField.class, "全局背景色"));
        paramViews.add(initParam(TextField.class, "是否有边框"));
        paramViews.add(initParam(TextField.class, "边框宽度"));
        paramViews.add(initParam(TextField.class, "边框颜色"));

        dataModel = new StructGraphicModel();
        initParamValue();
    }

    public void setListener(ParamChangeListener listener) {
        this.listener = listener;
    }

    private void initParamValue(){
        setRows(this.dataModel.getRows());
        setVerticalBits(this.dataModel.getVerticalBits());
        setHorizonBits(this.dataModel.getHorizontalBits());
        setCellWidth(this.dataModel.getCellWidth());
        setLineSegmentWidth(this.dataModel.getLineSegmentWidth());
        setLineSegmentColor(this.dataModel.getLineSegmentColor());
        setLineBackground(this.dataModel.getLineBackground());
        setBackground(this.dataModel.getBackgroundColor());
        enableBorder(this.dataModel.isHasBorder());
        setBorderWidth(this.dataModel.getBorderWidth());
        setBorderColor(this.dataModel.getBorderColor());

        // 监听rows变更
        paramViews.get(0).setListener( text -> {
            System.out.println("rows changed: " + text);
            if (text.isBlank() || !text.matches("\\d+")) {
                // TODO need to tip
                return;
            }
            int rows = Integer.parseInt(text);
            if (rows < 2 || rows > 100) {
                // TODO need to tip
                return;
            }
            this.dataModel.setRows(rows);
            if (null != listener) {
                listener.handleModel(this.dataModel);
            }
        });

        paramViews.get(1).setListener( text -> {
            System.out.println("vertical bits changed: " + text);
            if (!text.isBlank() && !text.matches("[0-1]+")) {
                // TODO need to tip
                return;
            }
            this.dataModel.setVerticalBits(text);
            if (null != listener) {
                listener.handleModel(this.dataModel);
            }
        });

        paramViews.get(2).setListener( text -> {
            System.out.println("horizontal bits changed: " + text);
            if (! text.isBlank() && !text.matches("[0-1]+")) {
                // TODO need to tip
                return;
            }
            this.dataModel.setHorizontalBits(text);
            if (null != listener) {
                listener.handleModel(this.dataModel);
            }
        });
    }

    private  <T extends Control> ParamView<T> initParam(Class<T> c, String labelName){
        try {
            T t = c.getDeclaredConstructor().newInstance();
            return new ParamView<>(labelName, t);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获取宫格大小
    public int getRows(){
        ParamView paramView = this.paramViews.get(0);
        Control input = paramView.getInput();
        if (input instanceof TextField) {
            TextField field = (TextField)input;
            return Integer.parseInt(field.getText());
        }
        return 0;
    }

    public void setRows(int rows){
        ParamView paramView = this.paramViews.get(0);
        Control input = paramView.getInput();
        if (input instanceof TextField) {
            TextField field = (TextField)input;
            field.setText(String.valueOf(rows));
        }
    }

    // 获取竖线段标识
    public String getVerticalBits(){
        ParamView paramView = this.paramViews.get(1);
        Control input = paramView.getInput();
        if (input instanceof TextField) {
            TextField field = (TextField)input;
            return field.getText();
        }
        return "";
    }

    public void setVerticalBits(String vBits){
        ParamView paramView = this.paramViews.get(1);
        Control input = paramView.getInput();
        if (input instanceof TextField) {
            TextField field = (TextField)input;
            field.setText(vBits);
        }
    }

    // 获取横线短标识
    public String getHorizonBits(){
        ParamView paramView = this.paramViews.get(2);
        Control input = paramView.getInput();
        if (input instanceof TextField) {
            TextField field = (TextField)input;
            return field.getText();
        }
        return "";
    }

    public void setHorizonBits(String hBits) {
        ParamView paramView = this.paramViews.get(2);
        Control input = paramView.getInput();
        if (input instanceof TextField) {
            TextField field = (TextField)input;
            field.setText(hBits);
        }
    }

    // 小方块宽度
    public double getCellWidth(){
        ParamView paramView = this.paramViews.get(3);
        Control input = paramView.getInput();
        if (input instanceof TextField) {
            TextField field = (TextField)input;
            return Double.parseDouble(field.getText());
        }
        return 0;
    }

    public void setCellWidth(double cellWidth){
        ParamView paramView = this.paramViews.get(3);
        Control input = paramView.getInput();
        if (input instanceof TextField) {
            TextField field = (TextField)input;
            field.setText(String.valueOf(cellWidth));
        }
    }

    // 线段宽度
    public double getLineSegmentWidth(){
        ParamView paramView = this.paramViews.get(4);
        Control input = paramView.getInput();
        if (input instanceof TextField) {
            TextField field = (TextField)input;
            return Double.parseDouble(field.getText());
        }
        return 0;
    }

    public void setLineSegmentWidth(double lineSegmentWidth){
        ParamView paramView = this.paramViews.get(4);
        Control input = paramView.getInput();
        if (input instanceof TextField) {
            TextField field = (TextField)input;
            field.setText(String.valueOf(lineSegmentWidth));
        }
    }

    // 线段颜色
    public Color getLineSegmentColor(){
        ParamView paramView = this.paramViews.get(5);
        Control input = paramView.getInput();
        if (input instanceof TextField) {
            TextField field = (TextField)input;
            return parseFromText(field.getText());
        }
        return null;
    }

    public void setLineSegmentColor(Color lineSegmentColor){
        ParamView paramView = this.paramViews.get(5);
        Control input = paramView.getInput();
        if (input instanceof TextField) {
            TextField field = (TextField)input;
            field.setText(getTextFromColor(lineSegmentColor));
        }
    }

    private Color parseFromText(String text){
        text = text.trim();
        if (!text.matches("([0-9]{1,3}\\s){2}[0-9]{1,3}(\\s0?\\.[0-9]+)?")) {
            return null;
        }

        String[] rgbc = text.split("\\s");
        int r = Integer.parseInt(rgbc[0])%255;
        int g = Integer.parseInt(rgbc[1])%255;
        int b = Integer.parseInt(rgbc[2])%255;
        if (rgbc.length == 4) {
            double opacity = Double.parseDouble(rgbc[3]);
            return Color.rgb(r, g, b, opacity);
        } else {
            return Color.rgb(r, g, b);
        }
    }

    private String getTextFromColor(Color color) {
        return (int)(color.getRed()*255) + " " + (int)(color.getGreen()*255) + " " + (int)(color.getBlue()*255) + " " + color.getOpacity();
    }

    // 线段背景色
    public Color getLineBackground(){
        ParamView paramView = this.paramViews.get(6);
        Control input = paramView.getInput();
        if (input instanceof TextField) {
            TextField field = (TextField)input;
            return parseFromText(field.getText());
        }
        return null;
    }

    public void setLineBackground(Color lineBackground){
        ParamView paramView = this.paramViews.get(6);
        Control input = paramView.getInput();
        if (input instanceof TextField) {
            TextField field = (TextField)input;
            field.setText(getTextFromColor(lineBackground));
        }
    }

    // 全局背景色
    public Color getBackground(){
        ParamView paramView = this.paramViews.get(7);
        Control input = paramView.getInput();
        if (input instanceof TextField) {
            TextField field = (TextField)input;
            return parseFromText(field.getText());
        }
        return null;
    }

    public void setBackground(Color background){
        ParamView paramView = this.paramViews.get(7);
        Control input = paramView.getInput();
        if (input instanceof TextField) {
            TextField field = (TextField)input;
            field.setText(getTextFromColor(background));
        }
    }

    // 是否有边框
    public boolean hasBorder(){
        ParamView paramView = this.paramViews.get(8);
        Control input = paramView.getInput();
        if (input instanceof TextField) {
            TextField field = (TextField)input;
            return Boolean.parseBoolean(field.getText());
        }
        return false;
    }

    public void enableBorder(boolean hasBorder){
        ParamView paramView = this.paramViews.get(8);
        Control input = paramView.getInput();
        if (input instanceof TextField) {
            TextField field = (TextField)input;
            field.setText(String.valueOf(hasBorder));
        }
    }

    // 边框宽度
    public double getBorderWidth(){
        ParamView paramView = this.paramViews.get(9);
        Control input = paramView.getInput();
        if (input instanceof TextField) {
            TextField field = (TextField)input;
            return Double.parseDouble(field.getText());
        }
        return 0;
    }

    public void setBorderWidth(double borderWidth){
        ParamView paramView = this.paramViews.get(9);
        Control input = paramView.getInput();
        if (input instanceof TextField) {
            TextField field = (TextField)input;
            field.setText(String.valueOf(borderWidth));
        }
    }

    // 边框颜色
    public Color getBorderColor(){
        ParamView paramView = this.paramViews.get(10);
        Control input = paramView.getInput();
        if (input instanceof TextField) {
            TextField field = (TextField)input;
            return parseFromText(field.getText());
        }
        return null;
    }

    public void setBorderColor(Color borderColor){
        ParamView paramView = this.paramViews.get(10);
        Control input = paramView.getInput();
        if (input instanceof TextField) {
            TextField field = (TextField)input;
            field.setText(getTextFromColor(borderColor));
        }
    }

    public List<ParamView> getParamViews() {
        return paramViews;
    }

    public interface ParamChangeListener{
        void handleModel(StructGraphicModel model);
    }

    public void invokeListener(){
        if (null != this.listener) {
            this.listener.handleModel(this.dataModel);
        }
    }

}
