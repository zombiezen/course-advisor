package edu.calpoly.razsoftware;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseList
{
    private Set<Course> catalog;

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

    private void completePointers()
    {
        for(Course c : catalog)
        {
            Set<Set<Course>> newPre = new HashSet<Set<Course>>(); 
            for(Set<Course> s : c.getPreRequisites())
            {
                HashSet<Course> newset = new HashSet<Course>();
                for(Course c1 : s)
                {
                    Course match = lookUp(c1.getMajor().get(0), c1.getNumber());
                    //if(match != null)
                        //System.out.println("\"" + c1.major.get(0) + c1.number + "\" " +match.ToString());
                    newset.add(match);
                }
                newPre.add(newset);
                //System.out.println(newset.toString());
            }
            c.setPreRequisites(newPre);
            
            Set<Set<Course>> newCo = new HashSet<Set<Course>>(); 
            for(Set<Course> s : c.getCoRequisites())
            {
                HashSet<Course> newset = new HashSet<Course>();
                for(Course c1 : s)
                {
                    Course match = lookUp(c1.getMajor().get(0), c1.getNumber());
                    //if(match != null)
                        //System.out.println("\"" + c1.major.get(0) + c1.number + "\" " +match.ToString());
                    newset.add(match);
                }
                newCo.add(newset);
            }
            c.setCoRequisites(newCo);
        }
    }
    
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
