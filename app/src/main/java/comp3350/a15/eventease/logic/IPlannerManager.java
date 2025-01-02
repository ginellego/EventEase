package comp3350.a15.eventease.logic;

import comp3350.a15.eventease.objects.Planner;

public interface IPlannerManager {
    Planner getPlannerByUserId(int userId);

    Planner addPlanner(int plannerId, CharSequence firstname, CharSequence lastname, CharSequence phone,
                       CharSequence email, float rating, CharSequence bio);

    Planner makePlanner(int id, CharSequence firstname, CharSequence lastname, CharSequence phone,
                        CharSequence email, float rating, CharSequence bio);
}
