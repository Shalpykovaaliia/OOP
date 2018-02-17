/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.controllers;

import com.jfoenix.controls.IFXTextInputControl;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import com.mysql.jdbc.NotImplemented;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.swing.JTextField;
import librarymanagementsystem.beans.UserBean;
import librarymanagementsystem.components.Sha1Hash;
import librarymanagementsystem.facade.ProfileFacade;
import librarymanagementsystem.facade.UserFacade;
import librarymanagementsystem.facade.exceptions.NonexistentEntityException;
import librarymanagementsystem.models.Profile;
import librarymanagementsystem.models.User;
import librarymanagementsystem.validator.EmailAddressValidator;
import librarymanagementsystem.validator.IntegerFieldValidator;
import librarymanagementsystem.validator.SameFieldValidator;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ManageUserController implements Initializable {

    private ArrayList<String> errorMessages = new ArrayList<>();

    @FXML
    private JFXTextField filterTextField;

    @FXML
    private JFXButton filterGridBtn;

    @FXML
    private TableView<UserBean> systemUserTable;

    @FXML
    private TableColumn<UserBean, String> usernameTableColumn;

    @FXML
    private TableColumn<UserBean, String> ownerNameTableColumn;
    @FXML
    private TableColumn<UserBean, String> roleTableColumn;
    @FXML
    private TableColumn<UserBean, Integer> idTableColumn;

    @FXML
    private JFXTextField usernameTextField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXPasswordField retypePasswordField;

    @FXML
    private JFXTextField firstnameField;

    @FXML
    private JFXTextField lastnameField;

    @FXML
    private JFXTextField emailAddressField;

    @FXML
    private JFXTextField phoneField;

    @FXML
    private JFXButton clearAllFieldsBtn;

    protected ContextMenu contextMenu;

    private UserFacade userFacade;
    private ProfileFacade profileFacade;

    private RequiredFieldValidator fieldValidator;

    private RequiredFieldValidator usernameTextFieldValidator;

    private RequiredFieldValidator passwordFieldValidator;

    private SameFieldValidator passwordCompareValidator;

    private RequiredFieldValidator retypePasswordFieldValidator;

    private SameFieldValidator retypePasswordCompareValidator;

    private RequiredFieldValidator firstnameFieldValidator;

    private RequiredFieldValidator lastnameFieldValidator;

    private RequiredFieldValidator emailAddressFieldValidator;

    private RequiredFieldValidator phoneFieldValidator;

    private RequiredFieldValidator roleFieldValidator;

    private RequiredFieldValidator securityQuestion1FieldValidator;

    private RequiredFieldValidator securityQuestion2FieldValidator;

    private RequiredFieldValidator securityQuestion3FieldValidator;

    private EmailAddressValidator emailAddressValidator;

    private IntegerFieldValidator integerFieldValidator;

    private User currentUser;

    @FXML
    private JFXButton submitBtn;

    @FXML
    private Label securityQuestionLabel1;

    @FXML
    private JFXTextField securityQuestionAnswer1;

    @FXML
    private Label securityQuestionLabel2;

    @FXML
    private JFXTextField securityQuestionAnswer2;

    @FXML
    private Label securityQuestionLabel3;

    @FXML
    private JFXTextField securityQuestionAnswer3;

    @FXML
    private JFXComboBox<String> userRole;
    private EntityManager em;
    private EntityManagerFactory emf;
    private ArrayList Controll;

    private ArrayList<IFXTextInputControl> formFields;

    @FXML
    void filterGridTable(ActionEvent event) {
    }

    @FXML
    void showRecentView(ActionEvent event) {

    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        this.emf = librarymanagementsystem.LibraryManagementSystem.APP_ENTITY_MANAGER_FACTORY;
        this.em = emf.createEntityManager();

        //load security question security question
        this.userFacade = new UserFacade(emf);
        this.profileFacade = new ProfileFacade(emf);

        initializeValidators();

        registerContextMenuUserTable();

        // Initialize and configure user table list view
        initializeUserRecordTableView();
        // Show list of users
        loadListOfUsers();
        // populate the role 
        this.loadRoleItems();

        registerValidation();

        registerFormFields();

    }

    @FXML
    void returnBack(ActionEvent event) {
        librarymanagementsystem.LibraryManagementSystem.back();
    }

    @FXML
    void submitRecord(ActionEvent event) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        System.out.println("Submitting record");
        this.errorMessages.clear();

        for (IFXTextInputControl formField : formFields) {
            formField.validate();
            //Boolean isValid = new Boolean(formField.validate());
            //System.out.println(isValid.toString());

            if (formField instanceof JFXTextField) {
                JFXTextField formField2 = (JFXTextField) formField;

                ObservableList<ValidatorBase> validators = formField2.getValidators();
                for (ValidatorBase validator : validators) {
                    if (validator.getHasErrors()) {
                        this.errorMessages.add(validator.getMessage());
                    }
                }
            }
            if (formField instanceof JFXPasswordField) {
                JFXPasswordField formField2 = (JFXPasswordField) formField;
                ObservableList<ValidatorBase> validators = formField2.getValidators();
                for (ValidatorBase validator : validators) {
                    if (validator.getHasErrors()) {
                        this.errorMessages.add(validator.getMessage());
                    }
                }
            }
        }
        System.out.println("Validation done");
        if (this.errorMessages.size() == 0) {
            System.out.println("Validation passed");
            System.out.println("About to save the record");
            if (this.currentUser != null) {
                System.out.println("Updating record");
                updateRecord();
                System.out.println("Record updated");

            } else {
                System.out.println("Saving record");
                saveRecord();
                System.out.println("Record saved");
            }
        } else {
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
            System.out.println("Validation failed");
        }
    }

    private void registerValidation() {
        this.usernameTextField.getValidators().add(usernameTextFieldValidator);
        this.passwordField.getValidators().add(passwordFieldValidator);
        this.passwordField.getValidators().add(passwordCompareValidator);
        this.retypePasswordField.getValidators().add(retypePasswordFieldValidator);
        this.retypePasswordField.getValidators().add(retypePasswordCompareValidator);
        this.firstnameField.getValidators().add(firstnameFieldValidator);
        this.lastnameField.getValidators().add(lastnameFieldValidator);
        this.emailAddressField.getValidators().add(emailAddressFieldValidator);
        this.emailAddressField.getValidators().add(emailAddressValidator);
        this.phoneField.getValidators().add(phoneFieldValidator);
        this.phoneField.getValidators().add(integerFieldValidator);

        this.securityQuestionAnswer1.getValidators().add(securityQuestion1FieldValidator);
        this.securityQuestionAnswer2.getValidators().add(securityQuestion2FieldValidator);
        this.securityQuestionAnswer3.getValidators().add(securityQuestion3FieldValidator);

    }

    private void saveRecord() {
        System.out.println("Saving record");
        // check if record exists
        User newUser = new User();
        newUser.setUsername(usernameTextField.getText());
        newUser.setFirstSecretQuestion(securityQuestionLabel1.getText());
        newUser.setFirstSecretAnswer(securityQuestionAnswer1.getText());
        newUser.setSecondSecretQuestion(securityQuestionLabel2.getText());
        newUser.setSecondSecretAnswer(securityQuestionAnswer2.getText());
        newUser.setThirdSecretQuestion(securityQuestionLabel3.getText());
        newUser.setThirdSecretAnswer(securityQuestionAnswer3.getText());
        newUser.setRole(this.userRole.getValue());

        if (!this.passwordField.getText().equals("") && !retypePasswordField.getText().equals("")) {
            String hashedPassword = null;
            Boolean hasError = false;
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("An error occured while hashing the password");
            try {
                hashedPassword = Sha1Hash.SHA1(passwordField.getText());
            } catch (NoSuchAlgorithmException ex) {
                errorAlert.setContentText(ex.getMessage());
                hasError = true;
                Logger.getLogger(ManageUserController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                errorAlert.setContentText(ex.getMessage());
                hasError = true;
                Logger.getLogger(ManageUserController.class.getName()).log(Level.SEVERE, null, ex);
            }
            newUser.setPassword(hashedPassword);
        }

        Profile currentUserProfile = new Profile();
        // update fields
        currentUserProfile.setFirstname(firstnameField.getText());
        currentUserProfile.setLastname(lastnameField.getText());
        currentUserProfile.setEmailAddress(emailAddressField.getText());
        currentUserProfile.setPhoneNumber(phoneField.getText());
        this.profileFacade.create(currentUserProfile);
        // add the profile
        newUser.setProfile(currentUserProfile);
        try {
            this.userFacade.create(newUser);
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Record saved");
            successAlert.setContentText("Record successfully saved");
        } catch (Exception ex) {
            Alert errorInformation = new Alert(Alert.AlertType.ERROR);
            errorInformation.setTitle("We met some error along the way");
            errorInformation.setContentText(ex.getMessage());
            errorInformation.showAndWait();
            Logger.getLogger(ManageUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadListOfUsers();
        this.clearFields();
        System.out.println("Done saving");

    }

    private void updateRecord() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        this.currentUser.setUsername(usernameTextField.getText());
        if (!this.passwordField.getText().equals("") && !retypePasswordField.getText().equals("")) {
            String hashedPassword = Sha1Hash.SHA1(passwordField.getText());
            this.currentUser.setPassword(hashedPassword);
        }

        // update security question
        Profile currentUserProfile = this.currentUser.getProfile();
        if (currentUserProfile == null) {
            currentUserProfile = new Profile();
        }
        // update fields
        currentUserProfile.setFirstname(firstnameField.getText());
        currentUserProfile.setLastname(lastnameField.getText());
        currentUserProfile.setEmailAddress(emailAddressField.getText());
        currentUserProfile.setPhoneNumber(phoneField.getText());
        // add the profile
        this.currentUser.setProfile(currentUserProfile);
        try {
            this.profileFacade.edit(currentUserProfile);
            this.userFacade.edit(currentUser);
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Record Updated");
            success.setContentText("The record is now updated");
            success.showAndWait();
        } catch (Exception ex) {
            Alert errorInformation = new Alert(Alert.AlertType.ERROR);
            errorInformation.setTitle("We met some error along the way");
            errorInformation.setContentText(ex.getMessage());
            errorInformation.showAndWait();
            Logger.getLogger(ManageUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.loadListOfUsers();
        this.clearFields();
        this.currentUser = null;
    }

    private void loadRoleItems() {
        ArrayList<String> availableRoles = new ArrayList<String>();
        availableRoles.add("Admin");
        availableRoles.add("Librarian");
        ObservableList<String> items = this.userRole.getItems();
        items.addAll(availableRoles);
        this.userRole.getSelectionModel().select("Librarian");

    }

    private void loadListOfUsers() {
        Query namedQuery = em.createNamedQuery("User.findAll");
        namedQuery.setHint(QueryHints.REFRESH, HintValues.TRUE);
        List<User> users = namedQuery.getResultList();
        ObservableList<UserBean> userData = FXCollections.observableArrayList();
        ObservableList<UserBean> userItems = systemUserTable.getItems();
        userItems.clear();
        for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
            User curUser = iterator.next();
            UserBean userBean = new UserBean();
            userBean.setUsername(new SimpleStringProperty(curUser.getUsername()));
            userBean.setRole(new SimpleStringProperty(curUser.getRole()));
            userBean.setId(new SimpleIntegerProperty(curUser.getId()));
            if (curUser.getProfile() != null) {
                Profile currentUserProfile = curUser.getProfile();
                //get updated profile 
                Profile updatedProfile = this.profileFacade.findProfile(currentUserProfile.getProfileId());
                StringBuilder ownerNameBuilder = new StringBuilder();
                ownerNameBuilder
                        .append(updatedProfile.getFirstname())
                        .append(" ")
                        .append(updatedProfile.getLastname());
                userBean.setOwnerName(new SimpleStringProperty(ownerNameBuilder.toString()));
            } else {
                userBean.setOwnerName(new SimpleStringProperty(""));
            }
            userData.add(userBean);
        }
        userItems.addAll(userData);
    }

    void initializeUserRecordTableView() {
        try {
            systemUserTable.setContextMenu(contextMenu);
            usernameTableColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
            ownerNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
            roleTableColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
            idTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        } catch (NullPointerException e) {

        }
    }

    private void registerContextMenuUserTable() {
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
                    UserBean selectedItem = systemUserTable.getSelectionModel().getSelectedItem();
                    try {
                        userFacade.destroy(new Integer(selectedItem.getId().get()));
                        Alert deleteMessage = new Alert(Alert.AlertType.INFORMATION);
                        deleteMessage.setTitle("Deleted");
                        deleteMessage.setContentText("Record is now deleted");
                        deleteMessage.showAndWait();
                        loadListOfUsers();
                        // issue a delete
                    } catch (NonexistentEntityException ex) {
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
                UserBean selectedItem = systemUserTable.getSelectionModel().getSelectedItem();

                if (selectedItem != null) {
                    // retrieve information
                    User userFound = userFacade.findUser(selectedItem.getId().get());
                    currentUser = userFound;
                    // load the information to the text box 
                    usernameTextField.setText(userFound.getUsername());
                    passwordField.setText("");
                    retypePasswordField.setText("");
                    if (userFound.getProfile() != null) {
                        Profile userProfile = userFound.getProfile();
                        firstnameField.setText(userProfile.getFirstname());
                        lastnameField.setText(userProfile.getLastname());
                        emailAddressField.setText(userProfile.getEmailAddress());
                        phoneField.setText(userProfile.getPhoneNumber());
                    }
                    securityQuestionAnswer1.setText(currentUser.getFirstSecretAnswer());
                    securityQuestionAnswer2.setText(currentUser.getSecondSecretAnswer());
                    securityQuestionAnswer3.setText(currentUser.getThirdSecretAnswer());
                    // set focus on the username
                    usernameTextField.requestFocus();
                }
                contextMenu.hide();
            }
        });
        contextMenu.getItems().addAll(updateMenuItem, deleteMenuItem);
    }

    private void initializeValidators() {
        this.fieldValidator = new RequiredFieldValidator();
        fieldValidator.setMessage("Input required");
        fieldValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        usernameTextFieldValidator = new RequiredFieldValidator();
        usernameTextFieldValidator.setMessage("Username is required");
        usernameTextFieldValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        passwordFieldValidator = new RequiredFieldValidator();
        passwordFieldValidator.setMessage("Password is required");
        passwordFieldValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        passwordCompareValidator = new SameFieldValidator("Password and retyped password should be the same", retypePasswordField);
        passwordCompareValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        retypePasswordFieldValidator = new RequiredFieldValidator();
        retypePasswordFieldValidator.setMessage("Retyped password required");
        retypePasswordFieldValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        retypePasswordCompareValidator = new SameFieldValidator("Password and retyped password should be the same", passwordField);
        retypePasswordCompareValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        firstnameFieldValidator = new RequiredFieldValidator();
        firstnameFieldValidator.setMessage("Firstname is required");
        firstnameFieldValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        lastnameFieldValidator = new RequiredFieldValidator();
        lastnameFieldValidator.setMessage("Lastname is required");
        lastnameFieldValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        emailAddressFieldValidator = new RequiredFieldValidator();
        emailAddressFieldValidator.setMessage("Email address is required");
        emailAddressFieldValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        phoneFieldValidator = new RequiredFieldValidator();
        phoneFieldValidator.setMessage("Phonenumber is required");
        phoneFieldValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        emailAddressValidator = new EmailAddressValidator();
        emailAddressValidator.setMessage("Current email is not valid");
        emailAddressValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        integerFieldValidator = new IntegerFieldValidator();
        integerFieldValidator.setMessage("Phonenumber should contain number only");

        securityQuestion1FieldValidator = new RequiredFieldValidator();
        securityQuestion1FieldValidator.setMessage("Security question 1 is required");

        securityQuestion2FieldValidator = new RequiredFieldValidator();
        securityQuestion2FieldValidator.setMessage("Security question 2 is required");

        securityQuestion3FieldValidator = new RequiredFieldValidator();
        securityQuestion3FieldValidator.setMessage("Security question 3 is required");
    }

    private void registerFormFields() {
        this.formFields = new ArrayList<IFXTextInputControl>();
        this.formFields.add(usernameTextField);
        this.formFields.add(passwordField);
        this.formFields.add(retypePasswordField);
        //this.formFields.add(userRole);
        this.formFields.add(firstnameField);
        this.formFields.add(lastnameField);
        this.formFields.add(emailAddressField);
        this.formFields.add(phoneField);
        this.formFields.add(securityQuestionAnswer1);
        this.formFields.add(securityQuestionAnswer2);
        this.formFields.add(securityQuestionAnswer3);

    }

    private void clearFields() {
        this.usernameTextField.setText("");
        passwordField.setText("");
        retypePasswordField.setText("");
        firstnameField.setText("");
        lastnameField.setText("");
        emailAddressField.setText("");
        phoneField.setText("");
        securityQuestionAnswer1.setText("");
        securityQuestionAnswer2.setText("");
        securityQuestionAnswer3.setText("");
    }

    @FXML
    void clearAllFields(ActionEvent event) {
        this.currentUser = null;
        this.clearFields();
    }
}