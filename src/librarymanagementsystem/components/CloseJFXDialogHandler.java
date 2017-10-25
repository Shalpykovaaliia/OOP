/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.components;

import com.jfoenix.controls.JFXDialog;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;

/**
 *
 * @author User
 */
public class CloseJFXDialogHandler implements EventHandler{

    JFXDialog dialogToClose;
    @Override
    public void handle(Event event) {
        dialogToClose.close();
    }

    public CloseJFXDialogHandler(JFXDialog dialogToClose) {
        this.dialogToClose = dialogToClose;
    }
    
}
