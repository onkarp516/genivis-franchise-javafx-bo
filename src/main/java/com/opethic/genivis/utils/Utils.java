package com.opethic.genivis.utils;

import com.opethic.genivis.GenivisApplication;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Utils {
    public static String input="";
    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
        alert.showAndWait();
    }

    public static void showAlertAndClose(String message) {
        Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
        Duration duration = Duration.seconds(0.9);
        alert.show();
        Timeline timeline = new Timeline(new KeyFrame(duration, event -> alert.close()));
        timeline.play();
    }

    public static boolean showConfirmationAlert(String header) {
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Do you want to "+header+"?");
        alert.setContentText("Please confirm your action.");

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(GenivisApplication.class.getResourceAsStream("ui/assets/genivis_favicon.png")));


        ImageView imageView = new ImageView((new Image(GenivisApplication.class.getResourceAsStream("ui/assets/alert.png"))));
        imageView.setFitHeight(48);
        imageView.setFitWidth(48);
        alert.setGraphic(imageView);

        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

        Button yesButton = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
        yesButton.setDefaultButton(false);

        Button noButton = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
        noButton.setDefaultButton(true);

        //  alert.getDialogPane().setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

        final ButtonType result = alert.showAndWait().orElse(ButtonType.NO);

        return result == ButtonType.YES;
    }

    public static void showSuccessAlert(String message) {
        Stage primaryStage =new Stage();
        ImageView imageView = new ImageView(new Image(GenivisApplication.class.getResourceAsStream("ui/assets/checked.png")));
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);

        Label successLabel = new Label(message);
        successLabel.setStyle("-fx-font-size: 16; -fx-text-fill: black;");
        successLabel.setPadding(new Insets(0, 22, 0, 0));

        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.getChildren().addAll(successLabel, imageView);

        Scene scene = new Scene(root, 350, 110);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Success");
        primaryStage.setResizable(false);

        // Close the stage after 1.5 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2.0),actionEvent -> primaryStage.close()));
        timeline.play();
        primaryStage.show();
    }

    public static void showSuccessAlertNew(String message) {
        // Ensure this runs on the JavaFX Application Thread
        Platform.runLater(() -> {
            Stage primaryStage = new Stage();

            // Load and set the image
            ImageView imageView = new ImageView(new Image(GenivisApplication.class.getResourceAsStream("ui/assets/checked.png")));
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);

            // Create and style the success label
            Label successLabel = new Label(message);
            successLabel.setStyle("-fx-font-size: 16; -fx-text-fill: black;");
            successLabel.setPadding(new Insets(0, 0, 0, 22)); // Adjusting padding for better alignment

            // Create the root HBox and add the label and image view
            HBox root = new HBox();
            root.setAlignment(Pos.CENTER);
            root.setSpacing(10);
            root.getChildren().addAll(successLabel, imageView);

            // Create the scene and set it on the stage
            Scene scene = new Scene(root, 350, 110);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Success");
            primaryStage.setResizable(false);

            // Close the stage after 1.5 seconds
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2.0), actionEvent -> {
                primaryStage.close();
            }));
            timeline.setCycleCount(1);
            timeline.play();

            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    primaryStage.close();
                }
            });
            // Show the stage
            primaryStage.show();
        });
    }
    public static void showDeleteSuccessAlertNew(String message) {
        // Ensure this runs on the JavaFX Application Thread
        Platform.runLater(() -> {
            Stage primaryStage = new Stage();

            // Load and set the image
            ImageView imageView = new ImageView(new Image(GenivisApplication.class.getResourceAsStream("ui/assets/checked.png")));
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);

            // Create and style the success label
            Label successLabel = new Label(message);
            successLabel.setStyle("-fx-font-size: 16; -fx-text-fill: black;");
            successLabel.setPadding(new Insets(0, 0, 0, 22)); // Adjusting padding for better alignment

            // Create the root HBox and add the label and image view
            HBox root = new HBox();
            root.setAlignment(Pos.CENTER);
            root.setSpacing(10);
            root.getChildren().addAll(successLabel, imageView);

            // Create the scene and set it on the stage
            Scene scene = new Scene(root, 350, 110);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Success");
            primaryStage.setResizable(false);

            // Close the stage after 1.5 seconds
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2.0), actionEvent -> {
                primaryStage.close();
            }));
            timeline.setCycleCount(1);
            timeline.play();

            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    primaryStage.close();
                }
            });
            // Show the stage
            primaryStage.show();
        });
    }

    public static void showErrorAlert(String message) {
        Stage primaryStage =new Stage();
        ImageView imageView = new ImageView(new Image(GenivisApplication.class.getResourceAsStream("ui/assets/delete.png")));
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);

        Label error = new Label(message);
        error.setStyle("-fx-font-size: 16; -fx-text-fill: black;");
        error.setPadding(new Insets(0, 22, 0, 0));

        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.getChildren().addAll(imageView,error);

        Scene scene = new Scene(root, 350, 110);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Success");
        primaryStage.setResizable(false);


        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), event -> primaryStage.close()));
        timeline.play();

        primaryStage.show();
    }
    public static void showErrorAlertNew(String message) {
        Platform.runLater(()->{
            Stage primaryStage =new Stage();
            ImageView imageView = new ImageView(new Image(GenivisApplication.class.getResourceAsStream("ui/assets/delete.png")));
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);

            Label error = new Label(message);
            error.setStyle("-fx-font-size: 16; -fx-text-fill: black;");
            error.setPadding(new Insets(0, 22, 0, 0));

            HBox root = new HBox();
            root.setAlignment(Pos.CENTER);
            root.setSpacing(10);
            root.getChildren().addAll(imageView,error);

            Scene scene = new Scene(root, 350, 110);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Success");
            primaryStage.setResizable(false);

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2.0), actionEvent -> {
                primaryStage.close();
            }));
            timeline.setCycleCount(1);
            timeline.play();
            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    primaryStage.close();
                }
            });

            primaryStage.show();
        });
    }
    //function for delete fail
    public static void showDeleteErrorAlertNew(String message) {
        Platform.runLater(()->{
            Stage primaryStage =new Stage();
            ImageView imageView = new ImageView(new Image(GenivisApplication.class.getResourceAsStream("ui/assets/delete.png")));
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);

            Label error = new Label(message);
            error.setStyle("-fx-font-size: 16; -fx-text-fill: black;");
            error.setPadding(new Insets(0, 22, 0, 0));

            HBox root = new HBox();
            root.setAlignment(Pos.CENTER);
            root.setSpacing(10);
            root.getChildren().addAll(imageView,error);

            Scene scene = new Scene(root, 350, 110);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Error");    //this error is for, if a unit OR category OR Brand is used in tranx and we try to delete this then This title is required
            primaryStage.setResizable(false);

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2.0), actionEvent -> {
                primaryStage.close();
            }));
            timeline.setCycleCount(1);
            timeline.play();
            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    primaryStage.close();
                }
            });

            primaryStage.show();
        });
    }

//    public static void popUpForCatalog(Stage stage, String title, Consumer<String> callback) {
//        Stage primaryStage = new Stage();
//
//        primaryStage.initOwner(stage); // Set the owner stage
//        primaryStage.initStyle(StageStyle.TRANSPARENT);
//        primaryStage.initModality(Modality.APPLICATION_MODAL);
//
//        //Main Layout................................................................................................................................
//        BorderPane borderPane = new BorderPane();
//        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
//        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");
//
//        //BorderPan under Top Layout....................................................................................................................
//        HBox hbox_top = new HBox();
//        hbox_top.setMinWidth(498);
//        hbox_top.setMaxWidth(498);
//        hbox_top.setPrefWidth(498);
//        hbox_top.setMaxHeight(50);
//        hbox_top.setMinHeight(50);
//        hbox_top.setPrefHeight(50);
//
//        Label popup_title = new Label(title);
//        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
//        popup_title.setPadding(new Insets(0, 10, 0, 0));
//
//        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
//        ImageView imageView = new ImageView(image);
//        imageView.setStyle("-fx-cursor: hand;");
//        imageView.setOnMouseClicked(event -> primaryStage.close());
//        HBox.setMargin(imageView, new Insets(0, 10, 0, 0));
//        imageView.setFitWidth(30);
//        imageView.setFitHeight(30);
//
//        hbox_top.setAlignment(Pos.CENTER_LEFT);
//        hbox_top.getChildren().add(popup_title);
//        Region spacer = new Region();
//        HBox.setHgrow(spacer, Priority.ALWAYS);
//        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
//        hbox_top.getChildren().add(spacer);
//        hbox_top.getChildren().add(imageView);
//        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #d9f0fb; -fx-border-color: #ffffff; -fx-border-width: 0 0 2 0;");
//
//
//
//        //BorderPane Under Center Layout.....................................................................................................
//        HBox hbox_center = new HBox();
//        hbox_center.setMinWidth(498);
//        hbox_center.setMaxWidth(498);
//        hbox_center.setPrefWidth(498);
//        hbox_center.setAlignment(Pos.CENTER);
//        hbox_center.setStyle("-fx-background-color: #e6f2f8;");
////
////        Label popup_lable = new Label(lable);
////        popup_lable.setStyle("-fx-font-size: 16; -fx-text-fill: black; -fx-font-weight: bold;");
////        popup_lable.setPadding(new Insets(0, 10, 0, 0));
//
//        TextField textField = new TextField("");
//        hbox_center.getChildren().addAll( textField);
//
//
//        //BorderPane Under Bottom Layout..............................................................................................................
//        HBox hbox_bottom = new HBox();
//        hbox_bottom.setMinWidth(498);
//        hbox_bottom.setMaxWidth(498);
//        hbox_bottom.setPrefWidth(498);
//        hbox_bottom.setMaxHeight(55);
//        hbox_bottom.setMinHeight(55);
//        hbox_bottom.setPrefHeight(55);
//        hbox_bottom.setSpacing(10);
//        hbox_bottom.setStyle("-fx-background-radius: 0 0 5 5; -fx-background-color: white;");
//        hbox_bottom.setAlignment(Pos.CENTER_RIGHT);
//
////        Button cancelButton = new Button("Cancel");
//        Button submitButton = new Button("Submit");
//        hbox_bottom.setMargin(submitButton, new Insets(0, 10, 0, 0));
//        submitButton.setId("submit-btn");
////        cancelButton.setId("cancel-btn");
////        cancelButton.setOnAction(event -> primaryStage.close());
//
//        hbox_bottom.getChildren().addAll( submitButton);
//
//        borderPane.setTop(hbox_top);
//        borderPane.setCenter(hbox_center);
//        borderPane.setBottom(hbox_bottom);
//
//        Node[] nodes =new Node[]{textField,submitButton};
//        CommonValidationsUtils.setupFocusNavigation(nodes);
//
//        Scene scene = new Scene(borderPane, 500, 170);
//
//        primaryStage.setScene(scene);
//        primaryStage.setTitle(title);
//        primaryStage.setResizable(false);
//
//        scene.setFill(Color.TRANSPARENT);
//
//        primaryStage.centerOnScreen();
//
//        primaryStage.show();
//        submitButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                callback.accept(textField.getText());
//                primaryStage.close();
//                showSuccessAlert("Brand Added Successfully");
//            }
//        });
//
//
//
//    }


}
