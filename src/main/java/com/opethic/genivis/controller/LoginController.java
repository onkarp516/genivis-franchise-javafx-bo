package com.opethic.genivis.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.dto.reqres.product.HsnResDTO;
import com.opethic.genivis.models.api.LoginApiRequest;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.sql_lite.UserToken;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.CommonFunctionalUtils;
import com.opethic.genivis.utils.GlobalController;
import com.opethic.genivis.utils.Globals;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.*;
//import java.util.concurrent.TimeUnit;

import static com.opethic.genivis.utils.FxmFileConstants.PURCHASE_CHALLAN_LIST_SLUG;
//import java.util.concurrent.TimeUnit;


public class LoginController implements Initializable {

    @FXML
    private Button hideShow;
    @FXML
    private TextField tfAreaHeadCreatePasswordInText;
    @FXML
    public TextField tffranchiseCode;
    @FXML
    private HBox loginMainDivider;
    @FXML
    private VBox loginMainDividerFirst, loginMainDividerSecond, innerLoginBox, firstinnerDiv, lgnBoxInnerTextFieldGap;
    @FXML
    private BorderPane bpLoginRootPane;
    @FXML
    private Label lbCopyright;
    @FXML
    private Text lbSolution;
    @FXML
    private HBox hbAnalysis;
    @FXML
    private ImageView ivLoginBg;
    //    @FXML
//    public Text tDuration;
    @FXML
    private Button btnLogin;
    @FXML
    private TextField tfUserName;
    @FXML
    private PasswordField tfPassword;
    private Scene scene;
    private Parent loader;

    @FXML
    private VBox vbLogin;
    @FXML
    private Label lbDesign;
    private boolean apiCall = true;
    private static final Logger loginLogger = LogManager.getLogger(LoginController.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ResponsiveWiseCssPicker();

        CommonFunctionalUtils.commonInit(tfPassword, tfAreaHeadCreatePasswordInText, hideShow);

        /*tffranchiseCode.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        tfUserName.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        tfPassword.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");*/

//        ivLoginBg.toBack();

        tffranchiseCode.requestFocus();

//        // Create an image view with the image
        ImageView imageView = new ImageView(new Image(GenivisApplication.class.getResourceAsStream("/com/opethic/genivis/ui/assets/arrow.png")));
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        // Set the image view as the graphic of the button
        btnLogin.setGraphic(imageView);


        btnLogin.setOnAction(event -> {
            if (apiCall) {

                loginLogger.info("Login initiated at " + new Date());
                String dataValidation = validateData();
                String franchiseCode = tffranchiseCode.getText().trim();
                String userName = tfUserName.getText().trim();
                String password = tfPassword.getText().trim();
                Globals.headerBranch = franchiseCode;
                String loginFailedMessage = "Login Failed, Try again";

                if (dataValidation.isEmpty()) {
                    loginLogger.info("Login validation success");
                    try {
                        LoginApiRequest loginApiRequest = new LoginApiRequest();
                        loginApiRequest.setFranchiseCode(franchiseCode);
                        Globals.headerBranch = franchiseCode;
                        loginApiRequest.setUsercode(userName);
                        loginApiRequest.setPassword(password);
                        String requestBody = new Gson().toJson(loginApiRequest);
                        APIClient apiClient = new APIClient(EndPoints.AUTHENTICATE_ENDPOINT, requestBody, RequestType.JSON_NO_TOKEN);
                        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                AlertUtility.CustomCallback callback = (number) -> {
                                    if (number == 1) {
                                    }
                                };
                                if (workerStateEvent.getSource().getValue() != null) {

                                    String loginResponse = workerStateEvent.getSource().getValue().toString();
                                    JsonObject responseBody = new Gson().fromJson(loginResponse, JsonObject.class);

                                    if (responseBody != null) {
                                        String message = responseBody.get("message") != null ? responseBody.get("message").getAsString() : "Login Failed!"; // using this for Alert Utilty in Else Part
                                        Stage stage2 = (Stage) bpLoginRootPane.getScene().getWindow(); // using this for Alert Utilty in Else Part
                                        if (responseBody.get("responseStatus").getAsInt() == 200) {
                                            apiCall = false;
                                            System.out.println("responseBody... " + responseBody);
                                            String accessToken = responseBody.getAsJsonObject("responseObject").get("access_token").getAsString();
                                            String refreshToken = responseBody.getAsJsonObject("responseObject").get("refresh_token").getAsString();
                                            String userRole = responseBody.get("userRole").getAsString();
//                            String userRole="CADMIN";
                                            UserToken.setToken(accessToken, refreshToken, userRole);
                                            Globals.headerBranch = UserToken.getAccessTokenDetails().get("companyCode").getAsString();
                                            Globals.USER_TOKEN = UserToken.getAccessToken();
                                            //Create Login Response Model Class
                                            try {
                                                Node node = (Node) event.getSource();
//                                Stage stage = (Stage) node.getScene().getWindow();
                                                Stage stage = new Stage();
                                                stage.setTitle(Globals.APP_TITLE);
                                                stage.getIcons().add(new Image(GenivisApplication.class.getResourceAsStream("ui/assets/mainlogo.png")));
                                                if (userRole.equalsIgnoreCase("cadmin"))
                                                    loader = FXMLLoader.load(GenivisApplication.class.getResource("ui/users/cadmin_dashboard.fxml"));
                                                else {
                                                    loader = FXMLLoader.load(GenivisApplication.class.getResource("ui/users/sadmin_dashboard.fxml"));
                                                }
                                                Globals.isLoggedIn = true;
                                                scene = new Scene(loader);
                                                stage.setScene(scene);
                                                stage.setMaximized(true);
                                                stage.show();
                                                Stage loginStage = (Stage) btnLogin.getScene().getWindow();
                                                loginStage.close();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                apiCall = true;
                                            }
                                        } else {
                                            apiCall = true;
                                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                                            });
                                        }

                                    } else {
                                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, LedgerMessageConsts.msgServerNotReady, in -> {
                                        });

                                    }


                                } else {
                                    apiCall = true;
                                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Login Failed, Try again.", in -> {
                                    });
                                }

                            }
                        });
                        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                apiCall = true;
                                if (workerStateEvent.getSource().getValue() != null) {
                                    loginLogger.error("Login error : " + workerStateEvent.getSource().getValue().toString());
                                } else {
                                    loginLogger.error("Login error : API return null");
                                }
                                showAlert(loginFailedMessage);
                            }
                        });
                        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                apiCall = true;
                                if (workerStateEvent.getSource().getValue() != null) {
                                    loginLogger.error("Login error : " + workerStateEvent.getSource().getValue().toString());
                                } else {
                                    loginLogger.error("Login error : API return null");
                                }
                                showAlert(loginFailedMessage);
                            }
                        });
                        apiClient.start();

                    } catch (Exception x) {
                        apiCall = true;
                        loginLogger.error("Login Error = > " + x.toString());
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, LedgerMessageConsts.msgServerNotReady, in -> {
                        });
                    }

                } else {
                    apiCall = true;
                    loginLogger.info("Login validation failed");
//                    showAlert(dataValidation);
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, dataValidation, in -> {
                    });
                }
            }
        });

    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            ImageView imageView = new ImageView(new Image(GenivisApplication.class.getResourceAsStream("/com/opethic/genivis/ui/assets/arrow.png")));
            imageView.setFitWidth(12);
            imageView.setFitHeight(12);
            btnLogin.setGraphic(imageView);

            innerLoginBox.setSpacing(10);
            firstinnerDiv.setSpacing(10);
            lgnBoxInnerTextFieldGap.setSpacing(10);
            loginMainDivider.setPadding(new Insets(25, 0, 0, 0));
            loginMainDividerFirst.prefWidthProperty().bind(loginMainDivider.widthProperty().multiply(0.5));
            loginMainDividerSecond.prefWidthProperty().bind(loginMainDivider.widthProperty().multiply(0.5));
            bpLoginRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            ImageView imageView = new ImageView(new Image(GenivisApplication.class.getResourceAsStream("/com/opethic/genivis/ui/assets/arrow.png")));
            imageView.setFitWidth(13);
            imageView.setFitHeight(13);
            btnLogin.setGraphic(imageView);

            innerLoginBox.setSpacing(12);
            firstinnerDiv.setSpacing(12);
            lgnBoxInnerTextFieldGap.setSpacing(12);
            loginMainDivider.setPadding(new Insets(25, 0, 0, 0));
            loginMainDividerFirst.prefWidthProperty().bind(loginMainDivider.widthProperty().multiply(0.58));
            loginMainDividerSecond.prefWidthProperty().bind(loginMainDivider.widthProperty().multiply(0.42));
            bpLoginRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            ImageView imageView = new ImageView(new Image(GenivisApplication.class.getResourceAsStream("/com/opethic/genivis/ui/assets/arrow.png")));
            imageView.setFitWidth(15);
            imageView.setFitHeight(15);
            btnLogin.setGraphic(imageView);

            innerLoginBox.setSpacing(15);
            firstinnerDiv.setSpacing(15);
            lgnBoxInnerTextFieldGap.setSpacing(15);
            loginMainDivider.setPadding(new Insets(30, 0, 0, 0));
            loginMainDividerFirst.prefWidthProperty().bind(loginMainDivider.widthProperty().multiply(0.58));
            loginMainDividerSecond.prefWidthProperty().bind(loginMainDivider.widthProperty().multiply(0.42));
            bpLoginRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            ImageView imageView = new ImageView(new Image(GenivisApplication.class.getResourceAsStream("/com/opethic/genivis/ui/assets/arrow.png")));
            imageView.setFitWidth(18);
            imageView.setFitHeight(18);
            btnLogin.setGraphic(imageView);

            innerLoginBox.setSpacing(20);
            firstinnerDiv.setSpacing(20);
            lgnBoxInnerTextFieldGap.setSpacing(20);
            loginMainDivider.setPadding(new Insets(40, 0, 0, 0));
            loginMainDividerFirst.prefWidthProperty().bind(loginMainDivider.widthProperty().multiply(0.6));
            loginMainDividerSecond.prefWidthProperty().bind(loginMainDivider.widthProperty().multiply(0.4));
            bpLoginRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            ImageView imageView = new ImageView(new Image(GenivisApplication.class.getResourceAsStream("/com/opethic/genivis/ui/assets/arrow.png")));
            imageView.setFitWidth(18);
            imageView.setFitHeight(18);
            btnLogin.setGraphic(imageView);

            innerLoginBox.setSpacing(20);
            firstinnerDiv.setSpacing(20);
            lgnBoxInnerTextFieldGap.setSpacing(20);
            loginMainDivider.setPadding(new Insets(30, 0, 0, 0));
            loginMainDividerFirst.prefWidthProperty().bind(loginMainDivider.widthProperty().multiply(0.6));
            loginMainDividerSecond.prefWidthProperty().bind(loginMainDivider.widthProperty().multiply(0.4));
            bpLoginRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            ImageView imageView = new ImageView(new Image(GenivisApplication.class.getResourceAsStream("/com/opethic/genivis/ui/assets/arrow.png")));
            imageView.setFitWidth(18);
            imageView.setFitHeight(18);
            btnLogin.setGraphic(imageView);


            innerLoginBox.setSpacing(20);
            firstinnerDiv.setSpacing(20);
            lgnBoxInnerTextFieldGap.setSpacing(20);
            loginMainDivider.setPadding(new Insets(30, 0, 0, 0));
            loginMainDividerFirst.prefWidthProperty().bind(loginMainDivider.widthProperty().multiply(0.6));
            loginMainDividerSecond.prefWidthProperty().bind(loginMainDivider.widthProperty().multiply(0.4));
            bpLoginRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            innerLoginBox.setSpacing(20);
            firstinnerDiv.setSpacing(20);
            lgnBoxInnerTextFieldGap.setSpacing(20);
            loginMainDivider.setPadding(new Insets(50, 0, 0, 0));
            loginMainDividerFirst.prefWidthProperty().bind(loginMainDivider.widthProperty().multiply(0.60));
            loginMainDividerSecond.prefWidthProperty().bind(loginMainDivider.widthProperty().multiply(0.40));
            bpLoginRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }


    @FXML
    public void passwordBtn() {
        CommonFunctionalUtils.passwordField(tfPassword, tfAreaHeadCreatePasswordInText, hideShow);
    }

    private String getDuration() {
        String mDuration = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.ENGLISH);
        String endDateString = "13-04-2024 23:59:59";
        try {
            Date startDate = new Date();
            Date endDate = simpleDateFormat.parse(endDateString);

            Period period = Period.between(LocalDate.now(), LocalDate.of(2024, Month.MARCH, 31));

            long diffInSeconds = endDate.getSeconds() - startDate.getSeconds();
            long diffInMinutes = endDate.getMinutes() - startDate.getMinutes();
            long diffInHours = endDate.getHours() - startDate.getHours();
            int diffInDays = period.getDays();
            mDuration = "Target : " + diffInDays + " days " + diffInHours + " hrs " + diffInMinutes + " min " + diffInSeconds + " sec";

        } catch (Exception x) {
            x.printStackTrace();
        }


        return mDuration;

    }


//    private void showAlert(String message) {
//        Alert alert = new Alert(Alert.AlertType.NONE,message,ButtonType.OK);
//        alert.show();
//    }

    //function for enter key username
    @FXML
    public void franchisecodenter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            tfUserName.requestFocus();
        }
    }

    @FXML
    public void usernameEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            tfPassword.requestFocus();
        }
    }

    //function for enter key password
    @FXML
    public void passwordEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            btnLogin.requestFocus();
        }
    }

    private void showAlert(String message) {
        //?: use util for all Alert messages
        Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
        Duration duration = Duration.seconds(0.9);
        alert.show();
        Timeline timeline = new Timeline(new KeyFrame(duration, event -> alert.close()));
        timeline.play();
    }

    private String validateData() {
        String message = "Invalid Data";

        message = tffranchiseCode.getText().trim().isEmpty() ? "Please enter Franchise Code" : "";
        if (!message.isEmpty())
            return message;

        message = tfUserName.getText().trim().isEmpty() ? "Please enter Username" : "";

        if (!message.isEmpty())
            return message;

        message = tfPassword.getText().trim().isEmpty() ? "Please enter Password" : "";
        return message;
    }


}
