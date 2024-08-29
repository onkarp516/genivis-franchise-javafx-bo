package com.opethic.genivis.controller.account_entry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.tranx_sales.SalesOrderListController;
import com.opethic.genivis.dto.SalesOrderListDTO;
import com.opethic.genivis.dto.account_entry.ContraListDTO;
import com.opethic.genivis.dto.account_entry.JournalListDTO;
import com.opethic.genivis.dto.reqres.receipt.ReceiptListDTO;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.FxmFileConstants.*;
import static com.opethic.genivis.utils.Globals.contraListDTO;

public class ContraListController implements Initializable {

    @FXML
    private TableView<ContraListDTO> tvContraList;
    @FXML
    private TableColumn<ContraListDTO, String> tcContraListContraNo;
    @FXML
    private TableColumn<ContraListDTO, String> tcContraListTranxDate;
    @FXML
    private TableColumn<ContraListDTO, String> tcContraListSupplierName;
    @FXML
    private TableColumn<ContraListDTO, String> tcContraListNarration;
    @FXML
    private TableColumn<ContraListDTO, String> tcContraListTotalAmount;
    @FXML
    private TableColumn<ContraListDTO, String> tcContraListAction;

    @FXML
    private TextField tfContraSearch;
    @FXML
    private Button btnContraListCreate;

    private Node[] focusableNodes;

    private ObservableList<ContraListDTO> originalData;
    @FXML
    private BorderPane spRootContraListPane;
    private JsonObject jsonObject = null;
    private static final Logger logger = LogManager.getLogger(ContraListController.class);
    ObservableList<ContraListDTO> contraObservableList = FXCollections.observableArrayList();
    private String responseBody, message;

    //? Highlight the Record Start
    public static boolean isNewContraCreated = false; // Flag for new creation
    public static String editedContraId = null; // ID for edited Sales Order
    //? Highlight the Record End

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        //Set the TableView Columns with proper height and Width
        tvContraList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //When Open the Page Focus of the First Field
        Platform.runLater(() -> tfContraSearch.requestFocus());

        //? this include all the Shortcut Keys
        initShortcutKeys();

        //Populate focusable nodes array with the desired order and Focus To next and Previous Element
        focusableNodes = new Node[]{tfContraSearch, btnContraListCreate, tvContraList};
        CommonValidationsUtils.setupFocusNavigation(focusableNodes);

        //Function to Get the List of Franchise
        getContraData("");

        //Redirect to Create Page from List Page on CLick on the Create Button and in the Same Tab of TabPane
        btnContraListCreate.setOnAction(event -> {
//                    Globals.franchiseListDTO=null;
            GlobalController.getInstance().addTabStatic(CONTRA_CREATE_SLUG, false);
        });
        //Search without API Call in the Table
        originalData = tvContraList.getItems();
        tfContraSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });

        // OnDoubleClick In Edit Page of Particular Receipt Screen
        tvContraList.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                contraEditPage();
            }
        });
    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            spRootContraListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            spRootContraListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            spRootContraListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            spRootContraListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            spRootContraListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            spRootContraListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            spRootContraListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    private void initShortcutKeys() {
        spRootContraListPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("create")) {
                } else {
                    contraEditPage();
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
                btnContraListCreate.fire();
            }else if(event.getCode() == KeyCode.DOWN && tfContraSearch.isFocused()) {
                tvContraList.getSelectionModel().select(0);
                tvContraList.requestFocus();
            } else if (event.getCode() == KeyCode.E && event.isControlDown()) {
                contraEditPage();
            }else if (event.getCode() == KeyCode.D && event.isControlDown()) {
                handleCtrlDPressed();
            }
        });
    }

    //Search Function to Search in the Table
    private void filterData(String keyword) {
        ObservableList<ContraListDTO> filteredData = FXCollections.observableArrayList();

        if (keyword.isEmpty()) {
            tvContraList.setItems(originalData);
            return;
        }

        for (ContraListDTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tvContraList.setItems(filteredData);
    }
    //Search Function to Search in the Table for columns
    private boolean matchesKeyword(ContraListDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        // todo:Check if any of the columns contain the keyword
        return item.getContraNo().toLowerCase().contains(lowerCaseKeyword) ||
                item.getContraTranxDate().toLowerCase().contains(lowerCaseKeyword) ||
                item.getContraSupplierName().toLowerCase().contains(lowerCaseKeyword) ||
                item.getContraNarration().toLowerCase().contains(lowerCaseKeyword) ||
                item.getContraTotalAmount().toLowerCase().contains(lowerCaseKeyword);
    }

    //Get and Set the  List Data of Franchise
    public void getContraData(String searchKey) {
        APIClient apiClient=null;
//        SalesQuotationListLogger.info(" fetch Sales Quotation invoice List : SalesQuotationListController");
        try {
            logger.debug("Get Contra Data Started...");
            Map<String, String> body = new HashMap<>();
            body.put("pageNo", "1");
            body.put("pageSize", "50");
            body.put("searchText", searchKey);
//            body.put("sort", "{ \"colId\": null, \"isAsc\": true }");
            body.put("sort", "{ \\\"colId\\\": null, \\\"isAsc\\\": true }");

            String requestBody = Globals.mapToString(body);
//            HttpResponse<String> response = APIClient.postJsonRequest(requestBody, EndPoints.CONTRA_LIST_ENDPOINT);
            apiClient = new APIClient(EndPoints.CONTRA_LIST_ENDPOINT, requestBody, RequestType.JSON);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

//                    JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
                    contraObservableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonObject responseObject = jsonObject.get("responseObject").getAsJsonObject();
                        JsonArray responseArray = responseObject.get("data").getAsJsonArray();

                        if (responseArray.size() > 0) {
                            tvContraList.setVisible(true);
                            for (JsonElement element : responseArray) {
                                JsonObject item = element.getAsJsonObject();
                                String id = item.get("id").getAsString();
                                String contraNo = item.get("contra_code").getAsString();    //imp for conversion check
                                String contraTranxDate = DateConvertUtil.convertDispDateFormat(item.get("transaction_dt").getAsString());
                                String contraSupplierName = item.get("ledger_name").getAsString();
                                String contraNarration = item.get("narration").getAsString();
                                String contraTotalAmount = item.get("total_amount").getAsString();
                                String contraAction = "";

                                contraObservableList.add(new ContraListDTO(id, contraNo, contraTranxDate, contraSupplierName, contraNarration,
                                        contraTotalAmount, contraAction)
                                );
                            }
                            tcContraListContraNo.setCellValueFactory(new PropertyValueFactory<>("contraNo"));
                            tcContraListTranxDate.setCellValueFactory(new PropertyValueFactory<>("contraTranxDate"));
                            tcContraListSupplierName.setCellValueFactory(new PropertyValueFactory<>("contraSupplierName"));
                            tcContraListNarration.setCellValueFactory(new PropertyValueFactory<>("contraNarration"));
                            tcContraListTotalAmount.setCellValueFactory(new PropertyValueFactory<>("contraTotalAmount"));
//                            tcContraListAction.setCellFactory(param -> {
//                                final TableCell<ContraListDTO, String> cell = new TableCell<>() {
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
//                                            ContraListDTO id = getTableView().getItems().get(getIndex());
//                                            if (id != null) {
//                                                Integer id1 = Integer.valueOf(id.getId());
//
//                                                deleteContra(id1);
//                                            } else {
//                                                // Show an alert indicating that the order is already closed
//                                                AlertUtility.CustomCallback callback = (number) -> {
//                                                    if (number == 0) {
//                                                        tfContraSearch.requestFocus();
//                                                    }
//                                                };
//                                                Stage stage = (Stage) spRootContraListPane.getScene().getWindow();
//                                                AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Failed to Delete", callback);
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
                            originalData.setAll(contraObservableList);

                            tvContraList.setItems(contraObservableList);
                            //******************************** Highlight on the Created/Edited Record in the List Start ********************************
                            if (ContraListController.isNewContraCreated) {
                                tvContraList.getSelectionModel().selectLast();
                                tvContraList.scrollTo(tvContraList.getItems().size() - 1);
                                ContraListController.isNewContraCreated = false; // Reset the flag
                            } else if (ContraListController.editedContraId != null) {
                                for (ContraListDTO contra : contraObservableList) {
                                    if (contra.getId().equals(ContraListController.editedContraId)) {
                                        tvContraList.getSelectionModel().select(contra);
                                        tvContraList.scrollTo(contra);
                                        ContraListController.editedContraId = null; // Reset the ID
                                        break;
                                    }
                                }
                            }
                            //******************************** Highlight on the Created/Edited Record in the List Start ********************************e Created/Edited Record in the List End ********************************

                        } else {
//                            AlertUtility.CustomCallback callback = (number) -> {
//                                if(number==0) {
//                                    tfContraSearch.requestFocus();
//                                }
//                            };
//                            Stage stage = (Stage) spRootContraListPane.getScene().getWindow();
//                            AlertUtility.AlertError(stage,AlertUtility.alertTypeError, "Failed to Load Data ", callback);

                        }
                    } else {
                        AlertUtility.CustomCallback callback = (number) -> {
                            if(number==0) {
                                tfContraSearch.requestFocus();
                            }
                        };
                        Stage stage = (Stage) spRootContraListPane.getScene().getWindow();
                        AlertUtility.AlertError(stage,AlertUtility.alertTypeError, "Falied to connect server! ", callback);

                    }
                }
            });

            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getContraData()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getContraData()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            logger.debug("Get Contra Data End...");
        } catch (Exception e) {
            AlertUtility.CustomCallback callback = (number) -> {
                if(number==0) {
                    tfContraSearch.requestFocus();
                }
            };
            Stage stage = (Stage) spRootContraListPane.getScene().getWindow();
            AlertUtility.AlertError(stage,AlertUtility.alertTypeError, "Falied to connect server! ", callback);

            e.printStackTrace();
        }finally {
            apiClient=null;
        }
    }

    //Delete API Function
    public void deleteContra(int id){

        try {
            AlertUtility.CustomCallback callback = number -> {
                if (number == 1) {
                    APIClient apiClient=null;
                    try {
                        logger.debug("Delete Contra Data Started...");
                        Map<String, String> map = new HashMap<>();
                        map.put("id", String.valueOf(id));
                        String formData = Globals.mapToStringforFormData(map);
//                        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.CONTRA_DELETE_ENDPOINT);
                        apiClient = new APIClient(EndPoints.CONTRA_DELETE_ENDPOINT, formData, RequestType.FORM_DATA);

                        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

//                                responseBody = response.body();
//                                JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                                message=jsonObject.get("message").getAsString();
//                                AlertUtility.CustomCallback callback1 = (number1) -> {
//                                    if (number1 == 1) {
//                                        // Update the list after successful deletion
//                                        contraObservableList.removeIf(item -> item.getId().equals(String.valueOf(id)));
//                                        tvContraList.setItems(contraObservableList);
////                                GlobalController.getInstance().addTabStatic(FRANCHISE_LIST_SLUG, false);
//                                    }
//                                };
//                                Stage stage2 = (Stage) spRootContraListPane.getScene().getWindow();
//                                if(jsonObject.get("responseStatus").getAsInt()==200){
//                                    AlertUtility.AlertSuccess(stage2,"Success",message,callback1);
//                                }else {
//                                    AlertUtility.AlertError(stage2,AlertUtility.alertTypeError, message, callback1);
//                                }
                                if (jsonObject.get("responseStatus").getAsInt() == 200) {
                                    AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                                        // Update the list after successful deletion
                                        contraObservableList.removeIf(item -> item.getId().equals(String.valueOf(id)));
                                        tvContraList.setItems(contraObservableList);
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
                                logger.error("Network API cancelled in deleteContra()" + workerStateEvent.getSource().getValue().toString());

                            }
                        });

                        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                logger.error("Network API failed in deleteContra()" + workerStateEvent.getSource().getValue().toString());
                            }
                        });
                        apiClient.start();
                        logger.debug("Delete Contra Data End...");

                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        apiClient=null;
                    }
                } else {
                    logger.debug("Delete Contra deleteContra()");
                }
            };
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Delete", callback);
//        Globals.contraListDTO=null;
//        GlobalController.getInstance().addTabStatic(FRANCHISE_LIST_SLUG,false);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //?  Open Create Page Only for Edit in the Same Tab
    public void contraEditPage() {
        try {
            contraListDTO=tvContraList.getSelectionModel().getSelectedItem();
            if(contraListDTO!=null) {
                //? Highlight
                ContraListController.editedContraId = contraListDTO.getId();
                Integer id = Integer.valueOf(contraListDTO.getId());
                GlobalController.getInstance().addTabStaticWithParam(CONTRA_EDIT_SLUG, false, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleCtrlDPressed() {
        try {
            contraListDTO=tvContraList.getSelectionModel().getSelectedItem();
            if(Globals.contraListDTO!=null) {
                Integer id = Integer.valueOf(contraListDTO.getId());
                deleteContra(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
