/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import librarymanagementsystem.beans.UserBean;
import librarymanagementsystem.facade.UserFacade;
import librarymanagementsystem.facade.exceptions.NonexistentEntityException;
import librarymanagementsystem.models.Profile;
import librarymanagementsystem.models.User;
import librarymanagementsystem.validator.EmailAddressValidator;
import librarymanagementsystem.validator.IntegerFieldValidator;
import librarymanagementsystem.validator.SameFieldValidator;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ManageUserController implements Initializable {

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

    protected ContextMenu contextMenu;

    private UserFacade userFacade;

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

    private EmailAddressValidator emailAddressValidator;

    private IntegerFieldValidator integerFieldValidator;
    
    private User currentUser;

    @FXML
    private JFXButton submitBtn;

    @FXML
    void filterGridTable(ActionEvent event) {

    }

    @FXML
    void showRecentView(ActionEvent event) {

    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EntityManagerFactory emf = librarymanagementsystem.LibraryManagementSystem.APP_ENTITY_MANAGER_FACTORY;
        EntityManager em = emf.createEntityManager();

        this.userFacade = new UserFacade(emf);

        this.fieldValidator = new RequiredFieldValidator();
        fieldValidator.setMessage("Input required");
        fieldValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        usernameTextFieldValidator = new RequiredFieldValidator();
        usernameTextFieldValidator.setMessage("Input required");
        usernameTextFieldValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        passwordFieldValidator = new RequiredFieldValidator();
        passwordFieldValidator.setMessage("Input required");
        passwordFieldValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        passwordCompareValidator = new SameFieldValidator("Password and retyped password should be the same", retypePasswordField);
        passwordCompareValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        retypePasswordFieldValidator = new RequiredFieldValidator();
        retypePasswordFieldValidator.setMessage("Input required");
        retypePasswordFieldValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        retypePasswordCompareValidator = new SameFieldValidator("Password and retyped password should be the same", passwordField);
        retypePasswordCompareValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        firstnameFieldValidator = new RequiredFieldValidator();
        firstnameFieldValidator.setMessage("Input required");
        firstnameFieldValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        lastnameFieldValidator = new RequiredFieldValidator();
        lastnameFieldValidator.setMessage("Input required");
        lastnameFieldValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        emailAddressFieldValidator = new RequiredFieldValidator();
        emailAddressFieldValidator.setMessage("Input required");
        emailAddressFieldValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        phoneFieldValidator = new RequiredFieldValidator();
        phoneFieldValidator.setMessage("Input required");
        phoneFieldValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        emailAddressValidator = new EmailAddressValidator();
        emailAddressValidator.setMessage("Not a valid email address");
        emailAddressValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        integerFieldValidator = new IntegerFieldValidator();
        integerFieldValidator.setMessage("This field should contain numbers only");

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
                // @TODO - get the selected item from table
                UserBean selectedItem = systemUserTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    // retrieve information
                    User userFound = userFacade.findUser(selectedItem.getId().get());
                    currentUser  = userFound;
                    // load the information to the text box 
                    usernameTextField.setText(userFound.getUsername());
                    if (userFound.getProfileCollection() != null) {
                        ArrayList<Profile> profileCol = new ArrayList<Profile>(userFound.getProfileCollection());
                        if (profileCol != null && profileCol.size() > 0) {
                            Profile profile = profileCol.get(0);
                            firstnameField.setText(profile.getFirstname());
                            lastnameField.setText(profile.getLastname());
                            emailAddressField.setText(profile.getEmailAddress());
                            phoneField.setText(profile.getPhoneNumber());
                        }
                    }
                    // set focus on the username
                    usernameTextField.requestFocus();
                }
                contextMenu.hide();
            }
        });
        contextMenu.getItems().addAll(updateMenuItem, deleteMenuItem);

//         show list of users
        List<User> users = em.createNamedQuery("User.findAll").getResultList();
        ObservableList<UserBean> userData = FXCollections.observableArrayList();
        for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
            User currentUser = iterator.next();
            UserBean userBean = new UserBean();
            ArrayList<Profile> profileCollection;
            userBean.setUsername(new SimpleStringProperty(currentUser.getUsername()));
            userBean.setRole(new SimpleStringProperty(currentUser.getRole()));
            userBean.setId(new SimpleIntegerProperty(currentUser.getId()));
            if (currentUser.getProfileCollection() == null) {
                profileCollection = new ArrayList<>();
                if (profileCollection.size() > 0) {
                    Profile profile = profileCollection.get(0);
                    StringBuilder ownerNameBuilder = new StringBuilder();
                    ownerNameBuilder
                            .append(profile.getFirstname())
                            .append(" ")
                            .append(profile.getLastname());
                    userBean.setOwnerName(new SimpleStringProperty(ownerNameBuilder.toString()));
                }
            } else {
                userBean.setOwnerName(new SimpleStringProperty(""));
            }

            userData.add(userBean);
        }
        try {
            systemUserTable.setContextMenu(contextMenu);
            usernameTableColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
            ownerNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
            roleTableColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
            idTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            systemUserTable.getItems().addAll(userData);
        } catch (NullPointerException e) {
        }

    }

    @FXML
    void returnBack(ActionEvent event) {
        librarymanagementsystem.LibraryManagementSystem.back();
    }

    @FXML
    void submitRecord(ActionEvent event) {
        registerValidation();

        if (this.usernameTextField.validate()
                && this.passwordField.validate()
                && this.retypePasswordField.validate()
                && this.firstnameField.validate()
                && this.lastnameField.validate()
                && this.emailAddressField.validate()
                && this.phoneField.validate()) {
            
            if(this.currentUser != null){
                updateRecord();
            }else{
                saveRecord();
            }
            
            
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

        this.usernameTextField.validate();
        this.passwordField.validate();
        this.retypePasswordField.validate();
        this.firstnameField.validate();
        this.lastnameField.validate();
        this.emailAddressField.validate();
        this.phoneField.validate();

    }

    private void saveRecord() {
        //@TODO
        // check if record exists

        // record exist
        // get the old record
        // if no create new record

        //set the field of user
        //set the field of profile model
        // assign the profile model to user 
        // persist the record
    }

    private void updateRecord() {
        this.currentUser.setUsername(usernameTextField.getText());
        if (!this.passwordField.getText().equals("") && !retypePasswordField.getText().equals("")) {
            this.currentUser.setPassword(passwordField.getText());
        }
        ArrayList<Profile> userProfileCollection = new ArrayList<>(currentUser.getProfileCollection());
        Profile profile = new Profile();
        if(userProfileCollection != null && userProfileCollection.size() > 0 ){
            // get the profile 
            // update
            // clear the profile collection
            // add the profile
        }else { 
            profile = userProfileCollection
        }
        this.userFacade.edit(currentUser);
        
        
        this.currentUser = null;
    }

}
