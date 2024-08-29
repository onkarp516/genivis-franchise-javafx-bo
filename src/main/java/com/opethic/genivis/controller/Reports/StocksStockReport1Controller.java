package com.opethic.genivis.controller.Reports;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.controller.commons.SwitchButton;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.dto.GSTR1.GSTR1B2B1DTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.GlobalController;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.opethic.genivis.utils.FxmFileConstants.ACCOUNTS_LEDGER_REPORT2_LIST_SLUG;
import static com.opethic.genivis.utils.FxmFileConstants.STOCKS_STOCK_REPORT2_LIST_SLUG;

public class StocksStockReport1Controller implements Initializable {
    private static final Logger StockReport1ListLogger = LoggerFactory.getLogger(StocksStockReport1Controller.class);
    @FXML
    private TextField tfStockReport1ListSearch;
    @FXML
    private ComboBox<String> cbFilter;
    @FXML
    private Button btExportPdf, btExportExcel, btExportCsv, btExportPrint;
    @FXML
    private CheckBox cbBatch, cbNonBatch, cbScheduleH, cbScheduleH1, cbNarcotics;
    private boolean batchSelected = false, nonBatchSelected = false, scheduleHSelected = false, scheduleH1Selected = false, narcoticsSelected = false, isGvProduct = false, showNearExpiryStock = false, showExpiredStock= false;
    @FXML
    private SwitchButton sbNegativeStock, sbNearExpiry, sbExpired;
    @FXML
    private VBox vboxNevStk, vboxNrExp, vboxExp;
    String selectedFilter = "";
    @FXML
    private TableView<StockReport1DTO> tblvStockReport1List;
    @FXML
    private TableColumn<StockReport1DTO, String> tblStockReport1Code, tblStockReport1ProductName, tblStockReport1Packing,
            tblStockReport1Brand, tblStockReport1Batch, tblStockReport1ExpiryDate, tblStockReport1Unit, tblStockReport1Quantity;
    @FXML
    private TableView<StockReport1DTOFilter> tblvStockReport1List1;
    @FXML
    private TableColumn<StockReport1DTOFilter, String> tblStockReport1Code1, tblStockReport1ProductName1, tblStockReport1Packing1,
            tblStockReport1Brand1, tblStockReport1Batch1, tblStockReport1ExpiryDate1, tblStockReport1Unit1, tblStockReport1Quantity1;

    private ObservableList<StockReport1DTO> originalData;
    private ObservableList<StockReport1DTOFilter> originalData1;

    private JsonObject jsonObject = null;
    @FXML
    private BorderPane mainBorderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sbNegativeStock.setParentBox(vboxNevStk);
        sbNearExpiry.setParentBox(vboxNrExp);
        sbExpired.setParentBox(vboxExp);

        tblvStockReport1List.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        StocksStockReport1TableDesign();

        tblvStockReport1List1.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        StocksStockReport1Table1Design();

        tblvStockReport1List.setVisible(true);
        tblvStockReport1List.setManaged(true);

        tblvStockReport1List1.setVisible(false);
        tblvStockReport1List1.setManaged(false);

        StockReport1ListLogger.info("Start of Initialize method of : StocksStockReport1Controller");
        Platform.runLater(() -> tfStockReport1ListSearch.requestFocus());
        fetchStockReport1List("");

        mainBorderPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.DOWN && tfStockReport1ListSearch.isFocused()){
                tblvStockReport1List.getSelectionModel().select(0);
                tblvStockReport1List.requestFocus();
            }
        });

        // on double click go to next page from Without-filter data
        tblvStockReport1List.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                StockReport1DTO selectedItem = (StockReport1DTO) tblvStockReport1List.getSelectionModel().getSelectedItem();
                String id = selectedItem.getId();
                String product_name = selectedItem.getProduct_name();
                StocksStockReport2Controller.product_name = product_name;
                System.out.println(product_name);
                System.out.println(id);
                GlobalController.getInstance().addTabStatic1(STOCKS_STOCK_REPORT2_LIST_SLUG, false, Integer.valueOf(id));
            }
        });

        // on double click go to next page from filter data
        tblvStockReport1List1.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                StockReport1DTOFilter  selectedItem = (StockReport1DTOFilter) tblvStockReport1List1.getSelectionModel().getSelectedItem();
                String id = selectedItem.getId();
                String product_name = selectedItem.getProduct_name();
                StocksStockReport2Controller.product_name = product_name;
                System.out.println(product_name);
                System.out.println(id);
                GlobalController.getInstance().addTabStatic1(STOCKS_STOCK_REPORT2_LIST_SLUG, false, Integer.valueOf(id));
            }
        });

        // onAction go to next page from Without-filter data
        tblvStockReport1List.addEventFilter(KeyEvent.ANY, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                StockReport1DTO selectedItem = (StockReport1DTO) tblvStockReport1List.getSelectionModel().getSelectedItem();
                String id = selectedItem.getId();
                String product_name = selectedItem.getProduct_name();
                StocksStockReport2Controller.product_name = product_name;
                System.out.println(product_name);
                System.out.println(id);
                GlobalController.getInstance().addTabStatic1(STOCKS_STOCK_REPORT2_LIST_SLUG, false, Integer.valueOf(id));
            }
        });

        // onAction go to next page from filter data
        tblvStockReport1List1.addEventFilter(KeyEvent.ANY, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                StockReport1DTOFilter selectedItem = (StockReport1DTOFilter) tblvStockReport1List1.getSelectionModel().getSelectedItem();
                String id = selectedItem.getId();
                String product_name = selectedItem.getProduct_name();
                StocksStockReport2Controller.product_name = product_name;
                System.out.println(product_name);
                System.out.println(id);
                GlobalController.getInstance().addTabStatic1(STOCKS_STOCK_REPORT2_LIST_SLUG, false, Integer.valueOf(id));
            }
        });

        originalData = tblvStockReport1List.getItems();
//        originalData1 = tblvStockReport1List1.getItems();

        cbFilter.getItems().addAll("Product", "Brand", "Packaging", "Content", "Rate", "Company", "Group", "Subgroup");
        cbFilter.setPromptText("Filter Search");
        cbFilter.setOnAction(this::handleFilterComboBoxAction);
        cbFilter.setValue("Product");
        handleFilterComboBoxAction(null);
        //todo:open Filter dropdown on Space
        cbFilter.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cbFilter.isShowing()) {
                cbFilter.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });

        cbBatch.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelectedBatch) -> {
            batchSelected = isNowSelectedBatch;
            if (isNowSelectedBatch) {
                filterBatchListByFilter();
            } else {
                filterBatchListByFilter();
            }
        });
        cbNonBatch.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelectedNonBatch) -> {
            nonBatchSelected = isNowSelectedNonBatch;
            if (isNowSelectedNonBatch) {
                filterBatchListByFilter();
            } else {
                filterBatchListByFilter();
            }
        });

        cbScheduleH.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelectedScheduleH) -> {
            scheduleHSelected = isNowSelectedScheduleH;
            if (isNowSelectedScheduleH) {
                filterDrugTypeListByFilter();
            } else {
                filterDrugTypeListByFilter();
            }
        });
        cbScheduleH1.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelectedScheduleH1) -> {
            scheduleH1Selected = isNowSelectedScheduleH1;
            if (isNowSelectedScheduleH1) {
                filterDrugTypeListByFilter();
            } else {
                filterDrugTypeListByFilter();
            }
        });
        cbNarcotics.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelectedNarcotics) -> {
            narcoticsSelected = isNowSelectedNarcotics;
            if (isNowSelectedNarcotics) {
                filterDrugTypeListByFilter();
            } else {
                filterDrugTypeListByFilter();
            }
        });

        sbNegativeStock.switchOnProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
//                System.out.println("true--------->");
                tblvStockReport1List.setVisible(false);
                tblvStockReport1List.setManaged(false);

                tblvStockReport1List1.setVisible(true);
                tblvStockReport1List1.setManaged(true);
                filterStockListByNegativeStock(newValue);
            } else {
                tblvStockReport1List.setVisible(true);
                tblvStockReport1List.setManaged(true);

                tblvStockReport1List1.setVisible(false);
                tblvStockReport1List1.setManaged(false);
//                System.out.println("false--------->");
            }
        });

        sbNearExpiry.switchOnProperty().addListener((observable, oldValue, newValue) -> {
            showNearExpiryStock = newValue;
            updateListVisibility();
            filterStockListByExpiryConditions();
        });
        sbExpired.switchOnProperty().addListener((observable, oldValue, newValue) -> {
            showExpiredStock = newValue;
            updateListVisibility();
            filterStockListByExpiryConditions();
        });

        nodetraversal(cbFilter, tfStockReport1ListSearch);
        nodetraversal(tfStockReport1ListSearch, cbBatch);
        nodetraversal(cbBatch, cbNonBatch);
        nodetraversalForNodeToToggle(cbNonBatch, sbNegativeStock);
        nodetraversalForToggleToTogle(sbNegativeStock, sbNearExpiry);
        nodetraversalForToggleToTogle(sbNearExpiry, sbExpired);
        nodetraversalForToggleToNode(sbExpired, cbScheduleH);
        nodetraversal(cbScheduleH, cbScheduleH1);
        nodetraversal(cbScheduleH1, cbNarcotics);
        nodetraversal(cbNarcotics, btExportPdf);
        nodetraversal(btExportPdf, btExportExcel);
        nodetraversal(btExportExcel, btExportCsv);
        nodetraversal(btExportCsv, btExportPrint);
        tblvStockReport1List1.requestFocus();



        btExportPdf.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                btExportPdf.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc; -fx-background-color: transparent;");
            } else {
                btExportPdf.setStyle("-fx-background-color: transparent;");
            }
        });
        btExportPdf.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                btExportPdf.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc; -fx-background-color: transparent;");
            } else {
                btExportPdf.setStyle("-fx-background-color: transparent;");
            }
        });

        btExportExcel.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                btExportExcel.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc; -fx-background-color: transparent;");
            } else {
                btExportExcel.setStyle("-fx-background-color: transparent;");
            }
        });
        btExportExcel.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                btExportExcel.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc; -fx-background-color: transparent;");
            } else {
                btExportExcel.setStyle("-fx-background-color: transparent;");
            }
        });

        btExportCsv.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                btExportCsv.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc; -fx-background-color: transparent;");
            } else {
                btExportCsv.setStyle("-fx-background-color: transparent;");
            }
        });
        btExportCsv.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                btExportCsv.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc; -fx-background-color: transparent;");
            } else {
                btExportCsv.setStyle("-fx-background-color: transparent;");
            }
        });

        btExportPrint.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                btExportPrint.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc; -fx-background-color: transparent;");
            } else {
                btExportPrint.setStyle("-fx-background-color: transparent;");
            }
        });
        btExportPrint.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                btExportPrint.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc; -fx-background-color: transparent;");
            } else {
                btExportPrint.setStyle("-fx-background-color: transparent;");
            }
        });
        btExportExcel.setOnAction(event->{
            if(tblvStockReport1List.isVisible()){
                exportToExcel();
            }
            else if(tblvStockReport1List1.isVisible()){
                exportToExcel1();
            }

        });
    }

    //excelExport code
    private void exportToExcel() {
        ObservableList<StockReport1DTO> data = tblvStockReport1List.getItems();

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Excel File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
            );
            File file = fileChooser.showSaveDialog(new Stage());
            if (file != null) {
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Data");

                // Create font for header
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setFontHeightInPoints((short) 12);
                CellStyle headerCellStyle = workbook.createCellStyle();
//                headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
//                headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
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

                // Populate data rows
                int rowNum = 1;
                for (StockReport1DTO dto : data) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(dto.getCode());
                    row.createCell(1).setCellValue(dto.getProduct_name());
                    row.createCell(2).setCellValue(dto.getPacking());
                    row.createCell(3).setCellValue(dto.getBrand());
                    row.createCell(4).setCellValue(dto.getBatch_no());
                    row.createCell(5).setCellValue(dto.getExpiry_date());
                    row.createCell(6).setCellValue(dto.getUnit_name());
                    row.createCell(7).setCellValue(dto.getUnit_qty());


                }

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
    private void exportToExcel1() {
        ObservableList<StockReport1DTOFilter> data = tblvStockReport1List1.getItems();

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Excel File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
            );
            File file = fileChooser.showSaveDialog(new Stage());
            if (file != null) {
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Data");

                // Create font for header
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setFontHeightInPoints((short) 12);
                CellStyle headerCellStyle = workbook.createCellStyle();
//                headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
//                headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
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

                // Populate data rows
                int rowNum = 1;
                for (StockReport1DTOFilter dto : data) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(dto.getCode());
                    row.createCell(1).setCellValue(dto.getProduct_name());
                    row.createCell(2).setCellValue(dto.getPacking());
                    row.createCell(3).setCellValue(dto.getBrand());
                    row.createCell(4).setCellValue(dto.getBatch_no());
                    row.createCell(5).setCellValue(dto.getExpiry_date());
                    row.createCell(6).setCellValue(dto.getUnit_name());
                    row.createCell(7).setCellValue(dto.getUnit_qty());

                }
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
        if(tblvStockReport1List.isVisible()){
            ObservableList<TableColumn<StockReport1DTO, ?>> columns = tblvStockReport1List.getColumns();
            String[] headers = {"Code ", "Product Name", "Packing","Brand", "Batch", "Expiry Date", "Unit", "Quantity"};
            for (int i = 0; i < headers.length; i++) {
                headers[i] = columns.get(i).getText();
            }
            return headers;
        }
        else if(tblvStockReport1List1.isVisible()){
            ObservableList<TableColumn<StockReport1DTOFilter, ?>> columns = tblvStockReport1List1.getColumns();
            String[] headers = {"Code ", "Product Name", "Packing","Brand", "Batch", "Expiry Date", "Unit", "Quantity"};
            for (int i = 0; i < headers.length; i++) {
                headers[i] = columns.get(i).getText();
            }
            return headers;
        }
//        ObservableList<TableColumn<GSTR1B2B1DTO, ?>> columns = tblvB2bScreen1List.getColumns();
//        String[] headers = {"Sr.No", "Particulars", "GSTIN/UIN","Voucher count", "Taxable Amt", "Integrated Tax Amt", "Central Tax Amt", "State Tax Amt", "Cess Amt", "Tax Amt", "Invoice Amt"};
//        for (int i = 0; i < headers.length; i++) {
//            headers[i] = columns.get(i).getText();
//        }
        else {
            String[] headers = {"Code ", "Product Name", "Packing", "Brand", "Batch", "Expiry Date", "Unit", "Quantity"};
            return headers;
        }
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

    private void nodetraversalForNodeToToggle(Node current_node, SwitchButton next_node) {
        current_node.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                next_node.requestButtonFocus();
                event.consume();
            }

            if (event.getCode() == KeyCode.SPACE) {
                if (current_node instanceof Button button) {
                    button.fire();
                }

            } else if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
                if (current_node instanceof RadioButton radioButton) {
                    radioButton.setSelected(!radioButton.isSelected());
                    radioButton.fire();
                }
            }
        });
    }

    private void nodetraversalForToggleToTogle(SwitchButton current_node, SwitchButton next_node) {
        current_node.getSwitchBtn().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                next_node.requestButtonFocus();
                event.consume();
            }
        });
    }

    private void nodetraversalForToggleToNode(SwitchButton current_node, Node next_node) {
        current_node.getSwitchBtn().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                next_node.requestFocus();
                event.consume();
            }
        });
    }


    public void StocksStockReport1TableDesign(){
        //todo: Responsive code for tableview
        tblStockReport1Code.prefWidthProperty().bind(tblvStockReport1List.widthProperty().multiply(0.07));
        tblStockReport1ProductName.prefWidthProperty().bind(tblvStockReport1List.widthProperty().multiply(0.38));
        tblStockReport1Packing.prefWidthProperty().bind(tblvStockReport1List.widthProperty().multiply(0.08));
        tblStockReport1Brand.prefWidthProperty().bind(tblvStockReport1List.widthProperty().multiply(0.08));
        tblStockReport1Batch.prefWidthProperty().bind(tblvStockReport1List.widthProperty().multiply(0.09));
        tblStockReport1ExpiryDate.prefWidthProperty().bind(tblvStockReport1List.widthProperty().multiply(0.07));
        tblStockReport1Unit.prefWidthProperty().bind(tblvStockReport1List.widthProperty().multiply(0.12));
        tblStockReport1Quantity.prefWidthProperty().bind(tblvStockReport1List.widthProperty().multiply(0.07));
    }
    public void StocksStockReport1Table1Design(){
        //todo: Responsive code for tableview
        tblStockReport1Code1.prefWidthProperty().bind(tblvStockReport1List1.widthProperty().multiply(0.07));
        tblStockReport1ProductName1.prefWidthProperty().bind(tblvStockReport1List1.widthProperty().multiply(0.38));
        tblStockReport1Packing1.prefWidthProperty().bind(tblvStockReport1List1.widthProperty().multiply(0.08));
        tblStockReport1Brand1.prefWidthProperty().bind(tblvStockReport1List1.widthProperty().multiply(0.08));
        tblStockReport1Batch1.prefWidthProperty().bind(tblvStockReport1List1.widthProperty().multiply(0.09));
        tblStockReport1ExpiryDate1.prefWidthProperty().bind(tblvStockReport1List1.widthProperty().multiply(0.07));
        tblStockReport1Unit1.prefWidthProperty().bind(tblvStockReport1List1.widthProperty().multiply(0.12));
        tblStockReport1Quantity1.prefWidthProperty().bind(tblvStockReport1List1.widthProperty().multiply(0.07));
    }

    private void updateListVisibility() {
        boolean anyFilterActive = showNearExpiryStock || showExpiredStock;
        tblvStockReport1List.setVisible(!anyFilterActive);
        tblvStockReport1List.setManaged(!anyFilterActive);

        tblvStockReport1List1.setVisible(anyFilterActive);
        tblvStockReport1List1.setManaged(anyFilterActive);
    }

    private void filterStockListByExpiryConditions() {
        ObservableList<StockReport1DTOFilter> filteredList = FXCollections.observableArrayList();
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (StockReport1DTOFilter item : originalData1) {
            String expiryDateString = item.getExpiry_date();
            if (expiryDateString != null && !expiryDateString.isEmpty()) {
                try {
                    LocalDate expiryDate = LocalDate.parse(expiryDateString, formatter);
                    long daysDifference = ChronoUnit.DAYS.between(currentDate, expiryDate);

                    // Check for near expiry condition
                    if (daysDifference <= 15 && daysDifference >= 0 && showNearExpiryStock) {
                        filteredList.add(item);
                    }
                    // Check for expired condition
                    else if (daysDifference < 0 && showExpiredStock) {
                        filteredList.add(item);
                    }
                } catch (DateTimeParseException e) {
                    System.err.println("Error parsing date: " + expiryDateString);
                }
            }
        }

//        tblvStockReport1List1.setItems(!filteredList.isEmpty() ? originalData1 :filteredList);
        tblvStockReport1List1.setItems(filteredList);
    }


    private void filterStockListByNegativeStock(boolean showNegativeStock) {
        ObservableList<StockReport1DTOFilter> filteredList = FXCollections.observableArrayList();
        boolean errorOccurred = false;

        if (showNegativeStock) {
            for (StockReport1DTOFilter item : originalData1) {
                String closingStockString = item.getClosing_stock();
                // Check if closing stock is not null and not empty
                if (closingStockString != null && !closingStockString.isEmpty()) {
                    try {
                        // Attempt to parse the closing stock string to a double
                        double closingStock = Double.parseDouble(closingStockString);
                        if (closingStock < 0.0) {
                            // Add the item to the filtered list if closing stock is negative
                            filteredList.add(item);
                        }
                    } catch (NumberFormatException e) {
                        // Handle the case where the closing stock is not a valid number
//                        System.err.println("Error parsing closing stock as a double: " + closingStockString);
                        errorOccurred = true;
                    }
                } else {
                    // Handle the case where the closing stock is null or empty
//                    System.err.println("Closing stock is null or empty");
                    errorOccurred = true;
                }
            }
        } else {
            // If showNegativeStock is false, add all items to the filtered list
            filteredList.addAll(originalData1);
        }
//        tblvStockReport1List1.setVisible(true);
        tblvStockReport1List1.setItems(filteredList);
    }

    private void filterBatchListByFilter() {
        ObservableList<StockReport1DTO> filteredList = FXCollections.observableArrayList();

        for (StockReport1DTO item : originalData) {
            boolean batchMatch = cbBatch.isSelected() && !item.getBatch_no().isEmpty();
            boolean nonBatchMatch = cbNonBatch.isSelected() && item.getBatch_no().isEmpty();

            if (batchMatch || nonBatchMatch) {
                filteredList.add(item);
            }
        }

        if(batchSelected && !nonBatchSelected){
            tblvStockReport1List.setItems(filteredList);
        } else if (!batchSelected && nonBatchSelected){
            tblvStockReport1List.setItems(filteredList);
        } else if (batchSelected && nonBatchSelected){
            tblvStockReport1List.setItems(filteredList);
        } else  {
            tblvStockReport1List.setItems(originalData);
        }
    }

    private void filterDrugTypeListByFilter() {
        ObservableList<StockReport1DTO> filteredList = FXCollections.observableArrayList();
        for (StockReport1DTO item : originalData) {
            boolean scheduleHMatch = cbScheduleH.isSelected() && item.getDrug_type().equalsIgnoreCase("1");
            boolean scheduleH1Match = cbScheduleH1.isSelected() && item.getDrug_type().equalsIgnoreCase("2");
            boolean narcoticsMatch = cbNarcotics.isSelected() && item.getDrug_type().equalsIgnoreCase("3");

            if (scheduleHMatch || scheduleH1Match || narcoticsMatch) {
                filteredList.add(item);
            }
        }

        if(scheduleHSelected && !scheduleH1Selected && !narcoticsSelected){
            tblvStockReport1List.setItems(filteredList);
        } else if(!scheduleHSelected && scheduleH1Selected && !narcoticsSelected){
            tblvStockReport1List.setItems(filteredList);
        } else if(!scheduleHSelected && !scheduleH1Selected && narcoticsSelected){
            tblvStockReport1List.setItems(filteredList);
        } else if(scheduleHSelected && scheduleH1Selected && !narcoticsSelected){
            tblvStockReport1List.setItems(filteredList);
        } else if(scheduleHSelected && !scheduleH1Selected && narcoticsSelected){
            tblvStockReport1List.setItems(filteredList);
        } else if(!scheduleHSelected && scheduleH1Selected && narcoticsSelected){
            tblvStockReport1List.setItems(filteredList);
        } else if(scheduleHSelected && scheduleH1Selected && narcoticsSelected){
            tblvStockReport1List.setItems(filteredList);
        }  else {
            tblvStockReport1List.setItems(originalData);
        }
    }

    private void handleFilterComboBoxAction(ActionEvent actionEvent) {
        System.out.println("actionEvent " + actionEvent);
        selectedFilter = cbFilter.getSelectionModel().getSelectedItem();

        System.out.println("Selected Filter: " + selectedFilter);
        if (cbFilter.getSelectionModel().getSelectedItem() == "Product") {
            selectedFilter = "product";
        } else if (cbFilter.getSelectionModel().getSelectedItem() == "Brand") {
            selectedFilter = "brand";
        } else if (cbFilter.getSelectionModel().getSelectedItem() == "Packaging") {
            selectedFilter = "packaging";
        } else if (cbFilter.getSelectionModel().getSelectedItem() == "Content") {
            selectedFilter = "content";
        } else if (cbFilter.getSelectionModel().getSelectedItem() == "Rate") {
            selectedFilter = "rate";
        } else if (cbFilter.getSelectionModel().getSelectedItem() == "Company") {
            selectedFilter = "company";
        } else if (cbFilter.getSelectionModel().getSelectedItem() == "Group") {
            selectedFilter = "group";
        } else if (cbFilter.getSelectionModel().getSelectedItem() == "Subgroup") {
            selectedFilter = "subgroup";
        }
        fetchStockReport1ListByFilterSearch();
    }

    private void fetchStockReport1ListByFilterSearch() {
        ObservableList<StockReport1DTOFilter> filteredData = FXCollections.observableArrayList();
        tfStockReport1ListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null && !newValue.equals("")) {
//                System.out.println("true--------->");
                tblvStockReport1List.setVisible(false);
                tblvStockReport1List.setManaged(false);

                tblvStockReport1List1.setVisible(true);
                tblvStockReport1List1.setManaged(true);

//                    filterData(newValue.trim(), StockReport1DTOFilter::getProduct_name);
            } else {
                tblvStockReport1List.setVisible(true);
                tblvStockReport1List.setManaged(true);

                tblvStockReport1List1.setVisible(false);
                tblvStockReport1List1.setManaged(false);
//                System.out.println("false--------->");
            }
        });
        if (selectedFilter.equals("product")) {
            tfStockReport1ListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
//                System.out.println("newValue in product search " + newValue);
                filterData(newValue.trim(), StockReport1DTOFilter::getProduct_name);
            });
        } else if (selectedFilter.equals("brand")) {
            tfStockReport1ListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
//                System.out.println("newValue in brand search " + newValue);
                filterData(newValue.trim(), StockReport1DTOFilter::getBrand);
            });
        } else if (selectedFilter.equals("packaging")) {
            tfStockReport1ListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
//                System.out.println("newValue in packaging search " + newValue);
                filterData(newValue.trim(), StockReport1DTOFilter::getPacking);
            });
        } else if (selectedFilter.equals("content")) {
            tfStockReport1ListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData(newValue.trim(), StockReport1DTOFilter::getDrug_content);
            });
        } else if (selectedFilter.equals("rate")) {
            tfStockReport1ListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData(newValue.trim(), StockReport1DTOFilter::getRate);
            });
        } else if (selectedFilter.equals("company")) {
            tfStockReport1ListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData(newValue.trim(), StockReport1DTOFilter::getCompany);
            });
        } else if (selectedFilter.equals("group")) {
            tfStockReport1ListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData(newValue.trim(), StockReport1DTOFilter::getGroup);
            });
        } else if (selectedFilter.equals("subgroup")) {
            tfStockReport1ListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData(newValue.trim(), StockReport1DTOFilter::getSub_group);
            });
        }
    }

    private void filterData(String keyword, Function<StockReport1DTOFilter, String> fieldExtractor) {
        ObservableList<StockReport1DTOFilter> filteredData = FXCollections.observableArrayList();
        StockReport1ListLogger.error("Search Stock Report 1 List in StocksStockReport1Controller");
        if (keyword.isEmpty()) {
            tblvStockReport1List1.setItems(originalData1);
            return;
        }
        for (StockReport1DTOFilter item : originalData1) {
            if (matchesKeyword(item, keyword, fieldExtractor) ) {
                filteredData.add(item);
            }
        }
        tblvStockReport1List1.setItems(filteredData);
    }

    private boolean matchesKeyword(StockReport1DTOFilter item, String keyword, Function<StockReport1DTOFilter, String> fieldExtractor) {
        String lowerCaseKeyword = keyword.toLowerCase();
        String fieldValue = fieldExtractor.apply(item).toLowerCase();
        return fieldValue.contains(lowerCaseKeyword);
    }

    private void fetchStockReport1List(String searchKey) {
        StockReport1ListLogger.info("fetch Stock Reports 1 List : StocksStockReport1Controller");

        APIClient apiClient = null;

        try {
            StockReport1ListLogger.debug("Get Stock Reports 1 Data Started...");

            Map<String, String> body = new HashMap<>();
            String requestBody = Globals.mapToStringforFormData(body);
//            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.STOCKS_STOCK_REPORT1_LIST);
            apiClient = new APIClient(EndPoints.STOCKS_STOCK_REPORT1_LIST, requestBody, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
//                    JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//            System.out.println("StockReport1_list" + responseBody);
                    ObservableList<StockReport1DTOFilter> observableList1 = FXCollections.observableArrayList();
                    ObservableList<StockReport1DTO> observableList = FXCollections.observableArrayList();
                    if(jsonObject.get("responseStatus").getAsInt() == 200){
                        JsonArray responseList = jsonObject.getAsJsonArray("data");
//                System.out.println("StockReport1_list" + responseList);

                        JsonObject mainjson = new JsonObject();

                        if (responseList.size() > 0) {
                            tblvStockReport1List.setVisible(true);

                            for (int i = 0; i < responseList.size(); i++){
                                mainjson = responseList.get(i).getAsJsonObject();

                                if(mainjson != null) {
                                    JsonArray productUnitData = new JsonArray();
                                    String id = mainjson.get("id") != null ? mainjson.get("id").getAsString() : "";
                                    String code = mainjson.get("item_code") != null ? mainjson.get("item_code").getAsString() : "";
                                    String product_name = mainjson.get("product_name") != null ? mainjson.get("product_name").getAsString() : "";
                                    String drug_type = mainjson.get("drugType") != null ? mainjson.get("drugType").getAsString() : "";
                                    String packing = mainjson.get("packaging") != null ? mainjson.get("packaging").getAsString() : "";
                                    String brand = mainjson.get("brand_name") != null ? mainjson.get("brand_name").getAsString() : "";
                                    String drug_content = mainjson.get("drug_content") != null ? mainjson.get("drug_content").getAsString() : "";
                                    String rate = mainjson.get("Product_clo_rate") != null ? mainjson.get("Product_clo_rate").getAsString() : "";
                                    String company = mainjson.get("companyName") != null ? mainjson.get("companyName").getAsString() : "";
                                    String group = mainjson.get("group") != null ? mainjson.get("group").getAsString() : "";
                                    String sub_group = mainjson.get("sub_group") != null ? mainjson.get("sub_group").getAsString() : "";

                                    productUnitData = mainjson.get("product_unit_data").getAsJsonArray();
//                            System.out.println("productUnitData " + productUnitData.size());

                                    if(productUnitData != null && productUnitData.size()!=0){
                                        int count = 0;
                                        for (int k = 0; k < productUnitData.size(); k++){
                                            String batch_no = "", expiry_date = "", closing_stock = "";
                                            JsonObject productUnitData1 = productUnitData.get(k).getAsJsonObject();

//                                    System.out.println("productUnitData1 " + productUnitData1);
//                                            batch_no = productUnitData1.get("batchno") != null ? productUnitData1.get("batchno").getAsString() : "";
                                            batch_no = productUnitData1.get("batch") != null ? productUnitData1.get("batch").getAsString() : "";
                                            expiry_date = productUnitData1.get("expiry_date") != null ? productUnitData1.get("expiry_date").getAsString() : "";
//                                            closing_stock = productUnitData1.get("closing_stock") != null ? productUnitData1.get("closing_stock").toString(): "";
                                            closing_stock = "";
//                                    System.out.println("closing_stock -----> " + closing_stock);

//                                            JsonArray batchUnitData = productUnitData1.get("batch_unit_data").getAsJsonArray();
                                            JsonArray batchUnitData = productUnitData1.get("unit_list").getAsJsonArray();
                                            for (int j = 0; j < batchUnitData.size(); j++){
                                                count++;
                                                JsonObject batchUnitData1 = batchUnitData.get(j).getAsJsonObject();
//                                        System.out.println("batchUnitData1 " + batchUnitData1);
//                                                String unit_name = batchUnitData1.get("unit_name") != null ? batchUnitData1.get("unit_name").getAsString() : "";
                                                String unit_name = batchUnitData1.get("unitName") != null ? batchUnitData1.get("unitName").getAsString() : "";
//                                                String unit_qty = batchUnitData1.get("unit_qty") != null ? batchUnitData1.get("unit_qty").getAsString() : "";
                                                String unit_qty = batchUnitData1.get("physical_stock") != null ? batchUnitData1.get("physical_stock").getAsString()+" ( "+batchUnitData1.get("logical_stock").getAsString()+" )" : "";

                                                if(count==1){
                                                    observableList.add(new StockReport1DTO(id, code, product_name, drug_type, packing, brand, drug_content, rate, company, group, sub_group, batch_no, expiry_date, closing_stock, unit_name, unit_qty));
                                                    observableList1.add(new StockReport1DTOFilter(id, code, product_name, drug_type, packing, brand, drug_content, rate, company, group, sub_group, batch_no, expiry_date, closing_stock, unit_name, unit_qty));
                                                    tblStockReport1Batch.setCellValueFactory(new PropertyValueFactory<>("batch_no"));
                                                    tblStockReport1ExpiryDate.setCellValueFactory(new PropertyValueFactory<>("expiry_date"));
                                                    tblStockReport1Unit.setCellValueFactory(new PropertyValueFactory<>("unit_name"));
                                                    tblStockReport1Quantity.setCellValueFactory(new PropertyValueFactory<>("unit_qty"));

                                                }else {
                                                    observableList1.add(new StockReport1DTOFilter(id, code, product_name, drug_type, packing, brand, drug_content, rate, company, group, sub_group, batch_no, expiry_date, closing_stock, unit_name, unit_qty));
                                                    observableList.add(new StockReport1DTO("", "", "", "", "", "", "", "", "", "", "",  batch_no, expiry_date, closing_stock, unit_name, unit_qty));
                                                    tblStockReport1Batch.setCellValueFactory(new PropertyValueFactory<>("batch_no"));
                                                    tblStockReport1ExpiryDate.setCellValueFactory(new PropertyValueFactory<>("expiry_date"));
                                                    tblStockReport1Unit.setCellValueFactory(new PropertyValueFactory<>("unit_name"));
                                                    tblStockReport1Quantity.setCellValueFactory(new PropertyValueFactory<>("unit_qty"));
                                                    tblStockReport1Batch1.setCellValueFactory(new PropertyValueFactory<>("batch_no"));
                                                    tblStockReport1ExpiryDate1.setCellValueFactory(new PropertyValueFactory<>("expiry_date"));
                                                    tblStockReport1Unit1.setCellValueFactory(new PropertyValueFactory<>("unit_name"));
                                                    tblStockReport1Quantity1.setCellValueFactory(new PropertyValueFactory<>("unit_qty"));
                                                }
                                            }
                                        }
                                    }
                                    else  {

                                        observableList1.add(new StockReport1DTOFilter(id, code, product_name, drug_type, packing, brand, drug_content, rate, company, group, sub_group,"", "", "", "", ""));
                                        observableList.add(new StockReport1DTO(id, code, product_name, drug_type, packing, brand, drug_content, rate, company, group, sub_group,"", "", "", "", ""));

                                    }
                                }
                            }

                            tblStockReport1Code.setCellValueFactory(new PropertyValueFactory<>("code"));
                            tblStockReport1ProductName.setCellValueFactory(new PropertyValueFactory<>("product_name"));
                            tblStockReport1Packing.setCellValueFactory(new PropertyValueFactory<>("packing"));
                            tblStockReport1Brand.setCellValueFactory(new PropertyValueFactory<>("brand"));

                            tblStockReport1Code1.setCellValueFactory(new PropertyValueFactory<>("code"));
                            tblStockReport1ProductName1.setCellValueFactory(new PropertyValueFactory<>("product_name"));
                            tblStockReport1Packing1.setCellValueFactory(new PropertyValueFactory<>("packing"));
                            tblStockReport1Brand1.setCellValueFactory(new PropertyValueFactory<>("brand"));


                            tblvStockReport1List1.setItems(observableList1);
                            tblvStockReport1List.setItems(observableList);

                            originalData1 = observableList1;
                            originalData = observableList;
//                    calculateTotalAmount();
                            StockReport1ListLogger.debug("Success in Displaying Stock Report 1 List : StocksStockReport1Controller");
                        } else {
                            StockReport1ListLogger.debug("Stock Report 1 List ResponseObject is null : StocksStockReport1Controller");
                            tblvStockReport1List.getItems().clear();
//                    calculateTotalAmount();
                        }
                    } else {
                        StockReport1ListLogger.debug("Error in response of Stock Report 1 List : StocksStockReport1Controller");
                        tblvStockReport1List.getItems().clear();
                        //                calculateTotalAmount();
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    StockReport1ListLogger.error("Network API cancelled in fetchStockReport1List()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    StockReport1ListLogger.error("Network API failed in fetchStockReport1List()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            StockReport1ListLogger.debug("Get Stock Report 1 Data End...");

        } catch (Exception e) {
            StockReport1ListLogger.error("Stock Report 1 Error is " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            apiClient = null;
        }
    }
}
