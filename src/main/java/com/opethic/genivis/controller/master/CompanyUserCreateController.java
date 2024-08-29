package com.opethic.genivis.controller.master;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.models.CompanyUserModel;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

import javax.swing.*;

public class CompanyUserCreateController implements Initializable {

    @FXML
    VBox cUserAccess;
    private Long userRoleId = 0L;
    private Long companyId = 0L;
    private HBox hBox;
    @FXML
    private ComboBox cmbCmpuserCreateCmpName;    //id of company name field dropdown
    @FXML
    private BorderPane companyUserRoot;
    @FXML
    private ComboBox cmbCmpuserCreateRoleName;     //id of User role field dropdown
    @FXML
    private TextField tfCmpUserCreateFullName;                    //id of full name
    @FXML
    private TextField tfCmpUserCreateMobileNo;                   //id of mobile no
    @FXML
    private TextField tfCmpUserCreateEmail;                    //id of email
    @FXML
    private RadioButton rbCmpUserCreateFemale, rbCmpUserCreateMale;                //id of male female radioButton

    @FXML
    private TextField tfCmpUserCreteUsername, tfCmpUserCreatePasswordInvisible;              // for username
    @FXML
    private PasswordField tfCmpUserCreatePassword;      //for password

    private GridPane gridPane;
    private String accessPerm;
    Font font;
    JsonArray jsonArray, permDataArray;
    @FXML
    private Button btnCmpUserCreateSubmit, btnCmpUserCreateCancel, btnPassword;
    @FXML
    private ImageView imageView;
    @FXML
    private Label toggleButton;
    @FXML
    private VBox starVbox;
    public String originalText = "";

    ScrollPane scrollPane;
    private Map<String, String> companyNameMap = new HashMap<>();
    private ToggleGroup toggleGroup = new ToggleGroup();
    private static final Logger comCreateLogger = LoggerFactory.getLogger(CompanyUserCreateController.class);
    public static KeyCombination com_user_save_CTRL_S = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
    public static KeyCombination com_user_cancel_CTRL_X = new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN);
    private long updateRoleId = 0L;
//    public static String selectedFocusId;
    private static Long createFocusId;


    @FXML
    private void getIdOfUserRole() {
        //function for getting the id of user role when we select the user role
        gridPane.getChildren().clear();
        CompanyUserModel role = (CompanyUserModel) cmbCmpuserCreateRoleName.getSelectionModel().getSelectedItem();
        if (role.getRoleId() != userRoleId) {
            userRoleId = 0L;
            if (role != null) {
                userRoleId = Long.valueOf(role.getRoleId());     //send this id in create API of company user
                rolePermissions(userRoleId);
            } else {
                System.out.println("No item selected.");
            }
        }
    }

    @FXML
    private void getIdOfCompany() {      //function for getting the id of user role when we select the user role
        companyId = 0L;
        CompanyUserModel company = (CompanyUserModel) cmbCmpuserCreateCmpName.getSelectionModel().getSelectedItem();
        if (company != null) {
            companyId = Long.valueOf(company.getCompanyId());     //send this id in create API of company user
        } else {
            System.out.println("No item selected.");
        }
    }

    @FXML
    private void showHidePassword() {
        CommonFunctionalUtils.passwordField(tfCmpUserCreatePassword, tfCmpUserCreatePasswordInvisible, btnPassword);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        Platform.runLater(() -> {
            cmbCmpuserCreateRoleName.requestFocus();
            cmbCmpuserCreateCmpName.setEditable(false);
        });

        CommonValidationsUtils.configureTextFieldToTitleCase(tfCmpUserCreateFullName);
        CommonFunctionalUtils.commonInit(tfCmpUserCreatePassword, tfCmpUserCreatePasswordInvisible, btnPassword);

        tfCmpUserCreatePassword.setOnKeyTyped(event -> {
            originalText = tfCmpUserCreatePassword.getText();
        });
        CommonValidationsUtils.changeStarColour(starVbox);
        rbCmpUserCreateMale.setToggleGroup(toggleGroup);
        rbCmpUserCreateFemale.setToggleGroup(toggleGroup);

        cmbCmpuserCreateRoleName.setOnKeyPressed(event -> {    //focus for role name
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                event.consume();
                if (cmbCmpuserCreateRoleName.getSelectionModel().isEmpty()) {
                    cmbCmpuserCreateRoleName.requestFocus();
                } else {
                    tfCmpUserCreateFullName.requestFocus();
                }
            } else if (event.getCode() == KeyCode.SPACE) {
                cmbCmpuserCreateRoleName.show();
            }
        });
        tfCmpUserCreateFullName.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressForName);

//        tfCmpUserCreateFullName.setOnKeyPressed(event -> {       //focus for full name
//            if(event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB){
//                if(tfCmpUserCreateFullName.getText().isEmpty()){
//                    tfCmpUserCreateFullName.requestFocus();
//                }else tfCmpUserCreateMobileNo.requestFocus();
//            }else if(event.getCode() == KeyCode.TAB && event.isShiftDown()  ){
//                System.out.println("step1");
//                event.consume();
//                if(tfCmpUserCreateFullName.getText().isEmpty() && tfCmpUserCreateFullName.getText().isEmpty()){
//                    System.out.println("step2");
//                    tfCmpUserCreateFullName.requestFocus();
//                }else{ cmbCmpuserCreateRoleName.requestFocus();
//                    System.out.println("step3");}
//            }
//
//        });
        tfCmpUserCreateMobileNo.setOnKeyPressed(event -> {     //focus for mobile no
            if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                event.consume();
                if (tfCmpUserCreateMobileNo.isFocused()) {
                    tfCmpUserCreateFullName.requestFocus();
                }

            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                if (tfCmpUserCreateMobileNo.getText().length() < 10 && !tfCmpUserCreateMobileNo.getText().isEmpty()) {
                    AlertUtility.AlertError("Warning", "Number should be upto 10 digit", eventNew -> {
                        tfCmpUserCreateMobileNo.requestFocus();
                    });
                } else tfCmpUserCreateEmail.requestFocus();
            }

        });
        //code for validation of Mobile number
        tfCmpUserCreateMobileNo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                //if entered text matches with characters
                AlertUtility.AlertError("Warning", "Allowed only numbers", event -> {
                    tfCmpUserCreateMobileNo.requestFocus();
                });
            }
            if (newValue.length() > 10) {
                // Limit input to 10 characters
                AlertUtility.AlertError("Warning", "Number should be upto 10 digit", event -> {
                    tfCmpUserCreateMobileNo.requestFocus();
                });
                tfCmpUserCreateMobileNo.setText(newValue.substring(0, 10));
            }
        });
        //code for email validation
        tfCmpUserCreateEmail.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                event.consume();
                if (!validEmail(tfCmpUserCreateEmail.getText()) && !tfCmpUserCreateEmail.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered email is not valid
                    AlertUtility.AlertError("Warning", "Please Enter Valid Email Id", newEvent -> {
                        tfCmpUserCreateEmail.requestFocus();
                    });
                    event.consume();
                } else {
                    tfCmpUserCreateMobileNo.requestFocus();
                }
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {

                if (!validEmail(tfCmpUserCreateEmail.getText()) && !tfCmpUserCreateEmail.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered email is not valid
                    AlertUtility.AlertError("Warning", "Please Enter Valid Email Id", newEvent -> {
                        tfCmpUserCreateEmail.requestFocus();
                    });
                    event.consume();
                } else rbCmpUserCreateMale.requestFocus();
            }
        });

        rbCmpUserCreateMale.setOnKeyPressed(event -> {    //
            if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                event.consume();
                if (rbCmpUserCreateMale.isFocused()) {
                    tfCmpUserCreateEmail.requestFocus();
                }
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB)
                rbCmpUserCreateFemale.requestFocus();
        });
        rbCmpUserCreateFemale.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                event.consume();
                if (rbCmpUserCreateFemale.isFocused()) {
                    rbCmpUserCreateMale.requestFocus();
                }
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                tfCmpUserCreteUsername.requestFocus();
            }
        });
        tfCmpUserCreatePassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                event.consume();
                if (tfCmpUserCreatePassword.isFocused() && !tfCmpUserCreatePassword.getText().isEmpty()) {
                    tfCmpUserCreteUsername.requestFocus();
                } else if (tfCmpUserCreatePassword.getText().isEmpty()) {
                    tfCmpUserCreatePassword.requestFocus();
                }
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                if (tfCmpUserCreatePassword.getText().isEmpty()) {
                    tfCmpUserCreatePassword.requestFocus();
                } else if (!tfCmpUserCreatePassword.getText().isEmpty()) {
                    gridPane.getChildren().stream().toList().get(8).requestFocus();
                }
            }

        });
        btnCmpUserCreateSubmit.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                btnCmpUserCreateCancel.requestFocus();
            }
        });
        btnCmpUserCreateCancel.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                btnCmpUserCreateSubmit.requestFocus();
            }
        });
        roleList();
        jsonArray = new JsonArray();
        scrollPane = new ScrollPane();
        gridPane = new GridPane();
        gridPane.setVgap(10.0);
        gridPane.setHgap(20.0);
        gridPane.setStyle("-fx-background-color: #F8F8F8;");

        hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setSpacing(20);

        Label perticulars = new Label();
        perticulars.setText("Particulars");
        font = Font.font(Font.getDefault().getFamily(), FontWeight.NORMAL, FontPosture.REGULAR, 16);
        perticulars.setFont(font);
        gridPane.add(perticulars, 0, 0);

        accessPerm = accessData();

        setGridHeaders();
        scrollPane.setContent(gridPane);
        cUserAccess.getChildren().add(scrollPane);
        cUserAccess.getChildren().add(hBox);

        btnCmpUserCreateSubmit.sceneProperty().addListener((obsValue, oldValue, newValue) -> {
            if (newValue != null) {
                newValue.getAccelerators().put(com_user_save_CTRL_S, btnCmpUserCreateSubmit::fire);
            }
        });
        btnCmpUserCreateCancel.sceneProperty().addListener((obsValue, oldValue, newValue) -> {
            if (newValue != null) {
                newValue.getAccelerators().put(com_user_cancel_CTRL_X, btnCmpUserCreateCancel::fire);
            }
        });

        if (Globals.companyUserModelDTO != null) {
//            System.out.println("in edit page");
//            System.out.println("selectedFocusIdddd " + selectedFocusId);
            getEditDataById();
            tfCmpUserCreteUsername.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                    event.consume();
                    if (tfCmpUserCreteUsername.getText().isEmpty()) {
                        tfCmpUserCreteUsername.requestFocus();
                    } else tfCmpUserCreatePassword.requestFocus();
                }
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    event.consume();
                    if (tfCmpUserCreteUsername.getText().isEmpty()) {
                        tfCmpUserCreteUsername.requestFocus();
                    } else tfCmpUserCreatePassword.requestFocus();
                }

            });
            btnCmpUserCreateSubmit.setText("Update");
//           System.out.println("globaliddd"+ Globals.companyUserModelDTO.getId());
        } else {
            System.out.println("in create page");
            companyName();
            tfCmpUserCreteUsername.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressForUsernameCreate);

        }
    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            companyUserRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            companyUserRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            companyUserRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            companyUserRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            companyUserRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            companyUserRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            companyUserRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    private void handleKeyPressForName(KeyEvent event) {
        // Check if the key pressed is TAB and Shift key is held down
        if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
            // Consume the event to prevent default behavior
            event.consume();
            // Check if the text field is empty
            if (tfCmpUserCreateFullName.isFocused() && tfCmpUserCreateFullName.getText().isEmpty()) {
                // If empty, request focus on the text field
                tfCmpUserCreateFullName.requestFocus();
            } else {
                // Otherwise, request focus on the combo box
                cmbCmpuserCreateRoleName.requestFocus();
            }
        } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
            event.consume();
            if (tfCmpUserCreateFullName.isFocused() && tfCmpUserCreateFullName.getText().isEmpty()) {
                tfCmpUserCreateFullName.requestFocus();
            } else {
                tfCmpUserCreateMobileNo.requestFocus();
            }
        }
    }

    private void handleKeyPressForUsernameCreate(KeyEvent event) {

        if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
            event.consume();

            if (tfCmpUserCreteUsername.isFocused() && tfCmpUserCreteUsername.getText().isEmpty()) {
                tfCmpUserCreteUsername.requestFocus();
            } else if (!tfCmpUserCreteUsername.getText().isEmpty()) {
                functionToValidateUser(event);
                cmbCmpuserCreateRoleName.requestFocus();
            }
        } else if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) {
            event.consume();

            if (tfCmpUserCreteUsername.isFocused() && tfCmpUserCreteUsername.getText().isEmpty()) {
                tfCmpUserCreteUsername.requestFocus();
            } else {
                functionToValidateUser(event);
            }
        }
    }

    //function for email validation @
    private boolean validEmail(String email) {
        String emailReg = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailReg);
    }

    //function for get node for user role permissions
    public Node getNodeByRowColumnIndex(int row, int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if (gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }

    private void setGridHeaders() {
        gridPane.getChildren().clear();
        gridPane.getColumnConstraints().clear();
        JsonObject jsonObject = new Gson().fromJson(accessPerm, JsonObject.class);
        JsonArray array = jsonObject.get("list").getAsJsonArray();
        for (int i = 0; i < array.size(); i++) {
            if (i == 0) {
                ColumnConstraints columnConstraints = new ColumnConstraints();
                columnConstraints.setMinWidth(250);
                gridPane.getColumnConstraints().add(columnConstraints);
            } else {
                ColumnConstraints columnConstraints = new ColumnConstraints();
                columnConstraints.setMinWidth(100);
                gridPane.getColumnConstraints().add(columnConstraints);
            }
            JsonObject obj = array.get(i).getAsJsonObject();
            Label label = new Label();
            label.setText(obj.get("name").getAsString());
            label.setFont(font);
            gridPane.add(label, i + 1, 0);
        }
    }

    public void companyName() {           //function for display company name
        try {
            comCreateLogger.info("Start API calling---> getCompanyName");
            HttpResponse<String> response = APIClient.getRequest(EndPoints.getCompanyName);
            String data = response.body();
            JsonObject companyData = new Gson().fromJson(data, JsonObject.class);
            ObservableList<CompanyUserModel> list = FXCollections.observableArrayList();
            if (companyData.get("responseStatus").getAsInt() == 200) {
                JsonArray array = companyData.get("responseObject").getAsJsonArray();
                JsonObject jsonObject = array.get(0).getAsJsonObject();
                String cName = "";
                System.out.println("obj in getIdOfCompany " + jsonObject);
                cName = jsonObject.get("companyName").getAsString();
                companyId = Long.valueOf(jsonObject.get("id").getAsString());
                list.add(new CompanyUserModel(cName, String.valueOf(companyId)));

                cmbCmpuserCreateCmpName.setItems(list);
                cmbCmpuserCreateCmpName.getSelectionModel().selectFirst();
                cmbCmpuserCreateCmpName.setConverter(new StringConverter<CompanyUserModel>() {
                    @Override
                    public String toString(CompanyUserModel name) {
                        return (name != null) ? name.getCompanyName() : "";
                    }

                    @Override
                    public CompanyUserModel fromString(String s) {
                        return null;
                    }
                });

            }
        } catch (Exception e) {
            comCreateLogger.error("Exception in company name " + e.getMessage());
        }
        comCreateLogger.info("END API calling---> getCompanyName");

    }

    //    private void getCompanyAdminUsers() {
//        APIClient apiClient=new APIClient(EndPoints.GET_COMPANIES_SUPER_ADMIN,null, RequestType.GET);
//        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
//            @Override
//            public void handle(WorkerStateEvent event) {
//                if(event.getSource().getValue()!=null){
//                    String resBody=event.getSource().getValue().toString();
//                    JsonObject jsonObject = new Gson().fromJson(resBody, JsonObject.class);
//                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
//                        JsonArray jsonArray = jsonObject.get("responseObject").getAsJsonArray();
//                        JsonObject jObject = jsonArray.get(0).getAsJsonObject();
//                        String cmpName = jObject.get("companyName").getAsString();
//                        cmbCmpuserCreateCmpName.add(cmpName);
//                        cmpId = jObject.get("id").getAsLong();
//                        cmbCmpAdmCompanyName.setItems(FXCollections.observableList(cmpNameList));
//                        cmbCmpAdmCompanyName.getSelectionModel().selectFirst();
//
//                    }
//                }else{
//
//                }
//
//            }
//        });
//        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
//            @Override
//            public void handle(WorkerStateEvent event) {
//
//            }
//        });
//        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
//            @Override
//            public void handle(WorkerStateEvent event) {
//
//            }
//        });
//        apiClient.start();
//
//    }
    //function for validate the user
    public void functionToValidateUser(KeyEvent event) {
        APIClient apiClient = null;
        try {

            Map<String, String> map = new HashMap<>();
            map.put("userCode", tfCmpUserCreteUsername.getText());
            String formData = Globals.mapToStringforFormData(map);
            System.out.println("formData of validate comp userr.. " + formData);
            apiClient = new APIClient(EndPoints.VALIDATE_COMPANY_USER, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    String resBody = workerStateEvent.getSource().getValue().toString();
                    JsonObject jsonObject = new Gson().fromJson(resBody, JsonObject.class);

                    if (jsonObject.get("responseStatus").getAsInt() == 409) {
                        String message = jsonObject.get("message").getAsString();

                        AlertUtility.AlertError("WARNING", message, input -> {
                            if (input == 1) {

                                tfCmpUserCreteUsername.requestFocus();

                            } else tfCmpUserCreteUsername.requestFocus();
                        });
                    } else {
                        if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                            rbCmpUserCreateFemale.requestFocus();
                        } else {
                            tfCmpUserCreatePassword.requestFocus();
                        }

                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    comCreateLogger.error("Network API cancelled in functionCompanyDuplicate()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    comCreateLogger.error("Network API failed in functionCompanyDuplicate()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO : use logger for alert messages
            AlertUtility.AlertDialogForError("WARNING", "Failed to Connect Server !", input -> {
                if (input) {
                }
            });
        } finally {
            apiClient = null;
        }
    }

    public String accessData() {       //function for access permission label (create, edit, delete,...)
        HttpResponse<String> response = APIClient.getRequest(EndPoints.userRoleAccessData);
        String data = response != null ? response.body() : "";
        return data;
    }

    public JsonObject rolePermissions(Long id) {     //function for rolePermission when we select the role
        Map<String, String> map = new HashMap<>();

        map.put("id", id.toString());
        String formData = Globals.mapToStringforFormData(map);
        System.out.println("formData " + formData);
        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.getUserRolePermissions);
        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
        try {

            permDataArray = new Gson().fromJson(responseBody, JsonObject.class).get("level").getAsJsonArray();
            System.out.println("permDataArrayyy " + permDataArray);
            if (responseBody.get("responseStatus").getAsInt() == 200) {       //for the role permissions when we select user role
//                gridPane.getChildren().clear();
                setGridHeaders();
                Label perticulars = new Label();
                perticulars.setText("Particulars");
                font = Font.font(Font.getDefault().getFamily(), FontWeight.NORMAL, FontPosture.REGULAR, 16);
                perticulars.setFont(font);
                gridPane.add(perticulars, 0, 0);

                for (int row = 0; row < permDataArray.size(); row++) {
                    JsonObject jsonObject1 = permDataArray.get(row).getAsJsonObject();
                    JsonObject levelObject = jsonObject1.get("level").getAsJsonObject();
                    JsonArray cbs = levelObject.get("actions").getAsJsonArray();
//                    System.out.println("cbsss "+cbs);
                    int rowIndex = row + 1;
                    CheckBox checkBox = new CheckBox();
                    checkBox.setText(levelObject.get("name").getAsString());
                    checkBox.selectedProperty().addListener((ObservableValue<? extends Boolean> obserValue, Boolean oldValue, Boolean newValue) -> {
                        System.out.println("obserValue " + obserValue + " oldValue " + oldValue + " newValue " + newValue);
                        setCheckedSelectedRow(newValue, rowIndex);
                    });


                    gridPane.add(checkBox, 0, rowIndex);
                    for (int k = 1; k < 8; k++) {
                        CheckBox checkBox1 = new CheckBox();
//                        if(checkBox1.isFocused()){
//                            System.out.println("is focus true33");
//                            checkBox1.setStyle("-fx-background-color: #fff9c4; -fx-border-color: #00a0f5;");
//                        }
                        for (int s = 0; s < cbs.size(); s++) {
                            if (k == cbs.get(s).getAsInt()) {
                                checkBox1.setSelected(true);
                            }
                        }
                        gridPane.add(checkBox1, k, rowIndex);
                    }
                    System.out.println("Before Size : " + gridPane.getChildren().size());

//                    int index= 1;
//                    for (int s = 0; s < cbs.size(); s++) {
//                        int cb = cbs.get(s).getAsInt();
//                        while (index<cb){
//                            CheckBox checkBox1=new CheckBox();
//                            gridPane.add(checkBox1,index,rowIndex);
//                            index++;
//                        }
////                        int colIndex = cbs.get(s).getAsInt();
//                        //Node node = getNodeByRowColumnIndex(rowIndex, colIndex, gridPane);
////                        System.out.println("cbbb "+cb);
////                        int rowindex = row + 1;
//                        CheckBox checkBox1 = new CheckBox();
//                        checkBox1.setSelected(true);
//                        gridPane.add(checkBox1, cb, rowIndex);
//                    }
                }

                System.out.println("GPSize : " + gridPane.getChildren().size());
                int size = gridPane.getChildren().size();
                int counter = 0;
                for (int i = 0; i < size; i++) {
                    Node node = gridPane.getChildren().get(i);
                    Node nextNode = i + 1 < size ? gridPane.getChildren().get(i + 1) : null;
                    if (node instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) node;
                        if (!checkBox.getText().isEmpty()) {
                            counter = 0;
                            System.out.println(i + " Label : " + checkBox.getText() + " counter : " + counter);

                        } else {
                            if (checkBox.isSelected())
                                counter++;
                            System.out.println(i + "No Lable counter : " + counter);
                            if (counter == 7) {
                                Node node1 = gridPane.getChildren().get(i - 7);
                                CheckBox checkBox1 = (CheckBox) node1;
                                checkBox1.setSelected(true);
                            }
                        }
                    }
                }
            }

            /*if(cbs.size()==7){
//                        gridPane.getChildren().size();
                        System.out.println("All Checked : "+rowIndex);
                        CheckBox checkBox1 = new CheckBox();
                        checkBox1.setSelected(true);
                        checkBox1.selectedProperty().addListener((ObservableValue<? extends Boolean> obserValue, Boolean oldValue, Boolean newValue) -> {
                            System.out.println("obserValue "+obserValue+" oldValue "+oldValue+" newValue "+newValue);
                            setCheckedSelectedRow(newValue,rowIndex);
                        });

                        gridPane.add(checkBox1,0,rowIndex);
                    }*/

        } catch (Exception e) {
            comCreateLogger.error("exception " + e.getMessage());
        }
        return responseBody;

    }

    public void roleList() {        //function for User role lists
        try {
            HttpResponse<String> response = APIClient.getRequest(EndPoints.getUserRolePermList);
            String data = response.body();
            JsonObject roleData = new Gson().fromJson(data, JsonObject.class);
            ObservableList<CompanyUserModel> list = FXCollections.observableArrayList();
            if (roleData.get("responseStatus").getAsInt() == 200) {
                JsonArray roleArray = roleData.get("responseObject").getAsJsonArray();
                for (JsonElement elmnt : roleArray) {
                    JsonObject element = elmnt.getAsJsonObject();
                    String rolName = element.get("roleName").getAsString();
                    int rolId = element.get("id").getAsInt();
                    list.add(new CompanyUserModel(rolId, rolName));
                }
                cmbCmpuserCreateRoleName.setItems(list);
                cmbCmpuserCreateRoleName.setConverter(new StringConverter<CompanyUserModel>() {
                    @Override
                    public String toString(CompanyUserModel name) {
                        return (name != null) ? name.getRoleName() : "";
                    }

                    @Override
                    public CompanyUserModel fromString(String s) {
                        return null;
                    }
                });
            }

        } catch (Exception e) {
            comCreateLogger.error("exception  " + e.getMessage());
        }

    }

    private void setCheckedSelectedRow(Boolean flag, int rowIndex) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof CheckBox) {
                if (GridPane.getRowIndex(node) == rowIndex) {
                    if (GridPane.getColumnIndex(node) != 0) {
                        CheckBox ckbox = (CheckBox) node;
                        ckbox.setSelected(flag);
                    }
                }
            }
        }
    }


    public void createCompanyUser() throws IOException {      //method for create and update the company user
        try {

            System.out.println("Size : " + gridPane.getChildren().size());
            jsonArray = new JsonArray();
            for (Node node : gridPane.getChildren()) {        //starting of code for how many role permissions
                if (node instanceof CheckBox) {
                    int rowIndex = GridPane.getRowIndex(node);
                    int columnIndex = GridPane.getColumnIndex(node);
                    CheckBox checkBox = (CheckBox) node;

                    if (columnIndex > 0 && checkBox.isSelected()) {
                        boolean record = false;
                        for (int i = 0; i < jsonArray.size(); i++) {
//                         System.out.println("jsonArrayy--- "+jsonArray);
                            JsonObject obj = jsonArray.get(i).getAsJsonObject();
                            if (obj.get("mapping_id").getAsInt() == rowIndex) {
                                jsonArray.get(i).getAsJsonObject().get("actions").getAsJsonArray().add((columnIndex) + "");
                                record = true;
                            }
                        }
                        if (!record) {
                            JsonObject jsonObject = new JsonObject();
                            JsonArray actions = new JsonArray();
                            JsonObject mappingData = permDataArray.get(rowIndex - 1).getAsJsonObject().get("level").getAsJsonObject();
                            jsonObject.addProperty("mapping_id", mappingData.get("id").getAsInt());
                            jsonObject.addProperty("mapping_name", mappingData.get("name").getAsString());
                            actions.add((columnIndex) + "");
                            jsonObject.add("actions", actions);
                            jsonArray.add(jsonObject);

                        }
                    }
                } else {
                    System.out.println("no node found " + jsonArray);
                }
            }                                                    //end of code for how many role permissions selected
            String finalPerm = new Gson().toJson(jsonArray);
            String id = Globals.companyUserModelDTO != null ? Globals.companyUserModelDTO.getId() : "";
            Map<String, String> map = new HashMap<>();
            if (id != "") {
                map.put("id", id);
            }
            if (tfCmpUserCreateFullName.getText().isEmpty()) {
                CommonValidationsUtils.highlightTextField(tfCmpUserCreateFullName);
            } else {
                CommonValidationsUtils.removeHighlight(tfCmpUserCreateFullName);
            }
            if (tfCmpUserCreteUsername.getText().isEmpty()) {
                CommonValidationsUtils.highlightTextField(tfCmpUserCreteUsername);
            } else CommonValidationsUtils.removeHighlight(tfCmpUserCreteUsername);
            if (tfCmpUserCreatePassword.getText().isEmpty()) {
                CommonValidationsUtils.highlightTextField(tfCmpUserCreatePassword);
            } else CommonValidationsUtils.removeHighlight(tfCmpUserCreatePassword);

            map.put("fullName", tfCmpUserCreateFullName.getText());
            map.put("mobileNumber", tfCmpUserCreateMobileNo.getText() != null ? tfCmpUserCreateMobileNo.getText() : "");
            map.put("email", tfCmpUserCreateEmail.getText() != null ? tfCmpUserCreateEmail.getText() : "");
            if (rbCmpUserCreateMale.isSelected()) {
                map.put("gender", rbCmpUserCreateMale.getText());
            } else if (rbCmpUserCreateFemale.isSelected()) {
                map.put("gender", rbCmpUserCreateFemale.getText());
            }
            map.put("usercode", tfCmpUserCreteUsername.getText());
            map.put("password", tfCmpUserCreatePassword.getText());
            map.put("userRole", "USER");
            map.put("roleId", userRoleId.toString());
//         System.out.println("company id in create API "+companyId);
            map.put("companyId", companyId.toString());
            map.put("user_permissions", finalPerm);
//         System.out.println("mapppp "+map);
            HttpResponse<String> response;
            String req = Globals.mapToStringforFormData(map);
            if (!tfCmpUserCreatePassword.getText().isEmpty() && !tfCmpUserCreateFullName.getText().isEmpty() && !tfCmpUserCreteUsername.getText().isEmpty() &&
                    !cmbCmpuserCreateRoleName.getSelectionModel().isEmpty()) {
                if (Globals.companyUserModelDTO == null) {
                    System.out.println("create API called");
                    response = APIClient.postFormDataRequest(req, EndPoints.createCompanyUser);
                    //? HIGHLIGHT
                    CompanyUserController.isNewCompanyUserCreated = true; //? Set the flag for new creation
                } else {
                    System.out.println("update API called");
                    response = APIClient.postFormDataRequest(req, EndPoints.updateCompanyUser);
                    //? HIGHLIGHT
                    CompanyUserController.editedCompanyUserId = id; //? Set the ID for editing
                }
                JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                if (responseBody.get("responseStatus").getAsInt() == 200) {
                    if (id != "" && id != null) {
                        AlertUtility.AlertConfirmation("Confirm", "Do You Want To Update?", callback -> {
                            if (callback == 1) {
                                GlobalController.getInstance().addTabStatic(FxmFileConstants.COM_USER_LIST_SLUG, false);
//                                CompanyUserController.focusId = selectedFocusId;
                            } else btnCmpUserCreateSubmit.requestFocus();
                        });
                    } else {
                        //code for create new user
                        AlertUtility.AlertConfirmation("Confirm", "Do You Want To Submit?", callback -> {

                            if (callback == 1) {
                                GlobalController.getInstance().addTabStatic(FxmFileConstants.COM_USER_LIST_SLUG, false);
//                                CompanyUserController.focusStatus = "create";
                            } else btnCmpUserCreateSubmit.requestFocus();
                        });
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(responseBody.get("message").getAsString());
                }
            } else {
                if (tfCmpUserCreateFullName.getText().isEmpty()) {
                    AlertUtility.AlertError("Warning", "Please fill all required fields", callback -> {
                        tfCmpUserCreateFullName.requestFocus();
                    });
                } else if (tfCmpUserCreteUsername.getText().isEmpty()) {
                    AlertUtility.AlertError("Warning", "Please fill all required fields", callback -> {
                        tfCmpUserCreteUsername.requestFocus();
                    });
                } else if (tfCmpUserCreatePassword.getText().isEmpty()) {
                    AlertUtility.AlertError("Warning", "Please fill all required fields", callback -> {
                        tfCmpUserCreatePassword.requestFocus();
                    });
                }

            }

        } catch (Exception e) {
            comCreateLogger.error("error " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
        }
    }

    //function for cancel button
    public void alertBox() {
        AlertUtility.AlertConfirmation("Confirm", "Do You Want To Cancel?", callback -> {

            if (callback == 1) {
                GlobalController.getInstance().addTabStatic(FxmFileConstants.COM_USER_LIST_SLUG, false);
//                CompanyUserController.focusId = selectedFocusId;
            } else btnCmpUserCreateCancel.requestFocus();
        });

    }

    public void getEditDataById() {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("id", Globals.companyUserModelDTO.getId());
            String formData = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.getEditDataOfCompanyUser);
            System.out.println("Get By Id Res => " + response.body());
            JsonObject respBody = new Gson().fromJson(response.body(), JsonObject.class);
            JsonObject obj = respBody.get("responseObject").getAsJsonObject();
            companyId = obj.get("companyId").getAsLong();
            System.out.println("obj in edit " + obj);
            JsonArray permissionArray = obj.get("permissions").getAsJsonArray();
            System.out.println("permissionArrayyyy111 " + permissionArray + "  sizeee " + permissionArray.size());
            updateRoleId = obj.get("roleId").getAsLong();
            if (respBody.get("responseStatus").getAsInt() == 200) {

                cmbCmpuserCreateCmpName.setValue(obj.get("companyName").getAsString());
                tfCmpUserCreateFullName.setText(obj.get("fullName").getAsString());
                tfCmpUserCreateMobileNo.setText(obj.get("mobileNumber").getAsString());
                tfCmpUserCreateEmail.setText(obj.get("email").getAsString());
                tfCmpUserCreteUsername.setText(obj.get("usercode").getAsString());
                tfCmpUserCreatePassword.setText(obj.get("password").getAsString());

                if (obj.get("gender").getAsString().equalsIgnoreCase("Male")) {
                    rbCmpUserCreateMale.setSelected(true);
                } else {
                    rbCmpUserCreateFemale.setSelected(true);
                }
                //rolePermissions(updateRoleId);
                setRole(obj.get("roleId").getAsInt());
                //**************start
                //for the role permissions when we select user role
                gridPane.getChildren().clear();
                setGridHeaders();

                System.out.println("PermArraySIze=>" + permissionArray.size());
                for (int row = 0; row < permissionArray.size(); row++) {
                    JsonObject jsonObject1 = permissionArray.get(row).getAsJsonObject();
                    System.out.println("jsonObject111 " + jsonObject1);
//                    JsonObject levelObject = jsonObject1.get("level").getAsJsonObject();

                    JsonArray cbs = jsonObject1.get("actions").getAsJsonArray();
//                    System.out.println("cbsss "+cbs);
                    int rowIndex = row + 1;
                    CheckBox checkBox = new CheckBox();
                    checkBox.setText(jsonObject1.get("name").getAsString());

                    checkBox.selectedProperty().addListener((ObservableValue<? extends Boolean> obserValue, Boolean oldValue, Boolean newValue) -> {
                        setCheckedSelectedRow(newValue, rowIndex);
                    });
                    Label perticulars = new Label();
                    perticulars.setText("Particulars");
                    font = Font.font(Font.getDefault().getFamily(), FontWeight.NORMAL, FontPosture.REGULAR, 16);
                    perticulars.setFont(font);
                    gridPane.add(perticulars, 0, 0);

                    gridPane.add(checkBox, 0, rowIndex);
                    for (int k = 1; k < 8; k++) {
                        CheckBox checkBox1 = new CheckBox();
                        for (int s = 0; s < cbs.size(); s++) {
                            if (k == cbs.get(s).getAsInt()) {
                                checkBox1.setSelected(true);
                            }
                        }
                        gridPane.add(checkBox1, k, rowIndex);
                    }

                    if (cbs.size() == 7) {
                        checkBox.setSelected(true);
                    }
                }
                //*************** End
            }
        } catch (Exception e) {
            comCreateLogger.error("exception " + e.getMessage());
        }
    }

    public void setRole(Integer RoleId) {        //function for User role lists
        try {
            HttpResponse<String> response = APIClient.getRequest(EndPoints.getUserRolePermList);
            String data = response.body();
            JsonObject roleData = new Gson().fromJson(data, JsonObject.class);
            ObservableList<CompanyUserModel> list = FXCollections.observableArrayList();
            if (roleData.get("responseStatus").getAsInt() == 200) {
                JsonArray roleArray = roleData.get("responseObject").getAsJsonArray();
                for (JsonElement elmnt : roleArray) {
                    JsonObject element = elmnt.getAsJsonObject();
                    String rolName = element.get("roleName").getAsString();
                    int rolId = element.get("id").getAsInt();
                    if (RoleId.equals(rolId)) {
                        CompanyUserModel userModel = new CompanyUserModel(rolId, rolName);
                        cmbCmpuserCreateRoleName.getSelectionModel().select(userModel);
                    }

                }
                cmbCmpuserCreateRoleName.setConverter(new StringConverter<CompanyUserModel>() {
                    @Override
                    public String toString(CompanyUserModel RoleList) {
                        return RoleList.getRoleName();
                    }

                    @Override
                    public CompanyUserModel fromString(String s) {
                        return null;
                    }
                });
            }

        } catch (Exception e) {
            comCreateLogger.error("exception  " + e.getMessage());
        }

    }
}
