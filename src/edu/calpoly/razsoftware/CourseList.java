package edu.calpoly.razsoftware;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseList
{
    private Set<Course> catalog=new TreeSet<Course>();

    
    /**
     * constructs a catalog off of a collection of courses
     * @param in 
     */
    public CourseList(Collection<Course> in)
    {
        for(Course c : in)
        {
            catalog.add(c);
        }
    }
    /**
     * constructs a course list based off a given JSON file
     * @param path File path
     */
    public CourseList(File path)
    {
        // TODO(rlight)
        Gson gson = new Gson();
        Scanner s = null;
        try
        {
            s = new Scanner(path);
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(CourseList.class.getName()).log(Level.SEVERE, null, ex);
        }
        while(s.hasNextLine())
        {
            catalog.add(gson.fromJson(s.nextLine(), Course.class));
        }
    }
    
    public CourseList(InputStream input)
    {
        // TODO(rlight)
        Gson gson = new Gson();
        Scanner s = null;
//        try
//        {
            s = new Scanner(input);
//        }
//        catch (FileNotFoundException ex)
//        {
//            Logger.getLogger(CourseList.class.getName()).log(Level.SEVERE, null, ex);
//        }
        while(s.hasNextLine())
        {
            catalog.add(gson.fromJson(s.nextLine(), Course.class));
        }
        completePointers();
    }

    public Set<Course> getCatalog() {
        
        return this.catalog;
        
    }
    /**
     * replaces course keys attained from JSON file with class references from CourseList
     */
    
    private void completePointers()
    {
        Course[] coursearray = new Course[0];
        coursearray = catalog.toArray(coursearray);
        for(Course c : coursearray)
        {
            /*workin with the prereqs*/
            Set<Set<Course>> preR = c.getPreRequisites();
            Set<Set<Course>> newPreR = new HashSet<Set<Course>>();
            if(preR != null) for(Set<Course> sc : preR)
            {
                HashSet<Course> sub = new HashSet<Course>();
                for(Course c1 : sc)
                {
                    boolean found = false;
                    for(Course lookup : coursearray)
                    {
                        if(lookup.getNumber() != c.getNumber() && lookup.getNumber() == c1.getNumber())
                        {
                            for(String s : lookup.getMajor())
                            {
                                if(c1.getMajor().contains(s))
                                {
                                    sub.add(lookup);
                                    found = true;
                                    break;
                                }                               
                            }
                        }
                        if (found == true)
                            break;                            
                    }
                }
                newPreR.add(sub);
            }
            c.setPreRequisites(newPreR);
            
            
            /*workin with the coreqs*/
            Set<Set<Course>> coR = c.getCoRequisites();
            Set<Set<Course>> newCoR = new HashSet<Set<Course>>();
            if(preR != null) for(Set<Course> sc : coR)
            {
                HashSet<Course> sub = new HashSet<Course>();
                for(Course c1 : sc)
                {
                    boolean found = false;
                    for(Course lookup : coursearray)
                    {
                        if(lookup.getNumber() != c.getNumber() && lookup.getNumber() == c1.getNumber())
                        {
                            for(String s : lookup.getMajor())
                            {
                                if(c1.getMajor().contains(s))
                                {
                                    sub.add(lookup);
                                    found = true;
                                    break;
                                }                               
                            }
                        }
                        if (found == true)
                            break;                            
                    }
                }
                newCoR.add(sub);
            }
            c.setCoRequisites(newCoR);
        }
    }
   

    /**
     * returns a full class object from the course list
     * @param major abbreviation of major
     * @param number course number
     * @return the full course reference
     */
    public Course lookUp(String major, int number) 
    {    
        for(Course c :catalog.toArray(new Course[catalog.size()]))
        {
            if(c.getNumber() == number)
            {
                for(String s : c.getMajor())
                {
                    if(major.equalsIgnoreCase(s))
                        return c;
                }
            }
        }
        return null;
    }
}
