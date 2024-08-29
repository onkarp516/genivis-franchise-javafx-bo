package com.opethic.genivis.controller.master;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.dto.AreaMasterDTO;
import com.opethic.genivis.dto.DoctorMasterDTO;
import com.opethic.genivis.dto.PatientDTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.CommonValidationsUtils;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AreaMasterController implements Initializable {

    private static final Logger logger = LogManager.getLogger(AreaMasterController.class);
    @FXML
    private BorderPane bpAreaMasterRoot;
    @FXML
    private Button btnAreaMasterCreateClear, btnAreaMasterCreateSubmit;
    @FXML
    private TableView<AreaMasterDTO> tbvAreaMasterCreateTbl;
    @FXML
    private TextField tfAreaMasterCreateAreaCode, tfAreaMasterCreateAreaName, tfAreaMasterCreatePincode, tfAreaMasterCreateSearch;
    @FXML
    private TableColumn<AreaMasterDTO, String> tcAreaName, tcAreacode, tcpincode;

    @FXML
    private HBox hbAreaMasterHeaderSect,hbAreaMasterSearchSect;

    @FXML
    private TableColumn<AreaMasterDTO, Void> tblcAction;
    @FXML
    private Integer id = 0;

    private static final Logger loggerAreaMaster = LogManager.getLogger(AreaMasterController.class);
    private Object map;
    private Object event;
    private String areaId;
    private Button delButton;
    private JsonObject jsonObject = null;

    String message = "";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();


        Platform.runLater(() -> {
            tfAreaMasterCreateAreaName.requestFocus();
            shortcutKeysAreaMaster();
        });
        tbvAreaMasterCreateTbl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);//only for table

        tfAreaMasterCreatePincode.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (!tfAreaMasterCreatePincode.getText().isEmpty()) {
                    tfPincodeValidation();
                }
            }
        });

        getAreaMaster();
        setupClearButtonAction();
        setupDoubleClickActionOnTable();
        setupPincodeValidation();
        initializeSearchFunctionality();
        tfAreaNameValidation();
        createUpdateFunctionality();

//        tfAreaMasterCreateAreaName.setOnKeyPressed(this::handleEnterKeyPressed);
        tfAreaMasterCreateAreaCode.setOnKeyPressed(this::handleEnterKeyPressed);
        tfAreaMasterCreatePincode.setOnKeyPressed(this::handleEnterKeyPressed);
        btnAreaMasterCreateSubmit.setOnKeyPressed(this::handleEnterKeyPressed);
        btnAreaMasterCreateClear.setOnKeyPressed(this::handleEnterKeyPressed);
        tfAreaMasterCreateSearch.setOnKeyPressed(this::handleEnterKeyPressed);

        tfAreaMasterCreateAreaName.setOnKeyPressed( event->{
            if(event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER){
                validAreaMasterName();
            }
        });
        hbAreaMasterHeaderSect.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN) {
                tfAreaMasterCreateSearch.requestFocus();
                event.consume();
            }
        });
        tfAreaMasterCreateSearch.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN) {
                tbvAreaMasterCreateTbl.getSelectionModel().selectFirst();
                tbvAreaMasterCreateTbl.requestFocus();
                event.consume();
            }
        });

        CommonValidationsUtils.configureTextFieldToTitleCase(tfAreaMasterCreateAreaName);

    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            bpAreaMasterRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            bpAreaMasterRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            bpAreaMasterRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            bpAreaMasterRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            bpAreaMasterRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            bpAreaMasterRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            bpAreaMasterRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    public void tfAreaNameValidation() {
        tfAreaMasterCreateAreaName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfAreaMasterCreateAreaName.getText().isEmpty()) {
                    tfAreaMasterCreateAreaName.requestFocus();
                }
            }
        });
    }
    private void validAreaMasterName(){
        APIClient apiClient=null;
        try {
            String areaMasterName = tfAreaMasterCreateAreaName.getText().trim();
            loggerAreaMaster.debug("Get Validate area master Data Started...");
            Map<String, String> map = new HashMap<>();
            map.put("areaName", areaMasterName);
            String formData = Globals.mapToStringforFormData(map);
            System.out.println("Pincode FormData >> " + formData);
            apiClient = new APIClient("validate_area_master", formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    System.out.println("JsonObject in area master >> " + jsonObject);
                    if (jsonObject.get("responseStatus").getAsInt() == 409) {
                        String message = jsonObject.get("message").getAsString();
                        AlertUtility.AlertError(AlertUtility.alertTypeError, message, in -> {
                            tfAreaMasterCreateAreaName.requestFocus();
                        });
                    }else{
                        tfAreaMasterCreateAreaCode.requestFocus();
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    loggerAreaMaster.error("Network API cancelled in validatePincode()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    loggerAreaMaster.error("Network API failed in validatePincode()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            loggerAreaMaster.debug("Get ValidatePincode Data End...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    public void tfPincodeValidation() {
        APIClient apiClient = null;
        try {
            loggerAreaMaster.debug("Get ValidatePincode Data Started...");
            Map<String, String> map = new HashMap<>();
            map.put("pincode", String.valueOf(tfAreaMasterCreatePincode.getText()));
            String formData = Globals.mapToStringforFormData(map);
            System.out.println("Pincode FormData >> " + formData);
            apiClient = new APIClient(EndPoints.VALIDATE_PINCODE_ENDPOINT, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    System.out.println("JsonObject >> " + jsonObject.get("responseStatus"));
                    if (jsonObject.get("responseStatus").getAsInt() != 200) {
                        String message = jsonObject.get("message").getAsString();
                        AlertUtility.AlertError(AlertUtility.alertTypeError, message, in -> {
                            tfAreaMasterCreatePincode.requestFocus();
                        });
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    loggerAreaMaster.error("Network API cancelled in validatePincode()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    loggerAreaMaster.error("Network API failed in validatePincode()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            loggerAreaMaster.debug("Get ValidatePincode Data End...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }


    }

    private void handleEnterKeyPressed(KeyEvent keyEvent) {
        Node source = (Node) keyEvent.getSource();
        if (keyEvent.getCode() == KeyCode.ENTER) {
            switch (source.getId()) {
                case "tfAreaMasterCreateAreaName":
                    if (!tfAreaMasterCreateAreaName.getText().isEmpty()) {
                        tfAreaMasterCreateAreaCode.requestFocus();
                    }
                    break;
                case "tfAreaMasterCreateAreaCode":
                    if (tfAreaMasterCreateAreaCode.getText().isEmpty() || !tfAreaMasterCreateAreaCode.getText().isEmpty()) {
                        tfAreaMasterCreatePincode.requestFocus();
                    }
                    break;
                case "tfAreaMasterCreatePincode":
                    if (tfAreaMasterCreatePincode.getText().isEmpty() || !tfAreaMasterCreatePincode.getText().isEmpty()) {
                        btnAreaMasterCreateSubmit.requestFocus();
                    }
                    break;
                default:
                    break;
            }
        }
        else if (keyEvent.getCode() == KeyCode.PAGE_UP) {
            // Handle Page Up key press event for TableView
            int selectedIndex = tbvAreaMasterCreateTbl.getSelectionModel().getSelectedIndex();
            if (selectedIndex == 0) {
                tfAreaMasterCreateAreaName.requestFocus();
            }
        }


    }


    private void initializeSearchFunctionality() {
        tfAreaMasterCreateSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTableData(newValue);
        });
    }


    private void filterTableData(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            getAreaMaster();
        } else {
            ObservableList<AreaMasterDTO> filteredList = FXCollections.observableArrayList();
            for (AreaMasterDTO item : tbvAreaMasterCreateTbl.getItems()) {
                if (item.getAreaName().toLowerCase().contains(searchText.toLowerCase()) ||
                        item.getAreaCode().toLowerCase().contains(searchText.toLowerCase()) ||
                        item.getPinCode().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            tbvAreaMasterCreateTbl.setItems(filteredList);
        }
    }


    private void setupPincodeValidation() {
        TextFormatter<String> pincodeFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,6}")) { // Matches exactly 6 digits
                return change;
            }
            return null; // Ignore the change if it doesn't match
        });

        tfAreaMasterCreatePincode.setTextFormatter(pincodeFormatter);
    }


    private void setupClearButtonAction() {
        btnAreaMasterCreateClear.setOnAction(actionEvent -> clearFields());
    }

    private void setupDoubleClickActionOnTable() {
        System.out.println("hello I am Here");
        tbvAreaMasterCreateTbl.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                setEditData();
            }
        });


        tbvAreaMasterCreateTbl.setOnKeyPressed(event -> {
            System.out.println("hello I am Here 12");
            if (event.getCode() == KeyCode.ENTER) {
                setEditData();
            }
        });


    }

    private void setEditData() {
        System.out.println("hello I am Here 1");
        AreaMasterDTO selectedItem = tbvAreaMasterCreateTbl.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            id = selectedItem.getId(); // Store the selected item's id
            tfAreaMasterCreateAreaName.setText(selectedItem.getAreaName());
            tfAreaMasterCreateAreaCode.setText(selectedItem.getAreaCode());
            tfAreaMasterCreatePincode.setText(selectedItem.getPinCode());
            btnAreaMasterCreateSubmit.setText("Update");
            Platform.runLater(() -> {
                tfAreaMasterCreateAreaName.requestFocus();
            });
        }
    }


    // createUpdate Functionality
    private  void createUpdateFunctionality(){
        btnAreaMasterCreateSubmit.setOnAction(e -> {
            if (CommonValidationsUtils.validateForm(tfAreaMasterCreateAreaName)) {

                if (btnAreaMasterCreateSubmit.getText().equalsIgnoreCase("submit")) {
                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Submit", input -> {
                        if (input == 1) {
                            btncamSubmit();
                        }
                    });
                } else {
                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Update", input -> {
                        if (input == 1) {
                            btncamSubmit();
                        }
                    });
                }
            }
        });
    }

    private void btncamSubmit() {

                    String areaName = tfAreaMasterCreateAreaName.getText();
                    String areaCode = tfAreaMasterCreateAreaCode.getText().isEmpty() ? "" : tfAreaMasterCreateAreaCode.getText();
                    String pinCode = tfAreaMasterCreatePincode.getText().isEmpty() ? "" : tfAreaMasterCreatePincode.getText();
                    // If all fields are filled and pincode length is correct, proceed with submission
                    Map<String, String> map = new HashMap<>();
                    map.put("areaName", areaName);
                    map.put("areaCode", areaCode);
                    map.put("pincode", pinCode);

                    String formData = Globals.mapToStringforFormData(map);
                    HttpResponse<String> response;
                    if (id > 0) {
                        formData = formData + "&id=" + id;
                        response = APIClient.postFormDataRequest(formData, EndPoints.updateAreaMaster);
                    } else {
                        response = APIClient.postFormDataRequest(formData, EndPoints.createAreaMaster);
                    }

                    handleResponse(response);

    }

    private void handleResponse(HttpResponse<String> response) {
        if (response != null && response.body() != null) {
            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
            if (responseBody.has("responseStatus") && responseBody.get("responseStatus").getAsInt() == 200) {
                AlertUtility.AlertSuccess(AlertUtility.alertTypeSuccess, responseBody.get("message").getAsString(), input -> {
                    clearFields();
                    getAreaMaster();
                });
            } else {
                // Display warning if response status is not 200
                AlertUtility.AlertError(AlertUtility.alertTypeError, "Failed to create/update area master.", in -> {
                    setFocus();
                });
            }
        }
    }

    private void setFocus() {
        tfAreaMasterCreateAreaName.requestFocus();
    }

    private void clearFields() {
        btnAreaMasterCreateSubmit.setText("Submit");
        tfAreaMasterCreateAreaName.requestFocus();
        tfAreaMasterCreateAreaName.clear();
        tfAreaMasterCreateAreaCode.clear();
        tfAreaMasterCreatePincode.clear();
        tfAreaMasterCreateSearch.clear();
        id = 0; // Reset the id when fields are cleared
    }

    private void getAreaMaster() {
        try {
            tbvAreaMasterCreateTbl.getItems().clear();
            HttpResponse<String> response = APIClient.getRequest(EndPoints.getOutletAreaMaster);
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            ObservableList<AreaMasterDTO> observableList = FXCollections.observableArrayList();
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseObject = jsonObject.getAsJsonArray("responseObject");
                for (JsonElement element : responseObject) {
                    JsonObject item = element.getAsJsonObject();
//                    AreaMasterDTO areaMasterDTO = new Gson().fromJson(item, AreaMasterDTO.class);
                    observableList.add(new AreaMasterDTO(item.get("id").getAsInt(), item.get("areaName").getAsString(), item.get("areaCode").getAsString(), item.get("pincode").getAsString()));
//                    observableList.add(areaMasterDTO);
                }

                tcAreaName.setCellValueFactory(new PropertyValueFactory<>("areaName"));
                tcAreacode.setCellValueFactory(new PropertyValueFactory<>("areaCode"));
                tcpincode.setCellValueFactory(new PropertyValueFactory<>("pinCode"));
//                tblcAction.setCellFactory(param -> {
//                    final TableCell<AreaMasterDTO, Void> cell = new TableCell<>() {
//                        private ImageView delImg = Globals.getDelImage();
//                        private ImageView edtImg = Globals.getEdtImage();
//
//                        {
//                            delImg.setFitHeight(16.0);
//                            delImg.setFitWidth(16.0);
//                            edtImg.setFitHeight(16.0);
//                            edtImg.setFitWidth(16.0);
//
//                        }
//
//
//                        private final Button delButton = new Button("", delImg);
//
//                        private final Button edtButton = new Button("", edtImg);
//
//
//                        {
//
//
//                            delButton.setOnAction((e) -> {
//                                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you Sure?", input -> {
//                                    if (input == 1) {
//                                        deleteAPICall(observableList.get(getIndex()).getId());
//                                        clearFields();
//                                    }
//                                });
//                            });
//                            edtButton.setOnAction((e) -> {
//                                AreaMasterDTO selectedItem = observableList.get(getIndex());
//                                if (selectedItem != null) {
//                                    id = selectedItem.getId(); // Store the selected item's id
//                                    tfAreaMasterCreateAreaName.setText(selectedItem.getAreaName());
//                                    tfAreaMasterCreateAreaCode.setText(selectedItem.getAreaCode());
//                                    tfAreaMasterCreatePincode.setText(selectedItem.getPinCode());
//                                    btnAreaMasterCreateSubmit.setText("Update");
//                                    tfAreaMasterCreateAreaName.requestFocus();
//                                }
//                            });
//
//
//                        }
//
//                        HBox hbActions = new HBox();
//
//                        {
//
//                            hbActions.getChildren().addAll(edtButton, delButton);
//                            hbActions.setSpacing(6.0);
//
//                        }
//
//                        @Override
//                        protected void updateItem(Void item, boolean empty) {
//                            super.updateItem(item, empty);
//                            if (empty) {
//                                setGraphic(null);
//                            } else {
//                                setGraphic(hbActions);
//                            }
//
//                        }
//
//                    };
//
//                    return cell;
//                });
                tbvAreaMasterCreateTbl.setItems(observableList);
            }
        } catch (Exception e) {
            loggerAreaMaster.error("Exception getAreaMaster() : " + Globals.getExceptionString(e));
        }
    }

    private void shortcutKeysAreaMaster() {
        bpAreaMasterRoot.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if(event.getCode()==KeyCode.S && event.isControlDown()){
                    if (CommonValidationsUtils.validateForm(tfAreaMasterCreateAreaName)) {
                        System.out.println("hello");
                        if (btnAreaMasterCreateSubmit.getText().equalsIgnoreCase("submit")) {
                            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Submit", input -> {
                                if (input == 1) {
                                    btncamSubmit();
                                }
                            });
                        } else {
                            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Update", input -> {
                                if (input == 1) {
                                    btncamSubmit();
                                }
                            });
                        }
                    }
                }
                if (event.getCode()==KeyCode.E && event.isControlDown()) {
                    if (!tbvAreaMasterCreateTbl.getSelectionModel().isEmpty()) {
                        AreaMasterDTO selectedItem = tbvAreaMasterCreateTbl.getSelectionModel().getSelectedItem();
                        if (selectedItem != null) {
                            id = selectedItem.getId(); // Store the selected item's id
                            tfAreaMasterCreateAreaName.setText(selectedItem.getAreaName());
                            tfAreaMasterCreateAreaCode.setText(selectedItem.getAreaCode());
                            tfAreaMasterCreatePincode.setText(selectedItem.getPinCode());
                            btnAreaMasterCreateSubmit.setText("Update");
                            Platform.runLater(() -> {
                                tfAreaMasterCreateAreaName.requestFocus();
                            });
                        }
                        event.consume();
                    } else{
                        AlertUtility.AlertError(AlertUtility.alertTypeError, "Select Area First", in -> {
                            tbvAreaMasterCreateTbl.getSelectionModel().selectFirst();
                            tbvAreaMasterCreateTbl.requestFocus();
                            event.consume();
                        });
                    }
                }

                if (event.getCode()==KeyCode.X && event.isControlDown()) {
                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Clear", input -> {
                        if (input == 1) {
                            clearFields();
                        }
                    });
                }
                if(event.getCode()==KeyCode.D && event.isControlDown()){
                   handleDeletePressed();
                }

            }
        });
    }
    public void deletePatient() {
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                APIClient apiClient = null;
                Map<String, String> map = new HashMap<>();
                map.put("id", String.valueOf(id));

                HttpResponse<String> response;
                String formData = Globals.mapToStringforFormData(map);
                apiClient = new APIClient(EndPoints.AREA_MASTER_DELETE_ENDPOINT, formData, RequestType.FORM_DATA);

                apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        // JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                        jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

                        message = jsonObject.get("message").getAsString();

                        if (jsonObject.get("responseStatus").getAsInt() == 200) {
                            AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                                tfAreaMasterCreateAreaName.requestFocus();
                                id = 0;
                            });
                        } else {
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                            });
                        }
                        getAreaMaster();
                        clearFields();

                    }
                });

                apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        logger.error("Network API cancelled in deletePatient()" + workerStateEvent.getSource().getValue().toString());

                    }
                });
                apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        logger.error("Network API failed in deletePatient()" + workerStateEvent.getSource().getValue().toString());

                    }
                });
                apiClient.start();
                logger.debug("delete patient data end ...");
            } else {
            }
        };
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnDelete, callback);
    }
    @FXML
    private void handleDeletePressed() {
        AreaMasterDTO selectedItem = tbvAreaMasterCreateTbl.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            id = selectedItem.getId();
            deletePatient();
        }
    }

}


