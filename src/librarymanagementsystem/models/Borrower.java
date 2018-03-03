/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.models;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "tbl_borrower")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Borrower.findAll", query = "SELECT b FROM Borrower b")
    , @NamedQuery(name = "Borrower.findByBorrowerId", query = "SELECT b FROM Borrower b WHERE b.borrowerId = :borrowerId")
    , @NamedQuery(name = "Borrower.findByBarcode", query = "SELECT b FROM Borrower b WHERE b.borrowerBarcodeId = :borrowerBarcode")
    , @NamedQuery(name = "Borrower.findByTitle", query = "SELECT b FROM Borrower b WHERE b.title = :title")
    , @NamedQuery(name = "Borrower.findByFirstname", query = "SELECT b FROM Borrower b WHERE b.firstname = :firstname")
    , @NamedQuery(name = "Borrower.findByLastname", query = "SELECT b FROM Borrower b WHERE b.lastname = :lastname")
    , @NamedQuery(name = "Borrower.findByFullname", query = "SELECT b FROM Borrower b WHERE concat(b.title,' ',b.firstname,' ',b.lastname) = :fullName")
    , @NamedQuery(name = "Borrower.findByBirthday", query = "SELECT b FROM Borrower b WHERE b.birthday = :birthday")
    , @NamedQuery(name = "Borrower.findByGender", query = "SELECT b FROM Borrower b WHERE b.gender = :gender")
    , @NamedQuery(name = "Borrower.findByAddress1", query = "SELECT b FROM Borrower b WHERE b.address1 = :address1")
    , @NamedQuery(name = "Borrower.findByAddress2", query = "SELECT b FROM Borrower b WHERE b.address2 = :address2")
    , @NamedQuery(name = "Borrower.findByAddress3", query = "SELECT b FROM Borrower b WHERE b.address3 = :address3")
    , @NamedQuery(name = "Borrower.findByPostalCode", query = "SELECT b FROM Borrower b WHERE b.postalCode = :postalCode")
    , @NamedQuery(name = "Borrower.findByTown", query = "SELECT b FROM Borrower b WHERE b.town = :town")
    , @NamedQuery(name = "Borrower.findByCountry", query = "SELECT b FROM Borrower b WHERE b.country = :country")
    , @NamedQuery(name = "Borrower.findByMobileNumber", query = "SELECT b FROM Borrower b WHERE b.mobileNumber = :mobileNumber")
    , @NamedQuery(name = "Borrower.findByEmailAddress", query = "SELECT b FROM Borrower b WHERE b.emailAddress = :emailAddress")})
public class Borrower implements Serializable {

    @Basic(optional = false)
    @Column(name = "borrower_barcode_id")
    private double borrowerBarcodeId;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "borrower_id")
    private Integer borrowerId;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @Column(name = "firstname")
    private String firstname;
    @Basic(optional = false)
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    private Date birthday;
    @Column(name = "gender")
    private String gender;
    @Column(name = "address1")
    private String address1;
    @Column(name = "address2")
    private String address2;
    @Column(name = "address3")
    private String address3;
    @Column(name = "postal_code")
    private String postalCode;
    @Column(name = "town")
    private String town;
    @Column(name = "country", columnDefinition = "varchar(255) default 'Philippines'")
    private String country;
    @Column(name = "mobile_number")
    private String mobileNumber;
    @Column(name = "email_address")
    private String emailAddress;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "borrowerId")
    private Collection<BookBorrower> bookBorrowerCollection;

    public Borrower() {
    }

    public Borrower(Integer borrowerId) {
        this.borrowerId = borrowerId;
    }

    public Borrower(Integer borrowerId, String title, String firstname, String lastname) {
        this.borrowerId = borrowerId;
        this.title = title;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Integer getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(Integer borrowerId) {
        this.borrowerId = borrowerId;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @XmlTransient
    public Collection<BookBorrower> getBookBorrowerCollection() {
        return bookBorrowerCollection;
    }

    public void setBookBorrowerCollection(Collection<BookBorrower> bookBorrowerCollection) {
        this.bookBorrowerCollection = bookBorrowerCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (borrowerId != null ? borrowerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Borrower)) {
            return false;
        }
        Borrower other = (Borrower) object; 
       if ((this.borrowerId == null && other.borrowerId != null) || (this.borrowerId != null && !this.borrowerId.equals(other.borrowerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "librarymanagementsystem.models.Borrower[ borrowerId=" + borrowerId + " ]";
    }

    public double getBorrowerBarcode() {
        return borrowerBarcodeId;
    }

    public void setBorrowerBarcode(double borrowerBarcodeId) {
        this.borrowerBarcodeId = borrowerBarcodeId;
    }

}
