package org;

import javafx.application.Application;
import javafx.stage.Stage;
import org.view.LevelSelect.SelectMap;

import java.io.IOException;

public class test2 extends Application {
    public static void main(String[] args) {

    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        SelectMap map = new SelectMap(primaryStage);
    }
}
