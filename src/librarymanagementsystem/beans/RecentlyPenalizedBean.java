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
public class RecentlyPenalizedBean {
    private SimpleStringProperty name;
    private SimpleFloatProperty amount;
    public SimpleStringProperty nameProperty(){
        return this.name;
    }
    public SimpleFloatProperty amountProperty(){
        return this.amount;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Float getAmount() {
        return amount.get();
    }

    public void setAmount(Float amount) {
        this.amount.set(amount);
    }
    
    
    
}
