package com.opethic.genivis.controller.Reports.Transactions;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.LedgerReport1DTO;
import com.opethic.genivis.dto.Reports.PayableReportDTO;
import com.opethic.genivis.dto.Reports.PaymentReportScreen1DTO;
import com.opethic.genivis.dto.Reports.ReceiptReportDTO;
import com.opethic.genivis.dto.StockReport2DTO;
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
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;
import static com.opethic.genivis.utils.FxmFileConstants.*;

public class PaymentReportScreen1Controller implements Initializable {
    private static final Logger paymentReportLogger = LoggerFactory.getLogger(PaymentReportScreen1Controller.class);
    @FXML
    private TextField tfPaymentReportFromDate,tfPaymentReportToDate,tfPaymentReportLedgerSearch,tfPaymentReportFromAmount,tfPaymentReportToAmount;
    @FXML
    private TableView<PaymentReportScreen1DTO> tblvPaymentReport;
    private ObservableList<PaymentReportScreen1DTO> originalData;
    @FXML
    private TableColumn<PaymentReportScreen1DTO, String> tblcPaymentReportDate,tblcPaymentReportParticulars,tblcPaymentReportVoucherType,tblcPaymentReportVoucherNo,
            tblcPaymentReportDebitAmt,tblcPaymentReportCreditAmt;
    @FXML
    private ComboBox<String> cbFilter,cbPaymentReportDuration;
    private String startDate, toDate, companyName,selectedFilter, selectedDuration,duration="month";
    @FXML
    private Label lblTotalDebit,lblTotalCredit;
    @FXML
    private Button btnPdf,btnExcel,btnCsv,btnPrint;


    //function for cursor focus
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

        DateValidator.applyDateFormat(tfPaymentReportFromDate);

        DateValidator.applyDateFormat(tfPaymentReportToDate);
        nodetraversal(tfPaymentReportLedgerSearch,cbPaymentReportDuration);
        nodetraversal(cbPaymentReportDuration,tfPaymentReportFromDate);
        nodetraversal(tfPaymentReportFromDate,tfPaymentReportToDate);
        nodetraversal(tfPaymentReportToDate,tfPaymentReportFromAmount);
        nodetraversal(tfPaymentReportFromAmount,tfPaymentReportToAmount);
        nodetraversal(tfPaymentReportToAmount,btnPdf);
        Platform.runLater(()->{
            tfPaymentReportLedgerSearch.requestFocus();
        });
        cbPaymentReportDuration.getItems().addAll("This Month", "Last Month", "Half Year", "Full Year");
        cbPaymentReportDuration.setPromptText("Select Duration");
        cbPaymentReportDuration.setOnAction(this::handleDurationComboBox);
        cbPaymentReportDuration.setValue(cbPaymentReportDuration.getItems().get(0));
        //todo:open Filter dropdown on Space
        cbPaymentReportDuration.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cbPaymentReportDuration.isShowing()) {
                cbPaymentReportDuration.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });

        responsiveTable();
        getPaymentDetails();

        tfPaymentReportToDate.setOnKeyPressed(event ->{
            if(event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB){
                System.out.println("from "+startDate+"  to "+toDate);
                getPaymentDetailsWithDate();
                tfPaymentReportFromAmount.requestFocus();
            }
        });
        cbFilter.getItems().addAll("Supplier", "Invoice");
        cbFilter.setPromptText("Filter Search");
        cbFilter.setOnAction(this::handleFilterComboBoxAction);
        cbFilter.setValue("Supplier");
        handleFilterComboBoxAction(null);
        //todo:open Filter dropdown on Space
        cbFilter.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cbFilter.isShowing()) {
                cbFilter.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });

        tfPaymentReportToAmount.addEventFilter(KeyEvent.ANY, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) {
                if (!tfPaymentReportFromAmount.getText().isEmpty() && !tfPaymentReportToAmount.getText().isEmpty()) {
                    filterPaymentListByAmount();
                    nodetraversal(tfPaymentReportToDate,tfPaymentReportFromAmount);
                } else {
//                    filterLedgerListByFilter();
                }

            }
        });
        btnExcel.setOnAction(event -> {
            exportToExcelForPayment();
        });
    }

    //function for responsiveness of list table
    private void responsiveTable(){
        tblcPaymentReportDate.prefWidthProperty().bind(tblvPaymentReport.widthProperty().multiply(0.1));
        tblcPaymentReportParticulars.prefWidthProperty().bind(tblvPaymentReport.widthProperty().multiply(0.5));
        tblcPaymentReportVoucherType.prefWidthProperty().bind(tblvPaymentReport.widthProperty().multiply(0.1));
        tblcPaymentReportVoucherNo.prefWidthProperty().bind(tblvPaymentReport.widthProperty().multiply(0.1));
        tblcPaymentReportDebitAmt.prefWidthProperty().bind(tblvPaymentReport.widthProperty().multiply(0.1));
        tblcPaymentReportCreditAmt.prefWidthProperty().bind(tblvPaymentReport.widthProperty().multiply(0.1));
    }

    //function for get the payment account entry details list
    private void getPaymentDetails(){
        paymentReportLogger.info("starting of getReceiptDetails");
        try{
            System.out.println("selectedDuration --->>> "+duration);
            Map<String, String> body = new HashMap<>();
            body.put("duration",duration);
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.GET_PAYMENT_REPORT_DETAILS);

            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("res in receipt report "+responseBody);
            startDate = responseBody.get("d_start_date").getAsString();
            toDate = responseBody.get("d_end_date").getAsString();
            companyName = responseBody.get("company_name").getAsString();
            LocalDate startLocalDt = LocalDate.parse(startDate);
            LocalDate endLocalDt = LocalDate.parse(toDate);
            ObservableList<PaymentReportScreen1DTO> observableList = FXCollections.observableArrayList();

            if(responseBody.get("responseStatus").getAsInt() == 200){

                JsonArray resArray = responseBody.get("response").getAsJsonArray();

//                tfPaymentReportFromDate.setText(fromDate);
//                tfPaymentReportToDate.setText(toDate);
                System.out.println("");
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

                        observableList.add(new PaymentReportScreen1DTO(startDate,toDate, companyName, rowId, tranxDate, voucherNo, voucherId,particulars,voucherType,crAmount,drAmount));
                    }
                }else{
                    paymentReportLogger.info("response data is null or empty");
                }
                tfPaymentReportFromDate.setText(startLocalDt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                tfPaymentReportToDate.setText(endLocalDt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                tblcPaymentReportDate.setCellValueFactory(new PropertyValueFactory<>("transaction_date"));
                tblcPaymentReportParticulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
                tblcPaymentReportVoucherType.setCellValueFactory(new PropertyValueFactory<>("voucher_type"));
                tblcPaymentReportVoucherNo.setCellValueFactory(new PropertyValueFactory<>("voucher_no"));
                tblcPaymentReportDebitAmt.setCellValueFactory(new PropertyValueFactory<>("debit"));
                tblcPaymentReportCreditAmt.setCellValueFactory(new PropertyValueFactory<>("credit"));

                tblvPaymentReport.setItems(observableList);
                originalData = observableList;
                calculateTotalAmount();
            }else{
                paymentReportLogger.error("error in getting the response");
            }

        }catch (Exception e){
            paymentReportLogger.error("error in getting the response "+e.getMessage());
        }
    }
    //function for get the list with date
    private void getPaymentDetailsWithDate(){
        paymentReportLogger.info("starting of getReceiptDetails");
        try{
            Map<String, String> body = new HashMap<>();
            body.put("start_date",Communicator.text_to_date.fromString(tfPaymentReportFromDate.getText()).toString());
            body.put("end_date", Communicator.text_to_date.fromString(tfPaymentReportToDate.getText()).toString());
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.GET_PAYMENT_REPORT_DETAILS);

            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("res in receipt report "+responseBody);
            startDate = responseBody.get("d_start_date").getAsString();
            toDate = responseBody.get("d_end_date").getAsString();
            companyName = responseBody.get("company_name").getAsString();
            LocalDate startLocalDt = LocalDate.parse(startDate);
            LocalDate endLocalDt = LocalDate.parse(toDate);
            ObservableList<PaymentReportScreen1DTO> observableList = FXCollections.observableArrayList();

            if(responseBody.get("responseStatus").getAsInt() == 200){

                JsonArray resArray = responseBody.get("response").getAsJsonArray();

//                tfPaymentReportFromDate.setText(fromDate);
//                tfPaymentReportToDate.setText(toDate);
                System.out.println("");
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

                        observableList.add(new PaymentReportScreen1DTO(startDate,toDate, companyName, rowId, tranxDate, voucherNo, voucherId,particulars,voucherType,crAmount,drAmount));
                    }
                }else{
                    paymentReportLogger.info("response data is null or empty");
                }
                originalData = observableList;
                tfPaymentReportFromDate.setText(startLocalDt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                tfPaymentReportToDate.setText(endLocalDt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                tblcPaymentReportDate.setCellValueFactory(new PropertyValueFactory<>("transaction_date"));
                tblcPaymentReportParticulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
                tblcPaymentReportVoucherType.setCellValueFactory(new PropertyValueFactory<>("voucher_type"));
                tblcPaymentReportVoucherNo.setCellValueFactory(new PropertyValueFactory<>("voucher_no"));
                tblcPaymentReportDebitAmt.setCellValueFactory(new PropertyValueFactory<>("debit"));
                tblcPaymentReportCreditAmt.setCellValueFactory(new PropertyValueFactory<>("credit"));

                tblvPaymentReport.setItems(observableList);
                calculateTotalAmount();
            }else{
                paymentReportLogger.error("error in getting the response");
            }

        }catch (Exception e){
            paymentReportLogger.error("error in getting the response "+e.getMessage());
        }
    }
    //
    private void handleFilterComboBoxAction(ActionEvent event) {
        selectedFilter = cbFilter.getSelectionModel().getSelectedItem();
//        System.out.println("Selected Filter: " + selectedFilter);
        if (cbFilter.getSelectionModel().getSelectedItem() == "Supplier") {
            selectedFilter = "supplier";
//            System.out.println("selectedType- " + selectedFilter);
        } else if (cbFilter.getSelectionModel().getSelectedItem() == "Invoice") {
            selectedFilter = "invoice";
//            System.out.println("selectedType- " + selectedFilter);
        }
        System.out.println("selectedFilterrr "+selectedFilter);
        fetchStockReport2ListByFilterSearch();
    }
    //
    private void fetchStockReport2ListByFilterSearch() {
        ObservableList<PaymentReportScreen1DTO> filteredData = FXCollections.observableArrayList();

        if (selectedFilter.equals("supplier")) {
            tfPaymentReportLedgerSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData(newValue.trim(), PaymentReportScreen1DTO::getParticulars);
            });
            calculateTotalAmount();
        } else if (selectedFilter.equals("invoice")) {
            tfPaymentReportLedgerSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData(newValue.trim(), PaymentReportScreen1DTO::getVoucher_no);
            });
            calculateTotalAmount();
        }
    }
    //
    private void filterData(String keyword, Function<PaymentReportScreen1DTO, String> fieldExtractor) {
        ObservableList<PaymentReportScreen1DTO> filteredData = FXCollections.observableArrayList();
        paymentReportLogger.info("Search Stock Report 2 List in StocksStockReport2Controller");
        if (keyword.isEmpty()) {
            tblvPaymentReport.setItems(originalData);
            calculateTotalAmount();
            return;
        }
        for (PaymentReportScreen1DTO item : originalData) {
            if (matchesKeyword(item, keyword, fieldExtractor)) {
                filteredData.add(item);
            }
        }
        tblvPaymentReport.setItems(filteredData);
        calculateTotalAmount();
    }

    //FUNCTION for filter the data by amount
    private void filterPaymentListByAmount() {
        ObservableList<PaymentReportScreen1DTO> filteredData = FXCollections.observableArrayList();

        paymentReportLogger.info("Search Accounts Ledger Report 1 in AccountsLedgerReport1Controller");
        Double tfFromAmount = Double.parseDouble(tfPaymentReportFromAmount.getText());
        Double tfToAmount = Double.parseDouble(tfPaymentReportToAmount.getText());
        System.out.println();

//                filteredData = originalData.stream().filter((item -> Double.parseDouble(item.getDebit()) > 0 && Double.parseDouble(item.getDebit()) >= tfFromAmount && Double.parseDouble(item.getDebit()) <= tfToAmount )).collect(Collectors.toCollection(FXCollections::observableArrayList));

        filteredData = originalData.stream().filter((item -> ((Double.parseDouble(item.getDebit()) > 0 && Double.parseDouble(item.getDebit()) >= tfFromAmount && Double.parseDouble(item.getDebit()) <= tfToAmount) || (Double.parseDouble(item.getCredit()) > 0 && Double.parseDouble(item.getCredit()) >= tfFromAmount && Double.parseDouble(item.getCredit()) <= tfToAmount)) )).collect(Collectors.toCollection(FXCollections::observableArrayList));


        tblvPaymentReport.setItems(filteredData);
        calculateTotalAmount();
    }

    private boolean matchesKeyword(PaymentReportScreen1DTO item, String keyword, Function<PaymentReportScreen1DTO, String> fieldExtractor) {
        String lowerCaseKeyword = keyword.toLowerCase();
        String fieldValue = fieldExtractor.apply(item).toLowerCase();
        return fieldValue.contains(lowerCaseKeyword);
    }
    //function for get the list by duration
    private void handleDurationComboBox(ActionEvent event){
        selectedDuration = cbPaymentReportDuration.getSelectionModel().getSelectedItem();
        if(selectedDuration == "This Month"){
            duration="month";
        }else if(selectedDuration == "Last Month"){
            duration = "lastMonth";
        }else if(selectedDuration == "Half Year"){
            duration = "halfYear";
        }else if(selectedDuration == "Full Year"){
            duration = "year";
            //to write the code of redirect to yearly page
            GlobalController.getInstance().addTabStatic(PAYMENT_REPORT_SCREEN2_SLUG,false);
        }
        getPaymentDetails();
    }
    private void calculateTotalAmount(){
        ObservableList<PaymentReportScreen1DTO> observableList = tblvPaymentReport.getItems();
        double totalDebit = 0.0;
        double totalCredit = 0.0;

        for(PaymentReportScreen1DTO item:observableList){
            totalDebit += Double.parseDouble(item.getDebit().isEmpty()? "0.0" : item.getDebit());
            totalCredit += Double.parseDouble(item.getCredit().isEmpty() ? "0.0" : item.getCredit());
        }
        lblTotalCredit.setText(String.format("%.2f",totalCredit));
        lblTotalDebit.setText(String.format("%.2f",totalDebit));

    }
    //function for export to excel
    private void exportToExcelForPayment() {
        ObservableList<PaymentReportScreen1DTO> data = tblvPaymentReport.getItems();

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
                titleCell.setCellValue("Payment Report From "+tfPaymentReportFromDate.getText()+" To "+tfPaymentReportToDate.getText());  //To show the Header with date
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
                for (PaymentReportScreen1DTO dto : data) {
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
        ObservableList<TableColumn<PaymentReportScreen1DTO, ?>> columns = tblvPaymentReport.getColumns();
        String[] headers = {"Date", "Particulars", "Voucher Type", "Voucher No", "Debit Amount", "Credit Amount"};
        System.out.println("headers.length-- "+headers.length);
        for (int i = 0; i < headers.length; i++) {
            headers[i] = columns.get(i).getText();
        }
        return headers;
    }


}
