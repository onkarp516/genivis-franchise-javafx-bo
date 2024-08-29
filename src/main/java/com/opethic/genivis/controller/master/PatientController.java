package com.opethic.genivis.controller.master;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.dto.CommonDTO;
import com.opethic.genivis.dto.CompanyAdminDTO;
import com.opethic.genivis.dto.PatientDTO;
import com.opethic.genivis.dto.PaymentModeDTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.network.EndPoints.*;
import static com.opethic.genivis.utils.FxmFileConstants.FRANCHISE_LIST_SLUG;

public class PatientController implements Initializable {

    @FXML
    private VBox boxV;
    @FXML
    private TextField tfPatientCreateName;
    @FXML
    private TextField tfPatientCreateAge;
    @FXML
    private TextField tfPatientCreateWeight;
    @FXML
    private TextField tfPatientCreateAddress;
    @FXML
    private TextField tfPatientCreateMobileNo;
    @FXML
    private TextField tfPatientCreateIdNo;
    @FXML
    private ComboBox cmbPatientCreateGender;
    @FXML
    private TextField tfPatientCreateBloodGrp;
    @FXML
    private TextField dpPatientCreateBirthDate;
    @FXML
    private TextField tfPatientCreatePin;
    @FXML
    private TextField dpPatientCreateDignosisDate;
    @FXML
    private TextField dpPatientCreateTIDate;
    @FXML
    private Button btnPatientCreateSubmit;
    @FXML
    private Button btnPatientCreateCancel;
    String selectedValue = "";
    public String id = "";
    String message = "";
    private JsonObject jsonObject = null;

    @FXML
    private TableView<PatientDTO> tcPatientView;

    @FXML
    private TableColumn<PatientDTO, String> tcPatientName;

    @FXML
    private TableColumn<PatientDTO, String> tcPatientAddress;
    @FXML
    private TableColumn<PatientDTO, String> tcMobileNo;
    @FXML
    private TableColumn<PatientDTO, String> tcPincode;
    @FXML
    private TableColumn<PatientDTO, String> tcGender;
//    @FXML
//    private TableColumn<PatientDTO, String> tcAction;

    @FXML
    private ScrollPane spRootPatientPane;

    @FXML
    private HBox toggleValidation;

    private String focusInd = "";
    private Integer rowIndex;

    @FXML
    private TextField tffieldSearch;
    private Node[] focusableNodes;

    private ObservableList<PatientDTO> originalData;
    private static final Logger patientlogger = LogManager.getLogger(PatientController.class);
    String selectedRadioValue="";
    private String isGender; // Variable to store the selected gender
    private void cursorMoment(TextField textField) {

        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = textField.getText().trim();
                if (text.isEmpty()) {
                    textField.requestFocus();
                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initShortcutKeys();

        tfPatientCreateName.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN) {
                tffieldSearch.requestFocus();
                event.consume();
            }
        });

        tfPatientCreateName.setOnKeyPressed(event->{

            if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                if (tfPatientCreateName.getText().isEmpty()){
                    tfPatientCreateName.requestFocus();
                    event.consume();
                }

            }
        });

        tffieldSearch.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN) {
                tcPatientView.getSelectionModel().selectFirst();
                tcPatientView.requestFocus();
                event.consume();
            }
        });


        CommonValidationsUtils.onlyEnterNumbers(tfPatientCreateIdNo);
        CommonValidationsUtils.onlyEnterNumbersLimit(tfPatientCreateAge,2);
        CommonValidationsUtils.onlyEnterCharactersLimit(tfPatientCreateBloodGrp,3);
        CommonValidationsUtils.restrictToDecimalNumbers(tfPatientCreateWeight);
        cursorMomentForComboBox(cmbPatientCreateGender);
        //? Pincode Validation Enter Only 6 Numbers
        CommonValidationsUtils.onlyEnterNumbersLimit(tfPatientCreatePin,6);
        CommonFunctionalUtils.restrictTextFieldToDigitsAndBirthDateFormat(dpPatientCreateBirthDate);
        CommonFunctionalUtils.restrictTextFieldToDigitsAndDateFormat(dpPatientCreateTIDate);
        CommonFunctionalUtils.restrictTextFieldToDigitsAndDateFormat(dpPatientCreateDignosisDate);
        CommonValidationsUtils.changeStarColour(boxV);
        CommonValidationsUtils.configureTextFieldToTitleCase(tfPatientCreateName);
        CommonValidationsUtils.configureTextFieldToTitleCase(tfPatientCreateAddress);

        tcPatientName.prefWidthProperty().bind(tcPatientView.widthProperty().multiply(0.19));
        tcPatientAddress.prefWidthProperty().bind(tcPatientView.widthProperty().multiply(0.19));
        tcMobileNo.prefWidthProperty().bind(tcPatientView.widthProperty().multiply(0.19));
        tcPincode.prefWidthProperty().bind(tcPatientView.widthProperty().multiply(0.19));
        tcGender.prefWidthProperty().bind(tcPatientView.widthProperty().multiply(0.19));
//        tcAction.prefWidthProperty().bind(tcPatientView.widthProperty().multiply(0.16));

        getPatient();
        btnPatientCreateSubmit.setOnAction(actionEvent -> {
            if (CommonValidationsUtils.validateForm(tfPatientCreateName, tfPatientCreateMobileNo)) {
                createPatient();
            }
        });

        TextField[] textFields = {tfPatientCreateMobileNo};
        for (TextField textField : textFields) {
            cursorMoment(textField);
        }



        tcPatientView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                System.out.println("souit");
                setEditData();
            }
        });

//        tcPatientView.setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.ENTER) {
//                System.out.println("souit");
//                rowIndex = tcPatientView.getSelectionModel().getSelectedIndex();
//                setEditData();
//                event.consume();
//            }
//        });

        tcPatientView.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            System.out.println("Key Pressed Event Triggered");
            if (event.getCode() == KeyCode.ENTER) {
                System.out.println("Enter Key Pressed");
                setEditData();
                System.out.println("Event Consumed");
                event.consume();
            }
        });

        CommonValidationsUtils.restrictMobileNumber(tfPatientCreateMobileNo);

        originalData = tcPatientView.getItems();
        tffieldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });

        DateValidator.applyDateFormat(dpPatientCreateBirthDate);
        DateValidator.applyDateFormat(dpPatientCreateDignosisDate);
        DateValidator.applyDateFormat(dpPatientCreateTIDate);
        sceneInitilization();

        handleRadioButtonAction();

        tfPatientCreatePin.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (!tfPatientCreatePin.getText().isEmpty()) {
                    validatePincode();
                }
            }
        });


//
        dpPatientCreateBirthDate.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                birthdateValidate();

            }
        });


        dpPatientCreateDignosisDate.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                diagnosisValidate();

            }
        });




        // Add a key event listener to the text field
        btnPatientCreateSubmit.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                btnPatientCreateCancel.requestFocus();
                event.consume();
            }
        });

        cmbPatientCreateGender.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    cmbPatientCreateGender.show();
                }
            }
        });
        //short cut key code function call
//        shortcutKeysCmpAdm();

        Platform.runLater(() -> tfPatientCreateName.requestFocus());

        //Create Payment Mode Function
        btnPatientCreateSubmit.setOnAction(e -> {
            if (CommonValidationsUtils.validateForm(tfPatientCreateName, tfPatientCreateMobileNo,cmbPatientCreateGender)) {
                createPatient();
            }
        });

    }
    public void backToList() {
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnClear, input -> {
            if (input == 1) {
                clearData();

            }
        });
    }



    //? to set the Gender values if Male set as true and if Female set as False
    @FXML
    public void handleRadioButtonAction() {
        // Initialize the ComboBox with options
        ObservableList<String> options = FXCollections.observableArrayList(
                "Male",
                "Female"
        );
        cmbPatientCreateGender.setItems(options);
//        Boolean val = rbFranchiseCreateMale.isSelected();
//        Boolean valF = rbFranchiseCreateFemale.isSelected();
//        if (val) {
//            isGender = "true";
//        }
//        if (valF) {
//            isGender = "false";
//        }
    }

    private void cursorMomentForComboBox(ComboBox<?> comboBox) {
        comboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("comboBox.getSelectionModel().getSelectedIndex()"+comboBox.getSelectionModel().getSelectedIndex());
            if (!newVal) {
                if (comboBox.getSelectionModel().getSelectedIndex() == -1) {
                    // No item selected in the ComboBox
                    comboBox.requestFocus();
                }
            }
        });
    }

    private void birthdateValidate(){
        if(!dpPatientCreateBirthDate.getText().isEmpty()){
            LocalDate currentDate = LocalDate.now();
            String brDate = dpPatientCreateBirthDate.getText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate brtDate = null;
            if(!brDate.isEmpty()) {
                brtDate = LocalDate.parse(brDate, formatter);
            }
            if (currentDate.isBefore(brtDate)) {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Birth Date Should be Valid", input -> {
                    dpPatientCreateBirthDate.requestFocus();
                });
            }
        }
    }

    private void diagnosisValidate(){
        if(!dpPatientCreateDignosisDate.getText().isEmpty()){
            LocalDate currentDate = LocalDate.now();
            String brDate = dpPatientCreateDignosisDate.getText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate brtDate = null;
            if(!brDate.isEmpty()) {
                brtDate = LocalDate.parse(brDate, formatter);
            }
            if (currentDate.isBefore(brtDate)) {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Diagnosis Date Should be Valid", input -> {
                    dpPatientCreateDignosisDate.requestFocus();
                });
            }
        }
    }
//    public void handleCmbPacking(ActionEvent actionEvent) {
//        Object pack[] = new Object[1];
//        pack[0] = cmbPack.getSelectionModel().getSelectedItem();
//        if (pack[0] != null) {
//
//            for (CommonDTO commonDTO : packingNames) {
//                if (pack[0].equals(commonDTO)) {
//                    cmbPackingId = Long.valueOf(commonDTO.getId());
//                }
//            }
//        }
//    }

    @FXML
    private void handleComboBoxActionGender() {
        if (cmbPatientCreateGender.getSelectionModel().getSelectedItem() != null) {
            if(cmbPatientCreateGender.getSelectionModel().getSelectedItem()=="Male") {
                isGender = "true";
            }
            else {
                isGender="false";
            }
            System.out.println("i am gender"+isGender);
        } else {
            //?  use logger for alert messages
            patientlogger.error("Please Select Proper Data");
        }
    }

    public void validatePincode() {
        APIClient apiClient = null;
        try {
            patientlogger.debug("Get ValidatePincode Data Started...");
            Map<String, String> map = new HashMap<>();
            map.put("pincode", String.valueOf(tfPatientCreatePin.getText()));
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
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                            tfPatientCreatePin.requestFocus();
                        });
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    patientlogger.error("Network API cancelled in validatePincode()" + workerStateEvent.getSource().getValue().toString());
                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    patientlogger.error("Network API failed in validatePincode()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            patientlogger.debug("Get ValidatePincode Data End...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }


    }


    public void sceneInitilization() {
        spRootPatientPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }


    private void filterData(String keyword) {
        ObservableList<PatientDTO> filteredData = FXCollections.observableArrayList();

        if (keyword.isEmpty()) {
            tcPatientView.setItems(originalData);
            return;
        }

        for (PatientDTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tcPatientView.setItems(filteredData);
    }

    //Search Function to Search in the Table for columns
    private boolean matchesKeyword(PatientDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        // Check if any of the columns contain the keyword
        return item.getPatientName().toLowerCase().contains(lowerCaseKeyword) ||
                item.getGender().toLowerCase().contains(lowerCaseKeyword) ||
                item.getPatientAddress().toLowerCase().contains(lowerCaseKeyword) ||
                item.getMobileNo().toLowerCase().contains(lowerCaseKeyword) ||
                item.getPincode().toLowerCase().contains(lowerCaseKeyword);
    }


    public void setEditData() {
        rowIndex = tcPatientView.getSelectionModel().getSelectedIndex();
        PatientDTO selectedItem = tcPatientView.getSelectionModel().getSelectedItem();
        System.out.println("PatientDTO-->" + selectedItem.getId());
        id = selectedItem.getId();
        tfPatientCreateName.setText(selectedItem.getPatientName());
        tfPatientCreateAge.setText(selectedItem.getAge());
        tfPatientCreateWeight.setText(selectedItem.getWeight());
        tfPatientCreateAddress.setText(selectedItem.getPatientAddress());
        tfPatientCreateMobileNo.setText(selectedItem.getMobileNo());
        tfPatientCreateIdNo.setText(selectedItem.getIdNo());
        String gender = selectedItem.getGender();
        if (gender.equals("true")) {
            cmbPatientCreateGender.getSelectionModel().select(0);
            isGender = "true";
        } else {
            cmbPatientCreateGender.getSelectionModel().select(1);
            isGender = "false";
        }

        tfPatientCreateBloodGrp.setText(selectedItem.getBloodGroup());
        LocalDate date = LocalDate.parse(selectedItem.getBirthDate());

        dpPatientCreateBirthDate.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        tfPatientCreatePin.setText(selectedItem.getPincode());
        LocalDate diagnosisDate = LocalDate.parse(selectedItem.getTbDiagnosisDate());
        LocalDate treatmentInitiationDate = LocalDate.parse(selectedItem.getTbTreatmentInitiationDate());

        dpPatientCreateDignosisDate.setText(diagnosisDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//                    dpPatientCreateTIDate.setValue(treatmentInitiationDate);
        dpPatientCreateTIDate.setText(treatmentInitiationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        btnPatientCreateSubmit.setText("Update");
        tfPatientCreateName.requestFocus();

    }

    //Create Patient
    public void createPatient() {
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1){
        APIClient apiClient = null;
        String patientName = tfPatientCreateName.getText();
        String patientAge = tfPatientCreateAge.getText();
        String patientWeight = tfPatientCreateWeight.getText();
        String patientAddress = tfPatientCreateAddress.getText();
        String patientMobileNo = tfPatientCreateMobileNo.getText();
        String patientIdNo = tfPatientCreateIdNo.getText();

        String patientPincode = tfPatientCreatePin.getText();
        patientPincode = patientPincode.isEmpty() ? "" : patientPincode;

        String patientBloodGrp = tfPatientCreateBloodGrp.getText();
        patientBloodGrp = patientBloodGrp.isEmpty() ? "" : patientBloodGrp;

        String patientBirthDate = dpPatientCreateBirthDate.getText();
        if (patientBirthDate.isEmpty()) {
            patientBirthDate = "";
        } else {
            patientBirthDate = Communicator.text_to_date.fromString(dpPatientCreateBirthDate.getText()).toString();
        }
        String patientDignosisDate = dpPatientCreateDignosisDate.getText();
        if (patientDignosisDate.isEmpty()) {
            patientDignosisDate = "";
        } else {
            patientDignosisDate = Communicator.text_to_date.fromString(dpPatientCreateDignosisDate.getText()).toString();
        }
        String patientTIDate = dpPatientCreateTIDate.getText();
        if (patientTIDate.isEmpty()) {
            patientTIDate = "";
        } else {
            patientTIDate = Communicator.text_to_date.fromString(dpPatientCreateTIDate.getText()).toString();
        }


        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("patientName", patientName);
        map.put("age", patientAge);
        map.put("weight", patientWeight);
        map.put("patientAddress", patientAddress);
        map.put("mobileNumber", patientMobileNo);
        map.put("birthDate", patientBirthDate);
        map.put("idNo", patientIdNo);
        map.put("gender", String.valueOf(isGender));
        map.put("pincode", patientPincode);
        map.put("bloodGroup", patientBloodGrp);
        map.put("tbDiagnosisDate", patientDignosisDate);
        map.put("tbTreatmentInitiationDate", patientTIDate);

        HttpResponse<String> response;
        String formData = Globals.mapToStringforFormData(map);
        System.out.println("formData" + formData);

        if (id.equalsIgnoreCase("")) {
            /// response = APIClient.postFormDataRequest(formData,PATIENT_CREATE_ENDPOINT);
            apiClient = new APIClient(EndPoints.PATIENT_CREATE_ENDPOINT, formData, RequestType.FORM_DATA);


        } else {
            //response = APIClient.postFormDataRequest(formData,PATIENT_UPDATE_ENDPOINT);
            apiClient = new APIClient(EndPoints.PATIENT_UPDATE_ENDPOINT, formData, RequestType.FORM_DATA);
            id = "";

        }
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                System.out.println("Response=>" + jsonObject);
                message = jsonObject.get("message").getAsString();

                if (jsonObject.get("responseStatus").getAsInt() == 200) {
                    if (btnPatientCreateSubmit.getText().equalsIgnoreCase("update")) {
                        focusInd = "update";
                    } else {
                        focusInd = "focus";
                    }

                    AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, e -> {
                        getPatient();
                        tfPatientCreateName.setText("");
                        tfPatientCreateAge.setText("");
                        tfPatientCreateWeight.setText("");
                        tfPatientCreateAddress.setText("");
                        tfPatientCreateMobileNo.setText("");
                        tfPatientCreateIdNo.setText("");
                        tfPatientCreateBloodGrp.setText("");
                        dpPatientCreateBirthDate.setText("");
                        tfPatientCreatePin.setText("");
                        dpPatientCreateDignosisDate.setText("");
                        dpPatientCreateTIDate.setText("");
                        cmbPatientCreateGender.getSelectionModel().clearSelection();
                    });
                }
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                patientlogger.error("Network API cancelled in createPatient()" + workerStateEvent.getSource().getValue().toString());

            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                patientlogger.error("Network API Failed in createPatient()" + workerStateEvent.getSource().getValue().toString());

            }
        });
        apiClient.start();
        patientlogger.debug("Create Patient Data End...");
    }else {
            System.out.println("working!");
        }
    };
        if (btnPatientCreateSubmit.getText().equalsIgnoreCase("submit")) {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnSubmit , callback);
        } else {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnUpdate, callback);
        }

    }

    //Get App Patient Data
    public void getPatient() {
        APIClient apiClient = null;
        try {
            tcPatientView.getItems().clear();

            // HttpResponse<String> response = APIClient.getRequest(PATIENT_LIST_ENDPOINT);
            apiClient = new APIClient(EndPoints.PATIENT_LIST_ENDPOINT, "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    //JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

                    System.out.println("" + jsonObject);
                    ObservableList<PatientDTO> observableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray responseObject = jsonObject.getAsJsonArray("responseObject");

                        if (responseObject.size() > 0) {
                            for (JsonElement element : responseObject) {
                                JsonObject item = element.getAsJsonObject();
                                String id = item.get("id").getAsString();
                                String patientName = item.get("patientName").getAsString();
                                String age = item.get("age")!=null?item.get("age").getAsString():"";
                                String patientAddress = item.get("patientAddress").getAsString();
                                String mobileNumber = item.get("mobileNumber").getAsString();
                                String birthDate = item.get("birthDate").getAsString();
                                String idNo = item.get("idNo").getAsString();

                                Boolean gender1 = Boolean.valueOf(item.get("gender").getAsString());
                                String gender = gender1==true?"Male":"Female";
                                String bloodGroup = item.get("bloodGroup").getAsString();
                                String patientCode = item.get("patientCode").getAsString();
                                String pincode = item.get("pincode").getAsString();
                                String weight = item.get("weight").getAsString();
                                String tbDiagnosisDate = item.get("TbDiagnosisDate").getAsString();
                                String tbTreatmentInitiationDate = item.get("TbTreatmentInitiationDate").getAsString();


                                observableList.add(new PatientDTO(id, patientName, patientCode,
                                        patientAddress, mobileNumber, pincode, gender, age, birthDate, idNo, bloodGroup, weight, tbDiagnosisDate, tbTreatmentInitiationDate)
                                );
                            }
                            tcPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
                            tcPatientAddress.setCellValueFactory(new PropertyValueFactory<>("patientAddress"));
                            tcMobileNo.setCellValueFactory(new PropertyValueFactory<>("mobileNo"));
                            tcPincode.setCellValueFactory(new PropertyValueFactory<>("pincode"));
                            tcGender.setCellValueFactory(new PropertyValueFactory<>("gender"));

                            tcPatientView.setItems(observableList);

                        } else {
                            System.out.println("responseObject is null");
                        }
                        if (focusInd.equalsIgnoreCase("focus")) {
                            tcPatientView.getSelectionModel().select(responseObject.size() - 1);
                            tcPatientView.scrollTo(responseObject.size() - 1);
                            tcPatientView.requestFocus();
                            focusInd = "";
                        } else if (focusInd.equalsIgnoreCase("update")) {
                            tcPatientView.getSelectionModel().select(rowIndex);
                            tcPatientView.scrollTo(rowIndex);
                            tcPatientView.requestFocus();
                            focusInd = "";
                            btnPatientCreateSubmit.setText("Submit");
                        }

                    } else {
                        System.out.println("Error in response");
                    }
                }
            });

            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    patientlogger.error("Network API cancelled in getPatient()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    patientlogger.error("Network API cancelled in getPatient()" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.start();
            patientlogger.debug("Get Patient data end");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    public void clearData() {
        tfPatientCreateName.setText("");
        tfPatientCreateAge.setText("");
        tfPatientCreateWeight.setText("");
        tfPatientCreateAddress.setText("");
        tfPatientCreateMobileNo.setText("");
        tfPatientCreateIdNo.setText("");
        tfPatientCreateBloodGrp.setText("");
        dpPatientCreateBirthDate.setText("");
        tfPatientCreatePin.setText("");
        dpPatientCreateDignosisDate.setText("");
        dpPatientCreateTIDate.setText("");
        cmbPatientCreateGender.getSelectionModel().clearSelection();
        id = "";
        btnPatientCreateSubmit.setText("Submit");
        tfPatientCreateName.requestFocus();

    }

//    private void shortcutKeysCmpAdm() {
//        spRootPatientPane.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
//
//            @Override
//            public void handle(KeyEvent event) {
//                if (event.getCode() == KeyCode.S && event.isControlDown()) {
//                    if (CommonValidationsUtils.validateForm(tfPatientCreateName, tfPatientCreateMobileNo,cmbPatientCreateGender)) {
//
//                        if (btnPatientCreateSubmit.getText().equalsIgnoreCase("submit")) {
//                            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Submit", input -> {
//                                if (input == 1) {
//                                    createPatient();
//                                }
//                            });
//                        } else {
//                            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Update", input -> {
//                                if (input == 1) {
//                                    createPatient();
//                                }
//                            });
//                        }
//                    }
//                }
//                if (event.getCode() == KeyCode.E && event.isControlDown()) {
//                    if (!tcPatientView.getSelectionModel().isEmpty()) {
////                        PatientDTO selectedItem = (pat) tcPatientView.getSelectionModel().getSelectedItem();
////                        selectedId = selectedItem.getId();
////                        get_company_admin_by_id(selectedId);
//                        setEditData();
//                        event.consume();
//                    } else {
//                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Select Patient", in -> {
//                            tcPatientView.getSelectionModel().selectFirst();
//                            tcPatientView.requestFocus();
//                            event.consume();
//                        });
//                    }
//                }
//
//
//                if (event.getCode() == KeyCode.X && event.isControlDown()) {
//                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Clear", input -> {
//                        if (input == 1) {
//                            clearData();
//                        }
//                    });
//                }
//
//            }
//        });
//    }

    private void initShortcutKeys() {
        spRootPatientPane.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {

            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("submit")) {
                    System.out.println(targetButton.getText());
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("Clear")) {
                    System.out.println(targetButton.getText());
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("update")) {
                    System.out.println(targetButton.getText());
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
            }else if (event.getCode() == KeyCode.S && event.isControlDown()) {
                btnPatientCreateSubmit.fire();
            } else if (event.getCode() == KeyCode.X && event.isControlDown()) {
                btnPatientCreateCancel.fire();
            }else if (event.getCode() == KeyCode.DOWN && tffieldSearch.isFocused()) {
                tcPatientView.getSelectionModel().select(0);
                tcPatientView.requestFocus();
            } else if (event.getCode() == KeyCode.DOWN && tfPatientCreateName.isFocused()) {
                tcPatientView.getSelectionModel().select(0);
                tcPatientView.requestFocus();
            } else if (event.getCode() == KeyCode.E && event.isControlDown()) {
                setEditData();
            } else if (event.getCode() == KeyCode.D && event.isControlDown()) {
                handleDeletePressed();
            } else if (event.getCode() == KeyCode.N && event.isControlDown()) {
                tfPatientCreateName.requestFocus();
            }
        });
    }

    public void deletePatient() {
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                APIClient apiClient = null;
                Map<String, String> map = new HashMap<>();
                map.put("id", id);

                HttpResponse<String> response;
                String formData = Globals.mapToStringforFormData(map);
                apiClient = new APIClient(EndPoints.PATIENT_DELETE_ENDPOINT, formData, RequestType.FORM_DATA);

                apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        // JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                        jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

                        message = jsonObject.get("message").getAsString();

                        if (jsonObject.get("responseStatus").getAsInt() == 200) {
                            AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                                tfPatientCreateName.requestFocus();
                                id = "";
                            });
                        } else {
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                            });
                        }
                        getPatient();
                        clearData();

                    }
                });

                apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        patientlogger.error("Network API cancelled in deletePatient()" + workerStateEvent.getSource().getValue().toString());

                    }
                });
                apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        patientlogger.error("Network API failed in deletePatient()" + workerStateEvent.getSource().getValue().toString());

                    }
                });
                apiClient.start();
                patientlogger.debug("delete patient data end ...");
            } else {
            }
        };
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnDelete, callback);
    }
    @FXML
    private void handleDeletePressed() {
        PatientDTO selectedItem = tcPatientView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            id = selectedItem.getId();
            deletePatient();
        }
    }
}
