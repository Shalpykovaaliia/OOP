/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.forms;

/**
 *
 * @author User
 */
public class UserForm {

    protected String scenario;

    private String username;
    private String password;
    private String retypePassword;
    private String firstname;
    private String lastname;
    private String emailAddress;
    private String phoneNumber;

    public UserForm(String scenario, String username, String password, String retypePassword, String firstname, String lastname, String emailAddress, String phoneNumber) {
        this.scenario = scenario;
        this.username = username;
        this.password = password;
        this.retypePassword = retypePassword;
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }
    

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRetypePassword() {
        return retypePassword;
    }

    public void setRetypePassword(String retypePassword) {
        this.retypePassword = retypePassword;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Get the value of username
     *
     * @return the value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the value of username
     *
     * @param username new value of username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    public boolean validate(){
        return false;
    }
    public boolean save(){
        // save the new User 
        // save the profile of the new model
        return false;
    }
}
