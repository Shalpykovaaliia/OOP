/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.beans;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author User
 */
public class PenaltyRecordBean {

    private SimpleStringProperty name;
    private SimpleStringProperty book;
    private SimpleFloatProperty amount;

    public SimpleStringProperty nameProperty() {
        return this.name;
    }

    public SimpleStringProperty bookProperty() {
        return this.book;
    }

    public SimpleFloatProperty amountProperty() {
        return this.amount;
    }
}
