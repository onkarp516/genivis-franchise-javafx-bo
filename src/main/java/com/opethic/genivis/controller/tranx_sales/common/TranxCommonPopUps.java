package com.opethic.genivis.controller.tranx_sales.common;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.commons.OverlaysEffect;
import com.opethic.genivis.dto.TranxProductWindowDTO;
import com.opethic.genivis.dto.pur_invoice.PurchaseInvoiceTable;
import com.opethic.genivis.dto.reqres.sales_tranx.CounterSaleRowDTO;
import com.opethic.genivis.dto.reqres.sales_tranx.SalesInvoiceTable;
import com.opethic.genivis.dto.reqres.tranx.sales.invoice.*;
import com.opethic.genivis.dto.tranx_sales.CmpTRowDTOSoToSc;
import com.opethic.genivis.models.tranx.sales.*;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.DateConvertUtil;
import com.opethic.genivis.utils.GlobalTranx;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class TranxCommonPopUps {

    private static final org.apache.logging.log4j.Logger loggerTranxCommonPopup = LogManager.getLogger(TranxCommonPopUps.class);
    private static ObservableList<TranxSelectedProduct> lstProducts = FXCollections.observableArrayList();

    private static ObservableList<TranxSelectedBatch> lstBatch = FXCollections.observableArrayList();

    public static double calculatePopUpHeight() {
        return GlobalTranx.TranxCalculatePer(Screen.getPrimary().getVisualBounds().getHeight(), 65.0);
    }

    public static double calculatePopUpWidth() {
        return GlobalTranx.TranxCalculatePer(Screen.getPrimary().getVisualBounds().getWidth(), 80.0);
    }

    public static double calculatePopUpWidth(Double per) {
        return GlobalTranx.TranxCalculatePer(Screen.getPrimary().getVisualBounds().getWidth(), per);
    }

    public static double calculatePopUpX() {
        return GlobalTranx.TranxCalculatePer(Screen.getPrimary().getVisualBounds().getWidth(), 10.0);
    }

    public static double calculatePopUpY() {
        return GlobalTranx.TranxCalculatePer(Screen.getPrimary().getVisualBounds().getHeight(), 15.0);
    }

    public static <T> void openProductPopUp(Stage stage, Integer prodId, String title, Consumer<TranxSelectedProduct> callback, Consumer<Boolean> addPrdCallback) {
        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();
        ObservableList<TranxProductWindowDTO> observableLedgerList = FXCollections.observableArrayList();
        primaryStage.initOwner(stage); // Set the owner stage
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");
        Platform.runLater(() -> borderPane.requestFocus());
        //BorderPan under Top Layout....................................................................................................................
        VBox vbox_top = new VBox();
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(calculatePopUpWidth());
        hbox_top.setMaxWidth(calculatePopUpWidth());
        hbox_top.setPrefWidth(calculatePopUpWidth());
        hbox_top.setMaxHeight(50);
        hbox_top.setMinHeight(50);
        hbox_top.setPrefHeight(50);

        Label popup_title = new Label(title);
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        HBox.setMargin(imageView, new Insets(0, 10, 0, 10));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        // Set the font size
        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        Region spacer = new Region();
        Button addButton = new Button("+ Add Product");
        addButton.setMinWidth(130);
        addButton.setMaxWidth(130);
        addButton.setMinHeight(30);
        addButton.setMaxHeight(30);
        addButton.setId("sub");
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
        hbox_top.getChildren().add(addButton);
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #D9F0FB; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 2 0;");
        //BorderPane Under Center Layout.....................................................................................................
        HBox hbox_top1 = new HBox();
        hbox_top1.setMinWidth(calculatePopUpWidth());
        hbox_top1.setMaxWidth(calculatePopUpWidth());
        hbox_top1.setPrefWidth(calculatePopUpWidth());
        hbox_top1.setMaxHeight(50);
        hbox_top1.setMinHeight(50);
        hbox_top1.setMaxHeight(50);
        hbox_top1.setAlignment(Pos.TOP_LEFT);
        hbox_top1.setStyle("-fx-background-color: white;");
        TextField search = new TextField("");
        search.setPromptText("Search");
        search.setPadding(new Insets(10, 0, 0, 10));
        search.setPrefWidth(350);
        HBox.setMargin(search, new Insets(0, 0, 0, 10));
        hbox_top1.getChildren().addAll(search);
        vbox_top.setSpacing(10);
        vbox_top.getChildren().addAll(hbox_top, hbox_top1);
        Object[] selectedItem = new Object[8];
        Integer initialIndex = -1;

        //BorderPane Under Bottom Layout..............................................................................................................
        VBox vBox = new VBox();
        TableView<TranxSelectedProduct> tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
//        tableView.setPrefHeight(GlobalTranx.TranxCalculatePer(calculatePopUpHeight(),90));
//        tableView.setMaxHeight(GlobalTranx.TranxCalculatePer(calculatePopUpHeight(),90));
        tableView.setMinHeight(GlobalTranx.TranxCalculatePer(calculatePopUpHeight(), 50));

        TableColumn<TranxSelectedProduct, String> productCode = new TableColumn<>("Code");
        TableColumn<TranxSelectedProduct, String> productName = new TableColumn<>("Product Name");
        TableColumn<TranxSelectedProduct, String> productPacking = new TableColumn<>("Packing");
        TableColumn<TranxSelectedProduct, String> productBarcode = new TableColumn<>("Barcode");
        TableColumn<TranxSelectedProduct, String> productBrand = new TableColumn<>("Brand");
        TableColumn<TranxSelectedProduct, Double> productUnit1Stock = new TableColumn<>("Unit1-Stk");
        TableColumn<TranxSelectedProduct, String> productUnit1 = new TableColumn<>("Unit1");
        TableColumn<TranxSelectedProduct, Double> productUnit2Stock = new TableColumn<>("Unit2-Stk");
        TableColumn<TranxSelectedProduct, String> productUnit2 = new TableColumn<>("Unit2");
        TableColumn<TranxSelectedProduct, Double> productUnit3Stock = new TableColumn<>("Unit3-Stk");
        TableColumn<TranxSelectedProduct, Double> productUnit3 = new TableColumn<>("Unit3");
        //! Adjusting the width for the Columns responsive
        productCode.prefWidthProperty().bind(tableView.widthProperty().multiply(0.01));
        productName.prefWidthProperty().bind(tableView.widthProperty().multiply(0.05));
        productPacking.prefWidthProperty().bind(tableView.widthProperty().multiply(0.01));
        productBarcode.prefWidthProperty().bind(tableView.widthProperty().multiply(0.02));
        productBrand.prefWidthProperty().bind(tableView.widthProperty().multiply(0.01));
        productUnit1Stock.prefWidthProperty().bind(tableView.widthProperty().multiply(0.01));
        productUnit1.prefWidthProperty().bind(tableView.widthProperty().multiply(0.01));
        productUnit2Stock.prefWidthProperty().bind(tableView.widthProperty().multiply(0.01));
        productUnit2.prefWidthProperty().bind(tableView.widthProperty().multiply(0.01));
        productUnit3Stock.prefWidthProperty().bind(tableView.widthProperty().multiply(0.01));
        productUnit3.prefWidthProperty().bind(tableView.widthProperty().multiply(0.01));

        tableView.getColumns().addAll(productCode, productName, productPacking, productBarcode, productBrand, productUnit1Stock, productUnit1, productUnit2Stock, productUnit2, productUnit3Stock, productUnit3);

        productCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productPacking.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        productBarcode.setCellValueFactory(new PropertyValueFactory<>("barCode"));
        productBrand.setCellValueFactory(new PropertyValueFactory<>("brandName"));
        productUnit1Stock.setCellValueFactory(new PropertyValueFactory<>("unit1ClsStock"));
        productUnit1.setCellValueFactory(new PropertyValueFactory<>("unit1Name"));
        productUnit2Stock.setCellValueFactory(new PropertyValueFactory<>("unit2ClsStock"));
        productUnit2.setCellValueFactory(new PropertyValueFactory<>("unit2Name"));
        productUnit3Stock.setCellValueFactory(new PropertyValueFactory<>("unit3ClsStock"));
        productUnit3.setCellValueFactory(new PropertyValueFactory<>("unit3Name"));

       /* cmActions.setCellFactory(param -> {
            final TableCell<ProductContentsMasterDTO, Void> cell = new TableCell<>() {
                private ImageView delImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/del.png"))));
                private ImageView edtImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/edt.png"))));

                {
                    delImg.setFitHeight(20.0);
                    delImg.setFitWidth(20.0);
                    edtImg.setFitHeight(20.0);
                    edtImg.setFitWidth(20.0);
                }

                private final Button delButton = new Button("", delImg);
                private final Button edtButton = new Button("", edtImg);

                {

                    delButton.setOnAction(event -> {
                        contentEdtIdx = -1;
                        tbllstContentMasterInfo.remove(getIndex());

                    });
                    edtButton.setOnAction(event -> {
                        // Handle button action here
                        contentEdtIdx = getIndex();
                        ProductContentsMasterDTO fnContentsDetails = tbllstContentMasterInfo.get(contentEdtIdx);
                        if (fnContentsDetails != null) {
                            setComboBoxValue(cbContentName, fnContentsDetails.getContentName());
                            tfPower.setText(fnContentsDetails.getPower());
                            setComboBoxValue(cbPackage, fnContentsDetails.getPacking());
                            setComboBoxValue(cbContentType, fnContentsDetails.getContentType());
                        }
                    });
                }

                HBox hbActions = new HBox();

                {
                    hbActions.getChildren().add(edtButton);
                    hbActions.getChildren().add(delButton);
                    hbActions.setSpacing(10.0);
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(hbActions);
                    }
                }

            };
            return cell;
        });*/
        lstProducts = getAllTranxProductList("", "");
        tableView.setItems(lstProducts);
        vBox.getChildren().addAll(tableView);
        tableView.setItems(lstProducts);

//        if (prodId > 0) {
//            OptionalInt rowIndex = IntStream.range(0, lstProducts.size()).filter(i -> lstProducts.get(i).getProductId() == prodId).findFirst();
//            if (rowIndex.getAsInt() > -1) {
//                tableView.getSelectionModel().select(rowIndex.getAsInt());
//                tableView.scrollTo(rowIndex.getAsInt());
//                borderPane.setBottom(getBottomUIDesignProduct(tableView));
//                GlobalTranx.requestFocusOrDieTrying(tableView, 3);
//            }
//        } else {
//            GlobalTranx.requestFocusOrDieTrying(search, 3);
//        }

        Platform.runLater(() -> {
            search.requestFocus();
        });

        search.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.DOWN) {
                tableView.getSelectionModel().select(0);
                GlobalTranx.requestFocusOrDieTrying(tableView, 3);
            } else if (e.getCode() == KeyCode.ESCAPE) {
                callback.accept(null);
                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
            e.consume();
        });

        // Add a listener to the text property of the search TextField
//        search.textProperty().addListener((observable, oldValue, newValue) -> {
//            // Filter the items based on the newValue
//            tableView.setItems(observableListProduct.filtered(item ->
//                    item.getProduct_name().toLowerCase().contains(newValue.toLowerCase()) ||
//                            item.getCode().toLowerCase().contains(newValue.toLowerCase()) ||
//                            item.getPacking().toLowerCase().contains(newValue.toLowerCase()) ||
//                            item.getBarcode().toLowerCase().contains(newValue.toLowerCase())||
//                            item.getBrand().toLowerCase().contains(newValue.toLowerCase())||
//                            item.getMrp().toLowerCase().contains(newValue.toLowerCase())||
//                            item.getCurrent_stock().toLowerCase().contains(newValue.toLowerCase())||
//                            item.getUnit().toLowerCase().contains(newValue.toLowerCase())||
//                            item.getSales_rate().toLowerCase().contains(newValue.toLowerCase())
//            ));
//        });

        search.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String searchKey = search.getText().trim();
                if (searchKey.length() >= 3) {
                    lstProducts = getAllTranxProductList(searchKey, "");
                } else if (searchKey.isEmpty()) {
                    lstProducts = getAllTranxProductList("", "");
                }
                tableView.setItems(lstProducts);
            }
        });

        borderPane.setTop(vbox_top);
        borderPane.setCenter(vBox);
        tableView.getFocusModel().focusedCellProperty().addListener((obs, old, nw) -> {
//            System.out.println("tableView : nw" + nw);
            borderPane.setBottom(getBottomUIDesignProduct(tableView));
        });
        //? Double click on ledger list
        tableView.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                TranxSelectedProduct selectedProduct = tableView.getSelectionModel().getSelectedItem();
                callback.accept(selectedProduct);
                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
            e.consume();
        });
        tableView.setRowFactory(tv -> {
            TableRow<TranxSelectedProduct> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        TranxSelectedProduct selectedProduct = tableView.getSelectionModel().getSelectedItem();
                        callback.accept(selectedProduct);
                        primaryStage.close();
                        OverlaysEffect.removeOverlaysEffect(stage);
                    }
                }
            });
            return row;
        });

        Scene scene = new Scene(borderPane, calculatePopUpWidth(), calculatePopUpHeight());

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

//        primaryStage.centerOnScreen();
        primaryStage.setX(calculatePopUpX());
        primaryStage.setY(calculatePopUpY());
        primaryStage.show();

      /*  addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                selectedItem[1] = tfPower.getText();
                insertIntoTable(selectedItem[0].toString(), selectedItem[1].toString(), selectedItem[2].toString()
                        , selectedItem[3].toString());
            }
        });*/
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addPrdCallback.accept(true);
                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
        });
    }

    private static VBox getBottomUIDesignProduct(TableView<TranxSelectedProduct> tableView) {
        VBox mainVbox = new VBox();
        mainVbox.setPrefHeight(500.0);
        mainVbox.setPadding(new Insets(10));
//        mainVbox.setStyle("-fx-padding:10px;");
//        mainVbox.getStyleClass().add("ledgertabMainDiv");
        VBox innerDiv = new VBox();
//        innerDiv.getStyleClass().add("mainGridStyle");#f8f0d2
        innerDiv.setStyle("-fx-background-color:#f8f0d2;-fx-padding:15px;-fx-background-radius:6;");
        HBox innerHbox = new HBox();
//        innerHbox.getStyleClass().add("ledgerTabInnerHb");
        innerHbox.setPadding(new Insets(10));
        //! VBOX1
        VBox vBox1 = new VBox();
        HBox.setHgrow(vBox1, Priority.ALWAYS);
        vBox1.setStyle("-fx-border-width: 0 1.2 0 0; -fx-border-color: #EAD8B1;");
//        vBox1.getStyleClass().add("ledgerSingleInfo");
        vBox1.setPadding(new Insets(10));
        vBox1.setSpacing(8);
        //? hbox => 4 UNIT-1 FSRMH,FSRAI,CSRMH CSRAI
        HBox v1h1 = new HBox();
        v1h1.setSpacing(10);
        Label lbl1v1h1 = new Label("UNIT : ");
        lbl1v1h1.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v1h1 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit1Name() : "");
        lbl2v1h1.setStyle("-fx-font-weight:bold;");
        v1h1.getChildren().addAll(lbl1v1h1, lbl2v1h1);

        HBox v1h2 = new HBox();
        v1h2.setSpacing(10);
        Label lbl1v1h2 = new Label("RATE-1 : ");
        lbl1v1h2.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v1h2 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit1FSRMH().toString() : "");
//        lbl2v1h2.getStyleClass().add("gridCommonTextStyle");
        lbl2v1h2.setStyle("-fx-font-weight:bold;");


        v1h2.getChildren().addAll(lbl1v1h2, lbl2v1h2);

        HBox v1h3 = new HBox();
        v1h3.setSpacing(10);
        Label lbl1v1h3 = new Label("RATE-2 : ");
//        lbl1v1h3.getStyleClass().add("gridCommonTextStyle");
        lbl1v1h3.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v1h3 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit1FSRAI().toString() : "");
        lbl2v1h3.setStyle("-fx-font-weight:bold;");

        v1h3.getChildren().addAll(lbl1v1h3, lbl2v1h3);

        HBox v1h4 = new HBox();
        v1h4.setSpacing(10);
        Label lbl1v1h4 = new Label("RATE-3 : ");
        lbl1v1h4.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v1h4 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit1CSRMH().toString() : "");
        lbl2v1h4.setStyle("-fx-font-weight:bold;");
        v1h4.getChildren().addAll(lbl1v1h4, lbl2v1h4);

        HBox v1h5 = new HBox();
        v1h5.setSpacing(10);
        Label lbl1v1h5 = new Label("UNIT CONV : ");
        lbl1v1h5.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v1h5 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit1Conv().toString() : "");
        lbl2v1h5.setStyle("-fx-font-weight:bold;");
        v1h5.getChildren().addAll(lbl1v1h5, lbl2v1h5);

        vBox1.getChildren().addAll(v1h1, v1h2, v1h3, v1h4, v1h5);

        //! VBOX2
        VBox vBox2 = new VBox();
        HBox.setHgrow(vBox2, Priority.ALWAYS);
        vBox2.setStyle("-fx-border-width: 0 1.2 0 0; -fx-border-color: #EAD8B1;");
        vBox2.setPadding(new Insets(10));
        vBox2.setSpacing(8);
        //? hbox => 4 UNIT-1 FSRMH,FSRAI,CSRMH CSRAI
        HBox v2h1 = new HBox();
        v2h1.setSpacing(10);
        Label lbl1v2h1 = new Label("UNIT : ");
        lbl1v2h1.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v2h1 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit2Name() : "");
        lbl2v2h1.setStyle("-fx-font-weight:bold;");
        v2h1.getChildren().addAll(lbl1v2h1, lbl2v2h1);

        HBox v2h2 = new HBox();
        v2h2.setSpacing(10);
        Label lbl1v2h2 = new Label("RATE-1 : ");
        lbl1v2h2.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v2h2 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit2FSRMH().toString() : "");
//        lbl2v1h2.getStyleClass().add("gridCommonTextStyle");
        lbl2v2h2.setStyle("-fx-font-weight:bold;");
        v2h2.getChildren().addAll(lbl1v2h2, lbl2v2h2);

        HBox v2h3 = new HBox();
        v2h3.setSpacing(10);
        Label lbl1v2h3 = new Label("RATE-2 : ");
//        lbl1v1h3.getStyleClass().add("gridCommonTextStyle");
        lbl1v2h3.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v2h3 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit2FSRAI().toString() : "");
        lbl2v2h3.setStyle("-fx-font-weight:bold;");

        v2h3.getChildren().addAll(lbl1v2h3, lbl2v2h3);

        HBox v2h4 = new HBox();
        v2h4.setSpacing(10);
        Label lbl1v2h4 = new Label("RATE-3 : ");
        lbl1v2h4.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v2h4 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit2CSRMH().toString() : "");
        lbl2v2h4.setStyle("-fx-font-weight:bold;");
        v2h4.getChildren().addAll(lbl1v2h4, lbl2v2h4);

        HBox v2h5 = new HBox();
        v2h5.setSpacing(10);
        Label lbl1v2h5 = new Label("UNIT CONV : ");
        lbl1v2h5.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v2h5 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit2Conv().toString() : "");
        lbl2v2h5.setStyle("-fx-font-weight:bold;");
        v2h5.getChildren().addAll(lbl1v2h5, lbl2v2h5);


        vBox2.getChildren().addAll(v2h1, v2h2, v2h3, v2h4, v2h5);

        //! VBOX3
        VBox vBox3 = new VBox();
        HBox.setHgrow(vBox3, Priority.ALWAYS);

        vBox3.setStyle("-fx-border-width: 0 1.2 0 0; -fx-border-color: #EAD8B1;");
        vBox3.setPadding(new Insets(10));
        vBox3.setSpacing(8);
        //? hbox => 4 UNIT-1 FSRMH,FSRAI,CSRMH CSRAI
        HBox v3h1 = new HBox();
        v3h1.setSpacing(10);
        Label lbl1v3h1 = new Label("UNIT : ");
        lbl1v3h1.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v3h1 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit3Name() : "");
        lbl2v3h1.setStyle("-fx-font-weight:bold;");
        v3h1.getChildren().addAll(lbl1v3h1, lbl2v3h1);

        HBox v3h2 = new HBox();
        v3h2.setSpacing(10);
        Label lbl1v3h2 = new Label("RATE-1 : ");
        lbl1v3h2.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v3h2 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit3FSRMH().toString() : "");
//        lbl2v1h2.getStyleClass().add("gridCommonTextStyle");
        lbl2v3h2.setStyle("-fx-font-weight:bold;");
        v3h2.getChildren().addAll(lbl1v3h2, lbl2v3h2);

        HBox v3h3 = new HBox();
        v3h3.setSpacing(10);
        Label lbl1v3h3 = new Label("RATE-2 : ");
//        lbl1v1h3.getStyleClass().add("gridCommonTextStyle");
        lbl1v3h3.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v3h3 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit3FSRAI().toString() : "");
        lbl2v3h3.setStyle("-fx-font-weight:bold;");

        v3h3.getChildren().addAll(lbl1v3h3, lbl2v3h3);

        HBox v3h4 = new HBox();
        v3h4.setSpacing(10);
        Label lbl1v3h4 = new Label("RATE-3 : ");
        lbl1v3h4.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v3h4 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit3CSRMH().toString() : "");
        lbl2v3h4.setStyle("-fx-font-weight:bold;");
        v3h4.getChildren().addAll(lbl1v3h4, lbl2v3h4);

        HBox v3h5 = new HBox();
        v3h5.setSpacing(10);
        Label lbl1v3h5 = new Label("UNIT CONV : ");
        lbl1v3h5.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v3h5 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit3Conv().toString() : "");
        lbl2v3h5.setStyle("-fx-font-weight:bold;");
        v3h5.getChildren().addAll(lbl1v3h5, lbl2v3h5);


        vBox3.getChildren().addAll(v3h1, v3h2, v3h3, v3h4, v3h5);


        innerHbox.getChildren().addAll(vBox1, vBox2, vBox3);
        innerDiv.getChildren().addAll(innerHbox);
        mainVbox.getChildren().add(innerDiv);
        return mainVbox;
    }

    public static ObservableList<TranxSelectedProduct> getAllTranxProductListByBarcodeOld(String barcode) {
        lstProducts.clear();

        try {
            Map<String, String> body = new HashMap<>();
            body.put("barcode", barcode);
            String formData = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.GET_TRANX_PRODUCT_LIST);
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
//            System.out.println("response product list barcode " + jsonObject);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseArray = jsonObject.get("list").getAsJsonArray();
//                System.out.println("ReponseArr" + responseArray);
                if (responseArray.size() > 0) {
                    for (JsonElement element : responseArray) {
                        JsonObject jobj = new Gson().fromJson(element, JsonObject.class);
//                        System.out.println("jobj" + jobj);
//                        TranxProductListRes tranxProductListRes = new Gson().fromJson(element, TranxProductListRes.class);
                        JsonArray jarr = jobj.get("unit_lst").getAsJsonArray();
                        String unit1Name = "";
                        Integer unit1Id = 0;
                        Double unit1FSRMH = 0.0;
                        Double unit1FSRAI = 0.0;
                        Double unit1CSRMH = 0.0;
                        Double unit1CSRAI = 0.0;
                        Double unit1Conv = 0.0;
                        Double unit1ClsStock = 0.0;
                        Double unit1ActualStock = 0.0;
                        if (jarr.size() >= 1) {
                            JsonObject unit1Obj = jarr.get(0).getAsJsonObject();
                            unit1Name = unit1Obj.get("unitName").getAsString();
                            unit1Id = unit1Obj.get("unitid").getAsInt();
                            unit1FSRMH = unit1Obj.get("fsrmh").getAsDouble();
                            unit1FSRAI = unit1Obj.get("fsrai").getAsDouble();
                            unit1CSRMH = unit1Obj.get("csrmh").getAsDouble();
                            unit1CSRAI = unit1Obj.get("csrai").getAsDouble();
                            unit1Conv = unit1Obj.get("unitConv").getAsDouble();
                            unit1ClsStock = unit1Obj.get("closingstk").getAsDouble();
                            unit1ActualStock = unit1Obj.get("actstkcheck").getAsDouble();
                        }
                        String unit2Name = "";
                        Integer unit2Id = 0;
                        Double unit2FSRMH = 0.0;
                        Double unit2FSRAI = 0.0;
                        Double unit2CSRMH = 0.0;
                        Double unit2CSRAI = 0.0;
                        Double unit2Conv = 0.0;
                        Double unit2ClsStock = 0.0;
                        Double unit2ActualStock = 0.0;
                        if (jarr.size() >= 2) {
                            JsonObject unit2Obj = jarr.get(1).getAsJsonObject();
                            unit2Name = unit2Obj.get("unitName").getAsString();
                            unit2Id = unit2Obj.get("unitid").getAsInt();
                            unit2FSRMH = unit2Obj.get("fsrmh").getAsDouble();
                            unit2FSRAI = unit2Obj.get("fsrai").getAsDouble();
                            unit2CSRMH = unit2Obj.get("csrmh").getAsDouble();
                            unit2CSRAI = unit2Obj.get("csrai").getAsDouble();
                            unit2Conv = unit2Obj.get("unitConv").getAsDouble();
                            unit2ClsStock = unit2Obj.get("closingstk").getAsDouble();
                            unit2ActualStock = unit2Obj.get("actstkcheck").getAsDouble();
                        }
                        String unit3Name = "";
                        Integer unit3Id = 0;
                        Double unit3FSRMH = 0.0;
                        Double unit3FSRAI = 0.0;
                        Double unit3CSRMH = 0.0;
                        Double unit3CSRAI = 0.0;
                        Double unit3Conv = 0.0;
                        Double unit3ClsStock = 0.0;
                        Double unit3ActualStock = 0.0;
                        if (jarr.size() >= 3) {
                            JsonObject unit3Obj = jarr.get(2).getAsJsonObject();
                            unit3Name = unit3Obj.get("unitName").getAsString();
                            unit3Id = unit3Obj.get("unitid").getAsInt();
                            unit3FSRMH = unit3Obj.get("fsrmh").getAsDouble();
                            unit3FSRAI = unit3Obj.get("fsrai").getAsDouble();
                            unit3CSRMH = unit3Obj.get("csrmh").getAsDouble();
                            unit3CSRAI = unit3Obj.get("csrai").getAsDouble();
                            unit3Conv = unit3Obj.get("unitConv").getAsDouble();
                            unit3ClsStock = unit3Obj.get("closingstk").getAsDouble();
                            unit3ActualStock = unit3Obj.get("actstkcheck").getAsDouble();

                        }

                        lstProducts.add(new TranxSelectedProduct(jobj.get("id").getAsInt(), jobj.get("product_name").getAsString(), jobj.get("igst").getAsDouble(), jobj.get("cgst").getAsDouble(), jobj.get("sgst").getAsDouble(), 0, jobj.get("packing").getAsString(),
                                jobj.get("code").getAsString(), jobj.get("barcode").getAsString(), jobj.get("is_negative").getAsBoolean(), jobj.get("is_batch").getAsBoolean(), jobj.get("is_serial").getAsBoolean(), 0.0, 0.0, 0.0, jobj.get("current_stock").getAsDouble(),
                                false, false, false, 0, "", 0, "", 0, "", jobj.get("brand").getAsString(), jobj.get("unit").getAsString(), 0.0, 0.0,
                                unit1Name, unit1Id, unit1FSRMH, unit1FSRAI, unit1CSRMH, unit1CSRAI, unit1Conv, unit1ClsStock, unit1ActualStock, unit2Name, unit2Id, unit2FSRMH, unit2FSRAI, unit2CSRMH, unit2CSRAI, unit2Conv, unit2ClsStock, unit2ActualStock, unit3Name, unit3Id, unit3FSRMH,
                                unit3FSRAI, unit3CSRMH, unit3CSRAI, unit3Conv, unit3ClsStock, unit3ActualStock, jobj.get("drugType").getAsString()));
                    }
                } else {
                    System.out.println("responseObject is null");
                }
            } else {
                System.out.println("Error in response");
            }
        } catch (Exception e) {
//            e.printStackTrace();
            loggerTranxCommonPopup.error("Exception in getAllTranxProductList(): ", Globals.getExceptionString(e));
        }

        return lstProducts;
    }

    public static ObservableList<TranxSelectedProduct> getAllTranxProductList(String searchQuery, String barcodeQuery) {
        lstProducts.clear();
        try {
            Map<String, String> body = new HashMap<>();
            body.put("search", searchQuery);
            body.put("barcode", barcodeQuery);
            String formData = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.GET_TRANX_PRODUCT_LIST);
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
//            System.out.println("response product list " + jsonObject);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseArray = jsonObject.get("list").getAsJsonArray();
                if (responseArray.size() > 0) {
                    for (JsonElement element : responseArray) {
                        JsonObject jobj = new Gson().fromJson(element, JsonObject.class);
//                        System.out.println("jobj" + jobj);
//                        TranxProductListRes tranxProductListRes = new Gson().fromJson(element, TranxProductListRes.class);
                        JsonArray jarr = jobj.get("unit_lst").getAsJsonArray();
                        String unit1Name = "";
                        Integer unit1Id = 0;
                        Double unit1Mrp = 0.0;
                        Double unit1PurchaseRate = 0.0;
                        Double unit1FSRMH = 0.0;
                        Double unit1FSRAI = 0.0;
                        Double unit1CSRMH = 0.0;
                        Double unit1CSRAI = 0.0;
                        Double unit1Conv = 0.0;
                        Double unit1ClsStock = 0.0;
                        Double unit1ActualStock = 0.0;
                        Boolean unit1IsNegetive = false;
                        if (jarr.size() >= 1) {
                            JsonObject unit1Obj = jarr.get(0).getAsJsonObject();
                            unit1Id = unit1Obj.get("unitid").getAsInt();
                            unit1Name = unit1Obj.get("unitName").getAsString();
                            unit1Mrp = unit1Obj.get("mrp").getAsDouble();
                            unit1PurchaseRate = unit1Obj.get("purchaserate").getAsDouble();
                            unit1FSRMH = unit1Obj.get("fsrmh").getAsDouble();
                            unit1FSRAI = unit1Obj.get("fsrai").getAsDouble();
                            unit1CSRMH = unit1Obj.get("csrmh").getAsDouble();
                            unit1CSRAI = unit1Obj.get("csrai").getAsDouble();
                            unit1Conv = unit1Obj.get("unitConv").getAsDouble();
                            unit1ClsStock = unit1Obj.get("closingstk").getAsDouble();
                            unit1ActualStock = unit1Obj.get("actstkcheck").getAsDouble();
                            unit1IsNegetive = unit1Obj.get("is_negetive").getAsBoolean();
                        }
                        String unit2Name = "";
                        Integer unit2Id = 0;
                        Double unit2Mrp = 0.0;
                        Double unit2PurchaseRate = 0.0;
                        Double unit2FSRMH = 0.0;
                        Double unit2FSRAI = 0.0;
                        Double unit2CSRMH = 0.0;
                        Double unit2CSRAI = 0.0;
                        Double unit2Conv = 0.0;
                        Double unit2ClsStock = 0.0;
                        Double unit2ActualStock = 0.0;
                        Boolean unit2IsNegetive = false;
                        if (jarr.size() >= 2) {
                            JsonObject unit2Obj = jarr.get(1).getAsJsonObject();
                            unit2Id = unit2Obj.get("unitid").getAsInt();
                            unit2Name = unit2Obj.get("unitName").getAsString();
                            unit2Mrp = unit2Obj.get("mrp").getAsDouble();
                            unit2PurchaseRate = unit2Obj.get("purchaserate").getAsDouble();
                            unit2FSRMH = unit2Obj.get("fsrmh").getAsDouble();
                            unit2FSRAI = unit2Obj.get("fsrai").getAsDouble();
                            unit2CSRMH = unit2Obj.get("csrmh").getAsDouble();
                            unit2CSRAI = unit2Obj.get("csrai").getAsDouble();
                            unit2Conv = unit2Obj.get("unitConv").getAsDouble();
                            unit2ClsStock = unit2Obj.get("closingstk").getAsDouble();
                            unit2ActualStock = unit2Obj.get("actstkcheck").getAsDouble();
                            unit2IsNegetive = unit2Obj.get("is_negetive").getAsBoolean();
                        }
                        String unit3Name = "";
                        Integer unit3Id = 0;
                        Double unit3FSRMH = 0.0;
                        Double unit3Mrp = 0.0;
                        Double unit3PurchaseRate = 0.0;
                        Double unit3FSRAI = 0.0;
                        Double unit3CSRMH = 0.0;
                        Double unit3CSRAI = 0.0;
                        Double unit3Conv = 0.0;
                        Double unit3ClsStock = 0.0;
                        Double unit3ActualStock = 0.0;
                        Boolean unit3IsNegetive = false;
                        if (jarr.size() >= 3) {
                            JsonObject unit3Obj = jarr.get(2).getAsJsonObject();
                            unit3Name = unit3Obj.get("unitName").getAsString();
                            unit3Id = unit3Obj.get("unitid").getAsInt();
                            unit3Mrp = unit3Obj.get("mrp").getAsDouble();
                            unit3PurchaseRate = unit3Obj.get("purchaserate").getAsDouble();
                            unit3FSRMH = unit3Obj.get("fsrmh").getAsDouble();
                            unit3FSRAI = unit3Obj.get("fsrai").getAsDouble();
                            unit3CSRMH = unit3Obj.get("csrmh").getAsDouble();
                            unit3CSRAI = unit3Obj.get("csrai").getAsDouble();
                            unit3Conv = unit3Obj.get("unitConv").getAsDouble();
                            unit3ClsStock = unit3Obj.get("closingstk").getAsDouble();
                            unit3ActualStock = unit3Obj.get("actstkcheck").getAsDouble();
                            unit3IsNegetive = unit3Obj.get("is_negetive").getAsBoolean();

                        }


                        lstProducts.add(new TranxSelectedProduct(jobj.get("id").getAsInt(), jobj.get("product_name").getAsString(), jobj.get("igst").getAsDouble(), jobj.get("cgst").getAsDouble(), jobj.get("sgst").getAsDouble(), 0, jobj.get("packing").getAsString(),
                                jobj.get("code").getAsString(), jobj.get("barcode").getAsString(), jobj.get("is_negative").getAsBoolean(), jobj.get("is_batch").getAsBoolean(), jobj.get("is_serial").getAsBoolean(), 0.0, 0.0, 0.0, jobj.get("current_stock").getAsDouble(),
                                false, false, false, 0, "", 0, "", 0, "", jobj.get("brand").getAsString(), jobj.get("unit").getAsString(), 0.0, jobj.get("drugType").getAsString(), 0.0,
                                unit1Id, unit1Name, unit1Mrp, unit1PurchaseRate, unit1FSRMH, unit1FSRAI, unit1CSRMH, unit1CSRAI, unit1Conv, unit1ClsStock, unit1ActualStock, unit1IsNegetive, unit2Id, unit2Name, unit2Mrp, unit2PurchaseRate, unit2FSRMH, unit2FSRAI, unit2CSRMH, unit2CSRAI, unit2Conv, unit2ClsStock, unit2ActualStock, unit2IsNegetive, unit3Id, unit3Name, unit3Mrp, unit3PurchaseRate, unit3FSRMH,
                                unit3FSRAI, unit3CSRMH, unit3CSRAI, unit3Conv, unit3ClsStock, unit3ActualStock, unit3IsNegetive));
                    }
                } else {
                    System.out.println("responseObject is null");
                }
            } else {
                System.out.println("Error in response");
            }
        } catch (Exception e) {
//            e.printStackTrace();
            loggerTranxCommonPopup.error("Exception in getAllTranxProductList(): ", Globals.getExceptionString(e));
        }

        return lstProducts;
    }

    public static Object getAllTranxProductListByBarcode(String searchQuery, String barcodeQuery) {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("search", searchQuery);
            body.put("barcode", barcodeQuery);
            String formData = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.GET_TRANX_PRODUCT_LIST_BY_BARCODE);
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
//            System.out.println("response product list barcode=>" + jsonObject);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                return jsonObject.get("list").getAsJsonArray();
            } else {
                System.out.println("Error in response");
            }
        } catch (Exception e) {
//            e.printStackTrace();
            loggerTranxCommonPopup.error("Exception in getAllTranxProductList(): ", Globals.getExceptionString(e));
        }

        return new JsonArray();
    }


    public static <T> void openBatchPopUp(Stage stage, TranxRow selectedRow, String TranxDate, String title, Consumer<TranxSelectedBatch> callback) {
        System.out.println("openBatchPopUp selectedRow" + selectedRow + TranxDate);
        getAllTranxProductBatchList(selectedRow.getProductId(), TranxDate);
        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();
        ObservableList<TranxProductWindowDTO> observableLedgerList = FXCollections.observableArrayList();
        primaryStage.initOwner(stage); // Set the owner stage
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");
        Platform.runLater(() -> borderPane.requestFocus());
        //BorderPan under Top Layout.   ...................................................................................................................
        VBox vbox_top = new VBox();
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(calculatePopUpWidth());
        hbox_top.setMaxWidth(calculatePopUpWidth());
        hbox_top.setPrefWidth(calculatePopUpWidth());
        hbox_top.setMaxHeight(50);
        hbox_top.setMinHeight(50);
        hbox_top.setPrefHeight(50);

        Label popup_title = new Label(title);
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        HBox.setMargin(imageView, new Insets(0, 10, 0, 10));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        // Set the font size
        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        Region spacer = new Region();
//        Button addButton = new Button("+ Add Product");
//        addButton.setMinWidth(130);
//        addButton.setMaxWidth(130);
//        addButton.setMinHeight(30);
//        addButton.setMaxHeight(30);
//        addButton.setId("submit-btn");
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
//        hbox_top.getChildren().add(addButton);
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #D9F0FB; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 2 0;");
        //BorderPane Under Center Layout.....................................................................................................
        HBox hbox_top1 = new HBox();
        hbox_top1.setMinWidth(calculatePopUpWidth());
        hbox_top1.setMaxWidth(calculatePopUpWidth());
        hbox_top1.setPrefWidth(calculatePopUpWidth());
        hbox_top1.setMaxHeight(50);
        hbox_top1.setMinHeight(50);
        hbox_top1.setMaxHeight(50);
        hbox_top1.setAlignment(Pos.TOP_LEFT);
        hbox_top1.setStyle("-fx-background-color: white;");
        TextField search = new TextField("");
        search.setPromptText("Search");
        search.setPadding(new Insets(10, 0, 0, 10));
        search.setPrefWidth(350);
        HBox.setMargin(search, new Insets(0, 0, 0, 10));
        hbox_top1.getChildren().addAll(search);
        vbox_top.setSpacing(10);
        vbox_top.getChildren().addAll(hbox_top, hbox_top1);
        Object[] selectedItem = new Object[8];
        Integer initialIndex = -1;

        //BorderPane Under Bottom Layout..............................................................................................................
        VBox vBox = new VBox();
        TableView<TranxSelectedBatch> tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
//        tableView.setPrefHeight(500);
//        tableView.setMaxHeight(500);
//        tableView.setMinHeight(500);
        tableView.setMinHeight(GlobalTranx.TranxCalculatePer(calculatePopUpHeight(), 50));
        TableColumn<TranxSelectedBatch, String> tcbatchNo = new TableColumn<>("BatchNo");
        TableColumn<TranxSelectedBatch, String> tcPurDate = new TableColumn<>("Purchase Date");
        TableColumn<TranxSelectedBatch, String> tcMFGDate = new TableColumn<>("MFG Date");
        TableColumn<TranxSelectedBatch, String> tcExpiryDate = new TableColumn<>("Expiry Date");
        TableColumn<TranxSelectedBatch, String> tcMRP = new TableColumn<>("MRP");
        TableColumn<TranxSelectedBatch, String> tcPurRate = new TableColumn<>("Purchase Rate");
        TableColumn<TranxSelectedBatch, Double> tcUnit1Stock = new TableColumn<>("Unit1-Stock");
        TableColumn<TranxSelectedBatch, String> tcUnit1 = new TableColumn<>("Unit1");
        TableColumn<TranxSelectedBatch, Double> tcUnit2Stock = new TableColumn<>("Unit2-Stock");
        TableColumn<TranxSelectedBatch, String> tcUnit2 = new TableColumn<>("Unit2");
        TableColumn<TranxSelectedBatch, Double> tcUnit3Stock = new TableColumn<>("Unit3-Stock");
        TableColumn<TranxSelectedBatch, Double> tcUnit3 = new TableColumn<>("Unit3");
        // Adjusting the width for the Columns
        /*tcbatchNo.setPrefWidth(40);
        tcPurDate.setPrefWidth(60);
        tcMFGDate.setPrefWidth(60);
        tcExpiryDate.setPrefWidth(60);
        tcMRP.setPrefWidth(30);
        tcPurRate.setPrefWidth(30);
        tcStock.setPrefWidth(40);
        tcMarginPer.setPrefWidth(30);
        tcRateA.setPrefWidth(30);
        tcRateB.setPrefWidth(30);
        tcRateC.setPrefWidth(30);
        tcAct.setPrefWidth(30);*/
        tableView.getColumns().addAll(tcbatchNo, tcPurDate, tcMFGDate, tcExpiryDate, tcMRP, tcPurRate, tcUnit1Stock, tcUnit1, tcUnit2Stock, tcUnit2, tcUnit3Stock, tcUnit3);
        tcbatchNo.setCellValueFactory(new PropertyValueFactory<>("batchNo"));
        tcPurDate.setCellValueFactory(new PropertyValueFactory<>("purDate"));
        tcMFGDate.setCellValueFactory(new PropertyValueFactory<>("manufactureDate"));
        tcExpiryDate.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        tcMRP.setCellValueFactory(new PropertyValueFactory<>("mrp"));
        tcPurRate.setCellValueFactory(new PropertyValueFactory<>("purchaseRate"));
        tcUnit1Stock.setCellValueFactory(new PropertyValueFactory<>("unit1ClsStock"));
        tcUnit1.setCellValueFactory(new PropertyValueFactory<>("unit1Name"));
        tcUnit2Stock.setCellValueFactory(new PropertyValueFactory<>("unit2ClsStock"));
        tcUnit2.setCellValueFactory(new PropertyValueFactory<>("unit2Name"));
        tcUnit3Stock.setCellValueFactory(new PropertyValueFactory<>("unit3ClsStock"));
        tcUnit3.setCellValueFactory(new PropertyValueFactory<>("unit3Name"));
        tableView.setItems(lstBatch);
        vBox.getChildren().addAll(tableView);
        tableView.setItems(lstBatch);
        if (!selectedRow.getbNo().trim().isEmpty()) {
            OptionalInt rowIndex = IntStream.range(0, lstBatch.size()).filter(i -> lstBatch.get(i).getBatchNo().equalsIgnoreCase(selectedRow.getbNo())).findFirst();
            if (rowIndex.getAsInt() > -1) {
                tableView.getSelectionModel().select(rowIndex.getAsInt());
                GlobalTranx.requestFocusOrDieTrying(tableView, 3);
            }
        } else {
            GlobalTranx.requestFocusOrDieTrying(search, 3);
        }


        tableView.setOnKeyReleased((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                TranxSelectedBatch productWindowDTO = tableView.getSelectionModel().getSelectedItem();
                callback.accept(productWindowDTO);
                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
            e.consume();
        });
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            tableView.setItems(lstBatch.filtered(s -> s.getBatchNo().contains(newValue)));
        });
        search.setOnKeyReleased((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                tableView.getSelectionModel().select(0);
                GlobalTranx.requestFocusOrDieTrying(tableView, 3);
            }
            e.consume();
        });
        //? Double click on ledger list
        tableView.setRowFactory(tv -> {
            TableRow<TranxSelectedBatch> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        TranxSelectedBatch productWindowDTO = tableView.getSelectionModel().getSelectedItem();
                        callback.accept(productWindowDTO);
                        primaryStage.close();
                        OverlaysEffect.removeOverlaysEffect(stage);
                    }
                }
            });
            return row;
        });

        //? Borderpane add elements
//        BorderPane.setMargin(vbox_top, new Insets(20, 0, 0, 0)); // Set top margin of 20 pixels
        borderPane.setTop(vbox_top);
        borderPane.setCenter(vBox);
        tableView.getFocusModel().focusedCellProperty().addListener((obs, old, nw) -> {
//            System.out.println("tableView : nw" + nw);
            borderPane.setBottom(getBottomUIDesignBatch(tableView));
        });

        Scene scene = new Scene(borderPane, calculatePopUpWidth(), calculatePopUpHeight());

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

//        primaryStage.centerOnScreen();
        primaryStage.setX(calculatePopUpX());
        primaryStage.setY(calculatePopUpY());
        primaryStage.show();

      /*  addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                selectedItem[1] = tfPower.getText();
                insertIntoTable(selectedItem[0].toString(), selectedItem[1].toString(), selectedItem[2].toString()
                        , selectedItem[3].toString());
            }
        });*//*
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
//                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
        });*/

    }

    public static <T> void openBatchPopUpSales(Stage stage, SalesInvoiceTable selectedRow, String TranxDate, String title, Consumer<TranxSelectedBatch> callback, Consumer<Boolean> isNewBatchCreate) {
//        System.out.println("openBatchPopUp selectedRow" + selectedRow + TranxDate);
        getAllTranxProductBatchListSales(selectedRow.getProduct_id(), TranxDate);
        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();
        ObservableList<TranxProductWindowDTO> observableLedgerList = FXCollections.observableArrayList();
        primaryStage.initOwner(stage); // Set the owner stage
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");
        Platform.runLater(() -> borderPane.requestFocus());
        //BorderPan under Top Layout.   ...................................................................................................................
        VBox vbox_top = new VBox();
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(calculatePopUpWidth());
        hbox_top.setMaxWidth(calculatePopUpWidth());
        hbox_top.setPrefWidth(calculatePopUpWidth());
        hbox_top.setMaxHeight(50);
        hbox_top.setMinHeight(50);
        hbox_top.setPrefHeight(50);

        Label popup_title = new Label(title);
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        HBox.setMargin(imageView, new Insets(0, 10, 0, 10));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        // Set the font size
        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        Region spacer = new Region();
        Button addButton = new Button("+ Add Batch");
        addButton.setMinWidth(130);
        addButton.setMaxWidth(130);
        addButton.setMinHeight(30);
        addButton.setMaxHeight(30);
        addButton.setId("submit-btn");
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
        if (selectedRow.getSelectedProduct().getNegetive() == true) {
            hbox_top.getChildren().add(addButton);
        }
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #D9F0FB; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 2 0;");
        //BorderPane Under Center Layout.....................................................................................................
        HBox hbox_top1 = new HBox();
        hbox_top1.setMinWidth(calculatePopUpWidth());
        hbox_top1.setMaxWidth(calculatePopUpWidth());
        hbox_top1.setPrefWidth(calculatePopUpWidth());
        hbox_top1.setMaxHeight(50);
        hbox_top1.setMinHeight(50);
        hbox_top1.setMaxHeight(50);
        hbox_top1.setAlignment(Pos.TOP_LEFT);
        hbox_top1.setStyle("-fx-background-color: white;");
        TextField search = new TextField("");
        search.setPromptText("Search");
        search.setPadding(new Insets(10, 0, 0, 10));
        search.setPrefWidth(350);
        HBox.setMargin(search, new Insets(0, 0, 0, 10));
        hbox_top1.getChildren().addAll(search);
        vbox_top.setSpacing(10);
        vbox_top.getChildren().addAll(hbox_top, hbox_top1);
        Object[] selectedItem = new Object[8];
        Integer initialIndex = -1;

        //BorderPane Under Bottom Layout..............................................................................................................
        VBox vBox = new VBox();
        TableView<TranxSelectedBatch> tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
//        tableView.setPrefHeight(500);
//        tableView.setMaxHeight(500);
//        tableView.setMinHeight(500);
        tableView.setMinHeight(GlobalTranx.TranxCalculatePer(calculatePopUpHeight(), 50));
        TableColumn<TranxSelectedBatch, String> tcbatchNo = new TableColumn<>("BatchNo");
        TableColumn<TranxSelectedBatch, String> tcPurDate = new TableColumn<>("Purchase Date");
        TableColumn<TranxSelectedBatch, String> tcMFGDate = new TableColumn<>("MFG Date");
        TableColumn<TranxSelectedBatch, String> tcExpiryDate = new TableColumn<>("Expiry Date");
        TableColumn<TranxSelectedBatch, String> tcMRP = new TableColumn<>("MRP");
        TableColumn<TranxSelectedBatch, String> tcPurRate = new TableColumn<>("Purchase Rate");
        TableColumn<TranxSelectedBatch, Double> tcUnit1Stock = new TableColumn<>("Unit1-Stk");
        tcUnit1Stock.setStyle("-fx-alignment:CENTER;");
        TableColumn<TranxSelectedBatch, String> tcUnit1 = new TableColumn<>("Unit1");
        TableColumn<TranxSelectedBatch, Double> tcUnit2Stock = new TableColumn<>("Unit2-Stk");
        tcUnit2Stock.setStyle("-fx-alignment:CENTER;");
        TableColumn<TranxSelectedBatch, String> tcUnit2 = new TableColumn<>("Unit2");
        TableColumn<TranxSelectedBatch, Double> tcUnit3Stock = new TableColumn<>("Unit3-Stk");
        tcUnit3Stock.setStyle("-fx-alignment:CENTER;");
        TableColumn<TranxSelectedBatch, Double> tcUnit3 = new TableColumn<>("Unit3");
        //! Adjusting the width for the Columns Responsive
/*
        tcbatchNo.prefWidthProperty().bind(tableView.widthProperty().multiply(0.01));
        tcPurDate.prefWidthProperty().bind(tableView.widthProperty().multiply(0.01));
        tcMFGDate.prefWidthProperty().bind(tableView.widthProperty().multiply(0.01));
        tcExpiryDate.prefWidthProperty().bind(tableView.widthProperty().multiply(0.01));
        tcMRP.prefWidthProperty().bind(tableView.widthProperty().multiply(0.01));
        tcPurRate.prefWidthProperty().bind(tableView.widthProperty().multiply(0.01));
        tcUnit1Stock.prefWidthProperty().bind(tableView.widthProperty().multiply(0.01));
        tcUnit1.prefWidthProperty().bind(tableView.widthProperty().multiply(0.01));
        tcUnit2Stock.prefWidthProperty().bind(tableView.widthProperty().multiply(0.01));
        tcUnit2.prefWidthProperty().bind(tableView.widthProperty().multiply(0.01));
        tcUnit3Stock.prefWidthProperty().bind(tableView.widthProperty().multiply(0.01));
        tcUnit3.prefWidthProperty().bind(tableView.widthProperty().multiply(0.01));
*/

        tableView.getColumns().addAll(tcbatchNo, tcPurDate, tcMFGDate, tcExpiryDate, tcMRP, tcPurRate, tcUnit1Stock, tcUnit1, tcUnit2Stock, tcUnit2, tcUnit3Stock, tcUnit3);
        tcbatchNo.setCellValueFactory(new PropertyValueFactory<>("batchNo"));
        tcPurDate.setCellValueFactory(new PropertyValueFactory<>("purDate"));
        tcMFGDate.setCellValueFactory(new PropertyValueFactory<>("manufactureDate"));
        tcExpiryDate.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        tcMRP.setCellValueFactory(new PropertyValueFactory<>("mrp"));
        tcPurRate.setCellValueFactory(new PropertyValueFactory<>("purchaseRate"));
        tcUnit1Stock.setCellValueFactory(new PropertyValueFactory<>("unit1ClsStock"));
        tcUnit1.setCellValueFactory(new PropertyValueFactory<>("unit1Name"));
        tcUnit2Stock.setCellValueFactory(new PropertyValueFactory<>("unit2ClsStock"));
        tcUnit2.setCellValueFactory(new PropertyValueFactory<>("unit2Name"));
        tcUnit3Stock.setCellValueFactory(new PropertyValueFactory<>("unit3ClsStock"));
        tcUnit3.setCellValueFactory(new PropertyValueFactory<>("unit3Name"));
        tableView.setItems(lstBatch);
        vBox.getChildren().addAll(tableView);
        tableView.setItems(lstBatch);
        if (!selectedRow.getB_no().trim().isEmpty()) {
            try {
                OptionalInt rowIndex = IntStream.range(0, lstBatch.size()).filter(i -> lstBatch.get(i).getBatchNo().equalsIgnoreCase(selectedRow.getB_no())).findFirst();
                if (rowIndex.getAsInt() > -1) {
                    tableView.getSelectionModel().clearAndSelect(rowIndex.getAsInt());
                    GlobalTranx.requestFocusOrDieTrying(tableView, 3);
                }
            } catch (Exception e) {
                loggerTranxCommonPopup.error("OptionalInt " + Globals.getExceptionString(e));
            }

        } else {
            GlobalTranx.requestFocusOrDieTrying(search, 3);
        }

        tableView.setOnKeyReleased((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                TranxSelectedBatch productWindowDTO = tableView.getSelectionModel().getSelectedItem();
                callback.accept(productWindowDTO);
                isNewBatchCreate.accept(false);
                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
            e.consume();
        });
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            tableView.setItems(lstBatch.filtered(s -> s.getBatchNo().contains(newValue)));
        });
        search.setOnKeyReleased((e) -> {
            if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.DOWN) {
                tableView.getSelectionModel().select(0);
                GlobalTranx.requestFocusOrDieTrying(tableView, 3);
            }
            e.consume();
        });
        //? Double click on ledger list
        tableView.setRowFactory(tv -> {
            TableRow<TranxSelectedBatch> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        TranxSelectedBatch productWindowDTO = tableView.getSelectionModel().getSelectedItem();
                        callback.accept(productWindowDTO);
                        isNewBatchCreate.accept(false);
                        primaryStage.close();
                        OverlaysEffect.removeOverlaysEffect(stage);
                    }
                }
            });
            return row;
        });

        //? Borderpane add elements
//        BorderPane.setMargin(vbox_top, new Insets(20, 0, 0, 0)); // Set top margin of 20 pixels
        borderPane.setTop(vbox_top);
        borderPane.setCenter(vBox);
        tableView.getFocusModel().focusedCellProperty().addListener((obs, old, nw) -> {
            borderPane.setBottom(getBottomUIDesignBatch(tableView));
        });

        Scene scene = new Scene(borderPane, calculatePopUpWidth(), calculatePopUpHeight());

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

//        primaryStage.centerOnScreen();
        primaryStage.setX(calculatePopUpX());
        primaryStage.setY(calculatePopUpY());
        primaryStage.show();


        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                /**! ?: Add product popup call for new batch creation */
                callback.accept(null);
                isNewBatchCreate.accept(true);
                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
        });

    }

    //!Final Batch PopUP in ALL Sales
    public static <T> void openBatchPopUpSales(Stage stage, Object selectedRow, String TranxDate, String title, Consumer<TranxSelectedBatch> callback, Consumer<Boolean> isNewBatchCreate) {
        if (selectedRow instanceof SalesInvoiceTable) {
            getAllTranxProductBatchListSales(((SalesInvoiceTable) selectedRow).getProduct_id(), TranxDate);
        } else if (selectedRow instanceof CmpTRowDTOSoToSc) {
            getAllTranxProductBatchListSales(((CmpTRowDTOSoToSc) selectedRow).getProduct_id(), TranxDate);
        }
        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();
        ObservableList<TranxProductWindowDTO> observableLedgerList = FXCollections.observableArrayList();
        primaryStage.initOwner(stage); // Set the owner stage
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");
        Platform.runLater(() -> borderPane.requestFocus());
        //BorderPan under Top Layout.   ...................................................................................................................
        VBox vbox_top = new VBox();
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(calculatePopUpWidth());
        hbox_top.setMaxWidth(calculatePopUpWidth());
        hbox_top.setPrefWidth(calculatePopUpWidth());
        hbox_top.setMaxHeight(50);
        hbox_top.setMinHeight(50);
        hbox_top.setPrefHeight(50);

        Label popup_title = new Label(title);
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        HBox.setMargin(imageView, new Insets(0, 10, 0, 10));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        // Set the font size
        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        Region spacer = new Region();
        Button addButton = new Button("+ Add Batch");
        addButton.setMinWidth(130);
        addButton.setMaxWidth(130);
        addButton.setMinHeight(30);
        addButton.setMaxHeight(30);
        addButton.setId("submit-btn");
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
        Boolean isNegetive = false;
        if (selectedRow instanceof SalesInvoiceTable) {
            isNegetive = ((SalesInvoiceTable) selectedRow).getSelectedProduct().getNegetive();
        } else if (selectedRow instanceof CmpTRowDTOSoToSc) {
            isNegetive = ((CmpTRowDTOSoToSc) selectedRow).getSelectedProduct().getNegetive();
        }
        if (isNegetive == true) {
            hbox_top.getChildren().add(addButton);
        }
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #D9F0FB; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 2 0;");
        //BorderPane Under Center Layout.....................................................................................................
        HBox hbox_top1 = new HBox();
        hbox_top1.setMinWidth(calculatePopUpWidth());
        hbox_top1.setMaxWidth(calculatePopUpWidth());
        hbox_top1.setPrefWidth(calculatePopUpWidth());
        hbox_top1.setMaxHeight(50);
        hbox_top1.setMinHeight(50);
        hbox_top1.setMaxHeight(50);
        hbox_top1.setAlignment(Pos.TOP_LEFT);
        hbox_top1.setStyle("-fx-background-color: white;");
        TextField search = new TextField("");
        search.setPromptText("Search");
        search.setPadding(new Insets(10, 0, 0, 10));
        search.setPrefWidth(350);
        HBox.setMargin(search, new Insets(0, 0, 0, 10));
        hbox_top1.getChildren().addAll(search);
        vbox_top.setSpacing(10);
        vbox_top.getChildren().addAll(hbox_top, hbox_top1);
        Object[] selectedItem = new Object[8];
        Integer initialIndex = -1;

        //BorderPane Under Bottom Layout..............................................................................................................
        VBox vBox = new VBox();
        TableView<TranxSelectedBatch> tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
//        tableView.setPrefHeight(500);
//        tableView.setMaxHeight(500);
//        tableView.setMinHeight(500);
        tableView.setMinHeight(GlobalTranx.TranxCalculatePer(calculatePopUpHeight(), 50));
        TableColumn<TranxSelectedBatch, String> tcbatchNo = new TableColumn<>("BatchNo");
        TableColumn<TranxSelectedBatch, String> tcPurDate = new TableColumn<>("Purchase Date");
        TableColumn<TranxSelectedBatch, String> tcMFGDate = new TableColumn<>("MFG Date");
        TableColumn<TranxSelectedBatch, String> tcExpiryDate = new TableColumn<>("Expiry Date");
        TableColumn<TranxSelectedBatch, String> tcMRP = new TableColumn<>("MRP");
        TableColumn<TranxSelectedBatch, String> tcPurRate = new TableColumn<>("Purchase Rate");
        TableColumn<TranxSelectedBatch, Double> tcUnit1Stock = new TableColumn<>("Unit1-Stk");
        TableColumn<TranxSelectedBatch, String> tcUnit1 = new TableColumn<>("Unit1");
        TableColumn<TranxSelectedBatch, Double> tcUnit2Stock = new TableColumn<>("Unit2-Stk");
        TableColumn<TranxSelectedBatch, String> tcUnit2 = new TableColumn<>("Unit2");
        TableColumn<TranxSelectedBatch, Double> tcUnit3Stock = new TableColumn<>("Unit3-Stk");
        TableColumn<TranxSelectedBatch, Double> tcUnit3 = new TableColumn<>("Unit3");
        // Adjusting the width for the Columns
        /*tcbatchNo.setPrefWidth(40);
        tcPurDate.setPrefWidth(60);
        tcMFGDate.setPrefWidth(60);
        tcExpiryDate.setPrefWidth(60);
        tcMRP.setPrefWidth(30);
        tcPurRate.setPrefWidth(30);
        tcStock.setPrefWidth(40);
        tcMarginPer.setPrefWidth(30);
        tcRateA.setPrefWidth(30);
        tcRateB.setPrefWidth(30);
        tcRateC.setPrefWidth(30);
        tcAct.setPrefWidth(30);*/
        tableView.getColumns().addAll(tcbatchNo, tcPurDate, tcMFGDate, tcExpiryDate, tcMRP, tcPurRate, tcUnit1Stock, tcUnit1, tcUnit2Stock, tcUnit2, tcUnit3Stock, tcUnit3);
        tcbatchNo.setCellValueFactory(new PropertyValueFactory<>("batchNo"));
        tcPurDate.setCellValueFactory(new PropertyValueFactory<>("purDate"));
        tcMFGDate.setCellValueFactory(new PropertyValueFactory<>("manufactureDate"));
        tcExpiryDate.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        tcMRP.setCellValueFactory(new PropertyValueFactory<>("mrp"));
        tcPurRate.setCellValueFactory(new PropertyValueFactory<>("purchaseRate"));
        tcUnit1Stock.setCellValueFactory(new PropertyValueFactory<>("unit1ClsStock"));
        tcUnit1.setCellValueFactory(new PropertyValueFactory<>("unit1Name"));
        tcUnit2Stock.setCellValueFactory(new PropertyValueFactory<>("unit2ClsStock"));
        tcUnit2.setCellValueFactory(new PropertyValueFactory<>("unit2Name"));
        tcUnit3Stock.setCellValueFactory(new PropertyValueFactory<>("unit3ClsStock"));
        tcUnit3.setCellValueFactory(new PropertyValueFactory<>("unit3Name"));
        tableView.setItems(lstBatch);
        vBox.getChildren().addAll(tableView);
        tableView.setItems(lstBatch);
        String bno;
        if (selectedRow instanceof SalesInvoiceTable && ((SalesInvoiceTable) selectedRow).getB_no().trim().isEmpty()) {
            bno = ((SalesInvoiceTable) selectedRow).getB_no();
        } else if (selectedRow instanceof CmpTRowDTOSoToSc && ((CmpTRowDTOSoToSc) selectedRow).getB_no().trim().isEmpty()) {
            bno = ((CmpTRowDTOSoToSc) selectedRow).getB_no();
        } else {
            bno = "";
        }
        if (!bno.isEmpty()) {
            try {
                OptionalInt rowIndex = IntStream.range(0, lstBatch.size()).filter(i -> lstBatch.get(i).getBatchNo().equalsIgnoreCase(bno)).findFirst();
                if (rowIndex.getAsInt() > -1) {
                    tableView.getSelectionModel().clearAndSelect(rowIndex.getAsInt());
                    tableView.scrollTo(rowIndex.getAsInt() - 1);
                    GlobalTranx.requestFocusOrDieTrying(tableView, 3);
                }
            } catch (Exception e) {
                loggerTranxCommonPopup.error("OptionalInt " + Globals.getExceptionString(e));
            }
        } else {
            GlobalTranx.requestFocusOrDieTrying(search, 3);
        }


        tableView.setOnKeyReleased((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                TranxSelectedBatch productWindowDTO = tableView.getSelectionModel().getSelectedItem();
                callback.accept(productWindowDTO);
                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
            e.consume();
        });
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            tableView.setItems(lstBatch.filtered(s -> s.getBatchNo().contains(newValue)));
        });
        search.setOnKeyReleased((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                tableView.getSelectionModel().select(0);
                GlobalTranx.requestFocusOrDieTrying(tableView, 3);
            }
            e.consume();
        });
        search.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN) {
                tableView.requestFocus();
                tableView.getSelectionModel().select(0);
            }
        });
        //? Double click on ledger list
        tableView.setRowFactory(tv -> {
            TableRow<TranxSelectedBatch> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        TranxSelectedBatch productWindowDTO = tableView.getSelectionModel().getSelectedItem();
                        callback.accept(productWindowDTO);
                        primaryStage.close();
                        OverlaysEffect.removeOverlaysEffect(stage);
                    }
                }
            });
            return row;
        });

        //? Borderpane add elements
//        BorderPane.setMargin(vbox_top, new Insets(20, 0, 0, 0)); // Set top margin of 20 pixels
        borderPane.setTop(vbox_top);
        borderPane.setCenter(vBox);
        tableView.getFocusModel().focusedCellProperty().addListener((obs, old, nw) -> {
            borderPane.setBottom(getBottomUIDesignBatch(tableView));
        });

        Scene scene = new Scene(borderPane, calculatePopUpWidth(), calculatePopUpHeight());

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

        primaryStage.setX(calculatePopUpX());
        primaryStage.setY(calculatePopUpY());
        primaryStage.show();
    }

    public static <T> void openBatchPopUpSales(Stage stage, PurchaseInvoiceTable selectedRow, String TranxDate, String title, Consumer<TranxSelectedBatch> callback, Consumer<Boolean> isNewBatchCreate) {
//        System.out.println("openBatchPopUp selectedRow" + selectedRow + TranxDate);
        getAllTranxProductBatchListSales(selectedRow.getProduct_id(), TranxDate);
        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();
        ObservableList<TranxProductWindowDTO> observableLedgerList = FXCollections.observableArrayList();
        primaryStage.initOwner(stage); // Set the owner stage
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");
        Platform.runLater(() -> borderPane.requestFocus());
        //BorderPan under Top Layout.   ...................................................................................................................
        VBox vbox_top = new VBox();
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(calculatePopUpWidth());
        hbox_top.setMaxWidth(calculatePopUpWidth());
        hbox_top.setPrefWidth(calculatePopUpWidth());
        hbox_top.setMaxHeight(50);
        hbox_top.setMinHeight(50);
        hbox_top.setPrefHeight(50);

        Label popup_title = new Label(title);
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        HBox.setMargin(imageView, new Insets(0, 10, 0, 10));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        // Set the font size
        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        Region spacer = new Region();
        Button addButton = new Button("+ Add Batch");
        addButton.setMinWidth(130);
        addButton.setMaxWidth(130);
        addButton.setMinHeight(30);
        addButton.setMaxHeight(30);
        addButton.setId("submit-btn");
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
        if (selectedRow.getSelectedProduct().getNegetive() == true) {
            hbox_top.getChildren().add(addButton);
        }
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #D9F0FB; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 2 0;");
        //BorderPane Under Center Layout.....................................................................................................
        HBox hbox_top1 = new HBox();
        hbox_top1.setMinWidth(calculatePopUpWidth());
        hbox_top1.setMaxWidth(calculatePopUpWidth());
        hbox_top1.setPrefWidth(calculatePopUpWidth());
        hbox_top1.setMaxHeight(50);
        hbox_top1.setMinHeight(50);
        hbox_top1.setMaxHeight(50);
        hbox_top1.setAlignment(Pos.TOP_LEFT);
        hbox_top1.setStyle("-fx-background-color: white;");
        TextField search = new TextField("");
        search.setPromptText("Search");
        search.setPadding(new Insets(10, 0, 0, 10));
        search.setPrefWidth(350);
        HBox.setMargin(search, new Insets(0, 0, 0, 10));
        hbox_top1.getChildren().addAll(search);
        vbox_top.setSpacing(10);
        vbox_top.getChildren().addAll(hbox_top, hbox_top1);
        Object[] selectedItem = new Object[8];
        Integer initialIndex = -1;

        //BorderPane Under Bottom Layout..............................................................................................................
        VBox vBox = new VBox();
        TableView<TranxSelectedBatch> tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
//        tableView.setPrefHeight(500);
//        tableView.setMaxHeight(500);
//        tableView.setMinHeight(500);
        tableView.setMinHeight(GlobalTranx.TranxCalculatePer(calculatePopUpHeight(), 50));
        TableColumn<TranxSelectedBatch, String> tcbatchNo = new TableColumn<>("BatchNo");
        TableColumn<TranxSelectedBatch, String> tcPurDate = new TableColumn<>("Purchase Date");
        TableColumn<TranxSelectedBatch, String> tcMFGDate = new TableColumn<>("MFG Date");
        TableColumn<TranxSelectedBatch, String> tcExpiryDate = new TableColumn<>("Expiry Date");
        TableColumn<TranxSelectedBatch, String> tcMRP = new TableColumn<>("MRP");
        TableColumn<TranxSelectedBatch, String> tcPurRate = new TableColumn<>("Purchase Rate");
        TableColumn<TranxSelectedBatch, Double> tcUnit1Stock = new TableColumn<>("Unit1-Stock");
        TableColumn<TranxSelectedBatch, String> tcUnit1 = new TableColumn<>("Unit1");
        TableColumn<TranxSelectedBatch, Double> tcUnit2Stock = new TableColumn<>("Unit2-Stock");
        TableColumn<TranxSelectedBatch, String> tcUnit2 = new TableColumn<>("Unit2");
        TableColumn<TranxSelectedBatch, Double> tcUnit3Stock = new TableColumn<>("Unit3-Stock");
        TableColumn<TranxSelectedBatch, Double> tcUnit3 = new TableColumn<>("Unit3");
        // Adjusting the width for the Columns
        /*tcbatchNo.setPrefWidth(40);
        tcPurDate.setPrefWidth(60);
        tcMFGDate.setPrefWidth(60);
        tcExpiryDate.setPrefWidth(60);
        tcMRP.setPrefWidth(30);
        tcPurRate.setPrefWidth(30);
        tcStock.setPrefWidth(40);
        tcMarginPer.setPrefWidth(30);
        tcRateA.setPrefWidth(30);
        tcRateB.setPrefWidth(30);
        tcRateC.setPrefWidth(30);
        tcAct.setPrefWidth(30);*/
        tableView.getColumns().addAll(tcbatchNo, tcPurDate, tcMFGDate, tcExpiryDate, tcMRP, tcPurRate, tcUnit1Stock, tcUnit1, tcUnit2Stock, tcUnit2, tcUnit3Stock, tcUnit3);
        tcbatchNo.setCellValueFactory(new PropertyValueFactory<>("batchNo"));
        tcPurDate.setCellValueFactory(new PropertyValueFactory<>("purDate"));
        tcMFGDate.setCellValueFactory(new PropertyValueFactory<>("manufactureDate"));
        tcExpiryDate.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        tcMRP.setCellValueFactory(new PropertyValueFactory<>("mrp"));
        tcPurRate.setCellValueFactory(new PropertyValueFactory<>("purchaseRate"));
        tcUnit1Stock.setCellValueFactory(new PropertyValueFactory<>("unit1ClsStock"));
        tcUnit1.setCellValueFactory(new PropertyValueFactory<>("unit1Name"));
        tcUnit2Stock.setCellValueFactory(new PropertyValueFactory<>("unit2ClsStock"));
        tcUnit2.setCellValueFactory(new PropertyValueFactory<>("unit2Name"));
        tcUnit3Stock.setCellValueFactory(new PropertyValueFactory<>("unit3ClsStock"));
        tcUnit3.setCellValueFactory(new PropertyValueFactory<>("unit3Name"));
        tableView.setItems(lstBatch);
        vBox.getChildren().addAll(tableView);
        tableView.setItems(lstBatch);
        if (!selectedRow.getB_no().trim().isEmpty()) {
            try {
                OptionalInt rowIndex = IntStream.range(0, lstBatch.size()).filter(i -> lstBatch.get(i).getBatchNo().equalsIgnoreCase(selectedRow.getB_no())).findFirst();
                if (rowIndex.getAsInt() > -1) {
                    tableView.getSelectionModel().clearAndSelect(rowIndex.getAsInt());
                    GlobalTranx.requestFocusOrDieTrying(tableView, 3);
                }
            } catch (Exception e) {
                loggerTranxCommonPopup.error("OptionalInt " + Globals.getExceptionString(e));
            }

        } else {
            GlobalTranx.requestFocusOrDieTrying(search, 3);
        }

        tableView.setOnKeyReleased((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                TranxSelectedBatch productWindowDTO = tableView.getSelectionModel().getSelectedItem();
                callback.accept(productWindowDTO);
                isNewBatchCreate.accept(false);
                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
            e.consume();
        });
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            tableView.setItems(lstBatch.filtered(s -> s.getBatchNo().contains(newValue)));
        });
        search.setOnKeyReleased((e) -> {
            if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.DOWN) {
                tableView.getSelectionModel().select(0);
                GlobalTranx.requestFocusOrDieTrying(tableView, 3);
            }
            e.consume();
        });
        //? Double click on ledger list
        tableView.setRowFactory(tv -> {
            TableRow<TranxSelectedBatch> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        TranxSelectedBatch productWindowDTO = tableView.getSelectionModel().getSelectedItem();
                        callback.accept(productWindowDTO);
                        isNewBatchCreate.accept(false);
                        primaryStage.close();
                        OverlaysEffect.removeOverlaysEffect(stage);
                    }
                }
            });
            return row;
        });

        //? Borderpane add elements
//        BorderPane.setMargin(vbox_top, new Insets(20, 0, 0, 0)); // Set top margin of 20 pixels
        borderPane.setTop(vbox_top);
        borderPane.setCenter(vBox);
        tableView.getFocusModel().focusedCellProperty().addListener((obs, old, nw) -> {
            borderPane.setBottom(getBottomUIDesignBatch(tableView));
        });

        Scene scene = new Scene(borderPane, calculatePopUpWidth(), calculatePopUpHeight());

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

//        primaryStage.centerOnScreen();
        primaryStage.setX(calculatePopUpX());
        primaryStage.setY(calculatePopUpY());
        primaryStage.show();


        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                /**! ?: Add product popup call for new batch creation */
                callback.accept(null);
                isNewBatchCreate.accept(true);
                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
        });

    }

    public static <T> void openBatchPopUpSalesChallan(Stage stage, CmpTRowDTOSoToSc selectedRow, String TranxDate, String title, Consumer<TranxSelectedBatch> callback) {
//        System.out.println("openBatchPopUp selectedRow" + selectedRow + TranxDate);
        getAllTranxProductBatchListSalesChallan(selectedRow, TranxDate);
        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();
        ObservableList<TranxProductWindowDTO> observableLedgerList = FXCollections.observableArrayList();
        primaryStage.initOwner(stage); // Set the owner stage
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");
        Platform.runLater(() -> borderPane.requestFocus());
        //BorderPan under Top Layout.   ...................................................................................................................
        VBox vbox_top = new VBox();
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(calculatePopUpWidth());
        hbox_top.setMaxWidth(calculatePopUpWidth());
        hbox_top.setPrefWidth(calculatePopUpWidth());
        hbox_top.setMaxHeight(50);
        hbox_top.setMinHeight(50);
        hbox_top.setPrefHeight(50);

        Label popup_title = new Label(title);
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        HBox.setMargin(imageView, new Insets(0, 10, 0, 10));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        // Set the font size
        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        Region spacer = new Region();
//        Button addButton = new Button("+ Add Product");
//        addButton.setMinWidth(130);
//        addButton.setMaxWidth(130);
//        addButton.setMinHeight(30);
//        addButton.setMaxHeight(30);
//        addButton.setId("submit-btn");
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
//        hbox_top.getChildren().add(addButton);
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #D9F0FB; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 2 0;");
        //BorderPane Under Center Layout.....................................................................................................
        HBox hbox_top1 = new HBox();
        hbox_top1.setMinWidth(calculatePopUpWidth());
        hbox_top1.setMaxWidth(calculatePopUpWidth());
        hbox_top1.setPrefWidth(calculatePopUpWidth());
        hbox_top1.setMaxHeight(50);
        hbox_top1.setMinHeight(50);
        hbox_top1.setMaxHeight(50);
        hbox_top1.setAlignment(Pos.TOP_LEFT);
        hbox_top1.setStyle("-fx-background-color: white;");
        TextField search = new TextField("");
        search.setPromptText("Search");
        search.setPadding(new Insets(10, 0, 0, 10));
        search.setPrefWidth(350);
        HBox.setMargin(search, new Insets(0, 0, 0, 10));
        hbox_top1.getChildren().addAll(search);
        vbox_top.setSpacing(10);
        vbox_top.getChildren().addAll(hbox_top, hbox_top1);
        Object[] selectedItem = new Object[8];
        Integer initialIndex = -1;

        //BorderPane Under Bottom Layout..............................................................................................................
        VBox vBox = new VBox();
        TableView<TranxSelectedBatch> tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
//        tableView.setPrefHeight(500);
//        tableView.setMaxHeight(500);
//        tableView.setMinHeight(500);
        tableView.setMinHeight(GlobalTranx.TranxCalculatePer(calculatePopUpHeight(), 50));
        TableColumn<TranxSelectedBatch, String> tcbatchNo = new TableColumn<>("BatchNo");
        TableColumn<TranxSelectedBatch, String> tcPurDate = new TableColumn<>("Purchase Date");
        TableColumn<TranxSelectedBatch, String> tcMFGDate = new TableColumn<>("MFG Date");
        TableColumn<TranxSelectedBatch, String> tcExpiryDate = new TableColumn<>("Expiry Date");
        TableColumn<TranxSelectedBatch, String> tcMRP = new TableColumn<>("MRP");
        TableColumn<TranxSelectedBatch, String> tcPurRate = new TableColumn<>("Purchase Rate");
        TableColumn<TranxSelectedBatch, Double> tcUnit1Stock = new TableColumn<>("Unit1-Stock");
        TableColumn<TranxSelectedBatch, String> tcUnit1 = new TableColumn<>("Unit1");
        TableColumn<TranxSelectedBatch, Double> tcUnit2Stock = new TableColumn<>("Unit2-Stock");
        TableColumn<TranxSelectedBatch, String> tcUnit2 = new TableColumn<>("Unit2");
        TableColumn<TranxSelectedBatch, Double> tcUnit3Stock = new TableColumn<>("Unit3-Stock");
        TableColumn<TranxSelectedBatch, Double> tcUnit3 = new TableColumn<>("Unit3");
        // Adjusting the width for the Columns
        /*tcbatchNo.setPrefWidth(40);
        tcPurDate.setPrefWidth(60);
        tcMFGDate.setPrefWidth(60);
        tcExpiryDate.setPrefWidth(60);
        tcMRP.setPrefWidth(30);
        tcPurRate.setPrefWidth(30);
        tcStock.setPrefWidth(40);
        tcMarginPer.setPrefWidth(30);
        tcRateA.setPrefWidth(30);
        tcRateB.setPrefWidth(30);
        tcRateC.setPrefWidth(30);
        tcAct.setPrefWidth(30);*/
        tableView.getColumns().addAll(tcbatchNo, tcPurDate, tcMFGDate, tcExpiryDate, tcMRP, tcPurRate, tcUnit1Stock, tcUnit1, tcUnit2Stock, tcUnit2, tcUnit3Stock, tcUnit3);
        tcbatchNo.setCellValueFactory(new PropertyValueFactory<>("batchNo"));
        tcPurDate.setCellValueFactory(new PropertyValueFactory<>("purDate"));
        tcMFGDate.setCellValueFactory(new PropertyValueFactory<>("manufactureDate"));
        tcExpiryDate.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        tcMRP.setCellValueFactory(new PropertyValueFactory<>("mrp"));
        tcPurRate.setCellValueFactory(new PropertyValueFactory<>("purchaseRate"));
        tcUnit1Stock.setCellValueFactory(new PropertyValueFactory<>("unit1ClsStock"));
        tcUnit1.setCellValueFactory(new PropertyValueFactory<>("unit1Name"));
        tcUnit2Stock.setCellValueFactory(new PropertyValueFactory<>("unit2ClsStock"));
        tcUnit2.setCellValueFactory(new PropertyValueFactory<>("unit2Name"));
        tcUnit3Stock.setCellValueFactory(new PropertyValueFactory<>("unit3ClsStock"));
        tcUnit3.setCellValueFactory(new PropertyValueFactory<>("unit3Name"));
        tableView.setItems(lstBatch);
        vBox.getChildren().addAll(tableView);
        tableView.setItems(lstBatch);
        if (!selectedRow.getB_no().trim().isEmpty()) {
            try {
                OptionalInt rowIndex = IntStream.range(0, lstBatch.size()).filter(i -> lstBatch.get(i).getBatchNo().equalsIgnoreCase(selectedRow.getB_no())).findFirst();
                if (rowIndex.getAsInt() > -1) {
                    tableView.getSelectionModel().select(rowIndex.getAsInt());
                    GlobalTranx.requestFocusOrDieTrying(tableView, 3);
                }
            } catch (Exception e) {
                loggerTranxCommonPopup.error("OptionalInt " + Globals.getExceptionString(e));
            }

        } else {
            GlobalTranx.requestFocusOrDieTrying(search, 3);
        }


        tableView.setOnKeyReleased((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                TranxSelectedBatch productWindowDTO = tableView.getSelectionModel().getSelectedItem();
                callback.accept(productWindowDTO);
                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
            e.consume();
        });
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            tableView.setItems(lstBatch.filtered(s -> s.getBatchNo().contains(newValue)));
        });
        search.setOnKeyReleased((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                tableView.getSelectionModel().select(0);
                GlobalTranx.requestFocusOrDieTrying(tableView, 3);
            }
            e.consume();
        });
        //? Double click on ledger list
        tableView.setRowFactory(tv -> {
            TableRow<TranxSelectedBatch> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        TranxSelectedBatch productWindowDTO = tableView.getSelectionModel().getSelectedItem();
                        callback.accept(productWindowDTO);
                        primaryStage.close();
                        OverlaysEffect.removeOverlaysEffect(stage);
                    }
                }
            });
            return row;
        });

        //? Borderpane add elements
//        BorderPane.setMargin(vbox_top, new Insets(20, 0, 0, 0)); // Set top margin of 20 pixels
        borderPane.setTop(vbox_top);
        borderPane.setCenter(vBox);
        tableView.getFocusModel().focusedCellProperty().addListener((obs, old, nw) -> {
//            System.out.println("tableView : nw" + nw);
            borderPane.setBottom(getBottomUIDesignBatch(tableView));
        });

        Scene scene = new Scene(borderPane, calculatePopUpWidth(), calculatePopUpHeight());

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

//        primaryStage.centerOnScreen();
        primaryStage.setX(calculatePopUpX());
        primaryStage.setY(calculatePopUpY());
        primaryStage.show();

      /*  addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                selectedItem[1] = tfPower.getText();
                insertIntoTable(selectedItem[0].toString(), selectedItem[1].toString(), selectedItem[2].toString()
                        , selectedItem[3].toString());
            }
        });*//*
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
//                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
        });*/

    }

    private static VBox getBottomUIDesignBatch(TableView<TranxSelectedBatch> tableView) {
        VBox mainVbox = new VBox();
        mainVbox.setPrefHeight(500.0);
        mainVbox.setPadding(new Insets(10));
//        mainVbox.setStyle("-fx-padding:10px;");
//        mainVbox.getStyleClass().add("ledgertabMainDiv");
        VBox innerDiv = new VBox();
//        innerDiv.getStyleClass().add("mainGridStyle");#f8f0d2
        innerDiv.setStyle("-fx-background-color:#f8f0d2;-fx-padding:15px;-fx-background-radius:6;");
        HBox innerHbox = new HBox();
//        innerHbox.getStyleClass().add("ledgerTabInnerHb");
        innerHbox.setPadding(new Insets(10));
        //! VBOX1
        VBox vBox1 = new VBox();
        HBox.setHgrow(vBox1, Priority.ALWAYS);
        vBox1.setStyle("-fx-border-width: 0 1.2 0 0; -fx-border-color: #EAD8B1;");
//        vBox1.getStyleClass().add("ledgerSingleInfo");
        vBox1.setPadding(new Insets(10));
        vBox1.setSpacing(8);
        //? hbox => 4 UNIT-1 FSRMH,FSRAI,CSRMH CSRAI
        HBox v1h1 = new HBox();
        v1h1.setSpacing(10);
        Label lbl1v1h1 = new Label("Unit : ");
        lbl1v1h1.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v1h1 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit1Name() : "");
        lbl2v1h1.setStyle("-fx-font-weight:bold;");
        v1h1.getChildren().addAll(lbl1v1h1, lbl2v1h1);

        HBox v1h2 = new HBox();
        v1h2.setSpacing(10);
        Label lbl1v1h2 = new Label("RATE-1 : ");
        lbl1v1h2.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v1h2 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit1FSRMH().toString() : "");
//        lbl2v1h2.getStyleClass().add("gridCommonTextStyle");
        lbl2v1h2.setStyle("-fx-font-weight:bold;");


        v1h2.getChildren().addAll(lbl1v1h2, lbl2v1h2);

        HBox v1h3 = new HBox();
        v1h3.setSpacing(10);
        Label lbl1v1h3 = new Label("RATE-2 : ");
//        lbl1v1h3.getStyleClass().add("gridCommonTextStyle");
        lbl1v1h3.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v1h3 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit1FSRAI().toString() : "");
        lbl2v1h3.setStyle("-fx-font-weight:bold;");

        v1h3.getChildren().addAll(lbl1v1h3, lbl2v1h3);

        HBox v1h4 = new HBox();
        v1h4.setSpacing(10);
        Label lbl1v1h4 = new Label("RATE-3 : ");
        lbl1v1h4.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v1h4 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit1CSRMH().toString() : "");
        lbl2v1h4.setStyle("-fx-font-weight:bold;");
        v1h4.getChildren().addAll(lbl1v1h4, lbl2v1h4);

      /*  HBox v1h5 = new HBox();
        v1h5.setSpacing(10);
        Label lbl1v1h5 = new Label("CSRAI : ");
        lbl1v1h5.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v1h5 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit1CSRAI().toString() : "");
        lbl2v1h5.setStyle("-fx-font-weight:bold;");
        v1h5.getChildren().addAll(lbl1v1h5, lbl2v1h5);
        v1h5
*/
        vBox1.getChildren().addAll(v1h1, v1h2, v1h3, v1h4);

        //! VBOX2
        VBox vBox2 = new VBox();
        HBox.setHgrow(vBox2, Priority.ALWAYS);
        vBox2.setStyle("-fx-border-width: 0 1.2 0 0; -fx-border-color: #EAD8B1;");
        vBox2.setPadding(new Insets(10));
        vBox2.setSpacing(8);
        //? hbox => 4 UNIT-1 FSRMH,FSRAI,CSRMH CSRAI
        HBox v2h1 = new HBox();
        v2h1.setSpacing(10);
        Label lbl1v2h1 = new Label("Unit : ");
        lbl1v2h1.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v2h1 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit2Name() : "");
        lbl2v2h1.setStyle("-fx-font-weight:bold;");
        v2h1.getChildren().addAll(lbl1v2h1, lbl2v2h1);

        HBox v2h2 = new HBox();
        v2h2.setSpacing(10);
        Label lbl1v2h2 = new Label("RATE-1 : ");
        lbl1v2h2.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v2h2 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit2FSRMH().toString() : "");
//        lbl2v1h2.getStyleClass().add("gridCommonTextStyle");
        lbl2v2h2.setStyle("-fx-font-weight:bold;");
        v2h2.getChildren().addAll(lbl1v2h2, lbl2v2h2);

        HBox v2h3 = new HBox();
        v2h3.setSpacing(10);
        Label lbl1v2h3 = new Label("RATE-2 : ");
//        lbl1v1h3.getStyleClass().add("gridCommonTextStyle");
        lbl1v2h3.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v2h3 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit2FSRAI().toString() : "");
        lbl2v2h3.setStyle("-fx-font-weight:bold;");

        v2h3.getChildren().addAll(lbl1v2h3, lbl2v2h3);

        HBox v2h4 = new HBox();
        v2h4.setSpacing(10);
        Label lbl1v2h4 = new Label("RATE-3 : ");
        lbl1v2h4.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v2h4 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit2CSRMH().toString() : "");
        lbl2v2h4.setStyle("-fx-font-weight:bold;");
        v2h4.getChildren().addAll(lbl1v2h4, lbl2v2h4);

       /* HBox v2h5 = new HBox();
        v2h5.setSpacing(10);
        Label lbl1v2h5 = new Label("CSRAI : ");
        lbl1v2h5.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v2h5 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit2CSRAI().toString() : "");
        lbl2v2h5.setStyle("-fx-font-weight:bold;");
        v2h5.getChildren().addAll(lbl1v2h5, lbl2v2h5);
        v2h5
*/
        vBox2.getChildren().addAll(v2h1, v2h2, v2h3, v2h4);

        //! VBOX3
        VBox vBox3 = new VBox();
        HBox.setHgrow(vBox3, Priority.ALWAYS);

        vBox3.setStyle("-fx-border-width: 0 1.2 0 0; -fx-border-color: #EAD8B1;");
        vBox3.setPadding(new Insets(10));
        vBox3.setSpacing(8);
        //? hbox => 4 UNIT-1 FSRMH,FSRAI,CSRMH CSRAI
        HBox v3h1 = new HBox();
        v3h1.setSpacing(10);
        Label lbl1v3h1 = new Label("Unit : ");
        lbl1v3h1.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v3h1 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit3Name() : "");
        lbl2v3h1.setStyle("-fx-font-weight:bold;");
        v3h1.getChildren().addAll(lbl1v3h1, lbl2v3h1);

        HBox v3h2 = new HBox();
        v3h2.setSpacing(10);
        Label lbl1v3h2 = new Label("RATE-1 : ");
        lbl1v3h2.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v3h2 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit3FSRMH().toString() : "");
//        lbl2v1h2.getStyleClass().add("gridCommonTextStyle");
        lbl2v3h2.setStyle("-fx-font-weight:bold;");
        v3h2.getChildren().addAll(lbl1v3h2, lbl2v3h2);

        HBox v3h3 = new HBox();
        v3h3.setSpacing(10);
        Label lbl1v3h3 = new Label("RATE-2 : ");
//        lbl1v1h3.getStyleClass().add("gridCommonTextStyle");
        lbl1v3h3.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v3h3 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit3FSRAI().toString() : "");
        lbl2v3h3.setStyle("-fx-font-weight:bold;");

        v3h3.getChildren().addAll(lbl1v3h3, lbl2v3h3);

        HBox v3h4 = new HBox();
        v3h4.setSpacing(10);
        Label lbl1v3h4 = new Label("RATE-3 : ");
        lbl1v3h4.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v3h4 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit3CSRMH().toString() : "");
        lbl2v3h4.setStyle("-fx-font-weight:bold;");
        v3h4.getChildren().addAll(lbl1v3h4, lbl2v3h4);

/*        HBox v3h5 = new HBox();
        v3h5.setSpacing(10);
        Label lbl1v3h5 = new Label("CSRAI : ");
        lbl1v3h5.setStyle("-fx-font-weight:bold;-fx-text-fill:#b8762b;");
        Label lbl2v3h5 = new Label(tableView.getSelectionModel().getSelectedItem() != null ? tableView.getSelectionModel().getSelectedItem().getUnit3CSRAI().toString() : "");
        lbl2v3h5.setStyle("-fx-font-weight:bold;");
        v3h5.getChildren().addAll(lbl1v3h5, lbl2v3h5);
        v3h5*/

        vBox3.getChildren().addAll(v3h1, v3h2, v3h3, v3h4);


        innerHbox.getChildren().addAll(vBox1, vBox2, vBox3);
        innerDiv.getChildren().addAll(innerHbox);
        mainVbox.getChildren().add(innerDiv);
        return mainVbox;
    }

    private static ObservableList<TranxSelectedBatch> getAllTranxProductBatchList(int productId, String tranxDate) {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("productId", String.valueOf(productId));
            /*body.put("product_id", String.valueOf(selectedRow.getProductId()));
            body.put("level_a_id", selectedRow.getLevelAId() > 0 ? String.valueOf(selectedRow.getLevelAId()) : "");
            body.put("level_b_id", selectedRow.getLevelBId() > 0 ? String.valueOf(selectedRow.getLevelBId()) : "");
            body.put("level_c_id", selectedRow.getLevelCId() > 0 ? String.valueOf(selectedRow.getLevelCId()) : "");
            body.put("unit_id", String.valueOf(selectedRow.getUnitId()));*/
            body.put("invoice_date", DateConvertUtil.convertStringDatetoAPIDateString(tranxDate));
            String formData = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.GET_TRANX_PRODUCT_BATCH_LIST);
//            System.out.println("Batch Response org " + response.body());
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseArray = jsonObject.get("resData").getAsJsonArray();
                lstBatch.clear();
                if (responseArray.size() > 0) {
                    for (JsonElement element : responseArray) {
                        try {
                            JsonObject eleObj = new Gson().fromJson(element, JsonObject.class);
//                            System.out.println("eleOBj =>" + eleObj);
                            JsonArray jarr = eleObj.get("unit_list").getAsJsonArray();
                            String unit1Name = "";
                            Integer unit1Id = 0;
                            Double unit1FSRMH = 0.0;
                            Double unit1FSRAI = 0.0;
                            Double unit1CSRMH = 0.0;
                            Double unit1CSRAI = 0.0;
                            Double unit1Conv = 0.0;
                            String unit1ClsStock = "";
                            Double unit1ActualStock = 0.0;
                            if (jarr.size() >= 1) {
                                JsonObject unit1Obj = jarr.get(0).getAsJsonObject();
                                unit1Name = unit1Obj.get("unitName").getAsString();
                                unit1Id = unit1Obj.get("unitid").getAsInt();
                                unit1FSRMH = unit1Obj.get("fsrmh").getAsDouble();
                                unit1FSRAI = unit1Obj.get("fsrai").getAsDouble();
                                unit1CSRMH = unit1Obj.get("csrmh").getAsDouble();
                                unit1CSRAI = unit1Obj.get("csrai").getAsDouble();
                                unit1Conv = unit1Obj.get("conversion_rate").getAsDouble();
//                                +""+unit1Obj.get("logical_stock").getAsDouble()>0? unit1Obj.get("logical_stock").getAsDouble():""
                                String PhyStock = unit1Obj.get("physical_stock").getAsDouble() > 0 ? unit1Obj.get("physical_stock").getAsString() : "";
                                String LogStock = unit1Obj.get("logical_stock").getAsDouble() > 0 ? "(" + unit1Obj.get("logical_stock").getAsString() + ")" : "";
                                unit1ClsStock = PhyStock + LogStock;
                                unit1ActualStock = unit1Obj.get("actual_stock").getAsDouble();
                            }
                            String unit2Name = "";
                            Integer unit2Id = 0;
                            Double unit2FSRMH = 0.0;
                            Double unit2FSRAI = 0.0;
                            Double unit2CSRMH = 0.0;
                            Double unit2CSRAI = 0.0;
                            Double unit2Conv = 0.0;
                            String unit2ClsStock = "";
                            Double unit2ActualStock = 0.0;
                            if (jarr.size() >= 2) {
                                JsonObject unit2Obj = jarr.get(1).getAsJsonObject();
                                unit2Name = unit2Obj.get("unitName").getAsString();
                                unit2Id = unit2Obj.get("unitid").getAsInt();
                                unit2FSRMH = unit2Obj.get("fsrmh").getAsDouble();
                                unit2FSRAI = unit2Obj.get("fsrai").getAsDouble();
                                unit2CSRMH = unit2Obj.get("csrmh").getAsDouble();
                                unit2CSRAI = unit2Obj.get("csrai").getAsDouble();
                                unit2Conv = unit2Obj.get("conversion_rate").getAsDouble();
                                String PhyStock = unit2Obj.get("physical_stock").getAsDouble() > 0 ? unit2Obj.get("physical_stock").getAsString() : "";
                                String LogStock = unit2Obj.get("logical_stock").getAsDouble() > 0 ? "(" + unit2Obj.get("logical_stock").getAsString() + ")" : "";
                                unit2ClsStock = PhyStock + LogStock;
                                unit2ActualStock = unit2Obj.get("actual_stock").getAsDouble();
                            }
                            String unit3Name = "";
                            Integer unit3Id = 0;
                            Double unit3FSRMH = 0.0;
                            Double unit3FSRAI = 0.0;
                            Double unit3CSRMH = 0.0;
                            Double unit3CSRAI = 0.0;
                            Double unit3Conv = 0.0;
                            String unit3ClsStock = "";
                            Double unit3ActualStock = 0.0;
                            if (jarr.size() >= 3) {
                                JsonObject unit3Obj = jarr.get(2).getAsJsonObject();
                                unit3Name = unit3Obj.get("unitName").getAsString();
                                unit3Id = unit3Obj.get("unitid").getAsInt();
                                unit3FSRMH = unit3Obj.get("fsrmh").getAsDouble();
                                unit3FSRAI = unit3Obj.get("fsrai").getAsDouble();
                                unit3CSRMH = unit3Obj.get("csrmh").getAsDouble();
                                unit3CSRAI = unit3Obj.get("csrai").getAsDouble();
                                unit3Conv = unit3Obj.get("conversion_rate").getAsDouble();
                                String PhyStock = unit3Obj.get("physical_stock").getAsDouble() > 0 ? unit3Obj.get("physical_stock").getAsString() : "";
                                String LogStock = unit3Obj.get("logical_stock").getAsDouble() > 0 ? "(" + unit3Obj.get("logical_stock").getAsString() + ")" : "";
                                unit3ClsStock = PhyStock + LogStock;
                                unit3ActualStock = unit3Obj.get("actual_stock").getAsDouble();
                            }

                            lstBatch.add(new TranxSelectedBatch(eleObj.get("batchId").getAsInt(), eleObj.get("is_expired").getAsBoolean(), DateConvertUtil.convertDispDateFormat(eleObj.get("purchase_date").getAsString()), DateConvertUtil.convertDispDateFormat(eleObj.get("manufacture_date").getAsString()), DateConvertUtil.convertDispDateFormat(eleObj.get("expiry_date").getAsString()), eleObj.get("mrp").getAsDouble(), eleObj.get("purchase_rate").getAsDouble(), eleObj.get("product_name").getAsString(), eleObj.get("batch").getAsString(), unit1Name, unit1Id, unit1FSRMH, unit1FSRAI, unit1CSRMH, unit1CSRAI, unit1Conv, unit1ClsStock, unit1ActualStock, unit2Name, unit2Id, unit2FSRMH, unit2FSRAI, unit2CSRMH, unit2CSRAI, unit2Conv, unit2ClsStock, unit2ActualStock, unit3Name, unit3Id, unit3FSRMH, unit3FSRAI, unit3CSRMH, unit3CSRAI, unit3Conv, unit3ClsStock, unit3ActualStock));
                        } catch (Exception e) {
                            e.printStackTrace();
//                            System.out.println("exception +e" + e.getMessage());
                        }
                    }
                } else {
                    System.out.println("responseObject is null");
                }
            } else {
                System.out.println("Error in response");
            }
        } catch (Exception e) {
            e.printStackTrace();
            loggerTranxCommonPopup.error("Exception in getAllTranxProductBatchList(): ", Globals.getExceptionString(e));
        }
        return lstBatch;
    }

    private static ObservableList<TranxSelectedBatch> getAllTranxProductBatchListSales(String productId, String tranxDate) {
//        System.out.println("getAllTranxProductBatchList" + selectedRow + "tranxDate" + tranxDate);
        try {
            Map<String, String> body = new HashMap<>();
            body.put("productId", productId);
            /*body.put("product_id", String.valueOf(selectedRow.getProductId()));
            body.put("level_a_id", selectedRow.getLevelAId() > 0 ? String.valueOf(selectedRow.getLevelAId()) : "");
            body.put("level_b_id", selectedRow.getLevelBId() > 0 ? String.valueOf(selectedRow.getLevelBId()) : "");
            body.put("level_c_id", selectedRow.getLevelCId() > 0 ? String.valueOf(selectedRow.getLevelCId()) : "");
            body.put("unit_id", String.valueOf(selectedRow.getUnitId()));*/
            body.put("invoice_date", DateConvertUtil.convertStringDatetoAPIDateString(tranxDate));
            String formData = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.GET_TRANX_PRODUCT_BATCH_LIST);
            System.out.println("Batch Response org " + response.body());
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseArray = jsonObject.get("resData").getAsJsonArray();
                lstBatch.clear();
                if (responseArray.size() > 0) {
                    for (JsonElement element : responseArray) {
                        try {
                            JsonObject eleObj = new Gson().fromJson(element, JsonObject.class);
                            System.out.println("eleOBj =>" + eleObj);
                            JsonArray jarr = eleObj.get("unit_list").getAsJsonArray();
                            String unit1Name = "";
                            Integer unit1Id = 0;
                            Double unit1FSRMH = 0.0;
                            Double unit1FSRAI = 0.0;
                            Double unit1CSRMH = 0.0;
                            Double unit1CSRAI = 0.0;
                            Double unit1Conv = 0.0;
                            String unit1ClsStock = "";
                            Double unit1ActualStock = 0.0;
                            if (jarr.size() >= 1) {
                                JsonObject unit1Obj = jarr.get(0).getAsJsonObject();
                                unit1Name = unit1Obj.get("unitName").getAsString();
                                unit1Id = unit1Obj.get("unitid").getAsInt();
                                unit1FSRMH = unit1Obj.get("fsrmh").getAsDouble();
                                unit1FSRAI = unit1Obj.get("fsrai").getAsDouble();
                                unit1CSRMH = unit1Obj.get("csrmh").getAsDouble();
                                unit1CSRAI = unit1Obj.get("csrai").getAsDouble();
                                unit1Conv = unit1Obj.get("conversion_rate").getAsDouble();
//                                +""+unit1Obj.get("logical_stock").getAsDouble()>0? unit1Obj.get("logical_stock").getAsDouble():""
                                String PhyStock = unit1Obj.get("physical_stock").getAsDouble() > 0 ? unit1Obj.get("physical_stock").getAsString() : "";
                                String LogStock = unit1Obj.get("logical_stock").getAsDouble() > 0 ? "(" + unit1Obj.get("logical_stock").getAsString() + ")" : "";
                                unit1ClsStock = PhyStock + LogStock;
                                unit1ActualStock = unit1Obj.get("actual_stock").getAsDouble();
                            }
                            String unit2Name = "";
                            Integer unit2Id = 0;
                            Double unit2FSRMH = 0.0;
                            Double unit2FSRAI = 0.0;
                            Double unit2CSRMH = 0.0;
                            Double unit2CSRAI = 0.0;
                            Double unit2Conv = 0.0;
                            String unit2ClsStock = "";
                            Double unit2ActualStock = 0.0;
                            if (jarr.size() >= 2) {
                                JsonObject unit2Obj = jarr.get(1).getAsJsonObject();
                                unit2Name = unit2Obj.get("unitName").getAsString();
                                unit2Id = unit2Obj.get("unitid").getAsInt();
                                unit2FSRMH = unit2Obj.get("fsrmh").getAsDouble();
                                unit2FSRAI = unit2Obj.get("fsrai").getAsDouble();
                                unit2CSRMH = unit2Obj.get("csrmh").getAsDouble();
                                unit2CSRAI = unit2Obj.get("csrai").getAsDouble();
                                unit2Conv = unit2Obj.get("conversion_rate").getAsDouble();
                                String PhyStock = unit2Obj.get("physical_stock").getAsDouble() > 0 ? unit2Obj.get("physical_stock").getAsString() : "";
                                String LogStock = unit2Obj.get("logical_stock").getAsDouble() > 0 ? "(" + unit2Obj.get("logical_stock").getAsString() + ")" : "";
                                unit2ClsStock = PhyStock + LogStock;
                                unit2ActualStock = unit2Obj.get("actual_stock").getAsDouble();
                            }
                            String unit3Name = "";
                            Integer unit3Id = 0;
                            Double unit3FSRMH = 0.0;
                            Double unit3FSRAI = 0.0;
                            Double unit3CSRMH = 0.0;
                            Double unit3CSRAI = 0.0;
                            Double unit3Conv = 0.0;
                            String unit3ClsStock = "";
                            Double unit3ActualStock = 0.0;
                            if (jarr.size() >= 3) {
                                JsonObject unit3Obj = jarr.get(2).getAsJsonObject();
                                unit3Name = unit3Obj.get("unitName").getAsString();
                                unit3Id = unit3Obj.get("unitid").getAsInt();
                                unit3FSRMH = unit3Obj.get("fsrmh").getAsDouble();
                                unit3FSRAI = unit3Obj.get("fsrai").getAsDouble();
                                unit3CSRMH = unit3Obj.get("csrmh").getAsDouble();
                                unit3CSRAI = unit3Obj.get("csrai").getAsDouble();
                                unit3Conv = unit3Obj.get("conversion_rate").getAsDouble();
                                String PhyStock = unit3Obj.get("physical_stock").getAsDouble() > 0 ? unit3Obj.get("physical_stock").getAsString() : "";
                                String LogStock = unit3Obj.get("logical_stock").getAsDouble() > 0 ? "(" + unit3Obj.get("logical_stock").getAsString() + ")" : "";
                                unit3ClsStock = PhyStock + LogStock;
                                unit3ActualStock = unit3Obj.get("actual_stock").getAsDouble();
                            }

                            lstBatch.add(new TranxSelectedBatch(eleObj.get("batchId").getAsInt(), eleObj.get("is_expired").getAsBoolean(), DateConvertUtil.convertDispDateFormat(eleObj.get("purchase_date").getAsString()), DateConvertUtil.convertDispDateFormat(eleObj.get("manufacture_date").getAsString()), DateConvertUtil.convertDispDateFormat(eleObj.get("expiry_date").getAsString()), eleObj.get("mrp").getAsString().isEmpty() ? 0 : eleObj.get("mrp").getAsDouble(), eleObj.get("purchase_rate").getAsString().isEmpty() ? 0 : eleObj.get("purchase_rate").getAsDouble(), eleObj.get("product_name").getAsString(), eleObj.get("batch").getAsString(), unit1Name, unit1Id, unit1FSRMH, unit1FSRAI, unit1CSRMH, unit1CSRAI, unit1Conv, unit1ClsStock, unit1ActualStock, unit2Name, unit2Id, unit2FSRMH, unit2FSRAI, unit2CSRMH, unit2CSRAI, unit2Conv, unit2ClsStock, unit2ActualStock, unit3Name, unit3Id, unit3FSRMH, unit3FSRAI, unit3CSRMH, unit3CSRAI, unit3Conv, unit3ClsStock, unit3ActualStock));
                        } catch (Exception e) {
                            e.printStackTrace();
//                            System.out.println("exception +e" + e.getMessage());
                        }
                    }
                } else {
                    System.out.println("responseObject is null");
                }
            } else {
                lstBatch.clear();
                System.out.println("Error in response");
            }
        } catch (Exception e) {
            e.printStackTrace();
            loggerTranxCommonPopup.error("Exception in getAllTranxProductBatchList(): ", Globals.getExceptionString(e));
        }
        return lstBatch;
    }

    private static ObservableList<TranxSelectedBatch> getAllTranxProductBatchListSalesChallan(CmpTRowDTOSoToSc selectedRow, String tranxDate) {
//        System.out.println("getAllTranxProductBatchList" + selectedRow + "tranxDate" + tranxDate);
        try {
            Map<String, String> body = new HashMap<>();
            body.put("productId", String.valueOf(selectedRow.getProduct_id()));
            /*body.put("product_id", String.valueOf(selectedRow.getProductId()));
            body.put("level_a_id", selectedRow.getLevelAId() > 0 ? String.valueOf(selectedRow.getLevelAId()) : "");
            body.put("level_b_id", selectedRow.getLevelBId() > 0 ? String.valueOf(selectedRow.getLevelBId()) : "");
            body.put("level_c_id", selectedRow.getLevelCId() > 0 ? String.valueOf(selectedRow.getLevelCId()) : "");
            body.put("unit_id", String.valueOf(selectedRow.getUnitId()));*/
            body.put("invoice_date", DateConvertUtil.convertStringDatetoAPIDateString(tranxDate));
            String formData = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.GET_TRANX_PRODUCT_BATCH_LIST);
//            System.out.println("Batch Response org " + response.body());
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseArray = jsonObject.get("resData").getAsJsonArray();
                lstBatch.clear();
                if (responseArray.size() > 0) {
                    for (JsonElement element : responseArray) {
                        try {
                            JsonObject eleObj = new Gson().fromJson(element, JsonObject.class);
                            System.out.println("eleOBj =>" + eleObj);
                            JsonArray jarr = eleObj.get("unit_list").getAsJsonArray();
                            String unit1Name = "";
                            Integer unit1Id = 0;
                            Double unit1FSRMH = 0.0;
                            Double unit1FSRAI = 0.0;
                            Double unit1CSRMH = 0.0;
                            Double unit1CSRAI = 0.0;
                            Double unit1Conv = 0.0;
                            String unit1ClsStock = "";
                            Double unit1ActualStock = 0.0;
                            if (jarr.size() >= 1) {
                                JsonObject unit1Obj = jarr.get(0).getAsJsonObject();
                                unit1Name = unit1Obj.get("unitName").getAsString();
                                unit1Id = unit1Obj.get("unitid").getAsInt();
                                unit1FSRMH = unit1Obj.get("fsrmh").getAsDouble();
                                unit1FSRAI = unit1Obj.get("fsrai").getAsDouble();
                                unit1CSRMH = unit1Obj.get("csrmh").getAsDouble();
                                unit1CSRAI = unit1Obj.get("csrai").getAsDouble();
                                unit1Conv = unit1Obj.get("conversion_rate").getAsDouble();
//                                +""+unit1Obj.get("logical_stock").getAsDouble()>0? unit1Obj.get("logical_stock").getAsDouble():""
                                String PhyStock = unit1Obj.get("physical_stock").getAsDouble() > 0 ? unit1Obj.get("physical_stock").getAsString() : "";
                                String LogStock = unit1Obj.get("logical_stock").getAsDouble() > 0 ? "(" + unit1Obj.get("logical_stock").getAsString() + ")" : "";
                                unit1ClsStock = PhyStock + LogStock;
                                unit1ActualStock = unit1Obj.get("actual_stock").getAsDouble();
                            }
                            String unit2Name = "";
                            Integer unit2Id = 0;
                            Double unit2FSRMH = 0.0;
                            Double unit2FSRAI = 0.0;
                            Double unit2CSRMH = 0.0;
                            Double unit2CSRAI = 0.0;
                            Double unit2Conv = 0.0;
                            String unit2ClsStock = "";
                            Double unit2ActualStock = 0.0;
                            if (jarr.size() >= 2) {
                                JsonObject unit2Obj = jarr.get(1).getAsJsonObject();
                                unit2Name = unit2Obj.get("unitName").getAsString();
                                unit2Id = unit2Obj.get("unitid").getAsInt();
                                unit2FSRMH = unit2Obj.get("fsrmh").getAsDouble();
                                unit2FSRAI = unit2Obj.get("fsrai").getAsDouble();
                                unit2CSRMH = unit2Obj.get("csrmh").getAsDouble();
                                unit2CSRAI = unit2Obj.get("csrai").getAsDouble();
                                unit2Conv = unit2Obj.get("conversion_rate").getAsDouble();
                                String PhyStock = unit2Obj.get("physical_stock").getAsDouble() > 0 ? unit2Obj.get("physical_stock").getAsString() : "";
                                String LogStock = unit2Obj.get("logical_stock").getAsDouble() > 0 ? "(" + unit2Obj.get("logical_stock").getAsString() + ")" : "";
                                unit2ClsStock = PhyStock + LogStock;
                                unit2ActualStock = unit2Obj.get("actual_stock").getAsDouble();
                            }
                            String unit3Name = "";
                            Integer unit3Id = 0;
                            Double unit3FSRMH = 0.0;
                            Double unit3FSRAI = 0.0;
                            Double unit3CSRMH = 0.0;
                            Double unit3CSRAI = 0.0;
                            Double unit3Conv = 0.0;
                            String unit3ClsStock = "";
                            Double unit3ActualStock = 0.0;
                            if (jarr.size() >= 3) {
                                JsonObject unit3Obj = jarr.get(2).getAsJsonObject();
                                unit3Name = unit3Obj.get("unitName").getAsString();
                                unit3Id = unit3Obj.get("unitid").getAsInt();
                                unit3FSRMH = unit3Obj.get("fsrmh").getAsDouble();
                                unit3FSRAI = unit3Obj.get("fsrai").getAsDouble();
                                unit3CSRMH = unit3Obj.get("csrmh").getAsDouble();
                                unit3CSRAI = unit3Obj.get("csrai").getAsDouble();
                                unit3Conv = unit3Obj.get("conversion_rate").getAsDouble();
                                String PhyStock = unit3Obj.get("physical_stock").getAsDouble() > 0 ? unit3Obj.get("physical_stock").getAsString() : "";
                                String LogStock = unit3Obj.get("logical_stock").getAsDouble() > 0 ? "(" + unit3Obj.get("logical_stock").getAsString() + ")" : "";
                                unit3ClsStock = PhyStock + LogStock;
                                unit3ActualStock = unit3Obj.get("actual_stock").getAsDouble();
                            }

                            lstBatch.add(new TranxSelectedBatch(eleObj.get("batchId").getAsInt(), eleObj.get("is_expired").getAsBoolean(), DateConvertUtil.convertDispDateFormat(eleObj.get("purchase_date").getAsString()), DateConvertUtil.convertDispDateFormat(eleObj.get("manufacture_date").getAsString()), DateConvertUtil.convertDispDateFormat(eleObj.get("expiry_date").getAsString()), eleObj.get("mrp").getAsString().isEmpty() ? 0.0 : eleObj.get("mrp").getAsDouble(), eleObj.get("purchase_rate").getAsString().isEmpty() ? 0.0 : eleObj.get("purchase_rate").getAsDouble(), eleObj.get("product_name").getAsString(), eleObj.get("batch").getAsString(), unit1Name, unit1Id, unit1FSRMH, unit1FSRAI, unit1CSRMH, unit1CSRAI, unit1Conv, unit1ClsStock, unit1ActualStock, unit2Name, unit2Id, unit2FSRMH, unit2FSRAI, unit2CSRMH, unit2CSRAI, unit2Conv, unit2ClsStock, unit2ActualStock, unit3Name, unit3Id, unit3FSRMH, unit3FSRAI, unit3CSRMH, unit3CSRAI, unit3Conv, unit3ClsStock, unit3ActualStock));
                        } catch (Exception e) {
                            e.printStackTrace();
//                            System.out.println("exception +e" + e.getMessage());
                        }
                    }
                } else {
                    System.out.println("responseObject is null");
                }
            } else {
                System.out.println("Error in response");
            }
        } catch (Exception e) {
            e.printStackTrace();
            loggerTranxCommonPopup.error("Exception in getAllTranxProductBatchList(): ", Globals.getExceptionString(e));
        }
        return lstBatch;
    }

    private static ObservableList<TranxSelectedBatch> getAllTranxProductBatchListNw(TranxRowNw selectedRow, String tranxDate) {
        System.out.println("getAllTranxProductBatchList" + selectedRow + "tranxDate" + tranxDate);
        try {
            Map<String, String> body = new HashMap<>();
            body.put("productId", String.valueOf(selectedRow.getProductId()));
            /*body.put("product_id", String.valueOf(selectedRow.getProductId()));
            body.put("level_a_id", selectedRow.getLevelAId() > 0 ? String.valueOf(selectedRow.getLevelAId()) : "");
            body.put("level_b_id", selectedRow.getLevelBId() > 0 ? String.valueOf(selectedRow.getLevelBId()) : "");
            body.put("level_c_id", selectedRow.getLevelCId() > 0 ? String.valueOf(selectedRow.getLevelCId()) : "");
            body.put("unit_id", String.valueOf(selectedRow.getUnitId()));*/
            body.put("invoice_date", DateConvertUtil.convertStringDatetoAPIDateString(tranxDate));
            String formData = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.GET_TRANX_PRODUCT_BATCH_LIST);
//            System.out.println("Batch Response org " + response.body());
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseArray = jsonObject.get("resData").getAsJsonArray();
                lstBatch.clear();
                if (responseArray.size() > 0) {
                    for (JsonElement element : responseArray) {
                        try {
                            JsonObject eleObj = new Gson().fromJson(element, JsonObject.class);
                            System.out.println("eleOBj =>" + eleObj);
                            JsonArray jarr = eleObj.get("unit_list").getAsJsonArray();
                            String unit1Name = "";
                            Integer unit1Id = 0;
                            Double unit1FSRMH = 0.0;
                            Double unit1FSRAI = 0.0;
                            Double unit1CSRMH = 0.0;
                            Double unit1CSRAI = 0.0;
                            Double unit1Conv = 0.0;
                            String unit1ClsStock = "";
                            Double unit1ActualStock = 0.0;
                            if (jarr.size() >= 1) {
                                JsonObject unit1Obj = jarr.get(0).getAsJsonObject();
                                unit1Name = unit1Obj.get("unitName").getAsString();
                                unit1Id = unit1Obj.get("unitid").getAsInt();
                                unit1FSRMH = unit1Obj.get("fsrmh").getAsDouble();
                                unit1FSRAI = unit1Obj.get("fsrai").getAsDouble();
                                unit1CSRMH = unit1Obj.get("csrmh").getAsDouble();
                                unit1CSRAI = unit1Obj.get("csrai").getAsDouble();
                                unit1Conv = unit1Obj.get("conversion_rate").getAsDouble();
//                                +""+unit1Obj.get("logical_stock").getAsDouble()>0? unit1Obj.get("logical_stock").getAsDouble():""
                                String PhyStock = unit1Obj.get("physical_stock").getAsDouble() > 0 ? unit1Obj.get("physical_stock").getAsString() : "";
                                String LogStock = unit1Obj.get("logical_stock").getAsDouble() > 0 ? "(" + unit1Obj.get("logical_stock").getAsString() + ")" : "";
                                unit1ClsStock = PhyStock + LogStock;
                                unit1ActualStock = unit1Obj.get("actual_stock").getAsDouble();
                            }
                            String unit2Name = "";
                            Integer unit2Id = 0;
                            Double unit2FSRMH = 0.0;
                            Double unit2FSRAI = 0.0;
                            Double unit2CSRMH = 0.0;
                            Double unit2CSRAI = 0.0;
                            Double unit2Conv = 0.0;
                            String unit2ClsStock = "";
                            Double unit2ActualStock = 0.0;
                            if (jarr.size() >= 2) {
                                JsonObject unit2Obj = jarr.get(1).getAsJsonObject();
                                unit2Name = unit2Obj.get("unitName").getAsString();
                                unit2Id = unit2Obj.get("unitid").getAsInt();
                                unit2FSRMH = unit2Obj.get("fsrmh").getAsDouble();
                                unit2FSRAI = unit2Obj.get("fsrai").getAsDouble();
                                unit2CSRMH = unit2Obj.get("csrmh").getAsDouble();
                                unit2CSRAI = unit2Obj.get("csrai").getAsDouble();
                                unit2Conv = unit2Obj.get("conversion_rate").getAsDouble();
                                String PhyStock = unit2Obj.get("physical_stock").getAsDouble() > 0 ? unit2Obj.get("physical_stock").getAsString() : "";
                                String LogStock = unit2Obj.get("logical_stock").getAsDouble() > 0 ? "(" + unit2Obj.get("logical_stock").getAsString() + ")" : "";
                                unit2ClsStock = PhyStock + LogStock;
                                unit2ActualStock = unit2Obj.get("actual_stock").getAsDouble();
                            }
                            String unit3Name = "";
                            Integer unit3Id = 0;
                            Double unit3FSRMH = 0.0;
                            Double unit3FSRAI = 0.0;
                            Double unit3CSRMH = 0.0;
                            Double unit3CSRAI = 0.0;
                            Double unit3Conv = 0.0;
                            String unit3ClsStock = "";
                            Double unit3ActualStock = 0.0;
                            if (jarr.size() >= 3) {
                                JsonObject unit3Obj = jarr.get(2).getAsJsonObject();
                                unit3Name = unit3Obj.get("unitName").getAsString();
                                unit3Id = unit3Obj.get("unitid").getAsInt();
                                unit3FSRMH = unit3Obj.get("fsrmh").getAsDouble();
                                unit3FSRAI = unit3Obj.get("fsrai").getAsDouble();
                                unit3CSRMH = unit3Obj.get("csrmh").getAsDouble();
                                unit3CSRAI = unit3Obj.get("csrai").getAsDouble();
                                unit3Conv = unit3Obj.get("conversion_rate").getAsDouble();
                                String PhyStock = unit3Obj.get("physical_stock").getAsDouble() > 0 ? unit3Obj.get("physical_stock").getAsString() : "";
                                String LogStock = unit3Obj.get("logical_stock").getAsDouble() > 0 ? "(" + unit3Obj.get("logical_stock").getAsString() + ")" : "";
                                unit3ClsStock = PhyStock + LogStock;
                                unit3ActualStock = unit3Obj.get("actual_stock").getAsDouble();
                            }

                            lstBatch.add(new TranxSelectedBatch(eleObj.get("batchId").getAsInt(), eleObj.get("is_expired").getAsBoolean(), DateConvertUtil.convertDispDateFormat(eleObj.get("purchase_date").getAsString()), DateConvertUtil.convertDispDateFormat(eleObj.get("manufacture_date").getAsString()), DateConvertUtil.convertDispDateFormat(eleObj.get("expiry_date").getAsString()), eleObj.get("mrp").getAsDouble(), eleObj.get("purchase_rate").getAsDouble(), eleObj.get("product_name").getAsString(), eleObj.get("batch").getAsString(), unit1Name, unit1Id, unit1FSRMH, unit1FSRAI, unit1CSRMH, unit1CSRAI, unit1Conv, unit1ClsStock, unit1ActualStock, unit2Name, unit2Id, unit2FSRMH, unit2FSRAI, unit2CSRMH, unit2CSRAI, unit2Conv, unit2ClsStock, unit2ActualStock, unit3Name, unit3Id, unit3FSRMH, unit3FSRAI, unit3CSRMH, unit3CSRAI, unit3Conv, unit3ClsStock, unit3ActualStock));
                        } catch (Exception e) {
                            e.printStackTrace();
//                            System.out.println("exception +e" + e.getMessage());
                        }
                    }
                } else {
                    System.out.println("responseObject is null");
                }
            } else {
                System.out.println("Error in response");
            }
        } catch (Exception e) {
            e.printStackTrace();
            loggerTranxCommonPopup.error("Exception in getAllTranxProductBatchList(): ", Globals.getExceptionString(e));
        }
        return lstBatch;
    }


    public static TranxSelectedProduct getSelectedProductFromProductId(String productName) {
        lstProducts = getAllTranxProductList(productName, "");
        return lstProducts.size() > 0 ? lstProducts.get(0) : null;
    }

    public static TranxSelectedBatch getSelectedBatchFromProductId(TranxRow selectedRow, String tranxDate, String batchNO) {
        lstBatch = getAllTranxProductBatchList(selectedRow.getProductId(), tranxDate);
        TranxSelectedBatch selectedBatch = lstBatch.stream().filter((v) -> v.getBatchNo().equalsIgnoreCase(batchNO)).findAny().orElse(null);
        return selectedBatch;
    }

    public static TranxSelectedBatch getSelectedBatchFromProductId(int productId, String tranxDate, String batchNO) {
        lstBatch = getAllTranxProductBatchList(productId, tranxDate);
        TranxSelectedBatch selectedBatch = lstBatch.stream().filter((v) -> v.getBatchNo().equalsIgnoreCase(batchNO)).findAny().orElse(null);
        return selectedBatch;
    }

    public static <T> void openGetPendings(Stage stage, String ledgerId, String cndSlug, String title, Consumer<List<Integer>> callback) {
        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();
        ObservableList<TranxConversionTbl> lstTableView;
        switch (cndSlug) {
            case "sales_quotation":
                lstTableView = getPendingSalesQuotations(ledgerId);
                break;
            case "sales_order":
                lstTableView = getPendingSalesOrders(ledgerId);
                break;
            case "sales_challan":
                lstTableView = getPendingSalesChallans(ledgerId);
                break;
            default:
                lstTableView = FXCollections.observableArrayList();
                break;
        }


        primaryStage.initOwner(stage); // Set the owner stage
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");
        Platform.runLater(() -> borderPane.requestFocus());
        //BorderPan under Top Layout....................................................................................................................
        VBox vbox_top = new VBox();
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(978);
        hbox_top.setMaxWidth(978);
        hbox_top.setPrefWidth(978);
        hbox_top.setMaxHeight(50);
        hbox_top.setMinHeight(50);
        hbox_top.setPrefHeight(50);

        Label popup_title = new Label(title);
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        HBox.setMargin(imageView, new Insets(0, 10, 0, 10));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        // Set the font size
        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        Region spacer = new Region();
//        Button addButton = new Button("+ Add Product");
//        addButton.setMinWidth(130);
//        addButton.setMaxWidth(130);
//        addButton.setMinHeight(30);
//        addButton.setMaxHeight(30);
//        addButton.setId("submit-btn");
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
//        hbox_top.getChildren().add(addButton);
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #D9F0FB; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 2 0;");
        //BorderPane Under Center Layout.....................................................................................................
        HBox hbox_top1 = new HBox();
        hbox_top1.setMinWidth(978);
        hbox_top1.setMaxWidth(978);
        hbox_top1.setPrefWidth(978);
        hbox_top1.setMaxHeight(50);
        hbox_top1.setMinHeight(50);
        hbox_top1.setMaxHeight(50);
        hbox_top1.setAlignment(Pos.TOP_LEFT);
        hbox_top1.setStyle("-fx-background-color: white;");
        TextField search = new TextField("");
        search.setPromptText("Search");
        search.setPadding(new Insets(10, 0, 0, 10));
        search.setPrefWidth(350);
        HBox.setMargin(search, new Insets(0, 0, 0, 10));
        hbox_top1.getChildren().addAll(search);
        vbox_top.setSpacing(10);
        vbox_top.getChildren().addAll(hbox_top, hbox_top1);
        List<Integer> selectedIds = new ArrayList<>();

        //BorderPane Under Bottom Layout..............................................................................................................
        VBox vBox = new VBox();
        TableView<TranxConversionTbl> tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tableView.setPrefHeight(500);
        tableView.setMaxHeight(350);
        tableView.setMinHeight(350);

        TableColumn<TranxConversionTbl, Boolean> tcIdChk = new TableColumn<>("Id");
        TableColumn<TranxConversionTbl, String> tcNo = new TableColumn<>("No");
        TableColumn<TranxConversionTbl, String> tcDate = new TableColumn<>("Date");
        TableColumn<TranxConversionTbl, String> tcLedgerName = new TableColumn<>("Ledger Name");
        TableColumn<TranxConversionTbl, String> tcNarration = new TableColumn<>("Narration");
        TableColumn<TranxConversionTbl, Double> tcTaxableAmt = new TableColumn<>("Taxable Amt");
        TableColumn<TranxConversionTbl, Double> tcTaxAmt = new TableColumn<>("Tax Amt");
        TableColumn<TranxConversionTbl, Double> tcBillAmt = new TableColumn<>("Bill Amt");
        // Adjusting the width for the Columns
        tcIdChk.setPrefWidth(10);
        tcNo.setPrefWidth(30);
        tcDate.setPrefWidth(30);
        tcLedgerName.setPrefWidth(40);
        tcNarration.setPrefWidth(30);
        tcTaxableAmt.setPrefWidth(30);
        tcTaxAmt.setPrefWidth(30);
        tcBillAmt.setPrefWidth(30);
        tableView.getColumns().addAll(tcIdChk, tcNo, tcDate, tcLedgerName, tcNarration, tcTaxableAmt, tcTaxAmt, tcBillAmt);

//        tcIdChk.setCellValueFactory(new PropertyValueFactory<>("id"));
//        tcIdChk.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isSelected()));
        tcIdChk.setCellValueFactory(cell -> new SimpleBooleanProperty(cell.getValue().isSelected()));
//        tcIdChk.setCellFactory(tc -> new CheckBoxTableCell<>());
//        TableColumn<MyItem, Boolean> checkboxColumn = new TableColumn<>("Select");
//        tcIdChk.setCellValueFactory(cellData -> cellData.getValue().isSelected());
//        tcIdChk.setCellFactory(CheckBoxTableCell.forTableColumn(tcIdChk));
        // Set the graphic for the checkbox
        tcIdChk.setCellFactory(column -> {
            return new TableCell<TranxConversionTbl, Boolean>() {
                private final CheckBox checkBox = new CheckBox();

                {
                    checkBox.setOnAction((e) -> {
                        Integer selId = lstTableView.get(getIndex()).getId();
                        if (checkBox.isSelected()) {
                            if (!selectedIds.contains(selId)) {
                                selectedIds.add(selId);
                            }
                        } else {
                            if (selectedIds.contains(selId)) {
                                selectedIds.remove(selId);
                            }
                        }
                    });

                }

                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        checkBox.setSelected(item);
                        setGraphic(checkBox);
                    }
                }
            };
        });
        tcNo.setCellValueFactory(new PropertyValueFactory<>("billNo"));
        tcDate.setCellValueFactory(new PropertyValueFactory<>("billDate"));
        tcLedgerName.setCellValueFactory(new PropertyValueFactory<>("ledgerName"));
        tcNarration.setCellValueFactory(new PropertyValueFactory<>("narration"));
        tcTaxableAmt.setCellValueFactory(new PropertyValueFactory<>("taxableAmt"));
        tcTaxAmt.setCellValueFactory(new PropertyValueFactory<>("taxAmt"));
        tcBillAmt.setCellValueFactory(new PropertyValueFactory<>("totalAmt"));
        vBox.getChildren().addAll(tableView);
        tableView.setItems(lstTableView);
        borderPane.setTop(vbox_top);
        borderPane.setCenter(vBox);
        HBox hbBottom = new HBox();
        hbBottom.setAlignment(Pos.BOTTOM_RIGHT);
        hbBottom.setSpacing(20.0);
        hbBottom.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #D9F0FB; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 2 0;");
        hbBottom.setPadding(new Insets(10));

        Button btnSubmit = new Button("Submit");
        Button btnCancel = new Button("Cancel");
        btnCancel.setId("cancel-btn");
//        btnCancel.setPadding(new Insets(20));
        btnSubmit.setOnAction((e) -> {
            callback.accept(selectedIds);
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        btnSubmit.setId("submit-btn");

        btnCancel.setOnAction((e) -> {
//            System.out.println("btnCancel clicked" + selectedIds);
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        hbBottom.getChildren().addAll(btnSubmit, btnCancel);
        borderPane.setBottom(hbBottom);
//        hbBottom.getChildren().add()
        //? Double click on ledger list
//        tableView.setRowFactory(tv -> {
//            TableRow<TranxConversionTbl> row = new TableRow<>();
//            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent mouseEvent) {
//                    if (mouseEvent.getClickCount() == 2) {
//                        TranxConversionTbl selectedConversionRow = tableView.getSelectionModel().getSelectedItem();
//                        callback.accept(selectedConversionRow);
//                        primaryStage.close();
//                        OverlaysEffect.removeOverlaysEffect(stage);
//                    }
//                }
//            });
//            return row;
//        });
        Scene scene = new Scene(borderPane, 980, 540);

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

        primaryStage.centerOnScreen();

        primaryStage.show();

    }

    private static ObservableList<TranxConversionTbl> getPendingSalesQuotations(String ledgerId) {
        ObservableList<TranxConversionTbl> rtnLst = FXCollections.observableArrayList();
        Map<String, String> params = new HashMap<>();
        params.put("supplier_code_id", ledgerId);
        String req = Globals.mapToStringforFormData(params);
        APIClient apiClient = new APIClient(EndPoints.TRANX_SALES_QUOTATION_PENDING_LIST, req, RequestType.FORM_DATA);
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                if (workerStateEvent.getSource().getValue() != null) {
                    JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray dataArr = jsonObject.get("data").getAsJsonArray();
                        for (Object object : dataArr) {
                            TranxPedingsRes tranxPedingsRes = new Gson().fromJson(object.toString(), TranxPedingsRes.class);
                            rtnLst.add(new TranxConversionTbl(false, tranxPedingsRes.getId(), tranxPedingsRes.getBillNo(), DateConvertUtil.convertDispDateFormat(tranxPedingsRes.getBillDate()), tranxPedingsRes.getSundryDebtorsName(), tranxPedingsRes.getNarration(), tranxPedingsRes.getTaxableAmt(), tranxPedingsRes.getTaxAmt(), tranxPedingsRes.getTotalAmount()));
                        }
                    }
                } else {
                    System.out.println("setONSucceded handle else");
                }

            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                Utils.showAlert("Failed to create User Role, Try Later!");
                System.out.println("Failed to get list");
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                Utils.showAlert("Failed to create User Role, Try Later!");
                System.out.println("Failed to get list");
            }
        });
        apiClient.start();


        return rtnLst;

    }

    private static ObservableList<TranxConversionTbl> getPendingSalesOrders(String ledgerId) {
        ObservableList<TranxConversionTbl> rtnLst = FXCollections.observableArrayList();
        Map<String, String> params = new HashMap<>();
        params.put("supplier_code_id", ledgerId);
        String req = Globals.mapToStringforFormData(params);
        APIClient apiClient = new APIClient(EndPoints.TRANX_SALES_ORDER_PENDING_LIST, req, RequestType.FORM_DATA);
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                if (workerStateEvent.getSource().getValue() != null) {
                    JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray dataArr = jsonObject.get("data").getAsJsonArray();
                        for (Object object : dataArr) {
                            TranxPedingsRes tranxPedingsRes = new Gson().fromJson(object.toString(), TranxPedingsRes.class);
                            rtnLst.add(new TranxConversionTbl(false, tranxPedingsRes.getId(), tranxPedingsRes.getBillNo(), DateConvertUtil.convertDispDateFormat(tranxPedingsRes.getBillDate()), tranxPedingsRes.getSundryDebtorsName(), tranxPedingsRes.getNarration(), tranxPedingsRes.getTaxableAmt(), tranxPedingsRes.getTaxAmt(), tranxPedingsRes.getTotalAmount()));
                        }
                    }
                } else {
                    System.out.println("setONSucceded handle else");
                }

            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                Utils.showAlert("Failed to create User Role, Try Later!");
                System.out.println("Failed to get list");
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                Utils.showAlert("Failed to create User Role, Try Later!");
                System.out.println("Failed to get list");
            }
        });
        apiClient.start();


        return rtnLst;

    }

    private static ObservableList<TranxConversionTbl> getPendingSalesChallans(String ledgerId) {
        ObservableList<TranxConversionTbl> rtnLst = FXCollections.observableArrayList();
        Map<String, String> params = new HashMap<>();
        params.put("supplier_code_id", ledgerId);
        String req = Globals.mapToStringforFormData(params);
        APIClient apiClient = new APIClient(EndPoints.TRANX_SALES_CHALLAN_PENDING_LIST, req, RequestType.FORM_DATA);
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                if (workerStateEvent.getSource().getValue() != null) {
                    JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray dataArr = jsonObject.get("data").getAsJsonArray();
                        for (Object object : dataArr) {
                            TranxPedingsRes tranxPedingsRes = new Gson().fromJson(object.toString(), TranxPedingsRes.class);
                            rtnLst.add(new TranxConversionTbl(false, tranxPedingsRes.getId(), tranxPedingsRes.getBillNo(), DateConvertUtil.convertDispDateFormat(tranxPedingsRes.getBillDate()), tranxPedingsRes.getSundryDebtorsName(), tranxPedingsRes.getNarration(), tranxPedingsRes.getTaxableAmt(), tranxPedingsRes.getTaxAmt(), tranxPedingsRes.getTotalAmount()));
                        }
                    }
                } else {
                    System.out.println("setONSucceded handle else");
                }

            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                Utils.showAlert("Failed to create User Role, Try Later!");
                System.out.println("Failed to get list");
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                Utils.showAlert("Failed to create User Role, Try Later!");
                System.out.println("Failed to get list");
            }
        });
        apiClient.start();


        return rtnLst;

    }

    public static List<TranxRowConvRes> getPendingSalesChallansWithIds(List<Integer> lst) {
        List<TranxRowConvRes> lstRowRes = new ArrayList<>();
//        ObservableList<TranxRowConvRes> rtnLst = FXCollections.observableArrayList();
        JSONArray inputArr = new JSONArray();
        for (Integer v : lst) {
            JSONObject inputObj = new JSONObject();
            inputObj.put("id", v);
            inputArr.put(inputObj);
        }

        Map<String, String> params = new HashMap<>();
        params.put("sales_challan_ids", inputArr.toString());
//        String req = Globals.mapToStringforFormData(params);
        Map<String, String> headers = new HashMap<>();
        String response = APIClient.postMultipartRequest(params, null, EndPoints.TRANX_SALES_CHALLAN_PENDING_WITH_IDS, headers);
        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
        if (jsonObject.get("responseStatus").getAsInt() == 200) {
            JsonArray dataArr = jsonObject.get("row").getAsJsonArray();
            for (Object object : dataArr) {
                TranxRowConvRes currRowRes = new Gson().fromJson(object.toString(), TranxRowConvRes.class);
                lstRowRes.add(currRowRes);

            }

        }
        /*APIClient apiClient = new APIClient(EndPoints.TRANX_SALES_CHALLAN_PENDING_WITH_IDS, req, RequestType.FORM_DATA);
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                if (workerStateEvent.getSource().getValue() != null) {
                    JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray dataArr = jsonObject.get("row").getAsJsonArray();
                        for (Object object : dataArr) {
                            try {
                                TranxRowConvRes currRowRes = new Gson().fromJson(object.toString(), TranxRowConvRes.class);
                                rtnLst.add(currRowRes);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println("API Call ->" + rtnLst);

                    }
                } else {
                    System.out.println("setONSucceded handle else");
                }

            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                Utils.showAlert("Failed to create User Role, Try Later!");
                System.out.println("Failed to get list");
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                Utils.showAlert("Failed to create User Role, Try Later!");
                System.out.println("Failed to get list");
            }
        });
        apiClient.start();*/
//        System.out.println("API Lst" + rtnLst);

        return lstRowRes;
    }

    public static List<TranxRowConvRes> getPendingSalesOrdersWithIds(List<Integer> lst) {
        List<TranxRowConvRes> lstRowRes = new ArrayList<>();
//        ObservableList<TranxRowConvRes> rtnLst = FXCollections.observableArrayList();
        JSONArray inputArr = new JSONArray();
        for (Integer v : lst) {
            JSONObject inputObj = new JSONObject();
            inputObj.put("id", v);
            inputArr.put(inputObj);
        }

        Map<String, String> params = new HashMap<>();
        params.put("sales_order_ids", inputArr.toString());
//        String req = Globals.mapToStringforFormData(params);
        Map<String, String> headers = new HashMap<>();
        String response = APIClient.postMultipartRequest(params, null, EndPoints.TRANX_SALES_ORDER_PENDING_WITH_IDS, headers);
        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
        if (jsonObject.get("responseStatus").getAsInt() == 200) {
            JsonArray dataArr = jsonObject.get("row").getAsJsonArray();
            for (Object object : dataArr) {
                TranxRowConvRes currRowRes = new Gson().fromJson(object.toString(), TranxRowConvRes.class);
                lstRowRes.add(currRowRes);
            }

        }
        /*APIClient apiClient = new APIClient(EndPoints.TRANX_SALES_CHALLAN_PENDING_WITH_IDS, req, RequestType.FORM_DATA);
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                if (workerStateEvent.getSource().getValue() != null) {
                    JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray dataArr = jsonObject.get("row").getAsJsonArray();
                        for (Object object : dataArr) {
                            try {
                                TranxRowConvRes currRowRes = new Gson().fromJson(object.toString(), TranxRowConvRes.class);
                                rtnLst.add(currRowRes);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println("API Call ->" + rtnLst);

                    }
                } else {
                    System.out.println("setONSucceded handle else");
                }

            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                Utils.showAlert("Failed to create User Role, Try Later!");
                System.out.println("Failed to get list");
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                Utils.showAlert("Failed to create User Role, Try Later!");
                System.out.println("Failed to get list");
            }
        });
        apiClient.start();*/
//        System.out.println("API Lst" + rtnLst);

        return lstRowRes;
    }

    public static List<TranxRowConvRes> getPendingSalesQuotationWithIds(List<Integer> lst) {
        List<TranxRowConvRes> lstRowRes = new ArrayList<>();
//        ObservableList<TranxRowConvRes> rtnLst = FXCollections.observableArrayList();
        JSONArray inputArr = new JSONArray();
        for (Integer v : lst) {
            JSONObject inputObj = new JSONObject();
            inputObj.put("id", v);
            inputArr.put(inputObj);
        }

        Map<String, String> params = new HashMap<>();
        params.put("sale_quotation_ids", inputArr.toString());
//        String req = Globals.mapToStringforFormData(params);
        Map<String, String> headers = new HashMap<>();
        String response = APIClient.postMultipartRequest(params, null, EndPoints.TRANX_SALES_QUOTATION_PENDING_WITH_IDS, headers);
        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
        if (jsonObject.get("responseStatus").getAsInt() == 200) {
            JsonArray dataArr = jsonObject.get("row").getAsJsonArray();
            for (Object object : dataArr) {
                TranxRowConvRes currRowRes = new Gson().fromJson(object.toString(), TranxRowConvRes.class);
                lstRowRes.add(currRowRes);
            }

        }
        /*APIClient apiClient = new APIClient(EndPoints.TRANX_SALES_CHALLAN_PENDING_WITH_IDS, req, RequestType.FORM_DATA);
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                if (workerStateEvent.getSource().getValue() != null) {
                    JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray dataArr = jsonObject.get("row").getAsJsonArray();
                        for (Object object : dataArr) {
                            try {
                                TranxRowConvRes currRowRes = new Gson().fromJson(object.toString(), TranxRowConvRes.class);
                                rtnLst.add(currRowRes);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println("API Call ->" + rtnLst);

                    }
                } else {
                    System.out.println("setONSucceded handle else");
                }

            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                Utils.showAlert("Failed to create User Role, Try Later!");
                System.out.println("Failed to get list");
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                Utils.showAlert("Failed to create User Role, Try Later!");
                System.out.println("Failed to get list");
            }
        });
        apiClient.start();*/
//        System.out.println("API Lst" + rtnLst);

        return lstRowRes;
    }

    public static <T> void openSalesInvoiceList(Stage stage, String ledgerId, String title, Consumer<TranxSalesInvoiceLedgerWiseTbl> callback) {
        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();
        ObservableList<TranxSalesInvoiceLedgerWiseTbl> lstTableView;
        lstTableView = getPendingSalesInvoices(ledgerId);

        primaryStage.initOwner(stage); // Set the owner stage
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");
        Platform.runLater(() -> borderPane.requestFocus());
        //BorderPan under Top Layout....................................................................................................................
        VBox vbox_top = new VBox();
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(978);
        hbox_top.setMaxWidth(978);
        hbox_top.setPrefWidth(978);
        hbox_top.setMaxHeight(50);
        hbox_top.setMinHeight(50);
        hbox_top.setPrefHeight(50);

        Label popup_title = new Label(title);
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        HBox.setMargin(imageView, new Insets(0, 10, 0, 10));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        // Set the font size
        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        Region spacer = new Region();
//        Button addButton = new Button("+ Add Product");
//        addButton.setMinWidth(130);
//        addButton.setMaxWidth(130);
//        addButton.setMinHeight(30);
//        addButton.setMaxHeight(30);
//        addButton.setId("submit-btn");
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
//        hbox_top.getChildren().add(addButton);
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #D9F0FB; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 2 0;");
        //BorderPane Under Center Layout.....................................................................................................
        HBox hbox_top1 = new HBox();
        hbox_top1.setMinWidth(978);
        hbox_top1.setMaxWidth(978);
        hbox_top1.setPrefWidth(978);
        hbox_top1.setMaxHeight(50);
        hbox_top1.setMinHeight(50);
        hbox_top1.setMaxHeight(50);
        hbox_top1.setAlignment(Pos.TOP_LEFT);
        hbox_top1.setStyle("-fx-background-color: white;");
        TextField search = new TextField("");
        search.setPromptText("Search");
        search.setPadding(new Insets(10, 0, 0, 10));
        search.setPrefWidth(350);
        HBox.setMargin(search, new Insets(0, 0, 0, 10));
        hbox_top1.getChildren().addAll(search);
        vbox_top.setSpacing(10);
        vbox_top.getChildren().addAll(hbox_top, hbox_top1);
        //BorderPane Under Bottom Layout..............................................................................................................
        VBox vBox = new VBox();
        TableView<TranxSalesInvoiceLedgerWiseTbl> tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tableView.setPrefHeight(500);
        tableView.setMaxHeight(350);
        tableView.setMinHeight(350);

        TableColumn<TranxSalesInvoiceLedgerWiseTbl, String> tcNo = new TableColumn<>("Sr.No");
        TableColumn<TranxSalesInvoiceLedgerWiseTbl, String> tcBillNo = new TableColumn<>("Bill No");
        TableColumn<TranxSalesInvoiceLedgerWiseTbl, String> tcBillAmt = new TableColumn<>("Bill Amount");
        TableColumn<TranxSalesInvoiceLedgerWiseTbl, String> tcBillDate = new TableColumn<>("Bill Date");

        // Adjusting the width for the Columns
        tcNo.setPrefWidth(30);
        tcBillNo.setPrefWidth(40);
        tcBillAmt.setPrefWidth(30);
        tcBillDate.setPrefWidth(30);
        tableView.getColumns().addAll(tcNo, tcBillNo, tcBillAmt, tcBillDate);

        tcNo.setCellFactory(new LineNumbersCellFactory());
        tcBillNo.setCellValueFactory(new PropertyValueFactory<>("billNo"));
        tcBillDate.setCellValueFactory(new PropertyValueFactory<>("billDate"));
        tcBillAmt.setCellValueFactory(new PropertyValueFactory<>("billAmount"));

        vBox.getChildren().addAll(tableView);
        tableView.setItems(lstTableView);
        borderPane.setTop(vbox_top);
        borderPane.setCenter(vBox);
        HBox hbBottom = new HBox();
        hbBottom.setAlignment(Pos.BOTTOM_RIGHT);
        hbBottom.setSpacing(20.0);
        hbBottom.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #D9F0FB; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 2 0;");
        hbBottom.setPadding(new Insets(10));

        Button btnSubmit = new Button("Submit");
        Button btnCancel = new Button("Cancel");
        btnCancel.setId("cancel-btn");
        btnSubmit.setOnAction((e) -> {
            TranxSalesInvoiceLedgerWiseTbl selectedItem = tableView.getSelectionModel().getSelectedItem();
            callback.accept(selectedItem);
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        btnSubmit.setId("submit-btn");

        btnCancel.setOnAction((e) -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        hbBottom.getChildren().addAll(btnSubmit, btnCancel);
        borderPane.setBottom(hbBottom);
        Scene scene = new Scene(borderPane, 980, 540);

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

        primaryStage.centerOnScreen();

        primaryStage.show();

    }

    private static ObservableList<TranxSalesInvoiceLedgerWiseTbl> getPendingSalesInvoices(String ledgerId) {
        ObservableList<TranxSalesInvoiceLedgerWiseTbl> lst = FXCollections.observableArrayList();
        if (!ledgerId.isEmpty()) {
            Map<String, String> body = new HashMap<>();
            body.put("sundry_debtors_id", ledgerId);
            String formData = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.TRANX_SALES_INVOICE_RTN_LIST_CLIENT_WISE);
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            if (jsonObject.has("responseStatus") && jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray jarr = jsonObject.get("data").getAsJsonArray();
                for (JsonElement element : jarr) {
                    JSONObject jobj = new JSONObject(element.toString());
                    lst.add(new TranxSalesInvoiceLedgerWiseTbl(jobj.getInt("id"), jobj.getString("invoice_no"), DateConvertUtil.convertDispDateFormat(jobj.getString("invoice_date")), jobj.getDouble("total_amount"), jobj.getString("source")));
                }
            }

        }
        return lst;
    }

    public static <T> void openSalesInvoiceProductList(Stage stage, Integer invoiceId, String title, Consumer<List<TranxRowRes>> callback) {
        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();
        ObservableList<TranxRowRes> lstTableView = getProductsFromSalesInvoices(invoiceId);
        primaryStage.initOwner(stage); // Set the owner stage
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");
        Platform.runLater(() -> borderPane.requestFocus());
        //BorderPan under Top Layout....................................................................................................................
        VBox vbox_top = new VBox();
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(978);
        hbox_top.setMaxWidth(978);
        hbox_top.setPrefWidth(978);
        hbox_top.setMaxHeight(50);
        hbox_top.setMinHeight(50);
        hbox_top.setPrefHeight(50);

        Label popup_title = new Label(title);
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        HBox.setMargin(imageView, new Insets(0, 10, 0, 10));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        // Set the font size
        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        Region spacer = new Region();
//        Button addButton = new Button("+ Add Product");
//        addButton.setMinWidth(130);
//        addButton.setMaxWidth(130);
//        addButton.setMinHeight(30);
//        addButton.setMaxHeight(30);
//        addButton.setId("submit-btn");
        HBox.setHgrow(spacer, Priority.ALWAYS);
        List<TranxRowRes> selectedLst = new ArrayList<>();
        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
//        hbox_top.getChildren().add(addButton);
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #D9F0FB; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 2 0;");
        //BorderPane Under Center Layout.....................................................................................................
        HBox hbox_top1 = new HBox();
        hbox_top1.setMinWidth(978);
        hbox_top1.setMaxWidth(978);
        hbox_top1.setPrefWidth(978);
        hbox_top1.setMaxHeight(50);
        hbox_top1.setMinHeight(50);
        hbox_top1.setMaxHeight(50);
        hbox_top1.setAlignment(Pos.TOP_LEFT);
        hbox_top1.setStyle("-fx-background-color: white;");
        TextField search = new TextField("");
        search.setPromptText("Search");
        search.setPadding(new Insets(10, 0, 0, 10));
        search.setPrefWidth(350);
        HBox.setMargin(search, new Insets(0, 0, 0, 10));
        hbox_top1.getChildren().addAll(search);
        vbox_top.setSpacing(10);
        vbox_top.getChildren().addAll(hbox_top, hbox_top1);
        //BorderPane Under Bottom Layout..............................................................................................................
        VBox vBox = new VBox();
        TableView<TranxRowRes> tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tableView.setPrefHeight(500);
        tableView.setMaxHeight(350);
        tableView.setMinHeight(350);
        TableColumn<TranxRowRes, Boolean> tcIdChk = new TableColumn<>("Id");
        TableColumn<TranxRowRes, String> tcNo = new TableColumn<>("Sr.No");
        TableColumn<TranxRowRes, String> tcProductName = new TableColumn<>("Product Name");
        TableColumn<TranxRowRes, String> tcPackageName = new TableColumn<>("Package Name");
        TableColumn<TranxRowRes, String> tcUnitName = new TableColumn<>("Unit Name");
        TableColumn<TranxRowRes, String> tcBatch = new TableColumn<>("Batch");
        TableColumn<TranxRowRes, String> tcExpDate = new TableColumn<>("Exp. Date");
        TableColumn<TranxRowRes, String> tcRetQty = new TableColumn<>("Ret Qty");
        TableColumn<TranxRowRes, String> tcQty = new TableColumn<>("Qty");
        TableColumn<TranxRowRes, String> tcFreeQty = new TableColumn<>("Free Qty");
        TableColumn<TranxRowRes, String> tcRate = new TableColumn<>("Rate");

        // Adjusting the width for the Columns
        tcIdChk.setPrefWidth(10);
        tcNo.setPrefWidth(30);
        tcProductName.setPrefWidth(40);
        tcPackageName.setPrefWidth(30);
        tcUnitName.setPrefWidth(30);
        tcBatch.setPrefWidth(30);
        tcExpDate.setPrefWidth(30);
        tcRetQty.setPrefWidth(30);
        tcQty.setPrefWidth(30);
        tcFreeQty.setPrefWidth(30);
        tcRate.setPrefWidth(30);
        tableView.getColumns().addAll(tcIdChk, tcNo, tcProductName, tcPackageName, tcUnitName, tcBatch, tcExpDate, tcRetQty, tcQty, tcFreeQty, tcRate);
        tcIdChk.setCellValueFactory(cell -> new SimpleBooleanProperty(cell.getValue().isSelected()));
//        tcIdChk.setCellFactory(tc -> new CheckBoxTableCell<>());
//        TableColumn<MyItem, Boolean> checkboxColumn = new TableColumn<>("Select");
//        tcIdChk.setCellValueFactory(cellData -> cellData.getValue().isSelected());
//        tcIdChk.setCellFactory(CheckBoxTableCell.forTableColumn(tcIdChk));
        // Set the graphic for the checkbox
        tcIdChk.setCellFactory(column -> {
            return new TableCell<TranxRowRes, Boolean>() {
                private final CheckBox checkBox = new CheckBox();

                {
                    checkBox.setOnAction((e) -> {
                        Integer selId = lstTableView.get(getIndex()).getDetailsId();
                        TranxRowRes selRow = selectedLst.stream().filter(s -> s.getDetailsId() == selId).findAny().orElse(null);
                        if (checkBox.isSelected()) {
                            if (selRow == null) {
                                selectedLst.add(lstTableView.get(getIndex()));
                            }
                        } else {
                            if (selRow != null) {
                                selectedLst.remove(lstTableView.get(getIndex()));
                            }
                        }
                    });

                }

                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        checkBox.setSelected(item);
                        setGraphic(checkBox);
                    }
                }
            };
        });
        tcNo.setCellFactory(new LineNumbersCellFactory());
        tcProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        tcPackageName.setCellValueFactory(new PropertyValueFactory<>("packName"));
        tcUnitName.setCellValueFactory(new PropertyValueFactory<>("unitName"));
        tcBatch.setCellValueFactory(new PropertyValueFactory<>("batchNo"));
//        tcExpDate.
        tcRetQty.setCellValueFactory(new PropertyValueFactory<>("returnableQty"));
        tcQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        tcFreeQty.setCellValueFactory(new PropertyValueFactory<>("freeQty"));
        tcRate.setCellValueFactory(new PropertyValueFactory<>("Rate"));
        vBox.getChildren().addAll(tableView);
        tableView.setItems(lstTableView);
        borderPane.setTop(vbox_top);
        borderPane.setCenter(vBox);
        HBox hbBottom = new HBox();
        hbBottom.setAlignment(Pos.BOTTOM_RIGHT);
        hbBottom.setSpacing(20.0);
        hbBottom.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #D9F0FB; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 2 0;");
        hbBottom.setPadding(new Insets(10));

        Button btnSubmit = new Button("Submit");
        Button btnCancel = new Button("Cancel");
        btnCancel.setId("cancel-btn");
        btnSubmit.setOnAction((e) -> {
            callback.accept(selectedLst);
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        btnSubmit.setId("submit-btn");

        btnCancel.setOnAction((e) -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        hbBottom.getChildren().addAll(btnSubmit, btnCancel);
        borderPane.setBottom(hbBottom);
        Scene scene = new Scene(borderPane, 980, 540);

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

        primaryStage.centerOnScreen();

        primaryStage.show();

    }

    private static ObservableList<TranxRowRes> getProductsFromSalesInvoices(Integer invoiceId) {
        ObservableList<TranxRowRes> lst = FXCollections.observableArrayList();
        Map<String, String> body = new HashMap<>();
        body.put("id", invoiceId.toString());
        Map<String, String> headers = new HashMap<>();
//        headers.put("branch", "gvhm001");
        String response = APIClient.postMultipartRequest(body, null, EndPoints.TRANX_SALES_INVOICE_EDIT_BY_ID, headers);
        JSONObject resObj = new JSONObject(response);
        System.out.println("resObj" + resObj);
        if (resObj.getInt("responseStatus") == 200) {
            JSONArray row = resObj.getJSONArray("row");
            for (Object rowObj : row) {
                TranxRowRes currRowRes = new Gson().fromJson(rowObj.toString(), TranxRowRes.class);
                currRowRes.setSelected(false);
                lst.add(currRowRes);
            }
        }

        return lst;
    }

    public static <T> void openBatchCSPopUpSales(Stage stage, CounterSaleRowDTO selectedRow, String TranxDate, String title, Consumer<TranxSelectedBatch> callback) {
//        System.out.println("openBatchPopUp selectedRow" + selectedRow + TranxDate);
        getAllTranxProductBatchListCounterSales(selectedRow, TranxDate);
        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();
        ObservableList<TranxProductWindowDTO> observableLedgerList = FXCollections.observableArrayList();
        primaryStage.initOwner(stage); // Set the owner stage
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");
        Platform.runLater(() -> borderPane.requestFocus());
        //BorderPan under Top Layout.   ...................................................................................................................
        VBox vbox_top = new VBox();
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(calculatePopUpWidth());
        hbox_top.setMaxWidth(calculatePopUpWidth());
        hbox_top.setPrefWidth(calculatePopUpWidth());
        hbox_top.setMaxHeight(50);
        hbox_top.setMinHeight(50);
        hbox_top.setPrefHeight(50);

        Label popup_title = new Label(title);
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        HBox.setMargin(imageView, new Insets(0, 10, 0, 10));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        // Set the font size
        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        Region spacer = new Region();
//        Button addButton = new Button("+ Add Product");
//        addButton.setMinWidth(130);
//        addButton.setMaxWidth(130);
//        addButton.setMinHeight(30);
//        addButton.setMaxHeight(30);
//        addButton.setId("submit-btn");
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
//        hbox_top.getChildren().add(addButton);
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #D9F0FB; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 2 0;");
        //BorderPane Under Center Layout.....................................................................................................
        HBox hbox_top1 = new HBox();
        hbox_top1.setMinWidth(calculatePopUpWidth());
        hbox_top1.setMaxWidth(calculatePopUpWidth());
        hbox_top1.setPrefWidth(calculatePopUpWidth());
        hbox_top1.setMaxHeight(50);
        hbox_top1.setMinHeight(50);
        hbox_top1.setMaxHeight(50);
        hbox_top1.setAlignment(Pos.TOP_LEFT);
        hbox_top1.setStyle("-fx-background-color: white;");
        TextField search = new TextField("");
        search.setPromptText("Search");
        search.setPadding(new Insets(10, 0, 0, 10));
        search.setPrefWidth(350);
        HBox.setMargin(search, new Insets(0, 0, 0, 10));
        hbox_top1.getChildren().addAll(search);
        vbox_top.setSpacing(10);
        vbox_top.getChildren().addAll(hbox_top, hbox_top1);
        Object[] selectedItem = new Object[8];
        Integer initialIndex = -1;

        //BorderPane Under Bottom Layout..............................................................................................................
        VBox vBox = new VBox();
        TableView<TranxSelectedBatch> tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
//        tableView.setPrefHeight(500);
//        tableView.setMaxHeight(500);
//        tableView.setMinHeight(500);
        tableView.setMinHeight(GlobalTranx.TranxCalculatePer(calculatePopUpHeight(), 50));
        TableColumn<TranxSelectedBatch, String> tcbatchNo = new TableColumn<>("BatchNo");
        TableColumn<TranxSelectedBatch, String> tcPurDate = new TableColumn<>("Purchase Date");
        TableColumn<TranxSelectedBatch, String> tcMFGDate = new TableColumn<>("MFG Date");
        TableColumn<TranxSelectedBatch, String> tcExpiryDate = new TableColumn<>("Expiry Date");
        TableColumn<TranxSelectedBatch, String> tcMRP = new TableColumn<>("MRP");
        TableColumn<TranxSelectedBatch, String> tcPurRate = new TableColumn<>("Purchase Rate");
        TableColumn<TranxSelectedBatch, Double> tcUnit1Stock = new TableColumn<>("Unit1-Stock");
        TableColumn<TranxSelectedBatch, String> tcUnit1 = new TableColumn<>("Unit1");
        TableColumn<TranxSelectedBatch, Double> tcUnit2Stock = new TableColumn<>("Unit2-Stock");
        TableColumn<TranxSelectedBatch, String> tcUnit2 = new TableColumn<>("Unit2");
        TableColumn<TranxSelectedBatch, Double> tcUnit3Stock = new TableColumn<>("Unit3-Stock");
        TableColumn<TranxSelectedBatch, Double> tcUnit3 = new TableColumn<>("Unit3");
        // Adjusting the width for the Columns
        /*tcbatchNo.setPrefWidth(40);
        tcPurDate.setPrefWidth(60);
        tcMFGDate.setPrefWidth(60);
        tcExpiryDate.setPrefWidth(60);
        tcMRP.setPrefWidth(30);
        tcPurRate.setPrefWidth(30);
        tcStock.setPrefWidth(40);
        tcMarginPer.setPrefWidth(30);
        tcRateA.setPrefWidth(30);
        tcRateB.setPrefWidth(30);
        tcRateC.setPrefWidth(30);
        tcAct.setPrefWidth(30);*/
        tableView.getColumns().addAll(tcbatchNo, tcPurDate, tcMFGDate, tcExpiryDate, tcMRP, tcPurRate, tcUnit1Stock, tcUnit1, tcUnit2Stock, tcUnit2, tcUnit3Stock, tcUnit3);
        tcbatchNo.setCellValueFactory(new PropertyValueFactory<>("batchNo"));
        tcPurDate.setCellValueFactory(new PropertyValueFactory<>("purDate"));
        tcMFGDate.setCellValueFactory(new PropertyValueFactory<>("manufactureDate"));
        tcExpiryDate.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        tcMRP.setCellValueFactory(new PropertyValueFactory<>("mrp"));
        tcPurRate.setCellValueFactory(new PropertyValueFactory<>("purchaseRate"));
        tcUnit1Stock.setCellValueFactory(new PropertyValueFactory<>("unit1ClsStock"));
        tcUnit1.setCellValueFactory(new PropertyValueFactory<>("unit1Name"));
        tcUnit2Stock.setCellValueFactory(new PropertyValueFactory<>("unit2ClsStock"));
        tcUnit2.setCellValueFactory(new PropertyValueFactory<>("unit2Name"));
        tcUnit3Stock.setCellValueFactory(new PropertyValueFactory<>("unit3ClsStock"));
        tcUnit3.setCellValueFactory(new PropertyValueFactory<>("unit3Name"));
        tableView.setItems(lstBatch);
        vBox.getChildren().addAll(tableView);
        tableView.setItems(lstBatch);
        if (!selectedRow.getB_no().trim().isEmpty()) {
            try {
                OptionalInt rowIndex = IntStream.range(0, lstBatch.size()).filter(i -> lstBatch.get(i).getBatchNo().equalsIgnoreCase(selectedRow.getB_no())).findFirst();
                if (rowIndex.getAsInt() > -1) {
                    tableView.getSelectionModel().select(rowIndex.getAsInt());
                    GlobalTranx.requestFocusOrDieTrying(tableView, 3);
                }
            } catch (Exception e) {
                loggerTranxCommonPopup.error("OptionalInt " + Globals.getExceptionString(e));
            }

        } else {
            GlobalTranx.requestFocusOrDieTrying(search, 3);
        }


        tableView.setOnKeyReleased((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                TranxSelectedBatch productWindowDTO = tableView.getSelectionModel().getSelectedItem();
                callback.accept(productWindowDTO);
                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
            e.consume();
        });
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            tableView.setItems(lstBatch.filtered(s -> s.getBatchNo().contains(newValue)));
        });
        search.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.DOWN) {
                tableView.getSelectionModel().select(0);
                tableView.requestFocus();
            } else if (e.getCode() == KeyCode.ESCAPE) {
                callback.accept(null);
                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
            e.consume();
        });
        //? Double click on ledger list
        tableView.setRowFactory(tv -> {
            TableRow<TranxSelectedBatch> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        TranxSelectedBatch productWindowDTO = tableView.getSelectionModel().getSelectedItem();
                        callback.accept(productWindowDTO);
                        primaryStage.close();
                        OverlaysEffect.removeOverlaysEffect(stage);
                    }
                }
            });
            return row;
        });

        //? Borderpane add elements
//        BorderPane.setMargin(vbox_top, new Insets(20, 0, 0, 0)); // Set top margin of 20 pixels
        borderPane.setTop(vbox_top);
        borderPane.setCenter(vBox);
        tableView.getFocusModel().focusedCellProperty().addListener((obs, old, nw) -> {
//            System.out.println("tableView : nw" + nw);
            borderPane.setBottom(getBottomUIDesignBatch(tableView));
        });

        Scene scene = new Scene(borderPane, calculatePopUpWidth(), calculatePopUpHeight());

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

//        primaryStage.centerOnScreen();
        primaryStage.setX(calculatePopUpX());
        primaryStage.setY(calculatePopUpY());
        primaryStage.show();

      /*  addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                selectedItem[1] = tfPower.getText();
                insertIntoTable(selectedItem[0].toString(), selectedItem[1].toString(), selectedItem[2].toString()
                        , selectedItem[3].toString());
            }
        });*//*
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
//                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
        });*/

    }

    public static ObservableList<TranxSelectedBatch> getAllTranxProductBatchListCounterSales(CounterSaleRowDTO selectedRow, String tranxDate) {
//        System.out.println("getAllTranxProductBatchList" + selectedRow + "tranxDate" + tranxDate);
        try {
            Map<String, String> body = new HashMap<>();
            System.out.println("product" + selectedRow.getProduct_id());
            body.put("productId", String.valueOf(selectedRow.getProduct_id()));
            /*body.put("product_id", String.valueOf(selectedRow.getProductId()));
            body.put("level_a_id", selectedRow.getLevelAId() > 0 ? String.valueOf(selectedRow.getLevelAId()) : "");
            body.put("level_b_id", selectedRow.getLevelBId() > 0 ? String.valueOf(selectedRow.getLevelBId()) : "");
            body.put("level_c_id", selectedRow.getLevelCId() > 0 ? String.valueOf(selectedRow.getLevelCId()) : "");
            body.put("unit_id", String.valueOf(selectedRow.getUnitId()));*/
            body.put("invoice_date", DateConvertUtil.convertStringDatetoAPIDateString(tranxDate));
            String formData = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.GET_TRANX_PRODUCT_BATCH_LIST);
//            System.out.println("Batch Response org " + response.body());
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseArray = jsonObject.get("resData").getAsJsonArray();
                lstBatch.clear();
                if (responseArray.size() > 0) {
                    for (JsonElement element : responseArray) {
                        try {
                            JsonObject eleObj = new Gson().fromJson(element, JsonObject.class);
                            System.out.println("eleOBj =>" + eleObj);
                            JsonArray jarr = eleObj.get("unit_list").getAsJsonArray();
                            String unit1Name = "";
                            Integer unit1Id = 0;
                            Double unit1FSRMH = 0.0;
                            Double unit1FSRAI = 0.0;
                            Double unit1CSRMH = 0.0;
                            Double unit1CSRAI = 0.0;
                            Double unit1Conv = 0.0;
                            String unit1ClsStock = "";
                            Double unit1ActualStock = 0.0;
                            if (jarr.size() >= 1) {
                                JsonObject unit1Obj = jarr.get(0).getAsJsonObject();
                                unit1Name = unit1Obj.get("unitName").getAsString();
                                unit1Id = unit1Obj.get("unitid").getAsInt();
                                unit1FSRMH = unit1Obj.get("fsrmh").getAsDouble();
                                unit1FSRAI = unit1Obj.get("fsrai").getAsDouble();
                                unit1CSRMH = unit1Obj.get("csrmh").getAsDouble();
                                unit1CSRAI = unit1Obj.get("csrai").getAsDouble();
                                unit1Conv = unit1Obj.get("conversion_rate").getAsDouble();
//                                +""+unit1Obj.get("logical_stock").getAsDouble()>0? unit1Obj.get("logical_stock").getAsDouble():""
                                String PhyStock = unit1Obj.get("physical_stock").getAsDouble() > 0 ? unit1Obj.get("physical_stock").getAsString() : "";
                                String LogStock = unit1Obj.get("logical_stock").getAsDouble() > 0 ? "(" + unit1Obj.get("logical_stock").getAsString() + ")" : "";
                                unit1ClsStock = PhyStock + LogStock;
                                unit1ActualStock = unit1Obj.get("actual_stock").getAsDouble();
                            }
                            String unit2Name = "";
                            Integer unit2Id = 0;
                            Double unit2FSRMH = 0.0;
                            Double unit2FSRAI = 0.0;
                            Double unit2CSRMH = 0.0;
                            Double unit2CSRAI = 0.0;
                            Double unit2Conv = 0.0;
                            String unit2ClsStock = "";
                            Double unit2ActualStock = 0.0;
                            if (jarr.size() >= 2) {
                                JsonObject unit2Obj = jarr.get(1).getAsJsonObject();
                                unit2Name = unit2Obj.get("unitName").getAsString();
                                unit2Id = unit2Obj.get("unitid").getAsInt();
                                unit2FSRMH = unit2Obj.get("fsrmh").getAsDouble();
                                unit2FSRAI = unit2Obj.get("fsrai").getAsDouble();
                                unit2CSRMH = unit2Obj.get("csrmh").getAsDouble();
                                unit2CSRAI = unit2Obj.get("csrai").getAsDouble();
                                unit2Conv = unit2Obj.get("conversion_rate").getAsDouble();
                                String PhyStock = unit2Obj.get("physical_stock").getAsDouble() > 0 ? unit2Obj.get("physical_stock").getAsString() : "";
                                String LogStock = unit2Obj.get("logical_stock").getAsDouble() > 0 ? "(" + unit2Obj.get("logical_stock").getAsString() + ")" : "";
                                unit2ClsStock = PhyStock + LogStock;
                                unit2ActualStock = unit2Obj.get("actual_stock").getAsDouble();
                            }
                            String unit3Name = "";
                            Integer unit3Id = 0;
                            Double unit3FSRMH = 0.0;
                            Double unit3FSRAI = 0.0;
                            Double unit3CSRMH = 0.0;
                            Double unit3CSRAI = 0.0;
                            Double unit3Conv = 0.0;
                            String unit3ClsStock = "";
                            Double unit3ActualStock = 0.0;
                            if (jarr.size() >= 3) {
                                JsonObject unit3Obj = jarr.get(2).getAsJsonObject();
                                unit3Name = unit3Obj.get("unitName").getAsString();
                                unit3Id = unit3Obj.get("unitid").getAsInt();
                                unit3FSRMH = unit3Obj.get("fsrmh").getAsDouble();
                                unit3FSRAI = unit3Obj.get("fsrai").getAsDouble();
                                unit3CSRMH = unit3Obj.get("csrmh").getAsDouble();
                                unit3CSRAI = unit3Obj.get("csrai").getAsDouble();
                                unit3Conv = unit3Obj.get("conversion_rate").getAsDouble();
                                String PhyStock = unit3Obj.get("physical_stock").getAsDouble() > 0 ? unit3Obj.get("physical_stock").getAsString() : "";
                                String LogStock = unit3Obj.get("logical_stock").getAsDouble() > 0 ? "(" + unit3Obj.get("logical_stock").getAsString() + ")" : "";
                                unit3ClsStock = PhyStock + LogStock;
                                unit3ActualStock = unit3Obj.get("actual_stock").getAsDouble();
                            }

                            lstBatch.add(new TranxSelectedBatch(eleObj.get("batchId").getAsInt(), eleObj.get("is_expired").getAsBoolean(), DateConvertUtil.convertDispDateFormat(eleObj.get("purchase_date").getAsString()), DateConvertUtil.convertDispDateFormat(eleObj.get("manufacture_date").getAsString()), DateConvertUtil.convertDispDateFormat(eleObj.get("expiry_date").getAsString()), eleObj.get("mrp").getAsDouble(), eleObj.get("purchase_rate").getAsDouble(), eleObj.get("product_name").getAsString(), eleObj.get("batch").getAsString(), unit1Name, unit1Id, unit1FSRMH, unit1FSRAI, unit1CSRMH, unit1CSRAI, unit1Conv, unit1ClsStock, unit1ActualStock, unit2Name, unit2Id, unit2FSRMH, unit2FSRAI, unit2CSRMH, unit2CSRAI, unit2Conv, unit2ClsStock, unit2ActualStock, unit3Name, unit3Id, unit3FSRMH, unit3FSRAI, unit3CSRMH, unit3CSRAI, unit3Conv, unit3ClsStock, unit3ActualStock));
                        } catch (Exception e) {
                            e.printStackTrace();
//                            System.out.println("exception +e" + e.getMessage());
                        }
                    }
                } else {
                    System.out.println("responseObject is null");
                }
            } else {
                System.out.println("Error in response");
                lstBatch.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
            loggerTranxCommonPopup.error("Exception in getAllTranxProductBatchList(): ", Globals.getExceptionString(e));
        }
        return lstBatch;
    }


    public static double getScreenWidth() {
        return Screen.getPrimary().getVisualBounds().getWidth();
    }
}
