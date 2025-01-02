package comp3350.a15.eventease.logic.implementation;

import javax.inject.Inject;

import comp3350.a15.eventease.logic.IPlannerManager;
import comp3350.a15.eventease.logic.exceptions.FieldMissingException;
import comp3350.a15.eventease.logic.exceptions.PlannerAlreadyExistsException;
import comp3350.a15.eventease.logic.exceptions.PlannerNotAddedException;
import comp3350.a15.eventease.logic.exceptions.PlannerNotFoundException;
import comp3350.a15.eventease.objects.Planner;
import comp3350.a15.eventease.objects.factory.IPlannerFactory;
import comp3350.a15.eventease.persistence.IPlannerPersistence;
import comp3350.a15.eventease.persistence.hsqldb.PersistenceException;


public class PlannerManagerImpl implements IPlannerManager {
    private final IPlannerPersistence plannerPersistence;
    private final IPlannerFactory plannerFactory;

    @Inject
    public PlannerManagerImpl(IPlannerPersistence plannerPersistence, IPlannerFactory plannerFactory
    ) {
        this.plannerPersistence = plannerPersistence;
        this.plannerFactory = plannerFactory;
    }

    @Override
    public Planner getPlannerByUserId(int userId) {
        try {
            return plannerPersistence.getPlannerByUserId(userId);
        } catch (PersistenceException e) {
            throw new PlannerNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public Planner addPlanner(int plannerId, CharSequence firstname, CharSequence lastname, CharSequence phone,
                              CharSequence email, float rating, CharSequence bio) {
        Planner addedPlanner;

        validateAllFieldsPresent(firstname, lastname, phone,
                email, bio);

        boolean plannerAdded;
        addedPlanner = makePlanner(plannerId, firstname, lastname, phone, email, rating, bio);
        checkDuplicatePlanner(addedPlanner);
        plannerAdded = plannerPersistence.addNewPlanner(addedPlanner);

        if (!plannerAdded) {
            throw new PlannerNotAddedException("Error adding planner to database");
        }
        return addedPlanner;
    }

    private void checkDuplicatePlanner(Planner addedPlanner) {
        if (plannerPersistence.doesPlannerExist(addedPlanner)) {
            throw new PlannerAlreadyExistsException("Planner already exists");
        }
    }

    private void validateAllFieldsPresent(CharSequence... fields) {
        boolean allFieldsPresent = true;
        for (CharSequence field : fields) {
            if (field == null || field.toString().trim().isEmpty()) {
                allFieldsPresent = false;
            }

            if (!allFieldsPresent) {
                throw new FieldMissingException("Fields Missing");
            }
        }
    }

    @Override
    public Planner makePlanner(int id, CharSequence firstname, CharSequence lastname, CharSequence phone,
                               CharSequence email, float rating, CharSequence bio) {

        return plannerFactory.create(id, (String) firstname, (String) lastname, (String) phone,
                (String) email, rating, (String) bio);
    }
}
