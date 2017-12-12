/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.controllers;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ReturnBookController implements Initializable {

    @FXML
    private Text borrowerName;

    @FXML
    private Text borrowerContactInformation;

    @FXML
    private Text borrowerAddress;

    @FXML
    private Text overdueTime;

    @FXML
    private Text penaltyPerDay;

    @FXML
    private Text dateToday;

    @FXML
    private Text totalPenalty;

    @FXML
    private JFXTextField bookIdScanned;

    @FXML
    private Text bookTitle;

    @FXML
    private Text bookEdition;

    @FXML
    private Text bookISBN;

    @FXML
    void returnBack(ActionEvent event) {
        librarymanagementsystem.LibraryManagementSystem.back();
    }

    @FXML
    void returnBook(ActionEvent event) {
        librarymanagementsystem.LibraryManagementSystem.back();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
