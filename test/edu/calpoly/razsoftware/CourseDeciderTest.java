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
    private Course cpe101, cpe102, cpe103;

    @Before
    public void createDecider()
    {
        decider = new CourseDecider();
    }

    @Before
    public void createSampleCourses()
    {
        cpe101 = new Course(ImmutableList.of("CPE", "CSC"), 101, 4,
                            "The first class in CPE");
        cpe102 = new Course(ImmutableList.of("CPE", "CSC"), 102, 4,
                            "The second class in CPE");
        cpe103 = new Course(ImmutableList.of("CPE", "CSC"), 103, 4,
                            "The third class in CPE");
        cpe102.getPreRequisites().add(ImmutableSet.<Course>of(cpe101));
        cpe103.getPreRequisites().add(ImmutableSet.<Course>of(cpe102));
    }

    @Test
    public void resultConstructorShouldAllowEmpty()
    {
        CourseDecider.Result result = new CourseDecider.Result(
                ImmutableSet.<Course>of(),
                ImmutableSet.<Course>of());
        assertEquals(ImmutableSet.of(), result.getPrerequisitesMet());
        assertEquals(ImmutableSet.of(), result.getPrerequisitesNotMet());
    }

    @Test
    public void emptyCase()
    {
        UserState state = new UserState();
        Flowchart flowchart = new Flowchart();

        CourseDecider.Result result = decider.decideClasses(state, flowchart);

        assertEquals(ImmutableSet.<Course>of(),
                     result.getPrerequisitesMet());
        assertEquals(ImmutableSet.<Course>of(),
                     result.getPrerequisitesNotMet());
    }

    @Test
    public void shouldReportEmptyForComplete()
    {
        Flowchart flowchart = new Flowchart();
        flowchart.addOption(new CourseOption(cpe101));
        UserState state = new UserState(ImmutableSet.of(cpe101));

        CourseDecider.Result result = decider.decideClasses(state, flowchart);

        assertEquals(ImmutableSet.<Course>of(),
                     result.getPrerequisitesMet());
        assertEquals(ImmutableSet.<Course>of(),
                     result.getPrerequisitesNotMet());
    }

    @Test
    public void shouldReportAbleToTake()
    {
        Flowchart flowchart = new Flowchart();
        flowchart.addOption(new CourseOption(cpe101));
        flowchart.addOption(new CourseOption(cpe102));
        UserState state = new UserState(ImmutableSet.of(cpe101));

        CourseDecider.Result result = decider.decideClasses(state, flowchart);

        assertEquals(ImmutableSet.<Course>of(cpe102),
                     result.getPrerequisitesMet());
        assertEquals(ImmutableSet.<Course>of(),
                     result.getPrerequisitesNotMet());
    }

    @Test
    public void shouldReportUnableToTake()
    {
        Flowchart flowchart = new Flowchart();
        flowchart.addOption(new CourseOption(cpe101));
        flowchart.addOption(new CourseOption(cpe102));
        flowchart.addOption(new CourseOption(cpe103));
        UserState state = new UserState(ImmutableSet.of(cpe101));

        CourseDecider.Result result = decider.decideClasses(state, flowchart);

        assertEquals(ImmutableSet.<Course>of(cpe102),
                     result.getPrerequisitesMet());
        assertEquals(ImmutableSet.<Course>of(cpe103),
                     result.getPrerequisitesNotMet());
    }
}
