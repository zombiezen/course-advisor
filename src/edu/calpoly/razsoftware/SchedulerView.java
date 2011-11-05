/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.calpoly.razsoftware;

/**
 *
 * @author michaelsvanbeek
 */
public class ScheulerView extends JFrame
{
   // OLD GUI CODE
   
   // ...
   
   /**
    * Returns the Course that is selected in the "Passed" pane
    * @return Course that is selected in the "Passed" pane
    */
   public Course getSelectedPassed() {}
   
   /**
    * Returns the Course that is selected in the "Required" pane
    * @return Course that is selected in the "Required" pane
    */
   public Course getSelectedRequired() {}
   
   /**
    * Returns the Course that is selected in the "Schedule" pane
    * @return Course that is selected in the "Schedule" pane
    */
   public Course getSelectedSchedule() {}
      
   /**
    * Returns the number of units in the units to take box
    * @return int the number of units in the units to take box
    */   
   public int getScheduleUnits() {}
   
   /**
    * Fills the "Info" section with the info of the parameter Course
    * @param Course course to display
    */
   public void setInfo(Course c) {}
}