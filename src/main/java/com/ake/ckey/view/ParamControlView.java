package com.ake.ckey.view;

import com.ake.ckey.model.StructGraphicModel;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

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
     * 参数4： 暂无
     */
    public List<ParamView> paramViews;
    private StructGraphicModel dataModel;
    private ParamChangeListener listener;

    public ParamControlView(){
        paramViews = new ArrayList<>();
        paramViews.add(initParam(TextField.class, "宫格数") );
        paramViews.add(initParam(TextField.class, "竖线段标识（二进制文本 1101）") );
        paramViews.add(initParam(TextField.class, "横线段标识（二进制文本 1101）") );

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
            if (text.isBlank()) return;
            if (!text.matches("[0-1]+")) {
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
            if (text.isBlank()) return;
            if (!text.matches("[0-1]+")) {
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
