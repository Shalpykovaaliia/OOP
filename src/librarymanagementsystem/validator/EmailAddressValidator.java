/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.validator;

import com.jfoenix.validation.base.ValidatorBase;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.TextInputControl;

/**
 *
 * @author User
 */
public class EmailAddressValidator extends ValidatorBase {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void eval() {
        TextInputControl currentInput = (TextInputControl) srcControl.get();
        if(EmailAddressValidator.validate(currentInput.getText())){
            hasErrors.set(false);
        }else{
            hasErrors.set(true);
        }
    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }
}
