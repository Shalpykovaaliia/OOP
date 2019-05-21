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
public class BookBean {

    private SimpleStringProperty bookId;
    private SimpleStringProperty isbn;
    private SimpleStringProperty availability;
    private SimpleStringProperty title;
    private SimpleStringProperty author;
    private SimpleStringProperty description;
    private SimpleStringProperty edition;
    
    public BookBean() {
        this.bookId = new SimpleStringProperty();
        this.isbn = new SimpleStringProperty();
        this.availability = new SimpleStringProperty();
        this.title = new SimpleStringProperty();
        this.author = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.edition = new SimpleStringProperty();
    }

    
    public BookBean(String bookId, String isbn, String availability, String title, String author, String description, String edition) {
        this.bookId.set(bookId);
        this.isbn.set(isbn);
        this.availability.set(availability);
        this.title.set(title);
        this.author.set(author);
        this.description.set(description);
        this.edition.set(edition);
    }
    public SimpleStringProperty id() {
        return this.bookId;
    }

    public SimpleStringProperty isbnProperty() {
        return this.isbn;
    }

    public SimpleStringProperty availabilityProperty() {
        return this.availability;
    }

    public SimpleStringProperty titleProperty() {
        return this.title;
    }

    public SimpleStringProperty authorProperty() {
        return this.author;
    }

    public SimpleStringProperty descriptionProperty() {
        return this.description;
    }

    public SimpleStringProperty editionProperty() {
        return this.edition;
    }

    public String getBookId() {
        return bookId.get();
    }

    public void setBookId(String bookId) {
        this.bookId.set(bookId);
    }

    public String getIsbn() {
        return isbn.get();
    }

    public void setIsbn(String isbn) {
        this.isbn.set(isbn);
    }

    public String getAvailability() {
        return availability.get();
    }

    public void setAvailability(String availability) {
        this.availability.set(availability);
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getEdition() {
        return edition.get();
    }

    public void setEdition(String edition) {
        this.edition.set(edition);
    }

}
