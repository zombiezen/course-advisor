package edu.calpoly.razsoftware;

import java.util.Set;

public class CourseOption
{
    private String requirement;
    private Set<Course> options;
    private boolean mutuallyExclusive;

    public boolean isMutuallyExclusive()
    {
        return mutuallyExclusive;
    }

    public Set<Course> getOptions()
    {
        return options;
    }

    public String getRequirement()
    {
        return requirement;
    }
}
