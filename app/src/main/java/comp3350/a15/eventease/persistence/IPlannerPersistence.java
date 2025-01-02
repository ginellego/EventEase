package comp3350.a15.eventease.persistence;

import comp3350.a15.eventease.objects.Planner;

public interface IPlannerPersistence {
    Planner getPlannerByUserId(int userId);

    boolean addNewPlanner(Planner user);

    boolean doesPlannerExist(Planner planner);

    void saveEditPlanner(Planner plannerToEdit, Planner plannerPostEdit);

}
