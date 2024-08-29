package com.opethic.genivis.controller.master;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.commons.SwitchButton;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.key_manager.ShortCutKey;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;

import static com.opethic.genivis.utils.FxmFileConstants.*;
import static com.opethic.genivis.utils.Globals.companyListDTO;

public class CompanyCreateController implements Initializable {

    private Map<String, String> AreaMapList = new HashMap<>();
    private Map<String, String> CorpAreaMapList = new HashMap<>();
    private Map<String, String> countryMapList = new HashMap<>();
    private Map<String, String> gstTypeMapList = new HashMap<>();
    private Map<String, String> currencyMapList = new HashMap<>();
    String message = "";
    String stateCodeById = "";
    String corporateStateCodeById = "";
    String pinText = "";
    String corppinText = "";
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(CompanyCreateController.class);
    private JsonObject jsonObject = null;

    @FXML
    private Button hideShow;
    private boolean showPassword = false;


    //TODO: for gst applicable
    private boolean isGSTApplicable = false;
    @FXML
    private SwitchButton sbCmpCreateGSTApplicable;
    @FXML
    private VBox gstAppMain;

    //TODO: for multibranch
    private boolean isMultiBranch = false;
    @FXML
    private SwitchButton sbCmpMultiBranch;
    @FXML
    private VBox multiMainSwitch;


    @FXML
    ComboBox cmbCmpTrade, cmbCmpGender;
    @FXML
    TextField tfCmpCreateUserControlLine, tfCmpCreateAdminDetailsLine;
    @FXML
    Button btnCmpCreateSubmit, btnCmpCreateCancel;
    private String responseBody;
    String selectedGenderRadioValue = "";
    String selectedTradeRadioValue = "";
    @FXML
    private ComboBox cmbCmpCreateBusinessType;
    @FXML
    private ComboBox<CompanyAreaDTO> cmbCmpCreateRegArea, cmbCmpCreateCorpArea;
    @FXML
    private RadioButton rbCmpCreateManufacturer, rbCmpCreateDistributor, rbCmpCreateRetailer, rbCmpCreateAdminGenderMale, rbCmpCreateAdminGenderFemale;
    @FXML
    private ComboBox cmbCmpCreateCorpCountry;
    @FXML
    private TextField tfAreaHeadCreatePasswordInText;
    @FXML
    private ComboBox cmbCmpCreateRegCountry;
    //    @FXML
//    private ComboBox cmbCmpCreateCorpArea;
    @FXML
    ScrollPane spCompanyCreateRootPane;
    @FXML
    private ComboBox cmbCmpCreateGSTType;
    @FXML
    private ComboBox cmbCmpCreateCurrency;
    @FXML
    private Label lblCmpCreateRegCityName, lblCmpCreateCorpStateName, lblCmpCreateCorpCityName, lblCmpCreateRegStateName, lblCmpCreateGSTINLabel, lblCmpCreateGSTType, lblCmpCreateApplicableDate;
    private ToggleGroup toggleGroup = new ToggleGroup();
    private ToggleGroup genderToggle = new ToggleGroup();
    @FXML
    private CheckBox cbCmpCreateGSTApplicable, cbCmpCreateMultiBranch, cbCmpCreateSameAsAddress;
    @FXML
    private DatePicker dpCmpCreateApplicableDate, dpCmpCreateFSSAINoExpDate, dpCmpCreateMfgLicenseNoExpDate, dpCmpCreateAdminBirthDate;
    @FXML
    FlowPane hbCmpCreateUserControl;
    @FXML
    ImageView ivCmpCreateSelectedImage;
    @FXML
    Button btnCmpCreateSelectFile;
    @FXML
    Label lbCmpCreateSelectedFileName, lblCmpCreateMainAdminDetails, lblCmpCreateFullName, lblCmpCreateAdminEmail, lblCmpCreateAdminMobileNo, lblCmpCreateAdminBirthDate, lblCmpCreateAdminGenderMale, lblCmpCreateAdminUsername, lblCmpCreateAdminPassword, lblCmpCreateMainUserControl;
    @FXML
    HBox hbCmpCreateAdminMobileNo, hbCmpCreateAdminUsername, hbCmpCreateAdminPassword, hbCmpCreateGSTINLabel, hbCmpCreateGSTType;
    @FXML
    Line lCmpCreateMainAdminLine, lCmpCreateMainUserControlLine;
    @FXML
    TextField tfCmpCreateLicenseExpDate, tfCmpCreateFSSAINoExpDate, tfCmpCreateMfgLicenseNoExpDate, tfCmpCreateApplicableDate, tfCmpCreateAdminBirthDate;
    @FXML
    TextField tfCmpCreateCompanyCode, tfCmpCreateName, tfCmpCreateLicenseNo, tfCmpCreateFSSAINo, tfCmpCreateMfgLicenseNo, tfCmpCreateWebsite, tfCmpCreateEmail, tfCmpCreateMobileNo, tfCmpCreateWhatsappNo, tfCmpCreateFullName, tfCmpCreateAdminEmail, tfCmpCreateAdminMobileNo, tfCmpCreateAdminUsername, tfCmpCreateAdminPassword, tfCmpCreateRegPincode, tfCmpCreateCorpPincode, tfCmpCreateCorpAddress, tfCmpCreateRegAddress, tfCmpCreateGSTIN;
    File selectedFile;
    private String districtName = "";
    private Long areaId = 0L;
    private Long corpAreaId = 0L;
    private String stateCode = "";
    private String corpStateCode = "";

    private Long businessTypeId = 0L;
    private Long countryId = 0L;
    private Long gstTypeId = 0L;
    List<CompanySystemConfigDTO> jsonArraySystemConfig = new ArrayList<>();
    ObservableList<CompanyAreaDTO> list = FXCollections.observableArrayList();
    ObservableList<CompanyAreaDTO> listC = FXCollections.observableArrayList();

    FileChooser fileChooser;
    private Node[] focusableNodes;

    public String tradeOptions = "";
    public String genderOptions = "";

    public TextField tfLevelA;
    public TextField tfLevelB;
    public TextField tfLevelC;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        CommonValidationsUtils.onlyEnterNumbersLimit(tfCmpCreateRegPincode, 6);
        CommonValidationsUtils.onlyEnterNumbersLimit(tfCmpCreateCorpPincode, 6);
        CommonFunctionalUtils.commonInit(tfCmpCreateAdminPassword, tfAreaHeadCreatePasswordInText, hideShow);
//
//        //TODO: password hide and show image
//        ImageView imageView = new ImageView(new Image(GenivisApplication.class.getResourceAsStream("/com/opethic/genivis/ui/assets/eye_hidden.png")));
//        imageView.setFitWidth(20);
//        imageView.setFitHeight(20);
//        hideShow.setGraphic(imageView);
        tfLevelA = new TextField();
        tfLevelB = new TextField();
        tfLevelC = new TextField();

        cmbCmpTrade.setItems(FXCollections.observableArrayList("Manufacturer", "Distributor", "Retailer"));
        cmbCmpTrade.setOnAction(event -> {
            tradeOptions = cmbCmpTrade.getValue().toString();
        });

        cmbCmpGender.setItems(FXCollections.observableArrayList("Male", "Female"));
        cmbCmpGender.setOnAction(event -> {
            genderOptions = cmbCmpGender.getValue().toString();
        });

        //TODO: gst applicable
        sbCmpCreateGSTApplicable.setParentBox(gstAppMain);
        sbCmpCreateGSTApplicable.switchOnProperty().set(isGSTApplicable);
        sbCmpCreateGSTApplicable.switchOnProperty().addListener((observable, oldValue, newValue) -> {
            isGSTApplicable = newValue;
            // todo:Toggle visibility of tfFranchiseCreateFundingAmount based on the value of tgFranchiseIsFunding
            if (newValue) {
                hbCmpCreateGSTINLabel.setVisible(true);
                tfCmpCreateGSTIN.setVisible(true);
                hbCmpCreateGSTType.setVisible(true);
                cmbCmpCreateGSTType.setVisible(true);
                lblCmpCreateApplicableDate.setVisible(true);
                tfCmpCreateApplicableDate.setVisible(true);
                hbCmpCreateGSTINLabel.setManaged(true);
                tfCmpCreateGSTIN.setManaged(true);
                hbCmpCreateGSTType.setManaged(true);
                cmbCmpCreateGSTType.setManaged(true);
                lblCmpCreateApplicableDate.setManaged(true);
                tfCmpCreateApplicableDate.setManaged(true);
            } else {
                hbCmpCreateGSTINLabel.setVisible(false);
                tfCmpCreateGSTIN.setVisible(false);
                hbCmpCreateGSTType.setVisible(false);
                cmbCmpCreateGSTType.setVisible(false);
                lblCmpCreateApplicableDate.setVisible(false);
                tfCmpCreateApplicableDate.setVisible(false);

                hbCmpCreateGSTINLabel.setManaged(false);
                tfCmpCreateGSTIN.setManaged(false);
                hbCmpCreateGSTType.setManaged(false);
                cmbCmpCreateGSTType.setManaged(false);
                lblCmpCreateApplicableDate.setManaged(false);
                tfCmpCreateApplicableDate.setManaged(false);
            }
        });

        //TODO: multi branch
        sbCmpMultiBranch.setParentBox(multiMainSwitch);
        sbCmpMultiBranch.switchOnProperty().addListener((observable, oldValue, newValue) -> {
            isMultiBranch = newValue;
            if (newValue) {
                sbCmpMultiBranch.switchOnProperty().set(true);
            } else {
                sbCmpMultiBranch.switchOnProperty().set(false);
            }
        });


        //TODO: focus next element
        spCompanyCreateRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("submit")) {
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("update")) {
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("cancel")) {
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
                btnCmpCreateSubmit.fire();
            }
            if (event.getCode() == KeyCode.X && event.isControlDown()) {
                btnCmpCreateCancel.fire();
            }
        });

        showDropDownOnKeyPress(cmbCmpCreateRegCountry);
        showDropDownOnKeyPress(cmbCmpCreateCorpCountry);
        showDropDownOnKeyPress(cmbCmpCreateCorpArea);
        showDropDownOnKeyPress(cmbCmpCreateRegArea);
        showDropDownOnKeyPress(cmbCmpCreateGSTType);
        showDropDownOnKeyPress(cmbCmpCreateCurrency);
        showDropDownOnKeyPress(cmbCmpCreateBusinessType);
        showDropDownOnKeyPress(cmbCmpTrade);
        showDropDownOnKeyPress(cmbCmpGender);

        //TODO: user control
        hbCmpCreateUserControl.setHgap(10.0);
        hbCmpCreateUserControl.setVgap(10.0);
        hbCmpCreateUserControl.setAlignment(Pos.CENTER_LEFT);

        //todo:visibility off for the gstin label and gstin textfield first time gst is disabled
        fileChooser = new FileChooser();
        hbCmpCreateGSTINLabel.setVisible(false);
        tfCmpCreateGSTIN.setVisible(false);
        hbCmpCreateGSTType.setVisible(false);
        cmbCmpCreateGSTType.setVisible(false);
        lblCmpCreateApplicableDate.setVisible(false);
        tfCmpCreateApplicableDate.setVisible(false);

        hbCmpCreateGSTINLabel.setManaged(false);
        tfCmpCreateGSTIN.setManaged(false);
        hbCmpCreateGSTType.setManaged(false);
        cmbCmpCreateGSTType.setManaged(false);
        lblCmpCreateApplicableDate.setManaged(false);
        tfCmpCreateApplicableDate.setManaged(false);

        ivCmpCreateSelectedImage.setPreserveRatio(true);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);
        EventHandler<ActionEvent> fileSelectEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                selectedFile = fileChooser.showOpenDialog(btnCmpCreateSelectFile.getScene().getWindow());
                if (selectedFile != null) {
                    lbCmpCreateSelectedFileName.setText(selectedFile.getName());
                    Image image = new Image(selectedFile.toURI().toString());
                    ivCmpCreateSelectedImage.setImage(image);
                }
            }
        };

        btnCmpCreateSelectFile.setOnAction(fileSelectEvent);
        // todo gst Type function (Asynch)
        getGSTTypeFun();
        //todo:getAllMasterSystemConfigFun (Asynch)
        getAllMasterSystemConfigFun();
        //todo:Business type Static Options ComboBox
        getBusinessType();
        //todo:get the country list (Asynch)
        getCountryFun();
        //todo:get currency (Asynch)
        getCurrency();

        sceneInitilization();
        DateValidator.applyDateFormat(tfCmpCreateLicenseExpDate);
        DateValidator.applyDateFormat(tfCmpCreateFSSAINoExpDate);
        DateValidator.applyDateFormat(tfCmpCreateMfgLicenseNoExpDate);
        DateValidator.applyDateFormat(tfCmpCreateApplicableDate);
        DateValidator.applyDateFormat(tfCmpCreateAdminBirthDate);

        //TODO: on page load focus to company code
        Platform.runLater(() -> tfCmpCreateCompanyCode.requestFocus());

        //TODO : disabling the by default field company id if null
        if (Globals.companyListDTO != null) {
            getCompanyEditDataById();
            disabledFields();
        } else {
            Globals.companyListDTO = null;
        }

        CommonValidationsUtils.restrictMobileNumber(tfCmpCreateMobileNo);
        CommonValidationsUtils.restrictMobileNumber(tfCmpCreateAdminMobileNo);
        CommonValidationsUtils.restrictEmail(tfCmpCreateEmail);
        CommonValidationsUtils.restrictEmail(tfCmpCreateAdminEmail);

        //TODO: mobile number and whatsapp number
        tfCmpCreateMobileNo.textProperty().addListener((observable, oldValue, newValue) -> {
            //TODO: Update the text property of whatsapp when the value changes in mobileNu,
            tfCmpCreateWhatsappNo.setText(newValue);
        });

        //TODO: address registered and corporate sameasaddress
        tfCmpCreateRegAddress.textProperty().addListener((observable, oldValue, newValue) -> {
            if (cbCmpCreateSameAsAddress.isSelected())
                tfCmpCreateCorpAddress.setText(newValue);
        });


        //TODO: reg area and corp area


        //TODO: Validate license Exp Date
        tfCmpCreateLicenseExpDate.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (!tfCmpCreateLicenseExpDate.getText().isEmpty()) {
                    licenseExpiryDateFun();
                }
            }
        });

        //TODO: Validate fssai Exp Date
        tfCmpCreateFSSAINoExpDate.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (!tfCmpCreateFSSAINoExpDate.getText().isEmpty()) {
                    fssaiLicenseExpiryDateFun();
                }
            }
        });

        //TODO: Validate mfg Exp Date
        tfCmpCreateMfgLicenseNoExpDate.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (!tfCmpCreateMfgLicenseNoExpDate.getText().isEmpty()) {
                    mfgExpiryDateFun();
                }
            }
        });


        //TODO: Validate Birth Date
        tfCmpCreateAdminBirthDate.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (!tfCmpCreateAdminBirthDate.getText().isEmpty()) {
                    birthDateFun();
                }
            }
        });


        //TODO: Required & Validate user
        tfCmpCreateAdminUsername.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = tfCmpCreateAdminUsername.getText().trim();
                if (text.isEmpty()) {
                    tfCmpCreateAdminUsername.requestFocus();
                } else {
                    functionToValidateUser();
                }
            }
        });

        //TODO: Required company code
        tfCmpCreateCompanyCode.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = tfCmpCreateCompanyCode.getText().trim();
                if (text.isEmpty()) {
                    tfCmpCreateCompanyCode.requestFocus();
                }
            }
        });

        //TODO: Required password
        tfCmpCreateAdminPassword.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = tfCmpCreateAdminPassword.getText().trim();
                if (text.isEmpty()) {
                    tfCmpCreateAdminPassword.requestFocus();
                }
            }
        });

        //TODO: Required & Validate company name
        tfCmpCreateName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = tfCmpCreateName.getText().trim();
                if (text.isEmpty()) {
                    tfCmpCreateName.requestFocus();
                } else {
                    functionCompanyDuplicate();
                }
            }
        });

        //TODO: Required & Validate reg pincode
//        tfCmpCreateRegPincode.focusedProperty().addListener((obs, old, nw) -> {
//            if (!nw) {
//                validatePincode();
//                if (tfCmpCreateRegPincode.getText().trim().isEmpty()) {
//                    tfCmpCreateRegPincode.requestFocus();
//                }
//                String newVal = tfCmpCreateRegPincode.getText().trim();
//                if (newVal.length() > 6) {
//                    tfCmpCreateRegPincode.setText(newVal.substring(0, 6));
//                } else if (newVal.length() == 6 && (pinText.isEmpty() || !pinText.trim().contains(newVal))) {
//                    pinText = newVal;
//                    getAddressDataByPincode();
//                }
//                if (cbCmpCreateSameAsAddress.isSelected()) {
//                    tfCmpCreateCorpPincode.setText(newVal);
//                }
//            }
//        });

        mandatory();


        //TODO: Required & Validate corp pincode
//        tfCmpCreateCorpPincode.focusedProperty().addListener((obs1, oldValue, newValue) -> {
//            if (!newValue) {
//                if (tfCmpCreateCorpPincode.getText().trim().isEmpty()) {
//                    tfCmpCreateCorpPincode.requestFocus();
//                }
//                String newVal1 = tfCmpCreateCorpPincode.getText().trim();
//                if (newVal1.length() > 6) {
//                    tfCmpCreateCorpPincode.setText(newVal1.substring(0, 6));
//                    validateCorpPincode();
//                } else if (newVal1.length() == 6 && (corppinText.isEmpty() || !corppinText.equalsIgnoreCase(newVal1))) {
//                    corppinText = newVal1;
//                    getCorporateAddressDataByPincode();
//                }
//            }
//        });

        //TODO: Required & Validate mobile number
        tfCmpCreateMobileNo.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = tfCmpCreateMobileNo.getText().trim();
                if (text.isEmpty()) {
                    tfCmpCreateMobileNo.requestFocus();
                }
            }
        });

        //TODO: Required & Validate contact number
        tfCmpCreateAdminMobileNo.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = tfCmpCreateAdminMobileNo.getText().trim();
                if (text.isEmpty()) {
                    tfCmpCreateAdminMobileNo.requestFocus();
                }
            }
        });

        //TODO: gstin number required and validation
        tfCmpCreateGSTIN.textProperty().addListener((observable, oldValue, newValue) -> {
            if (tfCmpCreateRegPincode.getText().length() != 0) {
                if (newValue.length() < 2) {
                    tfCmpCreateGSTIN.setText(oldValue.toUpperCase());
                }
            }
            if (newValue.length() <= 15) {
                tfCmpCreateGSTIN.setText(newValue.toUpperCase());
            } else {
                tfCmpCreateGSTIN.setText(oldValue);
            }
        });

        tfCmpCreateGSTIN.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfCmpCreateGSTIN.getText().isEmpty()) {
                    tfCmpCreateGSTIN.requestFocus();
                } else {
                    String gstin = tfCmpCreateGSTIN.getText();
                    if (!isValidGSTIN(gstin)) {
                        AlertUtility.AlertDialogForError("WARNING", "Entered GST Is Not Valid", input -> {
                            if (input) {
                                tfCmpCreateGSTIN.requestFocus();
                            }
                        });
                    }
                }
            }
        });

        //TODO: Required GST Type
        cmbCmpCreateGSTType.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (cmbCmpCreateGSTType.getSelectionModel().isEmpty()) {
                    cmbCmpCreateGSTType.requestFocus();
                }
            }
        });

        //TODO: Validate gst applicable Date
        tfCmpCreateApplicableDate.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (!tfCmpCreateApplicableDate.getText().isEmpty()) {
                    gstApplicableDate();
                }
            }
        });


        //TODO License Information Start
        //TODO: Drug License
        tfCmpCreateLicenseExpDate.setDisable(true);
        tfCmpCreateLicenseNo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                tfCmpCreateLicenseExpDate.setDisable(false); // Enable the other text field
            } else {
                tfCmpCreateLicenseExpDate.setDisable(true); // Disable the other text field
            }
        });

        //TODO: FSSAI License
        tfCmpCreateFSSAINoExpDate.setDisable(true);
        tfCmpCreateFSSAINo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                tfCmpCreateFSSAINoExpDate.setDisable(false); // Enable the other text field
            } else {
                tfCmpCreateFSSAINoExpDate.setDisable(true); // Disable the other text field
            }
        });

        //TODO: Mfg License
        tfCmpCreateMfgLicenseNoExpDate.setDisable(true);
        tfCmpCreateMfgLicenseNo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                tfCmpCreateMfgLicenseNoExpDate.setDisable(false); // Enable the other text field
            } else {
                tfCmpCreateMfgLicenseNoExpDate.setDisable(true); // Disable the other text field
            }
        });
        //TODO: License Information End


/*        //TODO: hide and show the password
        hideShow.setOnAction(e -> {
            showPassword = !showPassword;
            if (showPassword) {

                tfCmpCreateAdminPassword.setPromptText(tfCmpCreateAdminPassword.getText());
                tfCmpCreateAdminPassword.setText("");
                tfCmpCreateAdminPassword.setStyle("-fx-prompt-text-fill: black;");
                ImageView imageView1 = new ImageView(new Image(GenivisApplication.class.getResourceAsStream("/com/opethic/genivis/ui/assets/eye.png")));
                imageView1.setFitWidth(20);
                imageView1.setFitHeight(20);
                hideShow.setGraphic(imageView1);
            } else {
                tfCmpCreateAdminPassword.setText(tfCmpCreateAdminPassword.getPromptText());
                tfCmpCreateAdminPassword.setPromptText("");
                ImageView imageView1 = new ImageView(new Image(GenivisApplication.class.getResourceAsStream("/com/opethic/genivis/ui/assets/eye_hidden.png")));
                imageView1.setFitWidth(20);
                imageView1.setFitHeight(20);
                hideShow.setGraphic(imageView1);
            }
        });*/
    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            spCompanyCreateRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            spCompanyCreateRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            spCompanyCreateRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            spCompanyCreateRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            spCompanyCreateRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            spCompanyCreateRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            spCompanyCreateRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    private void mandatory() {
        tfCmpCreateRegPincode.focusedProperty().addListener((obs, old, nw) -> {
            if (!nw) {
                if (!tfCmpCreateRegPincode.getText().isEmpty()) {
                    validatePincode();
                }
                if (tfCmpCreateRegPincode.getText().trim().isEmpty()) {
                    tfCmpCreateRegPincode.requestFocus();
                }
                String newVal = tfCmpCreateRegPincode.getText().trim();
                if (newVal.length() > 6) {
                    tfCmpCreateRegPincode.setText(newVal.substring(0, 6));
//                    validatePincode();
                } else if (newVal.length() == 6 && (pinText.isEmpty() || !pinText.trim().contains(newVal))) {
                    pinText = newVal;
                    getAddressDataByPincode();
                }
                if (cbCmpCreateSameAsAddress.isSelected()) {
                    tfCmpCreateCorpPincode.setText(newVal);
                }
            }
        });

        tfCmpCreateCorpPincode.focusedProperty().addListener((obs, old, nw) -> {
            if (!nw) {
                if (!tfCmpCreateCorpPincode.getText().isEmpty()) {
                    validateCorpPincode();
                }
                if (tfCmpCreateCorpPincode.getText().trim().isEmpty()) {
                    tfCmpCreateCorpPincode.requestFocus();
                }
                String newVal = tfCmpCreateCorpPincode.getText().trim();
                if (newVal.length() > 6) {
                    tfCmpCreateCorpPincode.setText(newVal.substring(0, 6));
//                    validatePincode();
                } else if (newVal.length() == 6 && (pinText.isEmpty() || !pinText.trim().contains(newVal))) {
                    pinText = newVal;
                    getCorporateAddressDataByPincode();
                }
            }
        });
    }

    @FXML
    public void passwordBtn() {
        CommonFunctionalUtils.passwordField(tfCmpCreateAdminPassword, tfAreaHeadCreatePasswordInText, hideShow);
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.isControlDown() && event.getCode() == KeyCode.S) {
            submitTheForm();
        }
        if (event.isControlDown() && event.getCode() == KeyCode.X) {
            backToList();
        }
    }


    public void submitTheForm() {
        if (Globals.companyListDTO != null) {
            if (CommonValidationsUtils.validateForm(tfCmpCreateCompanyCode, tfCmpCreateName, tfCmpCreateRegPincode, tfCmpCreateMobileNo)) {
                updateCompanyFun();
            }
        } else {
            if (CommonValidationsUtils.validateForm(tfCmpCreateCompanyCode, tfCmpCreateName, tfCmpCreateRegPincode, tfCmpCreateMobileNo, tfCmpCreateAdminMobileNo, tfCmpCreateAdminUsername, tfCmpCreateAdminPassword))
                createCompanyFun();
        }
    }

    //TODO: open dropdown list onKey DOWN and SPACE
    public static void showDropDownOnKeyPress(ComboBox<?> comboBox) {
        comboBox.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.DOWN) {
                comboBox.show();
            }
        });
    }

    //TODO: Function to validate GSTIN
    private boolean isValidGSTIN(String gstin) {
        String regex = "^\\d{2}[A-Z]{5}\\d{4}[A-Z]{1}\\d[Z]{1}[A-Z\\d]{1}$";
        return Pattern.matches(regex, gstin);
    }

    //TODO: going back to list page
    public void backToList() {
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                GlobalSadminController.getInstance().addTabStatic(COMPANY_LIST_SLUG, false);
            } else {
                System.out.println("working!");
            }
        };
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are sure to cancel", callback);
    }

    public void sceneInitilization() {
        spCompanyCreateRootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }

    private boolean isValidDate(String input) {
        // Regular expression for date format "dd/MM/yyyy"
        return input.matches("(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[0-2])/\\d{4}");
    }

    private void disabledFields() {
        if (Globals.companyListDTO.getId() != null) {
            lblCmpCreateMainAdminDetails.setVisible(false);
            tfCmpCreateAdminDetailsLine.setVisible(false);
            lblCmpCreateFullName.setVisible(false);
            tfCmpCreateFullName.setVisible(false);
            lblCmpCreateAdminEmail.setVisible(false);
            tfCmpCreateAdminEmail.setVisible(false);
            hbCmpCreateAdminMobileNo.setVisible(false);
            tfCmpCreateAdminMobileNo.setVisible(false);
            lblCmpCreateAdminBirthDate.setVisible(false);
            tfCmpCreateAdminBirthDate.setVisible(false);
            lblCmpCreateAdminGenderMale.setVisible(false);
            cmbCmpGender.setVisible(false);
            hbCmpCreateAdminUsername.setVisible(false);
            tfCmpCreateAdminUsername.setVisible(false);
            hbCmpCreateAdminPassword.setVisible(false);
            tfCmpCreateAdminPassword.setVisible(false);
            hideShow.setVisible(false);
            lblCmpCreateMainUserControl.setVisible(false);
            tfCmpCreateUserControlLine.setVisible(false);
            hbCmpCreateUserControl.setVisible(false);
        }
    }

    //TODO: License Expiry Date
    public void licenseExpiryDateFun() {
        String licenseExpDateString = tfCmpCreateLicenseExpDate.getText();
        if (licenseExpDateString.isEmpty()) {
            // Handle case where input is empty
            return;
        }

        LocalDate selectedDate = null;
        try {
            // Parse the date string with the correct format "dd/MM/yyyy"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            selectedDate = LocalDate.parse(licenseExpDateString, formatter);
        } catch (DateTimeParseException e) {
            // Handle parsing error
            e.printStackTrace(); // Or handle it in a meaningful way
            return;
        }


        LocalDate currentDate = LocalDate.now();

        if (selectedDate.isBefore(currentDate)) {
            AlertUtility.AlertWarningTimeout("Warning", "Expiry Date should be greater than Current Date", input -> {
//                if (input) {
                    tfCmpCreateLicenseExpDate.requestFocus();
//                }
            });
        } else {
            long daysDifference = ChronoUnit.DAYS.between(currentDate, selectedDate);
            if (daysDifference <= 90) {
                AlertUtility.AlertWarningTimeout("Warning", "License getting expired in " + daysDifference + " Days.", input -> {
//                    if (input) {
//                    }
                });
            }
        }
    }

    //TODO: Fssai Expiry Date
    public void fssaiLicenseExpiryDateFun() {
        String fssaiLicenseExpDateString = tfCmpCreateFSSAINoExpDate.getText();
        if (fssaiLicenseExpDateString.isEmpty()) {
            // Handle case where input is empty
            return;
        }

        LocalDate selectedDate = null;
        try {
            // Parse the date string with the correct format "dd/MM/yyyy"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            selectedDate = LocalDate.parse(fssaiLicenseExpDateString, formatter);
        } catch (DateTimeParseException e) {
            // Handle parsing error
            e.printStackTrace(); // Or handle it in a meaningful way
            return;
        }


        LocalDate currentDate = LocalDate.now();

        if (selectedDate.isBefore(currentDate)) {
            AlertUtility.AlertWarningTimeout("Warning", "Expiry Date should be greater than Current Date", input -> {
//                if (input) {
                    tfCmpCreateFSSAINoExpDate.requestFocus();
//                }
            });
        } else {
            long daysDifference = ChronoUnit.DAYS.between(currentDate, selectedDate);
            if (daysDifference <= 90) {
                AlertUtility.AlertWarningTimeout("Warning", "License getting expired in " + daysDifference + " Days.", input -> {
//                    if (input) {
                        // Handle action after alert confirmation, if needed
//                    }
                });
            }
        }
    }

    //TODO: MFG License Expiry Date
    public void mfgExpiryDateFun() {
        String mfgLicenseExpDateString = tfCmpCreateMfgLicenseNoExpDate.getText();
        if (mfgLicenseExpDateString.isEmpty()) {
            // Handle case where input is empty
            return;
        }

        LocalDate selectedDate = null;
        try {
            // Parse the date string with the correct format "dd/MM/yyyy"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            selectedDate = LocalDate.parse(mfgLicenseExpDateString, formatter);
        } catch (DateTimeParseException e) {
            // Handle parsing error
            e.printStackTrace(); // Or handle it in a meaningful way
            return;
        }


        LocalDate currentDate = LocalDate.now();

        if (selectedDate.isBefore(currentDate)) {
            AlertUtility.AlertWarningTimeout("Warning", "Expiry Date should be greater than Current Date", input -> {
//                if (input) {
                    tfCmpCreateMfgLicenseNoExpDate.requestFocus();
//                }
            });
        } else {
            long daysDifference = ChronoUnit.DAYS.between(currentDate, selectedDate);
            if (daysDifference <= 90) {
                AlertUtility.AlertWarningTimeout("Warning", "License getting expired in " + daysDifference + " Days.", input -> {
//                    if (input) {
                        // Handle action after alert confirmation, if needed
//                    }
                });
            }
        }
    }

    //TODO: Birth Date
    public void birthDateFun() {
        String birthDateString = tfCmpCreateAdminBirthDate.getText();
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

        if (selectedDate.isAfter(LocalDate.now())) {
            AlertUtility.AlertWarningTimeout("Warning", "Select Valid Date", input -> {
//                if (input) {
                    tfCmpCreateAdminBirthDate.requestFocus();
//                }
            });
        }
    }

    //TODO: Birth Date
    public void gstApplicableDate() {
        String birthDateString = tfCmpCreateApplicableDate.getText();
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

        if (selectedDate.isAfter(LocalDate.now())) {
            AlertUtility.AlertWarningTimeout("Warning", "Select Valid Date", input -> {
//                if (input) {
                    tfCmpCreateApplicableDate.requestFocus();
//                }
            });
        }
    }


    //TODO: Get the country Details ****
    public void getCountryFun() {
        APIClient apiClient = null;
        try {
            logger.debug("Get Country Data Started...");
            apiClient = new APIClient(EndPoints.GET_COUNTRY_DATA_ENDPOINT, "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject != null) {
                        ObservableList<CompanyCountryDTO> list = FXCollections.observableArrayList();
                        String fullName = jsonObject.get("name").getAsString();
                        String id = jsonObject.get("id").getAsString();
                        list.add(new CompanyCountryDTO(fullName, id));
                        countryMapList.put(id, fullName);
                        cmbCmpCreateRegCountry.setItems(list);

                        cmbCmpCreateRegCountry.getSelectionModel().selectFirst(); // Set the first item as default
                        CompanyCountryDTO defaultSelectedItem = (CompanyCountryDTO) cmbCmpCreateRegCountry.getSelectionModel().getSelectedItem();
                        String defaultSelectedId = defaultSelectedItem.getId();
                        countryId = Long.valueOf(defaultSelectedId);

                        cmbCmpCreateRegCountry.setConverter(new StringConverter<CompanyCountryDTO>() {
                            @Override
                            public String toString(CompanyCountryDTO o) {
                                return o.getText();
                            }

                            @Override
                            public CompanyCountryDTO fromString(String s) {
                                return null;
                            }
                        });

                        cmbCmpCreateCorpCountry.setItems(list);
                        cmbCmpCreateCorpCountry.getSelectionModel().selectFirst(); // Set the first item as default
                        cmbCmpCreateCorpCountry.setConverter(new StringConverter<CompanyCountryDTO>() {
                            @Override
                            public String toString(CompanyCountryDTO o) {
                                return o.getText();
                            }

                            @Override
                            public CompanyCountryDTO fromString(String s) {
                                return null;
                            }
                        });
                    } else {
                        //TODO : use logger for alert messages
//                        AlertUtility.AlertDialogForError("WARNING", "Failed To Load Data", input -> {
//                            if (input) {
//                            }
//                        });
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
        } catch (Exception e) {
            e.printStackTrace();
            //TODO : use logger for alert messages
//            showAlert("Falied to connect server! ");
            AlertUtility.AlertDialogForError("WARNING", "Failed to Connect Server", input -> {
                if (input) {
                }
            });
        } finally {
            apiClient = null;
        }
    }

    //TODO: Get The country options *****
    @FXML
    private void handleComboBoxActionCountry() {

        CompanyCountryDTO countryDTO = (CompanyCountryDTO) cmbCmpCreateRegCountry.getSelectionModel().getSelectedItem();
        countryId = Long.valueOf(countryDTO.getId());
        if (countryDTO != null) {

        } else {
        }
    }

    //TODO: Function for get the Currency list
    public void getCurrency() {
        APIClient apiClient = null;
        try {
            logger.debug("Get Country Data Started...");
            apiClient = new APIClient(EndPoints.GET_COUNTRY_DATA_ENDPOINT, "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    String currency = jsonObject.get("currency").getAsString();
                    cmbCmpCreateCurrency.setItems(FXCollections.observableArrayList(currency));
                    cmbCmpCreateCurrency.getSelectionModel().selectFirst();
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getCurrency()" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getCurrency()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO : use logger for alert messages
//            showAlert("Falied to connect server! ");
            AlertUtility.AlertDialogForError("WARNING", "Failed to Connect Server !", input -> {
                if (input) {
                }
            });
        } finally {
            apiClient = null;
        }
    }

    //TODO: Gst Type Function *****
    private void getGSTTypeFun() {
        APIClient apiClient = null;
        try {
            apiClient = new APIClient(EndPoints.GET_GST_TYPE_ENDPOINT, "", RequestType.GET);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray jsonArray = jsonObject.get("responseObject").getAsJsonArray();
                        ObservableList<CompanyGstTypeDTO> list = FXCollections.observableArrayList();
                        for (JsonElement mElement : jsonArray) {
                            JsonObject mObject = mElement.getAsJsonObject();
                            String gstType = mObject.get("gstType").getAsString();
                            String id = mObject.get("id").getAsString();
                            list.add(new CompanyGstTypeDTO(id, gstType));
                            gstTypeMapList.put(id, gstType);
                        }
                        cmbCmpCreateGSTType.setItems(list);
                        cmbCmpCreateGSTType.getSelectionModel().selectFirst();
                        gstTypeId = Long.valueOf(list.get(0).getId());
                        cmbCmpCreateGSTType.setConverter(new StringConverter<CompanyGstTypeDTO>() {

                            @Override
                            public String toString(CompanyGstTypeDTO o) {
                                return o.getType();
                            }

                            @Override
                            public CompanyGstTypeDTO fromString(String s) {
                                return null;
                            }
                        });
                    } else {
                        //TODO : use logger for alert messages
//                        showAlert("Falied to Load Data ");
                        AlertUtility.AlertWarningTimeout("WARNING", "Falied to Load Data", input -> {
//                            if (input) {
//                            }
                        });
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
        } catch (Exception e) {
            e.printStackTrace();
            //TODO : use logger for alert messages
//            showAlert("Falied to connect server! ");
            AlertUtility.AlertDialogForError("WARNING", "Failed to Connect Server !", input -> {
                if (input) {
                }
            });
        } finally {
            apiClient = null;
        }
    }

    //TODO: selecting the gst type options *****
    @FXML
    private void handleComboBoxActionGetGSTType() {

        CompanyGstTypeDTO gstType = (CompanyGstTypeDTO) cmbCmpCreateGSTType.getSelectionModel().getSelectedItem();
        gstTypeId = Long.valueOf(gstType.getId());
        if (gstType != null) {

        } else {
        }
    }

    //Todo: Validate Company Duplicate or not
    public void functionCompanyDuplicate() {
        APIClient apiClient = null;
        try {

            Map<String, String> map = new HashMap<>();
            map.put("companyName", String.valueOf(tfCmpCreateName.getText()));
            String formData = Globals.mapToStringforFormData(map);

            if (companyListDTO == null) {
                apiClient = new APIClient(EndPoints.VALIDATE_COMPANY, formData, RequestType.FORM_DATA);
            } else {
                apiClient = new APIClient(EndPoints.VALIDATE_COMPANY_UPDATE, formData, RequestType.FORM_DATA);
            }

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

                    if (jsonObject.get("responseStatus").getAsInt() == 409) {
                        message = jsonObject.get("message").getAsString();
                        AlertUtility.AlertWarningTimeout("WARNING", message, input -> {
//                            if (input) {
                            tfCmpCreateName.requestFocus();
//                            }
                        });
                    } else if (tfCmpCreateName.getText().isEmpty()) {
//                        String message = jsonObject.get("message").getAsString();
                        AlertUtility.AlertWarningTimeout("WARNING", "Enter Company Name", input -> {
//                            if (input) {
                            tfCmpCreateName.requestFocus();
//                            }
                        });

                    }/* else if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        Platform.runLater(() -> {
                            rbCmpCreateManufacturer.requestFocus(); // Set focus back to tfFranchiseCreateRPincode
                        });
                    }*/
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in functionCompanyDuplicate()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in functionCompanyDuplicate()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO : use logger for alert messages
//            showAlert("Falied to connect server! ");
            AlertUtility.AlertDialogForError("WARNING", "Failed to Connect Server !", input -> {
                if (input) {
                }
            });
        } finally {
            apiClient = null;
        }

    }

    // Todo: Validate user
    public void functionToValidateUser() {
        APIClient apiClient = null;
        try {

            Map<String, String> map = new HashMap<>();
            map.put("userCode", String.valueOf(tfCmpCreateAdminUsername.getText()));
            String formData = Globals.mapToStringforFormData(map);

            apiClient = new APIClient(EndPoints.VALIDATE_COMPANY_USER, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 409) {
                        message = jsonObject.get("message").getAsString();

                        AlertUtility.AlertDialogForError("WARNING", message, input -> {
                            if (input) {
                                tfCmpCreateAdminUsername.requestFocus();
                            }
                        });
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in functionToValidateUser()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in functionToValidateUser()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO : use logger for alert messages
//            showAlert("Falied to connect server! ");
            AlertUtility.AlertDialogForError("WARNING", "Failed to Connect Server !", input -> {
                if (input) {
                }
            });
        } finally {
            apiClient = null;
        }
    }

    //TODO: Function to get the system configs
    private void getAllMasterSystemConfigFun() {
        APIClient apiClient = null;
        try {
            Map<String, String> map = new HashMap<>();
            String formData = Globals.mapToStringforFormData(map);
            apiClient = new APIClient(EndPoints.GET_ALL_MASTER_SYSTEM_CONFIG, formData, RequestType.JSON);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {

                        JsonArray jsonArray = jsonObject.getAsJsonArray("responseObject");

                        Type userListType = new TypeToken<ArrayList<CompanySystemConfigDTO>>() {
                        }.getType();

                        jsonArraySystemConfig = new Gson().fromJson(jsonArray, userListType);


                        for (int i = 0; i < jsonArray.size(); i++) {

                            jsonArraySystemConfig.get(i).setLabel("");
                            jsonArraySystemConfig.get(i).setValue(0);

                            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                            SwitchButton switchButton = new SwitchButton();
                            Label label = new Label();
                            VBox vBox = new VBox();
                            switchButton.setId(jsonObject.get("display_name").getAsString());
//                            if(jsonObject.get("display_name").getAsString().equalsIgnoreCase("level b") || jsonObject.get("display_name").getAsString().equalsIgnoreCase("level c"))
//                                switchButton.setDisable(true);
                            label.setText(jsonObject.get("display_name").getAsString());
                            HBox hBox = new HBox();
                            switchButton.setParentBox(vBox);
                            vBox.getChildren().add(switchButton);
                            hBox.getChildren().addAll(vBox, label);

                            vBox.setStyle("-fx-background-color: #DAEBF5; -fx-background-radius: 4; -fx-border-radius: 4; -fx-border-color: #f8f4fc; -fx-border-width: 2;");
                            hBox.setStyle("-fx-background-color: #DAEBF5;-fx-background-radius:4px;-fx-border-radius:4px;-fx-border-color:#E3E3E3");
                            hBox.setPadding(new Insets(8, 8, 8, 8));
                            hBox.setSpacing(8);
                            hbCmpCreateUserControl.getChildren().add(hBox);
                            switchButton.switchOnProperty().addListener((observable, oldValue, newValue) -> {
                                if (newValue) {

                                    for (int s = 0; s < jsonArraySystemConfig.size(); s++) {
                                        if (jsonArraySystemConfig.get(s).getDisplay_name().equalsIgnoreCase(switchButton.getId())) {
                                            jsonArraySystemConfig.get(s).setValue(1);
                                        }
                                    }
                                } else {
                                    for (int s = 0; s < jsonArraySystemConfig.size(); s++) {
                                        if (jsonArraySystemConfig.get(s).getDisplay_name().equalsIgnoreCase(switchButton.getId())) {
                                            jsonArraySystemConfig.get(s).setValue(0);
                                        }
                                    }
                                }

                                if (jsonObject.get("is_label").getAsBoolean()) {
                                    int index = hbCmpCreateUserControl.getChildren().indexOf(switchButton.getParent().getParent());
                                    System.out.println("newValue " + newValue);
                                    if (newValue) {
                                        if (switchButton.getId().equalsIgnoreCase("Multi Units")) {
                                            ComboBox comboBox = new ComboBox();
                                            showDropDownOnKeyPress(comboBox);
                                            comboBox.paddingProperty().set(new Insets(5, 5, 5, 5));
                                            comboBox.getItems().add("1");
                                            comboBox.getItems().add("2");
                                            comboBox.getItems().add("3");
                                            comboBox.getItems().add("4");
                                            comboBox.getSelectionModel().selectFirst();
                                            hbCmpCreateUserControl.getChildren().add((index + 1), comboBox);
                                        } else if (switchButton.getId().equalsIgnoreCase("Level A") || switchButton.getId().equalsIgnoreCase("Level B") || switchButton.getId().equalsIgnoreCase("Level C")) {
                                            TextField textField = new TextField();
                                            textField.setId("txt_" + switchButton.getId().trim());
                                            textField.setPromptText("Enter");

                                            if (hbCmpCreateUserControl.getChildren().get(index + 1) instanceof TextField) {

                                            } else {
                                                hbCmpCreateUserControl.getChildren().add((index + 1), textField);
                                            }

                                            if (switchButton.getId().equalsIgnoreCase("Level A"))
                                                tfLevelA = textField;
                                            if (switchButton.getId().equalsIgnoreCase("Level B"))
                                                tfLevelB = textField;
                                            if (switchButton.getId().equalsIgnoreCase("Level C"))
                                                tfLevelC = textField;


                                            textField.textProperty().addListener((obv, oValue, nValue) -> {
                                                if (nValue.isEmpty()) {
                                                    switchButton.setDisable(false);
                                                } else {
                                                    switchButton.setDisable(true);
                                                }
                                                updateLabel(switchButton.getId(), nValue);
                                            });
                                        }


//                                        for (Node node : hbCmpCreateUserControl.getChildren()) {
//                                            if (node instanceof TextField) {
//                                                TextField textField = (TextField) node;
//                                                if (textField.getText().trim().isEmpty()) {
//                                                    textField.requestFocus();
//
//                                                    // Add a listener to restrict focus until something is entered
//                                                    textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
//                                                        if (!isNowFocused && textField.getText().trim().isEmpty()) {
//                                                            textField.requestFocus();
//                                                        }
//                                                    });
//                                                    break;
//                                                }
//                                            }
//                                        }


                                    } else if (switchButton.getId().equalsIgnoreCase("Level A") || switchButton.getId().equalsIgnoreCase("Level B") || switchButton.getId().equalsIgnoreCase("Level C") || switchButton.getId().equalsIgnoreCase("Level A") || switchButton.getId().equalsIgnoreCase("Level B") || switchButton.getId().equalsIgnoreCase("Multi Units")) {
                                        hbCmpCreateUserControl.getChildren().remove(index + 1);
                                        updateLabel(switchButton.getId(), "");
                                    }
                                }
                            });
                        }

                    } else {
                        //TODO : use logger for alert messages
//                        showAlert("Falied to Load Data ");
//                        AlertUtility.AlertDialogForError("WARNING", "Failed to Load Data !", input -> {
//                            if (input) {
//                            }
//                        });
                    }

                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getAllMasterSystemConfigFun()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getAllMasterSystemConfigFun()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO : use logger for alert messages
//            showAlert("Falied to connect server! ");
            AlertUtility.AlertDialogForError("WARNING", "Failed to Connect Server !", input -> {
                if (input) {
                }
            });
        } finally {
            apiClient = null;
        }
    }

    //TODO: for getting the config check label
    private void updateLabel(String key, String nValue) {
        for (int i = 0; i < jsonArraySystemConfig.size(); i++) {
            if (jsonArraySystemConfig.get(i).getDisplay_name().equalsIgnoreCase(key)) {
                jsonArraySystemConfig.get(i).setLabel(nValue);
            }
        }
    }

    //TODO: function for gender radio buttons
    public void handleGenderRadioButtonAction() {
        RadioButton selectedGenderRadioButton = (RadioButton) genderToggle.getSelectedToggle();
        rbCmpCreateAdminGenderMale.setToggleGroup(genderToggle);
        rbCmpCreateAdminGenderFemale.setToggleGroup(genderToggle);

        if (selectedGenderRadioButton != null) {
            selectedGenderRadioValue = selectedGenderRadioButton.getText();
        }
    }

    //TODO: function for Trade radio buttons
    public void handleTradeRadioButtonsAction() {
        RadioButton selectedTradeRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        rbCmpCreateManufacturer.setToggleGroup(toggleGroup);
        rbCmpCreateDistributor.setToggleGroup(toggleGroup);
        rbCmpCreateRetailer.setToggleGroup(toggleGroup);

        if (selectedTradeRadioButton != null) {
            selectedTradeRadioValue = selectedTradeRadioButton.getText();
        }
    }

    //TODO: Business Type options
    public void getBusinessType() {
        // Create key-value pairs and add them to the ComboBox
        ObservableList<CompanyDTO> items = FXCollections.observableArrayList(
                new CompanyDTO("Proprietor", "1"),
                new CompanyDTO("Partner", "2"),
                new CompanyDTO("Trust", "3"),
                new CompanyDTO("Other", "4")
        );
        cmbCmpCreateBusinessType.setItems(items);

        cmbCmpCreateBusinessType.setConverter(new StringConverter<CompanyDTO>() {
            @Override
            public String toString(CompanyDTO o) {
                return o.getText();
            }

            @Override
            public CompanyDTO fromString(String s) {
                return null;
            }
        });

    }

    //TODO: Business type comboBox DTO function
    @FXML
    private void handleComboBoxActionBusinessType() {

        CompanyDTO companyDTO = (CompanyDTO) cmbCmpCreateBusinessType.getSelectionModel().getSelectedItem();
        //String selectedItem = String.valueOf(cmbFranchiseCreateDistrictHead.getSelectionModel().getSelectedItem());
        if (companyDTO != null) {
            businessTypeId = Long.valueOf(companyDTO.getId());
//            getDistrictHeadsData();
        } else {
            System.out.println("No item selected.");
        }
    }

    //TODO: get the area data and validaate of Registered pincode
    public void getAddressDataByPincode() {
        APIClient apiClient = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("pincode", String.valueOf(tfCmpCreateRegPincode.getText()));
            String formData = Globals.mapToStringforFormData(map);
            apiClient = new APIClient(EndPoints.GET_PINCODE_ENDPOINT, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
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

                            tfCmpCreateGSTIN.setText(stateCode);

                            lblCmpCreateRegCityName.setText(district);
                            lblCmpCreateRegStateName.setText(state);
                            lblCmpCreateRegCityName.setVisible(true);
                            lblCmpCreateRegStateName.setVisible(true);

                            if (cbCmpCreateSameAsAddress.isSelected()) {
                                lblCmpCreateCorpCityName.setText(district);
                                lblCmpCreateCorpStateName.setText(state);
                            }

                            String id = mObject.get("id").getAsString();
                            list.add(new CompanyAreaDTO(area, id, pincode, district, state, stateCode));
                            AreaMapList.put(id, area);
                        }
                        cmbCmpCreateRegArea.setItems(list);
                        cmbCmpCreateRegArea.getSelectionModel().selectFirst();
                        cmbCmpCreateRegArea.setMaxWidth(200);
                        cmbCmpCreateRegArea.setConverter(new StringConverter<CompanyAreaDTO>() {
                            @Override
                            public String toString(CompanyAreaDTO o) {
                                return o.getArea();
                            }

                            @Override
                            public CompanyAreaDTO fromString(String s) {
                                return null;
                            }
                        });
                        if (cbCmpCreateSameAsAddress.isSelected()) {
                            cmbCmpCreateCorpArea.setItems(list);
                            cmbCmpCreateCorpArea.getSelectionModel().selectFirst();
                            cmbCmpCreateCorpArea.setMaxWidth(200);
                            cmbCmpCreateCorpArea.setConverter(new StringConverter<CompanyAreaDTO>() {
                                @Override
                                public String toString(CompanyAreaDTO o) {
                                    return o.getArea();
                                }

                                @Override
                                public CompanyAreaDTO fromString(String s) {
                                    return null;
                                }
                            });
                        }

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


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    //TODO: Validate Pincode for Registered Pincode
    public void validatePincode() {
        System.out.println("i am in");
        APIClient apiClient = null;
        try {
            logger.debug("Get ValidatePincode Data Started...");
            Map<String, String> map = new HashMap<>();
            map.put("pincode", String.valueOf(tfCmpCreateRegPincode.getText()));
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
                        AlertUtility.AlertWarningTimeout("WARNING", message, input -> {
//                            if (input) {
                            tfCmpCreateRegPincode.requestFocus();
//                            }
                        });
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

    //TODO: Registered Area ComboBox Options ******
    @FXML
    private void handleComboBoxActionArea() {

        CompanyAreaDTO companyAreaDTO = (CompanyAreaDTO) cmbCmpCreateRegArea.getSelectionModel().getSelectedItem();
        //String selectedItem = String.valueOf(cmbFranchiseCreateDistrictHead.getSelectionModel().getSelectedItem());
        if (companyAreaDTO != null) {
            areaId = Long.valueOf(companyAreaDTO.getId());
            stateCode = companyAreaDTO.getStateCode();
            cmbCmpCreateRegArea.setValue(companyAreaDTO);


            districtName = String.valueOf(companyAreaDTO.getArea());

//            lblCmpCreateRegCityName.setVisible(true);
//            lblCmpCreateRegStateName.setVisible(true);
//
//            lblCmpCreateRegCityName.setText(companyAreaDTO.getDistrict());
//            lblCmpCreateRegStateName.setText(companyAreaDTO.getState());

            if (cbCmpCreateSameAsAddress.isSelected()) {
                setSameAsAddress();
            }

        } else {
            System.out.println("No item selected.");
        }
    }

    //TODO: get the area data of Corporate Address
    public void getCorporateAddressDataByPincode() {
        APIClient apiClient = null;
        try {

            Map<String, String> map = new HashMap<>();
            map.put("pincode", String.valueOf(tfCmpCreateCorpPincode.getText()));
            String formData = Globals.mapToStringforFormData(map);

            apiClient = new APIClient(EndPoints.GET_PINCODE_ENDPOINT, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray jsonArray = jsonObject.get("responseObject").getAsJsonArray();
                        listC.clear();
                        for (JsonElement mElement : jsonArray) {
                            JsonObject mObject = mElement.getAsJsonObject();
                            String area = mObject.get("area").getAsString();
                            String district = mObject.get("district").getAsString();
                            String state = mObject.get("state").getAsString();
                            String stateCode = mObject.get("stateCode").getAsString();
                            String pincode = mObject.get("pincode").getAsString();


                            lblCmpCreateCorpCityName.setText(district);
                            lblCmpCreateCorpStateName.setText(state);
                            lblCmpCreateCorpCityName.setVisible(true);
                            lblCmpCreateCorpStateName.setVisible(true);


                            String id = mObject.get("id").getAsString();
                            listC.add(new CompanyAreaDTO(area, id, pincode, district, state, stateCode));
                            AreaMapList.put(id, area);
                        }
                        cmbCmpCreateCorpArea.setItems(listC);
                        cmbCmpCreateCorpArea.getSelectionModel().selectFirst();
                        cmbCmpCreateCorpArea.setMaxWidth(200);
                        cmbCmpCreateCorpArea.setConverter(new StringConverter<CompanyAreaDTO>() {
                            @Override
                            public String toString(CompanyAreaDTO o) {
                                return o.getArea();
                            }

                            @Override
                            public CompanyAreaDTO fromString(String s) {
                                return null;
                            }
                        });
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getCorporateAddressDataByPincode()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getCorporateAddressDataByPincode()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    //TODO: validate pincode of corporate
    public void validateCorpPincode() {
        APIClient apiClient = null;
        try {
            logger.debug("Get ValidatePincode Data Started...");
            Map<String, String> map = new HashMap<>();
            map.put("pincode", String.valueOf(tfCmpCreateCorpPincode.getText()));
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
                                tfCmpCreateCorpPincode.requestFocus();
                            }
                        });
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in validateCorpPincode()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in validateCorpPincode()" + workerStateEvent.getSource().getValue().toString());
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

    //TODO: Corporate Address Area ComboBox Options
    @FXML
    private void handleComboBoxCorpActionArea() {

        CompanyAreaDTO companyCorpArea = (CompanyAreaDTO) cmbCmpCreateCorpArea.getSelectionModel().getSelectedItem();
        //String selectedItem = String.valueOf(cmbFranchiseCreateDistrictHead.getSelectionModel().getSelectedItem());
        if (companyCorpArea != null) {
            corpAreaId = Long.valueOf(companyCorpArea.getId());
            corpStateCode = companyCorpArea.getStateCode();
            cmbCmpCreateCorpArea.setValue(companyCorpArea);

            districtName = String.valueOf(companyCorpArea.getArea());

//            lblCmpCreateCorpCityName.setVisible(true);
//            lblCmpCreateCorpStateName.setVisible(true);
//
//            lblCmpCreateCorpCityName.setText(companyCorpArea.getDistrict());
//            lblCmpCreateCorpStateName.setText(companyCorpArea.getState());

        } else {
            System.out.println("No item selected.");
        }
    }


    //TODO: Same As Registered Address ****
    public void setSameAsAddress() {
        if (cbCmpCreateSameAsAddress.isSelected()) {
            tfCmpCreateCorpAddress.setText(tfCmpCreateRegAddress.getText());
            tfCmpCreateCorpPincode.setText(tfCmpCreateRegPincode.getText());
            cmbCmpCreateCorpArea.setItems(cmbCmpCreateRegArea.getItems());
            cmbCmpCreateCorpArea.setValue(cmbCmpCreateRegArea.getValue());
            cmbCmpCreateCorpArea.setMaxWidth(200);
            String corpCityName = lblCmpCreateRegCityName.getText();
            String corpStateName = lblCmpCreateRegStateName.getText();
            System.out.println("xity and state" + corpCityName + corpStateName);
            lblCmpCreateCorpCityName.setText(lblCmpCreateRegCityName.getText());
            lblCmpCreateCorpStateName.setText(lblCmpCreateRegStateName
                    .getText());
            tfCmpCreateCorpAddress.setDisable(true);
            tfCmpCreateCorpPincode.setDisable(true);
            cmbCmpCreateCorpArea.setDisable(true);
            lblCmpCreateCorpCityName.setDisable(true);
            lblCmpCreateCorpStateName.setDisable(true);
            cmbCmpCreateCorpCountry.setDisable(true);
            //            CompanyAreaDTO areaDTO = (CompanyAreaDTO) cmbCmpCreateRegArea.getSelectionModel().getSelectedItem();
//            if (areaDTO != null) {
//                String areaId = areaDTO.getId();
//                CompanyAreaDTO selectedArea = null;
//                for (Object obj : cmbCmpCreateCorpArea.getItems()) {
//                    CompanyAreaDTO districtHeadDTO = (CompanyAreaDTO) obj;
//                    if (districtHeadDTO.getId().equals(areaId)) {
//                        selectedArea = districtHeadDTO;
//                        break;
//                    }
//                }
//                if (selectedArea != null) {
//                    cmbCmpCreateCorpArea.getSelectionModel().select(selectedArea);
//                }
//            }
            cmbCmpCreateCorpArea.setConverter(new StringConverter<CompanyAreaDTO>() {
                @Override
                public String toString(CompanyAreaDTO o) {
                    return o.getArea();
                }

                @Override
                public CompanyAreaDTO fromString(String s) {
                    return null;
                }
            });
        } else {
            tfCmpCreateCorpAddress.setText("");
            tfCmpCreateCorpAddress.setDisable(false);
            tfCmpCreateCorpPincode.setText("");
            tfCmpCreateCorpPincode.setDisable(false);
            cmbCmpCreateCorpArea.getSelectionModel().clearSelection();
            cmbCmpCreateCorpArea.setDisable(false);
            lblCmpCreateCorpCityName.setText("");
            lblCmpCreateCorpCityName.setDisable(false);
            lblCmpCreateCorpStateName.setText("");
            lblCmpCreateCorpStateName.setDisable(false);
            cmbCmpCreateCorpCountry.setDisable(false);
        }
    }

    //TODO: Create Compnay api
    public void createCompanyFun() {
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                APIClient apiClient = null;
                try {
                    Map<String, String> req = new HashMap<>();
                    req.put("companyCode", tfCmpCreateCompanyCode.getText());
                    req.put("companyName", tfCmpCreateName.getText());
                    req.put("tradeOfBusiness", tradeOptions);
                    req.put("businessType", businessTypeId.toString());
                    req.put("registeredAddress", tfCmpCreateRegAddress.getText());
                    req.put("pincode", tfCmpCreateRegPincode.getText());
                    req.put("area", areaId.toString());
                    req.put("city", lblCmpCreateRegCityName.getText());
                    req.put("state", lblCmpCreateRegStateName.getText());
                    req.put("stateCode", stateCode.toString());
                    req.put("countryId", countryId.toString());
                    req.put("sameAsAddress", String.valueOf(cbCmpCreateSameAsAddress.isSelected()));
                    req.put("corporateAddress", tfCmpCreateCorpAddress.getText());
                    req.put("corporatePincode", tfCmpCreateCorpPincode.getText());
                    req.put("corporatearea", corpAreaId.toString());
                    req.put("userRole", "CADMIN");
                    req.put("corporatecity", lblCmpCreateCorpCityName.getText());
                    req.put("corporatestate", lblCmpCreateCorpStateName.getText());
                    req.put("corporatestateCode", corpStateCode.toString());
                    req.put("licenseNo", tfCmpCreateLicenseNo.getText());
                    if (tfCmpCreateLicenseExpDate != null && tfCmpCreateLicenseExpDate.getText() != null && !tfCmpCreateLicenseExpDate.getText().isEmpty()) {
                        req.put("licenseExpiryDate", Communicator.text_to_date.fromString(tfCmpCreateLicenseExpDate.getText()).toString());
                    }
                    req.put("foodLicenseNo", tfCmpCreateFSSAINo.getText());
                    if (tfCmpCreateFSSAINoExpDate != null && tfCmpCreateFSSAINoExpDate.getText() != null && !tfCmpCreateFSSAINoExpDate.getText().isEmpty()) {
                        req.put("foodLicenseExpiryDate", Communicator.text_to_date.fromString(tfCmpCreateFSSAINoExpDate.getText()).toString());
                    }
                    req.put("manufacturingLicenseNo", tfCmpCreateMfgLicenseNo.getText());
                    if (tfCmpCreateMfgLicenseNoExpDate != null && tfCmpCreateMfgLicenseNoExpDate.getText() != null && !tfCmpCreateMfgLicenseNoExpDate.getText().isEmpty()) {
                        req.put("manufacturingLicenseExpiry", Communicator.text_to_date.fromString(tfCmpCreateMfgLicenseNoExpDate.getText()).toString());
                    }
                    req.put("website", tfCmpCreateWebsite.getText());
                    req.put("email", tfCmpCreateEmail.getText());
                    req.put("mobileNumber", tfCmpCreateMobileNo.getText() != null ? tfCmpCreateMobileNo.getText() : "");
                    req.put("whatsappNumber", tfCmpCreateWhatsappNo.getText() != null ? tfCmpCreateWhatsappNo.getText() : "");
                    req.put("currency", String.valueOf(cmbCmpCreateCurrency.getValue()));
                    req.put("gstApplicable", String.valueOf(isGSTApplicable));
                    req.put("gstIn", tfCmpCreateGSTIN.getText());
                    req.put("gstType", gstTypeId.toString());
                    if (tfCmpCreateApplicableDate != null && tfCmpCreateApplicableDate.getText() != null && !tfCmpCreateApplicableDate.getText().isEmpty()) {
                        req.put("gstApplicableDate", Communicator.text_to_date.fromString(tfCmpCreateApplicableDate.getText()).toString());
                    }
                    req.put("isMultiBranch", String.valueOf(isMultiBranch));
                    req.put("fullName", tfCmpCreateFullName.getText());
                    req.put("emailId", tfCmpCreateAdminEmail.getText());
                    req.put("contactNumber", tfCmpCreateAdminMobileNo.getText());
                    if (tfCmpCreateAdminBirthDate != null && tfCmpCreateAdminBirthDate.getText() != null && !tfCmpCreateAdminBirthDate.getText().isEmpty()) {
                        req.put("userDob", Communicator.text_to_date.fromString(tfCmpCreateAdminBirthDate.getText()).toString());
                    }
                    req.put("gender", genderOptions);
                    req.put("usercode", tfCmpCreateAdminUsername.getText());
                    req.put("password", tfCmpCreateAdminPassword.getText());
                    String userControlData = new Gson().toJson(jsonArraySystemConfig);
                    req.put("userControlData", userControlData);
                    Map<String, String> headers = new HashMap<>();
                    headers.put("branch", Globals.headerBranch);
                    Map<String, File> fileMap = null;
                    if (selectedFile != null) {
                        fileMap = new HashMap<>();
                        fileMap.put("uploadImage", selectedFile);
                    }

                    apiClient = new APIClient(EndPoints.CREATE_COMPANY_MULTIPART, req, headers, fileMap, RequestType.MULTI_PART);
                    //? HIGHLIGHT
                    CompanyListController.isNewCompanyCreated = true; //? Set the flag for new creation
                    apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent workerStateEvent) {
                            jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                            message = jsonObject.get("message").getAsString();
                            int status = jsonObject.get("responseStatus").getAsInt();
                            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                                if (status == 200) {
                                    AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                                        GlobalSadminController.getInstance().addTabStatic(COMPANY_LIST_SLUG, false);
                                    });
                                } else {
                                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                                    });

                                }
//                        clearFieldsAfterSubmit();
                            }


                        }
                    });
                    apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent workerStateEvent) {
                            logger.error("Network API cancelled in createCompanyFun()" + workerStateEvent.getSource().getValue().toString());

                        }
                    });
                    apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent workerStateEvent) {
                            logger.error("Network API failed in createCompanyFun()" + workerStateEvent.getSource().getValue().toString());

                        }
                    });
                    apiClient.start();

                } catch (Exception e) {
                    System.out.println("exception:" + e);
                } finally {
                    apiClient = null;
                }
            } else {
                System.out.println("working!");
            }

        };
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Create " + tfCmpCreateName.getText(), callback);
    }

    //TODO: Update Company api
    public void updateCompanyFun() {
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                APIClient apiClient = null;
                try {
                    String id = Globals.companyListDTO != null ? Globals.companyListDTO.getId() : "";
                    Map<String, String> req = new HashMap<>();
                    req.put("id", id);
                    req.put("companyCode", tfCmpCreateCompanyCode.getText());
                    req.put("companyName", tfCmpCreateName.getText());
                    req.put("tradeOfBusiness", tradeOptions);
                    req.put("natureOfBusiness", businessTypeId.toString());
                    req.put("registeredAddress", tfCmpCreateRegAddress.getText());
                    req.put("pincode", tfCmpCreateRegPincode.getText());
                    req.put("city", lblCmpCreateRegCityName.getText());
                    req.put("area", areaId.toString());
                    req.put("stateName", lblCmpCreateRegStateName.getText());
                    req.put("stateCode", stateCodeById);
                    req.put("userRole", "CADMIN");
                    req.put("countryId", countryId.toString());
                    req.put("sameAsAddress", String.valueOf(cbCmpCreateSameAsAddress.isSelected()));
                    req.put("corporateAddress", tfCmpCreateCorpAddress.getText());
                    req.put("corporatePincode", tfCmpCreateCorpPincode.getText());
                    req.put("corporatearea", corpAreaId.toString());
                    req.put("corporatecity", lblCmpCreateCorpCityName.getText());
                    req.put("corporatestateName", lblCmpCreateCorpStateName.getText());
                    req.put("corporatestateCode", corporateStateCodeById);
                    req.put("licenseNo", tfCmpCreateLicenseNo.getText());
                    if (tfCmpCreateLicenseExpDate != null && tfCmpCreateLicenseExpDate.getText() != null && !tfCmpCreateLicenseExpDate.getText().isEmpty()) {
                        req.put("licenseExpiryDate", Communicator.text_to_date.fromString(tfCmpCreateLicenseExpDate.getText()).toString());
                    }
                    req.put("foodLicenseNo", tfCmpCreateFSSAINo.getText());
                    if (tfCmpCreateFSSAINoExpDate != null && tfCmpCreateFSSAINoExpDate.getText() != null && !tfCmpCreateFSSAINoExpDate.getText().isEmpty()) {
                        req.put("foodLicenseExpiryDate", Communicator.text_to_date.fromString(tfCmpCreateFSSAINoExpDate.getText()).toString());
                    }
                    req.put("manufacturingLicenseNo", tfCmpCreateMfgLicenseNo.getText());
                    if (tfCmpCreateMfgLicenseNoExpDate != null && tfCmpCreateMfgLicenseNoExpDate.getText() != null && !tfCmpCreateMfgLicenseNoExpDate.getText().isEmpty()) {
                        req.put("manufacturingLicenseExpiry", Communicator.text_to_date.fromString(tfCmpCreateMfgLicenseNoExpDate.getText()).toString());
                    }
                    req.put("website", tfCmpCreateWebsite.getText());
                    req.put("email", tfCmpCreateEmail.getText());
                    req.put("mobileNumber", tfCmpCreateMobileNo.getText() != null ? tfCmpCreateMobileNo.getText() : "");
                    req.put("whatsappNumber", tfCmpCreateWhatsappNo.getText() != null ? tfCmpCreateWhatsappNo.getText() : "");
                    req.put("currency", String.valueOf(cmbCmpCreateCurrency.getValue()));
                    req.put("gstApplicable", String.valueOf(isGSTApplicable));
                    req.put("gstIn", tfCmpCreateGSTIN.getText());
                    req.put("gstType", gstTypeId.toString());
                    if (tfCmpCreateApplicableDate != null && tfCmpCreateApplicableDate.getText() != null && !tfCmpCreateApplicableDate.getText().isEmpty()) {
                        req.put("gstApplicableDate", Communicator.text_to_date.fromString(tfCmpCreateApplicableDate.getText()).toString());
                    }
                    req.put("isMultiBranch", String.valueOf(isMultiBranch));
                    Map<String, String> headers = new HashMap<>();
                    headers.put("branch", Globals.headerBranch);
                    Map<String, File> fileMap = null;
                    if (selectedFile != null) {
                        fileMap = new HashMap<>();
                        fileMap.put("uploadImage", selectedFile);
                    }

                    apiClient = new APIClient(EndPoints.UPDATE_COMPANY_MULTIPART, req, headers, fileMap, RequestType.MULTI_PART);
                    //? HIGHLIGHT
                    CompanyListController.editedCompanyId = id; //? Set the ID for editing
                    apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent workerStateEvent) {
                            jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                            message = jsonObject.get("message").getAsString();
                            int status = jsonObject.get("responseStatus").getAsInt();
                            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                                if (status == 200) {
                                    AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                                        GlobalSadminController.getInstance().addTabStatic(COMPANY_LIST_SLUG, false);
                                    });
                                } else {
                                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                                    });

                                }
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

                } catch (Exception e) {
                    System.out.println("exception:" + e);
                } finally {
                    apiClient = null;
                }
            } else {
                System.out.println("working!");
            }
        };
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Update " + tfCmpCreateName.getText(), callback);
    }

    //TODO: Get the company Edit Data
    private void getCompanyEditDataById() {
        APIClient apiClient = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("id", Globals.companyListDTO.getId());
            String formData = Globals.mapToStringforFormData(map);
            apiClient = new APIClient(EndPoints.GET_COMPANY_BY_ID, formData, RequestType.FORM_DATA);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

                    JsonObject jObject = jsonObject.getAsJsonObject("responseObject").getAsJsonObject();

                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        tfCmpCreateCompanyCode.setText(jObject.get("companyCode").getAsString());
                        tfCmpCreateName.setText(jObject.get("companyName").getAsString());

                        //todo: Trade of Business
                        tradeOptions = jObject.get("tradeOfBusiness").getAsString();
                        String genn = jObject.get("tradeOfBusiness").getAsString();
                        if (tradeOptions.equalsIgnoreCase("manufacturer")) {
                            cmbCmpTrade.getSelectionModel().select(genn);
                        } else if (tradeOptions.equalsIgnoreCase("distributor")) {
                            cmbCmpTrade.getSelectionModel().select(genn);
                        } else if (tradeOptions.equalsIgnoreCase("retailer")) {
                            cmbCmpTrade.getSelectionModel().select(genn);
                        }

                        //todo: Business Type
                        String natureBusinessId = jObject.get("natureOfBusiness").getAsString();
                        businessTypeId = (jObject.get("natureOfBusiness").getAsLong());
                        CompanyDTO selectedBusinessType = null;
                        for (Object obj : cmbCmpCreateBusinessType.getItems()) {
                            CompanyDTO businessType = (CompanyDTO) obj;
                            if (businessType.getId().equals(natureBusinessId)) {
                                selectedBusinessType = businessType;
                                break;
                            }
                        }
                        if (selectedBusinessType != null) {
                            cmbCmpCreateBusinessType.getSelectionModel().select(selectedBusinessType);
                        } else {
                        }


                        //todo: upload image
                        try {
                            String imageUrl = jObject.get("uploadImage").getAsString().replace("\\", "/");
                            Image img = new Image(imageUrl, true);
                            img.errorProperty().addListener((obs, oldVal, newVal) -> {
                                if (newVal) {
//                                    System.out.println("Error loading the image");
                                }
                            });

                            // TODO: image label
                            // Create URL object
                            URL url = new URL(imageUrl);
                            // Get the file name
                            String fileName = url.getPath().substring(url.getPath().lastIndexOf("/") + 1);
                            lbCmpCreateSelectedFileName.setText(fileName);
                            //TODO: End image label

                            Platform.runLater(() -> {
                                try {
                                    ivCmpCreateSelectedImage.setImage(img);
                                } catch (Exception e) {
                                    System.out.println("Error setting image to ImageView: " + e.getMessage());
                                }
                            });
                        } catch (Exception e) {
                            System.out.println("Image Exception:" + e.getMessage());
                        }


                        //todo: Registered Area Address
                        tfCmpCreateRegAddress.setText(jObject.get("registeredAddress").getAsString());
                        //todo: Registered Area Pincode
                        tfCmpCreateRegPincode.setText(jObject.get("pincode").getAsString());
                        //todo: Registered Area
                        JsonArray area_list = jObject.get("area_list").getAsJsonArray();
                        ObservableList<CompanyAreaDTO> list = FXCollections.observableArrayList();
                        for (JsonElement mElement : area_list) {
                            JsonObject mObject = mElement.getAsJsonObject();
                            String area = mObject.get("area_name").getAsString();
                            String district = "";
                            String state = "";
                            String stateCode = "";
                            String pincode = "";
                            String id = mObject.get("area_id").getAsString();
                            list.add(new CompanyAreaDTO(area, id, pincode, district, state, stateCode));
                            AreaMapList.put(id, area);
                        }
                        String cmparea = jObject.get("area") != null ? jObject.get("area").getAsString() : "";
                        pinText = jObject.get("pincode").getAsString();
                        cmbCmpCreateRegArea.setItems(list);

                        cmbCmpCreateRegArea.setConverter(new StringConverter<CompanyAreaDTO>() {
                            @Override
                            public String toString(CompanyAreaDTO o) {
                                return o.getArea();
                            }

                            @Override
                            public CompanyAreaDTO fromString(String s) {
                                return null;
                            }
                        });
                        list.stream().filter((v) -> v.getId().equalsIgnoreCase(cmparea)).findAny().ifPresent((p) -> {
                            cmbCmpCreateRegArea.setValue(p);
                        });
                        //Set Area data of Registered by Id
                        /*CompanyAreaDTO selectedAreaHead = null;
                        for (Object obj : cmbCmpCreateRegArea.getItems()) {
                            CompanyAreaDTO regArea = (CompanyAreaDTO) obj;
                            if (regArea.getId().equals(jObject.get("area").getAsString())) {
                                selectedAreaHead = regArea;
                                break;
                            }
                        }*/
                        /*if (selectedAreaHead != null) {
                            areaId = jObject.get("area").getAsLong();
                            cmbCmpCreateRegArea.getSelectionModel().select(selectedAreaHead);
                        }*/
                        //todo: Registered Area City
                        lblCmpCreateRegCityName.setText(jObject.get("city").getAsString());
                        //todo: Registered Area State
                        lblCmpCreateRegStateName.setText(jObject.get("state").getAsString());
                        //todo: Registered Area Country
                        CompanyCountryDTO selectedCountry = null;
                        for (Object obj : cmbCmpCreateRegCountry.getItems()) {
                            CompanyCountryDTO countryDto = (CompanyCountryDTO) obj;
                            if (countryDto.getId().equals(jObject.get("countryId").getAsString())) {
                                selectedCountry = countryDto;
                                break;
                            }
                        }
                        if (selectedCountry != null) {
                            cmbCmpCreateRegCountry.getSelectionModel().select(selectedCountry);
                        }
                        //todo: state code
                        stateCodeById = jObject.get("stateCode").getAsString();
                        //todo: corporateState code
                        corporateStateCodeById = jObject.get("corporateStateCode").getAsString();

                        //todo: Corporate Area Address
                        tfCmpCreateCorpAddress.setText(jObject.get("corporateAddress").getAsString());
                        //todo: Corporate Area Pincode
                        tfCmpCreateCorpPincode.setText(jObject.get("corporatePincode").getAsString());
                        //todo: Corporate Area
                        JsonArray corporate_area_list = jObject.get("corporate_area_list").getAsJsonArray();
                        ObservableList<CompanyAreaDTO> corplist = FXCollections.observableArrayList();
                        for (JsonElement mElement : corporate_area_list) {
                            JsonObject mObject = mElement.getAsJsonObject();
                            String area = mObject.get("corporate_area_name").getAsString();
                            String district = "";
                            String state = "";
                            String stateCode = "";
                            String pincode = "";
                            String id = mObject.get("corporate_area_id").getAsString();
                            corplist.add(new CompanyAreaDTO(area, id, pincode, district, state, stateCode));
                            CorpAreaMapList.put(id, area);
                        }
                        String corpcmparea = jObject.get("corporatearea") != null ? jObject.get("corporatearea").getAsString() : "";
                        corppinText = jObject.get("pincode").getAsString();

                        cmbCmpCreateCorpArea.setItems(corplist);
                        cmbCmpCreateCorpArea.setConverter(new StringConverter<CompanyAreaDTO>() {
                            @Override
                            public String toString(CompanyAreaDTO o) {
                                return o.getArea();
                            }

                            @Override
                            public CompanyAreaDTO fromString(String s) {
                                return null;
                            }
                        });
                        list.stream().filter((v) -> v.getId().equalsIgnoreCase(corpcmparea)).findAny().ifPresent((p) -> {
                            cmbCmpCreateCorpArea.setValue(p);
                        });

                        //Set Area data of Registered by Id
//                        CompanyAreaDTO selectedCorpArea = null;
//                        for (Object obj : cmbCmpCreateCorpArea.getItems()) {
//                            CompanyAreaDTO regArea = (CompanyAreaDTO) obj;
//                            if (regArea.getId().equals(jObject.get("corporatearea").getAsString())) {
//                                selectedCorpArea = regArea;
//                                break;
//                            }
//                        }
//                        if (selectedCorpArea != null) {
//                            corpAreaId = jObject.get("corporatearea").getAsLong();
//                            cmbCmpCreateCorpArea.getSelectionModel().select(selectedCorpArea);
//                        }

                        //todo: Corporate Area City
                        lblCmpCreateCorpCityName.setText(jObject.get("corporatecity").getAsString());
                        //todo: Corporate Area State
                        lblCmpCreateCorpStateName.setText(jObject.get("corporatestate").getAsString());


                        //todo: Same as Address
                        cbCmpCreateSameAsAddress.setSelected(jObject.get("sameAsAddress").getAsBoolean());
                        Boolean sameAsAddVal = jObject.get("sameAsAddress").getAsBoolean();
                        if (cbCmpCreateSameAsAddress.isSelected()) {
                            tfCmpCreateCorpAddress.setDisable(true);
                            tfCmpCreateCorpPincode.setDisable(true);
                            cmbCmpCreateCorpArea.setDisable(true);
                            lblCmpCreateCorpCityName.setDisable(true);
                            lblCmpCreateCorpStateName.setDisable(true);
                            cmbCmpCreateCorpCountry.setDisable(true);

                        }


                        //todo: LicenseNo and License Exp
                        tfCmpCreateLicenseNo.setText(jObject.get("licenseNo").getAsString());

                        String licenseExpiryDateString = jObject.get("licenseExpiryDate").getAsString();
                        LocalDate licenseExpDate = null;

                        if (!licenseExpiryDateString.isEmpty()) {
                            licenseExpDate = LocalDate.parse(licenseExpiryDateString);
                        }

                        if (licenseExpDate != null) {
                            tfCmpCreateLicenseExpDate.setText(licenseExpDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        }


                        //todo: FoodLicenseNo and FoodLicense Exp
                        tfCmpCreateFSSAINo.setText(jObject.get("foodLicenseNo").getAsString());

                        String foodLicenseExpiryDateString = jObject.get("foodLicenseExpiryDate").getAsString();
                        LocalDate fssaiLicenseExpDate = null;

                        if (!foodLicenseExpiryDateString.isEmpty()) {
                            fssaiLicenseExpDate = LocalDate.parse(foodLicenseExpiryDateString);
                        }

                        if (fssaiLicenseExpDate != null) {
                            tfCmpCreateFSSAINoExpDate.setText(fssaiLicenseExpDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        }


                        //todo: ManufacturingLicenseNo and ManufacturingLicense Exp
                        tfCmpCreateMfgLicenseNo.setText(jObject.get("manufacturingLicenseNo").getAsString());

                        String manufacturingLicenseExpiryDateString = jObject.get("manufacturingLicenseExpiry").getAsString();
                        LocalDate mfgLicenseExpDate = null;

                        if (!manufacturingLicenseExpiryDateString.isEmpty()) {
                            mfgLicenseExpDate = LocalDate.parse(manufacturingLicenseExpiryDateString);
                        }

                        if (mfgLicenseExpDate != null) {
                            tfCmpCreateMfgLicenseNoExpDate.setText(mfgLicenseExpDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        }


                        //todo: Website
                        tfCmpCreateWebsite.setText(jObject.get("website").getAsString());
                        //todo: Email
                        tfCmpCreateEmail.setText(jObject.get("email").getAsString());
                        //todo: Mobile Number
                        tfCmpCreateMobileNo.setText(jObject.get("mobileNumber").getAsString());
                        //todo: Whatsapp Number
                        tfCmpCreateWhatsappNo.setText(jObject.get("whatsappNumber").getAsString());
                        //todo: Currency

                        //todo: GST Applicable
                        isGSTApplicable = jObject.get("gstApplicable").getAsBoolean();
//                        cbCmpCreateGSTApplicable.setSelected(gstApplicable);
                        if (isGSTApplicable) {
                            sbCmpCreateGSTApplicable.switchOnProperty().set(true);

                            //todo: GSTIN
                            tfCmpCreateGSTIN.setText(jObject.get("gstIn") != null ? jObject.get("gstIn").getAsString() : "");
                            hbCmpCreateGSTINLabel.setVisible(true);
                            tfCmpCreateGSTIN.setVisible(true);

                            //todo: GST Type
                            String gstTypetId = jObject.get("gstType").getAsString();
                            gstTypeId = jObject.get("gstType").getAsLong();
                            CompanyGstTypeDTO companyGSTType = null;
                            for (Object obj : cmbCmpCreateGSTType.getItems()) {
                                CompanyGstTypeDTO cmpGSTType = (CompanyGstTypeDTO) obj;
                                if (cmpGSTType.getId().equals(gstTypetId)) {
                                    companyGSTType = cmpGSTType;
                                    break;
                                }
                            }
                            if (companyGSTType != null) {
                                cmbCmpCreateGSTType.getSelectionModel().select(companyGSTType);
                                gstTypeId = Long.valueOf(companyGSTType.getId());
//                                System.out.println("Selected GST Type: " + companyGSTType.getId());
                            } else {
//                                System.out.println("GST Type not found: " + businessTypeId);
                            }
                            hbCmpCreateGSTType.setVisible(true);
                            cmbCmpCreateGSTType.setVisible(true);


                            //todo: GST Applicable Date
                            String gstApplicableDate = jObject.get("gstApplicableDate").getAsString();
                            LocalDate gstAppDate = null;

                            if (!gstApplicableDate.isEmpty()) {
                                gstAppDate = LocalDate.parse(gstApplicableDate);
                            }

                            if (gstAppDate != null) {
                                tfCmpCreateApplicableDate.setText(gstAppDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                            }

                            lblCmpCreateApplicableDate.setVisible(true);
                            tfCmpCreateApplicableDate.setVisible(true);

                        }
                        //todo: Is Multi_branch
                        isMultiBranch = jObject.get("multiBranch").getAsBoolean();
                        if (isMultiBranch) {
                            sbCmpMultiBranch.switchOnProperty().set(true);

                        }

                    } else {
//                        showAlert("Error in fetching...");
//                        AlertUtility.AlertDialogForError("WARNING", "Failed to Load Data !", input -> {
//                            if (input) {
//                            }
//                        });
                    }

                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getCompanyEditDataById()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getCompanyEditDataById()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();

            btnCmpCreateSubmit.setText("Update");
        } catch (Exception e) {
            e.printStackTrace();
            //TODO : use logger for alert messages
//            showAlert("Falied to connect server! ");
            AlertUtility.AlertDialogForError("WARNING", "Failed to Connect Server !", input -> {
                if (input) {
                }
            });
        } finally {
            apiClient = null;
        }
    }
}

