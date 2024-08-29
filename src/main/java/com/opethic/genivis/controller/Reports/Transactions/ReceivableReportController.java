package com.opethic.genivis.controller.Reports.Transactions;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.GSTR1.GSTR1B2B1DTO;
import com.opethic.genivis.dto.LedgerReport2DTO;
import com.opethic.genivis.dto.Reports.PaymentReportScreen1DTO;
import com.opethic.genivis.dto.Reports.ReceiptReportDTO;
import com.opethic.genivis.dto.Reports.RecievableReportDTO;
import com.opethic.genivis.dto.Reports.receivableDTOMain;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.CommonTraversa;
import com.opethic.genivis.utils.DateValidator;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReceivableReportController implements Initializable {

    private static final Logger receivableReportLogger = LoggerFactory.getLogger(ReceivableReportController.class);
    @FXML
    private ComboBox<String> cbType;
    @FXML
    private ComboBox<String> cbBalanceMethod;
    @FXML
    private TextField tfReceivableReportLedgerSearch,tfReceivableReportFromDate,tfReceivableReportToDate;
    @FXML
    private TableView<RecievableReportDTO> tblvReceivableReport;
    @FXML
    private TableColumn<RecievableReportDTO,String> tblcReceivableReportLedger,tblcReceivableReportBillNo,tblcReceivableReportDate,tblcReceivableReportInvoiceAmt,tblcReceivableReportPaidAmt,
            tblcReceivableReportBalanceAmt, tblcReceivableReportType,tblcReceivableReportDueDate,tblcReceivableReportOverDueDays;
    @FXML
    private Label lblTotalInvoiceAmt,lblTotalPaidAmt,lblTotalBalanceAmt;
    @FXML
    private Button btnReceivableExcelToExport,btnReceivablePdf,btnReceivableCsv,btnReceivablePrint;
    @FXML
    private HBox hboxReceivableFilter;
    private String fromDate="", toDate="", selectedBalancingMethod="", selectedFilter;
    private ObservableList<RecievableReportDTO> originalData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(()->{
            tfReceivableReportLedgerSearch.requestFocus();
        });
        DateValidator.applyDateFormat(tfReceivableReportFromDate);
        DateValidator.applyDateFormat(tfReceivableReportToDate);
        responsiveTable();
        getReceivableReport();
        tfReceivableReportToDate.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER ||event.getCode() == KeyCode.TAB ){
                getReceivableReportWithDate();
            }
        });
        cbBalanceMethod.getItems().addAll("All" , "OnAccount","BillByBill");
        cbBalanceMethod.setValue(cbBalanceMethod.getItems().get(0));
        cbBalanceMethod.setOnAction(this::handleFilterComboBox);
        //todo:open Filter dropdown on Space
        cbBalanceMethod.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cbBalanceMethod.isShowing()) {
                cbBalanceMethod.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });

        cbType.getItems().addAll("Ledger", "Invoice");
        cbType.setOnAction(this::handleFilterTypeComboBoxAction);
        cbType.setValue("Ledger");
        handleFilterTypeComboBoxAction(null);
        //todo:open Filter dropdown on Space
        cbType.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cbType.isShowing()) {
                cbType.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });
        btnReceivableExcelToExport.setOnAction(event ->{
            exportToExcelForReceivable();
        });
        hboxReceivableFilter.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if(event.getCode() == KeyCode.DOWN){
                tblvReceivableReport.requestFocus();
                tblvReceivableReport.getSelectionModel().select(0);

                event.consume();
            }
        });

        nodetraversal(tfReceivableReportLedgerSearch,tfReceivableReportFromDate);
        nodetraversal(tfReceivableReportFromDate,tfReceivableReportToDate);
        setKeyPressedHandler(cbBalanceMethod);
        setKeyPressedHandler(btnReceivablePdf);
        setKeyPressedHandler(btnReceivableExcelToExport);
        setKeyPressedHandler(btnReceivableCsv);
        setKeyPressedHandler(btnReceivablePrint);

    }

    private void handleFilterTypeComboBoxAction(ActionEvent event) {
        selectedFilter = cbType.getSelectionModel().getSelectedItem();
        if (cbType.getSelectionModel().getSelectedItem() == "Ledger") {
            selectedFilter = "ledger";
        } else if (cbType.getSelectionModel().getSelectedItem() == "Invoice") {
            selectedFilter = "invoice";
        }
        System.out.println("selectedFilterrr "+selectedFilter);
        fetchListByFilterSearch();
    }

    private void fetchListByFilterSearch() {
        ObservableList<RecievableReportDTO> filteredData = FXCollections.observableArrayList();

        if (selectedFilter.equals("ledger")) {
            tfReceivableReportLedgerSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData(newValue.trim(), RecievableReportDTO::getLedger_name);
            });
//            calculateTotalAmount();
        } else if (selectedFilter.equals("invoice")) {
            tfReceivableReportLedgerSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData(newValue.trim(), RecievableReportDTO::getInvoice_no);
            });
        }
    }

    private void filterData(String keyword, Function<RecievableReportDTO, String> fieldExtractor) {
        ObservableList<RecievableReportDTO> filteredData = FXCollections.observableArrayList();
        receivableReportLogger.info("Search Receivable Report List in ReceivableReportController");
        if (keyword.isEmpty()) {
            tblvReceivableReport.setItems(originalData);
//            calculateTotalAmount();
            return;
        }
        for (RecievableReportDTO item : originalData) {
            if (matchesKeyword(item, keyword, fieldExtractor)) {
                filteredData.add(item);
            }
        }
        tblvReceivableReport.setItems(filteredData);
//        calculateTotalAmount();
    }

    private boolean matchesKeyword(RecievableReportDTO item, String keyword, Function<RecievableReportDTO, String> fieldExtractor) {
        String lowerCaseKeyword = keyword.toLowerCase();
        String fieldValue = fieldExtractor.apply(item).toLowerCase();
        return fieldValue.contains(lowerCaseKeyword);
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
        tblcReceivableReportLedger.prefWidthProperty().bind(tblvReceivableReport.widthProperty().multiply(0.17));
        tblcReceivableReportBillNo.prefWidthProperty().bind(tblvReceivableReport.widthProperty().multiply(0.15));
        tblcReceivableReportDate.prefWidthProperty().bind(tblvReceivableReport.widthProperty().multiply(0.08));
        tblcReceivableReportInvoiceAmt.prefWidthProperty().bind(tblvReceivableReport.widthProperty().multiply(0.12));
        tblcReceivableReportPaidAmt.prefWidthProperty().bind(tblvReceivableReport.widthProperty().multiply(0.12));
        tblcReceivableReportBalanceAmt.prefWidthProperty().bind(tblvReceivableReport.widthProperty().multiply(0.12));
        tblcReceivableReportType.prefWidthProperty().bind(tblvReceivableReport.widthProperty().multiply(0.08));
        tblcReceivableReportDueDate.prefWidthProperty().bind(tblvReceivableReport.widthProperty().multiply(0.08));
        tblcReceivableReportOverDueDays.prefWidthProperty().bind(tblvReceivableReport.widthProperty().multiply(0.08));
    }
    private void getReceivableReport(){       //Function to call at start
        receivableReportLogger.info("starting of getPayableReport");
        try{
            Map<String, String> body = new HashMap<>();
            body.put("","");
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.GET_RECEIVABLE_REPORTS);

            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
            fromDate = responseBody.get("startDate").getAsString();
            toDate = responseBody.get("endDate").getAsString();
            LocalDate startLocalDate = LocalDate.parse(fromDate);
            LocalDate endLocalDate = LocalDate.parse(toDate);

            tfReceivableReportFromDate.setText(startLocalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            tfReceivableReportToDate.setText(endLocalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            ObservableList<RecievableReportDTO> observableList = FXCollections.observableArrayList();
            if(responseBody.get("responseStatus").getAsInt() == 200){
                JsonArray jsonArray = responseBody.get("data").getAsJsonArray();
                if(jsonArray.size()>0){
                    for(int i=0;i<jsonArray.size();i++){
                        JsonObject arrItem = jsonArray.get(i).getAsJsonObject();
                        System.out.println("arrItem "+arrItem);
                        String ledgerName = arrItem.get("Ledger_name").getAsString();            //ledger level name  (column:Ledger)
                        String balancingMethod = arrItem.get("balancing_method").getAsString();
                        String ftotal_amt = arrItem.get("total_amount").getAsString();           //ledger level total invoice amount (column: Invoice Amount)
                        String totalPaidAmt = arrItem.get("total_paid_amt").getAsString();       //ledger level total paid amount    (column: Paid Amount)
                        String totalBalance = arrItem.get("total_balance").getAsString();        //ledger level total balance amount  (column: Balance Amount)
                        JsonArray invoiceData = arrItem.get("invoice_data").getAsJsonArray();
                        observableList.add(new RecievableReportDTO(ledgerName,ftotal_amt,totalPaidAmt,totalBalance,"","","","","","","","","",""));
                        tblcReceivableReportLedger.setCellValueFactory(new PropertyValueFactory<>("Ledger_name"));
                        tblcReceivableReportInvoiceAmt.setCellValueFactory(new PropertyValueFactory<>("total_invoice_amount"));
                        tblcReceivableReportPaidAmt.setCellValueFactory(new PropertyValueFactory<>("total_paid_amt"));
                        tblcReceivableReportBalanceAmt.setCellValueFactory(new PropertyValueFactory<>("total_balance_amt"));
                        if(invoiceData.size()>0){
                            for(int j=0;j<invoiceData.size();j++){
                                JsonObject invItem = invoiceData.get(j).getAsJsonObject();
//                                System.out.println("invItem "+invItem);

                                String creditorsId = invItem.get("CreditorsId").getAsString();
                                String dueDate = invItem.get("Due_date").getAsString();            //tranx level due date   (Column: Date)
                                String invoiceNo = invItem.get("Invoice_no").getAsString();        //tranx level bill no    (Column: Bill No)
                                String crName = invItem.get("ledger_name").getAsString();
                                String balance = invItem.get("balance").getAsString();              //tranx level Balance amt (Column: Balance Amount)
                                String balancType = invItem.get("balancing_type").getAsString();
                                String creditDays = invItem.get("credit_days").getAsString();
                                String totalAmount = invItem.get("total_amount").getAsString();     //tranx level Invoice Amount (Column: Invoice Amount)
                                String invoiceDate = invItem.get("invoiceDate").getAsString();      //tranx level Date        (Column: Date)
                                String overDueDays = invItem.get("overDueDays").getAsString();       //tranx level over due days (Column: Over Due Days)
                                String remainingAmt = invItem.get("remaining_amt").getAsString();    //
                                String balancingMethodTranx = invItem.get("balancing_method").getAsString();
                                String paidAmt = invItem.get("paid_amt").getAsString();              //tranx level paid amount for each tranx (Col: Paid Amount)
                                observableList.add(new RecievableReportDTO("","","","",invoiceNo,invoiceDate,totalAmount,
                                        paidAmt,balance,balancingMethodTranx,dueDate,overDueDays,creditorsId,remainingAmt));
                                tblcReceivableReportBillNo.setCellValueFactory(new PropertyValueFactory<>("invoice_no"));
                                tblcReceivableReportDate.setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));
                                tblcReceivableReportInvoiceAmt.setCellValueFactory(new PropertyValueFactory<>("total_amount"));
                                tblcReceivableReportPaidAmt.setCellValueFactory(new PropertyValueFactory<>("paid_amt"));
                                tblcReceivableReportBalanceAmt.setCellValueFactory(new PropertyValueFactory<>("balance"));   //remaining amt is Balance amt of the multiple bills
                                tblcReceivableReportType.setCellValueFactory(new PropertyValueFactory<>("balancing_type"));
                                tblcReceivableReportDueDate.setCellValueFactory(new PropertyValueFactory<>("due_date"));
                                tblcReceivableReportOverDueDays.setCellValueFactory(new PropertyValueFactory<>("overDueDays"));
//                                tblvReceivableReport.setItems(observableList);
                            }
                        }

                    }
                    tblvReceivableReport.setItems(observableList);
                    originalData = observableList;
                    totalCalculation();
                }
            }
        }catch (Exception e){
            receivableReportLogger.error("error in getting payable report "+e.getMessage());
        }

    }
    //function for get the receivable list with date
    private void getReceivableReportWithDate(){       //Function to call at start
        receivableReportLogger.info("starting of getPayableReport");
        try{
            System.out.println("fDay "+tfReceivableReportFromDate.getText()+"  lDay "+tfReceivableReportToDate.getText());
            Map<String, String> body = new HashMap<>();
            body.put("start_date", Communicator.text_to_date.fromString(tfReceivableReportFromDate.getText()).toString());
            body.put("end_date",Communicator.text_to_date.fromString(tfReceivableReportToDate.getText()).toString());
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.GET_RECEIVABLE_REPORTS);

            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("res in receivable report "+responseBody);
            ObservableList<RecievableReportDTO> observableList = FXCollections.observableArrayList();

            if(responseBody.get("responseStatus").getAsInt() == 200){
                JsonArray jsonArray = responseBody.get("data").getAsJsonArray();
                if(jsonArray.size()>0){
                    for(int i=0;i<jsonArray.size();i++){
                        JsonObject arrItem = jsonArray.get(i).getAsJsonObject();
                        System.out.println("arrItem "+arrItem);
                        String ledgerName = arrItem.get("Ledger_name").getAsString();
                        String balancingMethod = arrItem.get("balancing_method").getAsString();
                        String ftotal_amt = arrItem.get("total_amount").getAsString();
                        String totalBalance = arrItem.get("total_balance").getAsString();
                        String totalPaidAmt = arrItem.get("total_paid_amt").getAsString();
                        JsonArray invoiceData = arrItem.get("invoice_data").getAsJsonArray();
                        observableList.add(new RecievableReportDTO(ledgerName,ftotal_amt,totalPaidAmt,totalBalance,"","","","","","","","","",""));
                        tblcReceivableReportLedger.setCellValueFactory(new PropertyValueFactory<>("Ledger_name"));
                        tblcReceivableReportInvoiceAmt.setCellValueFactory(new PropertyValueFactory<>("total_invoice_amount"));
                        tblcReceivableReportPaidAmt.setCellValueFactory(new PropertyValueFactory<>("total_paid_amt"));
                        tblcReceivableReportBalanceAmt.setCellValueFactory(new PropertyValueFactory<>("total_balance_amt"));

                        if(invoiceData.size()>0){
                            for(int j=0;j<invoiceData.size();j++){
                                JsonObject invItem = invoiceData.get(j).getAsJsonObject();
                                System.out.println("invItem "+invItem);

                                String creditorsId = invItem.get("CreditorsId").getAsString();
                                String dueDate = invItem.get("Due_date").getAsString();
                                String invoiceNo = invItem.get("Invoice_no").getAsString();
                                String balance = invItem.get("balance").getAsString();
                                String balancType = invItem.get("balancing_type").getAsString();
                                String creditDays = invItem.get("credit_days").getAsString();
                                String totalAmount = invItem.get("total_amount").getAsString();
                                String invoiceDate = invItem.get("invoiceDate").getAsString();
                                String overDueDays = invItem.get("overDueDays").getAsString();
                                String remainingAmt = invItem.get("remaining_amt").getAsString();
                                String balancingMethodTranx = invItem.get("balancing_method").getAsString();
                                String paidAmt = invItem.get("paid_amt").getAsString();
                                observableList.add(new RecievableReportDTO("","","","",invoiceNo,invoiceDate,totalAmount,
                                        paidAmt,balance,balancingMethodTranx,dueDate,overDueDays,creditorsId,remainingAmt));
                                tblcReceivableReportBillNo.setCellValueFactory(new PropertyValueFactory<>("invoice_no"));
                                tblcReceivableReportDate.setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));
                                tblcReceivableReportInvoiceAmt.setCellValueFactory(new PropertyValueFactory<>("total_amount"));
                                tblcReceivableReportPaidAmt.setCellValueFactory(new PropertyValueFactory<>("paid_amt"));
                                tblcReceivableReportBalanceAmt.setCellValueFactory(new PropertyValueFactory<>("balance"));   //remaining amt is Balance amt of the multiple bills
                                tblcReceivableReportType.setCellValueFactory(new PropertyValueFactory<>("balancing_type"));
                                tblcReceivableReportDueDate.setCellValueFactory(new PropertyValueFactory<>("due_date"));
                                tblcReceivableReportOverDueDays.setCellValueFactory(new PropertyValueFactory<>("overDueDays"));
//                                tblvReceivableReport.setItems(observableList);
                            }
                        }
                    }
                }
                tblvReceivableReport.setItems(observableList);
                originalData = observableList;
                totalCalculation();
                cbBalanceMethod.requestFocus();
            }
        }catch (Exception e){
            receivableReportLogger.error("error in getting payable report "+e.getMessage());
        }
    }
    //function for bottom calculation total invoice amt, paid amt, balance amt
    private void totalCalculation(){
        ObservableList<RecievableReportDTO>  observableList = tblvReceivableReport.getItems();
        double totalInvoiceAmount = 0.0;
        double totalPaidAmount = 0.0;
        double totalBalanceAmount = 0.0;

        for(RecievableReportDTO item : observableList){
            totalInvoiceAmount += Double.parseDouble(item.getTotal_amount().isEmpty() ? "0.0" : item.getTotal_amount());
            totalPaidAmount += Double.parseDouble(item.getPaid_amt().isEmpty() ? "0.0" : item.getPaid_amt());
            totalBalanceAmount += Double.parseDouble(item.getBalance().isEmpty() ? "0.0" : item.getBalance());
        }
        System.out.println("invAmt "+totalInvoiceAmount+" paidAmt "+totalPaidAmount+" balAmt "+totalBalanceAmount);
        lblTotalInvoiceAmt.setText(String.format("%.2f", totalInvoiceAmount));
        lblTotalPaidAmt.setText(String.format("%.2f",totalPaidAmount));
        lblTotalBalanceAmt.setText(String.format("%.2f", totalBalanceAmount));
    }
    //function for set the filter type
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
        filterReceivableByBalancingMethod();

    }

    //for filter the data by balancing method
    private void filterReceivableByBalancingMethod() {
        ObservableList<RecievableReportDTO> filteredData = FXCollections.observableArrayList();
        if (selectedBalancingMethod == "BillByBill") {
            filteredData = originalData.stream().filter((item -> item.getBalancing_method().equalsIgnoreCase("1"))).collect(Collectors.toCollection(FXCollections::observableArrayList));
        }if (selectedBalancingMethod == "OnAccount") {
            filteredData = originalData.stream().filter((item -> item.getBalancing_method().equalsIgnoreCase("2"))).collect(Collectors.toCollection(FXCollections::observableArrayList));
        }
        else {
            filteredData = originalData.stream().collect(Collectors.toCollection(FXCollections::observableArrayList));
        }

        tblvReceivableReport.setItems(filteredData);
        totalCalculation();
    }
    //function for export to excel
    private void exportToExcelForReceivable() {
        ObservableList<RecievableReportDTO> data = tblvReceivableReport.getItems();

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
                Sheet sheet = workbook.createSheet("Receivable Report");

                // Create font for header
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setFontHeightInPoints((short) 12);
                CellStyle headerCellStyle = workbook.createCellStyle();
                // Create cell style with bold header font
                headerCellStyle.setFont(headerFont);

                // Create header row
                Row headerRow = sheet.createRow(0);

                // Populate header row
                String[] headers = getColumnHeaders();

                for (int colNum = 0; colNum < headers.length; colNum++) {
                    Cell headerCell = headerRow.createCell(colNum);
                    headerCell.setCellValue(headers[colNum]);
                    headerCell.setCellStyle(headerCellStyle);
                }
                Double totalInvoiceAmount = 0.0;
                Double totalPaidAmount = 0.0;
                Double totalBalanceAmount = 0.0;
                // Populate data rows
                int rowNum = 1;
                int srNo =0;
                for (RecievableReportDTO dto : data) {
                    Row row = sheet.createRow(rowNum++);
//                    srNo = srNo +1;
//                    row.createCell(0).setCellValue(srNo);
                    row.createCell(0).setCellValue(dto.getLedger_name());
                    row.createCell(1).setCellValue(dto.getInvoice_no());
                    row.createCell(2).setCellValue(dto.getInvoiceDate());
                    row.createCell(3).setCellValue(dto.getTotal_amount());
                    row.createCell(4).setCellValue(dto.getPaid_amt());
                    row.createCell(5).setCellValue(dto.getBalance());
                    row.createCell(6).setCellValue(dto.getBalancing_method());
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
        ObservableList<TableColumn<RecievableReportDTO, ?>> columns = tblvReceivableReport.getColumns();
        String[] headers = {"Ledger Name", "Bill No", "Date", "Invoice Amt", "Paid Amt", "Balance Amt", "Type", "Due Date", "Over Due Days"};
        System.out.println("headers.length-- "+headers.length);
        for (int i = 0; i < headers.length; i++) {
            headers[i] = columns.get(i).getText();
        }
        return headers;
    }
    //function for cursot focus on enter
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

}
