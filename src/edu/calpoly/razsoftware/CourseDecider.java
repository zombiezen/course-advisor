package edu.calpoly.razsoftware;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public class CourseDecider
{
   private CourseList        required;
   private Set<CourseOption> unfulfilled;

   public CourseDecider(CourseList required)
   {
      this.required = required;
   }

   public void decideClasses(CourseList state, Flowchart flowchart)
   {
      final HashMap<Course, Set<CourseOption>> optionMap =
            new HashMap<Course, Set<CourseOption>>(state.getCourses().size());
      final ImmutableSet<Course> taken =
            ImmutableSet.copyOf(state.getCourses());
      final Set<CourseOption> sectionReqs = flowchart.getSectionReqs();
      final Set<CourseOption> fulfilled = new HashSet<CourseOption>();

      for (CourseOption req : sectionReqs)
      {
         System.out.println("Req: "+req.getRequirement());
         
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
                     new Comparator<Map.Entry<Course, Set<CourseOption>>>()
                     {
                        @Override
                        public int compare(
                              Map.Entry<Course, Set<CourseOption>> a,
                              Map.Entry<Course, Set<CourseOption>> b)
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
      unfulfilled = new HashSet<CourseOption>(sectionReqs);
      unfulfilled.removeAll(fulfilled);
      required.clear();
      for (CourseOption o : unfulfilled)
      {
         required.addAll(o.getOptions());
      }
   }

   /**
    * Accessor to the set of unfulfilled requirements
    * @return the set of unfulfilled requirements so the controller can suggest a schedule
    */
   Set<CourseOption> getUnfulfilledOptions()
   {
      return unfulfilled;
   }
}
