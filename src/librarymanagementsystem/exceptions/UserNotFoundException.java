/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.exceptions;

/**
 *
 * @author User
 */
public class UserNotFoundException extends Exception{

    @Override
    public String getMessage() {
        return "This invalid username/password combination";
    }

    
}
