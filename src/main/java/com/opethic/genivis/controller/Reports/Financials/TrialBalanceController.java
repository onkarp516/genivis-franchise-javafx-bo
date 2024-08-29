package com.opethic.genivis.controller.Reports.Financials;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.controller.master.CompanyListController;
import com.opethic.genivis.dto.CompanyListDTO;
import com.opethic.genivis.dto.Reports.Financials.TrialBalanceListDTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class TrialBalanceController  implements Initializable {

    @FXML
    private Rectangle rectangle;
    private boolean isRectangleVisible = false;
    private static final Logger logger = LogManager.getLogger(CompanyListController.class);
    private JsonObject jsonObject = null;

    @FXML
    private BorderPane bpRootPane;
    @FXML
    private TableView<TrialBalanceListDTO> tblvTrialBalanceList;

    @FXML
    private TableColumn<TrialBalanceListDTO, String> tblcTrialBalanceParticulars,tblcTrialBalanceOpenBalDebit,tblcTrialBalanceOpenBalCredit,tblcTrialBalanceTranxDebit,tblcTrialBalanceTranxCredit,tblcTrialBalanceCloseBalDebit,tblcTrialBalanceCloseBalCredit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO: resizing the table columns as per the resolution.
        tblvTrialBalanceList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        tblcTrialBalanceParticulars.prefWidthProperty().bind(tblvTrialBalanceList.widthProperty().multiply(0.40));
        tblcTrialBalanceOpenBalDebit.prefWidthProperty().bind(tblvTrialBalanceList.widthProperty().multiply(0.10));
        tblcTrialBalanceOpenBalCredit.prefWidthProperty().bind(tblvTrialBalanceList.widthProperty().multiply(0.10));
        tblcTrialBalanceTranxDebit.prefWidthProperty().bind(tblvTrialBalanceList.widthProperty().multiply(0.10));
        tblcTrialBalanceTranxCredit.prefWidthProperty().bind(tblvTrialBalanceList.widthProperty().multiply(0.10));
        tblcTrialBalanceCloseBalDebit.prefWidthProperty().bind(tblvTrialBalanceList.widthProperty().multiply(0.10));
        tblcTrialBalanceCloseBalCredit.prefWidthProperty().bind(tblvTrialBalanceList.widthProperty().multiply(0.10));

//        System.out.println("Hello");

        getTrialBalanceData();
    }

    //todo: Function to load the all company list
    public void getTrialBalanceData() {
        APIClient apiClient = null;
        try {
            tblvTrialBalanceList.getItems().clear();
            apiClient = new APIClient(EndPoints.TRIAL_BALANCE_LIST, "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    ObservableList<TrialBalanceListDTO> trialBalanceList = FXCollections.observableArrayList();
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray responseObject = jsonObject.getAsJsonArray("responseList");
//                        System.out.println("response trial balance"+responseObject);

//                        if (responseObject.size() > 0) {
//                            for (JsonElement element : responseObject) {
//                                Long i = 0L;
//                                JsonObject item = element.getAsJsonObject();
//                                String id = item.get("id").getAsString();
//                                String particulars = item.get("ledger_name").getAsString();
//                                String openBalDebitAmt = item.get("dr").getAsString();
//                                String openBalCreditAmt = item.get("cr").getAsString();
//                                String tranxDebitAmt = item.get("dr").getAsString();
//                                String tranxCreditAmt = item.get("cr").getAsString();
//                                String closeBalDebitAmt = item.get("dr").getAsString();
//                                String closeBalCreditAmt = item.get("cr").getAsString();
//                                String extra1 = item.get("foundations_name").getAsString();
//                                String extra2 = item.get("principle_name").getAsString();
//                                String extra3 = item.get("default_ledger").getAsString();
//                                String extra4 = item.get("unique_code").getAsString();
//
//                                trialBalanceList.add(new TrialBalanceListDTO(id, particulars, openBalDebitAmt,
//                                        openBalCreditAmt, tranxDebitAmt, tranxCreditAmt,closeBalDebitAmt,closeBalCreditAmt,extra1,extra2,extra3,extra4)
//                                );
//                            }
//                            tblcTrialBalanceParticulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
//                            tblcTrialBalanceOpenBalDebit.setCellValueFactory(new PropertyValueFactory<>("openBalDebitAmt"));
//                            tblcTrialBalanceOpenBalCredit.setCellValueFactory(new PropertyValueFactory<>("openBalCreditAmt"));
////                            tblcTrialBalanceTranxDebit.setCellValueFactory(new PropertyValueFactory<>("corporateAddress"));
////                            tblcTrialBalanceTranxCredit.setCellValueFactory(new PropertyValueFactory<>("corporateAddress"));
////                            tblcTrialBalanceCloseBalDebit.setCellValueFactory(new PropertyValueFactory<>("companyMobileNo"));
////                            tblcTrialBalanceCloseBalCredit.setCellValueFactory(new PropertyValueFactory<>("companyMobileNo"));
//
//                            tblvTrialBalanceList.setItems(trialBalanceList);
//
//                        } else {
//                            //TODO : use logger for alert messages
//                            AlertUtility.CustomCallback callback = (number) -> {
//                                if (number == 0) {
//                                }
//                            };
//                            Stage stage = (Stage) bpRootPane.getScene().getWindow();
//                            AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Failed to Load Data ", callback);
//
//                        }
                    } else {
                        //TODO : use logger for alert messages
                        AlertUtility.CustomCallback callback = (number) -> {
                            if (number == 0) {
                            }
                        };
                        Stage stage = (Stage) bpRootPane.getScene().getWindow();
                        AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Falied to connect server! ", callback);

                    }


                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getCompanyList()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getCompanyList()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            AlertUtility.CustomCallback callback = (number) -> {
                if (number == 0) {
                }
            };
            Stage stage = (Stage) bpRootPane.getScene().getWindow();
            AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Falied to connect server! ", callback);

            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    @FXML
    private void toggleDivVisibility() {
        System.out.println("ssasasa"+isRectangleVisible);
        isRectangleVisible = !isRectangleVisible;
        rectangle.setVisible(isRectangleVisible);
    }
}
