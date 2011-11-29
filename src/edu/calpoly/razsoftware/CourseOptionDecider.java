package edu.calpoly.razsoftware;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

/**
 * A class that computes the course options remaining to be fulfilled.
 * 
 * @author rlight
 * @version $Revision$
 */
public class CourseOptionDecider
{
   /**
    * Computes the course options remaining to be fulfilled.
    * 
    * @param state The list of courses that have already been taken.
     * @param flowchart The flowchart that specifies
     * @return Returns the courses that need to be taken 
    */
    public Set<CourseOption> decideClasses(CourseList state, Degree flowchart)
    {
      // INITIALIZE the mapping of courses to the options it can fulfill to an
        // empty map
        final HashMap<Course, Set<CourseOption>> optionMap =
            new HashMap<Course, Set<CourseOption>>(state.getCourses().size());
        // INITIALIZE the set of courses taken to the user's list of taken courses
        final ImmutableSet<Course> taken =
            ImmutableSet.copyOf(state.getCourses());
        // INITIALIZE the set of section requirements to the flowchart's section
        // requirements
        final Set<CourseOption> sectionReqs = flowchart.getSectionReqs();
        // INITIALIZE the set of fulfilled course options to the empty set
        final Set<CourseOption> fulfilled = new HashSet<CourseOption>();

      // FOR each requirement in the flowchart's section requirements
        for (CourseOption req : sectionReqs)
        {
            final Set<Course> options = ImmutableSet.copyOf(
                req.getFulfillmentOptions());
            // COMPUTE the intersection of the taken courses with the courses that
            // can fulfill the current course option.
            final Set<Course> isect = Sets.intersection(taken, options);

            // IF the intersection set is not empty and the requirement is not
            // mutually exclusive THEN
            if (!isect.isEmpty() && !req.isMutuallyExclusive())
            {
               // ADD the course option to the set of fullfilled course options
                fulfilled.add(req);
            }
            else
            {
                // FOR each course in the intersection
                for (Course course : isect)
                {
                    // RETRIEVE the set of course options the course can fulfill from
                    // the mapping
                    Set<CourseOption> optionSet = optionMap.get(course);
    
                    // IF there is no set in the mapping THEN
                    if (optionSet == null)
                    {
                        // CREATE a new set of course options
                        optionSet = new HashSet<CourseOption>();
                        // ADD the set of course options into the mapping
                        optionMap.put(course, optionSet);
                    }
    
                    // ADD the course option to the set of options the course can
                    // fulfill
                    optionSet.add(req);
                }
            }
        } // ENDFOR

      // WHILE the mapping of courses to course options it can fulfill is not
      // empty DO
        while (!optionMap.isEmpty())
        {
           // FIND the course with the least number of course options it can
           // fulfill.
            final Map.Entry<Course, Set<CourseOption>> next =
                 Collections.min(optionMap.entrySet(),
                       new Comparator<Map.Entry<Course, Set<CourseOption>>>()
                     {
                         @Override
                         public int compare(
                               Map.Entry<Course, Set<CourseOption>> a,
                               Map.Entry<Course, Set<CourseOption>> b)
                         {
                             final int sizeA = a.getValue().size();
                             final int sizeB = b.getValue().size();
                             return new Integer(sizeA).compareTo(sizeB);
                         }
                     });

            // REMOVE the course from the mapping
            optionMap.remove(next.getKey());

            // IF the set of course options the course can fulfill is not empty
            // THEN
            if (!next.getValue().isEmpty())
            {
               // GET the first course option the course can fulfill
                final CourseOption opt = next.getValue().iterator().next();
                // ADD the course option to the set of fulfilled courses
                fulfilled.add(opt);
                // FOR each set of course options in the mapping
                for (Set<CourseOption> optionSet : optionMap.values())
                {
                   // REMOVE the course option from the other set of course options
                   // in the mapping
                    optionSet.remove(opt);
                } // ENDFOR
            } // ENDIF
        } // ENDWHILE
      
        // INITIALIZE the set of unfulfilled course options to the set of course
        // options in the flowchart
        Set<CourseOption> unfulfilled = new HashSet<CourseOption>(sectionReqs);
        // REMOVE the set of fulfilled course options from the set of unfulfilled
        // course options
        unfulfilled.removeAll(fulfilled);
        return unfulfilled;
    }
   
   /**
    * Returns the set of courses that can fulfill a set of course options.
    * 
    * @param unfulfilled The unfulfilled course options
    * @return The set of courses that can fulfill a set of course options.
    */
    public Set<Course> getRequiredCourses(Set<CourseOption> unfulfilled)
    {
        // INITIALIZE the set of required courses to the empty set
        Set<Course> required = new HashSet<Course>();

        // FOR each course option in the set of unfulfilled course options
        for (CourseOption option  : unfulfilled)
        {
            // ADD the set of courses that can fulfill the option
            required.addAll(option.getFulfillmentOptions());
        } // ENDFOR
        return required;
    }
}
