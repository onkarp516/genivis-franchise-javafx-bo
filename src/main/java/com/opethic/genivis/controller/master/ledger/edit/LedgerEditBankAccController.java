package com.opethic.genivis.controller.master.ledger.edit;

import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.models.master.ledger.PaymentMode;
import com.opethic.genivis.models.master.ledger.YesNoOption;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.CommonTraversa;
import com.opethic.genivis.utils.GlobalLedgerEditController;
import com.opethic.genivis.utils.Globals;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.util.StringConverter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class LedgerEditBankAccController implements Initializable {

    @FXML
    private ComboBox<YesNoOption> cmbLedgerEditIsTaxable;

    @FXML
    private BorderPane stpanebankacc;

    @FXML
    private TextField tfbankaccLedgerEditBankName, tfsbankacccLedgerEditBankAccNo, tfbankaccLedgerEditBankIFSC, tfbankaccLedgerEditBankBranch;


    @FXML
    private Label lblLedgerEditGstIn;

    @FXML
    private TextField tfbankaccLedgerEditGstIn;

    @FXML
    private HBox hbbankaccLedgerEditPaymentModes;

    @FXML
    private Button btnbankaccLedgerEditCancel, btnbankaccLedgerEditSubmit;

    @FXML
    private CheckBox chkIsDefaultBank;

    Set<PaymentMode> selectedPaymentModes = new HashSet<>();
    ObservableList<PaymentMode> lstPaymentMode = FXCollections.observableArrayList();

    ObservableList<YesNoOption> lstYesNoOpt = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        initLoadPaymentModes();
        initTaxableOptions();
        initSubmitAndCancelBtn();

        // open Filter dropdown on Space
        cmbLedgerEditIsTaxable.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cmbLedgerEditIsTaxable.isShowing()) {
                cmbLedgerEditIsTaxable.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });

        // Setting the handler for each node
        setKeyPressedHandler(cmbLedgerEditIsTaxable);
        setKeyPressedHandler(tfbankaccLedgerEditGstIn);
        setKeyPressedHandler(tfbankaccLedgerEditBankName);
        setKeyPressedHandler(tfsbankacccLedgerEditBankAccNo);
        setKeyPressedHandler(tfbankaccLedgerEditBankIFSC);
        setKeyPressedHandler(tfbankaccLedgerEditBankBranch);
        setKeyPressedHandler(chkIsDefaultBank);

        btnbankaccLedgerEditSubmit.setOnKeyPressed(event->{
            if(event.getCode() == KeyCode.RIGHT){
                btnbankaccLedgerEditCancel.requestFocus();
            }
        });
    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            stpanebankacc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            stpanebankacc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            stpanebankacc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            stpanebankacc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            stpanebankacc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            stpanebankacc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            stpanebankacc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_7.css").toExternalForm());
        }
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


    private void initLoadPaymentModes() {
        HttpResponse<String> response = APIClient.getRequest(EndPoints.getPaymentMode);
        JSONObject resObj = new JSONObject(response.body());
        if (resObj.getInt("responseStatus") == 200) {
            JSONArray respArr = resObj.getJSONArray("responseObject");
            if (respArr.length() > 0) {
                for (Object object : respArr) {
                    JSONObject obj = new JSONObject(object.toString());
                    lstPaymentMode.add(new PaymentMode(obj.getInt("id"), obj.getString("payment_mode"), 0, 0));
                }
            }
            if (lstPaymentMode.size() > 0) {
                for (PaymentMode paymentMode : lstPaymentMode) {
                    CheckBox chkPaymentMode = new CheckBox(paymentMode.getPaymentMode());
//                    chkPaymentMode.setAlignment(Pos.CENTER);
                    chkPaymentMode.setPadding(new Insets(5));
                    chkPaymentMode.setPrefHeight(32.0);

                    hbbankaccLedgerEditPaymentModes.getChildren().add(chkPaymentMode);
                    chkPaymentMode.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                        if (isNowSelected) {
                            selectedPaymentModes.add(paymentMode);
                        } else {
                            selectedPaymentModes.remove(paymentMode);
                        }
                    });
                }
            }
        } else {
            String msg = resObj.getString("message");
            AlertUtility.CustomCallback callback = number -> {
            };
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
        }
    }

    private void initTaxableOptions() {
        List<YesNoOption> lstYesNo = Globals.getYesNoOptions();
        lstYesNoOpt = FXCollections.observableArrayList(lstYesNo);
        cmbLedgerEditIsTaxable.getItems().addAll(lstYesNoOpt);
        cmbLedgerEditIsTaxable.setConverter(new StringConverter<YesNoOption>() {
            @Override
            public String toString(YesNoOption yesNoOption) {
                return yesNoOption != null ? yesNoOption.getLabel() : "";
            }

            @Override
            public YesNoOption fromString(String string) {
                // You can implement this method if needed
                return null;
            }
        });

        cmbLedgerEditIsTaxable.setValue(lstYesNo.get(1));
        tfbankaccLedgerEditGstIn.setVisible(false);
        lblLedgerEditGstIn.setVisible(false);

        cmbLedgerEditIsTaxable.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null && newValue.getValue() == true) {
                tfbankaccLedgerEditGstIn.setVisible(true);
                lblLedgerEditGstIn.setVisible(true);
            } else {
                tfbankaccLedgerEditGstIn.setVisible(false);
                lblLedgerEditGstIn.setVisible(false);
            }
        });
    }

    private void initSubmitAndCancelBtn() {
        GlobalLedgerEditController globalLedgerEditController = GlobalLedgerEditController.getInstance();

        btnbankaccLedgerEditSubmit.setOnAction(actionEvent -> {
            YesNoOption taxable = cmbLedgerEditIsTaxable.getSelectionModel().getSelectedItem();
            List<PaymentMode> filterLst = lstPaymentMode.stream().map(paymentMode -> {
                if (selectedPaymentModes.contains(paymentMode)) {
                    paymentMode.setIsSelected(1);
                } else {
                    paymentMode.setIsSelected(0);
                }
                return paymentMode;
            }).toList();
            if (ValidateBankAccForm()) {

                globalLedgerEditController.fnCallLedgerEditBankAccSubmit(filterLst, chkIsDefaultBank.selectedProperty().getValue(), taxable.getValue(), taxable.getValue() ? tfbankaccLedgerEditGstIn.getText() : "", tfbankaccLedgerEditBankName.getText(), tfsbankacccLedgerEditBankAccNo.getText(), tfbankaccLedgerEditBankIFSC.getText(), tfbankaccLedgerEditBankBranch.getText());
            }
//            System.out.println("create Ledger Other Clicked");
//            globalLedgerEditController.fcCallLedgerEditOtherSubmit();
        });
        btnbankaccLedgerEditCancel.setOnAction(actionEvent -> {
//            System.out.println("create Ledger Other Cancel");
            globalLedgerEditController.fcCallLedgerEditOtherCancel();
        });

    }

    private Boolean ValidateBankAccForm() {

        return true;
    }

    public void setEditData(JSONObject object) {
        if (object.getBoolean("isGST")) {
            lstYesNoOpt.stream().filter(s -> s.getValue() == object.getBoolean("isGST")).findAny().ifPresent((opt -> cmbLedgerEditIsTaxable.setValue(opt)));
            tfbankaccLedgerEditGstIn.setText(object.getString("gstin"));
        }

        tfbankaccLedgerEditBankBranch.setText(object.getString("bank_branch"));
        tfbankaccLedgerEditBankIFSC.setText(object.getString("ifsc_code"));
        tfsbankacccLedgerEditBankAccNo.setText(object.getString("account_no"));
        tfbankaccLedgerEditBankName.setText(object.getString("bank_name"));

        JSONArray pyMdArr = object.getJSONArray("payment_modes");
        for (Object pyObj : pyMdArr) {
            JSONObject paymentMdObj = new JSONObject(pyObj.toString());
            lstPaymentMode.stream().filter(s -> s.getId() == paymentMdObj.getInt("id")).findAny().ifPresent(paymentMode -> {
                paymentMode.setDetailsId(paymentMdObj.getInt("detailsId"));
                paymentMode.setIsSelected(paymentMdObj.getInt("value"));
                selectedPaymentModes.add(paymentMode);
            });
        }
        ChkUpdateAfterEdit();
    }

    private void ChkUpdateAfterEdit() {
        ObservableList<Node> lstChks = hbbankaccLedgerEditPaymentModes.getChildren();
        for (Node chk : lstChks) {
            CheckBox innerChk = (CheckBox) chk;
            lstPaymentMode.stream().filter(slug -> slug.getPaymentMode().equalsIgnoreCase(innerChk.getText())).findAny().ifPresent(findObj -> {
                innerChk.setSelected(findObj.getIsSelected() == 1);
            });
        }
    }
}
