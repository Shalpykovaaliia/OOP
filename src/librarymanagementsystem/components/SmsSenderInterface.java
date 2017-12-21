/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.components;

import librarymanagementsystem.exceptions.FailedToSendSMSException;

/**
 *
 * @author User
 */
public interface SmsSenderInterface {

    public void sendSms() throws FailedToSendSMSException;

    public String getMessage();

    public void setMessage(String message);

    public String getRecipient();

    public void setRecipient(String recipient);
}
