package comp3350.a15.eventease;


import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;

import java.util.List;

import comp3350.a15.eventease.application.EventEaseApp;
import comp3350.a15.eventease.logic.IEventManager;
import comp3350.a15.eventease.logic.IUserManager;
import comp3350.a15.eventease.logic.implementation.EventManagerImpl;
import comp3350.a15.eventease.logic.implementation.UserManagerImpl;
import comp3350.a15.eventease.objects.Event;
import comp3350.a15.eventease.objects.factory.implementation.EventFactoryImpl;
import comp3350.a15.eventease.persistence.IEventPersistence;
import comp3350.a15.eventease.persistence.IUserPersistence;
import comp3350.a15.eventease.persistence.hsqldb.EventPersistenceImplHSQLDB;
import comp3350.a15.eventease.persistence.hsqldb.UserPersistenceHSQLDB;

public class SystemTestUtils {
    private static IEventManager eventManager;
    private final IUserManager userManager;


    public SystemTestUtils() {
        EventFactoryImpl eventFactory = new EventFactoryImpl();
        IEventPersistence persistence = new EventPersistenceImplHSQLDB(EventEaseApp.getDBPathName(), eventFactory);
        IUserPersistence userPersistence = new UserPersistenceHSQLDB(EventEaseApp.getDBPathName());
        eventManager = new EventManagerImpl(persistence, eventFactory);
        userManager = new UserManagerImpl(userPersistence);
    }

    public int getNumOfEvents() {
        List<Event> allEvents;
        allEvents = eventManager.getAllEvents(userManager.getUserId("planner"));
        return allEvents.size();

    }

    public void deleteUser(String username) {
        userManager.deleteUser(username);
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }

}
