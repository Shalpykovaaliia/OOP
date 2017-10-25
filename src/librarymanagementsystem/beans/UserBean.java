/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.beans;

import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author User
 */
public class UserBean {

    private SimpleIntegerProperty id;
    private SimpleStringProperty username;
    private SimpleStringProperty ownerName;
    private SimpleStringProperty role;
    
    
    public SimpleIntegerProperty idProperty(){
        return this.id;
    }

    public SimpleIntegerProperty getId() {
        return id;
    }

    public void setId(SimpleIntegerProperty id) {
        this.id = id;
    }

    public SimpleStringProperty roleProperty() {
        return this.role;
    }

    public SimpleStringProperty ownerNameProperty() {
        return this.ownerName;
    }

    public SimpleStringProperty usernameProperty() {
        return this.username;
    }

    /**
     * Get the value of role
     *
     * @return the value of role
     */
    public SimpleStringProperty getRole() {
        return role;
    }

    /**
     * Set the value of role
     *
     * @param role new value of role
     */
    public void setRole(SimpleStringProperty role) {
        this.role = role;
    }

    /**
     * Get the value of ownerName
     *
     * @return the value of ownerName
     */
    public SimpleStringProperty getOwnerName() {
        return ownerName;
    }

    /**
     * Set the value of ownerName
     *
     * @param ownerName new value of ownerName
     */
    public void setOwnerName(SimpleStringProperty ownerName) {
        this.ownerName = ownerName;
    }

    /**
     * Get the value of username
     *
     * @return the value of username
     */
    public SimpleStringProperty getUsername() {
        return username;
    }

    /**
     * Set the value of username
     *
     * @param username new value of username
     */
    public void setUsername(SimpleStringProperty username) {
        this.username = username;
    }

}
