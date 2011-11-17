package edu.calpoly.razsoftware;

import java.io.File;
import com.google.gson.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlowchartReader
{
    private static Gson gson = new Gson();
    /**
     * provides a flowchart based off a JSON file containing newline-delineated "CourseOption" classes
     * @param path location of the JSON file
     * @return a flowchart based off the file
     */
   /* public static Flowchart readFlowchart(File path, CourseList courses)
    {
        HashSet<CourseOption> options = new HashSet<CourseOption>();
        Scanner s = null;
        try
        {
            s = new Scanner(path);
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(FlowchartReader.       class.getName()).log(Level.SEVERE, null, ex);
        }
        HashSet<Course> tmpCourseList = new HashSet<Course>();
        while(s.hasNextLine())
        {
            CourseOption co = gson.fromJson(s.nextLine(), CourseOption.class);
            for(Course c : co.getOptions())
            {
                Course course = courses.lookUp(c.getMajor().get(0), c.getNumber());
                if(course != null)
                {
                    c = course;
                }
            }
            options.add(co);
        }
        return new Flowchart(options);
    }*/

    public static Flowchart readFlowchart(InputStream iStream, CourseList courses)
    {
        // INITIALIZE the set of course options to the empty set
        HashSet<CourseOption> options = new HashSet<CourseOption>();
        // CREATE new scanner
        Scanner s = new Scanner(iStream);
        
        // WHILE the input has another line DO
        while(s.hasNextLine())
        {
            // INITIALIZE tmpCourseList to the empty set
            HashSet<Course> tmpCourseList = new HashSet<Course>();
            // PARSE a course option and store it as co
            CourseOption co = gson.fromJson(s.nextLine(), CourseOption.class);
            // FOR each course in co
            for(Course c : co.getFulfillmentOptions())
            {
                // FIND the course with the major and number parsed
                Course course = courses.lookUp(c.getMajor().get(0), c.getNumber());
                // IF there is a course with the major and number parsed THEN
                if(course != null)
                {
                    // ADD the course to tmpCourseList
                    tmpCourseList.add(course);
                }
                else
                {
                    // LOG the failure to find the course
                    System.out.println("could not find course " + c.getMajor().get(0) + " " + c.getNumber());
                } // ENDIF
            } // ENDFOR

            // CREATE a course option that uses the referenced objects
            co = new CourseOption(co.getRequirement(), tmpCourseList, co.isMutuallyExclusive(), co.getQuarter());
            System.out.println("--");
            // ADD co to the course options set
            options.add(co);
        }
        // CREATE a new flowchart from the course options set
        return new Flowchart(options);
    }

  

    public static void main(String[] args) throws FileNotFoundException
    {
        InputStream catalog = new FileInputStream("/home/aspurgin/NetBeansProjects/RAZ/src/edu/calpoly/razsoftware/Cat.json");
        InputStream flowchart = new FileInputStream("/home/aspurgin/NetBeansProjects/RAZ/src/edu/calpoly/razsoftware/FlowChart.json");
        System.out.println(catalog);
        System.out.println(flowchart);
        CourseList courses = new CourseList(catalog);
        Flowchart fc = readFlowchart(flowchart, courses);

        for(CourseOption co : fc.getSectionReqs())
        {
            for(Course c : co.getFulfillmentOptions())
            {
                System.out.println(gson.toJson(c));
            }
            System.out.print("\n----\n\n");
        }
    }
}
