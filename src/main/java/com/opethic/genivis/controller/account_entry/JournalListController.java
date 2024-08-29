package com.opethic.genivis.controller.account_entry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.master.FranchiseListController;
import com.opethic.genivis.controller.tranx_sales.SalesOrderListController;
import com.opethic.genivis.dto.FranchiseListDTO;
import com.opethic.genivis.dto.SalesOrderListDTO;
import com.opethic.genivis.dto.account_entry.JournalListDTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.FxmFileConstants.*;
import static com.opethic.genivis.utils.Globals.*;

public class JournalListController implements Initializable {

    @FXML
    private TableView<JournalListDTO> tvJournalList;
    @FXML
    private TableColumn<JournalListDTO,String> tcJournalListJournalNo;
    @FXML
    private TableColumn<JournalListDTO,String>  tcJournalListTranxDate;
    @FXML
    private TableColumn<JournalListDTO,String>  tcJournalListSupplierName;
    @FXML
    private TableColumn<JournalListDTO,String>  tcJournalListNarration;
    @FXML
    private TableColumn<JournalListDTO,String>  tcJournalListTotalAmount;
    @FXML
    private TableColumn<JournalListDTO,String>  tcJournalListAction;
    @FXML
    private TextField tfJournalSearch;
    @FXML
    private Button btnJournalListCreate;

    private Node[] focusableNodes;

    private ObservableList<JournalListDTO> originalData;
    @FXML
    private BorderPane spRootJournalListPane;
    private JsonObject jsonObject = null;
    private static final Logger logger = LogManager.getLogger(JournalListController.class);
    ObservableList<JournalListDTO> journalObservableList = FXCollections.observableArrayList();
    private String responseBody,message;

    //? Highlight the Record Start
    public static boolean isNewJournalCreated = false; // Flag for new creation
    public static String editedJournalId = null; // ID for edited Sales Order
    //? Highlight the Record End

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        //? this include all the Shortcut Keys
        initShortcutKeys();

        //? this inculde all the design related properties and important Fields
        madatoryFields();

        //? this include all the table view operations - Edit , Conversions , search , etc..
        tableViewOperations();

        //? Function to Get the List of Franchise
        getJournalData("");

        //? Redirect to Create Page from List Page on CLick on the Create Button and in the Same Tab of TabPane
        btnJournalListCreate.setOnAction( event ->{
                    Globals.salesOrderListDTO=null;
                    GlobalController.getInstance().addTabStatic(JOURNAL_CREATE_SLUG,false);
                }
        );

        //? Search without API Call in the Table
        originalData = tvJournalList.getItems();
        tfJournalSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });

        //? get the id onDoubleClick for Edit
        tvJournalList.setRowFactory(tv ->{
            TableRow<JournalListDTO> row=new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(mouseEvent.getClickCount()==2){
                        JournalListDTO journalListDTO=row.getItem();
                        journalEditPage();
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
            spRootJournalListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            spRootJournalListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            spRootJournalListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            spRootJournalListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            spRootJournalListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            spRootJournalListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            spRootJournalListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    private void initShortcutKeys() {
        spRootJournalListPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("create")) {
                } else {
                    journalEditPage();
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
            }

            if (event.getCode() == KeyCode.N && event.isControlDown()) {
                btnJournalListCreate.fire();
            }
            if (event.getCode() == KeyCode.DOWN && tfJournalSearch.isFocused()) {
                tvJournalList.getSelectionModel().select(0);
                tvJournalList.requestFocus();
            } else if (event.getCode() == KeyCode.E && event.isControlDown()) {
                journalEditPage();
            }else if (event.getCode() == KeyCode.D && event.isControlDown()) {
                handleCtrlDPressed();
            }
        });
    }
    private void tableViewOperations() {
        //? Set the TableView Columns with proper height and Width
        tvJournalList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void madatoryFields() {

        //? When Open the Page Focus of the First Field
        Platform.runLater(() -> tfJournalSearch.requestFocus());
    }



    //? Search Function to Search in the Table
    private void filterData(String keyword) {
        ObservableList<JournalListDTO> filteredData = FXCollections.observableArrayList();

        if (keyword.isEmpty()) {
            tvJournalList.setItems(originalData);
            return;
        }

        for (JournalListDTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tvJournalList.setItems(filteredData);
    }

    //? Search Function to Search in the Table for columns
    private boolean matchesKeyword(JournalListDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        // ?Check if any of the columns contain the keyword
        return item.getJournalNo().toLowerCase().contains(lowerCaseKeyword) ||
                item.getJournalTranxDate().toLowerCase().contains(lowerCaseKeyword) ||
                item.getJournalSupplierName().toLowerCase().contains(lowerCaseKeyword) ||
                item.getJournalNarration().toLowerCase().contains(lowerCaseKeyword) ||
                item.getJournalTotalAmount().toLowerCase().contains(lowerCaseKeyword);
    }

    //? Get and Set the  List Data of Franchise
    public void getJournalData(String searchKey) {
        APIClient apiClient=null;
//        SalesQuotationListLogger.info(" fetch Sales Quotation invoice List : SalesQuotationListController");
        try {
            logger.debug("Get Journal Data Started...");
            Map<String, String> body = new HashMap<>();
            body.put("pageNo", "1");
            body.put("pageSize", "50");
            body.put("searchText", searchKey);
//            body.put("sort", "{ \"colId\": null, \"isAsc\": true }");
            body.put("sort", "{ \\\"colId\\\": null, \\\"isAsc\\\": true }");

            String requestBody = Globals.mapToString(body);
//            HttpResponse<String> response = APIClient.postJsonRequest(requestBody, EndPoints.JOURNAL_LIST_ENDPOINT);
            apiClient = new APIClient(EndPoints.JOURNAL_LIST_ENDPOINT, requestBody, RequestType.JSON);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

//                    System.out.println("SalesOrder" + response.body());
//                    JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
                    System.out.println("111Q" + jsonObject);
                    journalObservableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonObject responseObject = jsonObject.get("responseObject").getAsJsonObject();
                        System.out.println("journallllllllllllllllll"+responseObject);
                        JsonArray responseArray = responseObject.get("data").getAsJsonArray();

                        if (responseArray.size() > 0) {
                            tvJournalList.setVisible(true);
                            for (JsonElement element : responseArray) {
                                JsonObject item = element.getAsJsonObject();
                                System.out.println("item in sale order list "+item);
                                String id = item.get("id").getAsString();
                                String journalNo = item.get("journal_code").getAsString();    //imp for conversion check
                                String journalTranxDate = DateConvertUtil.convertDispDateFormat(item.get("transaction_dt").getAsString());
                                String journalSupplierName = item.get("ledger_name").getAsString();
                                String journalNarration = item.get("narration").getAsString();
                                String journalTotalAmount = item.get("total_amount").getAsString();
                                String journalAction = "";

                                journalObservableList.add(new JournalListDTO(id, journalNo, journalTranxDate, journalSupplierName, journalNarration,
                                        journalTotalAmount, journalAction)
                                );
                            }
                            tcJournalListJournalNo.setCellValueFactory(new PropertyValueFactory<>("journalNo"));
                            tcJournalListTranxDate.setCellValueFactory(new PropertyValueFactory<>("journalTranxDate"));
                            tcJournalListSupplierName.setCellValueFactory(new PropertyValueFactory<>("journalSupplierName"));
                            tcJournalListNarration.setCellValueFactory(new PropertyValueFactory<>("journalNarration"));
                            tcJournalListTotalAmount.setCellValueFactory(new PropertyValueFactory<>("journalTotalAmount"));
//                            tcJournalListAction.setCellFactory(param -> {
//                                final TableCell<JournalListDTO, String> cell = new TableCell<>() {
//                                    private ImageView delImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/del.png"))));
//                                    private ImageView edtImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/edt.png"))));
////                            private ImageView viewImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/view.png"))));
//
//                                    {
//                                        delImg.setFitHeight(20.0);
//                                        delImg.setFitWidth(20.0);
//                                        edtImg.setFitHeight(20.0);
//                                        edtImg.setFitWidth(20.0);
////                                viewImg.setFitHeight(20.0);
////                                viewImg.setFitWidth(20.0);
//                                    }
//
//                                    private final Button delButton = new Button("", delImg);
//                                    private final Button edtButton = new Button("", edtImg);
////                            private final Button viewButton = new Button("", viewImg);
//
//                                    {
//                                        edtButton.setOnAction(actionEvent -> {
//                                            Integer id = Integer.valueOf(getTableView().getItems().get(getIndex()).getId());
////                                            GlobalController.getInstance().addTabStaticWithParam(SALES_ORDER_EDIT_SLUG, false, id);
//                                        });
//                                    }{
//                                        delButton.setOnAction(actionEvent -> {
//                                            JournalListDTO id = getTableView().getItems().get(getIndex());
//                                            if (id != null) {
//                                                Integer id1 = Integer.valueOf(id.getId());
//
//                                                deleteJournal(id1);
//                                            } else {
//                                                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Failed to Delete", in -> {
//                                                    tfJournalSearch.requestFocus();
//                                                });
//                                            }
////                                    GlobalController.getInstance().addTabStaticWithParam(SALES_ORDER_EDIT_SLUG, false, id);
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

                            // Update originalData with new items
                            originalData.setAll(journalObservableList);

                            tvJournalList.setItems(journalObservableList);
                            //******************************** Highlight on the Created/Edited Record in the List Start ********************************
                            if (JournalListController.isNewJournalCreated) {
                                tvJournalList.getSelectionModel().selectLast();
                                tvJournalList.scrollTo(tvJournalList.getItems().size() - 1);
                                JournalListController.isNewJournalCreated = false; // Reset the flag
                            } else if (JournalListController.editedJournalId != null) {
                                for (JournalListDTO journal : journalObservableList) {
                                    if (journal.getId().equals(JournalListController.editedJournalId)) {
                                        tvJournalList.getSelectionModel().select(journal);
                                        tvJournalList.scrollTo(journal);
                                        JournalListController.editedJournalId = null; // Reset the ID
                                        break;
                                    }
                                }
                            }
                            //******************************** Highlight on the Created/Edited Record in the List End ********************************

                        } else {
                            logger.error("Failed to Load Data JournalList");
                        }
                    } else {
                        logger.error("Falied to connect server! JournalList");
//                        AlertUtility.CustomCallback callback = (number) -> {
//                            if(number==0) {
//                                tfJournalSearch.requestFocus();
//                            }
//                        };
//                        Stage stage = (Stage) spRootJournalListPane.getScene().getWindow();
//                        AlertUtility.AlertError(stage,AlertUtility.alertTypeError, "Falied to connect server! ", callback);
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
            logger.debug("Get Journal Data End...");
        } catch (Exception e) {
            logger.error("Falied to connect server! Journal List");
//            AlertUtility.CustomCallback callback = (number) -> {
//                if(number==0) {
//                    tfJournalSearch.requestFocus();
//                }
//            };
//            Stage stage = (Stage) spRootJournalListPane.getScene().getWindow();
//            AlertUtility.AlertError(stage,AlertUtility.alertTypeError, "Falied to connect server! ", callback);
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Falied to connect server! ", in -> {
            });
            e.printStackTrace();
        }finally {
            apiClient=null;
        }
    }

    //? Delete API Function
    public void deleteJournal(int id){

        try {
            AlertUtility.CustomCallback callback = number -> {
                if (number == 1) {
                    APIClient apiClient=null;
                    try {
                        logger.debug("Delete Journal Data Started...");
                        Map<String, String> map = new HashMap<>();
                        map.put("id", String.valueOf(id));
                        String formData = Globals.mapToStringforFormData(map);
//                        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.JOURNAL_DELETE_ENDPOINT);
                        apiClient = new APIClient(EndPoints.JOURNAL_DELETE_ENDPOINT, formData, RequestType.FORM_DATA);

                        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

//                                responseBody = response.body();
//                                JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                                message=jsonObject.get("message").getAsString();
                                System.out.println("Delete->"+jsonObject.get("message"));
                                if(jsonObject.get("responseStatus").getAsInt()==200){
                                    AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                                        // Update the list after successful deletion
                                        journalObservableList.removeIf(item -> item.getId().equals(String.valueOf(id)));
                                        tvJournalList.setItems(journalObservableList);
                                    });
                                }else {
                                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                                    });
                                }
                            }
                        });
                        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                logger.error("Network API cancelled in deleteJournal()" + workerStateEvent.getSource().getValue().toString());

                            }
                        });

                        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                logger.error("Network API failed in deleteJournal()" + workerStateEvent.getSource().getValue().toString());
                            }
                        });
                        apiClient.start();
                        logger.debug("Delete Journal Data End...");

                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        apiClient=null;
                    }
                } else {
                    System.out.println("working!");
//                System.out.println("this is idno"+id);
                }
            };
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Delete", callback);
//        Globals.journalListDTO=null;
//        GlobalController.getInstance().addTabStatic(FRANCHISE_LIST_SLUG,false);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    //? Open Create Page Only for Edit in the Same Tab
    public void journalEditPage() {
        try {
            journalListDTO=tvJournalList.getSelectionModel().getSelectedItem();
            if(journalListDTO!=null){
                //? Highlight
                JournalListController.editedJournalId = journalListDTO.getId();
                Integer id = Integer.valueOf(journalListDTO.getId());
                GlobalController.getInstance().addTabStaticWithParam(JOURNAL_EDIT_SLUG, false, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCtrlDPressed() {
        try {
            journalListDTO=tvJournalList.getSelectionModel().getSelectedItem();
            if(Globals.journalListDTO!=null) {
                Integer id = Integer.valueOf(journalListDTO.getId());
                deleteJournal(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
