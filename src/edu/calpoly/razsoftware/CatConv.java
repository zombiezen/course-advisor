/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.calpoly.razsoftware;

/**
 *
 * @author aspurgin
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import java.io.InputStream;
import java.util.List;

/**
 * this class is a utility used for converting plain-text catalog entries into 
 * usable json objects
 * @author adam
 */
public class CatConv
{
    private static final int kMAJORINDEX = 3; 

    /**
     * this method does the work, set it off and watch it go.
     * @param args arguments passed to the program
     * @throws IOException if file could not be found
     */
    public static void main(String[] args) throws IOException
    {
        // INITIALIZE file to resource destination
        File file = new File("/Users/michaelsvanbeek/Downloads/cat.txt");
        // INITIALIZE gson object
        Gson gson = new Gson();
        // INITIALIZE scanner object
        Scanner scanner = new Scanner(file);
        // INITIALIZE scanner object
        ArrayList<CatConvCourse> catConvCourses =
                new ArrayList<CatConvCourse>();
        parseTextToCourse(scanner, catConvCourses);
        // INITIALIZE jsonout
        File jsonout = new File("NewCat.json");
        // INITIALISE a filewriter to jsonout
        FileWriter fwjson = new FileWriter(jsonout);
        // FOR each course in the list
        for (CatConvCourse currentCourse : catConvCourses)
        {
            // IF the current course description starts with "(Also listed as"
            // THEN
            if (currentCourse.getDescription().startsWith("(Also listed as"))
            {
                // split the string after the closing parenthases
                String[] spl = currentCourse.getDescription().split("\\)", 2);
                currentCourse.setDescription(spl[1]);
                // set the description to the last half of the split
                String[] spl2 = spl[0].split(" ");
                // add the alternative major listing tot eh course
                currentCourse.getMajor().add(spl2[kMAJORINDEX]);
            }
            cleanLeftoverReqStructs(currentCourse);
            // ENDIF
            // IF
            if (currentCourse.getDescription().contains("prerequisite:"))
            {
                String[] tokens =
                        currentCourse.getDescription().split("prerequisite:");
                currentCourse.setDescription(tokens[0]);
                // IF the second half contains "Corequisite" THEN
                if (tokens[1].contains("corequisite"))
                {
                    String[] spl2 = tokens[1].split("corequisite:");
                    currentCourse.setPreRequisites(parseReq(spl2[0]));
                    currentCourse.setCoRequisites(parseReq(spl2[1]));
                }
                else
                {
                    currentCourse.setPreRequisites(parseReq(tokens[1]));
                }
            }
            // export the course to the file
            fwjson.append(gson.toJson(currentCourse) + "\n");
        }
        // close the writer
        fwjson.close();

    }

    private static void cleanLeftoverReqStructs(CatConvCourse currentCourse)
    {
        // ENDIF
        // IF the description contains the word "Prerequisite:" THEN
        if (currentCourse.getDescription().contains("Prerequisite:"))
        {
            // split the description at "Prerequisite:"
            String[] tokens =
                    currentCourse.getDescription().split("Prerequisite:");
            // set the description to the first half
            currentCourse.setDescription(tokens[0]);
            // IF the second half contains "Corequisite" THEN
            if (tokens[1].contains("Corequisite"))
            {
                // split the description at "Corequisite"
                String[] tokenTokens = tokens[1].split("Corequisite:");
                // parse first half of second part as prerequisites
                currentCourse.setPreRequisites(parseReq(tokenTokens[0]));
                // parse second half as of second part corequisites
                currentCourse.setCoRequisites(parseReq(tokenTokens[1]));
            }
            // ELSE
            else
            {
                // parse second half as prerequisites
                currentCourse.setPreRequisites(parseReq(tokens[1]));
            }
        }
    }

    private static void parseTextToCourse(
            Scanner scanner,
            ArrayList<CatConvCourse> catConvCourses) 
    {
        // WHILE there are still lines left to be scanned
        while (scanner.hasNextLine())
        {
            // SET currentline to the next line
            String currentLine = scanner.nextLine();
            // IF the current line matches the pattern signifying a new entry
            // THEN
            if (currentLine.matches("[A-Za-z]{3,4} [0-9]{3} [^(]* .*[(].*"))
            {
                // INITIALIZE a new course
                CatConvCourse newCatConvCourse = new CatConvCourse();
                // SET index to 2
                int tokenIndex = 2;
                // SET spl to be an array of tokens provided by splitting the
                // current
                // line at any space character
                String[] spl = currentLine.split(" ");
                // SET the major of the course to the first item in the list
                newCatConvCourse.getMajor().add(spl[0]);
                // SET the number of the course to the second item in the list
                newCatConvCourse.setNumber((int) Integer.valueOf(spl[1]));
                try
                {
                    // FOR each token that does not begin with "("
                    for (; !spl[tokenIndex].startsWith("("); tokenIndex++)
                    {
                        // add token to the name of the course
                        newCatConvCourse.setName(newCatConvCourse.getName() 
                                + (spl[tokenIndex] + " "));
                    }
                    // SET the units of the course to the value in the
                    // parenthases
                    newCatConvCourse.setUnits((int) Integer.valueOf(spl[
                            tokenIndex].charAt(1) - '0'));
                }
                catch (Exception anyException)
                {
                    System.out.println(currentLine);
                }
                // IF the current line indicates the class has more listings
                // THEN
                if (currentLine.contains("(Also listed as"))
                {
                    // add the alternate listings to the major list
                    String[] altListing =
                            currentLine.split("\\(Also listed as", 2);
                    newCatConvCourse.getMajor().add(altListing[1].split(" ")[1]);
                }
                // add the current course to the list
                catConvCourses.add(newCatConvCourse);
            }
            // ELSE IF the list of courses is not empty
            else if (catConvCourses.size() > 0)
            {
                // set current course to the course on top of the list
                CatConvCourse course =
                        catConvCourses.remove(catConvCourses.size() - 1);
                // append description to the course
                course.setDescription(course.getDescription() + currentLine);
                // put it back
                catConvCourses.add(course);
            }
        }
    }
    
    /**
     * this parses the pre/coreq strings
     * @param in the pre/coreq string
     * @return a set of properly constructed requisites
     */
    public static Set<Set<CatConvCourse>> parseReq(String in)
    {
        // initialize the pattern to check
        Pattern catConvCoursePattern = Pattern.compile("[A-Z]{3,4} [0-9]{3}");
        // INITIALIZE the hashset to rebuild
        HashSet<Set<CatConvCourse>> req = new HashSet<Set<CatConvCourse>>();
        // split the plain english statement up by common deliniators
        String[] stage1 = in.split("and|,|;");

        // FOR every string that was just generated
        for (String str : stage1)
        {
            // INITIALIZE a new set of courses
            HashSet<CatConvCourse> out = new HashSet<CatConvCourse>();


            Matcher regexMatch = catConvCoursePattern.matcher(str);
            // WHILE there is a pattern that matches a class, create a new class and
            // add it to the list
            while (regexMatch.find())
            {
                out.add(new CatConvCourse(regexMatch.group().split(" ")[0], Integer
                        .valueOf(regexMatch.group().split(" ")[1])));
            }
            // ENDFOR

            // IF a class was added
            if (out.size() != 0)
            {
                // Add the requisite to the hashset
                req.add(out);
            }
        }
        // ENDFOR
        // IF the requisite list is empty THEN
        if (req.size() == 0)
        {
            return null;
        }
        // ELSE
        else
        {
            //INITIALIZE a set of set of courses
            HashSet<Set<CatConvCourse>> permutations = 
                    new HashSet<Set<CatConvCourse>>();
            permutations = refacttest(req, permutations);
            req = permutations;
        }
        return req;
    }

    private static HashSet<Set<CatConvCourse>> 
    refacttest(HashSet<Set<CatConvCourse>> req, 
                       HashSet<Set<CatConvCourse>> permutations)
    {
        //FOR every set in the requirements structure
        for(Set<CatConvCourse> subSet : req)
        {
            //IF the set is empty
            if(permutations.isEmpty())
            {
                //FOR every course in the set
                for(CatConvCourse course : subSet)
                {
                    //INITIALIZE a new set of courses
                    HashSet<CatConvCourse> newitem = 
                            new HashSet<CatConvCourse>();
                    //add the course to that set
                    newitem.add(course);
                    //add this set to the set of sets
                    permutations.add(newitem);
                }
                //ENDFOR
            }
            //ELSE IF the set is not empty
            else
            {
                //INITIALIZE a new set of sets of courses
                HashSet<Set<CatConvCourse>> newPerm = 
                        new HashSet<Set<CatConvCourse>>();
                //FOR each set in the permutations
                for(Set<CatConvCourse> s2 : permutations)
                {
                    //FOR each course in the original set
                    for(CatConvCourse course : subSet)
                    {
                        //create a new set duplicating the set in the 
                        //original set
                        HashSet<CatConvCourse> dupeset = 
                                new HashSet<CatConvCourse>(s2);
                        //add the current course to the set
                        dupeset.add(course);
                        //add the set to the original set
                        newPerm.add(dupeset);
                    }
                    //ENDFOR
                }
                //ENDFOR
                permutations = new HashSet<Set<CatConvCourse>>(newPerm);

            }

        }
        return permutations;
    }

}
