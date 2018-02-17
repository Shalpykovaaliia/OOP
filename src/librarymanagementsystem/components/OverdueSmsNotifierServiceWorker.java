/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.components;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javax.persistence.Query;
import librarymanagementsystem.LibraryManagementSystem;
import librarymanagementsystem.exceptions.FailedToSendSMSException;
import librarymanagementsystem.facade.BooksFacade;
import librarymanagementsystem.facade.SmsNotificationLogFacade;
import librarymanagementsystem.models.BookBorrower;
import librarymanagementsystem.models.Books;
import librarymanagementsystem.models.Borrower;
import librarymanagementsystem.models.SmsNotificationLog;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

/**
 *
 * @author User
 */
public class OverdueSmsNotifierServiceWorker extends Service<ObservableList<String>> {

    protected SmsNotificationLogFacade smsNotifFacade;
    protected SmsSenderInterface smsSender;
    protected BooksFacade bookFacade;

    public OverdueSmsNotifierServiceWorker() {
        bookFacade = new BooksFacade(LibraryManagementSystem.APP_ENTITY_MANAGER_FACTORY);
        
        this.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                // then run again
                restart();
            }
        });
        
    }

    @Override
    protected Task<ObservableList<String>> createTask() {
        // @TODO - do the searching and notifying here
        Task<ObservableList<String>> smsNotifyTask = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws InterruptedException {
                updateMessage("About to find overdued borrowed books");
                Logger.getLogger(OverdueRecordReport.class.getName()).log(Level.INFO, "About to find overdued borrowed books");
                runOverdueSmsNotifier();
                Logger.getLogger(OverdueRecordReport.class.getName()).log(Level.INFO, "Done notifying.");
                updateMessage("Done notifying.");
                int minuteToWait = 1;
                try {
                    Thread.sleep((1000 * 60) * minuteToWait);
                } catch (InterruptedException ex) {
                    Logger.getLogger(OverdueSmsNotifierServiceWorker.class.getName()).log(Level.SEVERE, null, ex);
                }

                return FXCollections.observableArrayList("");
            }
        };
        return smsNotifyTask;
    }

    
    private void runOverdueSmsNotifier() {
        Logger.getLogger(OverdueRecordReport.class.getName()).log(Level.INFO, "Running notifier.");
        OverdueRecordReport overDueReport = new OverdueRecordReport(LibraryManagementSystem.APP_ENTITY_MANAGER, LibraryManagementSystem.APP_ENTITY_MANAGER_FACTORY);
        List<BookBorrower> overduedBooks = overDueReport.getOverdueReport();
        Logger.getLogger(OverdueRecordReport.class.getName()).log(Level.INFO, "Overdued books found : " + overduedBooks.size());
        for (Iterator<BookBorrower> iterator = overduedBooks.iterator(); iterator.hasNext();) {
            BookBorrower curBookBorrower = iterator.next();
            Borrower currentBorrower = curBookBorrower.getBorrowerId();
            Books foundBook = bookFacade.findBooks(curBookBorrower.getBookId());
            // sms notification not yet sent
            Logger.getLogger(OverdueRecordReport.class.getName()).log(Level.INFO, "Checking notification log.");
            if (!smsNotificationSent(curBookBorrower)) {
                Logger.getLogger(OverdueRecordReport.class.getName()).log(Level.INFO, "Sending the sms notification.");
                // send the notification
                sendSmsNotification(curBookBorrower, currentBorrower ,foundBook);
                // create a log of the activity
                SmsNotificationLog smsNotificationLog = new SmsNotificationLog();
                smsNotificationLog.setBookBorrowerId(curBookBorrower);
                smsNotificationLog.setStatus(SmsNotificationLog.SMS_NOTIFICATION_SENT);
                smsNotificationLog.setDateSent(new Date());
                smsNotifFacade.create(smsNotificationLog);
                Logger.getLogger(OverdueRecordReport.class.getName()).log(Level.INFO, "Creating sms log.");
            } else {
                Logger.getLogger(OverdueRecordReport.class.getName()).log(Level.INFO, "Notification is sent. Skipping sms notification.");
            }
        }
    }

    private boolean smsNotificationSent(BookBorrower curBookBorrower) {
        Query query = LibraryManagementSystem.APP_ENTITY_MANAGER.createNamedQuery("SmsNotificationLog.findByBookBorrower", SmsNotificationLog.class);
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        query.setParameter("bookBorrowerId", curBookBorrower);
        try {
            return (query.getSingleResult() != null);
        } catch (Exception ex) {
            Logger.getLogger(LibraryManagementSystem.class.getName()).log(Level.SEVERE, ex.getMessage());
            return false;
        }
    }

    private void sendSmsNotification(BookBorrower curBookBorrower, Borrower currentBorrower,Books foundBook) {
        this.smsSender.setMessage("Good day borrower. The book \""+foundBook.getTitle()+"\" that you borrowed has reached its overdue date. ");
        //mobile number is not null or not empty
        if (currentBorrower.getMobileNumber() != null || !currentBorrower.getMobileNumber().equals("")) {
            if (LibraryManagementSystem.SMS_NOTIFICATION_STATUS.equals("enabled")) {
                try {
                    this.smsSender.setRecipient(currentBorrower.getMobileNumber());
                    Logger.getLogger(LibraryManagementSystem.class.getName()).log(Level.INFO, "About to send sms notification to " + currentBorrower.getMobileNumber());
                    this.smsSender.sendSms();
                    Logger.getLogger(LibraryManagementSystem.class.getName()).log(Level.INFO, "Sms notification sent to " + currentBorrower.getMobileNumber());
                } catch (FailedToSendSMSException ex) {
                    Logger.getLogger(LibraryManagementSystem.class.getName()).log(Level.INFO, "Can't notify user via sms");
                    Logger.getLogger(LibraryManagementSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            Logger.getLogger(LibraryManagementSystem.class.getName()).log(Level.INFO, "This book borrower record doesnt have contact information. #" + curBookBorrower.getId());
        }
    }

    public SmsNotificationLogFacade getSmsNotifFacade() {
        return smsNotifFacade;
    }

    public void setSmsNotifFacade(SmsNotificationLogFacade smsNotifFacade) {
        this.smsNotifFacade = smsNotifFacade;
    }

    public SmsSenderInterface getSmsSender() {
        return smsSender;
    }

    public void setSmsSender(SmsSenderInterface smsSender) {
        this.smsSender = smsSender;
    }

}
