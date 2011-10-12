package edu.calpoly.razsoftware;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;

public class CourseDecider
{
    public static class Result
    {
        private Set<Course> preReqMet;
        private Set<Course> preReqNotMet;

        public Result(final Set<Course> preReqMet,
                      final Set<Course> preReqNotMet)
        {
            this.preReqMet = preReqMet;
            this.preReqNotMet = preReqNotMet;
        }

        public Set<Course> getPrerequisitesMet()
        {
            return preReqMet;
        }

        public Set<Course> getPrerequisitesNotMet()
        {
            return preReqNotMet;
        }
    }

    public Result decideClasses(UserState state, Flowchart flowchart)
    {
        final Set<CourseOption> sectionReqs = flowchart.getSectionReqs();
        final ImmutableSet<Course> taken =
                ImmutableSet.copyOf(state.getTaken());
        final Set<Course> need = new HashSet<Course>();
        final Set<Course> met = new HashSet<Course>();
        final Set<Course> notMet = new HashSet<Course>();

        for (CourseOption req : sectionReqs)
        {
            final Set<Course> options = ImmutableSet.copyOf(req.getOptions());
            if (Sets.intersection(taken, options).isEmpty())
            {
                // Requirement not fulfilled
                for (Course neededCourse : options)
                {
                    if (neededCourse.preRecsMet(taken))
                    {
                        met.add(neededCourse);
                    }
                    else
                    {
                        notMet.add(neededCourse);
                    }
                }
            }
        }

        return new Result(met, notMet);
    }
}
