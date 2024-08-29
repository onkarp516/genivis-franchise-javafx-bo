package com.opethic.genivis.controller.master;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.commons.SwitchButton;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.dto.PaymentModeDTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.CommonValidationsUtils;
import com.opethic.genivis.utils.Globals;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.network.EndPoints.*;

public class PaymentModeController implements Initializable {

    @FXML
    private TextField tfPaymentCreateMode;

    @FXML
    private TextField tfPaymentSearch;

    @FXML
    private Button btnPaymentCreateSubmit;
    @FXML
    private Button btnPaymentCreateCancel;

    @FXML
    private TableView<PaymentModeDTO> tcPaymentView;

    @FXML
    private TableColumn<PaymentModeDTO, String> tcPaymentMode;
    @FXML
    private Label lbPaymentModeCreate;

    @FXML
    private BorderPane spRootPaymentModePane;
    private Node[] focusableNodes;
    private ObservableList<PaymentModeDTO> originalData;
    private JsonObject jsonObject = null;
    private static final Logger logger = LogManager.getLogger(PaymentModeController.class);

    String id = "";
    String message = "";
//    String flagStatus = "";
    ObservableList<PaymentModeDTO> observableList = FXCollections.observableArrayList();

    @FXML
    private VBox vboxPaymentModeRoot;

    private String focusInd = "";
    private Integer rowIndex;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        Platform.runLater(() -> tfPaymentCreateMode.requestFocus());

        //? this include all the Shortcut Keys
        initShortcutKeys();

        //? this inculde all the design related properties and important Fields
        madatoryFields();

        //? this include all the table view operations - Edit , search , etc..
        tableViewOperations();

        //? Get All Payment Function
        getPaymentModeData();

        //Create Payment Mode Function
        btnPaymentCreateSubmit.setOnAction(e -> {
            if (CommonValidationsUtils.validateForm(tfPaymentCreateMode)) {
                createPaymentMode();
            }
        });

        //Show alert on the Screen
        sceneInitilization();
        //clear data on click clear button
        btnPaymentCreateCancel.setOnAction((event) -> {
            clearFields();
        });
    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            spRootPaymentModePane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            spRootPaymentModePane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            spRootPaymentModePane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            spRootPaymentModePane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            spRootPaymentModePane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            spRootPaymentModePane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            spRootPaymentModePane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    private void tableViewOperations() {
        tcPaymentView.setFocusTraversable(true);
        tcPaymentView.setDisable(false);
        //Get id Value on double click and set data to textField
        tcPaymentView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                setEditData();
            }
        });
        tcPaymentView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) {
                setEditData();
            }
        });

        //Search without API Call in the Table
        originalData = tcPaymentView.getItems();
        tfPaymentSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });
    }

    private void setEditData() {
        rowIndex = tcPaymentView.getSelectionModel().getSelectedIndex();
        PaymentModeDTO selectedItem = tcPaymentView.getSelectionModel().getSelectedItem();
        id = selectedItem.getId();
        if (selectedItem != null) {
            tfPaymentCreateMode.setText(selectedItem.getPaymentMode());
            btnPaymentCreateSubmit.setText("Update");
            tfPaymentCreateMode.requestFocus();
        }
    }

    private void madatoryFields() {
        CommonValidationsUtils.changeStarColour(vboxPaymentModeRoot);

        tcPaymentMode.prefWidthProperty().bind(tcPaymentView.widthProperty().multiply(0.25));


        AnchorPane.setTopAnchor(lbPaymentModeCreate, 20.0);
        AnchorPane.setLeftAnchor(lbPaymentModeCreate, 30.0);
    }

    private void initShortcutKeys() {
//        spRootPaymentModePane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        spRootPaymentModePane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        //Enter,Tab Key Cursor Movement
        spRootPaymentModePane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {

            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("submit")) {
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("update")) {
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("clear")) {
                } else {

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
            }else if (event.getCode() == KeyCode.S && event.isControlDown()) {
                btnPaymentCreateSubmit.fire();
            } else if (event.getCode() == KeyCode.X && event.isControlDown()) {
                btnPaymentCreateCancel.fire();
            }else if (event.getCode() == KeyCode.DOWN && tfPaymentSearch.isFocused()) {
                tcPaymentView.getSelectionModel().select(0);
                tcPaymentView.requestFocus();
            } else if (event.getCode() == KeyCode.DOWN && tfPaymentCreateMode.isFocused()) {
                tcPaymentView.getSelectionModel().select(0);
                tcPaymentView.requestFocus();
            } else if (event.getCode() == KeyCode.E && event.isControlDown()) {
                setEditData();
            } else if (event.getCode() == KeyCode.D && event.isControlDown()) {
                handleDeletePressed();
            } else if (event.getCode() == KeyCode.N && event.isControlDown()) {
                tfPaymentCreateMode.requestFocus();
            }
        });
    }

    public void sceneInitilization() {
        spRootPaymentModePane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }

    //clear Fields Function
    public void clearFields() {
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                tfPaymentCreateMode.setText("");
                tfPaymentCreateMode.requestFocus();
                btnPaymentCreateSubmit.setText("Submit");
            } else {
            }
        };
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnClear, callback);
    }

    //Search Function to Search in the Table
    private void filterData(String keyword) {
        ObservableList<PaymentModeDTO> filteredData = FXCollections.observableArrayList();

        if (keyword.isEmpty()) {
            tcPaymentView.setItems(originalData);
            return;
        }

        for (PaymentModeDTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tcPaymentView.setItems(filteredData);
    }

    //Search Function to Search in the Table for columns
    private boolean matchesKeyword(PaymentModeDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        // Check if any of the columns contain the keyword
        return item.getPaymentMode().toLowerCase().contains(lowerCaseKeyword);

    }


    //Create Payment Mode API called
    public void createPaymentMode() {
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                APIClient apiClient = null;
                String paymentMode = tfPaymentCreateMode.getText();

                Map<String, String> map = new HashMap<>();
                map.put("id", id);
                map.put("payment_mode", paymentMode);

                HttpResponse<String> response;
                String formData = Globals.mapToStringforFormData(map);
                if (id.equalsIgnoreCase("")) {
                    //  response = APIClient.postFormDataRequest(formData, PAYMENT_MODE_CREATE_ENDPOINT);
                    apiClient = new APIClient(EndPoints.PAYMENT_MODE_CREATE_ENDPOINT, formData, RequestType.FORM_DATA);
//                    flagStatus = "create";
                    focusInd = "focus";

                } else {
                    //response = APIClient.postFormDataRequest(formData, PAYMENT_MODE_UPDATE_ENDPOINT);
                    apiClient = new APIClient(EndPoints.PAYMENT_MODE_UPDATE_ENDPOINT, formData, RequestType.FORM_DATA);
//                    flagStatus = "update";
                    focusInd = "update";
                    id="";
                    btnPaymentCreateSubmit.setText("Submit");
                }
                apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        // JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                        jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

                        message = jsonObject.get("message").getAsString();
                        if (jsonObject.get("responseStatus").getAsInt() == 409) {
                            if (jsonObject.get("responseStatus").getAsInt() == 409) {
                                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, input -> {
//                                    tfPaymentCreateMode.requestFocus();
                                });
                            } else {
                                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Failed to Create or Edit Payment Mode.", in -> {
                                });
                            }
                            tfPaymentCreateMode.setText("");
                        }

                        if (jsonObject.get("responseStatus").getAsInt() == 200) {
//                            AlertUtility.CustomCallback callback = (number) -> {
//
//                                if (number == 1) {
////                                    GlobalController.getInstance().addTabStatic(FRANCHISE_LIST_SLUG, false);
//                                }
//                            };

                            Stage stage2 = (Stage) spRootPaymentModePane.getScene().getWindow();
                            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                                AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
//                                    tfPaymentCreateMode.requestFocus();
//                                    Integer currentNoOfRows = observableList.size();
//                                    tcPaymentView.getSelectionModel().select(currentNoOfRows - 1);
//                                    tcPaymentView.scrollTo(currentNoOfRows - 1);
//                                    tcPaymentView.requestFocus();
                                });
//                                AlertUtility.AlertSuccess(stage2, "Success", message, callback);
//                                tfPaymentCreateMode.requestFocus();
                            } else {
//                                AlertUtility.AlertError(stage2, AlertUtility.alertTypeError, message, callback);
                                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Failed to Create or Edit Payment Mode.", in -> {
//                                    tftranxPurChallLstSearchField.requestFocus();
                                });
                            }
                            getPaymentModeData();
                            tfPaymentCreateMode.setText("");


                        }
                    }
                });

                apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        logger.error("Network API cancelled in createPaymentMode()" + workerStateEvent.getSource().getValue().toString());

                    }
                });
                apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        logger.error("Network API failed in createPaymentMode()" + workerStateEvent.getSource().getValue().toString());

                    }
                });
                apiClient.start();
                logger.debug("create payment mode data end ...");
            } else {
            }
        };
        if (id.equalsIgnoreCase("")) {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnSubmit, callback);
        } else {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnUpdate, callback);
        }
    }

    //Get Payment Mode API called
    public void getPaymentModeData() {
        APIClient apiClient = null;
        try {
            tcPaymentView.getItems().clear();
            //  HttpResponse<String> response = APIClient.getRequest(PAYMENT_MODE_LIST_ENDPOINT);
            apiClient = new APIClient(EndPoints.PAYMENT_MODE_LIST_ENDPOINT, "", RequestType.GET);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
//                    JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray responseObject = jsonObject.getAsJsonArray("responseObject");

                        if (responseObject.size() > 0) {
                            for (JsonElement element : responseObject) {
                                JsonObject item = element.getAsJsonObject();
                                String id = item.get("id").getAsString();
                                String paymentMode = item.get("payment_mode").getAsString();

                                observableList.add(new PaymentModeDTO(id, paymentMode));

                            }
                            tcPaymentMode.setCellValueFactory(new PropertyValueFactory<>("paymentMode"));
                            // Update originalData with new items
                            originalData.setAll(observableList);
                            tcPaymentView.setItems(observableList);

//                            ObservableList<ObservableList<StringProperty>> rows = tcPaymentView.getItems();

                            //******************************** Highlight on the Created/Edited Record in the List Start ********************************
//                            Integer currentNoOfRows = observableList.size();
//                            if (currentNoOfRows > 0 && id == "" && flagStatus.equals("create")) {
//                                tcPaymentView.getSelectionModel().select(currentNoOfRows - 1);
//                                tcPaymentView.scrollTo(currentNoOfRows - 1);
//                            }
//
//                            if (currentNoOfRows > 0 && id != "" && flagStatus.equals("update")) {
//                                tcPaymentView.getSelectionModel().select(Integer.parseInt(id) - 1);
//                                tcPaymentView.scrollTo(Integer.parseInt(id) - 1);
//                                id = "";
//                                btnPaymentCreateSubmit.setText("Submit");
//                            }
                            //?******************************** Highlight on the Created/Edited Record in the List End ********************************
                            tcPaymentView.refresh();


                        } else {
                            logger.error("responseObject is null");
                        }
                        //******************************** Highlight on the Created/Edited Record in the List Start ********************************
                        if (focusInd.equalsIgnoreCase("focus")) {
                            tcPaymentView.getSelectionModel().select(responseObject.size() - 1);
                            tcPaymentView.scrollTo(responseObject.size() - 1);
                            tcPaymentView.requestFocus();
                            focusInd = "";
                        } else if (focusInd.equalsIgnoreCase("update")) {
                            tcPaymentView.getSelectionModel().select(rowIndex);
                            tcPaymentView.scrollTo(rowIndex);
                            tcPaymentView.requestFocus();
                            focusInd = "";
                            btnPaymentCreateSubmit.setText("Submit");
                        }
                        //?******************************** Highlight on the Created/Edited Record in the List End ********************************
                    } else {
                        logger.error("Error in response");
                    }
                }
            });

            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getPaymentModeData()" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getPaymentModeData()" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.start();
            logger.debug("Get payment mode Data End...");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }

    }

    public void deletePaymentMode() {
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                APIClient apiClient = null;
                Map<String, String> map = new HashMap<>();
                map.put("id", id);

                HttpResponse<String> response;
                String formData = Globals.mapToStringforFormData(map);
                apiClient = new APIClient(EndPoints.PAYMENT_MODE_DELETE_ENDPOINT, formData, RequestType.FORM_DATA);

                apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        // JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                        jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

                        message = jsonObject.get("message").getAsString();

                        if (jsonObject.get("responseStatus").getAsInt() == 200) {
                            AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                                tfPaymentCreateMode.requestFocus();
                                id = "";
                            });
//                                AlertUtility.AlertSuccess(stage2, "Success", message, callback);
//                                tfPaymentCreateMode.requestFocus();
                        } else {
//                                AlertUtility.AlertError(stage2, AlertUtility.alertTypeError, message, callback);
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
//                                    tftranxPurChallLstSearchField.requestFocus();
                            });
                        }
                        getPaymentModeData();
                        tfPaymentCreateMode.setText("");

                    }
                });

                apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        logger.error("Network API cancelled in deletePaymentMode()" + workerStateEvent.getSource().getValue().toString());

                    }
                });
                apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        logger.error("Network API failed in deletePaymentMode()" + workerStateEvent.getSource().getValue().toString());

                    }
                });
                apiClient.start();
                logger.debug("delete payment mode data end ...");
            } else {
            }
        };
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnDelete, callback);
    }

    //Search Input function defined
    @FXML
    private void handleSearch() {
        String searchText = tfPaymentSearch.getText().toLowerCase();
        ObservableList<PaymentModeDTO> filteredItems = FXCollections.observableArrayList();
        tcPaymentView.setItems(filteredItems);
    }

    // Method to handle Enter key pressed event
//    @FXML
//    private void handleEnterPressed() {
//        PaymentModeDTO selectedItem = tcPaymentView.getSelectionModel().getSelectedItem();
//        if (selectedItem != null) {
//            id = selectedItem.getId();
//            tfPaymentCreateMode.setText(selectedItem.getPaymentMode());
//            btnPaymentCreateSubmit.setText("Update");
//            Platform.runLater(() -> {
//                tfPaymentCreateMode.requestFocus(); // Set focus back to tfFranchiseCreateRPincode
//            });
//
//        }
//    }

    @FXML
    private void handleDeletePressed() {
        PaymentModeDTO selectedItem = tcPaymentView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            id = selectedItem.getId();
            deletePaymentMode();
        }
    }

}
