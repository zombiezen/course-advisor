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

import com.google.gson.Gson;

/**
 * represents a list of easily referenceable courses that can be constructed
 * from various sources
 * 
 * @author aspurgin
 */
public class CourseList extends AbstractListModel
{
    private Set<Course> courses;

    /**
     * blank constructor, initializes the inner set as a treeset
     */
    public CourseList()
    {
        courses = new TreeSet<Course>();
    }

    /**
     * constructs a catalog off of a collection of courses
     * 
     * @param in
     *            collection of courses
     */
    public CourseList(Collection<Course> in)
    {
        this();
        // FOR every course in the collection
        for (Course course : in)
        {
            // add course to the catalog
            courses.add(course);
        }
        // signal the controller
        this.fireContentsChanged(this, 0, courses.size());
    }

    /**
     * constructs a CourseList from a JSON input stream
     * 
     * @param input
     *            the JSON input stream
     */
    public CourseList(InputStream input)
    {
        this();
        Gson gson = new Gson();
        Scanner streamReader = null;

        streamReader = new Scanner(input);

        // WHILE input has another line DO
        while (streamReader.hasNextLine())
        {
            // PARSE a course from the current line
            Course addMe = gson.fromJson(streamReader.nextLine(), Course.class);
            // IF the list does not have a course with the major and number THEN
            if (lookUp(addMe.getMajor().get(0), addMe.getNumber()) == null)
            {
                // ADD the course to the list
                courses.add(addMe);
            } // ENDIF
        } // ENDWHILE

        // DETERMINE references for prerequisites
        completePointers();
    }

    /**
     * Returns true if the set contains the course passed as a parameter
     * 
     * @param c
     *            the course to look for
     * @return true if the list contains the course
     */
    public Boolean contains(Course c)
    {
        return courses.contains(c);
    }

    /**
     * adds a single course into the set
     * 
     * @param c
     *            the course
     */
    public void add(Course c)
    {
        courses.add(c);
        this.fireContentsChanged(this, 0, courses.size());
    }

    /**
     * adds all courses in a collection
     * 
     * @param addedCourses
     *            a collection of courses
     */
    public void addAll(Collection<Course> addedCourses)
    {
        // FOR each Course in the collection
        for (Course course : addedCourses)
        {
            // add the course to the list
            add(course);
        }
        this.fireContentsChanged(this, 0, courses.size());
    }

    /**
     * removes a course from the set
     * 
     * @param c
     *            the course to be removed
     */
    public void remove(Course c)
    {
        courses.remove(c);
        this.fireContentsChanged(this, 0, courses.size());
    }

    /**
     * removes a collection of courses from the set
     * 
     * @param removedCourses
     *            a collection of courses
     */
    public void removeAll(Collection<Course> removedCourses)
    {
        // FOR each Course in the collection
        for (Course course : removedCourses)
        {
            // remove the course from the list
            remove(course);
        }
        filtered = null;

        this.fireContentsChanged(this, 0, courses.size());
    }

    /**
     * clears the CourseList
     */
    public void clear()
    {
        courses = new TreeSet<Course>();
        filtered = null;

        this.fireContentsChanged(this, 0, courses.size());
    }

    /**
     * gives a reference of the current catalog
     * 
     * @return the catalog
     */
    public Set<Course> getCourses()
    {

        return this.courses;

    }

    /**
     * replaces course keys attained from JSON file with class references from
     * CourseList
     */
    private void completePointers()
    {
        // INITIALIZE coursearray to the contents of the course list
        Course[] coursearray = new Course[0];
        coursearray = courses.toArray(coursearray);
        // FOR each course in coursearray
        for (Course course : coursearray)
        {
            // GET the prerequisites of the course
            Set<Set<Course>> preR = course.getPreRequisites();
            // INITIALIZE newPreR to the empty set
            Set<Set<Course>> newPreR = new HashSet<Set<Course>>();
            // IF the course has prerequisites
            if (preR != null)
            {
                // FOR each 'or' set of courses in the prerequisites of the
                // course
                for (Set<Course> sc : preR)
                {
                    // INITIALIZE sub to the empty set
                    HashSet<Course> sub = new HashSet<Course>();
                    // FOR each course in the set of 'or' courses
                    for (Course c1 : sc)
                    {
                        // INITIALIZE found to false
                        boolean found = false;
                        // FOR each course in coursearray
                        for (Course lookup : coursearray)
                        {
                            // IF the coursenumber of the current course does
                            // not equal
                            // the Course number of the currently looked up
                            // course THEN
                            if (lookup.getNumber() != course.getNumber()
                                    && lookup.getNumber() == c1.getNumber())
                            {
                                // FOR every major String in the lookup course
                                for (String majorString : lookup.getMajor())
                                {
                                    // IF the course to be looked up contains
                                    // the string
                                    // from the lookup course THEN
                                    if (c1.getMajor().contains(majorString))
                                    {
                                        // add lookup to sub
                                        sub.add(lookup);
                                        // SET found to TRUE
                                        found = true;
                                        break;
                                    }
                                    // ENDIF
                                }
                                // ENDFOR
                            }
                            // ENDIF

                            // IF found is true
                            if (found)
                            {
                                break;
                            }
                            // ENDIF
                        }
                        // ENDFOR
                    }
                    // ENDFOR
                    newPreR.add(sub);
                }
                // ENDFOR
            } // ENDIF
            course.setPreRequisites(newPreR);

            /* workin with the coreqs */

            // pseudocode is same as above section, just working with different
            // part
            Set<Set<Course>> coR = course.getCoRequisites();
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
                            if (lookup.getNumber() != course.getNumber()
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
                            if (found)
                            {
                                break;
                            }
                        }
                    }
                    newCoR.add(sub);
                }
            course.setCoRequisites(newCoR);
        }
    }

    /**
     * returns a full class object from the course list
     * 
     * @param major
     *            abbreviation of major
     * @param number
     *            course number
     * @return the full course reference
     */
    public Course lookUp(String major, int number)
    {
        // FOR each item in the catalog
        for (Course course : courses.toArray(new Course[courses.size()]))
        {
            // IF the number matches the desired number THEN
            if (course.getNumber() == number)
            {
                // FOR every major in that course
                for (String majorString : course.getMajor())
                {
                    // IF the major is the same as the lookup THEN
                    if (major.equalsIgnoreCase(majorString))
                    {
                        return course;
                    }
                }
                // ENDFOR
            }
            // ENDIF
        }
        // ENDFOR
        return null;
    }

    /**
     * Allow the Course List to be used to represent both a filtered and and
     * un-filtered list
     * 
     * @return the correct length depending on the type of list
     */
    @Override
    public int getSize()
    {
        // IF filtered is null THEN
        if (filtered == null)
        {
            return courses.size();
        }
        // ELSE
        else
        {
            return filtered.size();
        }
        // ENDIF
    }

    private List<Course> filtered;
    
    /**
     * Accessor to the filtered list of courses
     * @return the filtered list of courses
     */
    List<Course> getFiltered()
    {
        return filtered;
        
    }

    /**
     * Allow the Course List to be used to represent both a filtered and and
     * un-filtered list
     * 
     * @param i
     *            the index of the element to return
     * @return the correct element depending on the type of list
     */
    @Override
    public Object getElementAt(int i)
    {
        // IF filtered is null
        if (filtered == null)
        {
            return courses.toArray()[i];
        }
        // ELSE
        else
        {
            return filtered.get(i);
        }
        // ENDIF

    }

    /**
     * Filters the Courses that don't match the given string out of the list
     * that this model presents to it's listeners
     * 
     * @param text
     *            The text to filter by
     */
    public void filterList(String text)
    {
        // INITIALIZE filtered as new list
        filtered = new ArrayList<Course>();
        // IF text is not empty
        if (!text.equals(""))
        {
            // FOR every course in the list
            for (Course course : courses)
            {
                // IF the course contains the text, THEN
                if (course.toString().toLowerCase().contains(text.toLowerCase()))
                {
                    // add c to the filtered list
                    filtered.add(course);
                }
                // ENDIF
            }
            // ENDFOR
        }
        // ENDIF
        this.fireContentsChanged(this, 0, filtered.size());

    }
}
