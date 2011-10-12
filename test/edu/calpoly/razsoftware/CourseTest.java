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
                              "Fund of CS 1","Hello, World!");
        assertEquals(ImmutableList.of("CSC", "CPE"), c.getMajor());
        assertEquals(101, c.getNumber());
        assertEquals("Fund of CS 1", c.getName());
        assertEquals(4, c.getUnits());
        assertEquals("Hello, World!", c.getDescription());
        assertEquals(ImmutableSet.<Set<Course>>of(), c.getPreRequisites());
        assertEquals(ImmutableSet.<Set<Course>>of(), c.getCoRequisites());
        assertEquals("CSC101",c.toString());
        assertEquals("",c.getPreRequisitesString());
    }

    @Test
    public void emptyPrerequisitesShouldAlwaysMatch()
    {
        final Course c1 = new Course(ImmutableList.of("CSC"), 101, 4,"Fund of CS 1", "101");
        final Course c2 = new Course(ImmutableList.of("CSC"), 102, 4,"Fund of CS 2", "102");

        assertTrue(c1.preRecsMet(ImmutableSet.<Course>of()));
        assertTrue(c1.preRecsMet(ImmutableSet.<Course>of(c2)));
    }

    @Test
    public void emptyShouldNotMatch()
    {
        final Course c1 = new Course(ImmutableList.of("CSC"), 101, 4, "Fund of CS 1", "101");
        final Course c2 = new Course(ImmutableList.of("CSC"), 102, 4, "Fund of CS 2", "102");

        c2.getPreRequisites().add(ImmutableSet.<Course>of(c1));

        assertFalse(c2.preRecsMet(ImmutableSet.<Course>of()));
    }

    @Test
    public void subsetShouldNotMatch()
    {
        final Course c1 = new Course(ImmutableList.of("CSC"), 141, 4, "Fund of CS 1", "141");
        final Course c2 = new Course(ImmutableList.of("CSC"), 102, 4, "Fund of CS 2", "102");
        final Course c3 = new Course(ImmutableList.of("CSC"), 103, 4, "Fund of CS 3", "103");

        c3.getPreRequisites().add(ImmutableSet.<Course>of(c1, c2));

        assertFalse(c3.preRecsMet(ImmutableSet.<Course>of(c1)));
    }

    @Test
    public void exactShouldMatch()
    {
        final Course c1 = new Course(ImmutableList.of("CSC"), 101, 4, "Fund of CS 1", "101");
        final Course c2 = new Course(ImmutableList.of("CSC"), 102, 4, "Fund of CS 2", "102");

        c2.getPreRequisites().add(ImmutableSet.<Course>of(c1));

        assertTrue(c2.preRecsMet(ImmutableSet.<Course>of(c1)));
        assertEquals("CSC101", c2.getPreRequisitesString());
    }

    @Test
    public void supersetShouldMatch()
    {
        final Course c1 = new Course(ImmutableList.of("CSC"), 101, 4,"Fund of CS 1",  "101");
        final Course c2 = new Course(ImmutableList.of("CSC"), 102, 4,"Fund of CS 2", "102");
        final Course c3 = new Course(ImmutableList.of("CSC"), 141, 4,"Fund of CS 3", "141");

        c2.getPreRequisites().add(ImmutableSet.<Course>of(c1));

        assertTrue(c2.preRecsMet(ImmutableSet.<Course>of(c1, c3)));
    }
    
    @Test
    public void optionShouldMatch()
    {
        final Course m1 = new Course(ImmutableList.of("MATH"),101, 4,"Calc 1", "MATH 101");
        final Course c1 = new Course(ImmutableList.of("CSC"),101, 4,"Fund of CS 1", "CSC 101");
        final Course c2 = new Course(ImmutableList.of("CSC"),102, 4,"Fund of CS 2", "CSC 102");
        final Course c3 = new Course(ImmutableList.of("CSC"),103, 4,"Fund of CS 3", "CSC 103");
        
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
        
        assertEquals("MATH101&CSC102 or MATH101&CSC101", c3.getPreRequisitesString());
    }
}
