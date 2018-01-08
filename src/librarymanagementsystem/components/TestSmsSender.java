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
public class TestSmsSender extends BaseSMSSender implements SmsSenderInterface {

    /**
     *
     * @return @throws FailedToSendSMSException
     */
    @Override
    public void sendSms() throws FailedToSendSMSException {
        System.out.println("Sending " + getMessage() + " to " + getRecipient());
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getRecipient() {
        return this.recipient;
    }

    @Override
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

}
