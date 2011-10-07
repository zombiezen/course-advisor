package edu.calpoly.razsoftware;

import java.util.List;
import java.util.Set;

public class Course
{
    private List<String> major;
    private int number;
    private int units;
    private String name;
    private String description;
    private Set<Set<Course>> preRequisites;
    private Set<Set<Course>> coRequisites;

    public Course(List<String> major, int number, int units, String description)
    {
        // TODO(msvanbee)
    }

    boolean preRequisitesMet(List<Course> taken)
    {
        // TODO(msvanbee)
        return false;
    }

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
    
    public void setPreRequisites(Set<Set<Course>> in)
    {
        preRequisites = in;
    }

    public int getUnits()
    {
        return units;
    }
}
