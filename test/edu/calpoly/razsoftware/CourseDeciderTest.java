package edu.calpoly.razsoftware;

import org.junit.Ignore;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CourseDeciderTest
{
    private CourseDecider decider;

    @Before
    public void createDecider()
    {
        decider = new CourseDecider();
    }

    @Test
    public void resultConstructorShouldAllowEmpty()
    {
        CourseDecider.Result result = new CourseDecider.Result(
                ImmutableSet.<CourseOption>of(),
                ImmutableSet.<CourseOption>of());
        assertEquals(ImmutableSet.of(), result.getPrerequisitesMet());
        assertEquals(ImmutableSet.of(), result.getPrerequisitesNotMet());
    }

    @Test
    public void shouldReturnEmptyFromEmpty()
    {
        UserState state = new UserState();
        Flowchart flowchart = new Flowchart();

        CourseDecider.Result result = decider.decideClasses(state, flowchart);

        assertEquals(ImmutableSet.<CourseOption>of(),
                     result.getPrerequisitesMet());
        assertEquals(ImmutableSet.<CourseOption>of(),
                     result.getPrerequisitesNotMet());
    }

    @Test
    public void shouldReportUnfulfilledForEmptyState()
    {
        UserState state = new UserState();
        Flowchart flowchart = new Flowchart();
        Course cpe101 = new Course(ImmutableList.of("CPE", "CSC"), 101, 4,
                                   "The first class in CPE");
        CourseOption opt = new CourseOption(cpe101);
        flowchart.addOption(opt);

        CourseDecider.Result result = decider.decideClasses(state, flowchart);

        assertEquals(ImmutableSet.<CourseOption>of(),
                     result.getPrerequisitesMet());
        assertEquals(ImmutableSet.<CourseOption>of(opt),
                     result.getPrerequisitesNotMet());
    }

    @Test
    public void shouldReportFulfilled()
    {
        Flowchart flowchart = new Flowchart();
        Course cpe101 = new Course(ImmutableList.of("CPE", "CSC"), 101, 4,
                                   "The first class in CPE");
        CourseOption opt = new CourseOption(cpe101);
        flowchart.addOption(opt);
        UserState state = new UserState(ImmutableSet.of(cpe101));

        CourseDecider.Result result = decider.decideClasses(state, flowchart);

        assertEquals(ImmutableSet.<CourseOption>of(opt),
                     result.getPrerequisitesMet());
        assertEquals(ImmutableSet.<CourseOption>of(),
                     result.getPrerequisitesNotMet());
    }
}
