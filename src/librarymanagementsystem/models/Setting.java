/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author User
 */
@Entity
@Table(name = "tbl_settings")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Setting.findAll", query = "SELECT s FROM Setting s")
    , @NamedQuery(name = "Setting.findById", query = "SELECT s FROM Setting s WHERE s.id = :id")
    , @NamedQuery(name = "Setting.findBySettingName", query = "SELECT s FROM Setting s WHERE s.settingName = :settingName")
    , @NamedQuery(name = "Setting.smsApiCode", query = "SELECT s FROM Setting s WHERE s.settingName = 'SMS_API_CODE'")
    , @NamedQuery(name = "Setting.smsSenderName", query = "SELECT s FROM Setting s WHERE s.settingName = 'SMS_SENDER_NAME'")
    , @NamedQuery(name = "Setting.notificationStatus", query = "SELECT s FROM Setting s WHERE s.settingName = 'NOTIFICATION_STATUS'")
    , @NamedQuery(name = "Setting.findBySettingValue", query = "SELECT s FROM Setting s WHERE s.settingValue = :settingValue")})
public class Setting implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "setting_name")
    private String settingName;
    @Basic(optional = false)
    @Column(name = "setting_value")
    private String settingValue;

    public Setting() {
    }

    public Setting(Integer id) {
        this.id = id;
    }

    public Setting(Integer id, String settingName, String settingValue) {
        this.id = id;
        this.settingName = settingName;
        this.settingValue = settingValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Setting)) {
            return false;
        }
        Setting other = (Setting) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "librarymanagementsystem.models.Setting[ id=" + id + " ]";
    }
    
}
