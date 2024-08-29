package com.opethic.genivis.controller.master;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.opethic.genivis.controller.commons.OverlaysEffect;
import com.opethic.genivis.controller.dialogs.SingleInputDialogs;
import com.opethic.genivis.dto.GstDetailsDTO;
import com.opethic.genivis.dto.TranxLedgerWindowDTO;
import com.opethic.genivis.dto.reqres.catalog.*;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.Globals;
import com.opethic.genivis.utils.Utils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.*;

public class CatalogController implements Initializable {
    @FXML
    public Label lblUnitTotal;
    @FXML
    public Label lblPkgTotal;
    @FXML
    public Label lblSubCatTotal;
    @FXML
    public Label lblCategoryTotal;
    @FXML
    public Label lblFormulationTotal;
    @FXML
    public Label lblMfgTotal;
    @FXML
    public Label lblBrandTotal;
    @FXML
    private VBox vbox;
    @FXML
    private Button btnCatlgBrndAdd,btnCatlgMFGAdd,btnCatlgFormAdd,btnCatlgCatgAdd,btnCatlgSbCatgAdd,btnCatlgPackAdd,btnCatlgUnitAdd;

    @FXML
    private TableView<ObservableList<StringProperty>> brandTable;

    @FXML
    private TableColumn<ObservableList<StringProperty>, String> brandAction;

    @FXML
    private TableColumn<ObservableList<StringProperty>, String> brandColumn;

    @FXML
    private TableView<ObservableList<StringProperty>> manufacturerTable;

    @FXML
    private TableColumn<ObservableList<StringProperty>, String> manufacturerAction;

    @FXML
    private TableColumn<ObservableList<StringProperty>, String> manufacturerColumn;

    @FXML
    private TableView<ObservableList<StringProperty>> formulationTable;

    @FXML
    private TableColumn<ObservableList<StringProperty>, String> formulationAction;

    @FXML
    private TableColumn<ObservableList<StringProperty>, String> formulationColumn;

    @FXML
    private TableView<ObservableList<StringProperty>> categoryTable;

    @FXML
    private TableColumn<ObservableList<StringProperty>, String> categoryAction;

    @FXML
    private TableColumn<ObservableList<StringProperty>, String> categoryColumn;

    @FXML
    private TableView<ObservableList<StringProperty>> subCategoryTable;

    @FXML
    private TableColumn<ObservableList<StringProperty>, String> subCategoryAction;

    @FXML
    private TableColumn<ObservableList<StringProperty>, String> subCategoryColumn;

    @FXML
    private TableView<ObservableList<StringProperty>> packageTable;

    @FXML
    private TableColumn<ObservableList<StringProperty>, String> packageAction;

    @FXML
    private TableColumn<ObservableList<StringProperty>, String> packageColumn;

    @FXML
    private TableView<ObservableList<StringProperty>> unitTable;

    @FXML
    private TableColumn<ObservableList<StringProperty>, String> unitAction;

    @FXML
    private TableColumn<ObservableList<StringProperty>, String> unitColumn;

    @FXML
    private VBox vboxBrand;

    private ObservableList<Unit> list = FXCollections.observableArrayList();


    private Set<String> selectedRows = new HashSet<>();
//    private static final KeyCombination new_brand_B =  new KeyCodeCombination(KeyCode.B);      //for open the brand pop up
//    private static final KeyCombination new_manufacturer_M =  new KeyCodeCombination(KeyCode.M);      //for open the mfg pop up
//    private static final KeyCombination new_formulation_F = new KeyCodeCombination(KeyCode.F);
//    private static final KeyCombination new_category_C = new KeyCodeCombination(KeyCode.C);
//    private static final KeyCombination new_subcategory_S = new KeyCodeCombination(KeyCode.S);
//    private static final KeyCombination new_package_p = new KeyCodeCombination(KeyCode.P);
//    private static final KeyCombination new_unit_U = new KeyCodeCombination(KeyCode.U);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(()->btnCatlgBrndAdd.requestFocus());

        vbox.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
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
        });

        brandTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        subCategoryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        manufacturerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        formulationTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        categoryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        packageTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        unitTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        getBrandData();
        getManufacturerData();
        getFormulationData();
        getCategoryData();
        getSubCategoryData();
        getPackageData();
        getUnitData();
        getUnitList();
        brandTable.setRowFactory(tv -> {
            TableRow<ObservableList<StringProperty>> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        ObservableList<StringProperty> brandRow = brandTable.getSelectionModel().getSelectedItem();
                        String id = brandRow.get(0).getValue();
                        String inputKey = brandRow.get(1).getValue();
                        Stage stage = (Stage) vbox.getScene().getWindow();
                        SingleInputDialogs.singleInputDialog(inputKey, stage, "Update Brand", input -> {
                            updateBrand(id, input);
                        });

                    }
                }
            });
            return row;
        });
        manufacturerTable.setRowFactory(tv -> {
            TableRow<ObservableList<StringProperty>> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        System.out.println("double clicked on manufacturer");
                        ObservableList<StringProperty> mafRow = manufacturerTable.getSelectionModel().getSelectedItem();
                        String id = mafRow.get(0).getValue();
                        String inputKey = mafRow.get(1).getValue();
                        Stage stage = (Stage) vbox.getScene().getWindow();
                        SingleInputDialogs.singleInputDialog(inputKey, stage, "Update Manufacturer", input -> {
                            updateGroup(id, input);
                        });

                    }
                }
            });
            return row;
        });
        formulationTable.setRowFactory(tv -> {
            TableRow<ObservableList<StringProperty>> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        ObservableList<StringProperty> formationRow = formulationTable.getSelectionModel().getSelectedItem();
                        String id = formationRow.get(0).getValue();
                        String inputKey = formationRow.get(1).getValue();
                        Stage stage = (Stage) vbox.getScene().getWindow();
                        SingleInputDialogs.singleInputDialog(inputKey, stage, "Update Formation", input -> {
                            updateFormation(id, input);
                        });

                    }
                }
            });
            return row;
        });
        categoryTable.setRowFactory(tv -> {
            TableRow<ObservableList<StringProperty>> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        ObservableList<StringProperty> categoryRow = categoryTable.getSelectionModel().getSelectedItem();
                        String id = categoryRow.get(0).getValue();
                        String inputKey = categoryRow.get(1).getValue();
                        Stage stage = (Stage) vbox.getScene().getWindow();
                        SingleInputDialogs.singleInputDialog(inputKey, stage, "Update Category", input -> {
                            updateCategory(id, input);
                        });

                    }
                }
            });
            return row;
        });
        subCategoryTable.setRowFactory(tv -> {
            TableRow<ObservableList<StringProperty>> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        ObservableList<StringProperty> subcategoryRow = subCategoryTable.getSelectionModel().getSelectedItem();
                        String id = subcategoryRow.get(0).getValue();
                        String inputKey = subcategoryRow.get(1).getValue();
                        Stage stage = (Stage) vbox.getScene().getWindow();
                        SingleInputDialogs.singleInputDialog(inputKey, stage, "Update Subcategory", input -> {
                            updateSubCategory(id, input);
                        });

                    }
                }
            });
            return row;
        });
        packageTable.setRowFactory(tv -> {
            TableRow<ObservableList<StringProperty>> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        ObservableList<StringProperty> packageRow = packageTable.getSelectionModel().getSelectedItem();
                        String id = packageRow.get(0).getValue();
                        String inputKey = packageRow.get(1).getValue();
                        Stage stage = (Stage) vbox.getScene().getWindow();
                        SingleInputDialogs.singleInputDialog(inputKey, stage, "Update Package", input -> {
                            updatePackage(id, input);
                        });

                    }
                }
            });
            return row;
        });
        unitTable.setRowFactory(tv -> {
            TableRow<ObservableList<StringProperty>> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        ObservableList<StringProperty> unitRow = unitTable.getSelectionModel().getSelectedItem();
                        System.out.println("Unit Row--->" + unitRow);
                        String id = unitRow.get(0).getValue();
                        String inputKey = unitRow.get(1).getValue();
                        Stage stage = (Stage) vbox.getScene().getWindow();
                        SingleInputDialogs.twoInputDialog(inputKey, stage, "Update Unit", list, inputKey, (input1, input2) -> {
                            updateUnit(id, input1, input2);
                        });

                    }
                }
            });
            return row;
        });

        //code for shortcut key to open the different popup to open the create window Ex.- B button => for brand, M => for Manufacturer
//        btnCatlgBrndAdd.sceneProperty().addListener((obsValue, oldValue, newValue)->{
//            if(newValue!=null){
//                newValue.getAccelerators().put(new_brand_B, btnCatlgBrndAdd::fire);
//            }
//        });
//        btnCatlgMFGAdd.sceneProperty().addListener((obsValue, oldValue, newValue)->{
//            if(newValue!= null){
//                newValue.getAccelerators().put(new_manufacturer_M, btnCatlgMFGAdd::fire);
//            }
//        });
//        btnCatlgFormAdd.sceneProperty().addListener((obsValue, oldValue, newValue)->{
//            if(newValue!= null){
//                newValue.getAccelerators().put(new_formulation_F, btnCatlgFormAdd::fire);
//            }
//        });
//        btnCatlgCatgAdd.sceneProperty().addListener((obsValue, oldValue, newValue)->{
//            if(newValue!= null){
//                newValue.getAccelerators().put(new_category_C, btnCatlgCatgAdd::fire);
//            }
//        });
//        btnCatlgSbCatgAdd.sceneProperty().addListener((obsValue, oldValue, newValue)->{
//            if(newValue != null){
//                newValue.getAccelerators().put(new_subcategory_S, btnCatlgSbCatgAdd::fire);
//            }
//        });
//        btnCatlgPackAdd.sceneProperty().addListener((obsValue, oldValue, newValue)->{
//            if(newValue != null){
//                newValue.getAccelerators().put(new_package_p, btnCatlgPackAdd::fire);
//            }
//        });
//        btnCatlgUnitAdd.sceneProperty().addListener((obsValue, oldValue, newValue)->{
//            if(newValue != null){
//                newValue.getAccelerators().put(new_unit_U, btnCatlgUnitAdd::fire);
//            }
//        });

        shortcutKeys();
    }
    private void shortcutKeys(){
        vbox.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event){
                if(event.getCode() == KeyCode.B && event.isControlDown()){
//                    event.consume();
                    System.out.println("inside B + ctrl");
                    Stage stage = (Stage) vbox.getScene().getWindow();
                    SingleInputDialogs.singleInputDialog("", stage, "Add Brand", input -> {
                        if (!input.isEmpty())
                            addBrand(input);
                        else {
                            System.out.println("Blank Brand");
                        }
                    });
                }
                if(event.getCode() == KeyCode.M && event.isControlDown()){
//                    event.consume();
                    System.out.println("inside M + ctrl");
                    Stage stage = (Stage) vbox.getScene().getWindow();
                    SingleInputDialogs.singleInputDialog("", stage, "Add Manufacturer", input -> {
                        System.out.println(input);
                        addManufacturer(input);
                    });
                }
                if(event.getCode() == KeyCode.F && event.isControlDown()){
//                     event.consume();
                    System.out.println("inside F + ctrl");
                    Stage stage = (Stage) vbox.getScene().getWindow();

                    SingleInputDialogs.singleInputDialog("", stage, "Add Formulation", input -> {
                        addFormulation(input);
                    });
                }
                if(event.getCode() == KeyCode.C && event.isControlDown()){
//                    event.consume();
                    System.out.println("inside C + ctrl");
                    Stage stage = (Stage) vbox.getScene().getWindow();

                    SingleInputDialogs.singleInputDialog("", stage, "Add Category", input -> {
                        System.out.println(input);
                        addCategory(input);
                    });
                }
                if(event.getCode() == KeyCode.S && event.isControlDown()){
//                    event.consume();
                    System.out.println("inside S + ctrl");
                    Stage stage = (Stage) vbox.getScene().getWindow();

                    SingleInputDialogs.singleInputDialog("", stage, "Add Sub Category", input -> {
                        System.out.println(input);
                        addSubCategory(input);
                    });
                }
                if (event.getCode() == KeyCode.P && event.isControlDown()){
//                    event.consume();
                    System.out.println("inside P + ctrl");
                    Stage stage = (Stage) vbox.getScene().getWindow();

                    SingleInputDialogs.singleInputDialog("", stage, "Add Package", input -> {
                        System.out.println(input);
                        addPackage(input);
                    });
                }
                if (event.getCode() == KeyCode.U && event.isControlDown()){
//                    event.consume();
                    System.out.println("inside U + ctrl");
                    Stage stage = (Stage) vbox.getScene().getWindow();
                    OverlaysEffect.setOverlaysEffect(stage);
                    SingleInputDialogs.twoInputDialog("", stage, "Add Unit", list, "", (input1, input2) -> {
                        addUnit(input1, input2, list);
                    });
                }
            }
        });
    }

    private void getUnitList() {
        list.add(new Unit("BAG-Bag", "Services"));
        list.add(new Unit("BAL-Bale", "BAL"));
        list.add(new Unit("BDL-Bundles", "BDL"));
        list.add(new Unit("BKL-Buckles", "BKL"));
        list.add(new Unit("BOU-Billions Of Units", "BOU"));
        list.add(new Unit("BOX-Box", "BOX"));
        list.add(new Unit("BTL-Bottles", "BTL"));
        list.add(new Unit("BUN-Bunches", "BUN"));
        list.add(new Unit("CAN-Cans", "CAN"));
        list.add(new Unit("CBM-Cubic Meter", "CBM"));
        list.add(new Unit("CCM-Cubic Centimeter", "CCM"));
        list.add(new Unit("CMS-Centimeter", "CMS"));
        list.add(new Unit("CTN-Cartons", "CTN"));
        list.add(new Unit("DOZ-Dozen", "DOZ"));
        list.add(new Unit("DRM-Drum", "DRM"));
        list.add(new Unit("GGR-Great Gross", "GGR"));
        list.add(new Unit("GMS-Grams", "GMS"));
        list.add(new Unit("GRS-Gross", "GRS"));
        list.add(new Unit("GYD-Gross Yard", "GYD"));
        list.add(new Unit("KGS-Kilograms", "KGS"));
        list.add(new Unit("KLR-Kilo Liter", "KLR"));
        list.add(new Unit("KME-Kilo Meter", "KME"));
        list.add(new Unit("MLT-Milliliter", "MLT"));
        list.add(new Unit("MTR-Meters", "MTR"));
        list.add(new Unit("MTS-Metric Tons", "MTS"));
        list.add(new Unit("NOS-Numbers", "NOS"));
        list.add(new Unit("PAC-Packs", "PAC"));
        list.add(new Unit("PCS-Pieces", "PCS"));
        list.add(new Unit("PRS-Pairs", "PRS"));
        list.add(new Unit("QTL-Quintal", "QTL"));
        list.add(new Unit("ROL-Rolls", "ROL"));
        list.add(new Unit("SET-Sets", "SET"));
        list.add(new Unit("SQF-Square Feet", "SQF"));
        list.add(new Unit("SQM-Square Meters", "SQM"));
        list.add(new Unit("TBS-Tablets", "TBS"));
        list.add(new Unit("TGM-Ten Gross", "TGM"));
        list.add(new Unit("THD-Thousands", "THD"));
        list.add(new Unit("TON-Tonnes", "TON"));
        list.add(new Unit("TUB-Tubes", "TUB"));
        list.add(new Unit("UGS-US Gallons", "UGS"));
        list.add(new Unit("UNT-Units", "UNT"));
        list.add(new Unit("SQY-Square Yards", "SQY"));
        list.add(new Unit("GYD-Gross Yards", "GYD"));
        list.add(new Unit("YDS-Yards", "YDS"));
        list.add(new Unit("OTH-Others", "OTH"));


    }

    @FXML
    private void onClickBrand(ActionEvent actionEvent) {
        Stage stage = (Stage) vbox.getScene().getWindow();
        SingleInputDialogs.singleInputDialog("", stage, "Add Brand", input -> {
            if (!input.isEmpty())
                addBrand(input);
            else {
                System.out.println("Blank Brand");
            }
        });
    }

    public void addBrand(String input) {
        Map<String, String> map = new HashMap<>();
        map.put("brandName", input);

        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.CREATE_BRAND);

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            // Utils.showSuccessAlertNew(responseBody.get("message").getAsString());
            AlertUtility.AlertSuccessTimeout("Success",responseBody.get("message").getAsString(),inputs->{});
            getBrandData();
        } else {
            // Utils.showErrorAlertNew(responseBody.get("message").getAsString());
            AlertUtility.AlertErrorTimeout2("Error",responseBody.get("message").getAsString(),inputs->{});
        }
    }

    public void updateBrand(String id, String input) {
        Map<String, String> map = new HashMap<>();
        map.put("brandName", input);
        map.put("id", id);
        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.UPDATE_BRAND);

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            Utils.showSuccessAlertNew(responseBody.get("message").getAsString());
            getBrandData();
        } else {
            Utils.showErrorAlertNew(responseBody.get("message").getAsString());
        }
    }

    public void updateGroup(String id, String input) {
        Map<String, String> map = new HashMap<>();
        map.put("groupName", input);
        map.put("id", id);
        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.UPDATE_GROUP);

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            Utils.showSuccessAlertNew("Manufacturer Updated Successfully");
            getManufacturerData();
        } else {
            Utils.showErrorAlertNew(responseBody.get("message").getAsString());
        }
    }

    public void updateFormation(String id, String input) {
        Map<String, String> map = new HashMap<>();
        map.put("subgroupName", input);
        map.put("id", id);
        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.UPDATE_FORMATION);

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            Utils.showSuccessAlertNew("Formation Updated Successfully");
            getFormulationData();
        } else {
            Utils.showErrorAlertNew(responseBody.get("message").getAsString());
        }
    }

    public void updateCategory(String id, String input) {
        Map<String, String> map = new HashMap<>();
        map.put("categoryName", input);
        map.put("id", id);
        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.UPDATE_CATEGORY);

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            Utils.showSuccessAlert(responseBody.get("message").getAsString());
            getCategoryData();
        } else {
            Utils.showErrorAlertNew(responseBody.get("message").getAsString());
        }
    }

    public void updateSubCategory(String id, String input) {
        Map<String, String> map = new HashMap<>();
        map.put("subCategoryName", input);
        map.put("id", id);
        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.UPDATE_SUB_CATEGORY);

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            Utils.showSuccessAlert(responseBody.get("message").getAsString());
            getSubCategoryData();
        } else {
            Utils.showErrorAlertNew(responseBody.get("message").getAsString());
        }
    }

    public void updatePackage(String id, String input) {
        Map<String, String> map = new HashMap<>();
        map.put("packing_name", input);
        map.put("id", id);
        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.UPDATE_PACKAGE);

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            Utils.showSuccessAlert(responseBody.get("message").getAsString());
            getPackageData();
        } else {
            Utils.showErrorAlertNew(responseBody.get("message").getAsString());
        }
    }

    public void updateUnit(String id, String input, Object comboBox) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);

        if (comboBox != null) {
            for (Unit unit : list) {
                if (comboBox.toString().equals(unit.getLabel())) {
                    map.put("unitName", input);
                    map.put("unitCode", unit.getValue());
                }
            }
        } else {
            System.out.println("No principle selected.");
        }

        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.UPDATE_UNIT);

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            Utils.showSuccessAlert(responseBody.get("message").getAsString());
            getUnitData();
        } else {
            Utils.showErrorAlert(responseBody.get("message").getAsString());
        }
    }

    public void onClickManufacturer(ActionEvent actionEvent) {

        Stage stage = (Stage) vbox.getScene().getWindow();
        SingleInputDialogs.singleInputDialog("", stage, "Add Manufacturer", input -> {
            System.out.println(input);
            addManufacturer(input);
        });
    }

    public void addManufacturer(String input) {
        Map<String, String> map = new HashMap<>();
        map.put("groupName", input);

        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "create_group");

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            AlertUtility.AlertSuccessTimeout("Success",responseBody.get("message").getAsString(),inputs->{});
            getManufacturerData();
            // Utils.showSuccessAlertNew("Manufacturer Added Successfully");
        } else {
            // Utils.showErrorAlertNew(responseBody.get("message").getAsString());
            AlertUtility.AlertErrorTimeout2("Error",responseBody.get("message").getAsString(),inputs->{});
        }
    }

    public void onClickFormulation(ActionEvent actionEvent) {

        Stage stage = (Stage) vbox.getScene().getWindow();

        SingleInputDialogs.singleInputDialog("", stage, "Add Formulation", input -> {
            addFormulation(input);
        });
    }

    public void addFormulation(String input) {
        Map<String, String> map = new HashMap<>();
        map.put("subgroupName", input);

        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "create_sub_group");

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            // Utils.showSuccessAlertNew("Formulation Added Successfully");
            AlertUtility.AlertSuccessTimeout("Success",responseBody.get("message").getAsString(),inputs->{});
            getFormulationData();
        } else {
            //   Utils.showErrorAlertNew(responseBody.get("message").getAsString());
            AlertUtility.AlertErrorTimeout2("Error",responseBody.get("message").getAsString(),inputs->{});
        }
    }


    public void onClickCategory(ActionEvent actionEvent) {

        Stage stage = (Stage) vbox.getScene().getWindow();

        SingleInputDialogs.singleInputDialog("", stage, "Add Category", input -> {
            System.out.println(input);
            addCategory(input);
        });
    }

    private void addCategory(String input) {
        Map<String, String> map = new HashMap<>();
        map.put("categoryName", input);

        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "create_category");

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            AlertUtility.AlertSuccessTimeout("Success",responseBody.get("message").getAsString(),inputs->{});
            getCategoryData();
        } else {
            AlertUtility.AlertErrorTimeout2("Error",responseBody.get("message").getAsString(),inputs->{});
        }
    }

    public void onClickSubCategory(ActionEvent actionEvent) {

        Stage stage = (Stage) vbox.getScene().getWindow();

        SingleInputDialogs.singleInputDialog("", stage, "Add Sub Category", input -> {
            System.out.println(input);
            addSubCategory(input);
        });
    }

    private void addSubCategory(String input) {
        Map<String, String> map = new HashMap<>();
        map.put("subCategoryName", input);

        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "create_sub_category");

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            //  Utils.showSuccessAlert(responseBody.get("message").getAsString());
            AlertUtility.AlertSuccessTimeout("Success",responseBody.get("message").getAsString(),inputs->{});
            getSubCategoryData();
        } else {
            //  Utils.showErrorAlertNew(responseBody.get("message").getAsString());
            AlertUtility.AlertErrorTimeout2("Error",responseBody.get("message").getAsString(),inputs->{});
        }
    }

    public void onClickPackage(ActionEvent actionEvent) {

        Stage stage = (Stage) vbox.getScene().getWindow();

        SingleInputDialogs.singleInputDialog("", stage, "Add Package", input -> {
            System.out.println(input);
            addPackage(input);
        });
    }

    private void addPackage(String input) {
        Map<String, String> map = new HashMap<>();
        map.put("packing_name", input);

        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "create_packing");

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            //Utils.showSuccessAlert(responseBody.get("message").getAsString());
            AlertUtility.AlertSuccessTimeout("Success",responseBody.get("message").getAsString(),inputs->{});
            getPackageData();
        } else {
            //Utils.showErrorAlertNew(responseBody.get("message").getAsString());
            AlertUtility.AlertErrorTimeout2("Error",responseBody.get("message").getAsString(),inputs->{});
        }
    }


    public void getBrandData() {

        brandTable.getItems().clear();

        brandAction.setCellFactory(getCheckBox());

        brandColumn.setCellValueFactory(cellData -> {
            if (!cellData.getValue().isEmpty() && cellData.getValue().size() > 1) {
                return cellData.getValue().get(1);
            } else {
                return new SimpleStringProperty("");
            }
        });

        try {

            HttpResponse<String> response = APIClient.getRequest("get_outlet_brands");
            String responseBody = response.body();
            CBrandResDTO cBrandResDTO = new Gson().fromJson(responseBody, CBrandResDTO.class);

            if (cBrandResDTO.getResponseStatus() == 200) {
                List<CBrandListDTO> listg = cBrandResDTO.getResponseObject();

                for (CBrandListDTO cBrandListDTO : listg) {
                    ObservableList<StringProperty> row = FXCollections.observableArrayList(
                            new SimpleStringProperty(cBrandListDTO.getId().toString()),
                            new SimpleStringProperty(cBrandListDTO.getBrandName())
                    );
                    brandTable.getItems().add(row);
                }
                brandTable.refresh();
                lblBrandTotal.setText("" + brandTable.getItems().size());
            }

        } catch (Exception c) {
            c.printStackTrace();
        }
        lblBrandTotal.setText("" + brandTable.getItems().size());

    }

    public void getManufacturerData() {

        manufacturerTable.getItems().clear();

        manufacturerAction.setCellFactory(getCheckBox());

        manufacturerColumn.setCellValueFactory(cellData -> {
            if (!cellData.getValue().isEmpty() && cellData.getValue().size() > 1) {
                return cellData.getValue().get(1);
            } else {
                return new SimpleStringProperty("");
            }
        });

        try {

            HttpResponse<String> response = APIClient.getRequest("get_outlet_groups");
            CManufacturerDTO cManufacturerDTO = new Gson().fromJson(response.body(), CManufacturerDTO.class);
            if (cManufacturerDTO.getResponseStatus() == 200) {
                List<CManufaturerListDTO> list = cManufacturerDTO.getResponseObject();

                for (CManufaturerListDTO cManufaturerListDTO : list) {
                    ObservableList<StringProperty> row = FXCollections.observableArrayList(
                            new SimpleStringProperty(cManufaturerListDTO.getId().toString()),
                            new SimpleStringProperty(cManufaturerListDTO.getGroupName())
                    );
                    manufacturerTable.getItems().add(row);
                }
                manufacturerTable.refresh();
                lblMfgTotal.setText("" + manufacturerTable.getItems().size());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFormulationData() {

        formulationTable.getItems().clear();

        formulationAction.setCellFactory(getCheckBox());

        formulationColumn.setCellValueFactory(cellData -> {
            if (!cellData.getValue().isEmpty() && cellData.getValue().size() > 1) {
                return cellData.getValue().get(1);
            } else {
                return new SimpleStringProperty("");
            }
        });

        try {

            HttpResponse<String> response = APIClient.getRequest("get_outlet_subgroups");
            CFormulationResDTO cFormulationResDTO = new Gson().fromJson(response.body(), CFormulationResDTO.class);
            if (cFormulationResDTO.getResponseStatus() == 200) {
                List<CFourmulationListDTO> list = cFormulationResDTO.getResponseObject();

                for (CFourmulationListDTO cFourmulationListDTO : list) {
                    ObservableList<StringProperty> row = FXCollections.observableArrayList(
                            new SimpleStringProperty(cFourmulationListDTO.getId().toString()),
                            new SimpleStringProperty(cFourmulationListDTO.getSubgroupName())
                    );
                    formulationTable.getItems().add(row);
                }
                formulationTable.refresh();
                lblFormulationTotal.setText("" + formulationTable.getItems().size());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCategoryData() {

        categoryTable.getItems().clear();

        categoryAction.setCellFactory(getCheckBox());

        categoryColumn.setCellValueFactory(cellData -> {
            if (!cellData.getValue().isEmpty() && cellData.getValue().size() > 1) {
                return cellData.getValue().get(1);
            } else {
                return new SimpleStringProperty("");
            }
        });

        try {

            HttpResponse<String> response = APIClient.getRequest("get_outlet_categories");
            CCategoryResDTO cCatalogResDTO = new Gson().fromJson(response.body(), CCategoryResDTO.class);
            if (cCatalogResDTO.getResponseStatus() == 200) {
                List<CCategoryListDTO> list = cCatalogResDTO.getResponseObject();

                for (CCategoryListDTO cCatalogListDTO : list) {
                    ObservableList<StringProperty> row = FXCollections.observableArrayList(
                            new SimpleStringProperty(cCatalogListDTO.getId().toString()),
                            new SimpleStringProperty(cCatalogListDTO.getCategoryName())
                    );
                    categoryTable.getItems().add(row);
                }
                categoryTable.refresh();
                lblCategoryTotal.setText("" + categoryTable.getItems().size());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSubCategoryData() {

        subCategoryTable.getItems().clear();

        subCategoryAction.setCellFactory(getCheckBox());

        subCategoryColumn.setCellValueFactory(cellData -> {
            if (!cellData.getValue().isEmpty() && cellData.getValue().size() > 1) {
                return cellData.getValue().get(1);
            } else {
                return new SimpleStringProperty("");
            }
        });

        try {

            HttpResponse<String> response = APIClient.getRequest("get_outlet_subcategories");
            CSubCatResDTO cSubCatResDTO = new Gson().fromJson(response.body(), CSubCatResDTO.class);
            if (cSubCatResDTO.getResponseStatus() == 200) {
                List<CSubCatListDTO> list = cSubCatResDTO.getResponseObject();

                for (CSubCatListDTO cSubCatListDTO : list) {
                    ObservableList<StringProperty> row = FXCollections.observableArrayList(
                            new SimpleStringProperty(cSubCatListDTO.getId().toString()),
                            new SimpleStringProperty(cSubCatListDTO.getSubcategoryName())
                    );
                    subCategoryTable.getItems().add(row);
                }
                subCategoryTable.refresh();
                lblSubCatTotal.setText("" + subCategoryTable.getItems().size());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPackageData() {
        packageTable.getItems().clear();

        packageAction.setCellFactory(getCheckBox());

        packageColumn.setCellValueFactory(cellData -> {
            if (!cellData.getValue().isEmpty() && cellData.getValue().size() > 1) {
                return cellData.getValue().get(1);
            } else {
                return new SimpleStringProperty("");
            }
        });

        try {

            HttpResponse<String> response = APIClient.getRequest("get_packings");
            CPackageResDTO cPackageResDTO = new Gson().fromJson(response.body(), CPackageResDTO.class);
            if (cPackageResDTO.getResponseStatus() == 200) {
                List<CPackageListDTO> list = cPackageResDTO.getList();
                for (CPackageListDTO cPackageListDTO : list) {
                    ObservableList<StringProperty> row = FXCollections.observableArrayList(
                            new SimpleStringProperty(cPackageListDTO.getId().toString()),
                            new SimpleStringProperty(cPackageListDTO.getName())
                    );
                    packageTable.getItems().add(row);
                }
                packageTable.refresh();
                lblPkgTotal.setText("" + packageTable.getItems().size());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getUnitData() {
        unitTable.getItems().clear();

        unitAction.setCellFactory(getCheckBox());

        unitColumn.setCellValueFactory(cellData -> {
            if (!cellData.getValue().isEmpty() && cellData.getValue().size() > 1) {
                return cellData.getValue().get(1);
            } else {
                return new SimpleStringProperty("");
            }
        });

        try {

            HttpResponse<String> response = APIClient.getRequest("get_units_by_outlet");
            CUnitResDTO cUnitResDTO = new Gson().fromJson(response.body(), CUnitResDTO.class);
            if (cUnitResDTO.getResponseStatus() == 200) {
                List<CUnitListDTO> list = cUnitResDTO.getResponseObject();
                for (CUnitListDTO cUnitListDTO : list) {
                    ObservableList<StringProperty> row = FXCollections.observableArrayList(
                            new SimpleStringProperty(cUnitListDTO.getId().toString()),
                            new SimpleStringProperty(cUnitListDTO.getUnitName()),
                            new SimpleStringProperty(cUnitListDTO.getUnitCode())
                    );
                    unitTable.getItems().add(row);
                }
                unitTable.refresh();
                lblUnitTotal.setText("" + unitTable.getItems().size());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteBrand() {
        Map<String, String> map = new HashMap<>();
        map.put("removebrandlist", selectedRows.toString());

        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.DELETE_BRANDS);

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
        System.out.println("Response in deleteBrand--->" + responseBody);
        if (responseBody.get("responseStatus").getAsInt() == 200) {
            JsonArray usedBrands = responseBody.get("usedBrands").getAsJsonArray();
            if (usedBrands.size() > 0) {
                String msg = usedBrands.get(0).getAsJsonObject().get("message").getAsString();
                //   Utils.showDeleteErrorAlertNew(msg);
//                AlertUtility.AlertTimer("Warning", msg, 2);
                AlertUtility.AlertErrorTimeout2("Error",msg,inputs->{});

            } else {
                JsonArray removedBrands = responseBody.get("removedBrands").getAsJsonArray();
                String msg = removedBrands.get(0).getAsJsonObject().get("message").getAsString();
                //     Utils.showDeleteSuccessAlertNew(msg);
//                AlertUtility.AlertTimer("Warning", msg, 2);
                AlertUtility.AlertSuccessTimeout("Success",msg,inputs->{});
            }
            getBrandData();
            selectedRows.clear();
        }
    }

    public void deleteManufacturer() {
        Map<String, String> map = new HashMap<>();
        map.put("removegrouplist", selectedRows.toString());

        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.DELETE_GROUPS);

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
        System.out.println("Response--->" + responseBody);
        if (responseBody.get("responseStatus").getAsInt() == 200) {
            JsonArray usedGroups = responseBody.get("usedGroups").getAsJsonArray();
            if (usedGroups.size() > 0) {
                String msg = usedGroups.get(0).getAsJsonObject().get("message").getAsString();
//                AlertUtility.AlertTimer("Warning", msg, 2);
                //   Utils.showDeleteErrorAlertNew(msg);
                AlertUtility.AlertErrorTimeout2("Error",msg,inputs->{});
            } else {
                JsonArray removedGroups = responseBody.get("removedGroups").getAsJsonArray();
                String msg = removedGroups.get(0).getAsJsonObject().get("message").getAsString();
//                AlertUtility.AlertTimer("Warning", msg, 2);
                // Utils.showDeleteSuccessAlertNew(msg);
                AlertUtility.AlertSuccessTimeout("Success",msg,inputs->{});
            }
            getManufacturerData();
            selectedRows.clear();
        }
    }

    public void deleteFormulation() {
        Map<String, String> map = new HashMap<>();
        map.put("removesubgrouplist", selectedRows.toString());

        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.DELETE_SUBGROUPS);

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            JsonArray usedSubgroups = responseBody.get("usedsubGroups").getAsJsonArray();
            if (usedSubgroups.size() > 0) {
                String msg = usedSubgroups.get(0).getAsJsonObject().get("message").getAsString();
//                AlertUtility.AlertTimer("Warning", msg, 2);
                //  Utils.showDeleteErrorAlertNew(msg);
                AlertUtility.AlertErrorTimeout2("Error",msg,inputs->{});

            } else {
                JsonArray removedSubGroups = responseBody.get("removedsubGroups").getAsJsonArray();
                String msg = removedSubGroups.get(0).getAsJsonObject().get("message").getAsString();
//                AlertUtility.AlertTimer("Warning", msg, 2);
                //     Utils.showDeleteSuccessAlertNew(msg);
                AlertUtility.AlertSuccessTimeout("Success",msg,inputs->{});
            }
            getFormulationData();
            selectedRows.clear();
        }
    }

    public void deleteCategory() {
        Map<String, String> map = new HashMap<>();
        map.put("removecategorylist", selectedRows.toString());

        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.DELETE_CATEGORIES);

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            JsonArray usedCategory = responseBody.get("usedCategory").getAsJsonArray();
            if (usedCategory.size() > 0) {
                String msg = usedCategory.get(0).getAsJsonObject().get("message").getAsString();
//                AlertUtility.AlertTimer("Warning", msg, 2);
                // Utils.showDeleteErrorAlertNew(msg);
                AlertUtility.AlertErrorTimeout2("Error",msg,inputs->{});

            } else {
                JsonArray removedCategory = responseBody.get("removedCategory").getAsJsonArray();
                String msg = removedCategory.get(0).getAsJsonObject().get("message").getAsString();
//                AlertUtility.AlertTimer("Warning", msg, 2);
//                Utils.showDeleteSuccessAlertNew(msg);
                AlertUtility.AlertSuccessTimeout("Success",msg,inputs->{});
            }
            getCategoryData();
            selectedRows.clear();
        }
    }

    public void deleteSubCategory() {
        Map<String, String> map = new HashMap<>();
        map.put("removesubcategorylist", selectedRows.toString());

        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.DELETE_SUBCATEGORIES);

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            JsonArray usedCategory = responseBody.get("usedCategory").getAsJsonArray();
            if (usedCategory.size() > 0) {
                String msg = usedCategory.get(0).getAsJsonObject().get("message").getAsString();
//                AlertUtility.AlertTimer("Warning", msg, 2);
                // Utils.showDeleteErrorAlertNew(msg);
                AlertUtility.AlertErrorTimeout2("Error",msg,inputs->{});

            } else {
                JsonArray removedCategory = responseBody.get("removedCategory").getAsJsonArray();
                String msg = removedCategory.get(0).getAsJsonObject().get("message").getAsString();
//                AlertUtility.AlertTimer("Warning", msg, 2);
                //       Utils.showDeleteSuccessAlertNew(msg);
                AlertUtility.AlertSuccessTimeout("Success",msg,inputs->{});
            }
            getSubCategoryData();
            selectedRows.clear();
        }
    }

    public void deletePackage() {
        Map<String, String> map = new HashMap<>();
        map.put("removepackageslist", selectedRows.toString());

        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.DELETE_PACKAGE);

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            JsonArray usedArray = responseBody.get("usedArray").getAsJsonArray();
            if (usedArray.size() > 0) {
                String msg = usedArray.get(0).getAsJsonObject().get("message").getAsString();
//                AlertUtility.AlertTimer("Warning", msg, 2);
                // Utils.showDeleteErrorAlertNew(msg);
                AlertUtility.AlertErrorTimeout2("Error",msg,inputs->{});

            } else {
                JsonArray removedArray = responseBody.get("removedArray").getAsJsonArray();
                String msg = removedArray.get(0).getAsJsonObject().get("message").getAsString();
//                AlertUtility.AlertTimer("Warning", msg, 2);
                //  Utils.showDeleteSuccessAlertNew(msg);
                AlertUtility.AlertSuccessTimeout("Success",msg,inputs->{});
            }
            getPackageData();
            selectedRows.clear();
        }
    }

    public void deleteUnit() {
        Map<String, String> map = new HashMap<>();
        map.put("removedunitlist", selectedRows.toString());

        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.DELETE_UNITS);

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            JsonArray usedArray = responseBody.get("usedArray").getAsJsonArray();
            if (usedArray.size() > 0) {
                String msg = usedArray.get(0).getAsJsonObject().get("message").getAsString();
//                AlertUtility.AlertTimer("Warning", msg, 2);
                //    Utils.showDeleteErrorAlertNew(msg);
                AlertUtility.AlertErrorTimeout2("Error",msg,inputs->{});


            } else {
                JsonArray removedArray = responseBody.get("removedArray").getAsJsonArray();
                String msg = removedArray.get(0).getAsJsonObject().get("message").getAsString();
//                AlertUtility.AlertTimer("Warning", msg, 2);
                // Utils.showDeleteSuccessAlertNew(msg);
                AlertUtility.AlertSuccessTimeout("Success",msg,inputs->{});
            }
            getUnitData();
            selectedRows.clear();
        }
    }

    private Callback<TableColumn<ObservableList<StringProperty>, String>, TableCell<ObservableList<StringProperty>, String>> getCheckBox() {
        return new Callback<>() {
            @Override
            public TableCell<ObservableList<StringProperty>, String> call(final TableColumn<ObservableList<StringProperty>, String> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            CheckBox checkBox = new CheckBox();

                            checkBox.setOnAction(event -> {
                                ObservableList<StringProperty> rowData = getTableView().getItems().get(getIndex());
                                String value = rowData.get(0).get();
                                if (checkBox.isSelected()) {
                                    selectedRows.add(value);
                                } else {
                                    selectedRows.remove(value);
                                }
                            });

                            HBox buttonsBox = new HBox(checkBox);
                            buttonsBox.setAlignment(Pos.CENTER);

                            Pane fillerPane = new Pane();
                            HBox.setHgrow(fillerPane, Priority.ALWAYS);

                            HBox hbox = new HBox(buttonsBox, fillerPane);
                            hbox.setSpacing(5);
                            hbox.setAlignment(Pos.CENTER);

                            setGraphic(hbox);
                        }
                    }
                };
            }
        };
    }

    public void onCLickUnit() {

        Stage stage = (Stage) vbox.getScene().getWindow();


        OverlaysEffect.setOverlaysEffect(stage);
        SingleInputDialogs.twoInputDialog("", stage, "Add Unit", list, "", (input1, input2) -> {
            addUnit(input1, input2, list);
        });
    }

    public void addUnit(String input, Object comboBox, ObservableList<Unit> list) {

        Map<String, String> map = new HashMap<>();

        if (comboBox != null) {
            for (Unit unit : list) {
                if (comboBox.toString().equals(unit.getLabel())) {
                    map.put("unitName", input);
                    map.put("unitCode", unit.getValue());
                }
            }
        } else {
            System.out.println("No principle selected.");
        }

        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "create_unit");

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            //Utils.showSuccessAlertNew(responseBody.get("message").getAsString());
            AlertUtility.AlertSuccessTimeout("Success",responseBody.get("message").getAsString(),inputs->{});
            getUnitData();
        } else {
            //  Utils.showErrorAlertNew(responseBody.get("message").getAsString());
            AlertUtility.AlertErrorTimeout2("Error",responseBody.get("message").getAsString(),inputs->{});
        }

    }


//    public void printSelectedRows() {
//        System.out.println("Selected Rows:");
//        for (String rowData : selectedRows) {
//            System.out.println(rowData);
//        }
//
//        System.out.println(selectedRows);
//    }

}


class Unit {
    private String label;
    private String value;

    @Override
    public String toString() {
        return label;
    }

    public Unit(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
