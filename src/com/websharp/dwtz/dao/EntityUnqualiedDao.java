package com.websharp.dwtz.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.websharp.dwtz.dao.EntityUnqualied;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ENTITY_UNQUALIED.
*/
public class EntityUnqualiedDao extends AbstractDao<EntityUnqualied, Void> {

    public static final String TABLENAME = "ENTITY_UNQUALIED";

    /**
     * Properties of entity EntityUnqualied.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property InnerID = new Property(0, String.class, "InnerID", false, "INNER_ID");
        public final static Property QuarantineId = new Property(1, String.class, "QuarantineId", false, "QUARANTINE_ID");
        public final static Property UnqualiedScanCode = new Property(2, String.class, "UnqualiedScanCode", false, "UNQUALIED_SCAN_CODE");
        public final static Property UnqualiedScanCode_2 = new Property(3, String.class, "UnqualiedScanCode_2", false, "UNQUALIED_SCAN_CODE_2");
        public final static Property ButcheryID = new Property(4, String.class, "ButcheryID", false, "BUTCHERY_ID");
        public final static Property DeliveryNum = new Property(5, String.class, "DeliveryNum", false, "DELIVERY_NUM");
        public final static Property SendTime = new Property(6, String.class, "SendTime", false, "SEND_TIME");
        public final static Property GoodsOwner = new Property(7, String.class, "GoodsOwner", false, "GOODS_OWNER");
        public final static Property Origin = new Property(8, String.class, "Origin", false, "ORIGIN");
        public final static Property QuarantineNum = new Property(9, String.class, "QuarantineNum", false, "QUARANTINE_NUM");
        public final static Property ImmuneTag = new Property(10, String.class, "ImmuneTag", false, "IMMUNE_TAG");
        public final static Property CheckCount = new Property(11, String.class, "CheckCount", false, "CHECK_COUNT");
        public final static Property ProcessReason = new Property(12, String.class, "ProcessReason", false, "PROCESS_REASON");
        public final static Property ProcessComment = new Property(13, String.class, "ProcessComment", false, "PROCESS_COMMENT");
        public final static Property OfficalVeterSign = new Property(14, String.class, "OfficalVeterSign", false, "OFFICAL_VETER_SIGN");
        public final static Property Remark = new Property(15, String.class, "Remark", false, "REMARK");
        public final static Property Add_UserID = new Property(16, String.class, "Add_UserID", false, "ADD__USER_ID");
        public final static Property Add_Time = new Property(17, String.class, "Add_Time", false, "ADD__TIME");
        public final static Property StaffNo = new Property(18, String.class, "StaffNo", false, "STAFF_NO");
        public final static Property ButcheryGroupID = new Property(19, String.class, "ButcheryGroupID", false, "BUTCHERY_GROUP_ID");
        public final static Property Weight = new Property(20, String.class, "Weight", false, "WEIGHT");
        public final static Property Type = new Property(21, String.class, "Type", false, "TYPE");
    };


    public EntityUnqualiedDao(DaoConfig config) {
        super(config);
    }
    
    public EntityUnqualiedDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ENTITY_UNQUALIED' (" + //
                "'INNER_ID' TEXT," + // 0: InnerID
                "'QUARANTINE_ID' TEXT," + // 1: QuarantineId
                "'UNQUALIED_SCAN_CODE' TEXT," + // 2: UnqualiedScanCode
                "'UNQUALIED_SCAN_CODE_2' TEXT," + // 3: UnqualiedScanCode_2
                "'BUTCHERY_ID' TEXT," + // 4: ButcheryID
                "'DELIVERY_NUM' TEXT," + // 5: DeliveryNum
                "'SEND_TIME' TEXT," + // 6: SendTime
                "'GOODS_OWNER' TEXT," + // 7: GoodsOwner
                "'ORIGIN' TEXT," + // 8: Origin
                "'QUARANTINE_NUM' TEXT," + // 9: QuarantineNum
                "'IMMUNE_TAG' TEXT," + // 10: ImmuneTag
                "'CHECK_COUNT' TEXT," + // 11: CheckCount
                "'PROCESS_REASON' TEXT," + // 12: ProcessReason
                "'PROCESS_COMMENT' TEXT," + // 13: ProcessComment
                "'OFFICAL_VETER_SIGN' TEXT," + // 14: OfficalVeterSign
                "'REMARK' TEXT," + // 15: Remark
                "'ADD__USER_ID' TEXT," + // 16: Add_UserID
                "'ADD__TIME' TEXT," + // 17: Add_Time
                "'STAFF_NO' TEXT," + // 18: StaffNo
                "'BUTCHERY_GROUP_ID' TEXT," + // 19: ButcheryGroupID
                "'WEIGHT' TEXT," + // 20: Weight
                "'TYPE' TEXT);"); // 21: Type
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ENTITY_UNQUALIED'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, EntityUnqualied entity) {
        stmt.clearBindings();
 
        String InnerID = entity.getInnerID();
        if (InnerID != null) {
            stmt.bindString(1, InnerID);
        }
 
        String QuarantineId = entity.getQuarantineId();
        if (QuarantineId != null) {
            stmt.bindString(2, QuarantineId);
        }
 
        String UnqualiedScanCode = entity.getUnqualiedScanCode();
        if (UnqualiedScanCode != null) {
            stmt.bindString(3, UnqualiedScanCode);
        }
 
        String UnqualiedScanCode_2 = entity.getUnqualiedScanCode_2();
        if (UnqualiedScanCode_2 != null) {
            stmt.bindString(4, UnqualiedScanCode_2);
        }
 
        String ButcheryID = entity.getButcheryID();
        if (ButcheryID != null) {
            stmt.bindString(5, ButcheryID);
        }
 
        String DeliveryNum = entity.getDeliveryNum();
        if (DeliveryNum != null) {
            stmt.bindString(6, DeliveryNum);
        }
 
        String SendTime = entity.getSendTime();
        if (SendTime != null) {
            stmt.bindString(7, SendTime);
        }
 
        String GoodsOwner = entity.getGoodsOwner();
        if (GoodsOwner != null) {
            stmt.bindString(8, GoodsOwner);
        }
 
        String Origin = entity.getOrigin();
        if (Origin != null) {
            stmt.bindString(9, Origin);
        }
 
        String QuarantineNum = entity.getQuarantineNum();
        if (QuarantineNum != null) {
            stmt.bindString(10, QuarantineNum);
        }
 
        String ImmuneTag = entity.getImmuneTag();
        if (ImmuneTag != null) {
            stmt.bindString(11, ImmuneTag);
        }
 
        String CheckCount = entity.getCheckCount();
        if (CheckCount != null) {
            stmt.bindString(12, CheckCount);
        }
 
        String ProcessReason = entity.getProcessReason();
        if (ProcessReason != null) {
            stmt.bindString(13, ProcessReason);
        }
 
        String ProcessComment = entity.getProcessComment();
        if (ProcessComment != null) {
            stmt.bindString(14, ProcessComment);
        }
 
        String OfficalVeterSign = entity.getOfficalVeterSign();
        if (OfficalVeterSign != null) {
            stmt.bindString(15, OfficalVeterSign);
        }
 
        String Remark = entity.getRemark();
        if (Remark != null) {
            stmt.bindString(16, Remark);
        }
 
        String Add_UserID = entity.getAdd_UserID();
        if (Add_UserID != null) {
            stmt.bindString(17, Add_UserID);
        }
 
        String Add_Time = entity.getAdd_Time();
        if (Add_Time != null) {
            stmt.bindString(18, Add_Time);
        }
 
        String StaffNo = entity.getStaffNo();
        if (StaffNo != null) {
            stmt.bindString(19, StaffNo);
        }
 
        String ButcheryGroupID = entity.getButcheryGroupID();
        if (ButcheryGroupID != null) {
            stmt.bindString(20, ButcheryGroupID);
        }
 
        String Weight = entity.getWeight();
        if (Weight != null) {
            stmt.bindString(21, Weight);
        }
 
        String Type = entity.getType();
        if (Type != null) {
            stmt.bindString(22, Type);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public EntityUnqualied readEntity(Cursor cursor, int offset) {
        EntityUnqualied entity = new EntityUnqualied( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // InnerID
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // QuarantineId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // UnqualiedScanCode
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // UnqualiedScanCode_2
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // ButcheryID
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // DeliveryNum
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // SendTime
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // GoodsOwner
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // Origin
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // QuarantineNum
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // ImmuneTag
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // CheckCount
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // ProcessReason
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // ProcessComment
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // OfficalVeterSign
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // Remark
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // Add_UserID
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // Add_Time
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // StaffNo
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // ButcheryGroupID
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // Weight
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21) // Type
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, EntityUnqualied entity, int offset) {
        entity.setInnerID(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setQuarantineId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUnqualiedScanCode(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUnqualiedScanCode_2(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setButcheryID(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDeliveryNum(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setSendTime(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setGoodsOwner(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setOrigin(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setQuarantineNum(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setImmuneTag(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setCheckCount(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setProcessReason(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setProcessComment(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setOfficalVeterSign(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setRemark(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setAdd_UserID(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setAdd_Time(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setStaffNo(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setButcheryGroupID(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setWeight(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setType(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(EntityUnqualied entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(EntityUnqualied entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
