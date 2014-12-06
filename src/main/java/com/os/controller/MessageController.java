package com.os.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jeremie on 2014/12/6.
 */
public class MessageController implements Initializable {

    @FXML
    private Button apply;
    @FXML
    private Label text;
    private static MessageController messageController;

    public static MessageController getInstance(){
        return messageController;
    }

    public void showTips(String tips){

        MainController mainController = MainController.getInstance();
        mainController.changeMsg(true);
        text.setText(tips);
    }

    public void setApplyEvent(EventHandler<MouseEvent> event){
        apply.setOnMouseClicked(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageController = this;
    }
}
