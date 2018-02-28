/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author User
 */
@Entity
@Table(name = "tbl_book_borrower")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BookBorrower.findAll", query = "SELECT b FROM BookBorrower b")
    , @NamedQuery(name = "BookBorrower.findUnique", query = "SELECT b FROM BookBorrower b where b.dateReturned is null GROUP BY b.bookId,b.borrowerId")
    , @NamedQuery(name = "BookBorrower.findById", query = "SELECT b FROM BookBorrower b WHERE b.id = :id")
    , @NamedQuery(name = "BookBorrower.findByBookId", query = "SELECT b FROM BookBorrower b WHERE b.bookId = :bookId")
    , @NamedQuery(name = "BookBorrower.findByDateReturned", query = "SELECT b FROM BookBorrower b WHERE b.dateReturned = :dateReturned")
    , @NamedQuery(name = "BookBorrower.findByExpectedReturnDate", query = "SELECT b FROM BookBorrower b WHERE b.expectedReturnDate = :expectedReturnDate")
    , @NamedQuery(name = "BookBorrower.hasDateReturned", query = "SELECT b FROM BookBorrower b WHERE b.borrowerId = :borrowerId and b.dateReturned is null")
    , @NamedQuery(name = "BookBorrower.findBooksBorrowed", query = "SELECT b FROM BookBorrower b WHERE b.dateReturned is null and b.borrowerId = :borrowerId group by b.bookId")
    , @NamedQuery(name = "BookBorrower.findOverduedBook", query = "SELECT b FROM BookBorrower b where b.expectedReturnDate < CURRENT_DATE and b.dateReturned is null group by b.bookId")
    , @NamedQuery(name = "BookBorrower.findByDateBorrowed", query = "SELECT b FROM BookBorrower b WHERE b.dateBorrowed = :dateBorrowed")})
public class BookBorrower implements Serializable {

    @JoinColumn(name = "book_id", referencedColumnName = "book_id")
    @ManyToOne(optional = false)
    private Books bookId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bookBorrowerId")
    private Collection<SmsNotificationLog> smsNotificationLogCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_returned")
    @Temporal(TemporalType.DATE)
    private Date dateReturned;
    @Column(name = "expected_return_date")
    @Temporal(TemporalType.DATE)
    private Date expectedReturnDate;
    @Column(name = "date_borrowed")
    @Temporal(TemporalType.DATE)
    private Date dateBorrowed;
    @Basic(optional = false)
    @Lob
    @Column(name = "notes")
    private String notes;
    @JoinColumn(name = "borrower_id", referencedColumnName = "borrower_id")
    @ManyToOne(optional = false)
    private Borrower borrowerId;

    public BookBorrower() {
    }

    public BookBorrower(Integer id) {
        this.id = id;
    }

    public BookBorrower(Integer id, String notes) {
        this.id = id;
        this.notes = notes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateReturned() {
        return dateReturned;
    }

    public void setDateReturned(Date dateReturned) {
        this.dateReturned = dateReturned;
    }

    public Date getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(Date expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public Date getDateBorrowed() {
        return dateBorrowed;
    }

    public void setDateBorrowed(Date dateBorrowed) {
        this.dateBorrowed = dateBorrowed;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Borrower getBorrower() {
        return borrowerId;
    }

    public void setBorrower(Borrower borrowerId) {
        this.borrowerId = borrowerId;
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
        if (!(object instanceof BookBorrower)) {
            return false;
        }
        BookBorrower other = (BookBorrower) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "librarymanagementsystem.models.BookBorrower[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<SmsNotificationLog> getSmsNotificationLogCollection() {
        return smsNotificationLogCollection;
    }

    public void setSmsNotificationLogCollection(Collection<SmsNotificationLog> smsNotificationLogCollection) {
        this.smsNotificationLogCollection = smsNotificationLogCollection;
    }

    public Books getBook() {
        return bookId;
    }

    public void setBook(Books bookId) {
        this.bookId = bookId;
    }

    public long getOverDueDays() {
        LocalDate expectedReturnDateLocalDate = this.expectedReturnDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dateToday = LocalDate.now();
        return DAYS.between(expectedReturnDateLocalDate, dateToday);
    }

    public long getComputerFee() {
        return (long) (this.getOverDueDays()* librarymanagementsystem.LibraryManagementSystem.BOOK_PENALTY_PER_DAY);
    }

}
