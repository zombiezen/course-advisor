package edu.calpoly.razsoftware;

import com.google.gson.Gson;
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
 * @author dpanger
 */
public class UserState
{
    private Set<Course> taken;

    /**
     * Creates a new, empty UserState object.
     */
    public UserState()
    {
        taken = new HashSet();
    }

    /**
     * Creates a new UserState object from a given Set of Courses the user has
     * completed.
     * @param taken the Set of Courses the user has completed.
     */
    public UserState(Set<Course> taken)
    {
        this.taken = new HashSet<Course>(taken);
    }

    /**
     * Creates a new UserState object from a given JSon file.
     * @param file The JSon file to be read from
     * @throws IOException If the file is not found or can not be opened, an
     * IOException is thrown
     */
    public UserState(File file) throws IOException
    {
        Gson gson = new Gson();
        Scanner s = new Scanner(file);
        taken = new HashSet<Course>();
        
        while ( s.hasNextLine() )
            taken.add(gson.fromJson(s.nextLine(), Course.class));
        // else we have an empty JSon file
        
        s.close();
    }

    /**
     * Returns a Set of Courses that the user has completed.
     * @return a Set of Courses that the user has completed or null if the
     * user's state has not yet been set. 
     */
    public Set<Course> getTaken()
    {
        return taken;
    }

    /**
     * Writes the user's current "state", which is defined as the courses that
     * he or she has completed, to the file that is passed in.
     * @param file The file to write the current user's state to.
     * @throws IOException IOException is thrown if the file that is passed in 
     * is not found.
     */
    public void write(File file) throws IOException
    {
        Gson gson = new Gson();
        if ( file != null ) {
            BufferedWriter bwriter = new BufferedWriter(new FileWriter(file));
        
            if ( taken != null ) {
                for ( Course c : taken )
                    bwriter.write(gson.toJson(c) + "\n");
            }
        
            bwriter.close();
        }
    }
}
