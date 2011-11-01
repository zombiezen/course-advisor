package edu.calpoly.razsoftware;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CourseDecider
{
    public Set<CourseOption> decideClasses(CoursesTaken state, Flowchart flowchart)
    {
        final HashMap<Course, Set<CourseOption>> optionMap =
                new HashMap<Course, Set<CourseOption>>(state.getTaken().size());
        final ImmutableSet<Course> taken =
                ImmutableSet.copyOf(state.getTaken());
        final Set<CourseOption> sectionReqs = flowchart.getSectionReqs();
        final Set<CourseOption> fulfilled = new HashSet<CourseOption>();

        for (CourseOption req : sectionReqs)
        {
            final Set<Course> options = ImmutableSet.copyOf(req.getOptions());
            final Set<Course> isect = Sets.intersection(taken, options);

            if (!isect.isEmpty() && !req.isMutuallyExclusive())
            {
                fulfilled.add(req);
            }
            else
            {
                for (Course course : isect)
                {
                    Set<CourseOption> optionSet = optionMap.get(course);
                    if (optionSet == null)
                    {
                        optionSet = new HashSet<CourseOption>();
                        optionMap.put(course, optionSet);
                    }
                    optionSet.add(req);
                }
            }
        }

        while (!optionMap.isEmpty())
        {
            final Map.Entry<Course, Set<CourseOption>> next =
                    Collections.min(optionMap.entrySet(),
                    new Comparator<Map.Entry <Course, Set<CourseOption>>>()
                    {
                        @Override
                        public int compare(
                                Map.Entry <Course, Set<CourseOption>> a,
                                Map.Entry <Course, Set<CourseOption>> b)
                        {
                            final int sizeA = a.getValue().size();
                            final int sizeB = b.getValue().size();

                            if (sizeA < sizeB)
                                return -1;
                            else if (sizeA > sizeB)
                                return 1;
                            else
                                return 0;
                        }
                    });

            optionMap.remove(next.getKey());
            
            if (!next.getValue().isEmpty())
            {
                final CourseOption opt = next.getValue().iterator().next();
                fulfilled.add(opt);
                for (Set<CourseOption> s : optionMap.values())
                {
                    s.remove(opt);
                }
            }
        }

        Set<CourseOption> unfulfilled = new HashSet<CourseOption>(sectionReqs);
        unfulfilled.removeAll(fulfilled);
        return unfulfilled;
    }
}
