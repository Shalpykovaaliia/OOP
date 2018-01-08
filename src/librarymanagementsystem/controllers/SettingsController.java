/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.controllers;

import com.jfoenix.controls.IFXTextInputControl;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import librarymanagementsystem.components.SettingsRetriever;
import librarymanagementsystem.facade.SettingFacade;
import librarymanagementsystem.models.BookBorrower;
import librarymanagementsystem.models.Setting;

/**
 * FXML Controller class
 *
 * @author User
 */
public class SettingsController implements Initializable {

    @FXML
    private JFXTextField smsApicode;

    @FXML
    private JFXTextField smsSenderName;

    @FXML
    private JFXRadioButton enableSMSNotification;

    private EntityManager em;

    private EntityManagerFactory emf;

    protected SettingFacade settingsFacade;

    private RequiredFieldValidator smsApiCodeValidator;

    private RequiredFieldValidator smsSenderNameValidator;

    private ArrayList<IFXTextInputControl> formFields = new ArrayList<>();

    private ArrayList<String> errorMessages = new ArrayList<>();

    @FXML
    void returnBack(ActionEvent event) {
        librarymanagementsystem.LibraryManagementSystem.back();
    }

    @FXML
    void saveSettings(ActionEvent event) {
        String smsApiCode = "", senderName = "", smsNotificationStatus = "";
        this.validateFields();
        Logger.getLogger(SettingsController.class.getName()).log(Level.INFO, "Is error message empty? "+this.errorMessages.isEmpty());
        if (this.errorMessages.isEmpty()) {
            smsApiCode = smsApicode.getText();
            senderName = smsSenderName.getText();
            this.saveApiCode(smsApiCode);
            this.saveSenderName(senderName);
            if (this.enableSMSNotification.isSelected()) {
                smsNotificationStatus = "enabled";
            } else {
                smsNotificationStatus = "disabled";
            }
            this.saveSmsNotification(smsNotificationStatus);

            Alert successMessage = new Alert(Alert.AlertType.INFORMATION);
            successMessage.setTitle("Saved");
            successMessage.setHeaderText("Settings saved");
            successMessage.setContentText("Application settings saved");
            successMessage.showAndWait();
        } else {
            // show error message
            StringBuilder errorMessagBuilder = new StringBuilder();
            //remove dups
            HashSet<String> hashSet = new HashSet<String>();
            hashSet.addAll(errorMessages);
            errorMessages.clear();
            errorMessages.addAll(hashSet);
            Alert validationErrorMessage = new Alert(Alert.AlertType.ERROR);
            for (String errorMessage : errorMessages) {
                errorMessagBuilder.append(errorMessage);
                errorMessagBuilder.append("\r\n");
            }
            String errorMessStr = errorMessagBuilder.toString();
            validationErrorMessage.setTitle("Please fix the following.");
            validationErrorMessage.setContentText(errorMessStr);
            validationErrorMessage.showAndWait();
        }
        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.emf = librarymanagementsystem.LibraryManagementSystem.APP_ENTITY_MANAGER_FACTORY;
        this.em = emf.createEntityManager();
        this.settingsFacade = new SettingFacade(emf);
        
        //disable changing of sender name
        this.smsSenderName.setEditable(false);

        this.setNotificationStatusValue();
        this.initializeValidators();
        this.registerValidator();
        this.registerFormFields();
        this.setDefaultValues();

    }

    private void saveApiCode(String smsApiCode) {
        TypedQuery<Setting> query = em.createNamedQuery("Setting.smsApiCode", Setting.class);
        try {
            Setting returnedSetting = query.getSingleResult();
            returnedSetting.setSettingValue(smsApiCode);
            // update the setting
            this.settingsFacade.edit(returnedSetting);
        } catch (Exception ex) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, ex.getMessage());
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, ex.getCause().getMessage());
        }
    }

    private void saveSenderName(String senderName) {
        TypedQuery<Setting> query = em.createNamedQuery("Setting.smsSenderName", Setting.class);
        try {
            Setting returnedSetting = query.getSingleResult();
            returnedSetting.setSettingValue(senderName);
            // update the setting
            this.settingsFacade.edit(returnedSetting);
        } catch (Exception ex) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, ex.getMessage());
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, ex.getCause().getMessage());
        }
    }

    private void saveSmsNotification(String smsNotificationEnabled) {
        TypedQuery<Setting> query = em.createNamedQuery("Setting.notificationStatus", Setting.class);
        try {
            Setting returnedSetting = query.getSingleResult();
            returnedSetting.setSettingValue(smsNotificationEnabled);
            // update the setting
            this.settingsFacade.edit(returnedSetting);
        } catch (Exception ex) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, ex.getMessage());
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, ex.getCause().getMessage());
        }
    }

    private void initializeValidators() {
        smsApiCodeValidator = new RequiredFieldValidator();
        smsApiCodeValidator.setMessage("ISBN is required");
        smsApiCodeValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

        smsSenderNameValidator = new RequiredFieldValidator();
        smsSenderNameValidator.setMessage("ISBN is required");
        smsSenderNameValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));

    }

    private void setNotificationStatusValue() {
        // retrieve enableSMSNotification 
        TypedQuery<Setting> query = em.createNamedQuery("Setting.notificationStatus", Setting.class);
        try {
            Setting returnedSetting = query.getSingleResult();
            // check value
            if (returnedSetting.getSettingValue().equals("enabled")) {
                enableSMSNotification.setSelected(true);
            } else {
                enableSMSNotification.setSelected(false);
            }
        } catch (Exception ex) {
            enableSMSNotification.setSelected(false);
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, ex.getMessage());
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, ex.getCause().getMessage());
        }

    }

    private void registerValidator() {
        this.smsApicode.getValidators().add(smsApiCodeValidator);
        this.smsSenderName.getValidators().add(smsSenderNameValidator);
    }

    private void registerFormFields() {
        this.formFields.add(this.smsApicode);
        this.formFields.add(this.smsSenderName);
    }

    private void validateFields() {
        this.errorMessages.clear();
        for (IFXTextInputControl formField : formFields) {
            formField.validate();
            if (formField instanceof JFXTextField) {
                JFXTextField formField2 = (JFXTextField) formField;
                ObservableList<ValidatorBase> validators = formField2.getValidators();
                for (ValidatorBase validator : validators) {
                    if (validator.getHasErrors()) {
                        this.errorMessages.add(validator.getMessage());
                    }
                }
            }
        }
    }


    private void setDefaultValues() {
        SettingsRetriever settingRetriever = new SettingsRetriever();
        this.smsApicode.setText(settingRetriever.getApiCode());
        this.smsSenderName.setText(settingRetriever.getSmsSenderName());
        if(settingRetriever.getNotificationStatus().equals("enabled")){
            this.enableSMSNotification.setSelected(true);
        }else{
            this.enableSMSNotification.setSelected(false);
        }
        
    }

}
