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
public class InvalidBarcodeIdValidator extends ValidatorBase {

    protected void eval() {
        TextInputControl currentInput = (TextInputControl) srcControl.get();
        
        double parseContent = Double.parseDouble(currentInput.getText());
        boolean isWithinIntLimit = (parseContent >= 1 && parseContent <= Double.MAX_VALUE);
        hasErrors.set(!isWithinIntLimit);
    }
}
