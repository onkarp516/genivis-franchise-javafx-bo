package com.opethic.genivis.controller.master;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.dialogs.SingleInputDialogs;
import com.opethic.genivis.controller.tranx_purchase.PurchaseChallanController;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.network.RequestType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.event.Event;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import javafx.scene.Parent;

//import com.opethic.genivis.dto.AreaHeadIndianStateDTO;
//import com.opethic.genivis.dto.AreaHeadRoleTypeDTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

import static com.opethic.genivis.utils.FxmFileConstants.AREA_HEAD_LIST_SLUG;
import static com.opethic.genivis.utils.Globals.areaHeadListDTO;


public class areaHeadCreateController implements Initializable {


    private Map<String, String> AreaMapList = new HashMap<>();

    private static ObservableList<CommonDTO> contentNamesList = FXCollections.observableArrayList();
    private static ObservableList<CommonDTO> contentNamesList1 = FXCollections.observableArrayList();
    private static ObservableList<CommonDTO> contentNamesList2 = FXCollections.observableArrayList();
    private static ObservableList<CommonDTO> contentNamesList3 = FXCollections.observableArrayList();

    private String responseBody;
    private Map<String, String> districtMapList = new HashMap<>();
    private Map<String, String> countryMapList = new HashMap<>();
    private Long dHId = 0L;
    private Long RoleId = 0L;

    private String RoleName = "";
    private String AreaName = "";
    private Long areaId = 0L;
    private Long areaIdR = 0L;

    private Long countryId = 0L;
    private Long countryIdR0L;

    private String regionalId = "";
    private String zoneId = "";
    private String stateId1 = "";
    private String stateCode = "";
    private String stateCodeR = "";
    private Long educationId = 0L;
    private String districtName = "";

    String message = "";

    File selectedFile;
    File selectedFileForBank;
    File selectedFileForAdhaar;
    File selectedFileForPan;
    File selectedFileForPartnership;


    private String selStateName = "";
    private int selStateId = 0;

    private String selZoneName = "";
    private int selZoneId = 0;

    private String selRegionName = "";
    private int selRegionId = 0;

    private String selDistrictName = "";
    private int selDistrictId = 0;


    //    For Zone
    private String zoneStateHead = "";
    private String zoneStateHeadId = "";
    private String zoneStateMainId = "";


    //    For region
    private String regionZoneHead = "";
    private String regionZoneHeadId = "";
    private String regionStateHeadId = "";
    private String regionStateMainId = "";
    private String regionZoneHeadMainId = "";
    private String regionId = "";


//    For district

    private String districtId = "";
    private String districtRegionHead = "";
    private String districtRegionHeadId = "";
    private String districtZoneHeadId = "";
    private String districtStateHead = "";
    private String districtStateHeadId = "";
    private String districtStateHeadMainId = "";
    private String districtZoneHeadMainId = "";
    private String districtRegionHeadMainId = "";
    private boolean sameAddress; // Variable to hold the state of tgFranchiseIsFunding
    private String corppinText = "";
    private String pinText = "";


//    regionId: [object Object]
//    regionZoneHead: Zonal Maharashtra
//    regionZoneHeadId: 2
//    regionStateMainId: 4008
//    regionZoneHeadMainId: 3

    @FXML
    private ComboBox<FranchiseAreaDTO> cmbAreaHeadCreateRArea;

    @FXML
    private Button chooseButton;

    //    @FXML
//    private Label selectedImageLabel;
    @FXML
    private Button btnAreaHeadCreateImgBank;
    @FXML
    private Button btnAreaHeadCreateImgAdhaar;
    @FXML
    private Button btnAreaHeadCreateImgPan;
    @FXML
    private Button btnAreaHeadCreateImgPartnership;
    @FXML
    private Button btnPass;
    @FXML
    private TextField tfAreaHeadCreateBank;
    @FXML
    private TextField tfAreaHeadCreateAdhaar;
    @FXML
    private TextField tfAreaHeadCreatePan;
    @FXML
    private TextField tfAreaHeadCreatePanText;
    @FXML
    private TextField tfAreaHeadCreatePartnership;
    @FXML
    private VBox boxV;

    @FXML
    private Label lbAreaHeadCreateState;
    @FXML
    private Label lbAreaHeadCreateRState;

    @FXML
    private Label lbAreaHeadCreateCity, lbAreaHeadCreateRole;
    @FXML
    private Label lbAreaHeadCreateRCity;

    private Label lbFranchiseCreateState;
    private Node[] focusableNodes;
    private String isGender; // Variable to store the selected gender
//    @FXML
//    private Label lbAreaHeadCreateRCity;
//    @FXML
//    private Label lbAreaHeadCreateRState;

    private static final String[] OPTIONS = {"Option 1", "Option 2", "Option 3"};

    private JsonObject jsonObject = null;

    @FXML
    private ComboBox<AreaHeadRoleTypeDTO> cmbAreaHeadCreateRole;

    //    @FXML
//    private ComboBox<AreaHeadZoneDTO> cmbAreaHeadCreateZone;
    @FXML
    private ComboBox cmbAreaHeadCreateRegion;

    @FXML
    private ComboBox cmbAreaHeadCreateDistrict;

    @FXML
    private ComboBox<FranchiseAreaDTO> cmbAreaHeadCreateArea;

    @FXML
    private ComboBox cmbAreaHeadCreateCountry;
    @FXML
    private ComboBox cmbFranchiseCreateGender;
    @FXML
    private ComboBox cmbAreaHeadCreateRCountry;
    @FXML
    private ComboBox cmbAreaHeadCreateState;

    @FXML
    private ComboBox cmbAreaHeadCreateZone;


    @FXML
    private TextField tfAreaHeadCreatePincode;
    @FXML
    private TextField tfAreaHeadCreateRPincode;

    @FXML
    private CheckBox cbAreaHeadCreateSameasAddress;

    @FXML
    private TextField tfAreaHeadCreateTemporaryAddress;

    @FXML
    private TextField tfAreaHeadCreatePermanentAddress;
    @FXML
    private TextField tfAreaHeadCreateUsername;
    @FXML
    private PasswordField tfAreaHeadCreatePassword;
    @FXML
    private TextField tfAreaHeadCreatePasswordInText;
    @FXML
    private TextField tfAreaHeadCreateFirstName;
    @FXML
    private TextField tfAreaHeadCreateMiddleName;
    @FXML
    private TextField tfAreaHeadCreateLastName;
    @FXML
    private TextField tfAreaHeadCreateEmail;
    @FXML
    private TextField tfAreaHeadCreateMobile;
    @FXML
    private TextField tfAreaHeadCreateWhatsApp;
    @FXML
    private TextField tfAreaHeadCreateBankName;
    @FXML
    private TextField tfAreaHeadCreateBankAccountNo;
    @FXML
    private TextField tfAreaHeadCreateIFSC;
    @FXML
    private TextField tfAreaHeadCreateAdhaarText;


    @FXML
    private TextField tfZHStateHead, tfRGStateHead, tfDSStateHead;

    @FXML
    private TextField tfRGZoneHead, tfDSZoneHead, tfDSRegionHead;

    //    @FXML
//    private TextField tfAreaHeadCreateAdhaar;
//    @FXML
//    private TextField tfAreaHeadCreateBank;
//    @FXML
//    private TextField tfAreaHeadCreatePan;
    @FXML
    private TextField dpAreaHeadCreateDOB;
//    @FXML
//    private RadioButton rbAreaHeadCreateMale;
//    @FXML
//    private RadioButton rbAreaHeadCreateFemale;

//    @FXML
//    private TextField tfAreaHeadCreatePermanentAddress;


    @FXML
    private Button btnAreaHeadCreateSubmit, btnAreaHeadCreateCancel, AreaHeadZoneAdd, AreaHeadRegionAdd, AreaHeadDistrictAdd;

    @FXML
    private HBox toggleValidation, stateSeleHbox, zoneSeleHbox, regionSeleHbox, districtSeleHbox;

    @FXML
    private ScrollPane spRootPane;

//    @FXML
//    private Image passViewImg;


    ToggleGroup toggleGroup = new ToggleGroup();
    String selectedRadioValue;
    public String response;

    private static final Logger areaHeadLogger = LoggerFactory.getLogger(PurchaseChallanController.class);

    @FXML
    public void passwordBtn() {
        CommonFunctionalUtils.passwordField(tfAreaHeadCreatePassword, tfAreaHeadCreatePasswordInText, btnPass);
    }


    @FXML
    private void handleComboBoxActionGender() {
        System.out.println("cmbFranchiseCreateGender.getSelectionModel().getSelectedItem()" + cmbFranchiseCreateGender.getSelectionModel().getSelectedItem());
        if (cmbFranchiseCreateGender.getSelectionModel().getSelectedItem() != null) {
            if (cmbFranchiseCreateGender.getSelectionModel().getSelectedItem() == "Male") {
                isGender = "true";
            } else {
                isGender = "false";
            }
            System.out.println("i am gender" + isGender);
        } else {
            //?  use logger for alert messages
//            showAlert("Please Select Proper Data");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        tfAreaHeadCreateBank.setDisable(true);
        tfAreaHeadCreateAdhaar.setDisable(true);
//        tfAreaHeadCreatePanText.setDisable(true);
        tfAreaHeadCreatePan.setDisable(true);
        tfAreaHeadCreatePartnership.setDisable(true);

//        cmbAreaHeadCreateState.getSelectionModel().clearSelection();
//        cmbAreaHeadCreateState.setPromptText("Select State");




        CommonFunctionalUtils.restrictTextFieldToDigits(tfAreaHeadCreatePincode, 6);
        CommonFunctionalUtils.restrictTextField(tfAreaHeadCreatePincode);
        CommonFunctionalUtils.cursorMomentForTextFieldWithParticularLenght(tfAreaHeadCreatePincode, 6);

        CommonFunctionalUtils.restrictTextFieldToDigits(tfAreaHeadCreateRPincode, 6);
        CommonFunctionalUtils.restrictTextField(tfAreaHeadCreateRPincode);
        CommonFunctionalUtils.cursorMomentForTextFieldWithParticularLenght(tfAreaHeadCreateRPincode, 6);

        CommonFunctionalUtils.restrictTextFieldToDigits(tfAreaHeadCreateAdhaarText, 12);
        CommonFunctionalUtils.restrictTextField(tfAreaHeadCreateAdhaarText);
        CommonFunctionalUtils.cursorMomentForTextFieldWithParticularLenght(tfAreaHeadCreateAdhaarText, 12);


        CommonFunctionalUtils.panCardValidation(tfAreaHeadCreatePanText);
        CommonFunctionalUtils.textToUpperCase(tfAreaHeadCreateIFSC);


        CommonFunctionalUtils.restrictTextFieldToDigitsAndBirthDateFormat(dpAreaHeadCreateDOB);
        CommonValidationsUtils.restrictMobileNumber(tfAreaHeadCreateMobile);
        CommonValidationsUtils.restrictMobileNumber(tfAreaHeadCreateWhatsApp);
        CommonFunctionalUtils.restrictEmail(tfAreaHeadCreateEmail);


        // Add listener to display popup if invalid email format
        // Assuming necessary imports like javafx.beans.value.ChangeListener, javafx.scene.control.TextField, etc.



        CommonFunctionalUtils.commonInit(tfAreaHeadCreatePassword, tfAreaHeadCreatePasswordInText, btnPass);

        tfAreaHeadCreateMobile.textProperty().addListener((observable, oldValue, newValue) -> {
            //TODO: Update the text property of whatsapp when the value changes in mobileNu,
            tfAreaHeadCreateWhatsApp.setText(newValue);
        });

        handleRadioButtonAction();


        getCountry();


        CommonValidationsUtils.changeStarColour(boxV);


        System.out.println(areaHeadListDTO);
        if (areaHeadListDTO != null) {
            AreaHeadZoneAdd.setDisable(true);
            AreaHeadRegionAdd.setDisable(true);
            AreaHeadDistrictAdd.setDisable(true);
            btnAreaHeadCreateSubmit.setText("Update");
        }


        spRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {


            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("submit")) {
                    System.out.println(targetButton.getText());
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("cancel")) {
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
            }
            if (event.getCode() == KeyCode.S && event.isControlDown()) {
                btnAreaHeadCreateSubmit.fire();
            }
            if (event.getCode() == KeyCode.X && event.isControlDown()) {
                btnAreaHeadCreateCancel.fire();
            }




        });


        Platform.runLater(() -> {
//            cmbAreaHeadCreateRole.setOnKeyPressed(actionEvent -> {
//                if (actionEvent.getCode() == KeyCode.DOWN || actionEvent.getCode() == KeyCode.TAB || actionEvent.getCode() == KeyCode.ENTER || actionEvent.getCode() == KeyCode.SPACE ) {
//                    CommonValidationsUtils.comboBoxDataShow(cmbAreaHeadCreateRole);
//                }
//
//            });
            ComboBox[] cmbFields = {
                    cmbAreaHeadCreateRole,
                    cmbAreaHeadCreateDistrict,
                    cmbAreaHeadCreateRegion,
                    cmbAreaHeadCreateZone,
                    cmbAreaHeadCreateCountry,
                    cmbAreaHeadCreateRCountry,
                    cmbAreaHeadCreateState,
                    cmbAreaHeadCreateRegion,
                    cmbAreaHeadCreateArea,
                    cmbAreaHeadCreateRArea,
                    cmbFranchiseCreateGender
            };

            for (ComboBox combo : cmbFields) {
                CommonValidationsUtils.comboBoxDataShow(combo);
            }

//common function to display the data from combo box
//            CommonValidationsUtils.comboBoxDataShow(cmbAreaHeadCreateRole);
//            CommonValidationsUtils.comboBoxDataShow(cmbAreaHeadCreateDistrict);
//            CommonValidationsUtils.comboBoxDataShow(cmbAreaHeadCreateRegion);
//            CommonValidationsUtils.comboBoxDataShow(cmbAreaHeadCreateZone);
//            CommonValidationsUtils.comboBoxDataShow(cmbAreaHeadCreateCountry);
//            CommonValidationsUtils.comboBoxDataShow(cmbAreaHeadCreateRCountry);
//            CommonValidationsUtils.comboBoxDataShow(cmbAreaHeadCreateState);
//            CommonValidationsUtils.comboBoxDataShow(cmbAreaHeadCreateRegion);
//            CommonValidationsUtils.comboBoxDataShow(cmbAreaHeadCreateArea);
//            CommonValidationsUtils.comboBoxDataShow(cmbAreaHeadCreateRArea);


        });
        DateValidator.applyDateFormat(dpAreaHeadCreateDOB);


        sceneInitilization();
        stateSeleHbox.setVisible(false);
        zoneSeleHbox.setVisible(false);
        regionSeleHbox.setVisible(false);
        districtSeleHbox.setVisible(false);

        stateSeleHbox.managedProperty().bind(stateSeleHbox.visibleProperty());
        zoneSeleHbox.managedProperty().bind(zoneSeleHbox.visibleProperty());
        regionSeleHbox.managedProperty().bind(regionSeleHbox.visibleProperty());
        districtSeleHbox.managedProperty().bind(districtSeleHbox.visibleProperty());

        spRootPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        spRootPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

//        nextElement();
        AreaRoleStaticData();


        TextField[] textFields = {tfAreaHeadCreateUsername, tfAreaHeadCreatePassword, tfAreaHeadCreateFirstName,
                tfAreaHeadCreateLastName, tfAreaHeadCreateMobile, dpAreaHeadCreateDOB,
                tfAreaHeadCreatePincode,
                tfAreaHeadCreateAdhaarText, tfAreaHeadCreateAdhaar, tfAreaHeadCreatePanText, tfAreaHeadCreatePan, tfAreaHeadCreatePartnership};

//        CommonValidationsUtils.setupFocusNavigation(focusableNodes);
//        rbAreaHeadCreateMale, rbAreaHeadCreateFemale
//        RadioButton[] radioFields = {rbAreaHeadCreateMale,rbAreaHeadCreateMale};
        ComboBox[] comboFields = {cmbAreaHeadCreateArea, cmbFranchiseCreateGender, cmbAreaHeadCreateRArea, cmbAreaHeadCreateRole, cmbAreaHeadCreateZone, cmbAreaHeadCreateState, cmbAreaHeadCreateRegion, cmbAreaHeadCreateDistrict};

        Label[] labelFields = {lbAreaHeadCreateCity,
                lbAreaHeadCreateState,
                lbAreaHeadCreateRCity,
                lbAreaHeadCreateRState};

        Button[] btnFields = {btnAreaHeadCreateImgBank,
                btnAreaHeadCreateImgAdhaar,
                btnAreaHeadCreateImgPan,
                btnAreaHeadCreateImgPartnership,
                btnAreaHeadCreateSubmit,
                btnAreaHeadCreateCancel};

        for (TextField textField : textFields) {
            CommonFunctionalUtils.cursorMomentForTextField(textField);
        }


//        tfAreaHeadCreateEmail.focusedProperty().addListener((obs, oldVal, newVal) -> {
//            if (!newVal) {
//                String text = tfAreaHeadCreateEmail.getText().trim();
//
//                if (text.isEmpty()) {
//                    tfAreaHeadCreateEmail.requestFocus();
//                }
//            }
//        });

        for (ComboBox comboField : comboFields) {
            CommonFunctionalUtils.cursorMomentForComboBox(comboField);
        }
//
//        for (Label labelField : labelFields) {
//            cursorMomentForComboBox(labelField);
//        }


        if (Globals.areaHeadListDTO != null) {
            System.out.println("FranchiseListDTOId" + Globals.areaHeadListDTO.getId());
            getAllIndianState();
            getEditDataById();
            AreaHeadListController.editedAreaHeadId = Globals.areaHeadListDTO.getId(); //? Set the ID for editing
//            handleCheckBoxAction();
        } else {
            Globals.areaHeadListDTO = null;
            AreaHeadListController.isNewAreaHeadCreated = true; //? Set the flag for new creation
            System.out.println("FranchiseListDTOId is NULL");
            // Delayed initialization to ensure tgFranchiseIsFunding's width is properly calculated

        }


        btnAreaHeadCreateImgBank.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Image");
            // Add supported image file extensions if necessary
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp"));
            // Show open file dialog
//            File selectedFile = fileChooser.showOpenDialog(new Stage());
            selectedFileForBank = fileChooser.showOpenDialog(new Stage());
            if (selectedFileForBank != null) {
                // Update the label with the selected image's name
                tfAreaHeadCreateBank.setText(selectedFileForBank.getName());
            }
        });

        btnAreaHeadCreateImgAdhaar.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Image");
            // Add supported image file extensions if necessary
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp"));
            // Show open file dialog
            selectedFileForAdhaar = fileChooser.showOpenDialog(new Stage());
            if (selectedFileForAdhaar != null) {
                // Update the label with the selected image's name
                tfAreaHeadCreateAdhaar.setText(selectedFileForAdhaar.getName());
            }
        });

        btnAreaHeadCreateImgPan.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Image");
            // Add supported image file extensions if necessary
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp"));
            // Show open file dialog
            selectedFileForPan = fileChooser.showOpenDialog(new Stage());
            if (selectedFileForPan != null) {
                // Update the label with the selected image's name
                tfAreaHeadCreatePan.setText(selectedFileForPan.getName());
            }
        });

        btnAreaHeadCreateImgPartnership.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Image");
            // Add supported image file extensions if necessary
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp"));
            // Show open file dialog
            selectedFileForPartnership = fileChooser.showOpenDialog(new Stage());
            if (selectedFileForPartnership != null) {
                // Update the label with the selected image's name
                tfAreaHeadCreatePartnership.setText(selectedFileForPartnership.getName());
            }
        });


        Platform.runLater(() -> cmbAreaHeadCreateRole.requestFocus());

        tfAreaHeadCreatePincode.focusedProperty().addListener((obs, old, nw) -> {
            if (!nw) {
                if (!tfAreaHeadCreatePincode.getText().isEmpty()) {
                    validatePincode();
                }
                if (tfAreaHeadCreatePincode.getText().trim().isEmpty()) {
                    tfAreaHeadCreatePincode.requestFocus();
                }
                String newVal = tfAreaHeadCreatePincode.getText().trim();
                if (newVal.length() > 6) {
                    tfAreaHeadCreatePincode.setText(newVal.substring(0, 6));
//                    validatePincode();
                } else if (newVal.length() == 6 && (pinText.isEmpty() || !pinText.trim().contains(newVal))) {
                    pinText = newVal;
                    getAddressDataByPincode();
                }
                if (cbAreaHeadCreateSameasAddress.isSelected()) {
                    tfAreaHeadCreateRPincode.setText(newVal);
                }
            }
        });

        tfAreaHeadCreateRPincode.focusedProperty().addListener((obs, old, nw) -> {
            if (!nw) {
                if (!tfAreaHeadCreateRPincode.getText().isEmpty()) {
                    validatePincodeR();
                }
                if (tfAreaHeadCreateRPincode.getText().trim().isEmpty()) {
                    tfAreaHeadCreateRPincode.requestFocus();
                }
                String newVal = tfAreaHeadCreateRPincode.getText().trim();
                if (newVal.length() > 6) {
                    tfAreaHeadCreateRPincode.setText(newVal.substring(0, 6));
//                    validatePincode();
                } else if (newVal.length() == 6 && (pinText.isEmpty() || !pinText.trim().contains(newVal))) {
                    pinText = newVal;
                    getAddressDataByRPincode();
                }
            }
        });


        //TODO: address registered and corporate sameasaddress
        tfAreaHeadCreatePermanentAddress.textProperty().addListener((observable, oldValue, newValue) -> {
            if (cbAreaHeadCreateSameasAddress.isSelected())
                tfAreaHeadCreateTemporaryAddress.setText(newValue);
        });


        btnAreaHeadCreateSubmit.setOnAction(e -> {
            if (CommonValidationsUtils.validateForm(cmbAreaHeadCreateRole, tfAreaHeadCreateUsername, tfAreaHeadCreatePassword, tfAreaHeadCreateFirstName,
                    tfAreaHeadCreateLastName, tfAreaHeadCreateEmail, tfAreaHeadCreateMobile, dpAreaHeadCreateDOB, cmbFranchiseCreateGender,
                    tfAreaHeadCreatePincode, cmbAreaHeadCreateArea, cmbAreaHeadCreateCountry, tfAreaHeadCreateRPincode, cmbAreaHeadCreateRArea,
                    tfAreaHeadCreateAdhaarText, tfAreaHeadCreateAdhaar, tfAreaHeadCreatePanText, tfAreaHeadCreatePan)) {
                createAreaHead();
            }
        });


    }

    private void showAlert(String message) {
        //TODO: use util for all Alert messages
        Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
        Duration duration = Duration.seconds(0.9);
        alert.show();
        Timeline timeline = new Timeline(new KeyFrame(duration, event -> alert.close()));
        timeline.play();
    }

    @FXML
    private void handleComboBoxActionCountry() {

        FranchiseCountryDTO countryDTO = (FranchiseCountryDTO) cmbAreaHeadCreateCountry.getSelectionModel().getSelectedItem();
        if (countryDTO != null) {
            countryId = Long.valueOf(countryDTO.getId());
        } else {
            //TODO : use logger for alert messages
//            showAlert("Please Select Proper Data");
        }
    }

    //todo:set the data of country in the combobox and when selcted the id to be sent in request
    public void getCountry() {
        APIClient apiClient = null;
        try {
            areaHeadLogger.debug("Get Country Data Started...");
//            HttpResponse<String> response = APIClient.getRequest(EndPoints.GET_COUNTRY_DATA_ENDPOINT);
            apiClient = new APIClient(EndPoints.GET_COUNTRY_DATA_ENDPOINT, "", RequestType.GET);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                    responseBody = response.body();
//                    JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                    if (jsonObject != null) {
                        ObservableList<FranchiseCountryDTO> list = FXCollections.observableArrayList();
                        String fullName = jsonObject.get("name").getAsString();
                        String id = jsonObject.get("id").getAsString();
                        list.add(new FranchiseCountryDTO(fullName, id));
                        countryMapList.put(id, fullName);
                        cmbAreaHeadCreateCountry.setItems(list);

                        cmbAreaHeadCreateCountry.getSelectionModel().selectFirst(); // Set the first item as default
                        FranchiseCountryDTO defaultSelectedItem = (FranchiseCountryDTO) cmbAreaHeadCreateCountry.getSelectionModel().getSelectedItem();
                        String defaultSelectedId = defaultSelectedItem.getId();
                        countryId = Long.valueOf(defaultSelectedId);

                        cmbAreaHeadCreateCountry.setConverter(new StringConverter<FranchiseCountryDTO>() {
                            @Override
                            public String toString(FranchiseCountryDTO o) {
                                return o.getText();
                            }

                            @Override
                            public FranchiseCountryDTO fromString(String s) {
                                return null;
                            }
                        });

                        cmbAreaHeadCreateRCountry.setItems(list);
                        cmbAreaHeadCreateRCountry.getSelectionModel().selectFirst(); // Set the first item as default
                        cmbAreaHeadCreateRCountry.setConverter(new StringConverter<FranchiseCountryDTO>() {
                            @Override
                            public String toString(FranchiseCountryDTO o) {
                                return o.getText();
                            }

                            @Override
                            public FranchiseCountryDTO fromString(String s) {
                                return null;
                            }
                        });
                    } else {
                        //TODO : use logger for alert messages
                        showAlert("Falied to Load Data ");
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    areaHeadLogger.error("Network API cancelled in getCountry()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    areaHeadLogger.error("Network API failed in getCountry()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            areaHeadLogger.debug("Get Country Data End...");

        } catch (Exception e) {
            e.printStackTrace();
            //TODO : use logger for alert messages
            showAlert("Falied to connect server! ");
        } finally {
            apiClient = null;
        }
    }


    public void sceneInitilization() {
        spRootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }

    @FXML
    private void openAreaHeadPopupForZone() {
        SingleInputDialogs.openAreaHeadPopUp(Communicator.stage, "Zone", input -> {
//            getTableRow().getItem().setBatch_or_serial((String) input[0]);
//            int selectedIndex = getIndex();
            System.out.println("popup Callback");
            System.out.println(input);
            getAllZones();
            cmbAreaHeadCreateZone.getSelectionModel().selectLast();
//            getSelectionModel().selectFirst()
//            Platform.runLater(() -> {
//                getTableView().refresh();
//            });
        });
    }

    @FXML
    private void openAreaHeadPopupForRegion() {
        SingleInputDialogs.openAreaHeadPopUp(Communicator.stage, "Region", input -> {
//            getTableRow().getItem().setBatch_or_serial((String) input[0]);
//            int selectedIndex = getIndex();
            System.out.println("popup Callback");
            System.out.println(input);
            getAllRegions();
            cmbAreaHeadCreateRegion.getSelectionModel().selectLast();

//            Platform.runLater(() -> {
//                getTableView().refresh();
//            });
        });
    }

    @FXML
    private void openAreaHeadPopupForDistrict() {
        SingleInputDialogs.openAreaHeadPopUp(Communicator.stage, "District", input -> {
//            getTableRow().getItem().setBatch_or_serial((String) input[0]);
//            int selectedIndex = getIndex();
            System.out.println("popup Callback");
            System.out.println(input);
            getAllDistricts();
            cmbAreaHeadCreateDistrict.getSelectionModel().selectLast();
//            Platform.runLater(() -> {
//                getTableView().refresh();
//            });
        });
    }

    @FXML
    public void handleRadioButtonAction() {
        // Initialize the ComboBox with options
        ObservableList<String> options = FXCollections.observableArrayList(
                "Male",
                "Female"
        );
        cmbFranchiseCreateGender.setItems(options);
//        Boolean val = rbFranchiseCreateMale.isSelected();
//        Boolean valF = rbFranchiseCreateFemale.isSelected();
//        if (val) {
//            isGender = "true";
//        }
//        if (valF) {
//            isGender = "false";
//        }
    }


    public void createAreaHead() {
        System.out.println("gender-->" + selectedRadioValue);
        String id = Globals.areaHeadListDTO != null ? Globals.areaHeadListDTO.getId() : null;


        String AreaHeadCreateUsername = tfAreaHeadCreateUsername.getText();
        String AreaHeadCreatePassword = tfAreaHeadCreatePassword.getText();
        String AreaHeadCreateFirstName = tfAreaHeadCreateFirstName.getText();
        String AreaHeadCreateMiddleName = tfAreaHeadCreateMiddleName.getText();
        String AreaHeadCreateLastName = tfAreaHeadCreateLastName.getText();
        String AreaHeadCreateEmail = tfAreaHeadCreateEmail.getText();
        String AreaHeadCreateMobile = tfAreaHeadCreateMobile.getText();
        String AreaHeadCreateWhatsApp = tfAreaHeadCreateWhatsApp.getText();
        String AreaHeadCreatePermanentAddress = tfAreaHeadCreatePermanentAddress.getText();
        String AreaHeadCreateTemporaryAddress = tfAreaHeadCreateTemporaryAddress.getText();
        String AreaHeadCreatePincode = tfAreaHeadCreatePincode.getText();
        String AreaHeadCreateRPincode = tfAreaHeadCreateRPincode.getText();
        String AreaHeadCreateBankName = tfAreaHeadCreateBankName.getText();
        String AreaHeadCreateBankAccountNo = tfAreaHeadCreateBankAccountNo.getText();
        String AreaHeadCreateIFSC = tfAreaHeadCreateIFSC.getText();
        String AreaHeadCreateBank = tfAreaHeadCreateBank.getText();
        String AreaHeadCreateAdhaarText = tfAreaHeadCreateAdhaarText.getText();
        String AreaHeadCreateAdhaar = tfAreaHeadCreateAdhaar.getText();
        String AreaHeadCreatePanText = tfAreaHeadCreatePanText.getText();
        String AreaHeadCreatePan = tfAreaHeadCreatePan.getText();
        String AreaHeadCreatePartnership = tfAreaHeadCreatePartnership.getText();
        String AreaHeadDOB = dpAreaHeadCreateDOB.getText();


        String roleId = RoleId.toString();
        String username = tfAreaHeadCreateUsername.getText();
        String password = tfAreaHeadCreatePassword.getText();
        String firstName = tfAreaHeadCreateFirstName.getText();
        String middleName = tfAreaHeadCreateMiddleName.getText();
        String lastName = tfAreaHeadCreateLastName.getText();
        String emailId = tfAreaHeadCreateEmail.getText();
        String mobileNumber = tfAreaHeadCreateMobile.getText();
        String whatsApp = tfAreaHeadCreateWhatsApp.getText();
        String birthDate = String.valueOf(dpAreaHeadCreateDOB.getText());

//            String gender = String.valueOf(toggleGroup.getSelectedToggle().isSelected());
//        String gender = selectedRadioValue.toString();
        String address = tfAreaHeadCreatePermanentAddress.getText();
        String addressR = tfAreaHeadCreateTemporaryAddress.getText();

        String pincode = tfAreaHeadCreatePincode.getText();
        String pincodeR = tfAreaHeadCreateRPincode.getText();


        String area = areaId.toString();
        String areaR = areaIdR.toString();

        String city = lbAreaHeadCreateCity.getText();
        String cityR = lbAreaHeadCreateRCity.getText();

        String state = lbAreaHeadCreateState.getText();
        String stateR = lbAreaHeadCreateRState.getText();

        String country = countryId.toString();

        String bankName = tfAreaHeadCreateBankName.getText();
        String bankAccNo = tfAreaHeadCreateBankAccountNo.getText();
        String ifsc = tfAreaHeadCreateIFSC.getText();
        String bankImg = tfAreaHeadCreateBank.getText();
        String adhaar = tfAreaHeadCreateAdhaarText.getText();
        String adhaarImg = tfAreaHeadCreateAdhaar.getText();
        String pan = tfAreaHeadCreatePanText.getText();
        String panImg = tfAreaHeadCreatePan.getText();
        String partnershipImg = tfAreaHeadCreatePartnership.getText();

//            String birthDate = dpAreaHeadCreateDOB.getText();
//            System.exit(0);

//            String birthDate = dpAreaHeadCreateDOB.getText();
//            String id=Globals.areaHeadListDTO.getId()!=null?Globals.areaHeadListDTO.getId():"";

//        if (username.isEmpty()) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText("Username is required.");
//            alert.showAndWait();
//            return;
//        }


        Map<String, String> map = new HashMap<>();

        if (id != null) {
            map.put("id", id);
        }
        map.put("username", username);
        map.put("password", password);

        map.put("areaRole", RoleName);


//        map.put("sameAsAddress", cbAreaHeadCreateSameasAddress.getText());


        if (RoleId == 1) {
            map.put("stateCode", String.valueOf(selStateId));
            map.put("stateName", String.valueOf(selStateName));
        }

//        map.put("areaRole", roleId);
        if (RoleId == 2) {
//            map.put("zoneId", String.valueOf(selZoneId));
            map.put("zoneId", String.valueOf(selZoneId));
            map.put("zoneCode", String.valueOf(selZoneId));
            map.put("zoneStateHeadId", zoneStateHeadId);
            map.put("zoneStateMainId", String.valueOf(selStateId));
            map.put("zoneStateHead", zoneStateHead);
            map.put("stateCode", zoneStateMainId);

//            map.put("stateCode", String.valueOf(selStateId));

        }

        if (RoleId == 3) {
            map.put("region", String.valueOf(selRegionId));

//            map.put("regionZoneHeadId", regionalId);
//            map.put("regionStateHeadId", state);

            map.put("regionZoneHead", regionZoneHead);
            map.put("regionZoneHeadId", regionZoneHeadId);
            map.put("regionStateHeadId", regionStateHeadId);
            map.put("regionStateMainId", regionStateMainId);
            map.put("regionZoneHeadMainId", regionZoneHeadMainId);
            map.put("regionId", regionId);
            map.put("stateCode", regionStateMainId);
            map.put("zoneCode", regionZoneHeadMainId);
            map.put("zoneId", regionZoneHeadMainId);


//            map.put("stateCode", zoneStateMainId);

        }

        if (RoleId == 4) {
            map.put("district", String.valueOf(selDistrictId));
//            map.put("districtRegionHeadId", "1");
//            map.put("districtZoneHeadId", "1");

            map.put("districtId", districtId);
            map.put("districtRegionHead", districtRegionHead);
            map.put("districtRegionHeadId", districtRegionHeadId);
            map.put("districtZoneHeadId", districtZoneHeadId);
            map.put("districtStateHead", districtStateHead);
            map.put("districtStateHeadId", districtStateHeadId);
            map.put("districtStateHeadMainId", districtStateHeadMainId);
            map.put("districtZoneHeadMainId", districtZoneHeadMainId);
            map.put("districtRegionHeadMainId", districtRegionHeadMainId);

//            region data
            map.put("regionId", regionId);
            map.put("regionZoneHead", regionZoneHead);
            map.put("regionZoneHeadId", regionZoneHeadId);
            map.put("regionStateMainId", regionStateMainId);
            map.put("regionZoneHeadMainId", regionZoneHeadMainId);

            map.put("stateCode", districtStateHeadMainId);
            map.put("zoneCode", regionZoneHeadId);
//            map.put("regionCode", regionZoneHeadId);

        }

        map.put("firstName", firstName);
        map.put("middleName", middleName);
        map.put("lastName", lastName);
        map.put("email", emailId);
        map.put("mobileNumber", mobileNumber);
        map.put("whatsappNumber", whatsApp);
//        map.put("birthDate", birthDate);
        map.put("birthDate", Communicator.text_to_date.fromString(dpAreaHeadCreateDOB.getText()).toString());

        map.put("birth", birthDate);
        map.put("gender", String.valueOf(isGender));
        map.put("permenantAddress", addressR);
        map.put("address2", addressR);
        map.put("pincode", pincode);
        map.put("corporatePincode", pincodeR);
        map.put("city", city);
        map.put("corporatecity", cityR);
        map.put("area", area);
        map.put("corporatearea", areaR);

        map.put("temporaryAddress", address);
        map.put("address1", address);
        map.put("sameAsAddress", String.valueOf(cbAreaHeadCreateSameasAddress.isSelected()?true:false));

        System.out.println("sameAsAddress : "+map.get("sameAsAddress"));
        map.put("aadharCardNo", adhaar);
        map.put("panCardNo", pan);
        map.put("bankAccName", bankName);
        map.put("bankAccNo", bankAccNo);
        map.put("bankAccIFSC", ifsc);


        map.put("pinStateCode", stateCode.toString());
//        corporatestateCode: 33
//        corporatestateName: TAMIL NADU
        map.put("corporatestateCode", stateCodeR.toString());
//        map.put("corporatestateName", stateCodeR.toString());


        map.put("countryId", "1");  // this is for both Registered and Residential Address

        Map<String, String> headers = new HashMap<>();
        headers.put("branch", "gvhm001");


        Map<String, File> fileMap = null;
        fileMap = new HashMap<>();

        if (selectedFileForAdhaar != null) {
            fileMap.put("aadharCardFile", selectedFileForAdhaar);
        }

        if (selectedFileForPan != null) {
            fileMap.put("panCardFile", selectedFileForPan);

        }
        if (selectedFileForBank != null) {
            fileMap.put("bankAccFile", selectedFileForBank);
        }
        if (selectedFileForPartnership != null) {
            fileMap.put("partnerDeedFile", selectedFileForPartnership);
        }

//        Map<String, File> finalFileMap1 = fileMap;
//        System.out.println("finalFileMap1");
//        System.out.println(finalFileMap1);
        Gson gson = new Gson();

        // Convert HashMap to JSON and print it
        String json = gson.toJson(map);
        System.out.println(json);

        if (id == null) {

            Map<String, File> finalFileMap = fileMap;
            AlertUtility.CustomCallback callback = number -> {
                if (number == 1) {
                    response = APIClient.postMultipartRequest(map, finalFileMap, EndPoints.createAreaHead, headers);
                    GlobalController.getInstance().addTabStatic(AREA_HEAD_LIST_SLUG, false);
                } else {
                    System.out.println("working!");
                }
            };
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are sure to submit", callback);


        } else {
            Map<String, File> finalFileMap1 = fileMap;
            AlertUtility.CustomCallback callback = number -> {
                if (number == 1) {

                    response = APIClient.postMultipartRequest(map, finalFileMap1, EndPoints.updateAreaHead, headers);
                    GlobalController.getInstance().addTabStatic(AREA_HEAD_LIST_SLUG, false);
                } else {
                    System.out.println("working!");
                }
            };
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are sure to submit", callback);

        }

        // Parse the response JSON string into a JsonObject using Gson
        JsonObject responseBody = new Gson().fromJson(response, JsonObject.class);
        System.out.println("Response => " + responseBody);


//
        if (responseBody.get("responseStatus").getAsInt() == 200) {
            Map<String, File> finalFileMap2 = fileMap;

            AlertUtility.CustomCallback callback = number -> {
                if (number == 1) {
                    response = APIClient.postMultipartRequest(map, finalFileMap2, EndPoints.updateAreaHead, headers);
                    GlobalController.getInstance().addTabStatic(AREA_HEAD_LIST_SLUG, false);
                } else {
                    System.out.println("working!");
                }
            };
            AlertUtility.AlertSuccess(AlertUtility.alertTypeConfirmation, responseBody.get("message").getAsString(), callback);

//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Success");
//            alert.setHeaderText(responseBody.get("message").getAsString());
//            alert.show();
//            // Close the alert after 1 second
//            PauseTransition delay = new PauseTransition(Duration.seconds(1));
//            delay.setOnFinished(event -> alert.close());
//            delay.play();
//
//            Globals.areaHeadListDTO=null;
//            GlobalController.getInstance().addTabStatic(AREA_HEAD_LIST_SLUG,false);


//                tfAreaHeadCreateUsername.setText("");
//                tfAreaHeadCreatePassword.setText("");
//                tfAreaHeadCreateFirstName.setText("");
//                tfAreaHeadCreateMiddleName.setText("");
//                tfAreaHeadCreateLastName.setText("");
//                tfAreaHeadCreateEmail.setText("");
//                tfAreaHeadCreateMobile.setText("");
//                tfAreaHeadCreateWhatsApp.setText("");
//                tfAreaHeadCreatePermanentAddress.setText("");
//                tfAreaHeadCreateTemporaryAddress.setText("");
//                tfAreaHeadCreatePincode.setText("");
//                tfAreaHeadCreateRPincode.setText("");
//                lbAreaHeadCreateCity.setText("");
//                lbAreaHeadCreateRCity.setText("");
//                lbAreaHeadCreateState.setText("");
//                lbAreaHeadCreateRState.setText("");
//                tfAreaHeadCreateBankName.setText("");
//                tfAreaHeadCreateBankAccountNo.setText("");
//                tfAreaHeadCreateIFSC.setText("");
//                tfAreaHeadCreateBank.setText("");
//                tfAreaHeadCreateAdhaarText.setText("");
//                tfAreaHeadCreateAdhaar.setText("");
//                tfAreaHeadCreatePanText.setText("");
//                tfAreaHeadCreatePan.setText("");
//                tfAreaHeadCreatePartnership.setText("");

        } else {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText(responseBody.get("message").getAsString());
//            alert.showAndWait();

            AlertUtility.CustomCallback callback = number -> {
                if (number == 1) {
//                    response = APIClient.postMultipartRequest(map, finalFileMap2, EndPoints.updateAreaHead, headers);
                    GlobalController.getInstance().addTabStatic(AREA_HEAD_LIST_SLUG, false);
                } else {
                    System.out.println("working!");
                }
            };
            AlertUtility.AlertSuccess(AlertUtility.alertTypeError, responseBody.get("message").getAsString(), callback);


        }


    }

    private void nodetraversal(Node current_node, Node next_node) {
        current_node.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                next_node.requestFocus();
                event.consume();
            }

            if (event.getCode() == KeyCode.ENTER) {
                if (current_node instanceof Button button) {
                    button.fire();
                }
            } else if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
                if (current_node instanceof RadioButton radioButton) {
                    radioButton.setSelected(!radioButton.isSelected());
                    radioButton.fire();
                }
            }
        });
    }


//    public void nextElement() {
//        nodetraversal(cmbAreaHeadCreateRole, tfAreaHeadCreateUsername);
//        nodetraversal(tfAreaHeadCreateUsername, tfAreaHeadCreatePassword);
//        nodetraversal(tfAreaHeadCreatePassword, tfAreaHeadCreateFirstName);
//        nodetraversal(tfAreaHeadCreateFirstName, tfAreaHeadCreateMiddleName);
//        nodetraversal(tfAreaHeadCreateMiddleName, tfAreaHeadCreateLastName);
//        nodetraversal(tfAreaHeadCreateLastName, tfAreaHeadCreateEmail);
//        nodetraversal(tfAreaHeadCreateEmail, tfAreaHeadCreateMobile);
//
//        nodetraversal(tfAreaHeadCreateMobile, tfAreaHeadCreateWhatsApp);
//        nodetraversal(tfAreaHeadCreateWhatsApp, dpAreaHeadCreateDOB);
////        nodetraversal(dpAreaHeadCreateDOB, tfAreaHeadCreateEmail);
//        nodetraversal(dpAreaHeadCreateDOB, rbAreaHeadCreateMale);
//        nodetraversal(rbAreaHeadCreateMale, rbAreaHeadCreateFemale);
//        nodetraversal(rbAreaHeadCreateFemale, tfAreaHeadCreatePermanentAddress);
//        nodetraversal(tfAreaHeadCreatePermanentAddress, tfAreaHeadCreatePincode);
//        nodetraversal(tfAreaHeadCreatePincode, cmbAreaHeadCreateArea);
//        nodetraversal(cmbAreaHeadCreateArea, cmbAreaHeadCreateCountry);
//        nodetraversal(cmbAreaHeadCreateCountry, tfAreaHeadCreateTemporaryAddress);
//        nodetraversal(tfAreaHeadCreateTemporaryAddress, tfAreaHeadCreateRPincode);
//        nodetraversal(tfAreaHeadCreateRPincode, cmbAreaHeadCreateRArea);
//        nodetraversal(cmbAreaHeadCreateRArea, cmbAreaHeadCreateRCountry);
//
//
//        nodetraversal(cmbAreaHeadCreateRCountry, tfAreaHeadCreateBankName);
//        nodetraversal(tfAreaHeadCreateBankName, tfAreaHeadCreateBankAccountNo);
//        nodetraversal(tfAreaHeadCreateBankAccountNo, tfAreaHeadCreateIFSC);
//        nodetraversal(tfAreaHeadCreateIFSC, btnAreaHeadCreateImgBank);
//        nodetraversal(btnAreaHeadCreateImgBank, tfAreaHeadCreateAdhaarText);
//        nodetraversal(tfAreaHeadCreateAdhaarText, btnAreaHeadCreateImgAdhaar);
//        nodetraversal(btnAreaHeadCreateImgAdhaar, tfAreaHeadCreatePanText);
//        nodetraversal(tfAreaHeadCreatePanText, btnAreaHeadCreateImgPan);
//        nodetraversal(btnAreaHeadCreateImgPan, btnAreaHeadCreateImgPartnership);
//        nodetraversal(btnAreaHeadCreateImgPartnership, btnAreaHeadCreateSubmit);
//        nodetraversal(btnAreaHeadCreateSubmit, btnAreaHeadCreateCancel);
//    }


    @FXML
    private void handleDatePickerAction() {
//        CommonValidationsUtils.dateCheckVal(dpAreaHeadCreateDOB);

        // Get the selected date from the DatePicker
//        LocalDate selectedDate = (LocalDate) cmbAreaHeadCreateRole.getValue();
//
//        if (selectedDate != null) {
//            // Calculate the age based on the selected date
//            LocalDate currentDate = LocalDate.now();
//            Period period = Period.between(selectedDate, currentDate);
//
//            // Display the calculated age in the TextField
//            int age = period.getYears();
////            tfFranchiseCreateAge.setText(String.valueOf(age));
//        } else {
//            // If no date is selected, clear the TextField
////            tfFranchiseCreateAge.clear();
//        }
    }

    static class HeadOption {
        private final String label;
        private final String value;

        public HeadOption(String label, String value) {
            this.label = label;
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return label;
        }
    }


    //    Fn on check set Same as address
    public void setSameAsAddress() {
        if (cbAreaHeadCreateSameasAddress.isSelected()) {
//              cmbAreaHeadCreateRArea.setText(cmbFranchiseCreateArea.getText());
            tfAreaHeadCreateTemporaryAddress.setDisable(true);
            tfAreaHeadCreateRPincode.setDisable(true);
            cmbAreaHeadCreateRArea.setDisable(true);
            lbAreaHeadCreateRCity.setDisable(true);
            lbAreaHeadCreateRState.setDisable(true);
//            cmbAreaHeadCreateRCountry.setDisable(true);
            tfAreaHeadCreateTemporaryAddress.setText(tfAreaHeadCreatePermanentAddress.getText());
            tfAreaHeadCreateRPincode.setText(tfAreaHeadCreatePincode.getText());
//            cbAreaHeadCreateSameasAddress.setText("true");
            lbAreaHeadCreateRCity.setText(lbAreaHeadCreateCity.getText());
            lbAreaHeadCreateRState.setText(lbAreaHeadCreateState.getText());


            ObservableList<FranchiseAreaDTO> itemsToCopy = cmbAreaHeadCreateArea.getItems();
            cmbAreaHeadCreateRArea.setItems(itemsToCopy);
            FranchiseAreaDTO areaDTO = (FranchiseAreaDTO) cmbAreaHeadCreateArea.getSelectionModel().getSelectedItem();
//            long testId= Long.valueOf(areaDTO.getId());
            String testId = String.valueOf(areaDTO.getId()); // Converting long back to String
            for (FranchiseAreaDTO item : cmbAreaHeadCreateRArea.getItems()) {
                if (item.getId().equals(testId)) {
                    cmbAreaHeadCreateRArea.getSelectionModel().select(item);
                    break;
                }
            }
        } else {
//            cbAreaHeadCreateSameasAddress.setText("false");
            tfAreaHeadCreateTemporaryAddress.setText("");
//            cmbAreaHeadCreateRArea.getSelectionModel().select("");
            lbAreaHeadCreateRCity.setText("");
            lbAreaHeadCreateRState.setText("");
//            cmbAreaHeadCreateRCountry.getSelectionModel().select("");
            tfAreaHeadCreateRPincode.setText("");
            tfAreaHeadCreateTemporaryAddress.setDisable(false);
            tfAreaHeadCreateRPincode.setDisable(false);
            cmbAreaHeadCreateRArea.setDisable(false);
            lbAreaHeadCreateRCity.setDisable(false);
            lbAreaHeadCreateRState.setDisable(false);
            cmbAreaHeadCreateRCountry.setDisable(false);
        }
//        });
    }


    public void getAddressDataByRPincode() {
        String pincodeR = tfAreaHeadCreateRPincode.getText().trim();

        Map<String, String> map = new HashMap<>();
        map.put("pincode", pincodeR);

        String formData = Globals.mapToStringforFormData(map);
        System.out.println("FormData: " + formData);
        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "get_pincode");
        String responseBody = response.body();

        JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
        System.out.println("jsonObject" + jsonObject);

        if (jsonObject.get("responseStatus").getAsInt() == 200) {
            JsonArray jsonArray = jsonObject.get("responseObject").getAsJsonArray();
            ObservableList<FranchiseAreaDTO> list = FXCollections.observableArrayList();

            for (JsonElement mElement : jsonArray) {
                JsonObject mObject = mElement.getAsJsonObject();
                String area = mObject.get("area").getAsString();
                String district = mObject.get("district").getAsString();
                String state = mObject.get("state").getAsString();
                String stateCode = mObject.get("stateCode").getAsString();
                String pin = mObject.get("pincode").getAsString();
                String id = mObject.get("id").getAsString();

                lbAreaHeadCreateRCity.setText(district);
                lbAreaHeadCreateRState.setText(state);
//                        lblCmpCreateRegCityName.setVisible(true);
//                        lblCmpCreateRegStateName.setVisible(true);

//                if (cbAreaHeadCreateSameasAddress.isSelected()) {
//                    lbAreaHeadCreateRCity.setText(district);
//                    lbAreaHeadCreateRState.setText(state);
//                }
//
                list.add(new FranchiseAreaDTO(area, id, pin, district, state, stateCode));
                AreaMapList.put(id, area);
            }

//                    cmbFranchiseCreateArea.setItems(list);
            cmbAreaHeadCreateRArea.setItems(list);

            StringConverter<FranchiseAreaDTO> converter = new StringConverter<>() {
                @Override
                public String toString(FranchiseAreaDTO o) {
                    return o != null ? o.getArea() : "";
                }

                @Override
                public FranchiseAreaDTO fromString(String s) {
                    return null;
                }
            };
//                    cmbFranchiseCreateArea.setConverter(converter);
            cmbAreaHeadCreateRArea.setConverter(converter);
        }
    }


    public void getAddressDataByPincode() {
        try {
            String pincode = tfAreaHeadCreatePincode.getText().trim();
            if (pincode.equals("413006")) {
                System.out.println(pincode);
            }


            if (!pincode.isEmpty()) {
                Map<String, String> map = new HashMap<>();
                map.put("pincode", pincode);

                String formData = Globals.mapToStringforFormData(map);
                System.out.println("FormData: " + formData);
                HttpResponse<String> response = APIClient.postFormDataRequest(formData, "get_pincode");
                String responseBody = response.body();

                JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                System.out.println("jsonObject" + jsonObject);

                if (jsonObject.get("responseStatus").getAsInt() == 200) {
                    JsonArray jsonArray = jsonObject.get("responseObject").getAsJsonArray();
                    ObservableList<FranchiseAreaDTO> list = FXCollections.observableArrayList();

                    for (JsonElement mElement : jsonArray) {
                        JsonObject mObject = mElement.getAsJsonObject();
                        String area = mObject.get("area").getAsString();
                        String district = mObject.get("district").getAsString();
                        String state = mObject.get("state").getAsString();
                        String stateCode = mObject.get("stateCode").getAsString();
                        String pin = mObject.get("pincode").getAsString();
                        String id = mObject.get("id").getAsString();

                        lbAreaHeadCreateCity.setText(district);
                        lbAreaHeadCreateState.setText(state);
//                        lblCmpCreateRegCityName.setVisible(true);
//                        lblCmpCreateRegStateName.setVisible(true);

                        if (cbAreaHeadCreateSameasAddress.isSelected()) {
                            lbAreaHeadCreateRCity.setText(district);
                            lbAreaHeadCreateRState.setText(state);
                        }

                        list.add(new FranchiseAreaDTO(area, id, pin, district, state, stateCode));
                        AreaMapList.put(id, area);
                    }

                    cmbAreaHeadCreateArea.setItems(list);
//                    cmbAreaHeadCreateRArea.setItems(list);

                    StringConverter<FranchiseAreaDTO> converter = new StringConverter<>() {
                        @Override
                        public String toString(FranchiseAreaDTO o) {
                            return o != null ? o.getArea() : "";
                        }

                        @Override
                        public FranchiseAreaDTO fromString(String s) {
                            return null;
                        }
                    };

                    cmbAreaHeadCreateArea.setConverter(converter);
                    cmbAreaHeadCreateRArea.setConverter(converter);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleComboBoxActionAreaOfResidential() {

        FranchiseAreaDTO franchiseAreaDTO = (FranchiseAreaDTO) cmbAreaHeadCreateRArea.getSelectionModel().getSelectedItem();
        //String selectedItem = String.valueOf(cmbFranchiseCreateDistrictHead.getSelectionModel().getSelectedItem());
        if (franchiseAreaDTO != null) {
//            districtNameR=String.valueOf(franchiseAreaDTO.getArea());
//            CommonValidationsUtils.checkComboFieldAndHighlight(cmbAreaHeadCreateRArea);

            districtName = String.valueOf(franchiseAreaDTO.getArea());

//            lbAreaHeadCreateRCity.setText(franchiseAreaDTO.getDistrict());
//            lbAreaHeadCreateRState.setText(franchiseAreaDTO.getState());

            System.out.println("Selected item: " + franchiseAreaDTO.getId());
        } else {
            System.out.println("No item selected.");
        }
        areaIdR = Long.valueOf(franchiseAreaDTO.getId());
        stateCodeR = franchiseAreaDTO.getStateCode();


    }

    @FXML
    private void handleComboBoxActionArea() {
        FranchiseAreaDTO franchiseAreaDTO = (FranchiseAreaDTO) cmbAreaHeadCreateArea.getSelectionModel().getSelectedItem();
        //String selectedItem = String.valueOf(cmbFranchiseCreateDistrictHead.getSelectionModel().getSelectedItem());
        if (franchiseAreaDTO != null) {
//            CommonValidationsUtils.checkComboFieldAndHighlight(cmbAreaHeadCreateArea);

            districtName = String.valueOf(franchiseAreaDTO.getArea());

//            lbAreaHeadCreateCity.setText(franchiseAreaDTO.getDistrict());
//            lbAreaHeadCreateState.setText(franchiseAreaDTO.getState());
//            lbAreaHeadCreateRCity.setText(franchiseAreaDTO.getDistrict());
//            lbAreaHeadCreateRState.setText(franchiseAreaDTO.getState());

//            getDistrictHeadsData();
            System.out.println("Selected item: " + franchiseAreaDTO.getId());
        } else {
            System.out.println("No item selected.");
        }
        areaId = Long.valueOf(franchiseAreaDTO.getId());
        stateCode = franchiseAreaDTO.getStateCode();


    }

    @FXML
    private void handleComboBoxActionRArea() {
        FranchiseAreaDTO franchiseAreaDTO = (FranchiseAreaDTO) cmbAreaHeadCreateRArea.getSelectionModel().getSelectedItem();
        //String selectedItem = String.valueOf(cmbFranchiseCreateDistrictHead.getSelectionModel().getSelectedItem());
        areaIdR = Long.valueOf(franchiseAreaDTO.getId());
        stateCodeR = franchiseAreaDTO.getStateCode();

        System.out.println(areaIdR);

        if (franchiseAreaDTO != null) {
            districtName = String.valueOf(franchiseAreaDTO.getArea());

//            lbFranchiseCreateCity.setText(franchiseAreaDTO.getDistrict());
//            lbFranchiseCreateState.setText(franchiseAreaDTO.getState());
            lbAreaHeadCreateRCity.setText(franchiseAreaDTO.getDistrict());
            lbAreaHeadCreateRState.setText(franchiseAreaDTO.getState());

//            getDistrictHeadsData();
            System.out.println("Selected item: " + franchiseAreaDTO.getId());
        } else {
            System.out.println("No item selected.");
        }
    }


//    public void AreaRoleDynamicData() {
////        soToscLogger.info("starting of salesman master list API");
//        try {
//            HttpResponse<String> response = APIClient.getRequest("get_all_area_head");
//            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
////            System.out.println("i am list" + jsonObject);
//
//            if (jsonObject.get("responseStatus").getAsInt() == 200) {
//                JsonArray responseArray = jsonObject.get("responseObject").getAsJsonArray();
//                // Create an ObservableList to store items for the combo box
//                ObservableList<AreaHeadRoleTypeDTO> salesmanList = FXCollections.observableArrayList();
//
//                // Assuming the JSON array contains the required information to create a AreaHeadRoleTypeDTO instance
//                for (JsonElement element : responseArray) {
//                    // Extract information from the JSON element
//                    JsonObject salesmanJson = element.getAsJsonObject();
//                    int id = salesmanJson.get("id").getAsInt();
//                    String firstName = salesmanJson.get("firstName").getAsString();
//                    // Repeat for other fields
//
//                    // Create a new AreaHeadRoleTypeDTO instance with the extracted information
//                    AreaHeadRoleTypeDTO areaHeadRoleTypeDTO = new AreaHeadRoleTypeDTO(id,firstName);
//                    salesmanList.add(areaHeadRoleTypeDTO);
//                }
//                // Set the items of the combo box
//                cmbAreaHeadCreateRole.setItems(salesmanList);
//                cmbAreaHeadCreateRole.setConverter(new StringConverter<AreaHeadRoleTypeDTO>() {
//                    @Override
//                    public String toString(AreaHeadRoleTypeDTO areaHeadRoleTypeDTO) {
//                        return areaHeadRoleTypeDTO != null ? areaHeadRoleTypeDTO.getFirstName() : "";
//                    }
//
//                    @Override
//                    public AreaHeadRoleTypeDTO fromString(String s) {
//                        return null;
//                    }
//                });
//            }
//        } catch (Exception e) {
////            e.printStackTrace();
////            soToscLogger.error("error in getting saleman " + e.getMessage());
//        }
//    }


    public void AreaRoleStaticData() {
        // Create key-value pairs and add them to the ComboBox


        ObservableList<AreaHeadRoleTypeDTO> items = FXCollections.observableArrayList(new AreaHeadRoleTypeDTO(1, "State Head", "state"), new AreaHeadRoleTypeDTO(2, "Zonal Head", "zonal"), new AreaHeadRoleTypeDTO(3, "Regional Head", "region"), new AreaHeadRoleTypeDTO(4, "District Head", "district"), new AreaHeadRoleTypeDTO(5, "Business Dev. Executive", "bde"), new AreaHeadRoleTypeDTO(6, "Business Dev. Manager", "bdm"));

        System.out.println("list: " + items);

        cmbAreaHeadCreateRole.getItems().addAll(items);

        //        cmbAreaHeadCreateRole.setItems(items);

//        cmbAreaHeadCreateRole.setConverter(new StringConverter<AreaHeadRoleTypeDTO>() {
//            @Override
//            public String toString(AreaHeadRoleTypeDTO o) {
//                return o.getFirstName();
//            }
//
//            @Override
//            public AreaHeadRoleTypeDTO fromString(String s) {
//                return null;
//            }
//        });

//            AutoCompleteBox<AreaHeadRoleTypeDTO> s = new AutoCompleteBox<>(cmbAreaHeadCreateRole, -1);

    }


    public void getAllIndianState() {
        try {
            contentNamesList.clear();
            HttpResponse<String> response = APIClient.getRequest("getIndianState");
            System.out.println("getContentNames =>" + response);
            AreaHeadIndianStateResDTO areaHeadIndianStateResDTO = new Gson().fromJson(response.body(), AreaHeadIndianStateResDTO.class);

            if (areaHeadIndianStateResDTO.getResponseStatus() == 200) {
                List<AreaHeadIndianStateDTO> list1 = areaHeadIndianStateResDTO.getResponseObject();
                for (AreaHeadIndianStateDTO contentList : list1) {
                    contentNamesList.add(new CommonDTO(contentList.getStateName(), contentList.getId().toString()));
                }
            } else {
                areaHeadLogger.info("ResponseObject is null--->getContentNames()");
            }


            // Set the items of the combo box
            cmbAreaHeadCreateState.setItems(contentNamesList);

//            AutoCompleteBox<CommonDTO> s = new AutoCompleteBox<>(cmbAreaHeadCreateState, -1);

        } catch (Exception e) {
//            e.printStackTrace();
//            soToscLogger.error("error in getting saleman " + e.getMessage());
        }
    }

    public void getAllZones() {
        try {
            contentNamesList1.clear();
            HttpResponse<String> response = APIClient.getRequest(EndPoints.GET_ALL_ZONES);
            System.out.println("getContentNames =>" + response);
            AreaHeadZoneResDTO areaHeadZoneResDTO = new Gson().fromJson(response.body(), AreaHeadZoneResDTO.class);

            if (areaHeadZoneResDTO.getResponseStatus() == 200) {
                List<AreaHeadZoneDTO> list1 = areaHeadZoneResDTO.getResponseObject();
                for (AreaHeadZoneDTO contentList : list1) {
                    contentNamesList1.add(new CommonDTO(contentList.getZoneName(), contentList.getId().toString()));
                }
            } else {
                areaHeadLogger.info("ResponseObject is null--->getContentNames()");
            }

            // Add "Select" option at the first position
//            contentNamesList1.add(0, new CommonDTO("Select", ""));

            // Set the items of the combo box
            cmbAreaHeadCreateZone.setItems(contentNamesList1);

//        AutoCompleteBox<CommonDTO> s = new AutoCompleteBox<>(cmbAreaHeadCreateZone, -1);
        } catch (Exception e) {
//        e.printStackTrace();
            areaHeadLogger.error("error in getting saleman " + e.getMessage());
        }
    }

    public void getAllRegions() {
        try {
            contentNamesList2.clear();
            HttpResponse<String> response = APIClient.getRequest(EndPoints.GET_ALL_REGIONS);
            System.out.println("getContentNames =>" + response);
            AreaHeadRegionsResDTO areaHeadRegionsResDTO = new Gson().fromJson(response.body(), AreaHeadRegionsResDTO.class);

            if (areaHeadRegionsResDTO.getResponseStatus() == 200) {
                List<AreaHeadRegionDTO> list1 = areaHeadRegionsResDTO.getResponseObject();
                for (AreaHeadRegionDTO contentList : list1) {
                    contentNamesList2.add(new CommonDTO(contentList.getRegionName(), contentList.getId().toString()));
                }
            } else {
                areaHeadLogger.info("ResponseObject is null--->getContentNames()");
            }


            // Set the items of the combo box
//            contentNamesList1.add(0, new CommonDTO("Select", ""));

            cmbAreaHeadCreateRegion.setItems(contentNamesList2);
//            AutoCompleteBox<CommonDTO> s = new AutoCompleteBox<>(cmbAreaHeadCreateRegion, -1);

        } catch (Exception e) {
//            e.printStackTrace();
            areaHeadLogger.error("error in getting saleman " + e.getMessage());
        }
    }

    public void getAllDistricts() {
        try {
            contentNamesList3.clear();
            HttpResponse<String> response = APIClient.getRequest(EndPoints.GET_ALL_DISTRICTS);
            System.out.println("getContentNames =>" + response);
            AreaHeadDistrictsResDTO areaHeadDistrictsResDTO = new Gson().fromJson(response.body(), AreaHeadDistrictsResDTO.class);

            if (areaHeadDistrictsResDTO.getResponseStatus() == 200) {
                List<AreaHeadDistrictDTO> list1 = areaHeadDistrictsResDTO.getResponseObject();
                for (AreaHeadDistrictDTO contentList : list1) {
                    contentNamesList3.add(new CommonDTO(contentList.getDistrictName(), contentList.getId().toString()));
                }
            } else {
                areaHeadLogger.info("ResponseObject is null--->getContentNames()");
            }

            // Set the items of the combo box
            cmbAreaHeadCreateDistrict.setItems(contentNamesList3);
//            AutoCompleteBox autoCompleteBox = new AutoCompleteBox(cmbFranchiseCreateBusinessType, -1);
//                AutoCompleteBox<CommonDTO> s = new AutoCompleteBox<>(cmbAreaHeadCreateDistrict, -1);


        } catch (Exception e) {
//            e.printStackTrace();
            areaHeadLogger.error("error in getting saleman " + e.getMessage());
        }
    }


    //    FNHandleComboBox
    @FXML
    private void handleComboBoxActionRoleHead() {

        AreaHeadRoleTypeDTO AreaRole = (AreaHeadRoleTypeDTO) cmbAreaHeadCreateRole.getSelectionModel().getSelectedItem();
        System.out.println("roleId----->" + AreaRole.getId());
        RoleId = Long.valueOf(AreaRole.getId());
        RoleName = AreaRole.getValue();

        if (AreaRole != null) {
            AreaName = String.valueOf(AreaRole.getFirstName());
            int roleId = AreaRole.getId();

            cmbAreaHeadCreateRole.getSelectionModel().select(AreaRole);
            stateSeleHbox.setVisible(false);
            zoneSeleHbox.setVisible(false);
            regionSeleHbox.setVisible(false);
            districtSeleHbox.setVisible(false);

            stateSeleHbox.managedProperty().bind(stateSeleHbox.visibleProperty());
            zoneSeleHbox.managedProperty().bind(zoneSeleHbox.visibleProperty());
            regionSeleHbox.managedProperty().bind(regionSeleHbox.visibleProperty());
            districtSeleHbox.managedProperty().bind(districtSeleHbox.visibleProperty());

            if (roleId == 1) {
                getAllIndianState();
                stateSeleHbox.setVisible(true);
            } else if (roleId == 2) {
                getAllZones();
                zoneSeleHbox.setVisible(true);
            } else if (roleId == 3) {
                getAllRegions();
                regionSeleHbox.setVisible(true);
            } else if (roleId == 4) {
                getAllDistricts();
                districtSeleHbox.setVisible(true);
            }

            System.out.println("Selected item: " + AreaRole.getId());
        } else {
            System.out.println("No item selected.");
        }

//        CommonValidationsUtils.checkComboFieldAndHighlight(cmbAreaHeadCreateRole);


    }

    @FXML
    private void handleComboBoxActionStateHead() {
        CommonDTO dtoData = (CommonDTO) cmbAreaHeadCreateState.getSelectionModel().getSelectedItem();
        System.out.println("roleId----->" + dtoData.getId());

        if (dtoData != null) {
            cmbAreaHeadCreateState.getSelectionModel().select(dtoData);
            selStateName = dtoData.getText();
            selStateId = Integer.parseInt(dtoData.getId());
            System.out.println("Selected item: " + dtoData.getId());
        } else {
            System.out.println("No item selected.");
        }
//        CommonValidationsUtils.checkComboFieldAndHighlight(cmbAreaHeadCreateState);
    }

    @FXML
    private void handleComboBoxActionZoneHead() {

        CommonDTO dtoData = (CommonDTO) cmbAreaHeadCreateZone.getSelectionModel().getSelectedItem();
        System.out.println(dtoData);
        System.out.println("roleId----->" + dtoData.getId());
        System.out.println("zone name----->" + dtoData.getText());

        if (dtoData != null) {
            cmbAreaHeadCreateZone.getSelectionModel().select(dtoData);
            selZoneName = dtoData.getId();
            selZoneId = Integer.parseInt(dtoData.getId());
            getStateByZone();
        } else {
            System.out.println("No item selected.");
        }
//        CommonValidationsUtils.checkComboFieldAndHighlight(cmbAreaHeadCreateZone);

    }

    @FXML
    private void handleComboBoxActionRegionHead() {

        CommonDTO dtoData = (CommonDTO) cmbAreaHeadCreateRegion.getSelectionModel().getSelectedItem();
        System.out.println("roleId----->" + dtoData.getId());

        if (dtoData != null) {
            cmbAreaHeadCreateRegion.getSelectionModel().select(dtoData);
            selRegionName = dtoData.getText();
            selRegionId = Integer.parseInt(dtoData.getId());

            regionZoneHead = selRegionName;
            regionZoneHeadId = String.valueOf(selRegionId);
            regionId = String.valueOf(selRegionId);

            getDataByZone();
        } else {
            System.out.println("No item selected.");
        }
//        CommonValidationsUtils.checkComboFieldAndHighlight(cmbAreaHeadCreateRegion);

    }

    @FXML
    private void handleComboBoxActionDistrictHead() {

        CommonDTO dtoData = (CommonDTO) cmbAreaHeadCreateDistrict.getSelectionModel().getSelectedItem();
        System.out.println("roleId----->" + dtoData.getId());

        if (dtoData != null) {
            cmbAreaHeadCreateDistrict.getSelectionModel().select(dtoData);
            selDistrictName = dtoData.getText();
            selDistrictId = Integer.parseInt(dtoData.getId());


            districtId = String.valueOf(selDistrictId);

            getDataByDistrict();
        } else {
            System.out.println("No item selected.");
        }
//        CommonValidationsUtils.checkComboFieldAndHighlight(cmbAreaHeadCreateDistrict);

    }

    public void getStateByZone() {
//        System.exit(0);
        tfZHStateHead.setText("");

        Map<String, String> map = new HashMap<>();
        map.put("areaId", String.valueOf(selZoneId));
        map.put("role", "zonal");
        String formData = Globals.mapToStringforFormData(map);
        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "get_parent_head_by_role");
        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
        System.out.println("Response=> i am in" + responseBody);
        tfZHStateHead.setText("");

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            JsonArray jsonArray = responseBody.get("result").getAsJsonArray();
            int arrayLength = jsonArray.size();
            if (arrayLength > 0) {
                System.out.println(jsonArray);
                JsonElement element = jsonArray.get(0); // Retrieves the first element
                if (element.isJsonObject()) { // Check if it's a JsonObject
                    JsonObject firstObject = element.getAsJsonObject();
                    System.out.println(firstObject.get("name")); // Outputs the entire object
                    String name = String.valueOf(firstObject.get("name"));
                    zoneStateHead = String.valueOf(firstObject.get("id"));
                    zoneStateHeadId = String.valueOf(firstObject.get("id"));
                    zoneStateMainId = String.valueOf(firstObject.get("stateId"));
                    tfZHStateHead.setText(name);
                }
            }

        }

    }

    public void getDataByZone() {
        tfRGStateHead.setText("");
        tfRGZoneHead.setText("");
        Map<String, String> map = new HashMap<>();
        map.put("areaId", String.valueOf(selRegionId));
        map.put("role", "region");
        String formData = Globals.mapToStringforFormData(map);
        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "get_parent_head_by_role");
        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
        System.out.println("Response=> i am in" + responseBody);
        tfZHStateHead.setText("");

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            JsonArray jsonArray = responseBody.get("result").getAsJsonArray();
            int arrayLength = jsonArray.size();
            if (arrayLength > 0) {
                System.out.println(jsonArray);

                // Check if the array has at least two elements
                if (arrayLength > 1) {
                    JsonElement element = jsonArray.get(0); // Retrieves the first element
                    JsonElement element1 = jsonArray.get(1); // Retrieves the second element

                    // Check if the elements are JsonObjects
                    if (element.isJsonObject() && element1.isJsonObject()) {
                        JsonObject firstObject = element.getAsJsonObject();
                        JsonObject secondObject = element1.getAsJsonObject();

                        // Check if the necessary fields are present in the objects
                        if (firstObject.has("name") && firstObject.has("id") && firstObject.has("stateId") &&
                                secondObject.has("name") && secondObject.has("zoneId")) {

                            System.out.println(firstObject.get("name")); // Outputs the entire object
                            String name = firstObject.get("name").getAsString();
                            String name1 = secondObject.get("name").getAsString();
                            tfRGStateHead.setText(name);
                            tfRGZoneHead.setText(name1);

                            regionStateHeadId = firstObject.get("id").getAsString();
                            regionStateMainId = firstObject.get("stateId").getAsString();
                            regionZoneHeadMainId = secondObject.get("zoneId").getAsString();
                        } else {
                            System.out.println("Required fields are missing in the JSON objects");
                        }
                    } else {
                        System.out.println("The elements in the array are not JSON objects");
                    }
                } else {
                    System.out.println("The JSON array does not have enough elements");
                }
            }
        }
    }


    public void getDataByDistrict() {
        tfDSStateHead.setText("");
        tfDSZoneHead.setText("");
        tfDSRegionHead.setText("");

        Map<String, String> map = new HashMap<>();
        map.put("areaId", String.valueOf(selDistrictId));
        map.put("role", "district");
        String formData = Globals.mapToStringforFormData(map);
        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "get_parent_head_by_role");
        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
        System.out.println("Response=> i am in" + responseBody);
        tfZHStateHead.setText("");

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            JsonArray jsonArray = responseBody.get("result").getAsJsonArray();
            int arrayLength = jsonArray.size();
            if (arrayLength > 0) {
                System.out.println(jsonArray);

                // Check if the array has at least three elements
                if (arrayLength > 2) {
                    JsonElement element = jsonArray.get(0); // Retrieves the first element
                    JsonElement element1 = jsonArray.get(1); // Retrieves the second element
                    JsonElement element2 = jsonArray.get(2); // Retrieves the third element

                    // Check if the elements are JsonObjects
                    if (element.isJsonObject() && element1.isJsonObject() && element2.isJsonObject()) {
                        JsonObject firstObject = element.getAsJsonObject();
                        JsonObject secondObject = element1.getAsJsonObject();
                        JsonObject thirdObject = element2.getAsJsonObject();

                        // Check if the necessary fields are present in the objects
                        if (firstObject.has("name") && firstObject.has("id") && firstObject.has("stateId") &&
                                secondObject.has("name") && secondObject.has("id") && secondObject.has("zoneId") &&
                                thirdObject.has("name") && thirdObject.has("id") && thirdObject.has("regionId") &&
                                thirdObject.has("stateId") && thirdObject.has("zoneId")) {

                            System.out.println(firstObject.get("name")); // Outputs the name field
                            String name = firstObject.get("name").getAsString();
                            String name1 = secondObject.get("name").getAsString();
                            String name2 = thirdObject.get("name").getAsString();
                            tfDSStateHead.setText(name);
                            tfDSZoneHead.setText(name1);
                            tfDSRegionHead.setText(name2);

                            districtRegionHead = name2;
                            districtRegionHeadId = thirdObject.get("id").getAsString();
                            districtZoneHeadId = thirdObject.get("zoneId").getAsString();

                            districtStateHead = name;
                            districtStateHeadId = firstObject.get("id").getAsString();
                            districtStateHeadMainId = firstObject.get("stateId").getAsString();

                            districtZoneHeadMainId = secondObject.get("zoneId").getAsString();
                            districtRegionHeadMainId = thirdObject.get("regionId").getAsString();

                            regionId = thirdObject.get("id").getAsString();

                            regionZoneHead = name1;
                            regionZoneHeadId = secondObject.get("id").getAsString();

                            regionStateMainId = thirdObject.get("stateId").getAsString();
                            regionZoneHeadMainId = secondObject.get("stateId").getAsString();
                        } else {
                            System.out.println("Required fields are missing in the JSON objects");
                        }
                    } else {
                        System.out.println("The elements in the array are not JSON objects");
                    }
                } else {
                    System.out.println("The JSON array does not have enough elements");
                }
            }
        }
    }


    public void selectItemById(JsonElement targetId, ComboBox comboBoxParticular, Runnable action) {
        if (cmbAreaHeadCreateState == null) {
            System.out.println("ComboBox is not initialized.");
            return;
        }

        ObservableList<CommonDTO> items = comboBoxParticular.getItems();

        if (items != null && !items.isEmpty()) {
            boolean itemFound = false;

            // Find the item with the specified ID
            for (CommonDTO item : items) {
                if (item.getId().matches(String.valueOf(targetId))) {
                    comboBoxParticular.getSelectionModel().select(item);
                    itemFound = true;
                    break;
                }
            }

            action.run();

            if (!itemFound) {
                System.out.println("Item with ID " + targetId + " not found.");
            }
        } else {
            System.out.println("ComboBox items list is null or empty.");
        }
    }

    //todo:Validate Pincode for Franchise Pincode
    public void validatePincode() {
        System.out.println("i am in");
        APIClient apiClient = null;
        try {
//            logger.debug("Get ValidatePincode Data Started...");
            Map<String, String> map = new HashMap<>();
            map.put("pincode", String.valueOf(tfAreaHeadCreatePincode.getText()));
            String formData = Globals.mapToStringforFormData(map);
//            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.VALIDATE_PINCODE_ENDPOINT);
            apiClient = new APIClient(EndPoints.VALIDATE_PINCODE_ENDPOINT, formData, RequestType.FORM_DATA);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

//                    responseBody = response.body();
//                    JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
//                        Platform.runLater(() -> {
//                            cmbCmpCreateRegArea.requestFocus(); // Set focus back to tfFranchiseCreateRPincode
//                        });
                    } else {
                        String message = jsonObject.get("message").getAsString();
                        AlertUtility.AlertDialogForError("WARNING", message, input -> {
                            if (input) {
                                tfAreaHeadCreatePincode.requestFocus();
                            }
                        });
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
//                    logger.error("Network API cancelled in validatePincode()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
//                    logger.error("Network API failed in validatePincode()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
//            logger.debug("Get ValidatePincode Data End...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    public void validatePincodeR() {
        APIClient apiClient = null;
        try {
//            logger.debug("Get ValidatePincode Data Started...");
            Map<String, String> map = new HashMap<>();
            map.put("pincode", String.valueOf(tfAreaHeadCreateRPincode.getText()));
            String formData = Globals.mapToStringforFormData(map);
//            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.VALIDATE_PINCODE_ENDPOINT);
            apiClient = new APIClient(EndPoints.VALIDATE_PINCODE_ENDPOINT, formData, RequestType.FORM_DATA);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

//                    responseBody = response.body();
//                    JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
//                        Platform.runLater(() -> {
//                            cmbCmpCreateCorpArea.requestFocus(); // Set focus back to tfFranchiseCreateRPincode
//                        });
                    } else {
                        String message = jsonObject.get("message").getAsString();
                        AlertUtility.AlertDialogForError("WARNING", message, input -> {
                            if (input) {
                                tfAreaHeadCreateRPincode.requestFocus();
                            }
                        });
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
//                    logger.error("Network API cancelled in validateCorpPincode()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
//                    logger.error("Network API failed in validateCorpPincode()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
//            logger.debug("Get ValidatePincode Data End...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }


    public void getEditDataById() {
//        System.exit(0);
        Map<String, String> map = new HashMap<>();
        map.put("id", Globals.areaHeadListDTO.getId());
        String formData = Globals.mapToStringforFormData(map);
        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "get_area_head_by_id");
        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
        System.out.println("Response=> i am in" + responseBody);
        JsonObject jsonObject = responseBody.get("responseObject").getAsJsonObject();
        if (responseBody.get("responseStatus").getAsInt() == 200) {
            tfAreaHeadCreateUsername.setText(jsonObject.get("userName").getAsString());
            tfAreaHeadCreatePassword.setText(jsonObject.get("userPassword").getAsString());
            tfAreaHeadCreateFirstName.setText(jsonObject.get("firstName").getAsString());
            tfAreaHeadCreateMiddleName.setText(jsonObject.get("middleName").getAsString());
            tfAreaHeadCreateLastName.setText(jsonObject.get("lastName").getAsString());
            tfAreaHeadCreateEmail.setText(jsonObject.get("email").getAsString());
            tfAreaHeadCreateMobile.setText(jsonObject.get("mobileNumber").getAsString());
            tfAreaHeadCreateWhatsApp.setText(jsonObject.get("whatsAppNumber").getAsString());

            LocalDate dob = LocalDate.parse(jsonObject.get("DOB").getAsString());

            sameAddress = jsonObject.get("sameAsAddress").getAsBoolean();

            System.out.println("sameAddress : "+sameAddress);
            cbAreaHeadCreateSameasAddress.setSelected(sameAddress);

            if(sameAddress){
                tfAreaHeadCreateTemporaryAddress.setDisable(true);
                tfAreaHeadCreateRPincode.setDisable(true);
                cmbAreaHeadCreateRArea.setDisable(true);
                lbAreaHeadCreateRCity.setDisable(true);
                lbAreaHeadCreateRState.setDisable(true);
                cmbAreaHeadCreateRCountry.setDisable(true);
            }

//            dpAreaHeadCreateDOB.setText(dob.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            dpAreaHeadCreateDOB.setText(dob.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            String areaRoleName = (jsonObject.get("areaRole").getAsString());
            int areaOfId = 0;
            System.out.println(areaRoleName);


            if (areaRoleName.equals("district")) {
                areaOfId = 4;
            } else if (areaRoleName.equals("region")) {
                areaOfId = 3;
            } else if (areaRoleName.equals("zonal")) {
                areaOfId = 2;
            } else if (areaRoleName.equals("state")) {
                areaOfId = 1;
            }

            ObservableList<AreaHeadRoleTypeDTO> items = cmbAreaHeadCreateRole.getItems();
            if (items != null) {
                // Find the item with the specified ID
                for (AreaHeadRoleTypeDTO item : items) {
                    if (item.getId() == areaOfId) { // Assuming getId() returns the ID
                        cmbAreaHeadCreateRole.getSelectionModel().select(item);
                        handleComboBoxActionRoleHead();
                        break; // Exit the loop once the correct item is found
                    }
                }
            }

            JsonObject dataObj = new Gson().fromJson(jsonObject.get(areaRoleName.equals("zonal") ? "zone" : areaRoleName), JsonObject.class);


            if (areaRoleName.equals("district")) {
                selectItemById(dataObj.get("value"), cmbAreaHeadCreateDistrict, this::handleComboBoxActionDistrictHead);
            } else if (areaRoleName.equals("region")) {
                selectItemById(dataObj.get("value"), cmbAreaHeadCreateRegion, this::handleComboBoxActionRegionHead);
            } else if (areaRoleName.equals("zonal")) {
                selectItemById(dataObj.get("value"), cmbAreaHeadCreateZone, this::handleComboBoxActionZoneHead);
            } else if (areaRoleName.equals("state")) {
                selectItemById(dataObj.get("value"), cmbAreaHeadCreateState, this::handleComboBoxActionStateHead);
            }


            String gender = jsonObject.get("gender").getAsString();
//            if (gender.equals("true")) {
//                rbAreaHeadCreateMale.setSelected(true);
//            } else {
//                rbAreaHeadCreateFemale.setSelected(true);
//            }

            if (gender.equals("true")) {
                cmbFranchiseCreateGender.getSelectionModel().select(0);
                isGender = "true";
            } else {
                cmbFranchiseCreateGender.getSelectionModel().select(1);
                isGender = "false";
            }

            selectedRadioValue = jsonObject.get("gender").getAsString();


            tfAreaHeadCreatePermanentAddress.setText(jsonObject.get("permenantAddress").getAsString());
            tfAreaHeadCreateTemporaryAddress.setText(jsonObject.get("temporaryAddress").getAsString());

            tfAreaHeadCreatePincode.setText(jsonObject.get("pincode").getAsString());
            tfAreaHeadCreateRPincode.setText(jsonObject.get("corpPincode").getAsString());

            lbAreaHeadCreateCity.setText(jsonObject.get("city").getAsString());
            lbAreaHeadCreateRCity.setText(jsonObject.get("corpCity").getAsString());


            lbAreaHeadCreateState.setText(jsonObject.get("stateName").getAsString());
            lbAreaHeadCreateRState.setText(jsonObject.get("corpStateName").getAsString());

            tfAreaHeadCreateBankName.setText(jsonObject.get("bankAccName").getAsString());
            tfAreaHeadCreateIFSC.setText(jsonObject.get("bankIfsc").getAsString());
            tfAreaHeadCreateBankAccountNo.setText(jsonObject.get("bankAccNo").getAsString());
            tfAreaHeadCreateAdhaarText.setText(jsonObject.get("aadharCard").getAsString());
            tfAreaHeadCreatePanText.setText(jsonObject.get("panCard").getAsString());

            tfAreaHeadCreateBank.setText(jsonObject.get("bankAccFile").getAsString());
            tfAreaHeadCreateAdhaar.setText(jsonObject.get("aadharCardFile").getAsString());
            tfAreaHeadCreatePan.setText(jsonObject.get("panCardFile").getAsString());
            tfAreaHeadCreatePartnership.setText(jsonObject.get("partnerDeedFile").getAsString());

            // Assuming jsonObject.get("DOB").getAsString() returns a valid string representation of the date of birth
//            String dobString = jsonObject.get("DOB").getAsString();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Specify the format of your date string
//            LocalDate dob = LocalDate.parse(dobString, formatter);
//            dpAreaHeadCreateDOB.setValue(dob);

            tfAreaHeadCreateEmail.setText(jsonObject.get("email").getAsString());

            getAddressDataByPincode();
            getAddressDataByRPincode();

//            setSameAsAddress();


            areaId = Long.valueOf(jsonObject.get("areaId").toString());
            areaIdR = Long.valueOf(jsonObject.get("corpAreaId").toString());
            stateCode = jsonObject.get("stateCode").getAsString();


            if (areaId != null) {
                for (FranchiseAreaDTO item : cmbAreaHeadCreateArea.getItems()) {
                    System.out.println(item);
                    if (item.getId().equals(areaId.toString())) {
                        // If found, select this item in the ComboBox
                        cmbAreaHeadCreateArea.getSelectionModel().select(item);
                        break;
                    }
                }
            }
//
            if (areaIdR != null) {
                for (FranchiseAreaDTO item : cmbAreaHeadCreateRArea.getItems()) {
                    System.out.println(item);
                    if (item.getId().equals(areaIdR.toString())) {
                        // If found, select this item in the ComboBox
                        cmbAreaHeadCreateRArea.getSelectionModel().select(item);
                        break;
                    }
                }
            }


        }
    }

//    private void selectItemById(JsonElement value) {
//    }

    //    FNCancel
    public void cancelButton() {
//            Globals.areaHeadListDTO=null;
//            GlobalController.getInstance().addTabStatic(AREA_HEAD_LIST_SLUG,false);

        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                GlobalController.getInstance().addTabStatic(AREA_HEAD_LIST_SLUG, false);
            } else {
                System.out.println("working!");
            }
        };
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are sure to cancel", callback);

//        try {
//            Parent parent = FXMLLoader.load(getClass().getResource("/com/opethic/genivis/ui/master/areaHeadList.fxml"));
//            Stage stage = new Stage();
//            stage.setTitle("Create Page");
//            stage.setMaximized(true);
//            stage.setScene(new Scene(parent, 1920, 1080));
//            stage.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
//    FNEditBYId





