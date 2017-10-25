/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.validator;

import com.jfoenix.validation.base.ValidatorBase;
import java.text.ParseException;
import javafx.scene.control.TextInputControl;

/**
 *
 * @author User
 */
public class IntegerFieldValidator extends ValidatorBase{

    @Override
    protected void eval() {
        TextInputControl currentInput = (TextInputControl) srcControl.get();
        try{
            Integer.parseInt(currentInput.getText());
            hasErrors.set(false);
        }catch(NumberFormatException ex){
            hasErrors.set(true);
        }
        
    }
    
}
