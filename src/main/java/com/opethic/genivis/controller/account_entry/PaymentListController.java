package com.opethic.genivis.controller.account_entry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.dto.CreditNoteListDTO;
import com.opethic.genivis.dto.ProductDTO;
import com.opethic.genivis.dto.account_entry.DebitNoteListDTO;
import com.opethic.genivis.dto.reqres.payment.PaymentListDTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.DateConvertUtil;
import com.opethic.genivis.utils.GlobalController;
import com.opethic.genivis.utils.Globals;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.FxmFileConstants.*;

public class PaymentListController implements Initializable {
    @FXML
    private BorderPane bpPaymentRootPane;
    @FXML
    private TextField tftranxPaymLstSearchField;

    @FXML
    private Button btnPaymLstCreatebtn;

    @FXML
    private TableView<PaymentListDTO> tblvPaymentLst;

    @FXML
    private TableColumn<PaymentListDTO, String> tblcPaymLstPaymentNo;
    @FXML
    private TableColumn<PaymentListDTO, String> tblcPaymLstTransDt;
    @FXML
    private TableColumn<PaymentListDTO, String> tblcPaymLstSupplierName;
    @FXML
    private TableColumn<PaymentListDTO, String> tblcPaymLstNarration;
    @FXML
    private TableColumn<PaymentListDTO, String> tblcPaymLstTotalAmt;
    @FXML
    private TableColumn<PaymentListDTO, String> tblcPaymLstAction;

    String message = "";
    private JsonObject jsonObject = null;

    public static final Logger paymentListLogger = LoggerFactory.getLogger(PaymentCreateController.class);
    Integer previousNoOfRows;
    //? Highlight the Record Start
    public static boolean isNewPaymentCreated = false; // Flag for new creation
    public static String editedPaymentId = null; // ID for edited Sales Order
    //? Highlight the Record End

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();
        Platform.runLater(() -> {
            //Focus on the First Field of the Page
            tftranxPaymLstSearchField.requestFocus();
        });
        tblvPaymentLst.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        System.out.println("hellllo I am In Payment List");
        //Redirect To Create Payment Create Page
        btnPaymLstCreatebtn.setOnAction(event -> GlobalController.getInstance().addTabStatic(TRANX_PAYMENT_CREATE_SLUG, false));

        // Amount Alignment on Right Side Code
        tblcPaymLstTotalAmt.setStyle("-fx-alignment: CENTER-RIGHT;");

        //Cursor Movement on ENTER KEY
        initialEnterMethod();

        // paymentListTableResponsive Method
        paymentListResponsive();

        //All Payments List API Integration
        if (tftranxPaymLstSearchField.getText().isEmpty()) {
            getAllPaymentsList("");
        } else {
            String searchKey = tftranxPaymLstSearchField.getText();
            getAllPaymentsList(searchKey);
        }
        paymentListLogger.debug("Before Double click event to get the id from selected row of Payment invoice" +
                " list table");
        tblvPaymentLst.setRowFactory(tv -> {
            TableRow<PaymentListDTO> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        PaymentListDTO paymentListDTO = row.getItem();
                        paymentInvoiceEdit();
                    }
                }
            });
            return row;
        });

    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            bpPaymentRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            bpPaymentRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            bpPaymentRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            bpPaymentRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            bpPaymentRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            bpPaymentRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            bpPaymentRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    private void initialEnterMethod() {
        bpPaymentRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("create")) {
                } else {
                    paymentInvoiceEdit();
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
            } else if (event.getCode() == KeyCode.N && event.isControlDown()) {
                btnPaymLstCreatebtn.fire();
            } else if (event.getCode() == KeyCode.DOWN && tftranxPaymLstSearchField.isFocused()) {
                tblvPaymentLst.getSelectionModel().select(0);
                tblvPaymentLst.requestFocus();
            } else if (event.getCode() == KeyCode.E && event.isControlDown()) {
                paymentInvoiceEdit();
            } else if (event.getCode() == KeyCode.D && event.isControlDown()) {
                handleCtrlDPressed();
            }
        });
    }

    private void paymentInvoiceEdit() {
        try {
            PaymentListDTO paymentListDTO = tblvPaymentLst.getSelectionModel().getSelectedItem();
            if (paymentListDTO != null) {
                Integer paymentInvoicId = Integer.valueOf(paymentListDTO.getId());
                //? Highlight
                PaymentListController.editedPaymentId = paymentInvoicId.toString();
                GlobalController.getInstance().addTabStaticWithParam(TRANX_PAYMENT_EDIT_SLUG, false, paymentInvoicId);
            }
        } catch (Exception e) {
            paymentListLogger.error("Failed to Load the Data : paymentInvoiceEdit()");
        }

    }

    private void handleCtrlDPressed() {
        PaymentListDTO paymentListDTO = tblvPaymentLst.getSelectionModel().getSelectedItem();
        if (paymentListDTO != null) {
            Integer paymentInvoicId = Integer.valueOf(paymentListDTO.getId());
            deletePaymentEntry(paymentInvoicId);
        }
    }

    // paymentListTableResponsive Method
    public void paymentListResponsive() {
        tblvPaymentLst.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        tblcPaymLstPaymentNo.prefWidthProperty().bind(tblvPaymentLst.widthProperty().multiply(0.2));
//        tblcPaymLstTransDt.prefWidthProperty().bind(tblvPaymentLst.widthProperty().multiply(0.15));
//        tblcPaymLstSupplierName.prefWidthProperty().bind(tblvPaymentLst.widthProperty().multiply(0.225));
//        tblcPaymLstNarration.prefWidthProperty().bind(tblvPaymentLst.widthProperty().multiply(0.225));
//        tblcPaymLstTotalAmt.prefWidthProperty().bind(tblvPaymentLst.widthProperty().multiply(0.13));
//        tblcPaymLstAction.prefWidthProperty().bind(tblvPaymentLst.widthProperty().multiply(0.07));
    }

    // Getting all the Payments
    private void getAllPaymentsList(String searchKey) {
        APIClient apiClient = null;
        try {
            paymentListLogger.debug("Get getAllPaymentsList Started...");
            tblvPaymentLst.getItems().clear();
            Map<String, String> body = new HashMap<>();
            body.put("pageNo", "1");
            body.put("pageSize", "50");
            body.put("searchText", searchKey);
            body.put("sort", "{ \\\"colId\\\": null, \\\"isAsc\\\": true }");

            String requestBody = Globals.mapToString(body);
            apiClient = new APIClient(EndPoints.GET_ALL_PAYMENT_LIST, requestBody, RequestType.JSON);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    System.out.println("jsonObject" + jsonObject);
                    ObservableList<PaymentListDTO> observableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        paymentListLogger.debug("Get getAllPaymentsList Success...");

                        JsonObject responseObject = jsonObject.get("responseObject").getAsJsonObject();
                        System.out.println("debitNoteList" + responseObject);
                        JsonArray responseArray = responseObject.get("data").getAsJsonArray();
                        if (responseArray.size() > 0) {
                            for (JsonElement element : responseArray) {
                                JsonObject item = element.getAsJsonObject();
                                String id = item.get("id").getAsString();
                                String credit_note_no = item.get("payment_code").getAsString();
                                String transaction_dt = DateConvertUtil.convertDispDateFormat(item.get("transaction_dt").getAsString());
                                String narration = item.get("narration").getAsString();
                                String ledger_name = item.get("ledger_name").getAsString();
                                String total_amount = item.get("total_amount").getAsString();
                                String paymentSrNo = item.get("payment_sr_no").getAsString();
                                observableList.add(new PaymentListDTO(id, credit_note_no, transaction_dt, ledger_name, narration, total_amount, paymentSrNo));
                            }

                            tblcPaymLstPaymentNo.setCellValueFactory(new PropertyValueFactory<>("paymentNo"));
                            tblcPaymLstTransDt.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
                            tblcPaymLstSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
                            tblcPaymLstNarration.setCellValueFactory(new PropertyValueFactory<>("narration"));
                            tblcPaymLstTotalAmt.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
//                            tblcPaymLstAction.setCellFactory(param -> {
//                                final TableCell<PaymentListDTO, String> cell = new TableCell<>() {
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
////                                            GlobalController.getInstance().addTabStaticWithParam(SALES_ORDER_EDIT_SLUG, false, id);
//                                        });
//                                    }
//
//                                    {
//                                        delButton.setOnAction(actionEvent -> {
//                                            PaymentListDTO paymentListDTO = getTableView().getItems().get(getIndex());
//                                            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you Sure?", input -> {
//                                                if (input == 1) {
//                                                    deletePaymentEntry(Integer.parseInt(paymentListDTO.getId()));
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
                            tblvPaymentLst.setItems(observableList);
                            //******************************** Highlight on the Created/Edited Record in the List Start ********************************

                            if (PaymentListController.isNewPaymentCreated) {
                                tblvPaymentLst.getSelectionModel().selectLast();
                                tblvPaymentLst.scrollTo(tblvPaymentLst.getItems().size() - 1);
                                PaymentListController.isNewPaymentCreated = false; // Reset the flag
                            } else if (PaymentListController.editedPaymentId != null) {
                                for (PaymentListDTO payment : observableList) {
                                    if (payment.getId().equals(PaymentListController.editedPaymentId)) {
                                        tblvPaymentLst.getSelectionModel().select(payment);
                                        tblvPaymentLst.scrollTo(payment);
                                        PaymentListController.editedPaymentId = null; // Reset the ID
                                        break;
                                    }
                                }
                            }
                            //******************************** Highlight on the Created/Edited Record in the List End ********************************
                        } else {
                            paymentListLogger.debug("Get getAllPaymentsList responseObject is null...");
                        }
                    } else {
                        paymentListLogger.debug("Get getAllPaymentsList Error in response...");
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    paymentListLogger.error("Network API cancelled in getAllPaymentsList()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    paymentListLogger.error("Network API cancelled in getAllPaymentsList()" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.start();
            paymentListLogger.debug("Get getAllPaymentsList end");

        } catch (Exception e) {
            e.printStackTrace();
            paymentListLogger.error("Exception getAllPaymentsList() : " + Globals.getExceptionString(e));
        } finally {
            apiClient = null;
        }
        tblvPaymentLst.getSelectionModel().select(0);
    }

    // OnCLick of Delete Icon Deleting the Payment
    private void deletePaymentEntry(Integer id) {
        APIClient apiClient = null;
        try {
            paymentListLogger.debug("Get deletePaymentEntry Started...");
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(id));
            String formData = Globals.mapToStringforFormData(map);
            apiClient = new APIClient(EndPoints.DELETE_PAYMENT, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    System.out.println("jsonObject ::: >>" + jsonObject);
                    message = jsonObject.get("message").getAsString();
//                    AlertUtility.CustomCallback callback = (number) -> {
//                        if (number == 1) {
//                        }
//                    };
//                    Stage stage2 = (Stage) bpPaymentRootPane.getScene().getWindow();
//                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
//                        AlertUtility.AlertSuccess(stage2, "Success", message, callback);
//                        getAllPaymentsList("");
//                        tftranxPaymLstSearchField.requestFocus();
//                    } else {
//                        AlertUtility.AlertError(stage2, AlertUtility.alertTypeError, message, callback);
//                    }
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                            getAllPaymentsList("");
                            tftranxPaymLstSearchField.requestFocus();
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
                    paymentListLogger.error("Network API cancelled in deletePaymentEntry()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    paymentListLogger.error("Network API cancelled in deletePaymentEntry()" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.start();
            paymentListLogger.debug("Get deletePaymentEntry end");

        } catch (Exception e) {
            e.printStackTrace();
            paymentListLogger.error("Exception deletePaymentEntry() : " + Globals.getExceptionString(e));
        } finally {
            apiClient = null;
        }
    }

}
