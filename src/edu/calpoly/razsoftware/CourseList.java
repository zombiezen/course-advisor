package edu.calpoly.razsoftware;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.AbstractListModel;
import javax.swing.event.ListDataListener;

import com.google.gson.Gson;

/**
 * acts as a list of easily referenceable courses that can be constructed from
 * various sources
 * 
 * @author aspurgin
 */
public class CourseList extends AbstractListModel
{
   private Set<Course> catalog;

   /**
    * blank constructor
    */
   public CourseList()
   {
      catalog = new TreeSet<Course>();
   }

   /**
    * constructs a catalog off of a collection of courses
    * 
    * @param in
    */
   public CourseList(Collection<Course> in)
   {
      this();
      for (Course c : in)
      {
         catalog.add(c);
      }
      this.fireContentsChanged(this, 0, catalog.size());
   }

   /**
    * constructs a CourseList from a JSON input stream
    * 
    * @param input
    *           the JSON input stream
    */
   public CourseList(InputStream input)
   {
      this();
      Gson gson = new Gson();
      Scanner s = null;

      s = new Scanner(input);

      while (s.hasNextLine())
      {
         Course addMe = gson.fromJson(s.nextLine(), Course.class);
         if (lookUp(addMe.getMajor().get(0), addMe.getNumber()) == null)
         {
            catalog.add(addMe);
         }
      }

      completePointers();
   }

   /**
    * Returns true if the set contains the course passed as a parameter
    * 
    * @param c
    *           the course to look for
    * @return
    */
   public Boolean contains(Course c)
   {
      return catalog.contains(c);
   }

   /**
    * adds a single course into the set
    * 
    * @param c
    *           the course
    */
   public void add(Course c)
   {
      catalog.add(c);
      this.fireContentsChanged(this, 0, catalog.size());
   }

   /**
    * adds all courses in a collection
    * 
    * @param C
    *           a collection of courses
    */
   public void addAll(Collection<Course> C)
   {
      for (Course c : C)
      {
         add(c);
      }
      this.fireContentsChanged(this, 0, catalog.size());
   }

   /**
    * removes a course from the set
    * 
    * @param c
    *           the course to be removed
    */
   public void remove(Course c)
   {
      catalog.remove(c);
      this.fireContentsChanged(this, 0, catalog.size());
   }

   /**
    * removes a collection of courses from the set
    * 
    * @param C
    *           a collection of courses
    */
   public void removeAll(Collection<Course> C)
   {
      for (Course c : C)
      {
         remove(c);
      }
      filtered=null;

      this.fireContentsChanged(this, 0, catalog.size());
   }

   /**
    * clears the CourseList
    */
   public void clear()
   {
      catalog = new TreeSet<Course>();
      filtered=null;

      this.fireContentsChanged(this, 0, catalog.size());
   }

   /**
    * gives a reference of the current catalog
    * 
    * @return the catalog
    */
   public Set<Course> getCourses()
   {

      return this.catalog;

   }

   /**
    * replaces course keys attained from JSON file with class references from
    * CourseList
    */
   private void completePointers()
   {
      Course[] coursearray = new Course[0];
      coursearray = catalog.toArray(coursearray);
      for (Course c : coursearray)
      {
         /* workin with the prereqs */
         Set<Set<Course>> preR = c.getPreRequisites();
         Set<Set<Course>> newPreR = new HashSet<Set<Course>>();
         if (preR != null)
            for (Set<Course> sc : preR)
            {
               HashSet<Course> sub = new HashSet<Course>();
               for (Course c1 : sc)
               {
                  boolean found = false;
                  for (Course lookup : coursearray)
                  {
                     if (lookup.getNumber() != c.getNumber()
                           && lookup.getNumber() == c1.getNumber())
                     {
                        for (String s : lookup.getMajor())
                        {
                           if (c1.getMajor().contains(s))
                           {
                              sub.add(lookup);
                              found = true;
                              break;
                           }
                        }
                     }
                     if (found == true)
                        break;
                  }
               }
               newPreR.add(sub);
            }
         c.setPreRequisites(newPreR);

         /* workin with the coreqs */
         Set<Set<Course>> coR = c.getCoRequisites();
         Set<Set<Course>> newCoR = new HashSet<Set<Course>>();
         if (preR != null)
            for (Set<Course> sc : coR)
            {
               HashSet<Course> sub = new HashSet<Course>();
               for (Course c1 : sc)
               {
                  boolean found = false;
                  for (Course lookup : coursearray)
                  {
                     if (lookup.getNumber() != c.getNumber()
                           && lookup.getNumber() == c1.getNumber())
                     {
                        for (String s : lookup.getMajor())
                        {
                           if (c1.getMajor().contains(s))
                           {
                              sub.add(lookup);
                              found = true;
                              break;
                           }
                        }
                     }
                     if (found == true)
                        break;
                  }
               }
               newCoR.add(sub);
            }
         c.setCoRequisites(newCoR);
      }
   }

   /**
    * returns a full class object from the course list
    * 
    * @param major
    *           abbreviation of major
    * @param number
    *           course number
    * @return the full course reference
    */
   public Course lookUp(String major, int number)
   {
      for (Course c : catalog.toArray(new Course[catalog.size()]))
      {
         if (c.getNumber() == number)
         {
            for (String s : c.getMajor())
            {
               if (major.equalsIgnoreCase(s))
                  return c;
            }
         }
      }
      return null;
   }

   /**
    * Allow the Course List to be used to represent both a filtered and and un-filtered list
    * @returns the correct length depending on the type of list 
    */
   @Override
   public int getSize()
   {
      if (filtered == null)
      {
         return catalog.size();
      }
      else
         return filtered.size();
   }

   List<Course> filtered;

   /**
    * Allow the Course List to be used to represent both a filtered and and un-filtered list
    * @param i the index of the element to return
    * @returns the correct element depending on the type of list
    */
   @Override
   public Object getElementAt(int i)
   {
      if (filtered == null)
      {
         return catalog.toArray()[i];
      }
      else
         return filtered.get(i);

   }

   /**
    * Filters the Courses that don't match the given string out of
    * the list that this model presents to it's listeners
    * @param text The text to filter by
    */
   public void filterList(String text)
   {
      filtered = new ArrayList<Course>();
      if (text != "")
      {
         for (Course c : catalog)
         {
            if (c.toString().toLowerCase().contains(text.toLowerCase()))
            {
               filtered.add(c);
            }
         }
      }
      this.fireContentsChanged(this, 0, filtered.size());

   }
}
