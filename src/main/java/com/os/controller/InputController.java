package com.os.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jeremie on 2014/12/6.
 */
public class InputController implements Initializable {
    private static InputController instance;

    public static InputController getInputController() {
        return instance;
    }
    @FXML
    private TextField path;
    @FXML
    private Label pathLabel;
    @FXML
    private Button apply,cancel;

    public void setPathLabel(String pathLabelStr){
        pathLabel.setText(pathLabelStr);
    }

    public String getPathStr(){
        return path.getText();
    }

    public void setApplyEvent(EventHandler<MouseEvent> event){
        apply.setOnMouseClicked(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        cancel.setOnMouseClicked(event -> {
            MainController mainController = MainController.getInstance();
            mainController.changeMain(false);
            mainController.changeInput(false);

        });
    }
}
