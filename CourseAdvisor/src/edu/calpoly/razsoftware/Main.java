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

import javax.swing.UIManager;

/**
 * The main method initializes the Model, {@link SchedulerView}, and
 * {@link SchedulerController}. It then links the appropriate pieces of the
 * model to the different components in the view and controller.
 * 
 * @author djohns34
 * @version $Revision$
 */
public class Main
{

    /**
     * Starts the running of the program
     * 
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(SchedulerView.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(SchedulerView.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(SchedulerView.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(SchedulerView.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }

        // CREATE an empty list of courses taken
        CourseList coursesTaken = new CourseList();

        // CREATE an empty list of courses required
        CourseList coursesRequired = new CourseList();

        // CREATE an empty list of courses in the suggested schedule
        CourseList suggestedSchedule = new CourseList();

        // CREATE a scheduler view
        SchedulerView view = new SchedulerView();

        // CREATE a controller that is connected to the model and the view
        SchedulerController controller =
                new SchedulerController(coursesTaken, coursesRequired,
                        suggestedSchedule, view);

        // CREATE a table model for the list of courses taken
        CourseTableModel tableModel =
                new CourseTableModel(coursesTaken, controller,
                        controller.getCatalog());

        // SET the view's data sources to the lists
        view.setSources(tableModel, coursesRequired, suggestedSchedule);

        // SET the view's controller to the created controller
        view.setController(controller);

        // DISPLAY the view
        view.setVisible(true);

    }
}
