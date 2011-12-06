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

import java.util.Set;
import java.util.TreeSet;

/**
 * The CourseOption class holds the set of courses that can be taken to fulfill
 * a requirement.  A CourseOption also has a name, whether it is mutually
 * exclusive, and which quarter the college recommends you take the option in.
 * 
 * @author rlight
 * @version $Revision$
 */
public class CourseOption 
{
    /** The description of the course option as it appears in the flowchart. */
    private String      requirementName;
    
    /** The set of courses that can fulfill the option. */
    private Set<Course> fulfillmentOptions;
    
    /** Whether or not fulfilling this option precludes using the course for
     *  fulfilling another course option. */
    private boolean     mutuallyExclusive;
    
    /** The recommended quarter to take the course option in.  1 is fall quarter
     *  of freshman year. */
    private int         quarter;

    /**
     * Constructs an new option that only holds one course
     * @param onlyCourse the course in this option
     * @param quarterToTake the quarter the flowchart recommends for the course
     */
    public CourseOption(Course onlyCourse, int quarterToTake)
    {
        this.requirementName = onlyCourse.toString();
        this.fulfillmentOptions = new TreeSet<Course>();
        this.fulfillmentOptions.add(onlyCourse);
        this.mutuallyExclusive = true;
        this.quarter = quarterToTake;
    }

    /**
     * Constructs a new option that holds multiple courses
     * @param name The name of the requirement
     * @param options the courses that can fulfill this requirement
     * @param mutuallyExclusive does this fulfills multiple requirements or not
     * @param quarterToTake the quarter the flowchart recommends for the course
     */
    public CourseOption(String name, Set<Course> options,
            boolean mutuallyExclusive, int quarterToTake)
    {
        this.requirementName = name;
        this.fulfillmentOptions = options;
        this.mutuallyExclusive = mutuallyExclusive;
        this.quarter = quarterToTake;
    }

    /**
     * Accessor to mutuallyExclusive
     * @return Checks if course fulfills multiple requirements
     */
    public boolean isMutuallyExclusive()
    {
        return mutuallyExclusive;
    }

    /**
     * Accesssor to the courses that can fulfill the requirement
     * @return Gives the courses that can fulfill the requirement
     */
    public Set<Course> getFulfillmentOptions()
    {
        return fulfillmentOptions;
    }

    /**
     * Accesssor to the name of the requirement
     * @return Gives a string for requirements to fulfill prereqs for a course
     */
    public String getRequirement()
    {
        return requirementName;
    }

    /**
     * Accesssor to the suggested quarter
     * @return Gives the quarter in which the class should be taken
     */
    public int getQuarter()
    {
        return quarter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        // INITIALIZE req to the empty string
        String req = "";
        String orSeparator = " or ";

        // IF the set of fulfillment is not empty THEN
        if (fulfillmentOptions.size() > 0)
        {
            // FOR each course in the set of fulfillment options
            for (Course fulfilmentCourse : fulfillmentOptions)
            {
                // CONCATENATE the course's string representation with " or "
                // and req
                req += fulfilmentCourse.toString() + orSeparator;
            }// ENDFOR

            // REMOVE the last " or " from the string
            req = req.substring(0, req.length() - orSeparator.length());
        } // ENDIF

        return req;
    }
}
