
package edu.calpoly.razsoftware;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import com.google.gson.Gson;

/**
 * A utility application to change the CVS flowchart to a json file
 * 
 * @author aspurgin
 * @version $Revision$
 */
public class FlowchartCsvToJsonConverter
{     /**
     * index in split array that contains the course
     */
    public static final int kCourseIndex = 3; 
    /**
     * Runs the application to convert the CVS file
     * 
     * @param args
     *            the command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
        Gson gson = new Gson();
        File csvInput = new File("FlowChart.csv");
        if(args.length>0)
            csvInput = new File(args[0]);
        File jsonOutput = new File("FlowChart.json");
        Scanner csvReader = new Scanner(csvInput);
        FileWriter jsonWriter = new FileWriter(jsonOutput);

        // WHILE there is another line in the file
        while (csvReader.hasNextLine())
        {
            // parse the line
            String[] tokens = csvReader.nextLine().split(",");
            // assign the correct fields to the CourseOption
            String req = tokens[0];
            int quarter = Integer.valueOf(tokens[1]);
            boolean mutex = tokens[2].contains("TR");
            HashSet<Course> options = new HashSet<Course>();
            //FOR each course available for the option            
            for (int course = kCourseIndex; course < tokens.length; course++)
            {
                //Parse the courses and add it to the options
                ArrayList<String> tmplst = new ArrayList<String>();
                tmplst.add(tokens[course].split(" ")[0]);
                Course createdCourse =
                        new Course(tmplst,
                                Integer.valueOf(tokens[course].split(" ")[1]), 0,
                                "NA", "NA");
                options.add(createdCourse);
            }
            CourseOption co = new CourseOption(req, options, mutex, quarter);
            jsonWriter.append(gson.toJson(co) + "\n");
        }// ENDWHILE
        jsonWriter.close();
        csvReader.close();
    }
}
