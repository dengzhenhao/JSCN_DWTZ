package com.websharp.dwtz.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.websharp.dwtz.dao.EntityUser;
import com.websharp.dwtz.dao.EntityQuarantine;
import com.websharp.dwtz.dao.EntityUnqualied;
import com.websharp.dwtz.dao.EntityArticle;
import com.websharp.dwtz.dao.EntityButcheryGroup;
import com.websharp.dwtz.dao.EntityButchery;
import com.websharp.dwtz.dao.EntityDestroy;
import com.websharp.dwtz.dao.EntityVersion;
import com.websharp.dwtz.dao.EntityCommonData;
import com.websharp.dwtz.dao.EntityDistributionApply;
import com.websharp.dwtz.dao.EntityDistributionApplyTarget;
import com.websharp.dwtz.dao.EntityLocation;
import com.websharp.dwtz.dao.EntityAnimalSlaughterImmuneApply;

import com.websharp.dwtz.dao.EntityUserDao;
import com.websharp.dwtz.dao.EntityQuarantineDao;
import com.websharp.dwtz.dao.EntityUnqualiedDao;
import com.websharp.dwtz.dao.EntityArticleDao;
import com.websharp.dwtz.dao.EntityButcheryGroupDao;
import com.websharp.dwtz.dao.EntityButcheryDao;
import com.websharp.dwtz.dao.EntityDestroyDao;
import com.websharp.dwtz.dao.EntityVersionDao;
import com.websharp.dwtz.dao.EntityCommonDataDao;
import com.websharp.dwtz.dao.EntityDistributionApplyDao;
import com.websharp.dwtz.dao.EntityDistributionApplyTargetDao;
import com.websharp.dwtz.dao.EntityLocationDao;
import com.websharp.dwtz.dao.EntityAnimalSlaughterImmuneApplyDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig entityUserDaoConfig;
    private final DaoConfig entityQuarantineDaoConfig;
    private final DaoConfig entityUnqualiedDaoConfig;
    private final DaoConfig entityArticleDaoConfig;
    private final DaoConfig entityButcheryGroupDaoConfig;
    private final DaoConfig entityButcheryDaoConfig;
    private final DaoConfig entityDestroyDaoConfig;
    private final DaoConfig entityVersionDaoConfig;
    private final DaoConfig entityCommonDataDaoConfig;
    private final DaoConfig entityDistributionApplyDaoConfig;
    private final DaoConfig entityDistributionApplyTargetDaoConfig;
    private final DaoConfig entityLocationDaoConfig;
    private final DaoConfig entityAnimalSlaughterImmuneApplyDaoConfig;

    private final EntityUserDao entityUserDao;
    private final EntityQuarantineDao entityQuarantineDao;
    private final EntityUnqualiedDao entityUnqualiedDao;
    private final EntityArticleDao entityArticleDao;
    private final EntityButcheryGroupDao entityButcheryGroupDao;
    private final EntityButcheryDao entityButcheryDao;
    private final EntityDestroyDao entityDestroyDao;
    private final EntityVersionDao entityVersionDao;
    private final EntityCommonDataDao entityCommonDataDao;
    private final EntityDistributionApplyDao entityDistributionApplyDao;
    private final EntityDistributionApplyTargetDao entityDistributionApplyTargetDao;
    private final EntityLocationDao entityLocationDao;
    private final EntityAnimalSlaughterImmuneApplyDao entityAnimalSlaughterImmuneApplyDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        entityUserDaoConfig = daoConfigMap.get(EntityUserDao.class).clone();
        entityUserDaoConfig.initIdentityScope(type);

        entityQuarantineDaoConfig = daoConfigMap.get(EntityQuarantineDao.class).clone();
        entityQuarantineDaoConfig.initIdentityScope(type);

        entityUnqualiedDaoConfig = daoConfigMap.get(EntityUnqualiedDao.class).clone();
        entityUnqualiedDaoConfig.initIdentityScope(type);

        entityArticleDaoConfig = daoConfigMap.get(EntityArticleDao.class).clone();
        entityArticleDaoConfig.initIdentityScope(type);

        entityButcheryGroupDaoConfig = daoConfigMap.get(EntityButcheryGroupDao.class).clone();
        entityButcheryGroupDaoConfig.initIdentityScope(type);

        entityButcheryDaoConfig = daoConfigMap.get(EntityButcheryDao.class).clone();
        entityButcheryDaoConfig.initIdentityScope(type);

        entityDestroyDaoConfig = daoConfigMap.get(EntityDestroyDao.class).clone();
        entityDestroyDaoConfig.initIdentityScope(type);

        entityVersionDaoConfig = daoConfigMap.get(EntityVersionDao.class).clone();
        entityVersionDaoConfig.initIdentityScope(type);

        entityCommonDataDaoConfig = daoConfigMap.get(EntityCommonDataDao.class).clone();
        entityCommonDataDaoConfig.initIdentityScope(type);

        entityDistributionApplyDaoConfig = daoConfigMap.get(EntityDistributionApplyDao.class).clone();
        entityDistributionApplyDaoConfig.initIdentityScope(type);

        entityDistributionApplyTargetDaoConfig = daoConfigMap.get(EntityDistributionApplyTargetDao.class).clone();
        entityDistributionApplyTargetDaoConfig.initIdentityScope(type);

        entityLocationDaoConfig = daoConfigMap.get(EntityLocationDao.class).clone();
        entityLocationDaoConfig.initIdentityScope(type);

        entityAnimalSlaughterImmuneApplyDaoConfig = daoConfigMap.get(EntityAnimalSlaughterImmuneApplyDao.class).clone();
        entityAnimalSlaughterImmuneApplyDaoConfig.initIdentityScope(type);

        entityUserDao = new EntityUserDao(entityUserDaoConfig, this);
        entityQuarantineDao = new EntityQuarantineDao(entityQuarantineDaoConfig, this);
        entityUnqualiedDao = new EntityUnqualiedDao(entityUnqualiedDaoConfig, this);
        entityArticleDao = new EntityArticleDao(entityArticleDaoConfig, this);
        entityButcheryGroupDao = new EntityButcheryGroupDao(entityButcheryGroupDaoConfig, this);
        entityButcheryDao = new EntityButcheryDao(entityButcheryDaoConfig, this);
        entityDestroyDao = new EntityDestroyDao(entityDestroyDaoConfig, this);
        entityVersionDao = new EntityVersionDao(entityVersionDaoConfig, this);
        entityCommonDataDao = new EntityCommonDataDao(entityCommonDataDaoConfig, this);
        entityDistributionApplyDao = new EntityDistributionApplyDao(entityDistributionApplyDaoConfig, this);
        entityDistributionApplyTargetDao = new EntityDistributionApplyTargetDao(entityDistributionApplyTargetDaoConfig, this);
        entityLocationDao = new EntityLocationDao(entityLocationDaoConfig, this);
        entityAnimalSlaughterImmuneApplyDao = new EntityAnimalSlaughterImmuneApplyDao(entityAnimalSlaughterImmuneApplyDaoConfig, this);

        registerDao(EntityUser.class, entityUserDao);
        registerDao(EntityQuarantine.class, entityQuarantineDao);
        registerDao(EntityUnqualied.class, entityUnqualiedDao);
        registerDao(EntityArticle.class, entityArticleDao);
        registerDao(EntityButcheryGroup.class, entityButcheryGroupDao);
        registerDao(EntityButchery.class, entityButcheryDao);
        registerDao(EntityDestroy.class, entityDestroyDao);
        registerDao(EntityVersion.class, entityVersionDao);
        registerDao(EntityCommonData.class, entityCommonDataDao);
        registerDao(EntityDistributionApply.class, entityDistributionApplyDao);
        registerDao(EntityDistributionApplyTarget.class, entityDistributionApplyTargetDao);
        registerDao(EntityLocation.class, entityLocationDao);
        registerDao(EntityAnimalSlaughterImmuneApply.class, entityAnimalSlaughterImmuneApplyDao);
    }
    
    public void clear() {
        entityUserDaoConfig.getIdentityScope().clear();
        entityQuarantineDaoConfig.getIdentityScope().clear();
        entityUnqualiedDaoConfig.getIdentityScope().clear();
        entityArticleDaoConfig.getIdentityScope().clear();
        entityButcheryGroupDaoConfig.getIdentityScope().clear();
        entityButcheryDaoConfig.getIdentityScope().clear();
        entityDestroyDaoConfig.getIdentityScope().clear();
        entityVersionDaoConfig.getIdentityScope().clear();
        entityCommonDataDaoConfig.getIdentityScope().clear();
        entityDistributionApplyDaoConfig.getIdentityScope().clear();
        entityDistributionApplyTargetDaoConfig.getIdentityScope().clear();
        entityLocationDaoConfig.getIdentityScope().clear();
        entityAnimalSlaughterImmuneApplyDaoConfig.getIdentityScope().clear();
    }

    public EntityUserDao getEntityUserDao() {
        return entityUserDao;
    }

    public EntityQuarantineDao getEntityQuarantineDao() {
        return entityQuarantineDao;
    }

    public EntityUnqualiedDao getEntityUnqualiedDao() {
        return entityUnqualiedDao;
    }

    public EntityArticleDao getEntityArticleDao() {
        return entityArticleDao;
    }

    public EntityButcheryGroupDao getEntityButcheryGroupDao() {
        return entityButcheryGroupDao;
    }

    public EntityButcheryDao getEntityButcheryDao() {
        return entityButcheryDao;
    }

    public EntityDestroyDao getEntityDestroyDao() {
        return entityDestroyDao;
    }

    public EntityVersionDao getEntityVersionDao() {
        return entityVersionDao;
    }

    public EntityCommonDataDao getEntityCommonDataDao() {
        return entityCommonDataDao;
    }

    public EntityDistributionApplyDao getEntityDistributionApplyDao() {
        return entityDistributionApplyDao;
    }

    public EntityDistributionApplyTargetDao getEntityDistributionApplyTargetDao() {
        return entityDistributionApplyTargetDao;
    }

    public EntityLocationDao getEntityLocationDao() {
        return entityLocationDao;
    }

    public EntityAnimalSlaughterImmuneApplyDao getEntityAnimalSlaughterImmuneApplyDao() {
        return entityAnimalSlaughterImmuneApplyDao;
    }

}