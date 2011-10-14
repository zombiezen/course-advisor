package edu.calpoly.razsoftware;

import java.io.File;
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
public class UserStateTest
{
    public UserStateTest()
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
     * Test of getTaken method, of class UserState.
     */
    @Test
    public void testGetTaken()
    {
        System.out.println("getTaken");
        
    }

    /**
     * Test of write method, of class UserState.
     */
    @Test
    public void testWrite() throws Exception
    {
        UserState us = new UserState();
        
        File file = new File("/home/dpanger/NetBeansProjects/Cat.json");
        UserState us2 = new UserState(file);
        
        System.out.println("write to a null File pointer");
        File file2 = null;
        us.write(file2);
        
        System.out.println("write an empty json file");
        File file3 = new File("emptyfile.json");
        us.write(file3);
        us.write(file3);
        
        System.out.println("write an actual json file");
        File file4 = new File("test.json");
        us2.write(file4);
    }
    
}
