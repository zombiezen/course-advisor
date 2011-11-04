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

/**
 * acts as a list of easily referenceable courses that can be constructed from various sources
 * @author aspurgin
 */
public class CourseList
{
    private Set<Course> catalog=new TreeSet<Course>();

    /**
     * blank constructor
     */
    public CourseList()
    {
        catalog = new TreeSet<Course>();
    }


    /**
     * constructs a catalog off of a collection of courses
     * @param in 
     */
    public CourseList(Collection<Course> in)
    {
        catalog = new TreeSet<Course>();
        for(Course c : in)
        {
            catalog.add(c);
        }
    }

    /**
     * constructs a CourseList from a JSON input stream
     * @param input the JSON input stream
     */
    public CourseList(InputStream input)
    {
        Gson gson = new Gson();
        Scanner s = null;
        
        s = new Scanner(input);   
     
        while(s.hasNextLine())
        {
            Course addMe = gson.fromJson(s.nextLine(), Course.class);
            if(lookUp(addMe.getMajor().get(0), addMe.getNumber()) == null)
            {
                catalog.add(addMe);
            }
        }
        
        completePointers();
    }

    /**
     * adds a single course into the set
     * @param c the course
     */
    public void add(Course c)
    {
        catalog.add(c);
    }

    /**
     * adds all courses in a collection
     * @param C a collection of courses
     */
    public void addAll(Collection<Course> C)
    {
        for(Course c : C)
        {
            add(c);
        }
    }

    /**
     * removes a course from the set
     * @param c the course to be removed
     */
    public void remove(Course c)
    {
        catalog.remove(c);
    }


    /**
     * removes a collection of courses from the set
     * @param C a collection of courses
     */
    public void removeAll(Collection<Course> C)
    {
        for(Course c : C)
        {
            remove(c);
        }
    }

    /**
     * clears the CourseList
     */
    public void clear()
    {
        catalog = new TreeSet<Course>();
    }

    /**
     * gives a reference of the current catalog
     * @return the catalog
     */
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
