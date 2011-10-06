package edu.calpoly.razsoftware;

import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Ignore;
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
    @Ignore
    public void decideClassesShouldReturnEmptyFromEmpty()
    {
        // TODO(rlight): Create empty UserState
        UserState state = null;
        // TODO(rlight): Create empty flowchart
        Flowchart flowchart = null;

        CourseDecider.Result result = decider.decideClasses(state, flowchart);

        assertEquals(ImmutableSet.<CourseOption>of(),
                     result.getPrerequisitesMet());
        assertEquals(ImmutableSet.<CourseOption>of(),
                     result.getPrerequisitesNotMet());
    }
}
