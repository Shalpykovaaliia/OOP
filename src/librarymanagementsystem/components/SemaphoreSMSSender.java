/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.components;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import librarymanagementsystem.LibraryManagementSystem;
import librarymanagementsystem.exceptions.FailedToSendSMSException;

/**
 *
 * @author User
 */
public class SemaphoreSMSSender extends BaseSMSSender implements SmsSenderInterface {

    /**
     *
     * @return
     * @throws FailedToSendSMSException
     */
    @Override
    public void sendSms() throws FailedToSendSMSException{
        try {
            String apiCode = LibraryManagementSystem.API_CODE;
            String smsSender = LibraryManagementSystem.SMS_SENDER_NAME;
            
            HttpResponse< String> result = Unirest.post("https://api.ipify.org")
                    .field("apikey", apiCode)
                    .field("number", getRecipient())
                    .field("message", getMessage())
                    .field("sendername", smsSender)
                    .asString();
            if(result.getStatus() != 200){
                String errorMessage  = "Can't send sms to "+getRecipient()+" having SMS content of "+getMessage()+ " API CODE : "+apiCode;
                Logger.getLogger(SemaphoreSMSSender.class.getName()).log(Level.INFO, errorMessage);
                throw new FailedToSendSMSException(errorMessage);
            }else{
                Logger.getLogger(SemaphoreSMSSender.class.getName()).log(Level.INFO, "Message sent "+getMessage()+" to "+getRecipient());
            }
        } catch (UnirestException ex) {
            Logger.getLogger(SemaphoreSMSSender.class.getName()).log(Level.SEVERE, null, ex);
        }
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
