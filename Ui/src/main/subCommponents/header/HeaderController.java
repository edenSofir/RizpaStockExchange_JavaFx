package main.subCommponents.header;

import Exceptions.CompanyNameIsAlreadyExists;
import Exceptions.SymbolIsAlreadyExists;
import Exceptions.SymbolNotExists;
import Exceptions.UserNameIsAlreadyExists;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;
import main.PrimMainController;
import main.subCommponents.exapetion.ExceptionController;
import mainLogic.MainLogic;
import menu.MenuEngine;
import menu.MenuEngineImpl;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class HeaderController {

    private Stage primaryStage;
    private PrimMainController mainController;
    private MenuEngine menuEngine;
    private MainLogic mainLogic;
    private ExceptionController exceptionController;

    private SimpleStringProperty selectedFilePropertyML;
    private SimpleStringProperty selectedFileHeader ;
    private SimpleBooleanProperty isFileSelected;
    private SimpleBooleanProperty isLoaded;


    @FXML
    private AnchorPane fullHeader;

    @FXML
    private Button openFileButton;

    @FXML
    private Button pressToLoadButton;

    @FXML
    private Label selectedFileName;

    @FXML
    private ProgressBar taskProgressBar;

    @FXML
    private Label percentLabel;

    @FXML
    private Label messageLabelTask;

    private String temp ;


    public ProgressBar getTaskProgressBar() {
        return taskProgressBar;
    }

    public Label getPercentLabel() {
        return percentLabel;
    }

    public Label getMessageLabelTask() {
        return messageLabelTask;
    }

    @FXML
    private void initialize() {
        fullHeader.getStylesheets().add("/main/subCommponents/header/headerStyleSheets/mainHeaderController.css");
        selectedFileName.textProperty().bind(selectedFilePropertyML);
        //selectedFileName.textProperty().bind(selectedFileHeader);
        pressToLoadButton.disableProperty().bind(isFileSelected.not());
    }


    public HeaderController() {

        menuEngine = new MenuEngineImpl();
        selectedFilePropertyML = new SimpleStringProperty();
        selectedFileHeader = new SimpleStringProperty("");
        isFileSelected = new SimpleBooleanProperty(false);
        isLoaded = new SimpleBooleanProperty(true);
    }


    public void setMainLogic(MainLogic mainLogic) {
        this.mainLogic = mainLogic;
        this.mainLogic.setMenuEngine(menuEngine);
        this.mainLogic.filePathProperty().bind(selectedFilePropertyML);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setExceptionController(ExceptionController exceptionController) {
        this.exceptionController = exceptionController;
    }

    public void setMainController(PrimMainController mainController) {
        this.mainController = mainController;
    }

    public SimpleBooleanProperty isLoadedProperty() {
        return isLoaded;
    }

    @FXML
    void LoadListener() {
        mainLogic.LoadData();
        if(mainLogic.loadedSuccessfulProperty().getValue())
        {
            this.temp = selectedFilePropertyML.getValue();
        }
        else
        {
            if(temp != null)
                selectedFilePropertyML.setValue(this.temp);
            else
                selectedFilePropertyML.setValue("");
        }
        mainController.doneHeaderProperty().setValue(true);
    }


    @FXML
    void openFileListner() throws FileNotFoundException, CompanyNameIsAlreadyExists, SymbolIsAlreadyExists, JAXBException, SymbolNotExists, UserNameIsAlreadyExists {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("select XML file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML document", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }
        selectedFilePropertyML.set(selectedFile.getAbsolutePath());
        isFileSelected.set(true);

    }

    @FXML
    void DarkThemeChosen(ActionEvent event) {
        mainController.setDarkSkin();
    }

    @FXML
    void defultThemeChosen(ActionEvent event) {
        mainController.setDefultSkin();
    }

    @FXML
    void lightThemeChosen(ActionEvent event) {
        mainController.seLightSkin();
    }

    public void changeDarkSkin()
    {
        fullHeader.getStylesheets().clear();
        fullHeader.getStylesheets().add("main/subCommponents/header/headerStyleSheets/headerDarkTheme.css");
    }

    public void changeDefaultSkin()
    {
        fullHeader.getStylesheets().clear();
        fullHeader.getStylesheets().add("main/subCommponents/header/headerStyleSheets/mainHeaderController.css");
    }

    public void changeLightSkin()
    {
        fullHeader.getStylesheets().clear();
        fullHeader.getStylesheets().add("main/subCommponents/header/headerStyleSheets/headerLightTheme.css");
    }



}

