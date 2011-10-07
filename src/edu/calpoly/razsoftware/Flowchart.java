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

    /*
     * returns the JSON string for this object
     */
    public String ToJSONString()
    {
        // TODO (aspurgin)
        Gson gson = new Gson();
        for(CourseOption cs : sectionReqs)
    }
}
