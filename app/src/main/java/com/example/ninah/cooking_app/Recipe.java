package com.example.ninah.cooking_app;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * Created by Jonas on 19.09.2017.
 */

@Entity(nameInDb = "recipe", active = true)
public class Recipe {

        @Id(autoincrement = true)
        private Long id;

        @Property(nameInDb = "name")
        private String name;

        @Property(nameInDb = "recipeDescribtion")
        private String recipeDescribtion;

        @Property(nameInDb = "difficulty")
        private String difficulty;

        @NotNull
        private int timeInMinutes;

        @NotNull
        private int ratingInStars;


        @ToMany(referencedJoinProperty = "recipeID")
        private List<Ingrident> ingridentList;

       @ToMany(referencedJoinProperty = "recipeID")
       private List<RecipeWorkStep> recipeWorkStepList;

        /** Used to resolve relations */
        @Generated(hash = 2040040024)
        private transient DaoSession daoSession;

        /** Used for active entity operations. */
        @Generated(hash = 1947830398)
        private transient RecipeDao myDao;

        @Generated(hash = 1236500943)
        public Recipe(Long id, String name, String recipeDescribtion, String difficulty, int timeInMinutes,
                      int ratingInStars) {
            this.id = id;
            this.name = name;
            this.recipeDescribtion = recipeDescribtion;
            this.difficulty = difficulty;
            this.timeInMinutes = timeInMinutes;
            this.ratingInStars = ratingInStars;
        }

        @Generated(hash = 829032493)
        public Recipe() {
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

        public String getRecipeDescribtion() {
            return this.recipeDescribtion;
        }

        public void setRecipeDescribtion(String recipeDescribtion) {
            this.recipeDescribtion = recipeDescribtion;
        }

        public String getDifficulty() {
            return this.difficulty;
        }

        public void setDifficulty(String difficulty) {
            this.difficulty = difficulty;
        }

        /**
         * To-many relationship, resolved on first access (and after reset).
         * Changes to to-many relations are not persisted, make changes to the target entity.
         */
        @Generated(hash = 2079369974)
        public List<Ingrident> getIngridentList() {
            if (ingridentList == null) {
                final DaoSession daoSession = this.daoSession;
                if (daoSession == null) {
                    throw new DaoException("Entity is detached from DAO context");
                }
                IngridentDao targetDao = daoSession.getIngridentDao();
                List<Ingrident> ingridentListNew = targetDao._queryRecipe_IngridentList(id);
                synchronized (this) {
                    if (ingridentList == null) {
                        ingridentList = ingridentListNew;
                    }
                }
            }
            return ingridentList;
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

        public int getTimeInMinutes() {
            return this.timeInMinutes;
        }

        public void setTimeInMinutes(int timeInMinutes) {
            this.timeInMinutes = timeInMinutes;
        }

        /**
         * To-many relationship, resolved on first access (and after reset).
         * Changes to to-many relations are not persisted, make changes to the target entity.
         */
        @Generated(hash = 1510171082)
        public List<RecipeWorkStep> getRecipeWorkStepList() {
            if (recipeWorkStepList == null) {
                final DaoSession daoSession = this.daoSession;
                if (daoSession == null) {
                    throw new DaoException("Entity is detached from DAO context");
                }
                RecipeWorkStepDao targetDao = daoSession.getRecipeWorkStepDao();
                List<RecipeWorkStep> recipeWorkStepListNew = targetDao._queryRecipe_RecipeWorkStepList(id);
                synchronized (this) {
                    if (recipeWorkStepList == null) {
                        recipeWorkStepList = recipeWorkStepListNew;
                    }
                }
            }
            return recipeWorkStepList;
        }

        /** Resets a to-many relationship, making the next get call to query for a fresh result. */
        @Generated(hash = 1136283418)
        public synchronized void resetRecipeWorkStepList() {
            recipeWorkStepList = null;
        }

        /** Resets a to-many relationship, making the next get call to query for a fresh result. */
        @Generated(hash = 1145552271)
        public synchronized void resetIngridentList() {
            ingridentList = null;
        }

        public int getRatingInStars() {
            return this.ratingInStars;
        }

        public void setRatingInStars(int ratingInStars) {
            this.ratingInStars = ratingInStars;
        }

        /** called by internal mechanisms, do not call yourself. */
        @Generated(hash = 1484851246)
        public void __setDaoSession(DaoSession daoSession) {
            this.daoSession = daoSession;
            myDao = daoSession != null ? daoSession.getRecipeDao() : null;
        }

}
