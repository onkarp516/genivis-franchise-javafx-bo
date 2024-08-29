package com.opethic.genivis.controller.commons;

import com.opethic.genivis.controller.master.CompanyAdminController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.util.jar.JarOutputStream;

public class SwitchButton extends Label {
    private SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(true);
    private VBox vBox;

    Button switchBtn = new Button();
    public SwitchButton() {
        switchBtn.setId("btnSwitch");

//        switchBtn.setPrefWidth(9);
//        switchBtn.setMinWidth(9);
//        switchBtn.setMaxWidth(9);

        switchBtn.setPrefWidth(10);
        switchBtn.setMinWidth(10);
        switchBtn.setMaxWidth(10);
        switchBtn.setStyle("-fx-background-color: white; -fx-background-radius: 0.5;");
        switchBtn.setPrefHeight(10);
        switchBtn.setMaxHeight(10);
        switchBtn.setMinHeight(10);

//        switchBtn.setPrefHeight(9);
//        switchBtn.setMaxHeight(9);
//        switchBtn.setMinHeight(9);


        switchBtn.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {

                if (vBox != null)
                    vBox.setStyle("-fx-background-color: white; -fx-background-radius: 4; -fx-border-radius: 4; -fx-border-color:#00A0F5; -fx-border-width: 2;");

                if(switchedOn.get()){
                    switchBtn.setStyle("-fx-background-color:  #0cbf56; -fx-background-radius: 0.5; -fx-border-width: 0;");
                    setStyle("-fx-background-color: #fff9c4;-fx-text-fill:black; -fx-border-color: #fff9c4; -fx-font-size: 7; -fx-background-radius: 3; -fx-border-radius: 3; -fx-padding: 3;");
                }
                else{
                    switchBtn.setStyle("-fx-background-color:  #ff5050; -fx-background-radius: 0.5; -fx-border-width: 0;");
                    setStyle("-fx-background-color: #fff9c4;-fx-text-fill:black; -fx-border-color: #fff9c4; -fx-font-size: 7; -fx-background-radius: 3; -fx-border-radius: 3; -fx-padding: 3;");
                }

            } else {

                if (vBox != null)
                    vBox.setStyle("-fx-background-color: #f8f4fc; -fx-background-radius: 4; -fx-border-radius: 4; -fx-border-color: #f8f4fc; -fx-border-width: 2;");

                if(switchedOn.get()){
                    switchBtn.setStyle("-fx-background-color: #0cbf56; -fx-background-radius: 0.5; -fx-border-width: 0;");
                    setStyle("-fx-background-color: white;-fx-text-fill:white; -fx-border-color: #449c69; -fx-font-size: 7; -fx-background-radius: 3; -fx-border-radius: 3; -fx-padding: 3;");
                }
                else{
                    switchBtn.setStyle("-fx-background-color: #ff5050; -fx-background-radius: 0.5; -fx-border-width: 0;");
                    setStyle("-fx-background-color: white;-fx-text-fill:black; -fx-border-color: #a0a6b0; -fx-font-size: 7; -fx-background-radius: 3; -fx-border-radius: 3; -fx-padding: 3;");
                }
            }
        });


        switchBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                switchedOn.set(!switchedOn.get());
                switchBtn.requestFocus();
                CompanyAdminController.isFirst=false;
            }
        });
//        switchBtn.setOnKeyPressed(e->{
//            if(e.getCode() == KeyCode.SPACE){
//                CompanyAdminController.isFirst=false;
//                switchedOn.set(!switchedOn.get());
//            }
//        });

//        switchBtn.setOnMouseClicked(event -> {
//            switchedOn.set(!switchedOn.get());
//        });

        setGraphic(switchBtn);


        switchedOn.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (t1) {
                    setText("     ");
                    if(switchBtn.isFocused()){
                        switchBtn.setStyle("-fx-background-color:  #0cbf56; -fx-background-radius: 0.5; -fx-border-width: 0;");
                        setStyle("-fx-background-color: #fff9c4;-fx-text-fill:black; -fx-border-color: #fff9c4; -fx-font-size: 7; -fx-background-radius: 3; -fx-border-radius: 3; -fx-padding: 3;");
                    }
                    else {
                        switchBtn.setStyle("-fx-background-color: #0cbf56; -fx-background-radius: 0.5; -fx-border-width: 0;");
                        setStyle("-fx-background-color: white;-fx-text-fill:white; -fx-border-color: #449c69; -fx-font-size: 7; -fx-background-radius: 3; -fx-border-radius: 3; -fx-padding: 3;");
                    }
                    setContentDisplay(ContentDisplay.RIGHT);
                } else {
                    setText("     ");
                    if(switchBtn.isFocused()){
                        switchBtn.setStyle("-fx-background-color:  #ff5050; -fx-background-radius: 0.5; -fx-border-width: 0;");
                        setStyle("-fx-background-color: #fff9c4;-fx-text-fill:black; -fx-border-color: #fff9c4; -fx-font-size: 7; -fx-background-radius: 3; -fx-border-radius: 3; -fx-padding: 3;");
                    }
                    else {
                        switchBtn.setStyle("-fx-background-color: #ff5050; -fx-background-radius: 0.5; -fx-border-width: 0;");
                        setStyle("-fx-background-color: white;-fx-text-fill:black; -fx-border-color: #a0a6b0; -fx-font-size: 7; -fx-background-radius: 3; -fx-border-radius: 3; -fx-padding: 3;");
                    }
                    setContentDisplay(ContentDisplay.LEFT);
                }
            }
        });


        switchedOn.set(false);

        setOnMouseEntered(e -> {
            setCursor(Cursor.HAND);
        });
        setOnMouseExited(e -> setCursor(Cursor.DEFAULT));

        setOnMouseClicked(e ->{ switchedOn.set(!switchedOn.get());
            requestButtonFocus();
        }
        );
    }

    public SimpleBooleanProperty switchOnProperty() {
        return switchedOn;
    }

    public void setParentBox(VBox vBox) {
        this.vBox = vBox;
    }


    public void setSwitchedOn(Boolean flag){
        switchedOn.set(flag);
    }


    public void toggle() {
        switchedOn.set(!switchedOn.get());
    }

    public void requestButtonFocus() {
        getGraphic().requestFocus();
    }

    public Button getSwitchBtn() {
        return switchBtn;
    }


}
