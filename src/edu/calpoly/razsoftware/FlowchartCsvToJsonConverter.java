/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.calpoly.razsoftware;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

/**
 *
 * @author adam
 */
public class FlowchartCsvToJsonConverter
{
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        Gson gson = new Gson();
        File f = new File("FlowChart.csv");
        File fo = new File("FlowChart.json");
        Scanner s = new Scanner(f);
        FileWriter fw = new FileWriter(fo);
        
        while(s.hasNextLine())
        {
            String[] tokens = s.nextLine().split(",");
            String req = tokens[0];
            int quarter = Integer.valueOf(tokens[1]);
            boolean mutex = (tokens[2].contains("TR")) ? true:false;
            HashSet<Course> options = new HashSet<Course>();
            for(int i=3; i<tokens.length; i++)
            {
                ArrayList<String> tmplst = new ArrayList<String>();
                tmplst.add(tokens[i].split(" ")[0]);
                Course c = new Course(tmplst, Integer.valueOf(tokens[i].split(" ")[1]), 0, "NA", "NA");
                options.add(c);
            }
            CourseOption co = new CourseOption(req, options, mutex, quarter);
            fw.append(gson.toJson(co) + "\n");
        }
        fw.close();
        s.close();
    }
}
