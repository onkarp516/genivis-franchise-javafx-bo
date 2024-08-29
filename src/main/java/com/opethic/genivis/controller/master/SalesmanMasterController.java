package com.opethic.genivis.controller.master;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.dto.AreaMasterDTO;
import com.opethic.genivis.dto.PatientDTO;
import com.opethic.genivis.dto.SalesmanMasterDTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SalesmanMasterController implements Initializable {

    @FXML
    private BorderPane salesmanMasterBorderPane;
    @FXML
    private TextField tfSearch, tfSalesmanMasterAddress;
    @FXML
    private HBox hbSalesmanMasterHeaderSect, hbSalesmanMasterHeaderSect1;

    @FXML
    private TextField tfSalesmanMasterDateOfBirth;

    @FXML
    private TextField tfSalesmanMasterFirstName;

    @FXML
    private TextField tfSalesmanMasterLastName;

    @FXML
    private TextField tfSalesmanMasterMiddleName;

    @FXML
    private TextField tfSalesmanMasterMobileNumber;

    @FXML
    private TextField tfSalesmanMasterPincode;

    @FXML
    private Button btnSalemanMasterSubmit;

    @FXML
    private Button btnSalesmanMasterClear;

    @FXML
    private TableView<SalesmanMasterDTO> tblSalemanMaster;

    @FXML
    private TableColumn<SalesmanMasterDTO, String> tcfirstname, tcmiddlename, tclastname, tcmobilenumber, tcsalesmanadd, tcpincode, tcdateofbirth;

    public ObservableList<SalesmanMasterDTO> observableList;

    @FXML
    private TableColumn<SalesmanMasterDTO, Void> tblcCreateSalesmanMasterAction;
    private Integer id = 0;

    private static final Logger loggerSalesmanMaster = LogManager.getLogger(SalesmanMasterController.class);
    private JsonObject jsonObject = null;
    private Object map;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        //Initial Enter Functionality Method
        initialEnterMethod();

        DateValidator.applyDateFormat(tfSalesmanMasterDateOfBirth);

        Platform.runLater(() -> {
            tfSalesmanMasterFirstName.requestFocus();
            shortcutKeysSalesmanMaster();
        });

        CommonValidationsUtils.configureTextFieldToTitleCase(tfSalesmanMasterFirstName);
        CommonValidationsUtils.configureTextFieldToTitleCase(tfSalesmanMasterMiddleName);
        CommonValidationsUtils.configureTextFieldToTitleCase(tfSalesmanMasterLastName);
        CommonValidationsUtils.configureTextFieldToTitleCase(tfSalesmanMasterAddress);

        tblSalemanMaster.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);//only for table

        observableList = FXCollections.observableArrayList();

        tcfirstname.setCellValueFactory(cellData -> cellData.getValue().first_nameProperty());
        tcmiddlename.setCellValueFactory(cellData -> cellData.getValue().middle_nameProperty());
        tclastname.setCellValueFactory(cellData -> cellData.getValue().last_nameProperty());
        tcmobilenumber.setCellValueFactory(cellData -> cellData.getValue().mobile_numberProperty());
        tcsalesmanadd.setCellValueFactory(cellData -> cellData.getValue().salesman_addProperty());
        tcpincode.setCellValueFactory(cellData -> cellData.getValue().pin_codeProperty());
        tcdateofbirth.setCellValueFactory(cellData -> new SimpleStringProperty(DateConvertUtil.convertDispDateFormat(cellData.getValue().getDate_of_birth())));


        tblSalemanMaster.setItems(observableList);

        getSalesmanMaster();
        createUpdateFunctionality();
        setupClearButtonAction();
        setupDoubleClickActionOnTable();
        setupNameValidation();
        setupMobileNumberValidation();
        setupPincodeValidation();
        tfFirstNameValidation();


        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTableData(newValue);
        });

        tfSalesmanMasterPincode.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (!tfSalesmanMasterPincode.getText().isEmpty()) {
                    tfPincodeValidation();
                }
            }
        });

        // Add a key event listener to the HBOX
        hbSalesmanMasterHeaderSect.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN) {
                tfSearch.requestFocus();
                event.consume();
            }
        });
        // Add a key event listener to the HBOX
        hbSalesmanMasterHeaderSect1.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN) {
                tfSearch.requestFocus();
                event.consume();
            }
        });

        // Add a key event listener to the text field
        tfSearch.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN) {
                tblSalemanMaster.getSelectionModel().selectFirst();
                tblSalemanMaster.requestFocus();
                event.consume();
            }
        });
        tfSalesmanMasterDateOfBirth.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if(event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER){
                birthDateFun();
            }
        });


    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            salesmanMasterBorderPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            salesmanMasterBorderPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            salesmanMasterBorderPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            salesmanMasterBorderPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            salesmanMasterBorderPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            salesmanMasterBorderPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            salesmanMasterBorderPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    //function for birth date validation
    public void birthDateFun() {
        String birthDateString = tfSalesmanMasterDateOfBirth.getText();
//        System.out.println("birthDateString-- "+birthDateString);
        if (birthDateString.isEmpty()) {
            // Handle case where input is empty
            return;
        }

        LocalDate selectedDate = null;
        try {
            // Parse the date string with the correct format "dd/MM/yyyy"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            selectedDate = LocalDate.parse(birthDateString, formatter);
        } catch (DateTimeParseException e) {
            // Handle parsing error
            e.printStackTrace(); // Or handle it in a meaningful way
            return;
        }
//        System.out.println("selectedDate-- "+selectedDate+"  LocalDate.now()-- "+LocalDate.now());
        if (selectedDate.isAfter(LocalDate.now())) {
            System.out.println("after true");
            AlertUtility.AlertWarningTimeout3(AlertUtility.alertTypeError, "Select Valid Date", in -> {
                tfSalesmanMasterDateOfBirth.requestFocus();
            });
        }
    }

    //Initial Enter Functionality Method
    private void initialEnterMethod() {
        //? It will work only on radio button
        salesmanMasterBorderPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof RadioButton) {
                    System.out.println("Filter RadioButton");
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

        salesmanMasterBorderPane.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
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

    public void tfFirstNameValidation() {
        tfSalesmanMasterFirstName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfSalesmanMasterFirstName.getText().isEmpty()) {
                    tfSalesmanMasterFirstName.requestFocus();
                }

            }
        });
    }

    public void tfPincodeValidation() {


        APIClient apiClient = null;
        try {
            loggerSalesmanMaster.debug("Get ValidatePincode Data Started...");
            Map<String, String> map = new HashMap<>();
            map.put("pincode", String.valueOf(tfSalesmanMasterPincode.getText()));
            String formData = Globals.mapToStringforFormData(map);
            System.out.println("Pincode FormData >> "+ formData);
            apiClient = new APIClient(EndPoints.VALIDATE_PINCODE_ENDPOINT, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    System.out.println("JsonObject >> "+ jsonObject.get("responseStatus"));
                    if (jsonObject.get("responseStatus").getAsInt() != 200) {
                        String message = jsonObject.get("message").getAsString();
//                        AlertUtility.AlertError(AlertUtility.alertTypeError, message, in -> {
//                            tfSalesmanMasterPincode.requestFocus();
//                        });
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                            tfSalesmanMasterPincode.requestFocus();
                        });
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    loggerSalesmanMaster.error("Network API cancelled in validatePincode()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    loggerSalesmanMaster.error("Network API failed in validatePincode()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            loggerSalesmanMaster.debug("Get ValidatePincode Data End...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }


    }

    private void filterTableData(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            // If search text is empty, display all items in the table
            tblSalemanMaster.setItems(observableList);
        } else {
            ObservableList<SalesmanMasterDTO> filteredList = FXCollections.observableArrayList();
            for (SalesmanMasterDTO item : observableList) {
                // Filter logic: Check if the item's fields contain the search text
                if (item.getFirst_name().toLowerCase().contains(searchText.toLowerCase()) ||
                        item.getMiddle_name().toLowerCase().contains(searchText.toLowerCase()) ||
                        item.getLast_name().toLowerCase().contains(searchText.toLowerCase()) ||
                        item.getMobile_number().toLowerCase().contains(searchText.toLowerCase()) ||
                        item.getSalesman_add().toLowerCase().contains(searchText.toLowerCase()) ||
                        item.getPin_code().toLowerCase().contains(searchText.toLowerCase()) ||
                        item.getDate_of_birth().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            tblSalemanMaster.setItems(filteredList); // Update table with filtered data
        }
    }

    private void setupPincodeValidation() {
        TextFormatter<String> pincodeFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,6}")) { // Matches up to 6 digits
                return change;
            }
            return null; // Ignore the change if it doesn't match
        });

        tfSalesmanMasterPincode.setTextFormatter(pincodeFormatter);
    }

    private void setupMobileNumberValidation() {

        TextFormatter<String> mobileNumberFormatter = new TextFormatter<>(change -> {
            // Allow change if it results in digits only and does not exceed 10 digits
            if (change.getControlNewText().matches("\\d{0,10}")) {
                return change;
            }
            // Reject the change if it doesn't match the required pattern
            return null;
        });

        tfSalesmanMasterMobileNumber.setTextFormatter(mobileNumberFormatter);
    }

    private void setupNameValidation() {
        TextFormatter<String> firstNameFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("[a-zA-Z\\s]*")) {
                return change;
            }
            return null;
        });

        TextFormatter<String> middleNameFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("[a-zA-Z\\s]*")) {
                return change;
            }
            return null;
        });

        TextFormatter<String> lastNameFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("[a-zA-Z\\s]*")) {
                return change;
            }
            return null;
        });

        tfSalesmanMasterFirstName.setTextFormatter(firstNameFormatter);
        tfSalesmanMasterMiddleName.setTextFormatter(middleNameFormatter);
        tfSalesmanMasterLastName.setTextFormatter(lastNameFormatter);
    }

    private void setupClearButtonAction() {
        btnSalesmanMasterClear.setOnAction(actionEvent -> clearFields());
    }

    private void setupDoubleClickActionOnTable() {
        tblSalemanMaster.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                setEditData();
            }
        });


        tblSalemanMaster.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                setEditData();
            }
        });

    }

    private void setEditData() {
        SalesmanMasterDTO selectedItem = tblSalemanMaster.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            id = selectedItem.getId(); // Store the selected item's id
            tfSalesmanMasterFirstName.setText(selectedItem.getFirst_name());
            tfSalesmanMasterMiddleName.setText(selectedItem.getMiddle_name());
            tfSalesmanMasterLastName.setText(selectedItem.getLast_name());
            tfSalesmanMasterMobileNumber.setText(selectedItem.getMobile_number());
            tfSalesmanMasterAddress.setText(selectedItem.getSalesman_add());
            tfSalesmanMasterPincode.setText(selectedItem.getPin_code());
            String dob = selectedItem.getDate_of_birth();
            System.out.println("DOB >> "+ dob);
            if (dob != null && tfSalesmanMasterDateOfBirth != null && !dob.isEmpty()) {
                LocalDate applicable_date = LocalDate.parse(dob);
                tfSalesmanMasterDateOfBirth.setText(applicable_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            btnSalemanMasterSubmit.setText("Update");
            Platform.runLater(() -> {
                tfSalesmanMasterFirstName.requestFocus();
            });
        }
    }


    private void removeSelectedItem() {
        SalesmanMasterDTO selectedItem = tblSalemanMaster.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            deleteAPICall(selectedItem.getId());
        }
    }

    private void deleteAPICall(int id) {

        try {
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(id));
            String formData = Globals.mapToStringforFormData(map);

            //Called API for get HSN Data
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.removesalesmanmaster);
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
//
//            if (jsonObject != null && jsonObject.get("responseStatus").getAsInt() == 200) {
//                // If removal is successful, remove the item from the table
//                AlertUtility.AlertSuccess(AlertUtility.alertTypeSuccess, jsonObject.get("message").getAsString(), in -> {
//                    clearFields();
//                    getSalesmanMaster();
//                    setFocus();
//                });
//            } else {
//                AlertUtility.AlertError(AlertUtility.alertTypeError, jsonObject.get("message").getAsString(), in -> {
//                    clearFields();
//                    getSalesmanMaster();
//                    setFocus();
//                });
//            }
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess,  jsonObject.get("message").getAsString(), input -> {
                    clearFields();
                    getSalesmanMaster();
                    setFocus();
                });
            } else {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError,  jsonObject.get("message").getAsString(), in -> {
                    clearFields();
                    getSalesmanMaster();
                    setFocus();
                });
            }
        } catch (Exception e) {
            // Handle exception (e.g., display error message)

            loggerSalesmanMaster.error("Exception deleteAPICall() : " + Globals.getExceptionString(e));
        }
    }

    // createUpdate Functionality
    private  void createUpdateFunctionality(){
        btnSalemanMasterSubmit.setOnAction(e -> {
            if (CommonValidationsUtils.validateForm(tfSalesmanMasterFirstName)) {

                if (btnSalemanMasterSubmit.getText().equalsIgnoreCase("submit")) {
                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnSubmit, input -> {
                        if (input == 1) {
                            btncamSubmit();
                        }
                    });
                } else {
                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnUpdate, input -> {
                        if (input == 1) {
                            btncamSubmit();
                        }
                    });
                }
            }
        });
    }

    private void btncamSubmit() {

                    String firstname = tfSalesmanMasterFirstName.getText();
                    String middlename = tfSalesmanMasterMiddleName.getText().isEmpty() ? "" : tfSalesmanMasterMiddleName.getText();
                    String lastname = tfSalesmanMasterLastName.getText().isEmpty() ? "" : tfSalesmanMasterLastName.getText();
                    String salesmanadd = tfSalesmanMasterAddress.getText().isEmpty() ? "" : tfSalesmanMasterAddress.getText();
                    String pinCode = tfSalesmanMasterPincode.getText().isEmpty() ? "" : tfSalesmanMasterPincode.getText();
                    String mobilenumber = tfSalesmanMasterMobileNumber.getText().isEmpty() ? "" : tfSalesmanMasterMobileNumber.getText();

                    Map<String, String> map = new HashMap<>();
                    map.put("firstName", firstname);
                    map.put("middleName", middlename);
                    map.put("lastName", lastname);
                    map.put("mobileNumber", mobilenumber);
                    map.put("address", salesmanadd);
                    map.put("pincode", pinCode);
                    if (tfSalesmanMasterDateOfBirth != null && tfSalesmanMasterDateOfBirth.getText() != null && !tfSalesmanMasterDateOfBirth.getText().isEmpty()) {
                        map.put("dob", Communicator.text_to_date.fromString(tfSalesmanMasterDateOfBirth.getText()).toString());
                    } else {
                        map.put("dob", "");
                    }
                    String formData = Globals.mapToStringforFormData(map);
                    HttpResponse<String> response;
                    if (id > 0) {
                        formData = formData + "&id=" + id;
                        response = APIClient.postFormDataRequest(formData, EndPoints.updateSalesmanMaster);
                    } else {
                        response = APIClient.postFormDataRequest(formData, EndPoints.createSalesmanMaster);
                    }
                    handleResponse(response);
    }

    private void handleResponse(HttpResponse<String> response) {
        if (response != null && response.body() != null) {
            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
            if (responseBody.has("responseStatus") && responseBody.get("responseStatus").getAsInt() == 200) {
                String operationResultMessage;
                if (id > 0) {
                    // This implies an update operation was performed
                    operationResultMessage = "Salesman master updated successfully.";
                } else {
                    // This implies a creation operation was performed
                    operationResultMessage = "Salesman master created successfully.";
                }
                AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, operationResultMessage, input -> {
                    clearFields();
                    getSalesmanMaster();
                    setFocus();
                });
            } else {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Failed to create/update area master.", in -> {
                    setFocus();
                    clearFields();
                });
            }
        }
    }

    private void setFocus() {
        tfSalesmanMasterFirstName.requestFocus();
    }


    private void clearFields() {
        btnSalemanMasterSubmit.setText("Submit");
        tfSalesmanMasterFirstName.clear();
        tfSalesmanMasterMiddleName.clear();
        tfSalesmanMasterLastName.clear();
        tfSalesmanMasterMobileNumber.clear();
        tfSalesmanMasterAddress.clear();
        tfSalesmanMasterPincode.clear();
        tfSalesmanMasterDateOfBirth.clear();
        id = 0; // Reset the id when fields are cleared
    }

    private void getSalesmanMaster() {
        try {
            // tblSalemanMaster.getItems().clear();
            HttpResponse<String> response = APIClient.getRequest(EndPoints.getOutletSalesmanMaster);
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);

//            observableList = FXCollections.observableArrayList();
            observableList.clear();
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseObject = jsonObject.getAsJsonArray("responseObject");
                for (JsonElement element : responseObject) {
                    JsonObject item = element.getAsJsonObject();
//                    AreaMasterDTO areaMasterDTO = new Gson().fromJson(item, AreaMasterDTO.class);
                    observableList.add(new SalesmanMasterDTO(item.get("id").getAsInt(), item.get("firstName").getAsString(), item.get("middleName").getAsString(), item.get("lastName").getAsString(), item.get("mobile").getAsString(), item.get("address").getAsString(), item.get("pincode").getAsString(), item.get("dob").getAsString()));
//                    observableList.add(areaMasterDTO);
                }


//                tblcCreateSalesmanMasterAction.setCellFactory(param -> {
//                    final TableCell<SalesmanMasterDTO, Void> cell = new TableCell<>() {
//                        private ImageView delImg = Globals.getDelImage();
//                        private ImageView edtImg = Globals.getEdtImage();
//
//                        {
//                            delImg.setFitHeight(16.0);
//                            delImg.setFitWidth(16.0);
//                            edtImg.setFitHeight(16.0);
//                            edtImg.setFitWidth(16.0);
//                        }
//
//                        private final Button delButton = new Button("", delImg);
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
//                                SalesmanMasterDTO selectedItem = observableList.get(getIndex());
//                                if (selectedItem != null) {
//                                    id = selectedItem.getId(); // Store the selected item's id
//                                    tfSalesmanMasterFirstName.setText(selectedItem.getFirst_name());
//                                    tfSalesmanMasterMiddleName.setText(selectedItem.getMiddle_name());
//                                    tfSalesmanMasterLastName.setText(selectedItem.getLast_name());
//                                    tfSalesmanMasterMobileNumber.setText(selectedItem.getMobile_number());
//                                    tfSalesmanMasterAddress.setText(selectedItem.getSalesman_add());
//                                    tfSalesmanMasterPincode.setText(selectedItem.getPin_code());
//                                    String dob = selectedItem.getDate_of_birth();
//                                    if (dob != null && tfSalesmanMasterDateOfBirth != null) {
//                                        LocalDate applicable_date = LocalDate.parse(dob);
//                                        tfSalesmanMasterDateOfBirth.setText(applicable_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//                                    }
//                                    btnSalemanMasterSubmit.setText("Update");
//                                    tfSalesmanMasterFirstName.requestFocus();
//                                }
//                            });
//
//
//                        }
//
//
//                        HBox hbActions = new HBox();
//
//                        {
//                            hbActions.getChildren().addAll(edtButton, delButton);
//                            hbActions.setSpacing(10.0);
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
//                        }
//
//                    };
//
//                    return cell;
//                });


                tblSalemanMaster.refresh();

            } else {
                String msg = "Record Not Found";
                AlertUtility.CustomCallback callback = number -> {
                    setFocus();
                };
                AlertUtility.AlertError(AlertUtility.alertTypeError, msg, callback);
            }


        } catch (Exception e) {
            loggerSalesmanMaster.error("Exception getSalesmanMaster() : " + Globals.getExceptionString(e));
        }
    }

    private void shortcutKeysSalesmanMaster() {
        salesmanMasterBorderPane.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if(event.getCode()==KeyCode.S && event.isControlDown()){
                    if (CommonValidationsUtils.validateForm(tfSalesmanMasterFirstName)) {
                        System.out.println("hello");
                        if (btnSalemanMasterSubmit.getText().equalsIgnoreCase("submit")) {
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
                    if (!tblSalemanMaster.getSelectionModel().isEmpty()) {
                        SalesmanMasterDTO selectedItem = tblSalemanMaster.getSelectionModel().getSelectedItem();
                        if (selectedItem != null) {
                            tfSalesmanMasterFirstName.setText(selectedItem.getFirst_name());
                            tfSalesmanMasterMiddleName.setText(selectedItem.getMiddle_name());
                            tfSalesmanMasterLastName.setText(selectedItem.getLast_name());
                            tfSalesmanMasterMobileNumber.setText(selectedItem.getMobile_number());
                            tfSalesmanMasterAddress.setText(selectedItem.getSalesman_add());
                            tfSalesmanMasterPincode.setText(selectedItem.getPin_code());
                            String dob = selectedItem.getDate_of_birth();
                            if (dob != null && tfSalesmanMasterDateOfBirth != null && !dob.isEmpty()) {
                                LocalDate applicable_date = LocalDate.parse(dob);
                                tfSalesmanMasterDateOfBirth.setText(applicable_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                            }
                            btnSalemanMasterSubmit.setText("Update");
                            Platform.runLater(() -> {
                                tfSalesmanMasterFirstName.requestFocus();
                            });
                        }
                        event.consume();
                    } else{
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Select Salesman First", in -> {
                            tblSalemanMaster.getSelectionModel().selectFirst();
                            tblSalemanMaster.requestFocus();
                            event.consume();
                        });
                    }
                }

                if (event.getCode()==KeyCode.X && event.isControlDown()) {
                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnClear, input -> {
                        if (input == 1) {
                            clearFields();
                        }
                    });
                }if (event.getCode()==KeyCode.D && event.isControlDown()) {
                    handleDeletePressed();
                }if (event.getCode()==KeyCode.E && event.isControlDown()) {
                    setEditData();
                }

            }
        });
    }

    @FXML
    private void handleDeletePressed() {
        SalesmanMasterDTO selectedItem = tblSalemanMaster.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            id = Integer.valueOf(selectedItem.getId());
            deleteAPICall(id);
        }
    }

}



