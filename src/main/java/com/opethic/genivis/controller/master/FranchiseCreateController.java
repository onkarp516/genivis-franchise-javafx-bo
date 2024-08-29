package com.opethic.genivis.controller.master;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.controller.commons.SwitchButton;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.UnaryOperator;

import static com.opethic.genivis.utils.FxmFileConstants.FRANCHISE_LIST_SLUG;

public class FranchiseCreateController implements Initializable {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(FranchiseCreateController.class);
    //? this is for the Loggers of this Page
    private static final Logger FranchiseControllerlogger = LoggerFactory.getLogger(FranchiseCreateController.class);
    ObservableList<FranchiseDistrictHeadDTO> listDistrictHead = FXCollections.observableArrayList();
    ObservableList<FranchiseAreaDTO> list = FXCollections.observableArrayList();
    ObservableList<FranchiseAreaDTO> listR = FXCollections.observableArrayList();

    ToggleGroup toggleGroup = new ToggleGroup();
    String selectedRadioValue = "";
    String message = "";
    private String responseBody;
    private String pinText = "";
    private String corppinText = "";
    private final List<String> districtHeadList = new ArrayList<>();
    private final Map<String, String> districtMapList = new HashMap<>();
    private final Map<String, String> countryMapList = new HashMap<>();
    private final Map<String, String> AreaMapList = new HashMap<>();
    private Long dHId = 0L;
    private Long countryId = 0L;
    private Long buinessTypeId = 0L;
    private final Long districtHeadId = 0L;
    private Long areaId = 0L;
    private Long areaIdR = 0L;
    private Long educationId = 0L;
    private String buisnessTypeName = "", regionalId = "", zoneId = "", stateId1 = "", stateCode = "", stateCodeR = "", districtName = "", districtNameR = "";
    private boolean isFunded = false; // Variable to hold the state of tgFranchiseIsFunding
    private boolean sameAddress; // Variable to hold the state of tgFranchiseIsFunding
    @FXML
    private TextField tfFranchiseCreateFranchiseCode, tfFranchiseCreateFranchiseName, tfFranchiseCreateApplicantName, tfFranchiseCreatePartnerName, tfFranchiseCreateRegionalHead,
            tfFranchiseCreateStateName, tfFranchiseCreateZoneHead, tfFranchiseCreateFundingAmount, tfFranchiseCreateMobileNo, tfFranchiseCreateWhatsappNo, tfFranchiseCreateAge,
            tfFranchiseCreateEmailId, tfFranchiseCreatePresentOccupation, tfFranchiseCreateAddress, tfFranchiseCreatePincode, tfFranchiseCreateLatitude, tfFranchiseCreateLongitude,
            tfFranchiseCreateRAddress, tfFranchiseCreateRPincode;
    @FXML
    private VBox vboxIsFunding, vboxFranchiseCreateRoot;
    @FXML
    private ComboBox cmbFranchiseCreateDistrictHead, cmbFranchiseCreateBusinessType, cmbFranchiseCreateEducation, cmbFranchiseCreateCountry, cmbFranchiseCreateRArea,
            cmbFranchiseCreateRCountry,cmbFranchiseCreateGender;
    @FXML
    private ComboBox<FranchiseAreaDTO> cmbFranchiseCreateArea;
    @FXML
    private CheckBox cbFranchiseCreateReadytoinvest, cbFranchiseCreateSameasAddress;
    @FXML
    private HBox toggleValidation;
    //    @FXML
//    private CheckBox cbFranchiseCreateFunding;
    @FXML
    private RadioButton rbFranchiseCreateMale, rbFranchiseCreateFemale;
    @FXML
    private ToggleGroup gender;
    private String isGender; // Variable to store the selected gender
    @FXML
    private TextField dpFranchiseCreateDOB;
    @FXML
    private Label tfFranchiseCreateCity, tfFranchiseCreateState, tfFranchiseCreateRState, tfFranchiseCreateRCity;
    @FXML
    private Button btnFranchiseCreateSubmit, btnFranchiseCreateCancel;
    private JsonObject jsonObject = null;
    private String FranchiseListDTOId;
    private FranchiseListDTO franchiseListDTO;
    private Node[] focusableNodes;
    @FXML
    private SwitchButton tgFranchiseIsFunding;
    @FXML
    private ScrollPane spRootFranchiseCreatePane;

    //?:Entry Point of this Page the Execution Starts from here initialize
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CommonValidationsUtils.changeStarColour(vboxFranchiseCreateRoot);

        //? this include all the Shortcut Keys
        initShorcutKeys();

        //? this inculde all the design related properties and important Fields
        mandatoryFields();

        //? this include all the table view operations - Edit , search , etc..
        whatsappNumberValidation();

        tgFranchiseIsFunding.setParentBox(vboxIsFunding);

        //DatePicker to TextField with validation
        DateValidator.applyDateFormat(dpFranchiseCreateDOB);
        sceneInitilization();

        //Focus on the First Field of the Page
        Platform.runLater(() -> {
            tfFranchiseCreateFranchiseCode.requestFocus();
            //Disable the Field to Read Only
            tfFranchiseCreateRegionalHead.setDisable(true);
            tfFranchiseCreateRegionalHead.setStyle("-fx-opacity: 1;"); // Make textfield fully visible
            tfFranchiseCreateRegionalHead.pseudoClassStateChanged(PseudoClass.getPseudoClass("disabled"), false);
            tfFranchiseCreateZoneHead.setDisable(true);
            tfFranchiseCreateZoneHead.setStyle("-fx-opacity: 1;"); // Make textfield fully visible
            tfFranchiseCreateZoneHead.pseudoClassStateChanged(PseudoClass.getPseudoClass("disabled"), false);
            tfFranchiseCreateStateName.setDisable(true);
            tfFranchiseCreateStateName.setStyle("-fx-opacity: 1;"); // Make textfield fully visible
            tfFranchiseCreateStateName.pseudoClassStateChanged(PseudoClass.getPseudoClass("disabled"), false);
            tfFranchiseCreateAge.setDisable(true);
            tfFranchiseCreateAge.setStyle("-fx-opacity: 1;"); // Make textfield fully visible
            tfFranchiseCreateAge.pseudoClassStateChanged(PseudoClass.getPseudoClass("disabled"), false);
        });

        //? Initially, hide the TextField
        tfFranchiseCreateFundingAmount.setVisible(false);
        //? Dynamic District Head List
        getDistrictHead();
        //? Static Business Type List
        getBusinessTypeData();
        //? Static Eduction List
        getEducation();
        //? get Country Data
        getCountry();

        //*********************************************************Call Functions On Focus Start***************************************************************************
        // ? Add event listener to the DatePicker to set the Calculated Age by getting the DOB
        dpFranchiseCreateDOB.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                handleDatePickerAction();
            }
        });

        //*********************************************************Call Functions On Focus END***************************************************************************

        //? Open Create and Edit Page On id if ID is Null Open Create Page and If ID is Not Null Open Edit Page
        if (Globals.franchiseListDTO != null) {
            getEditDataById();
            btnFranchiseCreateSubmit.setText("Update");
            tfFranchiseCreateFranchiseCode.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    duplicateFranchiseEdit();
                }
            });
        } else {
            Globals.franchiseListDTO = null;
            tfFranchiseCreateFranchiseCode.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    duplicateFranchise();
                }
            });
            //? Delayed initialization to ensure tgFranchiseIsFunding's width is properly calculated
        }



        //?hide and show field on checkbox
        //?Setting the value to true initially SwitchButton to false on page load
        tgFranchiseIsFunding.switchOnProperty().set(isFunded);

        //?Add a listener to update the isFunded variable when the SwitchButton value changes
        tgFranchiseIsFunding.switchOnProperty().addListener((observable, oldValue, newValue) -> {
            isFunded = newValue;

            //Toggle visibility of tfFranchiseCreateFundingAmount based on the value of tgFranchiseIsFunding
            tfFranchiseCreateFundingAmount.setVisible(newValue);
        });

        //?Automatically Enter the Number in the WhatsApp textfield when entered in the Mobile No textfield
        tfFranchiseCreateMobileNo.textProperty().addListener((observable, oldValue, newValue) -> {
            // Copying the value from mobileField to whatsappField
            tfFranchiseCreateWhatsappNo.setText(newValue);
        });

        //? Set indeterminate property to false to prevent displaying the true/false text
        cbFranchiseCreateReadytoinvest.setIndeterminate(false);

        //?address registered and corporate sameasaddress
        tfFranchiseCreateAddress.textProperty().addListener((observable, oldValue, newValue) -> {
            if (cbFranchiseCreateSameasAddress.isSelected())
                tfFranchiseCreateRAddress.setText(newValue);
        });



        btnFranchiseCreateSubmit.setOnAction(e -> {
            if (CommonValidationsUtils.validateForm(tfFranchiseCreateFranchiseCode, tfFranchiseCreateFranchiseName, tfFranchiseCreateApplicantName, cmbFranchiseCreateDistrictHead, cmbFranchiseCreateBusinessType, tfFranchiseCreateMobileNo, dpFranchiseCreateDOB, cmbFranchiseCreateEducation, tfFranchiseCreateAddress, tfFranchiseCreatePincode, tfFranchiseCreateRAddress, tfFranchiseCreateRPincode)) {
                createFranchise();
            }
        });
        handleRadioButtonAction();

    }
    private void cursorMomentForComboBox(ComboBox<?> comboBox) {
        comboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (comboBox.getSelectionModel().getSelectedIndex() == -1) {
                    // No item selected in the ComboBox
                    comboBox.requestFocus();
                }
            }
        });
    }

    //? Keep the Focus on the Mandatory Fields
    private void mandatoryFields() {
        //? Pincode Validation Enter Only 6 Numbers
        CommonValidationsUtils.onlyEnterNumbersLimit(tfFranchiseCreatePincode,6);
        CommonValidationsUtils.onlyEnterNumbersLimit(tfFranchiseCreateRPincode,6);
        CommonFunctionalUtils.restrictTextFieldToDigitsAndBirthDateFormat(dpFranchiseCreateDOB);
        // ? Add listener for focus change to restrict Mobile Number and Email when cursor moves away
        CommonValidationsUtils.restrictMobileNumber(tfFranchiseCreateMobileNo);
        CommonValidationsUtils.restrictEmail(tfFranchiseCreateEmailId);
        CommonValidationsUtils.onlyEnterNumbers(tfFranchiseCreateFundingAmount);

        //*********************************************************Stop the Cusor on the Field START***************************************************************************

        tfFranchiseCreateFranchiseCode.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = tfFranchiseCreateFranchiseCode.getText().trim();
                if (text.isEmpty()) {
                    tfFranchiseCreateFranchiseCode.requestFocus();
                }
            }
        });
        //Stop the Cusor on the Field
        tfFranchiseCreateFranchiseName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = tfFranchiseCreateFranchiseName.getText().trim();
                if (text.isEmpty()) {
                    tfFranchiseCreateFranchiseName.requestFocus();
                }
            }
        });
        //Stop the Cusor on the Field
        tfFranchiseCreateApplicantName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = tfFranchiseCreateApplicantName.getText().trim();
                if (text.isEmpty()) {
                    tfFranchiseCreateApplicantName.requestFocus();
                }
            }
        });

        tfFranchiseCreateMobileNo.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = tfFranchiseCreateMobileNo.getText().trim();
                if (text.isEmpty()) {
                    tfFranchiseCreateMobileNo.requestFocus();
                }
            }
        });
        dpFranchiseCreateDOB.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = dpFranchiseCreateDOB.getText().trim();
                if (text.isEmpty()) {
                    dpFranchiseCreateDOB.requestFocus();
                }
            }
        });
        tfFranchiseCreateAddress.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = tfFranchiseCreateAddress.getText().trim();
                if (text.isEmpty()) {
                    tfFranchiseCreateAddress.requestFocus();
                }
            }
        });
        tfFranchiseCreateRAddress.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = tfFranchiseCreateRAddress.getText().trim();
                if (text.isEmpty()) {
                    tfFranchiseCreateRAddress.requestFocus();
                }
            }
        });
        tfFranchiseCreatePincode.focusedProperty().addListener((obs, old, nw) -> {

            if (!nw) {
                validatePincode();
                if (tfFranchiseCreatePincode.getText().trim().isEmpty()) {
                    tfFranchiseCreatePincode.requestFocus();
                }
                String newVal = tfFranchiseCreatePincode.getText().trim();
                if (newVal.length() > 6) {
                    tfFranchiseCreatePincode.setText(newVal.substring(0, 6));
//                    validatePincode();
                } else if (newVal.length() == 6 && (pinText.isEmpty() || !pinText.trim().contains(newVal))) {
                    pinText = newVal;
                    getAddressDataByPincode();
                }
                if (cbFranchiseCreateSameasAddress.isSelected()) {
                    tfFranchiseCreateRPincode.setText(newVal);
                }
            }
        });

        tfFranchiseCreateRPincode.focusedProperty().addListener((obs1, oldValue, newValue) -> {
            if (!newValue) {
                validatePincodeR();
                if (tfFranchiseCreateRPincode.getText().trim().isEmpty()) {
                    tfFranchiseCreateRPincode.requestFocus();
                }
                String newVal1 = tfFranchiseCreateRPincode.getText().trim();
                if (newVal1.length() > 6) {
                    tfFranchiseCreateRPincode.setText(newVal1.substring(0, 6));

                } else if (newVal1.length() == 6 && (corppinText.isEmpty() || !corppinText.equalsIgnoreCase(newVal1))) {
                    corppinText = newVal1;
                    getAddressDataByPincodeOfResidential();
                }
            }
        });
        cursorMomentForComboBox(cmbFranchiseCreateDistrictHead);
        cursorMomentForComboBox(cmbFranchiseCreateBusinessType);
        cursorMomentForComboBox(cmbFranchiseCreateEducation);
        cursorMomentForComboBox(cmbFranchiseCreateGender);


        //*********************************************************Stop the Cusor on the Field END***************************************************************************
    }

    //? Shorcut Keys Tab ,Enter and All
    private void initShorcutKeys() {
        spRootFranchiseCreatePane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("submit")) {
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("cancel")) {
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("update")) {
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
                btnFranchiseCreateSubmit.fire();
            } else if (event.getCode() == KeyCode.X && event.isControlDown()) {
                btnFranchiseCreateCancel.fire();
            }
        });
        spRootFranchiseCreatePane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        spRootFranchiseCreatePane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        CommonValidationsUtils.comboBoxDataShow(cmbFranchiseCreateDistrictHead);
        CommonValidationsUtils.comboBoxDataShow(cmbFranchiseCreateBusinessType);
        CommonValidationsUtils.comboBoxDataShow(cmbFranchiseCreateEducation);
        CommonValidationsUtils.comboBoxDataShow(cmbFranchiseCreateGender);
        CommonValidationsUtils.comboBoxDataShow(cmbFranchiseCreateArea);
        CommonValidationsUtils.comboBoxDataShow(cmbFranchiseCreateRArea);
    }

    public void sceneInitilization() {
        spRootFranchiseCreatePane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }

    //? On Clicking on the Cancel Button From Create or Edit page redirect Back to List page
    public void backToList() {
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                GlobalController.getInstance().addTabStatic(FRANCHISE_LIST_SLUG, false);
            } else {
            }
        };
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnCancel, callback);
    }

    //? whatsapp Number Validation Function
    private void whatsappNumberValidation() {
        //*********************************************************Whatsapp Number Validation START***************************************************************************
        // Define a UnaryOperator to allow only numeric input
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        };
        // Apply the filter to a TextFormatter
        TextFormatter<String> formatter = new TextFormatter<>(filter);
        tfFranchiseCreateWhatsappNo.setTextFormatter(formatter);
        // Add a listener to check the input text while typing
        tfFranchiseCreateWhatsappNo.textProperty().addListener((observable, oldValue, newValue) -> {
            // Check if the new value matches the desired pattern
            if (!newValue.matches("\\d{0,10}")) {
                // If the new value does not match the pattern, replace it with the old value
                tfFranchiseCreateWhatsappNo.setText(oldValue);
            }
        });

        // Add a listener to check the length of the entered text after focus loss
        tfFranchiseCreateWhatsappNo.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // If focus lost
                String numbers = tfFranchiseCreateWhatsappNo.getText();
//                if (!numbers.isEmpty() && numbers.length() != 10) {
//                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please enter a 10-digit mobile number.", in -> {
//                        tfFranchiseCreateWhatsappNo.requestFocus();
//                    });
//                }
                AlertUtility.CustomCallback callback = (number) -> {
                    if (number == 0) {
                        tfFranchiseCreateWhatsappNo.requestFocus();
                    }
                };

                if (!numbers.isEmpty() && numbers.length() != 10) {
                    Stage stage = (Stage) spRootFranchiseCreatePane.getScene().getWindow();
                    AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Please enter a 10-digit mobile number.", callback);
                }
            }
        });

        //*********************************************************Whatsapp Number Validation END***************************************************************************
    }

    //? Function to Calculate the the Age by DOB
    private void handleDatePickerAction() {
        // ? Get the selected date from the DatePicker
        LocalDate selectedDate = Communicator.text_to_date.fromString(dpFranchiseCreateDOB.getText());


        if (selectedDate != null) {
            // ? Calculate the age based on the selected date
            LocalDate currentDate = LocalDate.now();
            Period period = Period.between(selectedDate, currentDate);

            //?  Display the calculated age in the TextField
            int age = period.getYears();
            tfFranchiseCreateAge.setText(String.valueOf(age));
        } else {
            //?  If no date is selected, clear the TextField
            tfFranchiseCreateAge.clear();
        }
    }


    //? to set the Gender values if Male set as true and if Female set as False
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

    @FXML
    private void handleComboBoxActionGender() {
        if (cmbFranchiseCreateGender.getSelectionModel().getSelectedItem() != null) {
            if(cmbFranchiseCreateGender.getSelectionModel().getSelectedItem()=="Male") {
                isGender = "true";
            }
            else {
                isGender="false";
            }
        } else {
            //?  use logger for alert messages
            logger.error("Please Select Proper Data");
        }
    }

    //? Get Business Type in Combo Box and send the Id in request
    public void getBusinessTypeData() {
        ObservableList<FranchiseBusinessTypeDTO> items = FXCollections.observableArrayList(
                new FranchiseBusinessTypeDTO("Proprietor", "1"),
                new FranchiseBusinessTypeDTO("Partner", "2"),
                new FranchiseBusinessTypeDTO("Trust", "3"),
                new FranchiseBusinessTypeDTO("Other", "4")
        );


        cmbFranchiseCreateBusinessType.setItems(items);

//        AutoCompleteBox autoCompleteBox = new AutoCompleteBox(cmbFranchiseCreateBusinessType, -1);


        cmbFranchiseCreateBusinessType.setConverter(new StringConverter<FranchiseBusinessTypeDTO>() {
            @Override
            public String toString(FranchiseBusinessTypeDTO o) {
                return o.getText();
            }

            @Override
            public FranchiseBusinessTypeDTO fromString(String s) {
                return null;
            }
        });
    }

    @FXML
    private void handleComboBoxActionBusinessType() {

        if (cmbFranchiseCreateBusinessType.getSelectionModel().getSelectedItem() != null) {
            FranchiseBusinessTypeDTO businessType = (FranchiseBusinessTypeDTO) cmbFranchiseCreateBusinessType.getSelectionModel().getSelectedItem();
            buinessTypeId = Long.valueOf(businessType.getId());
            buisnessTypeName = String.valueOf(businessType.getText());
        } else {
            //? use logger for alert messages
            logger.error("Please Select Proper Data");
        }
    }

    //? Get Education Data in Combo Box and send the Id in request
    public void getEducation() {
        ObservableList<FranchiseEducationDTO> items = FXCollections.observableArrayList(
                new FranchiseEducationDTO("10th - 12th", "1"),
                new FranchiseEducationDTO("Graduate", "2"),
                new FranchiseEducationDTO("Post graduate", "3"),
                new FranchiseEducationDTO("Other", "4")
        );


        cmbFranchiseCreateEducation.setItems(items);

//        AutoCompleteBox autoCompleteBox = new AutoCompleteBox(cmbFranchiseCreateEducation, -1);


        cmbFranchiseCreateEducation.setConverter(new StringConverter<FranchiseEducationDTO>() {
            @Override
            public String toString(FranchiseEducationDTO o) {
                return o.getText();
            }

            @Override
            public FranchiseEducationDTO fromString(String s) {
                return null;
            }
        });
    }

    @FXML
    private void handleComboBoxActionEducation() {
        if (cmbFranchiseCreateEducation.getSelectionModel().getSelectedItem() != null) {
            FranchiseEducationDTO education = (FranchiseEducationDTO) cmbFranchiseCreateEducation.getSelectionModel().getSelectedItem();
            educationId = Long.valueOf(education.getId());
        } else {
            //? use logger for alert messages
            logger.error("Please Select Proper Data");
        }
    }

    //? Get DistrictHead Data in Combo Box and send the Id in request
    public void getDistrictHead() {
        APIClient apiClient = null;
        try {
            logger.debug("Get District Head Data Started...");
//            HttpResponse<String> response = APIClient.getRequest(EndPoints.GET_ALL_DISTRICT_HEAD_ENDPOINT);
            apiClient = new APIClient("get_all_districts", "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                    responseBody = response.body();
//                    JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray jsonArray = jsonObject.get("responseObject").getAsJsonArray();
                        for (JsonElement mElement : jsonArray) {
                            JsonObject mObject = mElement.getAsJsonObject();
                            String fullName = mObject.get("districtName").getAsString();
                            String id = mObject.get("id").getAsString();
                            listDistrictHead.add(new FranchiseDistrictHeadDTO(fullName, id));
                            districtMapList.put(id, fullName);
                        }
                        cmbFranchiseCreateDistrictHead.setItems(listDistrictHead);

//                        AutoCompleteBox autoCompleteBox = new AutoCompleteBox(cmbFranchiseCreateDistrictHead, -1);
                        cmbFranchiseCreateDistrictHead.setConverter(new StringConverter<FranchiseDistrictHeadDTO>() {
                            @Override
                            public String toString(FranchiseDistrictHeadDTO o) {
                                return o.getText();
                            }

                            @Override
                            public FranchiseDistrictHeadDTO fromString(String s) {
                                return null;
                            }
                        });

                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getDistrictHead()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getDistrictHead()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            logger.debug("Get District Head Data End...");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }


    @FXML
    private void handleComboBoxActionDistrictHead() {

        Object[] object = new Object[1];
        object[0] = cmbFranchiseCreateDistrictHead.getSelectionModel().getSelectedItem();
        if (object[0] != null) {
            for (FranchiseDistrictHeadDTO franchiseDistrictHeadDTO : listDistrictHead) {
                if (franchiseDistrictHeadDTO.equals(object[0])) {
                    dHId = Long.valueOf(franchiseDistrictHeadDTO.getId());
                    getDistrictHeadsData();
                }
            }

        } else {
            //? use logger for alert messages
            logger.error("Please Select Proper Data");
        }
    }

    //? to set the data bu district head id of zonehead,regional head and state name
    public void getDistrictHeadsData() {

        Map<String, String> map = new HashMap<>();
        map.put("areaId", String.valueOf(dHId));
        map.put("role", "district");
        String formData = Globals.mapToStringforFormData(map);
        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "get_parent_head_by_role");
        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            JsonArray jsonArray = responseBody.get("result").getAsJsonArray();
            int arrayLength = jsonArray.size();
            if (arrayLength > 0) {

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

                            String name = firstObject.get("name").getAsString();
                            String name1 = secondObject.get("name").getAsString();
                            String name2 = thirdObject.get("name").getAsString();
//
                            tfFranchiseCreateStateName.setText(name);
                            stateId1 = firstObject.get("stateId").getAsString();

                            tfFranchiseCreateZoneHead.setText(name1);
                            zoneId = secondObject.get("zoneId").getAsString();

                            tfFranchiseCreateRegionalHead.setText(name2);
                            regionalId = thirdObject.get("regionId").getAsString();

                        } else {
                            logger.error("Required fields are missing in the JSON objects");
                        }
                    } else {
                        logger.error("The elements in the array are not JSON objects");
                    }
                } else {
                    logger.error("The JSON array does not have enough elements");
                }
            }
        }

    }

    //? set the data of country in the combobox and when selcted the id to be sent in request
    public void getCountry() {
        APIClient apiClient = null;
        try {
            logger.debug("Get Country Data Started...");
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
                        cmbFranchiseCreateCountry.setItems(list);

                        cmbFranchiseCreateCountry.getSelectionModel().selectFirst(); // Set the first item as default
                        FranchiseCountryDTO defaultSelectedItem = (FranchiseCountryDTO) cmbFranchiseCreateCountry.getSelectionModel().getSelectedItem();
                        String defaultSelectedId = defaultSelectedItem.getId();
                        countryId = Long.valueOf(defaultSelectedId);

                        cmbFranchiseCreateCountry.setConverter(new StringConverter<FranchiseCountryDTO>() {
                            @Override
                            public String toString(FranchiseCountryDTO o) {
                                return o.getText();
                            }

                            @Override
                            public FranchiseCountryDTO fromString(String s) {
                                return null;
                            }
                        });

                        cmbFranchiseCreateRCountry.setItems(list);
                        cmbFranchiseCreateRCountry.getSelectionModel().selectFirst(); // Set the first item as default
                        cmbFranchiseCreateRCountry.setConverter(new StringConverter<FranchiseCountryDTO>() {
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
                        //? use logger for alert messages
                        logger.error("Falied to Load Data ");
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getCountry()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getCountry()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            logger.debug("Get Country Data End...");

        } catch (Exception e) {
            e.printStackTrace();
            //? use logger for alert messages
            logger.error("Falied to connect server! ");
        } finally {
            apiClient = null;
        }
    }

    @FXML
    private void handleComboBoxActionCountry() {

        FranchiseCountryDTO countryDTO = (FranchiseCountryDTO) cmbFranchiseCreateCountry.getSelectionModel().getSelectedItem();
        if (countryDTO != null) {
            countryId = Long.valueOf(countryDTO.getId());
        } else {
            //? use logger for alert messages
            logger.error("Please Select Proper Data");
        }
    }

    //? Function to set Registerd Address Same in Residential Address
    public void setSameAsAddress() {
//        cbFranchiseCreateSameasAddress.setOnAction(event -> {
        if (cbFranchiseCreateSameasAddress.isSelected()) {
            tfFranchiseCreateRAddress.setDisable(true);
            tfFranchiseCreateRPincode.setDisable(true);
            cmbFranchiseCreateRArea.setDisable(true);
            tfFranchiseCreateRCity.setDisable(true);
            tfFranchiseCreateRState.setDisable(true);
            cmbFranchiseCreateRCountry.setDisable(true);
            tfFranchiseCreateRAddress.setText(tfFranchiseCreateAddress.getText());
            tfFranchiseCreateRPincode.setText(tfFranchiseCreatePincode.getText());
            cmbFranchiseCreateRArea.setItems(cmbFranchiseCreateArea.getItems());
            cmbFranchiseCreateRArea.setValue(cmbFranchiseCreateArea.getValue());
//                cbFranchiseCreateSameasAddress.setText("true");
//            FranchiseAreaDTO areaDTO = cmbFranchiseCreateArea.getSelectionModel().getSelectedItem();
//            if (areaDTO != null) {
//                String areaId = areaDTO.getId();
//                FranchiseAreaDTO selectedArea = null;
//                for (Object obj : cmbFranchiseCreateRArea.getItems()) {
//                    FranchiseAreaDTO districtHeadDTO = (FranchiseAreaDTO) obj;
//
//                    if (districtHeadDTO.getId().equals(areaId)) {
//
//                        selectedArea = districtHeadDTO;
//                        break;
//                    }
//                }
//
//                if (selectedArea != null) {
//                    cmbFranchiseCreateRArea.getSelectionModel().select(selectedArea);
//                }
//            }
            tfFranchiseCreateRCity.setText(tfFranchiseCreateCity.getText());
            tfFranchiseCreateRState.setText(tfFranchiseCreateState.getText());
            FranchiseCountryDTO countryDTO = (FranchiseCountryDTO) cmbFranchiseCreateCountry.getSelectionModel().getSelectedItem();
            if (countryDTO != null) {
                String countryId = countryDTO.getId();
                FranchiseCountryDTO selectedCountry = null;
                for (Object obj : cmbFranchiseCreateRCountry.getItems()) {
                    FranchiseCountryDTO countryListDTO = (FranchiseCountryDTO) obj;

                    if (countryListDTO.getId().equals(countryId)) {

                        selectedCountry = countryListDTO;
                        break;
                    }
                }

                if (selectedCountry != null) {
                    cmbFranchiseCreateRCountry.getSelectionModel().select(selectedCountry);
                }
            }
        } else {
//                cbFranchiseCreateSameasAddress.setText("false");
            tfFranchiseCreateRAddress.setText("");
//                cmbFranchiseCreateRArea.getSelectionModel().select("");
            cmbFranchiseCreateRArea.getSelectionModel().clearSelection();
            tfFranchiseCreateRCity.setText("");
            tfFranchiseCreateRState.setText("");
//                cmbFranchiseCreateRCountry.getSelectionModel().select("");
            tfFranchiseCreateRPincode.setText("");
            tfFranchiseCreateRAddress.setDisable(false);
            tfFranchiseCreateRPincode.setDisable(false);
            cmbFranchiseCreateRArea.setDisable(false);
            tfFranchiseCreateRCity.setDisable(false);
            tfFranchiseCreateRState.setDisable(false);
            cmbFranchiseCreateRCountry.setDisable(false);
        }
//        });
    }

    //? Function to set Ready to set Invest value to true or false
    public void setReadyToInvest() {
        cbFranchiseCreateReadytoinvest.setOnAction(event -> {
            if (cbFranchiseCreateReadytoinvest.isSelected()) {
//                cbFranchiseCreateReadytoinvest.setText("true");
            } else {
//                cbFranchiseCreateReadytoinvest.setText("false");
            }
        });
    }

    //? Set Data of Address by Pincode of Registered
    public void getAddressDataByPincode() {
        APIClient apiClient = null;
        try {
            logger.debug("Get Addres Data by Pincode Data Started...");
            Map<String, String> map = new HashMap<>();
            map.put("pincode", String.valueOf(tfFranchiseCreatePincode.getText()));
            String formData = Globals.mapToStringforFormData(map);
//            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.GET_PINCODE_ENDPOINT);
            apiClient = new APIClient(EndPoints.GET_PINCODE_ENDPOINT, formData, RequestType.FORM_DATA);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

//            responseBody = response.body();
//            JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray jsonArray = jsonObject.get("responseObject").getAsJsonArray();
                        list.clear();
                        for (JsonElement mElement : jsonArray) {
                            JsonObject mObject = mElement.getAsJsonObject();
                            String area = mObject.get("area").getAsString();
                            String district = mObject.get("district").getAsString();
                            String state = mObject.get("state").getAsString();
                            String stateCode = mObject.get("stateCode").getAsString();
                            String pincode = mObject.get("pincode").getAsString();

                            String id = mObject.get("id").getAsString();
                            list.add(new FranchiseAreaDTO(area, id, pincode, district, state, stateCode));
                            AreaMapList.put(id, area);
                        }
                        tfFranchiseCreateCity.setText(list.get(0).getDistrict());
                        tfFranchiseCreateState.setText(list.get(0).getState());
                        tfFranchiseCreateCity.setVisible(true);
                        tfFranchiseCreateState.setVisible(true);

                        if (cbFranchiseCreateSameasAddress.isSelected()) {
                            tfFranchiseCreateRCity.setText(list.get(0).getDistrict());
                            tfFranchiseCreateRState.setText(list.get(0).getState());
                        }
                        cmbFranchiseCreateArea.setItems(list);
                        cmbFranchiseCreateArea.setConverter(new StringConverter<FranchiseAreaDTO>() {
                            @Override
                            public String toString(FranchiseAreaDTO o) {
                                return o.getArea();
                            }

                            @Override
                            public FranchiseAreaDTO fromString(String s) {
                                return null;
                            }
                        });
                        cmbFranchiseCreateRArea.setItems(list);
                        cmbFranchiseCreateRArea.setConverter(new StringConverter<FranchiseAreaDTO>() {
                            @Override
                            public String toString(FranchiseAreaDTO o) {
                                return o.getArea();
                            }

                            @Override
                            public FranchiseAreaDTO fromString(String s) {
                                return null;
                            }
                        });
                    }
                }
            });

            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getAddressDataByPincode()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getAddressDataByPincode()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            logger.debug("Get Addres Data by Pincode Data End...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    @FXML
    private void handleComboBoxActionArea() {

        FranchiseAreaDTO franchiseAreaDTO = cmbFranchiseCreateArea.getSelectionModel().getSelectedItem();
        if (franchiseAreaDTO != null) {
            areaId = Long.valueOf(franchiseAreaDTO.getId());
            stateCode = franchiseAreaDTO.getStateCode();
            cmbFranchiseCreateArea.setValue(franchiseAreaDTO);
            districtName = String.valueOf(franchiseAreaDTO.getArea());
            tfFranchiseCreateCity.setText(franchiseAreaDTO.getDistrict());
            tfFranchiseCreateState.setText(franchiseAreaDTO.getState());

            if (cbFranchiseCreateSameasAddress.isSelected()) {
                setSameAsAddress();
            }
        } else {
            //? use logger for alert messages
            logger.error("Please Select Proper Data");
        }
    }

    //? Set Data of Address by Pincode of Residential
    public void getAddressDataByPincodeOfResidential() {
        APIClient apiClient = null;
        try {
            logger.debug("Get Adress Data by pincode of residential Data Started...");
            Map<String, String> map = new HashMap<>();
            map.put("pincode", String.valueOf(tfFranchiseCreateRPincode.getText()));
            String formData = Globals.mapToStringforFormData(map);
//            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.GET_PINCODE_ENDPOINT);
            apiClient = new APIClient(EndPoints.GET_PINCODE_ENDPOINT, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {

//            responseBody = response.body();
//            JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray jsonArray = jsonObject.get("responseObject").getAsJsonArray();
                        listR.clear();
                        for (JsonElement mElement : jsonArray) {
                            JsonObject mObject = mElement.getAsJsonObject();
                            String area = mObject.get("area").getAsString();
                            String district = mObject.get("district").getAsString();
                            String state = mObject.get("state").getAsString();
                            String stateCode = mObject.get("stateCode").getAsString();
                            String pincode = mObject.get("pincode").getAsString();

                            String id = mObject.get("id").getAsString();
                            listR.add(new FranchiseAreaDTO(area, id, pincode, district, state, stateCode));
                            AreaMapList.put(id, area);
                        }
                        tfFranchiseCreateRCity.setText(listR.get(0).getDistrict());
                        tfFranchiseCreateRState.setText(listR.get(0).getState());
                        tfFranchiseCreateRCity.setVisible(true);
                        tfFranchiseCreateRState.setVisible(true);
                        cmbFranchiseCreateRArea.setItems(listR);
                        cmbFranchiseCreateRArea.setConverter(new StringConverter<FranchiseAreaDTO>() {
                            @Override
                            public String toString(FranchiseAreaDTO o) {
                                return o.getArea();
                            }

                            @Override
                            public FranchiseAreaDTO fromString(String s) {
                                return null;
                            }
                        });
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getAddressDataByPincodeOfResidential()" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getAddressDataByPincodeOfResidential()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            logger.debug("Get Adress Data by pincode of residential Data End...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    @FXML
    private void handleComboBoxActionAreaOfResidential() {

        FranchiseAreaDTO franchiseAreaDTO = (FranchiseAreaDTO) cmbFranchiseCreateRArea.getSelectionModel().getSelectedItem();
        if (franchiseAreaDTO != null) {
            areaIdR = Long.valueOf(franchiseAreaDTO.getId());
            stateCodeR = franchiseAreaDTO.getStateCode();
            districtNameR = String.valueOf(franchiseAreaDTO.getArea());
            tfFranchiseCreateRCity.setText(franchiseAreaDTO.getDistrict());
            tfFranchiseCreateRState.setText(franchiseAreaDTO.getState());

        } else {
            //? use logger for alert messages
            logger.error("Please Select Proper Data ");
        }
    }

    //? Validate Code for Franchise Create
    public void duplicateFranchise() {
        APIClient apiClient = null;
        try {
            logger.debug("Get duplicateFranchise Data Started...");
            Map<String, String> map = new HashMap<>();
            map.put("franchiseCode", String.valueOf(tfFranchiseCreateFranchiseCode.getText()));
            String formData = Globals.mapToStringforFormData(map);

//            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.VALIDATE_PINCODE_ENDPOINT);
            apiClient = new APIClient(EndPoints.FRANCHISE_DUPLICATE_ENDPOINT, formData, RequestType.FORM_DATA);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

//                    responseBody = response.body();
//                    JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        Platform.runLater(() -> {
//                            tfFranchiseCreateFranchiseName.requestFocus(); // Set focus back to tfFranchiseCreateRPincode
                        });
                    } else {
                        String message = jsonObject.get("message").getAsString();

//                        if(!tfFranchiseCreateFranchiseCode.getText().isEmpty()){
                        AlertUtility.AlertDialogForError("WARNING", message, input -> {
                            if (input) {
                                tfFranchiseCreateFranchiseCode.requestFocus();
                            }
                        });
                        Platform.runLater(() -> {
                            tfFranchiseCreateFranchiseCode.requestFocus(); // Set focus back to tfFranchiseCreateRPincode
                        });
//                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError,message, in -> {
//                            tfFranchiseCreateFranchiseCode.requestFocus();
//                        });
//                        }

//                tfFranchiseCreatePincode.setText("");
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in duplicateFranchise()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in duplicateFranchise()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            logger.debug("Get duplicateFranchise Data End...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    //? Validate Code for Franchise Edit
    public void duplicateFranchiseEdit() {
        APIClient apiClient = null;
        try {
            logger.debug("Get duplicateFranchiseEdit Data Started...");
            String id = Globals.franchiseListDTO != null ? Globals.franchiseListDTO.getId() : "";
            Map<String, String> map = new HashMap<>();
            map.put("id", id);
            map.put("franchiseCode", String.valueOf(tfFranchiseCreateFranchiseCode.getText()));
            String formData = Globals.mapToStringforFormData(map);
//            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.VALIDATE_PINCODE_ENDPOINT);
            apiClient = new APIClient(EndPoints.FRANCHISE_DUPLICATE_UPDATE_ENDPOINT, formData, RequestType.FORM_DATA);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                    responseBody = response.body();
//                    JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        Platform.runLater(() -> {
                            tfFranchiseCreateFranchiseName.requestFocus(); // Set focus back to tfFranchiseCreateRPincode
                        });
                    } else {
                        String message = jsonObject.get("message").getAsString();

//                        if(!tfFranchiseCreateFranchiseCode.getText().isEmpty()){
                        AlertUtility.AlertDialogForError("WARNING", message, input -> {
                            if (input) {
                                tfFranchiseCreateFranchiseCode.requestFocus();
                            }
                        });
                        Platform.runLater(() -> {
                            tfFranchiseCreateFranchiseCode.requestFocus(); // Set focus back to tfFranchiseCreateRPincode
                        });
//                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError,message, in -> {
//                            tfFranchiseCreateFranchiseCode.requestFocus();
//                        });
//                        }

//                tfFranchiseCreatePincode.setText("");
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in duplicateFranchiseEdit()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in duplicateFranchiseEdit()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            logger.debug("Get duplicateFranchiseEdit Data End...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    //? Validate Pincode for Franchise Pincode
    public void validatePincode() {
        APIClient apiClient = null;
        try {
            logger.debug("Get ValidatePincode Data Started...");
            Map<String, String> map = new HashMap<>();
            map.put("pincode", String.valueOf(tfFranchiseCreatePincode.getText()));
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
                        Platform.runLater(() -> {
//                            cmbFranchiseCreateArea.requestFocus(); // Set focus back to tfFranchiseCreateRPincode
                        });
                    } else {
                        String message = jsonObject.get("message").getAsString();

                        if (!tfFranchiseCreatePincode.getText().isEmpty()) {
                            AlertUtility.AlertDialogForError("WARNING", message, input -> {
                                if (input) {
                                    tfFranchiseCreatePincode.requestFocus();
                                }
                            });
                            Platform.runLater(() -> {
                                tfFranchiseCreatePincode.requestFocus(); // Set focus back to tfFranchiseCreateRPincode
                            });
//                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
//                                tfFranchiseCreatePincode.requestFocus();
//                            });
                        }

//                tfFranchiseCreatePincode.setText("");
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in validatePincode()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in validatePincode()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            logger.debug("Get ValidatePincode Data End...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }
    //? Validate Pincode for Residential Pincode

    public void validatePincodeR() {
        APIClient apiClient = null;
        try {
            logger.debug("Get Validate Pincode Residential Data Started...");
            Map<String, String> map = new HashMap<>();
            map.put("pincode", String.valueOf(tfFranchiseCreateRPincode.getText()));
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
                        Platform.runLater(() -> {
//                            cmbFranchiseCreateRArea.requestFocus(); // Set focus back to tfFranchiseCreateRPincode
                        });
                    } else {
                        String message = jsonObject.get("message").getAsString();
                        if (!tfFranchiseCreateRPincode.getText().isEmpty()) {
                            AlertUtility.AlertDialogForError("WARNING", message, input -> {
                                if (input) {
                                    tfFranchiseCreateRPincode.requestFocus();
                                }
                            });
                            Platform.runLater(() -> {
                                tfFranchiseCreateRPincode.requestFocus(); // Set focus back to tfFranchiseCreateRPincode
                            });
//                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
//                                tfFranchiseCreateRPincode.requestFocus();
//                            });
                        }
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in validatePincodeR()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in validatePincodeR()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            logger.debug("Get Validate Pincode Residential Data End...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    //? Create Franchise API call
    public void createFranchise() {
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                //******************************************Validation******************************************************
                String franchiseCode = tfFranchiseCreateFranchiseCode.getText();
                String franchiseName = tfFranchiseCreateFranchiseName.getText();
                String franchiseApplicantName = tfFranchiseCreateApplicantName.getText();
                String franchiseDistrictHead = dHId.toString();
                String franchiseBusinessType = buinessTypeId.toString();
                String franchiseEducation = educationId.toString();
                String franchiseMobileNo = tfFranchiseCreateMobileNo.getText();
                String franchiseGender = String.valueOf(isGender);
                String franchiseDOB = dpFranchiseCreateDOB.getText();
                String franchiseAge = tfFranchiseCreateAge.getText();
                String franchiseAddress = tfFranchiseCreateAddress.getText();
                String franchisePincode = tfFranchiseCreatePincode.getText();
                String franchiseAddressR = tfFranchiseCreateRAddress.getText();
                String franchisePincodeR = tfFranchiseCreateRPincode.getText();
                if (!franchiseCode.isBlank() && !franchiseName.isBlank() && !franchiseApplicantName.isBlank() && !franchiseDistrictHead.isBlank() && !franchiseBusinessType.isBlank() && !franchiseMobileNo.isBlank() && !franchiseDOB.isBlank() && !franchiseGender.isBlank() && !franchiseAge.isBlank() && !franchiseEducation.isBlank() && !franchiseAddress.isBlank() && !franchisePincode.isBlank() && !franchiseAddressR.isBlank() && !franchisePincodeR.isBlank()) {
//            btnFranchiseCreateSubmit.setOnAction(actionEvent -> {

//                    rbFranchiseCreateMale.setToggleGroup(toggleGroup);
//                    rbFranchiseCreateFemale.setToggleGroup(toggleGroup);
                    String id = Globals.franchiseListDTO != null ? Globals.franchiseListDTO.getId() : "";
                    Map<String, String> map = new HashMap<>();
                    map.put("id", id);
                    map.put("franchiseCode", tfFranchiseCreateFranchiseCode.getText());
                    map.put("franchiseName", tfFranchiseCreateFranchiseName.getText());
                    map.put("applicationName", tfFranchiseCreateApplicantName.getText());
                    map.put("partnerName", tfFranchiseCreatePartnerName.getText());
                    map.put("districtId", dHId.toString());
                    map.put("rigionalId", regionalId);
                    map.put("zoneId", zoneId);
                    map.put("stateId", stateId1);
                    map.put("BusinessType", buinessTypeId.toString());
                    map.put("investAmt", String.valueOf(cbFranchiseCreateReadytoinvest.isSelected()));
                    map.put("isFunded", String.valueOf(isFunded));
                    map.put("fundAmt", tfFranchiseCreateFundingAmount.getText());
                    map.put("mobileNumber", tfFranchiseCreateMobileNo.getText());
                    if (!tfFranchiseCreateWhatsappNo.getText().isEmpty()) {
                        map.put("whatsappNumber", tfFranchiseCreateWhatsappNo.getText());
                    }
//                map.put("gender", String.valueOf(toggleGroup.getSelectedToggle().isSelected()));
                    map.put("gender", String.valueOf(isGender));
                    map.put("dob", Communicator.text_to_date.fromString(dpFranchiseCreateDOB.getText()).toString());
                    map.put("age", tfFranchiseCreateAge.getText());
                    if (!tfFranchiseCreateEmailId.getText().isEmpty()) {
                        map.put("email", tfFranchiseCreateEmailId.getText());
                    }
                    map.put("education", educationId.toString());
                    if (!tfFranchiseCreatePresentOccupation.getText().isEmpty()) {
                        map.put("presentOccupation", tfFranchiseCreatePresentOccupation.getText());
                    }
                    map.put("franchiseAddress", tfFranchiseCreateAddress.getText());
                    map.put("pincode", tfFranchiseCreatePincode.getText());
                    map.put("area", areaId.toString());
                    map.put("city", tfFranchiseCreateCity.getText());
                    map.put("state", stateCode);
                    map.put("countryId", countryId.toString());  // this is for both Registered and Residential Address
                    if (!tfFranchiseCreateLatitude.getText().isEmpty()) {
                        map.put("latitude", tfFranchiseCreateLatitude.getText());
                    }
                    if (!tfFranchiseCreateLongitude.getText().isEmpty()) {
                        map.put("longitude", tfFranchiseCreateLongitude.getText());
                    }
                    map.put("sameAsAddress", String.valueOf(cbFranchiseCreateSameasAddress.isSelected()));
                    map.put("residencialAddress", tfFranchiseCreateRAddress.getText());
                    map.put("residencialPincode", tfFranchiseCreateRPincode.getText());
                    map.put("residencialarea", areaIdR.toString());
                    map.put("residencialcity", tfFranchiseCreateRCity.getText());
                    map.put("residencialState", stateCodeR);
                    HttpResponse<String> response;
                    String formData = Globals.mapToStringforFormData(map);
                    APIClient apiClient = null;
                    try {
                        if (Globals.franchiseListDTO == null) {
//                    response = APIClient.postFormDataRequest(formData, EndPoints.FRANCHISE_CREATE_ENDPOINT);
                            apiClient = new APIClient(EndPoints.FRANCHISE_CREATE_ENDPOINT, formData, RequestType.FORM_DATA);
                            //? HIGHLIGHT
                            FranchiseListController.isNewFranchiseCreated = true; //? Set the flag for new creation
                        } else {
//                    response = APIClient.postFormDataRequest(formData, EndPoints.FRANCHISE_UPDATE_ENDPOINT);
                            apiClient = new APIClient(EndPoints.FRANCHISE_UPDATE_ENDPOINT, formData, RequestType.FORM_DATA);
                            //? HIGHLIGHT
                            FranchiseListController.editedFranchiseId = id; //? Set the ID for editing
                        }
                        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

//                        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                                message = jsonObject.get("message").getAsString();
                                int status = jsonObject.get("responseStatus").getAsInt();
                                if(status==409) {
                                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                                        tfFranchiseCreateFranchiseName.requestFocus();
                                    });
                                }
                                if (jsonObject.get("responseStatus").getAsInt() == 200) {
                                    //Successfully Alert Popup After Edit and Redirect to List Page
                                    AlertUtility.CustomCallback callback = (number) -> {
                                        if (Globals.franchiseListDTO != null) {
                                            if (number == 1) {
                                                GlobalController.getInstance().addTabStatic(FRANCHISE_LIST_SLUG, false);
                                            }
                                        }
                                    };

//                                    Stage stage2 = (Stage) spRootFranchiseCreatePane.getScene().getWindow();
                                    if (status == 200) {
//                                        AlertUtility.AlertSuccess(stage2, "Success", message, callback);
                                        AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                                            if (Globals.franchiseListDTO != null) {
                                                GlobalController.getInstance().addTabStatic(FRANCHISE_LIST_SLUG, false);
                                            }

                                        });
                                    } else {
//                                        AlertUtility.AlertError(stage2, AlertUtility.alertTypeError, message, callback);
                                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                                        });
                                    }
                                    //Successfully Alert Popup After Create and Redirect to Ledger Create with Data
                                    if (Globals.franchiseListDTO == null) {
//                                        Stage stage1 = (Stage) spRootFranchiseCreatePane.getScene().getWindow();
                                        if (status == 200) {
//                                            AlertUtility.AlertSuccess(stage1, "Success", message, callback);
                                            AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                                            });
                                        } else {
//                                            AlertUtility.AlertError(stage1, AlertUtility.alertTypeError, message, callback);
                                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                                            });

                                        }
                                        setDataToFranchiseDTO();
                                    }
                                    clearFieldAfterCreateOrUpdate();
                                }
                            }
                        });
                        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                logger.error("Network API cancelled in createFranchise()" + workerStateEvent.getSource().getValue().toString());

                            }
                        });
                        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                logger.error("Network API failed in createFranchise()" + workerStateEvent.getSource().getValue().toString());

                            }
                        });
                        apiClient.start();
                        logger.debug("Get Product Data End...");
                    } catch (Exception e) {
                    } finally {
                        apiClient = null;
                    }
//            });

                }
            } else {
            }
        };
        if (Globals.franchiseListDTO == null) {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnSubmit + " " + tfFranchiseCreateFranchiseName.getText(), callback);
        }else {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnUpdate + " " + tfFranchiseCreateFranchiseName.getText(), callback);
        }
    }

    public void clearFieldAfterCreateOrUpdate() {

        tfFranchiseCreateFranchiseCode.setText("");
        tfFranchiseCreateFranchiseName.setText("");
        tfFranchiseCreateApplicantName.setText("");
        tfFranchiseCreatePartnerName.setText("");
        cmbFranchiseCreateDistrictHead.getSelectionModel().clearSelection();
        tfFranchiseCreateRegionalHead.setText("");
        tfFranchiseCreateZoneHead.setText("");
        tfFranchiseCreateStateName.setText("");
        cmbFranchiseCreateBusinessType.getSelectionModel().clearSelection();
        cbFranchiseCreateReadytoinvest.setSelected(false);
        tgFranchiseIsFunding.switchOnProperty().set(false);
        tfFranchiseCreateFundingAmount.setText("");
        tfFranchiseCreateMobileNo.setText("");
        tfFranchiseCreateWhatsappNo.setText("");
        cmbFranchiseCreateGender.getSelectionModel().clearSelection();
//        toggleGroup.selectToggle(null);
        dpFranchiseCreateDOB.setText("");
        tfFranchiseCreateAge.setText("");
        tfFranchiseCreateEmailId.setText("");
        cmbFranchiseCreateEducation.getSelectionModel().clearSelection();
        tfFranchiseCreatePresentOccupation.setText("");
        tfFranchiseCreateAddress.setText("");
        tfFranchiseCreatePincode.setText("");
        cmbFranchiseCreateArea.getSelectionModel().clearSelection();
        tfFranchiseCreateCity.setText("");
        tfFranchiseCreateState.setText("");
        cmbFranchiseCreateCountry.getSelectionModel().clearSelection();
        tfFranchiseCreateLatitude.setText("");
        tfFranchiseCreateLongitude.setText("");
        cbFranchiseCreateSameasAddress.setSelected(false);
        tfFranchiseCreateRAddress.setText("");
        tfFranchiseCreateRPincode.setText("");
        cmbFranchiseCreateRArea.getSelectionModel().clearSelection();
        tfFranchiseCreateRCity.setText("");
        tfFranchiseCreateRState.setText("");
        cmbFranchiseCreateRCountry.getSelectionModel().clearSelection();

    }

    // to transfer data to ledger cerate
    public void setDataToFranchiseDTO() {
        String franchiseCode = tfFranchiseCreateFranchiseCode.getText();
        String franchiseName = tfFranchiseCreateFranchiseName.getText();
        String franchiseApplicantName = tfFranchiseCreateApplicantName.getText();
        String franchisePartnerName = tfFranchiseCreatePartnerName.getText();
        String franhciseDistrictId = dHId.toString();
        String franchiseRegionalId = regionalId;
        String franchiseZoneId = zoneId;
        String franchiseStateId1 = stateId1;
        String franchiseBusinessTypeId = buinessTypeId.toString();
        String franchiseReadyToInvest = String.valueOf(cbFranchiseCreateReadytoinvest.isSelected());
        String franchiseIsFunded = String.valueOf(isFunded);
        String franchiseFundingAmount = tfFranchiseCreateFundingAmount.getText();
        String franchiseMobileNo = tfFranchiseCreateMobileNo.getText();
        String franchiseWhatsappNo = tfFranchiseCreateWhatsappNo.getText();
        String franchiseGender = String.valueOf(isGender);
        String franchiseDOB = Communicator.text_to_date.fromString(dpFranchiseCreateDOB.getText()).toString();
        String franchiseAge = tfFranchiseCreateAge.getText();
        String franchiseEmailId = tfFranchiseCreateEmailId.getText();
        String franchiseEductionId = educationId.toString();
        String franchisePresentOccupation = tfFranchiseCreatePresentOccupation.getText();
        String franchiseAddress = tfFranchiseCreateAddress.getText();
        String franchisePincode = tfFranchiseCreatePincode.getText();
        String franchiseArea = areaId.toString();
        String franchiseCity = tfFranchiseCreateCity.getText();
        String franchiseStateCode = stateCode;
        String franchiseCountry = countryId.toString();  // this is for both Registered and Residential Address
        String franchiseLatitude = tfFranchiseCreateLatitude.getText();
        String franchiseLongitude = tfFranchiseCreateLongitude.getText();
        String franchiseSameAsAddress = String.valueOf(cbFranchiseCreateSameasAddress.isSelected());
        String franchiseAddressR = tfFranchiseCreateRAddress.getText();
        String franchisePincodeR = tfFranchiseCreateRPincode.getText();
        String franchiseAreaR = areaIdR.toString();
        String franchiseCityR = tfFranchiseCreateRCity.getText();
        String franchiseStateCodeR = stateCodeR;
        FranchiseDTO franchiseData = new FranchiseDTO();
        franchiseData.setFranchiseCode(franchiseCode);
        franchiseData.setFranchiseName(franchiseName);
        franchiseData.setApplicantName(franchiseApplicantName);
        franchiseData.setPartnerName(franchisePartnerName);
        franchiseData.setDistrictHeadId(franhciseDistrictId);
        franchiseData.setRegionalHeadId(franchiseRegionalId);
        franchiseData.setZonalHeadId(franchiseZoneId);
        franchiseData.setStateHeadId(franchiseStateId1);
        franchiseData.setBusinessType(franchiseBusinessTypeId);
        franchiseData.setInvestId(franchiseReadyToInvest);
//        franchiseData.setFranchiseName(franchiseIsFunded);
//        franchiseData.setFranchiseName(franchiseFundingAmount);
        franchiseData.setMobileNo(franchiseMobileNo);
        franchiseData.setWhatsappNo(franchiseWhatsappNo);
        franchiseData.setGender(franchiseGender);
        franchiseData.setBirthDate(franchiseDOB);
        franchiseData.setAge(franchiseAge);
        franchiseData.setEmailId(franchiseEmailId);
        franchiseData.setEducation(franchiseEductionId);
        franchiseData.setPresentOccupation(franchisePresentOccupation);
        franchiseData.setFranchiseAddress(franchiseAddress);
        franchiseData.setFranchisePincode(franchisePincode);
        franchiseData.setFranchiseAreaId(franchiseArea);
        franchiseData.setFranchiseCity(franchiseCity);
        franchiseData.setStateCode(franchiseStateCode);
        franchiseData.setCountryId(franchiseCountry);
//        franchiseData.setL(franchiseLatitude);
//        franchiseData.setFranchiseName(franchiseLongitude);
        franchiseData.setSameAsAddress(franchiseSameAsAddress);
        franchiseData.setResidentialAddress(franchiseAddressR);
        franchiseData.setResidentialPincode(franchisePincodeR);
        franchiseData.setResidentialAreaId(franchiseAreaR);
        franchiseData.setResidentialcity(franchiseCityR);
        franchiseData.setResidentialStateCode(franchiseStateCodeR);
        GlobalController.getInstance().addTabStatic("ledger-create", false, franchiseData);
//        LedfranchiseData = {FranchiseDTO@9010} gerCreateSDController ledgerCreateSDController=new LedgerCreateSDController();
//        LedgerCreateController ledgerCreateController=new LedgerCreateController();
//        ledgerCreateSDController.getFranchiseData(franchiseData);
//        ledgerCreateController.setFrDataToLedger(franchiseData);
    }

    //? set the data of Frachise by Id
    public void getEditDataById() {
        APIClient apiClient = null;
        logger.debug("Get Edit by id Data Started...");
        Map<String, String> map = new HashMap<>();
        map.put("id", Globals.franchiseListDTO.getId());
        String formData = Globals.mapToStringforFormData(map);
//        HttpResponse<String> response = APIClient.postFormDataRequest(formData,EndPoints.FRANCHISE_GET_DATA_BY_ID_ENDPOINT);
        apiClient = new APIClient(EndPoints.FRANCHISE_GET_DATA_BY_ID_ENDPOINT, formData, RequestType.FORM_DATA);

        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                JsonObject jsonObject = new JsonObject().get("responseObject").getAsJsonObject();
                JsonObject jObject = jsonObject.getAsJsonObject("responseObject").getAsJsonObject();
                if (jsonObject.get("responseStatus").getAsInt() == 200) {
                    tfFranchiseCreateFranchiseCode.setText(jObject.get("franchiseCode").getAsString());
                    tfFranchiseCreateFranchiseName.setText(jObject.get("franchiseName").getAsString());
                    tfFranchiseCreateApplicantName.setText(jObject.get("applicationName").getAsString());
                    tfFranchiseCreatePartnerName.setText(jObject.get("partnerName").getAsString());
                    //? Set District Head Data and Regional Head,Zone Head, State Head
                    JsonObject jsonArray = jObject.get("district").getAsJsonObject();
                    String dHId1 = jsonArray.get("districtId").getAsString();
                    dHId = jsonArray.get("districtId").getAsLong();

                    getDistrictHeadsData();
//            FranchiseDistrictHeadDTO selectedDistrictHead= null;
//            for (Object obj : cmbFranchiseCreateDistrictHead.getItems()) {
//                FranchiseDistrictHeadDTO districtHeadDTO = (FranchiseDistrictHeadDTO) obj;
//
//                if (districtHeadDTO.getId().equals(dHId1)) {
//
//                    selectedDistrictHead = districtHeadDTO;
//                    break;
//                }
//            }
//
//            if (selectedDistrictHead != null) {
//                cmbFranchiseCreateDistrictHead.getSelectionModel().select(selectedDistrictHead);
//            }

                    setComboBoxValueId(cmbFranchiseCreateDistrictHead, Long.parseLong(dHId1));


                    //? Area list API data calls(Onkar Gelchinav)
//                    JsonObject jsonArray1 = jObject.get("stateHead").getAsJsonObject();
//                    String sHID = jsonArray1.get("stateId").getAsString();
//                    stateId1 = jsonArray1.get("stateId").getAsString();
//                    String stateName = jsonArray1.get("stateName").getAsString();
//                    tfFranchiseCreateStateName.setText(stateName);
//                    JsonObject jsonArray2 = jObject.get("zone").getAsJsonObject();
//                    String zHID = jsonArray2.get("zoneId").getAsString();
//                    zoneId = jsonArray2.get("zoneId").getAsString();
//                    String zoneName = jsonArray2.get("zoneName").getAsString();
//                    tfFranchiseCreateZoneHead.setText(zoneName);
//                    JsonObject jsonArray3 = jObject.get("regional").getAsJsonObject();
//                    String rHID = jsonArray3.get("regionalId").getAsString();
//                    regionalId = jsonArray3.get("regionalId").getAsString();
//                    String regionalHead = jsonArray3.get("regionalName").getAsString();
//                    tfFranchiseCreateRegionalHead.setText(regionalHead);
                    //? this is to set the static data in Combo Box through ID of Business Type

                    String businessTypeId = jObject.get("businessType").getAsString();
                    buinessTypeId = jObject.get("businessType").getAsLong();
                    FranchiseBusinessTypeDTO selectedBusinessType = null;
                    for (Object obj : cmbFranchiseCreateBusinessType.getItems()) {
                        FranchiseBusinessTypeDTO businessType = (FranchiseBusinessTypeDTO) obj;
                        if (businessType.getId().equals(businessTypeId)) {
                            selectedBusinessType = businessType;
                            break;
                        }
                    }
                    if (selectedBusinessType != null) {
                        cmbFranchiseCreateBusinessType.getSelectionModel().select(selectedBusinessType);
                    } else {
                        //? use logger for alert messages
                        logger.error("Invalid Business Type ");
                    }
                    boolean investAmt = jObject.get("investAmt").getAsBoolean();
                    cbFranchiseCreateReadytoinvest.setSelected(investAmt);
                    isFunded = jObject.get("isFunded").getAsBoolean();
//            tgFranchiseIsFunding.setSelected(isFunded);//? toggle
                    if (isFunded) {
//                indicatorCircle.setTranslateX(40);//? toggle
//                tgFranchiseIsFunding.setSelected(true);//? toggle
                        tgFranchiseIsFunding.switchOnProperty().set(true);
                        tfFranchiseCreateFundingAmount.setVisible(true);
                    }
                    tfFranchiseCreateFundingAmount.setText(jObject.get("fundAmt").getAsString());
                    tfFranchiseCreateMobileNo.setText(jObject.get("mobileNumber").getAsString());
                    tfFranchiseCreateWhatsappNo.setText(jObject.get("whatsappNumber").getAsString());
                    //? this is to set the Gender Selected as Male if true and Female if false
                    String gender = jObject.get("gender").getAsString();
//                    CommonValidationsUtils.radioToggleValidation(gender, toggleValidation);
                    if (gender.equals("true")) {
                        cmbFranchiseCreateGender.getSelectionModel().select(0);
                        isGender = "true";
                    } else {
                        cmbFranchiseCreateGender.getSelectionModel().select(1);
                        isGender = "false";
                    }
                    LocalDate dob = LocalDate.parse(jObject.get("dob").getAsString());
                    dpFranchiseCreateDOB.setText(dob.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//            dpFranchiseCreateDOB.setText(String.valueOf(dob));
                    tfFranchiseCreateAge.setText(jObject.get("age").getAsString());
                    tfFranchiseCreateEmailId.setText(jObject.get("email").getAsString());
                    //? this is to set the static data in Combo Box through ID of Education
                    String educationId1 = jObject.get("education").getAsString();
                    educationId = jObject.get("education").getAsLong();
                    FranchiseEducationDTO selectedEducation = null;
                    for (Object obj : cmbFranchiseCreateEducation.getItems()) {
                        FranchiseEducationDTO education = (FranchiseEducationDTO) obj;
                        if (education.getId().equals(educationId1)) {
                            selectedEducation = education;
                            break;
                        }
                    }
                    if (selectedEducation != null) {
                        cmbFranchiseCreateEducation.getSelectionModel().select(selectedEducation);
                    }

                    tfFranchiseCreatePresentOccupation.setText(jObject.get("presentoccupation").getAsString());
                    tfFranchiseCreateAddress.setText(jObject.get("address").getAsString());
                    tfFranchiseCreatePincode.setText(jObject.get("pincode").getAsString());
                    //? To get the Registered Area List ID
                    JsonArray area_list = jObject.get("area_list").getAsJsonArray();
                    ObservableList<FranchiseAreaDTO> list = FXCollections.observableArrayList();
                    for (JsonElement mElement : area_list) {
                        JsonObject mObject = mElement.getAsJsonObject();
                        String area = mObject.get("area_name").getAsString();
                        String district = "";
                        String state = "";
                        String stateCode = "";
                        String pincode = "";
                        String id = mObject.get("area_id").getAsString();
                        list.add(new FranchiseAreaDTO(area, id, pincode, district, state, stateCode));
                        AreaMapList.put(id, area);
                    }
                    String cmparea = jObject.get("areaId") != null ? jObject.get("areaId").getAsString() : "";
                    pinText = jObject.get("pincode").getAsString();
                    cmbFranchiseCreateArea.setItems(list);
                    cmbFranchiseCreateArea.setConverter(new StringConverter<FranchiseAreaDTO>() {
                        @Override
                        public String toString(FranchiseAreaDTO o) {
                            return o.getArea();
                        }

                        @Override
                        public FranchiseAreaDTO fromString(String s) {
                            return null;
                        }
                    });

                    list.stream().filter((v) -> v.getId().equalsIgnoreCase(cmparea)).findAny().ifPresent((p) -> {
                        cmbFranchiseCreateArea.setValue(p);
                    });
                    areaId = jObject.get("areaId").getAsLong();
                    //? Set Area data of Registered by Id
//                    FranchiseAreaDTO selectedAreaHead = null;
//                    for (Object obj : cmbFranchiseCreateArea.getItems()) {
//                        FranchiseAreaDTO areaHeadDTO = (FranchiseAreaDTO) obj;
//                        if (areaHeadDTO.getId().equals(jObject.get("areaId").getAsString())) {
//                            selectedAreaHead = areaHeadDTO;
//                            break;
//                        }
//                    }
//                    if (selectedAreaHead != null) {
//                        cmbFranchiseCreateArea.getSelectionModel().select(selectedAreaHead);
//                    }
                    tfFranchiseCreateCity.setText(jObject.get("city").getAsString());
                    tfFranchiseCreateState.setText(jObject.get("state").getAsString());
                    //? Set Country Data
                    FranchiseCountryDTO selectedCountry = null;
                    for (Object obj : cmbFranchiseCreateCountry.getItems()) {
                        FranchiseCountryDTO countryDto = (FranchiseCountryDTO) obj;
                        if (countryDto.getId().equals(jObject.get("countryId").getAsString())) {
                            selectedCountry = countryDto;
                            break;
                        }
                    }
                    if (selectedCountry != null) {
                        cmbFranchiseCreateCountry.getSelectionModel().select(selectedCountry);
                    }
                    tfFranchiseCreateLatitude.setText(jObject.get("latitude").getAsString());
                    tfFranchiseCreateLongitude.setText(jObject.get("longitude").getAsString());
                    sameAddress = jObject.get("sameAsAddress").getAsBoolean();
                    cbFranchiseCreateSameasAddress.setSelected(sameAddress);

                    tfFranchiseCreateRAddress.setText(jObject.get("residencialAddress").getAsString());
                    tfFranchiseCreateRPincode.setText(jObject.get("residencialPincode").getAsString());
                    if (jObject.get("sameAsAddress").getAsBoolean()) {
                        cmbFranchiseCreateRArea.setItems(list);
                        cmbFranchiseCreateRArea.setConverter(new StringConverter<FranchiseAreaDTO>() {
                            @Override
                            public String toString(FranchiseAreaDTO o) {
                                return o.getArea();
                            }

                            @Override
                            public FranchiseAreaDTO fromString(String s) {
                                return null;
                            }
                        });

                        areaIdR = Long.valueOf(jObject.get("residencialAreaId").getAsString());
                        //? Set Area data of Registered by Id
                        FranchiseAreaDTO selectedRAreaHead = null;
                        for (Object obj : cmbFranchiseCreateRArea.getItems()) {
                            FranchiseAreaDTO areaHeadDTO = (FranchiseAreaDTO) obj;
                            if (areaHeadDTO.getId().equals(jObject.get("residencialAreaId").getAsString())) {
                                selectedRAreaHead = areaHeadDTO;
                                break;
                            }
                        }
                        cmbFranchiseCreateRArea.getSelectionModel().select(selectedRAreaHead);
                        tfFranchiseCreateRAddress.setDisable(true);
                        tfFranchiseCreateRPincode.setDisable(true);
                        cmbFranchiseCreateRArea.setDisable(true);
                        tfFranchiseCreateRCity.setDisable(true);
                        tfFranchiseCreateRState.setDisable(true);
                        cmbFranchiseCreateRCountry.setDisable(true);
                    } else {
                        JsonArray residencial_area_list = jObject.get("residencial_area_list").getAsJsonArray();
                        ObservableList<FranchiseAreaDTO> list1 = FXCollections.observableArrayList();
                        for (JsonElement mElement : residencial_area_list) {
                            JsonObject mObject = mElement.getAsJsonObject();
                            String area = mObject.get("corporate_area_name").getAsString();
                            String district = "";
                            String state = "";
                            String stateCode = "";
                            String pincode = "";
                            String id = mObject.get("residencial_area_id").getAsString();
                            list1.add(new FranchiseAreaDTO(area, id, pincode, district, state, stateCode));
                            AreaMapList.put(id, area);
                        }
                        String cmpareaR = jObject.get("residencialAreaId") != null ? jObject.get("residencialAreaId").getAsString() : "";
                        corppinText = jObject.get("residencialPincode").getAsString();
                        cmbFranchiseCreateRArea.setItems(list1);
                        cmbFranchiseCreateRArea.setConverter(new StringConverter<FranchiseAreaDTO>() {
                            @Override
                            public String toString(FranchiseAreaDTO o) {
                                return o.getArea();
                            }

                            @Override
                            public FranchiseAreaDTO fromString(String s) {
                                return null;
                            }
                        });
                        list1.stream().filter((v) -> v.getId().equalsIgnoreCase(cmpareaR)).findAny().ifPresent((p) -> {
                            cmbFranchiseCreateRArea.setValue(p);
                        });

                        areaIdR = Long.valueOf(jObject.get("residencialAreaId").getAsString());
                        //? Set Area data of Registered by Id
//                        FranchiseAreaDTO selectedRAreaHead = null;
//                        for (Object obj : cmbFranchiseCreateRArea.getItems()) {
//                            FranchiseAreaDTO areaHeadDTO = (FranchiseAreaDTO) obj;
//                            if (areaHeadDTO.getId().equals(jObject.get("residencialAreaId").getAsString())) {
//                                selectedRAreaHead = areaHeadDTO;
//                                break;
//                            }
//                        }
//                        if (selectedRAreaHead != null) {
//                            cmbFranchiseCreateRArea.getSelectionModel().select(selectedRAreaHead);
//                        }

                    }
                    //? Set Area data of Residential

                    tfFranchiseCreateRCity.setText(jObject.get("residencialcity").getAsString());
                    tfFranchiseCreateRState.setText(jObject.get("residencialStateName").getAsString());

                    stateCodeR = jObject.get("residencialstate").getAsString();
                    //? Set Residential Country Data
                    FranchiseCountryDTO selectedRCountry = null;
                    for (Object obj : cmbFranchiseCreateRCountry.getItems()) {
                        FranchiseCountryDTO countryRDto = (FranchiseCountryDTO) obj;
                        if (countryRDto.getId().equals(jObject.get("countryId").getAsString())) {
                            selectedRCountry = countryRDto;
                            break;
                        }
                    }
                    if (selectedRCountry != null) {
                        cmbFranchiseCreateRCountry.getSelectionModel().select(selectedRCountry);
                    }

                } else {
                    //? use logger for alert messages
                    logger.error("Failed to Load Data");
                }
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                logger.error("Network API cancelled in getEditDataById()" + workerStateEvent.getSource().getValue().toString());

            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                logger.error("Network API failed in getEditDataById()" + workerStateEvent.getSource().getValue().toString());
            }
        });
        apiClient.start();
        logger.debug("Get Edit data by id Data End...");

    }

    public void setComboBoxValueId(ComboBox comboBox, Long id) {
        FranchiseDistrictHeadDTO selecteditem = null;
        for (Object obj : comboBox.getItems()) {
            FranchiseDistrictHeadDTO commonDTO = (FranchiseDistrictHeadDTO) obj;
            Long comId = Long.valueOf(commonDTO.getId());
            if (comId != null) {
                if (comId == id) {
                    selecteditem = commonDTO;
                    break;
                }
            }
        }
        if (selecteditem != null) {
            comboBox.getSelectionModel().select(selecteditem);
        }
    }

}
