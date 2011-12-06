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

import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author light
 */
public class CourseListTest
{
    @Test
    public void dependenciesShouldReferToSameObjects() throws IOException
    {
        final CourseList list =
                new CourseList(CourseList.class.getResourceAsStream("Cat.json"));
        final Course cpe102 = list.lookUp("CPE", 102);
        final Course cpe103 = list.lookUp("CPE", 103);
        final Course csc141 = list.lookUp("CSC", 141);

        assertEquals(1, cpe103.getPreRequisites().size());
        assertEquals(2, cpe103.getPreRequisites().iterator().next().size());

        for (Course c : cpe103.getPreRequisites().iterator().next())
        {
            if (c.getMajor().get(0).equals("CPE") && c.getNumber() == 102)
            {
                assertSame(cpe102, c);
            }
            else if (c.getMajor().get(0).equals("CSC") && c.getNumber() == 141)
            {
                assertSame(csc141, c);
            }
        }

        assertEquals(ImmutableSet.of(ImmutableSet.of(cpe102, csc141)),
                     cpe103.getPreRequisites());
    }
}
