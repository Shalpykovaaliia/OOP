/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
    private TableView<?> systemUserTable;

    @FXML
    private TableColumn<?, ?> isbnColumn;

    @FXML
    private TableColumn<?, ?> bookTitleColumn;

    @FXML
    private TableColumn<?, ?> bookAuthorColumn;

    @FXML
    private TableColumn<?, ?> bookEditionColumn;

    @FXML
    private JFXTextField bookIsbn;

    @FXML
    private JFXComboBox<?> bookAvailability;

    @FXML
    private JFXTextField bookTitle;

    @FXML
    private JFXTextField bookAuthor;

    @FXML
    private JFXTextField bookDescription;

    @FXML
    private JFXTextField bookEdition;

    @FXML
    private JFXButton submitBtn;

    @FXML
    void submitBookInformation(ActionEvent event) {

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    @FXML
    void returnBack(ActionEvent event) {
        librarymanagementsystem.LibraryManagementSystem.back();
    }
}
