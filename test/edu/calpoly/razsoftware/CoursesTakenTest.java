package edu.calpoly.razsoftware;

import java.io.File;
import java.io.InputStream;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author dpanger
 */
public class CoursesTakenTest
{
    public CoursesTakenTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }
    
    /**
     * Test of write method, of class CoursesTaken.
     */
    @Test
    public void testWrite() throws Exception
    {
        System.out.println("test writing an initial User State");
        InputStream fis = CourseList.class.getResourceAsStream("Cat.json");

        CourseList cl = new CourseList(fis);
        CoursesTaken us = new CoursesTaken(cl.getCourses());
        
        File file2 = new File("CatTest.json");
        us.write(file2);
        
        System.out.println("test getting User State");
        CoursesTaken us3 = new CoursesTaken(file2, cl);
        // Unless tester wants to add a lot more setup code, use System.print
        // to verify data.
        
        System.out.println("write to a null File pointer");
        CoursesTaken us2 = new CoursesTaken();
        File file7 = null;
        us2.write(file7);
        
        System.out.println("write an empty json file");
        File file3 = new File("emptyfile.json");
        us2.write(file3);
        us2.write(file3);
        
        System.out.println("write an actual json file");
        File file4 = new File("test.json");
        us2.write(file4);
    }
    
}
