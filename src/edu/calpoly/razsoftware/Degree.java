package edu.calpoly.razsoftware;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents the Flowchart for a user
 * 
 * @author rlight
 * @version $Revision$
 */
public class Degree
{
    private Set<CourseOption> sectionReqs;

    /**
     * Used to Construct a Flowchart that represents the classes a user can take
     */
    public Degree()
    {
        sectionReqs = new HashSet<CourseOption>();
    }

    /**
     * Constructor for flowchart with section requirements
     * @param sectionReqs requirements for course option
     */
    public Degree(Set<CourseOption> sectionReqs)
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
     * @param newOption the option to add to this flowchart
     */
    public void addOption(CourseOption newOption)
    {
        sectionReqs.add(newOption);
    }
}
