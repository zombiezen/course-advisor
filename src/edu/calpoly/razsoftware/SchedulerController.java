/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.calpoly.razsoftware;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * 
 * @author michaelsvanbeek
 */
public class SchedulerController implements ActionListener,
      ListSelectionListener
{
   private CourseList    coursesTaken;
   private CourseList    schedule;
   private SchedulerView gui;

   /**
    * Constructor
    * 
    * @param coursesTaken
    *           a CourseList of Courses that have been taken
    * @param schedule
    *           a CourseList of Courses that are in your schedule
    * @param gui
    *           a SchedulerView to pull selections from
    */
   public SchedulerController(CourseList coursesTaken, CourseList schedule,
         SchedulerView gui)
   {
      this.coursesTaken = coursesTaken;
      this.schedule = schedule;
      this.gui = gui;
   }

   /**
    * Action Preformed method from ActionListener
    * 
    * @param
    */
   public void actionPerformed(ActionEvent e)
   {
      if (e.getActionCommand().equals("Add Course"))
      {
         addToSchedule();
      }
      else if (e.getActionCommand().equals("Remove Course"))
      {
         removeFromSchedule();

      }
      else if (e.getActionCommand().equals("Clear Suggested"))
      {
         clearSchedule();
      }
      else if (e.getActionCommand().equals("Autofill"))
      {
         autoFillSechdule();
      }
   }

   /**
    * Handles a check box being clicked in the GUI. Adds or removes the checked
    * Course as applicable.
    */
   private void CheckBoxClicked()
   {
      Course clickedCourse = gui.getSelectedPassed();

      if (coursesTaken.contains(clickedCourse))
         coursesTaken.remove(clickedCourse);
      else
         coursesTaken.add(clickedCourse);

      gui.repaint();
   }

   /**
    * Handles a row being selected in the GUI. Determines what row was selected
    * and displays that course's info in the GUI info pane
    */
   @Override
   public void valueChanged(ListSelectionEvent e)
   {

      if (e.getSource() instanceof JList)
      {
         JList selectedList = (JList) e.getSource();
         if (selectedList.getSelectedValue() instanceof Course)
         {
            Course selected = (Course) selectedList.getSelectedValue();
            CourseOption option = null;
            // for (CourseOption o : courseOptions)
            // {
            // if (o.getOptions().contains(selected))
            // {
            // option = o;
            // }
            // }
            // updateInfo(selected, option);
            gui.setInfo(selected);
         }
      }

      gui.repaint();
   }

   /**
    * Handles the ">" button being clicked. Adds the selected field from
    * required to the Schedule.
    */
   private void addToSchedule()
   {
      schedule.add(gui.getSelectedRequired());
   }

/**
    * Handles the "<" button being clicked.  Removes the selected field from
    * schedule.
    */
   private void removeFromSchedule()
   {
      schedule.remove(gui.getSelectedSchedule());
   }

   /**
    * Handles the "Clear" button being clicked. Removes all from the Schedule
    */
   private void clearSchedule()
   {
      schedule.clear();
   }

   /**
    * Handles the "AutoFill" button being pressed. Uses our autofill algorithm
    * to fill it.
    */
   private void autoFillSechdule()
   {
      // int unitsToFill = gui.getScheduleUnits() - schedule.getUnits();

      // auto-fill logic
   }

}