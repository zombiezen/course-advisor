package edu.calpoly.razsoftware;

import com.google.gson.*;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;

/**
 * Represents a current user's "state", which is the courses that he or she
 * has already completed.
 * @author RAZ Software
 */
public class CoursesTaken
{
    /**
     * The courses the user has taken.
     */
    private Set<Course> taken;

    /**
     * Creates a new, empty CoursesTaken object.
     */
    public CoursesTaken()
    {
        taken = new HashSet<Course>();
    }

    /**
     * Creates a new CoursesTaken object from a given Set of Courses the user has
     * completed.
     * @param taken the Set of Courses the user has completed.
     */
    public CoursesTaken(Set<Course> taken)
    {
        this.taken = new HashSet<Course>(taken);
    }

    /**
     * Creates a new CoursesTaken object from a given JSon file and a user's
     * course list
     * @param file The JSon file that holds the user's "state"
     * @param cl The course list for the user.
     * @throws IOException If the file is not found or can not be opened, an
     * IOException is thrown
     */
    public CoursesTaken(File file, CourseList cl) throws IOException
    {
        Scanner takenScanner = new Scanner(file);
        taken = new HashSet<Course>();

        //WHILE the user has taken more courses
        while (takenScanner.hasNextLine())
        {

            String str = takenScanner.nextLine();

            JsonParser jparse = new JsonParser();
            JsonElement jelem = jparse.parse(str);
            JsonObject json = jelem.getAsJsonObject();
            String major = json.get("major").getAsString();
            Integer number = json.get("number").getAsInt();

            // Look up the course from the Class List
            Course c2 = cl.lookUp(major, number);

            // Add Class List course so we are using the same objects, not
            // creating new ones.
            taken.add(c2);

        } // else we have an empty JSon file

        takenScanner.close();
    }

    /**
     * Gets the user's state.
     * @return a Set of Courses that the user has completed or null if the
     * user's state has not yet been set. 
     */
    public Set<Course> getTaken()
    {
        return taken;
    }

    /**
     * Writes the user's current "state" to the file that is passed in.
     * @param file The file to write the current user's state to.
     * @throws IOException IOException is thrown if the file that is passed in 
     * is not found.
     */
    public void write(File file) throws IOException
    {
        Gson gson = new Gson();
        //IF the file exists
        if (file != null)
        {
            
            BufferedWriter bwriter = new BufferedWriter(new FileWriter(file));
            //IF the user has taken classes
            if (taken != null)
            {
                //FOR each class the user has taken
                for (Course takenCourse : taken)
                {
                    //WRITE to the file
                    JsonObject json = new JsonObject();
                    json.addProperty("major", takenCourse.getMajor().get(0));
                    json.addProperty("number", takenCourse.getNumber());
                    bwriter.write(gson.toJson(json) + "\n");
                }
            }

            bwriter.close();
        }
    }
}
