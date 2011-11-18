package edu.calpoly.razsoftware;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.google.gson.Gson;

/**
 * This class represents all of the logic necessary to 
 * modify the model correctly depending on the users actions
 * @author michaelsvanbeek, Daniel Johnson, Derek Panger
 */
public class SchedulerController extends KeyAdapter implements ActionListener,
        ListSelectionListener
{

    private static final String kFileExtension   = "us";
    private static final String kApplicationName = "Course Advisor";
    /**
     * The choice the user selects when they would like to see all of the
     * required classes
     */
    public static final String  kAllFilter       = "All";
    /**
     * The choice the user selects when they would like to see required classes
     * with prerequisites met
     */
    public static final String  kPrereqMet       = "Prerequisites Met";
    /**
     * The choice the user selects when they would like to see required classes
     * with prerequisites not met
     */
    public static final String  kPrereqNotMet    = "Prerequisite Not Met";

    private CourseList          coursesTaken;
    private CourseList          coursesRequired;
    private CourseList          schedule;
    private Set<CourseOption>   unfulfilledOptions;
    private SchedulerView       gui;
    private Flowchart           chart;
    private CourseList          catalog;
    private CourseDecider       decider;

    private boolean             saved            = true;
    private File                savedFile;

    /**
     * Constructor
     * 
     * @param coursesTaken
     *            a CourseList of Courses that have been taken
     * @param coursesRequired
     *            The courseOptions the user must fulfill to graduate
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
        setSaved(true);

    }

    /**
     * Action Performed method from ActionListener
     * 
     * @param e
     *            The event to analyze
     */
    public void actionPerformed(ActionEvent e)
    {
        // IF the user wants to add a course to the suggested list
        if (e.getActionCommand().equals("Add Course"))
        {
            addToSchedule();
        }
        // ELSEIF the user wants to remove a course from the suggested list
        else if (e.getActionCommand().equals("Remove Course"))
        {
            removeFromSchedule();
        }
        // ELSEIF the user wants to clear the suggested list
        else if (e.getActionCommand().equals("Clear Suggested"))
        {
            clearSchedule();
        }
        // ELSEIF the user wants to Autofill the suggested list
        else if (e.getActionCommand().equals("Autofill"))
        {
            int maxUnits = gui.getScheduleUnits();
            int unitCount = 0;
            // FOR each course already in the suggested schedule
            for (Course scheduledCourse : schedule.getCourses())
            {
                // add the units value
                unitCount += scheduledCourse.getUnits();
            }
            autoFillSechdule(maxUnits - unitCount);
        }

        // the menu actions

        // ELSEIF the user wants to Open a saved file
        else if (e.getActionCommand().equals("Open"))
        {
            loadCoursesTaken();
        }
        // ELSEIF the user wants to Save their progress
        else if (e.getActionCommand().equals("Save"))
        {
            saveUserState();
        }
        // ELSEIF the user wants to Save their progress and give it a name
        else if (e.getActionCommand().equals("Save As"))
        {
            saveAsUserState();
        }
        // ELSEIF the user wants to quit the application
        else if (e.getActionCommand().equals("Quit"))
        {
            quit();
        }
        // ELSEIF the user has changed the combobox filter for the required
        // classes
        else if (e.getActionCommand().equals("Required Combo"))
        {
            filterRequired();
        }
        // ENDIF
    }

    /**
     * Key released from KeyAdapter, responds when the user types something in
     * the required list filer
     * 
     * @param keyevent
     *            The event to analyze
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
        // IF the user didn't accidently click the exit button
        if (checkSaved() != JOptionPane.CANCEL_OPTION)
        {
            System.exit(0);
        }// ENDIF

    }

    /**
     * Prompts the user to save any changes they have made since the last save
     * 
     * @return the choice the user made
     */
    private int checkSaved()
    {
        int choice = JOptionPane.YES_OPTION;
        // IF the application has been changed since the last save
        if (!saved)
        {
            // prompt the user to save their progress
            choice =
                    JOptionPane.showConfirmDialog(gui,
                            "You have unsaved changes, do you want to save?",
                            "Save?", JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
            // IF the user wasnts to save be sure to save
            if (choice == JOptionPane.YES_OPTION)
            {
                saveUserState();
            }// ENDIF
        }// ENDIF
        return choice;

    }

    /**
     * A filter that only displays the files with the extension that can be
     * loaded as a list of courses taken
     */
    private final FileFilter kSaveLoadFilter = new FileNameExtensionFilter(
                                                     "User State",
                                                     kFileExtension);

    /**
     * Loads the file that the user chooses as the new list of Courses Taken
     */
    private void loadCoursesTaken()
    {
        // IF the user didn't accidently click the load button
        if (checkSaved() != JOptionPane.CANCEL_OPTION)
        {
            // prompt the user for the file to load
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(kSaveLoadFilter);
            int choice = chooser.showOpenDialog(gui);
            // If the user gives a file to load
            if (choice == JFileChooser.APPROVE_OPTION)
            {
                Gson gson = new Gson();
                Scanner fileReader = null;
                try
                {
                    fileReader = new Scanner(chooser.getSelectedFile());
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                // IF there wasn't an error opening the file
                if (fileReader != null)
                {
                    // Clear the list of courses taken and add the ones from the
                    // file
                    this.coursesTaken.clear();
                    // WHILE there is another course to read from the file
                    // read it and add it to the list
                    while (fileReader.hasNextLine())
                    {
                        String str = fileReader.nextLine();
                        Course course = gson.fromJson(str, Course.class);
                        coursesTaken.add(catalog.lookUp(course.getMajor()
                                .get(0), course.getNumber()));

                    }// ENDWHILE

                    fileReader.close();
                }// ENDIF

                // else we have an empty JSon file

                // Reset the list of required classes after the load
                unfulfilledOptions = decider.decideClasses(coursesTaken, chart);
                this.coursesRequired.clear();
                this.coursesRequired.addAll(decider
                        .getRequiredCourses(unfulfilledOptions));
                savedFile = chooser.getSelectedFile();
                setSaved(true);
                filterRequired();
            }

            gui.repaint();
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
        // IF there is currently a file the user is working with
        if (savedFile != null)
        {
            // add the file's name to the title
            title += " - " + savedFile.getName();
        }// ENDIF
         // IF the application has been changed since the last save
        if (!saved)
        {
            // Add a notifier to the title
            title += "*";
        }// ENDIF

        gui.setTitle(title);

    }

    /**
     * Saves the current list of Courses Taken out to the correct file
     */
    private void saveUserState()
    {
        // IF the user is trying to save without having a file to back the save
        if (savedFile == null)
        {
            // Prompt the user for a file
            saveAsUserState();

        }
        // ELSE there is a file to back the save
        else
        {
            try
            {
                // only save if the user has taken courses
                if (coursesTaken != null)
                {
                    Gson gson = new Gson();
                    BufferedWriter bwriter =
                            new BufferedWriter(new FileWriter(savedFile));

                    Set<Course> courses = coursesTaken.getCourses();

                    // FOR each course the user has take
                    for (Course taken : courses)
                    {
                        // Save it to the file
                        bwriter.write(gson.toJson(taken));
                        bwriter.write("\n");
                    }// ENDFOR

                    setSaved(true);
                    bwriter.close();
                }
            }
            catch (IOException ex)
            {
                Logger.getLogger(SchedulerView.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }// ENDIF
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
                File selectedFile = getSelectedFile();                
                //IF the selected file doen't have the correct extension
                if (!selectedFile.getName().endsWith("." + kFileExtension))
                {
                   
                        //append the extension onto the name
                    selectedFile = new File(selectedFile.getPath() + "." 
                        + kFileExtension);
                    
                }//ENDIF
                //If the user is overwriting a already existing file
                if (selectedFile.exists() && !selectedFile.equals(savedFile))
                {
                    int overWrite = JOptionPane.showConfirmDialog(this,
                                    getSelectedFile().getName()
                                    +" already exists.\nDo you want to replace it?",
                                    "Confirm Save As",
                                    JOptionPane.YES_NO_CANCEL_OPTION,
                                    JOptionPane.QUESTION_MESSAGE);
                    //IF the user wants to overwrite
                    if (overWrite == JOptionPane.OK_OPTION)
                    {
                        //save the file
                        savedFile = selectedFile;
                        super.approveSelection();
                    }//ELSEIF the user wants to cancel
                    else if (overWrite == JOptionPane.CANCEL_OPTION)
                    {
                        //cancel the save
                        super.cancelSelection();
                    }
                    return;
                }//ELSE the file does not exist
                else
                {
                    savedFile = selectedFile;
                    super.approveSelection();
                }//ENDIF

            }
        };
        chooser.setFileFilter(kSaveLoadFilter);
        chooser.setSelectedFile(savedFile);
        int choice = chooser.showSaveDialog(gui);
        //IF the user wants to save
        if (choice == JFileChooser.APPROVE_OPTION)
        {
            //save their state
            saveUserState();
        }//END IF
    }

    /**
     * Handles a check box being clicked in the GUI. Adds or removes the checked
     * Course as applicable.
     */
    void checkBoxClicked()
    {
        setSaved(false);
        Course clickedCourse = gui.getSelectedPassed();
        // If the user has taken the course they are clicking on
        if (coursesTaken.contains(clickedCourse))
        {
            // remove it from their state
            coursesTaken.remove(clickedCourse);
        }
        // If the user has not taken the course they are clicking on
        else
        {
            // add it from their state
            coursesTaken.add(clickedCourse);
        }
        unfulfilledOptions = decider.decideClasses(coursesTaken, chart);
        coursesRequired.clear();
        coursesRequired.addAll(decider.getRequiredCourses(unfulfilledOptions));
        filterRequired();
        gui.repaint();
    }

    /**
     * Handles a row being selected in the GUI. Determines what row was selected
     * and displays that course's info in the GUI info pane
     * 
     * @param e
     *            the event to analyze
     */
    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        Course selected = null;
        CourseOption option = null;
        // IF the user clicked in one of the two lists
        if (e.getSource() instanceof JList)
        {
            JList selectedList = (JList) e.getSource();
            // IF the item clicked on was a course
            if (selectedList.getSelectedValue() instanceof Course)
            {
                selected = (Course) selectedList.getSelectedValue();
                // For each option in unfulfilledOptions
                for (CourseOption unfulfilled : unfulfilledOptions)
                {
                    // IF the course the user clicked on fulfills this option
                    if (unfulfilled.getFulfillmentOptions().contains(selected))
                    {
                        option = unfulfilled;
                    }// ENDIF
                }// ENDFOR
            }// ENDIF
        }
        // ELSE the usr clicked in the table
        else
        {
            selected = gui.getSelectedPassed();
            // For each option in the flowchart
            for (CourseOption flowchartOption : chart.getSectionReqs())
            {
                // IF the course the user clicked on fulfills this option
                if (flowchartOption.getFulfillmentOptions().contains(selected))
                {
                    option = flowchartOption;
                }// ENDIF
            }// ENDFOR
        }// ENDIF
         // update the gui
        gui.setInfo(selected, option);
        gui.repaint();
    }

    /**
     * Handles the ">" button being clicked. Adds the selected field from
     * required to the Schedule.
     */
    private void addToSchedule()
    {
        Course selectedCourse = gui.getSelectedRequired();
        // schedule.add();

        int choice = JOptionPane.YES_OPTION;

        // IF the user has not fulfilled the prereqs for the course they are
        // trying to add
        if (!selectedCourse.preRecsMet(coursesTaken.getCourses()))
        {
            // Verify they really want to add it to their schedule
            choice = JOptionPane.showConfirmDialog(
                            gui,
                            "You have not fulfilled the prerequisites for this course.\n"
                            +"Are you sure you want to add this to your schedule?",
                            "Prerequisites not met",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);

        }
        // IF the user wants to add the course, add it
        if (choice == JOptionPane.YES_OPTION)
        {
            schedule.add(selectedCourse);
            updateUnitCount();
        }

    }

    /**
     * update the unit count for the suggested schedule column
     */
    private void updateUnitCount()
    {
        int count = 0;
        // FOR each course in the schedule
        for (Course scheduledCourse : schedule.getCourses())
        {
            // add the number of units it is worth
            count += scheduledCourse.getUnits();
        }
        gui.updateUnitCount(count);
    }

/**
    * Handles the "<" button being clicked.  Removes the selected field from
    * schedule.
    */
    private void removeFromSchedule()
    {
        schedule.remove(gui.getSelectedSchedule());
        updateUnitCount();
    }

    /**
     * Handles the "Clear" button being clicked. Removes all from the Schedule
     */
    private void clearSchedule()
    {
        schedule.clear();
        updateUnitCount();
    }

    /**
     * Handles the "AutoFill" button being pressed. Uses our autofill algorithm
     * to fill it.
     * 
     * @param unitsToAdd
     *            the max number of units to add to the suggested list
     */
    private void autoFillSechdule(int unitsToAdd)
    {
        //IF the user has fulfilled all of the requirements
        if (unfulfilledOptions.isEmpty())
        {
            return;
        }//ENDIF

        //Sort the CourseOption depending on the quarter they are suggested
        List<CourseOption> unfulfilled = new ArrayList<CourseOption>(unfulfilledOptions);
        Collections.sort(unfulfilled, new Comparator<CourseOption>()
        {
            @Override
            public int compare(CourseOption o1, CourseOption o2)
            {
                return new Integer(o1.getQuarter()).compareTo(o2.getQuarter());
            }
        });
        //FOR each CourseOption
        for (CourseOption co : unfulfilled)
        {
            //If there is still room in the schedule
            if (unitsToAdd > 0)
            {
                boolean courseFound = false;
                //for each course in the CourseOption
                for (Course optionCourse : co.getFulfillmentOptions())
                {
                    //IF a course hasn't been found for this option
                    //the course has all of the prereqs met,
                    //they have not already taken it,
                    //it is a small enough unit count//
                    //and it isn't already in the list
                    if (!courseFound&&optionCourse.preRecsMet(coursesTaken.getCourses())
                            && !coursesTaken.contains(optionCourse)
                            && +optionCourse.getUnits() <= unitsToAdd
                            && !schedule.contains(optionCourse))
                    {
                        courseFound = true;
                        //Add it to the suggested schedule
                        schedule.add(optionCourse);
                        unitsToAdd-=optionCourse.getUnits();                    
                    }//ENDIF    
                }//ENDFOR
            }//ENDIF
        }//ENDFOR
        updateUnitCount();
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

    /**
     * Filters the courses in the required column depending on the combo box
     * selection
     */
    private void filterRequired()
    {
        String preReq = gui.getRequiredCombo();
        String filterText = gui.getRequiredFilter();
        coursesRequired.filterList(filterText);
        List<Course> filteredCourses =
                new ArrayList<Course>(coursesRequired.getFiltered());
        // FOR each course in the required list
        for (Course requiredCourse : coursesRequired.getFiltered())
        {
            // IF the prereqs are met for the course
            if (requiredCourse.preRecsMet(coursesTaken.getCourses()))
            {
                // IF the user only wants to see classes with prereqs not met
                if (preReq.equals(kPrereqNotMet))
                {
                    // remove the course
                    filteredCourses.remove(requiredCourse);
                }
            }
            // ELSE the prereqs are not me for the course
            else
            {
                // IF the user only wants to see classes with prereqs met
                if (preReq.equals(kPrereqMet))
                {
                    // remove the course
                    filteredCourses.remove(requiredCourse);
                }
            }
        }// ENDFOR
        coursesRequired.getFiltered().clear();
        coursesRequired.getFiltered().addAll(filteredCourses);
    }
}