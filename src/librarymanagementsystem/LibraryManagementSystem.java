/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import librarymanagementsystem.components.OverdueSmsNotifierServiceWorker;
import librarymanagementsystem.components.SemaphoreSMSSender;
import librarymanagementsystem.components.SettingsRetriever;
import librarymanagementsystem.facade.SmsNotificationLogFacade;

import librarymanagementsystem.models.User;

/**
 * @author User
 */
public class LibraryManagementSystem extends Application {

    public static User CURRENT_USER = null;
    public static EntityManagerFactory APP_ENTITY_MANAGER_FACTORY = null;
    public static EntityManager APP_ENTITY_MANAGER = null;
    public static HashMap<String, AnchorPane> APPLICATION_VIEW = null;
    public static Stage APP_ROOT_PANE = null;
    public static ArrayList<AnchorPane> RECENT_PANE_HISTORY = null;
    public static FXMLLoader FXMLViewLoader;
    public static HashMap<AnchorPane, Initializable> ControllerCollection;
    public static float PENALTY_PER_DAY = 2;// two pesos
    public static String API_CODE = "";
    public static String SMS_NOTIFICATION_STATUS = "";
    public static String SMS_SENDER_NAME = "";

    @Override
    public void start(Stage primaryStage) {
        APP_ROOT_PANE = primaryStage;
        APP_ROOT_PANE.initStyle(StageStyle.UNDECORATED);
        LibraryManagementSystem.ControllerCollection = new HashMap<>();
        AnchorPane root = new AnchorPane();
        Scene defaultScene = new Scene(root);
        APP_ROOT_PANE.setScene(defaultScene);
        RECENT_PANE_HISTORY = new ArrayList<>();
        //load application settings 
        this.loadApplicationSettings();
        // load the views
        this.loadViews();
        //get and show the login pane
        LibraryManagementSystem.showView("user.login");
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                OverdueSmsNotifierServiceWorker overdueWorker = new OverdueSmsNotifierServiceWorker();
                overdueWorker.setSmsNotifFacade(new SmsNotificationLogFacade(APP_ENTITY_MANAGER_FACTORY));
                //overdueWorker.setSmsSender(new TestSmsSender());// @TODO - change this to SemaPhoneSmsSender
                overdueWorker.setSmsSender(new SemaphoreSMSSender());// @TODO - change this to SemaPhoneSmsSender
                overdueWorker.start();
            }
        });

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        APP_ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("LibraryManagementSystemPU");
        APP_ENTITY_MANAGER = LibraryManagementSystem.APP_ENTITY_MANAGER_FACTORY.createEntityManager();
        APPLICATION_VIEW = new HashMap<>();
        launch(args);
    }

    public static AnchorPane showView(String viewName) {
        APP_ROOT_PANE.hide();
        AnchorPane returnedViewed = APPLICATION_VIEW.get(viewName);
        Scene scene = APP_ROOT_PANE.getScene();
        scene.setRoot(returnedViewed);
        APP_ROOT_PANE.setScene(scene);
        RECENT_PANE_HISTORY.add(returnedViewed);
        LibraryManagementSystem.APP_ROOT_PANE.show();
        return returnedViewed;
    }

    public static void fullScreen() {
        LibraryManagementSystem.APP_ROOT_PANE.setMaximized(true);
    }

    public static void undoFullScreen() {
        LibraryManagementSystem.APP_ROOT_PANE.setMaximized(false);
    }

    public static AnchorPane back() {
        AnchorPane recentView = null;
        // show recent view
        if (RECENT_PANE_HISTORY.size() >= 1) {
            //pop the last element
            RECENT_PANE_HISTORY.remove(RECENT_PANE_HISTORY.size() - 1);
            recentView = RECENT_PANE_HISTORY.get(RECENT_PANE_HISTORY.size() - 1);
            LibraryManagementSystem.APP_ROOT_PANE.hide();
            Scene scene = APP_ROOT_PANE.getScene();
            scene.setRoot(recentView);
            APP_ROOT_PANE.setScene(scene);
            LibraryManagementSystem.APP_ROOT_PANE.show();
        }
        return recentView;
    }

    private void loadViews() {
        HashMap<String, String> viewLocation = new HashMap<>();
        LibraryManagementSystem.FXMLViewLoader = new FXMLLoader();

        viewLocation.put("user.login", "views/user/login.fxml");
        viewLocation.put("user.dashboard", "views/dashboard/index.fxml");
        viewLocation.put("user.manage", "views/user/manage_user.fxml");
        viewLocation.put("borrower.manage", "views/borrowers/manage_borrower.fxml");
        viewLocation.put("book.manage", "views/books/manage_books.fxml");
        viewLocation.put("book.borrow", "views/books/borrow_books.fxml");
        viewLocation.put("book.return", "views/books/return_book.fxml");
        viewLocation.put("book.penalty", "views/books/penalty_book.fxml");
        viewLocation.put("settings.manage", "views/settings/index.fxml");
        viewLocation.put("scanner.manage", "views/scanner/index.fxml");

        for (Map.Entry<String, String> currentViewLocation : viewLocation.entrySet()) {
            String key = currentViewLocation.getKey();
            String value = currentViewLocation.getValue();
            LibraryManagementSystem.FXMLViewLoader = new FXMLLoader();
            try {
                AnchorPane currentView;
                LibraryManagementSystem.FXMLViewLoader.setLocation(getClass().getResource(value));
                currentView = LibraryManagementSystem.FXMLViewLoader.load();
                Initializable currentController = LibraryManagementSystem.FXMLViewLoader.getController();
                LibraryManagementSystem.ControllerCollection.put(currentView, currentController);
                LibraryManagementSystem.APPLICATION_VIEW.put(key, currentView);
            } catch (IOException ex) {
                Logger.getLogger(LibraryManagementSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void loadApplicationSettings() {
        // retrieve from database the apicode
        SettingsRetriever settingsRetriever = new SettingsRetriever();
        LibraryManagementSystem.API_CODE = settingsRetriever.getApiCode();
        LibraryManagementSystem.SMS_NOTIFICATION_STATUS = settingsRetriever.getNotificationStatus();
        LibraryManagementSystem.SMS_SENDER_NAME = settingsRetriever.getSmsSenderName();
        // done
    }

}
