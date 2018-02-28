/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.controllers;

import com.jfoenix.controls.IFXTextInputControl;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import librarymanagementsystem.beans.BookBean;
import librarymanagementsystem.constants.Scenario;
import librarymanagementsystem.facade.BooksFacade;
import librarymanagementsystem.facade.exceptions.NonexistentEntityException;
import librarymanagementsystem.models.Books;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import librarymanagementsystem.validator.UniqueBookBarcodeValidator;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ManageBooksController implements Initializable {

    @FXML
    private JFXTextField filterTextField;

    @FXML
    private JFXButton filterBtn;

    @FXML
    private TableView<BookBean> bookTable;

    @FXML
    private TableColumn<BookBean, String> isbnColumn;

    @FXML
    private TableColumn<BookBean, String> bookTitleColumn;

    @FXML
    private TableColumn<BookBean, String> bookAuthorColumn;

    @FXML
    private TableColumn<BookBean, String> bookEditionColumn;

    @FXML
    private JFXTextField bookIsbn;

    @FXML
    private JFXComboBox<String> bookAvailability;

    @FXML
    private JFXTextField bookTitle;

    @FXML
    private JFXTextField bookAuthor;

    @FXML
    private JFXTextField bookDescription;

    @FXML
    private JFXTextField bookEdition;

    @FXML
    private JFXTextField bookEditionYear;

    @FXML
    private JFXButton submitBtn;

    @FXML
    private JFXTextField bookBarcode;

    @FXML
    private JFXButton clearAllFieldsBtn;

    private RequiredFieldValidator isbnValidator;
    private RequiredFieldValidator bookTitleValidator;
    private RequiredFieldValidator bookAuthorValidator;
    private RequiredFieldValidator bookBarcodeValidator;
    private UniqueBookBarcodeValidator bookBarcodeUniqueValidator;
    private ArrayList<String> errorMessages = new ArrayList<>();
    private ArrayList<IFXTextInputControl> formFields = new ArrayList<>();
    protected ContextMenu contextMenu;
    private EntityManagerFactory emf;
    private EntityManager em;
    private BooksFacade bookFacade;
    private Books currentBook;
    private static Scenario CURRENT_SCENARIO = Scenario.NEW_RECORD;

    @FXML
    void submitBookInformation(ActionEvent event) {
        this.errorMessages.clear();
        this.bookBarcodeUniqueValidator.setScenario(ManageBooksController.CURRENT_SCENARIO);
        this.bookBarcodeUniqueValidator.setSelectedBook(currentBook);
        ObservableList<ValidatorBase> bookBarcodeValidators = this.bookBarcode.getValidators();
        for (IFXTextInputControl formField : formFields) {
            formField.validate();
            if (formField instanceof JFXTextField) {
                JFXTextField formField2 = (JFXTextField) formField;
                ObservableList<ValidatorBase> validators = formField2.getValidators();
                for (ValidatorBase validator : validators) {
                    if (validator.getHasErrors()) {
                        this.errorMessages.add(validator.getMessage());
                    }
                }
            }
        }

        //validation passed
        if (this.errorMessages.size() == 0) {
            //create new record
            if (this.currentBook != null) {
                updateRecord();// using the currentbook as current selected object
            } else {
                createNewBook();
            }
        } else {
            // show error message
            StringBuilder errorMessagBuilder = new StringBuilder();
            //remove dups
            HashSet<String> hashSet = new HashSet<String>();
            hashSet.addAll(errorMessages);
            errorMessages.clear();
            errorMessages.addAll(hashSet);
            Alert validationErrorMessage = new Alert(Alert.AlertType.ERROR);
            for (String errorMessage : errorMessages) {
                errorMessagBuilder.append(errorMessage);
                errorMessagBuilder.append("\r\n");
            }
            String errorMessStr = errorMessagBuilder.toString();
            validationErrorMessage.setTitle("Please fix the following.");
            validationErrorMessage.setContentText(errorMessStr);
            validationErrorMessage.showAndWait();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.emf = librarymanagementsystem.LibraryManagementSystem.APP_ENTITY_MANAGER_FACTORY;
        this.em = emf.createEntityManager();

        this.bookFacade = new BooksFacade(emf);

        this.populateAvailabilityField();
        this.initializeValidators();
        this.registerValidator();
        this.registerFormFields();
        this.registerTableMenu();
        this.initializeBookRecordTable();
        this.loadListOfBooks();

    }

    @FXML
    void returnBack(ActionEvent event) {
        librarymanagementsystem.LibraryManagementSystem.back();
    }

    private void registerValidator() {
        this.bookIsbn.getValidators().add(isbnValidator);
        this.bookTitle.getValidators().add(bookTitleValidator);
        this.bookAuthor.getValidators().add(bookAuthorValidator);
        this.bookBarcode.getValidators().add(bookBarcodeValidator);
        this.bookBarcode.getValidators().add(bookBarcodeUniqueValidator);
    }

    private void initializeValidators() {
        isbnValidator = new RequiredFieldValidator();
        isbnValidator.setMessage("ISBN is required");
        isbnValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));
        bookTitleValidator = new RequiredFieldValidator();
        bookTitleValidator.setMessage("Book title is required");
        bookTitleValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));
        bookAuthorValidator = new RequiredFieldValidator();
        bookAuthorValidator.setMessage("Book author is required");
        bookBarcodeValidator = new RequiredFieldValidator();
        bookBarcodeValidator.setMessage("Barcode identification is required");
        bookBarcodeValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));
        bookBarcodeUniqueValidator = new UniqueBookBarcodeValidator();
        bookBarcodeUniqueValidator.setMessage("This barcode number is already used.");
        bookBarcodeUniqueValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));
    }

    private void populateAvailabilityField() {
        //populate the Availability field
        this.bookAvailability.getItems().addAll("Available", "Unavailable");
        this.bookAvailability.getSelectionModel().selectFirst();
    }

    private void registerFormFields() {
        this.formFields.add(this.bookIsbn);
        this.formFields.add(this.bookTitle);
        this.formFields.add(this.bookBarcode);
        this.formFields.add(this.bookAuthor);
        this.formFields.add(this.bookDescription);
        this.formFields.add(this.bookEdition);
    }

    private void clearFields() {
        this.bookIsbn.setText("");
        this.bookTitle.setText("");
        this.bookAuthor.setText("");
        this.bookDescription.setText("");
        this.bookEdition.setText("");
        this.bookEditionYear.setText("");
        this.bookBarcode.setText("");
        ManageBooksController.CURRENT_SCENARIO = Scenario.NEW_RECORD;
    }

    private void registerTableMenu() {
        MenuItem deleteMenuItem = new MenuItem("Delete");
        MenuItem updateMenuItem = new MenuItem("Edit");
        contextMenu = new ContextMenu();
        contextMenu.autoHideProperty().set(true);
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // show confirmation modal
                Alert confirmDeletion = new Alert(Alert.AlertType.CONFIRMATION);
                confirmDeletion.setTitle("Delete record");
                confirmDeletion.setContentText("Are you sure you want to delete this item?");

                Optional<ButtonType> answer = confirmDeletion.showAndWait();
                if (answer.get() == ButtonType.OK) {
                    // get selected data
                    BookBean selectedBook = bookTable.getSelectionModel().getSelectedItem();
                    try {
                        bookFacade.destroy(new Integer(selectedBook.getBookId()));
                        Alert deleteMessage = new Alert(Alert.AlertType.INFORMATION);
                        deleteMessage.setTitle("Deleted");
                        deleteMessage.setContentText("Record is now deleted");
                        deleteMessage.showAndWait();
                        loadListOfBooks();
                        // issue a delete
                    } catch (NonexistentEntityException ex) {
                        Alert deleteMessage = new Alert(Alert.AlertType.ERROR);
                        deleteMessage.setTitle("Deleted");
                        deleteMessage.setContentText("Record is now deleted");
                        deleteMessage.showAndWait();

                        Logger.getLogger(ManageUserController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                contextMenu.hide();
                clearFields();
            }
        });
        updateMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ManageBooksController.CURRENT_SCENARIO = Scenario.UPDATE_OLD_RECORD;
                // Get the selected item from table
                BookBean selectedBook = bookTable.getSelectionModel().getSelectedItem();
                if (selectedBook != null) {
                    // retrieve information
                    Books foundBook = bookFacade.findBooks(selectedBook.getBookId());
                    currentBook = foundBook;
                    // load the information to the text box 
                    bookIsbn.setText(foundBook.getIsbn());
                    bookBarcode.setText(foundBook.getBarcodeIdentification());
                    bookAvailability.setValue(foundBook.getAvailability());
                    bookTitle.setText(foundBook.getTitle());
                    bookAuthor.setText(foundBook.getAuthor());
                    bookDescription.setText(foundBook.getDescription());
                    bookEdition.setText(foundBook.getEdition());
                    bookEditionYear.setText(foundBook.getEditionYear());
                    bookIsbn.requestFocus();
                }
                contextMenu.hide();
            }
        });
        contextMenu.getItems().addAll(updateMenuItem, deleteMenuItem);
    }

    private void loadListOfBooks() {
        // get all records in the database
        Query namedQuery = em.createNamedQuery("Books.findAll");
        namedQuery.setHint(QueryHints.REFRESH, HintValues.TRUE);

        List<Books> books = namedQuery.getResultList();
        ObservableList<BookBean> bookCollection = FXCollections.observableArrayList();
        ObservableList<BookBean> currentBookItems = bookTable.getItems();
        currentBookItems.clear();
        for (Iterator<Books> iterator = books.iterator(); iterator.hasNext();) {
            Books currentBookIter = iterator.next();
            BookBean bookBean = new BookBean();
            bookBean.setBookId(currentBookIter.getBookId());
            bookBean.setIsbn(currentBookIter.getIsbn());
            bookBean.setAvailability(currentBookIter.getAvailability());
            bookBean.setTitle(currentBookIter.getTitle());
            bookBean.setAuthor(currentBookIter.getAuthor());
            bookBean.setDescription(currentBookIter.getDescription());
            bookBean.setEdition(currentBookIter.getEdition());
            bookCollection.add(bookBean);
        }
        currentBookItems.addAll(bookCollection);
        //done
    }

    private void initializeBookRecordTable() {
        try {
            bookTable.setContextMenu(contextMenu);
            isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
            bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            bookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
            bookEditionColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));
        } catch (NullPointerException e) {

        }
    }

    private void updateRecord() {
        this.currentBook.setIsbn(bookIsbn.getText());
        this.currentBook.setAvailability(bookAvailability.getValue());
        this.currentBook.setTitle(bookTitle.getText());
        this.currentBook.setAuthor(bookAuthor.getText());
        this.currentBook.setDescription(bookDescription.getText());
        this.currentBook.setEdition(bookEdition.getText());
        this.currentBook.setEditionYear(bookEditionYear.getText());
        this.currentBook.setDateUpdated(new Date());
        try {
            this.bookFacade.edit(currentBook);
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Record Updated");
            success.setContentText("The book information is now updated");
            success.showAndWait();
        } catch (Exception ex) {
            Alert errorInformation = new Alert(Alert.AlertType.ERROR);
            errorInformation.setTitle("We met some error along the way");
            errorInformation.setContentText(ex.getMessage());
            errorInformation.showAndWait();
            Logger.getLogger(ManageBooksController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.loadListOfBooks();
        this.clearFields();
        this.currentBook = null;
    }

    private void createNewBook() {
        Books book = new Books();
        book.setIsbn(bookIsbn.getText());
        book.setBarcodeIdentification(bookBarcode.getText());
        book.setAvailability(bookAvailability.getValue());
        book.setTitle(bookTitle.getText());
        book.setAuthor(bookAuthor.getText());
        book.setDescription(bookDescription.getText());
        book.setEdition(bookEdition.getText());
        book.setEditionYear(bookEditionYear.getText());
        book.setDateCreated(new Date());
        book.setDateUpdated(new Date());
        try {
            this.bookFacade.create(book);
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Record saved");
            successAlert.setContentText("Record successfully saved");
            successAlert.showAndWait();
        } catch (Exception ex) {
            Alert errorInformation = new Alert(Alert.AlertType.ERROR);
            errorInformation.setTitle("We met some error along the way");
            errorInformation.setContentText(ex.getMessage());
            errorInformation.showAndWait();
            Logger.getLogger(ManageBooksController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.loadListOfBooks();
        this.clearFields();
    }

    @FXML
    void clearAllFields(ActionEvent event) {
        this.currentBook = null;
        this.clearFields();
    }

}
