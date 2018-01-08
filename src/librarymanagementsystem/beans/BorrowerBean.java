/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.beans;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author User
 */
public class BorrowerBean {

    private SimpleIntegerProperty borrower_id;
    private SimpleStringProperty borrower_name;
    private SimpleStringProperty borrower_mobile;
    private SimpleStringProperty borrower_address;

    public SimpleIntegerProperty borrower_idProperty() {
        return this.borrower_id;
    }

    public SimpleStringProperty borrower_nameProperty() {
        return this.borrower_name;
    }

    public SimpleStringProperty borrower_mobileProperty() {
        return this.borrower_mobile;
    }

    public SimpleStringProperty borrower_addressProperty() {
        return this.borrower_address;
    }

    public BorrowerBean() {
        this.borrower_id = new SimpleIntegerProperty();
        this.borrower_name = new SimpleStringProperty();
        this.borrower_mobile = new SimpleStringProperty();
        this.borrower_address = new SimpleStringProperty();
    }

    public BorrowerBean(Integer borrower_id, String borrower_name, String borrower_mobile, String borrower_address) {
        this.borrower_id = new SimpleIntegerProperty(borrower_id);
        this.borrower_name = new SimpleStringProperty(borrower_name);
        this.borrower_mobile = new SimpleStringProperty(borrower_mobile);
        this.borrower_address = new SimpleStringProperty(borrower_address);
    }

    public Integer getBorrowerId() {
        return this.borrower_id.get();
    }

    public void setBorrowerId(Integer borrower_id) {
        this.borrower_id.set(borrower_id);
    }

    public String getBorrowerName() {
        return borrower_name.get();
    }

    public void setBorrowerName(String borrower_name) {
        this.borrower_name.set(borrower_name);
    }

    public String getBorrowerMobile() {
        return borrower_mobile.get();
    }

    public void setBorrowerMobile(String borrower_mobile) {
        this.borrower_mobile.set(borrower_mobile);
    }

    public String getBorrowerAddress() {
        return borrower_address.get();
    }

    public void setBorrowerAddress(String borrower_address) {
        this.borrower_address.set(borrower_address);
    }

}
