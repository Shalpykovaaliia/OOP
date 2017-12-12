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
public class BookBorrowedBean {
    private SimpleIntegerProperty bookId;
    private SimpleStringProperty bookTitle;
    private SimpleStringProperty bookEdition;

    public BookBorrowedBean() {
        this.bookId = new SimpleIntegerProperty();
        this.bookTitle = new SimpleStringProperty();
        this.bookEdition = new SimpleStringProperty();
    }
    public BookBorrowedBean(SimpleIntegerProperty bookId, SimpleStringProperty bookTitle, SimpleStringProperty bookEdition) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookEdition = bookEdition;
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

}
