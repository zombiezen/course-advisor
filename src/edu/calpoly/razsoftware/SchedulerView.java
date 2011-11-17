package edu.calpoly.razsoftware;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.table.TableRowSorter;




/**
 * This class is the only part that the user interacts with.
 * Everything the user will need to know is presented here
 * Additionally this holds all the components that the user would
 * interact with to change the model
 *
 * @author Daniel,Michael Van Beek 
 */
public class SchedulerView extends JFrame
{
    private static final String      kApplicationName            = "Course Advisor";
    private static final String      kVersion             = "v1.0";
    private static final String      kAuthors             = "\nAdam Spurgin\n"
                                                            + "Ross Light\n"
                                                            + "Michael Van Beek\n"
                                                            + "Daniel Johnson\n"
                                                            + "Thomas Wong\n"
                                                            + "Derek Panger\n";

    private static final String      kLiscense  = "\nGson: Copyright \u00a9 "
                                                + "2008-2011 Google Inc.\n"
                                                + "Guava: Copyright \u00a9 " 
                                                + "2010-2011 Google Inc.\n"
                                                + "Licensed under Apache License 2.0\n"
                                                + "http://www.apache.org/"
                                                + "licenses/LICENSE-2.0";
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JMenuItem aboutMenuItem;
    private JButton addButton;
    private JButton clearButton;
    private JLabel descriptionInfo;
    private JTextPane descriptionPane;
    private JScrollPane descriptionScroller;
    private JLabel dynamicUnits;
    private JMenu fileMenu;
    private JLabel fulfillsInfo;
    private JTextPane fulfillsLabel;
    private JMenu helpMenu;
    private JMenuBar menuBar;
    private JLabel nameInfo;
    private JTextPane nameLabel;
    private JMenuItem openMenuItem;
    private JTextField passedFilter;
    private JLabel passedLabel;
    private JScrollPane passedScroller;
    private JTable passedTable;
    private TableRowSorter<CourseTableModel> passedSorter;
    private JLabel preReqInfo;
    private JTextPane prereqLabel;
    private JMenuItem quitMenuItem;
    private JButton removeButton;
    private JTextField requestedUnitsField;
    private JComboBox requiredComboBox;
    private JTextField requiredFilter;
    private JLabel requiredLabel;
    private JList requiredList;
    private JScrollPane requiredScroller;
    private JMenuItem saveAsMenuItem;
    private JMenuItem saveMenuItem;
    private JButton suggestButton;
    private JLabel suggestedLabel;
    private JList suggestedList;
    private JScrollPane suggestedScroller;
    private JLabel unitsInfo;
    private JTextPane unitsLabel;
    private JLabel unitsSelecter;    
    // End of variables declaration//GEN-END:variables
    
    
    /**
     * Overrides the dispose method so the application 
     * can ask to be saved before exiting
     */
    @Override
    public void dispose()
    {
        quitMenuItem.doClick();

    };
    
    /**
     * Creates the window that the user interacts with to decide their schedule
     */
    public SchedulerView() 
    {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        setMinimumSize(getPreferredSize());
        //When the user clicks on the about button display the about box
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                displayAboutInfo();
            }
        });
        //Update the table filter whenever the user types something
        passedFilter.addKeyListener(new KeyAdapter()
        {            
            @Override
            public void keyReleased(KeyEvent keyevent)
            {
                passedSorter.allRowsChanged();
                
            }
        });

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() 
    {

        passedFilter = new JTextField();
        passedLabel = new JLabel();
        nameLabel = new JTextPane();
        prereqLabel = new JTextPane();
        requiredComboBox = new JComboBox();
        requiredFilter = new JTextField();
        requiredLabel = new JLabel();
        requiredScroller = new JScrollPane();
        requiredList = new JList();
        unitsInfo = new JLabel();
        unitsLabel = new JTextPane();
        fulfillsInfo = new JLabel();
        fulfillsLabel = new JTextPane();
        clearButton = new JButton();
        removeButton = new JButton();
        addButton = new JButton();
        nameInfo = new JLabel();
        preReqInfo = new JLabel();
        descriptionScroller = new JScrollPane();
        descriptionPane = new JTextPane();
        dynamicUnits = new JLabel();
        requestedUnitsField = new JTextField();
        unitsSelecter = new JLabel();
        suggestButton = new JButton();
        suggestedLabel = new JLabel();
        descriptionInfo = new JLabel();
        suggestedScroller = new JScrollPane();
        suggestedList = new JList();
        passedScroller = new JScrollPane();
        passedTable = new JTable();
        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        openMenuItem = new JMenuItem();
        saveMenuItem = new JMenuItem();
        saveAsMenuItem = new JMenuItem();
        quitMenuItem = new JMenuItem();
        helpMenu = new JMenu();
        aboutMenuItem = new JMenuItem();

        passedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        passedLabel.setText("Courses Passed");

        Border loweredBorder = new BevelBorder(BevelBorder.LOWERED);

        unitsLabel.setBackground(null);
        nameLabel.setBorder(loweredBorder);
        nameLabel.setEditable(false);
        nameLabel.setFocusable(false);

        unitsLabel.setBackground(null);
        prereqLabel.setBorder(loweredBorder);
        prereqLabel.setEditable(false);
        prereqLabel.setFocusable(false);

        requiredComboBox.setModel(new DefaultComboBoxModel(new String[] 
        { "All", "Prerequisites Met", "Prerequisite Not Met" }));
        
        requiredComboBox.setActionCommand("Required Combo");
        requiredLabel.setHorizontalAlignment(SwingConstants.CENTER);
        requiredLabel.setText("Courses Required");

        requiredScroller.setViewportView(requiredList);

        unitsInfo.setText("Units:");

        unitsLabel.setBackground(null);
        unitsLabel.setBorder(loweredBorder);
        unitsLabel.setEditable(false);
        unitsLabel.setFocusable(false);

        fulfillsInfo.setText("Fulfills:");

        fulfillsLabel.setBackground(null);
        fulfillsLabel.setBorder(loweredBorder);
        fulfillsLabel.setEditable(false);
        fulfillsLabel.setFocusable(false);

        clearButton.setText("Clear");
        clearButton.setActionCommand("Clear Suggested");

        removeButton.setText("<");
        removeButton.setActionCommand("Remove Course");

        addButton.setText(">");
        addButton.setActionCommand("Add Course");

        nameInfo.setText("Name:");

        preReqInfo.setText("Prerequisites:");

        descriptionPane.setBackground(null);
        descriptionPane.setEditable(false);
        descriptionPane.setFocusable(false);
        descriptionScroller.setViewportView(descriptionPane);

        dynamicUnits.setHorizontalAlignment(SwingConstants.RIGHT);
        dynamicUnits.setText("0/");

        requestedUnitsField.setText("16");

        unitsSelecter.setText("Units");

        suggestButton.setText("Autofill");

        suggestedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        suggestedLabel.setText("Suggested Schedule");

        descriptionInfo.setText("Description:");
        
        suggestedScroller.setViewportView(suggestedList);

        passedScroller.setViewportView(passedTable);

        fileMenu.setMnemonic('F');
        fileMenu.setText("File");

        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openMenuItem.setMnemonic('O');
        openMenuItem.setText("Open");
        fileMenu.add(openMenuItem);

        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem.setMnemonic('S');
        saveMenuItem.setText("Save");
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK|java.awt.event.InputEvent.SHIFT_MASK));
        saveAsMenuItem.setText("Save As");
        saveAsMenuItem.setMnemonic('A');
        fileMenu.add(saveAsMenuItem);

        quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        quitMenuItem.setText("Quit");
        quitMenuItem.setMnemonic('Q');
        fileMenu.add(quitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setMnemonic('H');
        helpMenu.setText("Help");

        aboutMenuItem.setMnemonic('A');
        aboutMenuItem.setText("About");

        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(Alignment.LEADING, false)
                        .addComponent(passedScroller, 0, 0, Short.MAX_VALUE)
                        .addComponent(passedLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(passedFilter, GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(nameInfo)
                            .addPreferredGap(ComponentPlacement.RELATED))
                        .addGroup(layout.createParallelGroup(Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(preReqInfo)
                                .addPreferredGap(ComponentPlacement.RELATED))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                    .addComponent(requiredLabel, GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(requiredComboBox, 0, 155, Short.MAX_VALUE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(requiredFilter, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
                                            .addComponent(requiredScroller, GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                                            .addComponent(nameLabel, GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE))
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                                            .addComponent(removeButton)
                                            .addComponent(addButton))))
                                .addGap(6))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                    .addComponent(prereqLabel, GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                            .addComponent(unitsInfo)
                                            .addComponent(unitsLabel, GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                            .addComponent(fulfillsInfo)
                                            .addComponent(fulfillsLabel, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))))
                                .addGap(55))))
                    .addGroup(layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(suggestedLabel, GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                            .addContainerGap(30, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(suggestedScroller, GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                            .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(clearButton)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(dynamicUnits, GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(requestedUnitsField, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(unitsSelecter)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(suggestButton)
                            .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(descriptionInfo)
                            .addGap(234))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(descriptionScroller, GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                            .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                                .addComponent(suggestedLabel)
                                .addComponent(passedLabel))
                            .addGap(27))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(requiredLabel)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                                .addComponent(passedFilter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(requiredComboBox, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
                                .addComponent(requiredFilter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                                .addComponent(suggestedScroller, GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(addButton, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addComponent(removeButton, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                                .addComponent(requiredScroller, GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE))
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(suggestButton)
                                        .addComponent(unitsSelecter)
                                        .addComponent(requestedUnitsField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(dynamicUnits)
                                        .addComponent(clearButton))
                                    .addPreferredGap(ComponentPlacement.UNRELATED)
                                    .addComponent(descriptionInfo)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addComponent(descriptionScroller, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(nameInfo)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addComponent(nameLabel, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(unitsInfo)
                                            .addPreferredGap(ComponentPlacement.RELATED)
                                            .addComponent(unitsLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(fulfillsInfo)
                                            .addPreferredGap(ComponentPlacement.RELATED)
                                            .addComponent(fulfillsLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addComponent(preReqInfo)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addComponent(prereqLabel, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))))
                        .addComponent(passedScroller, GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE))
                    .addContainerGap())
        );
        layout.linkSize(SwingConstants.VERTICAL, new Component[] {requiredComboBox, requiredFilter});
        getContentPane().setLayout(layout);
        pack();

    }// </editor-fold>//GEN-END:initComponents
   


    /**
     * Associates the given controller with all of the components in this window
     * 
     * @param controller
     *            The controller for this view
     */
    public void setController(SchedulerController controller)
    {
        addButton.addActionListener(controller);
        removeButton.addActionListener(controller);
        clearButton.addActionListener(controller);
        suggestButton.addActionListener(controller);
        requiredList.addListSelectionListener(controller);
        suggestedList.addListSelectionListener(controller);
        openMenuItem.addActionListener(controller);
        saveMenuItem.addActionListener(controller);
        saveAsMenuItem.addActionListener(controller);
        quitMenuItem.addActionListener(controller);
        requiredFilter.addKeyListener(controller);
        requiredComboBox.addActionListener(controller);
        passedTable.getSelectionModel().addListSelectionListener(controller);

    }

    /**
     * Returns the Course that is selected in the "Passed" pane
     * 
     * @return Course that is selected in the "Passed" pane
     */
    public Course getSelectedPassed()
    {
        int selectedRow = passedTable.getSelectedRow();
        Course selectedCourse = null;
        // IF there is a row currently selected in the table
        if (selectedRow != -1)
        {
            selectedCourse = (Course) passedTable.getValueAt(selectedRow, 1);
        }// ENDIF
        System.out.println(selectedRow);
        return selectedCourse;
    }

    /**
     * Returns the Course that is selected in the "Required" pane
     * 
     * @return Course that is selected in the "Required" pane
     */
    public Course getSelectedRequired()
    {
        return (Course) requiredList.getSelectedValue();
    }

    /**
     * Returns the Course that is selected in the "Schedule" pane
     * 
     * @return Course that is selected in the "Schedule" pane
     */
    public Course getSelectedSchedule()
    {
        return (Course) suggestedList.getSelectedValue();
    }

    /**
     * Returns the current selected item in the required comboBox
     * 
     * @return The text that is visible in the required combo box
     */
    public String getRequiredCombo()
    {
        return (String) requiredComboBox.getSelectedItem();
    }

    /**
     * Returns the text currently typed in the required filter
     * 
     * @return The text that is visible in the required filter
     */
    public String getRequiredFilter()
    {
        return requiredFilter.getText();

    }

    /**
     * Returns the number of units in the units to take box
     * 
     * @return the number of units in the units to take box
     */
    public int getScheduleUnits()
    {
        return Integer.parseInt(requestedUnitsField.getText());
    }

    /**
     * Fills the "Info" section with the info of the parameter Course
     * 
     * @param selectedCourse
     *            course to display
     * @param option
     *            The CourseOption,if it exists, that corresponds to given
     *            course
     */
    public void setInfo(Course selectedCourse, CourseOption option)
    {
        // Default all of the fields to be updated to empty Strings
        String name = "";
        String units = "";
        String fulfills = "";
        String prereq = "";
        String description = "";
        // IF there is a course that needs it's info displayed
        if (selectedCourse != null)
        {
            // Fill the strings with the associated info about the course
            name = selectedCourse.getName();
            units = Integer.toString(selectedCourse.getUnits());
            prereq = selectedCourse.getPreRequisitesString();
            description = selectedCourse.getDescription();
            // IF there is a flowchart option associated with that course
            if (option != null)
            {
                // Fill the string with the associated info about the option
                fulfills = option.getRequirement();
            }
        }// ENDIF
         // update the fields to display the correct information
        nameLabel.setText(name);
        unitsLabel.setText(units);
        fulfillsLabel.setText(fulfills);
        prereqLabel.setText(prereq);
        descriptionPane.setText(description);
    }

    /**
     * Associates the model with the different panes in this window
     * 
     * @param tableModel
     *            The model the is displayed in the JTable
     * @param coursesRequired
     *            The list of classes required to fulfill the graduation
     *            requirement
     * @param coursesSuggested
     *            The suggested schedule that is displayed in the last column
     */
    public void setSources(CourseTableModel tableModel,
            final CourseList coursesRequired, CourseList coursesSuggested)
    {
        passedSorter = new TableRowSorter<CourseTableModel>(tableModel);
        //now that we have the model for the table we can create the tables filter
        passedSorter.setRowFilter(new RowFilter<CourseTableModel, Integer>()
        {
            public boolean include(
                    Entry<? extends CourseTableModel, ? extends Integer> entry)
            {
                return (entry.getStringValue(1).toLowerCase()
                        .contains(passedFilter.getText().toLowerCase()));
            }
        });
        //Associate the sorter to the table
        passedTable.setRowSorter(passedSorter);
        //Associate the models with their correct observers
        passedTable.setModel(tableModel);
        requiredList.setModel(coursesRequired);
        suggestedList.setModel(coursesSuggested);
        //Now that all the data is loaded the window can be visible
        setVisible(true);
    }

    /**
     * Updates the count of the dynamic unit count label to the given value
     * 
     * @param count
     *            the new unit count in the suggested schedule column;
     */
    public void updateUnitCount(int count)
    {
        dynamicUnits.setText(count + "/");
    }

    /**
     * Pops up a window that displays information about the application
     */
    private void displayAboutInfo()
    {
        JOptionPane.showMessageDialog(this, kApplicationName + " " + kVersion + "\n"
                + kAuthors + kLiscense, "About", JOptionPane.PLAIN_MESSAGE);
    }

}
