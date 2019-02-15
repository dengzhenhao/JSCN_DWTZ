package com.websharp.dwtz.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.websharp.dwtz.dao.EntityDistributionApply;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ENTITY_DISTRIBUTION_APPLY.
*/
public class EntityDistributionApplyDao extends AbstractDao<EntityDistributionApply, Void> {

    public static final String TABLENAME = "ENTITY_DISTRIBUTION_APPLY";

    /**
     * Properties of entity EntityDistributionApply.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property InnerID = new Property(0, String.class, "InnerID", false, "INNER_ID");
        public final static Property Distribution_LsNo = new Property(1, String.class, "Distribution_LsNo", false, "DISTRIBUTION__LS_NO");
        public final static Property Breed = new Property(2, String.class, "Breed", false, "BREED");
        public final static Property Old_QC_No = new Property(3, String.class, "Old_QC_No", false, "OLD__QC__NO");
        public final static Property Distribution_Company = new Property(4, String.class, "Distribution_Company", false, "DISTRIBUTION__COMPANY");
        public final static Property Distribution_Target_Address = new Property(5, String.class, "Distribution_Target_Address", false, "DISTRIBUTION__TARGET__ADDRESS");
        public final static Property Distribution_Count = new Property(6, String.class, "Distribution_Count", false, "DISTRIBUTION__COUNT");
        public final static Property Remark = new Property(7, String.class, "Remark", false, "REMARK");
        public final static Property Status = new Property(8, String.class, "Status", false, "STATUS");
        public final static Property Apply_User_ID = new Property(9, String.class, "Apply_User_ID", false, "APPLY__USER__ID");
        public final static Property Add_UserID = new Property(10, String.class, "Add_UserID", false, "ADD__USER_ID");
        public final static Property Add_Time = new Property(11, String.class, "Add_Time", false, "ADD__TIME");
        public final static Property Add_IP = new Property(12, String.class, "Add_IP", false, "ADD__IP");
        public final static Property Update_UserID = new Property(13, String.class, "Update_UserID", false, "UPDATE__USER_ID");
        public final static Property Update_Time = new Property(14, String.class, "Update_Time", false, "UPDATE__TIME");
        public final static Property Update_IP = new Property(15, String.class, "Update_IP", false, "UPDATE__IP");
        public final static Property Old_Distribution_Count = new Property(16, String.class, "Old_Distribution_Count", false, "OLD__DISTRIBUTION__COUNT");
        public final static Property BreedForElse = new Property(17, String.class, "BreedForElse", false, "BREED_FOR_ELSE");
    };


    public EntityDistributionApplyDao(DaoConfig config) {
        super(config);
    }
    
    public EntityDistributionApplyDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ENTITY_DISTRIBUTION_APPLY' (" + //
                "'INNER_ID' TEXT," + // 0: InnerID
                "'DISTRIBUTION__LS_NO' TEXT," + // 1: Distribution_LsNo
                "'BREED' TEXT," + // 2: Breed
                "'OLD__QC__NO' TEXT," + // 3: Old_QC_No
                "'DISTRIBUTION__COMPANY' TEXT," + // 4: Distribution_Company
                "'DISTRIBUTION__TARGET__ADDRESS' TEXT," + // 5: Distribution_Target_Address
                "'DISTRIBUTION__COUNT' TEXT," + // 6: Distribution_Count
                "'REMARK' TEXT," + // 7: Remark
                "'STATUS' TEXT," + // 8: Status
                "'APPLY__USER__ID' TEXT," + // 9: Apply_User_ID
                "'ADD__USER_ID' TEXT," + // 10: Add_UserID
                "'ADD__TIME' TEXT," + // 11: Add_Time
                "'ADD__IP' TEXT," + // 12: Add_IP
                "'UPDATE__USER_ID' TEXT," + // 13: Update_UserID
                "'UPDATE__TIME' TEXT," + // 14: Update_Time
                "'UPDATE__IP' TEXT," + // 15: Update_IP
                "'OLD__DISTRIBUTION__COUNT' TEXT," + // 16: Old_Distribution_Count
                "'BREED_FOR_ELSE' TEXT);"); // 17: BreedForElse
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ENTITY_DISTRIBUTION_APPLY'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, EntityDistributionApply entity) {
        stmt.clearBindings();
 
        String InnerID = entity.getInnerID();
        if (InnerID != null) {
            stmt.bindString(1, InnerID);
        }
 
        String Distribution_LsNo = entity.getDistribution_LsNo();
        if (Distribution_LsNo != null) {
            stmt.bindString(2, Distribution_LsNo);
        }
 
        String Breed = entity.getBreed();
        if (Breed != null) {
            stmt.bindString(3, Breed);
        }
 
        String Old_QC_No = entity.getOld_QC_No();
        if (Old_QC_No != null) {
            stmt.bindString(4, Old_QC_No);
        }
 
        String Distribution_Company = entity.getDistribution_Company();
        if (Distribution_Company != null) {
            stmt.bindString(5, Distribution_Company);
        }
 
        String Distribution_Target_Address = entity.getDistribution_Target_Address();
        if (Distribution_Target_Address != null) {
            stmt.bindString(6, Distribution_Target_Address);
        }
 
        String Distribution_Count = entity.getDistribution_Count();
        if (Distribution_Count != null) {
            stmt.bindString(7, Distribution_Count);
        }
 
        String Remark = entity.getRemark();
        if (Remark != null) {
            stmt.bindString(8, Remark);
        }
 
        String Status = entity.getStatus();
        if (Status != null) {
            stmt.bindString(9, Status);
        }
 
        String Apply_User_ID = entity.getApply_User_ID();
        if (Apply_User_ID != null) {
            stmt.bindString(10, Apply_User_ID);
        }
 
        String Add_UserID = entity.getAdd_UserID();
        if (Add_UserID != null) {
            stmt.bindString(11, Add_UserID);
        }
 
        String Add_Time = entity.getAdd_Time();
        if (Add_Time != null) {
            stmt.bindString(12, Add_Time);
        }
 
        String Add_IP = entity.getAdd_IP();
        if (Add_IP != null) {
            stmt.bindString(13, Add_IP);
        }
 
        String Update_UserID = entity.getUpdate_UserID();
        if (Update_UserID != null) {
            stmt.bindString(14, Update_UserID);
        }
 
        String Update_Time = entity.getUpdate_Time();
        if (Update_Time != null) {
            stmt.bindString(15, Update_Time);
        }
 
        String Update_IP = entity.getUpdate_IP();
        if (Update_IP != null) {
            stmt.bindString(16, Update_IP);
        }
 
        String Old_Distribution_Count = entity.getOld_Distribution_Count();
        if (Old_Distribution_Count != null) {
            stmt.bindString(17, Old_Distribution_Count);
        }
 
        String BreedForElse = entity.getBreedForElse();
        if (BreedForElse != null) {
            stmt.bindString(18, BreedForElse);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public EntityDistributionApply readEntity(Cursor cursor, int offset) {
        EntityDistributionApply entity = new EntityDistributionApply( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // InnerID
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Distribution_LsNo
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // Breed
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // Old_QC_No
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // Distribution_Company
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // Distribution_Target_Address
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // Distribution_Count
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // Remark
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // Status
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // Apply_User_ID
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // Add_UserID
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // Add_Time
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // Add_IP
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // Update_UserID
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // Update_Time
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // Update_IP
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // Old_Distribution_Count
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17) // BreedForElse
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, EntityDistributionApply entity, int offset) {
        entity.setInnerID(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setDistribution_LsNo(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setBreed(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setOld_QC_No(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDistribution_Company(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDistribution_Target_Address(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDistribution_Count(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setRemark(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setStatus(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setApply_User_ID(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setAdd_UserID(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setAdd_Time(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setAdd_IP(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setUpdate_UserID(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setUpdate_Time(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setUpdate_IP(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setOld_Distribution_Count(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setBreedForElse(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(EntityDistributionApply entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(EntityDistributionApply entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}