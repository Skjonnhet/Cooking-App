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

@Entity(nameInDb = "recipeWorkStep",  active = true)
public class RecipeWorkStep {
    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "workStepDescribition")
    private String workStepDescribition;

    @NotNull
    private Long recipeID;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 89935871)
    private transient RecipeWorkStepDao myDao;

    @Generated(hash = 984223649)
    public RecipeWorkStep(Long id, String workStepDescribition,
                          @NotNull Long recipeID) {
        this.id = id;
        this.workStepDescribition = workStepDescribition;
        this.recipeID = recipeID;
    }

    @Generated(hash = 108253773)
    public RecipeWorkStep() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkStepDescribition() {
        return this.workStepDescribition;
    }

    public void setWorkStepDescribition(String workStepDescribition) {
        this.workStepDescribition = workStepDescribition;
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
    @Generated(hash = 245605837)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRecipeWorkStepDao() : null;
    }
}
