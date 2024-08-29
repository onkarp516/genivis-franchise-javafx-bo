package com.opethic.genivis.controller.GSTR2;

import com.opethic.genivis.dto.GSTR2.GSTR2DashboardDTO;
import com.opethic.genivis.utils.GlobalController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import java.net.URL;
import java.util.ResourceBundle;
import static com.opethic.genivis.utils.FxmFileConstants.*;

public class GSTR2DashboardController implements Initializable {
    @FXML
    private TableView<GSTR2DashboardDTO> tblvGSTR2DashboardList;
    @FXML
    private TableColumn<GSTR2DashboardDTO, String> tblGSTR2DashboardTable_no, tblGSTR2DashboardParticulars, tblGSTR2DashboardTaxableAmt,
            tblGSTR2DashboardIGSTAmt, tblGSTR2DashboardCGSTAmt, tblGSTR2DashboardSGSTAmt, tblGSTR2DashboardCessAmt, tblGSTR2DashboardTaxAmt;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        tblGSTR2DashboardParticulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
        tblGSTR2DashboardParticulars.setCellFactory(column -> new TableCell<GSTR2DashboardDTO, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.contains("B2B Other Taxable Invoice")) {
                        setStyle("-fx-font-weight: normal; -fx-text-fill: #257dff; -fx-underline: true;");
                    } else if (item.contains("Credit/Debit Notes Registered")){
                        setStyle("-fx-font-weight: normal; -fx-text-fill: #257dff; -fx-underline: true;");
                    } else if (item.contains("Credit/Debit Notes Unregistered")){
                        setStyle("-fx-font-weight: normal; -fx-text-fill: #257dff; -fx-underline: true;");
                    } else if (item.contains("Amnedments To Credit/Debit Notes")){
                        setStyle("-fx-font-weight: normal; -fx-text-fill: #257dff; -fx-underline: true;");
                    } else if (item.contains("Nil Rated/Exempted Invoice")){
                        setStyle("-fx-font-weight: normal; -fx-text-fill: #257dff; -fx-underline: true;");
                    } else {
                        setStyle("-fx-font-weight: normal; -fx-text-fill: black;");
                    }
                }
            }
        });
        //code for go to next page of GSTR2
        tblvGSTR2DashboardList.setRowFactory(tableview->{
            TableRow<GSTR2DashboardDTO> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {

                if (!row.isEmpty() && mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2){
                    GSTR2DashboardDTO clickRow = row.getItem();
                    if(clickRow.getParticulars().contains("Credit/Debit Notes Unregistered")){
                        redirectToCreditDebitUnregister();
                    }else if(clickRow.getParticulars().contains("Amnedments To Credit/Debit Notes")){
                        redirectToAmmendCreditDebitReg();
                    }
                }
            });
            return row;
        });

        fetchGSTR1DashboardList("");
    }

    private void fetchGSTR1DashboardList(String s) {
        tblGSTR2DashboardTable_no.setCellValueFactory(new PropertyValueFactory<>("table_no"));
        tblGSTR2DashboardParticulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
        tblGSTR2DashboardTaxableAmt.setCellValueFactory(new PropertyValueFactory<>("taxable_amt"));
        tblGSTR2DashboardIGSTAmt.setCellValueFactory(new PropertyValueFactory<>("igst_amt"));
        tblGSTR2DashboardCGSTAmt.setCellValueFactory(new PropertyValueFactory<>("cgst_amt"));
        tblGSTR2DashboardSGSTAmt.setCellValueFactory(new PropertyValueFactory<>("sgst_amt"));
        tblGSTR2DashboardCessAmt.setCellValueFactory(new PropertyValueFactory<>("cess_amt"));
        tblGSTR2DashboardTaxAmt.setCellValueFactory(new PropertyValueFactory<>("tax_amt"));

        // Add data to the table
        tblvGSTR2DashboardList.setItems(getGSTR2DashboardData());
    }

    private ObservableList<GSTR2DashboardDTO> getGSTR2DashboardData() {
        ObservableList<GSTR2DashboardDTO> data = FXCollections.observableArrayList();
        data.add(new GSTR2DashboardDTO("1", "B2B Other Taxable Invoice", "00.00", "", "00.00", "00.00", "", "00.00"));
        data.add(new GSTR2DashboardDTO("", "Amendments to B2B Invoices", "", "", "", "", "", ""));
        data.add(new GSTR2DashboardDTO("", "Reverse Charge Supplies", "", "", "", "", "", ""));
        data.add(new GSTR2DashboardDTO("2", "Credit/Debit Notes Registered", "00.00", "", "00.00", "00.00", "", "00.00"));
        data.add(new GSTR2DashboardDTO("", "Credit/Debit Notes Unregistered", "", "", "", "", "", ""));
//        data.add(new GSTR2DashboardDTO("", "", "", "", "", "", "", ""));
        data.add(new GSTR2DashboardDTO("3", "Amnedments To Credit/Debit Notes", "", "", "", "", "", ""));
        data.add(new GSTR2DashboardDTO("4", "ISD Credits", "", "", "", "", "", ""));
        data.add(new GSTR2DashboardDTO("5", "Import of Goods From Overseas On Bill Of Entry", "", "", "", "", "", ""));
        data.add(new GSTR2DashboardDTO("6", "Import of Goods From SEZ Unit/Deveopers On Bill Entry", "", "", "", "", "", ""));
        data.add(new GSTR2DashboardDTO("7", "Nil Rated/Exempted Invoice", "00.00", "", "00.00", "00.00", "", "00.00"));
        data.add(new GSTR2DashboardDTO("", "Nil Rated", "", "", "", "", "", ""));
        data.add(new GSTR2DashboardDTO("", "Exempted", "", "", "", "", "", ""));
        data.add(new GSTR2DashboardDTO("8", "ITC Reversal", "", "", "", "", "", ""));
        data.add(new GSTR2DashboardDTO("9", "Tax Paid On Reverse Charges", "", "", "", "", "", ""));
        data.add(new GSTR2DashboardDTO("10", "Tax Paid Under Reverse Charges On Advance", "", "", "", "", "", ""));
        return data;
    }
    private void redirectToCreditDebitUnregister(){
        GlobalController.getInstance().addTabStatic(GSTR2_CR_DR_UNREGISTER_SLUG, false);
    }
    private  void redirectToAmmendCreditDebitReg(){
        GlobalController.getInstance().addTabStatic(GSTR2_AMMEND_CR_DR_REGISTER_SLUG,false);
    }
}
