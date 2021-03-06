/*
 * Copyright 2011 RAZ Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.calpoly.razsoftware;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
/**
 * Represents a course the user can take.  A course has at least one major that
 * it is listed as, a number representing its course number, a number of units,
 * a name, a description, and a representation of its prerequisites.
 * 
 * @author msvanbee
 * @version $Revision$
 */
public class Course implements Comparable<Course>
{
    /* Represents the padding in pixles between courses */
    private static final int kPADDING = 4;
    
    /* Represents the names of the majors that this course is listed as */
    private List<String>     major;
    
    /* Represnts the course number of this Course.  Ex: 102 for CPE102 */
    private int              number;
    
    /* Represnts the number of units for this Course.  Ex: 4 for CPE 308 */
    private int              units;
    
    /* Represents the name of this Course.*/
    private String           name;
    
    /* Represents a description of the Course */
    private String           description;
    
    /* Represents a set of set of courses that are prerequisites for this Course
     * .  The bottom set is "AND" and the top set is "OR".
     */
    private Set<Set<Course>> preRequisites;
    
    /* Represents a set of set of courses that are corequisites for this Course
     * .  The bottom set is "AND" and the top set is "OR".
     */
    private Set<Set<Course>> coRequisites;

    /**
     * Constructs a new Course used to represent a course the user can take
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

    /**
     * {@inheritDoc}
     */
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
        // IF num = the course's number THEN
        if(num == number)
        {
            // FOR each of the course's majors
            for(String majorString : major)
            {
                // IF the major is equal to dept (case-insensitive) THEN
                if(majorString.toUpperCase().equals(dept.toUpperCase()))
                {
                    return true;
                }// ENDIF
            }// ENDFOR
        }// ENDIF
                
        
        return false;
    }
    
    /**
     * Used to check if the prereqs for a class has been met
     * @param coursesTaken The courses the user has taken
     * @return returns true if the prerecs have been met, otherwise false
     */
    public boolean preRecsMet(Set<Course> coursesTaken)
    {
        // INITIALIZE preRecMet to false
        boolean preRecMet = false;
        // IF the set of prerequisites is not empty THEN
        if(preRequisites != null && preRequisites.size() > 0)
        {
            // FOR each set of courses in the set of prerequisites
            for(Set<Course> courseSet : preRequisites)
            {
                // SET preRecMet to true
                preRecMet = true;
                // FOR each course in the set of courses
                for(Course prereqCourse : courseSet)
                {
                    // IF the set of courses taken does not contain the course
                    // THEN
                    if(!coursesTaken.contains(prereqCourse))
                    {
                        // SET preRecMet to false
                        preRecMet = false;
                    }// ENDIF
                }
            
                // IF preRecMet THEN
                if(preRecMet)
                {
                    return true;
                }
                // ENDIF
            } // ENDFOR
            
            return false;
        }
        else 
        {
            return true;
        } // ENDIF
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
        // INITIALIZE preRecStr to the empty string
        String preRecStr = "";
        
        // IF the set of prerequisites is not empty THEN
        if(preRequisites.size() > 0)
        {
            // FOR each set of courses in the set of prerequisites
            for(Set<Course> prereqSet : preRequisites)
            {
                // FOR each course in the set of courses
                for(Course prereqCourse : prereqSet)
                {
                    // CONCATENATE the name of the course and an ampersand to
                    // preRecStr
                    preRecStr += prereqCourse.toString() + "&";
                }// ENDFOR
                 // IF preRecStr is not an empty string THEN
                if (preRecStr.length() != 0)
                {
                    // REMOVE the last character(&) of preRecStr
                    preRecStr = preRecStr.substring(0, preRecStr.length() - 1);
                } // ENDIF
                
                // CONCATENATE " or " with preRecStr
                preRecStr += " or ";
                
            } // ENDFOR
            

        } 
        // IF preRecStr is not an empty string THEN
        if (preRecStr.length() != 0)
        {
            // REMOVE the last " or " from preRecStr
            preRecStr = preRecStr.substring(0, preRecStr.length() - kPADDING);
        } // ENDIF
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
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return major.get(0) + number;
    }

}
