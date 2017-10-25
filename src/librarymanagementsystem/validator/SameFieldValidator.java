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
        
        if(!currentInput.getText().equals(this.retypedInput.getText())){
            hasErrors.set(true);
        }else{
            hasErrors.set(false);
        }
    }
    
}
