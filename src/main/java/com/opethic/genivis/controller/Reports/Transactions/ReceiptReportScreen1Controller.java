package com.opethic.genivis.controller.Reports.Transactions;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.Reports.PayableReportDTO;
import com.opethic.genivis.dto.Reports.PayableReportMainDTO;
import com.opethic.genivis.dto.Reports.ReceiptReportDTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.DateValidator;
import com.opethic.genivis.utils.GlobalController;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.opethic.genivis.utils.FxmFileConstants.PAYMENT_REPORT_SCREEN2_SLUG;
import static com.opethic.genivis.utils.FxmFileConstants.RECEIPT_SCREEN2_LIST_SLUG;

public class ReceiptReportScreen1Controller implements Initializable {

    private static final Logger receiptReportLogger = LoggerFactory.getLogger(ReceiptReportScreen1Controller.class);
    @FXML
    private TextField tfReceiptReportFromDate,tfReceiptReportToDate,tfReceiptReportLedgerSearch,tfReceiptReportFromAmount,tfReceiptReportToAmount;
    @FXML
    private TableView<ReceiptReportDTO> tblvReceiptReport;
    @FXML
    private ComboBox<String> cbReceiptReportDuration;
    @FXML
    private TableColumn<ReceiptReportDTO, String> tblcReceiptReportDate,tblcReceiptReportParticulars,tblcReceiptReportVoucherType,tblcReceiptReportVoucherNo,tblcReceiptReportDebitAmt,tblcReceiptReportCreditAmt;
    private String fromDate, toDate, companyName, selectedDuration, duration="month";
    private ObservableList<ReceiptReportDTO> originalData;
    @FXML
    private Label lblTotalDebit,lblTotalCredit;
    @FXML
    private Button btnReceiptExcel;

    //function for cursor forcus
    private void nodetraversal(Node current_node, Node next_node) {
        current_node.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                next_node.requestFocus();
                event.consume();
            }

            if (event.getCode() == KeyCode.SPACE) {
                if (current_node instanceof Button button) {
                    button.fire();
                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        DateValidator.applyDateFormat(tfReceiptReportFromDate);
        DateValidator.applyDateFormat(tfReceiptReportToDate);
        Platform.runLater(()->{
            tfReceiptReportLedgerSearch.requestFocus();
        });

        responsiveTable();
        getReceiptDetails();

        tfReceiptReportToDate.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB){
                getReceiptDetailsWithDate();
//                System.out.println("fromdate "+fromDate+"  todate "+toDate);
            }
        });
        cbReceiptReportDuration.getItems().addAll("This Month", "Last Month", "Half Year", "Full Year");
        cbReceiptReportDuration.setOnAction(this::handleDurationComboBox);
        cbReceiptReportDuration.setValue(cbReceiptReportDuration.getItems().get(0));
        nodetraversal(tfReceiptReportLedgerSearch,cbReceiptReportDuration);
        nodetraversal(cbReceiptReportDuration,tfReceiptReportFromDate);
        nodetraversal(tfReceiptReportFromDate,tfReceiptReportToDate);
        nodetraversal(tfReceiptReportToDate,tfReceiptReportFromAmount);
        nodetraversal(tfReceiptReportFromAmount,tfReceiptReportToAmount);

        tfReceiptReportToAmount.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER){
                if(!tfReceiptReportFromAmount.getText().isEmpty() && !tfReceiptReportToAmount.getText().isEmpty())
                    filterPaymentListByAmount();
                else calculateTotalAmount();
            }
        });
        btnReceiptExcel.setOnAction(event -> {
            exportToExcelForRecipt();
        });

    }
    //function for responsive of Table list
    private void responsiveTable(){
        tblcReceiptReportDate.prefWidthProperty().bind(tblvReceiptReport.widthProperty().multiply(0.1));
        tblcReceiptReportParticulars.prefWidthProperty().bind(tblvReceiptReport.widthProperty().multiply(0.5));
        tblcReceiptReportVoucherType.prefWidthProperty().bind(tblvReceiptReport.widthProperty().multiply(0.1));
        tblcReceiptReportVoucherNo.prefWidthProperty().bind(tblvReceiptReport.widthProperty().multiply(0.1));
        tblcReceiptReportDebitAmt.prefWidthProperty().bind(tblvReceiptReport.widthProperty().multiply(0.1));
        tblcReceiptReportCreditAmt.prefWidthProperty().bind(tblvReceiptReport.widthProperty().multiply(0.1));
    }

    //function for gettting the receipt details list
    private void getReceiptDetails(){
        receiptReportLogger.info("starting of getReceiptDetails");
        try{
            Map<String, String> body = new HashMap<>();
            body.put("duration",duration);
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.GET_RECEIPT_REPORT_DETAILS);

            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("res in receipt report "+responseBody);
            fromDate = responseBody.get("d_start_date").getAsString();
            toDate = responseBody.get("d_end_date").getAsString();
            companyName = responseBody.get("company_name").getAsString();
            LocalDate startLocalDt = LocalDate.parse(fromDate);
            LocalDate endLocalDt = LocalDate.parse(toDate);
            ObservableList<ReceiptReportDTO> observableList = FXCollections.observableArrayList();

            if(responseBody.get("responseStatus").getAsInt() == 200){

                JsonArray resArray = responseBody.get("response").getAsJsonArray();

                tfReceiptReportFromDate.setText(startLocalDt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                tfReceiptReportToDate.setText(endLocalDt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                if(resArray.size() >0){
                    for(JsonElement element : resArray){
                        JsonObject item = element.getAsJsonObject();
                        System.out.println("item in receipt report "+item);

                        String rowId = item.get("row_id").getAsString();
                        String tranxDate = item.get("transaction_date").getAsString();
                        String voucherNo = item.get("voucher_no").getAsString();
                        String voucherId = item.get("voucher_id").getAsString();
                        String particulars = item.get("particulars").getAsString();
                        String voucherType = item.get("voucher_type").getAsString();
                        String crAmount = item.get("credit").getAsString();
                        String drAmount = item.get("debit").getAsString();

                        observableList.add(new ReceiptReportDTO(fromDate,toDate, companyName, rowId, tranxDate, voucherNo, voucherId,particulars,voucherType,crAmount,drAmount));
                    }
                }else{
                    receiptReportLogger.info("response data is null or empty");
                }
                tblcReceiptReportDate.setCellValueFactory(new PropertyValueFactory<>("transaction_date"));
                tblcReceiptReportParticulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
                tblcReceiptReportVoucherType.setCellValueFactory(new PropertyValueFactory<>("voucher_type"));
                tblcReceiptReportVoucherNo.setCellValueFactory(new PropertyValueFactory<>("voucher_no"));
                tblcReceiptReportDebitAmt.setCellValueFactory(new PropertyValueFactory<>("debit"));
                tblcReceiptReportCreditAmt.setCellValueFactory(new PropertyValueFactory<>("credit"));

                tblvReceiptReport.setItems(observableList);
                calculateTotalAmount();
            }else{
                receiptReportLogger.error("error in getting the response");
            }

        }catch (Exception e){
            receiptReportLogger.error("error in getting the response "+e.getMessage());
        }
    }
    private void getReceiptDetailsWithDate(){
        receiptReportLogger.info("starting of getReceiptDetails");
        try{
            Map<String, String> body = new HashMap<>();
//            body.put("duration","month");
            System.out.println("from "+Communicator.text_to_date.fromString(tfReceiptReportToDate.getText()).toString()+"  to "+Communicator.text_to_date.fromString(tfReceiptReportFromDate.getText()).toString());
            body.put("start_date",  Communicator.text_to_date.fromString(tfReceiptReportFromDate.getText()).toString());
            body.put("end_date", Communicator.text_to_date.fromString(tfReceiptReportToDate.getText()).toString());
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.GET_RECEIPT_REPORT_DETAILS);

            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("res in receipt report "+responseBody);
            fromDate = responseBody.get("d_start_date").getAsString();
            toDate = responseBody.get("d_end_date").getAsString();
            companyName = responseBody.get("company_name").getAsString();
            ObservableList<ReceiptReportDTO> observableList = FXCollections.observableArrayList();

            if(responseBody.get("responseStatus").getAsInt() == 200){
//                tfReceiptReportFromDate.setText(fromDate);
//                tfReceiptReportToDate.setText(toDate);
                JsonArray resArray = responseBody.get("response").getAsJsonArray();
                if(resArray.size() >0){
                    for(JsonElement element : resArray){
                        JsonObject item = element.getAsJsonObject();
                        System.out.println("item in receipt report "+item);

                        String rowId = item.get("row_id").getAsString();
                        String tranxDate = item.get("transaction_date").getAsString();
                        String voucherNo = item.get("voucher_no").getAsString();
                        String voucherId = item.get("voucher_id").getAsString();
                        String particulars = item.get("particulars").getAsString();
                        String voucherType = item.get("voucher_type").getAsString();
                        String crAmount = item.get("credit").getAsString();
                        String drAmount = item.get("debit").getAsString();

                        observableList.add(new ReceiptReportDTO(fromDate,toDate, companyName, rowId, tranxDate, voucherNo, voucherId,particulars,voucherType,crAmount,drAmount));
                    }
                }else{
                    receiptReportLogger.info("response data is null or empty");
                }
                tblcReceiptReportDate.setCellValueFactory(new PropertyValueFactory<>("transaction_date"));
                tblcReceiptReportParticulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
                tblcReceiptReportVoucherType.setCellValueFactory(new PropertyValueFactory<>("voucher_type"));
                tblcReceiptReportVoucherNo.setCellValueFactory(new PropertyValueFactory<>("voucher_no"));
                tblcReceiptReportDebitAmt.setCellValueFactory(new PropertyValueFactory<>("debit"));
                tblcReceiptReportCreditAmt.setCellValueFactory(new PropertyValueFactory<>("credit"));

                tblvReceiptReport.setItems(observableList);
                calculateTotalAmount();
            }else{
                receiptReportLogger.error("error in getting the response");
            }

        }catch (Exception e){
            receiptReportLogger.error("error in getting the response "+e.getMessage());
        }
    }
    private void handleDurationComboBox(ActionEvent event){
        selectedDuration = cbReceiptReportDuration.getSelectionModel().getSelectedItem();
        if(selectedDuration == "This Month"){
            duration="month";
        }else if(selectedDuration == "Last Month"){
            duration = "lastMonth";
        }else if(selectedDuration == "Half Year"){
            duration = "halfYear";
        }else if(selectedDuration == "Full Year"){
            duration = "year";
            //to write the code of redirect to yearly page
            GlobalController.getInstance().addTabStatic(RECEIPT_SCREEN2_LIST_SLUG,false);
        }
        getReceiptDetails();
    }
    //function for calculation part
    private void calculateTotalAmount(){
        ObservableList<ReceiptReportDTO> observableList = tblvReceiptReport.getItems();
        double totalDebit = 0.0;
        double totalCredit = 0.0;

        for(ReceiptReportDTO item:observableList){
            totalDebit += Double.parseDouble(item.getDebit().isEmpty()? "0.0" : item.getDebit());
            totalCredit += Double.parseDouble(item.getCredit().isEmpty() ? "0.0" : item.getCredit());
        }
        lblTotalCredit.setText(String.format("%.2f",totalCredit));
        lblTotalDebit.setText(String.format("%.2f",totalDebit));

    }
    //function for filter the list by amount
    private void filterPaymentListByAmount() {
        ObservableList<ReceiptReportDTO> filteredData = FXCollections.observableArrayList();

        receiptReportLogger.info("Search Accounts Ledger Report 1 in AccountsLedgerReport1Controller");
        Double tfFromAmount = Double.parseDouble(tfReceiptReportFromAmount.getText());
        Double tfToAmount = Double.parseDouble(tfReceiptReportToAmount.getText());
        System.out.println();

//                filteredData = originalData.stream().filter((item -> Double.parseDouble(item.getDebit()) > 0 && Double.parseDouble(item.getDebit()) >= tfFromAmount && Double.parseDouble(item.getDebit()) <= tfToAmount )).collect(Collectors.toCollection(FXCollections::observableArrayList));

        filteredData = originalData.stream().filter((item -> ((Double.parseDouble(item.getDebit()) > 0 && Double.parseDouble(item.getDebit()) >= tfFromAmount && Double.parseDouble(item.getDebit()) <= tfToAmount) ||
                (Double.parseDouble(item.getCredit()) > 0 && Double.parseDouble(item.getCredit()) >= tfFromAmount && Double.parseDouble(item.getCredit()) <= tfToAmount)) )).collect(Collectors.toCollection(FXCollections::observableArrayList));


        tblvReceiptReport.setItems(filteredData);
//        calculateTotalAmount();
    }
    //function for eport to excel
    private void exportToExcelForRecipt() {
        ObservableList<ReceiptReportDTO> data = tblvReceiptReport.getItems();

        try {
//            System.out.println("data.size-- "+data.size());
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Excel File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
            );
            File file = fileChooser.showSaveDialog(new Stage());
            if (file != null) {
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Payable Report");

                // Create font for header
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setFontHeightInPoints((short) 12);
                CellStyle headerCellStyle = workbook.createCellStyle();

                headerCellStyle.setFont(headerFont);

                //start of code for header  ... Payable report from 01/06/2024 to 30/06/2024
                Font titleFont = workbook.createFont();
                titleFont.setBold(true);
                titleFont.setFontHeightInPoints((short) 16);
                CellStyle titleCellStyle = workbook.createCellStyle();
                titleCellStyle.setFont(titleFont);
                titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
                Row titleRow = sheet.createRow(0);
                org.apache.poi.ss.usermodel.Cell titleCell = titleRow.createCell(0);
                titleCell.setCellValue("Receipt Report From "+tfReceiptReportFromDate.getText()+" To "+tfReceiptReportToDate.getText());  //To show the Header with date
                titleCell.setCellStyle(titleCellStyle);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));    //for merging the no of columns for header text
                // Create header row
                Row headerRow = sheet.createRow(1);
                // Populate header row
                String[] headers = getColumnHeaders();

                for (int colNum = 0; colNum < headers.length; colNum++) {
                    Cell headerCell = headerRow.createCell(colNum);
                    headerCell.setCellValue(headers[colNum]);
                    headerCell.setCellStyle(headerCellStyle);
                }
                int[] columnWidths = {4000, 8000, 5000, 6000, 5000, 5000, 5000, 6000, 5000};
                for (int colNum = 0; colNum < columnWidths.length; colNum++) {
                    sheet.setColumnWidth(colNum, columnWidths[colNum]);
                }
                Double totalDebitAmount = 0.0;
                Double totalCreditAmount = 0.0;


                int rowNum = 2;
                int srNo =0;
                for (ReceiptReportDTO dto : data) {
                    Row row = sheet.createRow(rowNum++);
//                    srNo = srNo +1;
//                    row.createCell(0).setCellValue(srNo);
                    row.createCell(0).setCellValue(dto.getTransaction_date());
                    row.createCell(1).setCellValue(dto.getParticulars());
                    row.createCell(2).setCellValue(dto.getVoucher_type());
                    row.createCell(3).setCellValue(dto.getVoucher_no());
                    row.createCell(4).setCellValue(dto.getDebit());
                    row.createCell(5).setCellValue(dto.getCredit());

                    totalDebitAmount = totalDebitAmount + (dto.getDebit().isEmpty() ? 0.0 : Double.valueOf(dto.getDebit()));
                    totalCreditAmount = totalCreditAmount + (dto.getCredit().isEmpty() ? 0.0 : Double.parseDouble(dto.getCredit()));
                }

                Row prow = sheet.createRow(rowNum++);

                prow.createCell(0).setCellValue("Total");
                prow.createCell(4).setCellValue(totalDebitAmount);
                prow.createCell(5).setCellValue(totalCreditAmount);

                // Write the workbook content to a file
                FileOutputStream fileOut = new FileOutputStream(file);
                workbook.write(fileOut);
                fileOut.close();
                workbook.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String[] getColumnHeaders() {
        ObservableList<TableColumn<ReceiptReportDTO, ?>> columns = tblvReceiptReport.getColumns();
        String[] headers = {"Date", "Particulars", "Voucher Type", "Voucher No", "Debit Amount", "Credit Amount"};
        System.out.println("headers.length-- "+headers.length);
        for (int i = 0; i < headers.length; i++) {
            headers[i] = columns.get(i).getText();
        }
        return headers;
    }


}
