package com.opethic.genivis.controller.master;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.dto.CompanyListDTO;
import com.opethic.genivis.dto.FranchiseListDTO;
import com.opethic.genivis.dto.PurchaseOrderDTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.GlobalController;
import com.opethic.genivis.utils.GlobalSadminController;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.FxmFileConstants.*;
import static com.opethic.genivis.utils.Globals.*;
import static com.opethic.genivis.utils.Globals.areaHeadListDTO;


public class CompanyListController implements Initializable {

    @FXML
    private TableView<CompanyListDTO> tblvCompanyList;
    private JsonObject jsonObject = null;
    private static final Logger logger = LogManager.getLogger(CompanyListController.class);
    @FXML
    private BorderPane bpCmpListRoot;
    @FXML
    private TableColumn<CompanyListDTO, String> tblcCmpLstCompanyName, tblcCmpLstCompanyCode, tblcCmpLstCompanyRegAddress, tblcCmpLstCompanyCorpAddress, tblcCmpLstCompanyMblNo;
    private ObservableList<CompanyListDTO> originalData;
    @FXML
    TextField tfCpmListSearch;
    private String companyListCount;
    @FXML
    private Button btnCmpLstCreate;
    @FXML
    private Label lblTotalLstOfCmp;
    //? Highlight the Record Start
    public static boolean isNewCompanyCreated = false; // Flag for new creation
    public static String editedCompanyId = null; // ID for edited franchise
    //? Highlight the Record End

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        //TODO: Set column resize policy
        tblvCompanyList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //TODO: on page load focus Search field.
        Platform.runLater(() -> tfCpmListSearch.requestFocus());

        //TODO: node traversal
        nextElementFocus();

        companyListTableDesign();

        fetchDataOfAllCompanyList();

        btnCmpLstCreate.setOnAction(event -> {
                    companyListDTO = null;
                    GlobalSadminController.getInstance().addTabStatic(COMPANY_CREATE_SLUG, false);
                }
        );


//        tblvCompanyList.setRowFactory(tv -> {
//            TableRow<CompanyListDTO> row = new TableRow<>();
//            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent mouseEvent) {
//                    if (mouseEvent.getClickCount() == 2) {
//                        CompanyListDTO selectedItem = (CompanyListDTO) tblvCompanyList.getSelectionModel().getSelectedItem();
//                        Integer companyId = Integer.valueOf(selectedItem.getId());
//                        companyEditPage();
//                    }
//                }
//            });
//            return row;
//        });

        //todo: Get the id onDoubleClick for Company Edit
        tblvCompanyList.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                companyEditPage();
            }
        });

        tblvCompanyList.setRowFactory(tv -> {
            TableRow<CompanyListDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    companyEditPage();
                }
            });
            return row;
        });


        //todo: Search without API Call in the Table
        originalData = tblvCompanyList.getItems();
        tfCpmListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });

        //TODO: Shortcut Key Code go to create page
        bpCmpListRoot.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.N && e.isControlDown()) {
                companyListDTO = null;
                if (GlobalSadminController.getInstance() == null) {
                } else {
                }
                GlobalSadminController.getInstance().addTabStatic(COMPANY_CREATE_SLUG, false);
            }
            if (e.getCode() == KeyCode.E && e.isControlDown()) {
                companyEditPage();
            }
        });
    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            bpCmpListRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            bpCmpListRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            bpCmpListRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            bpCmpListRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            bpCmpListRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            bpCmpListRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            bpCmpListRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    public void companyListTableDesign() {
        tblcCmpLstCompanyName.prefWidthProperty().bind(tblvCompanyList.widthProperty().multiply(0.2));
        tblcCmpLstCompanyCode.prefWidthProperty().bind(tblvCompanyList.widthProperty().multiply(0.2));
        tblcCmpLstCompanyRegAddress.prefWidthProperty().bind(tblvCompanyList.widthProperty().multiply(0.2));
        tblcCmpLstCompanyCorpAddress.prefWidthProperty().bind(tblvCompanyList.widthProperty().multiply(0.2));
        tblcCmpLstCompanyMblNo.prefWidthProperty().bind(tblvCompanyList.widthProperty().multiply(0.2));
    }

    //TODO: travel components
    private void nodeNextElement(Node current_node, Node next_node) {
        current_node.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                next_node.requestFocus();
                event.consume();
            }
            if (event.getCode() == KeyCode.DOWN) {
                tblvCompanyList.getSelectionModel().select(0);
                tblvCompanyList.requestFocus();
            }
            if (event.getCode() == KeyCode.ENTER) {
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

//    private void nodeNextElement(Node current_node, Node next_node) {
//        current_node.setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.ENTER) {
//                next_node.requestFocus();
//                event.consume();
//            } else if (event.getCode() == KeyCode.DOWN) {
//                System.out.println("focused...");
//                tblvCompanyList.requestFocus();
//                tblvCompanyList.getSelectionModel().select(0);
//            } else if (event.getCode() == KeyCode.ENTER) {
//                if (current_node instanceof Button button) {
//                    button.fire();
//                }
//            } else if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
//                if (current_node instanceof RadioButton radioButton) {
//                    radioButton.setSelected(!radioButton.isSelected());
//                    radioButton.fire();
//                }
//            }
//        });
//    }


    public void nextElementFocus() {
        nodeNextElement(tfCpmListSearch, btnCmpLstCreate);
        nodeNextElement(btnCmpLstCreate, tblvCompanyList);

    }


    //todo: Function to Company Edit
    public void companyEditPage() {
        try {
            companyListDTO = tblvCompanyList.getSelectionModel().getSelectedItem();
            if(companyListDTO!=null) {
                //? Highlight
                CompanyListController.editedCompanyId = companyListDTO.getId(); //? Set the ID for editing
                String SelectedId = companyListDTO.getId();
                GlobalSadminController.getInstance().addTabStaticWithParam(COMPANY_EDIT_SLUG, false, Integer.valueOf(SelectedId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //todo: Function to load the all company list
    public void fetchDataOfAllCompanyList() {
        APIClient apiClient = null;
        try {
            tblvCompanyList.getItems().clear();
            apiClient = new APIClient(EndPoints.COMPANY_LIST_ENDPOINT, "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    ObservableList<CompanyListDTO> companyObjList = FXCollections.observableArrayList();
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray responseObject = jsonObject.getAsJsonArray("responseObject");

                        companyListCount = String.valueOf(responseObject.size());
                        lblTotalLstOfCmp.setText(companyListCount);

                        if (responseObject.size() > 0) {
                            for (JsonElement element : responseObject) {
                                Long i = 0L;
                                JsonObject item = element.getAsJsonObject();
                                String index = String.valueOf(i + 1);
                                String id = item.get("id").getAsString();
                                String companyName = item.get("companyName").getAsString();
                                String companyCode = item.get("companyCode").getAsString();
                                String registeredAddress = item.get("registeredAddress").getAsString();
                                String corporateAddress = item.get("corporateAddress").getAsString();
                                String companyMobileNo = item.get("mobile").getAsString();

                                companyObjList.add(new CompanyListDTO(id, companyName, companyCode,
                                        registeredAddress, corporateAddress, companyMobileNo)
                                );
                            }
                            tblcCmpLstCompanyName.setCellValueFactory(new PropertyValueFactory<>("companyName"));
                            tblcCmpLstCompanyCode.setCellValueFactory(new PropertyValueFactory<>("companyCode"));
                            tblcCmpLstCompanyRegAddress.setCellValueFactory(new PropertyValueFactory<>("registeredAddress"));
                            tblcCmpLstCompanyCorpAddress.setCellValueFactory(new PropertyValueFactory<>("corporateAddress"));
                            tblcCmpLstCompanyMblNo.setCellValueFactory(new PropertyValueFactory<>("companyMobileNo"));
                            // Update originalData with new items
                            originalData.setAll(companyObjList);

                            tblvCompanyList.setItems(companyObjList);
                            //******************************** Highlight on the Created/Edited Record in the List Start ********************************
                            if (CompanyListController.isNewCompanyCreated) {
                                tblvCompanyList.getSelectionModel().selectLast();
                                tblvCompanyList.scrollTo(tblvCompanyList.getItems().size() - 1);
                                CompanyListController.isNewCompanyCreated = false; // Reset the flag
                            } else if (CompanyListController.editedCompanyId != null) {
                                for (CompanyListDTO company : companyObjList) {
                                    if (company.getId().equals(CompanyListController.editedCompanyId)) {
                                        tblvCompanyList.getSelectionModel().select(company);
                                        tblvCompanyList.scrollTo(company);
                                        CompanyListController.editedCompanyId = null; // Reset the ID
                                        break;
                                    }
                                }
                            }
                            //******************************** Highlight on the Created/Edited Record in the List End ********************************

                        } else {
                            //TODO : use logger for alert messages
                            AlertUtility.CustomCallback callback = (number) -> {
                                if (number == 0) {
                                    tfCpmListSearch.requestFocus();
                                }
                            };
//                            Stage stage = (Stage) bpCmpListRoot.getScene().getWindow();
//                            AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Failed to Load Data ", callback);

                        }
                    } else {
                        //TODO : use logger for alert messages
                        AlertUtility.CustomCallback callback = (number) -> {
                            if (number == 0) {
                                tfCpmListSearch.requestFocus();
                            }
                        };
                        Stage stage = (Stage) bpCmpListRoot.getScene().getWindow();
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
                    tfCpmListSearch.requestFocus();
                }
            };
            Stage stage = (Stage) bpCmpListRoot.getScene().getWindow();
            AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Failed to connect server! ", callback);

            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    //todo: Search Function to Search in the Table
    public void filterData(String keyword) {
        ObservableList<CompanyListDTO> filteredData = FXCollections.observableArrayList();

        if (keyword.isEmpty()) {
            tblvCompanyList.setItems(originalData);
            return;
        }

        for (CompanyListDTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }
        tblvCompanyList.setItems(filteredData);
    }

    //todo: Search Function to Search in the Table for columns
    public boolean matchesKeyword(CompanyListDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        // Check if any of the columns contain the keyword
        return item.getCompanyName().toLowerCase().contains(lowerCaseKeyword) ||
                item.getCompanyCode().toLowerCase().contains(lowerCaseKeyword) ||
                item.getRegisteredAddress().toLowerCase().contains(lowerCaseKeyword) ||
                item.getCorporateAddress().toLowerCase().contains(lowerCaseKeyword) ||
                item.getCompanyMobileNo().toLowerCase().contains(lowerCaseKeyword);
    }
}


