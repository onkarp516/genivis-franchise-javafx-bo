package com.opethic.genivis.controller.account_entry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.tranx_sales.SalesOrderListController;
import com.opethic.genivis.dto.CreditNoteListDTO;
import com.opethic.genivis.dto.SalesOrderListDTO;
import com.opethic.genivis.dto.reqres.payment.PaymentListDTO;
import com.opethic.genivis.dto.reqres.receipt.ReceiptListDTO;
import com.opethic.genivis.dto.reqres.tranx.sales.invoice.TranxRowRes;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.DateConvertUtil;
import com.opethic.genivis.utils.GlobalController;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.FxmFileConstants.*;
import static com.opethic.genivis.utils.Globals.salesOrderListDTO;

public class ReceiptListController implements Initializable {

    @FXML
    private BorderPane bpReceiptRootPane;
    @FXML
    private TextField tftranxRecLstSearchField;
    @FXML
    private Button btnRecLstCreatebtn;
    @FXML
    private TableView tblvReceiptLst;
    @FXML
    private TableColumn tblcRecLstReceiptNo, tblcRecLstTransDt, tblcRecLstSupplierName, tblcRecLstNarration, tblcRecLstTotalAmt, tblcRecLstAction;

    String message = "";
    private JsonObject jsonObject = null;

    public static final Logger receiptListLogger = LoggerFactory.getLogger(ReceiptListController.class);

    //? Highlight the Record Start
    public static boolean isNewReceiptCreated = false; // Flag for new creation
    public static String editedReceiptId = null; // ID for edited Sales Order
    //? Highlight the Record End
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        Platform.runLater(() -> {
            //Focus on the First Field of the Page
            tftranxRecLstSearchField.requestFocus();
        });

        //? this include all the Shortcut Keys
        initShortcutKeys();

        //Redirect To Create Receipt Create Page
        btnRecLstCreatebtn.setOnAction(event -> GlobalController.getInstance().addTabStatic(RECEIPT_CREATE_SLUG, false));
        // Amount Alignment on Right Side Code
//        tblcRecLstTotalAmt.setStyle("-fx-alignment: CENTER-RIGHT;");
        // receiptListTableResponsive Method
        receiptListResponsive();
        //All Receipts List API Integration
        if (tftranxRecLstSearchField.getText().isEmpty()) {
            getAllReceiptsList("");
        } else {
            String searchKey = tftranxRecLstSearchField.getText();
            getAllReceiptsList(searchKey);
        }

        //? this include all the table view operations - Edit , Conversions , search , etc..
        tableViewOperations();

    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            bpReceiptRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            bpReceiptRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            bpReceiptRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            bpReceiptRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            bpReceiptRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            bpReceiptRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            bpReceiptRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    private void tableViewOperations() {
        // OnDoubleClick In Edit Page of Particular Receipt Screen
        tblvReceiptLst.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                receiptEditPage();
            }
        });
    }

    private void initShortcutKeys() {
        bpReceiptRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("create")) {
                } else {
                    receiptEditPage();
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
            }else if(event.getCode() == KeyCode.N && event.isControlDown()) {
                btnRecLstCreatebtn.fire();
            }else if(event.getCode() == KeyCode.DOWN && tftranxRecLstSearchField.isFocused()) {
                tblvReceiptLst.getSelectionModel().select(0);
                tblvReceiptLst.requestFocus();
            } else if (event.getCode() == KeyCode.E && event.isControlDown()) {
                receiptEditPage();
            }else if (event.getCode() == KeyCode.D && event.isControlDown()) {
                handleCtrlDPressed();
            }
        });
    }



    // paymentListTableResponsive Method
    public void receiptListResponsive() {
//        tblcRecLstReceiptNo.prefWidthProperty().bind(tblvReceiptLst.widthProperty().multiply(0.2));
//        tblcRecLstTransDt.prefWidthProperty().bind(tblvReceiptLst.widthProperty().multiply(0.15));
//        tblcRecLstSupplierName.prefWidthProperty().bind(tblvReceiptLst.widthProperty().multiply(0.225));
//        tblcRecLstNarration.prefWidthProperty().bind(tblvReceiptLst.widthProperty().multiply(0.225));
//        tblcRecLstTotalAmt.prefWidthProperty().bind(tblvReceiptLst.widthProperty().multiply(0.13));
//        tblcRecLstAction.prefWidthProperty().bind(tblvReceiptLst.widthProperty().multiply(0.07));
        //Set the TableView Columns with proper height and Width
        tblvReceiptLst.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // Getting all the Receipt
    private void getAllReceiptsList(String searchKey) {
        APIClient apiClient = null;
        try {
            receiptListLogger.debug("Get getAllReceiptsList Started...");
            tblvReceiptLst.getItems().clear();
            Map<String, String> body = new HashMap<>();
            body.put("pageNo", "1");
            body.put("pageSize", "50");
            body.put("searchText", searchKey);
            body.put("sort", "{ \\\"colId\\\": null, \\\"isAsc\\\": true }");

            String requestBody = Globals.mapToString(body);
            apiClient = new APIClient(EndPoints.getAllReceiptList, requestBody, RequestType.JSON);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    System.out.println("jsonObject" + jsonObject);
                    ObservableList<ReceiptListDTO> observableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        receiptListLogger.debug("Get getAllReceiptsList Success...");
                        JsonObject responseObject = jsonObject.get("responseObject").getAsJsonObject();
                        System.out.println("debitNoteList" + responseObject);
                        JsonArray responseArray = responseObject.get("data").getAsJsonArray();
                        if (responseArray.size() > 0) {
                            for (JsonElement element : responseArray) {
                                JsonObject item = element.getAsJsonObject();
                                String id = item.get("id").getAsString();
                                String receipt_no = item.get("receipt_code").getAsString();
                                String transaction_dt = DateConvertUtil.convertDispDateFormat(item.get("transaction_dt").getAsString());
                                String narration = item.get("narration").getAsString();
                                String ledger_name = item.get("ledger_name").getAsString();
                                String total_amount = item.get("total_amount").getAsString();
                                String paymentSrNo = item.get("receipt_sr_no").getAsString();
                                String autoGenerated = item.get("auto_generated").getAsString();
                                String isFrReceipt = item.get("isFrReceipt").getAsString();
                                observableList.add(new ReceiptListDTO(id, receipt_no, transaction_dt, ledger_name, narration,
                                        total_amount,paymentSrNo,autoGenerated,isFrReceipt));
                            }

                            tblcRecLstReceiptNo.setCellValueFactory(new PropertyValueFactory<>("receiptNo"));
                            tblcRecLstTransDt.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
                            tblcRecLstSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
                            tblcRecLstNarration.setCellValueFactory(new PropertyValueFactory<>("narration"));
                            tblcRecLstTotalAmt.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
//                            tblcRecLstAction.setCellFactory(param -> {
//                                final TableCell<ReceiptListDTO, String> cell = new TableCell<>() {
//                                    private ImageView delImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/del.png"))));
//                                    private ImageView edtImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/edt.png"))));
//
//                                    {
//                                        delImg.setFitHeight(20.0);
//                                        delImg.setFitWidth(20.0);
//                                        edtImg.setFitHeight(20.0);
//                                        edtImg.setFitWidth(20.0);
//                                    }
//
//                                    private final Button delButton = new Button("", delImg);
//                                    private final Button edtButton = new Button("", edtImg);
//
//                                    {
//                                        edtButton.setOnAction(actionEvent -> {
//                                            Integer id = Integer.valueOf(getTableView().getItems().get(getIndex()).getId());
//                                            GlobalController.getInstance().addTabStaticWithParam(RECEIPT_EDIT_SLUG, false, id);
//                                        });
//                                    }
//
//                                    {
//                                        delButton.setOnAction(actionEvent -> {
//                                            ReceiptListDTO receiptListDTO = getTableView().getItems().get(getIndex());
//                                            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you Sure?", input -> {
//                                                if (input == 1) {
//                                                    deleteReceiptEntry(Integer.parseInt(receiptListDTO.getId()));
//                                                }
//                                            });
//                                        });
//                                    }
//
//                                    HBox hbActions = new HBox();
//
//                                    {
//                                        hbActions.getChildren().add(edtButton);
////                                hbActions.getChildren().add(viewButton);
//                                        hbActions.getChildren().add(delButton);
//                                        hbActions.setSpacing(10.0);
//                                    }
//
//                                    // Set the action for the view button
//                                    @Override
//                                    protected void updateItem(String item, boolean empty) {
//                                        super.updateItem(item, empty);
//                                        if (empty) {
//                                            setGraphic(null);
//                                        } else {
//                                            setGraphic(hbActions);
//                                        }
//                                    }
//                                };
//                                return cell;
//                            });
                            tblvReceiptLst.setItems(observableList);
                            //******************************** Highlight on the Created/Edited Record in the List Start ********************************

                            if (ReceiptListController.isNewReceiptCreated) {
                                tblvReceiptLst.getSelectionModel().selectLast();
                                tblvReceiptLst.scrollTo(tblvReceiptLst.getItems().size() - 1);
                                ReceiptListController.isNewReceiptCreated = false; // Reset the flag
                            } else if (ReceiptListController.editedReceiptId != null) {
                                for (ReceiptListDTO receipt : observableList) {
                                    if (receipt.getId().equals(ReceiptListController.editedReceiptId)) {
                                        tblvReceiptLst.getSelectionModel().select(receipt);
                                        tblvReceiptLst.scrollTo(receipt);
                                        ReceiptListController.editedReceiptId = null; // Reset the ID
                                        break;
                                    }
                                }
                            }
                            //******************************** Highlight on the Created/Edited Record in the List End ********************************

                        } else {
                            receiptListLogger.debug("Get getAllReceiptsList responseObject is null...");
                        }
                    }else {
                        receiptListLogger.debug("Get getAllReceiptsList Error in response...");
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    receiptListLogger.error("Network API cancelled in getAllReceiptsList()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    receiptListLogger.error("Network API cancelled in getAllReceiptsList()" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.start();
            receiptListLogger.debug("Get getAllReceiptsList end");

        } catch (Exception e) {
            e.printStackTrace();
            receiptListLogger.error("Exception getAllReceiptsList() : " + Globals.getExceptionString(e));
        }finally {
            apiClient=null;
        }
    }

    // OnCLick of Delete Icon Deleting the Receipt
    private void deleteReceiptEntry(Integer id) {
        System.out.println("qwerty" +id);
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                APIClient apiClient=null;
                try {

                    receiptListLogger.debug("Get deleteReceiptEntry Started...");
                    Map<String, String> map = new HashMap<>();
                    map.put("id", String.valueOf(id));
                    String formData = Globals.mapToStringforFormData(map);
                    apiClient = new APIClient(EndPoints.deleteReceipt, formData, RequestType.FORM_DATA);
                    apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent workerStateEvent) {
                            jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                            System.out.println("jsonObject ::: >>"+jsonObject);
                            message=jsonObject.get("message").getAsString();
//                    AlertUtility.CustomCallback callback = (number) -> {
//                        if (number == 1) {
//                        }
//                    };
//                    Stage stage2 = (Stage) bpReceiptRootPane.getScene().getWindow();
//                    if(jsonObject.get("responseStatus").getAsInt()==200){
//                        AlertUtility.AlertSuccess(stage2,"Success",message,callback);
//                        getAllReceiptsList("");
//                        tftranxRecLstSearchField.requestFocus();
//                    }
//                    else{
//                        AlertUtility.AlertError(stage2,AlertUtility.alertTypeError, message, callback);
//                    }
                            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                                AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                                    getAllReceiptsList("");
                                    tftranxRecLstSearchField.requestFocus();
                                });
                            } else {
                                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                                });
                            }
                        }
                    });
                    apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent workerStateEvent) {
                            receiptListLogger.error("Network API cancelled in deleteReceiptEntry()" + workerStateEvent.getSource().getValue().toString());

                        }
                    });

                    apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent workerStateEvent) {
                            receiptListLogger.error("Network API cancelled in deleteReceiptEntry()" + workerStateEvent.getSource().getValue().toString());

                        }
                    });
                    apiClient.start();
                    receiptListLogger.debug("Get deleteReceiptEntry end");

                } catch (Exception e) {
                    e.printStackTrace();
                    receiptListLogger.error("Exception deleteReceiptEntry() : " + Globals.getExceptionString(e));
                }
                finally {
                    apiClient=null;
                }
            } else {
                receiptListLogger.error("Failed to Delete: deleteReceiptEntry()");
            }
        };
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Delete", callback);
    }


    private void receiptEditPage() {
        ReceiptListDTO selectedItem = (ReceiptListDTO) tblvReceiptLst.getSelectionModel().getSelectedItem();
        if(selectedItem!=null){
            Integer receiptEditId = Integer.valueOf(selectedItem.getId());
            //? Highlight
            ReceiptListController.editedReceiptId = receiptEditId.toString();
            GlobalController.getInstance().addTabStaticWithParam(RECEIPT_EDIT_SLUG,false, receiptEditId);
        }
    }
    private void handleCtrlDPressed() {
        ReceiptListDTO selectedItem = (ReceiptListDTO) tblvReceiptLst.getSelectionModel().getSelectedItem();
        if(selectedItem!=null) {
            Integer receiptEditId = Integer.valueOf(selectedItem.getId());
            deleteReceiptEntry(receiptEditId);
        }
    }
}
