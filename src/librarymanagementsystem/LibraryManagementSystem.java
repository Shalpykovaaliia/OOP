/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import librarymanagementsystem.components.ClickSendSMSSender;
import librarymanagementsystem.components.OverdueRecordReport;
import librarymanagementsystem.components.SemaphoreSMSSender;
import librarymanagementsystem.components.SettingsRetriever;
import librarymanagementsystem.components.SmsSenderInterface;
import librarymanagementsystem.exceptions.FailedToSendSMSException;
import librarymanagementsystem.models.BookBorrower;
import librarymanagementsystem.models.Borrower;

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
    public static String API_CODE = null;
    public static Boolean IS_SMS_NOTIFICATION_ENABLED = false;
    public static String SMS_SENDER_NAME = "J.A.R.S";

    @Override
    public void start(Stage primaryStage) {
        APP_ROOT_PANE = primaryStage;
        APP_ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("LibraryManagementSystemPU");
        APP_ENTITY_MANAGER = LibraryManagementSystem.APP_ENTITY_MANAGER_FACTORY.createEntityManager();
        APPLICATION_VIEW = new HashMap<>();
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
        this.runOverdueSmsNotifier();
        //get and show the login pane
        LibraryManagementSystem.showView("user.login");
        //LibraryManagementSystem.showView("user.dashboard");
        //LibraryManagementSystem.showView("borrower.manage");
        //LibraryManagementSystem.showView("user.manage");
        //LibraryManagementSystem.showView("book.manage");
        //LibraryManagementSystem.showView("settings.manage");
        //LibraryManagementSystem.showView("book.return");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
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

    // @TODO
    private void runOverdueSmsNotifier() {
        OverdueRecordReport overDueReport = new OverdueRecordReport(LibraryManagementSystem.APP_ENTITY_MANAGER, LibraryManagementSystem.APP_ENTITY_MANAGER_FACTORY);
        List<BookBorrower> overduedBooks = overDueReport.getOverdueReport();
        for (Iterator<BookBorrower> iterator = overduedBooks.iterator(); iterator.hasNext();) {
            BookBorrower curBookBorrower = iterator.next();
            Borrower currentBorrower = curBookBorrower.getBorrowerId();
            // sms notification not yet sent
            if (!smsNotificationSent(curBookBorrower)) {
                // send the notification
                SmsSenderInterface smssender = new SemaphoreSMSSender();
                smssender.setMessage("Good day borrower. The book/s that you borrowed has reached its overdue date. Please return the books asap. ");
                //mobile number is not null or not empty
                if (currentBorrower.getMobileNumber() != null || !currentBorrower.getMobileNumber().equals("")) {
                    if (LibraryManagementSystem.IS_SMS_NOTIFICATION_ENABLED) {
                        try {
                            smssender.setRecipient(currentBorrower.getMobileNumber());
                            Logger.getLogger(LibraryManagementSystem.class.getName()).log(Level.INFO, "About to send sms notification to " + currentBorrower.getMobileNumber());
                            smssender.sendSms();
                            Logger.getLogger(LibraryManagementSystem.class.getName()).log(Level.INFO, "Sms notification sent to " + currentBorrower.getMobileNumber());
                        } catch (FailedToSendSMSException ex) {
                            Logger.getLogger(LibraryManagementSystem.class.getName()).log(Level.INFO, "Can't notify user via sms");
                            Logger.getLogger(LibraryManagementSystem.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    Logger.getLogger(LibraryManagementSystem.class.getName()).log(Level.INFO, "This book borrower record doesnt have contact information. #" + curBookBorrower.getId());
                }

            }
        }
    }

    // @TODO - checks the tbl_sms_notification log
    private boolean smsNotificationSent(BookBorrower curBookBorrower) {
        return true;
    }

    // @TODO 
    private void loadApplicationSettings() {
        // retrieve from database the apicode
        LibraryManagementSystem.API_CODE = SettingsRetriever.getApiCode();
        LibraryManagementSystem.IS_SMS_NOTIFICATION_ENABLED = SettingsRetriever.isSmsNotificationEnable();
        LibraryManagementSystem.SMS_SENDER_NAME = SettingsRetriever.getSmsSenderName();
        // done
    }

}
