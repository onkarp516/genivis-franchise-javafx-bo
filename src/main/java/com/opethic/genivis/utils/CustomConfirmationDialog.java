package com.opethic.genivis.utils;


import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.commons.OverlaysEffect;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.function.Consumer;

public class CustomConfirmationDialog extends Dialog<Boolean> {

    public static void ConfirmationDialog(Stage stage, String message, Consumer<Boolean> callback) {
        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();
        primaryStage.initOwner(stage); // Set the owner stage
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");

        //BorderPan under Top Layout....................................................................................................................
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(398);
        hbox_top.setMaxWidth(398);
        hbox_top.setPrefWidth(398);
        hbox_top.setMaxHeight(50);
        hbox_top.setMinHeight(50);
        hbox_top.setPrefHeight(50);

        Label popup_title = new Label("Confirmation");
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        HBox.setMargin(imageView, new Insets(0, 10, 0, 0));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #d9f0fb; -fx-border-color: #ffffff; -fx-border-width: 0 0 2 0;");

        //BorderPane Under Center Layout.....................................................................................................
        HBox hbox_center = new HBox();
        hbox_center.setMinWidth(398);
        hbox_center.setMaxWidth(398);
        hbox_center.setPrefWidth(398);
        hbox_center.setAlignment(Pos.CENTER);
        Label popup_msg = new Label(message);
        popup_msg.setStyle("-fx-font-size: 16; -fx-text-fill: #404040;");
        hbox_center.getChildren().add(popup_msg);
        hbox_center.setStyle("-fx-background-color: #e6f2f8;");



        //BorderPane Under Bottom Layout..............................................................................................................
        HBox hbox_bottom = new HBox();
        hbox_bottom.setMinWidth(398);
        hbox_bottom.setMaxWidth(398);
        hbox_bottom.setPrefWidth(398);
        hbox_bottom.setMaxHeight(55);
        hbox_bottom.setMinHeight(55);
        hbox_bottom.setPrefHeight(55);
        hbox_bottom.setSpacing(10);
        hbox_bottom.setStyle("-fx-background-radius: 0 0 5 5; -fx-background-color: white;");
        hbox_bottom.setAlignment(Pos.CENTER);

       Button no = new Button("No");
       no.setId("no-btn");

        Button yes = new Button("Yes");
        yes.setId("yes-btn");
        Platform.runLater(yes::requestFocus);

        hbox_bottom.getChildren().addAll(yes,no);

        borderPane.setTop(hbox_top);
        borderPane.setCenter(hbox_center);
        borderPane.setBottom(hbox_bottom);


        Scene scene = new Scene(borderPane, 400, 170);

        primaryStage.setScene(scene);
        primaryStage.setTitle("CNF");
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

        primaryStage.centerOnScreen();

        primaryStage.show();

        yes.setOnAction(e->{
            callback.accept(true);
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });

        no.setOnAction(e->{
            callback.accept(false);
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
        });

    }
}