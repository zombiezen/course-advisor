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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CourseOptionDeciderTest
{
    private CourseOptionDecider decider;
    private Course cpe101, cpe102, cpe103;

    @Before
    public void createDecider()
    {
        decider = new CourseOptionDecider();
    }

    @Before
    public void createSampleCourses()
    {
        cpe101 = new Course(ImmutableList.of("CPE", "CSC"), 101, 4,
                            "Fund of CS 1", "The first class in CPE");
        cpe102 = new Course(ImmutableList.of("CPE", "CSC"), 102, 4,
                            "Fund of CS 2", "The second class in CPE");
        cpe103 = new Course(ImmutableList.of("CPE", "CSC"), 103, 4,
                            "Fund of CS 3", "The third class in CPE");
        cpe102.getPreRequisites().add(ImmutableSet.<Course>of(cpe101));
        cpe103.getPreRequisites().add(ImmutableSet.<Course>of(cpe102));
    }

    @Test
    public void emptyCase()
    {
        CourseList state = new CourseList();
        Degree flowchart = new Degree();

        assertEquals(ImmutableSet.<CourseOption>of(),
                     decider.decideClasses(state, flowchart));
    }

    @Test
    public void shouldReportEmptyForComplete()
    {
        Degree flowchart = new Degree();
        CourseOption option101 = new CourseOption(cpe101, 1);
        flowchart.addOption(option101);
        CourseList state = new CourseList(ImmutableSet.of(cpe101));

        assertEquals(ImmutableSet.<CourseOption>of(),
                     decider.decideClasses(state, flowchart));
    }

    @Test
    public void shouldReportAbleToTake()
    {
        Degree flowchart = new Degree();
        CourseOption option101 = new CourseOption(cpe101, 1);
        CourseOption option102 = new CourseOption(cpe102, 2);
        flowchart.addOption(option101);
        flowchart.addOption(option102);
        CourseList state = new CourseList(ImmutableSet.of(cpe101));

        assertEquals(ImmutableSet.<CourseOption>of(option102),
                     decider.decideClasses(state, flowchart));
    }

    @Test
    public void shouldReportUnableToTake()
    {
        Degree flowchart = new Degree();
        CourseOption option101 = new CourseOption(cpe101, 1);
        CourseOption option102 = new CourseOption(cpe102, 2);
        CourseOption option103 = new CourseOption(cpe103, 3);
        flowchart.addOption(option101);
        flowchart.addOption(option102);
        flowchart.addOption(option103);
        CourseList state = new CourseList(ImmutableSet.of(cpe101));

        assertEquals(ImmutableSet.<CourseOption>of(option102, option103),
                     decider.decideClasses(state, flowchart));
    }

    @Test
    public void shouldHandleSuperfluousCourse()
    {
        Degree flowchart = new Degree();
        CourseOption option = new CourseOption("SCIENCE!",
                                               ImmutableSet.of(cpe101, cpe102),
                                               true, 1);
        flowchart.addOption(option);
        CourseList state = new CourseList(ImmutableSet.of(cpe101, cpe102));

        assertEquals(ImmutableSet.<CourseOption>of(),
                     decider.decideClasses(state, flowchart));
    }
}
