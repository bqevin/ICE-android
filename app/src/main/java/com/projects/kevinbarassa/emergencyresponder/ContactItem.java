package com.projects.kevinbarassa.emergencyresponder;

/**
 * Created by Kevin Barassa on 17-Jan-17.
 */

public class ContactItem {
    private String ice_name;
    private String ice_blood;
    private String ice_phone;
    private String ice_email;
    private String ice_residence;

    //Blank constructor
    public ContactItem(){}

    //Main constructor
    public ContactItem(String ice_name, String ice_blood, String ice_phone, String ice_email, String ice_residence) {
        this.ice_name = ice_name;
        this.ice_blood = ice_blood;
        this.ice_phone = ice_phone;
        this.ice_email = ice_email;
        this.ice_residence = ice_residence;
    }

    public String getIce_name() {
        return ice_name;
    }

    public void setIce_name(String ice_name) {
        this.ice_name = ice_name;
    }

    public String getIce_blood() {
        return ice_blood;
    }

    public void setIce_blood(String ice_blood) {
        this.ice_blood = ice_blood;
    }

    public String getIce_phone() {
        return ice_phone;
    }

    public void setIce_phone(String ice_phone) {
        this.ice_phone = ice_phone;
    }

    public String getIce_email() {
        return ice_email;
    }

    public void setIce_email(String ice_email) {
        this.ice_email = ice_email;
    }

    public String getIce_residence() {
        return ice_residence;
    }

    public void setIce_residence(String ice_residence) {
        this.ice_residence = ice_residence;
    }
}
