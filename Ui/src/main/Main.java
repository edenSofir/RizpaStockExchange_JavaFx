package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;


public class Main extends Application {

    public static void main(String[] args) {
         launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("/main/primMain.fxml"); //move to constant
        loader.setLocation(mainFXML);
        ScrollPane root = loader.load();

        PrimMainController mainController = loader.getController();
        mainController.setPrimaryStage(primaryStage);
        mainController.createExceptionWindow();
        primaryStage.setTitle("RitzpaStockExchanger ");
        Scene scene = new Scene(root, 850, 600);
        primaryStage.setScene(scene);
        scene.getStylesheets().add("/main/primeMainStylesheets/primeMain.css");
        primaryStage.show();
    }
}
