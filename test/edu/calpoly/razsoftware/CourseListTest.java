/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
