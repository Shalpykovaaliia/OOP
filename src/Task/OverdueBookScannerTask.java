/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Task;

import javafx.concurrent.Task;

/**
 *
 * @author User
 */
public class OverdueBookScannerTask extends Task<Void>{

    @Override
    protected Void call() throws Exception {
        // @TODO -  
        // from book_borrower table if expected return date is less than today and date_returned is null
        // to prevent duplicate record . check if book_borrower id is in the record of book_overdue, else add it 
        // create an overdue record for each book_borrower record
        return null;
    }
    
}
