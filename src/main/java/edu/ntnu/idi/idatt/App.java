package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.ui.pages.MainMenu;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    MainMenu page = new MainMenu();
    primaryStage.setWidth(1200);
    primaryStage.setHeight(920);
    primaryStage.setResizable(false);
    page.start(primaryStage);
  }

  public static void main(String[] args) {
    launch(args);
  }
}