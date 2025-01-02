package comp3350.a15.eventease.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import comp3350.a15.eventease.logic.exceptions.FieldMissingException;
import comp3350.a15.eventease.logic.exceptions.PlannerAlreadyExistsException;
import comp3350.a15.eventease.logic.exceptions.PlannerNotFoundException;
import comp3350.a15.eventease.logic.implementation.PlannerManagerImpl;
import comp3350.a15.eventease.objects.Planner;
import comp3350.a15.eventease.objects.factory.IPlannerFactory;
import comp3350.a15.eventease.persistence.IPlannerPersistence;
import comp3350.a15.eventease.persistence.hsqldb.PersistenceException;

@RunWith(MockitoJUnitRunner.class)
public class PlannerManagerTest {

    @Mock
    private IPlannerPersistence plannerPersistence;

    @Mock
    private IPlannerFactory plannerFactory;

    private PlannerManagerImpl plannerManager;

    @Before
    public void setUp() {
        plannerManager = new PlannerManagerImpl(plannerPersistence, plannerFactory);
    }

    @Test
    public void testGetPlanner_Success() {
        int plannerId = 1;
        Planner expectedPlanner = new Planner(plannerId, "John", "Doe", "1234567890", "john.doe@example.com", 5, "Experienced planner");
        when(plannerPersistence.getPlannerByUserId(plannerId)).thenReturn(expectedPlanner);

        Planner planner = plannerManager.getPlannerByUserId(plannerId);

        assertNotNull(planner);
        assertEquals(expectedPlanner, planner);
    }

    @Test(expected = PlannerNotFoundException.class)
    public void testGetPlanner_NotFound() {
        int plannerId = 2;
        when(plannerPersistence.getPlannerByUserId(plannerId)).thenThrow(new PlannerNotFoundException("test", new PersistenceException("test")));

        plannerManager.getPlannerByUserId(plannerId);
    }

    @Test
    public void testAddNewPlanner_Success() {
        Planner newPlanner = new Planner(3, "Jane", "Smith", "0987654321", "jane.smith@example.com", 4, "Well-organized");
        when(plannerFactory.create(anyInt(), anyString(), anyString(), anyString(), anyString(), anyFloat(), anyString())).thenReturn(newPlanner);
        when(plannerPersistence.addNewPlanner(newPlanner)).thenReturn(true);

        plannerManager.addPlanner(3, "Jane", "Smith", "0987654321", "jane.smith@example.com", 4, "Well-organized");

        verify(plannerPersistence).addNewPlanner(newPlanner);
    }

    @Test(expected = PlannerAlreadyExistsException.class)
    public void testAddNewPlanner_Duplicate() {
        Planner duplicatePlanner = new Planner(4, "Alice", "Wonders", "0101010101", "alice.wonders@example.com", 5, "Creative thinker");
        when(plannerFactory.create(anyInt(), anyString(), anyString(), anyString(), anyString(), anyFloat(), anyString())).thenReturn(duplicatePlanner);
        when(plannerPersistence.doesPlannerExist(duplicatePlanner)).thenReturn(true);

        plannerManager.addPlanner(4, "Alice", "Wonders", "0101010101", "alice.wonders@example.com", 5, "Creative thinker");
    }

    @Test(expected = FieldMissingException.class)
    public void testAddNewPlanner_MissingField() {
        plannerManager.addPlanner(5, "", "Wonders", "0202020202", "alice.wonders@example.com", 5, "Highly creative");
    }

    @Test
    public void testMakePlanner_Success() {
        Planner expectedPlanner = new Planner(4, "MakeFirstName", "MakeLastName", "1234567890", "make.email@example.com", 5, "Make bio");
        when(plannerFactory.create(anyInt(), anyString(), anyString(), anyString(), anyString(), anyFloat(), anyString())).thenReturn(expectedPlanner);

        Planner result = plannerManager.makePlanner(4, "MakeFirstName", "MakeLastName", "1234567890", "make.email@example.com", 5, "Make bio");

        assertNotNull(result);
        assertEquals("MakeFirstName", result.getFirstname());
        assertEquals("MakeLastName", result.getLastname());
        assertEquals("make.email@example.com", result.getEmail());
    }
}
