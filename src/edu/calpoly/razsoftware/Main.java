/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.calpoly.razsoftware;

import javax.swing.UIManager;

/**
 * The main methos the initilaizes different componets and links them together
 * @author djohns34
 * @version $Revision$
 */
public class Main
{

    /**
     * Starts the running of the program
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
