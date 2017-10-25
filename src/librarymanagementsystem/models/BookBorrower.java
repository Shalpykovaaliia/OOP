/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.models;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
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
    , @NamedQuery(name = "BookBorrower.findById", query = "SELECT b FROM BookBorrower b WHERE b.id = :id")
    , @NamedQuery(name = "BookBorrower.findByBookId", query = "SELECT b FROM BookBorrower b WHERE b.bookId = :bookId")
    , @NamedQuery(name = "BookBorrower.findByDateReturned", query = "SELECT b FROM BookBorrower b WHERE b.dateReturned = :dateReturned")
    , @NamedQuery(name = "BookBorrower.findByExpectedReturnDate", query = "SELECT b FROM BookBorrower b WHERE b.expectedReturnDate = :expectedReturnDate")
    , @NamedQuery(name = "BookBorrower.findByDateBorrowed", query = "SELECT b FROM BookBorrower b WHERE b.dateBorrowed = :dateBorrowed")})
public class BookBorrower implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bookBorrowerRefId")
    private List<BookOverdue> tblBookOverdueList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "book_id")
    private int bookId;
    @Basic(optional = false)
    @Column(name = "date_returned")
    @Temporal(TemporalType.DATE)
    private Date dateReturned;
    @Basic(optional = false)
    @Column(name = "expected_return_date")
    @Temporal(TemporalType.DATE)
    private Date expectedReturnDate;
    @Basic(optional = false)
    @Column(name = "date_borrowed")
    @Temporal(TemporalType.DATE)
    private Date dateBorrowed;
    @Basic(optional = false)
    @Lob
    @Column(name = "notes")
    private String notes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "borrowerId")
    private Collection<BookBorrower> bookBorrowerCollection;
    @JoinColumn(name = "borrower_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private BookBorrower borrowerId;

    public BookBorrower() {
    }

    public BookBorrower(Integer id) {
        this.id = id;
    }

    public BookBorrower(Integer id, int bookId, Date dateReturned, Date expectedReturnDate, Date dateBorrowed, String notes) {
        this.id = id;
        this.bookId = bookId;
        this.dateReturned = dateReturned;
        this.expectedReturnDate = expectedReturnDate;
        this.dateBorrowed = dateBorrowed;
        this.notes = notes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
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

    @XmlTransient
    public Collection<BookBorrower> getBookBorrowerCollection() {
        return bookBorrowerCollection;
    }

    public void setBookBorrowerCollection(Collection<BookBorrower> bookBorrowerCollection) {
        this.bookBorrowerCollection = bookBorrowerCollection;
    }

    public BookBorrower getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(BookBorrower borrowerId) {
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
    public List<BookOverdue> getBookOverdueList() {
        return tblBookOverdueList;
    }

    public void setBookOverdueList(List<BookOverdue> tblBookOverdueList) {
        this.tblBookOverdueList = tblBookOverdueList;
    }
    
}
