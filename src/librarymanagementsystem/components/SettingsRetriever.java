/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.components;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import librarymanagementsystem.facade.SettingFacade;
import librarymanagementsystem.models.Setting;

/**
 *
 * @author User
 */
public class SettingsRetriever {

    private EntityManager em;
    private EntityManagerFactory emf;
    protected SettingFacade settingsFacade;

    public SettingsRetriever() {
        this.emf = librarymanagementsystem.LibraryManagementSystem.APP_ENTITY_MANAGER_FACTORY;
        this.em = emf.createEntityManager();
        this.settingsFacade = new SettingFacade(emf);
    }

    public String getApiCode() {
        TypedQuery<Setting> query = em.createNamedQuery("Setting.smsApiCode", Setting.class);
        Setting returnedSetting = null;
        try {
            returnedSetting = query.getSingleResult();
            return returnedSetting.getSettingValue();
        } catch (javax.persistence.NoResultException ex) {
            // create the setting , default to enabled
            Setting notificationStatus = new Setting();
            notificationStatus.setSettingName("SMS_API_CODE");
            notificationStatus.setSettingValue("");
            this.settingsFacade.create(notificationStatus);
            return notificationStatus.getSettingValue();
        }
    }

    public String getSmsSenderName() {
        TypedQuery<Setting> query = em.createNamedQuery("Setting.smsSenderName", Setting.class);
        try {
            Setting returnedSetting = query.getSingleResult();
            return returnedSetting.getSettingValue();
        } catch (javax.persistence.NoResultException ex) {
            Setting notificationStatus = new Setting();
            notificationStatus.setSettingName("SMS_SENDER_NAME");
            notificationStatus.setSettingValue("SEMAPHORE");
            this.settingsFacade.create(notificationStatus);
            return notificationStatus.getSettingValue();
        }
    }

    public String getNotificationStatus() {
        TypedQuery<Setting> query = em.createNamedQuery("Setting.notificationStatus", Setting.class);
        Setting returnedSetting = null;
        try {
            returnedSetting = query.getSingleResult();
            return returnedSetting.getSettingValue();
        } catch (javax.persistence.NoResultException ex) {
            Setting notificationStatus = new Setting();
            notificationStatus.setSettingName("NOTIFICATION_STATUS");
            notificationStatus.setSettingValue("enabled");
            this.settingsFacade.create(notificationStatus);
            return notificationStatus.getSettingValue();
        }
    }

    public double getPenaltyPerDay() {
        TypedQuery<Setting> query = em.createNamedQuery("Setting.penaltyPerDay", Setting.class);
        Setting returnedSetting = null;
        try {
            returnedSetting = query.getSingleResult();
            return Double.parseDouble(returnedSetting.getSettingValue());
        } catch (javax.persistence.NoResultException ex) {
            Setting penaltyPerDaySetting = new Setting();
            penaltyPerDaySetting.setSettingName("BOOK_PENALTY_PER_DAY");
            penaltyPerDaySetting.setSettingValue("2");
            this.settingsFacade.create(penaltyPerDaySetting);
            return 2;
        }
    }
}
