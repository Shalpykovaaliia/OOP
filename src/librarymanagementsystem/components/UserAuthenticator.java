/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.components;

import java.util.List;
import javax.persistence.Query;
import librarymanagementsystem.LibraryManagementSystem;
import librarymanagementsystem.exceptions.UserNotFoundException;
import librarymanagementsystem.models.User;

/**
 *
 * @author User
 */
public class UserAuthenticator {
    
    public static User authenticate(String username, String password) {
        // checks if user exists ,
        User userFound = null;
        Query query = LibraryManagementSystem.APP_ENTITY_MANAGER.createNamedQuery("User.findByUsernamePassword");
        query.setParameter("username", username);
        query.setParameter("password", password);
        List resultList = query.getResultList();
        if (resultList.size() != 0) {
            userFound = (User) resultList.get(0);
        }
        return userFound;
    }
    
}
