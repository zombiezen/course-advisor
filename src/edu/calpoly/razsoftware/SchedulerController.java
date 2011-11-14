/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.calpoly.razsoftware;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.jws.Oneway;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 
 * @author michaelsvanbeek, Daniel Johnson
 */
public class SchedulerController extends KeyAdapter implements ActionListener,
        ListSelectionListener
{

    private static final String kFileExtension   = "us";
    private static final String kApplicationName = "Course Advisor";
    public static final String  ALL_FILTER       = "All";
    public static final String  PREREQ_MET       = "Prerequisites Met";
    public static final String  PREREQ__NOT_MET  = "Prerequisite Not Met";

    private CourseList          coursesTaken;
    private CourseList          coursesRequired;
    private CourseList          schedule;
    private Set<CourseOption>   unfulfilledOptions;
    private SchedulerView       gui;
    private Flowchart           chart;
    private CourseList          catalog;
    private CourseDecider       decider;

    private boolean             saved            = false;
    private File                savedFile;

    /**
     * Constructor
     * 
     * @param coursesTaken
     *            a CourseList of Courses that have been taken
     * @param schedule
     *            a CourseList of Courses that are in your schedule
     * @param gui
     *            a SchedulerView to pull selections from
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
                        getClass().getResourceAsStream("FlowChart.json"),
                        catalog);
        unfulfilledOptions = decider.decideClasses(coursesTaken, chart);
        this.coursesRequired.clear();
        this.coursesRequired.addAll(decider
                .getRequiredCourses(unfulfilledOptions));

    }

    /**
     * Action Performed method from ActionListener
     * 
     * @param e
     *            The event to analyze
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

        // the menu actions
        else if (e.getActionCommand().equals("Open"))
        {
            loadCoursesTaken();
        }
        else if (e.getActionCommand().equals("Save"))
        {
            saveUserState();
        }
        else if (e.getActionCommand().equals("Save As"))
        {
            saveAsUserState();
        }
        else if (e.getActionCommand().equals("Quit"))
        {
            quit();
        }
        else if (e.getActionCommand().equals("Required Combo"))
        {
            filterRequired();
        }
    }

    /**
     * Key released from KeyAdapter, responds when the user types something in
     * the required list filer
     */
    @Override
    public void keyReleased(KeyEvent keyevent)
    {
        filterRequired();

    }

    /**
     * terminates the application first asking the user if the want to save
     * their state
     */
    private void quit()
    {
        if (!saved)
        {
            int choice =
                    JOptionPane.showConfirmDialog(gui,
                            "You have unsaved changes, do you want to save?",
                            "Save?", JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
            if (choice == JOptionPane.YES_OPTION)
            {
                saveUserState();
                System.exit(0);
            }
            if (choice == JOptionPane.NO_OPTION)
            {
                System.exit(0);
            }
        }
        else
        {
            System.exit(0);
        }

    }

    /**
     * A filter that only displays the files with the extension that can be
     * loaded as a list of courses taken
     */
    FileFilter saveLoadFilter = new FileNameExtensionFilter("User State",
                                      kFileExtension);

    /**
     * Loads the file that the user chooses as the new list of Courses Taken
     */
    private void loadCoursesTaken()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(saveLoadFilter);
        int choice = chooser.showOpenDialog(gui);
        if (choice == JFileChooser.APPROVE_OPTION)
        {
            /*
             * try { //
             * coursesTaken.load(chooser.getSelectedFile()getCatalog()); //
             * userStateFile = chooser.getSelectedFile(); // setSaved(true); }
             * catch (IOException ex) {
             * Logger.getLogger(SchedulerFrame.class.getName()).log(
             * Level.SEVERE, null, ex); }
             */
        }

    }

    /**
     * Updates the global saved variable and changes the title of the window to
     * show that it is saved
     */
    private void setSaved(boolean saved)
    {
        this.saved = saved;
        String title = kApplicationName;
        if (savedFile != null)
        {
            title += " - " + savedFile.getName();
        }
        if (!saved)
        {
            title += "*";
        }

        gui.setTitle(title);

    }

    /**
     * Saves the current list of Courses Taken out to the correct file
     */
    private void saveUserState()
    {
        if (savedFile == null)
        {
            saveAsUserState();

        }
        // try
        // {
        // state.write(savedFile);
        setSaved(true);
        // }
        // catch (IOException ex)
        // {
        // Logger.getLogger(SchedulerFrame.class.getName()).log(Level.SEVERE,
        // null, ex);
        // }

    }

    /**
     * Saves the current list of Courses Taken to the file the the user chooses
     */
    private void saveAsUserState()
    {
        JFileChooser chooser = new JFileChooser()
        {
            @Override
            public void approveSelection()
            {
                File SelectedFile = getSelectedFile();

                if (!SelectedFile.getName().endsWith("." + kFileExtension))
                {
                    try
                    {
                        SelectedFile =
                                new File(SelectedFile.getCanonicalPath() + "."
                                        + kFileExtension);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }

                if (SelectedFile.exists() && !SelectedFile.equals(savedFile))
                {
                    int overWrite =
                            JOptionPane
                                    .showConfirmDialog(
                                            this,
                                            getSelectedFile().getName()
                                                    + " already exists.\nDo you want to replace it?",
                                            "Confirm Save As",
                                            JOptionPane.YES_NO_CANCEL_OPTION,
                                            JOptionPane.QUESTION_MESSAGE);
                    if (overWrite == JOptionPane.OK_OPTION)
                    {
                        savedFile = SelectedFile;
                        super.approveSelection();
                    }
                    else if (overWrite == JOptionPane.CANCEL_OPTION)
                    {
                        super.cancelSelection();
                    }
                    return;
                }
                else
                {
                    savedFile = SelectedFile;
                    super.approveSelection();
                }

            }
        };
        chooser.setFileFilter(saveLoadFilter);
        chooser.setSelectedFile(savedFile);
        int choice = chooser.showSaveDialog(gui);
        if (choice == JFileChooser.APPROVE_OPTION)
        {
            saveUserState();
        }
    }

    /**
     * Handles a check box being clicked in the GUI. Adds or removes the checked
     * Course as applicable.
     */
    void CheckBoxClicked()
    {
        setSaved(false);
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
        Course selected = null;
        CourseOption option = null;
        if (e.getSource() instanceof JList)
        {
            JList selectedList = (JList) e.getSource();
            if (selectedList.getSelectedValue() instanceof Course)
            {
                selected = (Course) selectedList.getSelectedValue();
                for (CourseOption o : unfulfilledOptions)
                {
                    if (o.getFulfillmentOptions().contains(selected))
                    {
                        option = o;
                    }
                }

            }
        }
        else
        {
            selected = gui.getSelectedPassed();
            for (CourseOption o : chart.getSectionReqs())
            {
                System.out.println(o.getFulfillmentOptions());
                if (o.getFulfillmentOptions().contains(selected))
                {
                    option = o;
                }
            }
        }
        if (selected != null)
        {
            gui.setInfo(selected, option);
            gui.repaint();
        }
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
            unitCount += c.getUnits();
        }

        for (CourseOption co : unfulfilledOptions)
        {
            System.out.println(co.getQuarter() + " " + co.getRequirement());
            if (unitCount > maxUnits)
            {
                break;
            }

            for (Course c : co.getFulfillmentOptions())
            {
                System.out.println("\t" + c);
                if (c.preRecsMet(coursesTaken.getCourses())
                        && !coursesTaken.contains(c)
                        && unitCount + c.getUnits() <= maxUnits)
                {
                    System.out.println("\t\t" + c);
                    if (!coursesTaken.contains(c) && !schedule.contains(c))
                    {
                        System.out.println("\t\t\tAdd: " + c);
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
     * 
     * @return the catalog of courses that was loaded
     */
    public CourseList getCatalog()
    {
        return catalog;
    }

    private void filterRequired()
    {
        String preReq = gui.getRequiredCombo();
        String filterText = gui.getRequiredFilter();
        coursesRequired.filterList(filterText);
        List<Course> filteredCourses =
                new ArrayList<Course>(coursesRequired.filtered);
        for (Course c : coursesRequired.filtered)
        {
            if (c.preRecsMet(coursesTaken.getCourses()))
            {
                if (preReq.equals(PREREQ__NOT_MET))
                {
                    filteredCourses.remove(c);
                }
            }
            else
            {
                if (preReq.equals(PREREQ_MET))
                {
                    filteredCourses.remove(c);
                }
            }
        }
        coursesRequired.filtered.clear();
        coursesRequired.filtered.addAll(filteredCourses);
    }
}