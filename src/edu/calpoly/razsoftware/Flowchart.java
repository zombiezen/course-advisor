package edu.calpoly.razsoftware;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Flowchart
{
    private Set<CourseOption> sectionReqs;

    public Flowchart()
    {
        sectionReqs = new HashSet();
    }

    public Flowchart(Set<CourseOption> sectionReqs)
    {
       this();
        this.sectionReqs.addAll(sectionReqs);
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
