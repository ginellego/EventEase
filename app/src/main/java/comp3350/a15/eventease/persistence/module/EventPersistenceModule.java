package comp3350.a15.eventease.persistence.module;

import javax.inject.Singleton;

import comp3350.a15.eventease.persistence.IEventPersistence;
import comp3350.a15.eventease.persistence.hsqldb.EventPersistenceImplHSQLDB;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class EventPersistenceModule {
    @Binds
    @Singleton
    public abstract IEventPersistence provideEventPersistence(EventPersistenceImplHSQLDB eventPersistenceImpl);
}
