/*
 * Copyright 2011 RAZ Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.calpoly.razsoftware;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;

import com.google.gson.Gson;

/**
 * Converts the file of a flowchart into a Flowchart object to be
 *         used by the application
 * @author aspurgin
 * @version $Revision$
 * 
 */
public class FlowchartReader
{
    private static Gson gson = new Gson();

    /**
     * provides a flowchart based off a inputstream of a JSON file containing
     * newline-delineated "CourseOption" classes
     * 
     * @param iStream
     *            the stream of the JSON file
     * @param courses
     *            the catalog of courses
     * @return a flowchart based off the file
     */
    public static Degree readFlowchart(InputStream iStream,
            CourseList courses)
    {
        // INITIALIZE the set of course options to the empty set
        HashSet<CourseOption> options = new HashSet<CourseOption>();
        // CREATE new scanner
        Scanner flowScanner = new Scanner(iStream);

        // WHILE the input has another line DO
        while (flowScanner.hasNextLine())
        {
            // INITIALIZE tmpCourseList to the empty set
            HashSet<Course> tmpCourseList = new HashSet<Course>();
            // PARSE a course option and store it as co
            CourseOption co =
                    gson.fromJson(flowScanner.nextLine(), CourseOption.class);
            // FOR each course in co
            for (Course fulfilCourse : co.getFulfillmentOptions())
            {
                // FIND the course with the major and number parsed
                Course course =
                        courses.lookUp(fulfilCourse.getMajor().get(0),
                                fulfilCourse.getNumber());
                // IF there is a course with the major and number parsed THEN
                if (course != null)
                {
                    // ADD the course to tmpCourseList
                    tmpCourseList.add(course);
                }
                else
                {
                    // LOG the failure to find the course
                    System.out.println("could not find course "
                            + fulfilCourse.getMajor().get(0) + " "
                            + fulfilCourse.getNumber());
                } // ENDIF
            } // ENDFOR

            // CREATE a course option that uses the referenced objects
            co =
                    new CourseOption(co.getRequirement(), tmpCourseList,
                            co.isMutuallyExclusive(), co.getQuarter());
            System.out.println("--");
            // ADD co to the course options set
            options.add(co);
        }
        // CREATE a new flowchart from the course options set
        return new Degree(options);
    }
}
