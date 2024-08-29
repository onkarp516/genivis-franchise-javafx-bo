package com.opethic.genivis.controller.dashboard;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.CompanyCountryDTO;
import com.opethic.genivis.dto.dashboard.DashboardProductStockDTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.Globals;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    private static final Logger logger = LogManager.getLogger(DashboardController.class);

    private String responseBody, message, listCount;
    private JsonObject jsonObject = null;

    @FXML
    private Label lblRevenueToday, lblRevenueThisWeek, lblRevenueThisMonth, lblCollectionToday,
            lblCollectionThisWeek, lblCollectionThisMonth, lblReceivableToday, lblReceivableThisWeek,
            lblReceivableThisMonth, lblIncomeThisMonth, lblExpensesThisMonth, lblAssetTotalAsset, lblLiabilityTotalLiability;
    @FXML
    TableView<DashboardProductStockDTO> tblvDashboardProductStock;
    @FXML
    TableColumn<DashboardProductStockDTO, String> tblcDashboardProductName, tblcDashboardBrand, tblcDashboardClosingStock;
    @FXML
    private BorderPane bpRootPane;

    //    ObservableList<DashboardProductStockDTO> dashboardProStkList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tblcDashboardProductName.prefWidthProperty().bind(tblvDashboardProductStock.widthProperty().multiply(0.4));
        tblcDashboardBrand.prefWidthProperty().bind(tblvDashboardProductStock.widthProperty().multiply(0.3));
        tblcDashboardClosingStock.prefWidthProperty().bind(tblvDashboardProductStock.widthProperty().multiply(0.3));

        //TODO: resizing the table columns as per the resolution.
        tblvDashboardProductStock.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        //TODO: get the data of product and stock.
        getDashboardProductStockData("");
        //TODO: get the dashboard data.
        getDashboardData();
    }

    //TODO: get the dashboard data
    public void getDashboardData() {
        APIClient apiClient = null;
        try {
            logger.debug("Get Country Data Started...");
            apiClient = new APIClient(EndPoints.GET_DASHBOARD_DATA, "", RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonObject responseObject = jsonObject.get("result").getAsJsonObject();

                        JsonObject revenueObject = responseObject.get("RevenueList").getAsJsonArray().get(0).getAsJsonObject();

                        lblRevenueToday.setText(revenueObject.get("today_revenue_amt").getAsString());
                        lblRevenueThisWeek.setText(revenueObject.get("weekly_revenue_amt").getAsString());
                        lblRevenueThisMonth.setText(revenueObject.get("monthly_revenue_amt").getAsString());

                        JsonObject CollectAmtObject = responseObject.get("CollectAmt").getAsJsonArray().get(0).getAsJsonObject();

                        lblCollectionToday.setText(CollectAmtObject.get("today_receipt_amt").getAsString());
                        lblCollectionThisWeek.setText(CollectAmtObject.get("weekly_receipt_amt").getAsString());
                        lblCollectionThisMonth.setText(CollectAmtObject.get("month_receipt_amt").getAsString());

                        JsonObject ReceivableAmtObject = responseObject.get("ReceivableAmt").getAsJsonArray().get(0).getAsJsonObject();

                        lblReceivableToday.setText(ReceivableAmtObject.get("today_receivable_amt").getAsString());
                        lblReceivableThisWeek.setText(ReceivableAmtObject.get("weekly_receivable_amt").getAsString());
                        lblReceivableThisMonth.setText(ReceivableAmtObject.get("monthly_receivable_amt").getAsString());

                        JsonObject ExpensesObject = responseObject.get("Expenses").getAsJsonArray().get(0).getAsJsonObject();

                        lblExpensesThisMonth.setText(ExpensesObject.get("monthly_expensive_amt").getAsString());

                        JsonObject AssetsAmtObject = responseObject.get("AssetsAmt").getAsJsonArray().get(0).getAsJsonObject();

                        lblAssetTotalAsset.setText(AssetsAmtObject.get("monthly_assets_amt").getAsString());

                        JsonObject LiabilitiesAmtObject = responseObject.get("LiabilitiesAmt").getAsJsonArray().get(0).getAsJsonObject();

                        lblLiabilityTotalLiability.setText(LiabilitiesAmtObject.get("monthly_liabilities_amt").getAsString());

                    } else {
                        //TODO : use logger for alert messages
                        AlertUtility.AlertDialogForError("WARNING", "Failed To Load Data", input -> {
                            if (input) {
                            }
                        });
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getCountry()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getCountry()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO : use logger for alert messages
            AlertUtility.AlertDialogForError("WARNING", "Failed to Connect Server", input -> {
                if (input) {
                }
            });
        } finally {
            apiClient = null;
        }
    }

    //TODO: Get and Set the  List Data of Dashboard product and stock.
    public void getDashboardProductStockData(String searchKey) {
        APIClient apiClient = null;
        try {
            logger.debug("Get Franchise Data Started...");
//            String requestBody = Globals.mapToString(body);
            tblvDashboardProductStock.getItems().clear();
//            HttpResponse<String> response = APIClient.getRequest(EndPoints.FRANCHISE_LIST_ENDPOINT);
            logger.debug("Get Dashboard Product and Stock Data Started...");
            Map<String, String> body = new HashMap<>();
            body.put("pageNo", "1");
            body.put("pageSize", "50");
            body.put("searchText", searchKey);
            body.put("sort", "{ \\\"colId\\\": null, \\\"isAsc\\\": true }");
            String requestBody = Globals.mapToString(body);
            apiClient = new APIClient(EndPoints.GET_DASHBOARD_PRODUCT_AND_STOCK_DATA, requestBody, RequestType.JSON);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    ObservableList<DashboardProductStockDTO> observableList = FXCollections.observableArrayList();
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonObject responseObject = jsonObject.get("responseObject").getAsJsonObject();
                        JsonArray responseArray = responseObject.get("data").getAsJsonArray();
                        if (responseArray.size() > 0) {
                            for (JsonElement element : responseArray) {
                                Long i = 0L;
                                JsonObject item = element.getAsJsonObject();
                                String index = String.valueOf(i + 1);
                                String id = item.get("id").getAsString();
                                String product_name = item.get("product_name").getAsString();
                                String brand = item.get("brand").getAsString();
                                String closing_stocks = item.get("closing_stocks").getAsString();

                                observableList.add(new DashboardProductStockDTO(id, product_name, brand,
                                        closing_stocks)
                                );
                            }
                            tblcDashboardProductName.setCellValueFactory(new PropertyValueFactory<>("product_name"));
                            tblcDashboardBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
                            tblcDashboardClosingStock.setCellValueFactory(new PropertyValueFactory<>("closing_stocks"));
//                            tblcDashboardClosingStock.setStyle("-fx-alignment: CENTER;");


                            tblvDashboardProductStock.setItems(observableList);

                        } else {
                            //TODO : use logger for alert messages
//                            AlertUtility.CustomCallback callback = (number) -> {
//                                if (number == 0) {
//                                }
//                            };
//                            Stage stage = (Stage) bpRootPane.getScene().getWindow();
//                            AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Failed to Load Data ", callback);

                        }
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
                    logger.error("Network API cancelled in getDashboardData()" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getDashboardData()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            logger.debug("Get Franchise Data End...");
        } catch (Exception e) {
            AlertUtility.CustomCallback callback = (number) -> {
                if (number == 0) {
//                    tffieldSearch.requestFocus();
                }
            };
            Stage stage = (Stage) bpRootPane.getScene().getWindow();
            AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Falied to connect server! ", callback);

            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }
}
