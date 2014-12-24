package com.os.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
    private TextField path,readerLength;
    @FXML
    private Label pathLabel,text,readLengthLabel;
    @FXML
    private Button apply,cancel;
    @FXML
    private ComboBox<String> selected;

    public void setPathLabel(String pathLabelStr){
        pathLabel.setText(pathLabelStr);
    }

    public void readFile(){
        readLengthLabel.setVisible(true);
        readerLength.setVisible(true);
    }

    public void closeReadFile(){
        readLengthLabel.setVisible(false);
        readerLength.setVisible(false);
    }

    public void showSelected(){
        selected.setVisible(true);
    }

    public void closeSelected(){
        selected.setVisible(false);
    }

    public int getSelectNum(){
        switch (selected.getValue()){
            case "1普通文件":
                return 1;
            case "2只读文件":
                return 2;
            default:
                return 1;
        }
    }

    public int getReadLength(){
        int i =0;
        try {
            i = Integer.parseInt(readerLength.getText());
        }catch (NumberFormatException e){
            System.out.println("不是数字");
        }
        return i;
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
            closeSelected();
            closeReadFile();
        });
        selected.getItems().add("1普通文件");
        selected.getItems().add("2只读文件");
        selected.setValue("1普通文件");
    }
}
