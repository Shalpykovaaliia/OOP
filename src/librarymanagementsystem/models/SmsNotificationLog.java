/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author User
 */
@Entity
@Table(name = "tbl_sms_notification_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SmsNotificationLog.findAll", query = "SELECT s FROM SmsNotificationLog s")
    , @NamedQuery(name = "SmsNotificationLog.findById", query = "SELECT s FROM SmsNotificationLog s WHERE s.id = :id")
    , @NamedQuery(name = "SmsNotificationLog.findByStatus", query = "SELECT s FROM SmsNotificationLog s WHERE s.status = :status")
    , @NamedQuery(name = "SmsNotificationLog.findByBookBorrower", query = "SELECT s FROM SmsNotificationLog s WHERE s.bookBorrowerId = :bookBorrowerId")
    , @NamedQuery(name = "SmsNotificationLog.findByDateSent", query = "SELECT s FROM SmsNotificationLog s WHERE s.dateSent = :dateSent")})
public class SmsNotificationLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @Column(name = "date_sent")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSent;
    @JoinColumn(name = "book_borrower_id", referencedColumnName = "id" , nullable = false)
    @ManyToOne(optional = false)
    private BookBorrower bookBorrowerId;
    
    public static String SMS_NOTIFICATION_SENT = "SENT";

    public SmsNotificationLog() {
    }

    public SmsNotificationLog(Integer id) {
        this.id = id;
    }

    public SmsNotificationLog(Integer id, String status, Date dateSent) {
        this.id = id;
        this.status = status;
        this.dateSent = dateSent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public BookBorrower getBookBorrowerId() {
        return bookBorrowerId;
    }

    public void setBookBorrowerId(BookBorrower bookBorrowerId) {
        this.bookBorrowerId = bookBorrowerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SmsNotificationLog)) {
            return false;
        }
        SmsNotificationLog other = (SmsNotificationLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "librarymanagementsystem.models.SmsNotificationLog[ id=" + id + " ]";
    }
    
}
