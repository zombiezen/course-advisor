package edu.calpoly.razsoftware;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.google.common.collect.ImmutableList;

/**
 * An table model that accesses a backend model to display the values in the cells
 * It also delegates the changing of the data to a seperate controller class
 * @author Daniel
 *
 */
class CourseTableModel extends AbstractTableModel
{
   private List<Course> sortedCatalog;
   private CourseList   state;
   SchedulerController  controller;

   public CourseTableModel(CourseList state, SchedulerController controller,CourseList catalog)
   {
      this.controller = controller;
      sortedCatalog =
            ImmutableList.copyOf(catalog.getCourses());
      this.state = state;
   }

   @Override
   public int getRowCount()
   {
      return sortedCatalog.size();
   }

   @Override
   public int getColumnCount()
   {
      return 2;
   }

   @Override
   public String getColumnName(int columnIndex)
   {
      switch (columnIndex)
      {
         case 0:
            return "Passed";
         case 1:
            return "Course";
         default:
            return null;
      }
   }

   @Override
   public Class<?> getColumnClass(int columnIndex)
   {
      switch (columnIndex)
      {
         case 0:
            return Boolean.class;
         case 1:
            return Course.class;
         default:
            return null;
      }
   }

   @Override
   public boolean isCellEditable(int rowIndex, int columnIndex)
   {
      switch (columnIndex)
      {
         case 0:
            return true;
         case 1:
            return false;
         default:
            return false;
      }
   }

   @Override
   public Object getValueAt(int rowIndex, int columnIndex)
   {
      if (rowIndex > sortedCatalog.size())
         return null;

      final Course requestedCourse = (Course) sortedCatalog.get(rowIndex);

      switch (columnIndex)
      {
         case 0:
            return state.contains(requestedCourse);
         case 1:
            return requestedCourse;
         default:
            return null;
      }
   }

   @Override
   public void setValueAt(Object aValue, int rowIndex, int columnIndex)
   {
      if (rowIndex > sortedCatalog.size())
         return;

      switch (columnIndex)
      {
         case 0:
            controller.CheckBoxClicked();
            break;
      }
   }
}