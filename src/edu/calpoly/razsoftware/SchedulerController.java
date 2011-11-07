/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.calpoly.razsoftware;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Set;
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
   private CourseList        coursesTaken;
   private CourseList        coursesRequired;
   private CourseList        schedule;
   private Set<CourseOption> unfulfilledOptions;
   private SchedulerView     gui;
   private Flowchart         chart;
   private CourseList        catalog;
   private CourseDecider     decider;

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
   public SchedulerController(CourseList coursesTaken,
         CourseList coursesRequired, CourseList schedule, SchedulerView gui)
   {
      this.coursesRequired = coursesRequired;

      decider = new CourseDecider();

      this.coursesTaken = coursesTaken;
      this.schedule = schedule;
      this.gui = gui;
      catalog = new CourseList(getClass().getResourceAsStream("Cat.json"));
      

      
      chart =
            FlowchartReader.readFlowchart(
                  getClass().getResourceAsStream("FlowChart.json"), catalog);
      unfulfilledOptions = decider.decideClasses(coursesTaken, chart);
      this.coursesRequired.clear();
      this.coursesRequired.addAll(decider.getRequiredCourses(unfulfilledOptions));


   }

   /**
    * Action Preformed method from ActionListener
    * 
    * @param e The event to analyze
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
   void CheckBoxClicked()
   {
      for (Course c : coursesTaken.getCourses())
      {
         System.out.println(c);
      }
      Course clickedCourse = gui.getSelectedPassed();
      if (coursesTaken.contains(clickedCourse))
      {
         coursesTaken.remove(clickedCourse);
      }
      else
      {
         coursesTaken.add(clickedCourse);
      }
      unfulfilledOptions = decider.decideClasses(coursesTaken, chart);
      coursesRequired.clear();
      coursesRequired.addAll(decider.getRequiredCourses(unfulfilledOptions));

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
      System.out.println("AutoFill");
      if (unfulfilledOptions == null || unfulfilledOptions.isEmpty())
      {
         return;
      }

      int maxUnits = gui.getScheduleUnits();
      //
      int unitCount = 0;

      for (Course c : schedule.getCourses())
      {
         unitCount+=c.getUnits();
      }
         

         for (CourseOption co : unfulfilledOptions)
         {
            System.out.println(co.getQuarter()+" "+co.getRequirement());
            if (unitCount > maxUnits)
            {
               break;
            }

            for (Course c : co.getOptions())
            {
               System.out.println("\t"+c);
               if (c.preRecsMet(coursesTaken.getCourses()) && !coursesTaken.contains(c)
                     && unitCount + c.getUnits() <= maxUnits)
               {
                  System.out.println("\t\t"+c);
                  if (!coursesTaken.contains(c)&&!schedule.contains(c))
                  {
                     System.out.println("\t\t\tAdd: "+c);
                     schedule.add(c);
                     unitCount += c.getUnits();
                     break;
                  }
               }

            }
         }
   }

   /**
    * Accessor to the catalog of courses
    * @return the catalog of courses that was loaded
    */
   public CourseList getCatalog()
   {
      return catalog;
   }

}