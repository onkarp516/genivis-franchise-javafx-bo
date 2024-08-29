package com.opethic.genivis.controller.master.ledger.edit;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.commons.SwitchButton;
import com.opethic.genivis.controller.master.ledger.common.LedgerCommonController;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.dto.master.ledger.*;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.models.master.ledger.*;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LedgerEditSDController implements Initializable {

    @FXML
    public VBox vboxchksdLedgerEditIsCredit, vboxchksdIsGST, vboxchksdIsLicense, vboxchksdIsDepartment, vboxchksdIsBank, vboxchksdIsShippingDetails;


    @FXML
    private SwitchButton chksdLedgerEditIsSalesMan, chksdLedgerEditIsCredit;

    @FXML
    private HBox ledgersdMainDividerEdit, topFirstRowEdit, topSecondRowEdit, topThirdRowEdit, editSdInfoRowFirst, editSdInfoRowSecond, editSdInfoRowThird;
    @FXML
    private VBox ledgersdMainDividerFirstEdit, ledgersdMainDividerSecondEdit, leftRowInfoFirst, rightRowInfoFirst, leftRowInfoSecond, rightRowInfoSecond, leftRowInfoThird, rightRowInfoThird;

    @FXML
    private SwitchButton chksdIsGST, chksdIsBank, chksdIsLicense, chksdIsDepartment, chksdIsShippingDetails;

    @FXML
    private VBox vboxscLedgerEditIsCredit, vboxchkscIsGST, vboxchkscIsLicense, vboxchkscIsDepartment, vboxchkscIsBank, vboxchksdLedgerEditIsSalesMan, vboxchkscIsShippingDetails;

    @FXML
    private ComboBox<CommonOption> cmbsdLedgerEditApplicableFrom;

    @FXML
    private ComboBox<CommonOption> cmbsdLedgerEditArea;

    @FXML
    private ComboBox<CommonOption> cmbsdLedgerEditSalesman;

    @FXML
    private ComboBox<StateOption> cmbsdState;

    @FXML
    private RadioButton rdsdDistributor;

    @FXML
    private RadioButton rdsdManufacturer;

    @FXML
    private RadioButton rdsdRetailer;

    @FXML
    private BorderPane stpanesc;
//    private final ToggleGroup tgTrade = new ToggleGroup();

    @FXML
    private TextField tfsdLedgerEditBusinessNature, tfsdLedgerEditCreditBills, tfsdLedgerEditCreditDays, tfsdLedgerEditCreditValue, tfsdLedgerEditAdress;

    @FXML
    private TextField tfsdLedgerEditBankAccNo;

    @FXML
    private TextField tfsdLedgerEditBankBranch;

    @FXML
    private TextField tfsdLedgerEditBankINFSC;

    @FXML
    private TextField tfsdLedgerEditBankName;

    @FXML
    private TextField tfsdLedgerEditEmail;

    @FXML
    private TextField tfsdLedgerEditPhone;

    @FXML
    private TextField tfsdLedgerEditPin;

    //    @FXML
//    private DatePicker tfsdLedgerEditRegDate;
    @FXML
    private TextField tfsdLedgerEditRegDate;

    @FXML
    private TextField tfsdLedgerEditRegNo;

    @FXML
    private TextField tfsdLedgerEditRegisterdName;

    @FXML
    private TextField tfsdLedgerEditRoute;

    @FXML
    private TextField tfsdLedgerEditWhatsapp;

    @FXML
    private Button btnSdLedgerEditSubmit, btnSdLedgerEditCancel;

    //?GST Table Related Fields
    @FXML
    private Button btnGstPlus;
    @FXML
    private ComboBox<GSTType> cmbsdLedgerEditGSTType;
    @FXML
    private TableView<GstListDTO> tblsdGstInfo;
    @FXML
    private TableColumn<GstListDTO, Void> tcsdGstAction;

    @FXML
    private TableColumn<?, ?> tcsdGstIn;

    @FXML
    private TableColumn<?, ?> tcsdGstRegDate;

    @FXML
    private TableColumn<?, ?> tcsdGstType;
    @FXML
    private TableColumn<?, ?> tcsdPan;
    @FXML
    private TextField tfsdLedgerEditGSTNo;
    @FXML
    private TextField tfsdLedgerEditGSTPAN;
    @FXML
    private DatePicker tfsdLedgerEditGSTRegDate;
    //?GST Table Related Fields

    @FXML
    private ComboBox<String> cbTrade;
    String selectedTrade = "";

    @FXML
    private Button btnBankPlus;
    @FXML
    private TableView<BankDetailsDTO> tblsdBankInfo;
    @FXML
    private TableColumn<?, ?> tcsdBankAccNo;

    @FXML
    private TableColumn<BankDetailsDTO, Void> tcsdBankAct;

    @FXML
    private TableColumn<?, ?> tcsdBankBranch;

    @FXML
    private TableColumn<?, ?> tcsdBankIFSCCode;

    @FXML
    private TableColumn<?, ?> tcsdBankName;

    @FXML
    private ComboBox<LicenseType> cmbsdLedgerEditLicenseType;
    @FXML
    private TextField tfsdLedgerEditLicenseNo;
    @FXML
    private DatePicker tfsdLedgerEditLicenseExp;
    @FXML
    private Button btnLicensePlus;

    @FXML
    private TableView<LicenseDetailDTO> tblsdLedgerEditLicenses;
    @FXML
    private TableColumn<LicenseDetailDTO, Void> tcsdLicenseAct;

    @FXML
    private TableColumn<?, ?> tcsdLicenseExp;

    @FXML
    private TableColumn<?, ?> tcsdLicenseNo;

    @FXML
    private TableColumn<?, ?> tcsdLicenseType;

    @FXML
    private Button btnDeptPlus;
    @FXML
    private TableView<DeptInfoDTO> tblsdLedgerEditDept;
    @FXML
    private TableColumn<?, ?> tcsdDeptName;

    @FXML
    private TableColumn<DeptInfoDTO, Void> tcsdDeptPersonAct;

    @FXML
    private TableColumn<?, ?> tcsdDeptPersonEmail;

    @FXML
    private TableColumn<?, ?> tcsdDeptPersonName;

    @FXML
    private TableColumn<?, ?> tcsdDeptPersonPhone;

    @FXML
    private TextField tfsdLedgerEditDeptName;

    @FXML
    private TextField tfsdLedgerEditDeptPersonEmail;

    @FXML
    private TextField tfsdLedgerEditDeptPersonName;

    @FXML
    private TextField tfsdLedgerEditDeptPersonPhone;

    @FXML
    private Button btnShippingPlus;
    @FXML
    private TextField tfsdLedgerEditShippingAddress;
    @FXML
    private ComboBox<StateOption> cmbsdLedgerEditShippingState;
    @FXML
    private TableView<ShippingDTO> tblsdLedgerEditShipping;
    @FXML
    private TableColumn<ShippingDTO, Void> tcsdShippingAct;

    @FXML
    private TableColumn<?, ?> tcsdShippingAddress;

    @FXML
    private TableColumn<?, ?> tcsdShippingState;

    @FXML
    private Label lblBank, lblGST, lblLicense, lblDepartment, lblShipping, lblSalesman;

    String lblStyleCSS = "-fx-font-weight: bold; -fx-text-fill: #00a0f5;";
    private JsonObject jsonObject = null;

    private ObservableList<CommonOption> lstSalesMan = FXCollections.observableArrayList();
    private ObservableList<CommonOption> lstAreaMst = FXCollections.observableArrayList();

    private ObservableList<GstListDTO> tbllstGst = FXCollections.observableArrayList();
    private List<GstList> lstGst = new ArrayList<>();
    private ObservableList<GSTType> lstGstType = FXCollections.observableArrayList();
    private int edtIdx = -1, edtId = 0;

    private ObservableList<BankDetailsDTO> tbllstBankInfo = FXCollections.observableArrayList();
    private List<BankDetailList> lstBankInfo = new ArrayList<>();
    private int bankEdtIdx = -1, bankEdtId = 0;

    private final ObservableList<LicenseDetailDTO> tbllstLicenseInfo = FXCollections.observableArrayList();
    private final List<LicenseInfo> lstLicenseInfo = new ArrayList<>();
    private int licenseEdtIdx = -1, licenseEdtId = 0;

    private final ObservableList<DeptInfoDTO> tbllstDeptInfo = FXCollections.observableArrayList();
    private final List<DeptInfo> lstDeptInfo = new ArrayList<>();
    private int deptEdtIdx = -1, deptEdtId = 0;

    private ObservableList<ShippingDTO> tbllstShippingInfo = FXCollections.observableArrayList();
    private List<ShippingInfo> lstShippingInfo = new ArrayList<>();
    private int shippingEdtIdx = -1, shippingEdtId = 0;

    //?Edit lst All needed
    private ObservableList<StateOption> lstIndiaState = FXCollections.observableArrayList();
    private ObservableList<CommonOption> lstApplicableFrom = FXCollections.observableArrayList();

    private ObservableList<LicenseType> lstLicenseType = FXCollections.observableArrayList();
    private List<Integer> lstRemoveGst = new ArrayList<>();
    private List<Integer> lstRemoveLicense = new ArrayList<>();
    private List<Integer> lstRemoveBank = new ArrayList<>();
    private List<Integer> lstRemoveDept = new ArrayList<>();
    private List<Integer> lstRemoveShipping = new ArrayList<>();

    //    @FXML
//    private Label lblLedgerEditPANNo;
    @FXML
    private HBox tfLedgerEditPANNo;
    @FXML
    private TextField tfscLedgerEditPANNo;
    //    @FXML
//    private Label requiredID;
    @FXML
    private Text requiredID;

    private static final Logger loggerLESDC = LogManager.getLogger(LedgerEditSDController.class);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();
        //For Divide the two info tables same width
        leftRowInfoFirst.prefWidthProperty().bind(editSdInfoRowFirst.widthProperty().multiply(0.5));
        rightRowInfoFirst.prefWidthProperty().bind(editSdInfoRowFirst.widthProperty().multiply(0.5));

        leftRowInfoSecond.prefWidthProperty().bind(editSdInfoRowSecond.widthProperty().multiply(0.5));
        rightRowInfoSecond.prefWidthProperty().bind(editSdInfoRowSecond.widthProperty().multiply(0.5));

        leftRowInfoThird.prefWidthProperty().bind(editSdInfoRowThird.widthProperty().multiply(0.5));
        rightRowInfoThird.prefWidthProperty().bind(editSdInfoRowThird.widthProperty().multiply(0.5));
        // End


//        Platform.runLater(() -> {
//            tfsdLedgerEditRegisterdName.requestFocus();
//        });

        // open Filter dropdown on Space
        cmbsdState.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cmbsdState.isShowing()) {
                cmbsdState.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });
        cmbsdLedgerEditApplicableFrom.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cmbsdLedgerEditApplicableFrom.isShowing()) {
                cmbsdLedgerEditApplicableFrom.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });
        cmbsdLedgerEditSalesman.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cmbsdLedgerEditSalesman.isShowing()) {
                cmbsdLedgerEditSalesman.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });
        cmbsdLedgerEditArea.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cmbsdLedgerEditArea.isShowing()) {
                cmbsdLedgerEditArea.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });

        cbTrade.getItems().addAll("Retailer", "Distributor", "Manufacturer");
        cbTrade.setPromptText("Select Trade");
        cbTrade.setOnAction(this::handleTradeComboBoxAction);
        // open trade dropdown on Space
        cbTrade.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cbTrade.isShowing()) {
                cbTrade.show();
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                tfsdLedgerEditBusinessNature.requestFocus();
            }
            event.consume(); // Consume the event to prevent other actions
        });

        lblBank.setStyle(lblStyleCSS);
        lblGST.setStyle(lblStyleCSS);
        lblLicense.setStyle(lblStyleCSS);
        lblDepartment.setStyle(lblStyleCSS);
        lblShipping.setStyle(lblStyleCSS);
        lblSalesman.setStyle(lblStyleCSS);

        initAllSwitchButtons();
        initStateCmb();
        initSalesManCmb();
        initAreaCmb();
        initGSTTable();
        initBankInfoTable();
        initLicenseInfoTable();
        initDeptInfoTable();
        initShippingInfoTable();
        initApplicableFromCmb();
//        initTradeRadioButtonGroup();
        initBtnSubmitAndCancel();
        setupPincodeValidation();
        setupMobileNumberValidation();
        setupPanNoValidation();

        tfsdLedgerEditPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            //TODO: Update the text property of whatsapp when the value changes in mobileNu,
            tfsdLedgerEditWhatsapp.setText(newValue);
        });

        DateValidator.applyDateFormat(tfsdLedgerEditRegDate);
        CommonFunctionalUtils.restrictDateFormat(tfsdLedgerEditRegDate);

        regDateValidation();

//        tfsdLedgerEditRegisterdName.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfsdLedgerEditAdress.setOnKeyPressed(this::handleEnterKeyPressed);
//        cmbsdState.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfsdLedgerEditPin.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfsdLedgerEditPhone.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfsdLedgerEditWhatsapp.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfsdLedgerEditEmail.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfsdLedgerEditRegNo.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfsdLedgerEditRegDate.setOnKeyPressed(this::handleEnterKeyPressed);
//        chksdLedgerEditIsCredit.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfsdLedgerEditCreditDays.setOnKeyPressed(this::handleEnterKeyPressed);
//        cmbsdLedgerEditApplicableFrom.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfsdLedgerEditCreditBills.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfsdLedgerEditCreditValue.setOnKeyPressed(this::handleEnterKeyPressed);
//        rdsdRetailer.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfsdLedgerEditBusinessNature.setOnKeyPressed(this::handleEnterKeyPressed);

        // Add listener to only numbers input
        addNumericListener(tfsdLedgerEditCreditDays);
        addNumericListener(tfsdLedgerEditCreditBills);
        addNumericListener(tfsdLedgerEditCreditValue);

        // Add listener to convert text to uppercase
        tfscLedgerEditPANNo.textProperty().addListener((observable, oldValue, newValue) -> {
            tfscLedgerEditPANNo.setText(newValue.toUpperCase());
        });

        //code for pin code validation
        addPincodeValidationListener(tfsdLedgerEditPin);
        //code for email validation
        addEmailValidationListener(tfsdLedgerEditEmail);
        // pan no. validation
        addPANNoValidationListener(tfscLedgerEditPANNo);
        // phone no. validation
        addPhoneValidationListener(tfsdLedgerEditPhone);
        // whatsapp no. validation
        addWhatsappValidationListener(tfsdLedgerEditWhatsapp);

        initialEnterMethod();
//        tfFocused(tfsdLedgerEditPhone);
        tfFocused(tfsdLedgerEditWhatsapp);
        tfFocused(tfsdLedgerEditRegisterdName);
        tfFocused(tfsdLedgerEditAdress);
        cmbFocused(cmbsdState);
        cmbFocused(cbTrade);
        tfFocused(tfsdLedgerEditBusinessNature);
        tfFocused(tfscLedgerEditPANNo);

        btnSdLedgerEditSubmit.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                btnSdLedgerEditCancel.requestFocus();
            }
        });
    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            topFirstRowEdit.setSpacing(5);
            topSecondRowEdit.setSpacing(5);
            topThirdRowEdit.setSpacing(5);
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            topFirstRowEdit.setSpacing(6);
            topSecondRowEdit.setSpacing(6);
            topThirdRowEdit.setSpacing(6);
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            topFirstRowEdit.setSpacing(8);
            topSecondRowEdit.setSpacing(8);
            topThirdRowEdit.setSpacing(8);
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            topFirstRowEdit.setSpacing(8);
            topSecondRowEdit.setSpacing(8);
            topThirdRowEdit.setSpacing(8);
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            topFirstRowEdit.setSpacing(8);
            topSecondRowEdit.setSpacing(8);
            topThirdRowEdit.setSpacing(8);
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_7.css").toExternalForm());
        }
    }

    private void regDateValidation() {
        tfsdLedgerEditRegDate.focusedProperty().addListener((obs, old, nw) -> {
            System.out.println("nw" + nw);
            if (!nw) {
                String regDateStr = tfsdLedgerEditRegDate.getText();
                if (!regDateStr.isEmpty()) {
                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//                String formattedCurrentDate = currentDate.format(formatter);
                    LocalDate regDate = LocalDate.parse(regDateStr, formatter);
//                System.out.println("register date --> " + regDateStr);
//                System.out.println("currentDate --> " + formattedCurrentDate);
                    if (regDate.isAfter(currentDate)) {
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Register date cannot be greater than the current date. ", input -> {
                            tfsdLedgerEditRegDate.requestFocus();
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
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("update")) {
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

    //    Add listener to only numbers input
    private void addNumericListener(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void addWhatsappValidationListener(TextField tfsdLedgerEditWhatsapp) {
        tfsdLedgerEditWhatsapp.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                event.consume();
                if (!isValidMobileNumber(tfsdLedgerEditWhatsapp.getText()) && !tfsdLedgerEditWhatsapp.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered Whatsapp number is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Whatsapp Number", newEvent -> {
                        tfsdLedgerEditWhatsapp.requestFocus();
                    });
                    event.consume();
                }
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                if (!isValidMobileNumber(tfsdLedgerEditWhatsapp.getText()) && !tfsdLedgerEditWhatsapp.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered Whatsapp number is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Whatsapp Number", newEvent -> {
                        tfsdLedgerEditWhatsapp.requestFocus();
                    });
                    event.consume();
                }
            }
        });

    }

    private void addPhoneValidationListener(TextField tfsdLedgerEditPhone) {
        tfsdLedgerEditPhone.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                event.consume();
                if (!isValidMobileNumber(tfsdLedgerEditPhone.getText()) && !tfsdLedgerEditPhone.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered Phone number is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Phone Number", newEvent -> {
                        tfsdLedgerEditPhone.requestFocus();
                    });
                    event.consume();
                }
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                if (!isValidMobileNumber(tfsdLedgerEditPhone.getText()) && !tfsdLedgerEditPhone.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered Phone number is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Phone Number", newEvent -> {
                        tfsdLedgerEditPhone.requestFocus();
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

    private void addEmailValidationListener(TextField tfsdLedgerEditEmail) {
        tfsdLedgerEditEmail.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                event.consume();
                if (!validEmail(tfsdLedgerEditEmail.getText()) && !tfsdLedgerEditEmail.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered email is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Email Id", newEvent -> {
                        tfsdLedgerEditEmail.requestFocus();
                    });
                    event.consume();
                }
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {

                if (!validEmail(tfsdLedgerEditEmail.getText()) && !tfsdLedgerEditEmail.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered email is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Email Id", newEvent -> {
                        tfsdLedgerEditEmail.requestFocus();
                    });
                    event.consume();
                }
            }
        });
    }

    private void addPincodeValidationListener(TextField tfsdLedgerEditPin) {
        tfsdLedgerEditPin.focusedProperty().addListener((obs, old, nw) -> {
            if (!nw) {
                if (!tfsdLedgerEditPin.getText().isEmpty()) {
                    validatePincode();
                }
                if (tfsdLedgerEditPin.getText().trim().isEmpty()) {
                    tfsdLedgerEditPin.requestFocus();
                }
                String newVal = tfsdLedgerEditPin.getText().trim();
                if (newVal.length() > 6) {
                    tfsdLedgerEditPin.setText(newVal.substring(0, 6));
//                    validatePincode();
                }

            }
        });
    }

    public void validatePincode() {
        System.out.println("i am in");
        APIClient apiClient = null;
        try {
//            logger.debug("Get ValidatePincode Data Started...");
            Map<String, String> map = new HashMap<>();
            map.put("pincode", String.valueOf(tfsdLedgerEditPin.getText()));
            String formData = Globals.mapToStringforFormData(map);
//            System.out.println("formData--------> " + formData);
            apiClient = new APIClient(EndPoints.VALIDATE_PINCODE_ENDPOINT, formData, RequestType.FORM_DATA);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        System.out.println("200 --------- ");
                        tfFocused(tfsdLedgerEditPhone);
                    } else {
                        System.out.println("404 --------- ");
                        String message = jsonObject.get("message").getAsString();
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Pin Code", newEvent -> {
//                            tfsdLedgerEditPin.requestFocus();
                            GlobalTranx.requestFocusOrDieTrying(tfsdLedgerEditPin, 3);
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
        selectedTrade = cbTrade.getSelectionModel().getSelectedItem();
//        System.out.println("Selected Trade--------> : " + selectedTrade);
        if (cbTrade.getSelectionModel().getSelectedItem() == "Retailer") {
            selectedTrade = "retailer";
//            System.out.println("selectedType- " + selectedTrade);
        } else if (cbTrade.getSelectionModel().getSelectedItem() == "Distributor") {
            selectedTrade = "distributor";
//            System.out.println("selectedType- " + selectedTrade);
        } else if (cbTrade.getSelectionModel().getSelectedItem() == "Manufacturer") {
            selectedTrade = "manufacturer";
//            System.out.println("selectedType- " + selectedTrade);
        }
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

    private void setupPincodeValidation() {
        TextFormatter<String> pincodeFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,6}")) {
                return change;
            }
            return null;
        });

        tfsdLedgerEditPin.setTextFormatter(pincodeFormatter);
    }

    private void setupMobileNumberValidation() {

        TextFormatter<String> whatsappNumberFormatter = new TextFormatter<>(change -> {
            // Allow change if it results in digits only and does not exceed 10 digits
            if (change.getControlNewText().matches("\\d{0,10}")) {
                return change;
            }
            // Reject the change if it doesn't match the required pattern
            return null;
        });

        tfsdLedgerEditWhatsapp.setTextFormatter(whatsappNumberFormatter);
        TextFormatter<String> mobileNumberFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d{0,10}")) {
                return change;
            }
            return null;
        });

        tfsdLedgerEditPhone.setTextFormatter(mobileNumberFormatter);

    }

    private void initAllSwitchButtons() {
        chksdLedgerEditIsCredit.setParentBox(vboxscLedgerEditIsCredit);
        initCreditDaysDisable(false);
        chksdLedgerEditIsCredit.switchOnProperty().addListener((obv, old, nv) -> {
            initCreditDaysDisable(nv);
        });
        chksdIsGST.setParentBox(vboxchkscIsGST);
        initGSTActivate(false);

        chksdIsGST.switchOnProperty().addListener((obv, old, nv) -> {
            initGSTActivate(nv);
            if (lstGst.size() > 0) {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Remove GST List Details !", input -> {
                    btnGstPlus.requestFocus();
                });
                chksdIsGST.switchOnProperty().setValue(true);
            } else {
                System.out.println("List is empty. Cannot activate GST.");
            }
        });

        chksdIsLicense.setParentBox(vboxchkscIsLicense);
        initLicenseActivate(false);

        chksdIsLicense.switchOnProperty().addListener((obv, old, nv) -> {
            initLicenseActivate(nv);
            if (lstLicenseInfo.size() > 0) {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Remove License List Details !", input -> {
                    btnLicensePlus.requestFocus();
                });
                chksdIsLicense.switchOnProperty().setValue(true);
            } else {
                System.out.println("List is empty. Cannot activate License.");
            }
        });
        chksdIsDepartment.setParentBox(vboxchkscIsDepartment);
        initDeptActivate(false);

        chksdIsDepartment.switchOnProperty().addListener((obv, old, nv) -> {
            initDeptActivate(nv);
            if (lstDeptInfo.size() > 0) {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Remove Department List Details !", input -> {
                    btnDeptPlus.requestFocus();
                });
                chksdIsDepartment.switchOnProperty().setValue(true);
            } else {
                System.out.println("List is empty. Cannot activate Department.");
            }
        });
        chksdIsBank.setParentBox(vboxchkscIsBank);
        initBankActivate(false);

        chksdIsBank.switchOnProperty().addListener((obv, old, nv) -> {
            initBankActivate(nv);
            if (lstBankInfo.size() > 0) {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Remove Bank List Details !", input -> {
                    btnBankPlus.requestFocus();
                });
                chksdIsBank.switchOnProperty().setValue(true);
            } else {
                System.out.println("List is empty. Cannot activate Bank.");
            }
        });
        chksdIsShippingDetails.setParentBox(vboxchkscIsShippingDetails);
        initShippingActivate(false);

        chksdIsShippingDetails.switchOnProperty().addListener((obv, old, nv) -> {
            initShippingActivate(nv);
            if (lstShippingInfo.size() > 0) {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Remove Shipping List Details !", input -> {
                    btnShippingPlus.requestFocus();
                });
                chksdIsShippingDetails.switchOnProperty().setValue(true);
            } else {
                System.out.println("List is empty. Cannot activate Shipping.");
            }
        });

        chksdLedgerEditIsSalesMan.setParentBox(vboxchksdLedgerEditIsSalesMan);
        chksdLedgerEditIsCredit.setParentBox(vboxchksdLedgerEditIsCredit);
        chksdIsGST.setParentBox(vboxchksdIsGST);
        chksdIsLicense.setParentBox(vboxchksdIsLicense);
        chksdIsDepartment.setParentBox(vboxchksdIsDepartment);
        chksdIsBank.setParentBox(vboxchksdIsBank);
        chksdIsShippingDetails.setParentBox(vboxchksdIsShippingDetails);


        initSalesmanActivate(false);

        chksdLedgerEditIsSalesMan.switchOnProperty().addListener((obv, old, nv) -> {
            initSalesmanActivate(nv);
        });
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

    private void initLicenseActivate(boolean b) {

        btnLicensePlus.setDisable(!b);
//        if (b) {
//            Platform.runLater(() -> {
//                btnLicensePlus.requestFocus();
//            });
//        } else {
//            Platform.runLater(() -> {
//                chksdIsDepartment.requestFocus();
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
//                chksdIsBank.requestFocus();
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
//                chksdIsShippingDetails.requestFocus();
//            });
//        }
    }

    private void initShippingActivate(boolean b) {

        btnShippingPlus.setDisable(!b);
//        if (b) {
//            Platform.runLater(() -> {
//                btnShippingPlus.requestFocus();
//            });
//        } else {
//            Platform.runLater(() -> {
//                chksdLedgerEditIsSalesMan.requestFocus();
//            });
//        }
    }

    private void initSalesmanActivate(boolean b) {
        cmbsdLedgerEditSalesman.setDisable(!b);
        cmbsdLedgerEditArea.setDisable(!b);
        tfsdLedgerEditRoute.setDisable(!b);
//        if (b) {
//            Platform.runLater(() -> {
//                cmbsdLedgerEditSalesman.requestFocus();
//            });
//        } else {
//            Platform.runLater(() -> {
//                btnSdLedgerEditSubmit.requestFocus();
//            });
//        }
    }

    private void initCreditDaysDisable(Boolean nv) {
        tfsdLedgerEditCreditDays.setDisable(!nv);
        tfsdLedgerEditCreditBills.setDisable(!nv);
        tfsdLedgerEditCreditValue.setDisable(!nv);
        cmbsdLedgerEditApplicableFrom.setDisable(!nv);
//        if (nv) {
//            Platform.runLater(() -> {
//                tfsdLedgerEditCreditDays.requestFocus();
//            });
//        } else {
//           /* Platform.runLater(() -> {
//                rdsdRetailer.requestFocus();
//            });*/
//        }
    }

//    private void handleEnterKeyPressed(KeyEvent keyEvent) {
//        Node source = (Node) keyEvent.getSource();
//        if (keyEvent.getCode() == KeyCode.ENTER) {
//            switch (source.getId()) {
//                case "tfsdLedgerEditRegisterdName":
//                    Platform.runLater(() -> {
//                        tfsdLedgerEditAdress.requestFocus();
//                    });
//                    break;
//                case "tfsdLedgerEditAdress":
//                    Platform.runLater(() -> {
//                        cmbsdState.requestFocus();
//                    });
//                    break;
//                case "cmbsdState":
//                    Platform.runLater(() -> {
//                        tfsdLedgerEditPin.requestFocus();
//                    });
//                    break;
//                case "tfsdLedgerEditPin":
//                    Platform.runLater(() -> {
//                        tfsdLedgerEditPhone.requestFocus();
//                    });
//                    break;
//                case "tfsdLedgerEditPhone":
//                    Platform.runLater(() -> {
//                        tfsdLedgerEditWhatsapp.requestFocus();
//                    });
//                    break;
//                case "tfsdLedgerEditWhatsapp":
//                    Platform.runLater(() -> {
//                        tfsdLedgerEditEmail.requestFocus();
//                    });
//                    break;
//                case "tfsdLedgerEditEmail":
//                    Platform.runLater(() -> {
//                        tfsdLedgerEditRegNo.requestFocus();
//                    });
//                    break;
//                case "tfsdLedgerEditRegNo":
//                    Platform.runLater(() -> {
//                        tfsdLedgerEditRegDate.requestFocus();
//                    });
//                    break;
//                case "tfsdLedgerEditRegDate":
//                    Platform.runLater(() -> {
//                        chksdLedgerEditIsCredit.requestFocus();
//                    });
//                case "chksdLedgerEditIsCredit":
//                    if (chksdLedgerEditIsCredit.switchOnProperty().get()) {
//                        Platform.runLater(() -> {
//                            tfsdLedgerEditCreditDays.requestFocus();
//                        });
//                    } else {
//                        Platform.runLater(() -> {
//                            rdsdRetailer.requestFocus();
//                        });
//                    }
//                    break;
//                case "tfsdLedgerEditCreditDays":
//                    Platform.runLater(() -> {
//                        cmbsdLedgerEditApplicableFrom.requestFocus();
//                    });
//                    break;
//                case "cmbsdLedgerEditApplicableFrom":
//                    Platform.runLater(() -> {
//                        tfsdLedgerEditCreditBills.requestFocus();
//                    });
//                    break;
//                case "tfsdLedgerEditCreditBills":
//                    Platform.runLater(() -> {
//                        tfsdLedgerEditCreditValue.requestFocus();
//                    });
//                    break;
//                case "tfsdLedgerEditCreditValue":
//                    Platform.runLater(() -> {
//                        rdsdRetailer.requestFocus();
//                    });
//                    break;
//                case "rdsdRetailer":
//                    Platform.runLater(() -> {
//                        tfsdLedgerEditBusinessNature.requestFocus();
//                    });
//                    break;
//                case "tfsdLedgerEditBusinessNature":
//                    Platform.runLater(() -> {
//                        chksdIsGST.requestFocus();
//                    });
//                    break;
//                default:
//                    break;
//            }
//        } else if (keyEvent.getCode() == KeyCode.S && keyEvent.isControlDown()) {
//            onSubmit();
//        } else if (keyEvent.getCode() == KeyCode.X && keyEvent.isControlDown()) {
//            btnSdLedgerEditCancel.fire();
//        }
//    }


    private void initStateCmb() {
        Integer initialIndex = -1;
        List<StateOption> IndiaStateList = Globals.getIndiaStateList();
        lstIndiaState = FXCollections.observableArrayList(IndiaStateList);
        cmbsdState.getItems().addAll(lstIndiaState);
        cmbsdState.setConverter(new StringConverter<StateOption>() {
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
                return lstIndiaState.stream()
                        .filter(state -> state.getStateName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        AutoCompleteBox autoCompleteBox = new AutoCompleteBox(cmbsdState, initialIndex);
    }


    private void initAreaCmb() {
        List<CommonOption> areaMstList = getAreaAPICallList();
        lstAreaMst = FXCollections.observableArrayList(areaMstList);
        cmbsdLedgerEditArea.getItems().addAll(lstAreaMst);
        cmbsdLedgerEditArea.setConverter(new StringConverter<CommonOption>() {
            @Override
            public String toString(CommonOption gstType) {
                return gstType != null ? gstType.getName() : "";
            }

            @Override
            public CommonOption fromString(String string) {
                return null;
            }
        });
    }

    private List<CommonOption> getAreaAPICallList() {
        List<CommonOption> lstOpt = new ArrayList<>();
        try {
            HttpResponse<String> response = APIClient.getRequest(EndPoints.getOutletAreaMaster);
            JSONObject res = new JSONObject(response.body());
            if (res.getInt("responseStatus") == 200) {
                JSONArray jsonArray = res.getJSONArray("responseObject");
                if (!jsonArray.isEmpty()) {
                    for (Object object : jsonArray) {
                        JSONObject obj = new JSONObject(object.toString());
                        CommonOption commonOption = new CommonOption(obj.getInt("id"), obj.getString("areaName"));
                        lstOpt.add(commonOption);
                    }
                }
            } else {
                String msg = res.getString("message");
                AlertUtility.CustomCallback callback = number -> {
                };
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            }
        } catch (Exception e) {
            loggerLESDC.error("Exception getAreaAPICallList() : " + Globals.getExceptionString(e));
        }
        return lstOpt;
    }

    private void initSalesManCmb() {
        List<CommonOption> salesManList = getSalesManAPICallList();
        lstSalesMan = FXCollections.observableArrayList(salesManList);
        cmbsdLedgerEditSalesman.getItems().addAll(lstSalesMan);
        cmbsdLedgerEditSalesman.setConverter(new StringConverter<CommonOption>() {
            @Override
            public String toString(CommonOption gstType) {
                return gstType != null ? gstType.getName() : "";
            }

            @Override
            public CommonOption fromString(String string) {
                return null;
            }
        });
    }

    private List<CommonOption> getSalesManAPICallList() {
        List<CommonOption> lstOpt = new ArrayList<>();
        try {
            HttpResponse<String> response = APIClient.getRequest(EndPoints.getOutletSalesmanMaster);
            JSONObject res = new JSONObject(response.body());
            if (res.getInt("responseStatus") == 200) {
                JSONArray jsonArray = res.getJSONArray("responseObject");
                if (!jsonArray.isEmpty()) {
                    for (Object object : jsonArray) {
                        JSONObject obj = new JSONObject(object.toString());
                        CommonOption commonOption = new CommonOption(obj.getInt("id"), obj.getString("firstName") + " " + obj.getString("lastName"));
                        lstOpt.add(commonOption);
                    }
                }
            } else {
                String msg = res.getString("message");
                AlertUtility.CustomCallback callback = number -> {
                };
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            }
        } catch (Exception e) {
            loggerLESDC.error("Exception getSalesManAPICallList() : " + Globals.getExceptionString(e));
        }
        return lstOpt;
    }

    private void initGSTTable() {
        btnGstPlus.setOnAction(actionEvent -> {
            Stage stage = (Stage) stpanesc.getScene().getWindow();
            LedgerCommonController ledgerCommonController = new LedgerCommonController();
            ledgerCommonController.openGstPopUp(stage, "GST", new GstList(0, 0, "", null, "", ""), input -> {
                addAndUpdateGSTInfo(input);
            });
        });
        tcsdGstType.setCellValueFactory(new PropertyValueFactory<>("gstTypeName"));
        tcsdPan.setCellValueFactory(new PropertyValueFactory<>("panNo"));
        tcsdGstRegDate.setCellValueFactory(new PropertyValueFactory<>("gstRegDate"));
        tcsdGstIn.setCellValueFactory(new PropertyValueFactory<>("gstNo"));
        tcsdGstAction.setCellFactory(param -> {
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
                        // Handle button action here
                        lstRemoveGst.add(lstGst.get(getIndex()).getId());
                        tbllstGst.remove(getIndex());
                        lstGst.remove(getIndex());
                        btnGstPlus.requestFocus();
                    });
                    edtButton.setOnAction(event -> {
                        // Handle button action here
                        edtIdx = getIndex();
                        edtId = lstGst.get(getIndex()).getId();
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

        tblsdGstInfo.setItems(tbllstGst);

    }


    private void addAndUpdateGSTInfo(GstList gstList) {
        GstListDTO gstListDTO = new GstListDTO(edtId, gstList.getGstTypeid(), gstList.getGstTypeName(), gstList.getGstRegDate(), gstList.getGstNo(), gstList.getPanNo());
        if (edtIdx >= 0) {
            tbllstGst.remove(edtIdx);
            tbllstGst.add(gstListDTO);
            lstGst.remove(edtIdx);
            lstGst.add(gstList);
            edtIdx = -1;
            edtId = 0;
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
        tcsdBankAccNo.setCellValueFactory(new PropertyValueFactory<>("bankAccNo"));
        tcsdBankName.setCellValueFactory(new PropertyValueFactory<>("bankName"));
        tcsdBankBranch.setCellValueFactory(new PropertyValueFactory<>("bankBranch"));
        tcsdBankIFSCCode.setCellValueFactory(new PropertyValueFactory<>("bankIFSCCode"));
        tcsdBankAct.setCellFactory(param -> new TableCell<BankDetailsDTO, Void>() {
            private final ImageView delImg = Globals.getDelImage();
            private final ImageView edtImg = Globals.getEdtImage();

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
                    bankEdtId = 0;
                    lstRemoveBank.add(lstBankInfo.get(getIndex()).getId());
                    tbllstBankInfo.remove(getIndex());
                    lstBankInfo.remove(getIndex());
                    btnBankPlus.requestFocus();
                });
                edtButton.setOnAction(event -> {
                    bankEdtIdx = getIndex();
                    bankEdtId = lstBankInfo.get(getIndex()).getId();
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
        });

        tblsdBankInfo.setItems(tbllstBankInfo);

    }

    private void addAndUpdateBankInfo(BankDetailList bankDetailList) {
        BankDetailsDTO bankDetailsDTO = new BankDetailsDTO(bankDetailList.getId(), bankDetailList.getBankName(), bankDetailList.getBankAccNo(), bankDetailList.getBankIFSCCode(), bankDetailList.getBankBranch());
        if (bankEdtIdx >= 0) {
            tbllstBankInfo.remove(bankEdtIdx);
            lstBankInfo.remove(bankEdtIdx);
            tbllstBankInfo.add(bankDetailsDTO);
            lstBankInfo.add(bankDetailList);
            bankEdtIdx = -1;
            bankEdtId = 0;
        } else {
            tbllstBankInfo.add(bankDetailsDTO);
            lstBankInfo.add(bankDetailList);
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
        tcsdLicenseNo.setCellValueFactory(new PropertyValueFactory<>("licenseNo"));
        tcsdLicenseType.setCellValueFactory(new PropertyValueFactory<>("licenseType"));
        tcsdLicenseExp.setCellValueFactory(new PropertyValueFactory<>("licenseExpDate"));
        tcsdLicenseAct.setCellFactory(param -> {
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
                        lstRemoveLicense.add(lstLicenseInfo.get(getIndex()).getId());
                        licenseEdtId = 0;
                        licenseEdtIdx = -1;
                        tbllstLicenseInfo.remove(getIndex());
                        lstLicenseInfo.remove(getIndex());
                        btnLicensePlus.requestFocus();
                    });
                    edtButton.setOnAction(event -> {
                        licenseEdtIdx = getIndex();
                        licenseEdtId = lstLicenseInfo.get(getIndex()).getId();
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
        tblsdLedgerEditLicenses.setItems(tbllstLicenseInfo);
    }

    private void addAndUpdateLicenseInfo(LicenseInfo licenseInfo) {
        LicenseDetailDTO licenseDetailDTO = new LicenseDetailDTO(licenseInfo.getLicenseNo(), licenseInfo.getLicenseTypeName(), licenseInfo.getLicenseExp() != null ? DateConvertUtil.convertDispDateFormat(licenseInfo.getLicenseExp()) : null, "");
        if (licenseEdtIdx >= 0) {
            tbllstLicenseInfo.remove(licenseEdtIdx);
            lstLicenseInfo.add(licenseInfo);
            tbllstLicenseInfo.add(licenseDetailDTO);
            lstLicenseInfo.remove(licenseEdtIdx);
            licenseEdtIdx = -1;
            licenseEdtId = 0;
        } else {
            licenseEdtIdx = -1;
            licenseEdtId = 0;
            lstLicenseInfo.add(licenseInfo);
            tbllstLicenseInfo.add(licenseDetailDTO);
        }


    }

    private void initDeptInfoTable() {
        btnDeptPlus.setOnAction(actionEvent -> {
            Stage stage = (Stage) stpanesc.getScene().getWindow();
            LedgerCommonController ledgerCommonController = new LedgerCommonController();
            ledgerCommonController.openDepartmentPopUp(stage, "Department", new DeptInfo(0, "", "", "", ""), input -> {
                addAndUpdateDeptInfo(input);
            });
        });
        tcsdDeptName.setCellValueFactory(new PropertyValueFactory<>("personDeptName"));
        tcsdDeptPersonName.setCellValueFactory(new PropertyValueFactory<>("personName"));
        tcsdDeptPersonEmail.setCellValueFactory(new PropertyValueFactory<>("personEmail"));
        tcsdDeptPersonPhone.setCellValueFactory(new PropertyValueFactory<>("personPhone"));
        tcsdDeptPersonAct.setCellFactory(param -> {
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
                        lstRemoveDept.add(lstDeptInfo.get(getIndex()).getId());
                        deptEdtId = 0;
                        deptEdtIdx = -1;
                        tbllstDeptInfo.remove(getIndex());
                        lstDeptInfo.remove(getIndex());
                        btnDeptPlus.requestFocus();
                    });
                    edtButton.setOnAction(event -> {
                        deptEdtIdx = getIndex();
                        deptEdtId = lstDeptInfo.get(getIndex()).getId();
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
        tblsdLedgerEditDept.setItems(tbllstDeptInfo);
    }

    private void addAndUpdateDeptInfo(DeptInfo deptInfo) {
        DeptInfoDTO deptInfoDTO = new DeptInfoDTO(deptInfo.getPersonDeptName(), deptInfo.getPersonName(), deptInfo.getPersonEmail(), deptInfo.getPersonPhone());
        if (deptEdtIdx >= 0) {
            tbllstDeptInfo.add(deptInfoDTO);
            lstDeptInfo.add(deptInfo);
            tbllstDeptInfo.remove(deptEdtIdx);
            lstDeptInfo.remove(deptEdtIdx);
            deptEdtIdx = -1;
            deptEdtId = 0;
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
        tcsdShippingAddress.setCellValueFactory(new PropertyValueFactory<>("shippingName"));
        tcsdShippingState.setCellValueFactory(new PropertyValueFactory<>("shippingStateName"));
        tcsdShippingAct.setCellFactory(param -> {
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
                        lstRemoveShipping.add(lstShippingInfo.get(getIndex()).getId());
                        shippingEdtId = 0;
                        shippingEdtIdx = -1;
                        lstShippingInfo.remove(getIndex());
                        btnShippingPlus.requestFocus();
                        tbllstShippingInfo.remove(getIndex());
                    });
                    edtButton.setOnAction(event -> {
                        shippingEdtIdx = getIndex();
                        shippingEdtId = lstShippingInfo.get(getIndex()).getId();
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
        tblsdLedgerEditShipping.setItems(tbllstShippingInfo);
    }

    private void addAndUpdateShippingInfo(ShippingInfo shippingInfo) {
        ShippingDTO shippingDTO = new ShippingDTO(shippingInfo.getShippingName(), shippingInfo.getShippingStateName(), shippingInfo.getShippingStateId());
        if (shippingEdtIdx >= 0) {
            tbllstShippingInfo.add(shippingDTO);
            lstShippingInfo.add(shippingInfo);
            lstShippingInfo.remove(shippingEdtIdx);
            tbllstShippingInfo.remove(shippingEdtIdx);
            shippingEdtIdx = -1;
            shippingEdtId = 0;
        } else {
            tbllstShippingInfo.add(shippingDTO);
            lstShippingInfo.add(shippingInfo);
        }
    }

    private void initApplicableFromCmb() {
        List<CommonOption> ApplicableFromList = Globals.getApplicableFromList();
        lstApplicableFrom = FXCollections.observableArrayList(ApplicableFromList);
        cmbsdLedgerEditApplicableFrom.getItems().addAll(lstApplicableFrom);
        cmbsdLedgerEditApplicableFrom.setConverter(new StringConverter<CommonOption>() {
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

//    private void initTradeRadioButtonGroup() {
//        rdsdDistributor.setToggleGroup(tgTrade);
//        rdsdManufacturer.setToggleGroup(tgTrade);
//        rdsdRetailer.setToggleGroup(tgTrade);
//    }


    private void onSubmit() {
        GlobalLedgerEditController globalLedgerEditController = GlobalLedgerEditController.getInstance();
        if (ValidateSDForm()) {
//            RadioButton selectedRdBtn = (RadioButton) tgTrade.getSelectedToggle();
            globalLedgerEditController.fnCallLedgerEditSDSubmit(tfsdLedgerEditRegisterdName.getText(), tfsdLedgerEditAdress.getText(),
                    cmbsdState.getSelectionModel().getSelectedItem() != null ? String.valueOf(cmbsdState.getSelectionModel().getSelectedItem().getId()) : "",
                    tfsdLedgerEditPin.getText(), tfsdLedgerEditPhone.getText(), tfsdLedgerEditWhatsapp.getText(), tfsdLedgerEditEmail.getText(),
                    tfsdLedgerEditRegNo.getText(),
//                    DateConvertUtil.convertDatetoAPIDateString(tfsdLedgerEditRegDate.getValue()),
                    tfsdLedgerEditRegDate.getText().trim().isEmpty() ? "" : Communicator.text_to_date.fromString(tfsdLedgerEditRegDate.getText()).toString(),
//                    selectedRdBtn.getText(),
//                    selectedTrade != null ? selectedTrade : "",
                    cbTrade.getSelectionModel().getSelectedItem(),
                    tfsdLedgerEditBusinessNature.getText(), chksdLedgerEditIsCredit.switchOnProperty().get(), tfsdLedgerEditCreditDays.getText(),
                    cmbsdLedgerEditApplicableFrom.getSelectionModel().getSelectedItem() != null ? cmbsdLedgerEditApplicableFrom.getSelectionModel().getSelectedItem().getName() : "",
                    tfsdLedgerEditCreditBills.getText(), tfsdLedgerEditCreditValue.getText(), tfscLedgerEditPANNo.getText(), chksdIsGST.switchOnProperty().get(),
                    lstGst, chksdIsLicense.switchOnProperty().get(), lstLicenseInfo, chksdIsBank.switchOnProperty().get(),
                    lstBankInfo, chksdIsShippingDetails.switchOnProperty().get(), lstShippingInfo, chksdIsDepartment.switchOnProperty().get(),
                    lstDeptInfo, chksdLedgerEditIsSalesMan.switchOnProperty().get(),
                    cmbsdLedgerEditSalesman.getSelectionModel().getSelectedItem() != null ? String.valueOf(cmbsdLedgerEditSalesman.getSelectionModel().getSelectedItem().getValue()) : "",
                    cmbsdLedgerEditArea.getSelectionModel().getSelectedItem() != null ? String.valueOf(cmbsdLedgerEditArea.getSelectionModel().getSelectedItem().getValue()) : "",
                    tfsdLedgerEditRoute.getText(), lstRemoveGst, lstRemoveLicense, lstRemoveDept, lstRemoveBank, lstRemoveShipping);
        }
    }

    private void initBtnSubmitAndCancel() {
        GlobalLedgerEditController globalLedgerEditController = GlobalLedgerEditController.getInstance();
        btnSdLedgerEditSubmit.setOnAction(actionEvent -> {
            onSubmit();
        });
        btnSdLedgerEditCancel.setOnAction(actionEvent -> {
            globalLedgerEditController.fcCallLedgerEditOtherCancel();
        });
    }

    private boolean ValidateSDForm() {
        if (tfsdLedgerEditRegisterdName.getText().trim().isEmpty()) {
            String msg = "Please Fill Required Fields !";
            AlertUtility.CustomCallback callback = number -> {
                tfsdLedgerEditRegisterdName.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        } else if (tfsdLedgerEditAdress.getText().trim().isEmpty()) {
            String msg = "Enter mandatory Address ";
            AlertUtility.CustomCallback callback = number -> {
                tfsdLedgerEditAdress.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        } else if (cmbsdState.getSelectionModel().getSelectedItem() == null) {
            String msg = "Select mandatory State";
            AlertUtility.CustomCallback callback = number -> {
                cmbsdState.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        } else if (tfsdLedgerEditPin.getText().trim().isEmpty()) {
            String msg = "Enter mandatory Pin Code ";
            AlertUtility.CustomCallback callback = number -> {
                tfsdLedgerEditPin.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        } else if (tfsdLedgerEditPhone.getText().trim().isEmpty()) {
            String msg = "Enter mandatory Phone Number ";
            AlertUtility.CustomCallback callback = number -> {
                tfsdLedgerEditPhone.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        } else if (tfsdLedgerEditWhatsapp.getText().trim().isEmpty()) {
            String msg = "Enter mandatory Whatsapp Number ";
            AlertUtility.CustomCallback callback = number -> {
                tfsdLedgerEditWhatsapp.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        } else if (tfsdLedgerEditBusinessNature.getText().trim().isEmpty()) {
            String msg = "Enter mandatory Buiseness Nature ";
            AlertUtility.CustomCallback callback = number -> {
                tfsdLedgerEditBusinessNature.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }


        if (tfsdLedgerEditPin.getText().length() < 6) {
            String msg = "Enter 6 Digit Pin Code";
            AlertUtility.CustomCallback callback = number -> {
                tfsdLedgerEditPin.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }

        if (tfsdLedgerEditPhone.getText().length() < 10) {
            String msg = "Enter 10 Digit Number";
            AlertUtility.CustomCallback callback = number -> {
                tfsdLedgerEditPhone.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }

        if (tfsdLedgerEditWhatsapp.getText().length() < 10) {
            String msg = "Enter 10 Digit Number For Whatsapp";
            AlertUtility.CustomCallback callback = number -> {
                tfsdLedgerEditWhatsapp.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }

        if (chksdIsGST.switchOnProperty().get() && lstGst.size() == 0) {
            String msg = "GST Number is mandatory !";
            AlertUtility.CustomCallback callback = number -> {
                btnLicensePlus.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }
        if (chksdIsGST.switchOnProperty().get() == false && tfscLedgerEditPANNo.getText().isEmpty() && tfscLedgerEditPANNo.getText().length() < 10) {
            String msg = "PAN Number is mandatory !";
            AlertUtility.CustomCallback callback = number -> {
                tfscLedgerEditPANNo.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }
        if (chksdIsLicense.switchOnProperty().get() && lstLicenseInfo.size() == 0) {
            String msg = "License Number is mandatory";
            AlertUtility.CustomCallback callback = number -> {
                btnLicensePlus.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }
        if (chksdIsDepartment.switchOnProperty().get() && lstDeptInfo.size() == 0) {
            String msg = "Department Details Are mandatory";
            AlertUtility.CustomCallback callback = number -> {
                btnDeptPlus.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }
        if (chksdIsBank.switchOnProperty().get() && lstBankInfo.size() == 0) {
            String msg = "Bank Details Should be entered";
            AlertUtility.CustomCallback callback = number -> {
                btnDeptPlus.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }
        if (chksdIsShippingDetails.switchOnProperty().get() && lstShippingInfo.size() == 0) {
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
//            AlertUtility.AlertError(AlertUtility.alertTypeError, msg, callback);
//            return false;
//        }
        return true;
    }

    public void setEditData(JSONObject object) {
        System.out.println("Edit Data +" + object);
        if (!object.isEmpty()) {
            tfsdLedgerEditRegisterdName.setText(object.getString("mailing_name"));
            tfsdLedgerEditAdress.setText(object.getString("address"));
            tfsdLedgerEditPin.setText(object.getString("pincode"));
            tfsdLedgerEditPhone.setText(object.getString("mobile_no"));
            tfsdLedgerEditWhatsapp.setText(object.getString("whatsapp_no"));
            if (object.has("pancard_no") && !object.getString("pancard_no").isEmpty()) {
                tfscLedgerEditPANNo.setText(object.getString("pancard_no"));
            }
            StateOption stOpt = lstIndiaState.stream().filter(stop -> stop.getId() == object.getInt("state")).findAny().orElse(null);
            if (stOpt != null) {
                cmbsdState.setValue(stOpt);
            }
            tfsdLedgerEditEmail.setText(object.getString("email"));
            tfsdLedgerEditRegNo.setText(object.getString("licenseNo") != null ? object.getString("licenseNo") : "");
//            tfsdLedgerEditRegDate.setValue(!object.getString("reg_date").isEmpty() ? DateConvertUtil.convertStringToLocalDate(object.getString("reg_date")) : null);
            String editDataReg_date = object.getString("reg_date");
            if (!editDataReg_date.isEmpty() && editDataReg_date != null && tfsdLedgerEditRegDate != null) {
                LocalDate applicable_date = LocalDate.parse(editDataReg_date);
                tfsdLedgerEditRegDate.setText(applicable_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            chksdLedgerEditIsCredit.switchOnProperty().set(object.getBoolean("isCredit"));
            if (object.getBoolean("isCredit") == true) {
                tfsdLedgerEditCreditDays.setText("" + object.getInt("credit_days"));
                if (!object.getString("applicable_from").isEmpty()) {
                    CommonOption applicableFrom = lstApplicableFrom.stream().filter(s -> s.getName().toLowerCase().equalsIgnoreCase(object.getString("applicable_from").toLowerCase())).findAny().orElse(null);
                    if (applicableFrom != null) {
                        cmbsdLedgerEditApplicableFrom.setValue(applicableFrom);
                    }
                }
                tfsdLedgerEditCreditBills.setText("" + object.getInt("creditNumBills"));
                tfsdLedgerEditCreditValue.setText("" + object.getInt("creditBillValue"));
            }

            tfsdLedgerEditBusinessNature.setText("" + object.getString("businessTrade"));
            if (!object.getString("businessType").isEmpty()) {
                String businessType = object.getString("businessType");
                List<String> allTrades = Globals.getAllTrades();
                allTrades.stream().filter((v) -> v.equalsIgnoreCase(businessType)).findAny().ifPresent((p) -> {
                    cbTrade.setValue(p);
                });
//                cbTrade.
//                switch (businessType.toLowerCase()) {
//                    case "distributor":
//                        tgTrade.selectToggle(rdsdDistributor);
//                        break;
//                    case "manufacturer":
//                        tgTrade.selectToggle(rdsdManufacturer);
//                        break;
//                    case "retailer":
//                        tgTrade.selectToggle(rdsdRetailer);
//                        break;
//                }
            }

            //?Gst list
            chksdIsGST.switchOnProperty().set(object.getBoolean("isGST"));
            if (object.getBoolean("isGST") == true) {
                JSONArray gstDetailsArr = object.getJSONArray("gstdetails");
                for (Object obj : gstDetailsArr) {
                    JSONObject nwGstDetail = new JSONObject(obj.toString());
//                    LocalDate dt = !nwGstDetail.getString("dateOfRegistration").isEmpty() ? DateConvertUtil.convertStringToLocalDate(nwGstDetail.getString("dateOfRegistration")) : null;
                    String dt = !nwGstDetail.getString("dateOfRegistration").isEmpty() ? DateConvertUtil.convertDispDateFormat(nwGstDetail.getString("dateOfRegistration")) : null;
                    Integer id = nwGstDetail.getInt("id");
                    String panNo = nwGstDetail.getString("pancard");
                    Integer regType = nwGstDetail.getInt("registraion_type");
                    String gstIn = nwGstDetail.getString("gstin");
                    ObservableList<GSTType> lstGstType = FXCollections.observableArrayList(Globals.getGstTypeList());
                    GSTType regTypeOpt = lstGstType.stream().filter(s -> s.getValue() == regType).findAny().orElse(null);
                    GstList gstList = new GstList(id, regTypeOpt.getValue(), regTypeOpt.getLabel(), dt, gstIn, panNo);
                    GstListDTO gstListDTO = new GstListDTO(id, regTypeOpt.getValue(), regTypeOpt.getLabel(), dt, gstIn, panNo);
                    lstGst.add(gstList);
                    tbllstGst.add(gstListDTO);
                }
            }

            //?License
            chksdIsLicense.switchOnProperty().set(object.getBoolean("isLicense"));
            if (object.getBoolean("isLicense") == true) {
                JSONArray licenseArr = object.getJSONArray("licensesDetails");
                for (Object o : licenseArr) {
                    JSONObject licenseObj = new JSONObject(o.toString());
                    LocalDate expDate = !licenseObj.getString("licenses_exp").isEmpty() ? DateConvertUtil.convertStringToLocalDate(licenseObj.getString("licenses_exp")) : null;
                    String tfexpDate = !licenseObj.getString("licenses_exp").isEmpty() ? DateConvertUtil.convertDispDateFormat(licenseObj.getString("licenses_exp")) : null;
                    JSONObject licenseTypeObj = licenseObj.getJSONObject("licences_type");
                    ObservableList<LicenseType> lstLicenseType = FXCollections.observableArrayList(Globals.getLicenseTypeList());
                    LicenseType licenseType = lstLicenseType.stream().filter(s -> s.getSlugName().equalsIgnoreCase(licenseTypeObj.getString("slug_name"))).findAny().orElse(null);
                    String licenseNo = licenseObj.getString("licenses_num");
                    Integer lid = licenseObj.getInt("id");
                    LicenseInfo licenseInfo = new LicenseInfo(lid, licenseType.getLabel(), licenseType.getSlugName(), licenseType.getValue(), licenseNo, tfexpDate, "");
                    LicenseDetailDTO licenseDetailDTO = new LicenseDetailDTO(licenseNo, licenseType.getLabel(), DateConvertUtil.convertLocalDatetoString(expDate), "");
                    lstLicenseInfo.add(licenseInfo);
                    tbllstLicenseInfo.add(licenseDetailDTO);
                }
            }

            //?Dept
            chksdIsDepartment.switchOnProperty().set(object.getBoolean("isDepartment"));
            if (object.getBoolean("isDepartment") == true) {
                JSONArray deptDetailsArr = object.getJSONArray("deptDetails");
                for (Object obj : deptDetailsArr) {
                    JSONObject nwDeptObj = new JSONObject(obj.toString());
                    Integer id = nwDeptObj.getInt("id");
                    String personName = nwDeptObj.getString("contact_person");
                    String personContact = nwDeptObj.getString("contact_no");
                    String personEmail = nwDeptObj.getString("email");
                    String personDeptName = nwDeptObj.getString("dept");
                    DeptInfoDTO deptInfoDTO = new DeptInfoDTO(personDeptName, personName, personEmail, personContact);
                    DeptInfo deptInfo = new DeptInfo(id, personDeptName, personName, personEmail, personContact);
                    tbllstDeptInfo.add(deptInfoDTO);
                    lstDeptInfo.add(deptInfo);
                }
            }
        }

        //?Bank Details
        chksdIsBank.switchOnProperty().set(object.getBoolean("isBankDetails"));
        if (object.getBoolean("isBankDetails") == true) {
            JSONArray bankDetailsArr = object.getJSONArray("bankDetails");
            for (Object bankObj : bankDetailsArr) {
                JSONObject bankDetailObj = new JSONObject(bankObj.toString());
                Integer bid = bankDetailObj.getInt("id");
                String bankName = bankDetailObj.getString("bank_name");
                String bankBranch = bankDetailObj.getString("bank_branch");
                String bankAccNo = bankDetailObj.getString("bank_account_no");
                String bankIfscCode = bankDetailObj.getString("bank_ifsc_code");
                BankDetailList bankDetailList = new BankDetailList(bid, bankName, bankAccNo, bankIfscCode, bankBranch);
                BankDetailsDTO bankDetailsDTO = new BankDetailsDTO(bid, bankName, bankAccNo, bankIfscCode, bankBranch);
                tbllstBankInfo.add(bankDetailsDTO);
                lstBankInfo.add(bankDetailList);
            }
        }

        //?Shipping Details
        chksdIsShippingDetails.switchOnProperty().set(object.getBoolean("isShippingDetails"));
        if (object.getBoolean("isShippingDetails") == true) {
            System.out.println("isShippingDetails" + object.getBoolean("isShippingDetails"));
            JSONArray shippingArr = object.getJSONArray("shippingDetails");
            System.out.println("ShippingArr" + shippingArr);
            for (Object shippingObj : shippingArr) {
                System.out.println("shippingObj" + shippingObj.toString());
                JSONObject shippingDetailObj = new JSONObject(shippingObj.toString());
                System.out.println("shippingOBj" + shippingDetailObj);
                Integer id = shippingDetailObj.getInt("id");
                String shippingAddress = shippingDetailObj.getString("shipping_address");
                String shippingState = shippingDetailObj.getString("district");
                ObservableList<StateOption> lstIndiaState = FXCollections.observableArrayList(Globals.getIndiaStateList());
                StateOption stOp = lstIndiaState.stream().filter(s -> s.getId() == Integer.valueOf(shippingState)).findAny().orElse(null);
                ShippingInfo shippingInfo = new ShippingInfo(id, shippingAddress, stOp.getId(), stOp.getStateName(), stOp.getStateCode());
                ShippingDTO shippingDTO = new ShippingDTO(shippingAddress, stOp.getStateName(), stOp.getId());
                tbllstShippingInfo.add(shippingDTO);
                lstShippingInfo.add(shippingInfo);
            }
        }
        //? Sales man Info
        if (object.has("salesmanId") && !object.getString("salesmanId").isEmpty()) {
            chksdLedgerEditIsSalesMan.switchOnProperty().set(true);
            CommonOption salesManOp = lstSalesMan.stream().filter(s -> s.getValue() == object.getInt("salesmanId")).findAny().orElse(null);
            CommonOption areaOp = null;
            if (object.has("area") && !object.getString("area").isEmpty()) {
                areaOp = lstAreaMst.stream().filter(s -> s.getValue() == object.getInt("area")).findAny().orElse(null);
            }
            cmbsdLedgerEditSalesman.setValue(salesManOp);
            cmbsdLedgerEditArea.setValue(areaOp);
            tfsdLedgerEditRoute.setText(object.getString("route"));
        }
    }
}
