package com.websharp.dwtz.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.websharp.dwtz.dao.EntityButchery;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ENTITY_BUTCHERY.
*/
public class EntityButcheryDao extends AbstractDao<EntityButchery, Void> {

    public static final String TABLENAME = "ENTITY_BUTCHERY";

    /**
     * Properties of entity EntityButchery.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property InnerID = new Property(0, String.class, "InnerID", false, "INNER_ID");
        public final static Property ButcheryName = new Property(1, String.class, "ButcheryName", false, "BUTCHERY_NAME");
        public final static Property Address = new Property(2, String.class, "Address", false, "ADDRESS");
        public final static Property Telephone = new Property(3, String.class, "Telephone", false, "TELEPHONE");
        public final static Property Master = new Property(4, String.class, "Master", false, "MASTER");
        public final static Property MasterTelephone = new Property(5, String.class, "MasterTelephone", false, "MASTER_TELEPHONE");
    };


    public EntityButcheryDao(DaoConfig config) {
        super(config);
    }
    
    public EntityButcheryDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ENTITY_BUTCHERY' (" + //
                "'INNER_ID' TEXT," + // 0: InnerID
                "'BUTCHERY_NAME' TEXT," + // 1: ButcheryName
                "'ADDRESS' TEXT," + // 2: Address
                "'TELEPHONE' TEXT," + // 3: Telephone
                "'MASTER' TEXT," + // 4: Master
                "'MASTER_TELEPHONE' TEXT);"); // 5: MasterTelephone
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ENTITY_BUTCHERY'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, EntityButchery entity) {
        stmt.clearBindings();
 
        String InnerID = entity.getInnerID();
        if (InnerID != null) {
            stmt.bindString(1, InnerID);
        }
 
        String ButcheryName = entity.getButcheryName();
        if (ButcheryName != null) {
            stmt.bindString(2, ButcheryName);
        }
 
        String Address = entity.getAddress();
        if (Address != null) {
            stmt.bindString(3, Address);
        }
 
        String Telephone = entity.getTelephone();
        if (Telephone != null) {
            stmt.bindString(4, Telephone);
        }
 
        String Master = entity.getMaster();
        if (Master != null) {
            stmt.bindString(5, Master);
        }
 
        String MasterTelephone = entity.getMasterTelephone();
        if (MasterTelephone != null) {
            stmt.bindString(6, MasterTelephone);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public EntityButchery readEntity(Cursor cursor, int offset) {
        EntityButchery entity = new EntityButchery( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // InnerID
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // ButcheryName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // Address
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // Telephone
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // Master
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // MasterTelephone
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, EntityButchery entity, int offset) {
        entity.setInnerID(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setButcheryName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAddress(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTelephone(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setMaster(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setMasterTelephone(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(EntityButchery entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(EntityButchery entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
