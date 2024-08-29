package com.opethic.genivis.controller.master.ledger.create;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.commons.SwitchButton;
import com.opethic.genivis.controller.master.ledger.common.LedgerCommonController;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.dto.master.ledger.*;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.models.master.ledger.*;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class LedgerCreateSCController implements Initializable {
    @FXML
    private SwitchButton chkscIsBank;

    @FXML
    private SwitchButton chkscIsDepartment;

    @FXML
    private HBox ledgerscMainDivider, scFirstRow, scSecondRow, scThirdRow, infoFirstRow, infoSecondRow, infoThirdRow;
    @FXML
    private VBox ledgerscMainDividerFirst, ledgerscMainDividerSecond, leftFirstRow, leftSecondRow, leftThirdRow, rightFirstRow, rightSecondRow, rightThirdRow;

    @FXML
    private SwitchButton chkscIsGST;

    @FXML
    private SwitchButton chkscIsLicense;

    @FXML
    private SwitchButton chkscIsShippingDetails;

    @FXML
    private SwitchButton chkscLedgerCreateIsCredit;

    @FXML
    private ComboBox<StateOption> cmbscState;

    @FXML
    private Label lblscBusinessNature;

    @FXML
    private Label lblscLedgerCreateTrade;

    @FXML
    private RadioButton rdscDistributor;

    @FXML
    private RadioButton rdscManufacturer;

    @FXML
    private RadioButton rdscReatailer;
//    private final ToggleGroup tgTrade = new ToggleGroup();

    @FXML
    private BorderPane stpanesc;

    @FXML
    private TextField tfscLedgerCreateAdress;

    @FXML
    private TextField tfscLedgerCreateBusinessNature;

    @FXML
    private TextField tfscLedgerCreateCreditBills;

    @FXML
    private TextField tfscLedgerCreateCreditDays;

    @FXML
    private TextField tfscLedgerCreateCreditValue;

    @FXML
    private TextField tfscLedgerCreateEmail;

    @FXML
    private TextField tfscLedgerCreatePhone;

    @FXML
    private TextField tfscLedgerCreatePin;
    @FXML
    private VBox vboxchkscLedgerCreateIsCredit, vboxchkscIsGST, vboxchkscIsLicense, vboxchkscIsDepartment, vboxchkscIsShippingDetails, vboxchkscIsBank;

    //    @FXML
//    private DatePicker tfscLedgerCreateRegDate;
    @FXML
    private TextField tfscLedgerCreateRegDate;

    @FXML
    private TextField tfscLedgerCreateRegNo;

    @FXML
    private TextField tfscLedgerCreateRegisterdName;

    @FXML
    private TextField tfscLedgerCreateWhatsapp;


    @FXML
    private TableView<GstListDTO> tblscGstInfo;

    @FXML
    private TableColumn<GstListDTO, Void> tcscGstAction;

    @FXML
    private TableColumn<?, ?> tcscGstIn;

    @FXML
    private TableColumn<?, ?> tcscGstRegDate;

    @FXML
    private TableColumn<?, ?> tcscGstType;

    @FXML
    private TableColumn<?, ?> tcscPan;

    @FXML
    private TableView<BankDetailsDTO> tblscBankInfo;
    @FXML
    private TableColumn<BankDetailsDTO, Void> tcscBankAct;
    @FXML
    private TableColumn<?, ?> tcscBankAccNo;
    @FXML
    private TableColumn<?, ?> tcscBankBranch;
    @FXML
    private TableColumn<?, ?> tcscBankIFSCCode;
    @FXML
    private TableColumn<?, ?> tcscBankName;

    @FXML
    private TableView<DeptInfoDTO> tblscLedgerCreateDept;

    @FXML
    private TableColumn<?, ?> tcscDeptName;

    @FXML
    private TableColumn<DeptInfoDTO, Void> tcscDeptPersonAct;

    @FXML
    private TableColumn<?, ?> tcscDeptPersonEmail;

    @FXML
    private TableColumn<?, ?> tcscDeptPersonName;

    @FXML
    private TableColumn<?, ?> tcscDeptPersonPhone;

    @FXML
    private TableView<ShippingDTO> tblscLedgerCreateShipping;
    @FXML
    private TableColumn<ShippingDTO, Void> tcscShippingAct;
    @FXML
    private TableColumn<?, ?> tcscShippingAddress;
    @FXML
    private TableColumn<?, ?> tcscShippingState;

    @FXML
    private TableColumn<LicenseDetailDTO, Void> tcscLicenseAct;

    @FXML
    private TableColumn<?, ?> tcscLicenseExp;
    @FXML
    private TableColumn<?, ?> tcscLicenseNo;
    @FXML
    private TableColumn<?, ?> tcscLicenseType;
    @FXML
    private TableView<LicenseDetailDTO> tblscLedgerCreateLicenses;
    @FXML
    private ComboBox<LicenseType> cmbscLedgerCreateLicenseType;
    @FXML
    private TextField tfscLedgerCreateDeptPersonPhone, tfscLedgerCreateDeptPersonEmail, tfscLedgerCreateDeptPersonName, tfscLedgerCreateDeptName;
    @FXML
    private Button btnScLedgerCreateSubmit, btnScLedgerCreateCancel, btnGstPlus, btnBankPlus, btnDeptPlus, btnShippingPlus, btnLicensePlus;
    @FXML
    private ComboBox<StateOption> cmbscLedgerCreateShippingState;
    @FXML
    private DatePicker tfscLedgerCreateLicenseExp;
    @FXML
    private TextField tfscLedgerCreateLicenseNo;
    @FXML
    private ComboBox<CommonOption> cmbscLedgerCreateApplicableFrom;
    private ObservableList<GstListDTO> tbllstGst = FXCollections.observableArrayList();
    private List<GstList> lstGst = new ArrayList<>();
    private ObservableList<GSTType> lstGstType = FXCollections.observableArrayList();
    private ObservableList<BankDetailsDTO> tbllstBankInfo = FXCollections.observableArrayList();
    private List<BankDetailList> lstBankInfo = new ArrayList<>();
    private int edtIdx = -1, bankEdtIdx = -1;
    private ObservableList<DeptInfoDTO> tbllstDeptInfo = FXCollections.observableArrayList();
    private List<DeptInfo> lstDeptInfo = new ArrayList<>();
    private int deptEdtIdx = -1;
    @FXML
    private TextField tfscLedgerCreateShippingAddress;
    private ObservableList<ShippingDTO> tbllstShippingInfo = FXCollections.observableArrayList();
    private List<ShippingInfo> lstShippingInfo = new ArrayList<>();
    private int shippingEdtIdx = -1;
    private ObservableList<LicenseDetailDTO> tbllstLicenseInfo = FXCollections.observableArrayList();
    private List<LicenseInfo> lstLicenseInfo = new ArrayList<>();
    private int licenseEdtIdx = -1;
    @FXML
    private Label lblLedgerEditPANNo, lblBank, lblGST, lblLicense, lblDepartment, lblShipping;
    @FXML
    private HBox tfLedgerEditPANNo;
    @FXML
    private TextField tfscLedgerEditPANNo;
    //    @FXML
//    private Label requiredID;
    @FXML
    private Text requiredID;
    @FXML
    private ComboBox<String> cbTrade;
    String selectedTrade = "";
    private String pinText = "";
    String lblStyleCSS = "-fx-font-weight: bold; -fx-text-fill: #00a0f5;";
    Boolean dateVal = false;

    private JsonObject jsonObject = null;

    private ObservableList<String> items = FXCollections.observableArrayList(
            "Apple", "Banana", "Cherry", "Date", "Elderberry", "Fig", "Grape", "Honeydew"
    );

    public static String main_ledger_name = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("main_ledger_name----> <<<<<<<<<----- " + main_ledger_name);
        ResponsiveWiseCssPicker();

        //For Divide the two info tables same width
        leftFirstRow.prefWidthProperty().bind(infoFirstRow.widthProperty().multiply(0.5));
        rightFirstRow.prefWidthProperty().bind(infoFirstRow.widthProperty().multiply(0.5));

        leftSecondRow.prefWidthProperty().bind(infoSecondRow.widthProperty().multiply(0.5));
        rightSecondRow.prefWidthProperty().bind(infoSecondRow.widthProperty().multiply(0.5));

        leftThirdRow.prefWidthProperty().bind(infoThirdRow.widthProperty().multiply(0.5));
        rightThirdRow.prefWidthProperty().bind(infoThirdRow.widthProperty().multiply(0.5));
        // End

        DateValidator.applyDateFormat(tfscLedgerCreateRegDate);
        CommonFunctionalUtils.restrictDateFormat(tfscLedgerCreateRegDate);
//        Platform.runLater(() -> {
//            tfscLedgerCreateRegisterdName.requestFocus();
//        });
        initAllSwitchButtons();
        initStateCmb();
        initGSTTable();
        initBankInfoTable();
        initDeptInfoTable();
        initShippingInfoTable();
        initLicenseInfoTable();
        initApplicableFromCmb();
//        initTradeRadioButtonGroup();
        initBtnSubmitAndCancel();
        setupPincodeValidation();
        setupMobileNumberValidation();
        setupPanNoValidation();

        cbTrade.getItems().addAll(Globals.getAllTrades());
        cbTrade.setPromptText("Select Trade");
        cbTrade.setOnAction(this::handleTradeComboBoxAction);
        // open trade dropdown on Space
        cbTrade.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cbTrade.isShowing()) {
                cbTrade.show();
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                tfscLedgerCreateBusinessNature.requestFocus();
            }
            event.consume(); // Consume the event to prevent other actions
        });

        lblBank.setStyle(lblStyleCSS);
        lblGST.setStyle(lblStyleCSS);
        lblLicense.setStyle(lblStyleCSS);
        lblDepartment.setStyle(lblStyleCSS);
        lblShipping.setStyle(lblStyleCSS);

        tfscLedgerCreatePhone.textProperty().addListener((observable, oldValue, newValue) -> {
            //TODO: Update the text property of whatsapp when the value changes in mobileNu,
            tfscLedgerCreateWhatsapp.setText(newValue);
        });

        // open Filter dropdown on Space
        cmbscLedgerCreateApplicableFrom.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cmbscLedgerCreateApplicableFrom.isShowing()) {
                cmbscLedgerCreateApplicableFrom.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });

        regDateValidation();
//        tfscLedgerCreateRegDate.setOnKeyPressed(event -> {
//            System.out.println("event-> " + event);
//            if (event.getCode() == KeyCode.ENTER){
//                System.out.println("Register Date-------> " + tfscLedgerCreateRegDate.getText().toString());
//            }
//        });

//        tfscLedgerCreateRegisterdName.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfscLedgerCreateAdress.setOnKeyPressed(this::handleEnterKeyPressed);
//        cmbscState.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfscLedgerCreatePin.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfscLedgerCreatePhone.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfscLedgerCreateWhatsapp.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfscLedgerCreateEmail.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfscLedgerCreateRegNo.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfscLedgerCreateRegDate.setOnKeyPressed(this::handleEnterKeyPressed);
//        vboxchkscLedgerCreateIsCredit.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfscLedgerCreateCreditDays.setOnKeyPressed(this::handleEnterKeyPressed);
//        cmbscLedgerCreateApplicableFrom.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfscLedgerCreateCreditBills.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfscLedgerCreateCreditValue.setOnKeyPressed(this::handleEnterKeyPressed);
//        rdscReatailer.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfscLedgerCreateBusinessNature.setOnKeyPressed(this::handleEnterKeyPressed);
//        vboxchkscIsGST.setOnKeyPressed(this::handleEnterKeyPressed);

        CommonFunctionalUtils.restrictTextFieldToDigits(tfscLedgerCreatePin, 6);
        CommonFunctionalUtils.restrictTextField(tfscLedgerCreatePin);

        // Add listener to only numbers input
        addNumericListener(tfscLedgerCreateCreditDays);
        addNumericListener(tfscLedgerCreateCreditBills);
        addNumericListener(tfscLedgerCreateCreditValue);

        // Add listener to convert text to uppercase
        tfscLedgerEditPANNo.textProperty().addListener((observable, oldValue, newValue) -> {
            tfscLedgerEditPANNo.setText(newValue.toUpperCase());
        });

        //code for pin code validation
        addPincodeValidationListener(tfscLedgerCreatePin);
        //code for email validation
        addEmailValidationListener(tfscLedgerEditPANNo);
        // pan no. validation
        addPANNoValidationListener(tfscLedgerEditPANNo);
        // phone no. validation
        addPhoneValidationListener(tfscLedgerCreatePhone);
        // whatsapp no. validation
        addWhatsappValidationListener(tfscLedgerCreatePhone);

        initialEnterMethod();
//        tfFocused(tfscLedgerCreatePhone);
        tfFocused(tfscLedgerCreateWhatsapp);
        tfFocused(tfscLedgerCreateRegisterdName);
        tfFocused(tfscLedgerCreateAdress);
        cmbFocused(cmbscState);
        cmbFocused(cbTrade);
        tfFocused(tfscLedgerCreateBusinessNature);
        tfFocused(tfscLedgerEditPANNo);

        btnScLedgerCreateSubmit.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                btnScLedgerCreateCancel.requestFocus();
            }
        });
    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            scFirstRow.setSpacing(5);
            scSecondRow.setSpacing(5);
            scThirdRow.setSpacing(5);

            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            scFirstRow.setSpacing(6);
            scSecondRow.setSpacing(6);
            scThirdRow.setSpacing(6);

            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            scFirstRow.setSpacing(8);
            scSecondRow.setSpacing(8);
            scThirdRow.setSpacing(8);

            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            scFirstRow.setSpacing(8);
            scSecondRow.setSpacing(8);
            scThirdRow.setSpacing(8);
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            scFirstRow.setSpacing(8);
            scSecondRow.setSpacing(8);
            scThirdRow.setSpacing(8);
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_7.css").toExternalForm());
        }
    }

    private void regDateValidation() {
        tfscLedgerCreateRegDate.focusedProperty().addListener((obs, old, nw) -> {
            System.out.println("nw" + nw);
            if (!nw) {
                String regDateStr = tfscLedgerCreateRegDate.getText();
                if (!regDateStr.isEmpty()) {
                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//                String formattedCurrentDate = currentDate.format(formatter);
                    LocalDate regDate = LocalDate.parse(regDateStr, formatter);
//                System.out.println("register date --> " + regDateStr);
//                System.out.println("currentDate --> " + formattedCurrentDate);
                    if (regDate.isAfter(currentDate)) {
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Register date cannot be greater than the current date. ", input -> {
                            tfscLedgerCreateRegDate.requestFocus();
                        });
                    }
                }
            }
        });
    }

    private void tfFocused(TextField tf) {
        tf.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tf.getText().isEmpty()) {
                    tf.requestFocus();
                }
            }
        });
    }

    private void cmbFocused(ComboBox cmb) {
        cmb.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (cmb.getValue().equals("")) {
                    cmb.requestFocus();
                }
            }
        });
    }

    private void initialEnterMethod() {
        stpanesc.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if(event.getCode()==KeyCode.TAB){
                System.out.println("Tab Target "+event.getTarget().toString());
                if (event.getTarget() instanceof TextField) {
                    TextField rqTxFld = (TextField) event.getTarget();
                    if (rqTxFld.getStyleClass().contains("isRequired") && rqTxFld.getText().trim().isEmpty()) {
                        GlobalTranx.requestFocusOrDieTrying(rqTxFld, 5);
                    }
                }
            }
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("submit")) {
                    targetButton.getText();
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("cancel")) {
                    targetButton.getText();
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

    private void addPincodeValidationListener(TextField tfscLedgerCreatePin) {
        tfscLedgerCreatePin.focusedProperty().addListener((obs, old, nw) -> {
            if (!nw) {
                if (!tfscLedgerCreatePin.getText().isEmpty()) {
                    validatePincode();
                }
                if (tfscLedgerCreatePin.getText().trim().isEmpty()) {
                    tfscLedgerCreatePin.requestFocus();
                }
                String newVal = tfscLedgerCreatePin.getText().trim();
                if (newVal.length() > 6) {
                    tfscLedgerCreatePin.setText(newVal.substring(0, 6));
//                    validatePincode();
                }

            }
        });
    }

    private void addEmailValidationListener(TextField tfscLedgerEditPANNo) {
        tfscLedgerCreateEmail.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                event.consume();
                if (!validEmail(tfscLedgerCreateEmail.getText()) && !tfscLedgerCreateEmail.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered email is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Email Id", newEvent -> {
                        tfscLedgerCreateEmail.requestFocus();
                    });
                    event.consume();
                } else {
                    tfscLedgerCreateWhatsapp.requestFocus();
                }
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {

                if (!validEmail(tfscLedgerCreateEmail.getText()) && !tfscLedgerCreateEmail.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered email is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Email Id", newEvent -> {
                        tfscLedgerCreateEmail.requestFocus();
                    });
                    event.consume();
                } else tfscLedgerCreateRegNo.requestFocus();
            }
        });
    }

    private void addWhatsappValidationListener(TextField tfscLedgerCreatePhone) {
        tfscLedgerCreateWhatsapp.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                event.consume();
                if (!isValidMobileNumber(tfscLedgerCreateWhatsapp.getText()) && !tfscLedgerCreateWhatsapp.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered Whatsapp number is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Whatsapp Number", newEvent -> {
                        tfscLedgerCreateWhatsapp.requestFocus();
                    });
                    event.consume();
                }
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                if (!isValidMobileNumber(tfscLedgerCreateWhatsapp.getText()) && !tfscLedgerCreateWhatsapp.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered Whatsapp number is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Whatsapp Number", newEvent -> {
                        tfscLedgerCreateWhatsapp.requestFocus();
                    });
                    event.consume();
                }
            }
        });
    }

    private void addPANNoValidationListener(TextField tfscLedgerEditPANNo) {
        tfscLedgerEditPANNo.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                event.consume();
                if (!isValidPanNumber(tfscLedgerEditPANNo.getText()) && !tfscLedgerEditPANNo.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered PAN number is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid PAN Number", newEvent -> {
                        tfscLedgerEditPANNo.requestFocus();
                    });
                    event.consume();
                }
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                if (!isValidPanNumber(tfscLedgerEditPANNo.getText()) && !tfscLedgerEditPANNo.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered PAN number is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid PAN Number", newEvent -> {
                        tfscLedgerEditPANNo.requestFocus();
                    });
                    event.consume();
                }
            }
        });
    }

    private void addPhoneValidationListener(TextField tfscLedgerCreatePhone) {
        tfscLedgerCreatePhone.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                event.consume();
                if (!isValidMobileNumber(tfscLedgerCreatePhone.getText()) && !tfscLedgerCreatePhone.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered Phone number is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Phone Number", newEvent -> {
                        tfscLedgerCreatePhone.requestFocus();
                    });
                    event.consume();
                }
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                if (!isValidMobileNumber(tfscLedgerCreatePhone.getText()) && !tfscLedgerCreatePhone.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered Phone number is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Phone Number", newEvent -> {
                        tfscLedgerCreatePhone.requestFocus();
                    });
                    event.consume();
                }
            }
        });
    }

    //    Add listener to only numbers input
    private void addNumericListener(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public void validatePincode() {
        System.out.println("i am in validatePincode()");
        APIClient apiClient = null;
        try {
//            logger.debug("Get ValidatePincode Data Started...");
            Map<String, String> map = new HashMap<>();
            map.put("pincode", String.valueOf(tfscLedgerCreatePin.getText()));
            String formData = Globals.mapToStringforFormData(map);
//            System.out.println("formData--------> " + formData);
            apiClient = new APIClient(EndPoints.VALIDATE_PINCODE_ENDPOINT, formData, RequestType.FORM_DATA);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        System.out.println("200 --------- ");
                        tfFocused(tfscLedgerCreatePhone);
                    } else {
                        System.out.println("404 --------- ");
                        String message = jsonObject.get("message").getAsString();
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Pin Code", input -> {
//                            tfscLedgerCreatePin.requestFocus();
                            GlobalTranx.requestFocusOrDieTrying(tfscLedgerCreatePin, 3);
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

    private void handleTradeComboBoxAction(ActionEvent actionEvent) {
        selectedTrade = cbTrade.getSelectionModel().getSelectedItem() != null ? cbTrade.getSelectionModel().getSelectedItem().toLowerCase() : "";
    }

    //function for email validation
    private boolean validEmail(String email) {
        String emailReg = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailReg);
    }

    // pan no. validation
    public boolean isValidPanNumber(String panNumber) {
        // Regular expression to match PAN card number format
        String panPattern = "[A-Z]{5}[0-9]{4}[A-Z]{1}";

        // Check if the input matches the PAN card number format
        return panNumber.matches(panPattern);
    }

    // phone no. validation
    private boolean isValidMobileNumber(String mobileNumber) {
        // Define your mobile number pattern here. This example assumes a 10-digit number.
        String regex = "\\d{10}";
        return mobileNumber.matches(regex);
    }

    private void setupPincodeValidation() {
        TextFormatter<String> pincodeFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,6}")) { // Matches exactly 6 digits
                return change;
            }
            return null; // Ignore the change if it doesn't match
        });

        tfscLedgerCreatePin.setTextFormatter(pincodeFormatter);
    }

    private void setupPanNoValidation() {
        // Create a TextFormatter that allows only uppercase letters and numbers in the PAN input field
        TextFormatter<String> panFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() <= 10 && newText.matches("[A-Za-z0-9]*")) {
                return change;
            } else {
                return null;
            }
        });

        // Set the text formatter to the PAN input field
        tfscLedgerEditPANNo.setTextFormatter(panFormatter);
    }

    private void setupMobileNumberValidation() {
        TextFormatter<String> whatsappNumberFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d{0,10}")) {
                return change;
            }
            return null;
        });
        tfscLedgerCreateWhatsapp.setTextFormatter(whatsappNumberFormatter);

        TextFormatter<String> mobileNumberFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d{0,10}")) {
                return change;
            }
            return null;
        });
        tfscLedgerCreatePhone.setTextFormatter(mobileNumberFormatter);
    }


    private void initAllSwitchButtons() {
        chkscLedgerCreateIsCredit.setParentBox(vboxchkscLedgerCreateIsCredit);
        initCreditDaysDisable(false);
        chkscLedgerCreateIsCredit.switchOnProperty().addListener((obv, old, nv) -> {
            initCreditDaysDisable(nv);
        });
        chkscIsGST.setParentBox(vboxchkscIsGST);
        initGSTActivate(false);

        chkscIsGST.switchOnProperty().addListener((obv, old, nv) -> {
            initGSTActivate(nv);
            if (lstGst.size() > 0) {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Remove GST List Details !", input -> {
                    btnGstPlus.requestFocus();
                });
                chkscIsGST.switchOnProperty().setValue(true);
            } else {
                System.out.println("List is empty. Cannot activate GST.");
            }
        });


        chkscIsLicense.setParentBox(vboxchkscIsLicense);
        initLicenseActivate(false);

        chkscIsLicense.switchOnProperty().addListener((obv, old, nv) -> {
            initLicenseActivate(nv);
            if (lstLicenseInfo.size() > 0) {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Remove License List Details !", input -> {
                    btnLicensePlus.requestFocus();
                });
                chkscIsLicense.switchOnProperty().setValue(true);
            } else {
                System.out.println("List is empty. Cannot activate License.");
            }
        });
        chkscIsDepartment.setParentBox(vboxchkscIsDepartment);
        initDeptActivate(false);

        chkscIsDepartment.switchOnProperty().addListener((obv, old, nv) -> {
            initDeptActivate(nv);
            if (lstDeptInfo.size() > 0) {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Remove Department List Details !", input -> {
                    btnDeptPlus.requestFocus();
                });
                chkscIsDepartment.switchOnProperty().setValue(true);
            } else {
                System.out.println("List is empty. Cannot activate Department.");
            }
        });
        chkscIsBank.setParentBox(vboxchkscIsBank);
        initBankActivate(false);

        chkscIsBank.switchOnProperty().addListener((obv, old, nv) -> {
            initBankActivate(nv);
            if (lstBankInfo.size() > 0) {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Remove Bank List Details !", input -> {
                    btnBankPlus.requestFocus();
                });
                chkscIsBank.switchOnProperty().setValue(true);
            } else {
                System.out.println("List is empty. Cannot activate Bank.");
            }
        });
        chkscIsShippingDetails.setParentBox(vboxchkscIsShippingDetails);
        initShippingActivate(false);

        chkscIsShippingDetails.switchOnProperty().addListener((obv, old, nv) -> {
            initShippingActivate(nv);
            if (lstShippingInfo.size() > 0) {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Remove Shipping List Details !", input -> {
                    btnShippingPlus.requestFocus();
                });
                chkscIsShippingDetails.switchOnProperty().setValue(true);
            } else {
                System.out.println("List is empty. Cannot activate Shipping.");
            }
        });
    }

    private void initShippingActivate(boolean b) {
        btnShippingPlus.setDisable(!b);
//        if (b) {
//            Platform.runLater(() -> {
//                btnShippingPlus.requestFocus();
//            });
//        } else {
//            Platform.runLater(() -> {
//                btnScLedgerCreateSubmit.requestFocus();
//            });
//        }
    }


    private void initLicenseActivate(boolean b) {
        btnLicensePlus.setDisable(!b);
//        if (b) {
//            Platform.runLater(() -> {
//                btnLicensePlus.requestFocus();
//            });
//        } else {
//            Platform.runLater(() -> {
//                chkscIsDepartment.requestFocus();
//            });
//        }
    }

    private void initDeptActivate(boolean b) {
        btnDeptPlus.setDisable(!b);
//        if (b) {
//            Platform.runLater(() -> {
//                btnDeptPlus.requestFocus();
//            });
//        } else {
//            Platform.runLater(() -> {
//                chkscIsBank.requestFocus();
//            });
//        }
    }

    private void initBankActivate(boolean b) {
        btnBankPlus.setDisable(!b);
//        if (b) {
//            Platform.runLater(() -> {
//                btnBankPlus.requestFocus();
//            });
//        } else {
//            Platform.runLater(() -> {
//                chkscIsShippingDetails.requestFocus();
//            });
//        }
    }

    private void initGSTActivate(boolean b) {
        btnGstPlus.setDisable(!b);
//        lblLedgerEditPANNo.setVisible(!b);
        tfLedgerEditPANNo.setVisible(!b);
        tfscLedgerEditPANNo.setVisible(!b);
//        requiredID.setVisible(!b);
//        lblLedgerEditPANNo.setManaged(!b);
        tfLedgerEditPANNo.setManaged(!b);
        tfscLedgerEditPANNo.setManaged(!b);
//        requiredID.setManaged(!b);
//        if (!b) {
//            Platform.runLater(() -> {
//                tfscLedgerEditPANNo.requestFocus();
//            });
//        } else {
//            Platform.runLater(() -> {
//                btnGstPlus.requestFocus();
//            });
//        }
    }

    private void initCreditDaysDisable(Boolean nv) {
        tfscLedgerCreateCreditDays.setDisable(!nv);
        tfscLedgerCreateCreditBills.setDisable(!nv);
        tfscLedgerCreateCreditValue.setDisable(!nv);
        cmbscLedgerCreateApplicableFrom.setDisable(!nv);
//        if (nv) {
//            Platform.runLater(() -> {
//                tfscLedgerCreateCreditDays.requestFocus();
//            });
//        } else {
////            Platform.runLater(() -> {
////                rdscReatailer.requestFocus();
////            });
//        }
    }


//    private void handleEnterKeyPressed(KeyEvent keyEvent) {
//        Node source = (Node) keyEvent.getSource();
//        if (keyEvent.getCode() == KeyCode.ENTER) {
//            switch (source.getId()) {
//                case "tfscLedgerCreateRegisterdName":
//                    Platform.runLater(() -> {
//                        tfscLedgerCreateAdress.requestFocus();
//                    });
//                    break;
//                case "tfscLedgerCreateAdress":
//                    Platform.runLater(() -> {
//                        cmbscState.requestFocus();
//                    });
//                    break;
//                case "cmbscState":
//                    Platform.runLater(() -> {
//                        tfscLedgerCreatePin.requestFocus();
//                    });
//                    break;
//                case "tfscLedgerCreatePin":
//                    Platform.runLater(() -> {
//                        tfscLedgerCreatePhone.requestFocus();
//                    });
//                    break;
//                case "tfscLedgerCreatePhone":
//                    Platform.runLater(() -> {
//                        tfscLedgerCreateWhatsapp.requestFocus();
//                    });
//                    break;
//                case "tfscLedgerCreateWhatsapp":
//                    Platform.runLater(() -> {
//                        tfscLedgerCreateEmail.requestFocus();
//                    });
//                    break;
//                case "tfscLedgerCreateEmail":
//                    Platform.runLater(() -> {
//                        tfscLedgerCreateRegNo.requestFocus();
//                    });
//                    break;
//                case "tfscLedgerCreateRegNo":
//                    Platform.runLater(() -> {
//                        tfscLedgerCreateRegDate.requestFocus();
//                    });
//                    break;
//                case "tfscLedgerCreateRegDate":
//                    Platform.runLater(() -> {
//                        chkscLedgerCreateIsCredit.requestFocus();
//                    });
//                case "chkscLedgerCreateIsCredit":
//                    if (chkscLedgerCreateIsCredit.switchOnProperty().get()) {
//                        Platform.runLater(() -> {
//                            tfscLedgerCreateCreditDays.requestFocus();
//                        });
//                    } else {
//                        Platform.runLater(() -> {
//                            rdscReatailer.requestFocus();
//                        });
//                    }
//                    break;
//                case "tfscLedgerCreateCreditDays":
//                    Platform.runLater(() -> {
//                        cmbscLedgerCreateApplicableFrom.requestFocus();
//                    });
//                    break;
//                case "cmbscLedgerCreateApplicableFrom":
//                    Platform.runLater(() -> {
//                        tfscLedgerCreateCreditBills.requestFocus();
//                    });
//                    break;
//                case "tfscLedgerCreateCreditBills":
//                    Platform.runLater(() -> {
//                        tfscLedgerCreateCreditValue.requestFocus();
//                    });
//                    break;
//                case "tfscLedgerCreateCreditValue":
//                    Platform.runLater(() -> {
//                        rdscReatailer.requestFocus();
//                    });
//                    break;
//                case "rdscReatailer":
//                    Platform.runLater(() -> {
//                        tfscLedgerCreateBusinessNature.requestFocus();
//                    });
//                    break;
//                case "tfscLedgerCreateBusinessNature":
//                    Platform.runLater(() -> {
//                        vboxchkscIsGST.requestFocus();
//                    });
//                    break;
//                default:
//                    break;
//            }
//        } else if (keyEvent.getCode() == KeyCode.S && keyEvent.isControlDown()) {
//            onSubmit();
//        } else if (keyEvent.getCode() == KeyCode.X && keyEvent.isControlDown()) {
//            btnScLedgerCreateCancel.fire();
//        }
//    }

//    private void initTradeRadioButtonGroup() {
//        rdscDistributor.setToggleGroup(tgTrade);
//        rdscManufacturer.setToggleGroup(tgTrade);
//        rdscReatailer.setToggleGroup(tgTrade);
//    }

    private void initStateCmb() {
        Integer initialIndex = -1;
        List<StateOption> IndiaStateList = Globals.getIndiaStateList();
        ObservableList<StateOption> lstIndiaState = FXCollections.observableArrayList(IndiaStateList);

        cmbscState.getItems().addAll(lstIndiaState);
        cmbscState.setConverter(new StringConverter<StateOption>() {
            @Override
//            public String toString(StateOption commonOption) {
//                return commonOption != null ? commonOption.getStateName() : "";
//            }
            public String toString(StateOption item) {
                if (item == null) {
                    return null;
                } else {
                    return item.getStateName();
                }
            }

            @Override
//            public StateOption fromString(String string) {
//                return null;
//            }
            public StateOption fromString(String string) {
                return lstIndiaState.stream().filter(state -> state.getStateName().equals(string)).findFirst().orElse(null);
            }

        });

        AutoCompleteBox autoCompleteBox = new AutoCompleteBox(cmbscState, initialIndex);
    }

    private void initGSTTable() {
        btnGstPlus.setOnAction(actionEvent -> {
            Stage stage = (Stage) stpanesc.getScene().getWindow();
            LedgerCommonController ledgerCommonController = new LedgerCommonController();
            ledgerCommonController.openGstPopUp(stage, "GST", new GstList(0, 0, "", null, "", ""), input -> {
                addAndUpdateGSTInfo(input);
            });
        });
        tcscGstType.setCellValueFactory(new PropertyValueFactory<>("gstTypeName"));
        tcscPan.setCellValueFactory(new PropertyValueFactory<>("panNo"));
        tcscGstRegDate.setCellValueFactory(new PropertyValueFactory<>("gstRegDate"));
        tcscGstIn.setCellValueFactory(new PropertyValueFactory<>("gstNo"));
        tcscGstAction.setCellFactory(param -> {
            final TableCell<GstListDTO, Void> cell = new TableCell<>() {
                private ImageView delImg = Globals.getDelImage();
                private ImageView edtImg = Globals.getEdtImage();

                {
                    delImg.setFitHeight(20.0);
                    delImg.setFitWidth(20.0);
                    edtImg.setFitHeight(20.0);
                    edtImg.setFitWidth(20.0);
                }

                private final Button actionButton = new Button("", delImg);
                private final Button edtButton = new Button("", edtImg);

                {

                    actionButton.setOnAction(event -> {

                        tbllstGst.remove(getIndex());
                        lstGst.remove(getIndex());
                        btnGstPlus.requestFocus();
                    });
                    edtButton.setOnAction(event -> {

                        edtIdx = getIndex();

                        GstList fnGst = lstGst.get(edtIdx);
                        if (fnGst != null) {
                            Stage stage = (Stage) stpanesc.getScene().getWindow();
                            LedgerCommonController ledgerCommonController = new LedgerCommonController();
                            ledgerCommonController.openGstPopUp(stage, "GST", fnGst, input -> {
                                addAndUpdateGSTInfo(input);
                                btnGstPlus.requestFocus();
                            });

                        } else {
                            String msg = "Record Not Found!";
                            AlertUtility.CustomCallback callback = number -> {
                                btnGstPlus.requestFocus();
                            };
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
                        }
                    });

                }

                HBox hbActions = new HBox();

                {
                    hbActions.getChildren().add(edtButton);
                    hbActions.getChildren().add(actionButton);
                    hbActions.setSpacing(10.0);
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(hbActions);
                    }
                }
            };
            return cell;
        });

        tblscGstInfo.setItems(tbllstGst);

    }

    private void addAndUpdateGSTInfo(GstList gstList) {
        GstListDTO gstListDTO = new GstListDTO(0, gstList.getGstTypeid(), gstList.getGstTypeName(), gstList.getGstRegDate(), gstList.getGstNo(), gstList.getPanNo());
        if (edtIdx >= 0) {
            tbllstGst.remove(edtIdx);
            tbllstGst.add(gstListDTO);
            lstGst.remove(edtIdx);
            lstGst.add(gstList);
            edtIdx = -1;
        } else {
            GstList isExist = lstGst.stream().filter((v) -> v.getGstNo().equalsIgnoreCase(gstListDTO.getGstNo())).findAny().orElse(null);
            if (isExist == null) {
                lstGst.add(gstList);
                tbllstGst.add(gstListDTO);
            } else {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Gst Details are Already Exist !", input -> {
                });
            }
        }
    }


    private void initBankInfoTable() {
        btnBankPlus.setOnAction(actionEvent -> {
            Stage stage = (Stage) stpanesc.getScene().getWindow();
            LedgerCommonController ledgerCommonController = new LedgerCommonController();
            ledgerCommonController.openBankPopUp(stage, "Bank Details", new BankDetailList(0, "", "", "", ""), input -> {
                addAndUpdateBankInfo(input);
            });
        });
        tcscBankAccNo.setCellValueFactory(new PropertyValueFactory<>("bankAccNo"));
        tcscBankName.setCellValueFactory(new PropertyValueFactory<>("bankName"));
        tcscBankBranch.setCellValueFactory(new PropertyValueFactory<>("bankBranch"));
        tcscBankIFSCCode.setCellValueFactory(new PropertyValueFactory<>("bankIFSCCode"));
        tcscBankAct.setCellFactory(param -> {
            final TableCell<BankDetailsDTO, Void> cell = new TableCell<>() {
                private ImageView delImg = Globals.getDelImage();
                private ImageView edtImg = Globals.getEdtImage();

                {
                    delImg.setFitHeight(20.0);
                    delImg.setFitWidth(20.0);
                    edtImg.setFitHeight(20.0);
                    edtImg.setFitWidth(20.0);
                }

                private final Button delButton = new Button("", delImg);
                private final Button edtButton = new Button("", edtImg);

                {

                    delButton.setOnAction(event -> {
                        bankEdtIdx = -1;
                        lstBankInfo.remove(getIndex());
                        btnBankPlus.requestFocus();
                        tbllstBankInfo.remove(getIndex());
                    });
                    edtButton.setOnAction(event -> {
                        bankEdtIdx = getIndex();
                        BankDetailList fnBankDetails = lstBankInfo.get(bankEdtIdx);
                        if (fnBankDetails != null) {
                            Stage stage = (Stage) stpanesc.getScene().getWindow();
                            LedgerCommonController ledgerCommonController = new LedgerCommonController();
                            ledgerCommonController.openBankPopUp(stage, "Bank Details", fnBankDetails, input -> {
                                addAndUpdateBankInfo(input);
                                btnBankPlus.requestFocus();
                            });
                        } else {
                            String msg = "Record Not Found!";
                            AlertUtility.CustomCallback callback = number -> {
                                btnBankPlus.requestFocus();
                            };
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
                        }
                    });

                }

                HBox hbActions = new HBox();

                {
                    hbActions.getChildren().add(edtButton);
                    hbActions.getChildren().add(delButton);
                    hbActions.setSpacing(10.0);
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(hbActions);
                    }
                }
            };
            return cell;
        });

        tblscBankInfo.setItems(tbllstBankInfo);

    }

    private void addAndUpdateBankInfo(BankDetailList bankDetailList) {
        BankDetailsDTO bankDetailsDTO = new BankDetailsDTO(bankDetailList.getId(), bankDetailList.getBankName(), bankDetailList.getBankAccNo(), bankDetailList.getBankIFSCCode(), bankDetailList.getBankBranch());
        if (bankEdtIdx >= 0) {
            tbllstBankInfo.remove(bankEdtIdx);
            lstBankInfo.remove(bankEdtIdx);
            tbllstBankInfo.add(bankDetailsDTO);
            lstBankInfo.add(bankDetailList);
            bankEdtIdx = -1;
        } else {
            tbllstBankInfo.add(bankDetailsDTO);
            lstBankInfo.add(bankDetailList);
        }
    }

    private void onSubmit() {
        GlobalLedgerCreateController globalLedgerCreateController = GlobalLedgerCreateController.getInstance();
        if (ValidateSCForm()) {
            System.out.println("lstShippingInfo" + lstShippingInfo);
//            RadioButton selectedRdBtn = (RadioButton) tgTrade.getSelectedToggle();
            globalLedgerCreateController.fnCallLedgerCreateSCSubmit(tfscLedgerCreateRegisterdName.getText(), tfscLedgerCreateAdress.getText(), cmbscState.getSelectionModel().getSelectedItem() != null ? String.valueOf(cmbscState.getSelectionModel().getSelectedItem().getId()) : "", tfscLedgerCreatePin.getText(), tfscLedgerCreatePhone.getText(), tfscLedgerCreateWhatsapp.getText(), tfscLedgerCreateEmail.getText(), tfscLedgerCreateRegNo.getText(),
//                    DateConvertUtil.convertDatetoAPIDateString(tfscLedgerCreateRegDate.getValue()),
//                    Communicator.text_to_date.fromString(tfscLedgerCreateRegDate.getText()).toString(),
                    tfscLedgerCreateRegDate.getText().isEmpty() ? "" : Communicator.text_to_date.fromString(tfscLedgerCreateRegDate.getText()).toString(),
//                    selectedRdBtn.getText(),
//                    selectedTrade != null ? selectedTrade : "",
                    cbTrade.getSelectionModel().getSelectedItem() != null ? cbTrade.getSelectionModel().getSelectedItem().toLowerCase() : "", tfscLedgerCreateBusinessNature.getText(), chkscLedgerCreateIsCredit.switchOnProperty().get(), tfscLedgerCreateCreditDays.getText(), cmbscLedgerCreateApplicableFrom.getSelectionModel().getSelectedItem() != null ? cmbscLedgerCreateApplicableFrom.getSelectionModel().getSelectedItem().getName() : "", tfscLedgerCreateCreditBills.getText(), tfscLedgerCreateCreditValue.getText(), tfscLedgerEditPANNo.getText(), chkscIsGST.switchOnProperty().get(), lstGst, chkscIsLicense.switchOnProperty().get(), lstLicenseInfo, chkscIsBank.switchOnProperty().get(), lstBankInfo, chkscIsShippingDetails.switchOnProperty().get(), lstShippingInfo, chkscIsDepartment.switchOnProperty().get(), lstDeptInfo);
        }
    }

    private void initBtnSubmitAndCancel() {
        GlobalLedgerCreateController globalLedgerCreateController = GlobalLedgerCreateController.getInstance();
        btnScLedgerCreateSubmit.setOnAction(actionEvent -> {
            onSubmit();
        });
        btnScLedgerCreateCancel.setOnAction(actionEvent -> {
            globalLedgerCreateController.fcCallLedgerCreateOtherCancel();
        });
    }

    private boolean ValidateSCForm() {
        if (tfscLedgerCreateRegisterdName.getText().trim().isEmpty()) {
            String msg = "Enter mandatory Registered Name";
            AlertUtility.CustomCallback callback = number -> {
                tfscLedgerCreateRegisterdName.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        } else if (tfscLedgerCreateAdress.getText().trim().isEmpty()) {
            String msg = "Enter mandatory Address ";
            AlertUtility.CustomCallback callback = number -> {
                tfscLedgerCreateAdress.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        } else if (cmbscState.getSelectionModel().getSelectedItem() == null) {
            String msg = "Select mandatory State ";
            AlertUtility.CustomCallback callback = number -> {
                cmbscState.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        } else if (tfscLedgerCreatePin.getText().trim().isEmpty()) {
            String msg = "Enter mandatory Pin Code ";
            AlertUtility.CustomCallback callback = number -> {
                tfscLedgerCreatePin.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        } else if (tfscLedgerCreatePhone.getText().trim().isEmpty()) {
            String msg = "Enter mandatory Phone Number ";
            AlertUtility.CustomCallback callback = number -> {
                tfscLedgerCreatePhone.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        } else if (tfscLedgerCreateWhatsapp.getText().trim().isEmpty()) {
            String msg = "Enter mandatory Whatsapp Number ";
            AlertUtility.CustomCallback callback = number -> {
                tfscLedgerCreateWhatsapp.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        } else if (tfscLedgerCreateBusinessNature.getText().trim().isEmpty()) {
            String msg = "Enter mandatory Business Nature ";
            AlertUtility.CustomCallback callback = number -> {
                tfscLedgerCreateBusinessNature.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }

        if (tfscLedgerCreatePin.getText().length() < 6) {
            String msg = "Enter 6 Digit Pin Code ";
            AlertUtility.CustomCallback callback = number -> {
                tfscLedgerCreatePin.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }

        if (tfscLedgerCreatePhone.getText().length() < 10) {
            String msg = "Enter 10 Digit Number";
            AlertUtility.CustomCallback callback = number -> {
                tfscLedgerCreatePhone.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }

        if (tfscLedgerCreateWhatsapp.getText().length() < 10) {
            String msg = "Enter 10 Digit Number For Whatsapp";
            AlertUtility.CustomCallback callback = number -> {
                tfscLedgerCreateWhatsapp.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }

        if (chkscIsGST.switchOnProperty().get() && lstGst.size() == 0) {
            String msg = "GST Number is mandatory!";
            AlertUtility.CustomCallback callback = number -> {
                btnLicensePlus.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }
        if (chkscIsGST.switchOnProperty().get() == false && tfscLedgerEditPANNo.getText().isEmpty() && tfscLedgerEditPANNo.getText().length() < 10) {
            String msg = "PAN Number is mandatory !";
            AlertUtility.CustomCallback callback = number -> {
                tfscLedgerEditPANNo.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }
        if (chkscIsLicense.switchOnProperty().get() && lstLicenseInfo.size() == 0) {
            String msg = "License Number is mandatory";
            AlertUtility.CustomCallback callback = number -> {
                btnLicensePlus.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }
        if (chkscIsDepartment.switchOnProperty().get() && lstDeptInfo.size() == 0) {
            String msg = "Department Details Are mandatory";
            AlertUtility.CustomCallback callback = number -> {
                btnDeptPlus.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }
        if (chkscIsBank.switchOnProperty().get() && lstBankInfo.size() == 0) {
            String msg = "Bank Details Should be entered";
            AlertUtility.CustomCallback callback = number -> {
                btnDeptPlus.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }
        if (chkscIsShippingDetails.switchOnProperty().get() && lstShippingInfo.size() == 0) {
            String msg = "Shipping Details are mandatory";
            AlertUtility.CustomCallback callback = number -> {
                btnDeptPlus.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }

//        RadioButton selectedRdBtn = (RadioButton) tgTrade.getSelectedToggle();
//        if (selectedRdBtn == null) {
//            String msg = "Please select Trade";
//            AlertUtility.CustomCallback callback = number -> {
//                btnDeptPlus.requestFocus();
//            };
//            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
//            return false;
//        }

        return true;
    }

    private void initDeptInfoTable() {
        btnDeptPlus.setOnAction(actionEvent -> {
            Stage stage = (Stage) stpanesc.getScene().getWindow();
            LedgerCommonController ledgerCommonController = new LedgerCommonController();
            ledgerCommonController.openDepartmentPopUp(stage, "Department", new DeptInfo(0, "", "", "", ""), input -> {
                addAndUpdateDeptInfo(input);
            });
        });
        tcscDeptName.setCellValueFactory(new PropertyValueFactory<>("personDeptName"));
        tcscDeptPersonName.setCellValueFactory(new PropertyValueFactory<>("personName"));
        tcscDeptPersonEmail.setCellValueFactory(new PropertyValueFactory<>("personEmail"));
        tcscDeptPersonPhone.setCellValueFactory(new PropertyValueFactory<>("personPhone"));
        tcscDeptPersonAct.setCellFactory(param -> {
            final TableCell<DeptInfoDTO, Void> cell = new TableCell<>() {
                private ImageView delImg = Globals.getDelImage();
                private ImageView edtImg = Globals.getEdtImage();

                {
                    delImg.setFitHeight(20.0);
                    delImg.setFitWidth(20.0);
                    edtImg.setFitHeight(20.0);
                    edtImg.setFitWidth(20.0);
                }

                private final Button delButton = new Button("", delImg);
                private final Button edtButton = new Button("", edtImg);

                {
                    delButton.setOnAction(event -> {
                        deptEdtIdx = -1;
                        tbllstDeptInfo.remove(getIndex());
                        lstDeptInfo.remove(getIndex());
                        btnDeptPlus.requestFocus();
                    });
                    edtButton.setOnAction(event -> {
                        deptEdtIdx = getIndex();
                        DeptInfo fnDeptInfo = lstDeptInfo.get(deptEdtIdx);
                        if (fnDeptInfo != null) {
                            Stage stage = (Stage) stpanesc.getScene().getWindow();
                            LedgerCommonController ledgerCommonController = new LedgerCommonController();
                            ledgerCommonController.openDepartmentPopUp(stage, "Department", fnDeptInfo, input -> {
                                addAndUpdateDeptInfo(input);
                                btnDeptPlus.requestFocus();
                            });
                        } else {
                            String msg = "Record Not Found!";
                            AlertUtility.CustomCallback callback = number -> {
                                btnDeptPlus.requestFocus();
                            };
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
                        }
                    });
                }

                HBox hbActions = new HBox();

                {
                    hbActions.getChildren().add(edtButton);
                    hbActions.getChildren().add(delButton);
                    hbActions.setSpacing(10.0);
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(hbActions);
                    }
                }
            };
            return cell;
        });
        tblscLedgerCreateDept.setItems(tbllstDeptInfo);
    }

    private void addAndUpdateDeptInfo(DeptInfo deptInfo) {
        DeptInfoDTO deptInfoDTO = new DeptInfoDTO(deptInfo.getPersonDeptName(), deptInfo.getPersonName(), deptInfo.getPersonEmail(), deptInfo.getPersonPhone());
        if (deptEdtIdx >= 0) {
            tbllstDeptInfo.add(deptInfoDTO);
            lstDeptInfo.add(deptInfo);
            tbllstDeptInfo.remove(deptEdtIdx);
            lstDeptInfo.remove(deptEdtIdx);
            deptEdtIdx = -1;

        } else {
            tbllstDeptInfo.add(deptInfoDTO);
            lstDeptInfo.add(deptInfo);
        }
    }


    private void initShippingInfoTable() {
        btnShippingPlus.setOnAction(actionEvent -> {
            Stage stage = (Stage) stpanesc.getScene().getWindow();
            LedgerCommonController ledgerCommonController = new LedgerCommonController();
            ledgerCommonController.openShippingPopUp(stage, "Shipping Details", new ShippingInfo(0, "", 0, "", 0), input -> {
                addAndUpdateShippingInfo(input);
            });
        });
        tcscShippingAddress.setCellValueFactory(new PropertyValueFactory<>("shippingName"));
        tcscShippingState.setCellValueFactory(new PropertyValueFactory<>("shippingStateName"));
        tcscShippingAct.setCellFactory(param -> {
            final TableCell<ShippingDTO, Void> cell = new TableCell<>() {
                private ImageView delImg = Globals.getDelImage();
                private ImageView edtImg = Globals.getEdtImage();

                {
                    delImg.setFitHeight(20.0);
                    delImg.setFitWidth(20.0);
                    edtImg.setFitHeight(20.0);
                    edtImg.setFitWidth(20.0);
                }

                private final Button delButton = new Button("", delImg);
                private final Button edtButton = new Button("", edtImg);

                {

                    delButton.setOnAction(event -> {
                        lstShippingInfo.remove(getIndex());
                        btnShippingPlus.requestFocus();
                        tbllstShippingInfo.remove(getIndex());
                        shippingEdtIdx = -1;
                    });
                    edtButton.setOnAction(event -> {
                        shippingEdtIdx = getIndex();
                        ShippingInfo fnShipping = lstShippingInfo.get(shippingEdtIdx);
                        if (fnShipping != null) {
                            Stage stage = (Stage) stpanesc.getScene().getWindow();
                            LedgerCommonController ledgerCommonController = new LedgerCommonController();
                            ledgerCommonController.openShippingPopUp(stage, "Shipping Details", fnShipping, input -> {
                                addAndUpdateShippingInfo(input);
                                btnShippingPlus.requestFocus();
                            });
                        } else {
                            String msg = "Record Not Found!";
                            AlertUtility.CustomCallback callback = number -> {
                                btnShippingPlus.requestFocus();
                            };
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
                        }
                    });

                }

                HBox hbActions = new HBox();

                {
                    hbActions.getChildren().add(edtButton);
                    hbActions.getChildren().add(delButton);
                    hbActions.setSpacing(10.0);
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(hbActions);
                    }
                }
            };
            return cell;
        });
        tblscLedgerCreateShipping.setItems(tbllstShippingInfo);
    }

    private void addAndUpdateShippingInfo(ShippingInfo shippingInfo) {
        ShippingDTO shippingDTO = new ShippingDTO(shippingInfo.getShippingName(), shippingInfo.getShippingStateName(), shippingInfo.getShippingStateId());
        if (shippingEdtIdx >= 0) {
            tbllstShippingInfo.add(shippingDTO);
            lstShippingInfo.add(shippingInfo);
            lstShippingInfo.remove(shippingEdtIdx);
            tbllstShippingInfo.remove(shippingEdtIdx);
            shippingEdtIdx = -1;
        } else {
            tbllstShippingInfo.add(shippingDTO);
            lstShippingInfo.add(shippingInfo);
        }
    }


    private void initLicenseInfoTable() {
        btnLicensePlus.setOnAction(actionEvent -> {
            Stage stage = (Stage) stpanesc.getScene().getWindow();
            LedgerCommonController ledgerCommonController = new LedgerCommonController();
            ledgerCommonController.openLicensePopUp(stage, "License", new LicenseInfo(0, "", "", 0, "", null, ""), input -> {
                addAndUpdateLicenseInfo(input);
            });
        });
        tcscLicenseNo.setCellValueFactory(new PropertyValueFactory<>("licenseNo"));
        tcscLicenseType.setCellValueFactory(new PropertyValueFactory<>("licenseType"));
        tcscLicenseExp.setCellValueFactory(new PropertyValueFactory<>("licenseExpDate"));
        tcscLicenseAct.setCellFactory(param -> {
            final TableCell<LicenseDetailDTO, Void> cell = new TableCell<>() {
                private ImageView delImg = Globals.getDelImage();
                private ImageView edtImg = Globals.getEdtImage();

                {
                    delImg.setFitHeight(20.0);
                    delImg.setFitWidth(20.0);
                    edtImg.setFitHeight(20.0);
                    edtImg.setFitWidth(20.0);
                }

                private final Button delButton = new Button("", delImg);
                private final Button edtButton = new Button("", edtImg);

                {
                    delButton.setOnAction(event -> {
                        licenseEdtIdx = -1;
                        tbllstLicenseInfo.remove(getIndex());
                        lstLicenseInfo.remove(getIndex());
                        btnLicensePlus.requestFocus();
                    });
                    edtButton.setOnAction(event -> {
                        licenseEdtIdx = getIndex();
                        LicenseInfo fnLicenseInfo = lstLicenseInfo.get(licenseEdtIdx);
                        if (fnLicenseInfo != null) {
                            Stage stage = (Stage) stpanesc.getScene().getWindow();
                            LedgerCommonController ledgerCommonController = new LedgerCommonController();
                            ledgerCommonController.openLicensePopUp(stage, "License", fnLicenseInfo, input -> {
                                addAndUpdateLicenseInfo(input);
                                btnLicensePlus.requestFocus();
                            });
                        } else {
                            String msg = "Record Not Found!";
                            AlertUtility.CustomCallback callback = number -> {
                                btnLicensePlus.requestFocus();
                            };
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
                        }

                    });
                }

                HBox hbActions = new HBox();

                {
                    hbActions.getChildren().add(edtButton);
                    hbActions.getChildren().add(delButton);
                    hbActions.setSpacing(10.0);
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(hbActions);
                    }
                }
            };
            return cell;
        });
        tblscLedgerCreateLicenses.setItems(tbllstLicenseInfo);
    }

    private void addAndUpdateLicenseInfo(LicenseInfo licenseInfo) {
        LicenseDetailDTO licenseDetailDTO = new LicenseDetailDTO(licenseInfo.getLicenseNo(), licenseInfo.getLicenseTypeName(), licenseInfo.getLicenseExp() != null ? licenseInfo.getLicenseExp() : null, "");

        if (licenseEdtIdx >= 0) {
            tbllstLicenseInfo.remove(licenseEdtIdx);
            lstLicenseInfo.add(licenseInfo);
            tbllstLicenseInfo.add(licenseDetailDTO);
            lstLicenseInfo.remove(licenseEdtIdx);
            licenseEdtIdx = -1;
        } else {
            licenseEdtIdx = -1;
            lstLicenseInfo.add(licenseInfo);
            tbllstLicenseInfo.add(licenseDetailDTO);
        }


    }

    private void initApplicableFromCmb() {
        List<CommonOption> ApplicableFromList = Globals.getApplicableFromList();
        ObservableList<CommonOption> lstApplicableFrom = FXCollections.observableArrayList(ApplicableFromList);
        cmbscLedgerCreateApplicableFrom.getItems().addAll(lstApplicableFrom);
        cmbscLedgerCreateApplicableFrom.setConverter(new StringConverter<CommonOption>() {
            @Override
            public String toString(CommonOption commonOption) {
                return commonOption != null ? commonOption.getName() : "";
            }

            @Override
            public CommonOption fromString(String string) {
                return null;
            }
        });
    }

}