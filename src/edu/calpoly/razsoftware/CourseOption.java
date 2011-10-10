package edu.calpoly.razsoftware;

import java.util.Set;
import java.util.TreeSet;

public class CourseOption
{
    private String requirement;
    private Set<Course> options;
    private boolean mutuallyExclusive;
    
    public CourseOption(Course onlyCourse)
    {
        this.requirement = onlyCourse.toString();
        this.options = new TreeSet<Course>();
        this.options.add(onlyCourse);
        this.mutuallyExclusive = true;
    }
    
    public CourseOption(String name, Set<Course> options, boolean mutuallyExclusive)
    {
        this.requirement = name;
        this.options = options;
        this.mutuallyExclusive = mutuallyExclusive;
    }

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
