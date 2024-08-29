package com.opethic.genivis.controller.users;

import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.dto.FranchiseDTO;
import com.opethic.genivis.multitabs.TabDataDto;
import com.opethic.genivis.utils.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.ErrorManager;

import static com.opethic.genivis.utils.FxmFileConstants.*;
import static com.opethic.genivis.utils.FxmFileConstants.COMPANY_ADMIN_SLUG;
import static com.opethic.genivis.utils.Globals.*;


public class SadminController implements Initializable {

    @FXML
    public BorderPane bpSadminRoot;
    @FXML
    private MenuBar mbSadmin;
    @FXML
    ImageView ivLogo;

    @FXML
    private Label lblUserName;
    @FXML
    private MenuItem changePassword;

    @FXML
    private Label headerDateandTime;


    private List<TabDataDto> tabDataDtoList = new ArrayList<>();
    private GlobalSadminController globalController;
    private static final Logger sadminLogger = LogManager.getLogger(SadminController.class);
//    private TabPane tabPane;


    public void restartApplication() {
        // Get the current stage
        Stage currentStage = (Stage) bpSadminRoot.getScene().getWindow();

        // Close the current stage
        currentStage.close();

        // Start a new instance of the application
        try {
            new GenivisApplication().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace(); // Handle Exception appropriately
        }
    }

    public void handleLogout(ActionEvent event) {
        restartApplication();
    }

    private void loadUserName() {
        lblUserName.setText(GlobalTranx.getUserName() + "");
    }

    private void updateDateTimeLabel() {
        // Get current date and time
        LocalDateTime now = LocalDateTime.now();

        // Format the date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy, hh:mm:ss a");
        String formattedDateTime = now.format(formatter);

        // Update label text
        headerDateandTime.setText(formattedDateTime);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        changePassword.setOnAction(event -> addNewTab(CHANGE_PASSWORD_SLUG));

        globalController = GlobalSadminController.getInstance();
        globalController.setController(this);

        loadUserName();


        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> updateDateTimeLabel())
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // Update label initially
        updateDateTimeLabel();


//        if(globalController==null){
//            System.out.println("GC Null");
//        }else{
//            System.out.println("GC Non Null");
//        }
        tabPane = new TabPane();
        //tabPane.getTabs().add(new Tab("Dashboard"));
        lstTabs();
        bpSadminRoot.setCenter(tabPane);


        Menu masterMenu = new Menu();
        masterMenu.setText("_Master");
        Image image = new Image(GenivisApplication.class.getResource("ui/assets/master.png").toString());
        ImageView imageView = new ImageView();
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        imageView.setPreserveRatio(true);
        imageView.setImage(image);
        masterMenu.setGraphic(imageView);
        masterMenu.setMnemonicParsing(true);

        MenuItem companyMenuItem = new MenuItem();
        companyMenuItem.setText("Company");

        MenuItem roleMenuItem = new MenuItem();
        roleMenuItem.setText("Role");

        MenuItem companyAdminMenuItem = new MenuItem();
        companyAdminMenuItem.setText("Company Admin");

        masterMenu.getItems().addAll(companyMenuItem, roleMenuItem, companyAdminMenuItem);


        mbSadmin.getMenus().add(masterMenu);

        ImageView ivCompanyIcon = null;
        try {
            ivCompanyIcon = new ImageView(GenivisApplication.class.getResource("ui/assets/login_lock.png").toURI().toString());
            ivCompanyIcon.setPreserveRatio(true);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
//        System.out.println("URL"+GenivisApplication.class.getResource("ui/assets/login_lock.png").getPath());
        ivCompanyIcon.setFitWidth(10);
        ivCompanyIcon.setFitHeight(10);

//        roleMenuItem.setGraphic(ivCompanyIcon);
//        masterMenu.setGraphic(ivCompanyIcon);

        KeyCombination gotoKeyCombination = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);
        KeyCombination gotoKeyCombination1 = new KeyCodeCombination(KeyCode.C, KeyCombination.ALT_DOWN);
        roleMenuItem.setAccelerator(gotoKeyCombination);
        companyMenuItem.setAccelerator(gotoKeyCombination1);


        companyMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                /*try {
                    Parent loader = FXMLLoader.load(GenivisApplication.class.getResource("ui/master/companyList.fxml"));

                    sadminMenuTabPen.getTabs().add(companyTab);
                    companyTab.setContent(loader);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }*/
                //sadminMenuTabPen.getTabs().remove(sadminMenuDashboardTab);
                addNewTab(COMPANY_LIST_SLUG, true);
//                addNewTab(COMPANY_CREATE_SLUG);
//                defaultSelection(sadminMenuTabPen, companyTab);
            }
        });
        roleMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                /*try {
                    Parent loader = FXMLLoader.load(GenivisApplication.class.getResource("ui/users/user-role-list.fxml"));
                    sadminMenuTabPen.getTabs().add(roleTab);
                    roleTab.setContent(loader);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //sadminMenuTabPen.getTabs().remove(sadminMenuDashboardTab);
                defaultSelection(sadminMenuTabPen, roleTab);*/

                addNewTab(ROLE_LIST_SLUG, true);

            }

        });

        companyAdminMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                /*try {
                    Parent loader = FXMLLoader.load(GenivisApplication.class.getResource("ui/master/companyAdmin.fxml"));
                    sadminMenuTabPen.getTabs().add(companyAdminTab);
                    companyAdminTab.setContent(loader);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
//                sadminMenuTabPen.getTabs().remove(sadminMenuDashboardTab);
                defaultSelection(sadminMenuTabPen, companyAdminTab);*/
                addNewTab(COMPANY_ADMIN_SLUG);
            }

        });

    }

    public void GCSlug(String slg, Boolean fl) {
        addNewTab(slg, fl);
    }

    public void GCSlugWithFrParam(String slg, Boolean fl, FranchiseDTO franchiseDTO) {
        //addNewTab(slg, fl, franchiseDTO);
    }

    public void GCSlugWithParam(String slg, Boolean fl, Integer id) {
        addNewTab(slg, fl, id);
    }

    public void GCSlug1(String slg, Boolean fl, Integer id) {
//        addNewTab(slg, fl, id);
    }

    public void dashboardPageLoad(MouseEvent mouseEvent) {
        addNewTab(DASHBOARD_SLUG);
    }

    private void addNewTab(String slug) {
        addNewTab(slug, false);
    }

    private void addNewTab(String slug, boolean confirmation) {
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
                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to close the tab " + tabPane.getSelectionModel().getSelectedItem().getText() + "?", callback);
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
            sadminLogger.error("Exception in addTab():" + Globals.getExceptionString(e));
        }
    }

    private void addTab(TabDataDto findDao, boolean confirmation) {

        //boolean isTabReplaced = findAndReplaceTab(findDao,confirmation);


        FXMLLoader nLoader = null;
        Parent nRoot = null;
        try {
            // loader = FXMLLoader.load(GenivisApplication.class.getResource(findDao.getFileName()));
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

    private void lstTabs() {
        tabDataDtoList.add(new TabDataDto(CHANGE_PASSWORD_TITLE, CHANGE_PASSWORD_FXML, CHANGE_PASSWORD_SLUG, Arrays.asList(CHANGE_PASSWORD_SLUG), CHANGE_PASSWORD_CONTROLLER));
//        tabDataDtoList.add(new TabDataDto("Ledger List", "ui/master/ledger/ledgerlist.fxml", "ledger-list", Arrays.asList("ledger-list", "ledger-create", "ledger-edit"), " com.opethic.genivis.controller.master.ledger.LedgerListController"));
        tabDataDtoList.add(new TabDataDto("Company List", COMPANY_LIST_FXML, COMPANY_LIST_SLUG, Arrays.asList(COMPANY_LIST_SLUG, COMPANY_CREATE_SLUG, COMPANY_EDIT_SLUG), COMPANY_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Company Create", COMPANY_CREATE_FXML, COMPANY_CREATE_SLUG, Arrays.asList(COMPANY_LIST_SLUG, COMPANY_CREATE_SLUG, COMPANY_EDIT_SLUG), COMPANY_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Company Update", COMPANY_CREATE_FXML, COMPANY_EDIT_SLUG, Arrays.asList(COMPANY_LIST_SLUG, COMPANY_CREATE_SLUG, COMPANY_EDIT_SLUG), COMPANY_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Role List", ROLE_LIST_FXML, ROLE_LIST_SLUG, Arrays.asList(ROLE_LIST_SLUG, ROLE_CREATE_SLUG, ROLE_UPDATE_SLUG), ROLE_LIST_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Role Create", ROLE_CREATE_FXML, ROLE_CREATE_SLUG, Arrays.asList(ROLE_CREATE_SLUG, ROLE_LIST_SLUG, ROLE_UPDATE_SLUG), ROLE_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Role Update", ROLE_CREATE_FXML, ROLE_UPDATE_SLUG, Arrays.asList(ROLE_UPDATE_SLUG, ROLE_CREATE_SLUG, ROLE_LIST_SLUG), ROLE_CREATE_CONTROLLER));
        tabDataDtoList.add(new TabDataDto("Company Admin", COMPANY_ADMIN_FXML, COMPANY_ADMIN_SLUG, Arrays.asList(COMPANY_ADMIN_SLUG), COMPANY_ADMIN_CONTROLLER));
    }


}
