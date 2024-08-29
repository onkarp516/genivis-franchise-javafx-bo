package com.opethic.genivis.controller.master.ledger.create;

import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.Reports.StocksStockReport2Controller;
import com.opethic.genivis.controller.commons.SwitchButton;
import com.opethic.genivis.controller.master.FranchiseListController;
import com.opethic.genivis.controller.master.ledger.LedgerListController;
import com.opethic.genivis.controller.master.ledger.common.LedgerCommonController;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.dto.CommonDTO;
import com.opethic.genivis.dto.FranchiseDTO;
import com.opethic.genivis.dto.StockReport1DTO;
import com.opethic.genivis.dto.master.ledger.LedgerListDTO;
import com.opethic.genivis.dto.master.ledger.OBListDTO;
import com.opethic.genivis.dto.reqres.pur_tranx.PurTranxToProductRedirectionDTO;
//import com.opethic.genivis.dto.reqres.receipt.AccountEntryRedirectionDTO;
import com.opethic.genivis.models.master.ledger.*;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.*;

import org.controlsfx.control.textfield.TextFields;

import static com.opethic.genivis.utils.FxmFileConstants.*;


public class LedgerCreateController implements Initializable {

    @FXML
    private BorderPane anpledgercreate;
    @FXML
    private HBox ledgerMainTopRow;

    @FXML
    private StackPane loadLedgerPage;
    @FXML
    private Button btnCancelLedgerMain;
    @FXML
    private ComboBox<UnderLedger> cmbLedgerType;
    ObservableList<BalanceType> lstBalanceType = FXCollections.observableArrayList();
    ObservableList<BalancingMethods> lstBalancingMethods = FXCollections.observableArrayList();

    @FXML
    private ComboBox<BalancingMethods> cmbLedgerCreateBalancingMethods;

    @FXML
    private ComboBox<BalanceType> cmbLedgerCreateOpeningBalanceType;

    @FXML
    private Label lblLedgerCreateBalancingMethods, lblLedgerCreateLedgerCode;

    @FXML
    private TextField tfLedgerCreateLedgerName, tfLedgerCreateLedgerCode, tfLedgerCreateOpeningBal;

    private static final Logger loggerLedgerCreate = LogManager.getLogger(LedgerCreateController.class);
    ObservableList<UnderLedger> cmbUnderList = FXCollections.observableArrayList();

    private FranchiseDTO frDto = null;
    ObservableList<OBListDTO> tbllstOB = FXCollections.observableArrayList();

    private PurTranxToProductRedirectionDTO ledDTO = null;
//    private AccountEntryRedirectionDTO ledAccDTO = null;

    private String redirectSlug = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        GlobalLedgerCreateController globalLedgerCreateController = GlobalLedgerCreateController.getInstance();
        globalLedgerCreateController.setController(this);
        getUnderList();
        getBalancingTypeList();
        getBalanceTypeList();
        Platform.runLater(() -> {
            cmbLedgerType.requestFocus();
        });


        tfLedgerCreateLedgerName.textProperty().addListener((observable, oldValue, newValue) -> {
            tfLedgerCreateLedgerName.getText();
        });

//        cmbLedgerType.focusedProperty().addListener((obs, old, nw) -> {
//            if (!nw) {
//                System.out.println("Selected ledger type----------> " + cmbLedgerType.getSelectionModel().getSelectedItem());
//                UnderLedger selectedLedgerUnder = cmbLedgerType.getSelectionModel().getSelectedItem();
//                btnCancelLedgerMain.setVisible(selectedLedgerUnder == null);
//            }
//        });

        cmbLedgerType.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
//                System.out.println("Esc Clicked....!");
                GlobalController.getInstance().addTabStatic("ledger-list", false);
            }
        });

        // open Filter dropdown on Space
        cmbLedgerType.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cmbLedgerType.isShowing()) {
                cmbLedgerType.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });
        cmbLedgerCreateOpeningBalanceType.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cmbLedgerCreateOpeningBalanceType.isShowing()) {
                cmbLedgerCreateOpeningBalanceType.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });
        cmbLedgerCreateBalancingMethods.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cmbLedgerCreateBalancingMethods.isShowing()) {
                cmbLedgerCreateBalancingMethods.show();
                event.consume();
            }
        });

        cmbLedgerCreateBalancingMethods.setValue(cmbLedgerCreateBalancingMethods.getItems().get(0));

//        cmbLedgerType.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfLedgerCreateLedgerCode.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfLedgerCreateLedgerName.setOnKeyPressed(this::handleEnterKeyPressed);
//        cmbLedgerCreateBalancingMethods.setOnKeyPressed(this::handleEnterKeyPressed);
//        tfLedgerCreateOpeningBal.setOnKeyPressed(this::handleEnterKeyPressed);
//        cmbLedgerCreateOpeningBalanceType.setOnKeyPressed(this::handleEnterKeyPressed);
        initLedgerShorcuts();

        initLedgerOpeningBalancePopup();

        // Setting the handler for each node
        setKeyPressedHandler(cmbLedgerType);
        setKeyPressedHandler(tfLedgerCreateLedgerName);
        setKeyPressedHandler(tfLedgerCreateOpeningBal);
        setKeyPressedHandler(cmbLedgerCreateOpeningBalanceType);
        cmbLedgerType.focusedProperty().addListener((obs, old, nw) -> {
            if (!nw) {
                UnderLedger selectedLedgerUnder = cmbLedgerType.getSelectionModel().getSelectedItem();
                if (selectedLedgerUnder != null) {
                    if (selectedLedgerUnder.getLedger_form_parameter_slug().equalsIgnoreCase("sundry_creditors") ||
                            selectedLedgerUnder.getLedger_form_parameter_slug().equalsIgnoreCase("sundry_debtors")) {
                        Platform.runLater(() -> {
                            tfLedgerCreateLedgerCode.requestFocus();
                        });
                        setKeyPressedHandler(tfLedgerCreateLedgerCode);
                        setKeyPressedHandler(cmbLedgerCreateBalancingMethods);
                    }
                }
            }
        });

        tfFocused(tfLedgerCreateLedgerName);

        tfLedgerCreateLedgerName.addEventFilter(KeyEvent.ANY, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                String main_ledger_name = tfLedgerCreateLedgerName.getText();
                LedgerCreateSCController.main_ledger_name = main_ledger_name;
                System.out.println("main_ledger_name-------> " + main_ledger_name);
            }
        });
    }


    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            ledgerMainTopRow.setSpacing(5);
            anpledgercreate.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            ledgerMainTopRow.setSpacing(10);
            anpledgercreate.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            ledgerMainTopRow.setSpacing(12);
            anpledgercreate.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            ledgerMainTopRow.setSpacing(15);
            anpledgercreate.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            ledgerMainTopRow.setSpacing(15);
            anpledgercreate.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            anpledgercreate.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            anpledgercreate.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_7.css").toExternalForm());
        }
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

    // Method to handle ENTER key pressed event for any Node
    private void setKeyPressedHandler(Node node) {
        node.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Node nextNode = CommonTraversa.getNextFocusableNode(node.getScene());
                if (nextNode != null) {
                    nextNode.requestFocus();
                }
            }
        });
    }

    private void initLedgerShorcuts() {
        anpledgercreate.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.TAB) {
//                System.out.println("event - target Tab -> " + event.getTarget());
                if (event.getTarget() instanceof TextField) {
                    TextField rqTxFld = (TextField) event.getTarget();
                    System.out.println("rqTxFld" + rqTxFld.getId());
                    if (rqTxFld.getStyleClass().contains("isRequired") && rqTxFld.getText().trim().isEmpty()) {
                        GlobalTranx.requestFocusOrDieTrying(rqTxFld, 2);
                    }
                }
                if (event.getTarget() instanceof ComboBox<?>) {
                    ComboBox<?> rqCmb = (ComboBox<?>) event.getTarget();
//                    System.out.println("rqCmb.getSelectionModel().getSelectedItem() =>" + rqCmb.getSelectionModel().getSelectedItem());
                    if (rqCmb.getStyleClass().contains("isRequired") && rqCmb.getSelectionModel().getSelectedItem() == null) {
                        GlobalTranx.requestFocusOrDieTrying(rqCmb, 1);
                    }
                }
            }
           /* if (event.getCode() == KeyCode.ENTER) {
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
//                System.out.println("event Fire -> " + event.getTarget().toString());
                if (event.getTarget() instanceof TextField) {
                    TextField rqTxFld = (TextField) event.getTarget();
                    if (rqTxFld.getStyleClass().contains("isRequired") && !rqTxFld.getText().isEmpty()) {
                        Event.fireEvent(event.getTarget(), newEvent);
                    } else {
                        Event.fireEvent(event.getTarget(), newEvent);
                    }
                } else if (event.getTarget() instanceof Button) {
//                    Event.fireEvent(event.getTarget(), newEvent);
                    ((Button) event.getTarget()).fire();
                } else {
                    Event.fireEvent(event.getTarget(), newEvent);
                }
                event.consume();

            } else */
            if (event.getCode() == KeyCode.X && event.isControlDown()) {
                //! Sundry Creditor
                Button btnSCCancel = (Button) loadLedgerPage.lookup("#btnScLedgerCreateCancel");
                if (btnSCCancel != null) btnSCCancel.fire();
                //!  Sundry Debtor
                Button btnSDCancel = (Button) loadLedgerPage.lookup("#btnSdLedgerCreateCancel");
                if (btnSDCancel != null) btnSDCancel.fire();
                //!  Tax
                Button btnTaxCancel = (Button) loadLedgerPage.lookup("#btnLedgerCreateTaxCancel");
                if (btnTaxCancel != null) btnTaxCancel.fire();
                //!  Other
                Button btnOtherCancel = (Button) loadLedgerPage.lookup("#btnLedgerCreateOtherCancel");
                if (btnOtherCancel != null) btnOtherCancel.fire();
                //!  Bank
                Button btnBankCancel = (Button) loadLedgerPage.lookup("#btnbankaccLedgerCreateCancel");
                if (btnBankCancel != null) btnBankCancel.fire();
                //!  Assets
                Button btnAssetsCancel = (Button) loadLedgerPage.lookup("#btnLedgerCreateAssetsCancel");
                if (btnAssetsCancel != null) btnAssetsCancel.fire();

            } else if (event.getCode() == KeyCode.S && event.isControlDown()) {
                System.out.println("CTLR+S");
                //! Sundry Creditor
                Button btnSCSubmit = (Button) loadLedgerPage.lookup("#btnScLedgerCreateSubmit");
                if (btnSCSubmit != null) btnSCSubmit.fire();
                //! Sundry Creditor
                Button btnSDSubmit = (Button) loadLedgerPage.lookup("#btnSdLedgerCreateSubmit");
                if (btnSDSubmit != null) btnSDSubmit.fire();
                //! Tax
                Button btnTaxSubmit = (Button) loadLedgerPage.lookup("#btnLedgerCreateTaxSubmit");
                if (btnTaxSubmit != null) btnTaxSubmit.fire();
                //! Other
                Button btnOtherSubmit = (Button) loadLedgerPage.lookup("#btnLedgerCreateOtherSubmit");
                if (btnOtherSubmit != null) btnOtherSubmit.fire();
                //! Bank
                Button btnBankSubmit = (Button) loadLedgerPage.lookup("#btnbankaccLedgerCreateSubmit");
                if (btnBankSubmit != null) btnBankSubmit.fire();
                //! Assets
                Button btnAssetsSubmit = (Button) loadLedgerPage.lookup("#btnLedgerCreateAssetsSubmit");
                if (btnAssetsSubmit != null) {
                    btnAssetsSubmit.fire();
                    GlobalTranx.requestFocusOrDieTrying(btnAssetsSubmit, 3);
                }
            }
        });
    }

    private void initLedgerOpeningBalancePopup() {
//        cmbLedgerCreateBalancingMethods.focusedProperty().addListener((obs,old,nw)->{
//            if(!nw){
//                BalancingMethods selectedBalancingMethod = cmbLedgerCreateBalancingMethods.getSelectionModel().getSelectedItem();
//                if (selectedBalancingMethod.getBalance_method().equalsIgnoreCase("Bill by Bill")) {
//                    System.out.println("bill-by-bill");
//                }
//            }
//        });
//        cmbLedgerType.setEditable(true);
//        cmbLedgerType.getEditor().focusedProperty().addListener(observable -> {
//            if (cmbLedgerType.getSelectionModel().getSelectedIndex() < 0) {
//                cmbLedgerType.getEditor().setText(null);
//            }
//        });


        cmbLedgerType.focusedProperty().addListener((obs, old, nw) -> {
            if (!nw) {
                System.out.println("selected under type" + cmbLedgerType.getSelectionModel().getSelectedItem());

                UnderLedger selectedLedgerUnder = cmbLedgerType.getSelectionModel().getSelectedItem();
                if (selectedLedgerUnder != null) {
                    if (selectedLedgerUnder.getLedger_form_parameter_slug().equalsIgnoreCase("sundry_creditors")) {
                        lstBalanceType.stream().filter((v) -> v.getType().equalsIgnoreCase("Cr")).findAny().ifPresent((p) -> {
                            cmbLedgerCreateOpeningBalanceType.setValue(p);
                        });
                    } else if (selectedLedgerUnder.getLedger_form_parameter_slug().equalsIgnoreCase("sundry_debtors")) {
                        lstBalanceType.stream().filter((v) -> v.getType().equalsIgnoreCase("dr")).findAny().ifPresent((p) -> {
                            cmbLedgerCreateOpeningBalanceType.setValue(p);
                        });
                    } else {
                        cmbLedgerCreateOpeningBalanceType.setValue(lstBalanceType.get(0));
                    }
                }
            }
        });

        tfLedgerCreateOpeningBal.setOnKeyTyped(e -> {
            char input = e.getCharacter().charAt(0);
            if (Character.isDigit(input) != true) {
                e.consume();
            }
        });
        tfLedgerCreateOpeningBal.focusedProperty().addListener((obs, old, nw) -> {
            if (!nw) {
                BalancingMethods selectedBalancingMethod = cmbLedgerCreateBalancingMethods.getSelectionModel().getSelectedItem();
                String openBal = tfLedgerCreateOpeningBal.getText();
                Double actOpenBal = openBal.isEmpty() ? 0 : Double.parseDouble(openBal);
                if (selectedBalancingMethod != null && actOpenBal > 0) {
                    if (selectedBalancingMethod.getBalance_method().equalsIgnoreCase("Bill by Bill")) {
//                        System.out.println("bill-by-bill");
                        Stage stage = (Stage) anpledgercreate.getScene().getWindow();
                        LedgerCommonController ledgerCommonController = new LedgerCommonController();
                        ledgerCommonController.openOpeningBalancePopUpWithParam(stage, actOpenBal, tbllstOB, input -> {
                            tbllstOB = input;
                        });
                    }
                }
            }
        });

    }

//    private void handleEnterKeyPressed(KeyEvent keyEvent) {
//        Node source = (Node) keyEvent.getSource();
//        if (keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.TAB) {
//            switch (source.getId()) {
//                case "cmbLedgerType":
//                    Platform.runLater(() -> {
//                        tfLedgerCreateLedgerCode.requestFocus();
//                    });
//                    break;
//                case "tfLedgerCreateLedgerCode":
//                    Platform.runLater(() -> {
//                        tfLedgerCreateLedgerName.requestFocus();
//                    });
//                    break;
//                case "tfLedgerCreateLedgerName":
//                    Platform.runLater(() -> {
//                        cmbLedgerCreateBalancingMethods.requestFocus();
//                    });
//                    break;
//                case "cmbLedgerCreateBalancingMethods":
//                    Platform.runLater(() -> {
//                        tfLedgerCreateOpeningBal.requestFocus();
//                    });
//                    break;
//                case "tfLedgerCreateOpeningBal":
//                    Platform.runLater(() -> {
//                        cmbLedgerCreateOpeningBalanceType.requestFocus();
//                    });
//                    break;
//                default:
//                    break;
//            }
//        }
//    }


    private void getBalanceTypeList() {
        List<BalanceType> lst = Globals.getAllBalanceType();
        lstBalanceType = FXCollections.observableArrayList(lst);
        cmbLedgerCreateOpeningBalanceType.getItems().addAll(lstBalanceType);
        cmbLedgerCreateOpeningBalanceType.setConverter(new StringConverter<BalanceType>() {
            @Override
            public String toString(BalanceType t) {
                return t != null ? t.getType() : "";
            }

            @Override
            public BalanceType fromString(String string) {
                return null;
            }
        });
    }

    private void getBalancingTypeList() {
        List<BalancingMethods> lst = APICallgetBalancingMethods();
        ObservableList<BalancingMethods> lstBalancingMethods = FXCollections.observableArrayList(lst);

        cmbLedgerCreateBalancingMethods.getItems().addAll(lstBalancingMethods);
        cmbLedgerCreateBalancingMethods.setConverter(new StringConverter<BalancingMethods>() {
            @Override
            public String toString(BalancingMethods methods) {
                return methods != null ? methods.getBalance_method() : "";
            }

            @Override
            public BalancingMethods fromString(String string) {
                return null;
            }
        });
    }

    private List<BalancingMethods> APICallgetBalancingMethods() {
        HttpResponse<String> response = APIClient.getRequest(EndPoints.getBalancingMethods);
        JSONObject jsonObject = new JSONObject(response.body());
        List<BalancingMethods> balancingMethodsList = new ArrayList<>();
        if (jsonObject.getInt("responseStatus") == 200) {
            JSONArray jarr = jsonObject.getJSONArray("response");
            for (Object object : jarr) {
                JSONObject obj = new JSONObject(object.toString());
                BalancingMethods balancingMethods = new BalancingMethods(obj.getInt("balancing_id"), obj.getString("balance_method"));
                balancingMethodsList.add(balancingMethods);
            }
        } else {
            String msg = jsonObject.getString("message");
            AlertUtility.CustomCallback callback = number -> {
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
        }
        return balancingMethodsList;
    }


    private void getUnderList() {
        Integer initialIndex = -1;
        List<UnderLedger> lst = APICallgetUnderList();
        cmbUnderList = FXCollections.observableArrayList(lst);
        cmbLedgerType.getItems().addAll(cmbUnderList);

        cmbLedgerType.setConverter(new StringConverter<UnderLedger>() {
            @Override
//            public String toString(UnderLedger underLedger) {
//                return underLedger != null ? underLedger.getDisplay_name() : "";
//            }
            public String toString(UnderLedger underLedger) {
                if (underLedger == null) {
                    return null;
                } else {
                    return underLedger.getDisplay_name();
                }
            }

            @Override
//            public UnderLedger fromString(String string) {
//                // You can implement this method if needed
//                return null;
//            }
            public UnderLedger fromString(String string) {
                return lst.stream()
                        .filter(name -> name.getDisplay_name().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        //Focus change
//        cmbLedgerType.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
//            LoadLedgerFormCondition(newValue != null ? newValue.getLedger_form_parameter_slug() : "");
//        });
        //!Focus change
        cmbLedgerType.focusedProperty().addListener((src, ov, nv) -> {
            if (!nv) {
                System.out.println("combo committed: " +
                        cmbLedgerType.getEditor().getText() + "=" + cmbLedgerType.getValue());
                LoadLedgerFormCondition(cmbLedgerType != null && cmbLedgerType.getValue() != null ? cmbLedgerType.getValue().getLedger_form_parameter_slug() : "");
            }
        });

//        AutoCompleteBox s = new AutoCompleteBox<>(cmbLedgerType);
        AutoCompleteBox autoCompleteBox = new AutoCompleteBox(cmbLedgerType, initialIndex);
    }

    private void setUpperVisible(Boolean f) {
        cmbLedgerCreateBalancingMethods.setVisible(f);
        lblLedgerCreateBalancingMethods.setVisible(f);
        lblLedgerCreateLedgerCode.setVisible(f);
        tfLedgerCreateLedgerCode.setVisible(f);
        cmbLedgerCreateBalancingMethods.setManaged(f);
        lblLedgerCreateBalancingMethods.setManaged(f);
        lblLedgerCreateLedgerCode.setManaged(f);
        tfLedgerCreateLedgerCode.setManaged(f);

    }

    private void LoadLedgerFormCondition(String ledgerFormParameterSlug) {
        FXMLLoader nLoader = null;
        Parent nRoot = null;
        switch (ledgerFormParameterSlug) {
            case "sundry_creditors":
                try {
                    loggerLedgerCreate.info("Load ledger form on condition -> " + GenivisApplication.class.getResource(ConstLedgerCreateFxml.create_const_sundry_creditors));
                    setUpperVisible(true);
                    nLoader = new FXMLLoader(GenivisApplication.class.getResource(ConstLedgerCreateFxml.create_const_sundry_creditors));
                    nRoot = nLoader.load();
                    loadLedgerPage.getChildren().clear();
                    loadLedgerPage.getChildren().add(nRoot);
                } catch (IOException e) {
                    loggerLedgerCreate.error("Exception in LoadLedgerFormCondition(): -> sundry_creditors :" + Globals.getExceptionString(e));
                }
                break;

            case "sundry_debtors":
                try {
                    loggerLedgerCreate.info("Load ledger form on condition -> " + GenivisApplication.class.getResource(ConstLedgerCreateFxml.create_const_sundry_debtors));
                    setUpperVisible(true);
                    nLoader = new FXMLLoader(GenivisApplication.class.getResource(ConstLedgerCreateFxml.create_const_sundry_debtors));
                    nRoot = nLoader.load();
                    loadLedgerPage.getChildren().clear();
                    loadLedgerPage.getChildren().add(nRoot);
                    LedgerCreateSDController ledgerCreateSDController = nLoader.getController();
                    if (frDto != null) {
                        ledgerCreateSDController.getFranchiseData(frDto);
                    }
                } catch (IOException e) {
                    loggerLedgerCreate.error("Exception in LoadLedgerFormCondition(): -> sundry_debtors :" + Globals.getExceptionString(e));
                }
                break;
            case "bank_account":
                try {
                    loggerLedgerCreate.info("Load ledger form on condition -> " + GenivisApplication.class.getResource(ConstLedgerCreateFxml.create_const_bank_account));
                    setUpperVisible(false);
                    nLoader = new FXMLLoader(GenivisApplication.class.getResource(ConstLedgerCreateFxml.create_const_bank_account));
                    nRoot = nLoader.load();
                    loadLedgerPage.getChildren().clear();
                    loadLedgerPage.getChildren().add(nRoot);
                } catch (IOException e) {
                    loggerLedgerCreate.error("Exception in LoadLedgerFormCondition(): -> sundry_debtors :" + Globals.getExceptionString(e));
//                    throw new RuntimeException(e);
                }
                break;
            case "duties_taxes":
                try {
                    loggerLedgerCreate.info("Load ledger form on condition -> " + GenivisApplication.class.getResource(ConstLedgerCreateFxml.create_const_duties_taxes));
                    setUpperVisible(false);
                    nLoader = new FXMLLoader(GenivisApplication.class.getResource(ConstLedgerCreateFxml.create_const_duties_taxes));
                    nRoot = nLoader.load();
                    loadLedgerPage.getChildren().clear();
                    loadLedgerPage.getChildren().add(nRoot);

                } catch (IOException e) {
                    loggerLedgerCreate.error("Exception in LoadLedgerFormCondition(): -> duties_taxes :" + Globals.getExceptionString(e));
//                    throw new RuntimeException(e);
                }
                break;
            case "assets":
                try {
                    loggerLedgerCreate.info("Load ledger form on condition -> " + GenivisApplication.class.getResource(ConstLedgerCreateFxml.create_const_assets));
                    setUpperVisible(false);

                    nLoader = new FXMLLoader(GenivisApplication.class.getResource(ConstLedgerCreateFxml.create_const_assets));
                    nRoot = nLoader.load();
                    loadLedgerPage.getChildren().clear();
                    loadLedgerPage.getChildren().add(nRoot);

                } catch (IOException e) {
                    loggerLedgerCreate.error("Exception in LoadLedgerFormCondition(): -> assets :" + Globals.getExceptionString(e));
//                    throw new RuntimeException(e);
                }
                break;
            default:
                //! other
                try {
                    loggerLedgerCreate.info("Load ledger form on condition -> " + GenivisApplication.class.getResource(ConstLedgerCreateFxml.create_const_other));
                    setUpperVisible(false);

                    nLoader = new FXMLLoader(GenivisApplication.class.getResource(ConstLedgerCreateFxml.create_const_other));
                    nRoot = nLoader.load();
                    loadLedgerPage.getChildren().clear();
                    loadLedgerPage.getChildren().add(nRoot);

                } catch (IOException e) {
                    loggerLedgerCreate.error("Exception in LoadLedgerFormCondition(): -> other :" + Globals.getExceptionString(e));
//                    throw new RuntimeException(e);
                }
                break;
        }
    }

    private List<UnderLedger> APICallgetUnderList() {
        HttpResponse<String> response = APIClient.getRequest(EndPoints.underLedgerList);
        JSONObject jsonObject = new JSONObject(response.body());
        List<UnderLedger> underLedgerList = new ArrayList<>();
        if (jsonObject.getInt("responseStatus") == 200) {
            JSONArray jarr = jsonObject.getJSONArray("responseObject");
            for (Object o : jarr) {
                JSONObject object = new JSONObject(o.toString());
                String dispName = "";
                String dispId = "";
                if (!object.getString("associates_name").isEmpty()) {
                    dispName = object.getString("associates_name");
                    dispId = object.getInt("principle_id") + "_" + object.getInt("sub_principle_id") + "_" + object.getInt("associates_id");
                } else if (!object.getString("subprinciple_name").isEmpty()) {
                    dispName = object.getString("subprinciple_name");
                    dispId = object.getInt("principle_id") + "_" + object.getInt("sub_principle_id");
                } else if (!object.getString("principle_name").isEmpty()) {
                    dispName = object.getString("principle_name");
                    dispId = "" + object.getInt("principle_id");
                }
                UnderLedger underLedger = new UnderLedger(object.getInt("principle_id"), object.getString("principle_name"), object.getInt("ledger_form_parameter_id"), object.getString("ledger_form_parameter_slug"), object.getInt("sub_principle_id"), object.getString("subprinciple_name"), object.getString("under_prefix"), object.getInt("associates_id"), object.getString("associates_name"), dispName, dispId);
                underLedgerList.add(underLedger);
            }
        } else {
            String msg = jsonObject.getString("message");
            AlertUtility.CustomCallback callback = number -> {
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
        }
        return underLedgerList;
    }

    private void clearForm() {
        tfLedgerCreateLedgerName.clear();
        tfLedgerCreateOpeningBal.clear();
        cmbLedgerCreateBalancingMethods.setValue(null);
        cmbLedgerType.setValue(null);

    }

    /**
     * @ImplNote Call when ledgerSlug is others & assets to submit is clicked
     * @author kirankumar.gadagi
     */
    public void LedgerCreateOtherSubmit() {

        if (cmbLedgerType.getSelectionModel().getSelectedItem() != null && !tfLedgerCreateLedgerName.getText().isEmpty()) {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnSubmit + tfLedgerCreateLedgerName.getText(), input -> {
                if (input == 1) {
                    try {
                        UnderLedger underLedger = cmbLedgerType.getSelectionModel().getSelectedItem();
                        BalanceType balanceType = cmbLedgerCreateOpeningBalanceType.getSelectionModel().getSelectedItem();
                        Map<String, String> body = new HashMap<>();
                        body.put("under_prefix", underLedger.getUnder_prefix());
                        body.put("associates_id", String.valueOf(underLedger.getAssociates_id()));
                        body.put("principle_group_id", String.valueOf(underLedger.getSub_principle_id()));
                        body.put("principle_id", String.valueOf(underLedger.getPrinciple_id()));
                        body.put("opening_bal_type", balanceType != null ? String.valueOf(balanceType.getType()) : "");
                        body.put("opening_bal", tfLedgerCreateOpeningBal.getText());
                        body.put("underId", String.valueOf(underLedger.getLedger_form_parameter_id()));
                        body.put("slug", underLedger.getLedger_form_parameter_slug());
                        body.put("ledger_name", tfLedgerCreateLedgerName.getText());
                        body.put("is_private", "false");

                        Map<String, String> headers = new HashMap<>();
                        headers.put("branch", Globals.headerBranch);
//                Map<String, File> fileMap = null;
                        String response = APIClient.postMultipartRequest(body, null, EndPoints.createLedgerMaster, headers);
                        //? HIGHLIGHT
                        LedgerListController.isNewLedgerCreated = true; //? Set the flag for new creation
                        JSONObject resObj = new JSONObject(response);


                        Stage stage = (Stage) anpledgercreate.getScene().getWindow();
                        if (resObj.getInt("responseStatus") == 200) {
                            clearForm();
//                    System.out.println("resp" + resObj.getString("message"));
                            String message = resObj.getString("message");
                            AlertUtility.CustomCallback callback = number -> {
                                GlobalController.getInstance().addTabStatic("ledger-list", false);
                            };
                            AlertUtility.AlertSuccess(stage, AlertUtility.alertTypeSuccess, message, callback);

                        } else {
                            String message = resObj.getString("message");
                            AlertUtility.CustomCallback callback = number -> {
                            };
                            AlertUtility.AlertError(stage, AlertUtility.alertTypeError, message, callback);
                        }
                    } catch (Exception e) {
//                System.out.println("exception:" + e);
                        loggerLedgerCreate.error("Exception on LedgerCreateOtherSubmit():" + Globals.getExceptionString(e));
                    }
                }
            });
        } else {
            String msg = "Enter mandatory Name";
            AlertUtility.CustomCallback callback = number -> {
                GlobalTranx.requestFocusOrDieTrying(tfLedgerCreateLedgerName, 2);
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
        }
    }

    /**
     * @ImplNote Call when ledgerSlug is others & assets to cancel is clicked
     * @author kirankumar.gadagi
     */
    public void LedgerCreateOtherCancel() {
//        System.out.println("other ledger create controller cancel");
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                if (!redirectSlug.isEmpty()) {
                    GlobalController.getInstance().addTabStaticWithIsNewTab(redirectSlug, false, ledDTO);
                } else {
                    GlobalController.getInstance().addTabStatic("ledger-list", false);
                }
            }
        };
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Cancel", callback);
    }

    public void LedgerCreateTaxSubmit(String taxtype) {

        if (cmbLedgerType.getSelectionModel().getSelectedItem() != null && !tfLedgerCreateLedgerName.getText().isEmpty()) {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnSubmit + tfLedgerCreateLedgerName.getText(), input -> {
                if (input == 1) {
                    try {
                        UnderLedger underLedger = cmbLedgerType.getSelectionModel().getSelectedItem();
                        BalanceType balanceType = cmbLedgerCreateOpeningBalanceType.getSelectionModel().getSelectedItem();
                        Map<String, String> body = new HashMap<>();
                        body.put("under_prefix", underLedger.getUnder_prefix());
                        body.put("associates_id", String.valueOf(underLedger.getAssociates_id()));
                        body.put("principle_group_id", String.valueOf(underLedger.getSub_principle_id()));
                        body.put("principle_id", String.valueOf(underLedger.getPrinciple_id()));
                        body.put("opening_bal_type", balanceType != null ? String.valueOf(balanceType.getType()) : "");
                        body.put("opening_bal", tfLedgerCreateOpeningBal.getText());
                        body.put("underId", String.valueOf(underLedger.getLedger_form_parameter_id()));
                        body.put("slug", underLedger.getLedger_form_parameter_slug());
                        body.put("ledger_name", tfLedgerCreateLedgerName.getText());
                        body.put("tax_type", taxtype);
                        body.put("is_private", "false");

                        Map<String, String> headers = new HashMap<>();
                        headers.put("branch", Globals.headerBranch);//                Map<String, File> fileMap = null;
                        String response = APIClient.postMultipartRequest(body, null, EndPoints.createLedgerMaster, headers);
                        //? HIGHLIGHT
                        LedgerListController.isNewLedgerCreated = true; //? Set the flag for new creation
                        JSONObject resObj = new JSONObject(response);
                        if (resObj.getInt("responseStatus") == 200) {
                            clearForm();
//                    System.out.println("resp" + resObj.getString("message"));
                            String message = resObj.getString("message");

                            AlertUtility.CustomCallback callback = number -> {
                                GlobalController.getInstance().addTabStatic("ledger-list", false);
                            };
                            AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, callback);
                        } else {
                            String message = resObj.getString("message");

                            AlertUtility.CustomCallback callback = number -> {
                            };
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, callback);
                        }
                    } catch (Exception e) {
                        loggerLedgerCreate.error("Exception in LedgerCreateTaxSubmit(): -> :" + Globals.getExceptionString(e));
                    }
                }
            });

        } else {
            String msg = "Enter mandatory Name";
            AlertUtility.CustomCallback callback = number -> {
                GlobalTranx.requestFocusOrDieTrying(tfLedgerCreateLedgerName, 2);
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
        }
    }

    public void LedgerCreateBankAccSubmit(List<PaymentMode> paymentModeList, Boolean isDefaultBank, Boolean taxable, String GstIn, String BankName, String AccNo, String IFSCCode, String BankBranch) {
        JSONArray paymentJsonArray = new JSONArray();
        for (PaymentMode v : paymentModeList) {
            JSONObject obj = new JSONObject();
            obj.put("id", v.getId());
            obj.put("label", v.getPaymentMode());
            obj.put("value", v.getIsSelected());
            paymentJsonArray.put(obj);
        }
//        System.out.println("paymentJsonArray => " + paymentJsonArray);
        if (cmbLedgerType.getSelectionModel().getSelectedItem() != null && !tfLedgerCreateLedgerName.getText().isEmpty()) {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnSubmit + tfLedgerCreateLedgerName.getText(), input -> {
                if (input == 1) {
                    try {
                        UnderLedger underLedger = cmbLedgerType.getSelectionModel().getSelectedItem();
                        BalanceType balanceType = cmbLedgerCreateOpeningBalanceType.getSelectionModel().getSelectedItem();
                        Map<String, String> body = new HashMap<>();
                        body.put("under_prefix", underLedger.getUnder_prefix());
                        body.put("associates_id", String.valueOf(underLedger.getAssociates_id()));
                        body.put("principle_group_id", String.valueOf(underLedger.getSub_principle_id()));
                        body.put("principle_id", String.valueOf(underLedger.getPrinciple_id()));
                        body.put("opening_bal_type", balanceType != null ? String.valueOf(balanceType.getType()) : "");
                        body.put("opening_bal", tfLedgerCreateOpeningBal.getText());
                        body.put("underId", String.valueOf(underLedger.getLedger_form_parameter_id()));
                        body.put("slug", underLedger.getLedger_form_parameter_slug());
                        body.put("ledger_name", tfLedgerCreateLedgerName.getText());
                        body.put("is_private", "false");
                        body.put("payment_modes", paymentJsonArray.toString());
                        body.put("defaultBank", isDefaultBank.toString());
                        body.put("taxable", taxable.toString());
                        body.put("gstin", GstIn);
                        body.put("bank_name", BankName);
                        body.put("account_no", AccNo);
                        body.put("ifsc_code", IFSCCode);
                        body.put("bank_branch", BankBranch);

                        Map<String, String> headers = new HashMap<>();
                        headers.put("branch", Globals.headerBranch);
                        String response = APIClient.postMultipartRequest(body, null, EndPoints.createLedgerMaster, headers);
                        //? HIGHLIGHT
                        LedgerListController.isNewLedgerCreated = true; //? Set the flag for new creation
                        JSONObject resObj = new JSONObject(response);

                        if (resObj.getInt("responseStatus") == 200) {
                            clearForm();
                            String message = resObj.getString("message");

                            AlertUtility.CustomCallback callback = number -> {
                                GlobalController.getInstance().addTabStatic("ledger-list", false);
                            };
                            AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, callback);
                        } else {
                            String message = resObj.getString("message");
                            AlertUtility.CustomCallback callback = number -> {
                            };
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, callback);
                        }
                    } catch (Exception e) {
                        loggerLedgerCreate.error("Exception in LedgerCreateBankAccSubmit(): -> :" + Globals.getExceptionString(e));
                    }
                }
            });
        } else {
            String msg = "Please Fill Required Fields !";
            AlertUtility.CustomCallback callback = number -> {
                GlobalTranx.requestFocusOrDieTrying(tfLedgerCreateLedgerName, 2);
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
        }
    }

    public void LedgerCreateSCSubmit(String mailingName, String address, String state, String pinCode, String mobileNo, String whatsappNo, String Email, String licenseNo, String regDate, String businessType, String businessTrade, Boolean isCredit, String creditDays, String applicableFrom, String creditNumBills, String creditBillValue, String panNo, Boolean isGst, List<GstList> lstGstDetails, Boolean isLicense, List<LicenseInfo> lstLicenseInfo, Boolean isBank, List<BankDetailList> lstBankDetails, Boolean isShipping, List<ShippingInfo> lstShippingInfo, Boolean isDepartment, List<DeptInfo> lstDeptInfo) {
        if (cmbLedgerType.getSelectionModel().getSelectedItem() != null && !tfLedgerCreateLedgerName.getText().isEmpty()) {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnSubmit + tfLedgerCreateLedgerName.getText(), input -> {
                if (input == 1) {
                    try {
                        UnderLedger underLedger = cmbLedgerType.getSelectionModel().getSelectedItem();
                        BalanceType balanceType = cmbLedgerCreateOpeningBalanceType.getSelectionModel().getSelectedItem();
                        BalancingMethods balancingMethods = cmbLedgerCreateBalancingMethods.getSelectionModel().getSelectedItem();
                        String supplierCode = tfLedgerCreateLedgerCode.getText();
                        Map<String, String> body = new HashMap<>();
                        body.put("supplier_code", supplierCode);
                        body.put("under_prefix", underLedger.getUnder_prefix());
                        body.put("associates_id", String.valueOf(underLedger.getAssociates_id()));
                        body.put("principle_group_id", String.valueOf(underLedger.getSub_principle_id()));
                        body.put("principle_id", String.valueOf(underLedger.getPrinciple_id()));
                        body.put("balancing_method", balancingMethods != null ? String.valueOf(balancingMethods.getBalancing_id()) : "1");
                        body.put("opening_bal_type", balanceType != null ? String.valueOf(balanceType.getType()) : "Cr");
                        body.put("opening_bal", tfLedgerCreateOpeningBal.getText());
                        //!Opening Invoice bills if balancing method is bill-by-bill & opening_bal is greater the zero
                        if (tbllstOB.size() > 0) {
                            JSONArray openInvoices = new JSONArray();
                            for (OBListDTO obListDTO : tbllstOB) {

                                openInvoices.put(obListDTO.toAPIData());
                            }
                            body.put("opening_bal_invoice_list", openInvoices.toString());
                        }
                        body.put("underId", String.valueOf(underLedger.getLedger_form_parameter_id()));
                        body.put("slug", underLedger.getLedger_form_parameter_slug());
                        body.put("ledger_name", tfLedgerCreateLedgerName.getText());
                        body.put("ledger_code", tfLedgerCreateLedgerCode.getText());
                        body.put("is_private", "false");
                        body.put("mailing_name", mailingName);
                        body.put("address", address);
                        body.put("state", state);
                        body.put("country", Globals.countryCode);
                        body.put("pincode", pinCode);
                        body.put("mobile_no", mobileNo);
                        body.put("email", Email);
                        body.put("whatsapp_no", whatsappNo);
                        body.put("businessType", businessType);
                        body.put("businessTrade", businessTrade);
                        body.put("licenseNo", licenseNo);
                        body.put("reg_date", regDate);
                        body.put("isCredit", isCredit.toString());
                        if (isCredit == true) {
                            body.put("credit_days", creditDays);
                            body.put("applicable_from", applicableFrom);
                            body.put("creditNumBills", creditNumBills);
                            body.put("creditBillValue", creditBillValue);
                        }
                        //? Gst mapping list
                        body.put("isGST", isGst.toString());
                        if (isGst == true) {
                            if (lstGstDetails.size() > 0) {
                                JSONArray gstJsonArr = new JSONArray();
                                for (GstList gstDetail : lstGstDetails) {
                                    JSONObject gstObj = new JSONObject();
                                    gstObj.put("gstin", gstDetail.getGstNo());
                                    gstObj.put("registration_type", gstDetail.getGstTypeid());
                                    gstObj.put("dateofregistartion", DateConvertUtil.convertStringDatetoAPIDateString(gstDetail.getGstRegDate()));
                                    gstObj.put("pancard", gstDetail.getPanNo());
                                    gstJsonArr.put(gstObj);
                                }
                                body.put("gstdetails", gstJsonArr.toString());
                                body.put("pan_no", "");
                            }
                        } else {
                            body.put("pan_no", panNo);
                        }
                        //? License Info Mapping list
                        body.put("isLicense", isLicense.toString());
                        if (isLicense == true) {
                            if (lstLicenseInfo.size() > 0) {
                                JSONArray licenceJsonArr = new JSONArray();
                                for (LicenseInfo licenseInfo : lstLicenseInfo) {
                                    JSONObject licenseObj = new JSONObject();
                                    JSONObject licenseType = new JSONObject();
                                    licenseType.put("slug_name", licenseInfo.getLicenseSlugName());
                                    licenseObj.put("licences_type", licenseType);
                                    licenseObj.put("licenses_num", licenseInfo.getLicenseNo());
                                    licenseObj.put("licenses_exp", licenseInfo.getLicenseExp() != null ? DateConvertUtil.convertStringDatetoAPIDateString(licenseInfo.getLicenseExp()) : "");
                                    licenseObj.put("license_doc_upload", "");
                                    licenceJsonArr.put(licenseObj);
                                }
                                body.put("licensesDetails", licenceJsonArr.toString());
                            }
                        }
                        //? Shipping Details list Mapping
                        body.put("isShippingDetails", isShipping.toString());
                        if (isShipping) {
                            if (lstShippingInfo.size() > 0) {
                                JSONArray shippingJsonArr = new JSONArray();
                                for (ShippingInfo shippingInfo : lstShippingInfo) {
                                    JSONObject shippingObj = new JSONObject();
                                    shippingObj.put("shipping_address", shippingInfo.getShippingName());
                                    shippingObj.put("district", shippingInfo.getShippingStateId());
                                    shippingJsonArr.put(shippingObj);
                                }
                                body.put("shippingDetails", shippingJsonArr.toString());
                            }
                        }
                        //? Department list Mapping
                        body.put("isDepartment", isDepartment.toString());
                        if (isDepartment == true) {
                            if (lstDeptInfo.size() > 0) {
                                JSONArray deptJsonArr = new JSONArray();
                                for (DeptInfo deptInfo : lstDeptInfo) {
                                    JSONObject deptObj = new JSONObject();
                                    deptObj.put("dept", deptInfo.getPersonDeptName());
                                    deptObj.put("contact_person", deptInfo.getPersonName());
                                    deptObj.put("contact_no", deptInfo.getPersonPhone());
                                    deptObj.put("email", deptInfo.getPersonEmail().trim().isEmpty() ? "" : deptInfo.getPersonEmail());
                                    deptJsonArr.put(deptObj);
                                }
                                body.put("deptDetails", deptJsonArr.toString());
                            }
                        }
                        //? Bank list mapping
                        body.put("isBankDetails", isBank.toString());
                        if (isBank == true) {
                            if (lstBankDetails.size() > 0) {
                                JSONArray bankJsonArr = new JSONArray();
                                for (BankDetailList bankDetails : lstBankDetails) {
                                    JSONObject bankObj = new JSONObject();
                                    bankObj.put("bank_name", bankDetails.getBankName());
                                    bankObj.put("bank_account_no", bankDetails.getBankAccNo());
                                    bankObj.put("bank_branch", bankDetails.getBankBranch());
                                    bankObj.put("bank_ifsc_code", bankDetails.getBankIFSCCode());
                                    bankJsonArr.put(bankObj);
                                }
                                body.put("bankDetails", bankJsonArr.toString());
                            }
                        }
                        System.out.println("body in =>" + body);
                        Map<String, String> headers = new HashMap<>();
                        headers.put("branch", Globals.headerBranch);
                        String response = APIClient.postMultipartRequest(body, null, EndPoints.createLedgerMaster, headers);
                        //? HIGHLIGHT
                        LedgerListController.isNewLedgerCreated = true; //? Set the flag for new creation
                        JSONObject resObj = new JSONObject(response);
                        if (resObj.getInt("responseStatus") == 200) {
                            clearForm();
                            String message = resObj.getString("message");
                            String ledgerId = resObj.getString("data");
                            if (ledDTO != null) {
                                ledDTO.setLedegrId(ledgerId);
                            }
                            if (!redirectSlug.isEmpty()) {
                                AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, inp -> {
                                    GlobalController.getInstance().addTabStaticWithIsNewTab(redirectSlug, false, ledDTO);
                                });
                            } else {
                                AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, inp -> {
                                    GlobalController.getInstance().addTabStatic("ledger-list", false);
                                });
                            }
//                            AlertUtility.CustomCallback callback = number -> {
//                                GlobalController.getInstance().addTabStatic("ledger-list", false);
//                            };
//                            AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, callback);
                        } else {
                            String message = resObj.getString("message");

                            AlertUtility.CustomCallback callback = number -> {

                            };
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, callback);
                        }
                    } catch (Exception e) {
//                System.out.println("exception:" + e);
                        loggerLedgerCreate.error("Exception in LedgerCreateSCSubmit(): " + Globals.getExceptionString(e));
                    }
                }
            });
        } else {
            String msg = "Please Fill Required Fields !";
            AlertUtility.CustomCallback callback = number -> {
                GlobalTranx.requestFocusOrDieTrying(tfLedgerCreateLedgerName, 2);
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
        }
    }


    public void LedgerCreateSDSubmit(String mailingName, String address, String state, String pinCode, String mobileNo, String whatsappNo, String Email, String licenseNo, String regDate, String businessType, String businessTrade, Boolean isCredit, String creditDays, String applicableFrom, String creditNumBills, String creditBillValue, String panNo, Boolean isGst, List<GstList> lstGstDetails, Boolean isLicense, List<LicenseInfo> lstLicenseInfo, Boolean isBank, List<BankDetailList> lstBankDetails, Boolean isShipping, List<ShippingInfo> lstShippingInfo, Boolean isDepartment, List<DeptInfo> lstDeptInfo, Boolean isSalesman, String salesManId, String areaId, String route) {
        if (cmbLedgerType.getSelectionModel().getSelectedItem() != null && !tfLedgerCreateLedgerName.getText().isEmpty()) {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnSubmit + tfLedgerCreateLedgerName.getText(), input -> {
                if (input == 1) {
                    try {
                        UnderLedger underLedger = cmbLedgerType.getSelectionModel().getSelectedItem();
                        BalanceType balanceType = cmbLedgerCreateOpeningBalanceType.getSelectionModel().getSelectedItem();
                        BalancingMethods balancingMethods = cmbLedgerCreateBalancingMethods.getSelectionModel().getSelectedItem();
                        String supplierCode = tfLedgerCreateLedgerCode.getText();
                        Map<String, String> body = new HashMap<>();
                        body.put("supplier_code", supplierCode);
                        body.put("under_prefix", underLedger.getUnder_prefix());
                        body.put("associates_id", String.valueOf(underLedger.getAssociates_id()));
                        body.put("principle_group_id", String.valueOf(underLedger.getSub_principle_id()));
                        body.put("principle_id", String.valueOf(underLedger.getPrinciple_id()));
                        body.put("balancing_method", balancingMethods != null ? String.valueOf(balancingMethods.getBalancing_id()) : "1");
                        body.put("opening_bal_type", balanceType != null ? String.valueOf(balanceType.getType()) : "Dr");
                        body.put("opening_bal", tfLedgerCreateOpeningBal.getText());
                        //!Opening Invoice bills if balancing method is bill-by-bill & opening_bal is greater the zero
                        if (tbllstOB.size() > 0) {
                            JSONArray openInvoices = new JSONArray();
                            for (OBListDTO obListDTO : tbllstOB) {
                                openInvoices.put(obListDTO.toAPIData());
                            }
                            body.put("opening_bal_invoice_list", openInvoices.toString());
                        }
                        body.put("underId", String.valueOf(underLedger.getLedger_form_parameter_id()));
                        body.put("slug", underLedger.getLedger_form_parameter_slug());
                        body.put("ledger_name", tfLedgerCreateLedgerName.getText());
                        body.put("ledger_code", tfLedgerCreateLedgerCode.getText());
                        body.put("is_private", "false");
                        body.put("mailing_name", mailingName);
                        body.put("address", address);
                        body.put("state", state);
                        body.put("country", Globals.countryCode);
                        body.put("pincode", pinCode);
                        body.put("mobile_no", mobileNo);
                        body.put("email", Email);
                        body.put("whatsapp_no", whatsappNo);
                        body.put("businessType", businessType);
                        body.put("businessTrade", businessTrade);
                        body.put("licenseNo", licenseNo);
                        body.put("reg_date", regDate);
                        body.put("isCredit", isCredit.toString());
                        if (isCredit == true) {
                            body.put("credit_days", creditDays);
                            body.put("applicable_from", applicableFrom);
                            body.put("creditNumBills", creditNumBills);
                            body.put("creditBillValue", creditBillValue);
                        }
                        //? Gst mapping list
                        body.put("isGST", isGst.toString());
                        if (isGst == true) {
                            if (lstGstDetails.size() > 0) {
                                JSONArray gstJsonArr = new JSONArray();
                                for (GstList gstDetail : lstGstDetails) {
                                    JSONObject gstObj = new JSONObject();
                                    gstObj.put("gstin", gstDetail.getGstNo());
                                    gstObj.put("registration_type", gstDetail.getGstTypeid());
                                    gstObj.put("dateofregistartion", DateConvertUtil.convertStringDatetoAPIDateString(gstDetail.getGstRegDate()));
                                    gstObj.put("pancard", gstDetail.getPanNo());
                                    gstJsonArr.put(gstObj);
                                }
                                body.put("gstdetails", gstJsonArr.toString());
                                body.put("pan_no", "");
                            }
                        } else {
                            body.put("pan_no", panNo);
                        }
                        //? License Info Mapping list
                        body.put("isLicense", isLicense.toString());
                        if (isLicense == true) {
                            if (lstLicenseInfo.size() > 0) {
                                JSONArray licenceJsonArr = new JSONArray();
                                for (LicenseInfo licenseInfo : lstLicenseInfo) {
                                    JSONObject licenseObj = new JSONObject();
                                    JSONObject licenseType = new JSONObject();
                                    licenseType.put("slug_name", licenseInfo.getLicenseSlugName());
                                    licenseObj.put("licences_type", licenseType);
                                    licenseObj.put("licenses_num", licenseInfo.getLicenseNo());
                                    licenseObj.put("licenses_exp", licenseInfo.getLicenseExp() != null ? DateConvertUtil.convertStringDatetoAPIDateString(licenseInfo.getLicenseExp()) : "");
                                    licenseObj.put("license_doc_upload", "");
                                    licenceJsonArr.put(licenseObj);
                                }
                                body.put("licensesDetails", licenceJsonArr.toString());
                            }
                        }
                        //? Shipping Details list Mapping
                        body.put("isShippingDetails", isShipping.toString());
                        if (isShipping) {
                            if (lstShippingInfo.size() > 0) {
                                JSONArray shippingJsonArr = new JSONArray();
                                for (ShippingInfo shippingInfo : lstShippingInfo) {
                                    JSONObject shippingObj = new JSONObject();
                                    shippingObj.put("shipping_address", shippingInfo.getShippingName());
                                    shippingObj.put("district", shippingInfo.getShippingStateId());
                                    shippingJsonArr.put(shippingObj);
                                }
                                body.put("shippingDetails", shippingJsonArr.toString());
                            }
                        }
                        //? Department list Mapping
                        body.put("isDepartment", isDepartment.toString());
                        if (isDepartment == true) {
                            if (lstDeptInfo.size() > 0) {
                                JSONArray deptJsonArr = new JSONArray();
                                for (DeptInfo deptInfo : lstDeptInfo) {
                                    JSONObject deptObj = new JSONObject();
                                    deptObj.put("dept", deptInfo.getPersonDeptName());
                                    deptObj.put("contact_person", deptInfo.getPersonName());
                                    deptObj.put("contact_no", deptInfo.getPersonPhone());
                                    deptObj.put("email", deptInfo.getPersonEmail().trim().isEmpty() ? "" : deptInfo.getPersonEmail());
                                    deptJsonArr.put(deptObj);
                                }
                                body.put("deptDetails", deptJsonArr.toString());
                            }
                        }
                        //? Bank list mapping
                        body.put("isBankDetails", isBank.toString());
                        if (isBank == true) {
                            if (lstBankDetails.size() > 0) {
                                JSONArray bankJsonArr = new JSONArray();
                                for (BankDetailList bankDetails : lstBankDetails) {
                                    JSONObject bankObj = new JSONObject();
                                    bankObj.put("bank_name", bankDetails.getBankName());
                                    bankObj.put("bank_account_no", bankDetails.getBankAccNo());
                                    bankObj.put("bank_branch", bankDetails.getBankBranch());
                                    bankObj.put("bank_ifsc_code", bankDetails.getBankIFSCCode());
                                    bankJsonArr.put(bankObj);
                                }
                                body.put("bankDetails", bankJsonArr.toString());
                            }
                        }
                        //?Sales man exist
                        body.put("salesmanDetails", isSalesman.toString());
                        if (isSalesman == true) {
                            body.put("route", route);
                            body.put("salesmanId", salesManId);
                            body.put("area", areaId);
                        }
                        Map<String, String> headers = new HashMap<>();
                        System.out.println("body SD =>" + body);
                        headers.put("branch", Globals.headerBranch);

                        String response = APIClient.postMultipartRequest(body, null, EndPoints.createLedgerMaster, headers);
                        //? HIGHLIGHT
                        LedgerListController.isNewLedgerCreated = true; //? Set the flag for new creation
                        JSONObject resObj = new JSONObject(response);
                        if (resObj.getInt("responseStatus") == 200) {
                            String message = resObj.getString("message");
                            AlertUtility.CustomCallback callback = number -> {
                                //? Redirect and HIGHLIGHT New Record in Franchise List
                                if (FranchiseListController.isNewFranchiseCreated == true) //? Set the flag for new creation
                                {
                                    GlobalController.getInstance().addTabStatic(FRANCHISE_LIST_SLUG, false);
                                } else if (!redirectSlug.isEmpty()) {
                                    String ledgerId = resObj.getString("data");
                                    if (ledDTO != null) {
                                        ledDTO.setLedegrId(ledgerId);
                                    }
                                    AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, inp -> {
                                        GlobalController.getInstance().addTabStaticWithIsNewTab(redirectSlug, false, ledDTO);
                                    });
                                } else {
                                    AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, inp -> {
                                        GlobalController.getInstance().addTabStatic("ledger-list", false);
                                    });
                                }
                            };
                            AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, callback);
                        } else {
                            String message = resObj.getString("message");

                            AlertUtility.CustomCallback callback = number -> {
                            };
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, callback);
                        }
                    } catch (Exception e) {
//                System.out.println("exception:" + e);
                        loggerLedgerCreate.error("Exception in LedgerCreateSDSubmit(): " + Globals.getExceptionString(e));
                    }
                }
            });
        } else {
            String msg = "Please Fill Required Fields !";
            AlertUtility.CustomCallback callback = number -> {
                GlobalTranx.requestFocusOrDieTrying(tfLedgerCreateLedgerName, 2);
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
        }
    }

    public void setFrDataToLedger(FranchiseDTO franchiseDTO) {
        frDto = franchiseDTO;
//        System.out.println("resObj ->" + resObj);
        String edtunderPrefix = "PG#1";
        if (!edtunderPrefix.isEmpty()) {
            UnderLedger underLedger = cmbUnderList.stream().filter((s -> s.under_prefix.equalsIgnoreCase(edtunderPrefix))).findAny().orElse(null);
            if (underLedger != null) {
                cmbLedgerType.setValue(underLedger);
//                cmbLedgerType.setDisable(true);
                //!load on Franchise creation
                LoadLedgerFormCondition(cmbLedgerType != null && cmbLedgerType.getValue() != null ? cmbLedgerType.getValue().getLedger_form_parameter_slug() : "");
            }
        }

//                int edtBalancingMethod = 0;
//                if (edtBalancingMethod > 0) {
//                    lstBalancingMethods.stream().filter(s -> s.getBalancing_id() == edtBalancingMethod).findAny().ifPresent(balancingMethods -> cmbLedgerCreateBalancingMethods.setValue(balancingMethods));
//                }
//
//
//                String edtBalanceType = "CR";
//                if (edtBalanceType != null) {
//                    lstBalanceType.stream().filter(s -> s.getType().equalsIgnoreCase(edtBalanceType)).findAny().ifPresent(balanceType -> cmbLedgerCreateOpeningBalanceType.setValue(balanceType));
//                }

        tfLedgerCreateLedgerName.setText(franchiseDTO.getFranchiseName());
        tfLedgerCreateLedgerCode.setText(franchiseDTO.getFranchiseCode());
//            tfLedgerCreateLedgerCode.setText(resObj.getString("supplier_code"));
//            if (resObj.has("opening_bal")) tfLedgerCreateOpeningBal.setText(resObj.getInt("opening_bal") + "");
        cmbLedgerCreateOpeningBalanceType.setValue(lstBalanceType.get(0));
    }

    public void setRedirectData(Object infranchiseDTO) {

        ledDTO = (PurTranxToProductRedirectionDTO) infranchiseDTO;

        redirectSlug = ledDTO.getRedirect();

        System.out.println("HashMap data >> : " + ledDTO.getChallanNo());
        System.out.println("hash" + ledDTO.getRowData());
        System.out.println("ledDTO.getPurAcc() >> : " + ledDTO.getPurAcc());
        System.out.println("RediPrdCurrIndex in Product >> " + ledDTO.getRediPrdCurrIndex());
        System.out.println("RediPrdCurrIndex in Product >> " + ledDTO.getTranxType());
        if (ledDTO.getTranxType() == "purchase") {
            String edtunderPrefix = "PG#5";
            if (!edtunderPrefix.isEmpty()) {
                UnderLedger underLedger = cmbUnderList.stream().filter((s -> s.under_prefix.equalsIgnoreCase(edtunderPrefix))).findAny().orElse(null);
                if (underLedger != null) {
                    cmbLedgerType.setValue(underLedger);
                    cmbLedgerType.setEditable(false);
                }
            }
        } else {
            String edtunderPrefix = "PG#1";
            if (!edtunderPrefix.isEmpty()) {
                UnderLedger underLedger = cmbUnderList.stream().filter((s -> s.under_prefix.equalsIgnoreCase(edtunderPrefix))).findAny().orElse(null);
                if (underLedger != null) {
                    cmbLedgerType.setValue(underLedger);
                    cmbLedgerType.setEditable(false);
                }
            }
        }
    }

    public void ledgerTyped(KeyEvent keyEvent) {
        String s = tfLedgerCreateLedgerName.getText();
    }
}
