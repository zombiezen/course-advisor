package edu.calpoly.razsoftware;

import java.util.Set;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import static org.junit.Assert.*;

public class CourseOptionTest
{
    @Test
    public void constructorTest()
    {
        Course c1,c2,c3;
        c1 = new Course(ImmutableList.of("CSC","CPE"),101,4,"Fund of CS 1", "Learn C");
        c2 = new Course(ImmutableList.of("CSC","CPE"),102,4,"Fund of CS 2", "Learn Java");
        c3 = new Course(ImmutableList.of("CSC","CPE"),103,4,"Fund of CS 3", "Do Java Faster");
        
        CourseOption co = new CourseOption("Take a CS class",ImmutableSet.of(c1,c2,c3),false);
        
        assertEquals("CSC101 or CSC102 or CSC103",co.toString());
    }
}