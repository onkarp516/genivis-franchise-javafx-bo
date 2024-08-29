package com.opethic.genivis.controller.Reports.Transactions;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.Reports.PayableReportDTO;
import com.opethic.genivis.dto.Reports.RecievableReportDTO;
import com.opethic.genivis.dto.StockReport2DTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.DateValidator;
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

import javax.swing.text.DateFormatter;
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

public class PayableReportController implements Initializable {
    private static final Logger payableReportLogger = LoggerFactory.getLogger(PayableReportController.class);
    @FXML
    private TableView<PayableReportDTO> tblvPayableReport;
    @FXML
    private TextField tfPayableReportLedgerSearch;
    private ObservableList<PayableReportDTO> originalData;
    @FXML
    private TableColumn<PayableReportDTO,String> tblcPayableReportLedger,tblcPayableReportBillNo,tblcPayableReportDate,
            tblcPayableReportInvoiceAmt,tblcPayableReportPaidAmt,tblcPayableReportBalanceAmt,
            tblcPayableReportType,tblcPayableReportDueDate,tblcPayableReportOverDueDays;
    private String fromDate, toDate;
    @FXML
    private ComboBox<String> cbBalanceMethod;
    @FXML
    private TextField tfPayableReportFromDate,tfPayableReportToDate;
    @FXML
    private Label lblTotalInvoiceAmt,lblTotalPaidAmt,lblTotalBalanceAmt;
    @FXML
    private Button btnPayableExport;
    String selectedBalancingMethod = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(()->{
            tfPayableReportLedgerSearch.requestFocus();
        });
        responsiveTable();
        LocalDate thisMonth = LocalDate.now();
        fromDate = thisMonth.withDayOfMonth(1).toString();
        toDate = thisMonth.withDayOfMonth(thisMonth.lengthOfMonth()).toString();
        getPayableReport();

        DateValidator.applyDateFormat(tfPayableReportFromDate);
        DateValidator.applyDateFormat(tfPayableReportToDate);
        cbBalanceMethod.getItems().addAll("All", "BillByBill", "OnAccount");
        cbBalanceMethod.setPromptText("Select Type");
        cbBalanceMethod.setOnAction(this::handleFilterComboBox);
        cbBalanceMethod.setValue(cbBalanceMethod.getItems().get(0));
        selectedBalancingMethod = "All";

        nodetraversal(tfPayableReportLedgerSearch,tfPayableReportFromDate);
        nodetraversal(tfPayableReportFromDate,tfPayableReportToDate);

        tfPayableReportToDate.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB){
                getPayableReportWithDate();
//                nodetraversal(tfPayableReportToDate,cbBalanceMethod);
            }
        });
        btnPayableExport.setOnAction(event -> {
            exportToExcelForPayable();

        });

    }
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
    private void responsiveTable(){
        tblcPayableReportLedger.prefWidthProperty().bind(tblvPayableReport.widthProperty().multiply(0.17));
        tblcPayableReportBillNo.prefWidthProperty().bind(tblvPayableReport.widthProperty().multiply(0.15));
        tblcPayableReportDate.prefWidthProperty().bind(tblvPayableReport.widthProperty().multiply(0.08));
        tblcPayableReportInvoiceAmt.prefWidthProperty().bind(tblvPayableReport.widthProperty().multiply(0.12));
        tblcPayableReportPaidAmt.prefWidthProperty().bind(tblvPayableReport.widthProperty().multiply(0.12));
        tblcPayableReportBalanceAmt.prefWidthProperty().bind(tblvPayableReport.widthProperty().multiply(0.12));
        tblcPayableReportType.prefWidthProperty().bind(tblvPayableReport.widthProperty().multiply(0.08));
        tblcPayableReportDueDate.prefWidthProperty().bind(tblvPayableReport.widthProperty().multiply(0.08));
        tblcPayableReportOverDueDays.prefWidthProperty().bind(tblvPayableReport.widthProperty().multiply(0.08));
    }
    private void getPayableReport(){       //Function to call at start
        payableReportLogger.info("starting of getPayableReport");
        try{
            System.out.println("fDay "+fromDate+"  lDay "+toDate);
            Map<String, String> body = new HashMap<>();
            body.put("","");

            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.GET_PAYABLE_REPORTS);

            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
            fromDate = responseBody.get("startDate").getAsString();
            toDate = responseBody.get("endDate").getAsString();
            LocalDate startLocalDate = LocalDate.parse(fromDate);
            LocalDate endLocalDate = LocalDate.parse(toDate);
            tfPayableReportFromDate.setText(startLocalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            tfPayableReportToDate.setText(endLocalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("res in payble report "+responseBody);
            ObservableList<PayableReportDTO> observableList = FXCollections.observableArrayList();
            if(responseBody.get("responseStatus").getAsInt() == 200){
                JsonArray jsonArray = responseBody.get("data").getAsJsonArray();
                if(jsonArray.size()>0){
                    for(int i=0;i<jsonArray.size();i++){
                        JsonObject arrItem = jsonArray.get(i).getAsJsonObject();
                        System.out.println("arrItem "+arrItem);
                        String ledgerName = arrItem.get("Ledger_name").getAsString();
                        String balancingMethodLedger = arrItem.get("balancing_method").getAsString();
                        String ftotal_amt = arrItem.get("total_amount").getAsString();
                        String totalBalance = arrItem.get("total_balance").getAsString();
                        String totalPaidAmt = arrItem.get("total_paid_amt").getAsString();
                        JsonArray invoiceData = arrItem.get("invoice_data").getAsJsonArray();
                        observableList.add(new PayableReportDTO(ledgerName,ftotal_amt, totalPaidAmt,totalBalance,"","","","","",
                                "","","","",""));
                        tblcPayableReportLedger.setCellValueFactory(new PropertyValueFactory<>("Ledger_name"));
                        tblcPayableReportInvoiceAmt.setCellValueFactory(new PropertyValueFactory<>("total_invoice_amount"));
                        tblcPayableReportPaidAmt.setCellValueFactory(new PropertyValueFactory<>("total_paid_amt"));
                        tblcPayableReportBalanceAmt.setCellValueFactory(new PropertyValueFactory<>("total_balance_amt"));

                        if(invoiceData.size()>0){
                            for(int j=0;j<invoiceData.size();j++){
                                JsonObject invItem = invoiceData.get(j).getAsJsonObject();
                                System.out.println("invItem "+invItem);

                                String creditorsId = invItem.get("CreditorsId").getAsString();
                                String dueDate = invItem.get("Due_date").getAsString();
                                String invoiceNo = invItem.get("Invoice_no").getAsString();
                                String crName = invItem.get("ledger_name").getAsString();
                                String balance = invItem.get("balance").getAsString();
                                String balancType = invItem.get("balancing_type").getAsString();
                                String creditDays = invItem.get("credit_days").getAsString();
                                String totalAmount = invItem.get("total_amount").getAsString();
                                String invoiceDate = invItem.get("invoiceDate").getAsString();
                                String overDueDays = invItem.get("overDueDays").getAsString();
                                String remainingAmt = invItem.get("remaining_amt").getAsString();
                                String balancingMethod = invItem.get("balancing_method").getAsString();
                                String paidAmt = invItem.get("paid_amt").getAsString();
                                observableList.add(new PayableReportDTO("","","","",invoiceNo,invoiceDate,totalAmount,
                                        paidAmt,balance,balancingMethod,dueDate,overDueDays,creditorsId,remainingAmt));
                                tblcPayableReportBillNo.setCellValueFactory(new PropertyValueFactory<>("invoice_no"));
                                tblcPayableReportDate.setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));
                                tblcPayableReportInvoiceAmt.setCellValueFactory(new PropertyValueFactory<>("total_amount"));
                                tblcPayableReportPaidAmt.setCellValueFactory(new PropertyValueFactory<>("paid_amt"));
                                tblcPayableReportBalanceAmt.setCellValueFactory(new PropertyValueFactory<>("remaining_amt"));   //remaining amt is Balance amt of the multiple bills
                                tblcPayableReportType.setCellValueFactory(new PropertyValueFactory<>("balancing_type"));
                                tblcPayableReportDueDate.setCellValueFactory(new PropertyValueFactory<>("due_date"));
                                tblcPayableReportOverDueDays.setCellValueFactory(new PropertyValueFactory<>("overDueDays"));
//                                tblvPayableReport.setItems(observableList);
                            }
                        }
                    }
                    tblvPayableReport.setItems(observableList);
                    originalData = observableList;
                    totalCalculation();
                }
            }
        }catch (Exception e){
            payableReportLogger.error("error in getting payable report "+e.getMessage());
        }
    }

    private void getPayableReportWithDate(){       //Function to call at start
        payableReportLogger.info("starting of getPayableReport");
        try{
            System.out.println("fDay "+fromDate+"  lDay "+toDate);
            Map<String, String> body = new HashMap<>();
            body.put("start_date", Communicator.text_to_date.fromString(tfPayableReportFromDate.getText()).toString());
            body.put("end_date", Communicator.text_to_date.fromString(tfPayableReportToDate.getText()).toString());

            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.GET_PAYABLE_REPORTS);

            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("res in payble report "+responseBody);
            fromDate = responseBody.get("startDate").getAsString();
            toDate = responseBody.get("endDate").getAsString();
            LocalDate startLocalDate = LocalDate.parse(fromDate);
            LocalDate endLocalDate = LocalDate.parse(toDate);
            ObservableList<PayableReportDTO> observableList = FXCollections.observableArrayList();
            tfPayableReportFromDate.setText(startLocalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            tfPayableReportToDate.setText(endLocalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            if(responseBody.get("responseStatus").getAsInt() == 200){
                JsonArray jsonArray = responseBody.get("data").getAsJsonArray();
                if(jsonArray.size()>0){
                    for(int i=0;i<jsonArray.size();i++){
                        JsonObject arrItem = jsonArray.get(i).getAsJsonObject();
                        String ledgerName = arrItem.get("Ledger_name").getAsString();
                        String ftotal_amt = arrItem.get("total_amount").getAsString();
                        String totalBalance = arrItem.get("total_balance").getAsString();
                        String totalPaidAmt = arrItem.get("total_paid_amt").getAsString();
                        JsonArray invoiceData = arrItem.get("invoice_data").getAsJsonArray();
                        observableList.add(new PayableReportDTO(ledgerName,ftotal_amt, totalPaidAmt,totalBalance,"","","","","",
                                "","","","",""));
                        tblcPayableReportLedger.setCellValueFactory(new PropertyValueFactory<>("Ledger_name"));
                        tblcPayableReportInvoiceAmt.setCellValueFactory(new PropertyValueFactory<>("total_invoice_amount"));
                        tblcPayableReportPaidAmt.setCellValueFactory(new PropertyValueFactory<>("total_paid_amt"));
                        tblcPayableReportBalanceAmt.setCellValueFactory(new PropertyValueFactory<>("total_balance_amt"));

                        if(invoiceData.size()>0){
                            for(int j=0;j<invoiceData.size();j++){
                                JsonObject invItem = invoiceData.get(j).getAsJsonObject();
                                System.out.println("invItem "+invItem);

                                String creditorsId = invItem.get("CreditorsId").getAsString();
                                String dueDate = invItem.get("Due_date").getAsString();
                                String invoiceNo = invItem.get("Invoice_no").getAsString();
                                String crName = invItem.get("ledger_name").getAsString();
                                String balance = invItem.get("balance").getAsString();
                                String balancType = invItem.get("balancing_type").getAsString();
                                String creditDays = invItem.get("credit_days").getAsString();
                                String totalAmount = invItem.get("total_amount").getAsString();
                                String invoiceDate = invItem.get("invoiceDate").getAsString();
                                String overDueDays = invItem.get("overDueDays").getAsString();
                                String remainingAmt = invItem.get("remaining_amt").getAsString();
                                String paidAmt = invItem.get("paid_amt").getAsString();
                                String balancingMethod = invItem.get("balancing_method").getAsString();
                                observableList.add(new PayableReportDTO("","","","",invoiceNo,invoiceDate,totalAmount,
                                        paidAmt,balance,balancingMethod,dueDate,overDueDays,creditorsId,remainingAmt));
                                tblcPayableReportBillNo.setCellValueFactory(new PropertyValueFactory<>("invoice_no"));
                                tblcPayableReportDate.setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));
                                tblcPayableReportInvoiceAmt.setCellValueFactory(new PropertyValueFactory<>("total_amount"));
                                tblcPayableReportPaidAmt.setCellValueFactory(new PropertyValueFactory<>("paid_amt"));
                                tblcPayableReportBalanceAmt.setCellValueFactory(new PropertyValueFactory<>("remaining_amt"));   //remaining amt is Balance amt of the multiple bills
                                tblcPayableReportType.setCellValueFactory(new PropertyValueFactory<>("balancing_type"));
                                tblcPayableReportDueDate.setCellValueFactory(new PropertyValueFactory<>("due_date"));
                                tblcPayableReportOverDueDays.setCellValueFactory(new PropertyValueFactory<>("overDueDays"));
//                                tblvPayableReport.setItems(observableList);
                            }
                        }
                    }
                    tblvPayableReport.setItems(observableList);
                    originalData = observableList;
                    totalCalculation();
                    cbBalanceMethod.requestFocus();
                }
            }
        }catch (Exception e){
            payableReportLogger.error("error in getting payable report "+e.getMessage());
        }
    }
    //function for bottom calculation
    private void totalCalculation(){
        ObservableList<PayableReportDTO>  observableList = tblvPayableReport.getItems();
        double totalInvoiceAmount = 0.0;
        double totalPaidAmount = 0.0;
        double totalBalanceAmount = 0.0;

        for(PayableReportDTO item : observableList){
            totalInvoiceAmount += Double.parseDouble(item.getTotal_amount().isEmpty() ? "0.0" : item.getTotal_amount());
            totalPaidAmount += Double.parseDouble(item.getPaid_amt().isEmpty() ? "0.0" : item.getPaid_amt());
            totalBalanceAmount += Double.parseDouble(item.getBalance().isEmpty() ? "0.0" : item.getBalance());
        }
        System.out.println("invAmt "+totalInvoiceAmount+" paidAmt "+totalPaidAmount+" balAmt "+totalBalanceAmount);
        lblTotalInvoiceAmt.setText(String.format("%.2f", totalInvoiceAmount));
        lblTotalPaidAmt.setText(String.format("%.2f",totalPaidAmount));
        lblTotalBalanceAmt.setText(String.format("%.2f", totalBalanceAmount));
    }

    //function for set the balance method type for filter
    private void handleFilterComboBox(ActionEvent event){
        selectedBalancingMethod = cbBalanceMethod.getSelectionModel().getSelectedItem();
        if(cbBalanceMethod.getSelectionModel().getSelectedItem() == "OnAccount"){
            selectedBalancingMethod = "OnAccount";
        }else if(cbBalanceMethod.getSelectionModel().getSelectedItem() == "BillByBill"){
            selectedBalancingMethod = "BillByBill";
        }else if(cbBalanceMethod.getSelectionModel().getSelectedItem() == "All"){
            selectedBalancingMethod="All";
        }
        System.out.println("selectedBalancingMethod "+selectedBalancingMethod);
        filterPayableByBalancingMethod();

    }
    private void filterPayableByBalancingMethod() {
        ObservableList<PayableReportDTO> filteredData = FXCollections.observableArrayList();
        if (selectedBalancingMethod == "BillByBill") {
            filteredData = originalData.stream().filter((item -> item.getBalancing_method().equalsIgnoreCase("1"))).collect(Collectors.toCollection(FXCollections::observableArrayList));
        }if (selectedBalancingMethod == "OnAccount") {
            filteredData = originalData.stream().filter((item -> item.getBalancing_method().equalsIgnoreCase("2"))).collect(Collectors.toCollection(FXCollections::observableArrayList));
        }
        else {
            filteredData = originalData.stream().collect(Collectors.toCollection(FXCollections::observableArrayList));

        }

        tblvPayableReport.setItems(filteredData);
        totalCalculation();
    }
    //function for export to excel
    private void exportToExcelForPayable() {
        ObservableList<PayableReportDTO> data = tblvPayableReport.getItems();

        try {
            System.out.println("data.size-- "+data.size());
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
                Cell titleCell = titleRow.createCell(0);
                titleCell.setCellValue("Payable Report From "+tfPayableReportFromDate.getText()+" to "+tfPayableReportToDate.getText());
                titleCell.setCellStyle(titleCellStyle);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));    //for merging the no of columns for header
                // Create header row
                Row headerRow = sheet.createRow(1);
                // Populate header row
                String[] headers = getColumnHeaders();

                for (int colNum = 0; colNum < headers.length; colNum++) {
                    Cell headerCell = headerRow.createCell(colNum);
                    headerCell.setCellValue(headers[colNum]);
                    headerCell.setCellStyle(headerCellStyle);
                }
                int[] columnWidths = {7000, 3000, 3000, 4000, 4000, 4000, 3000, 3000, 3000};
                for (int colNum = 0; colNum < columnWidths.length; colNum++) {
                    sheet.setColumnWidth(colNum, columnWidths[colNum]);
                }
                Double totalInvoiceAmount = 0.0;
                Double totalPaidAmount = 0.0;
                Double totalBalanceAmount = 0.0;
                // Populate data rows
                int rowNum = 2;
                int srNo =0;
                for (PayableReportDTO dto : data) {
                    Row row = sheet.createRow(rowNum++);
//                    srNo = srNo +1;
//                    row.createCell(0).setCellValue(srNo);
                    row.createCell(0).setCellValue(dto.getLedger_name());
                    row.createCell(1).setCellValue(dto.getInvoice_no());
                    row.createCell(2).setCellValue(dto.getInvoiceDate());
                    row.createCell(3).setCellValue(dto.getTotal_amount());
                    row.createCell(4).setCellValue(dto.getPaid_amt());
                    row.createCell(5).setCellValue(dto.getBalance());
                    row.createCell(6).setCellValue("");
                    row.createCell(7).setCellValue(dto.getDue_date());
                    row.createCell(8).setCellValue(dto.getOverDueDays());
                    System.out.println("totalInvoiceAmount-- "+totalInvoiceAmount+"  invoiceAmtt "+dto.getPaid_amt());
                    totalInvoiceAmount = totalInvoiceAmount + (dto.getTotal_amount().isEmpty() ? 0.0 : Double.valueOf(dto.getTotal_amount()));
                    totalPaidAmount = totalPaidAmount +(dto.getPaid_amt().isEmpty() ? 0.0 :  Double.valueOf(dto.getPaid_amt()));
                    totalBalanceAmount = totalBalanceAmount + (dto.getBalance().isEmpty() ? 0.0 : Double.valueOf(dto.getBalance()));
                }

                Row prow = sheet.createRow(rowNum++);

                prow.createCell(0).setCellValue("Total");
                prow.createCell(3).setCellValue(totalInvoiceAmount);
                prow.createCell(4).setCellValue(totalPaidAmount);
                prow.createCell(5).setCellValue(totalBalanceAmount);


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
        ObservableList<TableColumn<PayableReportDTO, ?>> columns = tblvPayableReport.getColumns();
        String[] headers = {"Ledger Name", "Bill No", "Date", "Invoice Amt", "Paid Amt", "Balance Amt", "Type", "Due Date", "Over Due Days"};
        System.out.println("headers.length-- "+headers.length);
        for (int i = 0; i < headers.length; i++) {
            headers[i] = columns.get(i).getText();
        }
        return headers;
    }

}
