package edu.calpoly.razsoftware;

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
        // TODO(rlight)
        return null;
    }
}
