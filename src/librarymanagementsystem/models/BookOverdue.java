/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.models;

import java.io.Serializable;
import javax.persistence.Basic;
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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author User
 */
@Entity
@Table(name = "tbl_book_overdue")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BookOverdue.findAll", query = "SELECT b FROM BookOverdue b")
    , @NamedQuery(name = "BookOverdue.findByComputedFee", query = "SELECT b FROM BookOverdue b WHERE b.computedFee = :computedFee")
    , @NamedQuery(name = "BookOverdue.findByPaid", query = "SELECT b FROM BookOverdue b WHERE b.paid = :paid")
    , @NamedQuery(name = "BookOverdue.findByBalance", query = "SELECT b FROM BookOverdue b WHERE b.balance = :balance")
    , @NamedQuery(name = "BookOverdue.findById", query = "SELECT b FROM BookOverdue b WHERE b.id = :id")})
public class BookOverdue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "computed_fee")
    private float computedFee;
    @Basic(optional = false)
    @Column(name = "paid")
    private float paid;
    @Basic(optional = false)
    @Column(name = "balance")
    private float balance;
    @Basic(optional = false)
    @Lob
    @Column(name = "notes")
    private String notes;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "book_borrower_ref_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private BookBorrower bookBorrowerRefId;

    public BookOverdue() {
    }

    public BookOverdue(Integer id) {
        this.id = id;
    }

    public BookOverdue(Integer id, float computedFee, float paid, float balance, String notes) {
        this.id = id;
        this.computedFee = computedFee;
        this.paid = paid;
        this.balance = balance;
        this.notes = notes;
    }

    public float getComputedFee() {
        return computedFee;
    }

    public void setComputedFee(float computedFee) {
        this.computedFee = computedFee;
    }

    public float getPaid() {
        return paid;
    }

    public void setPaid(float paid) {
        this.paid = paid;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BookBorrower getBookBorrowerRefId() {
        return bookBorrowerRefId;
    }

    public void setBookBorrowerRefId(BookBorrower bookBorrowerRefId) {
        this.bookBorrowerRefId = bookBorrowerRefId;
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
        if (!(object instanceof BookOverdue)) {
            return false;
        }
        BookOverdue other = (BookOverdue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "librarymanagementsystem.models.BookOverdue[ id=" + id + " ]";
    }
    
}
