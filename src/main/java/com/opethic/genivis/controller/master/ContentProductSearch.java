package com.opethic.genivis.controller.master;

import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.dto.ContentProductMstDTO;
import com.opethic.genivis.models.ContentMst;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URL;
import java.util.*;

public class ContentProductSearch implements Initializable {

    @FXML
    private TableColumn<?, ?> Col1ContentProductSearch, Col2ContentProductSearch, Col3ContentProductSearch, Col4ContentProductSearch, Col5ContentProductSearch, Col6ContentProductSearch, Col7ContentProductSearch, Col8ContentProductSearch;

    @FXML
    private ComboBox<ContentMst> cboxcontentproductsearchmstpkg, cboxcontentproductsearchmsttype, CboxContProdctSearchMst;

    @FXML
    private TableView<ContentProductMstDTO> tblvcontentproductsearchmst;

    @FXML
    private TextField txfdcontprdtsearchmst;

    @FXML
    private BorderPane bpContentProductSearchRootPane;

    private static final Logger loggerContentProductSearch = LogManager.getLogger(ContentProductSearch.class);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        Platform.runLater(() -> {
            CboxContProdctSearchMst.requestFocus();
        });
        setPowerValidation();
        getContentData();
        getPkgData();
        getTypData();
        getContentDetails("", "", "", "");
        CboxContProdctSearchMst.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            getContentDetails(newValue.getContentName() != null ? newValue.getContentName() : "", txfdcontprdtsearchmst.getText() != null ? txfdcontprdtsearchmst.getText() : "", cboxcontentproductsearchmstpkg.getValue() != null ? cboxcontentproductsearchmstpkg.getValue().getContentName() : "", cboxcontentproductsearchmsttype.getValue() != null ? cboxcontentproductsearchmsttype.getValue().getContentName() : "");
        });
        cboxcontentproductsearchmstpkg.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            getContentDetails(CboxContProdctSearchMst.getValue() != null ? CboxContProdctSearchMst.getValue().getContentName() : "", txfdcontprdtsearchmst.getText() != null ? txfdcontprdtsearchmst.getText() : "", newValue.getContentName() != null ? newValue.getContentName() : "", cboxcontentproductsearchmsttype.getValue() != null ? cboxcontentproductsearchmsttype.getValue().getContentName() : "");
        });
        cboxcontentproductsearchmsttype.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            getContentDetails(CboxContProdctSearchMst.getValue() != null ? CboxContProdctSearchMst.getValue().getContentName() : "", txfdcontprdtsearchmst.getText() != null ? txfdcontprdtsearchmst.getText() : "", cboxcontentproductsearchmstpkg.getValue() != null ? cboxcontentproductsearchmstpkg.getValue().getContentName() : "", newValue.getContentName() != null ? newValue.getContentName() : "");
        });
        txfdcontprdtsearchmst.textProperty().addListener((obsv, oldv, newv) -> {
            getContentDetails(CboxContProdctSearchMst.getValue() != null ? CboxContProdctSearchMst.getValue().getContentName() : "", newv != null ? newv : "", cboxcontentproductsearchmstpkg.getValue() != null ? cboxcontentproductsearchmstpkg.getValue().getContentName() : "", cboxcontentproductsearchmsttype.getValue() != null ? cboxcontentproductsearchmsttype.getValue().getContentName() : "");
        });

        CboxContProdctSearchMst.setOnKeyPressed(this::handleEnterKeyPressed);
        txfdcontprdtsearchmst.setOnKeyPressed(this::handleEnterKeyPressed);
        cboxcontentproductsearchmstpkg.setOnKeyPressed(this::handleEnterKeyPressed);
        cboxcontentproductsearchmsttype.setOnKeyPressed(this::handleEnterKeyPressed);
    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            bpContentProductSearchRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            bpContentProductSearchRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            bpContentProductSearchRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            bpContentProductSearchRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            bpContentProductSearchRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            bpContentProductSearchRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            bpContentProductSearchRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }


    private void handleEnterKeyPressed(KeyEvent keyEvent) {
        Node source = (Node) keyEvent.getSource();
        if (keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.TAB ) {
            switch (source.getId()) {
                case "CboxContProdctSearchMst":
                        txfdcontprdtsearchmst.requestFocus();
                    break;
                case "txfdcontprdtsearchmst":
                        cboxcontentproductsearchmstpkg.requestFocus();
                    break;
                case "cboxcontentproductsearchmstpkg":
                        cboxcontentproductsearchmsttype.requestFocus();
                    break;
                case "cboxcontentproductsearchmsttype":
                        tblvcontentproductsearchmst.requestFocus();
                    break;
                default:
                    break;
            }
        }
    }




    private void setPowerValidation() {
        TextFormatter<String> powerFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        });
        txfdcontprdtsearchmst.setTextFormatter(powerFormatter);
    }


    public void getContentData() {
        APIClient apiClient=null;
        try {
            CboxContProdctSearchMst.getItems().clear();
            apiClient=new APIClient(EndPoints.getAllContentMaster, "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    JSONObject jsonObject = new JSONObject(workerStateEvent.getSource().getValue().toString());
                    if (jsonObject.getInt("responseStatus") == 200) {
                        JSONArray jarr = jsonObject.getJSONArray("responseObject");
                        List<ContentMst> contentMstList = new ArrayList<>();
                        for (Object o : jarr) {
                            JSONObject object = new JSONObject(o.toString());
                            ContentMst contentMst = new ContentMst(object.getInt("id"), object.getString("contentName"));
                            contentMstList.add(contentMst);
                        }
                        ObservableList<ContentMst> peopleList = FXCollections.observableArrayList(contentMstList);
                        CboxContProdctSearchMst.getItems().addAll(peopleList);
                        CboxContProdctSearchMst.setConverter(new StringConverter<ContentMst>() {
                            @Override
                            public String toString(ContentMst contentMst) {
                                return contentMst.getContentName();
                            }

                            @Override
                            public ContentMst fromString(String string) {
                                return null;
                            }
                        });
                    } else {
                        Stage stage1 = (Stage) bpContentProductSearchRootPane.getScene().getWindow();
                        String msg = jsonObject.getString("message");
                        AlertUtility.CustomCallback callback = number -> {
                            CboxContProdctSearchMstsetFocus();
                        };
                        AlertUtility.AlertError(stage1,AlertUtility.alertTypeError, msg, callback);
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    loggerContentProductSearch.error("Network API cancelled in getContentData() Content Product Search" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    loggerContentProductSearch.error("Network API failed in getContentData() Content Product Search" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            loggerContentProductSearch.error("Exception getContentData() Content Product Search : " + Globals.getExceptionString(e));

        }
    }

    private void CboxContProdctSearchMstsetFocus() {
        CboxContProdctSearchMst.requestFocus();
    }

    public void getPkgData() {
        APIClient apiClient=null;
        try {
            cboxcontentproductsearchmstpkg.getItems().clear();
            apiClient=new APIClient(EndPoints.getAllContentPackageMaster, "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    JSONObject jsonObject = new JSONObject(workerStateEvent.getSource().getValue().toString());
                    List<ContentMst> contentMstList = new ArrayList<>();
                    if (jsonObject.getInt("responseStatus") == 200) {
                        JSONArray jarr = jsonObject.getJSONArray("responseObject");
                        for (Object o : jarr) {
                            JSONObject object = new JSONObject(o.toString());
                            ContentMst contentMst = new ContentMst(object.getInt("id"), object.getString("contentPackageName"));
                            contentMstList.add(contentMst);
                        }
                        ObservableList<ContentMst> peopleList = FXCollections.observableArrayList(contentMstList);
                        cboxcontentproductsearchmstpkg.getItems().addAll(peopleList);
                        cboxcontentproductsearchmstpkg.setConverter(new StringConverter<ContentMst>() {
                            @Override
                            public String toString(ContentMst contentMst) {
                                return contentMst.getContentName();
                            }
                            @Override
                            public ContentMst fromString(String string) {
                                return null;
                            }
                        });
                    } else {
                        Stage stage1 = (Stage) bpContentProductSearchRootPane.getScene().getWindow();
                        String msg = jsonObject.getString("message");
                        AlertUtility.CustomCallback callback = number -> {
                            cboxcontentproductsearchmstpkgSetFocus();
                        };
                        AlertUtility.AlertError(stage1,AlertUtility.alertTypeError, msg, callback);
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    loggerContentProductSearch.error("Network API cancelled in getPkgData() Content Product Search" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    loggerContentProductSearch.error("Network API failed in getPkgData() Content Product Search" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            loggerContentProductSearch.error("Exception getPkgData() Content Product Search : " + Globals.getExceptionString(e));
        }
    }

    private void cboxcontentproductsearchmstpkgSetFocus() {
        cboxcontentproductsearchmstpkg.requestFocus();
    }

    public void getTypData() {
        APIClient apiClient=null;
        try {
            cboxcontentproductsearchmsttype.getItems().clear();
            apiClient=new APIClient(EndPoints.getAllContentMasterDose, "", RequestType.GET);
          apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
              @Override
              public void handle(WorkerStateEvent workerStateEvent) {
                  JSONObject jsonObject = new JSONObject(workerStateEvent.getSource().getValue().toString());
                  List<ContentMst> contentMstList = new ArrayList<>();
                  if (jsonObject.getInt("responseStatus") == 200) {
                      JSONArray jarr = jsonObject.getJSONArray("responseObject");
                      for (Object o : jarr) {
                          JSONObject object = new JSONObject(o.toString());
                          ContentMst contentMst = new ContentMst(object.getInt("id"), object.getString("contentNameDose"));
                          contentMstList.add(contentMst);
                      }
                      ObservableList<ContentMst> peopleList = FXCollections.observableArrayList(contentMstList);
                      cboxcontentproductsearchmsttype.getItems().addAll(peopleList);
                      cboxcontentproductsearchmsttype.setConverter(new StringConverter<ContentMst>() {
                          @Override
                          public String toString(ContentMst contentMst) {
                              return contentMst.getContentName();
                          }
                          @Override
                          public ContentMst fromString(String string) {
                              return null;
                          }
                      });
                  } else {
                      Stage stage1 = (Stage) bpContentProductSearchRootPane.getScene().getWindow();
                      String msg = jsonObject.getString("message");
                      AlertUtility.CustomCallback callback = number -> {
                          cboxcontentproductsearchmsttypeSetFocus();
                      };
                      AlertUtility.AlertError(stage1,AlertUtility.alertTypeError, msg, callback);
                  }
              }
          });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    loggerContentProductSearch.error("Network API cancelled in getTypData() Content Product Search" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    loggerContentProductSearch.error("Network API failed in getTypData() Content Product Search" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            loggerContentProductSearch.error("Exception getTypData() Content Product Search: " + Globals.getExceptionString(e));
        }
    }

    private void cboxcontentproductsearchmsttypeSetFocus() {
        cboxcontentproductsearchmstpkg.requestFocus();
    }

    private void getContentDetails(String contentType, String contentPower, String contentPkg, String contentDose) {
        APIClient apiClient=null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("contentType", contentType);
            map.put("contentPower", contentPower);
            map.put("contentPkg", contentPkg);
            map.put("contentDose", contentDose);
            String formData = Globals.mapToStringforFormData(map);
            apiClient = new APIClient(EndPoints.ProductListByContentDetails, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    JSONObject jsonObject = new JSONObject(workerStateEvent.getSource().getValue().toString());
                    ObservableList<ContentProductMstDTO> orgRow = FXCollections.observableArrayList();
                    if (jsonObject.getInt("responseStatus") == 200) {
                        JSONArray jarr = jsonObject.getJSONArray("list");
                        for (Object o : jarr) {
                            JSONObject object = new JSONObject(o.toString());
                                ContentProductMstDTO contentMst = new ContentProductMstDTO(object.getInt("sgst"), object.getInt("purchaserate"), object.getString("batch_expiry"), object.getString("hsn"), object.getString("code"), object.getBoolean("is_serial"), object.getInt("tax_per"), object.getInt("mrp"), object.getInt("cgst"), object.getString("packing"), object.getString("product_name"), object.getBoolean("is_inventory"), object.getInt("igst"), object.getString("unit"), object.getBoolean("is_batch"), object.getInt("current_stock"), object.getString("tax_type"), object.getInt("id"), object.getBoolean("is_negative"), object.get("barcode") != null ? object.getString("barcode") : "", object.getString("brand"), object.getInt("sales_rate"), object.getString("productType"));
                            orgRow.add(contentMst);
                        }
                        Col1ContentProductSearch.setCellValueFactory(new PropertyValueFactory<>("product_name"));
                        Col2ContentProductSearch.setCellValueFactory(new PropertyValueFactory<>("packing"));
                        Col3ContentProductSearch.setCellValueFactory(new PropertyValueFactory<>("barcode"));
                        Col4ContentProductSearch.setCellValueFactory(new PropertyValueFactory<>("brand"));
                        Col5ContentProductSearch.setCellValueFactory(new PropertyValueFactory<>("mrp"));
                        Col6ContentProductSearch.setCellValueFactory(new PropertyValueFactory<>("current_stock"));
                        Col7ContentProductSearch.setCellValueFactory(new PropertyValueFactory<>("unit"));
                        Col8ContentProductSearch.setCellValueFactory(new PropertyValueFactory<>("sales_rate"));
                        FilteredList<ContentProductMstDTO> filteredList = new FilteredList<>(orgRow);
                        tblvcontentproductsearchmst.setItems(filteredList);
                    } else {
                        Stage stage1 = (Stage) bpContentProductSearchRootPane.getScene().getWindow();
                        String msg = jsonObject.getString("message");
                        AlertUtility.CustomCallback callback = number -> {
                            CboxContProdctSearchMstsetFocus();
                        };
                        AlertUtility.AlertError(stage1,AlertUtility.alertTypeError, msg, callback);
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    loggerContentProductSearch.error("Network API cancelled in getContentDetails() Content Product Search" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    loggerContentProductSearch.error("Network API failed in getContentDetails() Content Product Search" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            loggerContentProductSearch.error("Exception getContentDetails() Content Product Search : " + Globals.getExceptionString(e));
        }
    }
}
