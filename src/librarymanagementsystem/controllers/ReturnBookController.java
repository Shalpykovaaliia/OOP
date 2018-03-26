/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.controllers;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import librarymanagementsystem.LibraryManagementSystem;
import librarymanagementsystem.beans.BookBorrowedBean;
import librarymanagementsystem.components.SqlDateToLocalDate;
import librarymanagementsystem.facade.BookBorrowerJpaController;
import librarymanagementsystem.facade.BookOverdueJpaController;
import librarymanagementsystem.facade.BooksFacade;
import librarymanagementsystem.facade.exceptions.NonexistentEntityException;
import librarymanagementsystem.interfaces.Refreshable;
import librarymanagementsystem.models.BookBorrower;
import librarymanagementsystem.models.BookOverdue;
import librarymanagementsystem.models.Books;
import librarymanagementsystem.models.Borrower;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ReturnBookController implements Initializable ,Refreshable{

    @FXML
    private TableView<BookBorrowedBean> booksBorrowedTable;

    @FXML
    private TableColumn<BookBorrowedBean, String> titleColumn;

    @FXML
    private TableColumn<BookBorrowedBean, String> overdueColumn;

    @FXML
    private Text overDuedaysField;

    @FXML
    private Text overdueTime;

    @FXML
    private Text penaltyPerDay;

    @FXML
    private Text dateToday;

    @FXML
    private Text totalPenalty;

    @FXML
    private JFXTextField borrowerNameField;

    @FXML
    private Text borrowerName;

    @FXML
    private Text borrowerContactInformation;

    private EntityManager em;
    private EntityManagerFactory emf;
    private ContextMenu booksBorrowedContextMenu;
    private BookBorrowerJpaController bookBorrowerJpaController;
    private BooksFacade bookFacade;
    private Borrower currentBorrowerSelected;
    private List<BookBorrower> currentBooksBorrowed;
    private BookOverdueJpaController bookOverDueFacade;

    @FXML
    void returnBack(ActionEvent event) {
        clearFields();
        //LibraryManagementSystem.ControllerCollection.get
        AnchorPane curView = LibraryManagementSystem.back();
        DashboardController dashboardController = (DashboardController) LibraryManagementSystem.ControllerCollection.get(curView);
        dashboardController.updateTables();
    }

    @FXML
    void returnBook(ActionEvent event) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Return book");
        confirmationAlert.setHeaderText("Return books borrowed");
        confirmationAlert.setContentText("Are you want to proceed?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.get() == ButtonType.OK) {
            // check if the book borrow record is overdued using borrowedBooks
            for (Iterator<BookBorrower> iterator = currentBooksBorrowed.iterator(); iterator.hasNext();) {
                BookBorrower curBookBorrowed = iterator.next();
                payOverDueFees(curBookBorrowed);
                // set date of return
                curBookBorrowed.setDateReturned(new Date());
                setToAvailable(curBookBorrowed.getBook());
                try {
                    //update the the Book Borrowed
                    this.bookBorrowerJpaController.edit(curBookBorrowed);
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(ReturnBookController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(ReturnBookController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText("Books returned");
            successAlert.setContentText("Thank you for using J.A.R.S Library System. Please come again.");
            successAlert.showAndWait();
            // clear fields 
            clearFields();
            // done
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.emf = librarymanagementsystem.LibraryManagementSystem.APP_ENTITY_MANAGER_FACTORY;
        this.em = emf.createEntityManager();
        this.bookBorrowerJpaController = new BookBorrowerJpaController(emf);
        this.bookFacade = new BooksFacade(emf);
        this.bookOverDueFacade = new BookOverdueJpaController(emf);
        // Load names of borrowed in borrowerNameField
        this.setTodaysDate();
        this.initializeBooksBorrowedTable();
        this.refresh();
    }

    @FXML
    void findBorrowerbyId(ActionEvent event) {
        int borrowerBarcodeId = Integer.parseInt(borrowerNameField.getText());
        this.setSelectedBorrower(borrowerBarcodeId);
    }

    private Boolean hasBorrowedBooks(Borrower foundBorrower) {
        // check if this borrower did borrowed book
        TypedQuery<BookBorrower> query = em.createNamedQuery("BookBorrower.hasDateReturned", BookBorrower.class);
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        Borrower borrower = new Borrower(foundBorrower.getBorrowerId());
        query.setParameter("borrowerId", borrower);
        try {
            if (query.getResultList().size() > 0) {
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
        // get book borrower record that has no date return
        return false;
    }

    private void setSelectedBorrower(int borrowerBarcodeId) {
        // search the database for borrower having this borrowersFullName 
        TypedQuery<Borrower> findBorrower = em.createNamedQuery("Borrower.findByBarcode", Borrower.class);
        findBorrower.setHint("javax.persistence.cache.storeMode", "REFRESH");
        findBorrower.setHint(QueryHints.REFRESH, HintValues.TRUE);
        findBorrower.setParameter("borrowerBarcode", borrowerBarcodeId);
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        try {
            Borrower foundBorrower = findBorrower.getSingleResult();
            if (!hasBorrowedBooks(foundBorrower)) {
                Alert userNotify = new Alert(Alert.AlertType.ERROR);
                userNotify.setTitle("No books");
                userNotify.setHeaderText("No borrowed books");
                userNotify.setHeaderText("This didn't borrowed any books.");
            } else {
                StringBuilder fullNameStr = new StringBuilder();
                fullNameStr.append(foundBorrower.getTitle())
                        .append(" ")
                        .append(foundBorrower.getFirstname())
                        .append(" ")
                        .append(foundBorrower.getLastname());
                borrowerName.setText(fullNameStr.toString());
                borrowerContactInformation.setText(foundBorrower.getMobileNumber());
                this.currentBorrowerSelected = foundBorrower;
                loadBorrowedBooks(foundBorrower);
                calculateTotalPenaltyFee();
                calculateOverDueDays();
            }
        } catch (javax.persistence.NoResultException e) {
            errorAlert.setTitle("No result");
            errorAlert.setContentText("Cant find borrower information");
        }
    }

    private void loadBorrowedBooks(Borrower borrower) {
        //booksBorrowedTable
        TypedQuery<BookBorrower> findBorrowedBooksQuery = em.createNamedQuery("BookBorrower.findBooksBorrowed", BookBorrower.class);
        findBorrowedBooksQuery.setHint("javax.persistence.cache.storeMode", "REFRESH");
        findBorrowedBooksQuery.setHint(QueryHints.REFRESH, HintValues.TRUE);
        findBorrowedBooksQuery.setParameter("borrowerId", borrower);
        List<BookBorrower> booksBorrowered = findBorrowedBooksQuery.getResultList();
        this.currentBooksBorrowed = booksBorrowered;
        booksBorrowedTable.getItems().clear();
        if (!booksBorrowered.isEmpty()) {
            for (BookBorrower next : booksBorrowered) {
                BookBorrowedBean bookBorrowedBean = new BookBorrowedBean();
                Books foundBook = next.getBook();
                bookBorrowedBean.setBookId(foundBook.getBookId());
                bookBorrowedBean.setBookTitle(foundBook.getTitle());
                bookBorrowedBean.setBookOverDueTime("" + (getOverDueDay(next)) + " day(s)");
                booksBorrowedTable.getItems().add(bookBorrowedBean);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No borrowed books");
            alert.setHeaderText("No borrowerd books");
            alert.setContentText("This borrower doesnt borrowed any book.");
            alert.showAndWait();
        }

    }

    private boolean hasNoOverDueRecord(Integer bookBorrowerId) {
        BookBorrower bookBorrower = new BookBorrower(bookBorrowerId);
        TypedQuery<BookOverdue> query = em.createNamedQuery("BookOverdue.hasOverDueRecord", BookOverdue.class);
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        query.setParameter("bookBorrowerId", bookBorrower);
        try {
            return (query.getResultList().size() > 0);
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occured");
            alert.setContentText("We met some error along the way. " + ex.getMessage());
            alert.showAndWait();
            Logger.getLogger(ReturnBookController.class.getName()).log(Level.SEVERE, ex.getMessage());
            return false;
        }

    }

    private void initializeBooksBorrowedTable() {
        try {
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
            overdueColumn.setCellValueFactory(new PropertyValueFactory<>("bookOverdueTime"));
        } catch (NullPointerException e) {
        }
    }

    private void clearFields() {
        // clear borrowers information
        borrowerName.setText("Name");
        borrowerContactInformation.setText("Contact #");
        borrowerNameField.setText("");
        // clear table
        booksBorrowedTable.getItems().clear();
        // clear over due fields calculation
        overDuedaysField.setText("0 day(s)");
        totalPenalty.setText("##### # , ####");

    }

    private void setTodaysDate() {
        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("MMM d yyyy");
        LocalDate dtToday = LocalDate.now();
        String todayStr = dtToday.format(dtFormatter);
        this.dateToday.setText(todayStr);
    }

    private void calculateTotalPenaltyFee() {
        double totalOverDueFee = 0;
        // get the current book borrow collection
        for (Iterator<BookBorrower> iterator = currentBooksBorrowed.iterator(); iterator.hasNext();) {
            // iterate through the collection 
            BookBorrower curBookBorrower = iterator.next();
            totalOverDueFee += getOverDueFee(curBookBorrower);
        }
        // set the calculation label
        totalPenalty.setText(totalOverDueFee+"");
    }

    private double getOverDueFee(BookBorrower curBookBorrower) {
        // get the day difference 
        LocalDate tempExpectedReturnDateContainer = SqlDateToLocalDate.convert(curBookBorrower.getExpectedReturnDate());
        double dayDifference = (double)ChronoUnit.DAYS.between(tempExpectedReturnDateContainer, LocalDate.now());
        if (dayDifference > 0) {
            return dayDifference * librarymanagementsystem.LibraryManagementSystem.BOOK_PENALTY_PER_DAY;
        } else {
            return 0;
        }
    }

    private long getOverDueDay(BookBorrower bookBorrower) {
        LocalDate tempExpectedReturnDateContainer = SqlDateToLocalDate.convert(bookBorrower.getExpectedReturnDate());
        long dateDifference = ChronoUnit.DAYS.between(tempExpectedReturnDateContainer, LocalDate.now());
        if (dateDifference < 0) {
            dateDifference = 0;
        }
        return dateDifference;
    }

    private void calculateOverDueDays() {
        // get the first book borrowed
        long dateDifference = 0;
        BookBorrower curBookBorrower = this.currentBooksBorrowed.get(0);
        if (curBookBorrower != null) {
            dateDifference = getOverDueDay(curBookBorrower);
        }
        overDuedaysField.setText(dateDifference + " day(s)");
    }


    /**
     * Create an overdue record for this book borrower record.
     *
     * @param curBookBorrowed
     */
    private void payOverDueFees(BookBorrower curBookBorrowed) {
        // check if it doesnt have a record yet
        BookOverdue bookOverDue = new BookOverdue();
        double overDueFee = getOverDueFee(curBookBorrowed);
        bookOverDue.setComputedFee((float)overDueFee);
        bookOverDue.setBookBorrowerRefId(curBookBorrowed);
        bookOverDue.setBalance(0);
        bookOverDue.setPaid((float)overDueFee);
        bookOverDue.setNotes("Paid overdue fee from total " + totalPenalty.getText());
        if (overDueFee > 0) {
            this.bookOverDueFacade.create(bookOverDue);
            Logger.getLogger(ReturnBookController.class.getName()).log(Level.INFO, "Paid overdue fee from total " + totalPenalty.getText());
        } else {
            Logger.getLogger(ReturnBookController.class.getName()).log(Level.INFO, "No overdue fee for bookborrow #" + curBookBorrowed.getId());
        }
        // done
    }

    // Update book availability to available
    private void setToAvailable(Books bookToUpdate) {
        // find the book.
        Books foundBook = null;
        try {
            bookToUpdate.setAvailability(Books.AVAILABLE_STATUS);
            bookFacade.edit(bookToUpdate);
        } catch (Exception ex) {
            Logger.getLogger(ReturnBookController.class.getName()).log(Level.INFO, "Cant set the availability of book. Book #" + bookToUpdate.toString());
        }
        // set availability.
    }

    @Override
    public void refresh() {
        Logger.getLogger(ReturnBookController.class.getName()).log(Level.INFO, "ReturnBookController refresh");
        double penaltyPerDaySettingVal = LibraryManagementSystem.BOOK_PENALTY_PER_DAY;
        this.penaltyPerDay.setText(""+penaltyPerDaySettingVal+" pesos");
    }

}
