package com.opethic.genivis.controller.master;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.commons.SwitchButton;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Callback;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.*;

import static com.opethic.genivis.utils.FxmFileConstants.FRANCHISE_LIST_SLUG;
import static com.opethic.genivis.utils.FxmFileConstants.ROLE_UPDATE_SLUG;
import static com.opethic.genivis.utils.Globals.userRoleModel;

public class CompanyAdminController implements Initializable {
    @FXML
    private ComboBox cmbCmpAdmCompanyName;
    private String responseBody;
    private List<String> cmpNameList = new ArrayList<>();

    @FXML
    private BorderPane spRootPane;
    @FXML
    private VBox vbCmpAdmHeaderSect;
    @FXML
    private HBox hbCmpAdmHeader1Sect, hbCmpAdmHeader2Sect;
    @FXML
    private TextField tfCmpAdmFullName;
    @FXML
    private TextField tfCmpAdmMobileNo;
    @FXML
    private TextField tfCmpAdmEmail;
    @FXML
    private TextField tfCmpAdmUsername;
    @FXML
    private PasswordField tfCmpAdmPassword;
    @FXML
    private RadioButton rbCmpAdmGenderMale;
    @FXML
    private RadioButton rbCmpAdmGenderFemale;
    @FXML
    private Button btnCmpAdmSubmit, btnCmpAdmClear, hideShow;
    @FXML
    private TextField tfCmpAdmSearchCmpAdm, tfPasswordInText;

    @FXML
    private TableView<CompanyAdminDTO> tableView;

    @FXML
    private TableColumn<CompanyAdminDTO, Number> tblcCmpAdmSrNo;

    @FXML
    private TableColumn<CompanyAdminDTO, String> tblcCmpAdmCompanyName;

    @FXML
    private TableColumn<CompanyAdminDTO, String> tblcCmpAdmFullName;
    @FXML
    private TableColumn<CompanyAdminDTO, String> tblcCmpAdmMobileNo;
    @FXML
    private TableColumn<CompanyAdminDTO, String> tblcCmpAdmEmail;
    @FXML
    private TableColumn<CompanyAdminDTO, String> tblcCmpAdmGender;
    @FXML
    private TableColumn<CompanyAdminDTO, String> tblcCmpAdmUsername;
    @FXML
    private TableColumn<CompanyAdminDTO, String> tblcCmpAdmAction;
    private Long cmpId = 0L;
    private ObservableList<StringProperty> row;
    private ObservableList<CompanyAdminDTO> originalData;
    private String selectedId;
    private Integer rowIndex;
    private ToggleGroup toggleGroup = new ToggleGroup();

    private String focusInd = "";
    // Array to hold focusable nodes
    private Node[] focusableNodes;
    private static final Logger companyAdminlogger = LogManager.getLogger(CompanyAdminController.class);

    public static Boolean isFirst = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        // Creating the Toggle Group
        rbCmpAdmGenderMale.setToggleGroup(toggleGroup);
        rbCmpAdmGenderFemale.setToggleGroup(toggleGroup);

        CommonFunctionalUtils.commonInit(tfCmpAdmPassword, tfPasswordInText, hideShow);

        // Initial Enter Functionality Method
        initialEnterMethod();

        // Inital Methods Call like  List of company Admins, Combobox Admin Users, shortcutKeysCmpAdm
        initialMethods();

        // Table Responsive Function
        companyAdminListTableDesign();

        // Add a key event listener to the HBOX
        vbCmpAdmHeaderSect.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN) {
                tfCmpAdmSearchCmpAdm.requestFocus();
                event.consume();
            }
        });

        // Add a key event listener to the text field
        tfCmpAdmSearchCmpAdm.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN) {
                tableView.getSelectionModel().selectFirst();
                tableView.requestFocus();
                event.consume();
            }
        });


        // Add a key event listener to the text field
        btnCmpAdmSubmit.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                btnCmpAdmClear.requestFocus();
                event.consume();
            }
        });

        // Search Functionality
        searchFunctionality();

        // OnDouble-click values set to textField and GetById Api call
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
              rowIndex = tableView.getSelectionModel().getSelectedIndex();
                CompanyAdminDTO selectedItem = (CompanyAdminDTO) tableView.getSelectionModel().getSelectedItem();
                selectedId = selectedItem.getId();
                get_company_admin_by_id(selectedId);
            }
        });

        // Add a key event listener to the TableView
        tableView.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!tableView.getSelectionModel().isEmpty()) {
                    rowIndex = tableView.getSelectionModel().getSelectedIndex();
                    CompanyAdminDTO selectedItem = (CompanyAdminDTO) tableView.getSelectionModel().getSelectedItem();
                    selectedId = selectedItem.getId();
                    get_company_admin_by_id(selectedId);
                    event.consume();
                }
            }
        });

        // Add listener for focus change to restrict Mobile Number and Email when cursor moves away
        CommonValidationsUtils.restrictMobileNumber(tfCmpAdmMobileNo);
        CommonValidationsUtils.restrictEmail(tfCmpAdmEmail);

        btnCmpAdmClear.setOnAction(e -> {
            clearFields();
        });


//        spRootPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Textfield Focus Validation
        tfuserNamePassValidation();

        // CreateUpdate Functionality
        createUpdateFunctionality();

        hideShow.setOnAction(e -> {
            passwordBtn();
        });


    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            spRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            spRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            spRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            spRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            spRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            spRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            spRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    public void passwordBtn() {
        CommonFunctionalUtils.passwordField(tfCmpAdmPassword, tfPasswordInText, hideShow);
    }

    //Initial Enter Functionality Method
    private void initialEnterMethod() {
        //? It will work only on radio button
        spRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof RadioButton) {
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
        });

        spRootPane.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("modify")) {
                    //System.out.println(targetButton.getText());
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("submit")) {
                    // System.out.println(targetButton.getText());
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("cancel")) {
                    // System.out.println(targetButton.getText());
                } else if (event.getTarget() instanceof ToggleButton targetButton) {
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
                } else {
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
        });
    }

    // Initial Methods Call like FetchdataofAllCompany, getCompanyAdminUsers, shortcutKeysCmpAdm
    private void initialMethods() {
        Platform.runLater(() -> {
            // List of all Company Admins Api call
            fetchDataOfAllCompany();
            // Combobox Options Api Call
            getCompanyAdminUsers();

            //shortKeys Method
            shortcutKeysCmpAdm();

            cmbCmpAdmCompanyName.setEditable(false);
            cmbCmpAdmCompanyName.setFocusTraversable(false);
            tfCmpAdmFullName.requestFocus();
        });

    }

    // Textfield Focus Validation
    public void tfuserNamePassValidation() {
        tfCmpAdmUsername.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfCmpAdmUsername.getText().isEmpty()) {
                    tfCmpAdmUsername.requestFocus();
                } else {
                    String btText = btnCmpAdmSubmit.getText();
                    if (btText.equalsIgnoreCase("Submit")) {
                        cmpAdmValidateUser();
                    } else {
                        cmpAdmUpdateValidateUser();
                    }
                }

            }
        });
        tfCmpAdmPassword.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfCmpAdmPassword.getText().isEmpty()) {
                    tfCmpAdmPassword.requestFocus();
                }

            }
        });
    }

    // createUpdate Functionality
    private void createUpdateFunctionality() {
        btnCmpAdmSubmit.setOnAction(e -> {
            if (CommonValidationsUtils.validateForm(tfCmpAdmUsername, tfCmpAdmPassword)) {

                if (btnCmpAdmSubmit.getText().equalsIgnoreCase("submit")) {
                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Submit", input -> {
                        if (input == 1) {
                            createUpdateCompanyAdmin("Create");
                        }
                    });
                } else {
                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Update", input -> {
                        if (input == 1) {
                            createUpdateCompanyAdmin("Update");
//                            Platform.runLater(()->{
//                                tableView.getSelectionModel().select(Integer.parseInt(selectedId));
//                                tableView.scrollTo(Integer.parseInt(selectedId));
//                                tableView.requestFocus();
//                           } );

                        }
                    });
                }
            }
        });
    }

    // search Functionality
    private void searchFunctionality() {
        tfCmpAdmSearchCmpAdm.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<CompanyAdminDTO> resultFilteredData = FXCollections.observableArrayList();
            if (newValue.isEmpty()) {
                tableView.setItems(originalData);
            } else {
                for (int i = 0; i < originalData.size(); i++) {
                    if (originalData.get(i).getCompanyName().toLowerCase().contains(newValue.toLowerCase()) ||
                            originalData.get(i).getFullName().contains(newValue) ||
                            originalData.get(i).getGender().toLowerCase().contains(newValue.toLowerCase()) ||
                            originalData.get(i).getEmail().contains(newValue) ||
                            originalData.get(i).getMobileNumber().toLowerCase().contains(newValue.toLowerCase()) ||
                            originalData.get(i).getUsercode().toLowerCase().contains(newValue.toLowerCase())
                    ) {
                        resultFilteredData.add((CompanyAdminDTO) originalData.get(i));
                    }

                }
                tableView.setItems(resultFilteredData);
            }
        });
    }

    // clear Fields
    private void clearFields() {
        tfCmpAdmFullName.setText("");
        tfCmpAdmUsername.setText("");
        tfCmpAdmMobileNo.setText("");
        tfCmpAdmEmail.setText("");
        tfCmpAdmPassword.setText("");
        rbCmpAdmGenderMale.setSelected(false);
        rbCmpAdmGenderFemale.setSelected(false);
        btnCmpAdmSubmit.setText("Submit");
    }

    private void getCompanyAdminUsers() {
        APIClient apiClient = new APIClient(EndPoints.GET_COMPANIES_SUPER_ADMIN, null, RequestType.GET);
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (event.getSource().getValue() != null) {
                    String resBody = event.getSource().getValue().toString();
                    JsonObject jsonObject = new Gson().fromJson(resBody, JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray jsonArray = jsonObject.get("responseObject").getAsJsonArray();
                        JsonObject jObject = jsonArray.get(0).getAsJsonObject();
                        String cmpName = jObject.get("companyName").getAsString();
                        cmpNameList.add(cmpName);
                        cmpId = jObject.get("id").getAsLong();
                        cmbCmpAdmCompanyName.setItems(FXCollections.observableList(cmpNameList));
                        cmbCmpAdmCompanyName.getSelectionModel().selectFirst();

                    }
                } else {

                }

            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {

            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {

            }
        });
        apiClient.start();

    }

    public void companyAdminListTableDesign() {
        tblcCmpAdmSrNo.prefWidthProperty().bind(tableView.widthProperty().multiply(0.05));
        tblcCmpAdmCompanyName.prefWidthProperty().bind(tableView.widthProperty().multiply(0.16));
        tblcCmpAdmFullName.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
        tblcCmpAdmMobileNo.prefWidthProperty().bind(tableView.widthProperty().multiply(0.12));
        tblcCmpAdmEmail.prefWidthProperty().bind(tableView.widthProperty().multiply(0.16));
        tblcCmpAdmGender.prefWidthProperty().bind(tableView.widthProperty().multiply(0.08));
        tblcCmpAdmUsername.prefWidthProperty().bind(tableView.widthProperty().multiply(0.12));
        tblcCmpAdmAction.prefWidthProperty().bind(tableView.widthProperty().multiply(0.05));
    }


    private void fetchDataOfAllCompany() {

        APIClient apiClient = new APIClient(EndPoints.GET_C_ADMINS, null, RequestType.GET);
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (event.getSource().getValue() != null) {
                    String resBody = event.getSource().getValue().toString();
                    try {
                        tableView.getItems().clear();
                        JsonObject jsonObject = new Gson().fromJson(resBody, JsonObject.class);

                        ObservableList<CompanyAdminDTO> observableList = FXCollections.observableArrayList();
                        if (jsonObject.get("responseStatus").getAsInt() == 200) {
                            JsonArray responseObject = jsonObject.getAsJsonArray("responseObject");
                            for (JsonElement element : responseObject) {
                                JsonObject item = element.getAsJsonObject();
                                String CmpAdmSrNo = item.get("id").getAsString();
                                String cmbCmpAdmCompanyName = item.get("companyName").getAsString();
                                String tfCmpAdmFullName = item.get("fullName").getAsString();
                                String tfCmpAdmMobileNo = item.get("mobileNumber").getAsString();
                                String tfCmpAdmEmail = item.get("email").getAsString();
                                String typeElement = item.get("gender").getAsString();
                                String rbCmpAdmGenderMale = null;
                                String rbCmpAdmGenderFemale = null;
                                if (typeElement == "male") {
                                    rbCmpAdmGenderMale = item.get("gender").getAsString();
                                } else {
                                    rbCmpAdmGenderFemale = item.get("gender").getAsString();
                                }
                                String tfCmpAdmUsername = item.get("usercode").getAsString();
                                String chkCmpAdmAction = item.get("isSwitch").getAsString();

                                String b = "false";
                                isFirst = true;
                                if (chkCmpAdmAction.equals("1")) {
                                    b = "true";
                                }

                                observableList.add(new CompanyAdminDTO(CmpAdmSrNo, cmbCmpAdmCompanyName, tfCmpAdmFullName, tfCmpAdmMobileNo, tfCmpAdmEmail, typeElement, tfCmpAdmUsername, b));
                            }


//                            tblcCmpAdmSrNo.setCellValueFactory(new PropertyValueFactory<>("id"));
                            // Set up the serial number column
                            tblcCmpAdmSrNo.setCellValueFactory(cellData ->
                                    new SimpleObjectProperty<>(tableView.getItems().indexOf(cellData.getValue()) + 1)
                            );
                            tblcCmpAdmCompanyName.setCellValueFactory(new PropertyValueFactory<>("companyName"));
                            tblcCmpAdmFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
                            tblcCmpAdmMobileNo.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
                            tblcCmpAdmEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
                            tblcCmpAdmGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
                            tblcCmpAdmUsername.setCellValueFactory(new PropertyValueFactory<>("usercode"));
                            tblcCmpAdmAction.setCellValueFactory(cellData -> cellData.getValue().isSwitchProperty());
                            tblcCmpAdmAction.setCellFactory(cellData -> new SwitchToogleTableCell("isSwitch"));
                            tableView.setItems(observableList);
                            originalData = tableView.getItems();

                            if (focusInd.equalsIgnoreCase("focus")) {
                                tableView.getSelectionModel().select(responseObject.size() - 1);
                                tableView.scrollTo(responseObject.size() - 1);
                                tableView.requestFocus();
                                focusInd = "";
                            } else if (focusInd.equalsIgnoreCase("update")) {
                                tableView.getSelectionModel().select(rowIndex);
                                tableView.scrollTo(rowIndex);
                                tableView.requestFocus();
                                focusInd = "";
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        companyAdminlogger.debug("Erorr in GET_C_ADMINS : " + e.toString());
                    }
                } else {
                    companyAdminlogger.debug("Erorr in GET_C_ADMINS : API Response is null");
                }

            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                companyAdminlogger.debug("Erorr in GET_C_ADMINS onCancel : " + event.getSource().getValue().toString());
            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                companyAdminlogger.debug("Erorr in GET_C_ADMINS onFail : " + event.getSource().getValue().toString());
            }
        });
        apiClient.start();


    }


    private void createUpdateCompanyAdmin(String key) {

        String fullName = !tfCmpAdmFullName.getText().isEmpty() ? tfCmpAdmFullName.getText() : "";
        String mobileNo = !tfCmpAdmMobileNo.getText().isEmpty() ? tfCmpAdmMobileNo.getText() : "";
        String email = !tfCmpAdmEmail.getText().isEmpty() ? tfCmpAdmEmail.getText() : "";
        String userName = tfCmpAdmUsername.getText();
        String password = tfCmpAdmPassword.getText();
        String gender;

        HttpResponse<String> response = null;
        String message = "";


        Map<String, String> map = new HashMap<>();
        if (key.equalsIgnoreCase("Update")) {
            map.put("id", selectedId);
        }

        if (!tfCmpAdmFullName.getText().isEmpty()) {
            map.put("fullName", fullName);
        }
        if (!tfCmpAdmMobileNo.getText().isEmpty()) {
            map.put("mobileNumber", mobileNo);
        }
        if (!tfCmpAdmEmail.getText().isEmpty()) {
            map.put("email", email);
        }
        if (toggleGroup.getSelectedToggle() != null) {
            RadioButton rb = (RadioButton) toggleGroup.getSelectedToggle();
            gender = rb.getText();
            map.put("gender", gender);
        }
        map.put("usercode", userName);
        map.put("password", password);
        map.put("userRole", "CADMIN");
        map.put("companyId", String.valueOf(cmpId));

        String formData = Globals.mapToStringforFormData(map);
        APIClient apiClient;
        if (key.equalsIgnoreCase("update")) {
            apiClient = new APIClient(EndPoints.UPDATE_USER, formData, RequestType.FORM_DATA);
        } else {
            apiClient = new APIClient(EndPoints.REGISTER_USER, formData, RequestType.FORM_DATA);
        }

        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (event.getSource().getValue() != null) {
                    JsonObject responseBody = new Gson().fromJson(event.getSource().getValue().toString(), JsonObject.class);

                    if (responseBody.get("responseStatus").getAsInt() == 200) {
                        AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, responseBody.get("message").getAsString(), input -> {
                            if (key.equalsIgnoreCase("update")) {
                                btnCmpAdmSubmit.setText("Submit");
                                focusInd = "update";
                            } else {
                                focusInd = "focus";
                            }

                            tfCmpAdmFullName.setText("");
                            tfCmpAdmUsername.setText("");
                            tfCmpAdmMobileNo.setText("");
                            tfCmpAdmEmail.setText("");
                            tfCmpAdmPassword.setText("");
                            rbCmpAdmGenderMale.setSelected(false);
                            rbCmpAdmGenderFemale.setSelected(false);

                            getCompanyAdminUsers();
                            fetchDataOfAllCompany();
//                            tfCmpAdmFullName.requestFocus();
                        });

                    } else {
                        companyAdminlogger.debug("Error : " + responseBody.get("message").getAsString());
                    }
                } else {
                    companyAdminlogger.debug("API Response is null");
                }

            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                companyAdminlogger.debug("API Response onCancel : " + event.getSource().getValue());
            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                companyAdminlogger.debug("API Response onFail : " + event.getSource().getValue());
            }
        });
        apiClient.start();

    }

    public void get_company_admin_by_id(String selectedItem) {
        Map<String, String> map = new HashMap<>();
        map.put("id", selectedItem);
        String formData = Globals.mapToStringforFormData(map);
        APIClient apiClient = new APIClient(EndPoints.GET_USER_BY_ID, formData, RequestType.FORM_DATA);
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (event.getSource().getValue() != null) {
                    String resBody = event.getSource().getValue().toString();
                    JsonObject responseBody = new Gson().fromJson(resBody, JsonObject.class);
                    JsonObject responseObject = responseBody.getAsJsonObject("responseObject");
                    int id = responseObject.get("id").getAsInt();
                    String companyName = responseObject.get("companyName").getAsString();
                    String userRole = responseObject.get("userRole").getAsString();
                    String fullName = responseObject.get("fullName").getAsString();
                    String mobileNumber = responseObject.get("mobileNumber").getAsString();
                    String email = responseObject.get("email").getAsString();
                    String gender = responseObject.get("gender").getAsString();
                    String usercode = responseObject.get("usercode").getAsString();
                    String password = responseObject.get("password").getAsString();

                    if (responseBody.get("responseStatus").getAsInt() == 200) {
                        tfCmpAdmFullName.setText(fullName);
                        tfCmpAdmUsername.setText(usercode);
                        tfCmpAdmMobileNo.setText(mobileNumber);
                        tfCmpAdmEmail.setText(email);
                        tfCmpAdmPassword.setText(password);
                        cmbCmpAdmCompanyName.setValue(companyName);
                        if (gender.equalsIgnoreCase("male")) {
                            rbCmpAdmGenderMale.setSelected(true);
                        } else if (gender.equalsIgnoreCase("female")) {
                            rbCmpAdmGenderFemale.setSelected(true);
                        }
                        btnCmpAdmSubmit.setText("Update");
                        tfCmpAdmFullName.requestFocus();
                    }
                } else {
                    companyAdminlogger.debug("API response is null");
                }


            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                companyAdminlogger.debug("API response onCancel : " + event.getSource().getValue().toString());
            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                companyAdminlogger.debug("API response onFail : " + event.getSource().getValue().toString());
            }
        });
        apiClient.start();


    }

    public void cmpAdmValidateUser() {
        APIClient apiClient = null;
        try {

            Map<String, String> map = new HashMap<>();
            map.put("userCode", String.valueOf(tfCmpAdmUsername.getText()));
            String formData = Globals.mapToStringforFormData(map);
            apiClient = new APIClient(EndPoints.VALIDATE_COMPANY_USER, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() != 200) {
                        String message = jsonObject.get("message").getAsString();
                        AlertUtility.AlertWarningTimeout3(AlertUtility.alertTypeError, message, in -> {
                            tfCmpAdmUsername.requestFocus();
                        });
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    companyAdminlogger.error("Network API cancelled in functionCompanyDuplicate()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    companyAdminlogger.error("Network API failed in functionCompanyDuplicate()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO : use logger for alert messages
//            showAlert("Falied to connect server! ");
            AlertUtility.AlertDialogForError("WARNING", "Failed to Connect Server !", input -> {
                if (input) {
                }
            });
        } finally {
            apiClient = null;
        }
    }

    public void cmpAdmUpdateValidateUser() {
        APIClient apiClient = null;
        try {

            Map<String, String> map = new HashMap<>();
            map.put("id", selectedId);
            map.put("userCode", String.valueOf(tfCmpAdmUsername.getText()));
            String formData = Globals.mapToStringforFormData(map);
            apiClient = new APIClient(EndPoints.VALIDATE_COMPANY_USER_UPDATE, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() != 200) {
                        String message = jsonObject.get("message").getAsString();
                        AlertUtility.AlertWarningTimeout(AlertUtility.alertTypeError, message, in -> {
                            tfCmpAdmUsername.requestFocus();
                        });
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    companyAdminlogger.error("Network API cancelled in functionCompanyDuplicate()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    companyAdminlogger.error("Network API failed in functionCompanyDuplicate()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO : use logger for alert messages
//            showAlert("Falied to connect server! ");
            AlertUtility.AlertDialogForError("WARNING", "Failed to Connect Server !", input -> {
                if (input) {
                }
            });
        } finally {
            apiClient = null;
        }
    }

    private void shortcutKeysCmpAdm() {
        spRootPane.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.S && event.isControlDown()) {
                    if (CommonValidationsUtils.validateForm(tfCmpAdmUsername, tfCmpAdmPassword)) {

                        if (btnCmpAdmSubmit.getText().equalsIgnoreCase("submit")) {
                            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Submit", input -> {
                                if (input == 1) {
                                    createUpdateCompanyAdmin("Create");
                                }
                            });
                        } else {
                            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Update", input -> {
                                if (input == 1) {
                                    createUpdateCompanyAdmin("Update");
                                }
                            });
                        }
                    }
                }
                if (event.getCode() == KeyCode.E && event.isControlDown()) {
                    if (!tableView.getSelectionModel().isEmpty()) {
                        CompanyAdminDTO selectedItem = (CompanyAdminDTO) tableView.getSelectionModel().getSelectedItem();
                        selectedId = selectedItem.getId();
                        get_company_admin_by_id(selectedId);
                        event.consume();
                    } else {
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Select Company Admin", in -> {
                            tableView.getSelectionModel().selectFirst();
                            tableView.requestFocus();
                            event.consume();
                        });
                    }
                }


                if (event.getCode() == KeyCode.X && event.isControlDown()) {
                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Clear", input -> {
                        if (input == 1) {
                            clearFields();
                        }
                    });
                }

            }
        });
    }


}

class SwitchToogleTableCell extends TableCell<CompanyAdminDTO, String> {
    private SwitchButton switchButton;

    VBox vboxGvProduct;
    private String columnName;
    Boolean aBoolean = true;
    private static final Logger companyAdminlogger = LogManager.getLogger(SwitchToogleTableCell.class);

    public SwitchToogleTableCell(String columnName) {
        this.columnName = columnName;
        this.switchButton = new SwitchButton();

        this.switchButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                switchButton.switchOnProperty().set(!switchButton.switchOnProperty().get());
                CompanyAdminDTO companyAdminDTO = (CompanyAdminDTO) getTableView().getItems().get(getIndex());
                String selId = companyAdminDTO.getId();
                String isEnable = String.valueOf(switchButton.switchOnProperty().get());
                commitEdit(String.valueOf(switchButton.switchOnProperty().get()));
                userEnableDisable(selId, isEnable);
            }
        });


        switchButton.switchOnProperty().addListener((observable, oldValue, newValue) -> {
        CompanyAdminDTO companyAdminDTO = (CompanyAdminDTO) getTableView().getItems().get(getIndex());
        String selId = companyAdminDTO.getId();
        String isEnable = String.valueOf(newValue);
            if (newValue != null && CompanyAdminController.isFirst == false) {
                userEnableDisable(selId, isEnable);
            commitEdit(String.valueOf(newValue));
        }

    });

        this.vboxGvProduct = new VBox();
        vboxGvProduct.setPrefHeight(23.0);
        vboxGvProduct.setMaxHeight(23.0);
        vboxGvProduct.setMinHeight(23.0);
        vboxGvProduct.setPrefWidth(35.5);
        vboxGvProduct.setMinWidth(35.5);
        vboxGvProduct.setMaxWidth(35.5);
        vboxGvProduct.setStyle("-fx-background-color: #f8f4fc; -fx-background-radius: 4; -fx-border-radius: 4; -fx-border-color: #f8f4fc; -fx-border-width: 2;");

        switchButton.setMinWidth(32.0);
        switchButton.setMaxWidth(32.0);
        switchButton.setPrefWidth(32.0);
        vboxGvProduct.getChildren().add(switchButton);
        switchButton.setParentBox(vboxGvProduct);
    }

    public void userEnableDisable(String selectedItem, String isEnable) {
        Map<String, String> map = new HashMap<>();
        map.put("isEnable", isEnable);
        map.put("id", selectedItem);
        String formData = Globals.mapToStringforFormData(map);

        APIClient apiClient = new APIClient(EndPoints.DISABLE_USER, formData, RequestType.FORM_DATA);
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                String title, message;

                if (event.getSource().getValue() != null) {
                    JsonObject responseBody = new Gson().fromJson(event.getSource().getValue().toString(), JsonObject.class);
                    if (responseBody.get("responseStatus").getAsInt() == 200) {
                        title = "Success";
                        message = responseBody.get("message").getAsString();
                        AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                        });
                    } else {
                        title = "Failed";
                        message = responseBody.get("message").getAsString();
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                        });
                    }
                } else {
                    title = "Failed";
                    message = "Please try later!";
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                    });
                }

            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                companyAdminlogger.debug("Erorr in user enable disable onFailed : " + event.getSource().getValue());
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                companyAdminlogger.debug("Erorr in user enable disable onCanceled : " + event.getSource().getValue());
            }
        });
        apiClient.start();

    }

    @Override
    public void startEdit() {
        super.startEdit();
        setText(null);
        setGraphic(vboxGvProduct);
        switchButton.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText((String) getItem());
        setGraphic(null);
    }


    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {

            switchButton.switchOnProperty().set(Boolean.parseBoolean(item));
            HBox vbox = new HBox(vboxGvProduct);
            vbox.setAlignment(Pos.CENTER);
            setGraphic(vbox);
        }
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        ((CompanyAdminDTO) getTableRow().getItem()).setIsSwitch(newValue);
    }


}