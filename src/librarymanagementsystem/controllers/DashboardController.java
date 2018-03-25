/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import librarymanagementsystem.LibraryManagementSystem;
import librarymanagementsystem.facade.BooksFacade;
import librarymanagementsystem.facade.BorrowerJpaController;
import librarymanagementsystem.interfaces.Refreshable;
import librarymanagementsystem.models.BookBorrower;
import librarymanagementsystem.models.Books;
import librarymanagementsystem.models.Borrower;
import librarymanagementsystem.models.User;
import org.controlsfx.control.PopOver;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

/**
 * FXML Controller class
 *
 * @author danml
 */
public class DashboardController implements Initializable ,Refreshable{

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

    @FXML
    private JFXTextField overdueBooksSearchField;

    @FXML
    private JFXTextField activeBorrowerSearchField;

    @FXML
    private JFXTextField borrowedBooksSearchField;

    private EntityManager em;

    private EntityManagerFactory emf;

    private BorrowerJpaController borrowerFacade;

    private BooksFacade booksFacade;

    protected HashMap<Books, Borrower> bookToBorrowerMap;

    protected HashMap<Borrower, List<Books>> borrowerToBook;
    private ContextMenu borrowedBooksContextMenu;
    private PopOver showBorrower;
    private HashMap<Books, BookBorrower> overduedBooksMap;
    private HashSet<String> activeBooksBorrowed;
    private HashSet<String> activeBorrowersName;
    private ArrayList<String> overDuedBooksList;

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

        showBorrower = new PopOver();

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

    @FXML
    void showBorrowerOfBook(MouseEvent event) {
        if (event.getClickCount() == 2) {
            // @TODO
            // get the selected borrowed books
            String selectedBookTitle = this.borrowedBooksListView.getSelectionModel().getSelectedItem();
            Set<Books> bookKeys = (Set<Books>) this.bookToBorrowerMap.keySet();
            ArrayList<Books> bookList = new ArrayList<>(bookKeys);

            Books filteredData;
            // search the book by title
            // get the bookborrower using book id
            // open view
            filteredData = bookList.stream()
                    .filter(book -> book.getTitle().equals(selectedBookTitle))
                    .findAny()
                    .orElse(null);
            Logger.getLogger(DashboardController.class.getName()).log(Level.INFO, filteredData.toString());
            Borrower borrowerTempContainer = bookToBorrowerMap.get(filteredData);
            VBox informationContainer = new VBox();
            informationContainer.setSpacing(5);
            informationContainer.setPadding(new Insets(10));
            StringBuilder sb = new StringBuilder();
            sb.append(borrowerTempContainer.getTitle())
                    .append(" ")
                    .append(borrowerTempContainer.getFirstname())
                    .append(" ")
                    .append(borrowerTempContainer.getLastname());
            Text titleText = new Text(filteredData.getTitle());
            titleText.setFont(new Font(20));
            informationContainer.getChildren().add(titleText);
            informationContainer.getChildren().add(new Text("Borrower : " + sb.toString()));
            informationContainer.getChildren().add(new Text("Contact # : " + borrowerTempContainer.getMobileNumber()));
            showBorrower.setTitle(" ");
            showBorrower.setContentNode(informationContainer);
            showBorrower.show(borrowedBooksListView, borrowedBooksListView.getScaleX() + borrowedBooksListView.getWidth() + 406, event.getSceneY() + 38);
        }
    }

    @FXML
    void showBorrowerdBookOfBorrower(MouseEvent event) {
        if (event.getClickCount() == 2) {
            // @TODO
            // get the selected borrowed books
            // search the name by title
            // get the bookborrower using book id
            // open view
            String selectedFromActiveBorrower = activeBorrowersListView.getSelectionModel().getSelectedItem();
            Set<Borrower> borrowerToBookKey = borrowerToBook.keySet();
            ArrayList<Borrower> borrowerToBookKeyList = new ArrayList<>(borrowerToBookKey);
            Borrower filteredData = borrowerToBookKeyList.stream()
                    .filter(borrower -> (selectedFromActiveBorrower.equals(borrower.getFirstname() + " " + borrower.getLastname())))
                    .findAny()
                    .orElse(null);
            Logger.getLogger(DashboardController.class.getName()).log(Level.INFO, filteredData.toString());
            List<Books> booksAssignedToBorrower = borrowerToBook.get(filteredData);
            VBox informationContainer = new VBox();
            informationContainer.setSpacing(5);
            informationContainer.setPadding(new Insets(10));
            StringBuilder sb = new StringBuilder();
            sb.append(filteredData.getTitle())
                    .append(" ")
                    .append(filteredData.getFirstname())
                    .append(" ")
                    .append(filteredData.getLastname());
            Text titleText = new Text(sb.toString());
            titleText.setFont(new Font(20));
            informationContainer.getChildren().add(titleText);
            booksAssignedToBorrower.forEach((currentItem) -> {
                informationContainer.getChildren().add(new Text(currentItem.getTitle()));
            });
            showBorrower.setTitle(" ");
            showBorrower.setContentNode(informationContainer);
            showBorrower.show(activeBorrowersListView, activeBorrowersListView.getScaleX() + activeBorrowersListView.getWidth() + 663, event.getSceneY() + 38);

        }
    }

    @FXML
    void showCurrentOverdueBookAndBorrower(MouseEvent event) {
        if (event.getClickCount() == 2) {
            // make the overdue available in class
            String selectedOverdueBook = this.overdueBooksListView.getSelectionModel().getSelectedItem();
            Set<Books> tempKeyContainer = this.overduedBooksMap.keySet();
            Logger.getLogger(DashboardController.class.getName()).log(Level.INFO, tempKeyContainer.toString());
            Logger.getLogger(DashboardController.class.getName()).log(Level.INFO, selectedOverdueBook);
            Books filteredData = tempKeyContainer.stream()
                    .filter(currentBook -> currentBook.getTitle().equals(selectedOverdueBook))
                    .findAny()
                    .orElse(null);
            Logger.getLogger(DashboardController.class.getName()).log(Level.INFO, filteredData.toString());

            BookBorrower tempBookBorrower = this.overduedBooksMap.get(filteredData);

            Books tempBooksContainer = tempBookBorrower.getBook();
            Borrower tempBorrowerContainer = tempBookBorrower.getBorrower();

            VBox informationContainer = new VBox();
            informationContainer.setSpacing(5);
            informationContainer.setPadding(new Insets(10));

            Text borrowerText = new Text("Borrower : " + tempBorrowerContainer.getTitle() + " " + tempBorrowerContainer.getFirstname() + " " + tempBorrowerContainer.getLastname());
            Text bookText = new Text("Book : " + tempBooksContainer.getTitle());
            Text computerFeeText = new Text("Penalty : P " + tempBookBorrower.getComputerFee() + ".00");
            Text daysOverdueText = new Text("Overdue : " + tempBookBorrower.getOverDueDays() + " day(s)");
            Text expectedReturnDate = new Text("Return Date : " + new SimpleDateFormat("EEE, MMM d, yyyy").format(tempBookBorrower.getExpectedReturnDate()));
            informationContainer.getChildren().add(borrowerText);
            informationContainer.getChildren().add(bookText);
            informationContainer.getChildren().add(computerFeeText);
            informationContainer.getChildren().add(daysOverdueText);
            informationContainer.getChildren().add(expectedReturnDate);
            showBorrower.setTitle(" ");
            showBorrower.setContentNode(informationContainer);
            showBorrower.show(overdueBooksListView, overdueBooksListView.getScaleX() + overdueBooksListView.getWidth() + 920, event.getSceneY() + 38);

            // on click , show the overdue information . and show how much to pay on that overdue record
            // show the book and the and the borrower
        }
    }

    @FXML
    void borrowedBooksFilterResult(ActionEvent event) {
        //@TODO
        //this.activeBooksBorrowed
        String filterString = this.borrowedBooksSearchField.getText();
        if (filterString.isEmpty()) {
            borrowedBooksListView.getItems().clear();
            borrowedBooksListView.getItems().addAll(new ArrayList<>(activeBooksBorrowed));
        } else {
            // filter the observable data of 
            List<String> filteredData = borrowedBooksListView.getItems().stream()
                    .filter(currentItem -> currentItem.toLowerCase().contains(filterString.toLowerCase()))
                    .collect(Collectors.toList());

            borrowedBooksListView.getItems().clear();
            borrowedBooksListView.getItems().addAll(filteredData);
        }
    }

    @FXML
    void activeBorrowerFilterResult(ActionEvent event) {
        //@TODO
        //this.activeBorrowersName
        String filterString = this.activeBorrowerSearchField.getText();
        if (filterString.isEmpty()) {
            activeBorrowersListView.getItems().clear();
            activeBorrowersListView.getItems().addAll(new ArrayList<>(activeBorrowersName));
        } else {
            List<String> filteredData = activeBorrowersListView.getItems().stream()
                    .filter(currentItem -> currentItem.toLowerCase().contains(filterString.toLowerCase()))
                    .collect(Collectors.toList());
            activeBorrowersListView.getItems().clear();
            activeBorrowersListView.getItems().addAll(filteredData);
        }
    }

    @FXML
    void overdueBooksFilterResult(ActionEvent event) {
        //@TODO
        String filterString = this.overdueBooksSearchField.getText();
        if (filterString.isEmpty()) {
            this.overdueBooksListView.getItems().clear();
            this.overdueBooksListView.getItems().addAll(overDuedBooksList);
        } else {
            List<String> filteredData = overdueBooksListView.getItems().stream()
                    .filter(currentItem -> currentItem.toLowerCase().contains(filterString.toLowerCase()))
                    .collect(Collectors.toList());
            overdueBooksListView.getItems().clear();
            overdueBooksListView.getItems().addAll(filteredData);

        }
    }

    private void loadOverDued() {
        this.overduedBooksMap = new HashMap<>();
        this.overDuedBooksList = new ArrayList<>();
        TypedQuery<BookBorrower> query = em.createNamedQuery("BookBorrower.findOverduedBook", BookBorrower.class);
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        List<BookBorrower> overduedBorrowedBooks = query.getResultList();
        overdueBooksListView.getItems().clear();
        for (Iterator<BookBorrower> iterator = overduedBorrowedBooks.iterator(); iterator.hasNext();) {
            BookBorrower curOverduedBook = iterator.next();
            Books curBook = curOverduedBook.getBook();
            this.overDuedBooksList.add(curBook.getTitle());
            this.overduedBooksMap.put(curBook, curOverduedBook);
        }
        overdueBooksListView.getItems().clear();
        overdueBooksListView.getItems().addAll(overDuedBooksList);
    }

    private void loadDashboardRecords() {
        // load borrowed books
        TypedQuery<BookBorrower> query = em.createNamedQuery("BookBorrower.findUnique", BookBorrower.class);
        List<BookBorrower> result = query.getResultList();
        bookToBorrowerMap = new HashMap<>();
        borrowerToBook = new HashMap<>();

        this.activeBooksBorrowed = new HashSet<>();
        this.activeBorrowersName = new HashSet<>();
        for (Iterator<BookBorrower> iterator = result.iterator(); iterator.hasNext();) {
            BookBorrower next = iterator.next();
            Borrower borrower = next.getBorrower();
            Books foundBook = next.getBook();
            StringBuilder borrowersName = new StringBuilder();
            borrowersName.append(borrower.getFirstname())
                    .append(" ")
                    .append(borrower.getLastname());
            activeBooksBorrowed.add(foundBook.getTitle());
            activeBorrowersName.add(borrowersName.toString());
            //Register the borrower to books map
            if (bookToBorrowerMap.get(foundBook) == null) {
                bookToBorrowerMap.put(foundBook, borrower);
            }
            // Register the books to borrower map
            if (borrowerToBook.get(borrower) != null) {
                borrowerToBook.get(borrower).add(foundBook);
            } else {
                ArrayList<Books> initialBook = new ArrayList<>();
                initialBook.add(foundBook);
                borrowerToBook.put(borrower, initialBook);
            }

            Logger.getLogger(DashboardController.class.getName()).log(Level.INFO, "Book " + next.getBook());
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

    @FXML
    void aboutUsButton(ActionEvent event) {
        LibraryManagementSystem.showView("about.index");
    }

    @Override
    public void refresh() {
        this.updateTables();
    }

}
