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

        // IF the set of fulfillment is not empty THEN
        if (fulfillmentOptions.size() > 0)
        {
            // FOR each course in the set of fulfillment options
            for (Course fulfilmentCourse : fulfillmentOptions)
            {
                // CONCATENATE the course's string representation with " or "
                // and req
                req += fulfilmentCourse.toString() + " or ";
            }// ENDFOR

            // REMOVE the last " or " from the string
            req = req.substring(0, req.length() - 4);
        } // ENDIF

        return req;
    }
}
