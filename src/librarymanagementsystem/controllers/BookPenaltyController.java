/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import librarymanagementsystem.beans.PenaltyRecordBean;
import librarymanagementsystem.beans.RecentlyPenalizedBean;

/**
 * FXML Controller class
 *
 * @author User
 */
public class BookPenaltyController implements Initializable {

    @FXML
    private TableView<RecentlyPenalizedBean> recentlyPenalizedBorrowers;

    @FXML
    private TableColumn<RecentlyPenalizedBean, String> recentlyPenalizedColumn;

    @FXML
    private TableColumn<RecentlyPenalizedBean, Float> recentlyPenalizedAmount;

    @FXML
    private TableView<PenaltyRecordBean> allPenaltyRecordTable;
    
    @FXML
    private TableColumn<PenaltyRecordBean, String> penaltyRecordNameColumn;

    @FXML
    private TableColumn<PenaltyRecordBean, String> penaltyRecordBookColumn;

    @FXML
    private TableColumn<PenaltyRecordBean, Float> penaltyRecordAmountColumn;

    @FXML
    private Text recentlyPenalizedTotalField;

    @FXML
    private Text totalPenaltyFeeField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.initializeTables();
        // TODO
        // get overdue paid today
        // get overdue record all 
        // fill up the tableview
        // done
    }

    @FXML
    void returnBack(ActionEvent event) {
        librarymanagementsystem.LibraryManagementSystem.back();
    }
    // @TODO 
    private void initializeTables() {
//        recentlyPenalizedBorrowers
//        allPenaltyRecordTable
    }

}
