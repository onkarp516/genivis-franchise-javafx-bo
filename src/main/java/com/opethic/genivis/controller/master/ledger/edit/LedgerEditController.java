package com.opethic.genivis.controller.master.ledger.edit;

import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.master.FranchiseListController;
import com.opethic.genivis.controller.master.ledger.LedgerListController;
import com.opethic.genivis.controller.master.ledger.common.LedgerCommonController;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.dto.master.ledger.OBListDTO;
import com.opethic.genivis.models.master.ledger.*;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.*;


public class LedgerEditController implements Initializable {

    @FXML
    private BorderPane anpledgercreate;

    @FXML
    private HBox ledgerMainTopEditRow;

    @FXML
    private StackPane loadLedgerPage;

    @FXML
    private ComboBox<UnderLedger> cmbLedgerType;

    @FXML
    private ComboBox<BalancingMethods> cmbLedgerCreateBalancingMethods;


    @FXML
    private ComboBox<BalanceType> cmbLedgerCreateOpeningBalanceType;


    @FXML
    private Label lblLedgerCreateBalancingMethods, lblLedgerCreateLedgerCode;


    @FXML
    private TextField tfLedgerCreateLedgerName, tfLedgerCreateLedgerCode, tfLedgerCreateOpeningBal;

    private Integer ledgerEditId = -1;
    ObservableList<UnderLedger> cmbUnderList = FXCollections.observableArrayList();
    ObservableList<BalancingMethods> lstBalancingMethods = FXCollections.observableArrayList();
    ObservableList<BalanceType> lstBalanceType = FXCollections.observableArrayList();

    private JSONObject resObj = null;

    private static final Logger loggerLedgerEdit = LogManager.getLogger(LedgerEditController.class);

    ObservableList<OBListDTO> tbllstOB = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        //set the ledger global controller
        GlobalLedgerEditController globalLedgerEditControllerController = GlobalLedgerEditController.getInstance();
        globalLedgerEditControllerController.setController(this);
        getUnderList();
        getBalancingTypeList();
        getBalanceTypeList();
        Platform.runLater(() -> {
            cmbLedgerType.requestFocus();
        });
        initLedgerShorcuts();
        initLedgerOpeningBalancePopup();

        cmbLedgerCreateOpeningBalanceType.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cmbLedgerCreateOpeningBalanceType.isShowing()) {
                cmbLedgerCreateOpeningBalanceType.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });
    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            ledgerMainTopEditRow.setSpacing(5);
            anpledgercreate.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            ledgerMainTopEditRow.setSpacing(10);
            anpledgercreate.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            ledgerMainTopEditRow.setSpacing(12);
            anpledgercreate.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            ledgerMainTopEditRow.setSpacing(15);
            anpledgercreate.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            ledgerMainTopEditRow.setSpacing(15);
            anpledgercreate.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            anpledgercreate.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            anpledgercreate.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_7.css").toExternalForm());
        }
    }

    private void initLedgerOpeningBalancePopup() {
        cmbLedgerType.focusedProperty().addListener((obs, old, nw) -> {
            if (!nw) {
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
        tfLedgerCreateOpeningBal.focusedProperty().addListener((obs, old, nw) -> {
            if (!nw) {
                BalancingMethods selectedBalancingMethod = cmbLedgerCreateBalancingMethods.getSelectionModel().getSelectedItem();
                String openBal = tfLedgerCreateOpeningBal.getText();
                Double actOpenBal = Double.parseDouble(openBal);
                if (selectedBalancingMethod != null && actOpenBal > 0) {
                    if (selectedBalancingMethod.getBalance_method().equalsIgnoreCase("Bill by Bill")) {
                        Stage stage = (Stage) anpledgercreate.getScene().getWindow();
                        LedgerCommonController ledgerCommonController = new LedgerCommonController();
                        ledgerCommonController.openOpeningBalancePopUpWithParam(stage, actOpenBal, tbllstOB, input -> {
                            tbllstOB = input;
                        });
                    }
                }
            }
        });

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
        anpledgercreate.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.TAB) {
//                System.out.println("event - target Tab -> " + event.getTarget());
                if (event.getTarget() instanceof TextField) {
                    TextField rqTxFld = (TextField) event.getTarget();
                    if (rqTxFld.getStyleClass().contains("isRequired") && rqTxFld.getText().isEmpty()) {
                        GlobalTranx.requestFocusOrDieTrying(rqTxFld, 1);
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
                } else {
                    Event.fireEvent(event.getTarget(), newEvent);
                }
                event.consume();

            } else */
            if (event.getCode() == KeyCode.X && event.isControlDown()) {
                //! Sundry Creditor
                Button btnSCCancel = (Button) loadLedgerPage.lookup("#btnScLedgerEditCancel");
                if (btnSCCancel != null) btnSCCancel.fire();
                //!  Sundry Debtor
                Button btnSDCancel = (Button) loadLedgerPage.lookup("#btnSdLedgerEditCancel");
                if (btnSDCancel != null) btnSDCancel.fire();
                //!  Tax
                Button btnTaxCancel = (Button) loadLedgerPage.lookup("#btnLedgerEditTaxCancel");
                if (btnTaxCancel != null) btnTaxCancel.fire();
                //!  Other
                Button btnOtherCancel = (Button) loadLedgerPage.lookup("#btnLedgerEditOtherCancel");
                if (btnOtherCancel != null) btnOtherCancel.fire();
                //!  Bank
                Button btnBankCancel = (Button) loadLedgerPage.lookup("#btnbankaccLedgerEditCancel");
                if (btnBankCancel != null) btnBankCancel.fire();
                //!  Assets
                Button btnAssetsCancel = (Button) loadLedgerPage.lookup("#btnLedgerEditAssetsCancel");
                if (btnAssetsCancel != null) btnAssetsCancel.fire();

            } else if (event.getCode() == KeyCode.S && event.isControlDown()) {
                //! Sundry Creditor
                Button btnSCSubmit = (Button) loadLedgerPage.lookup("#btnScLedgerEditSubmit");
                if (btnSCSubmit != null) btnSCSubmit.fire();
                //! Sundry Debtor
                Button btnSDSubmit = (Button) loadLedgerPage.lookup("#btnSdLedgerEditSubmit");
                if (btnSDSubmit != null) btnSDSubmit.fire();
                //! Tax
                Button btnTaxSubmit = (Button) loadLedgerPage.lookup("#btnLedgerEditTaxSubmit");
                if (btnTaxSubmit != null) btnTaxSubmit.fire();
                //! Other
                Button btnOtherSubmit = (Button) loadLedgerPage.lookup("#btnLedgerEditOtherSubmit");
                if (btnOtherSubmit != null) btnOtherSubmit.fire();
                //! Bank
                Button btnBankSubmit = (Button) loadLedgerPage.lookup("#btnbankaccLedgerEditSubmit");
                if (btnBankSubmit != null) btnBankSubmit.fire();
                //! Assets
                Button btnAssetsSubmit = (Button) loadLedgerPage.lookup("#btnLedgerEditAssetsSubmit");
                if (btnAssetsSubmit != null) {
                    btnAssetsSubmit.fire();
                    GlobalTranx.requestFocusOrDieTrying(btnAssetsSubmit, 3);
                }
            }
        });
    }

    private void getBalanceTypeList() {
//        System.out.println(":getBalanceTypeList start");
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
                // You can implement this method if needed
                return null;
            }
        });
//        System.out.println(":getBalanceTypeList end");
    }

    private void getBalancingTypeList() {
//        System.out.println(":getBalancingTypeList start");
        List<BalancingMethods> lst = APICallgetBalancingMethods();
        lstBalancingMethods = FXCollections.observableArrayList(lst);

        cmbLedgerCreateBalancingMethods.getItems().addAll(lstBalancingMethods);
        cmbLedgerCreateBalancingMethods.setConverter(new StringConverter<BalancingMethods>() {
            @Override
            public String toString(BalancingMethods methods) {
                return methods != null ? methods.getBalance_method() : "";
            }

            @Override
            public BalancingMethods fromString(String string) {
                // You can implement this method if needed
                return null;
            }
        });
//        System.out.println(":getBalancingTypeList end");
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
            String msg = resObj.getString("message");
            AlertUtility.CustomCallback callback = number -> {
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
        }
        // Adding the array of objects to the ComboBox
        return balancingMethodsList;
    }


    private void getUnderList() {
//        System.out.println(":getUnderList start");
        Integer initialIndex = -1;
        List<UnderLedger> lst = APICallgetUnderList();
        cmbUnderList = FXCollections.observableArrayList(lst);
//        FilteredList<UnderLedger> filteredItems = new FilteredList<>(cmbUnderList);
//        cmbLedgerType.setItems(cmbUnderList);
        cmbLedgerType.getItems().addAll(cmbUnderList);
//        cmbLedgerType.setEditable(true);

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
        //? Show drop down open
//        AutoCompleteBox<UnderLedger> cmbAutoUnderLedger = new AutoCompleteBox<>(cmbLedgerType);
        Globals.showDropDownOnKeyPress(cmbLedgerType);
//        cmbLedgerType.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
//
//            System.out.println("new Value" + newValue);
//            if (newValue != null && !newValue.isEmpty()) {
//                List<UnderLedger> filteredPrinciples = cmbUnderList.stream()
//                        .filter(item -> item.getDisplay_name().toLowerCase().contains(newValue.toLowerCase()))
//                        .collect(Collectors.toList());
//                System.out.println("filter lst => " + filteredPrinciples)
//                ;
//                if (filteredPrinciples.size() > 0) {
//                    cmbLedgerType.setItems(FXCollections.observableArrayList(filteredPrinciples));
//                    cmbLedgerType.show();
//                }
////                cmbLedgerType.getItems().clear();
////                cmbLedgerType.getItems().addAll(filteredPrinciples);
//
////                if (cmbUnderList.size() == 1) {
////                    cmbLedgerType.getSelectionModel().select(0);
////                }
//            } else if (newValue.isEmpty()) {
////                cmbLedgerType.setItems(cmbUnderList);
////                cmbLedgerType.getItems().clear();
////                cmbLedgerType.getItems().addAll(cmbUnderList);
//                cmbLedgerType.setItems(cmbUnderList);
//
//            }
//        });
        cmbLedgerType.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            LoadLedgerFormCondition(newValue != null ? newValue.getLedger_form_parameter_slug() : "");
        });

        AutoCompleteBox autoCompleteBox = new AutoCompleteBox(cmbLedgerType, initialIndex);

//        cmbLedgerType.setOnAction(actionEvent -> {
////            int selectedIndex = cmbLedgerType.getSelectionModel().getSelectedIndex();
////            Object selectedItem = cmbLedgerType.getSelectionModel().getSelectedItem();
////
////            System.out.println("Selection made: [" + selectedIndex + "] " + selectedItem);
////            System.out.println("   ComboBox.getValue(): " + cmbLedgerType.getValue().getLedger_form_parameter_slug());
//            LoadLedgerFormCondition(cmbLedgerType.getValue() != null ? cmbLedgerType.getValue().getLedger_form_parameter_slug() : "");
//
//        });


//        cmbLedgerType.getItems().addAll(cmbUnderList);
        // Setting a custom StringConverter to display the name property


//        cmbLedgerType.setOnAction(event -> {
//            Object selectedUnderLedger = cmbLedgerType.getSelectionModel().getSelectedItem();
//            for(UnderLedger underLedger: cmbUnderList){
//                if(selectedUnderLedger.toString().equals(underLedger.getDisplay_name())){
////                    System.out.println("id = "+principle.getPrinciple_id());
////                    System.out.println("under prefix = "+principle.getUnder_prefix());
////                    map.put("principle_id",principle.getPrinciple_id());
////                    map.put("principle_name",principle.getPrinciple_name());
////                    map.put("under_prefix",principle.getUnder_prefix());
////                    map.put("sub_principle_id",principle.getSub_principle_id());
//
//                }
//            }
//        });


//        cmbLedgerType.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                List<UnderLedger> filteredUnderLedger = cmbUnderList.stream()
//                        .filter(principle -> principle.getDisplay_name().toLowerCase().contains(newValue.toLowerCase()))
//                        .collect(Collectors.toList());
//                cmbLedgerType.getItems().addAll(FXCollections.observableArrayList(filteredUnderLedger));
//
////                cmbLedgerType.setItems();
//            } else {
//                cmbLedgerType.getItems().addAll(cmbUnderList);
////                cmbLedgerType.setItems(cmbUnderList);
//            }
//        });
//        cmbLedgerType.getEditor().textProperty().addListener((ov, oldValue, newValue) -> Platform.runLater(
//                () -> filteredItems.setPredicate(item -> (item.getDisplay_name().toLowerCase().startsWith(newValue.toLowerCase())) ? true : newValue.isEmpty())
//        ));

//        cmbLedgerType.setItems(filteredItems);
//        System.out.println(":getUnderList end");
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
                    loggerLedgerEdit.info("Load ledger form on condition -> " + GenivisApplication.class.getResource(ConstLedgerCreateFxml.edit_const_sundry_creditors));
                    setUpperVisible(true);
                    nLoader = new FXMLLoader(GenivisApplication.class.getResource(ConstLedgerCreateFxml.edit_const_sundry_creditors));
                    nRoot = nLoader.load();
                    loadLedgerPage.getChildren().clear();
                    loadLedgerPage.getChildren().add(nRoot);
                    LedgerEditSCController ledgerEditSCController = nLoader.getController();
                    if (resObj != null) ledgerEditSCController.setEditData(resObj);

                } catch (IOException e) {
                    loggerLedgerEdit.error("Exception in LoadLedgerFormCondition(): -> sundry_creditors :" + Globals.getExceptionString(e));
                }
                break;

            case "sundry_debtors":
                try {
                    loggerLedgerEdit.info("Load ledger form on condition -> " + GenivisApplication.class.getResource(ConstLedgerCreateFxml.edit_const_sundry_debtors));
                    setUpperVisible(true);
                    nLoader = new FXMLLoader(GenivisApplication.class.getResource(ConstLedgerCreateFxml.edit_const_sundry_debtors));
                    nRoot = nLoader.load();
                    loadLedgerPage.getChildren().clear();
                    loadLedgerPage.getChildren().add(nRoot);
                    LedgerEditSDController ledgerEditSDController = nLoader.getController();
                    if (resObj != null) ledgerEditSDController.setEditData(resObj);
                } catch (IOException e) {
                    loggerLedgerEdit.error("Exception in LoadLedgerFormCondition(): -> sundry_debtors :" + Globals.getExceptionString(e));
                    e.printStackTrace();
//                    throw new RuntimeException(e);
                }
                break;
            case "bank_account":
                try {
                    loggerLedgerEdit.info("Load ledger form on condition -> " + GenivisApplication.class.getResource(ConstLedgerCreateFxml.edit_const_bank_account));
                    setUpperVisible(false);

                    nLoader = new FXMLLoader(GenivisApplication.class.getResource(ConstLedgerCreateFxml.edit_const_bank_account));
                    nRoot = nLoader.load();
                    loadLedgerPage.getChildren().clear();
                    loadLedgerPage.getChildren().add(nRoot);
                    LedgerEditBankAccController ledgerEditBankAccController = nLoader.getController();
                    if (resObj != null) ledgerEditBankAccController.setEditData(resObj);

                } catch (IOException e) {
                    loggerLedgerEdit.error("Exception in LoadLedgerFormCondition(): -> sundry_debtors :" + Globals.getExceptionString(e));
                }
                break;
            case "duties_taxes":
                try {
                    loggerLedgerEdit.info("Load ledger form on condition -> " + GenivisApplication.class.getResource(ConstLedgerCreateFxml.edit_const_duties_taxes));
                    setUpperVisible(false);

                    nLoader = new FXMLLoader(GenivisApplication.class.getResource(ConstLedgerCreateFxml.edit_const_duties_taxes));
                    nRoot = nLoader.load();
                    loadLedgerPage.getChildren().clear();
                    loadLedgerPage.getChildren().add(nRoot);
                    LedgerEditTaxController ledgerEditTaxController = nLoader.getController();
                    if (resObj != null) ledgerEditTaxController.setEditData(resObj);


                } catch (IOException e) {
                    loggerLedgerEdit.error("Exception in LoadLedgerFormCondition(): -> duties_taxes :" + Globals.getExceptionString(e));
//                    throw new RuntimeException(e);
                }
                break;
            case "assets":
                try {
                    loggerLedgerEdit.info("Load ledger form on condition -> " + GenivisApplication.class.getResource(ConstLedgerCreateFxml.edit_const_assets));
                    setUpperVisible(false);

                    nLoader = new FXMLLoader(GenivisApplication.class.getResource(ConstLedgerCreateFxml.edit_const_assets));
                    nRoot = nLoader.load();
                    loadLedgerPage.getChildren().clear();
                    loadLedgerPage.getChildren().add(nRoot);

                } catch (IOException e) {
                    loggerLedgerEdit.error("Exception in LoadLedgerFormCondition(): -> assets :" + Globals.getExceptionString(e));
//                    throw new RuntimeException(e);
                }
                break;
            default:
                //! other
                try {
                    loggerLedgerEdit.info("Load ledger form on condition -> " + GenivisApplication.class.getResource(ConstLedgerCreateFxml.edit_const_other));
                    setUpperVisible(false);
                    nLoader = new FXMLLoader(GenivisApplication.class.getResource(ConstLedgerCreateFxml.edit_const_other));
                    nRoot = nLoader.load();
                    loadLedgerPage.getChildren().clear();
                    loadLedgerPage.getChildren().add(nRoot);

                } catch (IOException e) {
                    loggerLedgerEdit.error("Exception in LoadLedgerFormCondition(): -> other :" + Globals.getExceptionString(e));
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
            // Adding the array of objects to the ComboBox
        } else {
            String msg = resObj.getString("message");
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
    public void LedgerEditOtherSubmit() {

//        System.out.println("other ledger create controller submit");
//        System.out.println(cmbLedgerCreateOpeningBalanceType.getSelectionModel().getSelectedItem());
//        System.out.println(cmbLedgerType.getSelectionModel().getSelectedItem());
//        System.out.println(tfLedgerCreateLedgerName.getText());
//        System.out.println(tfLedgerCreateOpeningBal.getText());
        if (cmbLedgerType.getSelectionModel().getSelectedItem() != null && !tfLedgerCreateLedgerName.getText().isEmpty()) {
            try {
                UnderLedger underLedger = cmbLedgerType.getSelectionModel().getSelectedItem();
                BalanceType balanceType = cmbLedgerCreateOpeningBalanceType.getSelectionModel().getSelectedItem();
                Map<String, String> body = new HashMap<>();
                body.put("id", ledgerEditId.toString());
                body.put("under_prefix", underLedger.getUnder_prefix());
                body.put("associates_id", String.valueOf(underLedger.getAssociates_id()));
                body.put("principle_group_id", String.valueOf(underLedger.getSub_principle_id()));
                body.put("principle_id", String.valueOf(underLedger.getPrinciple_id()));
                body.put("opening_bal_type", balanceType != null ? String.valueOf(balanceType.getId()) : "1");
                body.put("opening_bal", tfLedgerCreateOpeningBal.getText());
                body.put("underId", String.valueOf(underLedger.getLedger_form_parameter_id()));
                body.put("slug", underLedger.getLedger_form_parameter_slug());
                body.put("ledger_name", tfLedgerCreateLedgerName.getText());
                body.put("is_private", "false");

                Map<String, String> headers = new HashMap<>();
//                headers.put("branch", "gvhm001");
                headers.put("branch", Globals.headerBranch);

//                Map<String, File> fileMap = null;
                String response = APIClient.postMultipartRequest(body, null, EndPoints.editLedgerMaster, headers);
                //? HIGHLIGHT
                LedgerListController.editedLedgerId = ledgerEditId; //? Set the ID for editing
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
                loggerLedgerEdit.error("Exception LedgerEditOtherSubmit() : " + Globals.getExceptionString(e));

            }
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
                GlobalController.getInstance().addTabStatic("ledger-list", false);
            }
        };
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure to cancel", callback);
    }

    public void LedgerEditTaxSubmit(String taxtype) {

//        System.out.println("other ledger create controller submit");
//        System.out.println(cmbLedgerCreateOpeningBalanceType.getSelectionModel().getSelectedItem());
//        System.out.println(cmbLedgerType.getSelectionModel().getSelectedItem());
//        System.out.println(tfLedgerCreateLedgerName.getText());
//        System.out.println(tfLedgerCreateOpeningBal.getText());
        if (cmbLedgerType.getSelectionModel().getSelectedItem() != null && !tfLedgerCreateLedgerName.getText().isEmpty()) {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnUpdate + tfLedgerCreateLedgerName.getText(), input -> {
                if (input == 1) {
                    try {
                        UnderLedger underLedger = cmbLedgerType.getSelectionModel().getSelectedItem();
                        BalanceType balanceType = cmbLedgerCreateOpeningBalanceType.getSelectionModel().getSelectedItem();
                        Map<String, String> body = new HashMap<>();
                        body.put("id", ledgerEditId.toString());
                        body.put("under_prefix", underLedger.getUnder_prefix());
                        body.put("associates_id", String.valueOf(underLedger.getAssociates_id()));
                        body.put("principle_group_id", String.valueOf(underLedger.getSub_principle_id()));
                        body.put("principle_id", String.valueOf(underLedger.getPrinciple_id()));
                        body.put("opening_bal_type", balanceType != null ? String.valueOf(balanceType.getType()) : "Cr");
                        body.put("opening_bal", tfLedgerCreateOpeningBal.getText());
                        body.put("underId", String.valueOf(underLedger.getLedger_form_parameter_id()));
                        body.put("slug", underLedger.getLedger_form_parameter_slug());
                        body.put("ledger_name", tfLedgerCreateLedgerName.getText());
                        body.put("tax_type", taxtype);
                        body.put("is_private", "false");

                        Map<String, String> headers = new HashMap<>();
//                        headers.put("branch", "gvhm001");
                        headers.put("branch", Globals.headerBranch);
//                Map<String, File> fileMap = null;
                        String response = APIClient.postMultipartRequest(body, null, EndPoints.editLedgerMaster, headers);
                        //? HIGHLIGHT
                        LedgerListController.editedLedgerId = ledgerEditId; //? Set the ID for editing
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

                        loggerLedgerEdit.error("Exception in LedgerCreateTaxSubmit(): -> :" + Globals.getExceptionString(e));
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

    public void LedgerEditBankAccSubmit(List<PaymentMode> paymentModeList, Boolean isDefaultBank, Boolean taxable, String GstIn, String BankName, String AccNo, String IFSCCode, String BankBranch) {
        JSONArray paymentJsonArray = new JSONArray();
        for (PaymentMode v : paymentModeList) {
            JSONObject obj = new JSONObject();
            obj.put("id", v.getId());
            obj.put("label", v.getPaymentMode());
            obj.put("value", v.getIsSelected());
            obj.put("detailsId", v.getDetailsId());
            paymentJsonArray.put(obj);
        }
//        System.out.println("paymentJsonArray => " + paymentJsonArray);
        if (cmbLedgerType.getSelectionModel().getSelectedItem() != null && !tfLedgerCreateLedgerName.getText().isEmpty()) {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnUpdate + tfLedgerCreateLedgerName.getText(), input -> {
                if (input == 1) {
                    try {
                        UnderLedger underLedger = cmbLedgerType.getSelectionModel().getSelectedItem();
                        BalanceType balanceType = cmbLedgerCreateOpeningBalanceType.getSelectionModel().getSelectedItem();
                        Map<String, String> body = new HashMap<>();
                        body.put("id", ledgerEditId.toString());
                        body.put("under_prefix", underLedger.getUnder_prefix());
                        body.put("associates_id", String.valueOf(underLedger.getAssociates_id()));
                        body.put("principle_group_id", String.valueOf(underLedger.getSub_principle_id()));
                        body.put("principle_id", String.valueOf(underLedger.getPrinciple_id()));
                        body.put("opening_bal_type", balanceType != null ? String.valueOf(balanceType.getId()) : "1");
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
//                        headers.put("branch", "gvhm001");
                        headers.put("branch", Globals.headerBranch);

//                System.out.println("BankAccEdit body ->" + body);
                        String response = APIClient.postMultipartRequest(body, null, EndPoints.editLedgerMaster, headers);
                        //? HIGHLIGHT
                        LedgerListController.editedLedgerId = ledgerEditId; //? Set the ID for editing
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

                        loggerLedgerEdit.error("Exception on LedgerEditBankAccSubmit():" + Globals.getExceptionString(e));

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

    public void LedgerEditSCSubmit(String mailingName, String address, String state, String pinCode, String mobileNo, String whatsappNo, String Email, String licenseNo, String regDate, String businessType, String businessTrade, Boolean isCredit, String creditDays, String applicableFrom, String creditNumBills, String creditBillValue, String panNo, Boolean isGst, List<GstList> lstGstDetails, Boolean isLicense, List<LicenseInfo> lstLicenseInfo, Boolean isBank, List<BankDetailList> lstBankDetails, Boolean isShipping, List<ShippingInfo> lstShippingInfo, Boolean isDepartment, List<DeptInfo> lstDeptInfo, List<Integer> lstRemoveGst, List<Integer> lstRemoveLicense, List<Integer> lstRemoveDept, List<Integer> lstRemoveBank, List<Integer> lstRemoveShipping) {
//        System.out.println("paymet" + paymentModeList);
//        System.out.println("isDefaulBank" + isDefaultBank);
//        System.out.println("taxable" + taxable);
//        System.out.println("GstIn" + GstIn);
//        System.out.println("Bankname" + BankName);
//        System.out.println("AccNo" + AccNo);
//        System.out.println("IFSCCode" + IFSCCode);
//        System.out.println("Bankbranch" + BankBranch);
        if (cmbLedgerType.getSelectionModel().getSelectedItem() != null && !tfLedgerCreateLedgerName.getText().isEmpty()) {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnUpdate + tfLedgerCreateLedgerName.getText(), input -> {
                if (input == 1) {
                    try {
                        UnderLedger underLedger = cmbLedgerType.getSelectionModel().getSelectedItem();
                        BalanceType balanceType = cmbLedgerCreateOpeningBalanceType.getSelectionModel().getSelectedItem();
                        BalancingMethods balancingMethods = cmbLedgerCreateBalancingMethods.getSelectionModel().getSelectedItem();
                        String supplierCode = tfLedgerCreateLedgerCode.getText();
                        Map<String, String> body = new HashMap<>();
                        body.put("id", ledgerEditId.toString());
                        body.put("supplier_code", supplierCode);
                        body.put("under_prefix", underLedger.getUnder_prefix());
                        body.put("associates_id", String.valueOf(underLedger.getAssociates_id()));
                        body.put("principle_group_id", String.valueOf(underLedger.getSub_principle_id()));
                        body.put("principle_id", String.valueOf(underLedger.getPrinciple_id()));
                        body.put("balancing_method", balancingMethods != null ? String.valueOf(balancingMethods.getBalancing_id()) : "1");
                        body.put("opening_bal_type", balanceType != null ? String.valueOf(balanceType.getId()) : "1");
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
                        body.put("businessType", businessType != null ? businessType : "");
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
                                    gstObj.put("bid", gstDetail.getId());
                                    gstObj.put("gstin", gstDetail.getGstNo());
                                    gstObj.put("registration_type", gstDetail.getGstTypeid());
                                    gstObj.put("dateofregistartion", DateConvertUtil.convertStringDatetoAPIDateString(gstDetail.getGstRegDate()));
                                    gstObj.put("pancard", gstDetail.getPanNo());
                                    gstJsonArr.put(gstObj);
                                }
                                body.put("gstdetails", gstJsonArr.toString());
                                body.put("pan_no", "");
                                body.put("removeGstList", lstRemoveGst.toString());
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
                                    licenseObj.put("lid", licenseInfo.getId());
                                    licenseObj.put("licences_type", licenseType);
                                    licenseObj.put("licenses_num", licenseInfo.getLicenseNo());
                                    licenseObj.put("licenses_exp", licenseInfo.getLicenseExp() != null ? DateConvertUtil.convertStringDatetoAPIDateString(licenseInfo.getLicenseExp()) : "");
                                    licenseObj.put("license_doc_upload", "");
                                    licenceJsonArr.put(licenseObj);
                                }
                                body.put("licensesDetails", licenceJsonArr.toString());
                                body.put("removelicensesList", lstRemoveLicense.toString());

                            }
                        }

                        //? Shipping Details list Mapping
                        body.put("isShippingDetails", isShipping.toString());
                        if (isShipping) {
                            if (lstShippingInfo.size() > 0) {
                                JSONArray shippingJsonArr = new JSONArray();
                                for (ShippingInfo shippingInfo : lstShippingInfo) {
                                    JSONObject shippingObj = new JSONObject();
                                    shippingObj.put("sid", shippingInfo.getId());
                                    shippingObj.put("shipping_address", shippingInfo.getShippingName());
                                    shippingObj.put("district", shippingInfo.getShippingStateId());
                                    shippingJsonArr.put(shippingObj);
                                }
                                body.put("shippingDetails", shippingJsonArr.toString());
                                body.put("removeShippingList", lstRemoveShipping.toString());
                            }
                        }

                        //? Department list Mapping
                        body.put("isDepartment", isDepartment.toString());
                        if (isDepartment == true) {
                            if (lstDeptInfo.size() > 0) {
                                JSONArray deptJsonArr = new JSONArray();
                                for (DeptInfo deptInfo : lstDeptInfo) {
                                    JSONObject deptObj = new JSONObject();
                                    deptObj.put("did", deptInfo.getId());
                                    deptObj.put("dept", deptInfo.getPersonDeptName());
                                    deptObj.put("contact_person", deptInfo.getPersonName());
                                    deptObj.put("contact_no", deptInfo.getPersonPhone());
                                    deptObj.put("email", deptInfo.getPersonEmail().trim().isEmpty() ? "" : deptInfo.getPersonEmail());
                                    deptJsonArr.put(deptObj);
                                }
                                body.put("deptDetails", deptJsonArr.toString());
                                body.put("removeDeptList", lstRemoveDept.toString());
                            }
                        }

                        //? Bank list mapping
                        body.put("isBankDetails", isBank.toString());
                        if (isBank == true) {
                            if (lstBankDetails.size() > 0) {
                                JSONArray bankJsonArr = new JSONArray();
                                for (BankDetailList bankDetails : lstBankDetails) {
                                    JSONObject bankObj = new JSONObject();
                                    bankObj.put("bid", bankDetails.getId());
                                    bankObj.put("bank_name", bankDetails.getBankName());
                                    bankObj.put("bank_account_no", bankDetails.getBankAccNo());
                                    bankObj.put("bank_branch", bankDetails.getBankBranch());
                                    bankObj.put("bank_ifsc_code", bankDetails.getBankIFSCCode());
                                    bankJsonArr.put(bankObj);
                                }
                                body.put("bankDetails", bankJsonArr.toString());
                                body.put("removeBankList", lstRemoveBank.toString());
                            }
                        }

                        Map<String, String> headers = new HashMap<>();
//                        headers.put("branch", "gvhm001");
                        headers.put("branch", Globals.headerBranch);
                        System.out.println("body => " + body);
                        String response = APIClient.postMultipartRequest(body, null, EndPoints.editLedgerMaster, headers);
                        //? HIGHLIGHT
                        LedgerListController.editedLedgerId = ledgerEditId; //? Set the ID for editing
                        JSONObject resObj = new JSONObject(response);
                        if (resObj.getInt("responseStatus") == 200) {
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

                        loggerLedgerEdit.error("Exception in LedgerEditSCSubmit(): -> :" + Globals.getExceptionString(e));

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


    public void LedgerEditSDSubmit(String mailingName, String address, String state, String pinCode, String mobileNo, String whatsappNo, String Email, String licenseNo, String regDate, String businessType, String businessTrade, Boolean isCredit, String creditDays, String applicableFrom, String creditNumBills, String creditBillValue, String panNo, Boolean isGst, List<GstList> lstGstDetails, Boolean isLicense, List<LicenseInfo> lstLicenseInfo, Boolean isBank, List<BankDetailList> lstBankDetails, Boolean isShipping, List<ShippingInfo> lstShippingInfo, Boolean isDepartment, List<DeptInfo> lstDeptInfo, Boolean isSalesman, String salesManId, String areaId, String route, List<Integer> lstRemoveGst, List<Integer> lstRemoveLicense, List<Integer> lstRemoveDept, List<Integer> lstRemoveBank, List<Integer> lstRemoveShipping) {
        if (cmbLedgerType.getSelectionModel().getSelectedItem() != null && !tfLedgerCreateLedgerName.getText().isEmpty()) {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnUpdate + tfLedgerCreateLedgerName.getText(), input -> {
                if (input == 1) {
                    try {
                        UnderLedger underLedger = cmbLedgerType.getSelectionModel().getSelectedItem();
                        BalanceType balanceType = cmbLedgerCreateOpeningBalanceType.getSelectionModel().getSelectedItem();
                        BalancingMethods balancingMethods = cmbLedgerCreateBalancingMethods.getSelectionModel().getSelectedItem();
                        String supplierCode = tfLedgerCreateLedgerCode.getText();
                        Map<String, String> body = new HashMap<>();
                        body.put("id", ledgerEditId.toString());
                        body.put("supplier_code", supplierCode);
                        body.put("under_prefix", underLedger.getUnder_prefix());
                        body.put("associates_id", String.valueOf(underLedger.getAssociates_id()));
                        body.put("principle_group_id", String.valueOf(underLedger.getSub_principle_id()));
                        body.put("principle_id", String.valueOf(underLedger.getPrinciple_id()));
                        body.put("balancing_method", balancingMethods != null ? String.valueOf(balancingMethods.getBalancing_id()) : "1");
                        body.put("opening_bal_type", balanceType != null ? String.valueOf(balanceType.getId()) : "1");
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
                        body.put("businessType", businessType != null ? businessType : "");
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
                                    gstObj.put("bid", gstDetail.getId());
                                    gstObj.put("gstin", gstDetail.getGstNo());
                                    gstObj.put("registration_type", gstDetail.getGstTypeid());
                                    gstObj.put("dateofregistartion", DateConvertUtil.convertStringDatetoAPIDateString(gstDetail.getGstRegDate()));
                                    gstObj.put("pancard", gstDetail.getPanNo());
                                    gstJsonArr.put(gstObj);
                                }
                                body.put("gstdetails", gstJsonArr.toString());
                                body.put("pan_no", "");
                                body.put("removeGstList", lstRemoveGst.toString());
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
                                    licenseObj.put("lid", licenseInfo.getId());
                                    licenseObj.put("licences_type", licenseType);
                                    licenseObj.put("licenses_num", licenseInfo.getLicenseNo());
                                    licenseObj.put("licenses_exp", licenseInfo.getLicenseExp() != null ? DateConvertUtil.convertStringDatetoAPIDateString(licenseInfo.getLicenseExp()) : "");
                                    licenseObj.put("license_doc_upload", "");
                                    licenceJsonArr.put(licenseObj);
                                }
                                body.put("licensesDetails", licenceJsonArr.toString());
                                body.put("removelicensesList", lstRemoveLicense.toString());
                            }
                        }
                        //? Shipping Details list Mapping
                        body.put("isShippingDetails", isShipping.toString());
                        if (isShipping) {
                            if (lstShippingInfo.size() > 0) {
                                JSONArray shippingJsonArr = new JSONArray();
                                for (ShippingInfo shippingInfo : lstShippingInfo) {
                                    JSONObject shippingObj = new JSONObject();
                                    shippingObj.put("sid", shippingInfo.getId());
                                    shippingObj.put("shipping_address", shippingInfo.getShippingName());
                                    shippingObj.put("district", shippingInfo.getShippingStateId());
                                    shippingJsonArr.put(shippingObj);
                                }
                                body.put("shippingDetails", shippingJsonArr.toString());
                                body.put("removeShippingList", lstRemoveShipping.toString());
                            }
                        }
                        //? Department list Mapping
                        body.put("isDepartment", isDepartment.toString());
                        if (isDepartment == true) {
                            if (lstDeptInfo.size() > 0) {
                                JSONArray deptJsonArr = new JSONArray();
                                for (DeptInfo deptInfo : lstDeptInfo) {
                                    JSONObject deptObj = new JSONObject();
                                    deptObj.put("did", deptInfo.getId());
                                    deptObj.put("dept", deptInfo.getPersonDeptName());
                                    deptObj.put("contact_person", deptInfo.getPersonName());
                                    deptObj.put("contact_no", deptInfo.getPersonPhone());
                                    deptObj.put("email", deptInfo.getPersonEmail().trim().isEmpty() ? "" : deptInfo.getPersonEmail());
                                    deptJsonArr.put(deptObj);
                                }
                                body.put("deptDetails", deptJsonArr.toString());
                                body.put("removeDeptList", lstRemoveDept.toString());
                            }
                        }
                        //? Bank list mapping
                        body.put("isBankDetails", isBank.toString());
                        if (isBank == true) {
                            if (lstBankDetails.size() > 0) {
                                JSONArray bankJsonArr = new JSONArray();
                                for (BankDetailList bankDetails : lstBankDetails) {
                                    JSONObject bankObj = new JSONObject();
                                    bankObj.put("bid", bankDetails.getId());
                                    bankObj.put("bank_name", bankDetails.getBankName());
                                    bankObj.put("bank_account_no", bankDetails.getBankAccNo());
                                    bankObj.put("bank_branch", bankDetails.getBankBranch());
                                    bankObj.put("bank_ifsc_code", bankDetails.getBankIFSCCode());
                                    bankJsonArr.put(bankObj);
                                }
                                body.put("bankDetails", bankJsonArr.toString());
                                body.put("removeBankList", lstRemoveBank.toString());
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
//                        headers.put("branch", "gvhm001");
                        headers.put("branch", Globals.headerBranch);
                        System.out.println("body => " + body);
                        String response = APIClient.postMultipartRequest(body, null, EndPoints.editLedgerMaster, headers);
                        //? HIGHLIGHT
                        LedgerListController.editedLedgerId = ledgerEditId; //? Set the ID for editing
                        JSONObject resObj = new JSONObject(response);
                        if (resObj.getInt("responseStatus") == 200) {
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
                        loggerLedgerEdit.error("Exception in LedgerEditSDSubmit(): " + Globals.getExceptionString(e));
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

    public void setEditId(Integer InId) {
        ledgerEditId = InId;
        if (InId > -1) {
            setEditData();
        }
    }

    private void setEditData() {
        fnAPICallLedgerDetailsById(ledgerEditId);
    }

    private void fnAPICallLedgerDetailsById(Integer ledgerEditId) {
        Map<String, String> body = new HashMap<>();
        body.put("id", ledgerEditId.toString());
        String formData = Globals.mapToStringforFormData(body);
        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.getLedgersById);
        JSONObject responseBody = new JSONObject(response.body());
        if (responseBody.getInt("responseStatus") == 200) {
            resObj = responseBody.getJSONObject("response");
            setFormData(resObj);
        }
    }

    private void setFormData(JSONObject resObj) {
        System.out.println("resObj ->" + resObj);
        String edtunderPrefix = resObj.getString("under_prefix");
        if (!edtunderPrefix.isEmpty()) {
            UnderLedger underLedger = cmbUnderList.stream().filter((s -> s.under_prefix.equalsIgnoreCase(edtunderPrefix))).findAny().orElse(null);
            if (underLedger != null) {
                cmbLedgerType.setValue(underLedger);
//                cmbLedgerType.setDisable(true);
            }
        }
        if (resObj.has("balancing_method")) {
            int edtBalancingMethod = resObj.getInt("balancing_method");
            if (edtBalancingMethod > 0) {
                lstBalancingMethods.stream().filter(s -> s.getBalancing_id() == edtBalancingMethod).findAny().ifPresent(balancingMethods -> cmbLedgerCreateBalancingMethods.setValue(balancingMethods));
            }
        }
        if (resObj.has("opening_bal_type")) {
            String edtBalanceType = resObj.getString("opening_bal_type");
            if (edtBalanceType != null) {
                lstBalanceType.stream().filter(s -> s.getType().equalsIgnoreCase(edtBalanceType)).findAny().ifPresent(balanceType -> cmbLedgerCreateOpeningBalanceType.setValue(balanceType));
            }
        }
        tfLedgerCreateLedgerName.setText(resObj.getString("ledger_name"));
        tfLedgerCreateLedgerCode.setText(resObj.getString("supplier_code"));
        if (resObj.has("opening_bal")) tfLedgerCreateOpeningBal.setText(resObj.getInt("opening_bal") + "");

        if (resObj.has("opening_bal_invoice_list")) {
            JSONArray openArr = resObj.getJSONArray("opening_bal_invoice_list");
            if (openArr.length() > 0) {
                tbllstOB.clear();
                for (Object object : openArr) {
                    JSONObject obj = new JSONObject(object.toString());
                    tbllstOB.add(new OBListDTO(0, obj.getString("invoice_no"), DateConvertUtil.convertDispDateFormat(obj.getString("invoice_date")), DateConvertUtil.convertDispDateFormat(obj.getString("invoice_date")), obj.getInt("due_days"), obj.getDouble("bill_amt"), obj.getDouble("invoice_paid_amt"), obj.getDouble("invoice_bal_amt"), obj.getJSONObject("type").getString("label")));
                }
            }
        }
        GlobalTranx.requestFocusOrDieTrying(tfLedgerCreateLedgerName, 3);
    }
}
