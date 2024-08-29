package com.opethic.genivis.controller.master;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.dto.AreaHeadListDTO;
import com.opethic.genivis.dto.CourierServiceDTO;
import com.opethic.genivis.dto.DoctorMasterDTO;
import com.opethic.genivis.dto.FranchiseListDTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.FxmFileConstants.AREA_HEAD_LIST_SLUG;

public class DoctorMasterController implements Initializable {

//    @FXML
//    private TableColumn<PatientDTO,String> tfCourierServiceServiceName;
private static final Logger doctorlogger = LogManager.getLogger(DoctorMasterController.class);
    @FXML
    private TextField tfDoctorMasterDoctorName, tfDoctorMasterHospitalName,
            tfDoctorMasterHospitalAddress,
            tfDoctorMasterMobileNumber,
            tfDoctorMasterSpecialization,
            tfDoctorMasterQualification,
            tfDoctorMasterRegisteredNo,
            tfDoctorMasterCommision;
    @FXML
    private VBox boxV;
    @FXML
    private TextField tffieldSearch;

//   @FXML
//    private TextField tfDoctorMasterDoctorName;
//   @FXML
//    private TextField tfDoctorMasterCommision;


    @FXML
    private TableColumn<DoctorMasterDTO, String> tcDoctorName;
    @FXML
    private TableColumn<DoctorMasterDTO, String> tcHospitalName;
    @FXML
    private TableColumn<DoctorMasterDTO, String> tcSpecialization;
    @FXML
    private TableColumn<DoctorMasterDTO, String> tcMobileNo;

    @FXML
    private TableColumn<DoctorMasterDTO, String> tcCommision;
    @FXML
    private TableColumn<DoctorMasterDTO, String> tcQualification;
    @FXML
    private TableColumn<DoctorMasterDTO, String> tcRegisterNo;
    @FXML
    private TableColumn<DoctorMasterDTO, String> tcHospitalAddress;


    @FXML
    private TableView<DoctorMasterDTO> tcDoctorMasterView;


//    @FXML
//    private TableColumn<CourierServiceDTO,String> tcServiceName;
//   @FXML
//    private TableColumn<CourierServiceDTO,String> tcContactPerson;
//   @FXML
//    private TableColumn<CourierServiceDTO,String> tcContactNo;
//   @FXML
//    private TableColumn<CourierServiceDTO,String> tcServiceAddress;


    public String id = "";
    String message = "";
    private JsonObject jsonObject = null;
    private Node[] focusableNodes;
    private HttpResponse<String> response;


    private ObservableList<DoctorMasterDTO> originalData;

    @FXML
    private Button btnDoctorMasterCreateSubmit, btnDoctorMasterCreateClear;

    @FXML
    private BorderPane spRootDoctorMasterServicePane;

    String s = "Create";
    private String focusInd = "";
    private Integer rowIndex;


    private boolean matchesKeyword(DoctorMasterDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        // Check if any of the columns contain the keyword
        return item.getDoctorName().toLowerCase().contains(lowerCaseKeyword) ||
                item.getHospitalName().toLowerCase().contains(lowerCaseKeyword) ||
                item.getHospitalAddress().toLowerCase().contains(lowerCaseKeyword) ||
                item.getMobileNumber().toLowerCase().contains(lowerCaseKeyword) ||
                item.getSpecialization().toLowerCase().contains(lowerCaseKeyword);
    }


    private void filterData(String keyword) {
        ObservableList<DoctorMasterDTO> filteredData = FXCollections.observableArrayList();

        if (keyword.isEmpty()) {
            tcDoctorMasterView.setItems(originalData);
            return;
        }

        for (DoctorMasterDTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tcDoctorMasterView.setItems(filteredData);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //? this include all the Shortcut Keys
        initShorcutKeys();

        //? this inculde all the design related properties and important Fields
        mandatoryFields();

        //? this include all the table view operations - Edit , search , etc..
        tableViewOperations();

        tfDoctorMasterDoctorName.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN) {
                tffieldSearch.requestFocus();
                event.consume();
            }
        });

        tfDoctorMasterDoctorName.setOnKeyPressed(event->{

            if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                if (tfDoctorMasterDoctorName.getText().isEmpty()){
                    tfDoctorMasterDoctorName.requestFocus();
                    event.consume();
                }

            }
        });

        tffieldSearch.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN) {
                tcDoctorMasterView.getSelectionModel().selectFirst();
                tcDoctorMasterView.requestFocus();
                event.consume();
            }
        });
        tcDoctorMasterView.setFocusTraversable(false);

        Platform.runLater(() -> tfDoctorMasterDoctorName.requestFocus());




        getDoctorMaster();
//        btnDoctorMasterCreateSubmit();

        btnDoctorMasterCreateSubmit.setOnAction(e -> {
            if (CommonValidationsUtils.validateForm(tfDoctorMasterDoctorName, tfDoctorMasterMobileNumber, tfDoctorMasterSpecialization, tfDoctorMasterQualification)) {
                createDoctorMasterService();
            }
        });


        TextField[] textFields = {tfDoctorMasterDoctorName, tfDoctorMasterMobileNumber, tfDoctorMasterSpecialization, tfDoctorMasterQualification};
        for (TextField textField : textFields) {
            cursorMoment(textField);
        }


        ResponsiveWiseCssPicker();
        CommonValidationsUtils.changeStarColour(boxV);
//        nextElement();

        originalData = tcDoctorMasterView.getItems();
        tffieldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });

        tcDoctorMasterView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                System.out.println("ia m in 2");
                setEditData();
            }
        });
        tcDoctorMasterView.setOnKeyPressed(event -> {
            System.out.println(event);
            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) {
                setEditData();
            }
        });


        tfDoctorMasterMobileNumber.setOnKeyPressed(event -> {
            System.out.println(event);
            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) {
                validateDuplication();
            }
        });


    }

    private void initShorcutKeys() {
        spRootDoctorMasterServicePane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {

            if (event.getCode() == KeyCode.DOWN && tffieldSearch.isFocused()) {
                tcDoctorMasterView.getSelectionModel().select(0);
                tcDoctorMasterView.requestFocus();
            } else if (event.getCode() == KeyCode.DOWN && tfDoctorMasterDoctorName.isFocused()) {
                tcDoctorMasterView.getSelectionModel().select(0);
                tcDoctorMasterView.requestFocus();
            }

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
                btnDoctorMasterCreateSubmit.fire();
            }else if (event.getCode() == KeyCode.X && event.isControlDown()) {
                btnDoctorMasterCreateClear.fire();
            }else if (event.getCode() == KeyCode.E && event.isControlDown()) {
                setEditData();
            }else if (event.getCode() == KeyCode.DOWN && tffieldSearch.isFocused()) {
                tcDoctorMasterView.getSelectionModel().select(0);
                tcDoctorMasterView.requestFocus();
            } else if (event.getCode() == KeyCode.DOWN && tfDoctorMasterDoctorName.isFocused()) {
                tcDoctorMasterView.getSelectionModel().select(0);
                tcDoctorMasterView.requestFocus();
            }else if (event.getCode() == KeyCode.D && event.isControlDown()) {
                handleDeletePressed();
            }else if (event.getCode() == KeyCode.N && event.isControlDown()) {
                tfDoctorMasterDoctorName.requestFocus();
            }
//            if (event.getCode() == KeyCode.D && event.isControlDown()) {
//                setDeleteData();
//            }

        });
    }

    private void mandatoryFields() {
        CommonValidationsUtils.changeStarColour(boxV);
        CommonFunctionalUtils.restrictTextField(tfDoctorMasterCommision);
        // Add listener for focus change to restrict Mobile Number and Email when cursor moves away
        CommonValidationsUtils.restrictMobileNumber(tfDoctorMasterMobileNumber);
    }
    private void tableViewOperations() {
        tcDoctorName.prefWidthProperty().bind(tcDoctorMasterView.widthProperty().multiply(0.2));
        tcSpecialization.prefWidthProperty().bind(tcDoctorMasterView.widthProperty().multiply(0.2));
        tcMobileNo.prefWidthProperty().bind(tcDoctorMasterView.widthProperty().multiply(0.2));
        tcHospitalName.prefWidthProperty().bind(tcDoctorMasterView.widthProperty().multiply(0.2));
        tcHospitalAddress.prefWidthProperty().bind(tcDoctorMasterView.widthProperty().multiply(0.2));
    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            spRootDoctorMasterServicePane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            spRootDoctorMasterServicePane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            spRootDoctorMasterServicePane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            spRootDoctorMasterServicePane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            spRootDoctorMasterServicePane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            spRootDoctorMasterServicePane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            spRootDoctorMasterServicePane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

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

    public void setEditData() {
        rowIndex = tcDoctorMasterView.getSelectionModel().getSelectedIndex();
        DoctorMasterDTO selectedItem = tcDoctorMasterView.getSelectionModel().getSelectedItem();
        System.out.println("tcDoctorMasterView-->" + selectedItem.getId());
        id = String.valueOf(selectedItem.getId());
        if (selectedItem != null) {

            tfDoctorMasterDoctorName.setText(selectedItem.getDoctorName());
            tfDoctorMasterHospitalName.setText(selectedItem.getHospitalName());
            tfDoctorMasterHospitalAddress.setText(selectedItem.getHospitalAddress());
            tfDoctorMasterSpecialization.setText(selectedItem.getSpecialization());
            tfDoctorMasterMobileNumber.setText(selectedItem.getMobileNumber());
            tfDoctorMasterCommision.setText(selectedItem.getCommision());
            tfDoctorMasterQualification.setText(selectedItem.getQualification());
            tfDoctorMasterRegisteredNo.setText(selectedItem.getRegisterNo());

            btnDoctorMasterCreateSubmit.setText("Update");
            s = "Update";
            tfDoctorMasterDoctorName.requestFocus();


        }
    }

    public void cancelDoctorMasterService() {

        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                id = "";
                tfDoctorMasterCommision.setText("");
                tfDoctorMasterDoctorName.setText("");
                tfDoctorMasterQualification.setText("");
                tfDoctorMasterHospitalAddress.setText("");
                tfDoctorMasterRegisteredNo.setText("");
                tfDoctorMasterHospitalName.setText("");
                tfDoctorMasterMobileNumber.setText("");
                tfDoctorMasterSpecialization.setText("");

                btnDoctorMasterCreateSubmit.setText("Submit");
                s="Create";
            } else {
                System.out.println("working!");
            }
        };
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnClear, callback);
    }

    public void createDoctorMasterService() {
        if (!validateDuplication()) {

            String doctorName = tfDoctorMasterDoctorName.getText();
            String hospitalName = tfDoctorMasterHospitalName.getText();
            String hospitalAddress = tfDoctorMasterHospitalAddress.getText();
            String mobileNumber = tfDoctorMasterMobileNumber.getText();
            String specialization = tfDoctorMasterSpecialization.getText();
            String qualification = tfDoctorMasterQualification.getText();
            String registerNo = tfDoctorMasterRegisteredNo.getText();
            String commision = tfDoctorMasterCommision.getText();

            if (!doctorName.isBlank() && !mobileNumber.isBlank() && !specialization.isBlank() && !qualification.isBlank()) {
                Map<String, String> map = new HashMap<>();
                map.put("id", id);
                map.put("doctorName", doctorName);
                map.put("hospitalName", hospitalName);
                map.put("specialization", specialization);
                map.put("hospitalAddress", hospitalAddress);
                map.put("mobileNumber", mobileNumber);
                map.put("commision", commision);
                map.put("qualification", qualification);
                map.put("registerNo", registerNo);

                String formData = Globals.mapToStringforFormData(map);
                System.out.println("formData: " + formData);

                AlertUtility.CustomCallback callback = number -> {
                    if (number == 1) {
                        s = "Create";
                        HttpResponse<String> response;
                        if (id.isEmpty()) {
                            response = APIClient.postFormDataRequest(formData, "create_doctor_master");
                        } else {
                            response = APIClient.postFormDataRequest(formData, "update_doctor_master");
                        }

                        if (response != null) {
                            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                            System.out.println("Response => " + responseBody);
                            String message = responseBody.get("message").getAsString();

                            if (responseBody.get("responseStatus").getAsInt() == 200) {
//                                AlertUtility.CustomCallback callbackq = numberq -> {
//                                    if (numberq == 1) {
//                                        System.out.println("done");
//
//
//                                        getDoctorMaster();
//                                        if (id != null && !id.isEmpty()) {
//                                            // Parse the ID to an integer
//                                            String targetId = id;
//                                            // Find the row index based on the ID
//                                            ObservableList<DoctorMasterDTO> items = tcDoctorMasterView.getItems();
//                                            int targetIndex = -1;
//
//                                            for (int i = 0; i < items.size(); i++) {
//                                                DoctorMasterDTO item = items.get(i);
//                                                if (targetId.equals(item.getId())) { // Replace getId() with your actual ID getter
//                                                    targetIndex = i;
//                                                    break;
//                                                }
//                                            }
//                                            // If a matching row is found, select it
//                                            if (targetIndex != -1) {
//                                                tcDoctorMasterView.getSelectionModel().select(targetIndex);
//                                            } else {
//                                                // Handle the case where the ID is not found, if necessary
//                                                System.out.println("ID not found in the table");
//                                            }
//                                        } else {
//                                            int lastIndex = tcDoctorMasterView.getItems().size() - 1;
//                                            if (lastIndex >= 0) {
//                                                tcDoctorMasterView.getSelectionModel().select(lastIndex);
//                                            }
//                                        }
//
//                                        btnDoctorMasterCreateSubmit.setText("Submit");
//
//                                        id = "";
//
////                                    response = APIClient.postMultipartRequest(map, finalFileMap2, EndPoints.updateAreaHead, headers);
////                                    GlobalController.getInstance().addTabStatic(AREA_HEAD_LIST_SLUG, false);
//                                    } else {
//                                        System.out.println("working!");
//                                    }
//                                };
//                                AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeConfirmation, responseBody.get("message").getAsString(), callbackq);
                                if (btnDoctorMasterCreateSubmit.getText().equalsIgnoreCase("update")) {
                                    focusInd = "update";
                                } else {
                                    focusInd = "focus";
                                }
                                AlertUtility.AlertSuccessTimeout( AlertUtility.alertTypeSuccess, message, e->{
                                    getDoctorMaster();
                                    clearDoctorMasterFields();
                                });

                            }
                        }
                    } else {
                        System.out.println("Working!");
                    }
                };

                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to "+s+"?", callback);
            }
        }

    }

    private boolean validateDuplication() {
        // Get all items from the TableView
        ObservableList<DoctorMasterDTO> items = tcDoctorMasterView.getItems();

        // Iterate over the items and compare
        for (int i = 0; i < items.size(); i++) {
            //            for (int j = i + 1; j < items.size(); j++) {
            DoctorMasterDTO item1 = items.get(i);
            //                DoctorMasterDTO item2 = items.get(j);

            System.out.println(!id.matches(String.valueOf(item1.getId())));
            boolean cndn=item1.getMobileNumber().equals(tfDoctorMasterMobileNumber.getText());
            // Perform your comparison logic here
            if(id.matches(String.valueOf(item1.getId()))){
                System.out.println("not a duplicate");
            }else{
                if (cndn) {
                    // Handle duplication case
                    tfDoctorMasterMobileNumber.requestFocus();

                    System.out.println("Duplicate item found: " + item1);
                    //                    tfDoctorMasterMobileNumber.requestFocus();
                    AlertUtility.AlertErrorTimeout("Error", "Duplicate Record", callback -> {
                        tfDoctorMasterMobileNumber.requestFocus();
                    });
                    return true;
                }
            }

            //            }
        }
        return false;
    }

    private void clearDoctorMasterFields() {
        tfDoctorMasterDoctorName.setText("");
        tfDoctorMasterHospitalName.setText("");
        tfDoctorMasterHospitalAddress.setText("");
        tfDoctorMasterMobileNumber.setText("");
        tfDoctorMasterSpecialization.setText("");
        tfDoctorMasterQualification.setText("");
        tfDoctorMasterCommision.setText("");
        tfDoctorMasterRegisteredNo.setText("");
        s="Create";
    }

    public void getDoctorMaster() {
        try {
            tcDoctorMasterView.getItems().clear();
            HttpResponse<String> response = APIClient.getRequest("get_all_doctor_master");
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println(jsonObject);
            ObservableList<DoctorMasterDTO> observableList = FXCollections.observableArrayList();
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseObject = jsonObject.getAsJsonArray("responseObject");


                if (responseObject.size() > 0) {
                    for (JsonElement element : responseObject) {
                        JsonObject item = element.getAsJsonObject();


                        int id = item.get("id").getAsInt();
                        String doctorName = item.get("doctorName").getAsString();
                        String hospitalName = item.get("hospitalName").getAsString();
                        String hospitalAddress = item.get("hospitalAddress").getAsString();
                        String mobileNumber = item.get("mobileNumber").getAsString();
                        String specialization = item.get("specialization").getAsString();
//
                        String qualification = item.get("qualification").getAsString();
                        String registerNo = item.get("registerNo").getAsString();
                        String commision = item.get("commision").getAsString();

//

//                        String commision = item.get("commision").getAsString();
//                        String qualification = item.get("qualification").getAsString();
//                        String registerNo = item.get("registerNo").getAsString();

//                        observableList.add(new DoctorMasterDTO(doctorName,
//                                specialization,hospitalName,hospitalAddress,mobileNumber)
//                        );
                        observableList.add(new DoctorMasterDTO(id, doctorName, hospitalName, hospitalAddress, mobileNumber, commision, qualification, registerNo, specialization));
                    }

//


                    tcDoctorName.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
                    tcHospitalName.setCellValueFactory(new PropertyValueFactory<>("hospitalName"));
                    tcHospitalAddress.setCellValueFactory(new PropertyValueFactory<>("hospitalAddress"));
                    tcSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
                    tcMobileNo.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));

//                    tcCommision.setCellValueFactory(new PropertyValueFactory<>("commision"));
//                    tcQualification.setCellValueFactory(new PropertyValueFactory<>("qualification"));
//                    tcRegisterNo.setCellValueFactory(new PropertyValueFactory<>("registerNo"));


//                    tcContactPerson.setCellValueFactory(new PropertyValueFactory<>("contact_person"));
//                    tcServiceAddress.setCellValueFactory(new PropertyValueFactory<>("service_Add"));
                    tcDoctorMasterView.setItems(observableList);

                } else {
                    System.out.println("responseObject is null");
                }
                if (focusInd.equalsIgnoreCase("focus")) {
                    tcDoctorMasterView.getSelectionModel().select(responseObject.size() - 1);
                    tcDoctorMasterView.scrollTo(responseObject.size() - 1);
                    tcDoctorMasterView.requestFocus();
                    focusInd = "";
                } else if (focusInd.equalsIgnoreCase("update")) {
                    tcDoctorMasterView.getSelectionModel().select(rowIndex);
                    tcDoctorMasterView.scrollTo(rowIndex);
                    tcDoctorMasterView.requestFocus();
                    focusInd = "";
                    btnDoctorMasterCreateSubmit.setText("Submit");
                }
            } else {
                System.out.println("Error in response");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void deleteDoctor() {
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                APIClient apiClient = null;
                Map<String, String> map = new HashMap<>();
                map.put("id", id);

                HttpResponse<String> response;
                String formData = Globals.mapToStringforFormData(map);
                apiClient = new APIClient(EndPoints.DOCTOR_DELETE_ENDPOINT, formData, RequestType.FORM_DATA);

                apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        // JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                        jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

                        message = jsonObject.get("message").getAsString();

                        if (jsonObject.get("responseStatus").getAsInt() == 200) {
                            AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                                tfDoctorMasterDoctorName.requestFocus();
                                id = "";
                            });
                        } else {
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                            });
                        }
                        getDoctorMaster();
                        clearDoctorMasterFields();

                    }
                });

                apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        doctorlogger.error("Network API cancelled in deleteDoctor()" + workerStateEvent.getSource().getValue().toString());

                    }
                });
                apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        doctorlogger.error("Network API failed in deleteDoctor()" + workerStateEvent.getSource().getValue().toString());

                    }
                });
                apiClient.start();
                doctorlogger.debug("delete doctor mode data end ...");
            } else {
            }
        };
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnDelete, callback);
    }
    @FXML
    private void handleDeletePressed() {
        DoctorMasterDTO selectedItem = tcDoctorMasterView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            id = String.valueOf(selectedItem.getId());
            deleteDoctor();
        }
    }


}
