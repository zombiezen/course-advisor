package edu.calpoly.razsoftware;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.google.common.collect.ImmutableList;

/**
 * An table model that accesses a backend model to display the values in the
 * cells It also delegates the changing of the data to a seperate controller
 * class
 * 
 * @author Daniel
 * 
 */
class CourseTableModel extends AbstractTableModel
{
    private List<Course> sortedCatalog;
    private CourseList   state;
    private SchedulerController  controller;

    public CourseTableModel(CourseList state, SchedulerController controller,
            CourseList catalog)
    {
        this.controller = controller;
        sortedCatalog = ImmutableList.copyOf(catalog.getCourses());
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
        //CASE column number
        switch (columnIndex)
        {
            //the title is Passed
            case 0:
                return "Passed";
                //the title is Course
            case 1:
                return "Course";
            default:
                return null;
        }//ENDCASE
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        //CASE column number
        switch (columnIndex)
        {
            //the type is boolean
            case 0:
                return Boolean.class;
            case 1:
                //the type is Course
                return Course.class;
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        //CASE column number
        switch (columnIndex)
        {
            //Booleans are editable
            case 0:
                return true;
                //Courses are not editable
            case 1:
                return false;
            default:
                return false;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        //IF trying to get something past the end of the table
        if (rowIndex > sortedCatalog.size())
        {
            return null;
        }//ENDIF

        final Course requestedCourse = (Course) sortedCatalog.get(rowIndex);

        //CASE column number
        switch (columnIndex)
        {
            //return true if the user has taken the course
            case 0:
                return state.contains(requestedCourse);
                //return the name of the course
            case 1:
                return requestedCourse;
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        //IF trying to set something past the end of the table
        if (rowIndex > sortedCatalog.size())
        {
            return;
        }

        //CASE column number
        switch (columnIndex)
        {
            //mark the course as clicked
            case 0:
                controller.checkBoxClicked();
                break;
            default:
                //do nothing
        }
    }
}