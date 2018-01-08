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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import librarymanagementsystem.LibraryManagementSystem;
import librarymanagementsystem.components.Sha1Hash;
import librarymanagementsystem.components.UserAuthenticator;

/**
 *
 * @author danml
 */
public class LoginController implements Initializable {

    @FXML
    private JFXTextField txtUsername;
    @FXML
    private JFXPasswordField txtPassword;
    @FXML
    private JFXButton btnLogin;
    @FXML
    private AnchorPane rootPane;

    private void handleButtonAction(ActionEvent event) {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handleValidation();
    }

    @FXML
    private void login(ActionEvent event) throws IOException {
        try {
            //if valid , set user session
            String password = this.txtPassword.getText();
            password = Sha1Hash.SHA1(password);
            librarymanagementsystem.LibraryManagementSystem.CURRENT_USER = UserAuthenticator.authenticate(this.txtUsername.getText(), password);
            if (librarymanagementsystem.LibraryManagementSystem.CURRENT_USER != null) {
                // show dashboard 
                AnchorPane currentView = LibraryManagementSystem.showView("user.dashboard");
                DashboardController dashboardController = (DashboardController)LibraryManagementSystem.ControllerCollection.get(currentView);
                dashboardController.updateUserLabel(librarymanagementsystem.LibraryManagementSystem.CURRENT_USER);
            } else {
                // show error dialog
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Invalid Account");
                alert.setHeaderText("Invalid Username/Password");
                alert.setContentText("Invalid username/password combination");
                alert.showAndWait();
                this.txtUsername.requestFocus();
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeApplication() {
        Platform.exit();
    }

    private void handleValidation() {
        RequiredFieldValidator fieldValidator = new RequiredFieldValidator();
        fieldValidator.setMessage("Input required");
        fieldValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));
        txtUsername.getValidators().add(fieldValidator);
        txtUsername.focusedProperty().addListener((ObservableValue<? extends Boolean> o, Boolean oldVal, Boolean newVal) -> {
            if (!newVal) {
                txtUsername.validate();
            }
        });
        RequiredFieldValidator fieldValidator2 = new RequiredFieldValidator();
        fieldValidator2.setMessage("Input required");
        fieldValidator2.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));
        txtPassword.getValidators().add(fieldValidator2);
        txtPassword.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtPassword.validate();
            }
        });

    }

    private void completeLogin() {

    }

}
