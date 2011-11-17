package edu.calpoly.razsoftware;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents the Flowchart for a user
 * 
 */
public class Flowchart
{
    private Set<CourseOption> sectionReqs;

    /**
     * Used to Construct a Flowchart that represents the classes a user can take
     */
    public Flowchart()
    {
        sectionReqs = new HashSet();
    }

    /**
     * Constructor for flowchart with section requirements
     * @param sectionReqs requirements for course option
     */
    public Flowchart(Set<CourseOption> sectionReqs)
    {
       this();
        this.sectionReqs.addAll(sectionReqs);
    }

    /**
     * Returns Section Requirements
     * @return Course Options for a given course option
     */
    public Set<CourseOption> getSectionReqs()
    {
        return sectionReqs;
    }

    /**
     * Adds a course Option to the requirements of a section
     * @param newOption
     */
    public void addOption(CourseOption newOption)
    {
        sectionReqs.add(newOption);
    }
}
