package edu.calpoly.razsoftware;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;

public class CourseDecider
{
    public Set<CourseOption> decideClasses(UserState state, Flowchart flowchart)
    {
        final Set<CourseOption> sectionReqs = flowchart.getSectionReqs();
        final ImmutableSet<Course> taken =
                ImmutableSet.copyOf(state.getTaken());
        final Set<CourseOption> notFulfilled = new HashSet<CourseOption>();

        for (CourseOption req : sectionReqs)
        {
            final Set<Course> options = ImmutableSet.copyOf(req.getOptions());
            if (Sets.intersection(taken, options).isEmpty())
            {
                notFulfilled.add(req);
            }
        }

        return notFulfilled;
    }
}
