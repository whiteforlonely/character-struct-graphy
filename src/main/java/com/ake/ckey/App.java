package com.ake.ckey;

import com.ake.ckey.view.MainController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        MainController mainController = new MainController();
        Scene scene = mainController.initWindow();
        stage.setScene(scene);
        stage.show();

        mainController.refreshView();
    }

    public static void main(String[] args) {
        launch();
    }
}
