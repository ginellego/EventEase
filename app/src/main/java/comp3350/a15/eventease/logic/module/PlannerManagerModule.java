package comp3350.a15.eventease.logic.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import comp3350.a15.eventease.logic.IPlannerManager;
import comp3350.a15.eventease.logic.implementation.PlannerManagerImpl;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class PlannerManagerModule {
    @Binds
    @Singleton
    abstract IPlannerManager providePlannerManager(PlannerManagerImpl plannerManagerImpl);

    @Provides
    @Singleton
    static Context provideContext(Application application) {
        return application.getApplicationContext();
    }
}