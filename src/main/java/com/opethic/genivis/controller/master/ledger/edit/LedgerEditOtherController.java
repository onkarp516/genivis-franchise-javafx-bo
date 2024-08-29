package com.opethic.genivis.controller.master.ledger.edit;

import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.GlobalLedgerEditController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;

import java.net.URL;
import java.util.ResourceBundle;

public class LedgerEditOtherController implements Initializable {

    @FXML
    private Button btnLedgerEditOtherSubmit;
    @FXML
    private BorderPane stpaneother;

    @FXML
    private Button btnLedgerEditOtherCancel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        GlobalLedgerEditController globalLedgerEditController = GlobalLedgerEditController.getInstance();

        btnLedgerEditOtherSubmit.setOnAction(actionEvent -> {

            globalLedgerEditController.fcCallLedgerEditOtherSubmit();

        });
        btnLedgerEditOtherCancel.setOnAction(actionEvent -> {
            globalLedgerEditController.fcCallLedgerEditOtherCancel();
        });

        btnLedgerEditOtherSubmit.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                btnLedgerEditOtherCancel.requestFocus();
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


}
