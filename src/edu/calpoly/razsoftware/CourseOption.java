package edu.calpoly.razsoftware;

import java.util.Set;
import java.util.TreeSet;

/**
 * Represents the different courses that can be taken to fulfill a requirement
 * 
 */
public class CourseOption 
{
    private String      requirementName;
    private Set<Course> fulfillmentOptions;
    private boolean     mutuallyExclusive;
    private int         quarter;

    /**
     * 
     * @param onlyCourse
     * @param quarterToTake 
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
     * 
     * @param name
     * @param options 
     * @param mutuallyExclusive
     * @param quarterToTake
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
     * 
     * @return Checks if course fulfills multiple requirements
     */
    public boolean isMutuallyExclusive()
    {
        return mutuallyExclusive;
    }

    /**
     * 
     * @return Gives the courses that can fulfill the requirement
     */
    public Set<Course> getFulfillmentOptions()
    {
        return fulfillmentOptions;
    }

    /**
     * 
     * @return Gives a string for requirements to fulfill prereqs for a course
     */
    public String getRequirement()
    {
        return requirementName;
    }

    /**
     * 
     * @return Gives the quarter in which the class should be taken
     */
    public int getQuarter()
    {
        return quarter;
    }

    @Override
    public String toString()
    {
        // INITIALIZE req to the empty string
        String req = "";

        // IF the set of fulfillment is not empty THEN
        if (fulfillmentOptions.size() > 0)
        {
            // FOR each course in the set of fulfillment options
            for (Course c : fulfillmentOptions)
                // CONCATENATE the course's string representation with " or "
                // and req
                req += c.toString() + " or ";
            // ENDFOR

            // REMOVE the last " or " from the string
            req = req.substring(0, req.length() - 4);
        } // ENDIF

        return req;
    }
}
