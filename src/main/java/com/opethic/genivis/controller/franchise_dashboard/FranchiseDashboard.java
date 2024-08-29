// Controller

package com.opethic.genivis.controller.franchise_dashboard;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.dashboard.DashboardController;
import com.opethic.genivis.dto.CompanyListDTO;
import com.opethic.genivis.dto.dashboard.DashboardProductStockDTO;
import com.opethic.genivis.dto.franchise_dashboard.PurchaseInvoiceListDTO;
import com.opethic.genivis.dto.franchise_dashboard.SummaryListDTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.GlobalController;
import com.opethic.genivis.utils.Globals;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.FxmFileConstants.*;

public class FranchiseDashboard implements Initializable {

    private static final Logger logger = LogManager.getLogger(FranchiseDashboard.class);


    private JsonObject jsonObject = null;
    @FXML
    private HBox firstRow, secondRow, thirdRow, fourthRow, firstRowFirst, firstRowSecond, firstRowSecondFirst, firstRowSecondSecond, secondRowFirst, secondRowSecond, secondRowSecondFirst, secondRowSecondSecond, secondRowSecondThird, thirdRowFirst, thirdRowSecond, fourthRowFirst, fourthRowSecond,cashSummaryRow,productsLblRow,TranxLblRow,bottomSmallDiv;
    @FXML
    private ImageView imageView, imageViewSecond, firstLeftArrow, firstRightArrow, secondLeftArrow, secondRightArrow,gvLogoImage,ethiqLogoimage,bottomLogo,bottomLogo1,bottomLogo2,bottomLogo3,bottomLogo4;
    private int currentIndex;
    private int currentIndexSecond;
    @FXML
    private BorderPane dashboardRoot;
    @FXML
    private VBox verticalVbox;
    private Image[] images;
    private Image[] imagesSecond;
    @FXML
    private Button frTranxPurchaseBtn, frTranxSaleBtn;
    @FXML
    private HBox hbCounterSale, hbStockReports, hbPurInvoice, hbBalanceSheet;
    @FXML
    private StackPane firstStack, secondStack;
    private Timeline autoSlideTimeline;
    private Timeline autoSlideTimelineSecond;
    private boolean isAutoSliding = true;
    private boolean isAutoSlidingSecond = true;
    // Define boolean flags to track if data has been loaded
    boolean purchaseDataLoaded = false;
    boolean saleDataLoaded = false;
    @FXML
    private HBox indicatorsContainer, indicatorsContainerSecond;
    @FXML
    private TableView tblvFrDashboardTopFr;
    @FXML
    private TableColumn tblcFrDashboardSrNo, tblcFrDashboardFrName;
    @FXML
    private TableView<SummaryListDTO> tblvFrDashboardCashSummary;
    @FXML
    private TableColumn<SummaryListDTO, String> tblcFrDashboardLedgerName, tblcFrDashboardAmount, tblcFrDashboardDays;
    @FXML
    private TableView tblvFrDashboardProducts;
    @FXML
    private TableColumn tblcFrDashboardNumber, tblcFrDashboardProductsName;
    @FXML
    private TableView<PurchaseInvoiceListDTO> tblvFrDashboardTranx;
    @FXML
    private TableColumn<PurchaseInvoiceListDTO, String> tblcFrDashboardTranxSrNo, tblcFrDashboardTranxAddAndName, tblcFrDashboardTranxAmt;

    ObservableList<PurchaseInvoiceListDTO> purchaseInvoiceListDTO = FXCollections.observableArrayList();
    ObservableList<SummaryListDTO> summaryListDTO = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //first Row
        firstRowFirst.prefWidthProperty().bind(firstRow.widthProperty().multiply(0.2));
        firstRowSecond.prefWidthProperty().bind(firstRow.widthProperty().multiply(0.8));

        //second Row
        secondRowFirst.prefWidthProperty().bind(secondRow.widthProperty().multiply(0.2));
        secondRowSecond.prefWidthProperty().bind(secondRow.widthProperty().multiply(0.8));

        //third Row
        thirdRowFirst.prefWidthProperty().bind(thirdRow.widthProperty().multiply(0.2));
        thirdRowSecond.prefWidthProperty().bind(thirdRow.widthProperty().multiply(0.8));

        //fourth Row
        fourthRowFirst.prefWidthProperty().bind(fourthRow.widthProperty().multiply(0.2));
        fourthRowSecond.prefWidthProperty().bind(fourthRow.widthProperty().multiply(0.8));

        //first Row Inner
        firstRowSecondFirst.prefWidthProperty().bind(firstRowSecond.widthProperty().multiply(0.5));
        firstRowSecondSecond.prefWidthProperty().bind(firstRowSecond.widthProperty().multiply(0.5));

        //second Row Inner
        secondRowSecondFirst.prefWidthProperty().bind(secondRowSecond.widthProperty().multiply(0.33));
        secondRowSecondSecond.prefWidthProperty().bind(secondRowSecond.widthProperty().multiply(0.33));
        secondRowSecondThird.prefWidthProperty().bind(secondRowSecond.widthProperty().multiply(0.34));

        //fourth Row Inner
        hbCounterSale.prefWidthProperty().bind(fourthRowSecond.widthProperty().multiply(0.25));
        hbStockReports.prefWidthProperty().bind(fourthRowSecond.widthProperty().multiply(0.25));
        hbPurInvoice.prefWidthProperty().bind(fourthRowSecond.widthProperty().multiply(0.25));
        hbBalanceSheet.prefWidthProperty().bind(fourthRowSecond.widthProperty().multiply(0.25));


        // Bind the width of the imageView to the width of the anchorPane


        // Optionally, maintain the aspect ratio
        imageView.setPreserveRatio(true);

        //image
//        imageView.setPreserveRatio(false);
//        imageViewSecond.setPreserveRatio(false);

//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });

        ResponsiveWiseCssPicker();

        //TODO: resizing the table columns as per the resolution.
//        tblvFrDashboardTopFr.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblvFrDashboardCashSummary.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        tblvFrDashboardProducts.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblvFrDashboardTranx.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        images = new Image[]{
                new Image(getClass().getResourceAsStream("/com/opethic/genivis/ui/assets/franchise_dashboard/banner1.png")),
                new Image(getClass().getResourceAsStream("/com/opethic/genivis/ui/assets/franchise_dashboard/banner2.png")),
                new Image(getClass().getResourceAsStream("/com/opethic/genivis/ui/assets/franchise_dashboard/banner1.png")),
                new Image(getClass().getResourceAsStream("/com/opethic/genivis/ui/assets/franchise_dashboard/banner2.png")),
        };
        imagesSecond = new Image[]{
                new Image(getClass().getResourceAsStream("/com/opethic/genivis/ui/assets/franchise_dashboard/banner2.png")),
                new Image(getClass().getResourceAsStream("/com/opethic/genivis/ui/assets/franchise_dashboard/banner1.png")),
                new Image(getClass().getResourceAsStream("/com/opethic/genivis/ui/assets/franchise_dashboard/banner2.png")),
                new Image(getClass().getResourceAsStream("/com/opethic/genivis/ui/assets/franchise_dashboard/banner1.png")),
                new Image(getClass().getResourceAsStream("/com/opethic/genivis/ui/assets/franchise_dashboard/banner2.png")),
                new Image(getClass().getResourceAsStream("/com/opethic/genivis/ui/assets/franchise_dashboard/banner1.png")),
        };

        // Bind the ImageView width to the HBox width
//        imageView.fitWidthProperty().bind(firstStack.widthProperty());
//        imageViewSecond.fitWidthProperty().bind(secondStack.widthProperty());

        currentIndex = 0;
        currentIndexSecond = 0;
        updateImageView();
        updateImageViewSecond();
        initIndicators(images.length);
        initIndicatorsSecond(imagesSecond.length);
        updateIndicators();
        updateIndicatorsSecond();
        startAutoSlide();
        startAutoSlideSecond();

        //redirect to Counter Sale
        hbCounterSale.setOnMouseClicked(mouseEvent -> {
            GlobalController.getInstance().addTabStatic(COUNTER_SALE_SLUG, false);
        });

        //redirect to stock report
        hbStockReports.setOnMouseClicked(mouseEvent -> {
            GlobalController.getInstance().addTabStatic(STOCKS_STOCK_REPORT1_LIST_SLUG, false);
        });

        //redirect to purchase invoice
        hbPurInvoice.setOnMouseClicked(mouseEvent -> {
            GlobalController.getInstance().addTabStatic(PURCHAE_INV_LIST_SLUG, false);
        });

        //redirect to balance sheet
        hbBalanceSheet.setOnMouseClicked(mouseEvent -> {
//            GlobalController.getInstance().addTabStatic(BALANCE_SHEET_SLUG,false);
        });


        getTopSellingList();
        getLeastSellingList();
        getSaleInvoiceList();
        getSummaryList();

        responsiveTranxTable();

        frTranxSaleBtn.getStyleClass().add("greenBtnStyle");
        frTranxPurchaseBtn.getStyleClass().add("whiteBtnStyle");

        frTranxPurchaseBtn.setOnAction(actionEvent -> {
            if (!purchaseDataLoaded) {
                getPurchaseInvoiceList();
                purchaseDataLoaded = true;
                saleDataLoaded = false;
                // Update button styles
                frTranxPurchaseBtn.getStyleClass().remove("whiteBtnStyle");
                frTranxPurchaseBtn.getStyleClass().add("greenBtnStyle");
                frTranxSaleBtn.getStyleClass().remove("greenBtnStyle");
                frTranxSaleBtn.getStyleClass().add("whiteBtnStyle");
            }
        });

        frTranxSaleBtn.setOnAction(actionEvent -> {
            if (!saleDataLoaded) {
                getSaleInvoiceList();
                saleDataLoaded = true;
                purchaseDataLoaded = false;
                // Update button styles
                frTranxSaleBtn.getStyleClass().remove("whiteBtnStyle");
                frTranxSaleBtn.getStyleClass().add("greenBtnStyle");
                frTranxPurchaseBtn.getStyleClass().remove("greenBtnStyle");
                frTranxPurchaseBtn.getStyleClass().add("whiteBtnStyle");
            }
        });


    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {


            //first Row
            firstRowFirst.prefWidthProperty().bind(firstRow.widthProperty().multiply(0.16));
            firstRowSecond.prefWidthProperty().bind(firstRow.widthProperty().multiply(0.84));

            //second Row
            secondRowFirst.prefWidthProperty().bind(secondRow.widthProperty().multiply(0.16));
            secondRowSecond.prefWidthProperty().bind(secondRow.widthProperty().multiply(0.84));

            //third Row
            thirdRowFirst.prefWidthProperty().bind(thirdRow.widthProperty().multiply(0.16));
            thirdRowSecond.prefWidthProperty().bind(thirdRow.widthProperty().multiply(0.84));

            //fourth Row
            fourthRowFirst.prefWidthProperty().bind(fourthRow.widthProperty().multiply(0.16));
            fourthRowSecond.prefWidthProperty().bind(fourthRow.widthProperty().multiply(0.84));

            //second Row Inner
            secondRowSecondFirst.prefWidthProperty().bind(secondRowSecond.widthProperty().multiply(0.31));
            secondRowSecondSecond.prefWidthProperty().bind(secondRowSecond.widthProperty().multiply(0.31));
            secondRowSecondThird.prefWidthProperty().bind(secondRowSecond.widthProperty().multiply(0.38));

            bottomLogo.setFitHeight(15);
            bottomLogo1.setFitHeight(15);
            bottomLogo2.setFitHeight(15);
            bottomLogo3.setFitHeight(15);
            bottomLogo4.setFitHeight(15);

            imageView.setFitHeight(140);
            imageViewSecond.setFitHeight(140);
            imageView.setFitWidth(410);
            imageViewSecond.setFitWidth(410);
            imageViewSecond.setPreserveRatio(false);
            imageView.setPreserveRatio(false);

            firstLeftArrow.setFitHeight(8);
            firstLeftArrow.setFitWidth(8);
            firstRightArrow.setFitHeight(8);
            firstRightArrow.setFitWidth(8);

            secondLeftArrow.setFitHeight(10);
            secondLeftArrow.setFitWidth(10);
            secondRightArrow.setFitHeight(10);
            secondRightArrow.setFitWidth(10);

            gvLogoImage.setFitWidth(100);
            gvLogoImage.setFitHeight(100);

            ethiqLogoimage.setFitWidth(100);
            ethiqLogoimage.setFitHeight(100);

            verticalVbox.setSpacing(14);
            firstRow.setSpacing(10);
            secondRow.setSpacing(10);
            thirdRow.setSpacing(10);
            fourthRow.setSpacing(10);
            secondRowSecond.setSpacing(8);
            secondRowSecond.setSpacing(8);
            firstRowSecond.setSpacing(8);
            cashSummaryRow.setSpacing(6);
            TranxLblRow.setSpacing(6);
            productsLblRow.setSpacing(6);
            bottomSmallDiv.setSpacing(0);



            dashboardRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/counterSaleStyle/counterSaleStyle1.css").toExternalForm());

        } else if (width >= 1025 && width <= 1280) {


            //first Row
            firstRowFirst.prefWidthProperty().bind(firstRow.widthProperty().multiply(0.16));
            firstRowSecond.prefWidthProperty().bind(firstRow.widthProperty().multiply(0.84));

            //second Row
            secondRowFirst.prefWidthProperty().bind(secondRow.widthProperty().multiply(0.16));
            secondRowSecond.prefWidthProperty().bind(secondRow.widthProperty().multiply(0.84));

            //third Row
            thirdRowFirst.prefWidthProperty().bind(thirdRow.widthProperty().multiply(0.16));
            thirdRowSecond.prefWidthProperty().bind(thirdRow.widthProperty().multiply(0.84));

            //fourth Row
            fourthRowFirst.prefWidthProperty().bind(fourthRow.widthProperty().multiply(0.16));
            fourthRowSecond.prefWidthProperty().bind(fourthRow.widthProperty().multiply(0.84));

            //second Row Inner
            secondRowSecondFirst.prefWidthProperty().bind(secondRowSecond.widthProperty().multiply(0.32));
            secondRowSecondSecond.prefWidthProperty().bind(secondRowSecond.widthProperty().multiply(0.32));
            secondRowSecondThird.prefWidthProperty().bind(secondRowSecond.widthProperty().multiply(0.36));

            bottomLogo.setFitHeight(22);
            bottomLogo1.setFitHeight(22);
            bottomLogo2.setFitHeight(22);
            bottomLogo3.setFitHeight(22);
            bottomLogo4.setFitHeight(22);

            imageView.setFitHeight(155);
            imageViewSecond.setFitHeight(155);
            imageView.setFitWidth(512);
            imageViewSecond.setFitWidth(512);
            imageViewSecond.setPreserveRatio(false);
            imageView.setPreserveRatio(false);

            firstLeftArrow.setFitHeight(8);
            firstLeftArrow.setFitWidth(8);
            firstRightArrow.setFitHeight(8);
            firstRightArrow.setFitWidth(8);

            secondLeftArrow.setFitHeight(10);
            secondLeftArrow.setFitWidth(10);
            secondRightArrow.setFitHeight(10);
            secondRightArrow.setFitWidth(10);

            gvLogoImage.setFitWidth(120);
            gvLogoImage.setFitHeight(120);

            ethiqLogoimage.setFitWidth(120);
            ethiqLogoimage.setFitHeight(120);

            verticalVbox.setSpacing(12);
            firstRow.setSpacing(12);
            secondRow.setSpacing(12);
            thirdRow.setSpacing(12);
            fourthRow.setSpacing(12);
            secondRowSecond.setSpacing(10);
            firstRowSecond.setSpacing(10);



            dashboardRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/counterSaleStyle/counterSaleStyle2.css").toExternalForm());

        } else if (width >= 1281 && width <= 1368) {


            //first Row
            firstRowFirst.prefWidthProperty().bind(firstRow.widthProperty().multiply(0.18));
            firstRowSecond.prefWidthProperty().bind(firstRow.widthProperty().multiply(0.82));

            //second Row
            secondRowFirst.prefWidthProperty().bind(secondRow.widthProperty().multiply(0.18));
            secondRowSecond.prefWidthProperty().bind(secondRow.widthProperty().multiply(0.82));

            //third Row
            thirdRowFirst.prefWidthProperty().bind(thirdRow.widthProperty().multiply(0.18));
            thirdRowSecond.prefWidthProperty().bind(thirdRow.widthProperty().multiply(0.82));

            //fourth Row
            fourthRowFirst.prefWidthProperty().bind(fourthRow.widthProperty().multiply(0.18));
            fourthRowSecond.prefWidthProperty().bind(fourthRow.widthProperty().multiply(0.82));

            //second Row Inner
            secondRowSecondFirst.prefWidthProperty().bind(secondRowSecond.widthProperty().multiply(0.32));
            secondRowSecondSecond.prefWidthProperty().bind(secondRowSecond.widthProperty().multiply(0.32));
            secondRowSecondThird.prefWidthProperty().bind(secondRowSecond.widthProperty().multiply(0.36));


            imageView.setFitHeight(160);
            imageViewSecond.setFitHeight(160);
            imageView.setFitWidth(530);
            imageViewSecond.setFitWidth(530);
            imageViewSecond.setPreserveRatio(false);
            imageView.setPreserveRatio(false);

            firstLeftArrow.setFitHeight(10);
            firstLeftArrow.setFitWidth(10);
            firstRightArrow.setFitHeight(10);
            firstRightArrow.setFitWidth(10);

            secondLeftArrow.setFitHeight(10);
            secondLeftArrow.setFitWidth(10);
            secondRightArrow.setFitHeight(10);
            secondRightArrow.setFitWidth(10);

            gvLogoImage.setFitWidth(130);
            gvLogoImage.setFitHeight(130);

            ethiqLogoimage.setFitWidth(130);
            ethiqLogoimage.setFitHeight(130);

            verticalVbox.setSpacing(20);
            firstRow.setSpacing(15);
            secondRow.setSpacing(15);
            thirdRow.setSpacing(15);
            fourthRow.setSpacing(15);
            secondRowSecond.setSpacing(12);



            dashboardRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/counterSaleStyle/counterSaleStyle3.css").toExternalForm());

        } else if (width >= 1369 && width <= 1400) {

            //first Row
            firstRowFirst.prefWidthProperty().bind(firstRow.widthProperty().multiply(0.18));
            firstRowSecond.prefWidthProperty().bind(firstRow.widthProperty().multiply(0.82));

            //second Row
            secondRowFirst.prefWidthProperty().bind(secondRow.widthProperty().multiply(0.18));
            secondRowSecond.prefWidthProperty().bind(secondRow.widthProperty().multiply(0.82));

            //third Row
            thirdRowFirst.prefWidthProperty().bind(thirdRow.widthProperty().multiply(0.18));
            thirdRowSecond.prefWidthProperty().bind(thirdRow.widthProperty().multiply(0.82));

            //fourth Row
            fourthRowFirst.prefWidthProperty().bind(fourthRow.widthProperty().multiply(0.18));
            fourthRowSecond.prefWidthProperty().bind(fourthRow.widthProperty().multiply(0.82));

            //second Row Inner
            secondRowSecondFirst.prefWidthProperty().bind(secondRowSecond.widthProperty().multiply(0.32));
            secondRowSecondSecond.prefWidthProperty().bind(secondRowSecond.widthProperty().multiply(0.32));
            secondRowSecondThird.prefWidthProperty().bind(secondRowSecond.widthProperty().multiply(0.36));


            imageView.setFitHeight(200);
            imageViewSecond.setFitHeight(200);
            imageView.setFitWidth(550);
            imageViewSecond.setFitWidth(550);
            imageViewSecond.setPreserveRatio(false);
            imageView.setPreserveRatio(false);

            firstLeftArrow.setFitHeight(10);
            firstLeftArrow.setFitWidth(10);
            firstRightArrow.setFitHeight(10);
            firstRightArrow.setFitWidth(10);

            secondLeftArrow.setFitHeight(10);
            secondLeftArrow.setFitWidth(10);
            secondRightArrow.setFitHeight(10);
            secondRightArrow.setFitWidth(10);

            gvLogoImage.setFitWidth(130);
            gvLogoImage.setFitHeight(130);

            ethiqLogoimage.setFitWidth(130);
            ethiqLogoimage.setFitHeight(130);

            verticalVbox.setSpacing(20);
            firstRow.setSpacing(15);
            secondRow.setSpacing(15);
            thirdRow.setSpacing(15);
            fourthRow.setSpacing(15);
            secondRowSecond.setSpacing(12);



            dashboardRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/counterSaleStyle/counterSaleStyle4.css").toExternalForm());

        } else if (width >= 1401 && width <= 1440) {

            //first Row
            firstRowFirst.prefWidthProperty().bind(firstRow.widthProperty().multiply(0.18));
            firstRowSecond.prefWidthProperty().bind(firstRow.widthProperty().multiply(0.82));

            //second Row
            secondRowFirst.prefWidthProperty().bind(secondRow.widthProperty().multiply(0.18));
            secondRowSecond.prefWidthProperty().bind(secondRow.widthProperty().multiply(0.82));

            //third Row
            thirdRowFirst.prefWidthProperty().bind(thirdRow.widthProperty().multiply(0.18));
            thirdRowSecond.prefWidthProperty().bind(thirdRow.widthProperty().multiply(0.82));

            //fourth Row
            fourthRowFirst.prefWidthProperty().bind(fourthRow.widthProperty().multiply(0.18));
            fourthRowSecond.prefWidthProperty().bind(fourthRow.widthProperty().multiply(0.82));

            //second Row Inner
            secondRowSecondFirst.prefWidthProperty().bind(secondRowSecond.widthProperty().multiply(0.32));
            secondRowSecondSecond.prefWidthProperty().bind(secondRowSecond.widthProperty().multiply(0.32));
            secondRowSecondThird.prefWidthProperty().bind(secondRowSecond.widthProperty().multiply(0.36));


            imageView.setFitHeight(152);
            imageViewSecond.setFitHeight(152);
            imageView.setFitWidth(562);
            imageViewSecond.setFitWidth(562);
            imageViewSecond.setPreserveRatio(false);
            imageView.setPreserveRatio(false);

            firstLeftArrow.setFitHeight(10);
            firstLeftArrow.setFitWidth(10);
            firstRightArrow.setFitHeight(10);
            firstRightArrow.setFitWidth(10);

            secondLeftArrow.setFitHeight(10);
            secondLeftArrow.setFitWidth(10);
            secondRightArrow.setFitHeight(10);
            secondRightArrow.setFitWidth(10);

            gvLogoImage.setFitWidth(120);
            gvLogoImage.setFitHeight(120);

            ethiqLogoimage.setFitWidth(120);
            ethiqLogoimage.setFitHeight(120);

            verticalVbox.setSpacing(20);
            firstRow.setSpacing(15);
            secondRow.setSpacing(15);
            thirdRow.setSpacing(15);
            fourthRow.setSpacing(15);
            secondRowSecond.setSpacing(12);



            dashboardRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/counterSaleStyle/counterSaleStyle5.css").toExternalForm());

        } else if (width >= 1441 && width <= 1680) {
            imageView.setFitHeight(192);
            imageViewSecond.setFitHeight(192);
            imageView.setFitWidth(644);
            imageViewSecond.setFitWidth(644);
            imageViewSecond.setPreserveRatio(false);
            imageView.setPreserveRatio(false);

            firstLeftArrow.setFitHeight(12);
            firstLeftArrow.setFitWidth(12);
            firstRightArrow.setFitHeight(12);
            firstRightArrow.setFitWidth(12);

            secondLeftArrow.setFitHeight(12);
            secondLeftArrow.setFitWidth(12);
            secondRightArrow.setFitHeight(12);
            secondRightArrow.setFitWidth(12);


            dashboardRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/counterSaleStyle/counterSaleStyle6.css").toExternalForm());

        } else if (width >= 1681 && width <= 1920) {
            imageView.setFitHeight(220);
            imageViewSecond.setFitHeight(220);
            imageView.setFitWidth(740);
            imageViewSecond.setFitWidth(740);
            imageViewSecond.setPreserveRatio(false);
            imageView.setPreserveRatio(false);
            dashboardRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/counterSaleStyle/counterSaleStyle7.css").toExternalForm());
        }
    }

    private void responsiveTranxTable() {
        tblcFrDashboardTranxSrNo.prefWidthProperty().bind(tblvFrDashboardTranx.widthProperty().multiply(0.1));
        tblcFrDashboardTranxAddAndName.prefWidthProperty().bind(tblvFrDashboardTranx.widthProperty().multiply(0.7));
        tblcFrDashboardTranxAmt.prefWidthProperty().bind(tblvFrDashboardTranx.widthProperty().multiply(0.2));
    }


    @FXML
    private void prevImage() {
        currentIndex = (currentIndex == 0) ? images.length - 1 : currentIndex - 1;
        updateImageView();
        updateIndicators();
    }

    @FXML
    private void nextImage() {
        currentIndex = (currentIndex == images.length - 1) ? 0 : currentIndex + 1;
        updateImageView();
        updateIndicators();
    }

    private void updateImageView() {
        imageView.setImage(images[currentIndex]);
    }

    public void prevImageSecond() {
        currentIndexSecond = (currentIndexSecond == 0) ? imagesSecond.length - 1 : currentIndexSecond - 1;
        updateImageViewSecond();
        updateIndicatorsSecond();
    }

    public void nextImageSecond() {
        currentIndexSecond = (currentIndexSecond == imagesSecond.length - 1) ? 0 : currentIndexSecond + 1;
        updateImageViewSecond();
        updateIndicatorsSecond();
    }

    private void updateImageViewSecond() {
        imageViewSecond.setImage(imagesSecond[currentIndexSecond]);
    }

    //For indicators
    private void initIndicators(int count) {
        Circle[] indicators = new Circle[count];
        indicatorsContainer.getChildren().clear();
        for (int i = 0; i < count; i++) {
            Circle indicator = new Circle(4);
            indicator.setFill(Color.WHITE);
            indicators[i] = indicator;
            indicatorsContainer.getChildren().add(indicator);
        }
    }

    private void updateIndicators() {
        Circle[] indicators = (Circle[]) indicatorsContainer.getChildren().toArray(new Circle[0]);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i].getStyleClass().clear(); // Clear existing style classes
            indicators[i].getStyleClass().add(i == currentIndex ? "selected-indicator" : "normal-indicator");
        }
    }

    private void initIndicatorsSecond(int count) {
        Circle[] indicators = new Circle[count];
        indicatorsContainerSecond.getChildren().clear();
        for (int i = 0; i < count; i++) {
            Circle indicator = new Circle(4);
            indicator.setFill(Color.WHITE);
            indicators[i] = indicator;
            indicatorsContainerSecond.getChildren().add(indicator);
        }
    }

    private void updateIndicatorsSecond() {
        Circle[] indicators = (Circle[]) indicatorsContainerSecond.getChildren().toArray(new Circle[0]);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].getStyleClass().clear();
            indicators[i].getStyleClass().add(i == currentIndexSecond ? "selected-indicator" : "normal-indicator");
        }
    }

    private void startAutoSlide() {
        autoSlideTimeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> nextImage()));
        autoSlideTimeline.setCycleCount(Timeline.INDEFINITE);
        autoSlideTimeline.play();
    }

    private void startAutoSlideSecond() {
        autoSlideTimelineSecond = new Timeline(new KeyFrame(Duration.seconds(3), event -> nextImageSecond()));
        autoSlideTimelineSecond.setCycleCount(Timeline.INDEFINITE);
        autoSlideTimelineSecond.play();
    }

    // Sale invoice List
    public void getSaleInvoiceList() {
        APIClient apiClient = null;
        try {
            tblvFrDashboardTranx.getItems().clear();
            logger.debug("Get sale invoice Data Started...");
            apiClient = new APIClient(EndPoints.GET_SALE_INVOICE_LIST, "", RequestType.GET);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    if (workerStateEvent.getSource().getValue() != null) {
                        jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                        if (jsonObject.get("responseStatus") != null && jsonObject.get("responseStatus").getAsInt() == 200) {
                            JsonArray responseArray = jsonObject.get("data").getAsJsonArray();
                            if (responseArray.size() > 0) {
                                purchaseInvoiceListDTO.clear();
                                for (JsonElement element : responseArray) {
                                    JsonObject item = element.getAsJsonObject();
                                    String index = String.valueOf(purchaseInvoiceListDTO.size() + 1);
                                    String id = item.get("id").getAsString();
                                    String SrNo = item.get("id").getAsString();
                                    String ledgerName = item.get("sundry_debtor_name").getAsString();
                                    String amount = item.get("total_amount").getAsString();
                                    String invoice_no = item.get("invoice_no").getAsString();
                                    String sundry_debtor_name = item.get("sundry_debtor_name").getAsString();
                                    String sundry_debtor_id = item.get("sundry_debtor_id").getAsString();
                                    String sale_account_name = item.get("sale_account_name").getAsString();
                                    String baseamt = item.get("baseamt").getAsString();
                                    String tax = item.get("tax").getAsString();

                                    purchaseInvoiceListDTO.add(new PurchaseInvoiceListDTO(id, SrNo, ledgerName,
                                            amount, invoice_no, sundry_debtor_name, sundry_debtor_id, sale_account_name, baseamt, tax));

                                    tblcFrDashboardTranxSrNo.setCellValueFactory(cellData -> cellData.getValue().srNoProperty());
                                    tblcFrDashboardTranxAmt.setCellValueFactory(cellData -> cellData.getValue().amountProperty());

                                    tblcFrDashboardTranxAddAndName.setCellValueFactory(cellData -> {
                                        PurchaseInvoiceListDTO dto = cellData.getValue();
                                        String combinedDetails = dto.getLedgerName() + "\n#: " + dto.getInvoice_no() + "" + "\tBase: " + dto.getBaseamt() + "\tTax: " + dto.getTax();
                                        return new SimpleStringProperty(combinedDetails);
                                    });
                                }
                                tblvFrDashboardTranx.setItems(purchaseInvoiceListDTO);
                            } else {
                                System.out.println("Error1");
                            }
                        } else {
                            System.out.println("Error2");
                        }
                    }

                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getSaleInvoiceList()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getSaleInvoiceList()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error connecting....!");
        } finally {
            apiClient = null;
        }
    }

    // Purchase invoice List
    public void getPurchaseInvoiceList() {
        APIClient apiClient = null;
        try {
            tblvFrDashboardTranx.getItems().clear();
            logger.debug("Get sale invoice Data Started...");
            apiClient = new APIClient(EndPoints.GET_PURCHASE_INVOICE_LIST, "", RequestType.GET);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    if (workerStateEvent.getSource().getValue() != null) {
                        jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                        if (jsonObject.get("responseStatus") != null && jsonObject.get("responseStatus").getAsInt() == 200) {
                            JsonArray responseArray = jsonObject.get("data").getAsJsonArray();
                            if (responseArray.size() > 0) {
                                purchaseInvoiceListDTO.clear();
                                for (JsonElement element : responseArray) {
                                    JsonObject item = element.getAsJsonObject();
                                    String index = String.valueOf(purchaseInvoiceListDTO.size() + 1);
                                    String id = item.get("id").getAsString();
                                    String SrNo = item.get("id").getAsString();
                                    String ledgerName = item.get("sundry_debtor_name").getAsString();
                                    String amount = item.get("total_amount").getAsString();
                                    String invoice_no = item.get("invoice_no").getAsString();
                                    String sundry_debtor_name = item.get("sundry_debtor_name").getAsString();
                                    String sundry_debtor_id = item.get("sundry_debtor_id").getAsString();
                                    String sale_account_name = item.get("sale_account_name").getAsString();
                                    String baseamt = item.get("baseamt").getAsString();
                                    String tax = item.get("tax").getAsString();

                                    purchaseInvoiceListDTO.add(new PurchaseInvoiceListDTO(id, SrNo, ledgerName,
                                            amount, invoice_no, sundry_debtor_name, sundry_debtor_id, sale_account_name, baseamt, tax));

                                    tblcFrDashboardTranxSrNo.setCellValueFactory(cellData -> cellData.getValue().srNoProperty());
                                    tblcFrDashboardTranxAmt.setCellValueFactory(cellData -> cellData.getValue().amountProperty());

                                    tblcFrDashboardTranxAddAndName.setCellValueFactory(cellData -> {
                                        PurchaseInvoiceListDTO dto = cellData.getValue();
                                        String combinedDetails = dto.getLedgerName() + "\n#: " + dto.getInvoice_no() + "" + "\tBase: " + dto.getBaseamt() + "\tTax: " + dto.getTax();
                                        return new SimpleStringProperty(combinedDetails);
                                    });
                                }
                                tblvFrDashboardTranx.setItems(purchaseInvoiceListDTO);
                            } else {
                                System.out.println("Error3");
                            }
                        } else {
                            System.out.println("Error4");
                        }
                    }

                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getSaleInvoiceList()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getSaleInvoiceList()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error connecting....!");
        } finally {
            apiClient = null;
        }
    }

    // Top Selling List
    public void getTopSellingList() {
        APIClient apiClient = null;
        try {
            logger.debug("Get sale invoice Data Started...");
            apiClient = new APIClient(EndPoints.GET_TOP_SELLING_LIST, "", RequestType.GET);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    if (workerStateEvent.getSource().getValue() != null) {
                        jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                        if (jsonObject.get("responseStatus") != null && jsonObject.get("responseStatus").getAsInt() == 200) {
                            JsonArray responseArray = jsonObject.get("data").getAsJsonArray();
                            System.out.println("getTopSellingList data--->" + responseArray);
                        } else {
                            System.out.println("Error");
                        }
                    }

                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getSaleInvoiceList()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getSaleInvoiceList()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error connecting....!");
        } finally {
            apiClient = null;
        }
    }

    // Least Selling List
    public void getLeastSellingList() {
        APIClient apiClient = null;
        try {
            logger.debug("Get sale invoice Data Started...");
            apiClient = new APIClient(EndPoints.GET_LEAST_SELLING_LIST, "", RequestType.GET);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    if (workerStateEvent.getSource().getValue() != null) {
                        jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                        if (jsonObject.get("responseStatus") != null && jsonObject.get("responseStatus").getAsInt() == 200) {
                            JsonArray responseArray = jsonObject.get("data").getAsJsonArray();
                            System.out.println("getLeastSellingList data--->" + responseArray);
                        } else {
                            System.out.println("Error");
                        }
                    }

                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getSaleInvoiceList()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getSaleInvoiceList()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error connecting....!");
        } finally {
            apiClient = null;
        }
    }

    // get summary cash List
    public void getSummaryList() {
        APIClient apiClient = null;
        try {
            tblvFrDashboardTranx.getItems().clear();
            logger.debug("Get sale invoice Data Started...");
            Map<String, String> map = new HashMap<>();
            map.put("flag", String.valueOf(true));
            String formData = Globals.mapToStringforFormData(map);
            apiClient = new APIClient(EndPoints.GET_SUMMARY_LIST, formData, RequestType.FORM_DATA);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    if (workerStateEvent.getSource().getValue() != null) {
                        jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                        if (jsonObject.get("responseStatus") != null && jsonObject.get("responseStatus").getAsInt() == 200) {
                            JsonArray responseArray = jsonObject.get("data").getAsJsonArray();
                            if (responseArray.size() > 0) {
                                for (JsonElement responseElement : responseArray) {
                                    JsonObject responseObject = responseElement.getAsJsonObject();
                                    JsonArray invoiceDataArray = responseObject.get("invoice_data").getAsJsonArray();

                                    // Loop through each invoice data element
                                    for (JsonElement invoiceElement : invoiceDataArray) {
                                        JsonObject invoiceObject = invoiceElement.getAsJsonObject();
//                                        String index = String.valueOf(purchaseInvoiceListDTO.size() + 1);
                                        String ledgerName = invoiceObject.get("ledger_name").getAsString();
                                        String amount = invoiceObject.get("total_amount").getAsString();
                                        String overDueDays = invoiceObject.get("overDueDays").getAsString();

                                        summaryListDTO.add(new SummaryListDTO(ledgerName,
                                                amount, overDueDays));
                                    }
                                    tblcFrDashboardLedgerName.setCellValueFactory(cellData -> cellData.getValue().ledgerNameProperty());
                                    tblcFrDashboardAmount.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
                                    tblcFrDashboardDays.setCellValueFactory(cellData -> cellData.getValue().overDueDaysProperty());

                                    tblvFrDashboardCashSummary.setItems(summaryListDTO);
                                }
                            }
                        } else {
                            System.out.println("Error");
                        }
                    } else {
                        System.out.println("Error");
                    }
                }

            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getSaleInvoiceList()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getSaleInvoiceList()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (
                Exception e) {
            e.printStackTrace();
            System.out.println("error connecting....!");
        } finally {
            apiClient = null;
        }
    }

}
