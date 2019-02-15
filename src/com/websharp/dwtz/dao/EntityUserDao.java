package com.websharp.dwtz.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.websharp.dwtz.dao.EntityUser;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ENTITY_USER.
*/
public class EntityUserDao extends AbstractDao<EntityUser, Void> {

    public static final String TABLENAME = "ENTITY_USER";

    /**
     * Properties of entity EntityUser.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Description = new Property(0, String.class, "Description", false, "DESCRIPTION");
        public final static Property Email = new Property(1, String.class, "Email", false, "EMAIL");
        public final static Property InnerID = new Property(2, String.class, "InnerID", false, "INNER_ID");
        public final static Property LastLogin = new Property(3, String.class, "LastLogin", false, "LAST_LOGIN");
        public final static Property NavigationUrl = new Property(4, String.class, "NavigationUrl", false, "NAVIGATION_URL");
        public final static Property Password = new Property(5, String.class, "Password", false, "PASSWORD");
        public final static Property Role = new Property(6, String.class, "Role", false, "ROLE");
        public final static Property StaffNo = new Property(7, String.class, "StaffNo", false, "STAFF_NO");
        public final static Property Telephone = new Property(8, String.class, "Telephone", false, "TELEPHONE");
        public final static Property UserID = new Property(9, String.class, "UserID", false, "USER_ID");
        public final static Property UserName = new Property(10, String.class, "UserName", false, "USER_NAME");
        public final static Property ButcheryID = new Property(11, String.class, "ButcheryID", false, "BUTCHERY_ID");
        public final static Property ApplyCompanyName = new Property(12, String.class, "ApplyCompanyName", false, "APPLY_COMPANY_NAME");
    };


    public EntityUserDao(DaoConfig config) {
        super(config);
    }
    
    public EntityUserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ENTITY_USER' (" + //
                "'DESCRIPTION' TEXT," + // 0: Description
                "'EMAIL' TEXT," + // 1: Email
                "'INNER_ID' TEXT," + // 2: InnerID
                "'LAST_LOGIN' TEXT," + // 3: LastLogin
                "'NAVIGATION_URL' TEXT," + // 4: NavigationUrl
                "'PASSWORD' TEXT," + // 5: Password
                "'ROLE' TEXT," + // 6: Role
                "'STAFF_NO' TEXT," + // 7: StaffNo
                "'TELEPHONE' TEXT," + // 8: Telephone
                "'USER_ID' TEXT," + // 9: UserID
                "'USER_NAME' TEXT," + // 10: UserName
                "'BUTCHERY_ID' TEXT," + // 11: ButcheryID
                "'APPLY_COMPANY_NAME' TEXT);"); // 12: ApplyCompanyName
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ENTITY_USER'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, EntityUser entity) {
        stmt.clearBindings();
 
        String Description = entity.getDescription();
        if (Description != null) {
            stmt.bindString(1, Description);
        }
 
        String Email = entity.getEmail();
        if (Email != null) {
            stmt.bindString(2, Email);
        }
 
        String InnerID = entity.getInnerID();
        if (InnerID != null) {
            stmt.bindString(3, InnerID);
        }
 
        String LastLogin = entity.getLastLogin();
        if (LastLogin != null) {
            stmt.bindString(4, LastLogin);
        }
 
        String NavigationUrl = entity.getNavigationUrl();
        if (NavigationUrl != null) {
            stmt.bindString(5, NavigationUrl);
        }
 
        String Password = entity.getPassword();
        if (Password != null) {
            stmt.bindString(6, Password);
        }
 
        String Role = entity.getRole();
        if (Role != null) {
            stmt.bindString(7, Role);
        }
 
        String StaffNo = entity.getStaffNo();
        if (StaffNo != null) {
            stmt.bindString(8, StaffNo);
        }
 
        String Telephone = entity.getTelephone();
        if (Telephone != null) {
            stmt.bindString(9, Telephone);
        }
 
        String UserID = entity.getUserID();
        if (UserID != null) {
            stmt.bindString(10, UserID);
        }
 
        String UserName = entity.getUserName();
        if (UserName != null) {
            stmt.bindString(11, UserName);
        }
 
        String ButcheryID = entity.getButcheryID();
        if (ButcheryID != null) {
            stmt.bindString(12, ButcheryID);
        }
 
        String ApplyCompanyName = entity.getApplyCompanyName();
        if (ApplyCompanyName != null) {
            stmt.bindString(13, ApplyCompanyName);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public EntityUser readEntity(Cursor cursor, int offset) {
        EntityUser entity = new EntityUser( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // Description
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Email
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // InnerID
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // LastLogin
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // NavigationUrl
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // Password
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // Role
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // StaffNo
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // Telephone
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // UserID
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // UserName
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // ButcheryID
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12) // ApplyCompanyName
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, EntityUser entity, int offset) {
        entity.setDescription(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setEmail(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setInnerID(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setLastLogin(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setNavigationUrl(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setPassword(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setRole(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setStaffNo(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setTelephone(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setUserID(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setUserName(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setButcheryID(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setApplyCompanyName(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(EntityUser entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(EntityUser entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}