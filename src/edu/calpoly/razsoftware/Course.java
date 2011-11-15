package edu.calpoly.razsoftware;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
/**
 * Represents a course the user can take
 * 
 * 
 */
public class Course implements Comparable<Course>
{
    private List<String>     major;
    private int              number;
    private int              units;
    private String           name;
    private String           description;
    private Set<Set<Course>> preRequisites;
    private Set<Set<Course>> coRequisites;

    /**
     * Used to construct a course
     * @param major The course tag for the major
     * @param number The course number
     * @param units The number of units the course is worth
     * @param name The name of the course
     * @param description The description of the course
     */
    public Course(List<String> major, int number, int units, String name,
            String description)
    {
        this.major = major;
        this.number = number;
        this.units = units;
        this.name = name;
        this.description = description;
        preRequisites = new HashSet<Set<Course>>();
        coRequisites = new HashSet<Set<Course>>();
    }

    @Override
    public int compareTo(Course c)
    {
        return this.toString().compareTo(c.toString());
    }
    
    /**
     * Used to check if the course exists
     * @param dept Which major department the course is in
     * @param num The number for the class
     * @return returns true if the course exists, otherwise false
     */
    public boolean isClass(String dept, int num)
    {
        if(num == number)
            for(String s : major)
                if(s.toUpperCase().equals(dept.toUpperCase()))
                    return true;
        
        return false;
    }
    
    /**
     * Used to check if the prereqs for a class has been met
     * @param coursesTaken The courses the user has taken
     * @return returns true if the prerecs have been met, otherwise false
     */
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
    
    /**
     * Sets which courses should be taken in conjuction with the course
     * @param in The set of courses needed to be taken together
     */
    public void setCoRequisites(Set<Set<Course>> in)
    {
        coRequisites = in;
    }
    
    /**
     * Gets the name of a course
     * @return Returns a string with the name of a course
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the description of a course
     * @return returns a string with the description of the course
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Gets the major the course falls under
     * @return Returns the majors the course falls under
     */
    public List<String> getMajor()
    {
        return major;
    }

    /**
     * Returns the number that goes before the major tag
     * @return Returns an int with the number of the course
     */
    public int getNumber()
    {
        return number;
    }

    /**
     * Gets the set of prerequisites for a course
     * @return returns a set with the prerequisites
     */
    public Set<Set<Course>> getPreRequisites()
    {
        return preRequisites;
    }
    
    /**
     * Gets the prereqs in the form of a string
     * @return The prereqs in the form of a string
     */
    public String getPreRequisitesString()
    {
        String preRecStr = "";
        
        if(preRequisites.size() > 0)
        {
            for(Set<Course> s : preRequisites)
            {
                for(Course t : s)
                    preRecStr += t.toString() + "&";
                }
                if (preRecStr.length() != 0)
                {
                    preRecStr = preRecStr.substring(0, preRecStr.length() - 1);
                }
                preRecStr += " or ";
            }
            preRecStr = preRecStr.substring(0, preRecStr.length() - 4);
        }
        
        return preRecStr;
    }
    
    /**
     * Sets the prereqs according to the input
     * @param in The prereqs for a course
     */
    public void setPreRequisites(Set<Set<Course>> in)
    {
        preRequisites = in;
    }

    /**
     * Gets the units of a course
     * @return an Int with the amount of units a course is worth
     */
    public int getUnits()
    {
        return units;
    }
    
    @Override
    public String toString()
    {
        return major.get(0) + number;
    }
}
