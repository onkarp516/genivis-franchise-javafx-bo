package com.opethic.genivis.controller.Reports;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.*;
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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;

import static com.opethic.genivis.utils.FxmFileConstants.STOCKS_STOCK_REPORT2_LIST_SLUG;
import static com.opethic.genivis.utils.FxmFileConstants.STOCKS_STOCK_VALUATION2_LIST_SLUG;

public class StocksStockValuation1Controller implements Initializable {
    private static final Logger StockValuation1ListLogger = LoggerFactory.getLogger(StocksStockValuation1Controller.class);
    @FXML
    private TextField tfStockValuation1ListSearch;
    @FXML
    private ComboBox<String> cbFilter;
    String selectedFilter = "";
    @FXML
    private TableView<StocksStockValuation1DTO> tblvStockValuation1List;

    @FXML
    private TableView<StockValuation1filterDTO> tblvStockValuation1List1;

    @FXML
    private TableColumn<StocksStockValuation1DTO, String> tblStockValuation1ProductName, tblStockValuation1Packing, tblStockValuation1Batch,
            tblStockValuation1Unit, tblStockValuation1Qty, tblStockValuation1PurchaseRate, tblStockValuation1ValuationPR,
            tblStockValuation1CostWithTax, tblStockValuation1ValuationCWT, tblStockValuation1CostWithoutTax, tblStockValuation1ValuationCT,
            tblStockValuation1MRP, tblStockValuation1ValuationMRP, tblStockValuation1SalesRate, tblStockValuation1ValuationSR;

    @FXML
    private TableColumn<StocksStockValuation1DTO, String> tblStockValuation1ProductName1, tblStockValuation1Packing1, tblStockValuation1Batch1,
            tblStockValuation1Unit1, tblStockValuation1Qty1, tblStockValuation1PurchaseRate1, tblStockValuation1ValuationPR1,
            tblStockValuation1CostWithTax1, tblStockValuation1ValuationCWT1, tblStockValuation1CostWithoutTax1, tblStockValuation1ValuationCT1,
            tblStockValuation1MRP1, tblStockValuation1ValuationMRP1, tblStockValuation1SalesRate1, tblStockValuation1ValuationSR1;

    private ObservableList<StocksStockValuation1DTO> originalData;
    private ObservableList<StockValuation1filterDTO> originalData1;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Button btExportPdf, btExportExcel, btExportCsv, btExportPrint;

    private JsonObject jsonObject = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tblvStockValuation1List.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        StocksStockValuation1TableDesign();


        tblvStockValuation1List1.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        StocksStockValuation1Table1Design();

        tblvStockValuation1List.setVisible(true);
        tblvStockValuation1List.setManaged(true);

        tblvStockValuation1List1.setVisible(false);
        tblvStockValuation1List1.setManaged(false);


        mainBorderPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.DOWN && tfStockValuation1ListSearch.isFocused()){
                tblvStockValuation1List.getSelectionModel().select(0);
                tblvStockValuation1List.requestFocus();
            }
        });

        StockValuation1ListLogger.info("Start of Initialize method of : StocksStockValuation1Controller");
        Platform.runLater(() -> tfStockValuation1ListSearch.requestFocus());
        fetchStockValuation1List("");

        tblvStockValuation1List.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                StocksStockValuation1DTO selectedItem = (StocksStockValuation1DTO) tblvStockValuation1List.getSelectionModel().getSelectedItem();
                String id = selectedItem.getId();
                String product_name = selectedItem.getProduct_name();
                StocksStockValuation2Controller.product_name = product_name;
                System.out.println(product_name);
                System.out.println(id);
                GlobalController.getInstance().addTabStatic1(STOCKS_STOCK_VALUATION2_LIST_SLUG, false, Integer.valueOf(id));
            }
        });
        tblvStockValuation1List.addEventFilter(KeyEvent.ANY, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                StocksStockValuation1DTO selectedItem = (StocksStockValuation1DTO) tblvStockValuation1List.getSelectionModel().getSelectedItem();
                String id = selectedItem.getId();
                String product_name = selectedItem.getProduct_name();
                StocksStockValuation2Controller.product_name = product_name;
                System.out.println(product_name);
                System.out.println(id);
                GlobalController.getInstance().addTabStatic1(STOCKS_STOCK_VALUATION2_LIST_SLUG, false, Integer.valueOf(id));
            }
        });


        tblvStockValuation1List1.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                StockValuation1filterDTO selectedItem = (StockValuation1filterDTO) tblvStockValuation1List1.getSelectionModel().getSelectedItem();
                String id = selectedItem.getId();
                String product_name = selectedItem.getProduct_name();
                StocksStockValuation2Controller.product_name = product_name;
                System.out.println(product_name);
                System.out.println(id);
                GlobalController.getInstance().addTabStatic1(STOCKS_STOCK_VALUATION2_LIST_SLUG, false, Integer.valueOf(id));
            }
        });
        tblvStockValuation1List1.addEventFilter(KeyEvent.ANY, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                StockValuation1filterDTO selectedItem = (StockValuation1filterDTO) tblvStockValuation1List1.getSelectionModel().getSelectedItem();
                String id = selectedItem.getId();
                String product_name = selectedItem.getProduct_name();
                StocksStockValuation2Controller.product_name = product_name;
                System.out.println(product_name);
                System.out.println(id);
                GlobalController.getInstance().addTabStatic1(STOCKS_STOCK_VALUATION2_LIST_SLUG, false, Integer.valueOf(id));
            }
        });

        cbFilter.getItems().addAll("Product", "Brand", "Packaging", "Group", "Category");
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


        nodetraversal(cbFilter, tfStockValuation1ListSearch);
        nodetraversal(tfStockValuation1ListSearch, btExportPdf);
        nodetraversal(btExportPdf, btExportExcel);
        nodetraversal(btExportExcel, btExportCsv);
        nodetraversal(btExportCsv, btExportPrint);
        if(tblvStockValuation1List.isVisible()){
            tblvStockValuation1List.requestFocus();
        }
        else{
            tblvStockValuation1List1.requestFocus();
        }

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
            if(tblvStockValuation1List.isVisible()){
                exportToExcel();
            }
            else if(tblvStockValuation1List1.isVisible()){
                exportToExcel1();
            }

        });

    }


    //excelExport code
    private void exportToExcel() {
        ObservableList<StocksStockValuation1DTO> data = tblvStockValuation1List.getItems();

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
                    org.apache.poi.ss.usermodel.Cell headerCell = headerRow.createCell(colNum);
                    headerCell.setCellValue(headers[colNum]);
                    headerCell.setCellStyle(headerCellStyle);
                }

                // Populate data rows
                int rowNum = 1;
                for (StocksStockValuation1DTO dto : data) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(dto.getProduct_name());
                    row.createCell(1).setCellValue(dto.getPacking());
                    row.createCell(2).setCellValue(dto.getBatch());
                    row.createCell(3).setCellValue(dto.getUnit());
                    row.createCell(4).setCellValue(dto.getQty());
                    row.createCell(5).setCellValue(dto.getPurchase_rate());
                    row.createCell(6).setCellValue(dto.getValuation_pr());
                    row.createCell(8).setCellValue(dto.getCost_With_tax());
                    row.createCell(9).setCellValue(dto.getValuation_ct());
                    row.createCell(10).setCellValue(dto.getCost_Without_tax());
                    row.createCell(11).setCellValue(dto.getValuation_cwt());
                    row.createCell(12).setCellValue(dto.getMrp());
                    row.createCell(13).setCellValue(dto.getValuation_mrp());
                    row.createCell(14).setCellValue(dto.getSales_rate());
                    row.createCell(15).setCellValue(dto.getValuation_sr());



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
        ObservableList<StockValuation1filterDTO> data = tblvStockValuation1List1.getItems();

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
                for (StockValuation1filterDTO dto : data) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(dto.getProduct_name());
                    row.createCell(1).setCellValue(dto.getPacking());
                    row.createCell(2).setCellValue(dto.getBatch());
                    row.createCell(3).setCellValue(dto.getUnit());
                    row.createCell(4).setCellValue(dto.getQty());
                    row.createCell(5).setCellValue(dto.getPurchase_rate());
                    row.createCell(6).setCellValue(dto.getValuation_pr());
                    row.createCell(8).setCellValue(dto.getCost_With_tax());
                    row.createCell(9).setCellValue(dto.getValuation_ct());
                    row.createCell(10).setCellValue(dto.getCost_Without_tax());
                    row.createCell(11).setCellValue(dto.getValuation_cwt());
                    row.createCell(12).setCellValue(dto.getMrp());
                    row.createCell(13).setCellValue(dto.getValuation_mrp());
                    row.createCell(14).setCellValue(dto.getSales_rate());
                    row.createCell(15).setCellValue(dto.getValuation_sr());

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
        if(tblvStockValuation1List.isVisible()){
            ObservableList<TableColumn<StocksStockValuation1DTO, ?>> columns = tblvStockValuation1List.getColumns();
            String[] headers = {"Product Name", "Packing","batch", "Unit", "Quantity", "Purchase Rate", "Valuation", "Cost with Tax", "Valuation", "Cost Without Tax", "Valuation", "Mrp", "Valuation","Sale Rate", "Valuation",};
            for (int i = 0; i < headers.length; i++) {
                headers[i] = columns.get(i).getText();
            }
            return headers;
        }
        else{
            ObservableList<TableColumn<StockValuation1filterDTO, ?>> columns = tblvStockValuation1List1.getColumns();
            String[] headers = {"Product Name", "Packing","batch", "Unit", "Quantity", "Purchase Rate", "Valuation", "Cost with Tax", "Valuation", "Cost Without Tax", "Valuation", "Mrp", "Valuation","Sale Rate", "Valuation",};
            for (int i = 0; i < headers.length; i++) {
                headers[i] = columns.get(i).getText();
            }
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

    private void StocksStockValuation1TableDesign() {
        //todo: Responsive code for tableview
        tblStockValuation1ProductName.prefWidthProperty().bind(tblvStockValuation1List.widthProperty().multiply(0.27));
        tblStockValuation1Packing.prefWidthProperty().bind(tblvStockValuation1List.widthProperty().multiply(0.05));
        tblStockValuation1Batch.prefWidthProperty().bind(tblvStockValuation1List.widthProperty().multiply(0.05));
        tblStockValuation1Unit.prefWidthProperty().bind(tblvStockValuation1List.widthProperty().multiply(0.08));
        tblStockValuation1Qty.prefWidthProperty().bind(tblvStockValuation1List.widthProperty().multiply(0.05));
        tblStockValuation1PurchaseRate.prefWidthProperty().bind(tblvStockValuation1List.widthProperty().multiply(0.06));
        tblStockValuation1ValuationPR.prefWidthProperty().bind(tblvStockValuation1List.widthProperty().multiply(0.05));
        tblStockValuation1CostWithTax.prefWidthProperty().bind(tblvStockValuation1List.widthProperty().multiply(0.06));
        tblStockValuation1ValuationCWT.prefWidthProperty().bind(tblvStockValuation1List.widthProperty().multiply(0.06));
        tblStockValuation1CostWithoutTax.prefWidthProperty().bind(tblvStockValuation1List.widthProperty().multiply(0.07));
        tblStockValuation1ValuationCT.prefWidthProperty().bind(tblvStockValuation1List.widthProperty().multiply(0.05));
        tblStockValuation1MRP.prefWidthProperty().bind(tblvStockValuation1List.widthProperty().multiply(0.05));
        tblStockValuation1ValuationMRP.prefWidthProperty().bind(tblvStockValuation1List.widthProperty().multiply(0.05));
        tblStockValuation1SalesRate.prefWidthProperty().bind(tblvStockValuation1List.widthProperty().multiply(0.05));
        tblStockValuation1ValuationSR.prefWidthProperty().bind(tblvStockValuation1List.widthProperty().multiply(0.05));
    }
    private void StocksStockValuation1Table1Design() {
        //todo: Responsive code for tableview
        tblStockValuation1ProductName1.prefWidthProperty().bind(tblvStockValuation1List1.widthProperty().multiply(0.27));
        tblStockValuation1Packing1.prefWidthProperty().bind(tblvStockValuation1List1.widthProperty().multiply(0.05));
        tblStockValuation1Batch1.prefWidthProperty().bind(tblvStockValuation1List1.widthProperty().multiply(0.05));
        tblStockValuation1Unit1.prefWidthProperty().bind(tblvStockValuation1List1.widthProperty().multiply(0.08));
        tblStockValuation1Qty1.prefWidthProperty().bind(tblvStockValuation1List1.widthProperty().multiply(0.05));
        tblStockValuation1PurchaseRate1.prefWidthProperty().bind(tblvStockValuation1List1.widthProperty().multiply(0.06));
        tblStockValuation1ValuationPR1.prefWidthProperty().bind(tblvStockValuation1List1.widthProperty().multiply(0.05));
        tblStockValuation1CostWithTax1.prefWidthProperty().bind(tblvStockValuation1List1.widthProperty().multiply(0.06));
        tblStockValuation1ValuationCWT1.prefWidthProperty().bind(tblvStockValuation1List1.widthProperty().multiply(0.06));
        tblStockValuation1CostWithoutTax1.prefWidthProperty().bind(tblvStockValuation1List1.widthProperty().multiply(0.07));
        tblStockValuation1ValuationCT1.prefWidthProperty().bind(tblvStockValuation1List1.widthProperty().multiply(0.05));
        tblStockValuation1MRP1.prefWidthProperty().bind(tblvStockValuation1List1.widthProperty().multiply(0.05));
        tblStockValuation1ValuationMRP1.prefWidthProperty().bind(tblvStockValuation1List1.widthProperty().multiply(0.05));
        tblStockValuation1SalesRate1.prefWidthProperty().bind(tblvStockValuation1List1.widthProperty().multiply(0.05));
        tblStockValuation1ValuationSR1.prefWidthProperty().bind(tblvStockValuation1List1.widthProperty().multiply(0.05));
    }

    private void handleFilterComboBoxAction(ActionEvent actionEvent) {
        selectedFilter = cbFilter.getSelectionModel().getSelectedItem();
        System.out.println("Selected Filter: " + selectedFilter);
        if (cbFilter.getSelectionModel().getSelectedItem() == "Product") {
            selectedFilter = "product";
        } else if (cbFilter.getSelectionModel().getSelectedItem() == "Brand") {
            selectedFilter = "brand";
        } else if (cbFilter.getSelectionModel().getSelectedItem() == "Packaging") {
            selectedFilter = "packaging";
        } else if (cbFilter.getSelectionModel().getSelectedItem() == "Group") {
            selectedFilter = "group";
        } else if (cbFilter.getSelectionModel().getSelectedItem() == "Category") {
            selectedFilter = "category";
        }

        fetchStockValuation1ListByFilterSearch();
    }
    @FXML
    private void fetchStockValuation1ListByFilterSearch() {
        ObservableList<StockValuation1filterDTO> filteredData = FXCollections.observableArrayList();
        tfStockValuation1ListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null && !newValue.equals("")) {
//                System.out.println("true--------->");
                tblvStockValuation1List.setVisible(false);
                tblvStockValuation1List.setManaged(false);

                tblvStockValuation1List1.setVisible(true);
                tblvStockValuation1List1.setManaged(true);

//                    filterData(newValue.trim(), StockReport1DTOFilter::getProduct_name);
            } else {
                tblvStockValuation1List.setVisible(true);
                tblvStockValuation1List.setManaged(true);

                tblvStockValuation1List1.setVisible(false);
                tblvStockValuation1List1.setManaged(false);
//                System.out.println("false--------->");
            }
        });
        if (selectedFilter.equals("product")) {
            tfStockValuation1ListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData(newValue.trim(), StockValuation1filterDTO::getProduct_name);
            });
        } else if (selectedFilter.equals("brand")) {
            tfStockValuation1ListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData(newValue.trim(), StockValuation1filterDTO::getBrand);
            });
        } else if (selectedFilter.equals("packaging")) {
            tfStockValuation1ListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData(newValue.trim(), StockValuation1filterDTO::getPacking);
            });
        } else if (selectedFilter.equals("group")) {
            tfStockValuation1ListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData(newValue.trim(), StockValuation1filterDTO::getGroup);
            });
        } else if (selectedFilter.equals("category")) {
            tfStockValuation1ListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData(newValue.trim(), StockValuation1filterDTO::getCategory);
            });
        }
    }

    private void filterData(String keyword, Function<StockValuation1filterDTO, String> fieldExtractor) {
        ObservableList<StockValuation1filterDTO> filteredData = FXCollections.observableArrayList();
        StockValuation1ListLogger.error("Search Stock Valuation 1 List in StocksStockValuation1Controller");
        if (keyword.isEmpty()) {
            tblvStockValuation1List1.setItems(originalData1);
            return;
        }
        for (StockValuation1filterDTO item : originalData1) {
            if (matchesKeyword(item, keyword, fieldExtractor) ) {
                filteredData.add(item);
            }
        }
        tblvStockValuation1List1.setItems(filteredData);
    }

    private boolean matchesKeyword(StockValuation1filterDTO item, String keyword, Function<StockValuation1filterDTO, String> fieldExtractor) {
        String lowerCaseKeyword = keyword.toLowerCase();
        String fieldValue = fieldExtractor.apply(item).toLowerCase();
        return fieldValue.contains(lowerCaseKeyword);
    }

    private void fetchStockValuation1List(String searchKey) {
        StockValuation1ListLogger.info("Fetch Stock Valuation List  : StocksStockValuation1Controller");

        APIClient apiClient = null;

        try {
            StockValuation1ListLogger.debug("Get Stock Valuation List Data Started...");
            Map<String, String> body = new HashMap<>();
//            HttpResponse<String> response = APIClient.postFormDataRequest("", EndPoints.STOCKS_STOCK_VALUATION1_LIST);
            apiClient = new APIClient(EndPoints.STOCKS_STOCK_VALUATION1_LIST, "", RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
//                    JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//            System.out.println("fetchStockValuation1List" + responseBody);
                    ObservableList<StocksStockValuation1DTO> observableList = FXCollections.observableArrayList();
                    ObservableList<StockValuation1filterDTO> observableList1 = FXCollections.observableArrayList();
                    if(jsonObject.get("responseStatus").getAsInt() == 200){
                        JsonArray responseList = jsonObject.getAsJsonArray("data");
//                System.out.println("fetchStockValuation1List" + responseList);

                        JsonObject mainjson = new JsonObject();

                        if (responseList.size() > 0){
                            tblvStockValuation1List.setVisible(true);
                            for (int i = 0; i < responseList.size(); i++){///JsonElement element : responseList
                                mainjson = responseList.get(i).getAsJsonObject();

                                if(mainjson != null) {
                                    JsonArray batchData = new JsonArray();
                                    String id = mainjson.get("id") != null ? mainjson.get("id").getAsString() : "";
                                    String product_name = mainjson.get("product_name") != null ? mainjson.get("product_name").getAsString() : "";
                                    String brand = mainjson.get("brand_name") != null ? mainjson.get("brand_name").getAsString() : "";
                                    String packing = mainjson.get("packaging") != null ? mainjson.get("packaging").getAsString() : "";
                                    String group = mainjson.get("group") != null ? mainjson.get("group").getAsString() : "";
                                    String category = mainjson.get("category") != null ? mainjson.get("category").getAsString() : "";

                                    batchData = mainjson.get("batch_data").getAsJsonArray();
//                            System.out.println("batchData " + batchData.size());

                                    if(batchData != null && !(batchData.size()==0) ){
                                        int count = 0;
                                        for (int k = 0; k < batchData.size(); k++) {
                                            String batchNo = "";
                                            JsonObject batchData1 = batchData.get(k).getAsJsonObject();

                                            count++;
//                                    System.out.println("bathData1 " + batchData1);
                                            batchNo = batchData1.get("batchno") != null ? batchData1.get("batchno").getAsString() : "";

                                            JsonArray unitData = batchData1.get("unit_data").getAsJsonArray();
                                            for(int j = 0; j < unitData.size(); j++){
                                                JsonObject unitData1 = unitData.get(j).getAsJsonObject();
//                                        System.out.println("unitData1 " + unitData1);
                                                String unit = unitData1.get("unit_name") != null ? unitData1.get("unit_name").getAsString() : "";
                                                String qty = unitData1.get("unit_qty") != null ? unitData1.get("unit_qty").getAsString() : "";
                                                String purchase_rate = unitData1.get("unit_purchaseRate") != null ? unitData1.get("unit_purchaseRate").getAsString() : "";
                                                String valuation_pr = unitData1.get("unit_purchasevalue") != null ? unitData1.get("unit_purchasevalue").getAsString() : "";
                                                String cost_With_tax = unitData1.get("unit_costingWT") != null ? unitData1.get("unit_costingWT").getAsString() : "";
                                                String valuation_cwt = unitData1.get("unit_costingWTValue") != null ? unitData1.get("unit_costingWTValue").getAsString() : "";
                                                String cost_Without_tax = unitData1.get("unit_costing") != null ? unitData1.get("unit_costing").getAsString() : "";
                                                String valuation_ct = unitData1.get("unit_costingValue") != null ? unitData1.get("unit_costingValue").getAsString() : "";
                                                String mrp = unitData1.get("unit_mrp") != null ? unitData1.get("unit_mrp").getAsString() : "";
                                                String valuation_mrp = unitData1.get("unit_mrpValue") != null ? unitData1.get("unit_mrpValue").getAsString() : "";
                                                String sales_rate = unitData1.get("unit_saleRate") != null ? unitData1.get("unit_saleRate").getAsString() : "";
                                                String valuation_sr = unitData1.get("unit_salevalue") != null ? unitData1.get("unit_salevalue").getAsString() : "";


                                                if(count==1){
                                                    observableList.add(new StocksStockValuation1DTO(id, product_name, brand, packing, group, category, batchNo, unit, qty, purchase_rate, valuation_pr,
                                                            cost_With_tax, valuation_cwt, cost_Without_tax, valuation_ct, mrp, valuation_mrp, sales_rate, valuation_sr));
                                                    observableList1.add(new StockValuation1filterDTO(id, product_name, brand, packing, group, category, batchNo, unit, qty, purchase_rate, valuation_pr,
                                                            cost_With_tax, valuation_cwt, cost_Without_tax, valuation_ct, mrp, valuation_mrp, sales_rate, valuation_sr));
                                                    tblStockValuation1Batch.setCellValueFactory(new PropertyValueFactory<>("batch"));
                                                    tblStockValuation1Unit.setCellValueFactory(new PropertyValueFactory<>("unit"));
                                                    tblStockValuation1Qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
                                                    tblStockValuation1PurchaseRate.setCellValueFactory(new PropertyValueFactory<>("purchase_rate"));
                                                    tblStockValuation1ValuationPR.setCellValueFactory(new PropertyValueFactory<>("valuation_pr"));
                                                    tblStockValuation1CostWithTax.setCellValueFactory(new PropertyValueFactory<>("cost_With_tax"));
                                                    tblStockValuation1ValuationCWT.setCellValueFactory(new PropertyValueFactory<>("valuation_cwt"));
                                                    tblStockValuation1CostWithoutTax.setCellValueFactory(new PropertyValueFactory<>("cost_Without_tax"));
                                                    tblStockValuation1ValuationCT.setCellValueFactory(new PropertyValueFactory<>("valuation_ct"));
                                                    tblStockValuation1MRP.setCellValueFactory(new PropertyValueFactory<>("mrp"));
                                                    tblStockValuation1ValuationMRP.setCellValueFactory(new PropertyValueFactory<>("valuation_mrp"));
                                                    tblStockValuation1SalesRate.setCellValueFactory(new PropertyValueFactory<>("sales_rate"));
                                                    tblStockValuation1ValuationSR.setCellValueFactory(new PropertyValueFactory<>("valuation_sr"));
                                                    //set data in tableview2
                                                    tblStockValuation1Batch1.setCellValueFactory(new PropertyValueFactory<>("batch"));
                                                    tblStockValuation1Unit1.setCellValueFactory(new PropertyValueFactory<>("unit"));
                                                    tblStockValuation1Qty1.setCellValueFactory(new PropertyValueFactory<>("qty"));
                                                    tblStockValuation1PurchaseRate1.setCellValueFactory(new PropertyValueFactory<>("purchase_rate"));
                                                    tblStockValuation1ValuationPR1.setCellValueFactory(new PropertyValueFactory<>("valuation_pr"));
                                                    tblStockValuation1CostWithTax1.setCellValueFactory(new PropertyValueFactory<>("cost_With_tax"));
                                                    tblStockValuation1ValuationCWT1.setCellValueFactory(new PropertyValueFactory<>("valuation_cwt"));
                                                    tblStockValuation1CostWithoutTax1.setCellValueFactory(new PropertyValueFactory<>("cost_Without_tax"));
                                                    tblStockValuation1ValuationCT1.setCellValueFactory(new PropertyValueFactory<>("valuation_ct"));
                                                    tblStockValuation1MRP1.setCellValueFactory(new PropertyValueFactory<>("mrp"));
                                                    tblStockValuation1ValuationMRP1.setCellValueFactory(new PropertyValueFactory<>("valuation_mrp"));
                                                    tblStockValuation1SalesRate1.setCellValueFactory(new PropertyValueFactory<>("sales_rate"));
                                                    tblStockValuation1ValuationSR1.setCellValueFactory(new PropertyValueFactory<>("valuation_sr"));
                                                }else{
                                                    observableList.add(new StocksStockValuation1DTO("", "", "", "", "", "", batchNo, unit, qty, purchase_rate, valuation_pr,
                                                            cost_With_tax, valuation_cwt, cost_Without_tax, valuation_ct, mrp, valuation_mrp, sales_rate, valuation_sr));
                                                    observableList1.add(new StockValuation1filterDTO(id, product_name, brand, packing, group, category, batchNo, unit, qty, purchase_rate, valuation_pr,
                                                            cost_With_tax, valuation_cwt, cost_Without_tax, valuation_ct, mrp, valuation_mrp, sales_rate, valuation_sr));
                                                    tblStockValuation1Batch.setCellValueFactory(new PropertyValueFactory<>("batch"));
                                                    tblStockValuation1Unit.setCellValueFactory(new PropertyValueFactory<>("unit"));
                                                    tblStockValuation1Qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
                                                    tblStockValuation1PurchaseRate.setCellValueFactory(new PropertyValueFactory<>("purchase_rate"));
                                                    tblStockValuation1ValuationPR.setCellValueFactory(new PropertyValueFactory<>("valuation_pr"));
                                                    tblStockValuation1CostWithTax.setCellValueFactory(new PropertyValueFactory<>("cost_With_tax"));
                                                    tblStockValuation1ValuationCWT.setCellValueFactory(new PropertyValueFactory<>("valuation_cwt"));
                                                    tblStockValuation1CostWithoutTax.setCellValueFactory(new PropertyValueFactory<>("cost_Without_tax"));
                                                    tblStockValuation1ValuationCT.setCellValueFactory(new PropertyValueFactory<>("valuation_ct"));
                                                    tblStockValuation1MRP.setCellValueFactory(new PropertyValueFactory<>("mrp"));
                                                    tblStockValuation1ValuationMRP.setCellValueFactory(new PropertyValueFactory<>("valuation_mrp"));
                                                    tblStockValuation1SalesRate.setCellValueFactory(new PropertyValueFactory<>("sales_rate"));
                                                    tblStockValuation1ValuationSR.setCellValueFactory(new PropertyValueFactory<>("valuation_sr"));
                                                    //set data in tableview2
                                                    tblStockValuation1Batch1.setCellValueFactory(new PropertyValueFactory<>("batch"));
                                                    tblStockValuation1Unit1.setCellValueFactory(new PropertyValueFactory<>("unit"));
                                                    tblStockValuation1Qty1.setCellValueFactory(new PropertyValueFactory<>("qty"));
                                                    tblStockValuation1PurchaseRate1.setCellValueFactory(new PropertyValueFactory<>("purchase_rate"));
                                                    tblStockValuation1ValuationPR1.setCellValueFactory(new PropertyValueFactory<>("valuation_pr"));
                                                    tblStockValuation1CostWithTax1.setCellValueFactory(new PropertyValueFactory<>("cost_With_tax"));
                                                    tblStockValuation1ValuationCWT1.setCellValueFactory(new PropertyValueFactory<>("valuation_cwt"));
                                                    tblStockValuation1CostWithoutTax1.setCellValueFactory(new PropertyValueFactory<>("cost_Without_tax"));
                                                    tblStockValuation1ValuationCT1.setCellValueFactory(new PropertyValueFactory<>("valuation_ct"));
                                                    tblStockValuation1MRP1.setCellValueFactory(new PropertyValueFactory<>("mrp"));
                                                    tblStockValuation1ValuationMRP1.setCellValueFactory(new PropertyValueFactory<>("valuation_mrp"));
                                                    tblStockValuation1SalesRate1.setCellValueFactory(new PropertyValueFactory<>("sales_rate"));
                                                    tblStockValuation1ValuationSR1.setCellValueFactory(new PropertyValueFactory<>("valuation_sr"));
                                                }

                                            }
                                        }
                                    }
                                    else  {
                                        observableList.add(new StocksStockValuation1DTO(id, product_name, brand, packing, group, category,"", "", "", "", "",
                                                "", "", "", "", "", "", "", ""));
//                                        tblStockValuation1Batch.setCellValueFactory(new PropertyValueFactory<>(""));
                                        observableList1.add(new StockValuation1filterDTO(id, product_name, brand, packing, group, category,"", "", "", "", "",
                                                "", "", "", "", "", "", "", ""));
//                                        tblStockValuation1Batch.setCellValueFactory(new PropertyValueFactory<>(""));
                                    }

                                }

                            }

                            tblStockValuation1ProductName.setCellValueFactory(new PropertyValueFactory<>("product_name"));
                            tblStockValuation1Packing.setCellValueFactory(new PropertyValueFactory<>("packing"));
                            tblStockValuation1ProductName1.setCellValueFactory(new PropertyValueFactory<>("product_name"));
                            tblStockValuation1Packing1.setCellValueFactory(new PropertyValueFactory<>("packing"));


                            tblvStockValuation1List.setItems(observableList);
                            originalData = observableList;
                            tblvStockValuation1List1.setItems(observableList1);
                            originalData1= observableList1;
//                    calculateTotalAmount();
                            StockValuation1ListLogger.debug("Success in Displaying Stock Valuation List : StocksStockValuation1Controller");

                        } else {
                            StockValuation1ListLogger.debug("Stock Valuation List ResponseObject is null : StocksStockValuation1Controller");
                            tblvStockValuation1List.getItems().clear();
//                    calculateTotalAmount();
                        }
                    } else {
                        StockValuation1ListLogger.debug("Error in response of Stock Valuation List : StocksStockValuation1Controller");
                        tblvStockValuation1List.getItems().clear();
//                calculateTotalAmount();
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    StockValuation1ListLogger.error("Network API cancelled in fetchStockValuation1List()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    StockValuation1ListLogger.error("Network API failed in fetchStockValuation1List()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            StockValuation1ListLogger.debug("Get Stock Valuation List Data End...");
        } catch (Exception e) {
            StockValuation1ListLogger.error("Stock Valuation List Error is " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            apiClient = null;
        }
    }
}
