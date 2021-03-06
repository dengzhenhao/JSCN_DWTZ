package com.websharp.dwtz.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ENTITY_BUTCHERY.
 */
public class EntityButchery {

    public String InnerID;
    public String ButcheryName;
    public String Address;
    public String Telephone;
    public String Master;
    public String MasterTelephone;

    public EntityButchery() {
    }

    public EntityButchery(String InnerID, String ButcheryName, String Address, String Telephone, String Master, String MasterTelephone) {
        this.InnerID = InnerID;
        this.ButcheryName = ButcheryName;
        this.Address = Address;
        this.Telephone = Telephone;
        this.Master = Master;
        this.MasterTelephone = MasterTelephone;
    }

    public String getInnerID() {
        return InnerID;
    }

    public void setInnerID(String InnerID) {
        this.InnerID = InnerID;
    }

    public String getButcheryName() {
        return ButcheryName;
    }

    public void setButcheryName(String ButcheryName) {
        this.ButcheryName = ButcheryName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String Telephone) {
        this.Telephone = Telephone;
    }

    public String getMaster() {
        return Master;
    }

    public void setMaster(String Master) {
        this.Master = Master;
    }

    public String getMasterTelephone() {
        return MasterTelephone;
    }

    public void setMasterTelephone(String MasterTelephone) {
        this.MasterTelephone = MasterTelephone;
    }

}
