package edu.calpoly.razsoftware;

import java.util.Set;
import java.util.TreeSet;


public class CourseOption 
//implements Comparable<CourseOption>
{
    private String      requirement;
    private Set<Course> options;
    private boolean     mutuallyExclusive;
    private int         quarter;

    /**
     * 
     * @param onlyCourse
     * @param quarterToTake 
     */
    public CourseOption(Course onlyCourse, int quarterToTake)
    {
        this.requirement = onlyCourse.toString();
        this.options = new TreeSet<Course>();
        this.options.add(onlyCourse);
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
        this.requirement = name;
        this.options = options;
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
    public Set<Course> getOptions()
    {
        return options;
    }

    /**
     * 
     * @return Gives a string for requirements to fulfill prereqs for a course
     */
    public String getRequirement()
    {
        return requirement;
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
        String req = "";

        if (options.size() > 0)
        {
            for (Course c : options)
                req += c.toString() + " or ";

            req = req.substring(0, req.length() - 4);
        }

        return req;
    }

//    @Override
//    public int compareTo(CourseOption o)
//    {
//        if (quarter == o.quarter)
//        {
//            return 0;
//        }
//        else if (quarter < o.quarter)
//        {
//            return -1;
//        }
//        return 1;
//        // return 0;
//    }
}
