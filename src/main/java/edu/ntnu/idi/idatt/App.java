package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.ui.pages.MainMenu;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        MainMenu page = new MainMenu();
        page.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}