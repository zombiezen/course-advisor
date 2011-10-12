package edu.calpoly.razsoftware;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class Course
{
    private List<String> major;
    private int number;
    private int units;
    private String name;
    private String description;
    private Set<Set<Course>> preRequisites;
    private Set<Set<Course>> coRequisites;

    public Course(List<String> major, int number, int units, String name, String description)
    {
        this.major = major;
        this.number = number;
        this.units = units;
        this.name = name;
        this.description = description;
        preRequisites = new HashSet<Set<Course>>();
        coRequisites = new HashSet<Set<Course>>();
    }

    public boolean isClass(String dept, int num)
    {
        if(num == number)
            for(String s : major)
                if(s.toUpperCase().equals(dept.toUpperCase()))
                    return true;
        
        return false;
    }
    
    public boolean preRecsMet(Set<Course> coursesTaken)
    {
        boolean preRecMet = false;
        if(preRequisites != null && preRequisites.size() > 0)
        {
            for(Set<Course> s : preRequisites)
            {
                preRecMet = true;
                for(Course t : s)
                    if(!coursesTaken.contains(t))
                        preRecMet = false;
            
                if(preRecMet)
                    return true;
            }
            
            return false;
        }
        else 
        {
            return true;
        }
    }

    /**
     * used to obtain a reference to the set of corequisites
     * @return set of coRequisites
     */
    public Set<Set<Course>> getCoRequisites()
    {
        return coRequisites;
    }
    
    public void setCoRequisites(Set<Set<Course>> in)
    {
        coRequisites = in;
    }
    
    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public List<String> getMajor()
    {
        return major;
    }

    public int getNumber()
    {
        return number;
    }

    public Set<Set<Course>> getPreRequisites()
    {
        return preRequisites;
    }
    
    public String getPreRequisitesString()
    {
        String preRecStr = "";
        
        if(preRequisites.size() > 0)
        {
            for(Set<Course> s : preRequisites)
            {
                for(Course t : s)
                    preRecStr += t.toString() + "&";
                preRecStr = preRecStr.substring(0, preRecStr.length() - 1);
                preRecStr += " or ";
            }
            preRecStr = preRecStr.substring(0, preRecStr.length() - 4);
        }
        
        return preRecStr;
    }
    
    public void setPreRequisites(Set<Set<Course>> in)
    {
        preRequisites = in;
    }

    public int getUnits()
    {
        return units;
    }
    
    public String toString()
    {
        return major.get(0) + number;
    }
}
