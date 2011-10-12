package edu.calpoly.razsoftware;

import java.util.Set;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import static org.junit.Assert.*;

public class CourseTest
{
    @Test
    public void constructorTest()
    {
        Course c = new Course(ImmutableList.of("CSC", "CPE"), 101, 4,
                              "Hello, World!");
        assertEquals(ImmutableList.of("CSC", "CPE"), c.getMajor());
        assertEquals(101, c.getNumber());
        // TODO(rlight): name isn't being constructed. :(
        assertEquals(4, c.getUnits());
        assertEquals("Hello, World!", c.getDescription());
        assertEquals(ImmutableSet.<Set<Course>>of(), c.getPreRequisites());
        assertEquals(ImmutableSet.<Set<Course>>of(), c.getCoRequisites());
    }

    @Test
    public void emptyPrerequisitesShouldAlwaysMatch()
    {
        final Course c1 = new Course(ImmutableList.of("CSC"), 101, 4, "101");
        final Course c2 = new Course(ImmutableList.of("CSC"), 102, 4, "102");

        assertTrue(c1.preRecsMet(ImmutableSet.<Course>of()));
        assertTrue(c1.preRecsMet(ImmutableSet.<Course>of(c2)));
    }

    @Test
    public void emptyShouldNotMatch()
    {
        final Course c1 = new Course(ImmutableList.of("CSC"), 101, 4, "101");
        final Course c2 = new Course(ImmutableList.of("CSC"), 102, 4, "102");

        c2.getPreRequisites().add(ImmutableSet.<Course>of(c1));

        assertFalse(c2.preRecsMet(ImmutableSet.<Course>of()));
    }

    @Test
    public void subsetShouldNotMatch()
    {
        final Course c1 = new Course(ImmutableList.of("CSC"), 141, 4, "141");
        final Course c2 = new Course(ImmutableList.of("CSC"), 102, 4, "102");
        final Course c3 = new Course(ImmutableList.of("CSC"), 103, 4, "103");

        c3.getPreRequisites().add(ImmutableSet.<Course>of(c1, c2));

        assertFalse(c3.preRecsMet(ImmutableSet.<Course>of(c1)));
    }

    @Test
    public void exactShouldMatch()
    {
        final Course c1 = new Course(ImmutableList.of("CSC"), 101, 4, "101");
        final Course c2 = new Course(ImmutableList.of("CSC"), 102, 4, "102");

        c2.getPreRequisites().add(ImmutableSet.<Course>of(c1));

        assertTrue(c2.preRecsMet(ImmutableSet.<Course>of(c1)));
    }

    @Test
    public void supersetShouldMatch()
    {
        final Course c1 = new Course(ImmutableList.of("CSC"), 101, 4, "101");
        final Course c2 = new Course(ImmutableList.of("CSC"), 102, 4, "102");
        final Course c3 = new Course(ImmutableList.of("CSC"), 141, 4, "141");

        c2.getPreRequisites().add(ImmutableSet.<Course>of(c1));

        assertTrue(c2.preRecsMet(ImmutableSet.<Course>of(c1, c3)));
    }
    
    @Test
    public void optionShouldMatch()
    {
        final Course m1 = new Course(ImmutableList.of("MATH"),101, 4, "MATH 101");
        final Course c1 = new Course(ImmutableList.of("CSC"),101, 4, "CSC 101");
        final Course c2 = new Course(ImmutableList.of("CSC"),102, 4, "CSC 102");
        final Course c3 = new Course(ImmutableList.of("CSC"),103, 4, "CSC 103");
        
        c3.getPreRequisites().add(ImmutableSet.<Course>of(m1,c1));
        c3.getPreRequisites().add(ImmutableSet.<Course>of(m1,c2));
        
        assertFalse(c3.preRecsMet(ImmutableSet.<Course>of()));
        assertFalse(c3.preRecsMet(ImmutableSet.<Course>of(m1)));
        assertFalse(c3.preRecsMet(ImmutableSet.<Course>of(c1)));
        assertFalse(c3.preRecsMet(ImmutableSet.<Course>of(c2)));
        assertTrue(c3.preRecsMet(ImmutableSet.<Course>of(m1, c1)));
        assertTrue(c3.preRecsMet(ImmutableSet.<Course>of(m1, c2)));
        assertFalse(c3.preRecsMet(ImmutableSet.<Course>of(c1, c2)));
        assertTrue(c3.preRecsMet(ImmutableSet.<Course>of(m1, c1, c2)));
    }
}
