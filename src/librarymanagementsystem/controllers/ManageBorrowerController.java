/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ManageBorrowerController implements Initializable {

    @FXML
    private JFXTextField filterBorrowerRecord;

    @FXML
    private JFXButton filterBorrowerBtn;

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
    private JFXButton submitFormBtn;

    @FXML
    void filterBorrowerRecord(ActionEvent event) {

    }

    @FXML
    void submitForm(ActionEvent event) {

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    void returnBack(ActionEvent event) {
        librarymanagementsystem.LibraryManagementSystem.back();
    }

}
