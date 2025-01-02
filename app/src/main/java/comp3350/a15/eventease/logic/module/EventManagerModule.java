package comp3350.a15.eventease.logic.module;

import javax.inject.Singleton;

import comp3350.a15.eventease.logic.IEventManager;
import comp3350.a15.eventease.logic.implementation.EventManagerImpl;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class EventManagerModule {
    @Binds
    @Singleton
    abstract IEventManager provideEventManager(EventManagerImpl eventManager);
}
