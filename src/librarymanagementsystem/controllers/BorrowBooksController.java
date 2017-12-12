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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
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
import javafx.scene.text.Text;
import javafx.util.Callback;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import librarymanagementsystem.BookNotFoundException;
import librarymanagementsystem.beans.BookBean;
import librarymanagementsystem.beans.BookBorrowedBean;
import librarymanagementsystem.facade.BookBorrowerJpaController;
import librarymanagementsystem.facade.exceptions.NonexistentEntityException;
import librarymanagementsystem.models.BookBorrower;
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
public class BorrowBooksController implements Initializable {

    @FXML
    private JFXTextField bookBarcodeField;

    @FXML
    private TableView<BookBorrowedBean> bookBorrowedTable;

    @FXML
    private TableColumn<BookBorrowedBean, String> titleColumn;

    @FXML
    private TableColumn<BookBorrowedBean, Integer> idColumn;

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
    private Borrower currentBorrowerSelected;

    @FXML
    void addBook(ActionEvent event) {
        // get the current book
        BookBorrowedBean bookBorrowedBean = new BookBorrowedBean();
        bookBorrowedBean.setBookId(this.currentBookFound.getBookId());
        bookBorrowedBean.setBookTitle(this.currentBookFound.getTitle());
        bookBorrowedBean.setBookEdition(this.currentBookFound.getEdition());
        bookBorrowedTable.getItems().add(bookBorrowedBean);
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
            errorAlert.setTitle("No borrower");
            errorAlert.setContentText("Please select a borrower");
            errorAlert.showAndWait();
        } else {
            // check if borrower is selected 
            BookBorrower bookBorrower = new BookBorrower();
            bookBorrower.setDateBorrowed(new Date());
            Date tempReturnDateContainer = Date.from(returnDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            bookBorrower.setDateReturned(tempReturnDateContainer);
            bookBorrower.setBorrowerId(currentBorrowerSelected);
            this.bookBorrowerJpaController.create(bookBorrower);
            // this should create book borrower
            // @TODO
        }
    }

    @FXML
    void returnBack(ActionEvent event) {
        librarymanagementsystem.LibraryManagementSystem.back();
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
        }
    }

    @FXML
    void selectCurrentBorrower(ActionEvent event) {
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.emf = librarymanagementsystem.LibraryManagementSystem.APP_ENTITY_MANAGER_FACTORY;
        this.em = emf.createEntityManager();
        this.bookBorrowerJpaController = new BookBorrowerJpaController(emf);
        this.restrictDatepickerMinDate();
        this.initializeContextMenu();
        this.initializeBookToBeBorrowedTable();
        this.loadBorrowersCollection();
    }

    private Books getBook(String inputCode) throws BookNotFoundException {
        Books returnedResult = null;
        TypedQuery<Books> query = em.createNamedQuery("Books.findByBarcode", Books.class);
        query.setParameter("barcode", inputCode);
        try {
            returnedResult = query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new BookNotFoundException("Can't find book having barcode of : " + inputCode);
        }
        return returnedResult;
    }

    private void initializeBookToBeBorrowedTable() {
        this.bookBorrowedTable.setContextMenu(this.tableContextMenu);
        try {
            idColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
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

    private void loadBorrowersCollection() {
        //find all borrowers
        Query namedQuery = em.createNamedQuery("Borrower.findAll");
        namedQuery.setHint(QueryHints.REFRESH, HintValues.TRUE);
        List<Borrower> borrowers = namedQuery.getResultList();
        List<String> nameCollection = new ArrayList<>();

        for (Iterator<Borrower> iterator = borrowers.iterator(); iterator.hasNext();) {
            Borrower tempBorrower = iterator.next();
            StringBuilder fullName = new StringBuilder();
            fullName.append(tempBorrower.getTitle())
                    .append(" ")
                    .append(tempBorrower.getFirstname())
                    .append(" ")
                    .append(tempBorrower.getLastname());
            nameCollection.add(fullName.toString());
        }
        AutoCompletionBinding<String> acb = TextFields.bindAutoCompletion(filterBorrowerField, nameCollection);
        acb.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                setSelectedBorrower(event.getCompletion());
            }
        });
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

}
