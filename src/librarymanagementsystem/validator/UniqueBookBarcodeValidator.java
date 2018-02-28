/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.validator;

import com.jfoenix.validation.base.ValidatorBase;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextInputControl;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import librarymanagementsystem.constants.Scenario;
import librarymanagementsystem.facade.BooksFacade;
import librarymanagementsystem.models.Books;
import librarymanagementsystem.models.Borrower;

/**
 *
 * @author User
 */
public class UniqueBookBarcodeValidator extends ValidatorBase {

    protected Scenario currentScenario;
    private Books currentSelectedBook;

    @Override
    protected void eval() {
        Logger.getLogger(UniqueBookBarcodeValidator.class.getName()).log(Level.INFO, "Checking uniqueness of book barcode");
        TextInputControl currentInput = (TextInputControl) srcControl.get();
        EntityManager em = librarymanagementsystem.LibraryManagementSystem.APP_ENTITY_MANAGER_FACTORY.createEntityManager();
        TypedQuery<Books> query = em.createNamedQuery("Books.findByBarcode", Books.class);
        query.setParameter("barcode", currentInput.getText());
        List<Books> result = query.getResultList();
        if (currentScenario == Scenario.NEW_RECORD) {
            this.hasErrors.set(result.size() != 0);
        } else if (currentScenario == Scenario.UPDATE_OLD_RECORD) {
            if (result.size() >= 1) {
                Books sampleRetrievedBookRecord = result.get(0);
                if (sampleRetrievedBookRecord.getBookId().equals(this.currentSelectedBook.getBookId())) {
                    // if the same borrower , the user is updating.
                    this.hasErrors.set(false);
                } else {
                    // if different borrower record , barcode is already taken
                    this.hasErrors.set(true);
                }
            }
        }
        Logger.getLogger(UniqueBookBarcodeValidator.class.getName()).log(Level.INFO, "Current scenario: " + this.currentScenario.toString());
        Logger.getLogger(UniqueBookBarcodeValidator.class.getName()).log(Level.INFO, "Duplicate detected : " + currentInput.getText());
        Logger.getLogger(UniqueBookBarcodeValidator.class.getName()).log(Level.INFO, "We found " + result.size() + " results");
    }

    public void setScenario(Scenario currentScenario) {
        this.currentScenario = currentScenario;
    }

    public void setSelectedBook(Books currentBook) {
        this.currentSelectedBook = currentBook;
    }

}
