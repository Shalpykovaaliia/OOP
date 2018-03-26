/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import librarymanagementsystem.exceptions.BookNotAvailable;
import librarymanagementsystem.exceptions.BookNotFoundException;
import librarymanagementsystem.LibraryManagementSystem;
import librarymanagementsystem.beans.BookBorrowedBean;
import librarymanagementsystem.facade.BookBorrowerJpaController;
import librarymanagementsystem.facade.BooksFacade;
import librarymanagementsystem.facade.BorrowerJpaController;
import librarymanagementsystem.interfaces.Refreshable;
import librarymanagementsystem.models.BookBorrower;
import librarymanagementsystem.models.Books;
import librarymanagementsystem.models.Borrower;
import org.eclipse.persistence.jpa.JpaEntityManager;

/**
 * FXML Controller class
 *
 * @author User
 */
public class BorrowBooksController implements Initializable ,Refreshable{

    @FXML
    private JFXTextField bookBarcodeField;

    @FXML
    private TableView<BookBorrowedBean> bookBorrowedTable;

    @FXML
    private TableColumn<BookBorrowedBean, String> titleColumn;

    @FXML
    private TableColumn<BookBorrowedBean, String> editionColumn;

    @FXML
    private JFXTextField filterBorrowerField;

    @FXML
    private Text borrowerIdentificationId;

    @FXML
    private Text borrowerName;

    @FXML
    private Text borrowerContactInformation;

    @FXML
    private JFXButton doneButton;

    @FXML
    private Text bookTitle;

    @FXML
    private Text bookEdition;

    @FXML
    private Text bookISBN;

    @FXML
    private JFXDatePicker returnDateField;

    @FXML
    private TextArea notesField;

    private EntityManager em;
    private EntityManagerFactory emf;
    private Books currentBookFound;
    private ContextMenu tableContextMenu;
    private BookBorrowerJpaController bookBorrowerJpaController;
    private BooksFacade bookFacade;
    private Borrower currentBorrowerSelected;
    private HashSet<BookBorrowedBean> booksBorrowedBeanCollection = new HashSet<>();
    private BorrowerJpaController borrowerFacade;

    @FXML
    void addBook(ActionEvent event) {
        // get the current book
        BookBorrowedBean bookBorrowedBean = new BookBorrowedBean();
        bookBorrowedBean.setBookId(this.currentBookFound.getBookId());
        bookBorrowedBean.setBookTitle(this.currentBookFound.getTitle());
        bookBorrowedBean.setBookEdition(this.currentBookFound.getEdition());
        booksBorrowedBeanCollection.add(bookBorrowedBean);
        bookBorrowedTable.getItems().clear();
        bookBorrowedTable.getItems().addAll(new ArrayList<BookBorrowedBean>(booksBorrowedBeanCollection));
        // book to be borrowed should now be updated
    }

    @FXML
    void done(ActionEvent event) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        // check if borrowed book is more than 0
        if (bookBorrowedTable.getItems().size() == 0) {
            errorAlert.setTitle("No books borrowed");
            errorAlert.setContentText("Please add books to borrow");
            errorAlert.showAndWait();
        } else if (this.currentBorrowerSelected == null) {
            // check if borrower is selected 
            errorAlert.setTitle("No borrower");
            errorAlert.setContentText("You forgot to select a borrower for this book/s.");
            errorAlert.showAndWait();
        } else if (this.returnDateField.getValue() == null) {
            errorAlert.setTitle("No return date selected");
            errorAlert.setContentText("Please select a return date.");
            errorAlert.showAndWait();
        } else {
            Alert notifier;
            BookBorrower bookBorrower = new BookBorrower();
            bookBorrower.setDateBorrowed(new Date());
            Date tempReturnDateContainer = Date.from(returnDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            bookBorrower.setExpectedReturnDate(tempReturnDateContainer);
            bookBorrower.setBorrower(currentBorrowerSelected);

            Alert saveRecordAlert = new Alert(Alert.AlertType.CONFIRMATION);
            saveRecordAlert.setTitle("Save");
            saveRecordAlert.setHeaderText("Save record");
            saveRecordAlert.setContentText("Would you like to save this record?");
            Optional<ButtonType> result = saveRecordAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    // get all books borrowed
                    ObservableList<BookBorrowedBean> booksToBorrow = bookBorrowedTable.getItems();
                    for (Iterator<BookBorrowedBean> iterator = booksToBorrow.iterator(); iterator.hasNext();) {
                        BookBorrowedBean currentBookBean = iterator.next();
                        Books foundBookModel = this.bookFacade.findBooks(currentBookBean.getBookId());
                        bookBorrower.setBook(foundBookModel);
                        this.bookBorrowerJpaController.create(bookBorrower);
                        //change the availability to unavailable
                        this.setToUnavailable(currentBookBean.getBookId());
                    }
                    notifier = new Alert(Alert.AlertType.INFORMATION);
                    notifier.setTitle("Saved");
                    notifier.setHeaderText("Record saved");
                    notifier.setContentText("Thank you for using J.A.R.S Library System. Enjoy reading.");
                    notifier.showAndWait();

                    Alert anotherTransactionConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                    anotherTransactionConfirmation.setTitle("Another transaction");
                    anotherTransactionConfirmation.setHeaderText("Another transaction");
                    anotherTransactionConfirmation.setContentText("Would you like to create another transaction?");
                    Optional<ButtonType> confirmationRes = anotherTransactionConfirmation.showAndWait();
                    if (confirmationRes.get() == ButtonType.OK) {
                        this.clearFields();
                        this.bookBarcodeField.requestFocus();
                    }
                } catch (Exception ex) {
                    notifier = new Alert(Alert.AlertType.ERROR);
                    notifier.setTitle("Can't save the record");
                    notifier.setContentText("We met an error while saving this record." + ex.getMessage());
                    notifier.showAndWait();
                }
            }

        }
    }

    @FXML
    void returnBack(ActionEvent event) {
        this.clearFields();
        AnchorPane curView = LibraryManagementSystem.back();
        DashboardController dashboardController = (DashboardController) LibraryManagementSystem.ControllerCollection.get(curView);
        dashboardController.updateTables();
    }

    @FXML
    void bookScan(InputMethodEvent event) {

    }

    @FXML
    void searchBookByBarcode(ActionEvent event) {
        JFXTextField sourceField = (JFXTextField) event.getSource();
        String inputCode = sourceField.getText();
        Alert alertInformation = new Alert(Alert.AlertType.ERROR);
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        //get the value input 
        try {
            Books foundBook = this.getBook(inputCode);
            this.currentBookFound = foundBook;
            this.bookTitle.setText(foundBook.getTitle());
            StringBuilder bookEditionStr = new StringBuilder();
            if (!foundBook.getEdition().equals("")) {
                bookEditionStr.append(foundBook.getEdition()).append(" ").append("edition");
            }
            if (!foundBook.getEditionYear().equals("")) {
                bookEditionStr.append(" ").append(foundBook.getEdition());
            }
            this.bookEdition.setText(bookEditionStr.toString());
            this.bookISBN.setText(foundBook.getIsbn());
            successAlert.setTitle("Barcode valid");
            successAlert.setContentText("Book found");
            successAlert.showAndWait();
        } catch (BookNotFoundException ex) {
            alertInformation.setTitle("Code doesnt exists");
            alertInformation.setContentText("We can't find a book that having this code " + inputCode);
            alertInformation.showAndWait();
        } catch (BookNotAvailable ex) {
            alertInformation.setTitle("Book not available");
            alertInformation.setContentText("This book is unavailable. The borrower may have forgotten to return the book. Please contact the borrower.");
            alertInformation.showAndWait();

        }
    }
    @FXML
    void selectCurrentBorrower(ActionEvent event) {
        // get the barcode id 
        Logger.getLogger(BorrowBooksController.class.getName()).log(Level.INFO, "Searching for borrower");
        JFXTextField studentIdBarcode = (JFXTextField) event.getSource();
        Logger.getLogger(BorrowBooksController.class.getName()).log(Level.INFO, "Looking for " + studentIdBarcode.getText());
        Integer studentId = new Integer(Integer.parseInt(studentIdBarcode.getText()));
        TypedQuery<Borrower> query = this.em.createNamedQuery("Borrower.findByBarcode", Borrower.class);
        query.setParameter("borrowerBarcode", Double.parseDouble(studentIdBarcode.getText()));
        try {
            Borrower borrowerFound = query.getSingleResult();
            DecimalFormat df = new DecimalFormat("#");
            borrowerIdentificationId.setText(df.format(borrowerFound.getBorrowerBarcode()));
            String borrowerFullName = borrowerFound.getTitle() + " " + borrowerFound.getFirstname() + " " + borrowerFound.getLastname();
            borrowerName.setText(borrowerFullName);
            borrowerContactInformation.setText(borrowerFound.getMobileNumber());
            this.currentBorrowerSelected = borrowerFound;
        } catch (javax.persistence.NoResultException ex) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("No record");
            errorAlert.setTitle("No record found");
            errorAlert.setContentText("We can't find a borrower with student id : " + studentIdBarcode.getText());
            errorAlert.showAndWait();
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
        this.borrowerFacade = new BorrowerJpaController(emf);
        this.bookFacade = new BooksFacade(emf);
        this.setOnFocusListener();
        this.restrictDatepickerMinDate();
        this.initializeContextMenu();
        this.initializeBookToBeBorrowedTable();
    }

    private Books getBook(String inputCode) throws BookNotFoundException, BookNotAvailable {
        Books returnedResult = null;
        this.clearCache();
        Logger.getLogger(BorrowBooksController.class.getName()).log(Level.INFO, "Cache clear done");
        TypedQuery<Books> query = em.createNamedQuery("Books.findByBarcode", Books.class);
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        query.setParameter("barcode", inputCode);
        try {
            returnedResult = query.getSingleResult();
            if (returnedResult.getAvailability().equals("Unavailable")) {
                throw new BookNotAvailable("Book unavailable");
            }
        } catch (javax.persistence.NoResultException e) {
            throw new BookNotFoundException("Can't find book having barcode of : " + inputCode);
        }
        return returnedResult;
    }

    private void initializeBookToBeBorrowedTable() {
        this.bookBorrowedTable.setContextMenu(this.tableContextMenu);
        try {
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
            editionColumn.setCellValueFactory(new PropertyValueFactory<>("bookEdition"));
        } catch (NullPointerException e) {
        }
    }

    private void initializeContextMenu() {
        MenuItem deleteMenuItem = new MenuItem("Delete");
        this.tableContextMenu = new ContextMenu();
        tableContextMenu.autoHideProperty().setValue(true);
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // show confirmation modal
                Alert confirmDeletion = new Alert(Alert.AlertType.CONFIRMATION);
                confirmDeletion.setTitle("Delete record");
                confirmDeletion.setContentText("Are you sure you want to delete this item?");
                Optional<ButtonType> answer = confirmDeletion.showAndWait();
                if (answer.get() == ButtonType.OK) {
                    // get selected index of tableBorrowedBooks
                    BookBorrowedBean selectedBook = bookBorrowedTable.getSelectionModel().getSelectedItem();
                    bookBorrowedTable.getItems().remove(selectedBook);
                }

            }
        });
        this.tableContextMenu.getItems().add(deleteMenuItem);
    }

    private void setSelectedBorrower(String borrowersFullName) {
        // search the database for borrower having this name 
        TypedQuery<Borrower> findBorrower = em.createNamedQuery("Borrower.findByFullname", Borrower.class);
        findBorrower.setParameter("fullName", borrowersFullName);
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        try {
            Borrower foundBorrower = findBorrower.getSingleResult();
            borrowerIdentificationId.setText("ID # " + foundBorrower.getBorrowerId().toString());
            StringBuilder fullNameStr = new StringBuilder();
            fullNameStr.append(foundBorrower.getTitle())
                    .append(" ")
                    .append(foundBorrower.getFirstname())
                    .append(" ")
                    .append(foundBorrower.getLastname());
            borrowerName.setText(fullNameStr.toString());
            borrowerContactInformation.setText(foundBorrower.getMobileNumber());
            this.currentBorrowerSelected = foundBorrower;
            // if exists , set the information
        } catch (javax.persistence.NoResultException e) {
            errorAlert.setTitle("No result");
            errorAlert.setContentText("Cant find borrower information");
        }
    }

    private void restrictDatepickerMinDate() {
        this.returnDateField.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(LocalDate.now().plusDays(1))) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        });
    }

    private void clearFields() {
        this.bookBarcodeField.setText("");
        this.bookTitle.setText("Book Title");
        this.bookEdition.setText("Book Edition");
        this.bookISBN.setText("Book ISBN");
        this.bookBorrowedTable.getItems().clear();
        this.filterBorrowerField.setText("");
        this.borrowerIdentificationId.setText("Borrowers Identifacation");
        this.borrowerName.setText("Borrowers Name");
        this.borrowerContactInformation.setText("Borrowers Contact Information");
        this.returnDateField.setValue(null);
        this.notesField.setText("");
        this.booksBorrowedBeanCollection.clear();
    }

    private void setToUnavailable(Integer bookId) {
        // find the book to update
        try {
            Books foundBook = this.bookFacade.findBooks(bookId);
            foundBook.setAvailability(Books.UNAVAILABLE_STATUS);
            this.bookFacade.edit(foundBook);
            // update the status
        } catch (javax.persistence.NoResultException e) {
            Logger.getLogger(BorrowBooksController.class.getName()).log(Level.INFO, "Cant find book having ID :" + bookId.toString());
        } catch (Exception ex) {
            Logger.getLogger(BorrowBooksController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        // done
    }

    private void setOnFocusListener() {
        this.bookBarcodeField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                bookBarcodeField.setText("");

            }
        });
    }

    private void clearCache() {
        //clear cache
        ((JpaEntityManager) em.getDelegate()).getServerSession().getIdentityMapAccessor().invalidateAll();
        Logger.getLogger(BorrowBooksController.class.getName()).log(Level.INFO, "Clear cache");
        em.getEntityManagerFactory().getCache().evictAll();
        Logger.getLogger(BorrowBooksController.class.getName()).log(Level.INFO, "Clear cache again");

    }

    @Override
    public void refresh() {
    }
}
