package edu.calpoly.razsoftware;

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
                            "Fund of CS 1","The first class in CPE");
        cpe102 = new Course(ImmutableList.of("CPE", "CSC"), 102, 4,
                            "Fund of CS 2","The second class in CPE");
        cpe103 = new Course(ImmutableList.of("CPE", "CSC"), 103, 4,
                            "Fund of CS 3","The third class in CPE");
        cpe102.getPreRequisites().add(ImmutableSet.<Course>of(cpe101));
        cpe103.getPreRequisites().add(ImmutableSet.<Course>of(cpe102));
    }

    @Test
    public void emptyCase()
    {
        UserState state = new UserState();
        Flowchart flowchart = new Flowchart();

        assertEquals(ImmutableSet.<CourseOption>of(),
                     decider.decideClasses(state, flowchart));
    }

    @Test
    public void shouldReportEmptyForComplete()
    {
        Flowchart flowchart = new Flowchart();
        CourseOption option101 = new CourseOption(cpe101);
        flowchart.addOption(option101);
        UserState state = new UserState(ImmutableSet.of(cpe101));

        assertEquals(ImmutableSet.<CourseOption>of(),
                     decider.decideClasses(state, flowchart));
    }

    @Test
    public void shouldReportAbleToTake()
    {
        Flowchart flowchart = new Flowchart();
        CourseOption option101 = new CourseOption(cpe101);
        CourseOption option102 = new CourseOption(cpe102);
        flowchart.addOption(option101);
        flowchart.addOption(option102);
        UserState state = new UserState(ImmutableSet.of(cpe101));

        assertEquals(ImmutableSet.<CourseOption>of(option102),
                     decider.decideClasses(state, flowchart));
    }

    @Test
    public void shouldReportUnableToTake()
    {
        Flowchart flowchart = new Flowchart();
        CourseOption option101 = new CourseOption(cpe101);
        CourseOption option102 = new CourseOption(cpe102);
        CourseOption option103 = new CourseOption(cpe103);
        flowchart.addOption(option101);
        flowchart.addOption(option102);
        flowchart.addOption(option103);
        UserState state = new UserState(ImmutableSet.of(cpe101));

        assertEquals(ImmutableSet.<CourseOption>of(option102, option103),
                     decider.decideClasses(state, flowchart));
    }
}
