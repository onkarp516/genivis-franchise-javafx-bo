package com.opethic.genivis.controller.master.ledger.create;


import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.master.ledger.LedgerListController;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.models.master.ledger.PaymentMode;
import com.opethic.genivis.models.master.ledger.YesNoOption;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.CommonTraversa;
import com.opethic.genivis.utils.GlobalLedgerCreateController;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.http.HttpResponse;
import java.security.Key;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;


public class LedgerCreateBankAccController implements Initializable {

    @FXML
    private ComboBox<YesNoOption> cmbLedgerCreateIsTaxable;

    @FXML
    private BorderPane stpanebankacc;

    @FXML
    private TextField tfbankaccLedgerCreateBankName, tfsbankacccLedgerCreateBankAccNo, tfbankaccLedgerCreateBankIFSC, tfbankaccLedgerCreateBankBranch;


    @FXML
    private Label lblLedgerCreateGstIn;

    @FXML
    private TextField tfbankaccLedgerCreateGstIn;

    @FXML
    private HBox hbbankaccLedgerCreatePaymentModes;

    @FXML
    private Button btnbankaccLedgerCreateCancel, btnbankaccLedgerCreateSubmit;

    @FXML
    private CheckBox chkIsDefaultBank;

    Set<PaymentMode> selectedPaymentModes = new HashSet<>();
    ObservableList<PaymentMode> lstPaymentMode = FXCollections.observableArrayList();

    private static final Logger loggerLedgerCreateBankAccController = LogManager.getLogger(LedgerCreateBankAccController.class);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        initLoadPaymentModes();
        initTaxableOptions();
        initSubmitAndCancelBtn();

        // open Filter dropdown on Space
        cmbLedgerCreateIsTaxable.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cmbLedgerCreateIsTaxable.isShowing()) {
                cmbLedgerCreateIsTaxable.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });

        // Setting the handler for each node
        setKeyPressedHandler(cmbLedgerCreateIsTaxable);
        setKeyPressedHandler(tfbankaccLedgerCreateGstIn);
        setKeyPressedHandler(tfbankaccLedgerCreateBankName);
        setKeyPressedHandler(tfsbankacccLedgerCreateBankAccNo);
        setKeyPressedHandler(tfbankaccLedgerCreateBankIFSC);
        setKeyPressedHandler(tfbankaccLedgerCreateBankBranch);
        setKeyPressedHandler(chkIsDefaultBank);

        btnbankaccLedgerCreateSubmit.setOnKeyPressed(event->{
            if(event.getCode() == KeyCode.RIGHT){
                btnbankaccLedgerCreateCancel.requestFocus();
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
        try {
            HttpResponse<String> response = APIClient.getRequest(EndPoints.getPaymentMode);
            JSONObject resObj = new JSONObject(response.body());
            if (resObj.getInt("responseStatus") == 200) {
                JSONArray respArr = resObj.getJSONArray("responseObject");
                if (respArr.length() > 0) {
                    for (Object object : respArr) {
                        JSONObject obj = new JSONObject(object.toString());
                        lstPaymentMode.add(new PaymentMode(obj.getInt("id"), obj.getString("payment_mode"), 0, 0));
//                        System.out.println("lstPaymentMode------> " + lstPaymentMode.stream());
                    }
                }
                if (lstPaymentMode.size() > 0) {
                    for (PaymentMode paymentMode : lstPaymentMode) {
                        CheckBox chkPaymentMode = new CheckBox(paymentMode.getPaymentMode());
//                    chkPaymentMode.setAlignment(Pos.CENTER);
                        chkPaymentMode.setPadding(new Insets(5));
                        chkPaymentMode.setPrefHeight(32.0);

                        hbbankaccLedgerCreatePaymentModes.getChildren().add(chkPaymentMode);
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
        } catch (Exception e) {
            loggerLedgerCreateBankAccController.error("Exception initLoadPaymentModes() : " + Globals.getExceptionString(e));
        }

    }

    private void initTaxableOptions() {
        List<YesNoOption> lst = Globals.getYesNoOptions();
        ObservableList<YesNoOption> cmbUnderList = FXCollections.observableArrayList(lst);
        cmbLedgerCreateIsTaxable.getItems().addAll(cmbUnderList);
        cmbLedgerCreateIsTaxable.setConverter(new StringConverter<YesNoOption>() {
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

        cmbLedgerCreateIsTaxable.setValue(lst.get(1));
        tfbankaccLedgerCreateGstIn.setVisible(false);
        lblLedgerCreateGstIn.setVisible(false);

        cmbLedgerCreateIsTaxable.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null && newValue.getValue() == true) {
                tfbankaccLedgerCreateGstIn.setVisible(true);
                lblLedgerCreateGstIn.setVisible(true);
            } else {
                tfbankaccLedgerCreateGstIn.setVisible(false);
                lblLedgerCreateGstIn.setVisible(false);
            }
        });
    }

    private void initSubmitAndCancelBtn() {
        GlobalLedgerCreateController globalLedgerCreateController = GlobalLedgerCreateController.getInstance();

        btnbankaccLedgerCreateSubmit.setOnAction(actionEvent -> {
            YesNoOption taxable = cmbLedgerCreateIsTaxable.getSelectionModel().getSelectedItem();
            List<PaymentMode> filterLst = lstPaymentMode.stream().map(paymentMode -> {
                if (selectedPaymentModes.contains(paymentMode)) {
                    paymentMode.setIsSelected(1);
                } else {
                    paymentMode.setIsSelected(0);
                }
                return paymentMode;

            }).toList();
            if (ValidateBankAccForm()) {
                globalLedgerCreateController.fnCallLedgerCreateBankAccSubmit(filterLst, chkIsDefaultBank.selectedProperty().getValue(), taxable.getValue(), taxable.getValue() == true ? tfbankaccLedgerCreateGstIn.getText() : "", tfbankaccLedgerCreateBankName.getText(), tfsbankacccLedgerCreateBankAccNo.getText(), tfbankaccLedgerCreateBankIFSC.getText(), tfbankaccLedgerCreateBankBranch.getText());
            }
//            System.out.println("create Ledger Other Clicked");
//            globalLedgerCreateController.fcCallLedgerCreateOtherSubmit();
        });
        btnbankaccLedgerCreateCancel.setOnAction(actionEvent -> {
//            System.out.println("create Ledger Other Cancel");
            globalLedgerCreateController.fcCallLedgerCreateOtherCancel();
        });

    }

    private Boolean ValidateBankAccForm() {
       /* if (tfbankaccLedgerCreateBankName.getText().trim().isEmpty()) {
            String msg = "Please Enter Bank Name !";
            AlertUtility.CustomCallback callback = number -> {
                tfbankaccLedgerCreateBankName.requestFocus();
            };
            AlertUtility.AlertError(AlertUtility.alertTypeError, msg, callback);
            return false;
        }
        if (tfsbankacccLedgerCreateBankAccNo.getText().trim().isEmpty()) {
            String msg = "Please Enter Bank Acc No !";
            AlertUtility.CustomCallback callback = number -> {
                tfsbankacccLedgerCreateBankAccNo.requestFocus();
            };
            AlertUtility.AlertError(AlertUtility.alertTypeError, msg, callback);
            return false;
        }
        if (tfbankaccLedgerCreateBankIFSC.getText().trim().isEmpty()) {
            String msg = "Please Enter Bank IFSC Code !";
            AlertUtility.CustomCallback callback = number -> {
                tfbankaccLedgerCreateBankIFSC.requestFocus();
            };
            AlertUtility.AlertError(AlertUtility.alertTypeError, msg, callback);
            return false;
        }
        if (tfbankaccLedgerCreateBankBranch.getText().trim().isEmpty()) {
            String msg = "Please Enter Bank Branch !";
            AlertUtility.CustomCallback callback = number -> {
                tfbankaccLedgerCreateBankBranch.requestFocus();
            };
            AlertUtility.AlertError(AlertUtility.alertTypeError, msg, callback);
            return false;
        }*/

        return true;
    }
}
