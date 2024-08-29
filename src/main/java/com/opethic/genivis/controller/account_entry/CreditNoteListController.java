package com.opethic.genivis.controller.account_entry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.dto.AreaMasterDTO;
import com.opethic.genivis.dto.CreditNoteListDTO;
import com.opethic.genivis.dto.PatientDTO;
import com.opethic.genivis.dto.PurchaseChallanDTO;
import com.opethic.genivis.dto.reqres.creditNote.CreditNoteLastRecordResponse;
import com.opethic.genivis.dto.reqres.payment.PaymentListDTO;
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
import javafx.geometry.Pos;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.Position;
import java.io.IOException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.FxmFileConstants.*;

public class CreditNoteListController implements Initializable {

    @FXML
    private BorderPane bpCreditNoteRootPane;
    @FXML
    private TableView tblvCreditNoteListCreditList;
    @FXML
    private TableColumn tblcCreditListDebitNoteNo,tblcCreditListTransactionDate,tblcCreditListSupplierName,tblcCreditListNarration,
            tblcCreditListTotalAmount,tblcCreditListAction;

    @FXML
    private Button btnCreditNoteListCreate;
    @FXML
    private TextField tfCreditNoteSearchField;
    String message = "";
    private JsonObject jsonObject = null;

    public static final Logger creditNoteListLogger = LoggerFactory.getLogger(PaymentCreateController.class);

    //? Highlight the Record Start
    public static boolean isNewCreditNoteCreated = false; // Flag for new creation
    public static String editedCreditNoteId = null; // ID for edited Sales Order
    //? Highlight the Record End
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        Platform.runLater(() -> {
            //Focus on the First Field of the Page
            tfCreditNoteSearchField.requestFocus();
        });
        //Redirect To Create CreditNote Page
        btnCreditNoteListCreate.setOnAction( event -> GlobalController.getInstance().addTabStatic(CREDIT_NOTE_CREATE_SLUG,false));

        // Amount Alignment on Right Side Code
        tblcCreditListTotalAmount.setStyle("-fx-alignment: CENTER-RIGHT;");

        // CreditNoteTableResponsive Method
        creditNoteListResponsive();

        //All CreditNotes List API Integration
        getAllCreditNotesList();

        // OnDoubleClick In Edit Page of Particular Credit Note Screen
        tblvCreditNoteListCreditList.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                editCreditNote();
            }
        });
        initialEnterMethod();
    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            bpCreditNoteRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            bpCreditNoteRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            bpCreditNoteRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            bpCreditNoteRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            bpCreditNoteRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            bpCreditNoteRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            bpCreditNoteRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    // CreditNoteTableResponsive Method
    public void creditNoteListResponsive() {
        tblvCreditNoteListCreditList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        tblcCreditListDebitNoteNo.prefWidthProperty().bind(tblvCreditNoteListCreditList.widthProperty().multiply(0.2));
//        tblcCreditListTransactionDate.prefWidthProperty().bind(tblvCreditNoteListCreditList.widthProperty().multiply(0.15));
//        tblcCreditListSupplierName.prefWidthProperty().bind(tblvCreditNoteListCreditList.widthProperty().multiply(0.225));
//        tblcCreditListNarration.prefWidthProperty().bind(tblvCreditNoteListCreditList.widthProperty().multiply(0.225));
//        tblcCreditListTotalAmount.prefWidthProperty().bind(tblvCreditNoteListCreditList.widthProperty().multiply(0.13));
//        tblcCreditListAction.prefWidthProperty().bind(tblvCreditNoteListCreditList.widthProperty().multiply(0.07));
    }

    // Getting all the Credit Notes
    private void getAllCreditNotesList() {
        APIClient apiClient=null;
        try {
            creditNoteListLogger.debug("Get getAllCreditNotesList Started...");
            tblvCreditNoteListCreditList.getItems().clear();
            apiClient = new APIClient(EndPoints.getAllCreditNoteList, "", RequestType.GET);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    ObservableList<CreditNoteListDTO> observableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        creditNoteListLogger.debug("Get getAllCreditNotesList Success...");
                        JsonArray responseObject = jsonObject.getAsJsonArray("data");
                        if (responseObject.size() > 0) {
                            for (JsonElement element : responseObject) {
                                JsonObject item = element.getAsJsonObject();
                                String id = item.get("id").getAsString();
                                String credit_note_no = item.get("credit_note_no").getAsString();
                                String transaction_dt = DateConvertUtil.convertDispDateFormat(item.get("transaction_dt").getAsString());
                                String narration = item.get("narration").getAsString();
                                String ledger_name = item.get("ledger_name").getAsString();
                                String total_amount = item.get("total_amount").getAsString();
                                observableList.add(new CreditNoteListDTO(id, credit_note_no, transaction_dt, ledger_name, narration, total_amount));
                            }

                            tblcCreditListDebitNoteNo.setCellValueFactory(new PropertyValueFactory<>("creditNoteNo"));
                            tblcCreditListTransactionDate.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
                            tblcCreditListSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
                            tblcCreditListNarration.setCellValueFactory(new PropertyValueFactory<>("narration"));
                            tblcCreditListTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
//                            tblcCreditListAction.setCellFactory(param -> {
//                                final TableCell<CreditNoteListDTO, String> cell = new TableCell<>() {
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
//                                            Integer creditNotId = Integer.valueOf(getTableView().getItems().get(getIndex()).getId());
//                                            GlobalController.getInstance().addTabStaticWithParam(CREDIT_NOTE_EDIT_SLUG,false, creditNotId);
//                                        });
//                                    }
//
//                                    {
//                                        delButton.setOnAction(actionEvent -> {
//                                            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you Sure?", input -> {
//                                                if (input == 1) {
//                                                    int credntId = Integer.parseInt(observableList.get(getIndex()).getId());
//                                                    deleteAPICall(credntId);
//                                                }
//                                            });
//                                        });
//                                    }
//
//                                    HBox hbActions = new HBox();
//
//                                    {
//                                        hbActions.getChildren().add(edtButton);
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
                            tblvCreditNoteListCreditList.setItems(observableList);

                            //******************************** Highlight on the Created/Edited Record in the List Start ********************************

                            if (CreditNoteListController.isNewCreditNoteCreated) {
                                tblvCreditNoteListCreditList.getSelectionModel().selectLast();
                                tblvCreditNoteListCreditList.scrollTo(tblvCreditNoteListCreditList.getItems().size() - 1);
                                CreditNoteListController.isNewCreditNoteCreated = false; // Reset the flag
                            } else if (CreditNoteListController.editedCreditNoteId != null) {
                                for (CreditNoteListDTO creditNote : observableList) {
                                    if (creditNote.getId().equals(CreditNoteListController.editedCreditNoteId)) {
                                        tblvCreditNoteListCreditList.getSelectionModel().select(creditNote);
                                        tblvCreditNoteListCreditList.scrollTo(creditNote);
                                        CreditNoteListController.editedCreditNoteId = null; // Reset the ID
                                        break;
                                    }
                                }
                            }
                            //******************************** Highlight on the Created/Edited Record in the List End ********************************

                        } else {
                            creditNoteListLogger.debug("Get getAllCreditNotesList responseObject is null...");
                        }
                    }else {
                        creditNoteListLogger.debug("Get getAllCreditNotesList Error in response...");
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    creditNoteListLogger.error("Network API cancelled in getAllCreditNotesList()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    creditNoteListLogger.error("Network API cancelled in getAllCreditNotesList()" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.start();
            creditNoteListLogger.debug("Get getAllCreditNotesList end");

        } catch (Exception e) {
            e.printStackTrace();
            creditNoteListLogger.error("Exception getAllCreditNotesList() : " + Globals.getExceptionString(e));
        }finally {
            apiClient=null;
        }
    }

    // OnCLick of Delete Icon Deleting the Credit Note
    private void deleteAPICall(Integer id) {
        APIClient apiClient=null;
        try {
            creditNoteListLogger.debug("Get deleteAPICall Started...");
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(id));
            String formData = Globals.mapToStringforFormData(map);
            apiClient = new APIClient(EndPoints.deleteCreditNoteEntry, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    System.out.println("jsonObject ::: >>"+jsonObject);
                    message=jsonObject.get("message").getAsString();
                    AlertUtility.CustomCallback callback = (number) -> {
                        if (number == 1) {
                        }
                    };
                    Stage stage2 = (Stage) bpCreditNoteRootPane.getScene().getWindow();
                    if(jsonObject.get("responseStatus").getAsInt()==200){
                        AlertUtility.AlertSuccess(stage2,"Success",message,callback);
                        getAllCreditNotesList();
                        tfCreditNoteSearchField.requestFocus();
                    }
                    else{
                        AlertUtility.AlertError(stage2,AlertUtility.alertTypeError, message, callback);
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    creditNoteListLogger.error("Network API cancelled in deleteAPICall()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    creditNoteListLogger.error("Network API cancelled in deleteAPICall()" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.start();
            creditNoteListLogger.debug("Get deleteAPICall end");

        } catch (Exception e) {
            e.printStackTrace();
            creditNoteListLogger.error("Exception deleteAPICall() : " + Globals.getExceptionString(e));
        }
        finally {
            apiClient=null;
        }
    }
    private void initialEnterMethod() {
        bpCreditNoteRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("create")) {
                } else {
                    editCreditNote();
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
            }else if (event.getCode() == KeyCode.N && event.isControlDown()) {
                btnCreditNoteListCreate.fire();
            } else if (event.getCode() == KeyCode.DOWN && tfCreditNoteSearchField.isFocused()) {
                tblvCreditNoteListCreditList.getSelectionModel().select(0);
                tblvCreditNoteListCreditList.requestFocus();
            } else if (event.getCode() == KeyCode.E && event.isControlDown()) {
                editCreditNote();
            }else if (event.getCode() == KeyCode.D && event.isControlDown()) {
                handleCtrlDPressed();
            }
        });
    }

    // OnDoubleClick In Edit Page of Particular Credit Note Screen
    private void editCreditNote(){
        CreditNoteListDTO selectedItem = (CreditNoteListDTO) tblvCreditNoteListCreditList.getSelectionModel().getSelectedItem();
        if(selectedItem!=null) {
            Integer creditNoteId = Integer.valueOf(selectedItem.getId());
            //? Highlight
            CreditNoteListController.editedCreditNoteId = creditNoteId.toString();
            GlobalController.getInstance().addTabStaticWithParam(CREDIT_NOTE_EDIT_SLUG, false, creditNoteId);
        }
    }

    private void handleCtrlDPressed() {
        CreditNoteListDTO selectedItem = (CreditNoteListDTO) tblvCreditNoteListCreditList.getSelectionModel().getSelectedItem();
        if(selectedItem!=null) {
            Integer creditNoteId = Integer.valueOf(selectedItem.getId());
            deleteAPICall(creditNoteId);
        }
    }



}
