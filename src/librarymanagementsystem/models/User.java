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
@Table(name = "tbl_user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
    , @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id")
    , @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username")
    , @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password")
    , @NamedQuery(name = "User.findByUsernamePassword", query = "SELECT u FROM User u WHERE u.username = :username and u.password = :password")
    , @NamedQuery(name = "User.findByFirstSecretQuestion", query = "SELECT u FROM User u WHERE u.firstSecretQuestion = :firstSecretQuestion")
    , @NamedQuery(name = "User.findByFirstSecretAnswer", query = "SELECT u FROM User u WHERE u.firstSecretAnswer = :firstSecretAnswer")
    , @NamedQuery(name = "User.findBySecondSecretQuestion", query = "SELECT u FROM User u WHERE u.secondSecretQuestion = :secondSecretQuestion")
    , @NamedQuery(name = "User.findBySecondSecretAnswer", query = "SELECT u FROM User u WHERE u.secondSecretAnswer = :secondSecretAnswer")
    , @NamedQuery(name = "User.findByThirdSecretQuestion", query = "SELECT u FROM User u WHERE u.thirdSecretQuestion = :thirdSecretQuestion")
    , @NamedQuery(name = "User.findByThirdSecretAnswer", query = "SELECT u FROM User u WHERE u.thirdSecretAnswer = :thirdSecretAnswer")
    , @NamedQuery(name = "User.findByRole", query = "SELECT u FROM User u WHERE u.role = :role")})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "first_secret_question")
    private String firstSecretQuestion;
    @Basic(optional = false)
    @Column(name = "first_secret_answer")
    private String firstSecretAnswer;
    @Basic(optional = false)
    @Column(name = "second_secret_question")
    private String secondSecretQuestion;
    @Basic(optional = false)
    @Column(name = "second_secret_answer")
    private String secondSecretAnswer;
    @Basic(optional = false)
    @Column(name = "third_secret_question")
    private String thirdSecretQuestion;
    @Basic(optional = false)
    @Column(name = "third_secret_answer")
    private String thirdSecretAnswer;
    @Basic(optional = false)
    @Column(name = "role")
    private String role;
    @JoinColumn(name = "profile_id", referencedColumnName = "profile_id")
    @ManyToOne(optional = false)
    private Profile profile;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String username, String password, String firstSecretQuestion, String firstSecretAnswer, String secondSecretQuestion, String secondSecretAnswer, String thirdSecretQuestion, String thirdSecretAnswer, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstSecretQuestion = firstSecretQuestion;
        this.firstSecretAnswer = firstSecretAnswer;
        this.secondSecretQuestion = secondSecretQuestion;
        this.secondSecretAnswer = secondSecretAnswer;
        this.thirdSecretQuestion = thirdSecretQuestion;
        this.thirdSecretAnswer = thirdSecretAnswer;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstSecretQuestion() {
        return firstSecretQuestion;
    }

    public void setFirstSecretQuestion(String firstSecretQuestion) {
        this.firstSecretQuestion = firstSecretQuestion;
    }

    public String getFirstSecretAnswer() {
        return firstSecretAnswer;
    }

    public void setFirstSecretAnswer(String firstSecretAnswer) {
        this.firstSecretAnswer = firstSecretAnswer;
    }

    public String getSecondSecretQuestion() {
        return secondSecretQuestion;
    }

    public void setSecondSecretQuestion(String secondSecretQuestion) {
        this.secondSecretQuestion = secondSecretQuestion;
    }

    public String getSecondSecretAnswer() {
        return secondSecretAnswer;
    }

    public void setSecondSecretAnswer(String secondSecretAnswer) {
        this.secondSecretAnswer = secondSecretAnswer;
    }

    public String getThirdSecretQuestion() {
        return thirdSecretQuestion;
    }

    public void setThirdSecretQuestion(String thirdSecretQuestion) {
        this.thirdSecretQuestion = thirdSecretQuestion;
    }

    public String getThirdSecretAnswer() {
        return thirdSecretAnswer;
    }

    public void setThirdSecretAnswer(String thirdSecretAnswer) {
        this.thirdSecretAnswer = thirdSecretAnswer;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profileId) {
        this.profile = profileId;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "librarymanagementsystem.models.User[ id=" + id + " ]";
    }
    
}
