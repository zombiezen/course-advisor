package edu.calpoly.razsoftware;

import java.util.Set;
import java.util.TreeSet;

public class CourseOption
{
    private String requirement;
    private Set<Course> options;
    private boolean mutuallyExclusive;
    private int quarter;
    
    public CourseOption(Course onlyCourse)
    {
        this.requirement = onlyCourse.toString();
        this.options = new TreeSet<Course>();
        this.options.add(onlyCourse);
        this.mutuallyExclusive = true;
    }
    
    public CourseOption(String name, Set<Course> options, boolean mutuallyExclusive, int quarter)
    {
        this.requirement = name;
        this.options = options;
        this.mutuallyExclusive = mutuallyExclusive;
        this.quarter = quarter;
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
    
    @Override
    public String toString()
    {
        String req = "";
        
        if(options.size() > 0)
        {
            for(Course c : options)
                req += c.toString() + " or ";
            
            req = req.substring(0,req.length() - 4);
        }
        
        return req;
    }
}
