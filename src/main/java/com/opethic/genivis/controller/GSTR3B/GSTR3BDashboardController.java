package com.opethic.genivis.controller.GSTR3B;

import com.opethic.genivis.controller.Reports.AccountsLedgerReport2Controller;
import com.opethic.genivis.dto.GSTR3BDashboaedDTO;
import com.opethic.genivis.dto.LedgerReport1DTO;
import com.opethic.genivis.utils.GlobalController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.FxmFileConstants.*;

public class GSTR3BDashboardController implements Initializable {
    @FXML
    private TableView<GSTR3BDashboaedDTO> tblvGSTR3BDashboardList;
    @FXML
    private TableColumn<GSTR3BDashboaedDTO, String> tblGSTR3BDashboardTable_no, tblGSTR3BDashboardParticulars, tblGSTR3BDashboardTaxableAmt,
            tblGSTR3BDashboardIGSTAmt, tblGSTR3BDashboardCGSTAmt, tblGSTR3BDashboardSGSTAmt, tblGSTR3BDashboardCessAmt, tblGSTR3BDashboardTaxAmt;

    @FXML
    private BorderPane mainBorderpane;
    @FXML
    private TextField tfStartDt;

    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(() -> tfStartDt.requestFocus());

        mainBorderpane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
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
        });

//        tblvGSTR3BDashboardList.addEventFilter(KeyEvent.ANY, (KeyEvent event) -> {
//            if (event.getCode() == KeyCode.ENTER) {
//                GlobalController.getInstance().addTabStatic(GSTR3B_OUTWARD_TAXABLE_SUPPLIES_LIST_SLUG, false);
//            }
//        });

        tblvGSTR3BDashboardList.setRowFactory(tv -> {
            TableRow<GSTR3BDashboaedDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    GSTR3BDashboaedDTO clickedRow = row.getItem();
                    if (clickedRow.getParticulars().contains("Outward Taxable supplies (other than zero rated, nil rated and exempted)")) {
                        navigateToNextPage3_1A();
                    } else if (clickedRow.getParticulars().contains("Other Outward Taxable supplies(Nil rated, exempted)")) {
                        navigateToNextPage3_1C();
                    } else if (clickedRow.getParticulars().contains("All Other ITC")) {
                        navigateToNextPage4_A_5();
                    } else if (clickedRow.getParticulars().contains("From a supplier under composition scheme, Exempt and Nil rated supply")) {
                        navigateToNextPage5();
                    }
                }
            });
            return row;
        });

        tblGSTR3BDashboardParticulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
        // Custom cell factory for particulars column to apply styles
        tblGSTR3BDashboardParticulars.setCellFactory(column -> new TableCell<GSTR3BDashboaedDTO, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.contains("Outward Taxable supplies (other than zero rated, nil rated and exempted)")) {
                        setStyle("-fx-font-weight: normal; -fx-text-fill: #257dff; -fx-underline: true;");
                    } else if (item.contains("Other Outward Taxable supplies(Nil rated, exempted)")){
                        setStyle("-fx-font-weight: normal; -fx-text-fill: #257dff; -fx-underline: true;");
                    } else if (item.contains("(5)All Other ITC")){
                        setStyle("-fx-font-weight: normal; -fx-text-fill: #257dff; -fx-underline: true;");
                    } else if (item.contains("From a supplier under composition scheme, Exempt and Nil rated supply")){
                        setStyle("-fx-font-weight: normal; -fx-text-fill: #257dff; -fx-underline: true;");
                    } else if (item.contains("Outward Supplies and inward supplies liable to reverse charge")){
                        setStyle("-fx-font-weight: bold; -fx-text-fill: black;");
                    } else if (item.contains("Of the supplies shown in 3.1 (a), details of inter-state supplies made to unregistered persons, \ncomposition taxable person and UIN holders")){
                        setStyle("-fx-font-weight: bold; -fx-text-fill: black;");
                    } else if (item.contains("Eligible ITC")){
                        setStyle("-fx-font-weight: bold; -fx-text-fill: black;");
                    } else if (item.contains("Values of exempt, Nil-rated and non-GST inward supplies")){
                        setStyle("-fx-font-weight: bold; -fx-text-fill: black;");
                    } else if (item.contains("5.1 Interest & late fee payable")){
                        setStyle("-fx-font-weight: bold; -fx-text-fill: black;");
                    } else if (item.contains("Reverse Charge Liability and inpute credit to be booked")){
                        setStyle("-fx-font-weight: bold; -fx-text-fill: black;");
                    } else if (item.contains("Advance Receipts")){
                        setStyle("-fx-font-weight: bold; -fx-text-fill: black;");
                    } else if (item.contains("Advance Payment")){
                        setStyle("-fx-font-weight: bold; -fx-text-fill: black;");
                    } else {
                        setStyle("-fx-font-weight: normal; -fx-text-fill: black;");
                    }
                }
            }
        });

        fetchGSTR3BDashboardList("");

        responsiveTableDesign();

        //TODO: resizing the table columns as per the resolution.
        tblvGSTR3BDashboardList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void responsiveTableDesign() {
        tblGSTR3BDashboardTable_no.prefWidthProperty().bind(tblvGSTR3BDashboardList.widthProperty().multiply(0.05));
        tblGSTR3BDashboardParticulars.prefWidthProperty().bind(tblvGSTR3BDashboardList.widthProperty().multiply(0.35));
        tblGSTR3BDashboardTaxableAmt.prefWidthProperty().bind(tblvGSTR3BDashboardList.widthProperty().multiply(0.10));
        tblGSTR3BDashboardIGSTAmt.prefWidthProperty().bind(tblvGSTR3BDashboardList.widthProperty().multiply(0.10));
        tblGSTR3BDashboardCGSTAmt.prefWidthProperty().bind(tblvGSTR3BDashboardList.widthProperty().multiply(0.10));
        tblGSTR3BDashboardSGSTAmt.prefWidthProperty().bind(tblvGSTR3BDashboardList.widthProperty().multiply(0.10));
        tblGSTR3BDashboardCessAmt.prefWidthProperty().bind(tblvGSTR3BDashboardList.widthProperty().multiply(0.10));
        tblGSTR3BDashboardTaxAmt.prefWidthProperty().bind(tblvGSTR3BDashboardList.widthProperty().multiply(0.10));
    }

    private void navigateToNextPage3_1A() {
        System.out.println("Next Page Calling 3.1A ---> ");
            GlobalController.getInstance().addTabStatic(GSTR3B_OUTWARD_TAXABLE_SUPPLIES_LIST_SLUG, false);
    }
    private void navigateToNextPage3_1C() {
        System.out.println("Next Page Calling 3.1C ---> ");
    }
    private void navigateToNextPage4_A_5() {
        System.out.println("Next Page Calling 4_A_5 ---> ");
        GlobalController.getInstance().addTabStatic(GSTR3B_ALL_OTHER_ITC_LIST_SLUG, false);
    }
    private void navigateToNextPage5() {
        System.out.println("Next Page Calling 5---> ");
    }

    private void fetchGSTR3BDashboardList(String s) {
        tblGSTR3BDashboardTable_no.setCellValueFactory(new PropertyValueFactory<>("table_no"));
        tblGSTR3BDashboardParticulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
        tblGSTR3BDashboardTaxableAmt.setCellValueFactory(new PropertyValueFactory<>("taxable_amt"));
        tblGSTR3BDashboardIGSTAmt.setCellValueFactory(new PropertyValueFactory<>("igst_amt"));
        tblGSTR3BDashboardCGSTAmt.setCellValueFactory(new PropertyValueFactory<>("cgst_amt"));
        tblGSTR3BDashboardSGSTAmt.setCellValueFactory(new PropertyValueFactory<>("sgst_amt"));
        tblGSTR3BDashboardCessAmt.setCellValueFactory(new PropertyValueFactory<>("cess_amt"));
        tblGSTR3BDashboardTaxAmt.setCellValueFactory(new PropertyValueFactory<>("tax_amt"));

//        tblGSTR3BDashboardTable_no.setStyle("-fx-alignment: CENTER;");
//        tblGSTR3BDashboardTaxableAmt.setStyle("-fx-alignment: CENTER;");
//        tblGSTR3BDashboardIGSTAmt.setStyle("-fx-alignment: CENTER;");
//        tblGSTR3BDashboardCGSTAmt.setStyle("-fx-alignment: CENTER;");
//        tblGSTR3BDashboardSGSTAmt.setStyle("-fx-alignment: CENTER;");
//        tblGSTR3BDashboardCessAmt.setStyle("-fx-alignment: CENTER;");
//        tblGSTR3BDashboardTaxAmt.setStyle("-fx-alignment: CENTER;");

        // Add data to the table
        tblvGSTR3BDashboardList.setItems(getGSTR3BDashboardData());
    }

    private ObservableList<GSTR3BDashboaedDTO> getGSTR3BDashboardData() {
        ObservableList<GSTR3BDashboaedDTO> data = FXCollections.observableArrayList();
        data.add(new GSTR3BDashboaedDTO("3.1", "Outward Supplies and inward supplies liable to reverse charge", "00.00", "", "00.00", "00.00", "", "00.00"));
        data.add(new GSTR3BDashboaedDTO("a", "Outward Taxable supplies (other than zero rated, nil rated and exempted)", "00.00", "", "00.00", "00.00", "", "00.00"));
        data.add(new GSTR3BDashboaedDTO("b", "Outward Taxable supplies(zero rated )", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("c", "Other Outward Taxable supplies(Nil rated, exempted)", "00.00", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("d", "Inward supplies(liable to reverse charge)", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("e", "Non-GST Outward supplies", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("3.2", "Of the supplies shown in 3.1 (a), details of inter-state supplies made to unregistered persons, \ncomposition taxable person and UIN holders", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "Supplies made to Unregistered Persons", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "Supplies made to Composition Taxable Persons", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "Supplies made to UIN holders", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("4", "Eligible ITC", "", "", "00.00", "00.00", "", "00.00"));
        data.add(new GSTR3BDashboaedDTO("a", "ITC Available(Whether in full or part)", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "(1)Import of goods", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "(2)Import of services", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "(3)Inward supplies liable to reverse charge (other than 1 &2 above)", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "(4)Inward supplies from ISD", "", "", "00.00", "00.00", "", "00.00"));
        data.add(new GSTR3BDashboaedDTO("", "(5)All Other ITC", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("b", "ITC Reversed", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "(1) As per Rule 42 & 43 of SGST/CGST rules", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "(2) Others", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("c", "Net ITC Available (A)-(B)", "", "", "00.00", "00.00", "", "00.00"));
        data.add(new GSTR3BDashboaedDTO("", "", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("d", "Ineligible ITC", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "(1) As per section 17(5) of CGST//SGST Act", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "(2) Others", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("5", "Values of exempt, Nil-rated and non-GST inward supplies", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "From a supplier under composition scheme, Exempt and Nil rated supply", "00.00", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "Non GST Supply", "00.00", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("5.1", "5.1 Interest & late fee payable", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "Interest", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "Late Fees", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "Reverse Charge Liability and inpute credit to be booked", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "URD Purchase", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "Reverse Charge Inward Supplies", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "Import Of Services", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "Import Of Credit To Debit Booked", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "Advance Receipts", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "Amount Unadjusted Against Supplies", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "Sales Against Advance Form Previous Periods", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "Advance Payment", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "Amount Unadjusted Against Purchase", "", "", "", "", "", ""));
        data.add(new GSTR3BDashboaedDTO("", "Purchase Against Advance Form Previous Periods", "", "", "", "", "", ""));
        return data;
    }
}
