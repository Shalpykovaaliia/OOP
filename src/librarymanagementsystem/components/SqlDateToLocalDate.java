/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.components;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import librarymanagementsystem.models.BookBorrower;

/**
 *
 * @author User
 */
public class SqlDateToLocalDate {
    public static LocalDate convert(Date curBookBorrowed) {
        return curBookBorrowed.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
