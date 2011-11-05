/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.calpoly.razsoftware;

/**
 *
 * @author michaelsvanbeek
 */
public class SchedulerController extends ActionListener
{
   private CourseList coursesTaken;
   private CourseList schedule;
   private SchedulerView gui;
   
   /**
    * Constructor
    * @param CourseList coursesTaken a CourseList of Courses that have been taken
    * @param CourseList scheduler a CourseList of Courses that are in your schedule
    * @param SchedulerView gui a SchedulerView to pull selections from
    */
   public SchedulerController(CourseList coursesTaken, CourseList schedule, SchedulerView gui)
   {
      this.coursesTaken = coursesTaken;
      this.schedule = schedule;
      this.gui = gui;
   }
   
   /**
    * Action Preformed method from ActionListener
    * @param
    */
   public actionPreformed(ActionEvent e)
   {
      //determine which of the below methods to use
   }
   
   /**
    * Handles a check box being clicked in the GUI.  Adds or removes 
    * the checked Course as applicable.
    */
   private void CheckBoxClicked()
   {
      clickedCourse = gui.getPassedClicked()
      
      if(courseTaken.contains(clickedCourse))
         coursesTaken.remove(clickedCourse);
      else
         coursesTaken.add(clickedCourse);

      gui.repaint();
   }
   
   /**
    * Handles a row being selected in the GUI.  Determines what row
    * was selected and displays that course's info in the GUI info pane
    */
   private void rowSelected(ActionEvent e)
   {
      if(e is from Passed)
      {
         gui.getInfo.set(gui.passed.getSelected());
      }
      else if (e is from Required)
      {
         gui.getInfo.set(gui.required.getSelected());
      }
      else if (e is from Schedule)
      {
         gui.getInfo.set(gui.schedule.getSelected());
      }

      gui.repaint()
   }
   
   /**
    * Handles the ">" button being clicked.  Adds the selected field from
    * required to the Schedule.
    */
   private void addToSchedule()
   {
      schedule.add(gui.required.getSelected());
   }
   
   /**
    * Handles the "<" button being clicked.  Removes the selected field from
    * schedule.
    */
   private void removeFromSchedule()
   {
      schedule.remove(gui.schedule.getSelected())
   }
   
   /**
    * Handles the "Clear" button being clicked.  Removes all from the 
    * Schedule
    */
   private void clearSchedule()
   {
      schedule.removeAll();
   }
   
   /**
    * Handles the "AutoFill" button being pressed.  Uses our autofill 
    * algorithm to fill it.
    */
   private void autoFillSechdule()
   {
      int unitsToFill = gui.getUnitsToTake() - schedule.getUnits();
      
      //auto-fill logic
   }
}