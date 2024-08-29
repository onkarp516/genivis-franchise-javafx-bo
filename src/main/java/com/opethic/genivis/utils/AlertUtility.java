package com.opethic.genivis.utils;

import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.commons.OverlaysEffect;
import com.opethic.genivis.dto.reqres.product.Communicator;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author kirankumar.gadagi
 * @ImplNote : This is AlterUtility Class which provide the alert box with design & functionlity like Success,Warning,Confirmation etc.
 */
public class AlertUtility {

    public interface CustomCallback {
        void onYesNo(int number);
    }

    public static final String alertTypeSuccess = "SUCCESS";
    public static final String alertTypeWarning = "WARNING";
    public static final String alertTypeError = "ERROR";
    public static final String alertTypeConfirmation = "CONFIRMATION";


    private static final Logger loggerAlterUtility = LogManager.getLogger(AlertUtility.class);


    public static void AlertSuccess(Stage rootStage, String Title, String Message, CustomCallback callback) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(GenivisApplication.class.getResource("ui/alertcomponent/alertsuccess.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 216);
            scene.setFill(null);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.initModality(Modality.APPLICATION_MODAL);
            OverlaysEffect.setOverlaysEffect(rootStage);

            stage.setOnShown((e) -> {
                Button btnOK = (Button) scene.lookup("#btnYes");
                btnOK.requestFocus();
            });
            Button btnOK = (Button) scene.lookup("#btnYes");

            Label lblTitle = (Label) scene.lookup("#lblTitle");
            lblTitle.setText(Title);
            Label lblMessage = (Label) scene.lookup("#lblMessage");
            lblMessage.setText(Message);
            btnOK.setOnAction((e) -> {
//            System.out.println("btnOK clicked!");
                callback.onYesNo(1);
                stage.close();
                e.consume();
                OverlaysEffect.removeOverlaysEffect(rootStage);
            });
            ImageView imgClose = (ImageView) scene.lookup("#imgClose");
            imgClose.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Stage lstage = (Stage) imgClose.getScene().getWindow();
                    lstage.close();
                    event.consume();
                    OverlaysEffect.removeOverlaysEffect(rootStage);
                }
            });
            scene.setOnKeyReleased((e) -> {
                if (e.getCode() == KeyCode.ESCAPE) {
                    stage.close();
                } else if (e.getCode() == KeyCode.TAB) {
                    Platform.runLater(btnOK::requestFocus);
                }
                e.consume();
            });
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();


        } catch (Exception e) {
            loggerAlterUtility.error("Exception on AlertSuccess() -> " + Globals.getExceptionString(e));
        }
    }

    public static void AlertDialogForSuccess(String Title, String Message, Consumer<Boolean> callback) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(GenivisApplication.class.getResource("ui/alertcomponent/alertsuccess.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 216);
            scene.setFill(null);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.initModality(Modality.APPLICATION_MODAL);

            OverlaysEffect.setOverlaysEffect(Communicator.stage);

            stage.setOnShown((e) -> {
                Button btnOK = (Button) scene.lookup("#btnYes");
                btnOK.requestFocus();
            });
            Button btnOK = (Button) scene.lookup("#btnYes");

            Label lblTitle = (Label) scene.lookup("#lblTitle");
            lblTitle.setText(Title);
            Label lblMessage = (Label) scene.lookup("#lblMessage");
            lblMessage.setText(Message);
            btnOK.setOnAction((e) -> {
//            System.out.println("btnOK clicked!");
                callback.accept(true);
                stage.close();
                e.consume();
                OverlaysEffect.removeOverlaysEffect(Communicator.stage);
            });
            ImageView imgClose = (ImageView) scene.lookup("#imgClose");
            imgClose.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Stage lstage = (Stage) imgClose.getScene().getWindow();
                    lstage.close();
                    event.consume();
                    OverlaysEffect.removeOverlaysEffect(Communicator.stage);
                }
            });
            scene.setOnKeyReleased((e) -> {
                if (e.getCode() == KeyCode.ESCAPE) {
                    stage.close();
                } else if (e.getCode() == KeyCode.TAB) {
                    Platform.runLater(btnOK::requestFocus);
                }
                e.consume();
            });
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();


        } catch (Exception e) {
            loggerAlterUtility.error("Exception on AlertSuccess() -> " + Globals.getExceptionString(e));
        }
    }

    public static void AlertDialogForError(String Title, String Message, Consumer<Boolean> callback) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(GenivisApplication.class.getResource("ui/alertcomponent/alerterror.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 216);
            scene.setFill(null);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.initModality(Modality.APPLICATION_MODAL);
            OverlaysEffect.setOverlaysEffect(Communicator.stage);
            stage.setOnShown((e) -> {
                Button btnNo = (Button) scene.lookup("#btnNo");
                btnNo.requestFocus();
            });

            Label lblTitle = (Label) scene.lookup("#lblTitle");
            lblTitle.setText(Title);
            Label lblMessage = (Label) scene.lookup("#lblMessage");
            lblMessage.setText(Message);

            Button btnNo = (Button) scene.lookup("#btnNo");
            btnNo.setOnAction((e) -> {
                callback.accept(true);
                stage.close();
                e.consume();
                OverlaysEffect.removeOverlaysEffect(Communicator.stage);
            });
            ImageView imgClose = (ImageView) scene.lookup("#imgClose");
            imgClose.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Stage lstage = (Stage) imgClose.getScene().getWindow();
                    lstage.close();
                    event.consume();
                    OverlaysEffect.removeOverlaysEffect(Communicator.stage);
                }
            });
            scene.setOnKeyReleased((e) -> {
                if (e.getCode() == KeyCode.ESCAPE) {
                    stage.close();
                } else if (e.getCode() == KeyCode.TAB) {
                    Platform.runLater(btnNo::requestFocus);
                }
                e.consume();
            });
            stage.show();
        } catch (Exception e) {
            loggerAlterUtility.error("Exception on AlertError() -> " + Globals.getExceptionString(e));
        }
    }

    public static void AlertDialogForErrorWithStage(Stage childStage, String Title, String Message, Consumer<Boolean> callback) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(GenivisApplication.class.getResource("ui/alertcomponent/alerterror.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 216);
            scene.setFill(null);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.initModality(Modality.APPLICATION_MODAL);
            OverlaysEffect.setOverlaysEffect(childStage);
            stage.setOnShown((e) -> {
                Button btnNo = (Button) scene.lookup("#btnNo");
                btnNo.requestFocus();
            });

            Label lblTitle = (Label) scene.lookup("#lblTitle");
            lblTitle.setText(Title);
            Label lblMessage = (Label) scene.lookup("#lblMessage");
            lblMessage.setText(Message);

            Button btnNo = (Button) scene.lookup("#btnNo");
            btnNo.setOnAction((e) -> {
                callback.accept(true);
                stage.close();
                OverlaysEffect.removeOverlaysEffect(childStage);
            });
            ImageView imgClose = (ImageView) scene.lookup("#imgClose");
            imgClose.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Stage lstage = (Stage) imgClose.getScene().getWindow();
                    lstage.close();
                    event.consume();
                    OverlaysEffect.removeOverlaysEffect(childStage);
                }
            });
            scene.setOnKeyReleased((e) -> {
                if (e.getCode() == KeyCode.ESCAPE) {
                    stage.close();
                    OverlaysEffect.removeOverlaysEffect(childStage);
                } else if (e.getCode() == KeyCode.TAB) {
                    Platform.runLater(btnNo::requestFocus);
                }
                e.consume();
            });
            stage.show();
        } catch (Exception e) {
            loggerAlterUtility.error("Exception on AlertError() -> " + Globals.getExceptionString(e));
        }
    }

    public static void AlertDialogForSuccessWithStage(Stage childStage, String Title, String Message, Consumer<Boolean> callback) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(GenivisApplication.class.getResource("ui/alertcomponent/alertsuccess.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 216);
            scene.setFill(null);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.initModality(Modality.APPLICATION_MODAL);

            OverlaysEffect.setOverlaysEffect(childStage);

            stage.setOnShown((e) -> {
                Button btnOK = (Button) scene.lookup("#btnYes");
                btnOK.requestFocus();
            });
            Button btnOK = (Button) scene.lookup("#btnYes");

            Label lblTitle = (Label) scene.lookup("#lblTitle");
            lblTitle.setText(Title);
            Label lblMessage = (Label) scene.lookup("#lblMessage");
            lblMessage.setText(Message);
            btnOK.setOnAction((e) -> {
//            System.out.println("btnOK clicked!");
                callback.accept(true);
                stage.close();
                OverlaysEffect.removeOverlaysEffect(childStage);
            });
            ImageView imgClose = (ImageView) scene.lookup("#imgClose");
            imgClose.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Stage lstage = (Stage) imgClose.getScene().getWindow();
                    lstage.close();
                    event.consume();
                    OverlaysEffect.removeOverlaysEffect(childStage);
                }
            });
            scene.setOnKeyReleased((e) -> {
                if (e.getCode() == KeyCode.ESCAPE) {
                    stage.close();
                    OverlaysEffect.removeOverlaysEffect(childStage);
                } else if (e.getCode() == KeyCode.TAB) {
                    Platform.runLater(btnOK::requestFocus);
                }
                e.consume();
            });
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();


        } catch (Exception e) {
            loggerAlterUtility.error("Exception on AlertSuccess() -> " + Globals.getExceptionString(e));
        }
    }

    public static void AlertDialogForConfirmation(String Title, String Message, Consumer<Boolean> callback) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(GenivisApplication.class.getResource("ui/alertcomponent/alertconfirmation.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 216);
            scene.setFill(null);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);

            OverlaysEffect.setOverlaysEffect(Communicator.stage);
            stage.setOnShown((e) -> {
                Button btnOK = (Button) scene.lookup("#btnYes");
                btnOK.requestFocus();
            });

            Button btnOK = (Button) scene.lookup("#btnYes");

            Label lblTitle = (Label) scene.lookup("#lblTitle");
            lblTitle.setText(Title);
            Label lblMessage = (Label) scene.lookup("#lblMessage");
            lblMessage.setText(Message);
            btnOK.setOnAction((e) -> {
                callback.accept(true);
                OverlaysEffect.removeOverlaysEffect(Communicator.stage);
                stage.close();
                e.consume();
            });
            Button btnNo = (Button) scene.lookup("#btnNo");
            btnNo.setOnAction((e) -> {
                callback.accept(false);
                OverlaysEffect.removeOverlaysEffect(Communicator.stage);
                stage.close();
            });
            ImageView imgClose = (ImageView) scene.lookup("#imgClose");
            imgClose.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Stage lstage = (Stage) imgClose.getScene().getWindow();
                    OverlaysEffect.removeOverlaysEffect(Communicator.stage);
                    lstage.close();
                    event.consume();
                }
            });

            scene.setOnKeyReleased((e) -> {
                if (e.getCode() == KeyCode.ESCAPE) {
                    stage.close();
                } else if (e.getCode() == KeyCode.TAB) {
                    if (btnOK.isFocused()) {
                        Platform.runLater(btnNo::requestFocus);
                    } else {
                        Platform.runLater(btnOK::requestFocus);
                    }
                } else if (e.getCode() == KeyCode.LEFT) {
                    if (btnOK.isFocused()) {
                        Platform.runLater(btnNo::requestFocus);
                    } else {
                        Platform.runLater(btnOK::requestFocus);
                    }
                } else if (e.getCode() == KeyCode.RIGHT) {
                    if (btnOK.isFocused()) {
                        Platform.runLater(btnNo::requestFocus);
                    } else {
                        Platform.runLater(btnOK::requestFocus);
                    }
                }
                e.consume();
            });
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            loggerAlterUtility.error("Exception on AlertConfirmation() -> " + Globals.getExceptionString(e));
        }
    }


    public static void AlertSuccess(String Title, String Message, CustomCallback callback) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(GenivisApplication.class.getResource("ui/alertcomponent/alertsuccess.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 216);
            scene.setFill(null);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);

            stage.setOnShown((e) -> {
                Button btnOK = (Button) scene.lookup("#btnYes");
                btnOK.requestFocus();
            });
            Button btnOK = (Button) scene.lookup("#btnYes");

            Label lblTitle = (Label) scene.lookup("#lblTitle");
            lblTitle.setText(Title);
            Label lblMessage = (Label) scene.lookup("#lblMessage");
            lblMessage.setText(Message);
            btnOK.setOnAction((e) -> {
//            System.out.println("btnOK clicked!");
                callback.onYesNo(1);
                stage.close();
                e.consume();
            });
            ImageView imgClose = (ImageView) scene.lookup("#imgClose");
            imgClose.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Stage lstage = (Stage) imgClose.getScene().getWindow();
                    lstage.close();
                    event.consume();
                }
            });
            scene.setOnKeyReleased((e) -> {
                if (e.getCode() == KeyCode.ESCAPE) {
                    stage.close();
                } else if (e.getCode() == KeyCode.TAB) {
                    Platform.runLater(btnOK::requestFocus);
                }
                e.consume();
            });
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();


        } catch (Exception e) {
            loggerAlterUtility.error("Exception on AlertSuccess() -> " + Globals.getExceptionString(e));
        }
    }

    public static void AlertWarning(String Title, String Message, CustomCallback callback) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(GenivisApplication.class.getResource("ui/alertcomponent/alertwarning.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 216);
            scene.setFill(null);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.setOnShown((e) -> {
                Button btnOK = (Button) scene.lookup("#btnYes");
                btnOK.requestFocus();
            });
            scene.setOnKeyReleased((e) -> {

            });
            Button btnOK = (Button) scene.lookup("#btnYes");

            Label lblTitle = (Label) scene.lookup("#lblTitle");
            lblTitle.setText(Title);
            Label lblMessage = (Label) scene.lookup("#lblMessage");
            lblMessage.setText(Message);
            btnOK.setOnAction((e) -> {
                callback.onYesNo(1);
                stage.close();
                e.consume();
            });
            Button btnNo = (Button) scene.lookup("#btnNo");
            btnNo.setOnAction((e) -> {
                callback.onYesNo(0);
                stage.close();
                e.consume();
            });
            ImageView imgClose = (ImageView) scene.lookup("#imgClose");
            imgClose.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Stage lstage = (Stage) imgClose.getScene().getWindow();
                    lstage.close();
                    event.consume();
                }
            });
            scene.setOnKeyReleased((e) -> {
                if (e.getCode() == KeyCode.ESCAPE) {
                    stage.close();
                } else if (e.getCode() == KeyCode.TAB) {
                    if (btnOK.isFocused()) {
                        Platform.runLater(btnNo::requestFocus);
                    } else {
                        Platform.runLater(btnOK::requestFocus);
                    }
                } else if (e.getCode() == KeyCode.LEFT) {
                    if (btnOK.isFocused()) {
                        Platform.runLater(btnNo::requestFocus);
                    } else {
                        Platform.runLater(btnOK::requestFocus);
                    }
                } else if (e.getCode() == KeyCode.RIGHT) {
                    if (btnOK.isFocused()) {
                        Platform.runLater(btnNo::requestFocus);
                    } else {
                        Platform.runLater(btnOK::requestFocus);
                    }
                }
                e.consume();
            });
            stage.show();
        } catch (Exception e) {
            loggerAlterUtility.error("Exception on AlertWarning() -> " + Globals.getExceptionString(e));
        }
    }

    public static void AlertConfirmation(String Title, String Message, CustomCallback callback) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(GenivisApplication.class.getResource("ui/alertcomponent/alertconfirmation.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 216);
            scene.setFill(null);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.setOnShown((e) -> {
                Button btnOK = (Button) scene.lookup("#btnYes");
                btnOK.requestFocus();
            });

            Button btnOK = (Button) scene.lookup("#btnYes");

            Label lblTitle = (Label) scene.lookup("#lblTitle");
            lblTitle.setText(Title);
            Label lblMessage = (Label) scene.lookup("#lblMessage");
            lblMessage.setText(Message);
            btnOK.setOnAction((e) -> {
                callback.onYesNo(1);
                stage.close();
                e.consume();
            });
            Button btnNo = (Button) scene.lookup("#btnNo");
            btnNo.setOnAction((e) -> {
                callback.onYesNo(0);
                stage.close();
                e.consume();
            });
            ImageView imgClose = (ImageView) scene.lookup("#imgClose");
            imgClose.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Stage lstage = (Stage) imgClose.getScene().getWindow();
                    lstage.close();
                    event.consume();
                }
            });

            scene.setOnKeyReleased((e) -> {
                if (e.getCode() == KeyCode.ESCAPE) {
                    stage.close();
                } else if (e.getCode() == KeyCode.TAB) {
                    if (btnOK.isFocused()) {
                        Platform.runLater(btnNo::requestFocus);
                    } else {
                        Platform.runLater(btnOK::requestFocus);
                    }
                } else if (e.getCode() == KeyCode.LEFT) {
                    if (btnOK.isFocused()) {
                        Platform.runLater(btnNo::requestFocus);
                    } else {
                        Platform.runLater(btnOK::requestFocus);
                    }
                } else if (e.getCode() == KeyCode.RIGHT) {
                    if (btnOK.isFocused()) {
                        Platform.runLater(btnNo::requestFocus);
                    } else {
                        Platform.runLater(btnOK::requestFocus);
                    }
                }
                e.consume();
            });
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            loggerAlterUtility.error("Exception on AlertConfirmation() -> " + Globals.getExceptionString(e));
        }
    }

    public static void AlertError(Stage rootStage, String Title, String Message, CustomCallback callback) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(GenivisApplication.class.getResource("ui/alertcomponent/alerterror.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 216);
            scene.setFill(null);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.initModality(Modality.APPLICATION_MODAL);
            OverlaysEffect.setOverlaysEffect(rootStage);
            stage.setOnShown((e) -> {
                Button btnNo = (Button) scene.lookup("#btnNo");
                btnNo.requestFocus();
            });

            Label lblTitle = (Label) scene.lookup("#lblTitle");
            lblTitle.setText(Title);
            Label lblMessage = (Label) scene.lookup("#lblMessage");
            lblMessage.setText(Message);

            Button btnNo = (Button) scene.lookup("#btnNo");
            btnNo.setOnAction((e) -> {
                callback.onYesNo(0);
                stage.close();
                e.consume();
                OverlaysEffect.removeOverlaysEffect(rootStage);
            });
            ImageView imgClose = (ImageView) scene.lookup("#imgClose");
            imgClose.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Stage lstage = (Stage) imgClose.getScene().getWindow();
                    lstage.close();
                    event.consume();
                    OverlaysEffect.removeOverlaysEffect(rootStage);
                }
            });
            scene.setOnKeyReleased((e) -> {
                if (e.getCode() == KeyCode.ESCAPE) {
                    stage.close();
                } else if (e.getCode() == KeyCode.TAB) {
                    Platform.runLater(btnNo::requestFocus);
                }
                e.consume();
            });
            stage.show();
        } catch (Exception e) {
            loggerAlterUtility.error("Exception on AlertError() -> " + Globals.getExceptionString(e));
        }
    }


    public static void AlertError(String Title, String Message, CustomCallback callback) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(GenivisApplication.class.getResource("ui/alertcomponent/alerterror.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 216);
            scene.setFill(null);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOnShown((e) -> {
                Button btnNo = (Button) scene.lookup("#btnNo");
                btnNo.requestFocus();
            });
            Label lblTitle = (Label) scene.lookup("#lblTitle");
            lblTitle.setText(Title);
            Label lblMessage = (Label) scene.lookup("#lblMessage");
            lblMessage.setText(Message);

            Button btnNo = (Button) scene.lookup("#btnNo");
            btnNo.setOnAction((e) -> {
                callback.onYesNo(0);
                stage.close();
                e.consume();
            });
            ImageView imgClose = (ImageView) scene.lookup("#imgClose");
            imgClose.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Stage lstage = (Stage) imgClose.getScene().getWindow();
                    lstage.close();
                    event.consume();
                }
            });
            scene.setOnKeyReleased((e) -> {
                if (e.getCode() == KeyCode.ESCAPE) {
                    stage.close();
                } else if (e.getCode() == KeyCode.TAB) {
                    Platform.runLater(btnNo::requestFocus);
                }
                e.consume();
            });
            stage.show();
        } catch (Exception e) {
            loggerAlterUtility.error("Exception on AlertError() -> " + Globals.getExceptionString(e));
        }
    }

    public static void AlertTimer(String Title, String Message, int countInSeconds) {
        AtomicInteger currentProgressCounter = new AtomicInteger(0);
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(GenivisApplication.class.getResource("ui/alertcomponent/alerttimer.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 75);
            scene.setFill(null);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            ProgressBar pb = (ProgressBar) scene.lookup("#pb");
            pb.setProgress(0);
            Label lblTitle = (Label) scene.lookup("#lblTitle");
            lblTitle.setText(Title);
            Label lblMessage = (Label) scene.lookup("#lblMessage");
            lblMessage.setText(Message);
            ImageView imgClose = (ImageView) scene.lookup("#imgClose");
            imgClose.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Stage lstage = (Stage) imgClose.getScene().getWindow();
                    lstage.close();
                    event.consume();
                }
            });
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(1), event -> pb.setProgress(currentProgressCounter.getAndIncrement() / (double) countInSeconds))
            );
            timeline.setCycleCount(countInSeconds);
            timeline.setOnFinished(event -> {
                Stage lstage = (Stage) imgClose.getScene().getWindow();
                lstage.close();
                event.consume();
            });
            timeline.play();
            stage.show();
        } catch (Exception e) {
            loggerAlterUtility.error("Exception on AlertTimer() -> " + Globals.getExceptionString(e));
        }
    }


    /**
     * Info : ALertSuccess PopUp Will Close After The TimeOut
     *
     * @Author : Vinit Chilaka
     */
    public static void AlertSuccessTimeout(String Title, String Message, CustomCallback callback) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(GenivisApplication.class.getResource("ui/alertcomponent/alertSuccessTimeout.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 216);
            scene.setFill(null);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);

            Label lblTitle = (Label) scene.lookup("#lblTitle");
            lblTitle.setText(Title);
            Label lblMessage = (Label) scene.lookup("#lblMessage");
            lblMessage.setStyle("-fx-font-size: 16px;");
            lblMessage.setText(Message);
            lblMessage.setWrapText(true);
            ImageView imgClose = (ImageView) scene.lookup("#imgClose");
            imgClose.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.close();
                    event.consume();
                }
            });

            stage.initModality(Modality.APPLICATION_MODAL);

            // Center the stage on the screen
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - 400) / 2);  // 400 is the width of the scene
            stage.setY((screenBounds.getHeight() - 216) / 2); // 216 is the height of the scene

            stage.show();

            // Create a PauseTransition to close the stage after 2 seconds
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> {
                stage.close();
                if (callback != null) {
                    callback.onYesNo(1); // or some other appropriate value
                }
            });
            delay.play();

        } catch (Exception e) {
            loggerAlterUtility.error("Exception on AlertSuccess() -> " + Globals.getExceptionString(e));
        }
    }

    /**
     * Info : ALertError PopUp Will Close After The TimeOut
     *
     * @Author : Vinit Chilaka
     */
    public static void AlertErrorTimeout(String Title, String Message, CustomCallback callback) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(GenivisApplication.class.getResource("ui/alertcomponent/alertErrorTimeout.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 216);
            scene.setFill(null);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);

            Label lblTitle = (Label) scene.lookup("#lblTitle");
            lblTitle.setText(Title);
            Label lblMessage = (Label) scene.lookup("#lblMessage");
            lblMessage.setStyle("-fx-font-weight: normal; -fx-font-size: 16px;");

            lblMessage.setText(Message);
            lblMessage.setWrapText(true);


            ImageView imgClose = (ImageView) scene.lookup("#imgClose");
            imgClose.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.close();
                    event.consume();
                }
            });

            stage.initModality(Modality.APPLICATION_MODAL);

            // Center the stage on the screen
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - 400) / 2);  // 400 is the width of the scene
            stage.setY((screenBounds.getHeight() - 216) / 2); // 216 is the height of the scene

            stage.show();

            // Create a PauseTransition to close the stage after 2 seconds
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> {
                stage.close();
                if (callback != null) {
                    callback.onYesNo(1); // or some other appropriate value
                }
            });
            delay.play();

        } catch (Exception e) {
            loggerAlterUtility.error("Exception on AlertSuccess() -> " + Globals.getExceptionString(e));
        }
    }

    public static void AlertErrorTimeout2(String Title, String Message, CustomCallback callback) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(GenivisApplication.class.getResource("ui/alertcomponent/alertErrorTimeout.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 216);
            scene.setFill(null);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);

            Label lblTitle = (Label) scene.lookup("#lblTitle");
            lblTitle.setText(Title);
            Label lblMessage = (Label) scene.lookup("#lblMessage");
            lblMessage.setStyle("-fx-font-weight: normal; -fx-font-size: 16px;");

            lblMessage.setText(Message);
            lblMessage.setWrapText(true);


            ImageView imgClose = (ImageView) scene.lookup("#imgClose");
            imgClose.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.close();
                    event.consume();
                }
            });

            stage.initModality(Modality.APPLICATION_MODAL);

            // Center the stage on the screen
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - 400) / 2);  // 400 is the width of the scene
            stage.setY((screenBounds.getHeight() - 216) / 2); // 216 is the height of the scene

            stage.show();

            // Create a PauseTransition to close the stage after 2 seconds
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(event -> {
                stage.close();
                if (callback != null) {
                    callback.onYesNo(1); // or some other appropriate value
                }
            });
            delay.play();

        } catch (Exception e) {
            loggerAlterUtility.error("Exception on AlertSuccess() -> " + Globals.getExceptionString(e));
        }
    }

    public static void AlertErrorTimeout3(String Title, String Message, Consumer<Boolean> callback) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(GenivisApplication.class.getResource("ui/alertcomponent/alertErrorTimeout.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 216);
            scene.setFill(null);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);

            Label lblTitle = (Label) scene.lookup("#lblTitle");
            lblTitle.setText(Title);
            Label lblMessage = (Label) scene.lookup("#lblMessage");
            lblMessage.setStyle("-fx-font-weight: normal; -fx-font-size: 16px;");

            lblMessage.setText(Message);
            lblMessage.setWrapText(true);


            ImageView imgClose = (ImageView) scene.lookup("#imgClose");
            imgClose.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.close();
                    event.consume();
                }
            });

            stage.initModality(Modality.APPLICATION_MODAL);

            // Center the stage on the screen
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - 400) / 2);  // 400 is the width of the scene
            stage.setY((screenBounds.getHeight() - 216) / 2); // 216 is the height of the scene

            stage.show();

            // Create a PauseTransition to close the stage after 2 seconds
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(event -> {
                callback.accept(true);
                stage.close();
            });
            delay.play();

        } catch (Exception e) {
            loggerAlterUtility.error("Exception on AlertSuccess() -> " + Globals.getExceptionString(e));
        }
    }
    public static void AlertWarningTimeout3(String Title, String Message, Consumer<Boolean> callback) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(GenivisApplication.class.getResource("ui/alertcomponent/alertWarningTimeout.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 216);
            scene.setFill(null);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);

            Label lblTitle = (Label) scene.lookup("#lblTitle");
            lblTitle.setText(Title);
            Label lblMessage = (Label) scene.lookup("#lblMessage");
            lblMessage.setStyle("-fx-font-weight: normal; -fx-font-size: 16px;");

            lblMessage.setText(Message);
            lblMessage.setWrapText(true);


            ImageView imgClose = (ImageView) scene.lookup("#imgClose");
            imgClose.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.close();
                    event.consume();
                }
            });

            stage.initModality(Modality.APPLICATION_MODAL);

            // Center the stage on the screen
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - 400) / 2);  // 400 is the width of the scene
            stage.setY((screenBounds.getHeight() - 216) / 2); // 216 is the height of the scene

            stage.show();

            // Create a PauseTransition to close the stage after 2 seconds
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> {
                callback.accept(true);
                stage.close();
            });
            delay.play();

        } catch (Exception e) {
            loggerAlterUtility.error("Exception on AlertSuccess() -> " + Globals.getExceptionString(e));
        }
    }

    /**
     * Info : ALertWarning PopUp Will Close After The TimeOut
     *
     * @Author : Vinit Chilaka
     */
    public static void AlertWarningTimeout(String Title, String Message, CustomCallback callback) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(GenivisApplication.class.getResource("ui/alertcomponent/alertWarningTimeout.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 216);
            scene.setFill(null);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);

            Label lblTitle = (Label) scene.lookup("#lblTitle");
            lblTitle.setText(Title);
            Label lblMessage = (Label) scene.lookup("#lblMessage");
            lblMessage.setStyle("-fx-font-weight: normal; -fx-font-size: 16px;");
            lblMessage.setText(Message);

            ImageView imgClose = (ImageView) scene.lookup("#imgClose");
            imgClose.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.close();
                    event.consume();
                }
            });

            stage.initModality(Modality.APPLICATION_MODAL);

            // Center the stage on the screen
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - 400) / 2);  // 400 is the width of the scene
            stage.setY((screenBounds.getHeight() - 216) / 2); // 216 is the height of the scene

            stage.show();

            // Create a PauseTransition to close the stage after 2 seconds
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> {
                stage.close();
                if (callback != null) {
                    callback.onYesNo(1); // or some other appropriate value
                }
            });
            delay.play();

        } catch (Exception e) {
            loggerAlterUtility.error("Exception on AlertSuccess() -> " + Globals.getExceptionString(e));
        }
    }


}
