/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.controllers;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import librarymanagementsystem.interfaces.Refreshable;

/**
 * FXML Controller class
 *
 * @author User
 */
public class AboutController implements Initializable ,Refreshable{

    @FXML
    private JFXButton backButton;

    @FXML
    void returnBack(ActionEvent event) {
        librarymanagementsystem.LibraryManagementSystem.back();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @Override
    public void refresh() {

    }

}
