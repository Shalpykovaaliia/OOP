/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.beans;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author User
 */
public class BookBorrowedBean {

    private SimpleIntegerProperty bookId;
    private SimpleStringProperty bookTitle;
    private SimpleStringProperty bookEdition;
    private SimpleStringProperty bookOverdueTime;

    public BookBorrowedBean() {
        this.bookId = new SimpleIntegerProperty();
        this.bookTitle = new SimpleStringProperty();
        this.bookEdition = new SimpleStringProperty();
        this.bookOverdueTime = new SimpleStringProperty();
    }

    public BookBorrowedBean(Integer bookId, String bookTitle, String bookEdition, String bookOverdueTime) {
        this.bookId = new SimpleIntegerProperty(bookId);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.bookEdition = new SimpleStringProperty(bookEdition);
        this.bookOverdueTime = new SimpleStringProperty(bookOverdueTime);
    }

    public SimpleIntegerProperty bookIdProperty() {
        return this.bookId;
    }

    public SimpleStringProperty bookTitleProperty() {
        return this.bookTitle;
    }

    public SimpleStringProperty bookEditionProperty() {
        return this.bookEdition;
    }

    public SimpleStringProperty bookOverdueTimeProperty() {
        return this.bookOverdueTime;
    }

    public String getBookOverDueTime() {
        return this.bookOverdueTime.get();
    }

    public void setBookOverDueTime(String bookOverdueTime) {
        this.bookOverdueTime.set(bookOverdueTime);
    }

    public Integer getBookId() {
        return bookId.get();
    }

    public void setBookId(Integer bookId) {
        this.bookId.set(bookId);
    }

    public String getBookTitle() {
        return bookTitle.get();
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle.set(bookTitle);
    }

    public String getBookEdition() {
        return bookEdition.get();
    }

    public void setBookEdition(String bookEdition) {
        this.bookEdition.set(bookEdition);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != BookBorrowedBean.class){
            return false;
        }
        BookBorrowedBean tempObjContainer = (BookBorrowedBean) obj;
        if (tempObjContainer.getBookId() != null) {
            Logger.getLogger(BookBorrowedBean.class.getName()).log(Level.INFO, "Is equall"+(tempObjContainer.getBookId() == this.getBookId()));
            return tempObjContainer.getBookId() == this.getBookId();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBookId());
    }
    
}
