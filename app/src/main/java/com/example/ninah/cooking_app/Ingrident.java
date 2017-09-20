package com.example.ninah.cooking_app;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.DaoException;

/**
 * Created by Jonas on 19.09.2017.
 */

@Entity(nameInDb = "ingrident",  active = true)
public class Ingrident {

    @Id
    private Long id;

    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "Einheit")
    private String einheit;

   @NotNull
    private double menge;

    @NotNull
    private Long recipeID;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 409356898)
    private transient IngridentDao myDao;

    @Generated(hash = 77507271)
    public Ingrident(Long id, String name, String einheit, double menge,
                     @NotNull Long recipeID) {
        this.id = id;
        this.name = name;
        this.einheit = einheit;
        this.menge = menge;
        this.recipeID = recipeID;
    }

    @Generated(hash = 1621881215)
    public Ingrident() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEinheit() {
        return this.einheit;
    }

    public void setEinheit(String einheit) {
        this.einheit = einheit;
    }

    public double getMenge() {
        return this.menge;
    }

    public void setMenge(double menge) {
        this.menge = menge;
    }

    public Long getRecipeID() {
        return this.recipeID;
    }

    public void setRecipeID(Long recipeID) {
        this.recipeID = recipeID;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1520298810)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getIngridentDao() : null;
    }
}
