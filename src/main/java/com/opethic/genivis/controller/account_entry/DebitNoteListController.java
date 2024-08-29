package com.opethic.genivis.controller.account_entry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.dto.CreditNoteListDTO;
import com.opethic.genivis.dto.PurchaseOrderDTO;
import com.opethic.genivis.dto.account_entry.DebitNoteListDTO;
import com.opethic.genivis.dto.account_entry.JournalListDTO;
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
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.FxmFileConstants.*;

public class DebitNoteListController implements Initializable {

    private static final Logger logger = LogManager.getLogger(DebitNoteListController.class);
    ObservableList<DebitNoteListDTO> debitNoteObjList = FXCollections.observableArrayList();
    private String responseBody, message, listCount;
    private ObservableList<DebitNoteListDTO> originalData;

    @FXML
    private TextField tfDebitNoteListSearch;
    @FXML
    private BorderPane bpRootPane;
    @FXML
    private Label lblDebitNoteListCount;
    @FXML
    private TableView<DebitNoteListDTO> tblvDebitNoteListDebitList;
    @FXML
    private TableColumn<DebitNoteListDTO, String> tblcDebitListDebitNoteNo, tblcDebitListTransactionDate, tblcDebitListSupplierName, tblcDebitListNarration, tblcDebitListTotalAmount, tblcDebitListAction;
    @FXML
    Button btnDebitNoteListCreate;
    private JsonObject jsonObject = null;

    //? Highlight the Record Start
    public static boolean isNewDibitNoteCreated = false; // Flag for new creation
    public static String editedDebitNoteId = null; // ID for edited Sales Order
    //? Highlight the Record End

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        //todo: Search without API Call in the Table
        originalData = tblvDebitNoteListDebitList.getItems();
        tfDebitNoteListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });

        //TODO: resizing the table columns as per the resolution.
        tblvDebitNoteListDebitList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        //TODO: Redirecting the list page onClick of Cancel btn.
        btnDebitNoteListCreate.setOnAction(event -> GlobalController.getInstance().addTabStatic(DEBIT_NOTE_CREATE_SLUG, false));
        //TODO: on page load focus Search field.
        Platform.runLater(() -> tfDebitNoteListSearch.requestFocus());
        //TODO: get all list of Debit Note
        getDebitNoteData("");

        //? this include all the Shortcut Keys
        initShortcutKeys();
        tblvDebitNoteListDebitList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                editDebitNote();
            }
        });

    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            bpRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            bpRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            bpRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            bpRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            bpRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            bpRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            bpRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    private void initShortcutKeys() {
        bpRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("create")) {
                } else {
                    editDebitNote();
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
                btnDebitNoteListCreate.fire();
            } else if (event.getCode() == KeyCode.DOWN && tfDebitNoteListSearch.isFocused()) {
                tblvDebitNoteListDebitList.getSelectionModel().select(0);
                tblvDebitNoteListDebitList.requestFocus();
            } else if (event.getCode() == KeyCode.E && event.isControlDown()) {
                editDebitNote();
            } else if (event.getCode() == KeyCode.D && event.isControlDown()) {
                handleCtrlDPressed();
            }
        });
    }

    //todo: Search Function to Search in the Table
    public void filterData(String keyword) {
        ObservableList<DebitNoteListDTO> filteredData = FXCollections.observableArrayList();

        if (keyword.isEmpty()) {
            tblvDebitNoteListDebitList.setItems(originalData);
            return;
        }

        for (DebitNoteListDTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tblvDebitNoteListDebitList.setItems(filteredData);
    }

    //todo: Search Function to Search in the Table for columns
    public boolean matchesKeyword(DebitNoteListDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        // Check if any of the columns contain the keyword
        return item.getSupplierName().toLowerCase().contains(lowerCaseKeyword) ||
                item.getNarration().toLowerCase().contains(lowerCaseKeyword) ||
                item.getTotalAmount().toLowerCase().contains(lowerCaseKeyword) ||
                item.getTransactionDate().toLowerCase().contains(lowerCaseKeyword) ||
                item.getCreditNoteNo().toLowerCase().contains(lowerCaseKeyword);
    }


    //TODO: Get and Set the  List Data of Franchise
    public void getDebitNoteData(String searchKey) {
        APIClient apiClient = null;
        try {
            logger.debug("Get Debit Note Data Started...");
            Map<String, String> body = new HashMap<>();
            body.put("pageNo", "1");
            body.put("pageSize", "50");
            body.put("searchText", searchKey);
//            body.put("sort", "{ \"colId\": null, \"isAsc\": true }");
            body.put("sort", "{ \\\"colId\\\": null, \\\"isAsc\\\": true }");

            String requestBody = Globals.mapToString(body);
            apiClient = new APIClient(EndPoints.DEBIT_NOTE_LIST_ENDPOINT, requestBody, RequestType.JSON);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    debitNoteObjList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonObject responseObject = jsonObject.get("responseObject").getAsJsonObject();
                        JsonArray responseArray = responseObject.get("data").getAsJsonArray();

                        listCount = String.valueOf(responseArray.size());
//                        System.out.println("listCount"+listCount);
                        lblDebitNoteListCount.setText(listCount);

                        if (responseArray.size() > 0) {
                            tblvDebitNoteListDebitList.setVisible(true);
                            for (JsonElement element : responseArray) {
                                JsonObject item = element.getAsJsonObject();
//                                System.out.println("item in Debit Note list " + item);
                                String id = item.get("id").getAsString();
                                String debitNoteNo = item.get("debit_note_no").getAsString();    //imp for conversion check
                                String transactionDate = DateConvertUtil.convertDispDateFormat(item.get("transaction_dt").getAsString());
                                String supplierName = item.get("ledger_name").getAsString();
                                String narration = item.get("narration").getAsString();
                                String totalAmount = item.get("total_amount").getAsString();
                                String action = "";

                                debitNoteObjList.add(new DebitNoteListDTO(id, debitNoteNo, transactionDate, supplierName, narration,
                                        totalAmount, action)
                                );
                            }
                            tblcDebitListDebitNoteNo.setCellValueFactory(new PropertyValueFactory<>("debitNoteNo"));
                            tblcDebitListTransactionDate.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
                            tblcDebitListSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
                            tblcDebitListNarration.setCellValueFactory(new PropertyValueFactory<>("narration"));
                            tblcDebitListTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

                            // Update originalData with new items
                            originalData.setAll(debitNoteObjList);

//                            tblcDebitListAction.setCellFactory(param -> {
//                                final TableCell<DebitNoteListDTO, String> cell = new TableCell<>() {
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
//                                            GlobalController.getInstance().addTabStaticWithParam(DEBIT_NOTE_EDIT_SLUG, false, id);
//                                        });
//                                    }
//
//                                    {
//                                        delButton.setOnAction(actionEvent -> {
//                                            DebitNoteListDTO credntId = getTableView().getItems().get(getIndex());
//                                            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you Sure?", input -> {
//                                                if (input == 1) {
//                                                    deleteDebitNoteList(Integer.parseInt(credntId.getId()));
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

                            tblvDebitNoteListDebitList.setItems(debitNoteObjList);

                            //******************************** Highlight on the Created/Edited Record in the List Start ********************************
                            if (DebitNoteListController.isNewDibitNoteCreated) {
                                tblvDebitNoteListDebitList.getSelectionModel().selectLast();
                                tblvDebitNoteListDebitList.scrollTo(tblvDebitNoteListDebitList.getItems().size() - 1);
                                DebitNoteListController.isNewDibitNoteCreated = false; // Reset the flag
                            } else if (DebitNoteListController.editedDebitNoteId != null) {
                                for (DebitNoteListDTO debitNote : debitNoteObjList) {
                                    if (debitNote.getId().equals(DebitNoteListController.editedDebitNoteId)) {
                                        tblvDebitNoteListDebitList.getSelectionModel().select(debitNote);
                                        tblvDebitNoteListDebitList.scrollTo(debitNote);
                                        DebitNoteListController.editedDebitNoteId = null; // Reset the ID
                                        break;
                                    }
                                }
                            }
                            //******************************** Highlight on the Created/Edited Record in the List End ********************************

                        } else {
                            logger.error("Failed to Load Data ");
//                            AlertUtility.CustomCallback callback = (number) -> {
//                                if (number == 0) {
//                                    tfDebitNoteListSearch.requestFocus();
//                                }
//                            };
//                            Stage stage = (Stage) bpRootPane.getScene().getWindow();
//                            AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Failed to Load Data ", callback);

                        }
                    } else {
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Falied to connect server! ", in -> {
                        });
                    }
                }
            });

            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getJournalData()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getJournalData()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            logger.debug("Get Debit Note Date End...");
        } catch (Exception e) {
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Falied to connect server! ", in -> {
            });
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    //TODO: Delete API Function
    public void deleteDebitNoteList(int id) {

        try {
            AlertUtility.CustomCallback callback = number -> {
                if (number == 1) {
                    APIClient apiClient = null;
                    try {
                        logger.debug("Delete Debit Note Data Started...");
                        Map<String, String> map = new HashMap<>();
                        map.put("id", String.valueOf(id));
                        String formData = Globals.mapToStringforFormData(map);
//                        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.SALES_ORDER_DELETE_ENDPOINT);
                        apiClient = new APIClient(EndPoints.DEBIT_NOTE_DELETE_ENDPOINT, formData, RequestType.FORM_DATA);

                        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                                System.out.println("jsonObject-->" + jsonObject);
//                                responseBody = response.body();
//                                JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                                message = jsonObject.get("message").getAsString();
                                System.out.println("Delete->" + jsonObject.get("message"));
//                                AlertUtility.CustomCallback callback1 = (number1) -> {
//                                    if (number1 == 1) {
//                                        // Update the list after successful deletion
//                                        debitNoteObjList.removeIf(item -> item.getId().equals(String.valueOf(id)));
//                                        tblvDebitNoteListDebitList.setItems(debitNoteObjList);
//                                    }
//                                };
//                                Stage stage2 = (Stage) bpRootPane.getScene().getWindow();
//                                if (jsonObject.get("responseStatus").getAsInt() == 200) {
//                                    AlertUtility.AlertSuccess(stage2, "Success", message, callback1);
//                                } else {
//                                    AlertUtility.AlertError(stage2, AlertUtility.alertTypeError, message, callback1);
//                                }
                                if (jsonObject.get("responseStatus").getAsInt() == 200) {
                                    AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                                        // Update the list after successful deletion
                                        debitNoteObjList.removeIf(item -> item.getId().equals(String.valueOf(id)));
                                        tblvDebitNoteListDebitList.setItems(debitNoteObjList);
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
                                logger.error("Network API cancelled in Delete Debit Note()" + workerStateEvent.getSource().getValue().toString());

                            }
                        });

                        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                logger.error("Network API failed in Delete Debit Note()" + workerStateEvent.getSource().getValue().toString());
                            }
                        });
                        apiClient.start();
                        logger.debug("Delete Debit Note Data End...");

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        apiClient = null;
                    }
                } else {
                    System.out.println("working!");
                }
            };
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Delete", callback);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //? OnDoubleClick In Edit Page of Particular Credit Note Screen
    private void editDebitNote() {
        DebitNoteListDTO selectedItem = (DebitNoteListDTO) tblvDebitNoteListDebitList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            //? Highlight
            DebitNoteListController.editedDebitNoteId = String.valueOf(selectedItem);
            Integer debitNoteId = Integer.valueOf(selectedItem.getId());
            GlobalController.getInstance().addTabStaticWithParam(DEBIT_NOTE_EDIT_SLUG, false, debitNoteId);
        }
    }

    @FXML
    private void handleCtrlDPressed() {
        try {
            DebitNoteListDTO selectedItem = (DebitNoteListDTO) tblvDebitNoteListDebitList.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                Integer debitNoteId = Integer.valueOf(selectedItem.getId());
                deleteDebitNoteList(debitNoteId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
