package com.opethic.genivis.controller.master;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.commons.OverlaysEffect;
import com.opethic.genivis.controller.commons.SwitchButton;
import com.opethic.genivis.controller.dialogs.SingleInputDialogs;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.dto.cmp_t_row.BatchWindowTableDTO;
import com.opethic.genivis.dto.pur_invoice.LevelCForPurInvoice;
import com.opethic.genivis.dto.pur_invoice.PurInvoiceCommunicator;
import com.opethic.genivis.dto.pur_invoice.PurchaseInvoiceTable;
import com.opethic.genivis.dto.pur_invoice.UnitForPurInvoice;
import com.opethic.genivis.dto.reqres.ResponseMsg;
import com.opethic.genivis.dto.reqres.catalog.*;
import com.opethic.genivis.dto.reqres.product.*;
import com.opethic.genivis.dto.reqres.pur_tranx.PurTranxToProductRedirectionDTO;
import com.opethic.genivis.dto.reqres.pur_tranx.UnitListDTO;
import com.opethic.genivis.key_manager.ShortCutKey;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.sql_lite.UserToken;
import com.opethic.genivis.utils.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.*;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.PropertySheet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static com.opethic.genivis.utils.FxmFileConstants.*;
import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;
import static javafx.scene.control.PopupControl.USE_PREF_SIZE;


public class ProductCreateController implements Initializable  {
    private String responseBody;
    @FXML
    private TextField tfCode, tfName, tfDiscPer, tfMargin, tfBarcode, tfShelfId, tfManufacturer;
    @FXML
    private TextField tfFormulation, tfCategory, tfSubCategory, tfMinStk, tfMaxStk, tfNoOfDays, tfContents, tfEcommType;
    @FXML
    private TextField tfEcommPrice, tfEcommDisc, tfEcommAmt, tfEcommLoyalty, tfApplicableFrom, tfWarranty;
    @FXML
    private ComboBox<String> cmbTaxType;

/*    @FXML
    private ComboBox<String> cmbGvProduct;*/

    /*private ObservableList<String> gv_product_list = FXCollections.observableArrayList("Owned", "Traded");*/

    @FXML
    private ComboBox<CommonDTO> cmbBrand;

    @FXML
    private ComboBox<CommonDTO> cmbPack;

    @FXML
    private ComboBox<CommonDTO> cmbHsn;

    @FXML
    private ComboBox<CommonDTO> cmbDrugType;

    @FXML
    private ComboBox<CommonDTO> cmbTax;

    @FXML
    private Button btnContents;
    public String productDescription = "";
    @FXML
    private SwitchButton switchMis, switchIsInventory, switchIsSerialNo, switchIsPrescription;
//            , switchIsCommission;
    @FXML
    private SwitchButton switchIsEcomm, switchIsBatch, switchIsWarranty;
//            , switchIsGvProduct;
//    @FXML
//    private RadioButton rbGvOwned, rbTraded;
    @FXML
    private ScrollPane spRootPane;
    @FXML
    private Button imgUpload,imgview;
    private List<File> selectedFiles;
    @FXML
    private Label lbMinStk, lbMaxStk, lbSerialNo, lbWarrenty, lbBatch, lbDrugType, lbDrugTypeDot;
    private ObservableList<CommonDTO> packingNames = FXCollections.observableArrayList();
    private ObservableList<CommonDTO> brandNames = FXCollections.observableArrayList();
    private ObservableList<String> tax_type_list = FXCollections.observableArrayList("Taxable", "TaxPaid", "Exempted");
    private ObservableList<CommonDTO> taxList = FXCollections.observableArrayList();
    private ObservableList<CommonDTO> hsnList = FXCollections.observableArrayList();
    private ObservableList<CommonDTO> drugTypeList = FXCollections.observableArrayList();
    private String cmbTaxTypeText = "taxable", cmbTaxText = "", cmbContentNameText = "", cmbContentPackText = "", contentPowerText = "";
    private String cmbContentTypeText = "";
    private String selectedRadioValue = "owned";
    private Long cmbPackingId, cmbBrandId, cmbTaxId, cmbHsnId, cmbDrugTypeId;
    private Boolean isInventory, isBatch, isSerialNo, isMis, isWarranty, isPrescription, isDruType, isCommision;
    private Boolean isGvProduct, isEcomm;
   // private ToggleGroup toggleGroup = new ToggleGroup();
    private final Logger productLogger = LogManager.getLogger(ProductCreateController.class);

    private ObservableList<CommonDTO> ecommTypeList = FXCollections.observableArrayList();

    private Map<String, String> productMap = new HashMap<>();
    private ObservableList<CommonDTO> manufacturerList = FXCollections.observableArrayList();
    private ObservableList<CommonDTO> formulationList = FXCollections.observableArrayList();
    private ObservableList<CommonDTO> categoryList = FXCollections.observableArrayList();
    private ObservableList<CommonDTO> subCategoryList = FXCollections.observableArrayList();
    private ObservableList<CommonDTO> contentNamesList = FXCollections.observableArrayList();
    private ObservableList<CommonDTO> packageList = FXCollections.observableArrayList();

    private ObservableList<CommonDTO> contentTypeList = FXCollections.observableArrayList();
    private ObservableList<ProductContentsMasterDTO> tbllstContentMasterInfo = FXCollections.observableArrayList();
    private int contentEdtIdx = -1;
    private String inputName = "";
    private String ecommTypeString = "";
    private String[] misDataToSetTexfield = new String[]{null, null, null, null};
    @FXML
    private VBox vboxMis, vboxInventory, vboxBatch, vboxSerialNo, vboxWarranty, vboxPrescription, vboxEcomm;
    private List<ContentMapApiRequest> contentMapApiRequests = new ArrayList<>();
    private Long productId = 0L;
    private HttpResponse<String> productGetByIdResponse = null;
    private ProductMasterDTO productMasterDTO = new ProductMasterDTO();

    //TableView-Initialization-------------------------------------------------------------------------------------------------------------

    @FXML
    private TableView<ProductRowDTO> tvProductRow;

    @FXML
    private TableColumn<ProductRowDTO, String> tcConv;

    @FXML
    private TableColumn<ProductRowDTO, String> tcMRP;

    @FXML
    private TableColumn<ProductRowDTO, String> tcPurRate;

    @FXML
    private TableColumn<ProductRowDTO, String> tcRate1;

    @FXML
    private TableColumn<ProductRowDTO, String> tcRate2;

    @FXML
    private TableColumn<ProductRowDTO, String> tcRate3;

//    @FXML
//    private TableColumn<ProductRowDTO, String> tcRate4;

    @FXML
    private TableColumn<ProductRowDTO, String> tcOpenStock;

    @FXML
    private TableColumn<ProductRowDTO, String> tcNegative;

    @FXML
    private TableColumn<ProductRowDTO, String> tcIsRate;

    @FXML
    private TableColumn<ProductRowDTO, String> tcLevelA;

    @FXML
    private TableColumn<ProductRowDTO, String> tcLevelB;
    @FXML
    private TableColumn<ProductRowDTO, String> tcLevelC;
    @FXML
    private TableColumn<ProductRowDTO, String> tcUnit;


    Map<String, Object> updateResponse = new HashMap<>();

    List<ProductContentMap> contentResponse = new ArrayList<>();

    List<Productrow> productRowsResponse = new ArrayList<>();

    String screen_type = null;

    JSONArray rowDelDetailsIds = new JSONArray();

    List<Integer> removeConten = new ArrayList<>();

    @FXML
    Button btnNamePlus, btnBrandPlus, btnHsnPlus, btnPackingPlus;
    private ObservableList<CommonDTO> levelAList = FXCollections.observableArrayList();

    private ObservableList<CommonDTO> levelBList = FXCollections.observableArrayList();

    private ObservableList<CommonDTO> levelCList = FXCollections.observableArrayList();

    private ObservableList<CommonDTO> unitList = FXCollections.observableArrayList();

//    @FXML
//    Button modify;

    @FXML
    Button btnSubmit,btnCancel;

    @FXML
    Label disc,margin;
    private PurTranxToProductRedirectionDTO prdDTO = null;

    private String redirectSlug = "";

    public static void restrictDateFormat(TextField textField) {
        Pattern pattern = Pattern.compile("\\d*");
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText().replaceAll("\\D", "");
            if (newText.length() > 8 || !pattern.matcher(newText).matches()) {
                return null;
            }
            return change;
        }));

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            String digitsOnly = newValue.replaceAll("\\D", "");
            StringBuilder formattedText = new StringBuilder();

            for (int i = 0; i < digitsOnly.length(); i++) {
                if ((i == 2 || i == 4) && i < digitsOnly.length()) {
                    formattedText.append('/');
                }
                formattedText.append(digitsOnly.charAt(i));
            }
            if (formattedText.length() > 10) {
                formattedText.setLength(10);
            }
            if (!newValue.equals(formattedText.toString())) {
                textField.setText(formattedText.toString());
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        spRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("submit")) {
                    targetButton.getText();
                }
                else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("cancel")) {
                        targetButton.getText();
                }else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("update")) {
                        targetButton.getText();
                }
                else {
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
         if (event.getCode() == KeyCode.S && event.isControlDown()) {
                validate();
            } else if (event.getCode() == KeyCode.X && event.isControlDown()) {
                btnCancel.fire();
            }
        });


      //  CommonValidationsUtils.configureTextFieldToTitleCase(tfName);
      //  CommonValidationsUtils.configureTextFieldToTitleCase(tfName);





        imgUpload.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Images");
            // Add supported image file extensions
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp")
            );

            // Show open multiple file dialog
            List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
            if (files != null) {
                if (files.size() > 5) {
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "You can only Upload up to 5 images.", in -> {});
                } else {
                    selectedFiles = files;
                    for (File file : selectedFiles) {
                    }
                }
            }
        });


        imgview.setOnAction(event -> {
            if (selectedFiles != null && !selectedFiles.isEmpty()) {
                try {
                    viewProductImage(selectedFiles);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "No Images Uploaded", in -> {});
            }
        });




        Platform.runLater(()->{
            tfCode.requestFocus();
        });

//        view.setOnKeyPressed(event->{
//            if (event.getCode() == KeyCode.ENTER) {
//                sub.requestFocus();
//                event.consume();
//            }
//        });


        try {
            productLogger.debug("Get Tax List method called..... ");
            getTaxList();
            getAPICALL();
            tfManufacturer.setFocusTraversable(false);
            tfFormulation.setFocusTraversable(false);
            tfCategory.setFocusTraversable(false);
            tfSubCategory.setFocusTraversable(false);
            tfContents.setFocusTraversable(false);
            tfEcommPrice.setFocusTraversable(false);
            tfEcommDisc.setFocusTraversable(false);
            tfEcommAmt.setFocusTraversable(false);
            tfEcommLoyalty.setFocusTraversable(false);
            tfEcommType.setFocusTraversable(false);

            fourColumnsApiCalling();

           // cmbGvProduct.getSelectionModel().select("Owned");

            cmbTaxType.getSelectionModel().select("Taxable");
            cmbTax.getSelectionModel().select(0);
            String tempS = String.valueOf(cmbTax.getValue());
            for(CommonDTO commonDTO:taxList){
                if(commonDTO.getText().equals(tempS)){
                    cmbTaxId = Long.valueOf(commonDTO.getId());
                    cmbTaxText = commonDTO.getText(); 
                }
            }

           // spRootPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            spRootPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            //Focus on toggle button ----------------------------------------------------------------------------------------------
            switchMis.setParentBox(vboxMis);
            switchIsInventory.setParentBox(vboxInventory);
            switchIsBatch.setParentBox(vboxBatch);
            switchIsSerialNo.setParentBox(vboxSerialNo);
            switchIsWarranty.setParentBox(vboxWarranty);
            switchIsPrescription.setParentBox(vboxPrescription);
//            switchIsCommission.setParentBox(vboxCommission);
//            switchIsGvProduct.setParentBox(vboxGvProduct);
            switchIsEcomm.setParentBox(vboxEcomm);
            //----------------------------------------------------------------------------------------------------------------------
          //  rbGvOwned.setSelected(true);
            tableInitialization();

            tfEcommPrice.setEditable(false);
            tfEcommDisc.setEditable(false);
            tfEcommAmt.setEditable(false);
            tfEcommLoyalty.setEditable(false);
            tfEcommType.setEditable(false);


            tfContents.setEditable(false);

            productLogger.info("Start Initialize method Product Create..... ");
            switchIsInventory.switchOnProperty().set(true);
            switchIsSerialNo.switchOnProperty().set(false);
//            switchIsGvProduct.switchOnProperty().set(true);
            /**** set default false to Warranty bcz Batch option is default true ****/
            tfWarranty.setVisible(false);
            tfWarranty.setManaged(false);
            /**** END ****/
            /**** set default false to DrugType bcz IsPrescription option is default false ****/
            lbDrugType.setVisible(false);
            lbDrugType.setManaged(false);
            lbDrugTypeDot.setVisible(false);
            lbDrugTypeDot.setManaged(false);
            cmbDrugType.setVisible(false);
            cmbDrugType.setManaged(false);
           // btnDrugTypePlus.setVisible(false);
         //   btnDrugTypePlus.setManaged(false);
            /**** add radiobutton into toggle group ****/
         //   rbGvOwned.setToggleGroup(toggleGroup);
          //  rbTraded.setToggleGroup(toggleGroup);
            /**** END ****/

            productLogger.info("checkIsInventory() method called..... ");
            checkIsInventory();
            productLogger.info("checkIsBatch() method called..... ");
            checkIsBatch();
            productLogger.info("checkIsSerialNo() method called..... ");
            checkIsSerialNo();
            productLogger.info("checkIsWarranty() method called..... ");
         //   checkIsWarranty();
            productLogger.info("checkIsPrescription() method called..... ");
            checkIsPrescription();
//            productLogger.info("checkIsGvProduct() method called..... ");
//            checkIsGvProduct();
//            productLogger.info("checkIsCommission() method called..... ");
//            checkIsCommission();
            productLogger.info("checkIsMis() method called..... ");
            checkIsMis();
            productLogger.info("checkIsEcomm() method called..... ");
            checkIsEcomm();
            isBatch = true;
            isInventory = true;
            isGvProduct = false;
            isWarranty = false;
            isMis = false;
            isSerialNo = false;
            isPrescription = false;
            isCommision = false;
            isEcomm = false;
            productMap.put("isEcom", "false");
            tfContents.setText(inputName);
            productId = ProductListController.getProductId();
            if (productId > 0) {
                getProductById(productId);
                btnSubmit.setText("Update");
            } else {
                switchIsBatch.switchOnProperty().set(true);

            }

            Communicator.isBatch = isBatch;

            DateValidator.applyDateFormat(tfApplicableFrom);
            restrictDateFormat(tfApplicableFrom);


            sceneInitilization();

            if(productId<=0) {
                tfCodeValidation();
            }

//            cmbGvProduct.focusedProperty().addListener(new ChangeListener<Boolean>() {
//                @Override
//                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                    if (newValue) {
//                        cmbGvProduct.show();
//                    }
//                }
//            });

//            cmbGvProduct.setOnKeyPressed(event -> {
//                if (event.getCode() == KeyCode.O) {
//                    cmbGvProduct.getSelectionModel().select("Owned");
//                } else if (event.getCode() == KeyCode.T) {
//                    cmbGvProduct.getSelectionModel().select("Traded");
//                }
//                else if (event.getCode() == KeyCode.DOWN) {
//                    cmbGvProduct.show();
//                }
//            });

            CommonValidationsUtils.comboBoxDataShow(cmbTax);
            CommonValidationsUtils.comboBoxDataShow(cmbTaxType);
            cmbTaxType.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        cmbTaxType.show();
                    }
                }
            });



            tfNameValidation();

            cmbPackValidation();

            cmbBrandValidation();

            cmbDrugTypeAddItems();

            cmbTaxTypeValidation();

            cmbTaxValidation();

            cmbHsnValidation();

//            nodetraversal(tfCode, tfName);
//            nodetraversal(tfName, btnNamePlus);
//            nodetraversal(btnNamePlus, cmbPack);
//            nodetraversal(cmbPack, btnPackingPlus);
//            nodetraversal(btnPackingPlus, cmbBrand);
//            nodetraversal(cmbBrand, btnBrandPlus);
//            nodetraversal(btnBrandPlus, cmbTaxType);
//            nodetraversal(cmbTaxType, cmbTax);
//            nodetraversal(cmbTax, tfApplicableFrom);
//            nodetraversal(tfApplicableFrom, cmbHsn);
//            nodetraversal(cmbHsn, btnHsnPlus);
//            nodetraversal(btnHsnPlus, tfDiscPer);
//            nodetraversal(tfDiscPer, tfMargin);
//            nodetraversal(tfMargin, tfBarcode);
//            nodetraversal(tfBarcode, tfShelfId);
//            nodetraversalForNodeToToggle(tfShelfId, switchMis);
//            nodetraversalForToggleToTogle(switchMis, switchIsInventory);
//            if (switchIsInventory.switchOnProperty().get()) {
//                nodetraversalForToggleToNode(switchIsInventory, tfMinStk);
//                nodetraversal(tfMinStk, tfMaxStk);
//                nodetraversalForNodeToToggle(tfMaxStk, switchIsBatch);
//            } else {
//                nodetraversalForToggleToTogle(switchIsInventory, switchIsBatch);
//            }
//            nodetraversalForToggleToTogle(switchIsBatch, switchIsSerialNo);
//            nodetraversalForToggleToTogle(switchIsSerialNo, switchIsWarranty);
//            if (switchIsWarranty.switchOnProperty().get()) {
//                nodetraversalForToggleToNode(switchIsWarranty, tfWarranty);
//            } else {
//                nodetraversalForToggleToTogle(switchIsWarranty, switchIsPrescription);
//            }
//            if (switchIsPrescription.switchOnProperty().get()) {
//                nodetraversalForToggleToNode(switchIsPrescription, cmbDrugType);
//                nodetraversal(cmbDrugType, btnDrugTypePlus);
//                nodetraversal(btnDrugTypePlus, btnContents);
//            } else {
//                nodetraversalForToggleToNode(switchIsPrescription, btnContents);
//            }
//            nodetraversalForNodeToToggle(btnContents, switchIsCommission);
//            nodetraversalForToggleToTogle(switchIsCommission, switchIsGvProduct);
//
//            if (switchIsGvProduct.switchOnProperty().get()) {
//                nodetraversalForToggleToNode(switchIsGvProduct, rbGvOwned);
//                nodetraversalForNodeToToggle(rbGvOwned, switchIsEcomm);
//                nodetraversalForNodeToToggle(rbTraded, switchIsEcomm);
//            } else {
//                nodetraversalForToggleToTogle(switchIsGvProduct, switchIsEcomm);
//            }


            cmbTax.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    cmbTax.show();
                }
            });



        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            productLogger.error("Exception in initialize():" + exceptionAsString);
        }
        productLogger.info("Product IsMis clicked ...");
        getToggleAction();
        productLogger.info("Product IsMis clicked action completed ...");
        tfManufacturer.setEditable(false);
        tfCategory.setEditable(false);
        tfSubCategory.setEditable(false);
        tfFormulation.setEditable(false);
        productLogger.info("Before Content master plus button clicked....");
        btnContents.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showContent();
            }
        });
        productLogger.info("After Content  master plus button clicked action completed....");

        onlyEnterNumbers();

    }

    public void setRedirectData(Object infranchiseDTO) {
        prdDTO = (PurTranxToProductRedirectionDTO) infranchiseDTO;

        redirectSlug = prdDTO.getRedirect();

        System.out.println("HashMap data >> : " + prdDTO.getChallanNo());
        System.out.println("hash" + prdDTO.getRowData());
        System.out.println("hash" + prdDTO.getChallanDate());
        System.out.println("RediPrdCurrIndex in Product >> " + prdDTO.getRediPrdCurrIndex());

        Platform.runLater(()->{
            tfCode.requestFocus();
        });
    }


    private void onlyEnterNumbers() {
        CommonValidationsUtils.onlyEnterNumbers(tfDiscPer);
        CommonValidationsUtils.onlyEnterNumbers(tfMargin);
        CommonValidationsUtils.onlyEnterNumbers(tfBarcode);
        CommonValidationsUtils.onlyEnterNumbers(tfMinStk);
        CommonValidationsUtils.onlyEnterNumbers(tfMaxStk);
    }

    public void onClickCancel() {
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are You Sure to Cancel ?", input -> {
            if (!redirectSlug.isEmpty()) {
                GlobalController.getInstance().addTabStaticWithIsNewTab(redirectSlug, false, prdDTO);
            } else {
                GlobalController.getInstance().addTabStatic(PRODUCT_LIST_SLUG, false);
            }
        });
    }


    private void fourColumnsApiCalling() {

        for (LevelListDTO levelListDTO : getLevelAList()) {
            levelAList.add(new CommonDTO(levelListDTO.getLevelName(), levelListDTO.getId().toString()));
        }

        for (LevelListDTO levelListDTO : getLevelBList()) {
            levelBList.add(new CommonDTO(levelListDTO.getLevelName(), levelListDTO.getId().toString()));
        }

        for (LevelListDTO levelListDTO : getLevelCList()) {
            levelCList.add(new CommonDTO(levelListDTO.getLevelName(), levelListDTO.getId().toString()));
        }

        for (CUnitListDTO unitListDTO : getUnitList()) {
            unitList.add(new CommonDTO(unitListDTO.getUnitName(), unitListDTO.getId().toString()));
        }

    }

    private void getAPICALL() {
        productLogger.debug("Get Packages method called..... ");
        getPackages();
        productLogger.debug("Get Brands method called..... ");
        getBrands();
        productLogger.debug("Get TaxType method called..... ");
        getTaxTypes();

//        getGVProduct();

        productLogger.debug("Get Hsn method called..... ");
        getHsn();
        productLogger.debug("Get DrugType method called..... ");
        getDrugType();
    }

    private void nodetraversal(Node current_node, Node next_node) {
        current_node.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                next_node.requestFocus();
                event.consume();
            }

            if (event.getCode() == KeyCode.SPACE) {
                if (current_node instanceof Button button) {
                    button.fire();
                }
            }
        });
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

    private void nodetraversalForToggleToTogle(SwitchButton current_node, SwitchButton next_node) {
        current_node.getSwitchBtn().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                next_node.requestButtonFocus();
                event.consume();
            }
        });
    }

    private void cmbHsnValidation() {
        cmbHsn.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String s = String.valueOf(cmbHsn.getValue());
                if (s.isEmpty()) {
                    cmbHsn.requestFocus();
                }
                else {
                    String inputText = cmbHsn.getEditor().getText().trim();
                    if (!inputText.isEmpty()) {
                        boolean itemExists = cmbHsn.getItems().stream()
                                .anyMatch(item -> item.toString().equalsIgnoreCase(inputText));
                        System.out.println("test "+inputText);
                        if (!itemExists) {

                            btnHsnClicked(inputText);
                        }
                        else {
                            cmbHsn.getSelectionModel().getSelectedItem();
                        }
                    }
                }
            }

        });
    }

    private void cmbTaxValidation() {
        cmbTax.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String s = String.valueOf(cmbTax.getValue());
                if (s.isEmpty()) {
                    cmbTax.requestFocus();
                }
            }
        });
    }

    private void cmbTaxTypeValidation() {
        cmbTaxType.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String s = String.valueOf(cmbTaxType.getValue());
                if (s.isEmpty()) {
                    cmbTaxType.requestFocus();
                }
            }
        });
    }

    private void cmbPackValidation() {
        cmbPack.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
//                String s = String.valueOf(cmbPack.getValue());
//                if (s.isEmpty()) {
//                    cmbPack.requestFocus();
//                }
//                else {
                    String inputText = cmbPack.getEditor().getText().trim();
                    if (!inputText.isEmpty()) {
                        boolean itemExists = cmbPack.getItems().stream()
                                .anyMatch(item -> item.toString().equalsIgnoreCase(inputText));

                        if (!itemExists) {
                            Stage max =(Stage) spRootPane.getScene().getWindow();
                            CustomConfirmationDialog.ConfirmationDialog(max,"Do you want to add "+cmbPack.getEditor().getText()+"?",output-> {

                                if (output) {
                                            Map<String, String> map = new HashMap<>();
                                            map.put("packing_name", inputText);
                                            HttpResponse<String> response;
                                            String formData = Globals.mapToStringforFormData(map);
                                            response = APIClient.postFormDataRequest(formData, EndPoints.PRODUCT_CREATE_PKG_ENDPOINT);
                                            ResponseMsg responseBody = new Gson().fromJson(response.body(), ResponseMsg.class);
                                            if (responseBody.getResponseStatus() == 200) {
                                                packingNames.add(new CommonDTO(inputText, "" + responseBody.getResponseObject()));
                                                cmbPack.setItems(packingNames);
                                                setComboBoxValue(cmbPack, inputText);
                                            }

                                } else {
                                    cmbPack.requestFocus();
                                }
                            });
                        }
                        else {
                            cmbPack.getSelectionModel().getSelectedItem();
                        }
                    }
//                }
            }
        });
    }

    private void cmbBrandValidation() {
        cmbBrand.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String s = String.valueOf(cmbBrand.getValue());
                if (s.isEmpty()) {
                    cmbBrand.requestFocus();
                }
                else {
                    String inputText = cmbBrand.getEditor().getText().trim();
                    if (!inputText.isEmpty()) {
                        boolean itemExists = cmbBrand.getItems().stream()
                                .anyMatch(item -> item.toString().equalsIgnoreCase(inputText));

                        if (!itemExists) {
                            Stage max =(Stage) spRootPane.getScene().getWindow();
                            CustomConfirmationDialog.ConfirmationDialog(max,"Do you want to add "+cmbBrand.getEditor().getText()+"?",output-> {

                                if (output) {
                                    Map<String, String> map = new HashMap<>();
                                    map.put("brandName", inputText);
                                    HttpResponse<String> response;
                                    String formData = Globals.mapToStringforFormData(map);
                                    response = APIClient.postFormDataRequest(formData, EndPoints.PRODUCT_CREATE_BRAND_ENDPOINT);
                                    ResponseMsg responseBody = new Gson().fromJson(response.body(), ResponseMsg.class);
                                    if (responseBody.getResponseStatus() == 200) {
                                        brandNames.add(new CommonDTO(inputText, "" + responseBody.getResponseObject()));
                                        cmbBrand.setItems(brandNames);
                                        setComboBoxValue(cmbBrand, inputText);
                                    }

                                } else {
                                    cmbBrand.requestFocus();
                                }
                            });
                        }
                        else {
                            cmbBrand.getSelectionModel().getSelectedItem();
                        }
                    }
                }
            }
        });
    }

    private void cmbDrugTypeAddItems() {
        cmbDrugType.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                    String inputText = cmbDrugType.getEditor().getText().trim();
                    if (!inputText.isEmpty()) {
                        boolean itemExists = cmbDrugType.getItems().stream()
                                .anyMatch(item -> item.toString().equalsIgnoreCase(inputText));
                        if (!itemExists) {
                            Stage max =(Stage) spRootPane.getScene().getWindow();
                            CustomConfirmationDialog.ConfirmationDialog(max,"Do you want to add "+cmbDrugType.getEditor().getText()+"?",output-> {

                                if (output) {

                                    Map<String, String> map = new HashMap<>();
                                    map.put("drugName", inputText);
                                    HttpResponse<String> response;
                                    String formData = Globals.mapToStringforFormData(map);
                                    response = APIClient.postFormDataRequest(formData, EndPoints.PRODUCT_CREATE_DRUGTYPE_ENDPOINT);
                                    ResponseMsg responseBody = new Gson().fromJson(response.body(), ResponseMsg.class);
                                    if (responseBody.getResponseStatus() == 200) {
                                        drugTypeList.add(new CommonDTO(inputText, "" + responseBody.getResponseObject()));
                                        cmbDrugType.setItems(drugTypeList);
                                        setComboBoxValue(cmbDrugType, inputText);
                                    }

                                } else {
                                    cmbDrugType.requestFocus();
                                }
                            });
                        }
                        else {
                            cmbDrugType.getSelectionModel().getSelectedItem();
                        }
                    }
            }
        });
    }

    public void tfCodeValidation() {
        tfCode.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (!tfCode.getText().isEmpty()) {
                    try {
                        Map<String, String> map = new HashMap<>();
                        map.put("productCode", tfCode.getText());
                        String s = Globals.mapToStringforFormData(map);
                        HttpResponse<String> response = APIClient.postFormDataRequest(s, "validate_product_code");
                        JSONObject jsonResponse = new JSONObject(response.body());
                        String message = jsonResponse.getString("message");
                        int responseStatus = jsonResponse.getInt("responseStatus");

                        if (responseStatus != 200) {
                            AlertUtility.CustomCallback callback = (number) -> {
                                if (number == 0) {
                                    tfCode.requestFocus();
                                }
                            };
                            AlertUtility.AlertError(Communicator.stage, AlertUtility.alertTypeError, message, callback);
                        }
                    } catch (Exception e) {
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        String exceptionAsString = sw.toString();
                        productLogger.error("Exception in tfCodeValidation:" + exceptionAsString);
                    }
                } else {
                    tfCode.requestFocus();
                }

            }
        });
    }

    public void tfNameValidation() {
        tfName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfName.getText().isEmpty()) {
                    tfName.requestFocus();
                }
            }
        });
    }

    public void sceneInitilization() {
        spRootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }

    public void tableInitialization() {
        tvProductRow.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tvProductRow.setFocusTraversable(false);
        tvProductRow.setEditable(true);

        TableCellCallback<JSONObject> callback = item -> {
            rowDelDetailsIds.put(item);
            System.out.println("rowDelDetailsIds: " + String.valueOf(rowDelDetailsIds));
        };

        TableCellCallback<Boolean> requestFo = item -> {
            if(item){
                Platform.runLater(()->{
                    btnSubmit.requestFocus();
                });

            }
        };

        tvProductRow.getItems().addAll(new ProductRowDTO("0", "", "", "", "", "", "","", "", "", "", "","", "false","false"));


        tcLevelA.setCellValueFactory(cellData -> cellData.getValue().levelAProperty());
        tcLevelA.setCellFactory(column -> new ButtonsAndComboBoxTableCell("level_A", callback, levelAList));

        tcLevelB.setCellValueFactory(cellData -> cellData.getValue().levelBProperty());
        tcLevelB.setCellFactory(column -> new ButtonsAndComboBoxTableCell("level_B", callback, levelBList));

        tcLevelC.setCellValueFactory(cellData -> cellData.getValue().levelCProperty());
        tcLevelC.setCellFactory(column -> new ButtonsAndComboBoxTableCell("level_C", callback, levelCList));

        tcUnit.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
       // tcUnit.setCellFactory(column -> new ButtonsAndComboBoxTableCell("unit", callback, unitList));
        tcUnit.setCellFactory(column -> new UnitCell("unit",unitList,requestFo));

        tcConv.setCellValueFactory(cellData -> cellData.getValue().convProperty());
        tcConv.setCellFactory(column -> new TextFieldTableCell("conv"));

        tcMRP.setCellValueFactory(cellData -> cellData.getValue().mrpProperty());
        tcMRP.setCellFactory(column -> new TextFieldTableCell("mrp"));

        tcPurRate.setCellValueFactory(cellData -> cellData.getValue().purRateProperty());
        tcPurRate.setCellFactory(column -> new TextFieldTableCell("pur_rate"));

        tcRate1.setCellValueFactory(cellData -> cellData.getValue().rate_1Property());
        tcRate1.setCellFactory(column -> new TextFieldTableCell("rate_1"));

        tcRate2.setCellValueFactory(cellData -> cellData.getValue().rate_2Property());
        tcRate2.setCellFactory(column -> new TextFieldTableCell("rate_2"));

        tcRate3.setCellValueFactory(cellData -> cellData.getValue().rate_3Property());
        tcRate3.setCellFactory(column -> new TextFieldTableCell("rate_3"));

/*        tcRate4.setCellValueFactory(cellData -> cellData.getValue().rate_4Property());
        tcRate4.setCellFactory(column -> new TextFieldTableCell("rate_4"));*/


        tcOpenStock.setVisible(false);

        tcOpenStock.setCellValueFactory(cellData -> cellData.getValue().opnStockProperty());
        tcOpenStock.setCellFactory(column -> new ButtonAndTextFieldTableCell("opn_stock", spRootPane));

        tcNegative.setCellValueFactory(cellData -> cellData.getValue().is_negetiveProperty());
        tcNegative.setCellFactory(column -> new ToggleTableCell("is_negetive"));

        tcIsRate.setCellValueFactory(cellData -> cellData.getValue().is_rateProperty());
        tcIsRate.setCellFactory(column -> new ToggleTableCell("is_rate"));

//        columnVisibility(tcLevelA,Globals.getUserControlsWithSlug("is_level_a"));
//        columnVisibility(tcLevelB,Globals.getUserControlsWithSlug("is_level_b"));
//        columnVisibility(tcLevelC,Globals.getUserControlsWithSlug("is_level_c"));
//
//        tcLevelA.setText(Globals.getUserControlsNameWithSlug("is_level_a") + "");
//        tcLevelB.setText(Globals.getUserControlsNameWithSlug("is_level_B") + "");
//        tcLevelC.setText(Globals.getUserControlsNameWithSlug("is_level_C") + "");

        columnVisibility(tcLevelA,false);
        columnVisibility(tcLevelB,false);
        columnVisibility(tcLevelC,false);

    }

    private void columnVisibility(TableColumn<ProductRowDTO, String> column, boolean visible) {
        if (visible) {
            column.setPrefWidth(USE_COMPUTED_SIZE);
            column.setMinWidth(USE_PREF_SIZE);
            column.setMaxWidth(Double.MAX_VALUE);
        } else {
            column.setPrefWidth(0);
            column.setMinWidth(0);
            column.setMaxWidth(0);
        }
    }

    private void checkIsEcomm() {
        switchIsEcomm.switchOnProperty().addListener((observable, oldValue, newValue) -> {
            isEcomm = newValue;
        });
    }

    private void checkIsMis() {
        switchMis.switchOnProperty().addListener((observable, oldValue, newValue) -> {
            productMap.put("isMIS", String.valueOf(newValue));
        });
    }

  /*  private void checkIsCommission() {
        switchIsCommission.switchOnProperty().addListener((observable, oldValue, newValue) -> {
            //productMap.put("isCommision", String.valueOf(newValue));
            isCommision = newValue;
        });
    }*/

/*    private void checkIsGvProduct() {
        switchIsGvProduct.switchOnProperty().addListener((observable, oldValue, newValue) -> {
            isGvProduct = newValue;
            if (newValue) {
                cmbGvProduct.setVisible(true);
                cmbGvProduct.setManaged(true);
//                rbGvOwned.setVisible(true);
//                rbGvOwned.setManaged(true);
//                rbTraded.setVisible(true);
//                rbTraded.setManaged(true);
//                nodetraversalForToggleToNode(switchIsGvProduct, rbGvOwned);
            } else {
                cmbGvProduct.setVisible(false);
                cmbGvProduct.setManaged(false);
//                rbGvOwned.setVisible(false);
//                rbGvOwned.setManaged(false);
//                rbTraded.setVisible(false);
//                rbTraded.setManaged(false);
//                nodetraversalForToggleToTogle(switchIsGvProduct, switchIsEcomm);
            }
        });
    }*/

    /**
     * @version sprint_migration_fx 01
     * DateTime: 18-03-2024:11:40:00
     * @implNote Toggle Button ON and OFF functionality
     * @auther ashwins@opethic.com
     **/
    private void checkIsInventory() {
        switchIsInventory.switchOnProperty().addListener((observable, oldValue, newValue) -> {
            isInventory = newValue;
            if (newValue) {
                lbMinStk.setVisible(true);
                lbMinStk.setManaged(true);
                lbMaxStk.setVisible(true);
                lbMaxStk.setManaged(true);
                tfMinStk.setVisible(true);
                tfMinStk.setManaged(true);
                tfMaxStk.setVisible(true);
                tfMaxStk.setManaged(true);
                nodetraversalForToggleToNode(switchIsInventory, tfMinStk);
                nodetraversal(tfMinStk, tfMaxStk);
                nodetraversalForNodeToToggle(tfMaxStk, switchIsBatch);
            } else {
                lbMinStk.setVisible(false);
                lbMinStk.setManaged(false);
                lbMaxStk.setVisible(false);
                lbMaxStk.setManaged(false);
                tfMinStk.setVisible(false);
                tfMinStk.setManaged(false);
                tfMaxStk.setVisible(false);
                tfMaxStk.setManaged(false);
                nodetraversalForToggleToTogle(switchIsInventory, switchIsBatch);
            }
        });
    }

    /**
     * @version sprint_migration_fx 01
     * DateTime: 18-03-2024:11:40:00
     * @implNote Toggle Button ON and OFF functionality
     * @auther ashwins@opethic.com
     **/
    private void checkIsBatch() {
        switchIsBatch.switchOnProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                isBatch = true;
                isSerialNo = false;
//                switchIsBatch.switchOnProperty().set(true);
                switchIsSerialNo.switchOnProperty().set(false);


             //   Communicator.isBatch = isBatch;

            } else {
                isBatch = false;
//                switchIsSerialNo.switchOnProperty().set(true);
//                switchIsBatch.switchOnProperty().set(false);

        //        Communicator.isBatch = isBatch;

            }
        });

    }

    //these methods are requiere for isBatch .............................................


    /**
     * @version sprint_migration_fx 01
     * DateTime: 18-03-2024:11:40:00
     * @implNote Toggle Button ON and OFF functionality
     * @auther ashwins@opethic.com
     **/
    private void checkIsSerialNo() {
        switchIsSerialNo.switchOnProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue) {
                isBatch = false;
                isSerialNo = true;
                switchIsBatch.switchOnProperty().set(false);


                switchIsWarranty.getSwitchBtn().setDisable(false);
                switchIsWarranty.setOnMouseClicked(e -> {
                    switchIsWarranty.requestButtonFocus();
                    boolean warrantyState = !switchIsWarranty.switchOnProperty().get();
                    switchIsWarranty.switchOnProperty().set(warrantyState);
                });

                switchIsWarranty.switchOnProperty().addListener((obs, wasOn, isOn) -> {
                    isWarranty = isOn;
                    tfWarranty.setVisible(isOn);
                    tfWarranty.setManaged(isOn);
                });

            } else {
                isSerialNo = false;

                tfWarranty.setVisible(false);
                tfWarranty.setManaged(false);

                switchIsWarranty.getSwitchBtn().setDisable(true);
                switchIsWarranty.setOnMouseClicked(null);
                switchIsWarranty.switchOnProperty().set(false);
            }
        });

        if(!switchIsSerialNo.switchOnProperty().get()){
            switchIsWarranty.getSwitchBtn().setDisable(true);
            switchIsWarranty.setOnMouseClicked(null);
            switchIsWarranty.switchOnProperty().set(false);
        }
    }

    /**
     * @version sprint_migration_fx 01
     * DateTime: 18-03-2024:11:40:00
     * @implNote Toggle Button ON and OFF functionality
     * @auther ashwins@opethic.com
     **/
    private void checkIsWarranty() {
        switchIsWarranty.switchOnProperty().addListener((observable, oldValue, newValue) -> {
            isWarranty = newValue;
            if (newValue) {
                tfWarranty.setVisible(true);
                tfWarranty.setManaged(true);
                nodetraversalForToggleToNode(switchIsWarranty, tfWarranty);
            } else {
                tfWarranty.setVisible(false);
                tfWarranty.setManaged(false);
                nodetraversalForToggleToTogle(switchIsWarranty, switchIsPrescription);
            }
        });
    }

    /**
     * @version sprint_migration_fx 01
     * DateTime: 19-03-2024:11:48:00
     * @implNote Toggle Button ON and OFF functionality
     * @auther ashwins@opethic.com
     **/
    private void checkIsPrescription() {
        switchIsPrescription.switchOnProperty().addListener((observable, oldValue, newValue) -> {
            isPrescription = newValue;
            if (newValue) {
                lbDrugType.setVisible(true);
                lbDrugType.setManaged(true);
                lbDrugTypeDot.setVisible(true);
                lbDrugTypeDot.setManaged(true);
                cmbDrugType.setVisible(true);
                cmbDrugType.setManaged(true);
                //btnDrugTypePlus.setVisible(true);
             //   btnDrugTypePlus.setManaged(true);
               // nodetraversalForToggleToNode(switchIsPrescription, cmbDrugType);
             //   nodetraversal(cmbDrugType, btnDrugTypePlus);
             //   nodetraversal(btnDrugTypePlus, btnContents);
            } else {
                lbDrugType.setVisible(false);
                lbDrugType.setManaged(false);
                lbDrugTypeDot.setVisible(false);
                lbDrugTypeDot.setManaged(false);
                cmbDrugType.setVisible(false);
                cmbDrugType.setManaged(false);
            //    btnDrugTypePlus.setVisible(false);
            //    btnDrugTypePlus.setManaged(false);
            //    nodetraversalForToggleToNode(switchIsPrescription, btnContents);
            }
        });

    }

    /**
     * @version sprint_migration_fx 01
     * DateTime: 18-03-2024:10:18:00
     * @implNote to get selected value of packing on Action Event of Packing combobox
     * @auther ashwins@opethic.com
     **/
    public void handleCmbPacking(ActionEvent actionEvent) {
        Object pack[] = new Object[1];
        pack[0] = cmbPack.getSelectionModel().getSelectedItem();
        if (pack[0] != null) {

            for (CommonDTO commonDTO : packingNames) {
                if (pack[0].equals(commonDTO)) {
                    cmbPackingId = Long.valueOf(commonDTO.getId());
                }
            }
        }
    }

    /**
     * @version sprint_migration_fx 01
     * DateTime: 18-03-2024:10:18:00
     * @implNote to get selected value of packing on Action Event of Brand combobox
     * @auther ashwins@opethic.com
     **/
    public void handleCmbBrand(ActionEvent actionEvent) {
        Object object[] = new Object[1];
        object[0] = cmbBrand.getSelectionModel().getSelectedItem();
        if (object[0] != null) {
            for (CommonDTO commonDTO : brandNames) {
                if (object[0].equals(commonDTO)) {
                    cmbBrandId = Long.valueOf(commonDTO.getId());
                }
            }
        }
    }


    /**
     * @version sprint_migration_fx 01
     * DateTime: 18-03-2024:10:18:00
     * @implNote to get selected value of packing on Action Event of TaxType combobox
     * @auther ashwins@opethic.com
     **/
//    public void handleCmbGVProduct(ActionEvent actionEvent) {
//        selectedRadioValue = cmbGvProduct.getValue();
//
//    }
    public void handleCmbTaxType(ActionEvent actionEvent) {
        cmbTaxTypeText = cmbTaxType.getValue();

    }

    /**
     * @version sprint_migration_fx 01
     * DateTime: 18-03-2024:10:18:00
     * @implNote to get selected value on Action Event of Tax combobox
     * @auther ashwins@opethic.com
     **/
    public void handleCmbTax(ActionEvent actionEvent) {
        Object[] object = new Object[1];
        object[0] = cmbTax.getSelectionModel().getSelectedItem();
        if (object[0] != null) {
            for (CommonDTO commonDTO : taxList) {
                if (object[0].equals(commonDTO)) {
                    cmbTaxId = Long.valueOf(commonDTO.getId());
                    cmbTaxText = commonDTO.getText();
                }
            }
        }
    }

    /**
     * @version sprint_migration_fx 01
     * DateTime: 18-03-2024:10:18:00
     * @implNote to get selected value of packing on Action Event of Hsn combobox
     * @auther ashwins@opethic.com
     **/
    public void handleCmbHsn(ActionEvent actionEvent) {
        Object object[] = new Object[1];
        object[0] = cmbHsn.getSelectionModel().getSelectedItem();
        if (object[0] != null) {
            for (CommonDTO commonDTO : hsnList) {
                if (object[0].equals(commonDTO)) {
                    cmbHsnId = Long.valueOf(commonDTO.getId());
                }
            }
        }
    }

    /**
     * @version sprint_migration_fx 01
     * DateTime: 18-03-2024:10:18:00
     * @implNote API calling to get Hsn List
     * @auther ashwins@opethic.com
     **/
    private void getHsn() {
       /* try {
            HttpResponse<String> response = APIClient.getRequest(EndPoints.PRODUCT_GET_HSN_ENDPOINT);
            responseBody = response.body();
            HsnResDTO hsnDTO = new Gson().fromJson(responseBody, HsnResDTO.class);
            if (hsnDTO.getResponseStatus() == 200) {
                List<HsnListDTO> listDTOS = hsnDTO.getResponseObject();

                System.out.println("list Hsn:" + listDTOS);
                for (HsnListDTO mElement : listDTOS) {
                    hsnList.add(new CommonDTO(mElement.getHsnNo(), mElement.getId().toString()));
                }
                cmbHsn.setItems(hsnList);
                AutoCompleteBox<CommonDTO> s = new AutoCompleteBox<>(cmbHsn, -1);
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            productLogger.error("Exception in getHsn():" + exceptionAsString);
        }*/
        APIClient apiClient = null;
        try {
//            System.out.println("Hsn--->" + Globals.headerBranch);
            apiClient = new APIClient(EndPoints.PRODUCT_GET_HSN_ENDPOINT, "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    productLogger.info("getHsn()---> on Succeeded ");
//                    System.out.println("getHsn()---> on Succeeded ");

                    String hsnRes = workerStateEvent.getSource().getValue().toString();
                    HsnResDTO hsnDTO = new Gson().fromJson(hsnRes, HsnResDTO.class);
                    if (hsnDTO.getResponseStatus() == 200) {
                        List<HsnListDTO> listDTOS = hsnDTO.getResponseObject();
                       /* for (HsnListDTO mElement : listDTOS) {
                            hsnList.add(new CommonDTO(mElement.getHsnNo(), mElement.getId().toString()));
                        }*/
//                        System.out.println("---> " + listDTOS.size());
                        listDTOS.stream().forEach(p -> {
                            hsnList.add(new CommonDTO(p.getHsnNo(), p.getId().toString()));
                        });

                    }
                }
            });

            cmbHsn.setItems(hsnList);
            AutoCompleteBox<CommonDTO> s = new AutoCompleteBox<>(cmbHsn, -1);
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    productLogger.error("Network API cancelled in getHsn()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    productLogger.error("Network API failed in getHsn()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            productLogger.error("Exception in getHsn():" + exceptionAsString);
        } finally {
            apiClient = null;
        }
    }


    /**
     * @version sprint_migration_fx 01
     * DateTime: 18-03-2024:10:18:00
     * @implNote API calling to get Tax List
     * @auther ashwins@opethic.com
     **/
    private void getTaxList() {
        try {
            HttpResponse<String> response = APIClient.getRequest(EndPoints.PRODUCT_GET_TAX_ENDPOINT);
            responseBody = response.body();
            TaxDTO taxDTO = new Gson().fromJson(responseBody, TaxDTO.class);
            if (taxDTO.getResponseStatus() == 200) {
                List<TaxListDTO> listDTOS = taxDTO.getResponseObject();
                for (TaxListDTO mElement : listDTOS) {
                    taxList.add(new CommonDTO(mElement.getGstPer(), "" + mElement.getId()));
                }
                cmbTax.setItems(taxList);
              //  AutoCompleteBox<CommonDTO> s = new AutoCompleteBox<>(cmbTax, -1);
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            productLogger.error("Exception in getTaxList():" + exceptionAsString);
        }
//        APIClient apiClient = null;
//        try {
////            System.out.println("Tax-->" + Globals.headerBranch);
//            apiClient = new APIClient(EndPoints.PRODUCT_GET_TAX_ENDPOINT, "", RequestType.GET);
//            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
//                @Override
//                public void handle(WorkerStateEvent workerStateEvent) {
//                    productLogger.info("getTaxList()---> on Succeeded ");
////                    System.out.println("getTaxList()---> on Succeeded ");
//
//                    String taxRes = workerStateEvent.getSource().getValue().toString();
//                    TaxDTO taxDTO = new Gson().fromJson(taxRes, TaxDTO.class);
//                    if (taxDTO.getResponseStatus() == 200) {
//                        List<TaxListDTO> listDTOS = taxDTO.getResponseObject();
//                       /* for (TaxListDTO mElement : listDTOS) {
//                            taxList.add(new CommonDTO(mElement.getGstPer(), "" + mElement.getId()));
//                        }*/
////                        System.out.println("---> " + listDTOS.size());
//                        listDTOS.stream().forEach(p -> {
//                            taxList.add(new CommonDTO(p.getGstPer(), p.getId().toString()));
//                        });
//                        System.out.println("tax list API"+taxList);
//                    }
//                }
//            });
//
//            cmbTax.setItems(taxList);
//            System.out.println("tax list CMB"+cmbTax.getItems());
//            AutoCompleteBox<CommonDTO> s = new AutoCompleteBox<>(cmbTax, -1);
//
//            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
//                @Override
//                public void handle(WorkerStateEvent workerStateEvent) {
//                    productLogger.error("Network API cancelled in getTaxList()" + workerStateEvent.getSource().getValue().toString());
//                }
//            });
//            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
//                @Override
//                public void handle(WorkerStateEvent workerStateEvent) {
//                    productLogger.error("Network API failed in getTaxList()" + workerStateEvent.getSource().getValue().toString());
//                }
//            });
//            apiClient.start();
//        } catch (Exception e) {
//            StringWriter sw = new StringWriter();
//            e.printStackTrace(new PrintWriter(sw));
//            String exceptionAsString = sw.toString();
//            productLogger.error("Exception in getTaxList():" + exceptionAsString);
//        } finally {
//            apiClient = null;
//        }
    }


    /**
     * @version sprint_migration_fx 01
     * DateTime: 18-03-2024:10:18:00
     * @implNote API calling to get Tax Type List
     * @auther ashwins@opethic.com
     **/
//    private void getGVProduct() {
//        cmbGvProduct.setItems(gv_product_list);
//    }
    private void getTaxTypes() {
        cmbTaxType.setItems(tax_type_list);
       // AutoCompleteBox<String> s = new AutoCompleteBox<>(cmbTaxType, -1);
    }

    /**
     * @version sprint_migration_fx 01
     * DateTime: 18-03-2024:10:18:00
     * @implNote API calling to get Brand List
     * @auther ashwins@opethic.com
     **/
    private void getBrands() {
      /*  try {

            HttpResponse<String> response = APIClient.getRequest(EndPoints.PRODUCT_GET_BRAND_ENDPOINT);
            responseBody = response.body();
            BrandResDTO brandResDTO = new Gson().fromJson(responseBody, BrandResDTO.class);
            if (brandResDTO.getResponseStatus() == 200) {
                List<BrandListDTO> brandListDTOS = brandResDTO.getResponseObject();
                for (BrandListDTO mElement : brandListDTOS) {
                    brandNames.add(new CommonDTO(mElement.getBrandName(), mElement.getId().toString()));
                }
                cmbBrand.setItems(brandNames);

                AutoCompleteBox<CommonDTO> autoCompleteBox = new AutoCompleteBox<>(cmbBrand, -1);

            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            productLogger.error("Exception in getBrands():" + exceptionAsString);
        }*/
        APIClient apiClient = null;
        try {

            apiClient = new APIClient(EndPoints.PRODUCT_GET_BRAND_ENDPOINT, "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    productLogger.info("getBrands()---> on Succeeded ");
//                    System.out.println("getBrands()---> on Succeeded ");
                    String brandRes = workerStateEvent.getSource().getValue().toString();
                    System.out.println("Brand Res--->" + brandRes);
                    BrandResDTO brandResDTO = new Gson().fromJson(brandRes, BrandResDTO.class);
                    if (brandResDTO.getResponseStatus() == 200) {
                        List<BrandListDTO> brandListDTOS = brandResDTO.getResponseObject();
                     /*   for (BrandListDTO mElement : brandListDTOS) {
                            brandNames.add(new CommonDTO(mElement.getBrandName(), mElement.getId().toString()));
                            System.out.println("Brand Names:" + mElement.getBrandName());
                        }*/
                        // For each Times, we want to retrieve the corresponding Product Id from the productRowList list. We'll use the
                        // Java 8 Streams API to do this
                        /*  System.out.println("---> " + brandListDTOS.size());*/
                        brandListDTOS.stream().forEach(p -> {
                            brandNames.add(new CommonDTO(p.getBrandName(), p.getId().toString()));
                        });

                    }
                }
            });
            cmbBrand.setItems(brandNames);
            AutoCompleteBox<CommonDTO> autoCompleteBox = new AutoCompleteBox<>(cmbBrand, -1);
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    productLogger.error("Network API cancelled in getBrands()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    productLogger.error("Network API failed in getBrands()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            productLogger.error("Exception in getBrands():" + exceptionAsString);
        } finally {
            apiClient = null;
        }
    }

    /**
     * @version sprint_migration_fx 01
     * DateTime: 18-03-2024:10:18:00
     * @implNote API calling to get Package List
     * @auther ashwins@opethic.com
     **/
    private void getPackages() {
        APIClient apiClient = null;
        try {
//            System.out.println("Package-->" + Globals.headerBranch);
            apiClient = new APIClient(EndPoints.PRODUCT_GET_PACKING_ENDPOINT, "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    productLogger.info("getPackages()---> on Succeeded ");
//                    System.out.println("getPackages()---> on Succeeded ");
                    String packRes = workerStateEvent.getSource().getValue().toString();
                    PackageResDTO resDTO = new Gson().fromJson(packRes, PackageResDTO.class);
                    if (resDTO.getResponseStatus() == 200) {
                        List<PackageListData> list = resDTO.getList();
                       /* for (PackageListData mElement : list) {
                            packingNames.add(new CommonDTO(mElement.getName(), mElement.getId().toString()));
                        }*/
//                        System.out.println("---->" + list.size());
                        list.stream().forEach(p -> {
                            packingNames.add(new CommonDTO(p.getName(), p.getId().toString()));
                        });
                    }
                }
            });

            cmbPack.setItems(packingNames);
            AutoCompleteBox<CommonDTO> autoCompleteBox = new AutoCompleteBox<>(cmbPack, -1);
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    productLogger.error("Network API cancelled in getPackages()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    productLogger.error("Network API failed in getPackages()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            productLogger.error("Exception in getPackages():" + exceptionAsString);
        } finally {
            apiClient = null;

        }
    }


    /**** Used to maintain the state of textField in dialog box i.e SingleInputDialogs *****/
//    public static String getDescription() {
//        return productDescription;
//    }

    /**
     * @version sprint_migration_fx 01
     * DateTime: 18-03-2024:10:18:00
     * @implNote to get selected value of packing on Action Event of Drug type combobox
     * @auther ashwins@opethic.com
     **/
    public void handleCmbDrugType(ActionEvent actionEvent) {
        Object[] object = new Object[1];
        object[0] = cmbDrugType.getSelectionModel().getSelectedItem();
        if (object[0] != null) {
            for (CommonDTO commonDTO : drugTypeList) {
                if (object[0].equals(commonDTO)) {
                    cmbDrugTypeId = Long.valueOf(commonDTO.getId());
                }
            }
        }
    }


    /**
     * @version sprint_migration_fx 01
     * DateTime: 20-03-2024:16:11:00
     * @implNote API calling to get DrugType List
     * @auther ashwins@opethic.com
     **/
    private void getDrugType() {
//        System.out.println("Drug Type" + Globals.headerBranch);
       /* try {
            HttpResponse<String> response = APIClient.getRequest(EndPoints.PRODUCT_GET_DRUGTYPE_ENDPOINT);
            responseBody = response.body();
            DrugTypeResDTO drugType = new Gson().fromJson(responseBody, DrugTypeResDTO.class);
            if (drugType.getResponseStatus() == 200) {
                List<DrugTypeListDTO> listDTOS = drugType.getResponseObject();
                for (DrugTypeListDTO mElement : listDTOS) {
                    drugTypeList.add(new CommonDTO(mElement.getDrugName(), mElement.getId().toString()));
                }
                cmbDrugType.setItems(drugTypeList);
                AutoCompleteBox<CommonDTO> s = new AutoCompleteBox<>(cmbDrugType, -1);
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            productLogger.error("Exception in getDrugType():" + exceptionAsString);
        }*/
        APIClient apiClient = null;
        try {
            apiClient = new APIClient(EndPoints.PRODUCT_GET_DRUGTYPE_ENDPOINT, "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    productLogger.info("getDrugType()---> on Succeeded ");
//                    System.out.println("getDrugType()---> on Succeeded");
                    String drugTypeRes = workerStateEvent.getSource().getValue().toString();
                    DrugTypeResDTO drugType = new Gson().fromJson(drugTypeRes, DrugTypeResDTO.class);
                    if (drugType.getResponseStatus() == 200) {
                        List<DrugTypeListDTO> listDTOS = drugType.getResponseObject();
                       /* for (DrugTypeListDTO mElement : listDTOS) {
                            drugTypeList.add(new CommonDTO(mElement.getDrugName(), mElement.getId().toString()));
                        }*/
//                        System.out.println("---> " + listDTOS.size());
                        listDTOS.stream().forEach(p -> {
                            drugTypeList.add(new CommonDTO(p.getDrugName(), p.getId().toString()));
                        });

                    }
                }
            });

            cmbDrugType.setItems(drugTypeList);
            AutoCompleteBox<CommonDTO> s = new AutoCompleteBox<>(cmbDrugType, -1);
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    productLogger.error("Network API cancelled in getDrugType()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    productLogger.error("Network API failed in getDrugType()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            productLogger.error("Exception in getDrugType():" + exceptionAsString);
        } finally {
            apiClient = null;
        }
    }

    /**
     * @version sprint_migration_fx 01
     * DateTime: 18-03-2024:10:55:00
     * @implNote click event of plus button of product description
     * @auther ashwins@opethic.com
     **/
    public void btnNameClicked(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) spRootPane.getScene().getWindow();
        SingleInputDialogs.singleInputDialog(productDescription, stage, "Add Description", input -> {
            productDescription = input;
        });
    }
    public void viewProductImage(List<File> imageFiles) throws IOException {
        Stage stage = (Stage) spRootPane.getScene().getWindow();
        SingleInputDialogs.singleInputDialogForImage(imageFiles, stage, "View Images", input -> {
            System.out.println("Modal Aala re");
            // Update the selected images or handle the callback as needed
        });
    }


    /**
     * @version sprint_migration_fx 01
     * DateTime: 18-03-2024:10:55:00
     * @implNote this function select inserted (into DB) value in Combo box of package on click event of plus button
     * @auther ashwins@opethic.com
     **/
//    public void btnPkgClicked(ActionEvent actionEvent) {
//        try {
//            Stage stage = (Stage) spRootPane.getScene().getWindow();
//            SingleInputDialogs.singleInputDialog("", stage, "Add New Package", input -> {
//
//                if(input!=null && !input.isEmpty()) {
//                    Map<String, String> map = new HashMap<>();
//                    map.put("packing_name", input);
//                    HttpResponse<String> response;
//                    String formData = Globals.mapToStringforFormData(map);
//                    response = APIClient.postFormDataRequest(formData, EndPoints.PRODUCT_CREATE_PKG_ENDPOINT);
//                    ResponseMsg responseBody = new Gson().fromJson(response.body(), ResponseMsg.class);
//                    if (responseBody.getResponseStatus() == 200) {
//                        packingNames.add(new CommonDTO(input, "" + responseBody.getResponseObject()));
//                        cmbPack.setItems(packingNames);
//                        setComboBoxValue(cmbPack, input);
//                    }
//                }
//                else{
//                    Stage stage1 = (Stage) spRootPane.getScene().getWindow();
//                    AlertUtility.AlertDialogForErrorWithStage(stage1,"Error","Packing Required",output->{
//                        if(output){
//                            btnPackingPlus.requestFocus();
//                        }
//                    });
//                }
//            });
//        } catch (Exception e) {
//            StringWriter sw = new StringWriter();
//            e.printStackTrace(new PrintWriter(sw));
//            String exceptionAsString = sw.toString();
//            productLogger.error("Exception in btnPkgClicked():" + exceptionAsString);
//        }
//    }


    /**
     * @version sprint_migration_fx 01
     * DateTime: 18-03-2024:11:05:00
     * @implNote this function select inserted (into DB) value in Combo box of brand on click event of plus button
     * @auther ashwins@opethic.com
     **/
//    public void btnBrandClicked(ActionEvent actionEvent) {
//        try {
//            Stage stage = (Stage) spRootPane.getScene().getWindow();
//            SingleInputDialogs.singleInputDialog("", stage, "Add New Brand", input -> {
//
//                if(input!=null && !input.isEmpty()) {
//                    Map<String, String> map = new HashMap<>();
//                    map.put("brandName", input);
//                    HttpResponse<String> response;
//                    String formData = Globals.mapToStringforFormData(map);
//                    response = APIClient.postFormDataRequest(formData, EndPoints.PRODUCT_CREATE_BRAND_ENDPOINT);
//                    ResponseMsg responseBody = new Gson().fromJson(response.body(), ResponseMsg.class);
//                    if (responseBody.getResponseStatus() == 200) {
//                        brandNames.add(new CommonDTO(input, "" + responseBody.getResponseObject()));
//                        cmbBrand.setItems(brandNames);
//                        setComboBoxValue(cmbBrand, input);
//                    }
//                }
//                else{
//                    Stage stage1 = (Stage) spRootPane.getScene().getWindow();
//                    AlertUtility.AlertDialogForErrorWithStage(stage1,"Error","Brand Required",output->{
//                        if(output){
//                            btnBrandPlus.requestFocus();
//                        }
//                    });
//                }
//            });
//
//        } catch (Exception e) {
//            StringWriter sw = new StringWriter();
//            e.printStackTrace(new PrintWriter(sw));
//            String exceptionAsString = sw.toString();
//            productLogger.error("Exception in btnBrandClicked():" + exceptionAsString);
//        }
//    }

    /**
     * @version sprint_migration_fx 01
     * DateTime: 18-03-2024:11:15:00
     * @implNote this function select inserted (into DB) value in Combo box of Hsn on click event of plus button
     * @auther ashwins@opethic.com
     **/
    public void btnHsnClicked(String hsnString) {
        Stage stage = (Stage) spRootPane.getScene().getWindow();
        try {
            SingleInputDialogs.productHsnInputDialog(hsnString,stage, "Add New Hsn", input -> {
                Map<String, String> map = new HashMap<>();
                map.put("hsnNumber", input[0]);
                map.put("description", input[1]);
                map.put("type", input[2]);
                HttpResponse<String> response;
                String formData = Globals.mapToStringforFormData(map);
                response = APIClient.postFormDataRequest(formData, EndPoints.PRODUCT_CREATE_HSN_ENDPOINT);
                ResponseMsg responseBody = new Gson().fromJson(response.body(), ResponseMsg.class);
                System.out.println("res body : "+responseBody);
                if (responseBody.getResponseStatus() == 200) {
                    hsnList.add(new CommonDTO(input[0], "" + responseBody.getResponseObject()));
                    cmbHsn.setItems(hsnList);
                    setComboBoxValue(cmbHsn, input[0]);
                    cmbHsn.requestFocus();
                }
            });
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            productLogger.error("Exception in btnHsnClicked():" + exceptionAsString);
        }
    }

    /**
     * @version sprint_migration_fx 01
     * DateTime: 18-03-2024:10:43:00
     * @implNote to set value to comboBox
     * @auther ashwins@opethic.com
     **/
    private void setComboBoxValue(ComboBox comboBox, String key) {
        CommonDTO selecteditem = null;
        for (Object obj : comboBox.getItems()) {
            CommonDTO commonDTO = (CommonDTO) obj;
            if (commonDTO.getText() != null) {
                if (commonDTO.getText().equals(key)) {
                    selecteditem = commonDTO;
                    break;
                }
            }
        }
        if (selecteditem != null) {
            comboBox.getSelectionModel().select(selecteditem);
        }
    }

    private void setComboBoxValueId(ComboBox comboBox, Long id) {
        CommonDTO selecteditem = null;
        for (Object obj : comboBox.getItems()) {
            CommonDTO commonDTO = (CommonDTO) obj;
            Long comId = Long.valueOf(commonDTO.getId());
            if (comId != null) {
                if (comId == id) {
                    selecteditem = commonDTO;
                    break;
                }
            }
        }
        if (selecteditem != null) {
            comboBox.getSelectionModel().select(selecteditem);
        }
    }

    private void setComboBoxValue2(ComboBox comboBox, String key) {
        CommonDTO selecteditem = null;
        for (Object obj : comboBox.getItems()) {
            CommonDTO commonDTO = (CommonDTO) obj;
            if (commonDTO.getText() != null) {
                if (commonDTO.getText().toLowerCase().equals(key.toLowerCase())) {
                    selecteditem = commonDTO;
                    break;
                }
            }
        }
        if (selecteditem != null) {
            comboBox.getSelectionModel().select(selecteditem);
        }
    }


    /**
     * @version sprint_migration_fx 01
     * DateTime: 20-03-2024:15:53:00
     * @implNote add drug Type into DB on plus buttton action event
     * @auther ashwins@opethic.com
     **/
    public void btnDrugClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) spRootPane.getScene().getWindow();
        try {
            SingleInputDialogs.singleInputDialog("", stage, "Add New Drug", input -> {
                Map<String, String> map = new HashMap<>();
                map.put("drugName", input);
                HttpResponse<String> response;
                String formData = Globals.mapToStringforFormData(map);
                response = APIClient.postFormDataRequest(formData, EndPoints.PRODUCT_CREATE_DRUGTYPE_ENDPOINT);
                ResponseMsg responseBody = new Gson().fromJson(response.body(), ResponseMsg.class);
                if (responseBody.getResponseStatus() == 200) {
                    drugTypeList.add(new CommonDTO(input, "" + responseBody.getResponseObject()));
                    cmbDrugType.setItems(drugTypeList);
                    setComboBoxValue(cmbDrugType, input);
                }
            });
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            productLogger.error("Exception in btnDrugClicked():" + exceptionAsString);
        }
    }

    /**
     * @version sprint_migration_fx 01
     * DateTime: 19-03-2024:15:42:00
     * @implNote create product api
     * @auther ashwins@opethic.com
     **/
    public void onSubmit() {
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                try {
                    productMap.put("productCode", tfCode.getText());
                    productMap.put("productName", tfName.getText());
                    productMap.put("productDescription", productDescription);
                    productMap.put("isInventory", String.valueOf(isInventory));
                    productMap.put("isCommision", String.valueOf(isCommision));
                    productMap.put("barcodeNo", tfBarcode.getText());
                    productMap.put("isBatchNo", String.valueOf(isBatch));
                    productMap.put("isSerialNo", String.valueOf(isSerialNo));
                    productMap.put("isWarranty", String.valueOf(isWarranty));
                    if (isWarranty) {
                        if (!tfWarranty.getText().isEmpty()) productMap.put("nodays", tfWarranty.getText());
                        else productMap.put("nodays", "0");
                    }
                    productMap.put("isPrescription", String.valueOf(isPrescription));
                    if (isPrescription) productMap.put("drugType", String.valueOf(cmbDrugTypeId));
                    else productMap.put("drugType", "");
                    productMap.put("drug_contents", tfContents.getText());
                    productMap.put("uploadImage", "");//
                    productMap.put("isGVProducts", String.valueOf(isGvProduct));
                    if (isGvProduct) productMap.put("gvOfProducts", selectedRadioValue);
                    productMap.put("shelfId", tfShelfId.getText() == null ? "" : tfShelfId.getText());
                    if (!tfMargin.getText().isEmpty()) productMap.put("margin", tfMargin.getText());
                    productMap.put("brandId", String.valueOf(cmbBrandId));
                    productMap.put("packagingId", String.valueOf(cmbPackingId));
                    if (!tfDiscPer.getText().isEmpty()) productMap.put("disPer1", tfDiscPer.getText());
                    productMap.put("hsnNo", String.valueOf(cmbHsnId));
                    productMap.put("tax", String.valueOf(cmbTaxId));

                    if (tfApplicableFrom != null && tfApplicableFrom.getText() != null && !tfApplicableFrom.getText().isEmpty()) {
                        productMap.put("taxApplicableDate", Communicator.text_to_date.fromString(tfApplicableFrom.getText()).toString());
                    }
                    productMap.put("taxType", cmbTaxTypeText);
                    Double tax = Double.parseDouble(cmbTaxText);
                    Double cgst = tax / 2;
                    Double sgst = cgst;

                    productMap.put("igst", String.valueOf(tax));
                    productMap.put("sgst", String.valueOf(sgst));
                    productMap.put("cgst", String.valueOf(cgst));
                    if (isInventory) {
                        if (!tfMinStk.getText().isEmpty()) productMap.put("minStock", tfMinStk.getText());
                        if (!tfMaxStk.getText().isEmpty()) productMap.put("maxStock", tfMaxStk.getText());

                    }

                    List<ProductRowDTO> currentItems = new ArrayList<>(tvProductRow.getItems());

                    if (!currentItems.isEmpty()) {
                        ProductRowDTO lastItem = currentItems.get(currentItems.size() - 1);

                        if (lastItem.getSelectedUnit() != null && lastItem.getSelectedUnit().isEmpty()) {
                            currentItems.remove(currentItems.size() - 1);
                        }
                    }

                    List<ProductRowDTO> tableList = new ArrayList<>(currentItems);

                    String flag = "false";
                    int index = -1;

                    for (ProductRowDTO productRow : tableList) {
                        if (productRow.getConv().isEmpty()) {
                            productRow.setConv("1");
                        }
                        if (productRow.getMrp().isEmpty()) {
                            productRow.setMrp("0.0");
                        }
                        if (productRow.getPurRate().isEmpty()) {
                            productRow.setPur_rate("0.0");
                        }
                        if (productRow.getRate_1().isEmpty()) {
                            productRow.setRate_1("0.0");
                        }
                        if (productRow.getRate_2().isEmpty()) {
                            productRow.setRate_2("0.0");
                        }
                        if (productRow.getRate_3().isEmpty()) {
                            productRow.setRate_3("0.0");
                        }
                        else {
                            index = Integer.parseInt(productRow.getCurrent_index());
                        }

                        for (CommonDTO commonDTO : levelAList) {
                            if (productRow.getSelectedLevelA().equals(commonDTO.getText())) {
                                productRow.setSelectedLevelA(commonDTO.getId());
                            }
                        }
                        for (CommonDTO commonDTO : levelBList) {
                            if (productRow.getSelectedLevelB().equals(commonDTO.getText())) {
                                productRow.setSelectedLevelB(commonDTO.getId());
                            }
                        }
                        for (CommonDTO commonDTO : levelCList) {
                            if (productRow.getSelectedLevelC().equals(commonDTO.getText())) {
                                productRow.setSelectedLevelC(commonDTO.getId());
                            }
                        }
                        for (CommonDTO commonDTO : unitList) {
                            if (productRow.getSelectedUnit().equals(commonDTO.getText())) {
                                productRow.setSelectedUnit(commonDTO.getId());
                            }
                        }

                        if (productRow.getIs_rate().equals("true")) {
                            flag = "true";
                        }

                    }

                    System.out.println("productrows: " + tableList.toString());
                    productMap.put("productrows", tableList.toString());
                    productMap.put("productId", String.valueOf(productId));

                    productMap.put("rowDelDetailsIds", String.valueOf(rowDelDetailsIds));

                    productLogger.debug("row del: " + productMap.get(rowDelDetailsIds));
                    productMap.put("isEcom", productMap.get("isEcom"));
                    Map<String, String> headers = new HashMap<>();
                    headers.put("branch", Globals.headerBranch);
                    productLogger.info("Storing the information into contentMap data in json format to String format");

                    productMap.put("cantentMapData", tbllstContentMasterInfo.toString());
                    productLogger.info("End---> Storing the information into contentMap data in json format to String format");
                    productMap.put("removeContentMapData", removeConten.toString());

                    if(flag.equalsIgnoreCase("false")){
                        int finalIndex = index;
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please enable rate", input -> {
                            TableColumn<ProductRowDTO, ?> colName = tvProductRow.getColumns().get(7);
                            tvProductRow.edit(finalIndex, colName);

                        });
                    }
                    else {
                        String response = "";

                        if (productId > 0) {
                            response = APIClient.postMultipartRequest(productMap, null, "update_product_new", headers);
                            System.out.println("Update");
                            //? HIGHLIGHT
                            ProductListController.editedProductId = String.valueOf(productId); //? Set the ID for editing
                        } else {
                            response = APIClient.postMultipartRequest(productMap, null, EndPoints.CREATE_NEW_PRODUCT_ENDPOINT, headers);
                            System.out.println("Create");
                            //? HIGHLIGHT
                            ProductListController.isNewProductCreated = true; //? Set the flag for new creation
                        }

                        System.out.println("res: " + response);

                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getInt("responseStatus") == 200) {
                            int status = jsonResponse.getInt("responseStatus");

                            System.out.println("helllo Redirect >> :" + redirectSlug);
                            if (!redirectSlug.isEmpty()) {
                                System.out.println("helllo If >> :");
                                String productId = jsonResponse.getString("data");
                                prdDTO.setRedirectProductId(productId);
                                AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, jsonResponse.getString("message"), input -> {
                                    GlobalController.getInstance().addTabStaticWithIsNewTab(redirectSlug, false, prdDTO);
                                });
                            } else {
                                System.out.println("helllo else >> :");
                                AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, jsonResponse.getString("message"), input -> {
                                    GlobalController.getInstance().addTabStatic("product-list", false);
                                });
                            }

                        } else {
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, jsonResponse.getString("message"), in -> {
                            });
                        }

                        productLogger.info("Product Response ----->" + response);
                    }
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String exceptionAsString = sw.toString();
                    productLogger.error("Exception in onSubmit():" + exceptionAsString);
                }
            }else {
                System.out.println("working!");
                }
            };
        if (productId > 0) {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Update " + " " + tfName.getText(), callback);
        }else {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Create " + " " + tfName.getText(), callback);
        }
    }

    public void onCancel(ActionEvent actionEvent) {
        GlobalController.getInstance().addTabStatic("product-create", true);

    }

    /**
     * @version sprint_migration_fx 01
     * DateTime: 20-03-2024:17:35:00
     * @implNote select product type: GV owned or traded on radio button action event
     * @auther ashwins@opethic.com
     **/
//    public void handleRadioButtonAction(ActionEvent actionEvent) {
//        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
//        if (selectedRadioButton != null) {
//            selectedRadioValue = selectedRadioButton.getText();
//
//        }
//    }

//MAHESH-EDITED---------------------------------------------------------------------------------------------------------------------------------------------------------

    //MIS popup+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private void getToggleAction() {

        switchMis.switchOnProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                showMIS();
            }
        });

        switchIsEcomm.switchOnProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                showEcomm();
            }
        });
    }

    public void showMIS() {
        Stage stage = (Stage) spRootPane.getScene().getWindow();

        Map<String, String> misMap = new HashMap<>();
        productLogger.info("MIS poup Opened...");
        openMisPopUp(stage, "Configure MIS", (input) -> {
            System.out.println(input[0] + "" + input[1] + input[2] + input[3]);
            getMisMapData(input);
        });
    }

    public void getMisMapData(Object[] object) {

        if (object[0] != null) {
            for (CommonDTO commonDTO : manufacturerList) {
                if (object[0].toString().equals(commonDTO.getText())) {
                    productMap.put("groupId", commonDTO.getId());
                    misDataToSetTexfield[0] = commonDTO.getText();
                    tfManufacturer.setText(misDataToSetTexfield[0]);
                }
            }
        }
        if (object[1] != null) {
            for (CommonDTO commonDTO : formulationList) {
                if (object[1].toString().equals(commonDTO.getText())) {
                    productMap.put("subgroupId", commonDTO.getId());
                    misDataToSetTexfield[1] = commonDTO.getText();
                    tfFormulation.setText(misDataToSetTexfield[1]);
                }
            }
        }
        if (object[2] != null) {
            for (CommonDTO commonDTO : categoryList) {
                if (object[2].toString().equals(commonDTO.getText())) {
                    productMap.put("categoryId", commonDTO.getId());
                    misDataToSetTexfield[2] = commonDTO.getText();
                    tfCategory.setText(misDataToSetTexfield[2]);
                }
            }
        }
        if (object[3] != null) {
            for (CommonDTO commonDTO : subCategoryList) {
                if (object[3].toString().equals(commonDTO.getText())) {
                    productMap.put("subcategoryId", commonDTO.getId());
                    misDataToSetTexfield[3] = commonDTO.getText();
                    tfSubCategory.setText(misDataToSetTexfield[3]);
                }
            }
        }

    }

    private Button createButtonWithImage() {

        ImageView imageView = new ImageView(new Image(GenivisApplication.class.getResourceAsStream("/com/opethic/genivis/ui/assets/add3.png")));
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        Button button = new Button();
        button.setMaxWidth(25);
        button.setMinWidth(25);
        button.setPrefWidth(25);
        button.setMaxHeight(30);
        button.setPrefHeight(30);
        button.setMinHeight(30);
        button.setGraphic(imageView);
        button.getStyleClass().add("add-button-style");

        return button;
    }

    private Button deleteButtonWithImage() {

        ImageView imageView = new ImageView(new Image(GenivisApplication.class.getResourceAsStream("/com/opethic/genivis/ui/assets/remove2.png")));
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);

        Button button = new Button();
        button.setMaxWidth(25);
        button.setMinWidth(25);
        button.setPrefWidth(25);
        button.setMaxHeight(30);
        button.setPrefHeight(30);
        button.setMinHeight(30);
        button.setGraphic(imageView);
        button.getStyleClass().add("add-button-style");

        return button;
    }

    private <T> ComboBox<T> createComboBox(String promptText, EventHandler<ActionEvent> actionHandler) {
        ComboBox<T> comboBox = new ComboBox<>();
        comboBox.setPrefWidth(200);
        comboBox.setMaxWidth(200);
        comboBox.setMinWidth(200);
        comboBox.setMaxHeight(32);
        comboBox.setMinHeight(32);
        comboBox.setPrefHeight(32);
        comboBox.setPromptText(promptText);
        comboBox.setOnAction(actionHandler);
        return comboBox;
    }

    public <T> void openMisPopUp(Stage stage, String title, Consumer<Object[]> callback) {
        Stage primaryStage = new Stage();
        OverlaysEffect.setOverlaysEffect(stage);
        primaryStage.initOwner(stage); // Set the owner stage
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);



        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");

        Platform.runLater(() -> {
            borderPane.requestFocus();
        });

        //BorderPan under Top Layout....................................................................................................................
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(998);
        hbox_top.setMaxWidth(998);
        hbox_top.setPrefWidth(998);
        hbox_top.setMaxHeight(50);
        hbox_top.setMinHeight(50);
        hbox_top.setPrefHeight(50);

        Label popup_title = new Label(title);
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            switchMis.switchOnProperty().set(false);
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        HBox.setMargin(imageView, new Insets(0, 10, 0, 0));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #d9f0fb; -fx-border-color: #ffffff; -fx-border-width: 0 0 3 0;");


        //BorderPane Under Center Layout.....................................................................................................
        HBox hbox_center = new HBox();
        hbox_center.setSpacing(20);
        hbox_center.setMinWidth(998);
        hbox_center.setMaxWidth(998);
        hbox_center.setPrefWidth(998);
        hbox_center.setAlignment(Pos.CENTER);
        hbox_center.setStyle("-fx-background-color: #e6f2f8;");

        Object[] selectedItem = new Object[4];
        Integer initialIndex = -1;

        EventHandler<ActionEvent> cbManufacturerAction = event -> {
            ComboBox<T> comboBox = (ComboBox<T>) event.getSource();
            selectedItem[0] = comboBox.getValue();
        };
        ComboBox<CommonDTO> cbManufacturer = createComboBox("Manufacturer", cbManufacturerAction);
        cbManufacturer.setItems(FXCollections.observableArrayList(getManufacturerList()));
        AutoCompleteBox autoCompleteBox1 = new AutoCompleteBox(cbManufacturer, initialIndex);


        EventHandler<ActionEvent> cbFormulationAction = event -> {
            ComboBox<T> comboBox = (ComboBox<T>) event.getSource();
            selectedItem[1] = comboBox.getValue();
        };
        ComboBox<CommonDTO> cbFormulation = createComboBox("Formulation", cbFormulationAction);
        cbFormulation.setItems(FXCollections.observableArrayList(getFormulationList()));
        AutoCompleteBox autoCompleteBox2 = new AutoCompleteBox(cbFormulation, initialIndex);


        EventHandler<ActionEvent> cbCategoryAction = event -> {
            ComboBox<T> comboBox = (ComboBox<T>) event.getSource();
            selectedItem[2] = comboBox.getValue();
        };
        ComboBox<CommonDTO> cbCategory = createComboBox("Category", cbCategoryAction);
        cbCategory.setItems(FXCollections.observableArrayList(getCategoryList()));
        AutoCompleteBox autoCompleteBox3 = new AutoCompleteBox(cbCategory, initialIndex);


        EventHandler<ActionEvent> cbSubCategoryAction = event -> {
            ComboBox<T> comboBox = (ComboBox<T>) event.getSource();
            selectedItem[3] = comboBox.getValue();
        };
        ComboBox<CommonDTO> cbSubCategory = createComboBox("Sub Category", cbSubCategoryAction);
        cbSubCategory.getItems().clear();
        cbSubCategory.setItems(FXCollections.observableArrayList(getSubCategoryList()));
        AutoCompleteBox autoCompleteBox4 = new AutoCompleteBox(cbSubCategory, initialIndex);

        if (misDataToSetTexfield[0] != null)
            setComboBoxValue(cbManufacturer, misDataToSetTexfield[0]);
        if (misDataToSetTexfield[1] != null)
            setComboBoxValue(cbFormulation, misDataToSetTexfield[1]);
        if (misDataToSetTexfield[2] != null)
            setComboBoxValue(cbCategory, misDataToSetTexfield[2]);
        if (misDataToSetTexfield[3] != null)
            setComboBoxValue(cbSubCategory, misDataToSetTexfield[3]);

        if (selectedItem[0] == null)
            selectedItem[0] = cbManufacturer.getValue();
        if (selectedItem[1] == null)
            selectedItem[1] = cbFormulation.getValue();
        if (selectedItem[2] == null)
            selectedItem[2] = cbCategory.getValue();
        if (selectedItem[3] == null)
            selectedItem[3] = cbSubCategory.getValue();

        hbox_center.getChildren().addAll(cbManufacturer, cbFormulation, cbCategory, cbSubCategory);


        //BorderPane Under Bottom Layout..............................................................................................................
        HBox hbox_bottom = new HBox();
        hbox_bottom.setMinWidth(998);
        hbox_bottom.setMaxWidth(998);
        hbox_bottom.setPrefWidth(998);
        hbox_bottom.setMaxHeight(55);
        hbox_bottom.setMinHeight(55);
        hbox_bottom.setPrefHeight(55);
        hbox_bottom.setSpacing(10);
        hbox_bottom.setStyle("-fx-background-radius: 0 0 5 5; -fx-background-color: white;");
        hbox_bottom.setAlignment(Pos.CENTER_RIGHT);

        Button suButton = new Button("Submit");
        Button clButton = new Button("Clear");
        hbox_bottom.setMargin(clButton, new Insets(0, 10, 0, 0));
        suButton.setId("sub");
        clButton.setId("can");
        suButton.setMinWidth(70);
        suButton.setMaxWidth(70);
        suButton.setPrefWidth(70);
        clButton.setMinWidth(70);
        clButton.setMaxWidth(70);
        clButton.setPrefWidth(70);


        hbox_bottom.getChildren().addAll(suButton,clButton);

        borderPane.setTop(hbox_top);
        borderPane.setCenter(hbox_center);
        borderPane.setBottom(hbox_bottom);

        cbManufacturer.setOnKeyPressed(actionEvent -> {
            if (actionEvent.getCode() == KeyCode.ENTER || actionEvent.getCode() == KeyCode.TAB && !actionEvent.isShiftDown()) {
                String inputText = cbManufacturer.getEditor().getText().trim();
                if (!inputText.isEmpty()) {
                    boolean itemExists = cbManufacturer.getItems().stream()
                            .anyMatch(item -> item.toString().equalsIgnoreCase(inputText));

                    if (!itemExists) {
                        Stage stage2 =(Stage) primaryStage.getScene().getWindow();
                        CustomConfirmationDialog.ConfirmationDialog(stage2,"Do you want to add "+cbManufacturer.getEditor().getText()+"?",output-> {

                            if (output) {
                                String responseObject = insertManufacturer(inputText);
                                if (!responseObject.isEmpty()) {
                                    cbManufacturer.getItems().clear();
                                    manufacturerList.add(new CommonDTO(inputText, responseObject));
                                    cbManufacturer.setItems(manufacturerList);
                                    setComboBoxValue(cbManufacturer, inputText);
                                    cbFormulation.requestFocus();
                                }

                            } else {
                                cbManufacturer.requestFocus();
                                cbManufacturer.getEditor().clear();
                            }
                        });
                    }
                    else {
                        cbManufacturer.getSelectionModel().getSelectedItem();
                        cbFormulation.requestFocus();
                    }
                }
                else {
                    cbFormulation.requestFocus();
                }
            }
        });

        cbFormulation.setOnKeyPressed(actionEvent -> {
            if (actionEvent.getCode() == KeyCode.ENTER || actionEvent.getCode() == KeyCode.TAB && !actionEvent.isShiftDown()) {
                String inputText = cbFormulation.getEditor().getText().trim();
                if (!inputText.isEmpty()) {
                    boolean itemExists = cbFormulation.getItems().stream()
                            .anyMatch(item -> item.toString().equalsIgnoreCase(inputText));

                    if (!itemExists) {
                        Stage stage2 =(Stage) primaryStage.getScene().getWindow();
                        CustomConfirmationDialog.ConfirmationDialog(stage2,"Do you want to add "+cbFormulation.getEditor().getText()+"?",output-> {

                            if (output) {
                                String responseObject = insertFormulation(inputText);
                                if (!responseObject.isEmpty()) {
                                    cbFormulation.getItems().clear();
                                    formulationList.add(new CommonDTO(inputText, responseObject));
                                    cbFormulation.setItems(formulationList);
                                    setComboBoxValue(cbFormulation, inputText);
                                    cbCategory.requestFocus();
                                }

                            } else {
                                cbFormulation.requestFocus();
                            }
                        });
                    }
                    else {
                        cbFormulation.getSelectionModel().getSelectedItem();
                        cbCategory.requestFocus();
                    }
                }
                else {
                    cbCategory.requestFocus();
                }

            }
        });

        cbCategory.setOnKeyPressed(actionEvent -> {
            if (actionEvent.getCode() == KeyCode.ENTER || actionEvent.getCode() == KeyCode.TAB && !actionEvent.isShiftDown()) {
                String inputText = cbCategory.getEditor().getText().trim();
                if (!inputText.isEmpty()) {
                    boolean itemExists = cbCategory.getItems().stream()
                            .anyMatch(item -> item.toString().equalsIgnoreCase(inputText));

                    if (!itemExists) {
                        Stage stage2 =(Stage) primaryStage.getScene().getWindow();
                        CustomConfirmationDialog.ConfirmationDialog(stage2,"Do you want to add "+cbCategory.getEditor().getText()+"?",output-> {

                            if (output) {
                                String responseObject = insertCategory(inputText);
                                if (!responseObject.isEmpty()) {
                                    cbCategory.getItems().clear();
                                    categoryList.add(new CommonDTO(inputText, responseObject));
                                    cbCategory.setItems(categoryList);
                                    setComboBoxValue(cbCategory, inputText);
                                    cbSubCategory.requestFocus();
                                }

                            } else {
                                cbCategory.requestFocus();
                            }
                        });
                    }
                    else {
                        cbCategory.getSelectionModel().getSelectedItem();
                        cbSubCategory.requestFocus();
                    }
                }

                else {
                    cbSubCategory.requestFocus();
                }
            }
        });

        cbSubCategory.setOnKeyPressed(actionEvent -> {
            if (actionEvent.getCode() == KeyCode.ENTER || actionEvent.getCode() == KeyCode.TAB && !actionEvent.isShiftDown()) {
                String inputText = cbSubCategory.getEditor().getText().trim();
                if (!inputText.isEmpty()) {
                    boolean itemExists = cbSubCategory.getItems().stream()
                            .anyMatch(item -> item.toString().equalsIgnoreCase(inputText));

                    if (!itemExists) {
                        Stage stage2 =(Stage) primaryStage.getScene().getWindow();
                        CustomConfirmationDialog.ConfirmationDialog(stage2,"Do you want to add "+cbSubCategory.getEditor().getText()+"?",output-> {

                            if (output) {
                                String responseObject = insertSubCategory(inputText);
                                if (!responseObject.isEmpty()) {
                                    cbSubCategory.getItems().clear();
                                    subCategoryList.add(new CommonDTO(inputText, responseObject));
                                    cbSubCategory.setItems(subCategoryList);
                                    setComboBoxValue(cbSubCategory, inputText);
                                    suButton.requestFocus();
                                }

                            } else {
                                suButton.requestFocus();
                            }
                        });
                    }
                    else {
                        cbSubCategory.getSelectionModel().getSelectedItem();
                        suButton.requestFocus();
                    }
                }

                else {
                    suButton.requestFocus();
                }
            }
        });

        suButton.setOnKeyPressed(actionEvent -> {
            if (actionEvent.getCode() == KeyCode.LEFT || actionEvent.getCode() == KeyCode.RIGHT) {
                clButton.requestFocus();
            }
            if(actionEvent.getCode() == KeyCode.TAB && actionEvent.isShiftDown()){
                cbSubCategory.requestFocus();
            }
        });

        clButton.setOnKeyPressed(actionEvent -> {
            if (actionEvent.getCode() == KeyCode.RIGHT || actionEvent.getCode() == KeyCode.LEFT) {
                suButton.requestFocus();
            }
            if(actionEvent.getCode() == KeyCode.TAB && actionEvent.isShiftDown()){
                cbSubCategory.requestFocus();
            }
        });



        Scene scene = new Scene(borderPane, 1000, 170);

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

        primaryStage.centerOnScreen();

        primaryStage.show();

        Platform.runLater(() -> {
            cbManufacturer.requestFocus();
        });

        suButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                callback.accept(selectedItem);
                primaryStage.close();
                switchMis.switchOnProperty().set(false);
                productMap.put("isMIS", "true");
                OverlaysEffect.removeOverlaysEffect(stage);
            }
        });

        clButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                cbManufacturer.setValue(null);
                cbFormulation.setValue(null);
                cbCategory.setValue(null);
                cbSubCategory.setValue(null);
                cbManufacturer.requestFocus();
                Arrays.fill(misDataToSetTexfield, null);

                productMap.remove("groupId");
                productMap.remove("subgroupId");
                productMap.remove("categoryId");
                productMap.remove("subcategoryId");
                tfManufacturer.clear();
                tfFormulation.clear();
                tfCategory.clear();
                tfSubCategory.clear();
//                primaryStage.close();
                switchMis.switchOnProperty().set(false);
                productMap.put("isMIS", "false");
//                OverlaysEffect.removeOverlaysEffect(stage);
            }
        });

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                switchMis.switchOnProperty().set(false);
                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
        });
    }


    public List<CommonDTO> getManufacturerList() {
        manufacturerList.clear();
        try {

            HttpResponse<String> response = APIClient.getRequest("get_outlet_groups");
            CManufacturerDTO cManufacturerDTO = new Gson().fromJson(response.body(), CManufacturerDTO.class);

            if (cManufacturerDTO.getResponseStatus() == 200) {

                List<CManufaturerListDTO> list1 = cManufacturerDTO.getResponseObject();

                for (CManufaturerListDTO cManufaturerListDTO : list1) {
                    manufacturerList.add(new CommonDTO(cManufaturerListDTO.getGroupName(), cManufaturerListDTO.getId().toString()));
                }

            } else {
                productLogger.info("ResponseObject is null--->getManufacturerList()");
            }

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            productLogger.error("Exception in getManufacturerList():" + exceptionAsString);
        }
        return manufacturerList;
    }


    public List<CommonDTO> getFormulationList() {
        formulationList.clear();
        try {

            HttpResponse<String> response = APIClient.getRequest("get_outlet_subgroups");
            CFormulationResDTO cManufacturerDTO = new Gson().fromJson(response.body(), CFormulationResDTO.class);

            if (cManufacturerDTO.getResponseStatus() == 200) {

                List<CFourmulationListDTO> list1 = cManufacturerDTO.getResponseObject();

                for (CFourmulationListDTO cFourmulationListDTO : list1) {
                    formulationList.add(new CommonDTO(cFourmulationListDTO.getSubgroupName(), cFourmulationListDTO.getId().toString()));
                }

            } else {
                System.out.println("responseObject is null");
            }

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            productLogger.error("Exception in getFormulationList():" + exceptionAsString);
        }

        return formulationList;
    }

    public List<CommonDTO> getCategoryList() {
        categoryList.clear();
        try {

            HttpResponse<String> response = APIClient.getRequest("get_outlet_categories");
            CCategoryResDTO cCategoryResDTO = new Gson().fromJson(response.body(), CCategoryResDTO.class);

            if (cCategoryResDTO.getResponseStatus() == 200) {

                List<CCategoryListDTO> list1 = cCategoryResDTO.getResponseObject();

                for (CCategoryListDTO cCategoryListDTO : list1) {
                    categoryList.add(new CommonDTO(cCategoryListDTO.getCategoryName(), cCategoryListDTO.getId().toString()));
                }

            } else {
                productLogger.info("getCategoryList() method ----> Response is Empty");
            }

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            productLogger.error("Exception in getCategoryList():" + exceptionAsString);
        }

        return categoryList;
    }

    public List<CommonDTO> getSubCategoryList() {
        subCategoryList.clear();
        try {

            HttpResponse<String> response = APIClient.getRequest("get_outlet_subcategories");
            CSubCatResDTO cSubCatResDTO = new Gson().fromJson(response.body(), CSubCatResDTO.class);

            if (cSubCatResDTO.getResponseStatus() == 200) {

                List<CSubCatListDTO> list1 = cSubCatResDTO.getResponseObject();

                for (CSubCatListDTO cSubCatListDTO : list1) {
                    subCategoryList.add(new CommonDTO(cSubCatListDTO.getSubcategoryName(), cSubCatListDTO.getId().toString()));
                }

            } else {
                System.out.println("responseObject is null");
            }

        } catch (Exception e) {
            productLogger.error("Error in getSubCategoryList() method ----> " + e.getMessage());
            e.printStackTrace();
        }

        return subCategoryList;
    }

    public String insertManufacturer(String input) {

        String responseObejct = "";
        try {
            Map<String, String> map = new HashMap<>();
            map.put("groupName", input);

            String formData = Globals.mapToStringforFormData(map);

            HttpResponse<String> response = APIClient.postFormDataRequest(formData, "create_group");

            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

            if (responseBody.get("responseStatus").getAsInt() == 200) {
              //  AlertUtility.AlertDialogForSuccessWithStage(stage,"Success","Manufacture success",output->{});

                responseObejct = responseBody.get("responseObject").getAsString();
            } else {
            }

        } catch (Exception e) {
            productLogger.error("Exception in insertManufacturer()----> " + e.getMessage());
        }
        return responseObejct;
    }

    public String insertFormulation(String input) {
        String responseObejct = "";
        Map<String, String> map = new HashMap<>();
        map.put("subgroupName", input);

        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "create_sub_group");

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            responseObejct = responseBody.get("responseObject").getAsString();
        } else {
        }
        return responseObejct;

    }

    public String insertCategory(String input) {
        String responseObejct = "";
        Map<String, String> map = new HashMap<>();
        map.put("categoryName", input);

        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "create_category");


        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            responseObejct = responseBody.get("responseObject").getAsString();
        } else {

        }
        return responseObejct;

    }

    public String insertSubCategory(String input) {
        String responseObejct = "";
        Map<String, String> map = new HashMap<>();
        map.put("subCategoryName", input);

        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "create_sub_category");

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            responseObejct = responseBody.get("responseObject").getAsString();
        } else {
        }
        return responseObejct;

    }

//Content-Master---------------------------------------------------------------------------------------------------------------------------------------------------------

    public void showContent() {
        Stage stage = (Stage) spRootPane.getScene().getWindow();

        Map<String, String> misMap = new HashMap<>();
        productLogger.info("Content master poup window opened...");
        openContentPopUp(stage, "Content Master", (input) -> {
            setContentsMaster(input);
        });
    }

//    public <T> void openContentPopUp(Stage stage, String title, Consumer<Object[]> callback) {
//        OverlaysEffect.setOverlaysEffect(stage);
//        Stage primaryStage = new Stage();
//
//        primaryStage.initOwner(stage); // Set the owner stage
//        primaryStage.initStyle(StageStyle.TRANSPARENT);
//        primaryStage.initModality(Modality.APPLICATION_MODAL);
//
//        //Main Layout................................................................................................................................
//        BorderPane borderPane = new BorderPane();
//        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
//        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");
//        Platform.runLater(() -> borderPane.requestFocus());
//        //BorderPan under Top Layout....................................................................................................................
//        HBox hbox_top = new HBox();
//        hbox_top.setMinWidth(998);
//        hbox_top.setMaxWidth(998);
//        hbox_top.setPrefWidth(998);
//        hbox_top.setMaxHeight(50);
//        hbox_top.setMinHeight(50);
//        hbox_top.setPrefHeight(50);
//
//        Label popup_title = new Label(title);
//        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
//        popup_title.setPadding(new Insets(0, 10, 0, 0));
//
//        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
//        ImageView imageView = new ImageView(image);
//        imageView.setStyle("-fx-cursor: hand;");
//        imageView.setOnMouseClicked(event -> {
//            primaryStage.close();
//            OverlaysEffect.removeOverlaysEffect(stage);
//        });
//        HBox.setMargin(imageView, new Insets(0, 10, 0, 0));
//        imageView.setFitWidth(30);
//        imageView.setFitHeight(30);
//
//        hbox_top.setAlignment(Pos.CENTER_LEFT);
//        hbox_top.getChildren().add(popup_title);
//        Region spacer = new Region();
//        HBox.setHgrow(spacer, Priority.ALWAYS);
//        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
//        hbox_top.getChildren().add(spacer);
//        hbox_top.getChildren().add(imageView);
//        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #D9F0FB; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 2 0;");
//        //BorderPane Under Center Layout.....................................................................................................
//        HBox hbox_center = new HBox();
//        hbox_center.setMinWidth(998);
//        hbox_center.setMaxWidth(998);
//        hbox_center.setPrefWidth(998);
//        hbox_center.setMaxHeight(50);
//        hbox_center.setMinHeight(50);
//        hbox_center.setMaxHeight(50);
//        hbox_center.setAlignment(Pos.CENTER);
//        hbox_center.setStyle("-fx-background-color: white;");
//
//        productLogger.info("all input field mapped to Object selectedItem[] , in openContentPopUp()");
//        Object[] selectedItem = new Object[5];
//        Integer initialIndex = -1;
//
//        EventHandler<ActionEvent> cbContentNameAction = event -> {
//            ComboBox<T> comboBox = (ComboBox<T>) event.getSource();
//            selectedItem[0] = comboBox.getValue();
//        };
//        ComboBox<CommonDTO> cbContentName = createComboBox("Content Name", cbContentNameAction);
//        cbContentName.setItems(FXCollections.observableArrayList(getContentNames()));
//        cbContentName.setPrefWidth(300);
//        cbContentName.setMaxWidth(300);
//        cbContentName.setMinWidth(300);
//
//
//        AutoCompleteBox autoCompleteBox1 = new AutoCompleteBox(cbContentName, initialIndex);
//
//        TextField tfPower = new TextField();
//        tfPower.setPromptText("Power");
//        tfPower.setMinWidth(120);
//        tfPower.setMaxWidth(120);
//        tfPower.setPrefWidth(120);
//        EventHandler<ActionEvent> cbPackageAction = event -> {
//            ComboBox<T> comboBox = (ComboBox<T>) event.getSource();
//            selectedItem[2] = comboBox.getValue();
//
//        };
//        ComboBox<CommonDTO> cbPackage = createComboBox("Package", cbPackageAction);
//        cbPackage.setItems(FXCollections.observableArrayList(getPackageList()));
//        cbPackage.setMinWidth(120);
//        cbPackage.setMaxWidth(120);
//        cbPackage.setPrefWidth(120);
//        AutoCompleteBox autoCompleteBox3 = new AutoCompleteBox(cbPackage, initialIndex);
//
//        EventHandler<ActionEvent> cbContentTypeAction = event -> {
//            ComboBox<T> comboBox = (ComboBox<T>) event.getSource();
//            selectedItem[3] = comboBox.getValue();
//        };
//        ComboBox<CommonDTO> cbContentType = createComboBox("Content Type", cbContentTypeAction);
//        cbContentType.setItems(FXCollections.observableArrayList(getContentType()));
//        AutoCompleteBox autoCompleteBox4 = new AutoCompleteBox(cbContentType, initialIndex);
//
////        if (!cmbContentNameText.isEmpty())
////            setComboBoxValue(cbContentName, cmbContentNameText);
////        if (!contentPowerText.isEmpty())
////            tfPower.setText(contentPowerText);
////        if (!cmbContentPackText.isEmpty())
////            setComboBoxValue(cbPackage, cmbContentPackText);
////        if (!cmbContentTypeText.isEmpty())
////            setComboBoxValue(cbPackage, cmbContentTypeText);
//        Button buttonConten = createButtonWithImage();
//
//        Button buttonContentType = createButtonWithImage();
//        //Button deleteButton = deleteButtonWithImage();
//
//        Insets Margin = new Insets(0, 30, 0, 1);
//        HBox.setMargin(buttonConten, Margin);
//        HBox.setMargin(cbPackage, Margin);
//        HBox.setMargin(buttonContentType, Margin);
//        HBox.setMargin(tfPower, Margin);
//        hbox_center.getChildren().addAll(cbContentName, buttonConten, tfPower, cbPackage, cbContentType, buttonContentType);
//
//
//        //BorderPane Under Bottom Layout..............................................................................................................
//        VBox vBox = new VBox();
//        vBox.setSpacing(10);
//        HBox hbox_bottom = new HBox();
//        hbox_bottom.setMinWidth(998);
//        hbox_bottom.setMaxWidth(998);
//        hbox_bottom.setPrefWidth(998);
//        hbox_bottom.setMaxHeight(40);
//        hbox_bottom.setMinHeight(4);
//        hbox_bottom.setPrefHeight(40);
//        hbox_bottom.setSpacing(10);
//        hbox_bottom.setStyle("-fx-background-radius: 0 0 5 5; -fx-background-color: #FFFFFF;");
//        hbox_bottom.setAlignment(Pos.TOP_RIGHT);
//
//        Button addButton = new Button("Add");
//        Button clButton = new Button("Clear");
//        addButton.setId("submit-btn");
//        clButton.setId("cancel-btn");
//        hbox_bottom.setMargin(clButton, new Insets(0, 10, 0, 0));
//
//        hbox_bottom.getChildren().addAll(addButton, clButton);
//
//        HBox hbox_bottom2 = new HBox();
//        hbox_bottom2.setMinWidth(998);
//        hbox_bottom2.setMaxWidth(998);
//        hbox_bottom2.setPrefWidth(998);
//        hbox_bottom2.setMaxHeight(40);
//        hbox_bottom2.setMinHeight(4);
//        hbox_bottom2.setPrefHeight(40);
//        hbox_bottom2.setSpacing(10);
//        hbox_bottom2.setStyle("-fx-background-radius: 0 0 5 5; -fx-background-color: #FFFFFF;");
//        hbox_bottom2.setAlignment(Pos.TOP_RIGHT);
//
//        Button subButton = new Button("Submit");
//        subButton.setId("submit-btn");
//        hbox_bottom2.setMargin(subButton, new Insets(0, 10, 0, 0));
//
//
//        hbox_bottom2.getChildren().addAll(subButton);
//
//        TableView<ProductContentsMasterDTO> tableView = new TableView();
//        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        tableView.setPrefHeight(200);
//        tableView.setMaxHeight(200);
//        tableView.setMinHeight(200);
//        TableColumn<ProductContentsMasterDTO, String> cmName = new TableColumn<>("Content Name");
//        TableColumn<ProductContentsMasterDTO, String> cmPower = new TableColumn<>("Power");
//        TableColumn<ProductContentsMasterDTO, String> cmPackage = new TableColumn<>("Packing");
//        TableColumn<ProductContentsMasterDTO, String> cmType = new TableColumn<>("Content Type");
//        TableColumn<ProductContentsMasterDTO, Void> cmActions = new TableColumn<>("Actions");
//
//        tableView.getColumns().addAll(cmName, cmPower, cmPackage, cmType, cmActions);
//
//        cmName.setCellValueFactory(new PropertyValueFactory<>("contentName"));
//        cmPower.setCellValueFactory(new PropertyValueFactory<>("power"));
//        cmPackage.setCellValueFactory(new PropertyValueFactory<>("packing"));
//        cmType.setCellValueFactory(new PropertyValueFactory<>("contentType"));
//        cmActions.setCellFactory(param -> {
//            final TableCell<ProductContentsMasterDTO, Void> cell = new TableCell<>() {
//                private ImageView delImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/del.png"))));
//                private ImageView edtImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/edt.png"))));
//
//                {
//                    delImg.setFitHeight(20.0);
//                    delImg.setFitWidth(20.0);
//                    edtImg.setFitHeight(20.0);
//                    edtImg.setFitWidth(20.0);
//                }
//
//                private final Button delButton = new Button("", delImg);
//                private final Button edtButton = new Button("", edtImg);
//
//                {
//
//                    delButton.setOnAction(event -> {
//                        contentEdtIdx = -1;
//                        Integer id_of_content = tbllstContentMasterInfo.get(getIndex()).getId();
//                        removeConten.add(id_of_content);
//                        tbllstContentMasterInfo.remove(getIndex());
//                        System.out.println("del id" + removeConten);
//                    });
//                    edtButton.setOnAction(event -> {
//                        // Handle button action here
//                        contentEdtIdx = getIndex();
//                        ProductContentsMasterDTO fnContentsDetails = tbllstContentMasterInfo.get(contentEdtIdx);
//                        if (fnContentsDetails != null) {
//                            setComboBoxValue(cbContentName, fnContentsDetails.getContentName());
//                            tfPower.setText(fnContentsDetails.getPower());
//                            setComboBoxValue(cbPackage, fnContentsDetails.getPacking());
//                            setComboBoxValue(cbContentType, fnContentsDetails.getContentType());
//                        }
//                    });
//                }
//
//                HBox hbActions = new HBox();
//
//                {
//                    hbActions.getChildren().add(edtButton);
//                    hbActions.getChildren().add(delButton);
//                    hbActions.setSpacing(10.0);
//                }
//
//                @Override
//                protected void updateItem(Void item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (empty) {
//                        setGraphic(null);
//                    } else {
//                        setGraphic(hbActions);
//                    }
//                }
//
//            };
//            return cell;
//        });
//        tableView.setItems(tbllstContentMasterInfo);
//        vBox.getChildren().addAll(hbox_bottom, tableView, hbox_bottom2);
//
//        borderPane.setTop(hbox_top);
//        borderPane.setCenter(hbox_center);
//        borderPane.setBottom(vBox);
//
//        buttonConten.setOnAction(actionEvent -> {
//            Stage stage1 = (Stage) borderPane.getScene().getWindow();
//            SingleInputDialogs.singleInputDialog("", stage1, "Add New Content", input -> {
//                productLogger.info("api call to insert content master on plus button key");
//                String responseObject = insertContentNameMaster(input);
//                if (!responseObject.isEmpty()) {
//                    cbContentName.getItems().clear();
//                    contentNamesList.add(new CommonDTO(input, responseObject));
//                    cbContentName.setItems(contentNamesList);
//                    setComboBoxValue(cbContentName, input);
//                }
//            });
//        });
//
//        buttonContentType.setOnAction(actionEvent -> {
//            Stage stage1 = (Stage) borderPane.getScene().getWindow();
//            SingleInputDialogs.singleInputDialog("", stage1, "Add New Content Type", input -> {
//                productLogger.info("api call to insert content Type on plus button key");
//                String responseObject = insertContentType(input);
//                if (!responseObject.isEmpty()) {
//                    cbContentType.getItems().clear();
//                    contentTypeList.add(new CommonDTO(input, responseObject));
//                    cbContentType.setItems(contentTypeList);
//                    setComboBoxValue(cbContentType, input);
//                }
//            });
//        });
//
//        Node[] nodes = new Node[]{cbContentName, buttonConten, cbPackage, cbContentType, buttonContentType, addButton};
//        CommonValidationsUtils.setupFocusNavigation(nodes);
//
//        Scene scene = new Scene(borderPane, 1000, 410);
//
//        primaryStage.setScene(scene);
//        primaryStage.setTitle(title);
//        primaryStage.setResizable(false);
//
//        scene.setFill(Color.TRANSPARENT);
//
//        primaryStage.centerOnScreen();
//
//        primaryStage.show();
//
//        addButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                selectedItem[1] = tfPower.getText();
//                insertIntoTable(selectedItem[0].toString(), selectedItem[1].toString(), selectedItem[2].toString(), selectedItem[3].toString());
//            }
//        });
//
//        clButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                // Clear ComboBox selections
//                cbContentType.setValue(null); // Clear selection
//                cbContentName.setValue(null); // Clear selection
//                cbPackage.setValue(null);
//
//                // Clear TextField
//                tfPower.clear();
//
//                // Optionally, clear the table
//                tbllstContentMasterInfo.clear();
//                // Set focus back to the first ComboBox
//                Platform.runLater(() -> cbContentName.requestFocus());
//            }
//        });
//        subButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                inputName = "";
//                for (int i = 0; i < tbllstContentMasterInfo.size(); i++) {
//                    inputName += tbllstContentMasterInfo.get(i).getContentName() + " " + tbllstContentMasterInfo.get(i).getPower() + " " + tbllstContentMasterInfo.get(i).getPacking() + " " + tbllstContentMasterInfo.get(i).getContentType();
//                    if (i < tbllstContentMasterInfo.size() - 1) {
//                        inputName += ",";
//                    }
//                }
//                selectedItem[4] = inputName;
//                callback.accept(selectedItem);
//                primaryStage.close();
//                OverlaysEffect.removeOverlaysEffect(stage);
//            }
//        });
//
//        scene.setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.ESCAPE) {
//                primaryStage.close();
//                OverlaysEffect.removeOverlaysEffect(stage);
//            }
//        });
//    }


    public <T> void openContentPopUp(Stage stage, String title, Consumer<Object[]> callback) {
        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();

        primaryStage.initOwner(stage); // Set the owner stage
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");
        Platform.runLater(() -> borderPane.requestFocus());
        //BorderPan under Top Layout....................................................................................................................
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(998);
        hbox_top.setMaxWidth(998);
        hbox_top.setPrefWidth(998);
        hbox_top.setMaxHeight(50);
        hbox_top.setMinHeight(50);
        hbox_top.setPrefHeight(50);

        Label popup_title = new Label(title);
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        HBox.setMargin(imageView, new Insets(0, 10, 0, 0));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #D9F0FB; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 2 0;");
        //BorderPane Under Center Layout.....................................................................................................
        HBox hbox_center = new HBox();
        hbox_center.setMinWidth(998);
        hbox_center.setMaxWidth(998);
        hbox_center.setPrefWidth(998);
        hbox_center.setSpacing(20);
        hbox_center.setAlignment(Pos.CENTER);
        hbox_center.setStyle("-fx-background-color: white;");

        productLogger.info("all input field mapped to Object selectedItem[] , in openContentPopUp()");
        Object[] selectedItem = new Object[5];
        Integer initialIndex = -1;

        EventHandler<ActionEvent> cbContentNameAction = event -> {
            ComboBox<T> comboBox = (ComboBox<T>) event.getSource();
            selectedItem[0] = comboBox.getValue();
        };
        ComboBox<CommonDTO> cbContentName = createComboBox("Content Name", cbContentNameAction);
        cbContentName.getItems().clear();
        ObservableList<CommonDTO> items = FXCollections.observableArrayList(getContentNames());
        cbContentName.setItems(items);
        AutoCompleteBox autoCompleteBox1 = new AutoCompleteBox(cbContentName, initialIndex);

        cbContentName.setPrefWidth(300);
        cbContentName.setMaxWidth(300);
        cbContentName.setMinWidth(300);

        TextField tfPower = new TextField();
        tfPower.setPromptText("Power");
        tfPower.setMinWidth(120);
        tfPower.setMaxWidth(120);
        tfPower.setPrefWidth(120);
        EventHandler<ActionEvent> cbPackageAction = event -> {
            ComboBox<T> comboBox = (ComboBox<T>) event.getSource();
            selectedItem[2] = comboBox.getValue();

        };
        ComboBox<CommonDTO> cbPackage = createComboBox("Package", cbPackageAction);
        cbPackage.setItems(FXCollections.observableArrayList(getPackageList()));
        cbPackage.setMinWidth(120);
        cbPackage.setMaxWidth(120);
        cbPackage.setPrefWidth(120);
        AutoCompleteBox autoCompleteBox3 = new AutoCompleteBox(cbPackage, initialIndex);

        EventHandler<ActionEvent> cbContentTypeAction = event -> {
            ComboBox<T> comboBox = (ComboBox<T>) event.getSource();
            selectedItem[3] = comboBox.getValue();
        };
        ComboBox<CommonDTO> cbContentType = createComboBox("Content Type", cbContentTypeAction);
        ObservableList<CommonDTO> items1 = FXCollections.observableArrayList(getContentType());
        cbContentType.setItems(items1);
        AutoCompleteBox autoCompleteBox4 = new AutoCompleteBox(cbContentType, initialIndex);

//        if (!cmbContentNameText.isEmpty())
//            setComboBoxValue(cbContentName, cmbContentNameText);
//        if (!contentPowerText.isEmpty())
//            tfPower.setText(contentPowerText);
//        if (!cmbContentPackText.isEmpty())
//            setComboBoxValue(cbPackage, cmbContentPackText);
//        if (!cmbContentTypeText.isEmpty())
//            setComboBoxValue(cbPackage, cmbContentTypeText);
//        Button buttonConten = createButtonWithImage();

//        Button buttonContentType = createButtonWithImage();
        //Button deleteButton = deleteButtonWithImage();

//        Insets Margin = new Insets(0, 30, 0, 1);
//        HBox.setMargin(cbContentName, Margin);
//        HBox.setMargin(cbPackage, Margin);
//        HBox.setMargin(buttonContentType, Margin);
       // HBox.setMargin(tfPower, Margin);

        Button addButton = new Button("Add");
        Button clButton = new Button("Clear");
        addButton.setId("sub");
        clButton.setId("can");
        addButton.setMinWidth(60);
        addButton.setMaxWidth(60);
        addButton.setPrefWidth(60);
        clButton.setMinWidth(60);
        clButton.setMaxWidth(60);
        clButton.setPrefWidth(60);
        hbox_center.getChildren().addAll(cbContentName, tfPower, cbPackage, cbContentType,addButton, clButton);


        //BorderPane Under Bottom Layout..............................................................................................................
        VBox vBox = new VBox();
        vBox.setSpacing(10);
//        HBox hbox_bottom = new HBox();
//        hbox_bottom.setMinWidth(998);
//        hbox_bottom.setMaxWidth(998);
//        hbox_bottom.setPrefWidth(998);
//        hbox_bottom.setMaxHeight(40);
//        hbox_bottom.setMinHeight(4);
//        hbox_bottom.setPrefHeight(40);
//        hbox_bottom.setSpacing(10);
//        hbox_bottom.setStyle("-fx-background-radius: 0 0 5 5; -fx-background-color: #FFFFFF;");
//        hbox_bottom.setAlignment(Pos.TOP_RIGHT);


//        hbox_bottom.setMargin(clButton, new Insets(0, 10, 0, 0));

       // hbox_bottom.getChildren().addAll();

        HBox hbox_bottom2 = new HBox();
        hbox_bottom2.setMinWidth(998);
        hbox_bottom2.setMaxWidth(998);
        hbox_bottom2.setPrefWidth(998);
        hbox_bottom2.setMaxHeight(40);
        hbox_bottom2.setMinHeight(4);
        hbox_bottom2.setPrefHeight(40);
        hbox_bottom2.setSpacing(10);
        hbox_bottom2.setStyle("-fx-background-radius: 0 0 5 5; -fx-background-color: #FFFFFF;");
        hbox_bottom2.setAlignment(Pos.TOP_RIGHT);

        Button subButton = new Button("Submit");
        subButton.setId("sub");
        subButton.setMinWidth(80);
        subButton.setMaxWidth(80);
        subButton.setPrefWidth(80);
        hbox_bottom2.setMargin(subButton, new Insets(0, 10, 0, 0));


        hbox_bottom2.getChildren().addAll(subButton);

        TableView<ProductContentsMasterDTO> tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefHeight(195);
        tableView.setMaxHeight(195);
        tableView.setMinHeight(195);
        TableColumn<ProductContentsMasterDTO, String> cmName = new TableColumn<>("Content Name");
        TableColumn<ProductContentsMasterDTO, String> cmPower = new TableColumn<>("Power");
        TableColumn<ProductContentsMasterDTO, String> cmPackage = new TableColumn<>("Packing");
        TableColumn<ProductContentsMasterDTO, String> cmType = new TableColumn<>("Content Type");
        TableColumn<ProductContentsMasterDTO, Void> cmActions = new TableColumn<>("Actions");
        cmActions.setVisible(false);

        tableView.getColumns().addAll(cmName, cmPower, cmPackage, cmType, cmActions);

        cmName.setCellValueFactory(new PropertyValueFactory<>("contentName"));
        cmPower.setCellValueFactory(new PropertyValueFactory<>("power"));
        cmPackage.setCellValueFactory(new PropertyValueFactory<>("packing"));
        cmType.setCellValueFactory(new PropertyValueFactory<>("contentType"));


        tableView.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                ProductContentsMasterDTO fnContentsDetails = tableView.getSelectionModel().getSelectedItem();
                if (fnContentsDetails != null) {
                    setComboBoxValue(cbContentName, fnContentsDetails.getContentName());
                    tfPower.setText(fnContentsDetails.getPower());
                    setComboBoxValue(cbPackage, fnContentsDetails.getPacking());
                    setComboBoxValue(cbContentType, fnContentsDetails.getContentType());
                }
            }

            if (keyEvent.getCode() == KeyCode.DELETE) {
                int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0) {
                    contentEdtIdx = -1;
                    Integer id_of_content = tbllstContentMasterInfo.get(selectedIndex).getId();
                    removeConten.add(id_of_content);
                    tbllstContentMasterInfo.remove(selectedIndex);
                }
            }
        });


        tableView.setRowFactory(tv -> {
            TableRow<ProductContentsMasterDTO> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        ProductContentsMasterDTO fnContentsDetails = tableView.getSelectionModel().getSelectedItem();
                        if (fnContentsDetails != null) {
                            setComboBoxValue(cbContentName, fnContentsDetails.getContentName());
                            tfPower.setText(fnContentsDetails.getPower());
                            setComboBoxValue(cbPackage, fnContentsDetails.getPacking());
                            setComboBoxValue(cbContentType, fnContentsDetails.getContentType());
                        }
                    }
                }
            });
            return row;
        });





        cmActions.setCellFactory(param -> {
            final TableCell<ProductContentsMasterDTO, Void> cell = new TableCell<>() {
                private ImageView delImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/del.png"))));
                private ImageView edtImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/edt.png"))));

                {
                    delImg.setFitHeight(20.0);
                    delImg.setFitWidth(20.0);
                    edtImg.setFitHeight(20.0);
                    edtImg.setFitWidth(20.0);
                }

                private final Button delButton = new Button("", delImg);
                private final Button edtButton = new Button("", edtImg);

                {

                    delButton.setOnAction(event -> {
                        contentEdtIdx = -1;
                        Integer id_of_content = tbllstContentMasterInfo.get(getIndex()).getId();
                        removeConten.add(id_of_content);
                        tbllstContentMasterInfo.remove(getIndex());
                    });
                    edtButton.setOnAction(event -> {
                        contentEdtIdx = getIndex();
                        ProductContentsMasterDTO fnContentsDetails = tbllstContentMasterInfo.get(contentEdtIdx);
                        if (fnContentsDetails != null) {
                            setComboBoxValue(cbContentType, fnContentsDetails.getContentType());
                            tfPower.setText(fnContentsDetails.getPower());
                            setComboBoxValue(cbPackage, fnContentsDetails.getPacking());
                            setComboBoxValue(cbContentType, fnContentsDetails.getContentType());
                        }
                    });
                }

                HBox hbActions = new HBox();

                {
                    hbActions.getChildren().add(edtButton);
                    hbActions.getChildren().add(delButton);
                    hbActions.setSpacing(10.0);
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
        });
        tableView.setItems(tbllstContentMasterInfo);
        tableView.setFocusTraversable(false);
        vBox.getChildren().addAll(tableView, hbox_bottom2);

        borderPane.setTop(hbox_top);
        borderPane.setCenter(hbox_center);
        borderPane.setBottom(vBox);

        addButton.setOnKeyPressed(actionEvent -> {
            if (actionEvent.getCode() == KeyCode.ENTER || actionEvent.getCode() == KeyCode.TAB && !actionEvent.isShiftDown()) {
                clButton.requestFocus();
            }
            if (actionEvent.getCode() == KeyCode.TAB && actionEvent.isShiftDown()) {
                cbContentType.requestFocus();
            }

            if (actionEvent.getCode() == KeyCode.DOWN) {
                tableView.getSelectionModel().select(0);
                tableView.requestFocus();
                actionEvent.consume();
            }
        });

        clButton.setOnKeyPressed(actionEvent -> {
            if (actionEvent.getCode() == KeyCode.ENTER || actionEvent.getCode() == KeyCode.TAB && !actionEvent.isShiftDown()) {
                subButton.requestFocus();
            }
            if (actionEvent.getCode() == KeyCode.TAB && actionEvent.isShiftDown()) {
                addButton.requestFocus();
            }
            if (actionEvent.getCode() == KeyCode.DOWN) {
                tableView.getSelectionModel().select(0);
                tableView.requestFocus();
                actionEvent.consume();
            }
        });



        cbPackage.setOnKeyPressed( actionEvent -> {
            if (actionEvent.getCode() == KeyCode.ENTER || actionEvent.getCode() == KeyCode.TAB && !actionEvent.isShiftDown()) {

                String inputText = cbPackage.getEditor().getText().trim();
                if (!inputText.isEmpty()) {
                    cbContentType.requestFocus();
                }
                else {
                    cbPackage.requestFocus();
                }
            }

            if (actionEvent.getCode() == KeyCode.TAB && actionEvent.isShiftDown()) {
                tfPower.requestFocus();
            }

            if (actionEvent.getCode() == KeyCode.DOWN) {
                tableView.getSelectionModel().select(0);
            }
        });

        tfPower.setOnKeyPressed( actionEvent -> {
            if (actionEvent.getCode() == KeyCode.ENTER || actionEvent.getCode() == KeyCode.TAB && !actionEvent.isShiftDown()) {

                String inputText = tfPower.getText().trim();
                if (!inputText.isEmpty()) {
                    cbPackage.requestFocus();
                }
                else {
                    tfPower.requestFocus();
                }
            }

            if (actionEvent.getCode() == KeyCode.TAB && actionEvent.isShiftDown()) {
                cbContentName.requestFocus();
            }

            if (actionEvent.getCode() == KeyCode.DOWN) {
                tableView.getSelectionModel().select(0);
            }
        });

        cbContentName.setOnKeyPressed( actionEvent -> {
            if (actionEvent.getCode() == KeyCode.ENTER || actionEvent.getCode() == KeyCode.TAB && !actionEvent.isShiftDown()) {

                String inputText = cbContentName.getEditor().getText().trim();
                if (!inputText.isEmpty()) {
                    boolean itemExists = cbContentName.getItems().stream()
                            .anyMatch(item -> item.toString().equalsIgnoreCase(inputText));

                    if (!itemExists) {
                        Stage stage2 =(Stage) primaryStage.getScene().getWindow();
                        CustomConfirmationDialog.ConfirmationDialog(stage2,"Do you want to add "+cbContentName.getEditor().getText()+"?",output-> {

                            if (output) {
                                String status = insertContentNameMaster(inputText);
                                if ("success".equals(status)) {
                                    Platform.runLater(()->{
                                        items.clear();
                                        items.addAll(getContentNames());
                                        setComboBoxValue(cbContentName, inputText);
                                    });
                                    tfPower.requestFocus();
                                }
                            } else {
                                cbContentName.requestFocus();
                            }
                        });
                    }
                    else {
                        cbContentName.getSelectionModel().getSelectedItem();
                        tfPower.requestFocus();
                    }
                }
                else {
                    cbContentName.requestFocus();
                }
            }

            if (actionEvent.getCode() == KeyCode.TAB && actionEvent.isShiftDown()) {
                cbContentName.requestFocus();
            }

            if (actionEvent.getCode() == KeyCode.DOWN) {
                tableView.getSelectionModel().select(0);
            }
        });

        cbContentType.setOnKeyPressed( actionEvent -> {
            if (actionEvent.getCode() == KeyCode.ENTER || actionEvent.getCode() == KeyCode.TAB && !actionEvent.isShiftDown()) {

                String inputText = cbContentType.getEditor().getText().trim();
                if (!inputText.isEmpty()) {
                    boolean itemExists = cbContentType.getItems().stream()
                            .anyMatch(item -> item.toString().equalsIgnoreCase(inputText));

                    if (!itemExists) {
                        Stage stage2 =(Stage) primaryStage.getScene().getWindow();
                        CustomConfirmationDialog.ConfirmationDialog(stage2,"Do you want to add "+cbContentType.getEditor().getText()+"?",output-> {

                            if (output) {
                                String status = insertContentType(inputText);
                                if ("success".equals(status)) {
                                    Platform.runLater(()->{
                                        items1.clear();
                                        items1.addAll(getContentType());
                                        setComboBoxValue(cbContentType, inputText);
                                    });
                                    addButton.requestFocus();
                                }
                            } else {
                                cbContentType.requestFocus();
                            }
                        });
                    }
                    else {
                        cbContentType.getSelectionModel().getSelectedItem();
                        addButton.requestFocus();
                    }
                }
                else {
                    addButton.requestFocus();
                }
                actionEvent.consume();

            }
            if (actionEvent.getCode() == KeyCode.TAB && actionEvent.isShiftDown()) {
                    cbContentName.requestFocus();
            }

            if (actionEvent.getCode() == KeyCode.DOWN) {
                tableView.getSelectionModel().select(0);
                actionEvent.consume();
            }
        });



        Scene scene = new Scene(borderPane, 1000, 370);

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

        primaryStage.centerOnScreen();

        primaryStage.show();

        Platform.runLater(() -> {
            cbContentName.requestFocus();
        });

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                selectedItem[1] = tfPower.getText();
                insertIntoTable(selectedItem[0].toString(), selectedItem[1].toString(), selectedItem[2].toString(), selectedItem[3].toString());
            }
        });

        clButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Clear ComboBox selections
                cbContentType.setValue(null); // Clear selection
                cbContentName.setValue(null); // Clear selection
                cbPackage.setValue(null);

                // Clear TextField
                tfPower.clear();

                // Optionally, clear the table
                tbllstContentMasterInfo.clear();
                // Set focus back to the first ComboBox
                Platform.runLater(() -> cbContentName.requestFocus());
            }
        });
        subButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                inputName = "";
                for (int i = 0; i < tbllstContentMasterInfo.size(); i++) {
                    inputName += tbllstContentMasterInfo.get(i).getContentName() + " " + tbllstContentMasterInfo.get(i).getPower() + " " + tbllstContentMasterInfo.get(i).getPacking() + " " + tbllstContentMasterInfo.get(i).getContentType();
                    if (i < tbllstContentMasterInfo.size() - 1) {
                        inputName += ",";
                    }
                }
                selectedItem[4] = inputName;
                callback.accept(selectedItem);
                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
        });

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
        });
    }

    /**** adding selected data into table view of content master in popup window  ****/
    public void insertIntoTable(String cmbContentNameText, String contentPowerText, String cmbContentPackText, String cmbContentTypeText) {

        ProductContentsMasterDTO contentMasterDetailList = new ProductContentsMasterDTO(0, cmbContentNameText, contentPowerText, cmbContentPackText, cmbContentTypeText);
        if (contentEdtIdx >= 0) {
            tbllstContentMasterInfo.remove(contentEdtIdx);
            tbllstContentMasterInfo.add(contentMasterDetailList);
            contentEdtIdx = -1;
        } else {
            tbllstContentMasterInfo.add(contentMasterDetailList);
        }
    }

    private String insertContentNameMaster(String input) {

//        String responseObejct = "";
        String status = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("contentName", input);

            String formData = Globals.mapToStringforFormData(map);

            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.PRODUCT_CREATE_CONTENTS_MASTER_ENDPOINT);

            ResponseMsg responseBody = new Gson().fromJson(response.body(), ResponseMsg.class);
            if (responseBody.getResponseStatus() == 200) {
//                responseObejct = "" + responseBody.getResponseObject();
                status = "success";
            } else {
                productLogger.error("Response is Empty in insertContentNameMaster()----->");
            }

        } catch (Exception e) {
            productLogger.error("Exception in insertContentNameMaster()----> " + e.getMessage());
        }
        return status;

    }

    private String insertContentType(String input) {

        String status = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("contentNameDose", input);
            String formData = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.PRODUCT_CREATE_CONTENTS_TYPE_ENDPOINT);
            ResponseMsg responseBody = new Gson().fromJson(response.body(), ResponseMsg.class);
            if (responseBody.getResponseStatus() == 200) {
//                responseObejct = "" + responseBody.getResponseObject();
                status = "success";
            } else {
                productLogger.error("Response is Empty in insertContentType()----->");
            }

        } catch (Exception e) {
            productLogger.error("Exception in insertContentType()----> " + e.getMessage());
        }
        return status;

    }

    public List<CommonDTO> getContentNames() {
        try {
            contentNamesList.clear();
            HttpResponse<String> response = APIClient.getRequest(EndPoints.PRODUCT_GET_CONTENTS_MASTER_ENDPOINT);
            ContentMasterResDTO contentMasterResDTO = new Gson().fromJson(response.body(), ContentMasterResDTO.class);
            if (contentMasterResDTO.getResponseStatus() == 200) {
                List<ContentMasterListDTO> list1 = contentMasterResDTO.getResponseObject();
                for (ContentMasterListDTO contentList : list1) {
                    contentNamesList.add(new CommonDTO(contentList.getContentName(), contentList.getId().toString()));
                }
            } else {
                productLogger.info("ResponseObject is null--->getContentNames()");
            }

        } catch (Exception e) {
            productLogger.error("Exception in getContentNames()");
        }

        return contentNamesList;
    }

    public List<CommonDTO> getPackageList() {
        packageList.clear();
        try {
            HttpResponse<String> response = APIClient.getRequest(EndPoints.PRODUCT_GET_CONTENT_PACKINGS_ENDPOINT);
            ContentPackingResDTO contentPackResDTO = new Gson().fromJson(response.body(), ContentPackingResDTO.class);
            if (contentPackResDTO.getResponseStatus() == 200) {
                List<ContentPackingListDTO> list1 = contentPackResDTO.getResponseObject();
                for (ContentPackingListDTO contentList : list1) {
                    packageList.add(new CommonDTO(contentList.getContentPackageName(), contentList.getId().toString()));
                }
            } else {
                productLogger.info("ResponseObject is null--->getPackageList()");
            }

        } catch (Exception e) {
            productLogger.error("Exception in getPackageList()");
        }

        return packageList;
    }

    private List<CommonDTO> getContentType() {
        contentTypeList.clear();
        try {
            HttpResponse<String> response = APIClient.getRequest(EndPoints.PRODUCT_GET_CONTENT_TYPE_ENDPOINT);
            ContentTypeResDTO contentTypeResDTO = new Gson().fromJson(response.body(), ContentTypeResDTO.class);
            if (contentTypeResDTO.getResponseStatus() == 200) {
                List<ContentTypeListDTO> list1 = contentTypeResDTO.getResponseObject();
                for (ContentTypeListDTO contentList : list1) {
                    contentTypeList.add(new CommonDTO(contentList.getContentNameDose(), contentList.getId().toString()));
                }
            } else {
                productLogger.info("ResponseObject is null--->getPackageList()");
            }

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            productLogger.error("Exception in getPackageList():" + exceptionAsString);
        }

        return contentTypeList;

    }

    private void setContentsMaster(Object[] object) {
        tfContents.setText("");
        contentMapApiRequests.clear();
        if (object != null) {
            for (CommonDTO commonDTO : contentNamesList) {
                if (object[0] != null) {
                    if (object[0].toString().equals(commonDTO.getText())) {
                        cmbContentNameText = commonDTO.getText();
                    }
                }
            }
            if (object[1] != null) {
                contentPowerText = object[1].toString();

            }

            for (CommonDTO commonDTO : packageList) {
                if (object[2] != null) {
                    if (object[2].toString().equals(commonDTO.getText())) {
                        cmbContentPackText = commonDTO.getText();
                    }
                }
            }
            for (CommonDTO commonDTO : contentTypeList) {
                if (object[3] != null) {
                    if (object[3].toString().equals(commonDTO.getText())) {
                        cmbContentTypeText = commonDTO.getText();
                    }
                }
            }
            inputName = object[4].toString();
            tfContents.setText(inputName);
        }
    }


    public String insertEcommType(String input) {
        String status = null;
        Map<String, String> map = new HashMap<>();
        map.put("ecomType", input);

        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "create_ecom_master");

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);

        System.out.println("json : "+responseBody);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            status = "success";
        }
        return status;

    }

    public void getEcommMapData(Object[] object) {

        ObservableList<CommonDTO> eccomList = FXCollections.observableArrayList(getEcommList());

        if (object[0] != null) {
            for (CommonDTO commonDTO : eccomList) {
                if (object[0].toString().equals(commonDTO.getText())) {
                    productMap.put("ecomType", commonDTO.getId());
                    ecommTypeString = commonDTO.getText();
                    tfEcommType.setText(ecommTypeString);
                }
            }
        }

    }

    public List<CommonDTO> getEcommList() {
        ecommTypeList.clear();
        try {
            HttpResponse<String> response = APIClient.getRequest("get_all_ecom_master");
            EcommResDTO ecommResDTO = new Gson().fromJson(response.body(), EcommResDTO.class);
            if (ecommResDTO.getResponseStatus() == 200) {

                List<EcommResListDTO> list1 = ecommResDTO.getResponse();

                for (EcommResListDTO ecommResListDTO : list1) {
                    ecommTypeList.add(new CommonDTO(ecommResListDTO.getEcommerceType(), ecommResListDTO.getId().toString()));
                }
            } else {
                System.out.println("responseObject is null");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ecommTypeList;
    }

    public void showEcomm() {
        Stage stage = (Stage) spRootPane.getScene().getWindow();

        openEcommPopUp(stage, "Configure E-Commerce", (input) -> {
            getEcommMapData(input);
        });
    }

    public <T> void openEcommPopUp(Stage stage, String title, Consumer<Object[]> callback) {
        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();

        primaryStage.initOwner(stage);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);


        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");

        Platform.runLater(() -> {
            borderPane.requestFocus();
        });


        //BorderPan under Top Layout....................................................................................................................
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(998);
        hbox_top.setMaxWidth(998);
        hbox_top.setPrefWidth(998);
        hbox_top.setMaxHeight(50);
        hbox_top.setMinHeight(50);
        hbox_top.setPrefHeight(50);

        Label popup_title = new Label(title);
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            switchIsEcomm.switchOnProperty().set(false);
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        HBox.setMargin(imageView, new Insets(0, 10, 0, 0));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #d9f0fb; -fx-border-color: #ffffff; -fx-border-width: 0 0 3 0;");


        //BorderPane Under Center Layout.....................................................................................................
        HBox hbox_center = new HBox();
        hbox_center.setMinWidth(998);
        hbox_center.setMaxWidth(998);
        hbox_center.setPrefWidth(998);
        hbox_center.setAlignment(Pos.CENTER);
        hbox_center.setStyle("-fx-background-color: #e6f2f8;");

        Object[] selectedItem = new Object[1];
        Integer initialIndex = -1;

        EventHandler<ActionEvent> cbMEcommerceTypeAction = event -> {
            ComboBox<T> comboBox = (ComboBox<T>) event.getSource();
            selectedItem[0] = comboBox.getValue();
        };
        ComboBox<CommonDTO> cbMEcommerceType = createComboBox("E-Commerce Type", cbMEcommerceTypeAction);

        ObservableList<CommonDTO> items = FXCollections.observableArrayList(getEcommList());
        cbMEcommerceType.setItems(items);
        AutoCompleteBox autoCompleteBox1 = new AutoCompleteBox(cbMEcommerceType, initialIndex);


        setComboBoxValue(cbMEcommerceType, ecommTypeString);

        TextField tfSellingPrice = new TextField();
        tfSellingPrice.setPromptText("Selling Price");
        tfSellingPrice.setMinWidth(140);
        tfSellingPrice.setMaxWidth(140);
        tfSellingPrice.setPrefWidth(140);
        CommonValidationsUtils.onlyEnterNumbers(tfSellingPrice);  // Ensure only numbers can be entered

        TextField tfDiscount = new TextField();
        tfDiscount.setPromptText("Discount %");
        tfDiscount.setMinWidth(140);
        tfDiscount.setMaxWidth(140);
        tfDiscount.setPrefWidth(140);
        CommonValidationsUtils.onlyEnterNumbers(tfDiscount);  // Ensure only numbers can be entered

        TextField tfAmount = new TextField();
        tfAmount.setPromptText("Amount");
        tfAmount.setMinWidth(140);
        tfAmount.setMaxWidth(140);
        tfAmount.setPrefWidth(140);
        CommonValidationsUtils.onlyEnterNumbers(tfAmount);  // Ensure only numbers can be entered

        TextField tfLoyalty = new TextField();
        tfLoyalty.setPromptText("Loyalty");
        tfLoyalty.setMinWidth(140);
        tfLoyalty.setMaxWidth(140);
        tfLoyalty.setPrefWidth(140);
        CommonValidationsUtils.onlyEnterNumbers(tfLoyalty);



        String sellingPriceValue = productMap.get("ecomPrice");
        if (sellingPriceValue != null) {
            tfSellingPrice.setText(sellingPriceValue);
        } else {
            tfSellingPrice.setText("");
        }


        String discountValue = productMap.get("ecomDiscount");
        if (discountValue != null) {
            tfDiscount.setText(discountValue);
        } else {
            tfDiscount.setText("");
        }


        String amountValue = productMap.get("ecomAmount");
        if (amountValue != null) {
            tfAmount.setText(amountValue);
        } else {
            tfAmount.setText("");
        }


        String loyaltyValue = productMap.get("ecomLoyality");
        if (loyaltyValue != null) {
            tfLoyalty.setText(loyaltyValue);
        } else {
            tfLoyalty.setText("");
        }

        productMap.put("ecomPrice", tfSellingPrice.getText() != null ? tfSellingPrice.getText() : "");
        productMap.put("ecomDiscount", tfDiscount.getText() != null ? tfDiscount.getText() : "");
        productMap.put("ecomAmount", tfAmount.getText() != null ? tfAmount.getText() : "");
        productMap.put("ecomLoyalty", tfLoyalty.getText() != null ? tfLoyalty.getText() : "");

//
//        Insets Margin = new Insets(0, 20, 0, 1);
//        HBox.setMargin(tfSellingPrice, Margin);
//        HBox.setMargin(tfAmount, Margin);
//        HBox.setMargin(tfDiscount, Margin);

        hbox_center.setSpacing(20);

        hbox_center.getChildren().addAll(cbMEcommerceType, tfSellingPrice, tfDiscount, tfAmount, tfLoyalty);

        tfSellingPrice.setOnKeyPressed(actionEvent -> {
            if (actionEvent.getCode() == KeyCode.ENTER || actionEvent.getCode() == KeyCode.TAB && !actionEvent.isShiftDown()) {
                    tfDiscount.requestFocus();
            }
        });

        tfDiscount.setOnKeyPressed(actionEvent -> {
            if (actionEvent.getCode() == KeyCode.ENTER || actionEvent.getCode() == KeyCode.TAB && !actionEvent.isShiftDown()) {
                tfAmount.requestFocus();
            }
        });

        tfAmount.setOnKeyPressed(actionEvent -> {
            if (actionEvent.getCode() == KeyCode.ENTER || actionEvent.getCode() == KeyCode.TAB && !actionEvent.isShiftDown()) {
                tfLoyalty.requestFocus();
            }
        });






        //BorderPane Under Bottom Layout..............................................................................................................
        HBox hbox_bottom = new HBox();
        hbox_bottom.setMinWidth(998);
        hbox_bottom.setMaxWidth(998);
        hbox_bottom.setPrefWidth(998);
        hbox_bottom.setMaxHeight(55);
        hbox_bottom.setMinHeight(55);
        hbox_bottom.setPrefHeight(55);
        hbox_bottom.setSpacing(10);
        hbox_bottom.setStyle("-fx-background-radius: 0 0 5 5; -fx-background-color: white;");
        hbox_bottom.setAlignment(Pos.CENTER_RIGHT);

        Button suButton = new Button("Submit");
        Button clButton = new Button("Clear");
        hbox_bottom.setMargin(clButton, new Insets(0, 10, 0, 0));


        hbox_bottom.getChildren().addAll(suButton,clButton);

        suButton.setId("sub");
        clButton.setId("can");
        suButton.setMinWidth(70);
        suButton.setMaxWidth(70);
        suButton.setPrefWidth(70);
        clButton.setMinWidth(70);
        clButton.setMaxWidth(70);
        clButton.setPrefWidth(70);

        tfLoyalty.setOnKeyPressed(actionEvent -> {
            if (actionEvent.getCode() == KeyCode.ENTER || actionEvent.getCode() == KeyCode.TAB && !actionEvent.isShiftDown()) {
                suButton.requestFocus();
            }
        });

        suButton.setOnKeyPressed(actionEvent -> {
            if (actionEvent.getCode() == KeyCode.LEFT || actionEvent.getCode() == KeyCode.RIGHT) {
                clButton.requestFocus();
            }
            if(actionEvent.getCode() == KeyCode.TAB && actionEvent.isShiftDown()){
                tfLoyalty.requestFocus();
            }
        });

        clButton.setOnKeyPressed(actionEvent -> {
            if (actionEvent.getCode() == KeyCode.RIGHT || actionEvent.getCode() == KeyCode.LEFT) {
                suButton.requestFocus();
            }
            if(actionEvent.getCode() == KeyCode.TAB && actionEvent.isShiftDown()){
                tfLoyalty.requestFocus();
            }
        });

        borderPane.setTop(hbox_top);
        borderPane.setCenter(hbox_center);
        borderPane.setBottom(hbox_bottom);

//        buttonEcomm.setOnAction(actionEvent -> {
//            Stage stage1 = (Stage) borderPane.getScene().getWindow();
//            SingleInputDialogs.singleInputDialog("", stage1, "Add New E-Commerce Type", input -> {
//
//                String status = insertEcommType(input);
//                if ("success".equals(status)) {
//                    cbMEcommerceType.getItems().clear();
//                    cbMEcommerceType.setItems(FXCollections.observableArrayList(getEcommList()));
//                    setComboBoxValue(cbMEcommerceType, input);
//                }
//            });
//        });


        cbMEcommerceType.setOnKeyPressed(actionEvent -> {
            if (actionEvent.getCode() == KeyCode.ENTER) {
                String inputText = cbMEcommerceType.getEditor().getText().trim();
                if (!inputText.isEmpty()) {
                    boolean itemExists = cbMEcommerceType.getItems().stream()
                            .anyMatch(item -> item.toString().equalsIgnoreCase(inputText));

                    if (!itemExists) {
                        Stage stage2 =(Stage) primaryStage.getScene().getWindow();
                        CustomConfirmationDialog.ConfirmationDialog(stage2,"Do you want to add "+cbMEcommerceType.getEditor().getText()+"?",output-> {

                            if (output) {
                                String status = insertEcommType(inputText);
                                if ("success".equals(status)) {
                                    Platform.runLater(()->{
                                        items.clear();
                                        items.addAll(getEcommList());
                                        setComboBoxValue(cbMEcommerceType, inputText);
                                    });
                                    tfSellingPrice.requestFocus();
                                }
                            } else {
                                cbMEcommerceType.requestFocus();
                            }
                        });
                    }
                    else {
                        cbMEcommerceType.getSelectionModel().getSelectedItem();
                        tfSellingPrice.requestFocus();
                    }
                }
            }
        });


        Scene scene = new Scene(borderPane, 1000, 170);

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

        primaryStage.centerOnScreen();

        primaryStage.show();

        Platform.runLater(() -> {
            cbMEcommerceType.requestFocus();
        });
        tfDiscount.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

                if (tfSellingPrice.getText() != null && tfDiscount.getText() != null && !tfSellingPrice.getText().isEmpty() && !tfDiscount.getText().isEmpty()) {

                    double sellingPrice = Double.parseDouble(tfSellingPrice.getText());
                    double discount = Double.parseDouble(tfDiscount.getText());

                    ecommDisAmount(sellingPrice, discount, (input) -> {
                        String formattedAmount = String.format("%.2f", input);
                        tfAmount.setText(formattedAmount);
                    });
                } else {
                    tfAmount.clear();
                }
            }
        });
        suButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                callback.accept(selectedItem);
                primaryStage.close();
                switchIsEcomm.switchOnProperty().set(false);

                productMap.put("isEcom", "true");

                productMap.put("ecomPrice", tfSellingPrice.getText());
                productMap.put("ecomDiscount", tfDiscount.getText());
                productMap.put("ecomAmount", tfAmount.getText());
                productMap.put("ecomLoyality", tfLoyalty.getText());

                tfEcommPrice.setText(productMap.get("ecomPrice"));
                tfEcommDisc.setText(productMap.get("ecomDiscount"));
                tfEcommAmt.setText(productMap.get("ecomAmount"));
                tfEcommLoyalty.setText(productMap.get("ecomLoyality"));

                OverlaysEffect.removeOverlaysEffect(stage);
            }
        });

        clButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                cbMEcommerceType.setValue(null);
                tfSellingPrice.clear();
                tfDiscount.clear();
                tfAmount.clear();
                tfLoyalty.clear();
                cbMEcommerceType.requestFocus();
//                primaryStage.close();
                switchIsEcomm.switchOnProperty().set(false);
                productMap.put("isEcom", "false");
                productMap.remove("ecomPrice");
                productMap.remove("ecomDiscount");
                productMap.remove("ecomAmount");
                productMap.remove("ecomLoyality");
                productMap.remove("ecomType");
                ecommTypeString = null;
                tfEcommPrice.clear();
                tfEcommDisc.clear();
                tfEcommAmt.clear();
                tfEcommLoyalty.clear();
                tfEcommType.clear();

//                OverlaysEffect.removeOverlaysEffect(stage);
            }
        });

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                switchIsEcomm.switchOnProperty().set(false);
                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
        });

    }

    private void ecommDisAmount(Double selling_Price, Double discount, Consumer<Double> callback) {
        callback.accept(selling_Price * (discount / 100));
    }

    private void getProductById(Long prId) {
        try {
            ProductListController.selectedPrId = 0L;
            Map<String, String> map = new HashMap<>();
            map.put("product_id", "" + prId);
            String formData = Globals.mapToStringforFormData(map);
            // productGetByIdResponse = APIClient.postFormDataRequest(formData, EndPoints.GET_PRODUCT_BY_ID_ENDPOINT);
            APIClient apiClient = new APIClient(EndPoints.GET_PRODUCT_BY_ID_ENDPOINT, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    String response = workerStateEvent.getSource().getValue().toString();
                    System.out.println("Edit Product Resp => "+response);
                    ProductMasterDTO productMatser = new Gson().fromJson(response, ProductMasterDTO.class);
                    if (productMatser.getResponseStatus() == 200) {
                        System.out.println("Response Completed");
                        productLogger.debug("Response Completed");
                        setupMapResponse(productMatser.getResponseObject());
                    } else {
                        productLogger.error("Data not received: getProductById() ");
                    }
                }
            });
            apiClient.start();

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            productLogger.error("Exception in getProductById():" + exceptionAsString);
        }
    }


    private void setupMapResponse(ProductResponseObject productResponseObject) {
        updateResponse.put("productName", productResponseObject.getProductName());
        updateResponse.put("id", productResponseObject.getId());
        updateResponse.put("description", productResponseObject.getDescription());
        updateResponse.put("productCode", productResponseObject.getProductCode());
        updateResponse.put("isBatchNo", productResponseObject.getIsBatchNo());
        updateResponse.put("isInventory", productResponseObject.getIsInventory());
        updateResponse.put("isSerialNo", productResponseObject.getIsSerialNo());

        updateResponse.put("barcodeNo", productResponseObject.getBarcodeNo());
        updateResponse.put("shelfId", productResponseObject.getShelfId());
        updateResponse.put("barcodeSalesQty", productResponseObject.getBarcodeSalesQty());
        updateResponse.put("purchaseRate", productResponseObject.getPurchaseRate());
        updateResponse.put("margin", productResponseObject.getMargin());
        updateResponse.put("brandId", productResponseObject.getBrandId());
        updateResponse.put("packagingId", productResponseObject.getPackagingId());
        updateResponse.put("groupId", productResponseObject.getGroupId());
        updateResponse.put("subgroupId", productResponseObject.getSubgroupId());
        updateResponse.put("categoryId", productResponseObject.getCategoryId());
        updateResponse.put("subcategoryId", productResponseObject.getSubcategoryId());
        updateResponse.put("weight", productResponseObject.getWeight());
        updateResponse.put("weightUnit", productResponseObject.getWeightUnit());
        updateResponse.put("disPer1", productResponseObject.getDisPer1());
        updateResponse.put("hsnNo", productResponseObject.getHsnNo());
        updateResponse.put("tax", productResponseObject.getTax());
        updateResponse.put("taxApplicableDate", productResponseObject.getTaxApplicableDate());
        updateResponse.put("type_tax", productResponseObject.getTaxType());


        updateResponse.put("igst", productResponseObject.getIgst());
        updateResponse.put("cgst", productResponseObject.getCgst());
        updateResponse.put("sgst", productResponseObject.getSgst());

        double minStockValue = productResponseObject.getMinStock();
        int minStockIntValue = (int) minStockValue;
        updateResponse.put("minStock", minStockIntValue);
        double maxStockValue = productResponseObject.getMaxStock();
        int maxStockIntValue = (int) maxStockValue;
        updateResponse.put("maxStock", maxStockIntValue);

        updateResponse.put("fsrmh",productResponseObject.getFsrmh());
        updateResponse.put("fsrai",productResponseObject.getFsrai());
        updateResponse.put("csrmh",productResponseObject.getCsrmh());
        updateResponse.put("csrai",productResponseObject.getCsrai());


        updateResponse.put("isWarranty", productResponseObject.getIsWarranty());
        updateResponse.put("nodays", productResponseObject.getNodays());
        updateResponse.put("isCommision", productResponseObject.getIsCommision());

        updateResponse.put("isGVProducts", productResponseObject.getIsGVProducts());
        updateResponse.put("gvOfProducts", productResponseObject.getGvOfProducts());
        updateResponse.put("drugType", productResponseObject.getDrugType());


        updateResponse.put("isMIS", productResponseObject.getIsMIS());
        updateResponse.put("isGroup", productResponseObject.getIsGroup());
        updateResponse.put("isFormulation", productResponseObject.getIsFormulation());
        updateResponse.put("isCategory", productResponseObject.getIsCategory());
        updateResponse.put("isSubcategory", productResponseObject.getIsSubcategory());
        updateResponse.put("isPrescription", productResponseObject.getIsPrescription());
        updateResponse.put("drugContent", productResponseObject.getDrugContent());
        updateResponse.put("uploadImage", productResponseObject.getUploadImage());

        updateResponse.put("isEcom", productResponseObject.getIsEcom());
        updateResponse.put("ecomType", productResponseObject.getEcomType());
        updateResponse.put("ecomPrice", productResponseObject.getEcomPrice());
        updateResponse.put("ecomDiscount", productResponseObject.getEcomDiscount());
        updateResponse.put("ecomAmount", productResponseObject.getEcomAmount());
        updateResponse.put("ecomLoyality", productResponseObject.getEcomLoyality());

        updateResponse.put("imageExists", productResponseObject.getImageExists());
        updateResponse.put("prevImage1", productResponseObject.getPrevImage1());
        updateResponse.put("prevImage2", productResponseObject.getPrevImage2());
        updateResponse.put("prevImage3", productResponseObject.getPrevImage3());
        updateResponse.put("prevImage4", productResponseObject.getPrevImage4());
        updateResponse.put("prevImage5", productResponseObject.getPrevImage5());
        updateResponse.put("productType", productResponseObject.getProductType());

        contentResponse = productResponseObject.getContentMap();
        productRowsResponse = productResponseObject.getProductrows();

        System.out.println("productRowsResponse : "+productResponseObject.getProductrows());


//        System.out.println(updateResponse);
//
//        System.out.println(contentResponse);
//
//        System.out.println(productRowsResponse);
        if(updateResponse.get("isBatchNo").toString()=="false"){
            tcMRP.setVisible(true);
            tcPurRate.setVisible(true);
            tcRate1.setVisible(true);
            tcRate2.setVisible(true);
            tcRate3.setVisible(true);
        }


        switchIsBatch.switchOnProperty().set((Boolean) updateResponse.get("isBatchNo"));

        int count = 0;

        for (Productrow productrow : productRowsResponse) {

            String sLevelA = "";
            String sLevelB = "";
            String sLevelC = "";
            String sUnit = "";
            System.out.println("Prd Id:"+productrow.getId());
            System.out.println("Is Rate:"+productrow.getIsRate());

            for (CommonDTO commonDTO : levelAList) {
                String id = commonDTO.getId();
                String selectedLevelA = productrow.getSelectedLevelA();
                if (!id.isEmpty() && !selectedLevelA.isEmpty()) {
                    if (Long.parseLong(id) == Long.parseLong(selectedLevelA)) {
                        sLevelA = commonDTO.getText();
                    }
                }
            }

            for (CommonDTO commonDTO : levelBList) {
                String id = commonDTO.getId();
                String selectedLevelB = productrow.getSelectedLevelB();
                if (!id.isEmpty() && !selectedLevelB.isEmpty()) {
                    if (Long.parseLong(id) == Long.parseLong(selectedLevelB)) {
                        sLevelB = commonDTO.getText();
                    }
                }
            }


            for (CommonDTO commonDTO : levelCList) {
                String id = commonDTO.getId();
                String selectedLevelC = productrow.getSelectedLevelC();
                if (!id.isEmpty() && !selectedLevelC.isEmpty()) {
                    if (Long.parseLong(id) == Long.parseLong(selectedLevelC)) {
                        sLevelC = commonDTO.getText();
                    }
                }
            }


            for (CommonDTO commonDTO : unitList) {
                String id = commonDTO.getId();
                String selectedUnit = productrow.getSelectedUnit();
                if (!id.isEmpty() && !selectedUnit.isEmpty()) {
                    if (Long.parseLong(id) == Long.parseLong(selectedUnit)) {
                        sUnit = commonDTO.getText();
                    }
                }
            }

            ProductRowDTO productRowDTO = new ProductRowDTO(String.valueOf(count), sLevelA, sLevelB, sLevelC, sUnit, productrow.getConv().toString(), productrow.getMrp().toString(), productrow.getPurRate().toString(), productrow.getRate1().toString(), productrow.getRate2().toString(), productrow.getRate3().toString(),productrow.getRate4().toString(),"", productrow.getIsNegetive().toString(), productrow.getIsRate().toString());


            List<ProductRowBatchItem> B_list = new ArrayList<>();

            for (ProductBatchList productBatchList : productRowsResponse.get(count).getBatchList()) {
                B_list.add(new ProductRowBatchItem(productBatchList.getId().toString(), productBatchList.getbNo(), productBatchList.getBatchId(), productBatchList.getOpeningQty().toString(), productBatchList.getbFreeQty(), productBatchList.getbMrp(), productBatchList.getbSaleRate(), productBatchList.getbPurchaseRate(), productBatchList.getbCosting().toString(), productBatchList.getbExpiry(), productBatchList.getbManufacturingDate()

                ));
            }

            for (int i = 0; i < B_list.size(); i++) {
                B_list.get(i).setIsOpeningbatch(String.valueOf(isBatch));
            }

            double opn_stk = 0.0;
            for (int i = 0; i < B_list.size(); i++) {
                String openingQtyStr = B_list.get(i).getOpening_qty();
                String freeQtyStr = B_list.get(i).getB_free_qty();

                double openingQty = openingQtyStr == null || openingQtyStr.isEmpty() ? 0.0 : Double.parseDouble(openingQtyStr);
                double freeQty = freeQtyStr == null || freeQtyStr.isEmpty() ? 0.0 : Double.parseDouble(freeQtyStr);

                opn_stk += openingQty + freeQty;
            }


            productRowDTO.setOpn_stock(String.valueOf(opn_stk));

            productRowDTO.setBatchList(B_list);

            productRowDTO.setId(String.valueOf(productrow.getId()));


            if (count < tvProductRow.getItems().size()) {
                tvProductRow.getItems().set(count, productRowDTO);
            } else {
                tvProductRow.getItems().add(productRowDTO);
            }


            count++;
        }

        System.out.println("Tbale :  " + tvProductRow.getItems());

        setInputFields();//get By Id for Update
    }

    private void setInputFields() {

        screen_type = "edit";

        productDescription = (String) updateResponse.get("description");

        try {
            if (updateResponse.get("isGVProducts").equals(true)){
                tfCode.setDisable(true);
                tfName.setDisable(true);
                btnNamePlus.setDisable(true);
                cmbPack.setDisable(true);
//                btnPackingPlus.setDisable(true);
                cmbBrand.setDisable(true);
//                btnBrandPlus.setDisable(true);
                cmbTaxType.setDisable(true);
                cmbTax.setDisable(true);
                tfApplicableFrom.setDisable(true);
                cmbHsn.setDisable(true);
//                btnHsnPlus.setDisable(true);
                tfDiscPer.setDisable(true);
                tfMargin.setDisable(true);
                tfBarcode.setDisable(true);
                tfShelfId.setDisable(true);
                switchMis.setDisable(true);
                tfManufacturer.setDisable(true);
                tfFormulation.setDisable(true);
                tfCategory.setDisable(true);
                tfSubCategory.setDisable(true);
                switchIsInventory.setDisable(true);
                tfMinStk.setDisable(true);
                tfMaxStk.setDisable(true);
                switchIsBatch.setDisable(true);
                switchIsSerialNo.setDisable(true);
                switchIsWarranty.setDisable(true);
                tfWarranty.setDisable(true);
                switchIsPrescription.setDisable(true);
                cmbDrugType.setDisable(true);
//                btnDrugTypePlus.setDisable(true);
                tfContents.setDisable(true);
                btnContents.setDisable(true);
                switchIsEcomm.setDisable(true);
                tfEcommType.setDisable(true);
                tfEcommPrice.setDisable(true);
                tfEcommDisc.setDisable(true);
                tfEcommAmt.setDisable(true);
                tfEcommLoyalty.setDisable(true);
                tvProductRow.setDisable(true);
                btnSubmit.setDisable(true);
            }
            tfCode.setText((String) updateResponse.get("productCode"));
            tfName.setText((String) updateResponse.get("productName"));

//            LocalDate applicable_date = LocalDate.parse((String) updateResponse.get("taxApplicableDate"));
//            tfApplicableFrom.setText(applicable_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            String taxApplicableDate = (String) updateResponse.get("taxApplicableDate");
            if (taxApplicableDate != null && tfApplicableFrom != null) {
                LocalDate applicable_date = LocalDate.parse(taxApplicableDate);
                tfApplicableFrom.setText(applicable_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }

            tfDiscPer.setText(updateResponse.get("disPer1") != null ? String.valueOf(updateResponse.get("disPer1")) : "");
            tfMargin.setText(updateResponse.get("margin") != null ? String.valueOf(updateResponse.get("margin")) : "");
            tfBarcode.setText(updateResponse.get("barcodeNo") != null ? String.valueOf(updateResponse.get("barcodeNo")) : "");
            tfShelfId.setText((String) updateResponse.get("shelfId"));

            tfMinStk.setText(updateResponse.get("minStock") != null ? String.valueOf(updateResponse.get("minStock")) : "");
            tfMaxStk.setText(updateResponse.get("maxStock") != null ? String.valueOf(updateResponse.get("maxStock")) : "");


            setComboBoxValueId(cmbPack, (Long) updateResponse.get("packagingId"));
            setComboBoxValueId(cmbBrand, (Long) updateResponse.get("brandId"));

            String type_tax_response = (String) updateResponse.get("type_tax");

            if (cmbTaxType.getItems().contains(type_tax_response)) {
                cmbTaxType.setValue(type_tax_response);
            }
            setComboBoxValueId(cmbTax, (Long) updateResponse.get("tax"));
            cmbTaxText = cmbTax.getValue().toString();
            setComboBoxValueId(cmbHsn, (Long) updateResponse.get("hsnNo"));


            cmbBrandId = (Long) updateResponse.get("brandId");
            cmbPackingId = (Long) updateResponse.get("packagingId");
            cmbTaxTypeText = String.valueOf(updateResponse.get("type_tax"));
            cmbTaxId = (Long) updateResponse.get("tax");
            cmbHsnId = (Long) updateResponse.get("hsnNo");

            //switchMis.switchOnProperty().set(productResponseObject.getIsMIS());
            switchIsInventory.switchOnProperty().set((Boolean) updateResponse.get("isInventory"));

            switchIsSerialNo.switchOnProperty().set((Boolean) updateResponse.get("isSerialNo"));
//            switchIsGvProduct.switchOnProperty().set((Boolean) updateResponse.get("isGVProducts"));
            if ((Boolean) updateResponse.get("isSerialNo")) {
                switchIsWarranty.switchOnProperty().set((Boolean) updateResponse.get("isWarranty"));
            }


            Boolean isPrescription2 = (Boolean) updateResponse.get("isPrescription");
            Boolean isCommission2 = (Boolean) updateResponse.get("isCommision");

            if (isPrescription2 == null) {
                isPrescription2 = false;
            }
            if (isCommission2 == null) {
                isCommission2 = true;
            }

            switchIsPrescription.switchOnProperty().set(isPrescription2);
//            isCommision = isCommission2;
//            switchIsCommission.switchOnProperty().set(isCommision);


            if (updateResponse.get("isWarranty") != null && (Boolean) updateResponse.get("isWarranty")) {
                tfWarranty.setText(String.valueOf(updateResponse.get("nodays")));
            }
/*

            if ((Boolean) updateResponse.get("isGVProducts")) {
                String s = (String) updateResponse.get("gvOfProducts");
                if (s.trim().toLowerCase().equals("owned")) {
                  //  rbGvOwned.setSelected(true);
                    selectedRadioValue = s;
                    cmbGvProduct.getSelectionModel().select("Owned");
                } else if (s.trim().toLowerCase().equals("traded")) {
                  //  rbTraded.setSelected(true);
                    selectedRadioValue = s;
                    cmbGvProduct.getSelectionModel().select("Traded");
                }
            }
*/


            if ((Boolean) updateResponse.get("isPrescription")) {
                String drugTypeString = (String) updateResponse.get("drugType");
                if (drugTypeString != null && !drugTypeString.isEmpty()) {
                    Long drugTypeId = Long.parseLong(drugTypeString);
                    setComboBoxValueId(cmbDrugType, drugTypeId);
                    cmbDrugTypeId = drugTypeId;
                }
            }


            productMap.put("isMIS", String.valueOf(updateResponse.get("isMIS")));


            if ((Boolean) updateResponse.get("isMIS")) {

                for (CommonDTO commonDTO : getManufacturerList()) {
                    if (updateResponse.get("groupId") != null && Long.parseLong(commonDTO.getId()) == (Long) updateResponse.get("groupId")) {
                        misDataToSetTexfield[0] = commonDTO.getText();
                        tfManufacturer.setText(commonDTO.getText());
                        productMap.put("groupId", commonDTO.getId());
                    }
                }

                for (CommonDTO commonDTO : getFormulationList()) {
                    if (updateResponse.get("subgroupId") != null && Long.parseLong(commonDTO.getId()) == (Long) updateResponse.get("subgroupId")) {
                        misDataToSetTexfield[1] = commonDTO.getText();
                        tfFormulation.setText(commonDTO.getText());
                        productMap.put("subgroupId", commonDTO.getId());
                    }
                }

                for (CommonDTO commonDTO : getCategoryList()) {
                    if (updateResponse.get("categoryId") != null && Long.parseLong(commonDTO.getId()) == (Long) updateResponse.get("categoryId")) {
                        misDataToSetTexfield[2] = commonDTO.getText();
                        tfCategory.setText(commonDTO.getText());
                        productMap.put("categoryId", commonDTO.getId());
                    }
                }

                for (CommonDTO commonDTO : getSubCategoryList()) {
                    if (updateResponse.get("subcategoryId") != null && Long.parseLong(commonDTO.getId()) == (Long) updateResponse.get("subcategoryId")) {
                        misDataToSetTexfield[3] = commonDTO.getText();
                        tfSubCategory.setText(commonDTO.getText());
                        productMap.put("subcategoryId", commonDTO.getId());
                    }
                }
            }


            productMap.put("isEcom", String.valueOf(updateResponse.get("isEcom")));


            if ((boolean) updateResponse.get("isEcom")) {

                if (updateResponse.get("ecomType") != null && !((String) updateResponse.get("ecomType")).isEmpty()) {
                    long ecomm_id = Long.parseLong((String) updateResponse.get("ecomType"));
                    for (CommonDTO commonDTO : getEcommList()) {
                        if (Long.parseLong(commonDTO.getId()) == ecomm_id) {
                            ecommTypeString = commonDTO.getText();
                            productMap.put("ecomType", commonDTO.getId());
                        }
                    }
                }
                tfEcommType.setText(ecommTypeString);

                productMap.put("ecomPrice", (String) updateResponse.get("ecomPrice"));
                productMap.put("ecomDiscount", (String) updateResponse.get("ecomDiscount"));
                productMap.put("ecomAmount", (String) updateResponse.get("ecomAmount"));
                productMap.put("ecomLoyality", (String) updateResponse.get("ecomLoyality"));

                tfEcommPrice.setText(productMap.get("ecomPrice"));
                tfEcommDisc.setText(productMap.get("ecomDiscount"));
                tfEcommAmt.setText(productMap.get("ecomAmount"));
                tfEcommLoyalty.setText(productMap.get("ecomLoyality"));
            }

            for (int i = 0; i < contentResponse.size(); i++) {
                tbllstContentMasterInfo.add(new ProductContentsMasterDTO(contentResponse.get(i).getId().intValue(),
                        contentResponse.get(i).getContentType(), contentResponse.get(i).getContentPower(), contentResponse.get(i).getContentPackage(), String.valueOf(contentResponse.get(i).getContentTypeDose())));
            }


            inputName = "";
            for (int i = 0; i < tbllstContentMasterInfo.size(); i++) {
                inputName += tbllstContentMasterInfo.get(i).getContentName() + " " +
                        tbllstContentMasterInfo.get(i).getPower() + " " +
                        tbllstContentMasterInfo.get(i).getPacking() + " " +
                        tbllstContentMasterInfo.get(i).getContentType();
                if (i < tbllstContentMasterInfo.size() - 1) {
                    inputName += ",";
                }
            }

            tfContents.setText(inputName);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public List<LevelListDTO> getLevelAList() {
        List<LevelListDTO> list1 = null;
        try {

            HttpResponse<String> response = APIClient.getRequest("get_outlet_levelA");
            LevelResDTO levelResDTO = new Gson().fromJson(response.body(), LevelResDTO.class);

            if (levelResDTO.getResponseStatus() == 200) {
                list1 = levelResDTO.getResponseObject();

            } else {
                System.out.println("responseObject is null");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list1;
    }

    public List<LevelListDTO> getLevelBList() {
        List<LevelListDTO> list1 = null;
        try {

            HttpResponse<String> response = APIClient.getRequest("get_outlet_levelB");
            LevelResDTO levelResDTO = new Gson().fromJson(response.body(), LevelResDTO.class);

            if (levelResDTO.getResponseStatus() == 200) {

                list1 = levelResDTO.getResponseObject();


            } else {
                System.out.println("responseObject is null");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list1;
    }

    public List<LevelListDTO> getLevelCList() {
        List<LevelListDTO> list1 = null;
        try {

            HttpResponse<String> response = APIClient.getRequest("get_outlet_levelC");
            LevelResDTO levelResDTO = new Gson().fromJson(response.body(), LevelResDTO.class);

            if (levelResDTO.getResponseStatus() == 200) {

                list1 = levelResDTO.getResponseObject();


            } else {
                System.out.println("responseObject is null");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list1;
    }

    public List<CUnitListDTO> getUnitList() {
        List<CUnitListDTO> list1 = null;
        try {

            HttpResponse<String> response = APIClient.getRequest("get_units_by_outlet");
            CUnitResDTO unitResDTO = new Gson().fromJson(response.body(), CUnitResDTO.class);

            if (unitResDTO.getResponseStatus() == 200) {

                list1 = unitResDTO.getResponseObject();

            } else {
                System.out.println("responseObject is null");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list1;
    }

    public void onClickSubmit(ActionEvent actionEvent) {
        validate();
    }

    public void validate() {
        Stage stage = (Stage) spRootPane.getScene().getWindow();
        if (tfCode.getText().isEmpty()) {
            AlertUtility.AlertDialogForErrorWithStage(stage, "Validation Error", "Code field is empty", output -> {
                if (output) {
                    tfCode.requestFocus();
                }
            });
        }
        else if(tfName.getText().isEmpty()){
            AlertUtility.AlertDialogForErrorWithStage(stage,"Validation Error","Name field is empty",output->{
                if (output){
                    tfName.requestFocus();
                }
            });
        }

        else if(cmbPackingId == null){
            AlertUtility.AlertDialogForErrorWithStage(stage,"Validation Error","Packing Required",output->{
                if (output){
                    cmbPack.requestFocus();
                }
            });
        }

        else if(cmbBrandId == null){
            AlertUtility.AlertDialogForErrorWithStage(stage,"Validation Error","Brand Required",output->{
                if (output){
                    cmbBrand.requestFocus();
                }
            });
        }
        else if(cmbHsnId == null){
            AlertUtility.AlertDialogForErrorWithStage(stage,"Validation Error","HSN Required",output->{
                if (output){
                    cmbHsn.requestFocus();
                }
            });
        }
        else {

            onSubmit();
        }
    }

}


class ButtonsAndComboBoxTableCell extends TableCell<ProductRowDTO, String> {

    private final TableCellCallback<JSONObject> callback;

    private final ComboBox<String> comboBox;
    private final Button add;
    private final Button delete;
    private final String columnName;

    ObservableList<CommonDTO> comboList = null;

    public ButtonsAndComboBoxTableCell(String columnName, TableCellCallback<JSONObject> callback, ObservableList<CommonDTO> comboList) {

        this.callback = callback;

        this.comboList = comboList;

        this.add = new Button();

        this.delete = new Button();

        ImageView imageView1 = new ImageView(new Image(getClass().getResourceAsStream("/com/opethic/genivis/ui/assets/add.png")));
        imageView1.setFitWidth(20);
        imageView1.setFitHeight(20);
        add.setGraphic(imageView1);
        add.setStyle("-fx-background-color: transparent; -fx-background-radius: 50; -fx-border-radius:50;" +
                "-fx-min-width: 20px; " +
                "-fx-min-height: 20px; " +
                "-fx-max-width: 20px; " +
                "-fx-max-height: 20px;");

        ImageView imageView2 = new ImageView(new Image(getClass().getResourceAsStream("/com/opethic/genivis/ui/assets/sub.png")));
        imageView2.setFitWidth(20);
        imageView2.setFitHeight(20);
        delete.setGraphic(imageView2);
        delete.setStyle("-fx-background-color: transparent; -fx-background-radius: 50; -fx-border-radius:50;" +
                "-fx-min-width: 20px; " +
                "-fx-min-height: 20px; " +
                "-fx-max-width: 20px; " +
                "-fx-max-height: 20px;");


        add.setOnAction(event -> {
            ProductRowDTO item = getTableView().getItems().get(getIndex());
            System.out.println("Button 1 clicked for item: " + item.getConv());
            int currentIndex = getTableRow().getIndex();
            getTableView().getItems().add(new ProductRowDTO(String.valueOf(currentIndex + 1), "", "", "","", "","", "", "", "", "", "", "", "false","false"));
            System.out.println("Row index: " + currentIndex);
        });


        delete.setOnAction(event -> {
//                ProductRowDTO item = getTableView().getItems().get(getIndex());
//                System.out.println("Button 2 clicked for item: " + item.getMrp());
//                System.out.println("Row index: " + getTableRow().getIndex());
            int indexToRemove = getTableRow().getIndex();
            if (indexToRemove != 0) {
                ProductRowDTO productRowDTO = getTableView().getItems().get(indexToRemove);
                getTableView().getItems().remove(indexToRemove);
                System.out.println("Row index: " + indexToRemove + " Id: " + productRowDTO.getId());
                JSONObject obj1 = new JSONObject();
                obj1.put("del_id", productRowDTO.getId());

//            List<ProductRowDTO> items = getTableView().getItems();
//            System.out.println(items);

                if (callback != null) {
                    callback.call(obj1);
                }
            }


        });





        this.columnName = columnName;
        this.comboBox = new ComboBox<>();

        this.comboBox.setPromptText("Select");

        this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");

        this.comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                this.comboBox.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #00a0f5;");
            } else {
                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #f6f6f9;");
            }
        });

        if (comboList != null) {

            for (CommonDTO commonDTO : comboList) {
                this.comboBox.getItems().add(commonDTO.getText());
            }
        }

        if(!columnName.equals("unit")){
            this.comboBox.setFocusTraversable(false);
        }





        this.comboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit(comboBox.getValue());
            }
        });


//        comboBox.getEditor().addEventFilter(KeyEvent.KEY_RELEASED, e -> {
//            if (e.getCode() == KeyCode.SPACE && !e.isControlDown()) {
//                comboBox.getEditor().setText("");
//                comboBox.show();
//            }
//
//        });

        comboBox.setOnKeyPressed(op->{
            if (op.getCode() == KeyCode.SPACE) {
                AutoCompleteBox2 autoCompleteBox1 = new AutoCompleteBox2(this.comboBox);
                comboBox.getEditor().setText("");
                CommonTraversa.getNextFocusableNode(this.comboBox.getScene());
                op.consume();
            }
        });



        comboBox.setOnHidden(e -> {
            if (comboBox.getSelectionModel().getSelectedItem() != null &&
                    !comboBox.getSelectionModel().getSelectedItem().equals(getItem())) {
                commitEdit(comboBox.getValue());
            }
        });

    }

    private void addDescendants(Node node, List<Node> nodes) {
        if (node != null) {
            if (node instanceof Parent) {
                ((Parent) node).getChildrenUnmodifiable().forEach(child -> addDescendants(child, nodes));
            }
            nodes.add(node);
        }
    }


    private Node getPreviousFocusableNode() {
        List<Node> nodes = new ArrayList<>();
        addDescendants(this.getScene().getRoot(), nodes);
        Node previousNode = null;
        for (Node node : nodes) {
            if (node.equals(this)) {
                break; // Stop searching once the current node is found
            }
            if (node.isFocusTraversable() && node.isVisible() && node.isManaged()) {
                previousNode = node;
            }
        }
        return previousNode;
    }



    @Override
    public void startEdit() {
        super.startEdit();
        setText(null);
        //setGraphic(new HBox(add, delete, comboBox));
        setGraphic(new HBox(comboBox));
        comboBox.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText((String) getItem());
        setGraphic(null);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {


            String itemToSet = item;
            if (comboBox.getItems().contains(itemToSet)) {
                comboBox.setValue(itemToSet);
            }
//            if (columnName.equals("unit"))
//                System.out.println(item);

            //comboBox.setValue(item);
            HBox hbox = new HBox();
           // hbox.getChildren().addAll(add, delete, comboBox);
            hbox.getChildren().addAll(comboBox);
            hbox.setAlignment(Pos.CENTER);
            hbox.setSpacing(5);

            setGraphic(hbox);
        }
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        if (columnName.equals("level_A")) {
            ((ProductRowDTO) getTableRow().getItem()).setSelectedLevelA(newValue);
        } else if (columnName.equals("level_B")) {
            ((ProductRowDTO) getTableRow().getItem()).setSelectedLevelB(newValue);
        } else if (columnName.equals("level_C")) {
            ((ProductRowDTO) getTableRow().getItem()).setSelectedLevelC(newValue);
        } else if (columnName.equals("unit")) {
            ((ProductRowDTO) getTableRow().getItem()).setSelectedUnit(newValue);
        }
    }

}

class TextFieldTableCell extends TableCell<ProductRowDTO, String> {
    private TextField textField;
    private String columnName;
    private SimpleStringProperty is_rate = new SimpleStringProperty("false");


    public void rate3(){
        int currentIndex = getTableRow().getIndex();
        if(currentIndex == getTableView().getItems().size()-1) {

            getTableView().getItems().add(new ProductRowDTO(String.valueOf(currentIndex + 1), "", "", "", "", "", "", "", "", "", "", "", "", "false", "false"));
            textField.requestFocus();
            PauseTransition pause = new PauseTransition(Duration.seconds(0.0001));
            pause.setOnFinished(eve -> {
                Platform.runLater(() -> {
                    TableColumn<ProductRowDTO,?> colName =  getTableView().getColumns().get(3);
                    getTableView().edit(getIndex()+1, colName);
                });
            });
            pause.play();

        }else {
            TableColumn<ProductRowDTO,?> colName =  getTableView().getColumns().get(3);
            getTableView().edit(getIndex()+1, colName);
        }
    }

    public TextFieldTableCell(String columnName) {
        this.columnName = columnName;
        this.textField = new TextField();

        // Commit when focus is lost
        int[] index = new int[1];
        this.textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit(textField.getText());
            }
            else {
                index[0] = getIndex();
            }
        });

        if (!columnName.equals("conv")) {
            DecimalFormat decimalFormat = new DecimalFormat("#.00");

            TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
                String newText = change.getControlNewText();
                if (newText.isEmpty()) {
                    return change;
                }

                try {
                    Number parsedNumber = decimalFormat.parse(newText);
                    String formattedText = decimalFormat.format(parsedNumber);
                    change.setText(formattedText);
                    change.setRange(0, change.getControlText().length());
                    return change;
                } catch (ParseException e) {
                    return null;
                }
            });

            textField.setTextFormatter(textFormatter);
        }



//        this.textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
//            if (newVal) {
//                if (columnName.equals("temp")) {
//                    PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
//                    pause.setOnFinished(event -> {
//                        Platform.runLater(() -> {
//                            commitEdit(textField.getText());
//                            Node nextNode = CommonTraversa.getNextFocusableNode(textField.getScene());
//                            if (nextNode != null) {
//                                nextNode.requestFocus();
//                            }
//                        });
//                    });
//                    pause.play();
//                }
//
//            }
//        });


        // Commit when Tab key is pressed
        this.textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB && event.isShiftDown()){
                TableTraversal.previous(getIndex(), getTableView().getColumns().indexOf(getTableColumn()), 1, getTableView());
            }
            else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB && !event.isShiftDown() ) {
                commitEdit(textField.getText());
                 if (columnName.equals("rate_3")) {

                     if (textField.getText().isEmpty()) {
                         rate3();
                     }
                     else {

                         double mrp = Double.parseDouble(getTableRow().getItem().getMrp());
                         double pur_rate = Double.parseDouble(getTableRow().getItem().getPurRate());
                         if (mrp > Double.parseDouble(textField.getText()) && pur_rate <=Double.parseDouble(textField.getText())) {
                             rate3();
                         } else {
                             AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Rate-3 should be in between Pur rate and MRP", input -> {
                                 TableColumn<ProductRowDTO, ?> colName = getTableView().getColumns().get(12);
                                 getTableView().edit(getIndex(), colName);
                             });
                         }
                     }
                     event.consume();
                }
                if (columnName.equals("conv")) {
                    TableColumn<ProductRowDTO,?> colName =  getTableView().getColumns().get(6);
                    getTableView().edit(getIndex(), colName);
                    event.consume();
                } else if (columnName.equals("mrp")) {
                    TableColumn<ProductRowDTO,?> colName =  getTableView().getColumns().get(9);
                    getTableView().edit(getIndex(), colName);
                    event.consume();
                } else if (columnName.equals("pur_rate")) {
                    if (textField.getText().isEmpty()) {
                        TableColumn<ProductRowDTO, ?> colName = getTableView().getColumns().get(10);
                        getTableView().edit(getIndex(), colName);
                    }
                    else {

                        double mrp = Double.parseDouble(getTableRow().getItem().getMrp());
                        if (!textField.getText().isEmpty() && mrp >= Double.parseDouble(textField.getText())) {
                            TableColumn<ProductRowDTO, ?> colName = getTableView().getColumns().get(10);
                            getTableView().edit(getIndex(), colName);
                        } else {
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Purchase Rate Should not be Greater Than MRP", input -> {
                                TableColumn<ProductRowDTO, ?> colName = getTableView().getColumns().get(9);
                                getTableView().edit(getIndex(), colName);
                            });
                        }
                    }
                    event.consume();

                } else if (columnName.equals("rate_1")) {
                    if (textField.getText().isEmpty()) {
                        TableColumn<ProductRowDTO, ?> colName = getTableView().getColumns().get(11);
                        getTableView().edit(getIndex(), colName);
                    }
                    else {
                        double mrp = Double.parseDouble(getTableRow().getItem().getMrp());
                        double pur_rate = Double.parseDouble(getTableRow().getItem().getPurRate());
                        if (mrp > Double.parseDouble(textField.getText()) && pur_rate <= Double.parseDouble(textField.getText())) {
                            TableColumn<ProductRowDTO, ?> colName = getTableView().getColumns().get(11);
                            getTableView().edit(getIndex(), colName);
                        } else {
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Rate-1 should be in between Pur rate and MRP", input -> {
                                TableColumn<ProductRowDTO, ?> colName = getTableView().getColumns().get(10);
                                getTableView().edit(getIndex(), colName);
                            });
                        }
                    }

                    event.consume();

                } else if (columnName.equals("rate_2")) {
                    if (textField.getText().isEmpty()) {
                        TableColumn<ProductRowDTO, ?> colName = getTableView().getColumns().get(12);
                        getTableView().edit(getIndex(), colName);
                    }
                    else {
                        double mrp = Double.parseDouble(getTableRow().getItem().getMrp());
                        double pur_rate = Double.parseDouble(getTableRow().getItem().getPurRate());
                        if (mrp > Double.parseDouble(textField.getText()) && pur_rate <=Double.parseDouble(textField.getText())) {
                            TableColumn<ProductRowDTO, ?> colName = getTableView().getColumns().get(12);
                            getTableView().edit(getIndex(), colName);
                        } else {
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Rate-2 should be in between Pur rate and MRP", input -> {
                                TableColumn<ProductRowDTO, ?> colName = getTableView().getColumns().get(11);
                                getTableView().edit(getIndex(), colName);
                            });
                        }
                    }
                    event.consume();

                }
                /*else if (columnName.equals("rate_3")) {
                    TableColumn<ProductRowDTO,?> colName =  getTableView().getColumns().get(13);
                    getTableView().edit(getIndex(), colName);
                    event.consume();
                }*/


//                else if (columnName.equals("rate_4")) {
//                    int currentIndex = getTableRow().getIndex();
//                    getTableView().getItems().add(new ProductRowDTO(String.valueOf(currentIndex + 1), "", "", "", "", "", "", "", "", "", "", "", "", "false", "false"));
//                    commitEdit(textField.getText());
//                    textField.requestFocus();
//                    PauseTransition pause = new PauseTransition(Duration.seconds(0.0001));
//                    pause.setOnFinished(eve -> {
//                        Platform.runLater(() -> {
//                            commitEdit(textField.getText());
//                            Node nextNode = CommonTraversa.getNextFocusableNode(textField.getScene());
//                            if (nextNode != null) {
//                                nextNode.requestFocus();
//                            }
//                        });
//                    });
//                    pause.play();
////                    TableTraversal.next(getIndex(), getTableView().getColumns().indexOf(getTableColumn()), 2, getTableView());
////                    TableColumn<ProductRowDTO,?> colName =  getTableView().getColumns().get(3);
////                    getTableView().edit(getIndex()+1, colName);
//                }
            }



//            else if (event.getCode() == KeyCode.UP) {
//                getTableView().getSelectionModel().selectAboveCell();
//            } else if (event.getCode() == KeyCode.DOWN) {
//                getTableView().getSelectionModel().selectBelowCell();
//            }

//            if (event.getCode() == KeyCode.UP) {
//                if(index[0]>0) {
//                    commitEdit(textField.getText());
//                    up(getIndex(), getTableView().getColumns().indexOf(getTableColumn()), getTableView());
//                }
//            }
//            if (event.getCode() == KeyCode.DOWN) {
//                commitEdit(textField.getText());
//                down(getIndex(),getTableView().getColumns().indexOf(getTableColumn()),getTableView());
//            }
        });

        if (columnName.equals("conv")) {
            textField.setPromptText("0");
        } else if (columnName.equals("mrp")) {
            textField.setPromptText("0.00");
           // textField.setDisable(true);
        } else if (columnName.equals("pur_rate")) {
            textField.setPromptText("0.00");
        //    textField.setDisable(true);
        } else if (columnName.equals("rate_1")) {
            textField.setPromptText("0.00");
//            textField.setDisable(true);
        } else if (columnName.equals("rate_2")) {
            textField.setPromptText("0.00");
//            textField.setDisable(true);
        } else if (columnName.equals("rate_3")) {
            textField.setPromptText("0.00");
//            textField.setDisable(true);
        }
        else if (columnName.equals("rate_4")) {
            textField.setPromptText("0.00");
//            textField.setDisable(true);
        }




        textField.setStyle("-fx-background-color: #f6f6f9; -fx-alignment: CENTER-RIGHT; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");

        textField.setPrefHeight(38);
        textField.setMaxHeight(38);
        textField.setMinHeight(38);

        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                textField.setStyle("-fx-background-color: #fff9c4; -fx-alignment: CENTER-RIGHT; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #00a0f5;");
            } else {
                textField.setStyle("-fx-background-color: #f6f6f9; -fx-alignment: CENTER-RIGHT; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #f6f6f9;");
            }
        });


    }

//    private TableRow<ProductRowDTO> createTableRow(TableView<ProductRowDTO> table) {
//        TableRow<ProductRowDTO> row =  new TableRow<ProductRowDTO>();
//        row.indexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
//            rowChanged(row, newValue.intValue());
//        });
//        return row;
//    }

//    private void rowChanged(TableRow<ProductRowDTO> row, int idx) {
//        items = row.getTableView().getItems();
//        if (idx >= 0 && idx <items.size()) {
//            Person p = items.get(idx);
//            if (p.getFirstName().contains("U")) {
//                row.setCursor(Cursor.CROSSHAIR);
//            }
//            else {
//                row.setCursor(Cursor.DEFAULT);
//            }
//        }
 //   }

    public  <T> void up(int row, int column, TableView<T> tableView) {
        TableColumn<T,?> colName = tableView.getColumns().get(column);
        tableView.edit(row-1, colName);
    }
    public  <T> void down(int row, int column, TableView<T> tableView) {
        TableColumn<T,?> colName = tableView.getColumns().get(column);
        tableView.edit(row+1, colName);
    }

    @Override
    public void startEdit() {
        super.startEdit();
        setText(null);
        setGraphic(new VBox(textField));
        textField.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText((String) getItem());
        VBox vbox = new VBox(textField);
        setGraphic(vbox);
    }


    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            if (getTableRow() == null || getTableRow().getItem() == null) {
                setGraphic(null);
            }else {
                VBox vbox = new VBox(textField);
                setGraphic(vbox);
            }

        } else {
            VBox vbox = new VBox(textField);
            textField.setText(item.toString());
            setGraphic(vbox);
        }
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        if (columnName.equals("conv")) {
            TableRow<ProductRowDTO> row = getTableRow();
            if (row != null) {
                ProductRowDTO item = row.getItem();
                if (item != null) {
                    item.setConv(newValue.isEmpty() ? "" : newValue);
                }
            }
        } else if (columnName.equals("mrp")) {
            ((ProductRowDTO) getTableRow().getItem()).setMrp(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("pur_rate")) {
            ((ProductRowDTO) getTableRow().getItem()).setPur_rate(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("rate_1")) {
            ((ProductRowDTO) getTableRow().getItem()).setRate_1(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("rate_2")) {
            ((ProductRowDTO) getTableRow().getItem()).setRate_2(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("rate_3")) {
            ((ProductRowDTO) getTableRow().getItem()).setRate_3(newValue.isEmpty() ? "" : newValue);
        }else if (columnName.equals("rate_4")) {
            ((ProductRowDTO) getTableRow().getItem()).setRate_4(newValue.isEmpty() ? "" : newValue);
        }
    }
}

class ButtonAndTextFieldTableCell extends TableCell<ProductRowDTO, String> {
    private TextField textField;
    private String columnName;

    private Button add;

    public ButtonAndTextFieldTableCell(String columnName, Parent pane) {
        this.columnName = columnName;
        this.textField = new TextField();

        this.add = createButtonWithImage();


        this.textField.setOnAction(event -> commitEdit(textField.getText()));

        // Commit when focus is lost
        this.textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit(textField.getText());
            }
        });

        // Commit when Tab key is pressed
        this.textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                commitEdit(textField.getText());
            }

        });


        textField.setPromptText("Opn Stock");


        textField.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");

        textField.setPrefHeight(38);
        textField.setMaxHeight(38);
        textField.setMinHeight(38);

        textField.setEditable(false);
        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                textField.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #00a0f5;");
            } else {
                textField.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #f6f6f9;");
            }
        });

        add.setOnAction(actionEvent -> {
            Stage stage1 = (Stage) pane.getScene().getWindow();
            String currentIndex = String.valueOf(getTableRow().getIndex());
            List<ProductRowDTO> tableList = getTableView().getItems();

            List<ProductRowBatchItem> previousBatchList;

            ProductRowDTO batchListRow = tableList.get(Integer.parseInt(currentIndex));
            {
                previousBatchList = batchListRow.getBatchList();
            }

            openStockPopUp(previousBatchList, currentIndex, stage1, "Add Category", input -> {
                for (int i = 0; i < input.size(); i++) {
                    if (input.get(i).getCurrentIndex().equals(String.valueOf(getTableRow().getIndex()))) {
                        // System.out.println("my Index: "+ input.get(i).getId()+"Table Index: "+getTableRow().getIndex()+" o "+input.get(i).getOpening_qty());
                        commitEdit(input.get(i).getOpening_qty());
                        ProductRowDTO productRow = getTableView().getItems().get(getTableRow().getIndex());
                        productRow.setBatchList(input);
                    }
                }
            });
        });

    }


    private Button createButtonWithImage() {

        ImageView imageView = new ImageView(new Image(GenivisApplication.class.getResourceAsStream("/com/opethic/genivis/ui/assets/add3.png")));
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        Button button = new Button();
        button.setMaxWidth(25);
        button.setMinWidth(25);
        button.setPrefWidth(25);
        button.setMaxHeight(38);
        button.setPrefHeight(38);
        button.setMinHeight(38);
        button.setGraphic(imageView);
        button.setStyle("-fx-background-color: #f6f6f9; -fx-border-width: 1; -fx-border-color: #8e39a5;");
        button.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                button.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #00a0f5;");
            } else {
                button.setStyle("-fx-background-color: #f6f6f9; -fx-border-width: 1; -fx-border-color: #8e39a5;");
            }
        });
        return button;
    }

    @Override
    public void startEdit() {
        super.startEdit();
        setText(null);
        setGraphic(new VBox(textField));
        textField.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText((String) getItem());
        setGraphic(null);
    }


    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            HBox vbox = new HBox(textField, add);
            vbox.setAlignment(Pos.CENTER);
            textField.setText(item.toString()); // this for display data that commited durring openStockPopUp
            setGraphic(vbox);
        }
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        if (columnName.equals("opn_stock")) {
            ((ProductRowDTO) getTableRow().getItem()).setOpn_stock(newValue);
        }
    }

    public TextField createCustomTextField(String promptText, double prefWidth) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setMinWidth(prefWidth);
        textField.setMaxWidth(prefWidth);
        textField.setPrefWidth(prefWidth);
        return textField;
    }

    public Label createCustomLabel(String labelText, double prefWidth) {
        Label label = new Label(labelText);
        label.setMinWidth(prefWidth);
        label.setMaxWidth(prefWidth);
        label.setPrefWidth(prefWidth);
        label.setFont(Font.font("System", FontWeight.BOLD, 14));
        return label;
    }

    public <T> void openStockPopUp(List<ProductRowBatchItem> previousBtachList, String index, Stage stage, String title, Consumer<List<ProductRowBatchItem>> callback) {
        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();

        primaryStage.initOwner(stage); // Set the owner stage
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");
        Platform.runLater(() -> borderPane.requestFocus());
        //BorderPan under Top Layout....................................................................................................................
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(998);
        hbox_top.setMaxWidth(998);
        hbox_top.setPrefWidth(998);
        hbox_top.setMaxHeight(50);
        hbox_top.setMinHeight(50);
        hbox_top.setPrefHeight(50);

        Label popup_title = new Label(title);
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        HBox.setMargin(imageView, new Insets(0, 10, 0, 0));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #D9F0FB; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 2 0;");

        //BorderPane Under Center Layout.....................................................................................................
        HBox hbox_center = new HBox();
        hbox_center.setMinWidth(998);
        hbox_center.setMaxWidth(998);
        hbox_center.setPrefWidth(998);
        hbox_center.setMaxHeight(150);
        hbox_center.setMinHeight(150);
        hbox_center.setMaxHeight(150);
        hbox_center.setAlignment(Pos.CENTER);
        hbox_center.setStyle("-fx-background-color: white;");

        //ProductCreateController.productLogger.info("all input field mapped to Object selectedItem[] , in openContentPopUp()");

        VBox vBox1 = new VBox();
        TextField tfBatch = createCustomTextField("Batch", 150);
        TextField tfMRP = createCustomTextField("MRP", 150);
        TextField tfSaleRate = createCustomTextField("Sale Rate", 150);
        vBox1.getChildren().addAll(tfBatch, tfMRP, tfSaleRate);
        vBox1.setSpacing(25);

        VBox vBox2 = new VBox();
        TextField tfOpeningQty = createCustomTextField("Opening Qty", 150);
        TextField tfPurchaseRate = createCustomTextField("Purchase Rate", 150);
        TextField tfMFGDate = createCustomTextField("MFG Date", 150);
        vBox2.getChildren().addAll(tfOpeningQty, tfPurchaseRate, tfMFGDate);
        vBox2.setSpacing(25);

        VBox vBox3 = new VBox();
        TextField tfFreeQty = createCustomTextField("Free Qty", 150);
        TextField tfCosting = createCustomTextField("Costing", 150);
        TextField tfExpiryDate = createCustomTextField("Expiry Date", 150);
        vBox3.getChildren().addAll(tfFreeQty, tfCosting, tfExpiryDate);
        vBox3.setSpacing(25);

        if (!Communicator.isBatch) {
            tfBatch.setEditable(false);
            tfMFGDate.setEditable(false);
            tfExpiryDate.setEditable(false);
        }

        VBox vBox4 = new VBox();
        Label lblBatch = createCustomLabel("Batch", 70);
        Label lblMRP = createCustomLabel("MRP", 70);
        Label lblSaleRate = createCustomLabel("Sale Rate", 70);
        vBox4.getChildren().addAll(lblBatch, lblMRP, lblSaleRate);
        vBox4.setSpacing(40);
        vBox4.setAlignment(Pos.CENTER);

        VBox vBox5 = new VBox();
        Label lblOpeningQty = createCustomLabel("Opening Qty.", 100);
        Label lblPurchaseRate = createCustomLabel("Purchase Rate", 100);
        Label lblMFGDate = createCustomLabel("MFG Date", 100);
        vBox5.getChildren().addAll(lblOpeningQty, lblPurchaseRate, lblMFGDate);
        vBox5.setSpacing(40);
        vBox5.setAlignment(Pos.CENTER);

        VBox vBox6 = new VBox();
        Label lblFreeQty = createCustomLabel("Free Qty.", 80);
        Label lblCosting = createCustomLabel("Costing", 80);
        Label lblExpiryDate = createCustomLabel("Expiry Date", 80);
        vBox6.getChildren().addAll(lblFreeQty, lblCosting, lblExpiryDate);
        vBox6.setSpacing(40);
        vBox6.setAlignment(Pos.CENTER);

        hbox_center.setSpacing(30);
        hbox_center.getChildren().addAll(vBox4, vBox1, vBox5, vBox2, vBox6, vBox3);


        //BorderPane Under Bottom Layout..............................................................................................................
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        HBox hbox_bottom = new HBox();
        hbox_bottom.setMinWidth(998);
        hbox_bottom.setMaxWidth(998);
        hbox_bottom.setPrefWidth(998);
        hbox_bottom.setMaxHeight(40);
        hbox_bottom.setMinHeight(4);
        hbox_bottom.setPrefHeight(40);
        hbox_bottom.setSpacing(10);
        hbox_bottom.setStyle("-fx-background-radius: 0 0 5 5; -fx-background-color: #FFFFFF;");
        hbox_bottom.setAlignment(Pos.TOP_RIGHT);

        Button addButton = new Button("Add");
        Button clButton = new Button("Clear");
        addButton.setId("submit-btn");
        clButton.setId("cancel-btn");
        hbox_bottom.setMargin(clButton, new Insets(0, 10, 0, 0));

        hbox_bottom.getChildren().addAll(addButton, clButton);

        HBox hbox_bottom2 = new HBox();
        hbox_bottom2.setMinWidth(998);
        hbox_bottom2.setMaxWidth(998);
        hbox_bottom2.setPrefWidth(998);
        hbox_bottom2.setMaxHeight(40);
        hbox_bottom2.setMinHeight(40);
        hbox_bottom2.setPrefHeight(40);
        hbox_bottom2.setSpacing(10);
        hbox_bottom2.setStyle("-fx-background-radius: 0 0 5 5; -fx-background-color: #FFFFFF;");
        hbox_bottom2.setAlignment(Pos.TOP_RIGHT);

        Button subButton = new Button("Submit");
        subButton.setId("submit-btn");
        hbox_bottom2.setMargin(subButton, new Insets(0, 10, 0, 0));


        hbox_bottom2.getChildren().addAll(subButton);

        TableView<ProductRowBatchItem> tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefHeight(200);
        tableView.setMaxHeight(200);
        tableView.setMinHeight(200);
        TableColumn<ProductRowBatchItem, String> cmBatch = new TableColumn<>("Batch");
        TableColumn<ProductRowBatchItem, String> cmOpeningQty = new TableColumn<>("Opening Qty");
        TableColumn<ProductRowBatchItem, String> cmFreeQty = new TableColumn<>("Free Qty");
        TableColumn<ProductRowBatchItem, String> cmMRP = new TableColumn<>("MRP");
        TableColumn<ProductRowBatchItem, String> cmPurchaseRate = new TableColumn<>("Purchase Rate");
        TableColumn<ProductRowBatchItem, String> cmCosting = new TableColumn<>("Costing");
        TableColumn<ProductRowBatchItem, String> cmSaleRate = new TableColumn<>("Sale Rate");
        TableColumn<ProductRowBatchItem, String> cmMFGDate = new TableColumn<>("MFG Date");
        TableColumn<ProductRowBatchItem, String> cmExpiryDate = new TableColumn<>("Expiry Date");

        tableView.getColumns().addAll(cmBatch, cmOpeningQty, cmFreeQty, cmMRP, cmPurchaseRate, cmCosting, cmSaleRate, cmMFGDate, cmExpiryDate);

        cmBatch.setCellValueFactory(cellData -> cellData.getValue().b_noProperty());
        cmOpeningQty.setCellValueFactory(cellData -> cellData.getValue().opening_qtyProperty());
        cmFreeQty.setCellValueFactory(cellData -> cellData.getValue().b_free_qtyProperty());
        cmMRP.setCellValueFactory(cellData -> cellData.getValue().b_mrpProperty());
        cmPurchaseRate.setCellValueFactory(cellData -> cellData.getValue().b_purchase_rateProperty());
        cmCosting.setCellValueFactory(cellData -> cellData.getValue().b_costingProperty());
        cmSaleRate.setCellValueFactory(cellData -> cellData.getValue().b_sale_rateProperty());
        cmMFGDate.setCellValueFactory(cellData -> cellData.getValue().b_manufacturing_dateProperty());
        cmExpiryDate.setCellValueFactory(cellData -> cellData.getValue().b_expiryProperty());


        if (previousBtachList != null) {
            for (ProductRowBatchItem r : previousBtachList) {
                tableView.getItems().addAll(new ProductRowBatchItem(r.getId(), r.getB_no(), r.getOpening_qty(), r.getB_free_qty(), r.getB_mrp(), r.getB_purchase_rate(), r.getB_costing(), r.getB_sale_rate(), r.getB_manufacturing_date(), r.getB_expiry(), String.valueOf(Communicator.isBatch), "false", r.getBatch_id()));
            }
        }

        vBox.getChildren().addAll(hbox_bottom, tableView, hbox_bottom2);

        borderPane.setTop(hbox_top);
        borderPane.setCenter(hbox_center);
        borderPane.setBottom(vBox);


        Node[] nodes = new Node[]{tfBatch, tfOpeningQty, tfFreeQty, tfMRP, tfPurchaseRate, tfCosting, tfSaleRate, tfMFGDate, tfExpiryDate, addButton, clButton};
        CommonValidationsUtils.setupFocusNavigation(nodes);

        Scene scene = new Scene(borderPane, 1000, 560);

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

        primaryStage.centerOnScreen();

        primaryStage.show();

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Button Clicked");
                tableView.getItems().addAll(new ProductRowBatchItem(Integer.parseInt(index), tfBatch.getText(), tfOpeningQty.getText(), tfFreeQty.getText(), tfMRP.getText(), tfPurchaseRate.getText(), tfCosting.getText(), tfSaleRate.getText(), tfMFGDate.getText(), tfExpiryDate.getText(), String.valueOf(Communicator.isBatch), "false", ""));
            }
        });

        clButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                tfBatch.clear();
                tfOpeningQty.clear();
                tfFreeQty.clear();
                tfMRP.clear();
                tfPurchaseRate.clear();
                tfCosting.clear();
                tfSaleRate.clear();
                tfMFGDate.clear();
                tfExpiryDate.clear();
            }
        });
        subButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                List<ProductRowBatchItem> items = tableView.getItems();
//                    System.out.println(items);
                callback.accept(items);
                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
        });

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
        });
    }
}

class ToggleTableCell extends TableCell<ProductRowDTO, String> {
    private SwitchButton switchButton;
    VBox vboxGvProduct;
    private String columnName;

    public ToggleTableCell(String columnName) {
        this.columnName = columnName;
        this.switchButton = new SwitchButton();


        this.switchButton.switchOnProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                commitEdit(String.valueOf(newValue));
            } else {
                commitEdit(String.valueOf(newValue));
            }
        });


        if (columnName.equals("is_rate")) {
            switchButton.setOnKeyPressed(event -> {
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<ProductRowDTO,?> colName =  getTableView().getColumns().get(5);
                    getTableView().edit(getIndex()-1, colName);

                }
                if(event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB && !event.isShiftDown()){
                    TableColumn<ProductRowDTO,?> colName =  getTableView().getColumns().get(7);
                    getTableView().edit(getIndex()-1, colName);
                    event.consume();
                }
            });

        }

        if (columnName.equals("is_negetive")) {

            switchButton.setOnKeyPressed(event -> {
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<ProductRowDTO,?> colName =  getTableView().getColumns().get(4);
                    getTableView().edit(getIndex()-1, colName);
                    event.consume();

                }
                if(event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB && !event.isShiftDown()){
                    TableColumn<ProductRowDTO,?> colName =  getTableView().getColumns().get(6);
                    getTableView().edit(getIndex()-1, colName);
                    event.consume();
                }
            });
        }


        this.vboxGvProduct = new VBox();
        vboxGvProduct.setPrefHeight(26.0);
        vboxGvProduct.setMaxHeight(26.0);
        vboxGvProduct.setMinHeight(26.0);
        vboxGvProduct.setPrefWidth(35.5);
        vboxGvProduct.setMinWidth(35.5);
        vboxGvProduct.setMaxWidth(35.5);
        vboxGvProduct.setStyle("-fx-background-color: #f8f4fc; -fx-background-radius: 4; -fx-border-radius: 4; -fx-border-color: #f8f4fc; -fx-border-width: 2;");

        switchButton.setPrefHeight(22.0);
        switchButton.setMaxHeight(22.0);
        switchButton.setMinHeight(22.0);
        switchButton.setMinWidth(32.0);
        switchButton.setMaxWidth(32.0);
        switchButton.setPrefWidth(32.0);
        vboxGvProduct.getChildren().add(switchButton);
        switchButton.setParentBox(vboxGvProduct);

    }

    @Override
    public void startEdit() {
        super.startEdit();
        setText(null);
        HBox vbox = new HBox(vboxGvProduct);
        vbox.setAlignment(Pos.CENTER);
        setGraphic(vbox);
        switchButton.requestButtonFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(null);
        HBox vbox = new HBox(vboxGvProduct);
        vbox.setAlignment(Pos.CENTER);
        setGraphic(vbox);
    }


    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {

            if (getTableRow() == null || getTableRow().getItem() == null) {
                setGraphic(null);
            }
            else {
                //anySwitchOn.set(Boolean.parseBoolean(item));
                HBox vbox = new HBox(vboxGvProduct);
                vbox.setAlignment(Pos.CENTER);
                setGraphic(vbox);
            }
        } else {
            switchButton.switchOnProperty().set(Boolean.parseBoolean(item));
            HBox vbox = new HBox(vboxGvProduct);
            vbox.setAlignment(Pos.CENTER);
            setGraphic(vbox);
         //asas   switchButton.requestButtonFocus();
        }
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        if (columnName.equals("is_negetive")) {
            ((ProductRowDTO) getTableRow().getItem()).setIs_negetive(newValue);
        }
        if (columnName.equals("is_rate")) {
            ((ProductRowDTO) getTableRow().getItem()).setIs_rate(newValue);
        }
    }

}


class UnitCell extends TableCell<ProductRowDTO, String> {
    private final SearchFieldForTable<CommonDTO> comboBox;

    private final String columnName;

    private ObservableList<CommonDTO> unitList;

    TableCellCallback<Boolean> callback;

    public UnitCell(String columnName,ObservableList<CommonDTO> comboList,TableCellCallback<Boolean> callback) {


        this.callback = callback;

        this.columnName = columnName;

        this.unitList = comboList;


        this.comboBox = new SearchFieldForTable<>();
      //  this.comboBox.setEditable(false);


        this.comboBox.setPromptText("Select");




        String imageUrl = getClass().getResource("/com/opethic/genivis/ui/assets/down_arrow.png").toExternalForm();


        this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9; -fx-background-image: url(" + imageUrl + ");  -fx-background-repeat: no-repeat; -fx-background-position: right 10px center;" );


        this.comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                Platform.runLater(() -> comboBox.positionCaret(comboBox.getText().length()));
                this.comboBox.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #00a0f5; -fx-background-image: url(" + imageUrl + ");  -fx-background-repeat: no-repeat; -fx-background-position: right 10px center;");
            } else {
                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #f6f6f9; -fx-background-image: url(" + imageUrl + ");  -fx-background-repeat: no-repeat; -fx-background-position: right 10px center;");
                commitEdit(comboBox.getText());
            }
        });

        this.comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
               // comboBox.setEditable(false);

            }
        });


        comboBox.setMaxHeight(40);
        comboBox.setMinHeight(40);
        comboBox.setPrefHeight(40);

        comboBox.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isEmpty()) {
                comboBox.requestFocus();
            }
        });

        comboBox.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.DOWN){
                comboBox.showPopup();
                event.consume();
            }
            if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                                TableColumn<ProductRowDTO,?> colName =  getTableView().getColumns().get(12);
                                getTableView().edit(getIndex()-1, colName);

            }
            if(event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB && !event.isShiftDown()){
                if(comboBox.getText().isEmpty() && getIndex() == getTableView().getItems().size()-1 && getIndex() != 0) {
                    if (callback != null) {
                        callback.call(true);
                    }
                }
                else if(!comboBox.getText().isEmpty()){
                    TableColumn<ProductRowDTO,?> colName =  getTableView().getColumns().get(4);
                    getTableView().edit(getIndex(), colName);
                }
                else {
                    comboBox.requestFocus();
                }
                event.consume();
            }
        });

//        comboBox.setTextFormatter(new TextFormatter<>(change -> {
//            if (change.isAdded() && change.getText().contains(" ")) {
//                String modifiedText = change.getText().replace(" ", "");
//                if(comboBox.getText().equals("")) {
//                    change.setText(modifiedText);
//                }
//            }
//            return change;
//        }));
    }


    @Override
    public void startEdit() {
        super.startEdit();
        setText(null);
        setGraphic(comboBox);
        comboBox.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText((String) getItem());
        setGraphic(comboBox);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {

            if (getTableRow() == null || getTableRow().getItem() == null) {
                setGraphic(null);
            }
            else {
                setGraphic(comboBox);
            }
        } else {
            this.comboBox.setItems(unitList);

            for(CommonDTO commonDTO:this.comboBox.getItems()){
                if(item.equals(commonDTO.getText())){
                    comboBox.setText(item);
                }
            }

            setGraphic(comboBox);
        }
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        if (columnName.equals("unit")) {
            ((ProductRowDTO) getTableRow().getItem()).setSelectedUnit(newValue);
        }
    }

}


class UnitAPI{
    public static List<CUnitListDTO> getUnitList() {
        List<CUnitListDTO> list1 = null;
        try {

            HttpResponse<String> response = APIClient.getRequest("get_units_by_outlet");
            CUnitResDTO unitResDTO = new Gson().fromJson(response.body(), CUnitResDTO.class);

            if (unitResDTO.getResponseStatus() == 200) {

                list1 = unitResDTO.getResponseObject();

            } else {
                System.out.println("responseObject is null");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list1;
    }
}

