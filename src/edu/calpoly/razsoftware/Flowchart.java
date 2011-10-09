package edu.calpoly.razsoftware;

import com.google.gson.Gson;
import java.util.Set;

public class Flowchart
{
    private Set<CourseOption> sectionReqs;

    public Set<CourseOption> getSectionReqs()
    {
        return sectionReqs;
    }

    public void addOption(CourseOption newOption)
    {
        sectionReqs.add(newOption);
    }

}
