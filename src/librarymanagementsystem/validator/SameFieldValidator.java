/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.validator;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.scene.control.TextInputControl;

/**
 *
 * @author User
 */
public class SameFieldValidator extends ValidatorBase{
    private javafx.scene.control.TextInputControl retypedInput;
    public SameFieldValidator(String message, javafx.scene.control.TextInputControl retypedInput) {
        super(message);
        this.retypedInput = retypedInput;
    }

    
    @Override
    protected void eval() {
        TextInputControl currentInput = (TextInputControl)srcControl.get();
        System.out.println(currentInput.getText());
        System.out.println(this.retypedInput.getText());
        Boolean isEqual = new Boolean(currentInput.getText().equals(this.retypedInput.getText()));
        System.out.println("Is equal : "+isEqual.toString());
        if(isEqual){
            hasErrors.set(false);
        }else{
            hasErrors.set(true);
        }
    }
    
}
