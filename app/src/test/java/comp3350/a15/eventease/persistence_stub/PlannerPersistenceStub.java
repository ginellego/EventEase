package comp3350.a15.eventease.persistence_stub;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import comp3350.a15.eventease.logic.exceptions.PlannerNotFoundException;
import comp3350.a15.eventease.objects.Planner;
import comp3350.a15.eventease.persistence.IPlannerPersistence;

@Singleton
public class PlannerPersistenceStub implements IPlannerPersistence {
    private final ArrayList<Planner> planners;

    @Inject
    public PlannerPersistenceStub() {
        planners = new ArrayList<>();
    }

    @Override
    public Planner getPlannerByUserId(int userId) {
        for (int i = 0; i < planners.size(); i++) {
            if (planners.get(i) != null && planners.get(i).getId() == userId) {
                return planners.get(i);
            }
        }
        throw new PlannerNotFoundException("Planner could not be found");
//        return null;
    }

    @Override
    public boolean addNewPlanner(Planner planner) {
        if (planner != null && !planners.contains(planner)) {
            return planners.add(planner);
        }
        return false;
    }

    @Override
    public boolean doesPlannerExist(Planner planner) {
        return planners.contains(planner);
    }

    @Override
    public void saveEditPlanner(Planner plannerToEdit, Planner plannerPostEdit) {
        int position = planners.indexOf(plannerToEdit);
        if (position >= 0)
            planners.set(position, plannerPostEdit);

    }
}
