/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.exceptions;

/**
 *
 * @author User
 */
public class FailedToSendSMSException extends Exception{

    public FailedToSendSMSException(String message) {
        super(message);
    }
    
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
