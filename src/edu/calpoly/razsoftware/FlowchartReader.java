package edu.calpoly.razsoftware;

import java.io.File;
import com.google.gson.*;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FlowchartReader
{
    /**
     * provides a flowchart based off a JSON file containing newline-delineated "CourseOption" classes
     * @param path location of the JSON file
     * @return a flowchart based off the file
     */
    public static Flowchart readFlowchart(File path)
    {
        Scanner s;
        Gson gson = new Gson();
        Flowchart fc = new Flowchart();
        try
        {
            s = new Scanner(path);
            while(s.hasNext())
            {
                fc.addOption(gson.fromJson(s.nextLine(), CourseOption.class));
            }
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        
        return fc;
    }
}
