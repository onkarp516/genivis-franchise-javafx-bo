package com.opethic.genivis.controller.account_entry;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PaymentEditController extends Application {

    @FXML
    private StackPane stkpnPaymEditLedgerInfo;

    @FXML
    private AnchorPane AnchorPPaymentEditBankInfo;

    @FXML
    private TextField tfpaymentEditVoucherSrNo, tfpaymentEditVoucherNo, tfpaymentEditSupplierGstin;

    @FXML
    private TableView tblvPaymentEdit, tblvPaymentEditinvoiceData;

    @FXML
    private TableColumn tblcPaymentEditSource, tblcPaymenteditInvoiceNo, tblcPaymentEditInvoiceDate, tblcPaymentEditBillAmt,
            tblcPaymentEditPaidAmt, tblcPaymentEditRemainingAmt, tblcPaymentEditType, tblcPaymentEditParticulars, tblcPaymentEditDebit,
            tblcPaymentEditCredit;

    @FXML
    private Button btnPaymentEditCancle, btnPaymentEditSubmit;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

    }


}
