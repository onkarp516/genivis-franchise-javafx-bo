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
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LedgerEditSCController implements Initializable {
    @FXML
    private SwitchButton chkscIsBank;

    @FXML
    private HBox ledgerEditScMainDivider, scThirdRowEdit, scSecondRowEdit, scFirstRowEdit, editScInforFirstRow, editScInforSecondRow, editScInforThirdRow;
    @FXML
    private VBox ledgerscMainDividerSecondEdit, ledgerscMainDividerFirstEdit, leftRowInfoFirst, rightRowInfoFirst, leftRowInfoSecond, leftRowInfoThird, rightRowInfoSecond, rightRowInfoThird;

    @FXML
    private SwitchButton chkscIsDepartment;

    @FXML
    private SwitchButton chkscIsGST;

    @FXML
    private SwitchButton chkscIsLicense;

    @FXML
    private SwitchButton chkscIsShippingDetails;

    @FXML
    private SwitchButton chkscLedgerEditIsCredit;

    @FXML
    private ComboBox<StateOption> cmbscState;

    @FXML
    private RadioButton rdscDistributor;

    @FXML
    private RadioButton rdscManufacturer;

    @FXML
    private RadioButton rdscRetailer;
//    private final ToggleGroup tgTrade = new ToggleGroup();

    @FXML
    private BorderPane stpanesc;

    @FXML
    private TextField tfscLedgerEditAdress;

    @FXML
    private TextField tfscLedgerEditBusinessNature;

    @FXML
    private TextField tfscLedgerEditCreditBills;

    @FXML
    private TextField tfscLedgerEditCreditDays;

    @FXML
    private TextField tfscLedgerEditCreditValue;

    @FXML
    private TextField tfscLedgerEditEmail;


    @FXML
    private TextField tfscLedgerEditPhone;

    @FXML
    private TextField tfscLedgerEditPin;

    //    @FXML
//    private DatePicker tfscLedgerEditRegDate;
    @FXML
    private TextField tfscLedgerEditRegDate;

    @FXML
    private TextField tfscLedgerEditRegNo;

    @FXML
    private TextField tfscLedgerEditRegisterdName;

    @FXML
    private TextField tfscLedgerEditWhatsapp;


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
    private TableView<DeptInfoDTO> tblscLedgerEditDept;

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
    private TableView<ShippingDTO> tblscLedgerEditShipping;
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
    private TableView<LicenseDetailDTO> tblscLedgerEditLicenses;
    @FXML
    private Button btnScLedgerEditSubmit, btnScLedgerEditCancel, btnGstPlus, btnBankPlus, btnDeptPlus, btnShippingPlus, btnLicensePlus;

    @FXML
    private VBox vboxscLedgerEditIsCredit, vboxchkscIsGST, vboxchkscIsLicense, vboxchkscIsDepartment, vboxchkscIsBank, vboxchkscIsShippingDetails;
    @FXML
    private ComboBox<CommonOption> cmbscLedgerEditApplicableFrom;
    @FXML
    private Label lblBank, lblGST, lblLicense, lblDepartment, lblShipping;

    private JsonObject jsonObject = null;

    String lblStyleCSS = "-fx-font-weight: bold; -fx-text-fill: #00a0f5;";

    private final ObservableList<GstListDTO> tbllstGst = FXCollections.observableArrayList();
    private final List<GstList> lstGst = new ArrayList<>();
    private final ObservableList<BankDetailsDTO> tbllstBankInfo = FXCollections.observableArrayList();
    private final List<BankDetailList> lstBankInfo = new ArrayList<>();
    private int edtIdx = -1, edtId = 0, bankEdtIdx = -1, bankEdtId = 0;
    private final ObservableList<DeptInfoDTO> tbllstDeptInfo = FXCollections.observableArrayList();
    private final List<DeptInfo> lstDeptInfo = new ArrayList<>();
    private int deptEdtIdx = -1, deptEdtId = 0;

    private final ObservableList<ShippingDTO> tbllstShippingInfo = FXCollections.observableArrayList();
    private final List<ShippingInfo> lstShippingInfo = new ArrayList<>();
    private int shippingEdtIdx = -1, shippingEdtId = 0;
    private final ObservableList<LicenseDetailDTO> tbllstLicenseInfo = FXCollections.observableArrayList();
    private final List<LicenseInfo> lstLicenseInfo = new ArrayList<>();
    private int licenseEdtIdx = -1, licenseEdtId = 0;
    private ObservableList<CommonOption> lstApplicableFrom = FXCollections.observableArrayList();
    private ObservableList<StateOption> lstIndiaState = FXCollections.observableArrayList();

    private ObservableList<LicenseType> lstLicenseType = FXCollections.observableArrayList();

    private List<Integer> lstRemoveGst = new ArrayList<>();

    private List<Integer> lstRemoveLicense = new ArrayList<>();
    private List<Integer> lstRemoveBank = new ArrayList<>();
    private List<Integer> lstRemoveDept = new ArrayList<>();
    private List<Integer> lstRemoveShipping = new ArrayList<>();

    @FXML
    private Label lblLedgerEditPANNo;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();
        //For Divide the two info tables same width
        leftRowInfoFirst.prefWidthProperty().bind(editScInforFirstRow.widthProperty().multiply(0.5));
        rightRowInfoFirst.prefWidthProperty().bind(editScInforFirstRow.widthProperty().multiply(0.5));

        leftRowInfoSecond.prefWidthProperty().bind(editScInforSecondRow.widthProperty().multiply(0.5));
        rightRowInfoSecond.prefWidthProperty().bind(editScInforSecondRow.widthProperty().multiply(0.5));

        leftRowInfoThird.prefWidthProperty().bind(editScInforThirdRow.widthProperty().multiply(0.5));
        rightRowInfoThird.prefWidthProperty().bind(editScInforThirdRow.widthProperty().multiply(0.5));
        // End

//        Platform.runLater(() -> {
//            tfscLedgerEditRegisterdName.requestFocus();
//        });

        lblBank.setStyle(lblStyleCSS);
        lblGST.setStyle(lblStyleCSS);
        lblLicense.setStyle(lblStyleCSS);
        lblDepartment.setStyle(lblStyleCSS);
        lblShipping.setStyle(lblStyleCSS);

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
        setupMobileNumberValidation();
        setupPincodeValidation();
        setupPanNoValidation();

        cbTrade.getItems().addAll("Retailer", "Distributor", "Manufacturer");
        cbTrade.setPromptText("Select Trade");
        cbTrade.setOnAction(this::handleTradeComboBoxAction);
        // open trade dropdown on Space
        cbTrade.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cbTrade.isShowing()) {
                cbTrade.show();
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                tfscLedgerEditBusinessNature.requestFocus();
            }
            event.consume(); // Consume the event to prevent other actions
        });

        tfscLedgerEditPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            //TODO: Update the text property of whatsapp when the value changes in mobileNu,
            tfscLedgerEditWhatsapp.setText(newValue);
        });
        cmbscLedgerEditApplicableFrom.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cmbscLedgerEditApplicableFrom.isShowing()) {
                cmbscLedgerEditApplicableFrom.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });

        DateValidator.applyDateFormat(tfscLedgerEditRegDate);
        CommonFunctionalUtils.restrictDateFormat(tfscLedgerEditRegDate);

        regDateValidation();

//        tfscLedgerEditRegisterdName.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfscLedgerEditAdress.setOnKeyPressed(this::handleEnterKeyPressed);
//        cmbscState.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfscLedgerEditPin.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfscLedgerEditPhone.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfscLedgerEditWhatsapp.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfscLedgerEditEmail.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfscLedgerEditRegNo.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfscLedgerEditRegDate.setOnKeyPressed(this::handleEnterKeyPressed);
//        chkscLedgerEditIsCredit.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfscLedgerEditCreditDays.setOnKeyPressed(this::handleEnterKeyPressed);
//        cmbscLedgerEditApplicableFrom.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfscLedgerEditCreditBills.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfscLedgerEditCreditValue.setOnKeyPressed(this::handleEnterKeyPressed);
//        rdscRetailer.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfscLedgerEditBusinessNature.setOnKeyPressed(this::handleEnterKeyPressed);

        // Add listener to only numbers input
        addNumericListener(tfscLedgerEditCreditDays);
        addNumericListener(tfscLedgerEditCreditBills);
        addNumericListener(tfscLedgerEditCreditValue);

        // Add listener to convert text to uppercase
        tfscLedgerEditPANNo.textProperty().addListener((observable, oldValue, newValue) -> {
            tfscLedgerEditPANNo.setText(newValue.toUpperCase());
        });

        //code for pin code validation
        addPincodeValidationListener(tfscLedgerEditPin);
        //code for email validation
        addEmailValidationListener(tfscLedgerEditEmail);
        // pan no. validation
        addPANNoValidationListener(tfscLedgerEditPANNo);
        // phone no. validation
        addPhoneValidationListener(tfscLedgerEditPhone);
        // whatsapp no. validation
        addWhatsappValidationListener(tfscLedgerEditWhatsapp);

        initialEnterMethod();
//        tfFocused(tfscLedgerEditPhone);
        tfFocused(tfscLedgerEditWhatsapp);
        tfFocused(tfscLedgerEditRegisterdName);
        tfFocused(tfscLedgerEditAdress);
        cmbFocused(cmbscState);
        cmbFocused(cbTrade);
        tfFocused(tfscLedgerEditBusinessNature);
        tfFocused(tfscLedgerEditPANNo);

        btnScLedgerEditSubmit.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                btnScLedgerEditCancel.requestFocus();
            }
        });
    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            scFirstRowEdit.setSpacing(5);
            scSecondRowEdit.setSpacing(5);
            scThirdRowEdit.setSpacing(5);
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            scFirstRowEdit.setSpacing(6);
            scSecondRowEdit.setSpacing(6);
            scThirdRowEdit.setSpacing(6);
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            scFirstRowEdit.setSpacing(8);
            scSecondRowEdit.setSpacing(8);
            scThirdRowEdit.setSpacing(8);
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            scFirstRowEdit.setSpacing(8);
            scSecondRowEdit.setSpacing(8);
            scThirdRowEdit.setSpacing(8);
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            scFirstRowEdit.setSpacing(8);
            scSecondRowEdit.setSpacing(8);
            scThirdRowEdit.setSpacing(8);
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            stpanesc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_7.css").toExternalForm());
        }
    }

    private void regDateValidation() {
        tfscLedgerEditRegDate.focusedProperty().addListener((obs, old, nw) -> {
            System.out.println("nw" + nw);
            if (!nw) {
                String regDateStr = tfscLedgerEditRegDate.getText();
                if (!regDateStr.isEmpty()) {
                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//                String formattedCurrentDate = currentDate.format(formatter);
                    LocalDate regDate = LocalDate.parse(regDateStr, formatter);
//                System.out.println("register date --> " + regDateStr);
//                System.out.println("currentDate --> " + formattedCurrentDate);
                    if (regDate.isAfter(currentDate)) {
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Register date cannot be greater than the current date. ", input -> {
                            tfscLedgerEditRegDate.requestFocus();
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

    private void addWhatsappValidationListener(TextField tfscLedgerEditWhatsapp) {
        tfscLedgerEditWhatsapp.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                event.consume();
                if (!isValidMobileNumber(tfscLedgerEditWhatsapp.getText()) && !tfscLedgerEditWhatsapp.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered Whatsapp number is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Whatsapp Number", newEvent -> {
                        tfscLedgerEditWhatsapp.requestFocus();
                    });
                    event.consume();
                }
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                if (!isValidMobileNumber(tfscLedgerEditWhatsapp.getText()) && !tfscLedgerEditWhatsapp.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered Whatsapp number is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Whatsapp Number", newEvent -> {
                        tfscLedgerEditWhatsapp.requestFocus();
                    });
                    event.consume();
                }
            }
        });
    }

    private void addPhoneValidationListener(TextField tfscLedgerEditPhone) {
        tfscLedgerEditPhone.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                event.consume();
                if (!isValidMobileNumber(tfscLedgerEditPhone.getText()) && !tfscLedgerEditPhone.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered Phone number is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Phone Number", newEvent -> {
                        tfscLedgerEditPhone.requestFocus();
                    });
                    event.consume();
                }
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                if (!isValidMobileNumber(tfscLedgerEditPhone.getText()) && !tfscLedgerEditPhone.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered Phone number is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Phone Number", newEvent -> {
                        tfscLedgerEditPhone.requestFocus();
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

    private void addEmailValidationListener(TextField tfscLedgerEditEmail) {
        tfscLedgerEditEmail.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                event.consume();
                if (!validEmail(tfscLedgerEditEmail.getText()) && !tfscLedgerEditEmail.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered email is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Email Id", newEvent -> {
                        tfscLedgerEditEmail.requestFocus();
                    });
                    event.consume();
                }
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {

                if (!validEmail(tfscLedgerEditEmail.getText()) && !tfscLedgerEditEmail.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered email is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Email Id", newEvent -> {
                        tfscLedgerEditEmail.requestFocus();
                    });
                    event.consume();
                }
            }
        });
    }

    private void addPincodeValidationListener(TextField tfscLedgerEditPin) {
        tfscLedgerEditPin.focusedProperty().addListener((obs, old, nw) -> {
            if (!nw) {
                if (!tfscLedgerEditPin.getText().isEmpty()) {
                    validatePincode();
                }
                if (tfscLedgerEditPin.getText().trim().isEmpty()) {
                    tfscLedgerEditPin.requestFocus();
                }
                String newVal = tfscLedgerEditPin.getText().trim();
                if (newVal.length() > 6) {
                    tfscLedgerEditPin.setText(newVal.substring(0, 6));
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
            map.put("pincode", String.valueOf(tfscLedgerEditPin.getText()));
            String formData = Globals.mapToStringforFormData(map);
//            System.out.println("formData--------> " + formData);
            apiClient = new APIClient(EndPoints.VALIDATE_PINCODE_ENDPOINT, formData, RequestType.FORM_DATA);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        System.out.println("200 --------- ");
                        tfFocused(tfscLedgerEditPhone);
                    } else {
                        System.out.println("404 --------- ");
                        String message = jsonObject.get("message").getAsString();
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Pin Code", newEvent -> {
//                            tfscLedgerEditPin.requestFocus();
                            GlobalTranx.requestFocusOrDieTrying(tfscLedgerEditPin, 3);
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

    private void setupPincodeValidation() {
        TextFormatter<String> pincodeFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,6}")) { // Matches exactly 6 digits
                return change;
            }
            return null; // Ignore the change if it doesn't match
        });

        tfscLedgerEditPin.setTextFormatter(pincodeFormatter);
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

        tfscLedgerEditWhatsapp.setTextFormatter(whatsappNumberFormatter);
        TextFormatter<String> mobileNumberFormatter = new TextFormatter<>(change -> {
            // Allow change if it results in digits only and does not exceed 10 digits
            if (change.getControlNewText().matches("\\d{0,10}")) {
                return change;
            }
            // Reject the change if it doesn't match the required pattern
            return null;
        });

        tfscLedgerEditPhone.setTextFormatter(mobileNumberFormatter);

    }

    private void initAllSwitchButtons() {
        chkscLedgerEditIsCredit.setParentBox(vboxscLedgerEditIsCredit);
        initCreditDaysDisable(false);
        chkscLedgerEditIsCredit.switchOnProperty().addListener((obv, old, nv) -> {
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

    private void initGSTActivate(boolean b) {

        btnGstPlus.setDisable(!b);
//        lblLedgerEditPANNo.setVisible(!b);
        tfLedgerEditPANNo.setVisible(!b);
        tfscLedgerEditPANNo.setVisible(!b);
//        requiredID.setVisible(!b);
//        lblLedgerEditPANNo.setManaged(!b);
        tfLedgerEditPANNo.setManaged(!b);
        tfscLedgerEditPANNo.setManaged(!b);
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

    private void initShippingActivate(boolean b) {

        btnShippingPlus.setDisable(!b);
//        if (b) {
//            Platform.runLater(() -> {
//                btnShippingPlus.requestFocus();
//            });
//        } else {
//            Platform.runLater(() -> {
//                btnScLedgerEditSubmit.requestFocus();
//            });
//        }
    }

    private void initCreditDaysDisable(Boolean nv) {
        tfscLedgerEditCreditDays.setDisable(!nv);
        tfscLedgerEditCreditValue.setDisable(!nv);
        cmbscLedgerEditApplicableFrom.setDisable(!nv);
        tfscLedgerEditCreditBills.setDisable(!nv);
//        if (nv) {
//            Platform.runLater(() -> {
//                tfscLedgerEditCreditDays.requestFocus();
//            });
//        } else {
////            Platform.runLater(() -> {
////                rdscRetailer.requestFocus();
////            });
//        }
    }

//    private void handleEnterKeyPressed(KeyEvent keyEvent) {
//        Node source = (Node) keyEvent.getSource();
//        if (keyEvent.getCode() == KeyCode.ENTER) {
//            switch (source.getId()) {
//                case "tfscLedgerEditRegisterdName":
//                    Platform.runLater(() -> {
//                        tfscLedgerEditAdress.requestFocus();
//                    });
//                    break;
//                case "tfscLedgerEditAdress":
//                    Platform.runLater(() -> {
//                        cmbscState.requestFocus();
//                    });
//                    break;
//                case "cmbscState":
//                    Platform.runLater(() -> {
//                        tfscLedgerEditPin.requestFocus();
//                    });
//                    break;
//                case "tfscLedgerEditPin":
//                    Platform.runLater(() -> {
//                        tfscLedgerEditPhone.requestFocus();
//                    });
//                    break;
//                case "tfscLedgerEditPhone":
//                    Platform.runLater(() -> {
//                        tfscLedgerEditWhatsapp.requestFocus();
//                    });
//                    break;
//                case "tfscLedgerEditWhatsapp":
//                    Platform.runLater(() -> {
//                        tfscLedgerEditEmail.requestFocus();
//                    });
//                    break;
//                case "tfscLedgerEditEmail":
//                    Platform.runLater(() -> {
//                        tfscLedgerEditRegNo.requestFocus();
//                    });
//                    break;
//                case "tfscLedgerEditRegNo":
//                    Platform.runLater(() -> {
//                        tfscLedgerEditRegDate.requestFocus();
//                    });
//                    break;
//                case "tfscLedgerEditRegDate":
//                    Platform.runLater(() -> {
//                        chkscLedgerEditIsCredit.requestFocus();
//                    });
//                case "chkscLedgerEditIsCredit":
//                    if (chkscLedgerEditIsCredit.switchOnProperty().get()) {
//                        Platform.runLater(() -> {
//                            tfscLedgerEditCreditDays.requestFocus();
//                        });
//                    } else {
//                        Platform.runLater(() -> {
//                            rdscRetailer.requestFocus();
//                        });
//                    }
//                    break;
//                case "tfscLedgerEditCreditDays":
//                    Platform.runLater(() -> {
//                        cmbscLedgerEditApplicableFrom.requestFocus();
//                    });
//                    break;
//                case "cmbscLedgerEditApplicableFrom":
//                    Platform.runLater(() -> {
//                        tfscLedgerEditCreditBills.requestFocus();
//                    });
//                    break;
//                case "tfscLedgerEditCreditBills":
//                    Platform.runLater(() -> {
//                        tfscLedgerEditCreditValue.requestFocus();
//                    });
//                    break;
//                case "tfscLedgerEditCreditValue":
//                    Platform.runLater(() -> {
//                        rdscRetailer.requestFocus();
//                    });
//                    break;
//                case "rdscRetailer":
//                    Platform.runLater(() -> {
//                        tfscLedgerEditBusinessNature.requestFocus();
//                    });
//                    break;
//                case "tfscLedgerEditBusinessNature":
//                    Platform.runLater(() -> {
//                        chkscIsGST.requestFocus();
//                    });
//                    break;
//                default:
//                    break;
//            }
//        } else if (keyEvent.getCode() == KeyCode.S && keyEvent.isControlDown()) {
//            onSubmit();
//        } else if (keyEvent.getCode() == KeyCode.X && keyEvent.isControlDown()) {
//            btnScLedgerEditCancel.fire();
//        }
//    }


//    private void initTradeRadioButtonGroup() {
//        rdscDistributor.setToggleGroup(tgTrade);
//        rdscManufacturer.setToggleGroup(tgTrade);
//        rdscRetailer.setToggleGroup(tgTrade);
//    }

    private void initStateCmb() {
        Integer initialIndex = -1;
        List<StateOption> IndiaStateList = Globals.getIndiaStateList();
        lstIndiaState = FXCollections.observableArrayList(IndiaStateList);
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
                return lstIndiaState.stream()
                        .filter(state -> state.getStateName().equals(string))
                        .findFirst()
                        .orElse(null);
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
                        lstRemoveGst.add(lstGst.get(getIndex()).getId());
                    });
                    edtButton.setOnAction(event -> {
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

        tblscGstInfo.setItems(tbllstGst);

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
                        bankEdtId = 0;
                        lstBankInfo.remove(getIndex());
                        btnBankPlus.requestFocus();
                        tbllstBankInfo.remove(getIndex());
                        lstRemoveBank.add(lstBankInfo.get(getIndex()).getId());
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
            bankEdtId = 0;
        } else {
            tbllstBankInfo.add(bankDetailsDTO);
            lstBankInfo.add(bankDetailList);
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
                        // Handle button action here
                        deptEdtIdx = -1;
                        deptEdtId = 0;
                        tbllstDeptInfo.remove(getIndex());
                        lstDeptInfo.remove(getIndex());
                        btnDeptPlus.requestFocus();
                        lstRemoveDept.add(lstDeptInfo.get(getIndex()).getId());
                    });
                    edtButton.setOnAction(event -> {
                        // Handle button action here
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
        tblscLedgerEditDept.setItems(tbllstDeptInfo);
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
                        licenseEdtId = 0;
                        tbllstLicenseInfo.remove(getIndex());
                        lstLicenseInfo.remove(getIndex());
                        btnLicensePlus.requestFocus();
                        lstRemoveLicense.add(lstLicenseInfo.get(getIndex()).getId());
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
        tblscLedgerEditLicenses.setItems(tbllstLicenseInfo);
    }

    private void addAndUpdateLicenseInfo(LicenseInfo licenseInfo) {

        LicenseDetailDTO licenseDetailDTO = new LicenseDetailDTO(licenseInfo.getLicenseNo(), licenseInfo.getLicenseTypeName(), licenseInfo.getLicenseExp() != null ? licenseInfo.getLicenseExp() : null, "");

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

    private void onSubmit() {
        GlobalLedgerEditController globalLedgerEditController = GlobalLedgerEditController.getInstance();
        if (ValidateSCForm()) {
//            RadioButton selectedRdBtn = (RadioButton) tgTrade.getSelectedToggle();
            globalLedgerEditController.fnCallLedgerEditSCSubmit(tfscLedgerEditRegisterdName.getText(), tfscLedgerEditAdress.getText(),
                    cmbscState.getSelectionModel().getSelectedItem() != null ? String.valueOf(cmbscState.getSelectionModel().getSelectedItem().getId()) : "", tfscLedgerEditPin.getText(),
                    tfscLedgerEditPhone.getText(), tfscLedgerEditWhatsapp.getText(), tfscLedgerEditEmail.getText(), tfscLedgerEditRegNo.getText(),
//                    DateConvertUtil.convertDatetoAPIDateString(tfscLedgerEditRegDate.getValue()),
//                    Communicator.text_to_date.fromString(tfscLedgerEditRegDate.getText()).toString(),
                    tfscLedgerEditRegDate.getText().isEmpty() ? "" : Communicator.text_to_date.fromString(tfscLedgerEditRegDate.getText()).toString(),
//                    selectedRdBtn.getText(),
//                    selectedTrade != null ? selectedTrade : "",
                    cbTrade.getSelectionModel().getSelectedItem(),
                    tfscLedgerEditBusinessNature.getText(),
                    chkscLedgerEditIsCredit.switchOnProperty().get(), tfscLedgerEditCreditDays.getText(),
                    cmbscLedgerEditApplicableFrom.getSelectionModel().getSelectedItem() != null ? cmbscLedgerEditApplicableFrom.getSelectionModel().getSelectedItem().getName() : "",
                    tfscLedgerEditCreditBills.getText(), tfscLedgerEditCreditValue.getText(), tfscLedgerEditPANNo.getText(), chkscIsGST.switchOnProperty().get(),
                    lstGst, chkscIsLicense.switchOnProperty().get(), lstLicenseInfo, chkscIsBank.switchOnProperty().get(),
                    lstBankInfo, chkscIsShippingDetails.switchOnProperty().get(), lstShippingInfo, chkscIsDepartment.switchOnProperty().get(),
                    lstDeptInfo, lstRemoveGst, lstRemoveLicense, lstRemoveDept, lstRemoveBank, lstRemoveShipping);
        }
    }


    private void initBtnSubmitAndCancel() {
        GlobalLedgerEditController globalLedgerEditController = GlobalLedgerEditController.getInstance();
        btnScLedgerEditSubmit.setOnAction(actionEvent -> {
            onSubmit();
        });
        btnScLedgerEditCancel.setOnAction(actionEvent -> {
            globalLedgerEditController.fcCallLedgerEditOtherCancel();
        });
    }

    private boolean ValidateSCForm() {
        if (tfscLedgerEditRegisterdName.getText().trim().isEmpty()) {
            String msg = "Enter mandatory Registered Name";
            AlertUtility.CustomCallback callback = number -> {
                tfscLedgerEditRegisterdName.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        } else if (tfscLedgerEditAdress.getText().trim().isEmpty()) {
            String msg = "Enter mandatory Address ";
            AlertUtility.CustomCallback callback = number -> {
                tfscLedgerEditAdress.requestFocus();
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
        } else if (tfscLedgerEditPin.getText().trim().isEmpty()) {
            String msg = "Enter mandatory Pin Code ";
            AlertUtility.CustomCallback callback = number -> {
                tfscLedgerEditPin.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        } else if (tfscLedgerEditPhone.getText().trim().isEmpty()) {
            String msg = "Enter mandatory Phone Number ";
            AlertUtility.CustomCallback callback = number -> {
                tfscLedgerEditPhone.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        } else if (tfscLedgerEditWhatsapp.getText().trim().isEmpty()) {
            String msg = "Enter mandatory Whatsapp Number ";
            AlertUtility.CustomCallback callback = number -> {
                tfscLedgerEditWhatsapp.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        } else if (tfscLedgerEditBusinessNature.getText().trim().isEmpty()) {
            String msg = "Enter mandatory Business Nature ";
            AlertUtility.CustomCallback callback = number -> {
                tfscLedgerEditBusinessNature.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }

        if (tfscLedgerEditPin.getText().length() < 6) {
            String msg = "Enter 6 Digit Pin Code ";
            AlertUtility.CustomCallback callback = number -> {
                tfscLedgerEditPin.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }

        if (tfscLedgerEditPhone.getText().length() < 10) {
            String msg = "Enter 10 Digit Number";
            AlertUtility.CustomCallback callback = number -> {
                tfscLedgerEditPhone.requestFocus();
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            return false;
        }

        if (tfscLedgerEditWhatsapp.getText().length() < 10) {
            String msg = "Enter 10 Digit Number For Whatsapp ";
            AlertUtility.CustomCallback callback = number -> {
                tfscLedgerEditWhatsapp.requestFocus();
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
//            AlertUtility.AlertError(AlertUtility.alertTypeError, msg, callback);
//            return false;
//        }
        return true;
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
                        shippingEdtIdx = -1;
                        shippingEdtId = 0;
                        lstRemoveShipping.add(lstShippingInfo.get(getIndex()).getId());
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
        tblscLedgerEditShipping.setItems(tbllstShippingInfo);
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
        cmbscLedgerEditApplicableFrom.getItems().addAll(lstApplicableFrom);
        cmbscLedgerEditApplicableFrom.setConverter(new StringConverter<CommonOption>() {
            @Override
            public String toString(CommonOption commonOption) {
                return commonOption != null ? commonOption.getName() : "";
            }

            @Override
            public CommonOption fromString(String string) {
                // You can implement this method if needed
                return null;
            }
        });
    }

    public void setEditData(JSONObject object) {
        System.out.println("Ledger Edit SD Controller Edit Data ->" + object);
        if (!object.isEmpty()) {
            tfscLedgerEditRegisterdName.setText(object.getString("mailing_name"));
            tfscLedgerEditAdress.setText(object.getString("address"));
            tfscLedgerEditPin.setText(object.getString("pincode"));
            tfscLedgerEditPhone.setText(object.getString("mobile_no"));
            tfscLedgerEditWhatsapp.setText(object.getString("whatsapp_no"));
            if (object.has("pancard_no") && !object.getString("pancard_no").isEmpty()) {
                tfscLedgerEditPANNo.setText(object.getString("pancard_no"));
            }
            if (object.get("state") != null) {
                lstIndiaState.stream().filter(opt -> opt.getId() == object.getInt("state")).findAny().ifPresent(stOpt -> cmbscState.setValue(stOpt));
            }

            tfscLedgerEditEmail.setText(object.getString("email"));
            tfscLedgerEditRegNo.setText(object.getString("licenseNo") != null ? object.getString("licenseNo") : "");
//            tfscLedgerEditRegDate.setValue(!object.getString("reg_date").isEmpty() ? DateConvertUtil.convertStringToLocalDate(object.getString("reg_date")) : null);
            String editDataReg_date = object.getString("reg_date");
            if (!editDataReg_date.isEmpty() && editDataReg_date != null && tfscLedgerEditRegDate != null) {
                LocalDate applicable_date = LocalDate.parse(editDataReg_date);
                tfscLedgerEditRegDate.setText(applicable_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            chkscLedgerEditIsCredit.switchOnProperty().set(object.getBoolean("isCredit"));
            if (object.getBoolean("isCredit") == true) {
                tfscLedgerEditCreditDays.setText("" + object.getInt("credit_days"));
                if (!object.getString("applicable_from").isEmpty()) {
                    CommonOption applicableFrom = lstApplicableFrom.stream().filter(s -> s.getName().toLowerCase().equalsIgnoreCase(object.getString("applicable_from").toLowerCase())).findAny().orElse(null);
                    if (applicableFrom != null) {
                        cmbscLedgerEditApplicableFrom.setValue(applicableFrom);
                    }
                }
                tfscLedgerEditCreditBills.setText("" + object.getInt("creditNumBills"));
                tfscLedgerEditCreditValue.setText("" + object.getInt("creditBillValue"));
            }

            tfscLedgerEditBusinessNature.setText("" + object.getString("businessTrade"));
            if (!object.getString("businessType").isEmpty()) {
                String businessType = object.getString("businessType");
                List<String> allTrades = Globals.getAllTrades();
                allTrades.stream().filter((v) -> v.equalsIgnoreCase(businessType)).findAny().ifPresent((p) -> {
                    cbTrade.setValue(p);
                });
//                switch (businessType.toLowerCase()) {
//                    case "distributor":
//                        tgTrade.selectToggle(rdscDistributor);
//                        break;
//                    case "manufacturer":
//                        tgTrade.selectToggle(rdscManufacturer);
//                        break;
//                    case "retailer":
//                        tgTrade.selectToggle(rdscRetailer);
//                        break;
//                }
            }

            //?Gst list
//            chkscIsGST.setSelected(object.getBoolean("isGST"));
            chkscIsGST.switchOnProperty().set(object.getBoolean("isGST"));
            initGSTActivate(object.getBoolean("isGST"));
            if (object.getBoolean("isGST") == true) {
                JSONArray gstDetailsArr = object.getJSONArray("gstdetails");
                for (Object obj : gstDetailsArr) {
                    JSONObject nwGstDetail = new JSONObject(obj.toString());
//                    LocalDate ndt = !nwGstDetail.getString("dateOfRegistration").isEmpty() ? DateConvertUtil.convertStringToLocalDate(nwGstDetail.getString("dateOfRegistration")) : null;

                    String tfdt = !nwGstDetail.getString("dateOfRegistration").isEmpty() ? DateConvertUtil.convertDispDateFormat(nwGstDetail.getString("dateOfRegistration")) : null;
                    int id = nwGstDetail.getInt("id");
                    String panNo = nwGstDetail.getString("pancard");
                    int regType = nwGstDetail.getInt("registraion_type");
                    String gstIn = nwGstDetail.getString("gstin");
                    ObservableList<GSTType> lstGstType = FXCollections.observableArrayList(Globals.getGstTypeList());
                    GSTType regTypeOpt = lstGstType.stream().filter(s -> s.getValue() == regType).findAny().orElse(null);
                    GstList gstList = new GstList(id, regTypeOpt != null ? regTypeOpt.getValue() : 0, regTypeOpt != null ? regTypeOpt.getLabel() : "", tfdt, gstIn, panNo);
                    GstListDTO gstListDTO = new GstListDTO(id, regTypeOpt != null ? regTypeOpt.getValue() : 0, regTypeOpt != null ? regTypeOpt.getLabel() : "", tfdt, gstIn, panNo);
                    lstGst.add(gstList);
                    tbllstGst.add(gstListDTO);
                }
            }

            //?License
//            chkscIsLicense.setSelected(object.getBoolean("isLicense"));
            chkscIsLicense.switchOnProperty().set(object.getBoolean("isLicense"));
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
//            chkscIsDepartment.setSelected(object.getBoolean("isDepartment"));
            chkscIsDepartment.switchOnProperty().set(object.getBoolean("isDepartment"));
            if (object.getBoolean("isDepartment") == true) {
                JSONArray deptDetailsArr = object.getJSONArray("deptDetails");
                for (Object obj : deptDetailsArr) {
                    JSONObject nwDeptObj = new JSONObject(obj.toString());
                    int id = nwDeptObj.getInt("id");
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
//        chkscIsBank.setSelected(object.getBoolean("isBankDetails"));
        chkscIsBank.switchOnProperty().set(object.getBoolean("isBankDetails"));
        if (object.getBoolean("isBankDetails") == true) {
            JSONArray bankDetailsArr = object.getJSONArray("bankDetails");
            for (Object bankObj : bankDetailsArr) {
                JSONObject bankDetailObj = new JSONObject(bankObj.toString());
                int bid = bankDetailObj.getInt("id");
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

        chkscIsShippingDetails.switchOnProperty().set(object.getBoolean("isShippingDetails"));
        System.out.println("isShippingDetails" + object.getBoolean("isShippingDetails"));
        if (object.getBoolean("isShippingDetails")) {
            JSONArray shippingArr = object.getJSONArray("shippingDetails");
            System.out.println("ShippingArr" + shippingArr);
            tbllstShippingInfo.clear();
            lstShippingInfo.clear();
            for (Object shippingObj : shippingArr) {
                System.out.println("shippingObj" + shippingObj.toString());
                JSONObject shippingDetailObj = new JSONObject(shippingObj.toString());
                System.out.println("shippingOBj jsonFormat" + shippingDetailObj);
                int id = shippingDetailObj.getInt("id");
                String shippingAddress = shippingDetailObj.getString("shipping_address");
                String shippingState = shippingDetailObj.getString("district");
                ObservableList<StateOption> lstIndiaState = FXCollections.observableArrayList(Globals.getIndiaStateList());
                System.out.println("lstIndiaState" + lstIndiaState);
                lstIndiaState.stream().filter(s -> s.getId() == Integer.parseInt(shippingState)).findAny().ifPresent(stOp -> {
                    ShippingInfo shippingInfo = new ShippingInfo(id, shippingAddress, stOp.getId(), stOp.getStateName(), stOp.getStateCode());
                    ShippingDTO shippingDTO = new ShippingDTO(shippingAddress, stOp.getStateName(), stOp.getId());
                    tbllstShippingInfo.add(shippingDTO);
                    lstShippingInfo.add(shippingInfo);
                });

                System.out.println("tbllstShippingInfo" + tbllstShippingInfo);
                System.out.println("lstShippingInfo" + lstShippingInfo);
            }
        }
    }
}













