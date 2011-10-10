package edu.calpoly.razsoftware;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CourseDecider
{
    public static class Result
    {
        private Set<CourseOption> preReqMet;
        private Set<CourseOption> preReqNotMet;
        
        public Result(final Set<CourseOption> preReqMet,
                      final Set<CourseOption> preReqNotMet)
        {
            this.preReqMet = preReqMet;
            this.preReqNotMet = preReqNotMet;
        }

        public Set<CourseOption> getPrerequisitesMet()
        {
            return preReqMet;
        }
        
        public Set<CourseOption> getPrerequisitesNotMet()
        {
            return preReqNotMet;
        }
    }

    public Result decideClasses(UserState state, Flowchart flowchart)
    {
        final Set<CourseOption> sectionReqs = flowchart.getSectionReqs();
        final Set<Course> available = state.getTaken();
        final Set<CourseOption> met = new HashSet(), notMet = new HashSet();

        for (CourseOption req : sectionReqs)
        {
            Course fulfillingCourse = null;

            for (Course opt : req.getOptions())
            {
                if (available.contains(opt))
                {
                    fulfillingCourse = opt;
                    break;
                }
            }

            if (fulfillingCourse != null)
            {
                met.add(req);
                if (req.isMutuallyExclusive())
                {
                    available.remove(fulfillingCourse);
                }
            }
            else
            {
                notMet.add(req);
            }
        }

        return new Result(met, notMet);
    }
}
