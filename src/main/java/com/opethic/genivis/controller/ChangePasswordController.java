package com.opethic.genivis.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opethic.genivis.controller.master.ProductListController;
import com.opethic.genivis.dto.PaymentModeDTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {
    @FXML
    private TextField tfChangePasswordUsername;
    @FXML
    private PasswordField pfChangePasswordPassword,pfChangePasswordConfirmPassword;
    @FXML
    private Button btnChangePasswordSubmit,btnChangePasswordClear;
    @FXML
    private ScrollPane spRootChangePasswordPane;
    private ObservableList<PaymentModeDTO> originalData;
    private JsonObject jsonObject = null;
    private static final Logger logger = LogManager.getLogger(ChangePasswordController.class);

    String id="";
    String message="";

    @FXML
    private VBox vboxChangePasswordRoot;
    @FXML
    private TextField tfChangePasswordShowPasswordInText,tfChangePasswordShowConfirmPasswordInText;
    @FXML
    private Button hideShow,hideShowConfirm;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Hide the ScrollBar of the Scroll Page
        spRootChangePasswordPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        spRootChangePasswordPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        //Show and Hide the Passwords on Eye Button
        CommonFunctionalUtils.commonInit(pfChangePasswordPassword, tfChangePasswordShowPasswordInText,hideShow);
        CommonFunctionalUtils.commonInit(pfChangePasswordConfirmPassword, tfChangePasswordShowConfirmPasswordInText,hideShowConfirm);

        //Get the user Name who is Logged In
        loadUserName();

        //Disable the Field to Read Only
//        Platform.runLater(() -> tfChangePasswordUsername.setDisable(true));
        //Disable the Field without changing the Design of the Field
        Platform.runLater(() -> {
            //On Page Open Focus on the First Field
            pfChangePasswordPassword.requestFocus();
            tfChangePasswordUsername.setDisable(true);
            tfChangePasswordUsername.setStyle("-fx-opacity: 1;"); // Make textfield fully visible
            tfChangePasswordUsername.pseudoClassStateChanged(PseudoClass.getPseudoClass("disabled"), false);
        });

        //Enter,Tab Key Cursor Movement
        initShortcutKeys();

        //Stop the Cusor on the Field
        mandatoryFields();

        //Show alert on the Screen
        sceneInitilization();

        //Change Password Function Call on Submit Button
        btnChangePasswordSubmit.setOnAction(e->{
            if (CommonValidationsUtils.validateForm(tfChangePasswordUsername,pfChangePasswordPassword,pfChangePasswordConfirmPassword)) {
                changePassword();
            }
        });
        //clear data on click clear button
        btnChangePasswordClear.setOnAction((event)->{
            clearFields();
        });
    }

    private void initShortcutKeys() {
        spRootChangePasswordPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {

            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("submit")) {
                    System.out.println(targetButton.getText());
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("clear")) {
                    System.out.println(targetButton.getText());
                }
                else {

                    KeyEvent newEvent = new KeyEvent(
                            null,
                            null,
                            KeyEvent.KEY_PRESSED,
                            "",
                            "\t",
                            KeyCode.TAB,
                            event.isShiftDown(),
                            event.isControlDown(),
                            event.isAltDown(),
                            event.isMetaDown()

                    );

                    Event.fireEvent(event.getTarget(), newEvent);
                    event.consume();
                }
            }
            if (event.getCode() == KeyCode.S && event.isControlDown()) {
                btnChangePasswordSubmit.fire();
            }else if(event.getCode() == KeyCode.X && event.isControlDown()) {
                btnChangePasswordClear.fire();
            }
        });
    }

    private void mandatoryFields() {

        //Change the Color of * Mandatory Fields to Red
        CommonValidationsUtils.changeStarColour(vboxChangePasswordRoot);

        //Stop the Cusor on the Field
        pfChangePasswordPassword.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = pfChangePasswordPassword.getText().trim();
                if (text.isEmpty()) {
                    pfChangePasswordPassword.requestFocus();
                }
            }
        });
        //Stop the Cusor on the Field
        tfChangePasswordShowPasswordInText.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = tfChangePasswordShowPasswordInText.getText().trim();
                if (text.isEmpty()) {
                    tfChangePasswordShowPasswordInText.requestFocus();
                }
            }
        });

////        Stop the Cusor on the Field
//        pfChangePasswordConfirmPassword.focusedProperty().addListener((obs, oldVal, newVal) -> {
//            if (!newVal) {
//                String text = pfChangePasswordConfirmPassword.getText().trim();
//                if (text.isEmpty()) {
//                    pfChangePasswordConfirmPassword.requestFocus();
//                }
//            }
//        });

        // Attach event handler to confirm password field
        pfChangePasswordConfirmPassword.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!pfChangePasswordConfirmPassword.getText().isEmpty()){
                if (!newValue) { // Check if focus lost
                    validatePasswords();
                }
            }
        });
        // Attach event handler to confirm password field
        tfChangePasswordShowConfirmPasswordInText.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!tfChangePasswordShowConfirmPasswordInText.getText().isEmpty()){
                if (!newValue) { // Check if focus lost
                    pfChangePasswordPassword.setText(tfChangePasswordShowConfirmPasswordInText.getText());
                    validatePasswords();
                }
            }
        });

        tfChangePasswordShowConfirmPasswordInText.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!tfChangePasswordShowConfirmPasswordInText.getText().isEmpty()){
                if (!newValue) { // Check if focus lost
                    pfChangePasswordConfirmPassword.setText(tfChangePasswordShowConfirmPasswordInText.getText());
                    validatePasswords();
                }
            }
        });


    }

    //function to set the Username in the Field
    private void loadUserName() {
        tfChangePasswordUsername.setText(GlobalTranx.getUserCode() + "");
    }

    public void sceneInitilization() {
        spRootChangePasswordPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }

    //Function to Show and Hide the Passwords on Eye Button
    @FXML
    public void passwordBtn() {
        CommonFunctionalUtils.passwordField(pfChangePasswordPassword, tfChangePasswordShowPasswordInText, hideShow);
    }
    @FXML
    public void passwordBtnConfirm() {
        CommonFunctionalUtils.passwordField(pfChangePasswordConfirmPassword, tfChangePasswordShowConfirmPasswordInText, hideShowConfirm);
    }

    // Method to validate passwords and display error if necessary
    private void validatePasswords() {
        // Get the text from the PasswordFields
        String password = pfChangePasswordPassword.getText();
        String confirmPassword = pfChangePasswordConfirmPassword.getText();

        // Check if the passwords match
        if (!password.equals(confirmPassword)) {
            AlertUtility.AlertDialogForError("WARNING", "Password and Confirm Password did not match.", input -> {
                if (input) {
//                    pfChangePasswordConfirmPassword.setText("");
                    pfChangePasswordConfirmPassword.requestFocus();
                    if(tfChangePasswordShowConfirmPasswordInText.isVisible()){
                        tfChangePasswordShowConfirmPasswordInText.requestFocus();
                    }
                }
            });
        }
    }

    //clear Fields Function
    public void clearFields(){
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
        pfChangePasswordPassword.setText("");
        pfChangePasswordConfirmPassword.setText("");
        tfChangePasswordShowPasswordInText.setText("");
        tfChangePasswordShowConfirmPasswordInText.setText("");
        pfChangePasswordPassword.requestFocus();
        if(tfChangePasswordShowPasswordInText.isVisible()){
            tfChangePasswordShowPasswordInText.requestFocus();
        }
            } else {
            }
        };
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Clear", callback);
    }

    //change password Function
    public void changePassword(){
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                APIClient apiClient=null;
                Map<String, String> map = new HashMap<>();
                map.put("usercode", tfChangePasswordUsername.getText());
                map.put("password", pfChangePasswordPassword.getText());

                HttpResponse<String> response;
                String formData = Globals.mapToStringforFormData(map);
                    //  response = APIClient.postFormDataRequest(formData, PAYMENT_MODE_CREATE_ENDPOINT);
                    apiClient = new APIClient(EndPoints.CHANGE_PASSWORD_ENDPOINT, formData, RequestType.FORM_DATA);

                apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        // JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                        jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

                        message = jsonObject.get("message").getAsString();

                        if (jsonObject.get("responseStatus").getAsInt() == 200) {
                            AlertUtility.CustomCallback callback = (number) -> {

                                if (number == 1) {
//                                    GlobalController.getInstance().addTabStatic(FRANCHISE_LIST_SLUG, false);
                                }
                            };

                            Stage stage2 = (Stage) spRootChangePasswordPane.getScene().getWindow();
                            if(jsonObject.get("responseStatus").getAsInt()==200){
                                AlertUtility.AlertSuccess(stage2,"Success",message,callback);

                                pfChangePasswordPassword.requestFocus();
                            }
                            else{
                                AlertUtility.AlertError(stage2,AlertUtility.alertTypeError, message, callback);
                            }
                            pfChangePasswordPassword.setText("");
                            pfChangePasswordConfirmPassword.setText("");
                            tfChangePasswordShowPasswordInText.setText("");
                            tfChangePasswordShowConfirmPasswordInText.setText("");

                        }
                    }
                });

                apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        logger.error("Network API cancelled in changePassword()" + workerStateEvent.getSource().getValue().toString());

                    }
                });
                apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        logger.error("Network API failed in changePassword()" + workerStateEvent.getSource().getValue().toString());

                    }
                });
                apiClient.start();
                logger.debug("create change password data end ...");
            } else {
            }
        };
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Submit", callback);

    }

}
