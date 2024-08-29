package com.opethic.genivis.controller.master.ledger.create;

import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.models.master.ledger.TaxType;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.CommonTraversa;
import com.opethic.genivis.utils.GlobalLedgerCreateController;
import com.opethic.genivis.utils.Globals;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LedgerCreateTaxController implements Initializable {
    @FXML
    private Button btnLedgerCreateTaxCancel;

    @FXML
    private Button btnLedgerCreateTaxSubmit;

    @FXML
    private ComboBox<TaxType> cmbLedgerCreateTax;

    @FXML
    private BorderPane stpaneother;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        getTaxTypeList();
        initBtn();

        // open Filter dropdown on Space
        cmbLedgerCreateTax.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cmbLedgerCreateTax.isShowing()) {
                cmbLedgerCreateTax.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });

        cmbLedgerCreateTax.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Node nextNode = CommonTraversa.getNextFocusableNode(cmbLedgerCreateTax.getScene());
                if (nextNode != null) {
                    nextNode.requestFocus();
                }
            }
        });

        btnLedgerCreateTaxSubmit.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                btnLedgerCreateTaxCancel.requestFocus();
            }
        });
    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            stpaneother.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            stpaneother.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            stpaneother.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            stpaneother.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            stpaneother.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            stpaneother.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            stpaneother.getStylesheets().add(GenivisApplication.class.getResource("ui/css/ledger_css_style/ledger_css_7.css").toExternalForm());
        }
    }

    private void initBtn() {
        GlobalLedgerCreateController globalLedgerCreateController = GlobalLedgerCreateController.getInstance();

        btnLedgerCreateTaxSubmit.setOnAction(actionEvent -> {
            if (cmbLedgerCreateTax.getSelectionModel().getSelectedItem() != null) {
                globalLedgerCreateController.fcCallLedgerCreateTaxSubmit(cmbLedgerCreateTax.getSelectionModel().getSelectedItem().getTypeId());
            } else {
                String msg = "Please select Tax Type";
                AlertUtility.CustomCallback callback = number -> {
                };
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            }
        });
        btnLedgerCreateTaxCancel.setOnAction(actionEvent -> {
//            System.out.println("create Ledger Other Cancel");
            globalLedgerCreateController.fcCallLedgerCreateOtherCancel();
        });

    }

    private void getTaxTypeList() {
        List<TaxType> lst = Globals.getAllTaxTypes();
        ObservableList<TaxType> lstTaxType = FXCollections.observableArrayList(lst);

        cmbLedgerCreateTax.getItems().addAll(lstTaxType);
        cmbLedgerCreateTax.setConverter(new StringConverter<TaxType>() {
            @Override
            public String toString(TaxType taxType) {
                return taxType != null ? taxType.getTypeName() : "";
            }

            @Override
            public TaxType fromString(String string) {
                // You can implement this method if needed
                return null;
            }
        });
    }
}
