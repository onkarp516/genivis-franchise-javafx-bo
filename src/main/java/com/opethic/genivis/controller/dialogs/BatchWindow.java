package com.opethic.genivis.controller.dialogs;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.commons.OverlaysEffect;
import com.opethic.genivis.controller.tranx_sales.common.TranxCommonPopUps;
import com.opethic.genivis.dto.TranxBatchWindowDTO;
import com.opethic.genivis.dto.cmp_t_row.BatchWindowTableDTO;
import com.opethic.genivis.dto.pur_invoice.LevelAForPurInvoice;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.dto.reqres.product.TableCellCallback;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.xmlbeans.soap.SOAPArrayType;
import org.json.JSONObject;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
//import java.time.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.opethic.genivis.controller.account_entry.PaymentCreateController.paymentLogger;
import static com.opethic.genivis.utils.Globals.decimalFormat;

public class BatchWindow {

    public static <T> void batchWindow(String current_string, Map<String, String> map, List<BatchWindowTableDTO> listEdit, Consumer<List<BatchWindowTableDTO>> callback) {
        OverlaysEffect.setOverlaysEffect(Communicator.stage);
        Stage primaryStage = new Stage();
        Communicator.stage2 = primaryStage;
        primaryStage.initOwner(Communicator.stage);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/cmptrow_windows.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");
        //  Platform.runLater(() -> borderPane.requestFocus());
        //BorderPan under Top Layout....................................................................................................................
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(1400);
        hbox_top.setMaxWidth(1400);
        hbox_top.setPrefWidth(1400);
        hbox_top.setMaxHeight(40);
        hbox_top.setMinHeight(40);
        hbox_top.setPrefHeight(40);

        Label popup_title = new Label("Batch");
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(Communicator.stage);
        });
        HBox.setMargin(imageView, new Insets(0, 10, 0, 0));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        //   hbox_top.getChildren().add(current_stock);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #EDF7FF; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 1 0;");

        HBox hbox_top2 = new HBox();
        hbox_top2.setMinWidth(1400);
        hbox_top2.setMaxWidth(1400);
        hbox_top2.setPrefWidth(1400);
        hbox_top2.setMaxHeight(40);
        hbox_top2.setMinHeight(40);
        hbox_top2.setPrefHeight(40);
        hbox_top2.setStyle(" -fx-background-color: #F4EBC8; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 1 0;");
        hbox_top2.setAlignment(Pos.CENTER_LEFT);


        HBox hbCost1 = new HBox();
        hbCost1.setMinWidth(600);
        hbCost1.setMaxWidth(600);
        hbCost1.setPrefWidth(600);
        hbCost1.setMaxHeight(40);
        hbCost1.setMinHeight(40);
        hbCost1.setPrefHeight(40);
        hbCost1.setStyle(" -fx-background-color: #F4EBC8; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 1 0;");
        hbCost1.setAlignment(Pos.CENTER_LEFT);

        Label product_name = new Label("Product Name: ");
        product_name.setStyle("-fx-font-size: 14; -fx-text-fill: #B97C33; -fx-font-weight: bold;");
        product_name.setPadding(new Insets(0, 10, 0, 5));
        Label product_name_field = new Label();
        product_name_field.setStyle("-fx-font-size: 14; -fx-text-fill: #000; -fx-font-weight: bold;");
        HBox.setMargin(product_name_field, new Insets(0, 10, 0, 5));
        product_name_field.setText(map.get("product_name"));
        hbCost1.getChildren().addAll(product_name,product_name_field );

        HBox hbCost2 = new HBox();
        hbCost2.setMinWidth(300);
        hbCost2.setMaxWidth(300);
        hbCost2.setPrefWidth(300);
        hbCost2.setMaxHeight(40);
        hbCost2.setMinHeight(40);
        hbCost2.setPrefHeight(40);
        hbCost2.setStyle(" -fx-background-color: #F4EBC8; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 1 0;");
        hbCost2.setAlignment(Pos.CENTER_LEFT);

        Label cost = new Label("Cost With Tax : ");
        cost.setStyle("-fx-font-size: 14; -fx-text-fill: #B97C33; -fx-font-weight: bold;");
        cost.setPadding(new Insets(0, 10, 0, 0));
        Label cost_field = new Label("");
        cost_field.setStyle("-fx-font-size: 14; -fx-text-fill: #000; -fx-font-weight: bold;");
        HBox.setMargin(cost_field, new Insets(0, 10, 0, 5));
        hbCost2.getChildren().addAll(cost,cost_field );

        HBox hbCost3 = new HBox();
        hbCost3.setMinWidth(400);
        hbCost3.setMaxWidth(400);
        hbCost3.setPrefWidth(400);
        hbCost3.setMaxHeight(40);
        hbCost3.setMinHeight(40);
        hbCost3.setPrefHeight(40);
        hbCost3.setStyle(" -fx-background-color: #F4EBC8; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 1 0;");
        hbCost3.setAlignment(Pos.CENTER_LEFT);

        Label costing = new Label("Cost Without Tax : ");
        costing.setStyle("-fx-font-size: 14; -fx-text-fill: #B97C33; -fx-font-weight: bold;");
        costing.setPadding(new Insets(0, 10, 0, 0));
        Label costing_field = new Label("");
        costing_field.setStyle("-fx-font-size: 14; -fx-text-fill: #000; -fx-font-weight: bold;");
        costing_field.setPadding(new Insets(0, 10, 0, 0));
        HBox.setMargin(costing_field, new Insets(0, 10, 0, 5));
        hbCost3.getChildren().addAll(costing,costing_field );

        HBox hbCost4 = new HBox();
        hbCost4.setMinWidth(100);
        hbCost4.setMaxWidth(100);
        hbCost4.setPrefWidth(100);
        hbCost4.setMaxHeight(40);
        hbCost4.setMinHeight(40);
        hbCost4.setPrefHeight(40);
        hbCost4.setStyle(" -fx-background-color: #F4EBC8; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 1 0;");
        hbCost4.setAlignment(Pos.CENTER_LEFT);

        String prdBatchTaxPer = map.get("tax");
        Label tax = new Label("Tax : ");
        tax.setStyle("-fx-font-size: 14; -fx-text-fill: #B97C33; -fx-font-weight: bold;");
        tax.setPadding(new Insets(0, 10, 0, 0));
        Label tax_field = new Label("");
        tax_field.setStyle("-fx-font-size: 14; -fx-text-fill: #000; -fx-font-weight: bold;");
        tax_field.setPadding(new Insets(0, 10, 0, 0));
        tax_field.setText(prdBatchTaxPer);
        hbCost4.getChildren().addAll(tax,tax_field );


        hbox_top2.getChildren().addAll(hbCost1,hbCost2,hbCost3,hbCost4);


        HBox hbox_top3 = new HBox();
        hbox_top3.setMinWidth(1400);
        hbox_top3.setMaxWidth(1400);
        hbox_top3.setPrefWidth(1400);
        hbox_top3.setMaxHeight(35);
        hbox_top3.setMinHeight(35);
        hbox_top3.setPrefHeight(35);
        hbox_top3.setStyle(" -fx-background-color: #EDF7FF; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 1 0;");


        VBox vBox = new VBox();
        vBox.getChildren().addAll(hbox_top, hbox_top2, hbox_top3);

        HBox hbox_top4 = new HBox();
        hbox_top4.setMinWidth(1398);
        hbox_top4.setMaxWidth(1398);
        hbox_top4.setPrefWidth(1398);
        hbox_top4.setMaxHeight(35);
        hbox_top4.setMinHeight(35);
        hbox_top4.setPrefHeight(35);
        hbox_top4.setStyle(" -fx-background-color: white; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 0 0;");

        borderPane.setTop(vBox);
        borderPane.setBottom(hbox_top4);

        TableView<BatchWindowTableDTO> tableView = new TableView<BatchWindowTableDTO>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setFocusTraversable(false);


        tableView.setPrefHeight(75);
        tableView.setMaxHeight(75);
        tableView.setMinHeight(75);
        tableView.setMaxWidth(1400);
        tableView.setMinWidth(1400);
        tableView.setPrefWidth(1400);
        borderPane.setCenter(tableView);

        tableView.setEditable(false);

        Platform.runLater(() -> {
            tableView.getSelectionModel().select(0);
        });


        TableColumn<BatchWindowTableDTO, String> cmBatch = new TableColumn<>("Batch");
        cmBatch.setMaxWidth(150);
        cmBatch.setMinWidth(150);


        TableColumn<BatchWindowTableDTO, String> cmMFGDate = new TableColumn<>("MFG Date");
        cmMFGDate.setMaxWidth(100);
        cmMFGDate.setMinWidth(100);


        TableColumn<BatchWindowTableDTO, String> cmExpiry = new TableColumn<>("Expiry");
        cmExpiry.setMaxWidth(100);
        cmExpiry.setMinWidth(100);

        TableColumn<BatchWindowTableDTO, String> cmMrp = new TableColumn<>("MRP");
        cmMrp.setMaxWidth(80);
        cmMrp.setMinWidth(80);
        cmMrp.setStyle("-fx-alignment: CENTER-RIGHT;");


        TableColumn<BatchWindowTableDTO, String> cmPurRate = new TableColumn<>("Pur Rate");
        cmPurRate.setMaxWidth(80);
        cmPurRate.setMinWidth(80);
        cmPurRate.setStyle("-fx-alignment: CENTER-RIGHT;");


        TableColumn<BatchWindowTableDTO, String> cmQty = new TableColumn<>("Qty");
        cmQty.setMaxWidth(60);
        cmQty.setMinWidth(60);
        cmQty.setStyle("-fx-alignment: CENTER-RIGHT;");
        TableColumn<BatchWindowTableDTO, String> cmFree = new TableColumn<>("Free");
        cmFree.setMaxWidth(60);
        cmFree.setMinWidth(60);
        cmFree.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<BatchWindowTableDTO, String> cmDiscPer = new TableColumn<>("Disc.%");
        cmDiscPer.setMaxWidth(70);
        cmDiscPer.setMinWidth(70);
        cmDiscPer.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<BatchWindowTableDTO, String> cmDiscAmt = new TableColumn<>("Disc.Amt");
        cmDiscAmt.setMaxWidth(70);
        cmDiscAmt.setMinWidth(70);
        cmDiscAmt.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<BatchWindowTableDTO, String> cmBarcode = new TableColumn<>("Barcode");
        cmBarcode.setMaxWidth(150);
        cmBarcode.setMinWidth(150);
        cmBarcode.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<BatchWindowTableDTO, String> cmMargin = new TableColumn<>("Margin %");
        cmMargin.setMaxWidth(70);
        cmMargin.setMinWidth(70);
        cmMargin.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<BatchWindowTableDTO, String> cmFSR_MH = new TableColumn<>("PTR-MH");
        cmFSR_MH.setMaxWidth(94);
        cmFSR_MH.setMinWidth(92);
        cmFSR_MH.setStyle("-fx-alignment: CENTER-RIGHT;");


        TableColumn<BatchWindowTableDTO, String> cmFSR_AI = new TableColumn<>("PTR-AI");
        cmFSR_AI.setMaxWidth(94);
        cmFSR_AI.setMinWidth(94);
        cmFSR_AI.setStyle("-fx-alignment: CENTER-RIGHT;");


        TableColumn<BatchWindowTableDTO, String> cmCSR_MH = new TableColumn<>("CSR-MH");
        cmCSR_MH.setMaxWidth(94);
        cmCSR_MH.setMinWidth(94);
        cmCSR_MH.setStyle("-fx-alignment: CENTER-RIGHT;");


        TableColumn<BatchWindowTableDTO, String> cmCSR_AI = new TableColumn<>("CSR-AI");
        cmCSR_AI.setMaxWidth(94);
        cmCSR_AI.setMinWidth(94);
        cmCSR_AI.setStyle("-fx-alignment: CENTER-RIGHT;");
        TableColumn<BatchWindowTableDTO, String> cmAction = new TableColumn<>("");
        cmAction.setMaxWidth(35.3);
        cmAction.setMinWidth(35.3);

        tableView.getColumns().addAll(cmBatch, cmMFGDate, cmExpiry, cmMrp, cmPurRate, cmQty, cmFree, cmDiscPer, cmDiscAmt, cmBarcode, cmMargin, cmFSR_MH, cmFSR_AI, cmCSR_MH, cmCSR_AI, cmAction);



        TableCellCallback<Object[]> batch_callback = item -> {

            String cstWtoutTax = (String) item[0];
            String cstWtTax = (String) item[1];
            cost_field.setText(cstWtTax);
            costing_field.setText(cstWtoutTax);

            tableView.getItems().get(0).setCost_with_tax(cstWtTax);
            tableView.getItems().get(0).setCost_without_tax(cstWtoutTax);
        };

        cmBatch.setCellValueFactory(cellData -> cellData.getValue().batchNoProperty());
        cmBatch.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmBatch", primaryStage));
        // cmBatch.setId("cmBatch");

        cmMFGDate.setCellValueFactory(cellData -> cellData.getValue().manufacturingDateProperty());
        cmMFGDate.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmMFGDate", primaryStage));

        cmExpiry.setCellValueFactory(cellData -> cellData.getValue().expiryDateProperty());
        cmExpiry.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmExpiry", primaryStage));

        cmMrp.setCellValueFactory(cellData -> cellData.getValue().mrpProperty());
        cmMrp.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmMrp", primaryStage));

        cmPurRate.setCellValueFactory(cellData -> cellData.getValue().purchaseRateProperty());
        cmPurRate.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmPurRate", primaryStage, prdBatchTaxPer, batch_callback));

        cmQty.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        cmQty.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmQty", primaryStage, prdBatchTaxPer, batch_callback));

        cmFree.setCellValueFactory(cellData -> cellData.getValue().freeProperty());
        cmFree.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmFree", primaryStage, prdBatchTaxPer, batch_callback));

        cmDiscPer.setCellValueFactory(cellData -> cellData.getValue().discountPercentageProperty());
        cmDiscPer.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmDiscPer", primaryStage, prdBatchTaxPer, batch_callback));

        cmDiscAmt.setCellValueFactory(cellData -> cellData.getValue().discountAmountProperty());
        cmDiscAmt.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmDiscAmt", primaryStage, prdBatchTaxPer, batch_callback));

        cmBarcode.setCellValueFactory(cellData -> cellData.getValue().barcodeProperty());
        cmBarcode.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmBarcode", primaryStage));

        cmMargin.setCellValueFactory(cellData -> cellData.getValue().marginProperty());
        cmMargin.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmMargin", primaryStage));

        cmFSR_MH.setCellValueFactory(cellData -> cellData.getValue().fsr_mhProperty());
        //  cmFSR_MH.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmFSR_MH", primaryStage));

        cmCSR_MH.setCellValueFactory(cellData -> cellData.getValue().csr_mhProperty());
        // cmCSR_MH.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmCSR_MH", primaryStage));

        cmFSR_AI.setCellValueFactory(cellData -> cellData.getValue().fsr_aiProperty());
        //cmFSR_AI.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmFSR_AI", primaryStage));

        cmCSR_AI.setCellValueFactory(cellData -> cellData.getValue().csr_aiProperty());
        // cmCSR_AI.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmCSR_AI", primaryStage));

        TableCellCallback<Boolean> callbackForClose = item -> {

            List<BatchWindowTableDTO> lst = new ArrayList<>();
            System.out.println("tableView ==->" + tableView.getItems().get(0));
            lst.add(tableView.getItems().get(0));
            callback.accept(lst);
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(Communicator.stage);
        };

        cmAction.setCellValueFactory(cellData -> cellData.getValue().actionProperty());
        cmAction.setCellFactory(column -> new ButtonAndTextFieldTableCell(callbackForClose));

        if (listEdit != null) {
            BatchWindowTableDTO batchWindowTableDTO = new BatchWindowTableDTO();
            System.out.println("batchWindowTableDTO.setCurrentIndex(listEdit.get(0).getCurrentIndex()); : " + listEdit.get(0).getCurrentIndex());
            batchWindowTableDTO.setCurrentIndex(current_string);
            batchWindowTableDTO.setProductId(listEdit.get(0).getProductId());
            batchWindowTableDTO.setLevelAId(listEdit.get(0).getLevelAId());
            batchWindowTableDTO.setLevelBId(listEdit.get(0).getLevelBId());
            batchWindowTableDTO.setLevelCId(listEdit.get(0).getLevelCId());
            batchWindowTableDTO.setUnitId(listEdit.get(0).getUnitId());
            batchWindowTableDTO.setBatchNo(listEdit.get(0).getBatchNo());
            batchWindowTableDTO.setManufacturingDate(listEdit.get(0).getManufacturingDate());
            batchWindowTableDTO.setExpiryDate(listEdit.get(0).getExpiryDate());
            batchWindowTableDTO.setMrp(listEdit.get(0).getMrp());
            batchWindowTableDTO.setPurchaseRate(listEdit.get(0).getPurchaseRate());
            batchWindowTableDTO.setQuantity(listEdit.get(0).getQuantity());
            batchWindowTableDTO.setFree(listEdit.get(0).getFree());
            batchWindowTableDTO.setDiscountPercentage(listEdit.get(0).getDiscountPercentage());
            batchWindowTableDTO.setDiscountAmount(listEdit.get(0).getDiscountAmount());

            batchWindowTableDTO.setBarcode(listEdit.get(0).getBarcode());
            batchWindowTableDTO.setMargin(listEdit.get(0).getMargin());
            batchWindowTableDTO.setFsr_mh(listEdit.get(0).getFsr_mh());
            batchWindowTableDTO.setFsr_ai(listEdit.get(0).getFsr_ai());
            batchWindowTableDTO.setCsr_mh(listEdit.get(0).getCsr_mh());
            batchWindowTableDTO.setCsr_ai(listEdit.get(0).getCsr_ai());
            batchWindowTableDTO.setMrp(listEdit.get(0).getMrp());
            batchWindowTableDTO.setPurchaseRate(listEdit.get(0).getPurchaseRate());
            batchWindowTableDTO.setB_details_id(listEdit.get(0).getB_details_id());
            batchWindowTableDTO.setSupplier_id(listEdit.get(0).getSupplier_id());
            batchWindowTableDTO.setTax(listEdit.get(0).getTax());

            batchWindowTableDTO.setCost_with_tax(listEdit.get(0).getCost_with_tax());
            batchWindowTableDTO.setCost_without_tax(listEdit.get(0).getCost_without_tax());
            tableView.getItems().addAll(batchWindowTableDTO);

            cost_field.setText(listEdit.get(0).getCost_with_tax());
            costing_field.setText(listEdit.get(0).getCost_without_tax());

        } else {

            tableView.getItems().addAll(new BatchWindowTableDTO(current_string, map.get("product_id"), map.get("levelA_id"), map.get("levelB_id"), map.get("levelC_id"), map.get("unit_id"), map.get("tax"), "", "", "", map.get("mrp"), map.get("purchaserate"), "", "", "", "", "", "", map.get("fsrmh"), map.get("csrmh"), map.get("fsrai"), map.get("csrai"), "", map.get("ledger_id"), ""));
        }


        Scene scene = new Scene(borderPane, 1400, 227);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Additional Charges");
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

        primaryStage.centerOnScreen();

        primaryStage.show();


        Platform.runLater(() -> {
            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    primaryStage.close();
                    OverlaysEffect.removeOverlaysEffect(Communicator.stage);
                    event.consume();
                }
            });
        });

        borderPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent eve) -> {
            if (eve.getCode() == KeyCode.ENTER) {
                if (eve.getTarget() instanceof Button targetButton) {
                    System.out.println("Helllllo Hii" + targetButton);
                } else {
                    KeyEvent newEvent = new KeyEvent(
                            null,
                            null,
                            KeyEvent.KEY_PRESSED,
                            "",
                            "\t",
                            KeyCode.TAB,
                            eve.isShiftDown(),
                            eve.isControlDown(),
                            eve.isAltDown(),
                            eve.isMetaDown()
                    );
                    Event.fireEvent(eve.getTarget(), newEvent);
                    eve.consume();
                }
            }
        });
    }

    public static <T> void salesBatchWindow(String current_string, Map<String, String> map, List<BatchWindowTableDTO> listEdit, Consumer<List<BatchWindowTableDTO>> callback) {
        OverlaysEffect.setOverlaysEffect(Communicator.stage);
        Stage primaryStage = new Stage();
        Communicator.stage2 = primaryStage;
        primaryStage.initOwner(Communicator.stage);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        // System.out.println("listEdit "+listEdit);

        System.out.println("Index : " + current_string);


        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/cmptrow_windows.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");
        //  Platform.runLater(() -> borderPane.requestFocus());
        //BorderPan under Top Layout....................................................................................................................
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(1300);
        hbox_top.setMaxWidth(1300);
        hbox_top.setPrefWidth(1300);
        hbox_top.setMaxHeight(40);
        hbox_top.setMinHeight(40);
        hbox_top.setPrefHeight(40);

        Label popup_title = new Label("Batch");
//        Label current_stock = new Label("Current Stock");
//        current_stock.setPrefWidth(200);
//        current_stock.setPrefHeight(30);
//        current_stock.setMinHeight(30);
//        current_stock.setMaxHeight(30);
//        current_stock.setStyle("-fx-background-color: blue; -fx-text-fill: white; -fx-padding: 10px; -fx-background-radius: 10;");


        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(Communicator.stage);
        });
        HBox.setMargin(imageView, new Insets(0, 10, 0, 0));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        //   hbox_top.getChildren().add(current_stock);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #EDF7FF; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 1 0;");

        HBox hbox_top2 = new HBox();
        hbox_top2.setMinWidth(1300);
        hbox_top2.setMaxWidth(1300);
        hbox_top2.setPrefWidth(1300);
        hbox_top2.setMaxHeight(40);
        hbox_top2.setMinHeight(40);
        hbox_top2.setPrefHeight(40);
        hbox_top2.setStyle(" -fx-background-color: #F4EBC8; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 1 0;");
        hbox_top2.setAlignment(Pos.CENTER_LEFT);


        Label product_name = new Label("Product Name: ");
        product_name.setStyle("-fx-font-size: 14; -fx-text-fill: #B97C33; -fx-font-weight: bold;");
        product_name.setPadding(new Insets(0, 10, 0, 5));
        Label product_name_field = new Label();
        product_name_field.setStyle("-fx-font-size: 14; -fx-text-fill: #000; -fx-font-weight: bold;");
        HBox.setMargin(product_name_field, new Insets(0, 250, 0, 5));
        product_name_field.setText(map.get("product_name"));


        Label cost = new Label("Cost With Tax : ");
        cost.setStyle("-fx-font-size: 14; -fx-text-fill: #B97C33; -fx-font-weight: bold;");
        cost.setPadding(new Insets(0, 10, 0, 0));
        Label cost_field = new Label("");
        cost_field.setStyle("-fx-font-size: 14; -fx-text-fill: #000; -fx-font-weight: bold;");
        HBox.setMargin(cost_field, new Insets(0, 120, 0, 5));

        Label costing = new Label("Cost Without Tax : ");
        costing.setStyle("-fx-font-size: 14; -fx-text-fill: #B97C33; -fx-font-weight: bold;");
        costing.setPadding(new Insets(0, 10, 0, 0));
        Label costing_field = new Label("");
        costing_field.setStyle("-fx-font-size: 14; -fx-text-fill: #000; -fx-font-weight: bold;");
        costing_field.setPadding(new Insets(0, 10, 0, 0));
        HBox.setMargin(costing_field, new Insets(0, 120, 0, 5));

        String prdBatchTaxPer = map.get("tax");
        Label tax = new Label("Tax : ");
        tax.setStyle("-fx-font-size: 14; -fx-text-fill: #B97C33; -fx-font-weight: bold;");
        tax.setPadding(new Insets(0, 10, 0, 0));
        Label tax_field = new Label("");
        tax_field.setStyle("-fx-font-size: 14; -fx-text-fill: #000; -fx-font-weight: bold;");
        tax_field.setPadding(new Insets(0, 5, 0, 0));
        tax_field.setText(prdBatchTaxPer);
//        HBox.setMargin(product_name, new Insets(0, 0, 0, 100));


        TableCellCallback<Object[]> batch_callback = item -> {

            String cstWtoutTax = (String) item[0];
            String cstWtTax = (String) item[1];
            cost_field.setText(cstWtTax);
            costing_field.setText(cstWtoutTax);
        };

        hbox_top2.getChildren().addAll(product_name, product_name_field, cost, cost_field, costing, costing_field, tax, tax_field);


        HBox hbox_top3 = new HBox();
        hbox_top3.setMinWidth(1300);
        hbox_top3.setMaxWidth(1300);
        hbox_top3.setPrefWidth(1300);
        hbox_top3.setMaxHeight(35);
        hbox_top3.setMinHeight(35);
        hbox_top3.setPrefHeight(35);
        hbox_top3.setStyle(" -fx-background-color: #EDF7FF; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 1 0;");


        VBox vBox = new VBox();
        vBox.getChildren().addAll(hbox_top, hbox_top2, hbox_top3);

        HBox hbox_top4 = new HBox();
        hbox_top4.setMinWidth(1298);
        hbox_top4.setMaxWidth(1298);
        hbox_top4.setPrefWidth(1298);
        hbox_top4.setMaxHeight(35);
        hbox_top4.setMinHeight(35);
        hbox_top4.setPrefHeight(35);
        hbox_top4.setStyle(" -fx-background-color: white; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 0 0;");

        borderPane.setTop(vBox);
        borderPane.setBottom(hbox_top4);

        TableView<BatchWindowTableDTO> tableView = new TableView<BatchWindowTableDTO>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setFocusTraversable(false);


        tableView.setPrefHeight(75);
        tableView.setMaxHeight(75);
        tableView.setMinHeight(75);
        tableView.setMaxWidth(1300);
        tableView.setMinWidth(1300);
        tableView.setPrefWidth(1300);
        borderPane.setCenter(tableView);

        tableView.setEditable(false);

        Platform.runLater(() -> {
            tableView.getSelectionModel().select(0);
        });


        TableColumn<BatchWindowTableDTO, String> cmBatch = new TableColumn<>("Batch");
        cmBatch.setMaxWidth(150);
        cmBatch.setMinWidth(150);

        TableColumn<BatchWindowTableDTO, String> cmMFGDate = new TableColumn<>("MFG Date");
        cmMFGDate.setMaxWidth(100);
        cmMFGDate.setMinWidth(100);

        TableColumn<BatchWindowTableDTO, String> cmExpiry = new TableColumn<>("Expiry");
        cmExpiry.setMaxWidth(100);
        cmExpiry.setMinWidth(100);

        TableColumn<BatchWindowTableDTO, String> cmMrp = new TableColumn<>("MRP");
        cmMrp.setMaxWidth(80);
        cmMrp.setMinWidth(80);
        cmMrp.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<BatchWindowTableDTO, String> cmPurRate = new TableColumn<>("Pur Rate");
        cmPurRate.setMaxWidth(80);
        cmPurRate.setMinWidth(80);
        cmPurRate.setStyle("-fx-alignment: CENTER-RIGHT;");


        TableColumn<BatchWindowTableDTO, String> cmQty = new TableColumn<>("Qty");
        cmQty.setMaxWidth(60);
        cmQty.setMinWidth(60);
        cmQty.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<BatchWindowTableDTO, String> cmFree = new TableColumn<>("Free");
        cmFree.setMaxWidth(60);
        cmFree.setMinWidth(60);
        cmFree.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<BatchWindowTableDTO, String> cmDiscPer = new TableColumn<>("Disc.%");
        cmDiscPer.setMaxWidth(70);
        cmDiscPer.setMinWidth(70);
        cmDiscPer.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<BatchWindowTableDTO, String> cmDiscAmt = new TableColumn<>("Disc.Amt");
        cmDiscAmt.setMaxWidth(70);
        cmDiscAmt.setMinWidth(70);
        cmDiscAmt.setStyle("-fx-alignment: CENTER-RIGHT;");


        TableColumn<BatchWindowTableDTO, String> cmBarcode = new TableColumn<>("Barcode");
        cmBarcode.setMaxWidth(140);
        cmBarcode.setMinWidth(140);


        TableColumn<BatchWindowTableDTO, String> cmMargin = new TableColumn<>("Margin %");
        cmMargin.setMaxWidth(80);
        cmMargin.setMinWidth(80);
        cmMargin.setMaxWidth(70);
        cmMargin.setMinWidth(70);
        cmMargin.setStyle("-fx-alignment: CENTER-RIGHT;");


        TableColumn<BatchWindowTableDTO, String> cmFSR_MH = new TableColumn<>("Rate-1");
        cmFSR_MH.setMaxWidth(86);
        cmFSR_MH.setMinWidth(82);
        cmFSR_MH.setStyle("-fx-alignment: CENTER-RIGHT;");


        TableColumn<BatchWindowTableDTO, String> cmFSR_AI = new TableColumn<>("Rate-2");
        cmFSR_AI.setMaxWidth(86);
        cmFSR_AI.setMinWidth(86);


        TableColumn<BatchWindowTableDTO, String> cmCSR_MH = new TableColumn<>("Rate-3");
        cmCSR_MH.setMaxWidth(86);
        cmCSR_MH.setMinWidth(86);

//        TableColumn<BatchWindowTableDTO, String> cmCSR_AI = new TableColumn<>("CSR-AI");
//        cmCSR_AI.setMaxWidth(94);
//        cmCSR_AI.setMinWidth(94);

        TableColumn<BatchWindowTableDTO, String> cmAction = new TableColumn<>("");
        cmAction.setMaxWidth(52.3);
        cmAction.setMinWidth(52.3);

        tableView.getColumns().addAll(cmBatch, cmMFGDate, cmExpiry, cmMrp, cmPurRate, cmQty, cmFree, cmDiscPer, cmDiscAmt, cmBarcode, cmMargin, cmFSR_MH, cmFSR_AI, cmCSR_MH, cmAction);


        cmBatch.setCellValueFactory(cellData -> cellData.getValue().batchNoProperty());
        cmBatch.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmBatch", primaryStage));
        // cmBatch.setId("cmBatch");

        cmMFGDate.setCellValueFactory(cellData -> cellData.getValue().manufacturingDateProperty());
        cmMFGDate.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmMFGDate", primaryStage));

        cmExpiry.setCellValueFactory(cellData -> cellData.getValue().expiryDateProperty());
        cmExpiry.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmExpiry", primaryStage));

        cmMrp.setCellValueFactory(cellData -> cellData.getValue().mrpProperty());
        cmMrp.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmMrp", primaryStage));

        cmPurRate.setCellValueFactory(cellData -> cellData.getValue().purchaseRateProperty());
        cmPurRate.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmPurRate", primaryStage, prdBatchTaxPer, batch_callback));

        cmQty.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        cmQty.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmQty", primaryStage, prdBatchTaxPer, batch_callback));

        cmFree.setCellValueFactory(cellData -> cellData.getValue().freeProperty());
        cmFree.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmFree", primaryStage, prdBatchTaxPer, batch_callback));

        cmDiscPer.setCellValueFactory(cellData -> cellData.getValue().discountPercentageProperty());
        cmDiscPer.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmDiscPer", primaryStage, prdBatchTaxPer, batch_callback));

        cmDiscAmt.setCellValueFactory(cellData -> cellData.getValue().discountAmountProperty());
        cmDiscAmt.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmDiscAmt", primaryStage, prdBatchTaxPer, batch_callback));

        cmBarcode.setCellValueFactory(cellData -> cellData.getValue().barcodeProperty());
        cmBarcode.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmBarcode", primaryStage));

        cmMargin.setCellValueFactory(cellData -> cellData.getValue().marginProperty());
        cmMargin.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmMargin", primaryStage));

        cmFSR_MH.setCellValueFactory(cellData -> cellData.getValue().fsr_mhProperty());
        //  cmFSR_MH.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmFSR_MH", primaryStage));

        cmCSR_MH.setCellValueFactory(cellData -> cellData.getValue().csr_mhProperty());
        // cmCSR_MH.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmCSR_MH", primaryStage));

        cmFSR_AI.setCellValueFactory(cellData -> cellData.getValue().fsr_aiProperty());
        //cmFSR_AI.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmFSR_AI", primaryStage));

//        cmCSR_AI.setCellValueFactory(cellData -> cellData.getValue().csr_aiProperty());
        // cmCSR_AI.setCellFactory(column -> new TextFieldTableCellForCmpTRow("cmCSR_AI", primaryStage));

        TableCellCallback<Boolean> callbackForClose = item -> {
            List<BatchWindowTableDTO> lst = new ArrayList<>();
            lst.add(tableView.getItems().get(0));
            callback.accept(lst);
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(Communicator.stage);
        };

        cmAction.setCellValueFactory(cellData -> cellData.getValue().actionProperty());
        cmAction.setCellFactory(column -> new ButtonAndTextFieldTableCell(callbackForClose));

        if (listEdit != null) {
            BatchWindowTableDTO batchWindowTableDTO = new BatchWindowTableDTO();
            System.out.println("batchWindowTableDTO.setCurrentIndex(listEdit.get(0).getCurrentIndex()); : " + listEdit.get(0).getCurrentIndex());
            batchWindowTableDTO.setCurrentIndex(listEdit.get(0).getCurrentIndex());
            batchWindowTableDTO.setProductId(listEdit.get(0).getProductId());
            batchWindowTableDTO.setLevelAId(listEdit.get(0).getLevelAId());
            batchWindowTableDTO.setLevelBId(listEdit.get(0).getLevelBId());
            batchWindowTableDTO.setLevelCId(listEdit.get(0).getLevelCId());
            batchWindowTableDTO.setUnitId(listEdit.get(0).getUnitId());
            batchWindowTableDTO.setBatchNo(listEdit.get(0).getBatchNo());
            batchWindowTableDTO.setManufacturingDate(listEdit.get(0).getManufacturingDate());
            batchWindowTableDTO.setExpiryDate(listEdit.get(0).getExpiryDate());
            batchWindowTableDTO.setMrp(listEdit.get(0).getMrp());
            batchWindowTableDTO.setPurchaseRate(listEdit.get(0).getPurchaseRate());
            batchWindowTableDTO.setQuantity(listEdit.get(0).getQuantity());
            batchWindowTableDTO.setFree(listEdit.get(0).getFree());
            batchWindowTableDTO.setDiscountPercentage(listEdit.get(0).getDiscountPercentage());
            batchWindowTableDTO.setDiscountAmount(listEdit.get(0).getDiscountAmount());

            batchWindowTableDTO.setBarcode(listEdit.get(0).getBarcode());
            batchWindowTableDTO.setMargin(listEdit.get(0).getMargin());
            batchWindowTableDTO.setFsr_mh(listEdit.get(0).getFsr_mh());
            batchWindowTableDTO.setFsr_ai(listEdit.get(0).getFsr_ai());
            batchWindowTableDTO.setCsr_mh(listEdit.get(0).getCsr_mh());
            batchWindowTableDTO.setCsr_ai(listEdit.get(0).getCsr_ai());
            batchWindowTableDTO.setMrp(listEdit.get(0).getMrp());
            batchWindowTableDTO.setPurchaseRate(listEdit.get(0).getPurchaseRate());
            batchWindowTableDTO.setB_details_id(listEdit.get(0).getB_details_id());
            batchWindowTableDTO.setSupplier_id(listEdit.get(0).getSupplier_id());
            batchWindowTableDTO.setTax(listEdit.get(0).getTax());

            tableView.getItems().addAll(batchWindowTableDTO);

        } else {
            tableView.getItems().addAll(new BatchWindowTableDTO(current_string, map.get("product_id"), map.get("levelA_id"), map.get("levelB_id"), map.get("levelC_id"), map.get("unit_id"), map.get("tax"), "", "", "", map.get("mrp"), map.get("purchaserate"), "", "", "", "", "", "", map.get("fsrmh"), map.get("csrmh"), map.get("fsrai"), map.get("csrai"), "", map.get("ledger_id"), ""));
        }


//        double width = TranxCommonPopUps.calculatePopUpWidth();
        Scene scene = new Scene(borderPane, 1300, 227);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Additional Charges");
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

        primaryStage.centerOnScreen();

        primaryStage.show();


        Platform.runLater(() -> {
            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    primaryStage.close();
                    OverlaysEffect.removeOverlaysEffect(Communicator.stage);
                    event.consume();
                }
            });
        });

        borderPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent eve) -> {
            if (eve.getCode() == KeyCode.ENTER) {
                if (eve.getTarget() instanceof Button targetButton) {
                    System.out.println("Helllllo Hii" + targetButton);
                } else {
                    KeyEvent newEvent = new KeyEvent(
                            null,
                            null,
                            KeyEvent.KEY_PRESSED,
                            "",
                            "\t",
                            KeyCode.TAB,
                            eve.isShiftDown(),
                            eve.isControlDown(),
                            eve.isAltDown(),
                            eve.isMetaDown()
                    );
                    Event.fireEvent(eve.getTarget(), newEvent);
                    eve.consume();
                }
            }
        });
    }

}

class TextFieldTableCellForCmpTRow extends TableCell<BatchWindowTableDTO, String> {
    private TextField textField;
    private String columnName;
    private TableCellCallback<Object[]> batch_callback;

    private Stage stage;


    public TextFieldTableCellForCmpTRow(String columnName, Stage stage) {
        this.stage = stage;


        this.columnName = columnName;
        this.textField = new TextField();

        this.textField.setOnAction(event -> commitEdit(textField.getText()));

        this.textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit(textField.getText());
            }
        });


        textfieldStyle();
        textfieldPromtText("", batch_callback);

    }

    public TextFieldTableCellForCmpTRow(String columnName, Stage stage, String prdBatchTaxPer, TableCellCallback<Object[]> callback) {

        this.stage = stage;
        this.batch_callback = callback;
        this.columnName = columnName;
        this.textField = new TextField();

        this.textField.setOnAction(event -> commitEdit(textField.getText()));

        this.textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit(textField.getText());
            }
        });


        textfieldStyle();
        textfieldPromtText(prdBatchTaxPer, callback);

    }

    public void textfieldPromtText(String prdBatchTax, TableCellCallback<Object[]> callback) {
        if ("cmBatch".equals(columnName)) {
            Platform.runLater(() -> {
                getTableView().edit(0, getTableView().getColumns().get(0));
                textField.requestFocus();
            });
            textField.setPromptText("Batch No");
            this.textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    if (!textField.getText().isEmpty()) {
                        commitEdit(textField.getText());
                    } else {
                        textField.requestFocus();
                        event.consume();
                    }
                }
                if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                    textField.requestFocus();
                }
            });
        }
        if ("cmMFGDate".equals(columnName)) {
            textField.setPromptText("DD/MM/YYYY");
            DateValidator.applyDateFormat(textField);
            this.textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    LocalDate currentDate = LocalDate.now();
                    String mfDate = textField.getText();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate mfgDate = null;
                    if (!mfDate.isEmpty()) {
                        mfgDate = LocalDate.parse(mfDate, formatter);
                    }
                    if (!textField.getText().isEmpty()) {
                        if (currentDate.isBefore(mfgDate)) {
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "MFG Date Should be Valid", input -> {
                                textField.requestFocus();
                            });
                        }
                    }
                    event.consume();
                    commitEdit(textField.getText());
                }
                if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                    LocalDate currentDate = LocalDate.now();
                    String mfDate = textField.getText();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate mfgDate = null;
                    if (!mfDate.isEmpty()) {
                        mfgDate = LocalDate.parse(mfDate, formatter);
                    }
                    if (!textField.getText().isEmpty()) {
                        if (currentDate.isBefore(mfgDate)) {
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "MFG Date Should be Valid", input -> {
                                textField.requestFocus();
                            });
                        }
                    }
                    event.consume();
                    commitEdit(textField.getText());
                }
            });
        }
        if ("cmExpiry".equals(columnName)) {
            textField.setPromptText("DD/MM/YYYY");
            DateValidator.applyDateFormat(textField);
            this.textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    event.consume();
                    commitEdit(textField.getText());
                    if (!textField.getText().isEmpty()) {
                        LocalDate currentDate = LocalDate.now();
                        String mfDate = getTableRow().getItem().getManufacturingDate();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate mfgDate = null;
                        if (!mfDate.isEmpty())
                            mfgDate = LocalDate.parse(mfDate, formatter);
                        String exDate = textField.getText();
                        LocalDate expDate = LocalDate.parse(exDate, formatter);
                        if (mfgDate != null && expDate.isBefore(mfgDate)) {
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Expiry Date Cannot be Before Manufacturing Date. ", input -> {
                                textField.requestFocus();
                            });
                        } else if (currentDate.isAfter(expDate)) {
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "The Product has Expired. ", input -> {
                                textField.requestFocus();
                            });
                        } else {
//                            long daysUntilExpiryAlert = ChronoUnit.DAYS.between(currentDate, expDate);
//                            if (daysUntilExpiryAlert < 30) {
//                                String message = "The Product Will Expire in " + daysUntilExpiryAlert + " Days. ";
//                                AlertUtility.AlertWarningTimeout(AlertUtility.alertTypeWarning, message, input -> {
//                                });
//                            }
//                            TableTraversal.next(getIndex(), getTableView().getColumns().indexOf(getTableColumn()), 1, getTableView());
                        }
                    }
                }
                if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                    event.consume();
                    commitEdit(textField.getText());
                    if (!textField.getText().isEmpty()) {
                        LocalDate currentDate = LocalDate.now();
                        String mfDate = getTableRow().getItem().getManufacturingDate();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate mfgDate = LocalDate.parse(mfDate, formatter);
                        String exDate = textField.getText();
                        LocalDate expDate = LocalDate.parse(exDate, formatter);
                        if (expDate.isBefore(mfgDate)) {
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Expiry Date Cannot be Before Manufacturing Date. ", input -> {
                                textField.requestFocus();
                            });
                        } else if (currentDate.isAfter(expDate)) {
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "The Product has Expired. ", input -> {
                                textField.requestFocus();
                            });
                        } else {
//                            long daysUntilExpiryAlert = ChronoUnit.DAYS.between(currentDate, expDate);
//                            if (daysUntilExpiryAlert < 30) {
//                                String message = "The Product has Expiring in " + daysUntilExpiryAlert + " Days. ";
//                                AlertUtility.AlertWarningTimeout(AlertUtility.alertTypeWarning, message, input -> {
//                                });
//                            }
//                            TableTraversal.previous(getIndex(), getTableView().getColumns().indexOf(getTableColumn()), 1, getTableView());
                        }
                    }

                }
            });
        }
        if ("cmMrp".equals(columnName)) {
            textField.setPromptText("0.00");
            CommonValidationsUtils.restrictToDecimalNumbers(textField);
            this.textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    event.consume();
                    commitEdit(textField.getText());
                }
                if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                    event.consume();
                    commitEdit(textField.getText());
                }
                ;
            });
        }
        if ("cmPurRate".equals(columnName)) {
            textField.setPromptText("0.00");
            CommonValidationsUtils.restrictToDecimalNumbers(textField);
            this.textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    event.consume();
                    commitEdit(textField.getText());
                    if (!textField.getText().isEmpty()) {
                        Double mrp = Double.parseDouble(getTableRow().getItem().getMrp());
                        Double purRateAmt = Double.parseDouble(textField.getText());
                        if (purRateAmt > mrp) {
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Purchase Rate Should not be Greater Than MRP ", input -> {
                                textField.requestFocus();
                            });
                        }
//                        else {
//                            costingCalculation(prdBatchTax, callback);
//                        }
                    }
                }
                if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                    event.consume();
                    commitEdit(textField.getText());
                    if (!textField.getText().isEmpty()) {
                        Double mrp = Double.parseDouble(getTableRow().getItem().getMrp());
                        Double purRateAmt = Double.parseDouble(textField.getText());
                        if (purRateAmt > mrp) {
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Purchase Rate Should not be Greater Than MRP ", input -> {
                                textField.requestFocus();
                            });
                        }
                    }
//                    else {
//                        event.consume();
//                        costingCalculation(prdBatchTax, callback);
//                    }
                }
            });
        }
        if ("cmQty".equals(columnName)) {
            textField.setPromptText("0");
            CommonValidationsUtils.restrictToDecimalNumbers(textField);
            this.textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    if (!textField.getText().isEmpty()) {
                        event.consume();
                        commitEdit(textField.getText());
                        costingCalculation(prdBatchTax, callback);
                    } else {
                        textField.requestFocus();
                    }
                }
                if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                    if (!textField.getText().isEmpty()) {
                        event.consume();
                        commitEdit(textField.getText());
                        costingCalculation(prdBatchTax, callback);
                    }
                }
            });
        }
        if ("cmFree".equals(columnName)) {
            textField.setPromptText("0");
            CommonValidationsUtils.restrictToDecimalNumbers(textField);
            this.textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    event.consume();
                    commitEdit(textField.getText());
                    if (!textField.getText().isEmpty()) {
                        costingCalculation(prdBatchTax, callback);
                    }
                }
                if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                    event.consume();
                    commitEdit(textField.getText());
                    if (!textField.getText().isEmpty()) {
                        costingCalculation(prdBatchTax, callback);
                    }
                }
            });
        }
        if ("cmDiscPer".equals(columnName)) {
            textField.setPromptText("0.0");
            CommonValidationsUtils.restrictToDecimalNumbers(textField);
            this.textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    event.consume();
                    commitEdit(textField.getText());
                    costingCalculation(prdBatchTax, callback);
//                    TableTraversal.next(getIndex(), getTableView().getColumns().indexOf(getTableColumn()), 1, getTableView());
                }
                if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                    event.consume();
                    commitEdit(textField.getText());
                    costingCalculation(prdBatchTax, callback);
//                    TableTraversal.previous(getIndex(), getTableView().getColumns().indexOf(getTableColumn()), 1, getTableView());
                }
            });
        }
        if ("cmDiscAmt".equals(columnName)) {
            textField.setPromptText("0.00");
            CommonValidationsUtils.restrictToDecimalNumbers(textField);
            this.textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    event.consume();
                    commitEdit(textField.getText());
                    costingCalculation(prdBatchTax, callback);
//                    TableTraversal.next(getIndex(), getTableView().getColumns().indexOf(getTableColumn()), 1, getTableView());
                }
                if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                    event.consume();
                    commitEdit(textField.getText());
                    costingCalculation(prdBatchTax, callback);
//                    TableTraversal.previous(getIndex(), getTableView().getColumns().indexOf(getTableColumn()), 1, getTableView());
                }
            });
        }
        if ("cmBarcode".equals(columnName)) {
            textField.setPromptText("Barcode");
            CommonValidationsUtils.restrictToNumbers(textField);
            this.textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    event.consume();
                    commitEdit(textField.getText());
//                    TableTraversal.next(getIndex(), getTableView().getColumns().indexOf(getTableColumn()), 1, getTableView());
                }
                if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                    event.consume();
                    commitEdit(textField.getText());
//                    TableTraversal.previous(getIndex(), getTableView().getColumns().indexOf(getTableColumn()), 1, getTableView());
                }
            });
        }
        if ("cmMargin".equals(columnName)) {
            textField.setPromptText("0.0");
            CommonValidationsUtils.restrictToDecimalNumbers(textField);
            this.textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    event.consume();
                    commitEdit(textField.getText());
//                    TableTraversal.next(getIndex(), getTableView().getColumns().indexOf(getTableColumn()), 1, getTableView());
                }
                if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                    event.consume();
                    commitEdit(textField.getText());
//                    TableTraversal.previous(getIndex(), getTableView().getColumns().indexOf(getTableColumn()), 1, getTableView());
                }
            });
        }
        if ("cmFSR_AI".equals(columnName)) {
            textField.setPromptText("0.00");
            this.textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    event.consume();
                    commitEdit(textField.getText());
//                    TableTraversal.next(getIndex(), getTableView().getColumns().indexOf(getTableColumn()), 1, getTableView());
                }
                if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                    event.consume();
                    commitEdit(textField.getText());
//                    TableTraversal.previous(getIndex(), getTableView().getColumns().indexOf(getTableColumn()), 1, getTableView());
                }
            });
        }
        if ("cmCSR_AI".equals(columnName)) {
            textField.setPromptText("0.00");
            this.textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    event.consume();
                    commitEdit(textField.getText());
//                    TableTraversal.next(getIndex(), getTableView().getColumns().indexOf(getTableColumn()), 1, getTableView());
                }
                if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                    event.consume();
                    commitEdit(textField.getText());
//                    TableTraversal.previous(getIndex(), getTableView().getColumns().indexOf(getTableColumn()), 1, getTableView());
                }
            });
        }
        if ("cmFSR_MH".equals(columnName)) {
            textField.setPromptText("0.00");
            this.textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    event.consume();
                    commitEdit(textField.getText());
//                    TableTraversal.next(getIndex(), getTableView().getColumns().indexOf(getTableColumn()), 1, getTableView());
                }
                if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                    event.consume();
                    commitEdit(textField.getText());
//                    TableTraversal.previous(getIndex(), getTableView().getColumns().indexOf(getTableColumn()), 1, getTableView());
                }
            });
        }
        if ("cmCSR_MH".equals(columnName)) {
            textField.setPromptText("0.00");
            this.textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    event.consume();
                    commitEdit(textField.getText());
//                    TableTraversal.next(getIndex(), getTableView().getColumns().indexOf(getTableColumn()), 1, getTableView());
                }
                if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                    event.consume();
                    commitEdit(textField.getText());
//                    TableTraversal.previous(getIndex(), getTableView().getColumns().indexOf(getTableColumn()), 1, getTableView());
                }
            });
        }

    }

    // Costing Calculations
    private void costingCalculation(String prdBatchTaxPer, TableCellCallback<Object[]> callback) {
        System.out.println("prdBatchTaxPer Hello >> : " + prdBatchTaxPer);
        ObservableList<BatchWindowTableDTO> tableView2Items1 = getTableView().getItems();
        String b_qtyText = "";
        String b_freeQtyText = "";
        String b_dis_amtText = "";
        String b_dis_perText = "";
        String b_rateText = "";
        for (BatchWindowTableDTO person : tableView2Items1) {
            System.out.println("person => " + person);
            b_qtyText = person.getQuantity();
            b_freeQtyText = person.getFree();
            b_dis_amtText = person.getDiscountAmount();
            b_dis_perText = person.getDiscountPercentage();
            b_rateText = person.getPurchaseRate();

        }
        String tax_perText = prdBatchTaxPer;
        System.out.println("Qty:: " + b_qtyText + " purRate :" + b_rateText + "Dis Per :" + b_dis_perText + "Dis Amt:" + b_dis_perText + " taxPer ::" + tax_perText);

        double b_qty = Double.parseDouble(b_qtyText);
        double b_freeQty = b_freeQtyText.isEmpty() ? 0 : Double.parseDouble(b_freeQtyText);
        double totalQty = b_qty + b_freeQty;

        double disPerAmt = 0;
        double totaldisAmt = 0;

        double b_rate = Double.parseDouble(b_rateText); // You'll need to get b_rate value from somewhere
        double totalAmt = b_rate * b_qty;

        double disPer = b_dis_perText.isEmpty() ? 0 : Double.parseDouble(b_dis_perText);
//        disPerAmt = (disPer * b_rate) / 100;
        disPerAmt = GlobalTranx.TranxCalculatePer(totalAmt, disPer);
        totaldisAmt += disPerAmt;

        double disAmt = b_dis_amtText.isEmpty() ? 0 : Double.parseDouble(b_dis_amtText);
        totaldisAmt += disAmt;

        System.out.println("totaldisAmt" + totaldisAmt);


        double costVal = (totalAmt - totaldisAmt) / totalQty;
        Double costWtoutTax = costVal;


        double taxAmt = (Double.parseDouble(tax_perText) * totalAmt) / 100;

        double costWithTax = costVal + (taxAmt / totalQty);
        Double costWithTaxValue = costWithTax;

        if (callback != null) {

            Object[] object = new Object[10];

            object[0] = String.format("%.2f", costWtoutTax);

            object[1] = String.format("%.2f", costWithTaxValue);

            if (callback != null) {
                callback.call(object);
            }
        }

    }

    public void next() {
        final int col = getTableView().getColumns().indexOf(getTableColumn());
        TableColumn<BatchWindowTableDTO, ?> colName = getTableView().getColumns().get(col + 1);
        getTableView().edit(0, colName);
    }

    public void previous() {
        final int col = getTableView().getColumns().indexOf(getTableColumn());
        TableColumn<BatchWindowTableDTO, ?> colName = getTableView().getColumns().get(col - 1);
        getTableView().edit(0, colName);
    }


    public void textfieldStyle() {
        textField.setStyle("-fx-alignment: CENTER-RIGHT;-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");

        textField.setPrefHeight(38);
        textField.setMaxHeight(38);
        textField.setMinHeight(38);


        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                textField.setStyle("-fx-alignment: CENTER-RIGHT;-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #00a0f5;");
            } else {
                textField.setStyle("-fx-alignment: CENTER-RIGHT;-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #f6f6f9;");
            }
        });
    }


    @Override
    public void startEdit() {
        super.startEdit();
        setText(null);
        setGraphic(new VBox(textField));
        textField.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText((String) getItem());
        VBox vbox = new VBox(textField);
        setGraphic(vbox);
    }


    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
//        System.out.println("itemmm & emptyyy in updateItem-->> "+item+" "+empty);
        if (empty) {
            VBox vbox = new VBox(textField);
            setGraphic(vbox);
        } else {
            VBox vbox = new VBox(textField);
            vbox.setAlignment(Pos.CENTER);

            textField.setText(item);
            setGraphic(vbox);
        }
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        if (columnName.equals("cmBatch")) {
            TableRow<BatchWindowTableDTO> row = getTableRow();
            if (row != null) {
                BatchWindowTableDTO item = row.getItem();
                if (item != null) {
                    item.setBatchNo(newValue.isEmpty() ? "" : newValue);
                }
            }
        } else if (columnName.equals("cmMFGDate")) {
            (getTableRow().getItem()).setManufacturingDate(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("cmExpiry")) {
            (getTableRow().getItem()).setExpiryDate(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("cmMrp")) {
            (getTableRow().getItem()).setMrp(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("cmPurRate")) {
            (getTableRow().getItem()).setPurchaseRate(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("cmQty")) {
            (getTableRow().getItem()).setQuantity(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("cmFree")) {
            (getTableRow().getItem()).setFree(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("cmDiscPer")) {
            (getTableRow().getItem()).setDiscountPercentage(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("cmDiscAmt")) {
            (getTableRow().getItem()).setDiscountAmount(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("cmBarcode")) {
            (getTableRow().getItem()).setBarcode(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("cmMargin")) {
            (getTableRow().getItem()).setMargin(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("cmFSR_AI")) {
            (getTableRow().getItem()).setFsr_ai(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("cmCSR_AI")) {
            (getTableRow().getItem()).setCsr_ai(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("cmFSR_MH")) {
            (getTableRow().getItem()).setFsr_mh(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("cmCSR_MH")) {
            (getTableRow().getItem()).setCsr_mh(newValue.isEmpty() ? "" : newValue);
        }
    }
}


class ButtonAndTextFieldTableCell extends TableCell<BatchWindowTableDTO, String> {


    private Button add;

    TableCellCallback<Boolean> callback;

    public ButtonAndTextFieldTableCell(TableCellCallback<Boolean> callback) {

        this.callback = callback;

        this.add = addButtonWithImage();

        add.setOnAction(event -> {
            System.out.println("Details Id >> " + getTableRow().getItem().getB_details_id());
            createBatch();
        });

    }

    private void createBatch() {
        Map<String, String> map = new HashMap<>();
        System.out.println("Batch table Row-->" + getTableView().getItems().get(0).toString());
        map.put("product_id", getTableRow().getItem().getProductId());
        map.put("level_a_id", getTableRow().getItem().getLevelAId());
        map.put("level_b_id", getTableRow().getItem().getLevelBId());
        map.put("level_c_id", getTableRow().getItem().getLevelCId());
        map.put("unit_id", getTableRow().getItem().getUnitId());
//        pur_date
//        map.put("pur_date", Communicator.purTranxDate.isEmpty() || Communicator.purTranxDate.equalsIgnoreCase(null) ? "" : String.valueOf(Communicator.text_to_date.fromString(Communicator.purTranxDate)));
        map.put("pur_date", (Communicator.purTranxDate == null || Communicator.purTranxDate.isEmpty()) ? "" : String.valueOf(Communicator.text_to_date.fromString(Communicator.purTranxDate)));
        map.put("manufacturing_date", getTableRow().getItem().getManufacturingDate().isEmpty() ? "" : Communicator.text_to_date.fromString(getTableRow().getItem().getManufacturingDate()).toString());
        map.put("b_expiry", getTableRow().getItem().getExpiryDate().isEmpty() ? "" : Communicator.text_to_date.fromString(getTableRow().getItem().getExpiryDate()).toString());
        map.put("mrp", getTableRow().getItem().getMrp());
        map.put("supplier_id", getTableRow().getItem().getSupplier_id());
        map.put("b_rate", getTableRow().getItem().getPurchaseRate());
        Double d_qty = Double.parseDouble(getTableRow().getItem().getQuantity());
        int int_qty = d_qty.intValue();
        map.put("b_qty", int_qty > 0 ? int_qty + "" : "0");
        map.put("b_freeQty", getTableRow().getItem().getFree());
        map.put("b_dis_per", getTableRow().getItem().getDiscountPercentage());
        map.put("b_dis_amt", getTableRow().getItem().getDiscountAmount());
        map.put("barcode", getTableRow().getItem().getBarcode());
        map.put("margin", getTableRow().getItem().getMargin());
        map.put("b_rate_a", getTableRow().getItem().getFsr_mh());
        map.put("b_rate_b", getTableRow().getItem().getCsr_mh());
        map.put("b_rate_c", getTableRow().getItem().getFsr_ai());
        map.put("b_rate_d", getTableRow().getItem().getCsr_ai());
        map.put("b_no", getTableRow().getItem().getBatchNo());

        Double costing = 0.0;
        Double pur_rate = getTableRow().getItem().getPurchaseRate().isEmpty() ? 0.0 : Double.parseDouble(getTableRow().getItem().getPurchaseRate());
        Double dis_amt = getTableRow().getItem().getDiscountAmount().isEmpty() ? 0.0 : Double.parseDouble(getTableRow().getItem().getDiscountAmount());
        Double dis_per = getTableRow().getItem().getDiscountPercentage().isEmpty() ? 0.0 : Double.parseDouble(getTableRow().getItem().getDiscountPercentage());
        Double tax = getTableRow().getItem().getTax().isEmpty() ? 0.0 : Double.parseDouble(getTableRow().getItem().getTax());
        costing = pur_rate - dis_amt;
        costing = costing - (costing * dis_per) / 100.0;

        Double costingWithTax = 0.0;
        costingWithTax = costing + (costing * tax) / 100.0;

        map.put("costing", String.valueOf(costing));
        map.put("costingWithTax", String.valueOf(costingWithTax));
        map.put("b_details_id", getTableRow().getItem().getB_details_id().isEmpty() ? String.valueOf(0) : getTableRow().getItem().getB_details_id());

//        System.out.println("batch map: " + map);


        try {
            String formdata = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = null;
            if (getTableRow().getItem().getB_details_id().isEmpty()) {
                response = APIClient.postFormDataRequest(formdata, "create_batch_details");
            } else {
                response = APIClient.postFormDataRequest(formdata, "edit_batch_details");
            }
            String responseBody = response.body();
            JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);

            int responseStatus = jsonObject.get("responseStatus").getAsInt();
            String message = jsonObject.get("message").getAsString();
            if (responseStatus == 200) {
                if (getTableRow().getItem().getB_details_id().isEmpty()) {
//                    System.out.println(jsonObject.get("Data").getAsString());
                    getTableRow().getItem().setB_details_id(jsonObject.get("Data").getAsString());
                }
                System.out.println("batch creation or updation " +  message);
                AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                    if (callback != null) {
                        callback.call(true);
                    }
                });
            } else {
                System.out.println(" batch responseObject is null and " + message );
//                AlertUtility.AlertDialogForErrorWithStage(Communicator.stage2, "Error", message, input -> {
//                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private Button addButtonWithImage() {
        ImageView imageView = new ImageView(new Image(GenivisApplication.class.getResourceAsStream("/com/opethic/genivis/ui/assets/add3.png")));
        imageView.setFitWidth(26);
        imageView.setFitHeight(26);
        Button button = new Button();
        button.setMaxHeight(38);
        button.setPrefHeight(38);
        button.setMinHeight(38);
        button.setPrefWidth(35);
        button.setMaxWidth(35);
        button.setMinWidth(35);
        button.setGraphic(imageView);


        button.setStyle("-fx-background-color: #f6f6f9; -fx-border-width: 1; -fx-border-color:  #666699;");
        button.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                button.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #00a0f5;");
            } else {
                button.setStyle("-fx-background-color: #f6f6f9; -fx-border-width: 1; -fx-border-color:  #666699;");
            }
        });
        return button;
    }

    private Button updateButtonWithImage() {
        ImageView imageView = new ImageView(new Image(GenivisApplication.class.getResourceAsStream("/com/opethic/genivis/ui/assets/update2.png")));
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        Button button = new Button();
        button.setMaxHeight(38);
        button.setPrefHeight(38);
        button.setMinHeight(38);
        button.setPrefWidth(35);
        button.setMaxWidth(35);
        button.setMinWidth(35);
        button.setGraphic(imageView);


        button.setStyle("-fx-background-color: #f6f6f9; -fx-border-width: 1; -fx-border-color:  #666699;");
        button.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                button.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #00a0f5;");
            } else {
                button.setStyle("-fx-background-color: #f6f6f9; -fx-border-width: 1; -fx-border-color:  #666699;");
            }
        });
        return button;
    }

    @Override
    public void startEdit() {
        super.startEdit();
        setText(null);
        System.out.println("r&d");
        setGraphic(new VBox(add));
        add.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText((String) getItem());

    }


    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(new VBox(add));
        } else {
            HBox vbox = new HBox(add);
            vbox.setAlignment(Pos.CENTER);
            setGraphic(vbox);
        }
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);

    }

}

