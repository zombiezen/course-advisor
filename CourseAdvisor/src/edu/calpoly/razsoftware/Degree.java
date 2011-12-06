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

import java.util.HashSet;
import java.util.Set;

/**
 * The Degree class models the data provided by a department course flowchart.
 * It contains a set of course options.
 * <p>
 * Operations:
 * <ul>
 * <li>Add course options
 * </ul>
 * 
 * @author rlight
 * @version $Revision$
 */
public class Degree
{
    /** The set of course options required to complete the degree. */
    private Set<CourseOption> sectionReqs;

    /**
     * Used to Construct a Flowchart that represents the classes a user can take
     */
    public Degree()
    {
        sectionReqs = new HashSet<CourseOption>();
    }

    /**
     * Constructor for flowchart with section requirements
     * @param sectionReqs requirements for course option
     */
    public Degree(Set<CourseOption> sectionReqs)
    {
       this();
        this.sectionReqs.addAll(sectionReqs);
    }

    /**
     * Returns Section Requirements
     * @return Course Options for a given course option
     */
    public Set<CourseOption> getSectionReqs()
    {
        return sectionReqs;
    }

    /**
     * Adds a course Option to the requirements of a section
     * @param newOption the option to add to this flowchart
     */
    public void addOption(CourseOption newOption)
    {
        sectionReqs.add(newOption);
    }
}
