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

import java.util.Set;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import static org.junit.Assert.*;

public class CourseOptionTest
{
    
    @Test
    public void constructorTest()
    {
        Course c1,c2,c3;
        c1 = new Course(ImmutableList.of("CSC","CPE"),101,4,"Fund of CS 1", "Learn C");
        c2 = new Course(ImmutableList.of("CSC","CPE"),102,4,"Fund of CS 2", "Learn Java");
        c3 = new Course(ImmutableList.of("CSC","CPE"),103,4,"Fund of CS 3", "Do Java Faster");
        
        CourseOption co = new CourseOption("Take a CS class",ImmutableSet.of(c1,c2,c3),false, 1);
        
        assertEquals("CSC101 or CSC102 or CSC103",co.toString());
    }
    
    @Test
    public void quarterTest()
    {
        Course c1,c2,c3;
        c1 = new Course(ImmutableList.of("CSC","CPE"),101,4,"Fund of CS 1", "Learn C");
        c2 = new Course(ImmutableList.of("CSC","CPE"),102,4,"Fund of CS 2", "Learn Java");
        c3 = new Course(ImmutableList.of("CSC","CPE"),103,4,"Fund of CS 3", "Do Java Faster");
        
        CourseOption co = new CourseOption("Take a CS class",ImmutableSet.of(c1,c2,c3),false, 1);
        
        assertEquals(co.getQuarter(),1);
    }
}
