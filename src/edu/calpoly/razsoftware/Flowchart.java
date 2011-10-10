package edu.calpoly.razsoftware;

import java.util.HashSet;
import java.util.Set;

public class Flowchart
{
    private Set<CourseOption> sectionReqs;

    public Flowchart()
    {
        sectionReqs = new HashSet();
    }

    public Flowchart(Set<CourseOption> sectionReqs)
    {
        this.sectionReqs = new HashSet<CourseOption>(sectionReqs);
    }

    public Set<CourseOption> getSectionReqs()
    {
        return sectionReqs;
    }

    public void addOption(CourseOption newOption)
    {
        sectionReqs.add(newOption);
    }
}
