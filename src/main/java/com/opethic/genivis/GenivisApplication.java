package com.opethic.genivis;

import com.opethic.genivis.utils.FxmFileConstants;
import com.opethic.genivis.utils.Globals;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class GenivisApplication extends Application {


    @Override
    public void start(Stage stage) {

        Parent fxmlLoader = null;
        try {
            fxmlLoader = FXMLLoader.load(GenivisApplication.class.getResource(FxmFileConstants.LOGIN_FXML_FILE));
//            fxmlLoader = FXMLLoader.load(GenivisApplication.class.getResource("ui/master/product_create.fxml"));
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
        stage.setMaximized(true);
        stage.setScene(new Scene(fxmlLoader));
        stage.setTitle(Globals.APP_TITLE);
        stage.getIcons().add(new Image(GenivisApplication.class.getResourceAsStream("ui/assets/genivis_favicon.png")));
//        stage.setTitle("Ethiq | "+Globals.APP_TITLE);
        stage.getIcons().add(new Image(GenivisApplication.class.getResourceAsStream("ui/assets/mainlogo.png")));
        stage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                if(!Globals.isLoggedIn)
                    System.exit(0);
            }
        });
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
//        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

    }

    public static void main(String[] args) {
        try {
            launch();
        } catch (Exception e) {
            // TODO : SHOW ALERT BOX FOR ERROR TRACING
            //TODO : PRINT ERROR IN LOGGER FILE
            e.printStackTrace();
        }
    }
}