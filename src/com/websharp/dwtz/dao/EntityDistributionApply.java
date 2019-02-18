package com.websharp.dwtz.dao;

import java.util.ArrayList;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ENTITY_DISTRIBUTION_APPLY.
 */
public class EntityDistributionApply {

    public String InnerID;
    public String Distribution_LsNo;
    public String Breed;
    public String Old_QC_No;
    public String Distribution_Company;
    public String Distribution_Target_Address;
    public String Distribution_Count;
    public String Remark;
    public String Status;
    public String Apply_User_ID;
    public String Add_UserID;
    public String Add_Time;
    public String Add_IP;
    public String Update_UserID;
    public String Update_Time;
    public String Update_IP;
    public String Old_Distribution_Count;
    public String BreedForElse;

    public ArrayList<EntityDistributionApplyTarget> target_list;
    
    
    public ArrayList<EntityDistributionApplyTarget> getTarget_list() {
		return target_list;
	}

	public void setTarget_list(ArrayList<EntityDistributionApplyTarget> target_list) {
		this.target_list = target_list;
	}

	public EntityDistributionApply() {
    }

    public EntityDistributionApply(String InnerID, String Distribution_LsNo, String Breed, String Old_QC_No, String Distribution_Company, String Distribution_Target_Address, String Distribution_Count, String Remark, String Status, String Apply_User_ID, String Add_UserID, String Add_Time, String Add_IP, String Update_UserID, String Update_Time, String Update_IP, String Old_Distribution_Count, String BreedForElse) {
        this.InnerID = InnerID;
        this.Distribution_LsNo = Distribution_LsNo;
        this.Breed = Breed;
        this.Old_QC_No = Old_QC_No;
        this.Distribution_Company = Distribution_Company;
        this.Distribution_Target_Address = Distribution_Target_Address;
        this.Distribution_Count = Distribution_Count;
        this.Remark = Remark;
        this.Status = Status;
        this.Apply_User_ID = Apply_User_ID;
        this.Add_UserID = Add_UserID;
        this.Add_Time = Add_Time;
        this.Add_IP = Add_IP;
        this.Update_UserID = Update_UserID;
        this.Update_Time = Update_Time;
        this.Update_IP = Update_IP;
        this.Old_Distribution_Count = Old_Distribution_Count;
        this.BreedForElse = BreedForElse;
    }

    public String getInnerID() {
        return InnerID;
    }

    public void setInnerID(String InnerID) {
        this.InnerID = InnerID;
    }

    public String getDistribution_LsNo() {
        return Distribution_LsNo;
    }

    public void setDistribution_LsNo(String Distribution_LsNo) {
        this.Distribution_LsNo = Distribution_LsNo;
    }

    public String getBreed() {
        return Breed;
    }

    public void setBreed(String Breed) {
        this.Breed = Breed;
    }

    public String getOld_QC_No() {
        return Old_QC_No;
    }

    public void setOld_QC_No(String Old_QC_No) {
        this.Old_QC_No = Old_QC_No;
    }

    public String getDistribution_Company() {
        return Distribution_Company;
    }

    public void setDistribution_Company(String Distribution_Company) {
        this.Distribution_Company = Distribution_Company;
    }

    public String getDistribution_Target_Address() {
        return Distribution_Target_Address;
    }

    public void setDistribution_Target_Address(String Distribution_Target_Address) {
        this.Distribution_Target_Address = Distribution_Target_Address;
    }

    public String getDistribution_Count() {
        return Distribution_Count;
    }

    public void setDistribution_Count(String Distribution_Count) {
        this.Distribution_Count = Distribution_Count;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getApply_User_ID() {
        return Apply_User_ID;
    }

    public void setApply_User_ID(String Apply_User_ID) {
        this.Apply_User_ID = Apply_User_ID;
    }

    public String getAdd_UserID() {
        return Add_UserID;
    }

    public void setAdd_UserID(String Add_UserID) {
        this.Add_UserID = Add_UserID;
    }

    public String getAdd_Time() {
        return Add_Time;
    }

    public void setAdd_Time(String Add_Time) {
        this.Add_Time = Add_Time;
    }

    public String getAdd_IP() {
        return Add_IP;
    }

    public void setAdd_IP(String Add_IP) {
        this.Add_IP = Add_IP;
    }

    public String getUpdate_UserID() {
        return Update_UserID;
    }

    public void setUpdate_UserID(String Update_UserID) {
        this.Update_UserID = Update_UserID;
    }

    public String getUpdate_Time() {
        return Update_Time;
    }

    public void setUpdate_Time(String Update_Time) {
        this.Update_Time = Update_Time;
    }

    public String getUpdate_IP() {
        return Update_IP;
    }

    public void setUpdate_IP(String Update_IP) {
        this.Update_IP = Update_IP;
    }

    public String getOld_Distribution_Count() {
        return Old_Distribution_Count;
    }

    public void setOld_Distribution_Count(String Old_Distribution_Count) {
        this.Old_Distribution_Count = Old_Distribution_Count;
    }

    public String getBreedForElse() {
        return BreedForElse;
    }

    public void setBreedForElse(String BreedForElse) {
        this.BreedForElse = BreedForElse;
    }

}
