package comp3350.a15.eventease.persistence.module;

import javax.inject.Singleton;

import comp3350.a15.eventease.persistence.IUserPersistence;
import comp3350.a15.eventease.persistence.hsqldb.UserPersistenceHSQLDB;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class UserPersistenceModule {
    @Binds
    @Singleton
    public abstract IUserPersistence provideUserPersistence(UserPersistenceHSQLDB userPersistence);
}
