package edu.calpoly.razsoftware;


import java.util.*;

import com.google.gson.Gson;
/**
 * this class represents a course. a general description for a quarter-long
 * session of instruction. independent of time period offered or specific
 * section. this was put together outside the production of the official course
 * object for swiftness of development and getting the course data.
 * 
 * DO NOT use this object outside of purely developer scope
 * DO NOT use this in any method or class the user directly or indirectly
 * accesses
 * 
 * @author adam
 */
public class CatConvCourse 
{
    private List<String> major;
    private int number;
    private int units;
    private String description;//list of lists for possiblities
                                                //outside or, inside and
    private String name = "";// pre or co ^^
    private Set<Set<CatConvCourse>> preRequisites;
    private Set<Set<CatConvCourse>> coRequisites;
   
   /**
    * constructs a default course, initializes all values to default
    */
    public CatConvCourse()
    {
       major = new ArrayList<String>();
       number = 0;
       units = 0;
       description = new String("");
       preRequisites = new HashSet<Set<CatConvCourse>>();
       coRequisites = new HashSet<Set<CatConvCourse>>();
    }
   
   /**
    * as in the default sense of the method
    * @param obj object to be compared to
    * @return if the object is similarly equivalent
    */
    public boolean equals(Object obj)
    {
       //IF the class of this course does not equal the class of the object
        if(this.getClass() != obj.getClass())
            return false;
        //SET eq to false
        boolean eq = false;
        //FOR every string in the major field
        for(String s : getMajor())                    //has common major name
        {
            //FOR every string in the comparing object's major field
            for(String s2 : ((CatConvCourse)obj).getMajor())
            {
                //SET eq as true if they match
                eq |= (s.equalsIgnoreCase(s2));
            }
            //ENDFOR
        }
        //ENDFOR
        //SET eq as false if the numbers don't match
        eq &= (getNumber() == ((CatConvCourse)obj).getNumber());    
        //RETURN eq
        return eq;       
    }
    
    /**
     * constructs a course with a given major and number
     * @param major string of major code
     * @param number value of course number
     */
    public CatConvCourse(String major, int number)
    {
        this.major = new ArrayList<String>();
        this.major.add(major);
        this.number = number;
    }
    
    /*---------'get' methods---------*/
    public int getNumber()
    {
        return number;
    }
    
    public String getName()
    {
        return name;
    }
   public int getUnits()
   {
       return units;
   }
   
   public String getDesc()
   {
       return getDescription();
   }
    
   /*---------'set' methods---------*/
   public void setNumber(int Number)
   {
       number = Number;
   }
   
   public void setUnits(int Units)
   {
       units = Units;
   }
   
   public void setDesc(String Description)
   {
        setDescription(Description);
   }
   
   public void setName(String Name)
   {
       name = Name;
   }
   
   public void setMajor(Collection<String> Major)
   {
        setMajor(new ArrayList<String>(Major));
   }

    /**
     * @return the major
     */
    public List<String> getMajor()
    {
        return major;
    }

    /**
     * @param major the major to set
     */
    public void setMajor(List<String> major)
    {
        this.major = major;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return the preRequisites
     */
    public Set<Set<CatConvCourse>> getPreRequisites()
    {
        return preRequisites;
    }

    /**
     * @param preRequisites the preRequisites to set
     */
    public void setPreRequisites(Set<Set<CatConvCourse>> preRequisites)
    {
        this.preRequisites = preRequisites;
    }

    /**
     * @return the coRequisites
     */
    public Set<Set<CatConvCourse>> getCoRequisites()
    {
        return coRequisites;
    }

    /**
     * @param coRequisites the coRequisites to set
     */
    public void setCoRequisites(Set<Set<CatConvCourse>> coRequisites)
    {
        this.coRequisites = coRequisites;
    }
   
}
