package comp3350.a15.eventease.application.module;

import javax.inject.Named;
import javax.inject.Singleton;

import comp3350.a15.eventease.application.EventEaseApp;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class EventEaseAppModule {
    @Provides
    @Singleton
    @Named("dbPathName")
    public static String provideDbPathName() {
        return EventEaseApp.getDBPathName();
    }
}
