package com.opethic.genivis.controller.users;

import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.dto.FranchiseDTO;
import com.opethic.genivis.key_manager.ShortCutKey;
import com.opethic.genivis.multitabs.DetachableTabPane;
import com.opethic.genivis.multitabs.TabDataDto;
import com.opethic.genivis.utils.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.opethic.genivis.utils.FxmFileConstants.*;
import static com.opethic.genivis.utils.Globals.tabPane;

public class CadminController implements Initializable {
    @FXML
    public BorderPane bpCadminDashboard;
    @FXML
    public MenuItem importproductMenu;
    @FXML
    public Menu financialMenu;
    @FXML
    public MenuItem profitlossMenu;
    @FXML
    public MenuItem balancesheetMenu;
    @FXML
    public MenuItem trialBalanceMenu;
    @FXML
    public MenuItem importstockMenu;
    public MenuItem spfolderpath;
    public MenuItem mibarcodeprint;
    @FXML
    private TabPane cadminMenuTabPen;
    @FXML
    private Tab cadminMenuDashboardTab;
    @FXML
    private MenuItem ledgerGroupMenu;
    @FXML
    private MenuItem ledgerMenu;
    @FXML
    private MenuItem hsnMenu;
    @FXML
    private MenuItem catalogMenu;
    @FXML
    private MenuItem cntPkgMstMenuCreate;
    @FXML
    private MenuItem contentProductSrch;
   /* @FXML
    private MenuItem createCommissionMst;*/
    @FXML
    private MenuItem miProduct;
    @FXML
    private MenuItem companyUser;
   /* @FXML
    private MenuItem franchiseMenu;*/
    @FXML
    private MenuItem franchiseDashboardMenu;
    @FXML
    private MenuItem patientMenu;
    @FXML
    private MenuItem areaMaster;
    private MenuItem branchMenu;

    @FXML
    private MenuItem dispatchmanagementMenu;

    @FXML
    private MenuItem paymentModeMenu;

    @FXML
    private MenuItem receiptMenu;


//    @FXML
//    private MenuItem courierServiceMenu;
    @FXML
    private MenuItem contentMasterMenu;
    @FXML
    private MenuItem doctorMasterMenu;
    @FXML
    private MenuItem salesmanMaster;
    @FXML
    private MenuItem userControl;
    @FXML
    private Menu menus;
    @FXML
    private Menu menusPurchase;
    @FXML
    private Menu menusSales;
    @FXML
    private Menu menusAccountEntry;
    @FXML
    private Menu menusReports;
    @FXML
    private Menu menusUtilities;
    @FXML
    private Menu menusControls;
    @FXML
    private Menu menusBillFormat;
    @FXML
    private Menu menusGSTR;

    @FXML
    private MenuItem purInvoiceMenu;
    @FXML
    private MenuItem orderMenu;
    @FXML
    private MenuItem challanMenu;

    @FXML
    MenuItem couterSaleMenu;

    @FXML
    private MenuItem purReturnMenu;
    @FXML
    private MenuItem salesinvoiceMenu;
    @FXML
    private MenuItem salesquotationMenu;
//    @FXML
//    private MenuItem salesorderMenu;
    @FXML
    private MenuItem onlySe;
    @FXML
    private MenuItem salesorderMenu;

    @FXML
    private MenuItem saleschallanMenu;
    @FXML
    private MenuItem salesreturnMenu;

    @FXML
    private MenuItem paymentMenu;
    @FXML
    private MenuItem contraMenu;
    @FXML
    private MenuItem journalMenu;
    @FXML
    private MenuItem debitnoteMenu;
    @FXML
    private MenuItem creditnoteMenu;
/*
    @FXML
    private MenuItem partnerpaymentMenu;
*/

    @FXML
    private Label headerDateandTime;
    @FXML
    private Label lblUserName;

    @FXML
    private MenuItem areaHeadMenu;
    @FXML
    private MenuItem salesOrderMenu;

    @FXML
    private MenuItem dayBookReportMenu;
    @FXML
    private MenuItem ledgerReportIMenu;
    @FXML
    private MenuItem transactionCreditNoteReportMenu;
    @FXML
    private MenuItem transactionDebitNoteReportMenu;

    @FXML
    private MenuItem stockReportMenu;
    @FXML
    private MenuItem stockValuationMenu;
    @FXML
    private MenuItem gstr3bMenu;
    @FXML
    private MenuItem gstr2Menu;
    @FXML
    private MenuItem gstr1Menu;

    @FXML
    private MenuItem LegerList;

    @FXML
    private MenuItem LegerListBank;

    @FXML
    private MenuItem Billbybill;

    @FXML
    private MenuItem BankAccount;
    @FXML
    private MenuItem transactionPayableReportMenu;
    @FXML
    private MenuItem transactionReceivableReportMenu;

    @FXML
    private MenuItem transactionReceiptReportMenu;
    @FXML
    private MenuItem transactionPaymentReportMenu;
    @FXML
    private MenuItem transactionJournalReportMenu;

    @FXML
    private MenuItem logout;
    @FXML
    private MenuItem changePassword;

    @FXML
    private MenuItem purchaseRegisterReportMenu;

    @FXML
    private Scene scene;

    @FXML
    private SplitPane mainPane;

    KeyCombination gotoKeyCombination;


    private Parent loader;
    private List<TabDataDto> tabDataDtoList = new ArrayList<>();
    private ObservableList dataLst = FXCollections.observableArrayList();

    /*private HashMap<String, Tab> activeTabs = new HashMap<>();
    private HashMap<String, Object> activeControllers = new HashMap<>();*/
    private ObservableList keys = FXCollections.observableArrayList();

    private GlobalController globalController;

    private static final Logger cadminLogger = LogManager.getLogger(CadminController.class);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        globalController = GlobalController.getInstance();
        globalController.setController(this);
        tabPane = new TabPane();
//        tabPane.setOnClosedPassSibling((sibling) -> tabPane = sibling);
        mainPane.getItems().add(tabPane);
        lstTabs();
        addNewTab(FRANCHISE_DASHBOARD_SLUG);
        loadUserName();
        ledgerMenu.setOnAction(e -> addNewTab("ledger-list"));

//        createCommissionMst.setOnAction(e -> addNewTab("commission-master"));
        contentProductSrch.setOnAction(e -> addNewTab("content-product-search"));
        cntPkgMstMenuCreate.setOnAction(e -> addNewTab("content-package-master"));
        contentMasterMenu.setOnAction(e -> addNewTab("content-master"));
//        partnerpaymentMenu.setOnAction(e -> addNewTab(FxmFileConstants.PARTNER_PAYMENT_SLUG));


        importproductMenu.setOnAction(e -> addNewTab(FxmFileConstants.IMPORT_PRODUCT_SLUG));
        spfolderpath.setOnAction(e -> addNewTab(FxmFileConstants.SET_BARCODE_HOME_SLUG));
        mibarcodeprint.setOnAction(e -> addNewTab(FxmFileConstants.BARCODE_PRINT_SLUG));
        importstockMenu.setOnAction(e -> addNewTab(FxmFileConstants.IMPORT_STOCK_SLUG));
        salesmanMaster.setOnAction(e ->
                addNewTab("salesman-master")
        );

        areaMaster.setOnAction(e ->
                addNewTab("area-master")
        );


//        receiptMenu.setOnAction(e ->
//                addNewTab("receipt-Menu")
//        );


        userControl.setOnAction(e ->
                addNewTab("user-control")
        );


//        dispatchmanagementMenu.setOnAction(e ->
//                addNewTab("dispatch-Management")
//        );
        dispatchmanagementMenu.setOnAction(event -> addNewTab(DISPATCH_MANAGEMENT_SLUG));



// Load the Inter font
        Font.loadFont(getClass().getResourceAsStream("/path/to/Inter-Regular.ttf"), 14);

        // Dynamic Change the Date and Time Runtime
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> updateDateTimeLabel())
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // Update label initially
        updateDateTimeLabel();

//        gotoKeyCombination = new KeyCodeCombination(KeyCode.F, KeyCombination.ALT_DOWN);
        /*** END ***/
/*        KeyCodeCombination gotoKeyCombination = new KeyCodeCombination(KeyCode.F, KeyCombination.ALT_DOWN);
        franchiseMenu.setAccelerator(gotoKeyCombination);*/

        miProduct.setAccelerator(ShortCutKey.product_create_ALT_I);


        KeyCombination gotoKeyCombination2 = new KeyCodeCombination(KeyCode.O, KeyCombination.ALT_DOWN);
        orderMenu.setAccelerator(gotoKeyCombination2);
        KeyCombination gotoKeyCombination3 = new KeyCodeCombination(KeyCode.H, KeyCombination.ALT_DOWN);
        challanMenu.setAccelerator(gotoKeyCombination3);
        KeyCombination gotoKeyCombination4 = new KeyCodeCombination(KeyCode.U, KeyCombination.ALT_DOWN);
        purReturnMenu.setAccelerator(gotoKeyCombination4);
        KeyCombination purInvShortcutKey = new KeyCodeCombination(KeyCode.P, KeyCombination.ALT_DOWN);
        purInvoiceMenu.setAccelerator(purInvShortcutKey);

        //Sales Shortcut Menus
        KeyCombination gotoKeyCombination5 = new KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN);///not working
        salesinvoiceMenu.setAccelerator(gotoKeyCombination5);
        KeyCombination gotoKeyCombination6 = new KeyCodeCombination(KeyCode.Q, KeyCombination.ALT_DOWN);
        salesquotationMenu.setAccelerator(gotoKeyCombination6);
        KeyCombination gotoKeyCombination7 = new KeyCodeCombination(KeyCode.D, KeyCombination.ALT_DOWN);
        salesOrderMenu.setAccelerator(gotoKeyCombination7);
        KeyCombination gotoKeyCombination8 = new KeyCodeCombination(KeyCode.A, KeyCombination.ALT_DOWN);///not working
        saleschallanMenu.setAccelerator(gotoKeyCombination8);
        KeyCombination gotoKeyCombination9 = new KeyCodeCombination(KeyCode.E, KeyCombination.ALT_DOWN);
        salesreturnMenu.setAccelerator(gotoKeyCombination9);
        KeyCombination gotoKeyCombination10 = new KeyCodeCombination(KeyCode.N, KeyCombination.ALT_DOWN);
        couterSaleMenu.setAccelerator(gotoKeyCombination10);
        //Sales Shortcut Menus
//        KeyCombination gotoKeyCombination11 = new KeyCodeCombination(KeyCode.I, KeyCombination.ALT_DOWN);
//        receiptMenu.setAccelerator(gotoKeyCombination11);
//        KeyCombination gotoKeyCombination12 = new KeyCodeCombination(KeyCode.Q, KeyCombination.ALT_DOWN);
//        paymentMenu.setAccelerator(gotoKeyCombination12);
//        KeyCombination gotoKeyCombination13 = new KeyCodeCombination(KeyCode.O, KeyCombination.ALT_DOWN);
//        contraMenu.setAccelerator(gotoKeyCombination13);
//        KeyCombination gotoKeyCombination14 = new KeyCodeCombination(KeyCode.C, KeyCombination.ALT_DOWN);
//        journalMenu.setAccelerator(gotoKeyCombination14);
//        KeyCombination gotoKeyCombination15 = new KeyCodeCombination(KeyCode.D, KeyCombination.ALT_DOWN);
//        debitnoteMenu.setAccelerator(gotoKeyCombination15);
//        KeyCombination gotoKeyCombination16 = new KeyCodeCombination(KeyCode.C, KeyCombination.ALT_DOWN);
//        creditnoteMenu.setAccelerator(gotoKeyCombination16);
//        KeyCombination gotoKeyCombination17 = new KeyCodeCombination(KeyCode.P, KeyCombination.ALT_DOWN);
//        partnerpaymentMenu.setAccelerator(gotoKeyCombination17);


//        Tab salesInvoiceTab = new Tab("Sales Invoice");

//        salesinvoiceMenu.setOnAction(e -> {
//            try {
//                FXMLLoader loader = new FXMLLoader(GenivisApplication.class.getResource("ui/sales/salesInvoice.fxml"));
//                Parent hsnContent = loader.load();
//                salesInvoiceTab.setContent(hsnContent);
//                cadminMenuTabPen.getTabs().add(salesInvoiceTab);
//                defaultSelection(cadminMenuTabPen, salesInvoiceTab);
//            } catch (IOException v) {
//                v.printStackTrace();
//            }
//
//        });
        changePassword.setOnAction(event -> addNewTab(CHANGE_PASSWORD_SLUG));
        hsnMenu.setOnAction(event -> addNewTab(HSN_MASTER_SLUG));
        contentMasterMenu.setOnAction(event -> addNewTab(CONTENT_MASTER_SLUG));

        ledgerGroupMenu.setOnAction(event -> addNewTab(LEDGER_GROUP_MASTER_SLUG));
        catalogMenu.setOnAction(event -> addNewTab(CATALOG_MASTER_SLUG));

        patientMenu.setOnAction(event -> addNewTab(PATIENT_MASTER_SLUG));

        paymentModeMenu.setOnAction(event -> addNewTab(PAYMENT_MODE_MASTER_SLUG));


       /* franchiseMenu.setOnAction(event -> addNewTab(FRANCHISE_LIST_SLUG));
//        franchiseDashboardMenu.setOnAction(event -> addNewTab(FRANCHISE_DASHBOARD_SLUG));

        courierServiceMenu.setOnAction((e) -> {
            addNewTab(COURIER_SERVICE_CREATE_SLUG);
        });


        areaHeadMenu.setOnAction((e) -> {
            addNewTab(AREA_HEAD_LIST_SLUG);
        });*/

        doctorMasterMenu.setOnAction((e) -> {
            addNewTab(DOCTOR_MASTER_CREATE_SLUG);
        });
        companyUser.setOnAction(event -> addNewTab(COM_USER_LIST_SLUG));

        companyUser.setOnAction(actionEvent -> addNewTab(COM_USER_LIST_SLUG));


        miProduct.setOnAction(e -> addNewTab(PRODUCT_LIST_SLUG));


        purInvoiceMenu.setOnAction(event -> addNewTab(PURCHASE_INV_CREATE_SLUG));


        orderMenu.setOnAction(event -> addNewTab(PURCHASE_ORDER_CREATE_SLUG));

        challanMenu.setOnAction(event -> addNewTab(PURCHASE_CHALLAN_CREATE_SLUG));

        couterSaleMenu.setOnAction(event -> addNewTab(COUNTER_SALE_SLUG));
        saleschallanMenu.setOnAction(event -> addNewTab(SALES_CHALLAN_CREATE_SLUG));
        doctorMasterMenu.setOnAction(event -> addNewTab(DOCTOR_MASTER_CREATE_SLUG));
/*        courierServiceMenu.setOnAction(event -> addNewTab(COURIER_SERVICE_CREATE_SLUG));
        areaHeadMenu.setOnAction(event -> addNewTab(AREA_HEAD_LIST_SLUG));*/

        salesOrderMenu.setOnAction(event -> addNewTab(SALES_ORDER_CREATE_SLUG));
        salesinvoiceMenu.setOnAction(e -> addNewTab(SALES_INVOICE_CREATE_SLUG));
        salesreturnMenu.setOnAction(e -> addNewTab(SALES_INVOICE_RTN_CREATE_SLUG));

        saleschallanMenu.setOnAction((e) -> {
            addNewTab(SALES_CHALLAN_CREATE_SLUG);
        });


        paymentMenu.setOnAction(e ->
                addNewTab(TRANX_PAYMENT_CREATE_SLUG));

        receiptMenu.setOnAction(event -> addNewTab(RECEIPT_CREATE_SLUG));
        debitnoteMenu.setOnAction(event -> addNewTab(DEBIT_NOTE_LIST_SLUG));


        creditnoteMenu.setOnAction(event -> addNewTab(CREDIT_NOTE_LIST_SLUG));

        journalMenu.setOnAction(event -> addNewTab(JOURNAL_CREATE_SLUG));
        contraMenu.setOnAction(event -> addNewTab(CONTRA_LIST_SLUG));


        salesquotationMenu.setOnAction(event -> addNewTab(SALES_QUOTATION_CREATE_SLUG));


        dayBookReportMenu.setOnAction(event -> addNewTab(ACCOUNTS_DAY_BOOK_LIST_SLUG));

        ledgerReportIMenu.setOnAction(event -> addNewTab(ACCOUNTS_LEDGER_REPORT1_LIST_SLUG));

        transactionCreditNoteReportMenu.setOnAction(event -> addNewTab(TRANSACTION_CREDIT_NOTE1_LIST_SLUG));

        stockReportMenu.setOnAction(event -> addNewTab(STOCKS_STOCK_REPORT1_LIST_SLUG));

        profitlossMenu.setOnAction(event -> addNewTab(PROFITLOSSREPORT_SLUG));

        trialBalanceMenu.setOnAction(event -> addNewTab(TRIAL_BALANCE_SLUG));

        transactionDebitNoteReportMenu.setOnAction(event -> addNewTab(TRANSACTION_DEBIT_NOTE1_LIST_SLUG));
        transactionDebitNoteReportMenu.setOnAction(event -> addNewTab(TRANSACTION_DEBIT_NOTE1_LIST_SLUG));

        stockValuationMenu.setOnAction(event -> addNewTab(STOCKS_STOCK_VALUATION1_LIST_SLUG));

        gstr3bMenu.setOnAction(event -> addNewTab(GSTR3B_DASHBOARD_LIST_SLUG));

        purchaseRegisterReportMenu.setOnAction(event -> addNewTab(PURCHASE_REGISTER_SLUG));

        transactionReceivableReportMenu.setOnAction(event -> addNewTab(RECEIVABLE_LIST_REPORT_SLUG));

        transactionPayableReportMenu.setOnAction(event -> addNewTab(PAYABLE_LIST_REPORT_SLUG));
        transactionReceiptReportMenu.setOnAction(event -> addNewTab(RECEIPT_SCREEN1_LIST_SLUG));
        transactionPaymentReportMenu.setOnAction(event -> addNewTab(PAYMENT_REPORT_SCREEN1_SLUG));
        transactionJournalReportMenu.setOnAction(event -> addNewTab(JOURNAL_REPORT_SCREEN1_SLUG));

        gstr1Menu.setOnAction(event -> addNewTab(GSTR1_DASHBOARD_LIST_SLUG));

        gstr2Menu.setOnAction(event -> addNewTab(GSTR2_DASHBOARD_LIST_SLUG));

        /*** Purchase Reuturn Tab ****/

        purReturnMenu.setOnAction(e -> addNewTab("purchase-return-create"));

        tabPane.setTabDragPolicy(TabPane.TabDragPolicy.REORDER);
    }

    private void loadUserName() {
        lblUserName.setText(GlobalTranx.getUserName() + "");
    }

    public void defaultSelection(TabPane sadminMenuTabPen, Tab tab) {
        SingleSelectionModel<Tab> selectionModel = sadminMenuTabPen.getSelectionModel();
        if (!sadminMenuTabPen.getTabs().contains(tab)) {
            sadminMenuTabPen.getTabs().add(tab);
            selectionModel.select(tab);
        } else {
            selectionModel.select(tab);
        }

    }

    //this function is called to set the local Date and Time
    private void updateDateTimeLabel() {
        // Get current date and time
        LocalDateTime now = LocalDateTime.now();

        // Format the date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy, hh:mm:ss a");
        String formattedDateTime = now.format(formatter);

        // Update label text
        headerDateandTime.setText(formattedDateTime);

    }


    private void lstTabs() {
        //! Add page in list
        tabDataDtoList.add(new TabDataDto(CHANGE_PASSWORD_TITLE, CHANGE_PASSWORD_FXML, CHANGE_PASSWORD_SLUG, Arrays.asList(CHANGE_PASSWORD_SLUG), CHANGE_PASSWORD_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Ledger List", "ui/master/ledger/ledgerlist.fxml", "ledger-list", Arrays.asList("ledger-list", "ledger-create", "ledger-edit", "ledger-details"), " com.opethic.genivis.controller.master.ledger.LedgerListController"));
        tabDataDtoList.add(new TabDataDto("Ledger Create", "ui/master/ledger/create/ledgercreate.fxml", "ledger-create", Arrays.asList("ledger-list", "ledger-create", "ledger-edit"), "com.opethic.genivis.controller.master.ledger.create.LedgerCreateController"));
        tabDataDtoList.add(new TabDataDto("Ledger Edit", "ui/master/ledger/edit/ledgeredit.fxml", "ledger-edit", Arrays.asList("ledger-list", "ledger-create", "ledger-edit"), "com.opethic.genivis.controller.master.ledger.edit.LedgerEditController"));
        tabDataDtoList.add(new TabDataDto("Product List", "ui/master/product_list.fxml", "product-list", Arrays.asList("product-list", "product-create", "product-edit"), "com.opethic.genivis.controller.master.ProductListController"));
        tabDataDtoList.add(new TabDataDto("Product Create", PRODUCT_CREATE_FXML, PRODUCT_CREATE_SLUG, Arrays.asList(PRODUCT_LIST_SLUG, PRODUCT_CREATE_SLUG, PRODUCT_EDIT_SLUG), PRODUCT_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Product Edit", PRODUCT_EDIT_FXML, PRODUCT_EDIT_SLUG, Arrays.asList(PRODUCT_LIST_SLUG, PRODUCT_CREATE_SLUG, PRODUCT_EDIT_SLUG), PRODUCT_EDIT_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(OPENING_STOCK_TITLE, OPENING_STOCK_FXML, OPENING_STOCK_SLUG, Arrays.asList(PRODUCT_LIST_SLUG, PRODUCT_CREATE_SLUG, PRODUCT_EDIT_SLUG,OPENING_STOCK_SLUG), OPENING_STOCK_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Purchase Return Create", PURCHASE_RETURN_CREATE_FXML, PURCHASE_RETURN_CREATE_SLUG, Arrays.asList(PURCHASE_RETURN_LIST_SLUG, PURCHASE_RETURN_CREATE_SLUG, PURCHASE_RETURN_EDIT_SLUG), PURCHASE_RETURN_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Purchase Return List", PURCHASE_RETURN_LIST_FXML, PURCHASE_RETURN_LIST_SLUG, Arrays.asList(PURCHASE_RETURN_LIST_SLUG, PURCHASE_RETURN_CREATE_SLUG, PURCHASE_RETURN_EDIT_SLUG), PURCHASE_RETURN_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Purchase Return Edit", PURCHASE_RETURN_EDIT_FXML, PURCHASE_RETURN_EDIT_SLUG, Arrays.asList(PURCHASE_RETURN_LIST_SLUG, PURCHASE_RETURN_CREATE_SLUG, PURCHASE_RETURN_EDIT_SLUG), PURCHASE_RETURN_EDIT_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Purchase Challan List", PURCHASE_CHALLAN_LIST_FXML, PURCHASE_CHALLAN_LIST_SLUG, Arrays.asList(PURCHASE_CHALLAN_LIST_SLUG, PURCHASE_CHALLAN_CREATE_SLUG, PURCHASE_CHALLAN_EDIT_SLUG), PURCHASE_CHALLAN_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Purchase Challan Create", PURCHASE_CHALLAN_CREATE_FXML, PURCHASE_CHALLAN_CREATE_SLUG, Arrays.asList(PURCHASE_CHALLAN_LIST_SLUG, PURCHASE_CHALLAN_CREATE_SLUG, PURCHASE_CHALLAN_EDIT_SLUG), PURCHASE_CHALLAN_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Purchase Challan To Invoice", PURCHASE_CHALL_TO_INV_FXML, PURCHASE_CHALL_TO_INV_CREATE_SLUG, Arrays.asList(PURCHASE_CHALL_TO_INV_CREATE_SLUG, PURCHASE_CHALLAN_CREATE_SLUG, PURCHASE_CHALLAN_LIST_SLUG, PURCHASE_CHALLAN_EDIT_SLUG), PURCHAE_CHALL_TO_INV_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Purchase Challan Edit", PURCHASE_CHALLAN_CREATE_FXML, PURCHASE_CHALLAN_EDIT_SLUG, Arrays.asList(PURCHASE_CHALLAN_LIST_SLUG, PURCHASE_CHALLAN_CREATE_SLUG, PURCHASE_CHALLAN_EDIT_SLUG), PURCHASE_CHALLAN_CREATE_CONTROLLER));
//        tabDataDtoList.add(new TabDataDto("Purchase Order List", PURCHASE_ORDER_LIST_FXML, PURCHASE_ORDER_LIST_SLUG, Arrays.asList(PURCHASE_ORDER_LIST_SLUG, PURCHASE_ORDER_CREATE_SLUG, PURCHASE_ORDER_EDIT_SLUG,PURCHASE_ORDER_TO_INV_EDIT_SLUG), PURCHASE_ORDER_LIST_CONTROLLER));
//        tabDataDtoList.add(new TabDataDto("Purchase Order Create", PURCHASE_ORDER_CREATE_FXML, PURCHASE_ORDER_CREATE_SLUG, Arrays.asList(PURCHASE_ORDER_LIST_SLUG, PURCHASE_ORDER_CREATE_SLUG, PURCHASE_ORDER_EDIT_SLUG,PURCHASE_ORDER_TO_INV_EDIT_SLUG), PURCHASE_ORDER_CREATE_CONTROLLER));
//        tabDataDtoList.add(new TabDataDto("Purchase Order Edit", PURCHASE_ORDER_CREATE_FXML, PURCHASE_ORDER_EDIT_SLUG, Arrays.asList(PURCHASE_ORDER_LIST_SLUG, PURCHASE_ORDER_CREATE_SLUG, PURCHASE_ORDER_EDIT_SLUG,PURCHASE_ORDER_TO_INV_EDIT_SLUG), PURCHASE_ORDER_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Purchase Order List", PURCHASE_ORDER_LIST_FXML, PURCHASE_ORDER_LIST_SLUG, Arrays.asList(PURCHASE_ORDER_LIST_SLUG, PURCHASE_ORDER_CREATE_SLUG, PURCHASE_ORDER_EDIT_SLUG, PURCHASE_ORDER_TO_CHALLAN_SLUG,PURCHASE_ORDER_TO_INV_EDIT_SLUG), PURCHASE_ORDER_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Purchase Order Create", PURCHASE_ORDER_CREATE_FXML, PURCHASE_ORDER_CREATE_SLUG, Arrays.asList(PURCHASE_ORDER_LIST_SLUG, PURCHASE_ORDER_CREATE_SLUG, PURCHASE_ORDER_EDIT_SLUG, PURCHASE_ORDER_TO_CHALLAN_SLUG,PURCHASE_ORDER_TO_INV_EDIT_SLUG), PURCHASE_ORDER_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Purchase Order Edit", PURCHASE_ORDER_CREATE_FXML, PURCHASE_ORDER_EDIT_SLUG, Arrays.asList(PURCHASE_ORDER_LIST_SLUG, PURCHASE_ORDER_CREATE_SLUG, PURCHASE_ORDER_EDIT_SLUG, PURCHASE_ORDER_TO_CHALLAN_SLUG,PURCHASE_ORDER_TO_INV_EDIT_SLUG), PURCHASE_ORDER_CREATE_CONTROLLER));
        //for purchase order to invoice
        tabDataDtoList.add(new TabDataDto("Purchase Order to Invoice", PURCHASE_ORDER_TO_INV_EDIT_FXML, PURCHASE_ORDER_TO_INV_EDIT_SLUG, Arrays.asList(PURCHASE_ORDER_TO_INV_EDIT_SLUG, PURCHASE_ORDER_LIST_SLUG, PURCHASE_ORDER_CREATE_SLUG, PURCHASE_ORDER_EDIT_SLUG,PURCHASE_ORDER_TO_CHALLAN_SLUG), PURCHAE_ORDER_TO_INV_EDIT_CONTROLLER));

        tabDataDtoList.add(new TabDataDto("Purchase Order To Challan", PURCHASE_ORDER_TO_CHALLAN_CREATE_FXML, PURCHASE_ORDER_TO_CHALLAN_SLUG, Arrays.asList(PURCHASE_ORDER_TO_CHALLAN_SLUG, PURCHASE_ORDER_LIST_SLUG, PURCHASE_ORDER_CREATE_SLUG, PURCHASE_ORDER_EDIT_SLUG), PURCHASE_ORDER_TO_CHALLAN_CREATE_CONTROLLER));

        tabDataDtoList.add(new TabDataDto("Company User", COM_USER_LIST_FXML, COM_USER_LIST_SLUG, Arrays.asList(COM_USER_LIST_SLUG, COM_USER_CREATE_SLUG, COM_USER_EDIT_SLUG), COM_USER_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Company User Create", COMPANY_USER_CREATE_FXML, COM_USER_CREATE_SLUG, Arrays.asList(COM_USER_CREATE_SLUG, COM_USER_LIST_SLUG, COM_USER_EDIT_SLUG), COM_USER_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Company User Edit", COMPANY_USER_CREATE_FXML, COM_USER_EDIT_SLUG, Arrays.asList(COM_USER_EDIT_SLUG, COM_USER_LIST_SLUG, COM_USER_CREATE_SLUG), COM_USER_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(CONTENT_MASTER_TILE, CONTENT_MASTER_FXML, CONTENT_MASTER_SLUG, Arrays.asList(CONTENT_MASTER_SLUG), CONTENT_MASTER_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Sales Quotation List", SALES_QUOTATION_LIST_FXML, SALES_QUOTATION_LIST_SLUG, Arrays.asList(SALES_QUOTATION_CREATE_SLUG, SALES_QUOTATION_LIST_SLUG, SALES_QUOTATION_EDIT_SLUG, SALES_QUOTATION_TO_ORDER_SLUG, SALES_QUOTATION_TO_CHALLAN_SLUG), SALES_QUOTATION_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Sales Quotation Create", SALES_QUOTATION_CREATE_FXML, SALES_QUOTATION_CREATE_SLUG, Arrays.asList(SALES_QUOTATION_CREATE_SLUG, SALES_QUOTATION_LIST_SLUG, SALES_QUOTATION_EDIT_SLUG, SALES_QUOTATION_TO_ORDER_SLUG, SALES_QUOTATION_TO_CHALLAN_SLUG), SALES_QUOTATION_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Sales Quotation Edit", SALES_QUOTATION_EDIT_FXML, SALES_QUOTATION_EDIT_SLUG, Arrays.asList(SALES_QUOTATION_EDIT_SLUG, SALES_QUOTATION_CREATE_SLUG, SALES_QUOTATION_LIST_SLUG, SALES_QUOTATION_TO_ORDER_SLUG, SALES_QUOTATION_TO_CHALLAN_SLUG), SALES_QUOTATION_EDIT_CONTROLLER));
        //SalesQuotationToOrder
        tabDataDtoList.add(new TabDataDto("Sales Quotation To Order", SALES_QUOTATION_TO_ORDER_FXML, SALES_QUOTATION_TO_ORDER_SLUG, Arrays.asList(SALES_QUOTATION_TO_ORDER_SLUG, SALES_QUOTATION_EDIT_SLUG, SALES_QUOTATION_CREATE_SLUG, SALES_QUOTATION_LIST_SLUG, SALES_QUOTATION_TO_CHALLAN_SLUG), SALES_QUOTATION_TO_ORDER_CONTROLLER));
        //      salesQuotation to Challan
        tabDataDtoList.add(new TabDataDto("Sale Quotation to Challan", SALES_QUOTATION_TO_CHALLAN_FXML, SALES_QUOTATION_TO_CHALLAN_SLUG, Arrays.asList(SALES_QUOTATION_TO_CHALLAN_SLUG, SALES_QUOTATION_CREATE_SLUG, SALES_QUOTATION_LIST_SLUG, SALES_QUOTATION_EDIT_SLUG, SALES_QUOTATION_TO_ORDER_SLUG), SALES_ORDER_TO_CHALLAN_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("SalesMan Master", "ui/master/salesmanMaster.fxml", "salesman-master", Arrays.asList("salesman-master"), "com.opethic.genivis.controller.master.SalesmanMasterController"));
        tabDataDtoList.add(new TabDataDto("Area Master", "ui/master/areaMaster.fxml", "area-master", Arrays.asList("area-master"), "com.opethic.genivis.controller.master.AreaMasterController"));
        tabDataDtoList.add(new TabDataDto("Commission Master", "ui/master/commisionMaster.fxml", "commission-master", Arrays.asList("commission-master"), "com.opethic.genivis.controller.master.CommisionMaster"));
        tabDataDtoList.add(new TabDataDto("Content Product Search", "ui/master/contentproductsearch.fxml", "content-product-search", Arrays.asList("content-product-search"), "com.opethic.genivis.controller.master.ContentProductSearch"));
        tabDataDtoList.add(new TabDataDto("Content Package Master", "ui/master/contentpackagemaster.fxml", "content-package-master", Arrays.asList("content-package-master"), "com.opethic.genivis.controller.master.Contentpackagemaster"));
        tabDataDtoList.add(new TabDataDto("User Control", "ui/master/userControl.fxml", "user-control", Arrays.asList("user-control"), "com.opethic.genivis.controller.master.UserControlController"));
        tabDataDtoList.add(new TabDataDto("Content Master", "ui/master/contentMaster.fxml", "content-master", Arrays.asList("content-master"), "com.opethic.genivis.controller.master.ContentMasterController"));
        tabDataDtoList.add(new TabDataDto("Day Book", ACCOUNTS_DAY_BOOK_LIST_FXML, ACCOUNTS_DAY_BOOK_LIST_SLUG, Arrays.asList(ACCOUNTS_DAY_BOOK_LIST_SLUG), ACCOUNTS_DAY_BOOK_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Ledger Report 1", ACCOUNTS_LEDGER_REPORT1_LIST_FXML, ACCOUNTS_LEDGER_REPORT1_LIST_SLUG, Arrays.asList(ACCOUNTS_LEDGER_REPORT1_LIST_SLUG, ACCOUNTS_LEDGER_REPORT2_LIST_SLUG, ACCOUNTS_LEDGER_REPORT3_LIST_SLUG), ACCOUNTS_LEDGER_REPORT1_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Ledger Report 2", ACCOUNTS_LEDGER_REPORT2_LIST_FXML, ACCOUNTS_LEDGER_REPORT2_LIST_SLUG, Arrays.asList(ACCOUNTS_LEDGER_REPORT2_LIST_SLUG, ACCOUNTS_LEDGER_REPORT1_LIST_SLUG, ACCOUNTS_LEDGER_REPORT3_LIST_SLUG), ACCOUNTS_LEDGER_REPORT2_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Ledger Report 3", ACCOUNTS_LEDGER_REPORT3_LIST_FXML, ACCOUNTS_LEDGER_REPORT3_LIST_SLUG, Arrays.asList(ACCOUNTS_LEDGER_REPORT3_LIST_SLUG, ACCOUNTS_LEDGER_REPORT2_LIST_SLUG, ACCOUNTS_LEDGER_REPORT1_LIST_SLUG), ACCOUNTS_LEDGER_REPORT3_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Payable Report", PAYABLE_LIST_REPORT_FXML, PAYABLE_LIST_REPORT_SLUG, Arrays.asList(PAYABLE_LIST_REPORT_SLUG), PAYABLE_LIST_REPORT_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Payable Report", PAYABLE_LIST_REPORT_FXML, PAYABLE_LIST_REPORT_SLUG, Arrays.asList(PAYABLE_LIST_REPORT_SLUG), PAYABLE_LIST_REPORT_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Receivable Report", RECEIVABLE_LIST_REPORT_FXML, RECEIVABLE_LIST_REPORT_SLUG, Arrays.asList(RECEIVABLE_LIST_REPORT_SLUG), RECEIVABLE_LIST_REPORT_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Receipt Report", RECEIPT_SCREEN1_REPORT_FXML, RECEIPT_SCREEN1_LIST_SLUG, Arrays.asList(RECEIPT_SCREEN1_LIST_SLUG, RECEIPT_SCREEN2_LIST_SLUG), RECEIPT_SCREEN1_REPORT_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Receipt Report2", RECEIPT_SCREEN2_REPORT_FXML, RECEIPT_SCREEN2_LIST_SLUG, Arrays.asList(RECEIPT_SCREEN2_LIST_SLUG, RECEIPT_SCREEN1_LIST_SLUG), RECEIPT_SCREEN2_REPORT_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Payment Report1", PAYMENT_REPORT_SCREEN1_FXML, PAYMENT_REPORT_SCREEN1_SLUG, Arrays.asList(PAYMENT_REPORT_SCREEN1_SLUG, PAYMENT_REPORT_SCREEN2_SLUG), PAYMENT_REPORT_SCREEN1_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Payment Report2", PAYMENT_REPORT_SCREEN2_FXML, PAYMENT_REPORT_SCREEN2_SLUG, Arrays.asList(PAYMENT_REPORT_SCREEN2_SLUG, PAYMENT_REPORT_SCREEN1_SLUG), PAYMENT_REPORT_SCREEN2_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(JOURNAL_REPORT_SCREEN1_TITLE, JOURNAL_REPORT_SCREEN1_FXML, JOURNAL_REPORT_SCREEN1_SLUG, Arrays.asList(JOURNAL_REPORT_SCREEN1_SLUG, JOURNAL_REPORT_SCREEN2_SLUG), JOURNAL_REPORT_SCREEN1_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(JOURNAL_REPORT_SCREEN2_TITLE, JOURNAL_REPORT_SCREEN2_FXML, JOURNAL_REPORT_SCREEN2_SLUG, Arrays.asList(JOURNAL_REPORT_SCREEN2_SLUG, JOURNAL_REPORT_SCREEN1_SLUG), JOURNAL_REPORT_SCREEN2_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Stock Report 1", STOCKS_STOCK_REPORT1_LIST_FXML, STOCKS_STOCK_REPORT1_LIST_SLUG, Arrays.asList(STOCKS_STOCK_REPORT1_LIST_SLUG, STOCKS_STOCK_REPORT2_LIST_SLUG, STOCKS_STOCK_REPORT3_LIST_SLUG), STOCKS_STOCK_REPORT1_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Stock Report 2", STOCKS_STOCK_REPORT2_LIST_FXML, STOCKS_STOCK_REPORT2_LIST_SLUG, Arrays.asList(STOCKS_STOCK_REPORT2_LIST_SLUG, STOCKS_STOCK_REPORT1_LIST_SLUG, STOCKS_STOCK_REPORT3_LIST_SLUG), STOCKS_STOCK_REPORT2_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Stock Report 3", STOCKS_STOCK_REPORT3_LIST_FXML, STOCKS_STOCK_REPORT3_LIST_SLUG, Arrays.asList(STOCKS_STOCK_REPORT3_LIST_SLUG, STOCKS_STOCK_REPORT2_LIST_SLUG, STOCKS_STOCK_REPORT1_LIST_SLUG), STOCKS_STOCK_REPORT3_LIST_CONTROLLER));
        //Trial Balance Report
        tabDataDtoList.add(new TabDataDto("Trial Balance", TRIAL_BALANCE_FXML, TRIAL_BALANCE_SLUG, Arrays.asList(TRIAL_BALANCE_SLUG), TRIAL_BALANCE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Stock Valuation 1", STOCKS_STOCK_VALUATION1_LIST_FXML, STOCKS_STOCK_VALUATION1_LIST_SLUG, Arrays.asList(STOCKS_STOCK_VALUATION1_LIST_SLUG, STOCKS_STOCK_VALUATION2_LIST_SLUG), STOCKS_STOCK_VALUATION1_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Stock Valuation 2", STOCKS_STOCK_VALUATION2_LIST_FXML, STOCKS_STOCK_VALUATION2_LIST_SLUG, Arrays.asList(STOCKS_STOCK_VALUATION2_LIST_SLUG, STOCKS_STOCK_VALUATION1_LIST_SLUG), STOCKS_STOCK_VALUATION2_LIST_CONTROLLER));

        tabDataDtoList.add(new TabDataDto("Profit Loss", PROFITLOSSREPORT_FXML, PROFITLOSSREPORT_SLUG, Arrays.asList(PROFITLOSSREPORT_SLUG), STOCKS_STOCK_VALUATION2_LIST_CONTROLLER));

        tabDataDtoList.add(new TabDataDto("GSTR-3B", GSTR3B_DASHBOARD_LIST_FXML, GSTR3B_DASHBOARD_LIST_SLUG, Arrays.asList(GSTR3B_DASHBOARD_LIST_SLUG, GSTR3B_OUTWARD_TAXABLE_SUPPLIES_LIST_SLUG, GSTR3B_ALL_OTHER_ITC_LIST_SLUG), GSTR3B_DASHBOARD_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Outward Taxable Supplies", GSTR3B_OUTWARD_TAXABLE_SUPPLIES_LIST_FXML, GSTR3B_OUTWARD_TAXABLE_SUPPLIES_LIST_SLUG, Arrays.asList(GSTR3B_OUTWARD_TAXABLE_SUPPLIES_LIST_SLUG, GSTR3B_DASHBOARD_LIST_SLUG, GSTR3B_ALL_OTHER_ITC_LIST_SLUG), GSTR3B_OUTWARD_TAXABLE_SUPPLIES_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("All Other ITC", GSTR3B_ALL_OTHER_ITC_LIST_FXML, GSTR3B_ALL_OTHER_ITC_LIST_SLUG, Arrays.asList(GSTR3B_ALL_OTHER_ITC_LIST_SLUG, GSTR3B_OUTWARD_TAXABLE_SUPPLIES_LIST_SLUG, GSTR3B_DASHBOARD_LIST_SLUG), GSTR3B_ALL_OTHER_ITC_LIST_CONTROLLER));

        tabDataDtoList.add(new TabDataDto(GSTR1_CREDIT_DEBIT_UNREGISTERED_9B_LIST_TITLE, GSTR1_CREDIT_DEBIT_UNREGISTERED_9B_LIST_FXML, GSTR1_CREDIT_DEBIT_UNREGISTERED_9B_LIST_SLUG, Arrays.asList(GSTR1_DASHBOARD_LIST_SLUG, GSTR1_CREDIT_DEBIT_UNREGISTERED_9B_LIST_SLUG), GSTR1_CREDIT_DEBIT_UNREGISTERED_9B_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("GSTR-1", GSTR1_DASHBOARD_LIST_FXML, GSTR1_DASHBOARD_LIST_SLUG, Arrays.asList(GSTR1_DASHBOARD_LIST_SLUG), GSTR1_DASHBOARD_LIST_CONTROLLER));

        tabDataDtoList.add(new TabDataDto("GSTR-2", GSTR2_DASHBOARD_LIST_FXML, GSTR2_DASHBOARD_LIST_SLUG, Arrays.asList(GSTR2_DASHBOARD_LIST_SLUG, GSTR2_CR_DR_UNREGISTER_SLUG, GSTR2_AMMEND_CR_DR_REGISTER_SLUG), GSTR2_DASHBOARD_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("DebitNote_Unreg", GSTR2_CR_DR_UNREGISTER_FXML, GSTR2_CR_DR_UNREGISTER_SLUG, Arrays.asList(GSTR2_CR_DR_UNREGISTER_SLUG, GSTR2_DASHBOARD_LIST_SLUG, GSTR2_AMMEND_CR_DR_REGISTER_SLUG), GSTR2_CR_DR_UNREGISTER_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("DebitNote_Reg", GSTR2_AMMEND_CR_DR_REGISTER_FXML, GSTR2_AMMEND_CR_DR_REGISTER_SLUG, Arrays.asList(GSTR2_AMMEND_CR_DR_REGISTER_SLUG, GSTR2_CR_DR_UNREGISTER_SLUG, GSTR2_DASHBOARD_LIST_SLUG), GSTR2_AMMEND_CR_DR_REGISTER_CONTROLLER));

        //! Add page in list
        tabDataDtoList.add(new TabDataDto(FRANCHISE_LIST_TITLE, FRANCHISE_LIST_FXML, FRANCHISE_LIST_SLUG, Arrays.asList(FRANCHISE_LIST_SLUG, FRANCHISE_CREATE_SLUG, FRANCHISE_EDIT_SLUG,"ledger-create"), FRANCHISE_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(FRANCHISE_CREATE_TITLE, FRANCHISE_CREATE_FXML, FRANCHISE_CREATE_SLUG, Arrays.asList(FRANCHISE_LIST_SLUG, FRANCHISE_CREATE_SLUG, FRANCHISE_EDIT_SLUG,"ledger-create"), FRANCHISE_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(FRANCHISE_EDIT_TITLE, FRANCHISE_EDIT_FXML, FRANCHISE_EDIT_SLUG, Arrays.asList(FRANCHISE_LIST_SLUG, FRANCHISE_CREATE_SLUG, FRANCHISE_EDIT_SLUG,"ledger-create"), FRANCHISE_EDIT_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(SALES_ORDER_LIST_TITLE, SALES_ORDER_LIST_FXML, SALES_ORDER_LIST_SLUG, Arrays.asList(SALES_ORDER_LIST_SLUG, SALES_ORDER_CREATE_SLUG, SALES_ORDER_EDIT_SLUG, SALES_ORDER_TO_CHALLAN_SLUG,SALES_ORDER_TO_INVOICE_SLUG), SALES_ORDER_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(SALES_ORDER_CREATE_TITLE, SALES_ORDER_CREATE_FXML, SALES_ORDER_CREATE_SLUG, Arrays.asList(SALES_ORDER_LIST_SLUG, SALES_ORDER_CREATE_SLUG, SALES_ORDER_EDIT_SLUG, SALES_ORDER_TO_CHALLAN_SLUG,SALES_ORDER_TO_INVOICE_SLUG), SALES_ORDER_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(SALES_ORDER_EDIT_TITLE, SALES_ORDER_EDIT_FXML, SALES_ORDER_EDIT_SLUG, Arrays.asList(SALES_ORDER_LIST_SLUG, SALES_ORDER_CREATE_SLUG, SALES_ORDER_EDIT_SLUG, SALES_ORDER_TO_CHALLAN_SLUG,SALES_ORDER_TO_INVOICE_SLUG), SALES_ORDER_EDIT_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("sale order to inv",SALES_ORDER_TO_INVOICE_FXML,SALES_ORDER_TO_INVOICE_SLUG,Arrays.asList(SALES_ORDER_TO_INVOICE_SLUG,SALES_ORDER_LIST_SLUG,SALES_ORDER_CREATE_SLUG,
                SALES_ORDER_EDIT_SLUG),SALES_ORDER_TO_INVOICE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(JOURNAL_LIST_TITLE, JOURNAL_LIST_FXML, JOURNAL_LIST_SLUG, Arrays.asList(JOURNAL_LIST_SLUG, JOURNAL_CREATE_SLUG, JOURNAL_EDIT_SLUG), JOURNAL_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(JOURNAL_CREATE_TITLE, JOURNAL_CREATE_FXML, JOURNAL_CREATE_SLUG, Arrays.asList(JOURNAL_LIST_SLUG, JOURNAL_CREATE_SLUG, JOURNAL_EDIT_SLUG), JOURNAL_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(JOURNAL_EDIT_TITLE, JOURNAL_EDIT_FXML, JOURNAL_EDIT_SLUG, Arrays.asList(JOURNAL_LIST_SLUG, JOURNAL_CREATE_SLUG, JOURNAL_EDIT_SLUG), JOURNAL_EDIT_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(CONTRA_LIST_TITLE, CONTRA_LIST_FXML, CONTRA_LIST_SLUG, Arrays.asList(CONTRA_LIST_SLUG, CONTRA_CREATE_SLUG), CONTRA_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(CONTRA_CREATE_TITLE, CONTRA_CREATE_FXML, CONTRA_CREATE_SLUG, Arrays.asList(CONTRA_LIST_SLUG, CONTRA_CREATE_SLUG), CONTRA_CREATE_CONTROLLER));
//        tabDataDtoList.add(new TabDataDto(CONTRA_EDIT_TITLE, CONTRA_EDIT_FXML, CONTRA_EDIT_SLUG, Arrays.asList(CONTRA_LIST_SLUG, CONTRA_CREATE_SLUG, CONTRA_EDIT_SLUG), CONTRA_EDIT_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Sale Order to Challan", SALES_ORDER_TO_CHALLAN_FXML, SALES_ORDER_TO_CHALLAN_SLUG, Arrays.asList(SALES_ORDER_TO_CHALLAN_SLUG, SALES_ORDER_LIST_SLUG, SALES_ORDER_CREATE_SLUG, SALES_ORDER_EDIT_SLUG), SALES_ORDER_TO_CHALLAN_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Counter Sale", COUNTER_SALE_FXML, COUNTER_SALE_SLUG, Arrays.asList(COUNTER_SALE_SLUG), COUNTER_SALE_CONTROLLER));

        //!Sales Invoice
        tabDataDtoList.add(new TabDataDto(SALES_INVOICE_CREATE_TITLE, SALES_INVOICE_CREATE_FXML, SALES_INVOICE_CREATE_SLUG, Arrays.asList(SALES_INVOICE_CREATE_SLUG, SALES_INVOICE_LIST_SLUG, SALES_INVOICE_EDIT_SLUG), SALES_INVOICE_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(SALES_INVOICE_LIST_TITLE, SALES_INVOICE_LIST_FXML, SALES_INVOICE_LIST_SLUG, Arrays.asList(SALES_INVOICE_CREATE_SLUG, SALES_INVOICE_LIST_SLUG, SALES_INVOICE_EDIT_SLUG), SALES_INVOICE_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(SALES_INVOICE_EDIT_TITLE, SALES_INVOICE_CREATE_FXML, SALES_INVOICE_EDIT_SLUG, Arrays.asList(SALES_INVOICE_CREATE_SLUG, SALES_INVOICE_LIST_SLUG, SALES_INVOICE_EDIT_SLUG), SALES_INVOICE_CREATE_CONTROLLER));

        //!Sales Invoice Return
        tabDataDtoList.add(new TabDataDto(SALES_INVOICE_RTN_CREATE_TITLE, SALES_INVOICE_RTN_CREATE_FXML, SALES_INVOICE_RTN_CREATE_SLUG, Arrays.asList(SALES_INVOICE_RTN_CREATE_SLUG, SALES_INVOICE_RTN_LIST_SLUG, SALES_INVOICE_RTN_EDIT_SLUG), SALES_INVOICE_RTN_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(SALES_INVOICE_RTN_LIST_TITLE, SALES_INVOICE_RTN_LIST_FXML, SALES_INVOICE_RTN_LIST_SLUG, Arrays.asList(SALES_INVOICE_RTN_CREATE_SLUG, SALES_INVOICE_RTN_LIST_SLUG, SALES_INVOICE_RTN_EDIT_SLUG), SALES_INVOICE_RTN_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(SALES_INVOICE_RTN_EDIT_TITLE, SALES_INVOICE_RTN_EDIT_FXML, SALES_INVOICE_RTN_EDIT_SLUG, Arrays.asList(SALES_INVOICE_RTN_CREATE_SLUG, SALES_INVOICE_RTN_LIST_SLUG, SALES_INVOICE_RTN_EDIT_SLUG), SALES_INVOICE_RTN_EDIT_CONTROLLER));

        tabDataDtoList.add(new TabDataDto(SALES_CHALLAN_LIST_TITLE, SALES_CHALLAN_LIST_FXML, SALES_CHALLAN_LIST_SLUG, Arrays.asList(SALES_CHALLAN_LIST_SLUG, SALES_CHALLAN_CREATE_SLUG, SALES_CHALLAN_EDIT_SLUG), SALES_CHALLAN_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(SALES_CHALLAN_CREATE_TITLE, SALES_CHALLAN_CREATE_FXML, SALES_CHALLAN_CREATE_SLUG, Arrays.asList(SALES_CHALLAN_LIST_SLUG, SALES_CHALLAN_CREATE_SLUG, SALES_CHALLAN_EDIT_SLUG), SALES_CHALLAN_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(SALES_CHALLAN_EDIT_TITLE, SALES_CHALLAN_EDIT_FXML, SALES_CHALLAN_EDIT_SLUG, Arrays.asList(SALES_CHALLAN_LIST_SLUG, SALES_CHALLAN_CREATE_SLUG, SALES_CHALLAN_EDIT_SLUG), SALES_CHALLAN_EDIT_CONTROLLER));

        tabDataDtoList.add(new TabDataDto(COURIER_SERVICE_CREATE_TITLE, COURIER_SERVICE_CREATE_FXML, COURIER_SERVICE_CREATE_SLUG, Arrays.asList(COURIER_SERVICE_CREATE_SLUG), COURIER_SERVICE_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(DOCTOR_MASTER_CREATE_TITLE, DOCTOR_MASTER_CREATE_FXML, DOCTOR_MASTER_CREATE_SLUG, Arrays.asList(DOCTOR_MASTER_CREATE_SLUG), DOCTOR_MASTER_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(AREA_HEAD_LIST_TITLE, AREA_HEAD_LIST_FXML, AREA_HEAD_LIST_SLUG, Arrays.asList(AREA_HEAD_LIST_SLUG, AREA_HEAD_CREATE_SLUG, AREA_HEAD_EDIT_SLUG), AREA_HEAD_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(AREA_HEAD_CREATE_TITLE, AREA_HEAD_CREATE_FXML, AREA_HEAD_CREATE_SLUG, Arrays.asList(AREA_HEAD_LIST_SLUG, AREA_HEAD_CREATE_SLUG, AREA_HEAD_EDIT_SLUG), AREA_HEAD_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(AREA_HEAD_EDIT_TITLE, AREA_HEAD_EDIT_FXML, AREA_HEAD_EDIT_SLUG, Arrays.asList(AREA_HEAD_LIST_SLUG, AREA_HEAD_CREATE_SLUG, AREA_HEAD_EDIT_SLUG), AREA_HEAD_EDIT_CONTROLLER));

        tabDataDtoList.add(new TabDataDto("Patient Master", PATIENT_MASTER_FXML, PATIENT_MASTER_SLUG, Arrays.asList(PATIENT_MASTER_SLUG), PATIENT_MASTER_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Payment Mode", PAYMENT_MODE_MASTER_FXML, PAYMENT_MODE_MASTER_SLUG, Arrays.asList(PAYMENT_MODE_MASTER_SLUG), PAYMENT_MODE_MASTER_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(HSN_MASTER_TITLE, HSN_MASTER_FXML, HSN_MASTER_SLUG, Arrays.asList(HSN_MASTER_SLUG), HSN_MASTER_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(LEDGER_GROUP_MASTER_TITLE, LEDGER_GROUP_MASTER_FXML, LEDGER_GROUP_MASTER_SLUG, Arrays.asList(LEDGER_GROUP_MASTER_SLUG), LEDGER_GROUP_MASTER_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(CATALOG_MASTER_TITLE, CATALOG_MASTER_FXML, CATALOG_MASTER_SLUG, Arrays.asList(CATALOG_MASTER_SLUG), CATALOG_MASTER_CONTROLLER));



        // Credit Note Report
        tabDataDtoList.add(new TabDataDto(TRANSACTION_CREDIT_NOTE1_TITLE, TRANSACTION_CREDIT_NOTE1_LIST_FXML, TRANSACTION_CREDIT_NOTE1_LIST_SLUG, Arrays.asList(TRANSACTION_CREDIT_NOTE1_LIST_SLUG, TRANSACTION_CREDIT_NOTE2_LIST_SLUG), TRANSACTION_CREDIT_NOTE1_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(TRANSACTION_CREDIT_NOTE2_TITLE, TRANSACTION_CREDIT_NOTE2_LIST_FXML, TRANSACTION_CREDIT_NOTE2_LIST_SLUG, Arrays.asList(TRANSACTION_CREDIT_NOTE1_LIST_SLUG, TRANSACTION_CREDIT_NOTE2_LIST_SLUG), TRANSACTION_CREDIT_NOTE2_LIST_CONTROLLER));


        // Debit Note Report
        tabDataDtoList.add(new TabDataDto(TRANSACTION_DEBIT_NOTE1_TITLE, TRANSACTION_DEBIT_NOTE1_LIST_FXML, TRANSACTION_DEBIT_NOTE1_LIST_SLUG, Arrays.asList(TRANSACTION_DEBIT_NOTE1_LIST_SLUG, TRANSACTION_DEBIT_NOTE2_LIST_SLUG), TRANSACTION_DEBIT_NOTE1_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(TRANSACTION_DEBIT_NOTE2_TITLE, TRANSACTION_DEBIT_NOTE2_LIST_FXML, TRANSACTION_DEBIT_NOTE2_LIST_SLUG, Arrays.asList(TRANSACTION_DEBIT_NOTE1_LIST_SLUG, TRANSACTION_DEBIT_NOTE2_LIST_SLUG), TRANSACTION_DEBIT_NOTE2_LIST_CONTROLLER));

        tabDataDtoList.add(new TabDataDto(PARTNER_PAYMENT_TITLE, PARTNER_PAYMENT_FXML, PARTNER_PAYMENT_SLUG, Arrays.asList(PARTNER_PAYMENT_SLUG), PARTNER_PAYMENT_CONTROLLER));


        tabDataDtoList.add(new TabDataDto("Purchase Invoice Create", PURCHASE_INV_CREATE_FXML, PURCHASE_INV_CREATE_SLUG, Arrays.asList(PURCHASE_INV_CREATE_SLUG, PURCHAE_INV_LIST_SLUG, PURCHASE_INV_EDIT_SLUG,PURCHASE_CHALL_TO_INV_CREATE_SLUG), PURCHAE_INV_CREATE_CONTROLLER));

        tabDataDtoList.add(new TabDataDto("Purchase Invoice List", PURCHASE_INV_LIST_FXML, PURCHAE_INV_LIST_SLUG, Arrays.asList(PURCHAE_INV_LIST_SLUG, PURCHASE_INV_CREATE_SLUG, PURCHASE_INV_EDIT_SLUG,PURCHASE_CHALL_TO_INV_CREATE_SLUG), PURCHAE_INV_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Purchase Invoice Edit", PURCHASE_INV_CREATE_FXML, PURCHASE_INV_EDIT_SLUG, Arrays.asList(MASTER_LDEGER_TRANX_REPORT_LIST_SLUG, PURCHASE_INV_EDIT_SLUG, PURCHASE_INV_CREATE_SLUG, PURCHAE_INV_LIST_SLUG,PURCHASE_CHALL_TO_INV_CREATE_SLUG), PURCHAE_INV_EDIT_CONTROLLER));

        tabDataDtoList.add(new TabDataDto("Payment Voucher", TRANX_PAYMENT_CREATE_FXML, TRANX_PAYMENT_CREATE_SLUG, Arrays.asList(TRANX_PAYMENT_CREATE_SLUG, TRANX_PAYMENT_LIST_SLUG, TRANX_PAYMENT_EDIT_SLUG), TRANX_PAYMENT_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(MASTER_LDEGER_TRANX_REPORT_LIST_TITLE, MASTER_LDEGER_TRANX_REPORT_LIST_FXML, MASTER_LDEGER_TRANX_REPORT_LIST_SLUG, Arrays.asList(MASTER_LDEGER_TRANX_REPORT_LIST_SLUG, "ledger-list", "ledger-create", "ledger-edit"), MASTER_LDEGER_TRANX_REPORT_LIST_CONTROLLER));

      /*  tabDataDtoList.add(new TabDataDto("Purchase Invoice Create", PURCHASE_INV_CREATE_FXML, PURCHASE_INV_CREATE_SLUG, Arrays.asList(PURCHASE_INV_CREATE_SLUG, PURCHAE_INV_LIST_SLUG, PURCHASE_INV_EDIT_SLUG), PURCHAE_INV_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Purchase Invoice List", PURCHASE_INV_LIST_FXML, PURCHAE_INV_LIST_SLUG, Arrays.asList(PURCHAE_INV_LIST_SLUG, PURCHASE_INV_CREATE_SLUG, PURCHASE_INV_EDIT_SLUG), PURCHAE_INV_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Purchase Invoice Edit", PURCHASE_INV_EDIT_FXML, PURCHASE_INV_EDIT_SLUG, Arrays.asList(MASTER_LDEGER_TRANX_REPORT_LIST_SLUG, PURCHASE_INV_EDIT_SLUG, PURCHASE_INV_CREATE_SLUG, PURCHAE_INV_LIST_SLUG), PURCHAE_INV_EDIT_CONTROLLER));
        */
        tabDataDtoList.add(new TabDataDto("Payment Voucher", TRANX_PAYMENT_CREATE_FXML, TRANX_PAYMENT_CREATE_SLUG, Arrays.asList(TRANX_PAYMENT_CREATE_SLUG, TRANX_PAYMENT_LIST_SLUG, TRANX_PAYMENT_EDIT_SLUG), TRANX_PAYMENT_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Payment Edit", TRANX_PAYMENT_EDIT_FXML, TRANX_PAYMENT_EDIT_SLUG, Arrays.asList(TRANX_PAYMENT_CREATE_SLUG, TRANX_PAYMENT_LIST_SLUG, TRANX_PAYMENT_EDIT_SLUG), TRANX_PAYMENT_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Payment Voucher List", TRANX_PAYMENT_LIST_FXML, TRANX_PAYMENT_LIST_SLUG, Arrays.asList(TRANX_PAYMENT_LIST_SLUG, TRANX_PAYMENT_CREATE_SLUG, TRANX_PAYMENT_EDIT_SLUG), TRANX_PAYMENT_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(MASTER_LDEGER_TRANX_REPORT_LIST_TITLE, MASTER_LDEGER_TRANX_REPORT_LIST_FXML, MASTER_LDEGER_TRANX_REPORT_LIST_SLUG, Arrays.asList(MASTER_LDEGER_TRANX_REPORT_LIST_SLUG, "ledger-list", "ledger-create", "ledger-edit"), MASTER_LDEGER_TRANX_REPORT_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(TRANSACTION_CREDIT_NOTE_TITLE, TRANSACTION_CREDIT_NOTE1_LIST_FXML, TRANSACTION_CREDIT_NOTE1_LIST_SLUG, Arrays.asList(TRANSACTION_CREDIT_NOTE1_LIST_SLUG), TRANSACTION_CREDIT_NOTE1_LIST_CONTROLLER));
       /* tabDataDtoList.add(new TabDataDto("Purchase Invoice Create", PURCHASE_INV_CREATE_FXML, PURCHASE_INV_CREATE_SLUG, Arrays.asList(PURCHASE_INV_CREATE_SLUG, PURCHAE_INV_LIST_SLUG, PURCHASE_INV_EDIT_SLUG), PURCHAE_INV_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Purchase Invoice List", PURCHASE_INV_LIST_FXML, PURCHAE_INV_LIST_SLUG, Arrays.asList(PURCHAE_INV_LIST_SLUG, PURCHASE_INV_CREATE_SLUG, PURCHASE_INV_EDIT_SLUG), PURCHAE_INV_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Purchase Invoice Edit", PURCHASE_INV_EDIT_FXML, PURCHASE_INV_EDIT_SLUG, Arrays.asList(MASTER_LDEGER_TRANX_REPORT_LIST_SLUG, PURCHASE_INV_EDIT_SLUG, PURCHASE_INV_CREATE_SLUG, PURCHAE_INV_LIST_SLUG), PURCHAE_INV_EDIT_CONTROLLER));
*/

        tabDataDtoList.add(new TabDataDto(MASTER_LDEGER_TRANX_REPORT_LIST_TITLE, MASTER_LDEGER_TRANX_REPORT_LIST_FXML, MASTER_LDEGER_TRANX_REPORT_LIST_SLUG, Arrays.asList(MASTER_LDEGER_TRANX_REPORT_LIST_SLUG, "ledger-list", "ledger-create", "ledger-edit"), MASTER_LDEGER_TRANX_REPORT_LIST_CONTROLLER));

        /**
         * @implNote : Debit Note List and Create Options
         */
        tabDataDtoList.add(new TabDataDto("Debit Note List", DEBIT_NOTE_LIST_FXML, DEBIT_NOTE_LIST_SLUG, Arrays.asList(DEBIT_NOTE_LIST_SLUG, DEBIT_NOTE_CREATE_SLUG, DEBIT_NOTE_EDIT_SLUG), DEBIT_NOTE_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Debit Note Create", DEBIT_NOTE_CREATE_FXML, DEBIT_NOTE_CREATE_SLUG, Arrays.asList(DEBIT_NOTE_CREATE_SLUG, DEBIT_NOTE_LIST_SLUG, DEBIT_NOTE_EDIT_SLUG), DEBIT_NOTE_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Debit Note Edit", DEBIT_NOTE_CREATE_FXML, DEBIT_NOTE_EDIT_SLUG, Arrays.asList(DEBIT_NOTE_CREATE_SLUG, DEBIT_NOTE_LIST_SLUG, DEBIT_NOTE_EDIT_SLUG), DEBIT_NOTE_CREATE_CONTROLLER));
        //GSTR1
        tabDataDtoList.add(new TabDataDto("GSTR1_B2B1_Report", GSTR1_B2B_SCREEN1_LIST_FXML, GSTR1_B2B_SCREEN1_LIST_SLUG, Arrays.asList(GSTR1_B2B_SCREEN1_LIST_SLUG, GSTR1_B2B_SCREEN2_LIST_SLUG, GSTR1_B2CLARGE_SCREEN1_LIST_SLUG, GSTR1_B2CLARGE_SCREEN2_LIST_SLUG, GSTR1_B2CSMALL_SCREEN1_LIST_SLUG, GSTR1_B2CSMALL_SCREEN2_LIST_SLUG), GSTR1_B2B_SCREEN1_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("GSTR1_B2B2_Report", GSTR1_B2B_SCREEN2_LIST_FXML, GSTR1_B2B_SCREEN2_LIST_SLUG, Arrays.asList(GSTR1_B2B_SCREEN2_LIST_SLUG, GSTR1_B2B_SCREEN1_LIST_SLUG, GSTR1_B2CLARGE_SCREEN1_LIST_SLUG, GSTR1_B2CLARGE_SCREEN2_LIST_SLUG, GSTR1_B2CSMALL_SCREEN1_LIST_SLUG, GSTR1_B2CSMALL_SCREEN2_LIST_SLUG), GSTR1_B2B_SCREEN2_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("GSTR1_B2C_large1_Report", GSTR1_B2CLARGE_SCREEN1_LIST_FXML, GSTR1_B2CLARGE_SCREEN1_LIST_SLUG, Arrays.asList(GSTR1_B2CLARGE_SCREEN1_LIST_SLUG, GSTR1_B2B_SCREEN1_LIST_SLUG, GSTR1_B2B_SCREEN2_LIST_SLUG, GSTR1_B2CLARGE_SCREEN2_LIST_SLUG, GSTR1_B2CSMALL_SCREEN1_LIST_SLUG, GSTR1_B2CSMALL_SCREEN2_LIST_SLUG), GSTR1_B2CLARGE_SCREEN1_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("GSTR1_B2C_large2_Report", GSTR1_B2CLARGE_SCREEN2_LIST_FXML, GSTR1_B2CLARGE_SCREEN2_LIST_SLUG, Arrays.asList(GSTR1_B2CLARGE_SCREEN2_LIST_SLUG, GSTR1_B2CLARGE_SCREEN1_LIST_SLUG, GSTR1_B2B_SCREEN1_LIST_SLUG, GSTR1_B2B_SCREEN2_LIST_SLUG, GSTR1_B2CSMALL_SCREEN1_LIST_SLUG, GSTR1_B2CSMALL_SCREEN2_LIST_SLUG), GSTR1_B2CLARGE_SCREEN2_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("GSTR1_B2C_small1_Report", GSTR1_B2CSMALL_SCREEN1_LIST_FXML, GSTR1_B2CSMALL_SCREEN1_LIST_SLUG, Arrays.asList(GSTR1_B2CSMALL_SCREEN1_LIST_SLUG, GSTR1_B2CLARGE_SCREEN2_LIST_SLUG, GSTR1_B2CLARGE_SCREEN1_LIST_SLUG, GSTR1_B2B_SCREEN1_LIST_SLUG, GSTR1_B2B_SCREEN2_LIST_SLUG, GSTR1_B2CSMALL_SCREEN2_LIST_SLUG), GSTR1_B2CSMALL_SCREEN1_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("GSTR1_B2C_small2_Report", GSTR1_B2CSMALL_SCREEN2_LIST_FXML, GSTR1_B2CSMALL_SCREEN2_LIST_SLUG, Arrays.asList(GSTR1_B2CSMALL_SCREEN2_LIST_SLUG, GSTR1_B2CLARGE_SCREEN2_LIST_SLUG, GSTR1_B2CLARGE_SCREEN1_LIST_SLUG, GSTR1_B2B_SCREEN1_LIST_SLUG, GSTR1_B2B_SCREEN2_LIST_SLUG, GSTR1_B2CSMALL_SCREEN1_LIST_SLUG), GSTR1_B2CSMALL_SCREEN2_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("GSTR1_B2B1_Report", GSTR1_B2B_SCREEN1_LIST_FXML, GSTR1_B2B_SCREEN1_LIST_SLUG, Arrays.asList(GSTR1_B2B_SCREEN1_LIST_SLUG, GSTR1_B2B_SCREEN2_LIST_SLUG, GSTR1_B2CLARGE_SCREEN1_LIST_SLUG, GSTR1_B2CLARGE_SCREEN2_LIST_SLUG), GSTR1_B2B_SCREEN1_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("GSTR1_B2B2_Report", GSTR1_B2B_SCREEN2_LIST_FXML, GSTR1_B2B_SCREEN2_LIST_SLUG, Arrays.asList(GSTR1_B2B_SCREEN2_LIST_SLUG, GSTR1_B2B_SCREEN1_LIST_SLUG, GSTR1_B2CLARGE_SCREEN1_LIST_SLUG, GSTR1_B2CLARGE_SCREEN2_LIST_SLUG), GSTR1_B2B_SCREEN2_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("GSTR1_B2C_large1_Report", GSTR1_B2CLARGE_SCREEN1_LIST_FXML, GSTR1_B2CLARGE_SCREEN1_LIST_SLUG, Arrays.asList(GSTR1_B2CLARGE_SCREEN1_LIST_SLUG, GSTR1_B2B_SCREEN1_LIST_SLUG, GSTR1_B2B_SCREEN2_LIST_SLUG, GSTR1_B2CLARGE_SCREEN2_LIST_SLUG), GSTR1_B2CLARGE_SCREEN1_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("GSTR1_B2C_large2_Report", GSTR1_B2CLARGE_SCREEN2_LIST_FXML, GSTR1_B2CLARGE_SCREEN2_LIST_SLUG, Arrays.asList(GSTR1_B2CLARGE_SCREEN2_LIST_SLUG, GSTR1_B2CLARGE_SCREEN1_LIST_SLUG, GSTR1_B2B_SCREEN1_LIST_SLUG, GSTR1_B2B_SCREEN2_LIST_SLUG), GSTR1_B2CLARGE_SCREEN2_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto(GSTR1_NIL_RATED_LIST_TITLE, GSTR1_NIL_RATED_LIST_FXML, GSTR1_NIL_RATED_LIST_SLUG, Arrays.asList(GSTR1_NIL_RATED_LIST_SLUG), GSTR1_NIL_RATED_LIST_CONTROLLER));


        tabDataDtoList.add(new TabDataDto("Credit Note List", CREDIT_NOTE_LIST_FXML, CREDIT_NOTE_LIST_SLUG, Arrays.asList(CREDIT_NOTE_LIST_SLUG, CREDIT_NOTE_CREATE_SLUG, CREDIT_NOTE_EDIT_SLUG), CREDIT_NOTE_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Credit Note Create", CREDIT_NOTE_CREATE_FXML, CREDIT_NOTE_CREATE_SLUG, Arrays.asList(CREDIT_NOTE_LIST_SLUG, CREDIT_NOTE_CREATE_SLUG, CREDIT_NOTE_EDIT_SLUG), CREDIT_NOTE_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Credit Note Edit", CREDIT_NOTE_CREATE_FXML, CREDIT_NOTE_EDIT_SLUG, Arrays.asList(CREDIT_NOTE_LIST_SLUG, CREDIT_NOTE_CREATE_SLUG, CREDIT_NOTE_EDIT_SLUG), CREDIT_NOTE_CREATE_CONTROLLER));

        tabDataDtoList.add(new TabDataDto("Receipt List", RECEIPT_LIST_FXML, RECEIPT_LIST_SLUG, Arrays.asList(RECEIPT_LIST_SLUG, RECEIPT_CREATE_SLUG, RECEIPT_EDIT_SLUG), RECEIPT_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Receipt Create", RECEIPT_CREATE_FXML, RECEIPT_CREATE_SLUG, Arrays.asList(RECEIPT_LIST_SLUG, RECEIPT_CREATE_SLUG, RECEIPT_EDIT_SLUG), RECEIPT_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Receipt Edit", RECEIPT_CREATE_FXML, RECEIPT_EDIT_SLUG, Arrays.asList(RECEIPT_LIST_SLUG, RECEIPT_CREATE_SLUG, RECEIPT_EDIT_SLUG), RECEIPT_CREATE_CONTROLLER));

        //Purchase Register Report
        tabDataDtoList.add(new TabDataDto("Purchase Register", PURCHASE_REGISTER_FXML, PURCHASE_REGISTER_SLUG, Arrays.asList(PURCHASE_REGISTER_SLUG, PURCHASE_REGISTER_YEARLY_SLUG), PURCHASE_REGISTER_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Purchase Register Yearly", PURCHASE_REGISTER_YEARLY_FXML, PURCHASE_REGISTER_YEARLY_SLUG, Arrays.asList(PURCHASE_REGISTER_SLUG, PURCHASE_REGISTER_YEARLY_SLUG), PURCHASE_REGISTER_YEARLY_CONTROLLER));

        //Dashboard
        tabDataDtoList.add(new TabDataDto("Dashboard", DASHBOARD_FXML, DASHBOARD_SLUG, Arrays.asList(DASHBOARD_SLUG), DASHBOARD_CONTROLLER));

        //Franchise Dashboard
        tabDataDtoList.add(new TabDataDto("Franchise Dashboard", FRANCHISE_DASHBOARD_FXML, FRANCHISE_DASHBOARD_SLUG, Arrays.asList(FRANCHISE_DASHBOARD_SLUG), FRANCHISE_DASHBOARD_CONTROLLER));


        //Utilities
        tabDataDtoList.add(new TabDataDto("Barcode Home Path", SET_BARCODE_HOME_FXML, SET_BARCODE_HOME_SLUG, Arrays.asList(SET_BARCODE_HOME_SLUG), SET_BARCODE_HOME_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Barcode Print", BARCODE_PRINT_FXML, BARCODE_PRINT_SLUG, Arrays.asList(BARCODE_PRINT_SLUG), BARCODE_PRINT_CONTROLLER));

        tabDataDtoList.add(new TabDataDto("Product Import", IMPORT_PRODUCT_FXML, IMPORT_PRODUCT_SLUG, Arrays.asList(IMPORT_PRODUCT_SLUG), IMPORT_PRODUCT_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Stock Import", IMPORT_STOCK_FXML, IMPORT_STOCK_SLUG, Arrays.asList(IMPORT_STOCK_SLUG), IMPORT_STOCK_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Dispatch Management", DISPATCH_MANAGEMENT_FXML, DISPATCH_MANAGEMENT_SLUG, Arrays.asList(DISPATCH_MANAGEMENT_SLUG), DISPATCH_MANAGEMENT_CONTROLLER));


    }

    private void addNewTab(String slug) {
        addNewTab(slug, false);
    }

    public void addNewTab(String slug, boolean confirmation) {
        TabDataDto findDao = tabDataDtoList.stream()
                .filter(fSlug -> slug.equals(fSlug.getSlug()))
                .findAny()
                .orElse(null);

        if (findDao != null) {
            if (tabPane.getTabs().stream().count() <= 0) {
                System.out.println("Adding New Tab Count 0");
                addTab(findDao, false);

            } else {
                boolean isTabFound = false;
                for (Tab tab : tabPane.getTabs()) {
                    if (tab.getId().equalsIgnoreCase(findDao.getSlug())) {
                        tabPane.getSelectionModel().select(tab);
                        isTabFound = true;
                        break;
                    }
                }
                if (!isTabFound) {
//                    System.out.println("No tab found so Adding New Tab");
                    findAndReplaceTab(findDao, confirmation);
                }
            }
        }

    }

    public void addNewTab(String slug, boolean confirmation,boolean isNewTab) {
        TabDataDto findDao = tabDataDtoList.stream()
                .filter(fSlug -> slug.equals(fSlug.getSlug()))
                .findAny()
                .orElse(null);

        if (findDao != null) {
            if (tabPane.getTabs().stream().count() <= 0) {
                System.out.println("Adding New Tab Count 0");
                addTab(findDao, false);

            } else {
                boolean isTabFound = false;
                for (Tab tab : tabPane.getTabs()) {
                    if (tab.getId().equalsIgnoreCase(findDao.getSlug())) {
                        tabPane.getSelectionModel().select(tab);
                        isTabFound = true;
                        break;
                    }
                }
                if (!isTabFound) {
//                    System.out.println("No tab found so Adding New Tab");
                    findAndReplaceTab(findDao, confirmation);
                }
            }
        }

    }

    private void findAndReplaceTab(TabDataDto findDao, boolean confirmation) {
        //AtomicBoolean isTabReplaced= new AtomicBoolean(false);
        List<String> slugList = findDao.getCmpSlug();
        Tab foundTab = null;
        for (String slug :
                slugList) {
            if (!findDao.getSlug().equalsIgnoreCase(slug)) {
                for (Tab tab :
                        tabPane.getTabs()) {
                    if (tab.getId().equalsIgnoreCase(slug)) {
                        foundTab = tab;
                        break;
                    }
                }
                if (foundTab != null)
                    break;
            }
        }

        if (foundTab != null) {
            if (confirmation) {
                tabPane.getSelectionModel().select(foundTab);
                Tab finalFoundTab = foundTab;
                AlertUtility.CustomCallback callback = number -> {
                    if (number == 1) {
                        tabPane.getTabs().remove(finalFoundTab);
                        addTab(findDao, confirmation);
//                        isTabReplaced.set(true);
                    } else {
//                        isTabReplaced.set(true);
                        System.out.println("working!");
                    }
                };
                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to close the tab " + foundTab.getText() + "?", callback);
            } else {
                tabPane.getTabs().remove(foundTab);
                addTab(findDao, confirmation);
            }

        } else {
            addTab(findDao, confirmation);
        }


    }

    private void findAndReplaceTab(TabDataDto findDao, boolean confirmation, int id) {
        //AtomicBoolean isTabReplaced= new AtomicBoolean(false);
        List<String> slugList = findDao.getCmpSlug();
        Tab foundTab = null;
        for (String slug :
                slugList) {
            if (!findDao.getSlug().equalsIgnoreCase(slug)) {
                for (Tab tab :
                        tabPane.getTabs()) {
                    if (tab.getId().equalsIgnoreCase(slug)) {
                        foundTab = tab;
                        break;
                    }
                }
                if (foundTab != null)
                    break;
            }
        }

        if (foundTab != null) {
            if (confirmation) {
                tabPane.getSelectionModel().select(foundTab);
                Tab finalFoundTab = foundTab;
                AlertUtility.CustomCallback callback = number -> {
                    if (number == 1) {
                        tabPane.getTabs().remove(finalFoundTab);
                        addTab(findDao, id);
//                        isTabReplaced.set(true);
                    } else {
//                        isTabReplaced.set(true);
                        System.out.println("working!");
                    }
                };
                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to close the tab " + foundTab.getText() + "?", callback);
            } else {
                tabPane.getTabs().remove(foundTab);
                addTab(findDao, id);
            }

        } else {
            addTab(findDao, id);
        }


    }

    private void findAndReplaceTab(TabDataDto findDao, boolean confirmation, FranchiseDTO franchiseDTO) {
        //AtomicBoolean isTabReplaced= new AtomicBoolean(false);
        List<String> slugList = findDao.getCmpSlug();
        Tab foundTab = null;
        for (String slug :
                slugList) {
            if (!findDao.getSlug().equalsIgnoreCase(slug)) {
                for (Tab tab :
                        tabPane.getTabs()) {
                    if (tab.getId().equalsIgnoreCase(slug)) {
                        foundTab = tab;
                        break;
                    }
                }
                if (foundTab != null)
                    break;
            }
        }

        if (foundTab != null) {
            if (confirmation) {
                tabPane.getSelectionModel().select(foundTab);
                Tab finalFoundTab = foundTab;
                AlertUtility.CustomCallback callback = number -> {
                    if (number == 1) {
                        tabPane.getTabs().remove(finalFoundTab);
                        addTab(findDao, franchiseDTO);
//                        isTabReplaced.set(true);
                    } else {
//                        isTabReplaced.set(true);
                        System.out.println("working!");
                    }
                };
                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to close the tab " + foundTab.getText() + "?", callback);
            } else {
                tabPane.getTabs().remove(foundTab);
                addTab(findDao, franchiseDTO);
            }

        } else {
            addTab(findDao, franchiseDTO);
        }


    }

    private void addTab(TabDataDto findDao, boolean confirmation) {
        //boolean isTabReplaced = findAndReplaceTab(findDao,confirmation);
        FXMLLoader nLoader = null;
        Parent nRoot = null;
        try {
            // loader = FXMLLoader.load(GenivisApplication.class.getResource(findDao.getFileName()));
            System.out.println("findDao.getFileName() >> " + findDao.getFileName());
            nLoader = new FXMLLoader(GenivisApplication.class.getResource(findDao.getFileName()));

//            nLoader.setController(GenivisApplication.class.getResource(findDao.getControllerName()));
            nRoot = nLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Tab tab = new Tab(findDao.getTitle());
        tab.setContent(nRoot);
        tab.setId(findDao.getSlug());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

        tab.setOnCloseRequest(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                event.consume();
                AlertUtility.CustomCallback callback = number -> {
                    if (number == 1) {
//                            System.out.println("Yes Confirm");
                        Tab confirmactTab = tabPane.getSelectionModel().getSelectedItem();
//                            System.out.println("confirmactTab => " + confirmactTab);
                        tabPane.getTabs().remove(confirmactTab);
                    } else {
                        System.out.println("working!");
                    }
                };
                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to close the tab " + tabPane.getSelectionModel().getSelectedItem().getText() + "?", callback);
            }
        });


    }


    public void addNewTab(String slug, Boolean askConfirmation, Integer id) {
        TabDataDto findDao = tabDataDtoList.stream()
                .filter(fSlug -> slug.equals(fSlug.getSlug()))
                .findAny()
                .orElse(null);

        if (tabPane.getTabs().stream().count() <= 0) {
            addTab(findDao, id);

        } else {
            boolean isTabFound = false;
            for (Tab tab : tabPane.getTabs()) {
                if (tab.getId().equalsIgnoreCase(findDao.getSlug())) {
                    tabPane.getSelectionModel().select(tab);
                    isTabFound = true;
                    break;
                }
            }
            if (!isTabFound) {
                findAndReplaceTab(findDao, askConfirmation, id);
            }

        }

    }

    public void addNewTab(String slug, Boolean askConfirmation, FranchiseDTO franchiseDTO) {
//        System.out.println("slug ->" + slug);
        TabDataDto findDao = tabDataDtoList.stream()
                .filter(fSlug -> slug.equals(fSlug.getSlug()))
                .findAny()
                .orElse(null);
//        System.out.println("findDao -> " + findDao);

        if (tabPane.getTabs().stream().count() <= 0) {
            addTab(findDao, franchiseDTO);

        } else {
            boolean isTabFound = false;
            for (Tab tab : tabPane.getTabs()) {
                if (tab.getId().equalsIgnoreCase(findDao.getSlug())) {
                    tabPane.getSelectionModel().select(tab);
                    isTabFound = true;
                    break;
                }
            }
            if (!isTabFound) {
//                    System.out.println("No tab found so Adding New Tab");
                findAndReplaceTab(findDao, askConfirmation, franchiseDTO);

            }

        }

    }


    private void addTab(TabDataDto findDao, Integer id) {
        FXMLLoader nLoader = null;
        Parent nRoot = null;
        try {
            nLoader = new FXMLLoader(GenivisApplication.class.getResource(findDao.getFileName()));
            nRoot = nLoader.load();

            Tab tab = new Tab(findDao.getTitle());
            tab.setId(findDao.getSlug());
            tab.setContent(nRoot);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);

            tab.setOnCloseRequest(event -> {
                event.consume();
                AlertUtility.CustomCallback callback = number -> {
                    if (number == 1) {
//                            System.out.println("Yes Confirm");
                        Tab confirmactTab = tabPane.getSelectionModel().getSelectedItem();
//                            System.out.println("confirmactTab => " + confirmactTab);
                        tabPane.getTabs().remove(confirmactTab);
                    } else {
                        System.out.println("working!");
                    }
                };
                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to close the tab " +
                        tabPane.getSelectionModel().getSelectedItem().getText() + "?", callback);
            });
            {
                Object obj = nLoader.getController();
                Method[] methods = obj.getClass().getDeclaredMethods();
                for (Method method : methods) {
//                System.out.println("method names -> " + method.getName());
                    if (method.getName().equalsIgnoreCase("setEditId")) {
                        try {
                            method.invoke(obj, id);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        } catch (IOException e) {
            cadminLogger.error("Exception in addTab():" + Globals.getExceptionString(e));
        }
    }

    private void addTab(TabDataDto findDao, FranchiseDTO franchiseDTO) {
        FXMLLoader nLoader = null;
        Parent nRoot = null;
        try {
            nLoader = new FXMLLoader(GenivisApplication.class.getResource(findDao.getFileName()));
            nRoot = nLoader.load();

            Tab tab = new Tab(findDao.getTitle());
            tab.setId(findDao.getSlug());
            tab.setContent(nRoot);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);

            tab.setOnCloseRequest(event -> {
                event.consume();
                AlertUtility.CustomCallback callback = number -> {
                    if (number == 1) {
//                            System.out.println("Yes Confirm");
                        Tab confirmactTab = tabPane.getSelectionModel().getSelectedItem();
//                            System.out.println("confirmactTab => " + confirmactTab);
                        tabPane.getTabs().remove(confirmactTab);
                    } else {
                        System.out.println("working!");
                    }
                };
                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to close the tab " + tabPane.getSelectionModel().getSelectedItem().getText() + "?", callback);
            });
            {

                Object obj = nLoader.getController();
                Method[] methods = obj.getClass().getDeclaredMethods();
                for (Method method : methods) {
//                System.out.println("method names -> " + method.getName());
                    if (method.getName().equalsIgnoreCase("setFrDataToLedger")) {
                        try {
                            method.invoke(obj, franchiseDTO);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        } catch (IOException e) {
            cadminLogger.error("Exception in actTabAdd():" + Globals.getExceptionString(e));
        }
//        this.keyboardShortcuts();
    }


    public void Logout() {
        try {
            Stage stage = new Stage();
            loader = FXMLLoader.load(GenivisApplication.class.getResource("login/login.fxml"));
            scene = new Scene(loader);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
//            Stage mainStage = (Stage) mainPane.getScene().getWindow();
//            mainStage.close();
        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("Message:" + e.getMessage());
            cadminLogger.error("Exception in Logout():" + Globals.getExceptionString(e));

        }
    }

    public void GCSlug(String slg, Boolean fl) {
        addNewTab(slg, fl);
    }

    public void GCSlugWithParam(String slg, Boolean fl, Integer id) {
        addNewTab(slg, fl, id);
    }

    public void GCSlugWithFrParam(String slg, Boolean fl, FranchiseDTO franchiseDTO) {
        addNewTab(slg, fl, franchiseDTO);
    }

    public void GCSlugIsNewTab(String slg, Boolean fl, Boolean isNewTab) {
        addNewTab(slg, fl, isNewTab);
    }

    // new method - reports id passing
    public void GCSlug1(String slg, Boolean fl, Integer id) {
        addNewTab(slg, fl, id);
    }

    public void dashboardPageLoad(MouseEvent mouseEvent) {
        addNewTab(FRANCHISE_DASHBOARD_SLUG);
    }

    //    *********************************************************LOGOUT START*****************************************************************************
    @FXML
    private void handleLogout(ActionEvent event) {
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to sign out?", input -> {
            if (input == 1) {
                restartApplication();
            }
        });
    }

    private void restartApplication() {
        // Get the current stage
        Stage currentStage = (Stage) bpCadminDashboard.getScene().getWindow();

        // Close the current stage
        currentStage.close();

        // Start a new instance of the application
        try {
            new GenivisApplication().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace(); // Handle Exception appropriately
        }
    }
//    *********************************************************LOGOUT END*****************************************************************************

    public void GCSlugWithIsNewTab(String slg, Boolean isNewTab, Object obj) {
//        addNewTab(slg, fl, id);

        addTabwithIsNewTab(slg, isNewTab, obj);
    }

    public void addTabwithIsNewTab(String slug, Boolean isNewTab, Object obj) {
        TabDataDto findDao = tabDataDtoList.stream()
                .filter(fSlug -> slug.equals(fSlug.getSlug()))
                .findAny()
                .orElse(null);

        if (tabPane.getTabs().stream().count() <= 0) {
            addTab(findDao, obj);
        } else if (isNewTab == true) {
            addTab(findDao, obj);
        } else {
            Tab confirmactTab = tabPane.getSelectionModel().getSelectedItem();
            tabPane.getTabs().remove(confirmactTab);
            addTab(findDao, obj);
        }

    }
    private void addTab(TabDataDto findDao, Object inObj) {
        FXMLLoader nLoader = null;
        Parent nRoot = null;
        try {
            nLoader = new FXMLLoader(GenivisApplication.class.getResource(findDao.getFileName()));
            nRoot = nLoader.load();

            Tab tab = new Tab(findDao.getTitle());
            tab.setId(findDao.getSlug());
            tab.setContent(nRoot);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);

            tab.setOnCloseRequest(event -> {
                event.consume();
                AlertUtility.CustomCallback callback = number -> {
                    if (number == 1) {
//                            System.out.println("Yes Confirm");
                        Tab confirmactTab = tabPane.getSelectionModel().getSelectedItem();
//                            System.out.println("confirmactTab => " + confirmactTab);
                        tabPane.getTabs().remove(confirmactTab);
                    } else {
                        System.out.println("working!");
                    }
                };
                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to close the tab " +
                        tabPane.getSelectionModel().getSelectedItem().getText() + "?", callback);
            });
            {
                Object obj = nLoader.getController();
                Method[] methods = obj.getClass().getDeclaredMethods();
                for (Method method : methods) {
//                System.out.println("method names -> " + method.getName());
                    if (method.getName().equalsIgnoreCase("setRedirectData")) {
                        try {
                            method.invoke(obj, inObj);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        } catch (IOException e) {
            cadminLogger.error("Exception in addTab():" + Globals.getExceptionString(e));
        }
    }
}


