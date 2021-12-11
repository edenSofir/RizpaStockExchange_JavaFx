package main.subCommponents.exapetion;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ExceptionController {

    @FXML
    private Label messageHolder; //in this current mode the label is null

    private SimpleStringProperty messageException ;

    public ExceptionController() {
        this.messageException = new SimpleStringProperty();
    }

    @FXML
    private void initialize() {
    messageHolder.textProperty().bind(messageException);
    }

    public void setMessageException(String messageException) {
        this.messageException.set(messageException);
      //  messageHolder.setText(this.messageException.getValue());
    }

    public void createNewWindow() throws IOException {

        Stage newWindow = new Stage();
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.setTitle("Error");

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL popUpFxml = getClass().getResource("/main/subCommponents/exapetion/exaptionWindow.fxml");
        fxmlLoader.setLocation(popUpFxml);
        AnchorPane root = fxmlLoader.load(popUpFxml.openStream());

        ExceptionController exceptionController = fxmlLoader.getController();
        Scene scene = new Scene(root);
        newWindow.setScene(scene);
        newWindow.show();


    }


}
