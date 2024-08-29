package com.opethic.genivis.controller.master.ledger;

import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.Reports.AccountsLedgerReport2Controller;
import com.opethic.genivis.controller.commons.SwitchButton;
import com.opethic.genivis.controller.master.FranchiseListController;
import com.opethic.genivis.dto.FranchiseListDTO;
import com.opethic.genivis.dto.LedgerReport1DTO;
import com.opethic.genivis.dto.master.ledger.LedgerListDTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.GlobalController;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.FxmFileConstants.ACCOUNTS_LEDGER_REPORT2_LIST_SLUG;
import static com.opethic.genivis.utils.FxmFileConstants.MASTER_LDEGER_TRANX_REPORT_LIST_SLUG;
import static com.opethic.genivis.utils.Globals.franchiseListDTO;

public class LedgerListController implements Initializable {
    @FXML
    private Button btnLedgerCreate;

    @FXML
    private TableView<LedgerListDTO> tvLedgerList;

/*
    @FXML
    private TableColumn<LedgerListDTO, Void> tcAction;
*/

    @FXML
    private TableColumn<LedgerListDTO, SimpleDoubleProperty> tcCredit;

    @FXML
    private TableColumn<LedgerListDTO, SimpleDoubleProperty> tcDebit;

    @FXML
    private TableColumn<LedgerListDTO, SimpleStringProperty> tcFoundation;

    @FXML
    private TableColumn<LedgerListDTO, SimpleStringProperty> tcLedgerName;

    @FXML
    private TableColumn<LedgerListDTO, SimpleStringProperty> tcPrinciple;

    @FXML
    private TableColumn<LedgerListDTO, SimpleStringProperty> tcSubPrinciple;

    @FXML
    private TextField tfSearch;

    @FXML
    private SwitchButton switchBalance;
    @FXML
    private VBox vboxBalance;

    @FXML
    private BorderPane bpParent;

    private static final Logger loggerLedgerListController = LogManager.getLogger(LedgerListController.class);

    ObservableList<LedgerListDTO> row = FXCollections.observableArrayList();

    //? Highlight the Record Start
    public static boolean isNewLedgerCreated = false; // Flag for new creation
    public static Integer editedLedgerId = null; // ID for edited franchise
    //? Highlight the Record End

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        bpParent.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.DOWN && tfSearch.isFocused()) {
                tvLedgerList.getSelectionModel().select(0);
                tvLedgerList.requestFocus();
            }
        });

        switchBalance.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.SPACE) {
                switchBalance.toggle();
                event.consume();
            }
        });

        switchBalance.setParentBox(vboxBalance);
        switchBalance.switchOnProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("newValue" + newValue);
            if (newValue == false) {
//                System.out.println("I am in If Part");
                tvLedgerList.setItems(row);
//                tvLedgerList.filtered
            } else {
                tvLedgerList.setItems(row.filtered((v) -> v.getCr() > 0 || v.getDr() > 0));
//                System.out.println("I am in else  Part");
            }
        });

//        tfSearch.setOnKeyPressed(this::handleEnterKeyPressed);

        btnLedgerCreate.setOnAction(e -> {
            GlobalController.getInstance().addTabStatic("ledger-create", false);
//            AlertUtility.CustomCallback callback = number -> System.out.println("Callback message: " + number);
//            AlertUtility.AlertSuccess(AlertUtility.alertTypeSuccess,"Testing Msg",callback);
//            AlertUtility.AlertWarning(AlertUtility.alertTypeWarning, "Testing Msg", callback);
//            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you Sure? ", callback);
//            AlertUtility.AlertError(AlertUtility.alertTypeError, "Fields are missing? ", callback);
//            AlertUtility.AlertTimer(AlertUtility.alertTypeSuccess, "Product Created Successfully", 5);
        });
        Platform.runLater(() -> {
            tfSearch.requestFocus();
        });

        getLedgerList("");
        tfSearch.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String searchKey = tfSearch.getText().trim();
                if (searchKey.length() >= 0) {
                    getLedgerList(searchKey);
                } else if (searchKey.isEmpty()) {
                    getLedgerList("");
                }
            }
        });

        initLedgerListShortCutKeys();

//        enter buttons
        nodetraversalForNodeToToggle(tfSearch, switchBalance);
        nodetraversalForToggleToNode(switchBalance, btnLedgerCreate);
        tvLedgerList.requestFocus();

//        enter button clicked edit page
        tvLedgerList.addEventFilter(KeyEvent.ANY, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                Integer id = tvLedgerList.getSelectionModel().getSelectedItem().getId();
                LedgerListController.editedLedgerId = id; //? Set the ID for editing
                GlobalController.getInstance().addTabStaticWithParam("ledger-edit", false, id);
            }
        });
//        double clicked edit page
        tvLedgerList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                //? Highlight
                Integer id = tvLedgerList.getSelectionModel().getSelectedItem().getId();
                LedgerListController.editedLedgerId = id; //? Set the ID for editing
                GlobalController.getInstance().addTabStaticWithParam("ledger-edit", false, id);
            }
        });
    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            bpParent.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            bpParent.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            bpParent.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            bpParent.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            bpParent.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            bpParent.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            bpParent.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
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

    private void nodetraversalForToggleToNode(SwitchButton current_node, Node next_node) {
        current_node.getSwitchBtn().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                next_node.requestFocus();
                event.consume();
            }
        });
    }

    /**
     * @ImplNote : This function will enables the all the shortcut keys like CTRL+N -> Create new , CTRL+E -> Edit Selected Row, CTRL+V -> View Selected && CTRL+D -> Delete Selected Row & also for table it will apply
     * @author : kirankumar.gadagi
     */
    private void initLedgerListShortCutKeys() {
//        bpParent.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
//            if (event.getCode() == KeyCode.ENTER) {
//                KeyEvent newEvent = new KeyEvent(
//                        null,
//                        null,
//                        KeyEvent.KEY_PRESSED,
//                        "",
//                        "\t",
//                        KeyCode.TAB,
//                        event.isShiftDown(),
//                        event.isControlDown(),
//                        event.isAltDown(),
//                        event.isMetaDown()
//                );
//                /*if (event.getTarget() instanceof Button) {
//                    Button btnCmp = (Button) event.getTarget();
//                    if (btnCmp != null) {
//                        if (btnCmp.getId() != null && btnCmp.getId().equalsIgnoreCase("btnSwitch")) {
//                        } else {
//                            btnCmp.fire();
//                        }
//                        Event.fireEvent(event.getTarget(), newEvent);
//                    }
//                }
//                else {
//                }*/
//                Event.fireEvent(event.getTarget(), newEvent);
//                event.consume();
//            } else if (event.getCode() == KeyCode.N && event.isControlDown()) {
//                GlobalController.getInstance().addTabStatic("ledger-create", false);
//            }
//        });

        bpParent.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.N && event.isControlDown()) {
                GlobalController.getInstance().addTabStatic("ledger-create", false);
            }
        });

        tfSearch.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.DOWN) {
                tvLedgerList.getSelectionModel().select(0);
                tvLedgerList.requestFocus();
            }
        });
        tvLedgerList.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.N && e.isControlDown()) {
                GlobalController.getInstance().addTabStatic("ledger-create", false);
            } else if (e.getCode() == KeyCode.E && e.isControlDown()) {
                if (tvLedgerList.getSelectionModel().getSelectedItem() != null) {
                    Integer id = tvLedgerList.getSelectionModel().getSelectedItem().getId();
                    LedgerListController.editedLedgerId = id; //? Set the ID for editing
                    GlobalController.getInstance().addTabStaticWithParam("ledger-edit", false, id);
                }
            } else if (e.getCode() == KeyCode.V && e.isControlDown()) {
                if (tvLedgerList.getSelectionModel().getSelectedItem() != null) {
                    Integer id = tvLedgerList.getSelectionModel().getSelectedItem().getId();
                    LedgerListController.editedLedgerId = id; //? Set the ID for editing
                    GlobalController.getInstance().addTabStaticWithParam(MASTER_LDEGER_TRANX_REPORT_LIST_SLUG, false, id);
                }
            } else if (e.getCode() == KeyCode.D && e.isControlDown()) {
                LedgerListDTO selectedLedgerDTO = tvLedgerList.getSelectionModel().getSelectedItem();
                if (selectedLedgerDTO != null) {
                    if (selectedLedgerDTO.isDefault_ledger() == false) {
                        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you Sure?", input -> {
                            if (input == 1) {
                                Integer id = selectedLedgerDTO.getId();
                                deleteLedgerAPICall(id);
                            }
                        });
                    } else {
                        AlertUtility.AlertError(AlertUtility.alertTypeError, "This is Default ledger. you can not delete it.", input -> {
                        });
                    }

                }
            } else if (e.getCode() == KeyCode.BACK_SPACE) {
                tfSearch.requestFocus();
            }
        });
    }

    /**
     * @param id accept the ledger Id
     * @author kirankumar.gadagi
     * @ImplNote This function will call API for deleting ledger using ledger id.
     */
    private void deleteLedgerAPICall(Integer id) {
        try {


            Map<String, String> map = new HashMap<>();
            map.put("id", "" + id);
            String formData = Globals.mapToStringforFormData(map);
            // productGetByIdResponse = APIClient.postFormDataRequest(formData, EndPoints.GET_PRODUCT_BY_ID_ENDPOINT);
            APIClient apiClient = new APIClient(EndPoints.DELETE_LEDGER, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    String response = workerStateEvent.getSource().getValue().toString();
                    System.out.println("Delete Ledger => " + response);
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.getInt("responseStatus") == 200) {
                        AlertUtility.AlertSuccess(AlertUtility.alertTypeSuccess, resObj.getString("message"), input -> {
                            getLedgerList("");
                        });
                    } else {
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, resObj.getString("message"), input -> {
                        });
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    loggerLedgerListController.error("Network API cancelled in deleteLedgerAPICall()" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    loggerLedgerListController.error("Network API failed in deleteLedgerAPICall()" + workerStateEvent.getSource().getValue().toString());
                }
            });

            apiClient.start();
        } catch (Exception e) {
            loggerLedgerListController.error("Exception in deleteLedgerAPICall():" + Globals.getExceptionString(e));
        }
    }


    private void handleEnterKeyPressed(KeyEvent keyEvent) {
        Node source = (Node) keyEvent.getSource();
        if (keyEvent.getCode() == KeyCode.ENTER) {
            switch (source.getId()) {
                case "tfSearch":
                    Platform.runLater(() -> {
                        btnLedgerCreate.requestFocus();
                    });
                    break;
                default:
                    break;
            }
        }
    }

    private void getLedgerList(String searchKey) {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("pageNo", "1");
            body.put("pageSize", "100");
            body.put("searchText", searchKey);
            String requestBody = Globals.mapToString(body);
            HttpResponse<String> response = APIClient.postJsonRequest(requestBody, EndPoints.ledgerList);
            JSONObject resObj = new JSONObject(response.body());
            row.clear();
            if (resObj.getInt("responseStatus") == 200) {
                JSONObject respObj = resObj.getJSONObject("responseObject");
                JSONArray respArr = respObj.getJSONArray("data");
                if (respArr.length() > 0) {
                    for (Object mEle : respArr) {
                        JSONObject obj = new JSONObject(mEle.toString());
                        row.add(new LedgerListDTO(obj.getInt("id"), obj.getString("foundations_name"), obj.getString("principle_name"), obj.get("subprinciple_name") != null ? obj.getString("subprinciple_name") : "", obj.getBoolean("default_ledger"), obj.getString("ledger_form_parameter_slug"), obj.getString("ledger_name"), obj.getDouble("cr"), obj.getDouble("dr")));
                    }

                    tcCredit.setCellValueFactory(new PropertyValueFactory<LedgerListDTO, SimpleDoubleProperty>("cr"));
                    tcDebit.setCellValueFactory(new PropertyValueFactory<LedgerListDTO, SimpleDoubleProperty>("dr"));
                    tcFoundation.setCellValueFactory(new PropertyValueFactory<LedgerListDTO, SimpleStringProperty>("foundations_name"));
                    tcLedgerName.setCellValueFactory(new PropertyValueFactory<LedgerListDTO, SimpleStringProperty>("ledger_name"));
                    tcPrinciple.setCellValueFactory(new PropertyValueFactory<LedgerListDTO, SimpleStringProperty>("principle_name"));
                    tcSubPrinciple.setCellValueFactory(new PropertyValueFactory<LedgerListDTO, SimpleStringProperty>("subprinciple_name"));
                    /*tcAction.setCellFactory(param -> {
                        final TableCell<LedgerListDTO, Void> cell = new TableCell<>() {
                            private ImageView delImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/del.png"))));
                            private ImageView edtImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/edt.png"))));
                            private ImageView viewImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/view.png"))));

                            {
                                delImg.setFitHeight(28);
                                delImg.setFitWidth(28);
                                edtImg.setFitHeight(28);
                                edtImg.setFitWidth(28);
                                viewImg.setFitHeight(28);
                                viewImg.setFitWidth(28);
                            }

                            private final Button delButton = new Button("", delImg);
                            private final Button edtButton = new Button("", edtImg);
                            private final Button viewButton = new Button("", viewImg);

                            {
                                edtButton.getStyleClass().add("btn-ledger-action");
                                edtButton.setOnAction(actionEvent -> {
                                    Integer id = row.get(getIndex()).getId();
                                    GlobalController.getInstance().addTabStaticWithParam("ledger-edit", false, id);
                                });
                                viewButton.getStyleClass().add("btn-ledger-action");
                                viewButton.setOnAction(actionEvent -> {
                                    Integer id = row.get(getIndex()).getId();
                                    GlobalController.getInstance().addTabStaticWithParam(MASTER_LDEGER_TRANX_REPORT_LIST_SLUG, false, id);
                                });
                                delButton.getStyleClass().add("btn-ledger-action");
                                delButton.setOnAction((actionEvent -> {
                                    LedgerListDTO selectedLedgerDTO = row.get(getIndex());
                                    if (selectedLedgerDTO.isDefault_ledger() == false) {
                                        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you Sure?", input -> {
                                            if (input == 1) {
                                                Integer id = selectedLedgerDTO.getId();
                                                deleteLedgerAPICall(id);
                                            }
                                        });
                                    } else {
                                        AlertUtility.AlertError(AlertUtility.alertTypeError, "This is Default ledger. you can not delete it.", input -> {
                                        });
                                    }
                                }));
                            }

                            HBox hbActions = new HBox();

                            {
                                hbActions.getChildren().add(edtButton);
                                hbActions.getChildren().add(viewButton);
                                hbActions.getChildren().add(delButton);
                                hbActions.setSpacing(5.0);
                                hbActions.setAlignment(Pos.CENTER);
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

                    tvLedgerList.setItems(row);

                    //******************************** Highlight on the Created/Edited Record in the List Start ********************************
                    if (LedgerListController.isNewLedgerCreated) {
//                        tvLedgerList.getSelectionModel().selectFirst();
//                        tvLedgerList.scrollTo(tvLedgerList.getItems().size() -1);
                        tvLedgerList.getSelectionModel().selectFirst();
                        tvLedgerList.requestFocus();
                        LedgerListController.isNewLedgerCreated = false; // Reset the flag
                    } else if (LedgerListController.editedLedgerId != null) {
                        for (LedgerListDTO ledger : row) {
                            if (ledger.getId() == LedgerListController.editedLedgerId) {
                                tvLedgerList.getSelectionModel().select(ledger);
                                tvLedgerList.scrollTo(ledger);
                                LedgerListController.editedLedgerId = null; // Reset the ID
                                break;
                            }
                        }
                    }
                    //******************************** Highlight on the Created/Edited Record in the List End ********************************

                } else {
                    tvLedgerList.setItems(row);
                }
            } else {
                String msg = resObj.getString("message");
                AlertUtility.CustomCallback callback = number -> {
                    tfSearch.requestFocus();
                };
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
            }
        } catch (Exception e) {
            loggerLedgerListController.error("Exception getLedgerList() : " + Globals.getExceptionString(e));
        }
    }
}
