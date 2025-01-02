package comp3350.a15.eventease.persistence.module;

import javax.inject.Singleton;

import comp3350.a15.eventease.persistence.IPlannerPersistence;
import comp3350.a15.eventease.persistence.hsqldb.PlannerPersistenceImplHSQLDB;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class PlannerPersistenceModule {
    @Binds
    @Singleton
    public abstract IPlannerPersistence providePlannerPersistence(PlannerPersistenceImplHSQLDB plannerPersistenceImplHSQLDB);
}