/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.controllers;

import com.jfoenix.controls.IFXTextInputControl;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
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
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import librarymanagementsystem.beans.BorrowerBean;
import librarymanagementsystem.constants.Scenario;
import librarymanagementsystem.facade.BorrowerFacade;
import librarymanagementsystem.facade.exceptions.NonexistentEntityException;
import librarymanagementsystem.models.Borrower;
import librarymanagementsystem.validator.UniqueBookBarcodeValidator;
import librarymanagementsystem.validator.UniqueBorrowerBarcodeValidator;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ManageBorrowerController implements Initializable {

    @FXML
    private TableView<BorrowerBean> borrowerTableGrid;

    @FXML
    private TableColumn<BorrowerBean, Integer> borrowerIdColumn;

    @FXML
    private TableColumn<BorrowerBean, String> borrowerNameColumn;

    @FXML
    private TableColumn<BorrowerBean, String> mobileNumberColumn;

    @FXML
    private TableColumn<BorrowerBean, String> borrowerAddressColumn;

    @FXML
    private JFXTextField borrowerBarcode;

    @FXML
    private JFXTextField borrowerTitle;

    @FXML
    private JFXTextField borrowerFirstname;

    @FXML
    private JFXTextField borrowerLastname;

    @FXML
    private JFXDatePicker borrowerBirthday;

    @FXML
    private JFXRadioButton borrowerGenderMale;

    @FXML
    private ToggleGroup gender;

    @FXML
    private JFXRadioButton borrowerGenderFemale;

    @FXML
    private JFXTextField borrowerAddressStreet;

    @FXML
    private JFXTextField borrowerAddressBarangay;

    @FXML
    private JFXTextField borrowerAddressMunicipality;

    @FXML
    private JFXTextField borrowerAddressZipcode;

    @FXML
    private JFXTextField borrowerMobileNumber;

    @FXML
    private JFXTextField borrowerEmailAddress;

    @FXML
    private JFXButton submitFormBtn;

    @FXML
    private JFXButton clearFieldsBtn;

    private EntityManagerFactory emf;
    private EntityManager em;
    private BorrowerFacade borrowerFacade;
    private RequiredFieldValidator borrowerTitleValidator;
    private RequiredFieldValidator firstNameRequiredValidator;
    private RequiredFieldValidator lastNameRequiredValidator;
    private ArrayList<IFXTextInputControl> formFields = new ArrayList<>();
    private ContextMenu contextMenu;
    private Borrower currentSelectedBorrower;
    private ArrayList<String> errorMessages = new ArrayList<>();
    private RequiredFieldValidator borrowerBarcodeValidator;
    private static Scenario CURRENT_SCENARIO = Scenario.NEW_RECORD;
    private UniqueBorrowerBarcodeValidator uniqueBorrowerBarcodeValidator;

    @FXML
    void filterBorrowerRecord(ActionEvent event) {

    }

    @FXML
    void submitForm(ActionEvent event) {
        this.errorMessages.clear();
        this.uniqueBorrowerBarcodeValidator.setScenario(ManageBorrowerController.CURRENT_SCENARIO);
        this.uniqueBorrowerBarcodeValidator.setCurrentBorrower(this.currentSelectedBorrower);
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
            if (this.currentSelectedBorrower != null) {
                updateRecord();// using the currentbook as current selected object
            } else {
                createNewBorrower();
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

        this.borrowerFacade = new BorrowerFacade(emf);

        this.initializeValidators();
        this.registerValidator();
        this.registerFormFields();
        this.registerTableMenu();
        this.initializeBorrowerRecordTable();
        this.loadListOfBorrowers();

    }

    @FXML
    void returnBack(ActionEvent event) {
        librarymanagementsystem.LibraryManagementSystem.back();
    }

    private void initializeValidators() {
        this.borrowerBarcodeValidator = new RequiredFieldValidator();
        this.borrowerBarcodeValidator.setMessage("Student ID is required");
        this.borrowerBarcodeValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        this.uniqueBorrowerBarcodeValidator = new UniqueBorrowerBarcodeValidator();
        this.uniqueBorrowerBarcodeValidator.setMessage("Barcode number is taken");
        this.uniqueBorrowerBarcodeValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        this.borrowerTitleValidator = new RequiredFieldValidator();
        this.borrowerTitleValidator.setMessage("Title is required");
        this.borrowerTitleValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        this.firstNameRequiredValidator = new RequiredFieldValidator();
        this.firstNameRequiredValidator.setMessage("Firstname is required");
        this.firstNameRequiredValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        this.lastNameRequiredValidator = new RequiredFieldValidator();
        this.lastNameRequiredValidator.setMessage("Lastname is required");
        this.lastNameRequiredValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

    }

    private void registerValidator() {
        this.borrowerBarcode.getValidators().add(this.borrowerBarcodeValidator);
        this.borrowerBarcode.getValidators().add(this.uniqueBorrowerBarcodeValidator);
        this.borrowerTitle.getValidators().add(this.borrowerTitleValidator);
        this.borrowerFirstname.getValidators().add(this.firstNameRequiredValidator);
        this.borrowerLastname.getValidators().add(this.lastNameRequiredValidator);
        this.borrowerBirthday.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isAfter(LocalDate.now().minusDays(1))) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        });

    }

    private void registerFormFields() {
        this.formFields.add(borrowerBarcode);
        this.formFields.add(borrowerTitle);
        this.formFields.add(borrowerFirstname);
        this.formFields.add(borrowerLastname);
    }

    private void registerTableMenu() {
        MenuItem deleteMenuItem = new MenuItem("Delete");
        MenuItem updateMenuItem = new MenuItem("Edit");
        this.contextMenu = new ContextMenu();
        this.contextMenu.autoHideProperty().set(true);
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ManageBorrowerController.CURRENT_SCENARIO = Scenario.NEW_RECORD;
                // show confirmation modal
                Alert confirmDeletion = new Alert(Alert.AlertType.CONFIRMATION);
                confirmDeletion.setTitle("Delete record");
                confirmDeletion.setContentText("Are you sure you want to delete this item?");

                Optional<ButtonType> answer = confirmDeletion.showAndWait();
                if (answer.get() == ButtonType.OK) {
                    // get selected data
                    BorrowerBean selectedBorrower = borrowerTableGrid.getSelectionModel().getSelectedItem();
                    try {
                        borrowerFacade.deleteByBorrowerBarcode(selectedBorrower.getBorrowerId());
                        Alert deleteMessage = new Alert(Alert.AlertType.INFORMATION);
                        deleteMessage.setTitle("Deleted");
                        deleteMessage.setContentText("Record deleted");
                        deleteMessage.showAndWait();
                        loadListOfBorrowers();
                        // issue a delete
                    } catch (NonexistentEntityException ex) {
                        Alert deleteMessage = new Alert(Alert.AlertType.ERROR);
                        deleteMessage.setTitle("Not found");
                        deleteMessage.setContentText("Can't find that record");
                        deleteMessage.showAndWait();
                        Logger.getLogger(ManageUserController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Alert deleteMessage = new Alert(Alert.AlertType.ERROR);
                        deleteMessage.setTitle("Error");
                        deleteMessage.setContentText(ex.getMessage());
                        deleteMessage.showAndWait();
                        Logger.getLogger(ManageUserController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                contextMenu.hide();
            }
        });
        updateMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Get the selected item from table
                ManageBorrowerController.CURRENT_SCENARIO = Scenario.UPDATE_OLD_RECORD;
                BorrowerBean selectedBorrower = borrowerTableGrid.getSelectionModel().getSelectedItem();
                if (selectedBorrower != null) {
                    // retrieve information
                    Borrower foundBorrower = borrowerFacade.findBorrowerByBarcode(selectedBorrower.getBorrowerId());
                    currentSelectedBorrower = foundBorrower;
                    // load the information to the text box 
                    DecimalFormat df = new DecimalFormat("#");
                    borrowerBarcode.setText(df.format(foundBorrower.getBorrowerBarcode()));
                    borrowerTitle.setText(foundBorrower.getTitle());
                    borrowerFirstname.setText(foundBorrower.getFirstname());
                    borrowerLastname.setText(foundBorrower.getLastname());
                    if (foundBorrower.getBirthday() != null) {
                        LocalDate curBirthday = foundBorrower.getBirthday()
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        borrowerBirthday.setValue(curBirthday);
                    }
                    if (foundBorrower.getGender() != null && foundBorrower.getGender().equals("MALE")) {
                        borrowerGenderMale.setSelected(true);
                        borrowerGenderFemale.setSelected(false);
                    } else if (foundBorrower.getGender() != null && foundBorrower.getGender().equals("FEMALE")) {
                        borrowerGenderMale.setSelected(false);
                        borrowerGenderFemale.setSelected(true);
                    }
                    borrowerAddressStreet.setText(foundBorrower.getAddress1());
                    borrowerAddressBarangay.setText(foundBorrower.getAddress2());
                    borrowerAddressMunicipality.setText(foundBorrower.getTown());
                    borrowerAddressZipcode.setText(foundBorrower.getPostalCode());
                    borrowerMobileNumber.setText(foundBorrower.getMobileNumber());
                    borrowerEmailAddress.setText(foundBorrower.getEmailAddress());
                    borrowerTitle.requestFocus();
                }
                contextMenu.hide();
            }
        });
        contextMenu.getItems().addAll(updateMenuItem, deleteMenuItem);
    }

    private void loadListOfBorrowers() {
        // get all records in the database
        Query namedQuery = em.createNamedQuery("Borrower.findAll");
        namedQuery.setHint(QueryHints.REFRESH, HintValues.TRUE);
        List<Borrower> borrowers = namedQuery.getResultList();
        ObservableList<BorrowerBean> borrowerCollection = FXCollections.observableArrayList();
        ObservableList<BorrowerBean> currentBorrowerItems = borrowerTableGrid.getItems();
        currentBorrowerItems.clear();
        for (Iterator<Borrower> iterator = borrowers.iterator(); iterator.hasNext();) {
            Borrower currentBorrowerIter = iterator.next();
            BorrowerBean tempBorrower = new BorrowerBean();
            //id
            tempBorrower.setBorrowerId((int) currentBorrowerIter.getBorrowerBarcode());
            //name
            StringBuilder borrowerFullName = new StringBuilder();
            borrowerFullName
                    .append(currentBorrowerIter.getTitle())
                    .append(" ")
                    .append(currentBorrowerIter.getFirstname())
                    .append(" ")
                    .append(currentBorrowerIter.getLastname());
            tempBorrower.setBorrowerName(borrowerFullName.toString());
            //contact information
            StringBuilder fullAddress = new StringBuilder();
            fullAddress
                    .append(currentBorrowerIter.getAddress1())
                    .append(" ")
                    .append(currentBorrowerIter.getAddress2())
                    .append(" ")
                    .append(currentBorrowerIter.getTown())
                    .append(" ")
                    .append(currentBorrowerIter.getPostalCode())
                    .append(" ")
                    .append(currentBorrowerIter.getTown())
                    .append(" ");
            //address
            tempBorrower.setBorrowerAddress(fullAddress.toString());
            //mobile
            tempBorrower.setBorrowerMobile(currentBorrowerIter.getMobileNumber());
            borrowerCollection.add(tempBorrower);
        }
        currentBorrowerItems.addAll(borrowerCollection);
        //done
    }

    private void initializeBorrowerRecordTable() {
        try {
            borrowerTableGrid.setContextMenu(contextMenu);
            borrowerIdColumn.setCellValueFactory(new PropertyValueFactory<>("borrower_id"));
            borrowerNameColumn.setCellValueFactory(new PropertyValueFactory<>("borrower_name"));
            mobileNumberColumn.setCellValueFactory(new PropertyValueFactory<>("borrower_mobile"));
            borrowerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("borrower_address"));
        } catch (NullPointerException e) {
        }
    }

    private void updateRecord() {
        Double updatedBarcodeVal = Double.parseDouble(borrowerBarcode.getText());
        currentSelectedBorrower.setBorrowerBarcode(updatedBarcodeVal);
        currentSelectedBorrower.setTitle(borrowerTitle.getText());
        currentSelectedBorrower.setFirstname(borrowerFirstname.getText());
        currentSelectedBorrower.setLastname(borrowerLastname.getText());
        if (borrowerBirthday.getValue() != null) {
            LocalDate borrowersBirthday = borrowerBirthday.getValue();
            Date tempDate = Date.from(borrowersBirthday.atStartOfDay(ZoneId.systemDefault()).toInstant());
            currentSelectedBorrower.setBirthday(tempDate);
        }
        if (!borrowerGenderMale.isSelected()) {
            currentSelectedBorrower.setGender("FEMALE");
        } else {
            currentSelectedBorrower.setGender("MALE");
        }
        currentSelectedBorrower.setAddress1(borrowerAddressStreet.getText());
        currentSelectedBorrower.setAddress2(borrowerAddressBarangay.getText());
        currentSelectedBorrower.setTown(borrowerAddressMunicipality.getText());
        currentSelectedBorrower.setPostalCode(borrowerAddressZipcode.getText());
        currentSelectedBorrower.setMobileNumber(borrowerMobileNumber.getText());
        currentSelectedBorrower.setEmailAddress(borrowerEmailAddress.getText());
        try {
            this.borrowerFacade.edit(currentSelectedBorrower);
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Record Updated");
            success.setContentText("The information is now updated");
            success.showAndWait();
        } catch (Exception ex) {
            Alert errorInformation = new Alert(Alert.AlertType.ERROR);
            errorInformation.setTitle("We met some error along the way");
            errorInformation.setContentText(ex.getMessage());
            errorInformation.showAndWait();
            Logger.getLogger(ManageBooksController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.loadListOfBorrowers();
        this.clearFields();
        this.currentSelectedBorrower = null;
    }

    private void createNewBorrower() {
        Borrower newBorrower = new Borrower();
        Double borrowerBarcodeVal = Double.parseDouble(borrowerBarcode.getText());
        newBorrower.setBorrowerBarcode(borrowerBarcodeVal);
        newBorrower.setTitle(borrowerTitle.getText());
        newBorrower.setFirstname(borrowerFirstname.getText());
        newBorrower.setLastname(borrowerLastname.getText());
        if (borrowerBirthday.getValue() != null) {
            LocalDate borrowersBirthday = borrowerBirthday.getValue();
            Date tempDate = Date.from(borrowersBirthday.atStartOfDay(ZoneId.systemDefault()).toInstant());
            newBorrower.setBirthday(tempDate);
        }
        if (!borrowerGenderMale.isSelected()) {
            newBorrower.setGender("FEMALE");
        } else {
            newBorrower.setGender("MALE");
        }
        newBorrower.setAddress1(borrowerAddressStreet.getText());
        newBorrower.setAddress2(borrowerAddressBarangay.getText());
        newBorrower.setTown(borrowerAddressMunicipality.getText());
        newBorrower.setPostalCode(borrowerAddressZipcode.getText());
        newBorrower.setMobileNumber(borrowerMobileNumber.getText());
        newBorrower.setEmailAddress(borrowerEmailAddress.getText());
        try {
            this.borrowerFacade.create(newBorrower);
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Record saved");
            success.setContentText("Record successfully saved");
            success.showAndWait();
        } catch (Exception ex) {
            Alert errorInformation = new Alert(Alert.AlertType.ERROR);
            errorInformation.setTitle("We met some error along the way");
            errorInformation.setContentText(ex.getMessage());
            errorInformation.showAndWait();
            Logger.getLogger(ManageBooksController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.loadListOfBorrowers();
        this.clearFields();
        this.currentSelectedBorrower = null;
    }

    private void clearFields() {
        borrowerBarcode.setText("");
        borrowerTitle.setText("");
        borrowerFirstname.setText("");
        borrowerLastname.setText("");
        borrowerBirthday.setValue(null);
        borrowerAddressStreet.setText("");
        borrowerAddressBarangay.setText("");
        borrowerAddressMunicipality.setText("");
        borrowerAddressZipcode.setText("");
        borrowerMobileNumber.setText("");
        borrowerEmailAddress.setText("");
        borrowerGenderMale.setSelected(true);
        this.currentSelectedBorrower = null;
        ManageBorrowerController.CURRENT_SCENARIO = Scenario.NEW_RECORD;
    }

    @FXML
    void clearAllFields(ActionEvent event) {
        this.currentSelectedBorrower = null;
        this.clearFields();
    }
}
