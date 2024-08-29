package com.opethic.genivis.controller.master.ledger.create;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.commons.SwitchButton;
import com.opethic.genivis.controller.master.ledger.common.LedgerCommonController;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.dto.FranchiseDTO;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LedgerCreateSDController implements Initializable {

    @FXML
    private SwitchButton chksdLedgerCreateIsSalesMan;

    @FXML
    private HBox ledgersdMainDivider, topThirdRow, topSecondRow, topFirstRow, sdInforRowFirst, sdInforRowSecond, sdInforRowThird;
    @FXML
    private VBox ledgersdMainDividerFirst, ledgersdMainDividerSecond, leftRowFirstInfo, rightRowFirstInfo, leftRowSecondInfo, rightRowSecondInfo, leftRowThirdInfo, rightRowThirdInfo;

    @FXML
    private SwitchButton chksdLedgerCreateIsCredit;
    @FXML
    private SwitchButton chksdIsGST, chksdIsBank, chksdIsLicense, chksdIsDepartment, chksdIsShippingDetails;

    @FXML
    private VBox vboxchksdLedgerCreateIsCredit, vboxchksdIsGST, vboxchksdIsLicense, vboxchksdIsDepartment, vboxchksdIsBank, vboxchksdIsShippingDetails, vboxchksdLedgerCreateIsSalesMan;

    @FXML
    private ComboBox<CommonOption> cmbsdLedgerCreateApplicableFrom;

    @FXML
    private ComboBox<CommonOption> cmbsdLedgerCreateArea;

    @FXML
    private ComboBox<CommonOption> cmbsdLedgerCreateSalesman;

    @FXML
    private ComboBox<StateOption> cmbsdState;

    @FXML
    private Label lblscBusinessNature;

    @FXML
    private Label lblscLedgerCreateTrade;

    @FXML
    private RadioButton rdsdDistributor;

    @FXML
    private RadioButton rdsdManufacturer;

    @FXML
    private RadioButton rdsdRetailer;

    //    private final ToggleGroup tgTrade = new ToggleGroup();
    @FXML
    private BorderPane stpanesc;
    @FXML
    private TextField tfsdLedgerCreateBusinessNature;

    @FXML
    private TextField tfsdLedgerCreateCreditBills;

    @FXML
    private TextField tfsdLedgerCreateCreditDays;

    @FXML
    private TextField tfsdLedgerCreateCreditValue;

    @FXML
    private TextField tfsdLedgerCreateAdress;

    @FXML
    private TextField tfsdLedgerCreateBankAccNo;

    @FXML
    private TextField tfsdLedgerCreateBankBranch;

    @FXML
    private TextField tfsdLedgerCreateBankINFSC;

    @FXML
    private TextField tfsdLedgerCreateBankName;

    @FXML
    private TextField tfsdLedgerCreateEmail;


    @FXML
    private TextField tfsdLedgerCreatePhone;

    @FXML
    private TextField tfsdLedgerCreatePin;

    //    @FXML
//    private DatePicker tfsdLedgerCreateRegDate;
    @FXML
    private TextField tfsdLedgerCreateRegDate;
    @FXML
    private TextField tfsdLedgerCreateRegNo;

    @FXML
    private TextField tfsdLedgerCreateRegisterdName;

    @FXML
    private TextField tfsdLedgerCreateRoute;

    @FXML
    private TextField tfsdLedgerCreateWhatsapp;

    @FXML
    private Button btnSdLedgerCreateSubmit, btnSdLedgerCreateCancel;

    //?GST Table Related Fields
    @FXML
    private Button btnGstPlus;
    //    @FXML
//    private ComboBox<GSTType> cmbsdLedgerCreateGSTType;
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

    //?GST Table Related Fields
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
    private Button btnLicensePlus;

    @FXML
    private TableView<LicenseDetailDTO> tblsdLedgerCreateLicenses;
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
    private TableView<DeptInfoDTO> tblsdLedgerCreateDept;
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
    private Button btnShippingPlus;
    @FXML
    private TextField tfsdLedgerCreateShippingAddress;
    //    @FXML
//    private ComboBox<StateOption> cmbsdLedgerCreateShippingState;
    @FXML
    private TableView<ShippingDTO> tblsdLedgerCreateShipping;
    @FXML
    private TableColumn<ShippingDTO, Void> tcsdShippingAct;

    @FXML
    private TableColumn<?, ?> tcsdShippingAddress;

    @FXML
    private TableColumn<?, ?> tcsdShippingState;

    @FXML
    private ComboBox<String> cbTrade;
    String selectedTrade = "";

    //    @FXML
//    private Label lblLedgerEditPANNo;
    @FXML
    private HBox tfLedgerEditPANNo;
    @FXML
    private TextField tfscLedgerEditPANNo;
    @FXML
    private Label lblBank, lblGST, lblLicense, lblDepartment, lblShipping, lblSalesman;
    //    @FXML
//    private Label requiredID;
    @FXML
    private Text requiredID;
    String lblStyleCSS = "-fx-font-weight: bold; -fx-text-fill: #00a0f5;";
    private JsonObject jsonObject = null;
    private ObservableList<CommonOption> lstSalesMan = FXCollections.observableArrayList();
    private ObservableList<CommonOption> lstAreaMst = FXCollections.observableArrayList();
    private ObservableList<StateOption> lstIndiaState = FXCollections.observableArrayList();

    private ObservableList<GstListDTO> tbllstGst = FXCollections.observableArrayList();
    private List<GstList> lstGst = new ArrayList<>();
    private ObservableList<GSTType> lstGstType = FXCollections.observableArrayList();
    private int edtIdx = -1;

    private ObservableList<BankDetailsDTO> tbllstBankInfo = FXCollections.observableArrayList();
    private List<BankDetailList> lstBankInfo = new ArrayList<>();
    private int bankEdtIdx = -1;

    private ObservableList<LicenseDetailDTO> tbllstLicenseInfo = FXCollections.observableArrayList();
    private List<LicenseInfo> lstLicenseInfo = new ArrayList<>();
    private int licenseEdtIdx = -1;

    private ObservableList<DeptInfoDTO> tbllstDeptInfo = FXCollections.observableArrayList();
    private List<DeptInfo> lstDeptInfo = new ArrayList<>();
    private int deptEdtIdx = -1;
    private ObservableList<ShippingDTO> tbllstShippingInfo = FXCollections.observableArrayList();
    private List<ShippingInfo> lstShippingInfo = new ArrayList<>();
    private int shippingEdtIdx = -1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();
        //For Divide the two info tables same width
        leftRowFirstInfo.prefWidthProperty().bind(sdInforRowFirst.widthProperty().multiply(0.5));
        rightRowFirstInfo.prefWidthProperty().bind(sdInforRowFirst.widthProperty().multiply(0.5));

        leftRowSecondInfo.prefWidthProperty().bind(sdInforRowSecond.widthProperty().multiply(0.5));
        rightRowSecondInfo.prefWidthProperty().bind(sdInforRowSecond.widthProperty().multiply(0.5));

        leftRowThirdInfo.prefWidthProperty().bind(sdInforRowThird.widthProperty().multiply(0.5));
        rightRowThirdInfo.prefWidthProperty().bind(sdInforRowThird.widthProperty().multiply(0.5));
        // End

//        Platform.runLater(() -> {
//            tfsdLedgerCreateRegisterdName.requestFocus();
//        });

        CommonFunctionalUtils.restrictTextFieldToDigitsAndBirthDateFormat(tfsdLedgerCreateRegDate);

        // open Filter dropdown on Space
        cmbsdState.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cmbsdState.isShowing()) {
                cmbsdState.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });
        cmbsdLedgerCreateApplicableFrom.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cmbsdLedgerCreateApplicableFrom.isShowing()) {
                cmbsdLedgerCreateApplicableFrom.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });
        cmbsdLedgerCreateSalesman.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cmbsdLedgerCreateSalesman.isShowing()) {
                cmbsdLedgerCreateSalesman.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });
        cmbsdLedgerCreateArea.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cmbsdLedgerCreateArea.isShowing()) {
                cmbsdLedgerCreateArea.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });

        cbTrade.getItems().addAll(Globals.getAllTrades());
        cbTrade.setPromptText("Select Trade");
        cbTrade.setOnAction(this::handleTradeComboBoxAction);
        // open trade dropdown on Space
        cbTrade.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cbTrade.isShowing()) {
                cbTrade.show();
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                tfsdLedgerCreateBusinessNature.requestFocus();
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
        initStateCmb();
        setupMobileNumberValidation();
        setupPincodeValidation();
        setupPanNoValidation();

        btnSdLedgerCreateSubmit.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                Platform.runLater(() -> {
                    btnSdLedgerCreateCancel.requestFocus();
                });
            }
            event.consume();
        });
        tfsdLedgerCreatePhone.textProperty().addListener((observable, oldValue, newValue) -> {
            //TODO: Update the text property of whatsapp when the value changes in mobileNu,
            tfsdLedgerCreateWhatsapp.setText(newValue);
        });

        // Add listener to only numbers input
        addNumericListener(tfsdLedgerCreateCreditDays);
        addNumericListener(tfsdLedgerCreateCreditBills);
        addNumericListener(tfsdLedgerCreateCreditValue);


        // Add listener to convert text to uppercase
        tfscLedgerEditPANNo.textProperty().addListener((observable, oldValue, newValue) -> {
            tfscLedgerEditPANNo.setText(newValue.toUpperCase());
        });

        //code for pin code validation
        addPincodeValidationListener(tfsdLedgerCreatePin);
        //code for email validation
        addEmailValidationListener(tfsdLedgerCreateEmail);
        // pan no. validation
        addPANNoValidationListener(tfscLedgerEditPANNo);
        // phone no. validation
        addPhoneValidationListener(tfsdLedgerCreatePhone);
        // whatsapp no. validation
        addWhatsappValidationListener(tfsdLedgerCreateWhatsapp);

        DateValidator.applyDateFormat(tfsdLedgerCreateRegDate);
        CommonFunctionalUtils.restrictDateFormat(tfsdLedgerCreateRegDate);

        regDateValidation();


//        tfsdLedgerCreateRegisterdName.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfsdLedgerCreateAdress.setOnKeyPressed(this::handleEnterKeyPressed);
//        cmbsdState.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfsdLedgerCreatePin.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfsdLedgerCreatePhone.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfsdLedgerCreateWhatsapp.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfsdLedgerCreateEmail.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfsdLedgerCreateRegNo.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfsdLedgerCreateRegDate.setOnKeyPressed(this::handleEnterKeyPressed);
//        chksdLedgerCreateIsCredit.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfsdLedgerCreateCreditDays.setOnKeyPressed(this::handleEnterKeyPressed);
//        cmbsdLedgerCreateApplicableFrom.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfsdLedgerCreateCreditBills.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfsdLedgerCreateCreditValue.setOnKeyPressed(this::handleEnterKeyPressed);
//        rdsdRetailer.setOnKeyPressed(this::handleEnterKeyPressed);
//        rdsdManufacturer.setOnKeyPressed(this::handleEnterKeyPressed);
//        rdsdDistributor.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfsdLedgerCreateBusinessNature.setOnKeyPressed(this::handleEnterKeyPressed);
//        chksdIsGST.setOnKeyPressed(this::handleEnterKeyPressed);

        initialEnterMethod();
//        tfFocused(tfsdLedgerCreatePhone);
        tfFocused(tfsdLedgerCreateWhatsapp);
        tfFocused(tfsdLedgerCreateRegisterdName);
        tfFocused(tfsdLedgerCreateAdress);
        cmbFocused(cmbsdState);
        cmbFocused(cbTrade);
        tfFocused(tfsdLedgerCreateBusinessNature);
        tfFocused(tfscLedgerEditPANNo);

        btnSdLedgerCreateSubmit.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                btnSdLedgerCreateCancel.requestFocus();
            }
        });
    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            topFirstRow.setSpacing(5);
            topSecondRow.setSpacing(5);
            topThirdRow.setSpacing(5);
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            topFirstRow.setSpacing(6);
            topSecondRow.setSpacing(6);
            topThirdRow.setSpacing(6);
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            topFirstRow.setSpacing(8);
            topSecondRow.setSpacing(8);
            topThirdRow.setSpacing(8);
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            topFirstRow.setSpacing(8);
            topSecondRow.setSpacing(8);
            topThirdRow.setSpacing(8);
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            topFirstRow.setSpacing(8);
            topSecondRow.setSpacing(8);
            topThirdRow.setSpacing(8);
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_7.css").toExternalForm());
        }
    }

    private void regDateValidation() {
        tfsdLedgerCreateRegDate.focusedProperty().addListener((obs, old, nw) -> {
            System.out.println("nw" + nw);
            if (!nw) {
                String regDateStr = tfsdLedgerCreateRegDate.getText();
                if (!regDateStr.isEmpty()) {
                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//                String formattedCurrentDate = currentDate.format(formatter);
                    LocalDate regDate = LocalDate.parse(regDateStr, formatter);
//                System.out.println("register date --> " + regDateStr);
//                System.out.println("currentDate --> " + formattedCurrentDate);
                    if (regDate.isAfter(currentDate)) {
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Register date cannot be greater than the current date. ", input -> {
                            tfsdLedgerCreateRegDate.requestFocus();
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

    private void addWhatsappValidationListener(TextField tfsdLedgerCreateWhatsapp) {
        tfsdLedgerCreateWhatsapp.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                event.consume();
                if (!isValidMobileNumber(tfsdLedgerCreateWhatsapp.getText()) && !tfsdLedgerCreateWhatsapp.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered Whatsapp number is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Whatsapp Number", newEvent -> {
                        tfsdLedgerCreateWhatsapp.requestFocus();
                    });
                    event.consume();
                }
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                if (!isValidMobileNumber(tfsdLedgerCreateWhatsapp.getText()) && !tfsdLedgerCreateWhatsapp.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered Whatsapp number is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Whatsapp Number", newEvent -> {
                        tfsdLedgerCreateWhatsapp.requestFocus();
                    });
                    event.consume();
                }
            }
        });
    }

    private void addPhoneValidationListener(TextField tfsdLedgerCreatePhone) {
        tfsdLedgerCreatePhone.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                event.consume();
                if (!isValidMobileNumber(tfsdLedgerCreatePhone.getText()) && !tfsdLedgerCreatePhone.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered Phone number is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Phone Number", newEvent -> {
                        tfsdLedgerCreatePhone.requestFocus();
                    });
                    event.consume();
                }
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                if (!isValidMobileNumber(tfsdLedgerCreatePhone.getText()) && !tfsdLedgerCreatePhone.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered Phone number is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Phone Number", newEvent -> {
                        tfsdLedgerCreatePhone.requestFocus();
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

    private void addEmailValidationListener(TextField tfsdLedgerCreateEmail) {
        tfsdLedgerCreateEmail.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                event.consume();
                if (!validEmail(tfsdLedgerCreateEmail.getText()) && !tfsdLedgerCreateEmail.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered email is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Email Id", newEvent -> {
                        tfsdLedgerCreateEmail.requestFocus();
                    });
                    event.consume();
                } else {
                    tfsdLedgerCreateWhatsapp.requestFocus();
                }
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {

                if (!validEmail(tfsdLedgerCreateEmail.getText()) && !tfsdLedgerCreateEmail.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered email is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Email Id", newEvent -> {
                        tfsdLedgerCreateEmail.requestFocus();
                    });
                    event.consume();
                } else tfsdLedgerCreateRegNo.requestFocus();
            }
        });
    }

    private void addPincodeValidationListener(TextField tfsdLedgerCreatePin) {
        tfsdLedgerCreatePin.focusedProperty().addListener((obs, old, nw) -> {
            if (!nw) {
                if (!tfsdLedgerCreatePin.getText().isEmpty()) {
                    validatePincode();
                }
                if (tfsdLedgerCreatePin.getText().trim().isEmpty()) {
                    tfsdLedgerCreatePin.requestFocus();
                }
                String newVal = tfsdLedgerCreatePin.getText().trim();
                if (newVal.length() > 6) {
                    tfsdLedgerCreatePin.setText(newVal.substring(0, 6));
//                    validatePincode();
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
        System.out.println("i am in");
        APIClient apiClient = null;
        try {
//            logger.debug("Get ValidatePincode Data Started...");
            Map<String, String> map = new HashMap<>();
            map.put("pincode", String.valueOf(tfsdLedgerCreatePin.getText()));
            String formData = Globals.mapToStringforFormData(map);
//            System.out.println("formData--------> " + formData);
            apiClient = new APIClient(EndPoints.VALIDATE_PINCODE_ENDPOINT, formData, RequestType.FORM_DATA);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        System.out.println("200 --------- ");
                        tfFocused(tfsdLedgerCreatePhone);
                    } else {
                        System.out.println("404 --------- ");
                        String message = jsonObject.get("message").getAsString();
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Pin Code", newEvent -> {
//                            tfsdLedgerCreatePin.requestFocus();
                            GlobalTranx.requestFocusOrDieTrying(tfsdLedgerCreatePin, 3);
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
//        System.out.println("Selected Trade--------> : " + selectedTrade);
       /* if (cbTrade.getSelectionModel().getSelectedItem() == "Retailer") {
            selectedTrade = "retailer";
//            System.out.println("selectedType- " + selectedTrade);
        } else if (cbTrade.getSelectionModel().getSelectedItem() == "Distributor") {
            selectedTrade = "distributor";
//            System.out.println("selectedType- " + selectedTrade);
        } else if (cbTrade.getSelectionModel().getSelectedItem() == "Manufacturer") {
            selectedTrade = "manufacturer";
//            System.out.println("selectedType- " + selectedTrade);
        }*/
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

        tfsdLedgerCreatePin.setTextFormatter(pincodeFormatter);
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
            // Allow change if it results in digits only and does not exceed 10 digits
            if (change.getControlNewText().matches("\\d{0,10}")) {
                return change;
            }
            // Reject the change if it doesn't match the required pattern
            return null;
        });

        tfsdLedgerCreateWhatsapp.setTextFormatter(whatsappNumberFormatter);
        TextFormatter<String> mobileNumberFormatter = new TextFormatter<>(change -> {
            // Allow change if it results in digits only and does not exceed 10 digits
            if (change.getControlNewText().matches("\\d{0,10}")) {
                return change;
            }
            // Reject the change if it doesn't match the required pattern
            return null;
        });

        tfsdLedgerCreatePhone.setTextFormatter(mobileNumberFormatter);

    }

    private void initAllSwitchButtons() {
        chksdLedgerCreateIsCredit.setParentBox(vboxchksdLedgerCreateIsCredit);
        initCreditDaysDisable(false);
        chksdLedgerCreateIsCredit.switchOnProperty().addListener((obv, old, nv) -> {
            initCreditDaysDisable(nv);
        });
        chksdIsGST.setParentBox(vboxchksdIsGST);
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

        chksdIsLicense.setParentBox(vboxchksdIsLicense);
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
        chksdIsDepartment.setParentBox(vboxchksdIsDepartment);
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
        chksdIsBank.setParentBox(vboxchksdIsBank);
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
        chksdIsShippingDetails.setParentBox(vboxchksdIsShippingDetails);
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

        chksdLedgerCreateIsSalesMan.setParentBox(vboxchksdLedgerCreateIsSalesMan);
        initSalesmanActivate(false);

        chksdLedgerCreateIsSalesMan.switchOnProperty().addListener((obv, old, nv) -> {
            initSalesmanActivate(nv);
        });
    }

    private void initSalesmanActivate(boolean b) {
        cmbsdLedgerCreateSalesman.setDisable(!b);
        cmbsdLedgerCreateArea.setDisable(!b);
        tfsdLedgerCreateRoute.setDisable(!b);
//        if (b) {
//            Platform.runLater(() -> {
//                cmbsdLedgerCreateSalesman.requestFocus();
//            });
//        } else {
//            Platform.runLater(() -> {
//                btnSdLedgerCreateSubmit.requestFocus();
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
//                chksdLedgerCreateIsSalesMan.requestFocus();
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

//    private void handleEnterKeyPressed(KeyEvent keyEvent) {
//        Node source = (Node) keyEvent.getSource();
//        if (keyEvent.getCode() == KeyCode.ENTER) {
//            switch (source.getId()) {
//                case "tfsdLedgerCreateRegisterdName":
//                    Platform.runLater(() -> {
//                        tfsdLedgerCreateAdress.requestFocus();
//                    });
//                    break;
//                case "tfsdLedgerCreateAdress":
//                    Platform.runLater(() -> {
//                        cmbsdState.requestFocus();
//                    });
//                    break;
//                case "cmbsdState":
//                    Platform.runLater(() -> {
//                        tfsdLedgerCreatePin.requestFocus();
//                    });
//                    break;
//                case "tfsdLedgerCreatePin":
//                    Platform.runLater(() -> {
//                        tfsdLedgerCreatePhone.requestFocus();
//                    });
//                    break;
//                case "tfsdLedgerCreatePhone":
//                    Platform.runLater(() -> {
//                        tfsdLedgerCreateWhatsapp.requestFocus();
//                    });
//                    break;
//                case "tfsdLedgerCreateWhatsapp":
//                    Platform.runLater(() -> {
//                        tfsdLedgerCreateEmail.requestFocus();
//                    });
//                    break;
//                case "tfsdLedgerCreateEmail":
//                    System.out.println("Email");
//                    Platform.runLater(() -> {
//                        tfsdLedgerCreateRegNo.requestFocus();
//                    });
//                    break;
//                case "tfsdLedgerCreateRegNo":
//                    Platform.runLater(() -> {
//                        tfsdLedgerCreateRegDate.requestFocus();
//                    });
//                    break;
//                case "tfsdLedgerCreateRegDate":
//                    Platform.runLater(() -> {
//                        chksdLedgerCreateIsCredit.requestFocus();
//                    });
//                case "chksdLedgerCreateIsCredit":
//                    if (chksdLedgerCreateIsCredit.switchOnProperty().get()) {
//                        Platform.runLater(() -> {
//                            tfsdLedgerCreateCreditDays.requestFocus();
//                        });
//                    } else {
//                        Platform.runLater(() -> {
//                            rdsdRetailer.requestFocus();
//                        });
//                    }
//                    break;
//                case "tfsdLedgerCreateCreditDays":
//                    Platform.runLater(() -> {
//                        cmbsdLedgerCreateApplicableFrom.requestFocus();
//                    });
//                    break;
//                case "cmbsdLedgerCreateApplicableFrom":
//                    Platform.runLater(() -> {
//                        tfsdLedgerCreateCreditBills.requestFocus();
//                    });
//                    break;
//                case "tfsdLedgerCreateCreditBills":
//                    Platform.runLater(() -> {
//                        tfsdLedgerCreateCreditValue.requestFocus();
//                    });
//                    break;
//                case "tfsdLedgerCreateCreditValue":
//                    Platform.runLater(() -> {
//                        rdsdRetailer.requestFocus();
//                    });
//                    break;
//                case "rdsdRetailer":
//                    Platform.runLater(() -> {
//                        rdsdManufacturer.requestFocus();
//                    });
//                    break;
//                case "rdsdManufacturer":
//                    Platform.runLater(() -> {
//                        rdsdDistributor.requestFocus();
//                    });
//                    break;
//                case "rdsdDistributor":
//                    Platform.runLater(() -> {
//                        tfsdLedgerCreateBusinessNature.requestFocus();
//                    });
//                    break;
//                case "tfsdLedgerCreateBusinessNature":
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
//            btnSdLedgerCreateCancel.fire();
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
                return lstIndiaState.stream().filter(state -> state.getStateName().equals(string)).findFirst().orElse(null);
            }

        });

        AutoCompleteBox autoCompleteBox = new AutoCompleteBox(cmbsdState, initialIndex);
    }


    private void initCreditDaysDisable(Boolean nv) {
        tfsdLedgerCreateCreditDays.setDisable(!nv);
        tfsdLedgerCreateCreditBills.setDisable(!nv);
        tfsdLedgerCreateCreditValue.setDisable(!nv);
        cmbsdLedgerCreateApplicableFrom.setDisable(!nv);
//        if (nv) {
//            Platform.runLater(() -> {
//                tfsdLedgerCreateCreditDays.requestFocus();
//            });
//        } else {
////            Platform.runLater(() -> {
////                rdsdRetailer.requestFocus();
////            });
//        }
    }


    private void initAreaCmb() {
        List<CommonOption> areaMstList = getAreaAPICallList();
        lstAreaMst = FXCollections.observableArrayList(areaMstList);
        cmbsdLedgerCreateArea.getItems().addAll(lstAreaMst);
        cmbsdLedgerCreateArea.setConverter(new StringConverter<CommonOption>() {
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
        HttpResponse<String> response = APIClient.getRequest(EndPoints.getOutletAreaMaster);
        JSONObject res = new JSONObject(response.body());
        if (res.getInt("responseStatus") == 200) {
            JSONArray jsonArray = res.getJSONArray("responseObject");
            if (jsonArray.length() > 0) {
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
        return lstOpt;
    }

    private void initSalesManCmb() {
        List<CommonOption> salesManList = getSalesManAPICallList();
        lstSalesMan = FXCollections.observableArrayList(salesManList);
        cmbsdLedgerCreateSalesman.getItems().addAll(lstSalesMan);
        cmbsdLedgerCreateSalesman.setConverter(new StringConverter<CommonOption>() {
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
        HttpResponse<String> response = APIClient.getRequest(EndPoints.getOutletSalesmanMaster);
        JSONObject res = new JSONObject(response.body());
        if (res.getInt("responseStatus") == 200) {
            JSONArray jsonArray = res.getJSONArray("responseObject");
            if (jsonArray.length() > 0) {
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

        tblsdGstInfo.setItems(tbllstGst);

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
        tcsdBankAccNo.setCellValueFactory(new PropertyValueFactory<>("bankAccNo"));
        tcsdBankName.setCellValueFactory(new PropertyValueFactory<>("bankName"));
        tcsdBankBranch.setCellValueFactory(new PropertyValueFactory<>("bankBranch"));
        tcsdBankIFSCCode.setCellValueFactory(new PropertyValueFactory<>("bankIFSCCode"));
        tcsdBankAct.setCellFactory(param -> {
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
        tblsdLedgerCreateLicenses.setItems(tbllstLicenseInfo);
    }

    private void addAndUpdateLicenseInfo(LicenseInfo licenseInfo) {
        LicenseDetailDTO licenseDetailDTO = new LicenseDetailDTO(licenseInfo.getLicenseNo(), licenseInfo.getLicenseTypeName(), licenseInfo.getLicenseExp() != null ? DateConvertUtil.convertDispDateFormat(licenseInfo.getLicenseExp()) : null, "");
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
        tblsdLedgerCreateDept.setItems(tbllstDeptInfo);
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
        List<StateOption> IndiaStateList = Globals.getIndiaStateList();
        lstIndiaState = FXCollections.observableArrayList(IndiaStateList);
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
        tblsdLedgerCreateShipping.setItems(tbllstShippingInfo);
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


    private void initApplicableFromCmb() {
        List<CommonOption> ApplicableFromList = Globals.getApplicableFromList();
        ObservableList<CommonOption> lstApplicableFrom = FXCollections.observableArrayList(ApplicableFromList);
        cmbsdLedgerCreateApplicableFrom.getItems().addAll(lstApplicableFrom);
        cmbsdLedgerCreateApplicableFrom.setConverter(new StringConverter<CommonOption>() {
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
        GlobalLedgerCreateController globalLedgerCreateController = GlobalLedgerCreateController.getInstance();
        if (ValidateSDForm()) {
//            RadioButton selectedRdBtn = (RadioButton) tgTrade.getSelectedToggle();
            globalLedgerCreateController.fnCallLedgerCreateSDSubmit(tfsdLedgerCreateRegisterdName.getText(), tfsdLedgerCreateAdress.getText(), cmbsdState.getSelectionModel().getSelectedItem() != null ? String.valueOf(cmbsdState.getSelectionModel().getSelectedItem().getId()) : "", tfsdLedgerCreatePin.getText(), tfsdLedgerCreatePhone.getText(), tfsdLedgerCreateWhatsapp.getText(), tfsdLedgerCreateEmail.getText(), tfsdLedgerCreateRegNo.getText(),
//                    DateConvertUtil.convertDatetoAPIDateString(tfsdLedgerCreateRegDate.getValue()),
//                    Communicator.text_to_date.fromString(tfsdLedgerCreateRegDate.getText()).toString(),
                    tfsdLedgerCreateRegDate.getText().isEmpty() ? "" : Communicator.text_to_date.fromString(tfsdLedgerCreateRegDate.getText()).toString(),
//                    selectedRdBtn.getText(),
//                    selectedTrade != null ? selectedTrade : "",
                    cbTrade.getSelectionModel().getSelectedItem() != null ? cbTrade.getSelectionModel().getSelectedItem().toLowerCase() : "", tfsdLedgerCreateBusinessNature.getText(), chksdLedgerCreateIsCredit.switchOnProperty().get(), tfsdLedgerCreateCreditDays.getText(), cmbsdLedgerCreateApplicableFrom.getSelectionModel().getSelectedItem() != null ? cmbsdLedgerCreateApplicableFrom.getSelectionModel().getSelectedItem().getName() : "", tfsdLedgerCreateCreditBills.getText(), tfsdLedgerCreateCreditValue.getText(), tfscLedgerEditPANNo.getText(), chksdIsGST.switchOnProperty().get(), lstGst, chksdIsLicense.switchOnProperty().get(), lstLicenseInfo, chksdIsBank.switchOnProperty().get(), lstBankInfo, chksdIsShippingDetails.switchOnProperty().get(), lstShippingInfo, chksdIsDepartment.switchOnProperty().get(), lstDeptInfo, chksdLedgerCreateIsSalesMan.switchOnProperty().get(), cmbsdLedgerCreateSalesman.getSelectionModel().getSelectedItem() != null ? String.valueOf(cmbsdLedgerCreateSalesman.getSelectionModel().getSelectedItem().getValue()) : "", cmbsdLedgerCreateArea.getSelectionModel().getSelectedItem() != null ? String.valueOf(cmbsdLedgerCreateArea.getSelectionModel().getSelectedItem().getValue()) : "", tfsdLedgerCreateRoute.getText());
        }
    }

    private void initBtnSubmitAndCancel() {
        GlobalLedgerCreateController globalLedgerCreateController = GlobalLedgerCreateController.getInstance();
        btnSdLedgerCreateSubmit.setOnAction(actionEvent -> {
            onSubmit();
        });
        btnSdLedgerCreateCancel.setOnAction(actionEvent -> {
            globalLedgerCreateController.fcCallLedgerCreateOtherCancel();
        });
    }

    private boolean ValidateSDForm() {
        if (tfsdLedgerCreateRegisterdName.getText().trim().isEmpty()) {
            String msg = "Enter mandatory Registered Name";
            AlertUtility.CustomCallback callback = number -> {
                tfsdLedgerCreateRegisterdName.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        } else if (tfsdLedgerCreateAdress.getText().trim().isEmpty()) {
            String msg = "Enter mandatory Address ";
            AlertUtility.CustomCallback callback = number -> {
                tfsdLedgerCreateAdress.requestFocus();
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
        } else if (tfsdLedgerCreatePin.getText().trim().isEmpty()) {
            String msg = "Enter mandatory Pin Code ";
            AlertUtility.CustomCallback callback = number -> {
                tfsdLedgerCreatePin.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        } else if (tfsdLedgerCreatePhone.getText().trim().isEmpty()) {
            String msg = "Enter mandatory Phone Number ";
            AlertUtility.CustomCallback callback = number -> {
                tfsdLedgerCreatePhone.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        } else if (tfsdLedgerCreateWhatsapp.getText().trim().isEmpty()) {
            String msg = "Enter mandatory Whatsapp Number ";
            AlertUtility.CustomCallback callback = number -> {
                tfsdLedgerCreateWhatsapp.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        } else if (tfsdLedgerCreateBusinessNature.getText().trim().isEmpty()) {
            String msg = "Enter mandatory Buiseness Nature ";
            AlertUtility.CustomCallback callback = number -> {
                tfsdLedgerCreateBusinessNature.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }

        if (tfsdLedgerCreatePin.getText().length() < 6) {
            String msg = "Enter 6 Digit Pin Code ";
            AlertUtility.CustomCallback callback = number -> {
                tfsdLedgerCreatePin.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }

        if (tfsdLedgerCreatePhone.getText().length() < 10) {
            String msg = "Enter 10 Digit Number";
            AlertUtility.CustomCallback callback = number -> {
                tfsdLedgerCreatePhone.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }

        if (tfsdLedgerCreateWhatsapp.getText().length() < 10) {
            String msg = "Enter 10 Digit Number For Whatsapp";
            AlertUtility.CustomCallback callback = number -> {
                tfsdLedgerCreateWhatsapp.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }
        if (chksdIsGST.switchOnProperty().get() && lstGst.size() == 0) {
            String msg = "GST Number is mandatory!";
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

    public void getFranchiseData(FranchiseDTO franchiseDTO) {
        tfsdLedgerCreateRegisterdName.setText(franchiseDTO.getFranchiseName());
        tfsdLedgerCreateAdress.setText(franchiseDTO.getFranchiseAddress());
        int stateId = Integer.parseInt(franchiseDTO.getStateCode());
        StateOption stOpt = lstIndiaState.stream().filter(stop -> stop.getStateCode() == stateId).findAny().orElse(null);
        if (stOpt != null) {
            cmbsdState.setValue(stOpt);
        }
        tfsdLedgerCreatePin.setText(franchiseDTO.getFranchisePincode());
        tfsdLedgerCreatePhone.setText(franchiseDTO.getMobileNo());
        tfsdLedgerCreateWhatsapp.setText(franchiseDTO.getWhatsappNo());
        tfsdLedgerCreateEmail.setText(franchiseDTO.getEmailId());
    }

}
