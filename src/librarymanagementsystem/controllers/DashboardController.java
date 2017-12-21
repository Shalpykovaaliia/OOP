/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import librarymanagementsystem.LibraryManagementSystem;
import librarymanagementsystem.facade.BooksFacade;
import librarymanagementsystem.facade.BorrowerJpaController;
import librarymanagementsystem.models.BookBorrower;
import librarymanagementsystem.models.BookOverdue;
import librarymanagementsystem.models.Books;
import librarymanagementsystem.models.Borrower;
import librarymanagementsystem.models.User;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

/**
 * FXML Controller class
 *
 * @author danml
 */
public class DashboardController implements Initializable {

    @FXML
    private Label currentUserName;

    @FXML
    private Hyperlink logoutLink;

    @FXML
    private JFXButton borrowerBtn;

    @FXML
    private JFXButton bookBtn;

    @FXML
    private JFXButton scanBtn;

    @FXML
    private JFXButton settingsBtn;

    @FXML
    private AnchorPane dashboardContentArea;

    @FXML
    private Label currentRole;

    @FXML
    private JFXListView<String> borrowedBooksListView;

    @FXML
    private JFXListView<String> activeBorrowersListView;

    @FXML
    private JFXListView<String> overdueBooksListView;

    @FXML
    private JFXButton borrowBooksBtn;

    @FXML
    private JFXButton returnBookButton;

    @FXML
    private JFXButton penaltyReportButton;

    private EntityManager em;

    private EntityManagerFactory emf;

    private BorrowerJpaController borrowerFacade;

    private BooksFacade booksFacade;

    @FXML
    void logOut(ActionEvent event) {
        // close current view
        LibraryManagementSystem.showView("user.login");
        // set current user to null
    }

    @FXML
    void openBookRecord(ActionEvent event) {
        LibraryManagementSystem.showView("book.manage");
    }

    @FXML
    void openBorrower(ActionEvent event) {
        LibraryManagementSystem.showView("borrower.manage");
    }

    @FXML
    void openSystemUser(ActionEvent event) {
        LibraryManagementSystem.showView("user.manage");
    }

    @FXML
    void openScanPanel(ActionEvent event) {
        LibraryManagementSystem.showView("scanner.manage");
    }

    @FXML
    void openSettings(ActionEvent event) {
        LibraryManagementSystem.showView("settings.manage");
    }

    public void updateUserLabel(User user) {
        this.currentUserName.setText(user.getFullName().toUpperCase());
        this.currentRole.setText(user.getRole().toUpperCase());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.emf = librarymanagementsystem.LibraryManagementSystem.APP_ENTITY_MANAGER_FACTORY;
        this.em = emf.createEntityManager();
        this.borrowerFacade = new BorrowerJpaController(emf);
        this.booksFacade = new BooksFacade(emf);
        // load books with issue and its borrower
        this.loadDashboardRecords();
        // load over dued books
        this.loadOverDued();
    }

    @FXML
    void maximize(MouseEvent event) {
        LibraryManagementSystem.APP_ROOT_PANE.setMaximized(true);
    }

    @FXML
    void compress(MouseEvent event) {
        LibraryManagementSystem.APP_ROOT_PANE.setMaximized(false);
    }

    @FXML
    void viewAllActiveBooks(ActionEvent event) {

    }

    @FXML
    void viewAllBorrowedBooks(ActionEvent event) {

    }

    @FXML
    void viewAllOverDueBooks(ActionEvent event) {

    }

    @FXML
    void openBorrowBookView(ActionEvent event) {
        LibraryManagementSystem.showView("book.borrow");
    }

    @FXML
    void openReturnBookView(ActionEvent event) {
        LibraryManagementSystem.showView("book.return");
    }

    @FXML
    void openPenaltyReportView(ActionEvent event) {
        LibraryManagementSystem.showView("book.penalty");
    }

    // @TODO - wrong implementation . use book borrower if date return is null and  expected return date is less date today
    private void loadOverDued() {
        TypedQuery<BookBorrower> query = em.createNamedQuery("BookBorrower.findOverduedBook", BookBorrower.class);
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        List<BookBorrower> overduedBorrowedBooks = query.getResultList();
        overdueBooksListView.getItems().clear();
        for (Iterator<BookBorrower> iterator = overduedBorrowedBooks.iterator(); iterator.hasNext();) {
            BookBorrower curOverduedBook = iterator.next();
            Books curBook = this.booksFacade.findBooks(curOverduedBook.getBookId());
            overdueBooksListView.getItems().add(curBook.getTitle());
        }
    }

    private void loadDashboardRecords() {
        // load borrowed books
        TypedQuery<BookBorrower> query = em.createNamedQuery("BookBorrower.findUnique", BookBorrower.class);
        List<BookBorrower> result = query.getResultList();
        HashSet<String> activeBooksBorrowed = new HashSet<>();
        HashSet<String> activeBorrowersName = new HashSet<>();
        for (Iterator<BookBorrower> iterator = result.iterator(); iterator.hasNext();) {
            BookBorrower next = iterator.next();
            Borrower borrower = next.getBorrowerId();
            Books foundBook = booksFacade.findBooks(next.getBookId());
            StringBuilder borrowersName = new StringBuilder();
            borrowersName.append(borrower.getFirstname())
                    .append(" ")
                    .append(borrower.getLastname());
            activeBooksBorrowed.add(foundBook.getTitle());
            activeBorrowersName.add(borrowersName.toString());
            Logger.getLogger(DashboardController.class.getName()).log(Level.INFO, "Book " + next.getBookId());
        }
        // From Set , load the items to the ListView
        borrowedBooksListView.getItems().clear();
        borrowedBooksListView.getItems().addAll(new ArrayList<>(activeBooksBorrowed));
        activeBorrowersListView.getItems().clear();
        activeBorrowersListView.getItems().addAll(new ArrayList<>(activeBorrowersName));
    }

    void updateTables() {
        // load books with issue and its borrower
        this.loadDashboardRecords();
        // load over dued books
        this.loadOverDued();
    }

}
