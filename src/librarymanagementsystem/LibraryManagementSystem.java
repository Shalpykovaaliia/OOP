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
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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

    @Override
    public void start(Stage primaryStage) {
        APP_ROOT_PANE = primaryStage;
        APP_ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("LibraryManagementSystemPU");
        APP_ENTITY_MANAGER = LibraryManagementSystem.APP_ENTITY_MANAGER_FACTORY.createEntityManager();
        APPLICATION_VIEW = new HashMap<>();
        APP_ROOT_PANE.initStyle(StageStyle.UNDECORATED);
        AnchorPane root = new AnchorPane();
        Scene defaultScene = new Scene(root);
        APP_ROOT_PANE.setScene(defaultScene);
        RECENT_PANE_HISTORY = new ArrayList<>();
        // load the views
        this.loadViews();
        //get and show the login pane
        
        
        LibraryManagementSystem.showView("user.dashboard");
        //LibraryManagementSystem.showView("borrower.manage");
        //LibraryManagementSystem.showView("user.manage");
        //LibraryManagementSystem.showView("book.manage");
        //LibraryManagementSystem.showView("settings.manage");
        //LibraryManagementSystem.showView("user.login");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static void showView(String viewName) {
        APP_ROOT_PANE.hide();
        AnchorPane returnedViewed = APPLICATION_VIEW.get(viewName);
        Scene scene = APP_ROOT_PANE.getScene();
        scene.setRoot(returnedViewed);
        APP_ROOT_PANE.setScene(scene);
        RECENT_PANE_HISTORY.add(returnedViewed);
        LibraryManagementSystem.APP_ROOT_PANE.show();
    }
    public static void fullScreen(){
        LibraryManagementSystem.APP_ROOT_PANE.setMaximized(true);
    }
    public static void undoFullScreen(){
        LibraryManagementSystem.APP_ROOT_PANE.setMaximized(false);
    }
    

    public static void back() {
        // show recent view
        if (RECENT_PANE_HISTORY.size() >= 1) {
            //pop the last element
            RECENT_PANE_HISTORY.remove(RECENT_PANE_HISTORY.size() - 1);
            AnchorPane recentView = RECENT_PANE_HISTORY.get(RECENT_PANE_HISTORY.size() - 1);
            LibraryManagementSystem.APP_ROOT_PANE.hide();
            Scene scene = APP_ROOT_PANE.getScene();
            scene.setRoot(recentView);
            APP_ROOT_PANE.setScene(scene);
            LibraryManagementSystem.APP_ROOT_PANE.show();
        }
    }

    private void loadViews() {
        HashMap<String, String> viewLocation = new HashMap<>();
        viewLocation.put("user.login", "views/user/login.fxml");
        viewLocation.put("user.dashboard", "views/dashboard/index.fxml");
        viewLocation.put("user.manage", "views/user/manage_user.fxml");
        viewLocation.put("borrower.manage", "views/borrowers/manage_borrower.fxml");
        viewLocation.put("book.manage", "views/books/manage_books.fxml");
        viewLocation.put("book.borrow", "views/books/borrow_books.fxml");
        viewLocation.put("book.return", "views/books/return_books.fxml");
        viewLocation.put("book.penalty", "views/books/penalty_book.fxml");
        viewLocation.put("settings.manage", "views/settings/index.fxml");
        viewLocation.put("scanner.manage", "views/scanner/index.fxml");

        for (Map.Entry<String, String> currentViewLocation : viewLocation.entrySet()) {
            String key = currentViewLocation.getKey();
            String value = currentViewLocation.getValue();
            try {
                AnchorPane currentView;
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(value));
                currentView = loader.load();
                LibraryManagementSystem.APPLICATION_VIEW.put(key, currentView);
            } catch (IOException ex) {
                Logger.getLogger(LibraryManagementSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
