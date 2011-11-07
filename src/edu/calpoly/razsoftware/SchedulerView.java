/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.calpoly.razsoftware;

import java.awt.Component;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
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
import javax.swing.event.ListDataListener;
import javax.swing.table.TableRowSorter;

/**
 * 
 * @author michaelsvanbeek
 */
public class SchedulerView extends JFrame
{
   // OLD GUI CODE

   private static final String      FILE_EXTENSION      = "us";
   private static final String      APP_NAME            = "Course Advisor";
   private static final String      VERSION             = "v1.0";
   private static final String      AUTHORS             =
                                                              "\nAdam Spurgin\n"
                                                                    + "Ross Light\n"
                                                                    + "Michael Van Beek\n"
                                                                    + "Daniel Johnson\n"
                                                                    + "Thomas Wong\n"
                                                                    + "Derek Panger\n";

   private static final String      LICENSE             =
                                                              "\nGson: Copyright \u00a9 2008-2011 Google Inc.\n"
                                                                    + "Guava: Copyright \u00a9 2010-2011 Google Inc.\n"
                                                                    + "Licensed under Apache License 2.0\nhttp://www.apache.org/licenses/LICENSE-2.0";

   private boolean                  saved               = false;
   // private CourseList list;
   // private CoursesTaken state = new CoursesTaken();
   // private CourseDecider decider = new CourseDecider();
   // private Flowchart flowchart;
   private File                     userStateFile;

   private JMenuBar                 menuBar             = new JMenuBar();
   private JMenu                    fileMenu            = new JMenu("File");
   private JMenuItem                openMenuItem        = new JMenuItem("Open");
   private JMenuItem                saveMenuItem        = new JMenuItem("Save");
   private JMenuItem                saveAsMenuItem      = new JMenuItem(
                                                              "Save As");
   private JMenuItem                quitMenuItem        = new JMenuItem("Quit");
   private JMenu                    helpMenu            = new JMenu("Help");
   private JMenuItem                aboutMenuItem       =
                                                              new JMenuItem(
                                                                    "About");
   private JLabel                   passedLabel         =
                                                              new JLabel(
                                                                    "Courses Passed",
                                                                    SwingConstants.CENTER);
   private JTextField               passedFilter        = new JTextField();

//   private CourseTableModel         passedModel;

   TableRowSorter<CourseTableModel> passedSorter;
   private JTable                   passedTable         = new JTable();
   private JScrollPane              passedScroller      = new JScrollPane(
                                                              passedTable);
   private JLabel                   requiredLabel       =
                                                              new JLabel(
                                                                    "Courses Required",
                                                                    SwingConstants.CENTER);
   private final String             ALL_FILTER          = "All";
   private final String             PREREQ_MET          = "Prerequisites Met";
   private final String             PREREQ__NOT_MET     =
                                                              "Prerequisite Not Met";
   private JComboBox                requiredComboBox    = new JComboBox(
                                                              new String[] {
         ALL_FILTER, PREREQ_MET, PREREQ__NOT_MET             });
   private JTextField               requiredFilter      = new JTextField();
//   DefaultListModel                 requiredModel       =
//                                                              new DefaultListModel();
   private JList                    requiredList        = new JList();
   private JScrollPane              requiredScroller    = new JScrollPane(requiredList);
   private static final String      UNITS               = "Units";
   private JLabel                   unitsSelecter       = new JLabel(UNITS);
   private JTextField               requestedUnitsField = new JTextField("16");
   private JButton                  addButton           = new JButton(">");
   private JButton                  removeButton        = new JButton("<");

   JButton                          clearButton         = new JButton("Clear");

   private final JButton            suggestButton       = new JButton(
                                                              "Autofill");
   private JLabel                   suggestedLabel      =
                                                              new JLabel(
                                                                    "Suggested Schedule",
                                                                    SwingConstants.CENTER);
   private JList                    suggestedList       = new JList();
   private JScrollPane              suggestedScroller   = new JScrollPane(
                                                              suggestedList);
   private JLabel                   dynamicUnits        = new JLabel("0/");
   private JLabel                   nameInfo            = new JLabel("Name:");
   private final JLabel             unitsInfo           = new JLabel("Units:");
   private JLabel                   fulfillsInfo        = new JLabel(
                                                              "Fulfills:");
   private final JLabel             preReqInfo          = new JLabel(
                                                              "Prerequisites:");
   private JLabel                   descriptionInfo     = new JLabel(
                                                              "Description:");
   private final JTextPane          nameLabel           = new JTextPane();
   private final JTextPane          unitsLabel          = new JTextPane();
   private final JTextPane          fulfillsLabel       = new JTextPane();
   private final JTextPane          prereqLabel         = new JTextPane();
   private JTextPane                descriptionPane     = new JTextPane();
   private JScrollPane              descriptionScroller = new JScrollPane(
                                                              descriptionPane);

   // ...

   private void layoutComponents()
   {
      GroupLayout layout = new GroupLayout(getContentPane());
      layout.setHorizontalGroup(layout
            .createParallelGroup(Alignment.LEADING)
            .addGroup(
                  layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                              layout.createParallelGroup(Alignment.LEADING,
                                    false)
                                    .addComponent(passedLabel,
                                          GroupLayout.PREFERRED_SIZE, 156,
                                          GroupLayout.PREFERRED_SIZE)
                                    .addComponent(passedScroller,
                                          GroupLayout.DEFAULT_SIZE, 165,
                                          Short.MAX_VALUE)
                                    .addComponent(passedFilter))
                        .addPreferredGap(ComponentPlacement.UNRELATED)
                        .addGroup(
                              layout.createParallelGroup(Alignment.LEADING)
                                    .addGroup(
                                          layout.createSequentialGroup()
                                                .addGroup(
                                                      layout.createParallelGroup(
                                                            Alignment.TRAILING)
                                                            .addGroup(
                                                                  layout.createSequentialGroup()
                                                                        .addComponent(
                                                                              nameLabel,
                                                                              GroupLayout.DEFAULT_SIZE,
                                                                              255,
                                                                              Short.MAX_VALUE)
                                                                        .addPreferredGap(
                                                                              ComponentPlacement.RELATED))
                                                            .addGroup(
                                                                  layout.createSequentialGroup()
                                                                        .addComponent(
                                                                              prereqLabel,
                                                                              GroupLayout.DEFAULT_SIZE,
                                                                              255,
                                                                              Short.MAX_VALUE)
                                                                        .addPreferredGap(
                                                                              ComponentPlacement.RELATED))
                                                            .addGroup(
                                                                  layout.createSequentialGroup()
                                                                        .addGroup(
                                                                              layout.createParallelGroup(
                                                                                    Alignment.TRAILING)
                                                                                    .addGroup(
                                                                                          layout.createSequentialGroup()
                                                                                                .addGroup(
                                                                                                      layout.createParallelGroup(
                                                                                                            Alignment.LEADING)
                                                                                                            .addGroup(
                                                                                                                  layout.createSequentialGroup()
                                                                                                                        .addComponent(
                                                                                                                              requiredComboBox,
                                                                                                                              GroupLayout.PREFERRED_SIZE,
                                                                                                                              137,
                                                                                                                              GroupLayout.PREFERRED_SIZE)
                                                                                                                        .addPreferredGap(
                                                                                                                              ComponentPlacement.RELATED)
                                                                                                                        .addComponent(
                                                                                                                              requiredFilter,
                                                                                                                              GroupLayout.DEFAULT_SIZE,
                                                                                                                              105,
                                                                                                                              Short.MAX_VALUE)
                                                                                                                        .addGap(
                                                                                                                              1))
                                                                                                            .addComponent(
                                                                                                                  requiredLabel,
                                                                                                                  GroupLayout.DEFAULT_SIZE,
                                                                                                                  249,
                                                                                                                  Short.MAX_VALUE))
                                                                                                .addGap(
                                                                                                      6))
                                                                                    .addGroup(
                                                                                          layout.createSequentialGroup()
                                                                                                .addComponent(
                                                                                                      requiredScroller,
                                                                                                      GroupLayout.DEFAULT_SIZE,
                                                                                                      253,
                                                                                                      Short.MAX_VALUE)
                                                                                                .addGap(
                                                                                                      2)))
                                                                        .addGap(
                                                                              0))
                                                            .addGroup(
                                                                  layout.createSequentialGroup()
                                                                        .addGroup(
                                                                              layout.createParallelGroup(
                                                                                    Alignment.LEADING)
                                                                                    .addComponent(
                                                                                          unitsInfo,
                                                                                          GroupLayout.DEFAULT_SIZE,
                                                                                          103,
                                                                                          Short.MAX_VALUE)
                                                                                    .addComponent(
                                                                                          unitsLabel,
                                                                                          Alignment.TRAILING,
                                                                                          GroupLayout.DEFAULT_SIZE,
                                                                                          103,
                                                                                          Short.MAX_VALUE))
                                                                        .addGap(
                                                                              23)
                                                                        .addGroup(
                                                                              layout.createParallelGroup(
                                                                                    Alignment.LEADING)
                                                                                    .addComponent(
                                                                                          fulfillsInfo,
                                                                                          GroupLayout.DEFAULT_SIZE,
                                                                                          129,
                                                                                          Short.MAX_VALUE)
                                                                                    .addComponent(
                                                                                          fulfillsLabel,
                                                                                          GroupLayout.DEFAULT_SIZE,
                                                                                          129,
                                                                                          Short.MAX_VALUE))
                                                                        .addPreferredGap(
                                                                              ComponentPlacement.RELATED)))
                                                .addGap(12)
                                                .addGroup(
                                                      layout.createParallelGroup(
                                                            Alignment.TRAILING)
                                                            .addComponent(
                                                                  removeButton)
                                                            .addComponent(
                                                                  addButton)))
                                    .addComponent(nameInfo)
                                    .addGroup(
                                          layout.createSequentialGroup()
                                                .addComponent(
                                                      preReqInfo,
                                                      GroupLayout.PREFERRED_SIZE,
                                                      234,
                                                      GroupLayout.PREFERRED_SIZE)
                                                .addGap(74)))
                        .addPreferredGap(ComponentPlacement.UNRELATED)
                        .addGroup(
                              layout.createParallelGroup(Alignment.LEADING)
                                    .addComponent(descriptionScroller,
                                          GroupLayout.DEFAULT_SIZE, 244,
                                          Short.MAX_VALUE)
                                    .addGroup(
                                          layout.createSequentialGroup()
                                                .addComponent(clearButton)
                                                .addPreferredGap(
                                                      ComponentPlacement.RELATED)
                                                .addComponent(dynamicUnits,
                                                      GroupLayout.DEFAULT_SIZE,
                                                      45, Short.MAX_VALUE)
                                                .addPreferredGap(
                                                      ComponentPlacement.RELATED)
                                                .addComponent(
                                                      requestedUnitsField,
                                                      GroupLayout.PREFERRED_SIZE,
                                                      30,
                                                      GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(
                                                      ComponentPlacement.RELATED)
                                                .addComponent(unitsSelecter)
                                                .addGap(11)
                                                .addComponent(suggestButton))
                                    .addComponent(suggestedLabel,
                                          GroupLayout.PREFERRED_SIZE, 196,
                                          GroupLayout.PREFERRED_SIZE)
                                    .addComponent(suggestedScroller,
                                          GroupLayout.DEFAULT_SIZE, 244,
                                          Short.MAX_VALUE)
                                    .addComponent(descriptionInfo))
                        .addContainerGap()));
      layout.setVerticalGroup(layout
            .createParallelGroup(Alignment.LEADING)
            .addGroup(
                  layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                              layout.createParallelGroup(Alignment.TRAILING)
                                    .addGroup(
                                          layout.createSequentialGroup()
                                                .addGroup(
                                                      layout.createParallelGroup(
                                                            Alignment.BASELINE)
                                                            .addComponent(
                                                                  passedLabel)
                                                            .addComponent(
                                                                  requiredLabel)
                                                            .addComponent(
                                                                  suggestedLabel))
                                                .addPreferredGap(
                                                      ComponentPlacement.RELATED)
                                                .addGroup(
                                                      layout.createParallelGroup(
                                                            Alignment.BASELINE)
                                                            .addComponent(
                                                                  requiredComboBox,
                                                                  GroupLayout.PREFERRED_SIZE,
                                                                  GroupLayout.DEFAULT_SIZE,
                                                                  GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(
                                                                  requiredFilter,
                                                                  GroupLayout.PREFERRED_SIZE,
                                                                  GroupLayout.DEFAULT_SIZE,
                                                                  GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(
                                                                  passedFilter,
                                                                  GroupLayout.PREFERRED_SIZE,
                                                                  GroupLayout.DEFAULT_SIZE,
                                                                  GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(
                                                      ComponentPlacement.RELATED)
                                                .addGroup(
                                                      layout.createParallelGroup(
                                                            Alignment.LEADING)
                                                            .addGroup(
                                                                  layout.createSequentialGroup()
                                                                        .addGroup(
                                                                              layout.createParallelGroup(
                                                                                    Alignment.TRAILING)
                                                                                    .addComponent(
                                                                                          requiredScroller,
                                                                                          GroupLayout.DEFAULT_SIZE,
                                                                                          243,
                                                                                          Short.MAX_VALUE)
                                                                                    .addGroup(
                                                                                          layout.createSequentialGroup()
                                                                                                .addGroup(
                                                                                                      layout.createParallelGroup(
                                                                                                            Alignment.TRAILING)
                                                                                                            .addGroup(
                                                                                                                  layout.createSequentialGroup()
                                                                                                                        .addComponent(
                                                                                                                              addButton,
                                                                                                                              GroupLayout.DEFAULT_SIZE,
                                                                                                                              118,
                                                                                                                              Short.MAX_VALUE)
                                                                                                                        .addPreferredGap(
                                                                                                                              ComponentPlacement.RELATED)
                                                                                                                        .addComponent(
                                                                                                                              removeButton,
                                                                                                                              GroupLayout.DEFAULT_SIZE,
                                                                                                                              119,
                                                                                                                              Short.MAX_VALUE))
                                                                                                            .addComponent(
                                                                                                                  suggestedScroller,
                                                                                                                  GroupLayout.DEFAULT_SIZE,
                                                                                                                  243,
                                                                                                                  Short.MAX_VALUE))
                                                                                                .addPreferredGap(
                                                                                                      ComponentPlacement.RELATED)))
                                                                        .addGroup(
                                                                              layout.createParallelGroup(
                                                                                    Alignment.LEADING)
                                                                                    .addGroup(
                                                                                          layout.createSequentialGroup()
                                                                                                .addGap(
                                                                                                      4)
                                                                                                .addComponent(
                                                                                                      clearButton))
                                                                                    .addGroup(
                                                                                          layout.createSequentialGroup()
                                                                                                .addPreferredGap(
                                                                                                      ComponentPlacement.RELATED)
                                                                                                .addGroup(
                                                                                                      layout.createParallelGroup(
                                                                                                            Alignment.BASELINE)
                                                                                                            .addComponent(
                                                                                                                  dynamicUnits)
                                                                                                            .addComponent(
                                                                                                                  requestedUnitsField,
                                                                                                                  GroupLayout.PREFERRED_SIZE,
                                                                                                                  GroupLayout.DEFAULT_SIZE,
                                                                                                                  GroupLayout.PREFERRED_SIZE)
                                                                                                            .addComponent(
                                                                                                                  unitsSelecter)
                                                                                                            .addComponent(
                                                                                                                  suggestButton))))
                                                                        .addGap(
                                                                              18)
                                                                        .addComponent(
                                                                              descriptionInfo)
                                                                        .addPreferredGap(
                                                                              ComponentPlacement.RELATED)
                                                                        .addGroup(
                                                                              layout.createParallelGroup(
                                                                                    Alignment.TRAILING)
                                                                                    .addGroup(
                                                                                          layout.createSequentialGroup()
                                                                                                .addComponent(
                                                                                                      nameLabel,
                                                                                                      GroupLayout.PREFERRED_SIZE,
                                                                                                      44,
                                                                                                      GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                      ComponentPlacement.RELATED)
                                                                                                .addGroup(
                                                                                                      layout.createParallelGroup(
                                                                                                            Alignment.BASELINE)
                                                                                                            .addComponent(
                                                                                                                  fulfillsInfo)
                                                                                                            .addComponent(
                                                                                                                  unitsInfo))
                                                                                                .addPreferredGap(
                                                                                                      ComponentPlacement.RELATED)
                                                                                                .addGroup(
                                                                                                      layout.createParallelGroup(
                                                                                                            Alignment.BASELINE,
                                                                                                            false)
                                                                                                            .addGroup(
                                                                                                                  layout.createSequentialGroup()
                                                                                                                        .addGap(
                                                                                                                              1)
                                                                                                                        .addComponent(
                                                                                                                              fulfillsLabel,
                                                                                                                              GroupLayout.PREFERRED_SIZE,
                                                                                                                              GroupLayout.DEFAULT_SIZE,
                                                                                                                              GroupLayout.PREFERRED_SIZE))
                                                                                                            .addComponent(
                                                                                                                  unitsLabel,
                                                                                                                  GroupLayout.PREFERRED_SIZE,
                                                                                                                  21,
                                                                                                                  GroupLayout.PREFERRED_SIZE))
                                                                                                .addPreferredGap(
                                                                                                      ComponentPlacement.RELATED)
                                                                                                .addComponent(
                                                                                                      preReqInfo)
                                                                                                .addPreferredGap(
                                                                                                      ComponentPlacement.RELATED)
                                                                                                .addComponent(
                                                                                                      prereqLabel,
                                                                                                      GroupLayout.PREFERRED_SIZE,
                                                                                                      41,
                                                                                                      GroupLayout.PREFERRED_SIZE))
                                                                                    .addGroup(
                                                                                          layout.createSequentialGroup()
                                                                                                .addComponent(
                                                                                                      descriptionScroller,
                                                                                                      GroupLayout.PREFERRED_SIZE,
                                                                                                      0,
                                                                                                      Short.MAX_VALUE)
                                                                                                .addGap(
                                                                                                      2))))
                                                            .addComponent(
                                                                  passedScroller,
                                                                  GroupLayout.DEFAULT_SIZE,
                                                                  470,
                                                                  Short.MAX_VALUE))
                                                .addContainerGap())
                                    .addGroup(
                                          layout.createSequentialGroup()
                                                .addGap(333)
                                                .addComponent(nameInfo)
                                                .addGap(180)))));
      layout.linkSize(SwingConstants.VERTICAL, new Component[] { nameLabel,
            prereqLabel });
      dynamicUnits.setHorizontalAlignment(SwingConstants.RIGHT);
      getContentPane().setLayout(layout);

      pack();
      setMinimumSize(getPreferredSize());
   }

   
   public SchedulerView()
   {

      passedFilter.addKeyListener(new KeyAdapter()
      {
         @Override
         public void keyReleased(KeyEvent e)
         {
            passedSorter.allRowsChanged();
         }
      });


      setDefaultCloseOperation(EXIT_ON_CLOSE);
      nameLabel.setEditable(false);
      nameLabel.setFocusable(false);
      fulfillsLabel.setEditable(false);
      fulfillsLabel.setFocusable(false);
      descriptionPane.setEditable(false);
      descriptionPane.setFocusable(false);
      unitsLabel.setEditable(false);
      unitsLabel.setFocusable(false);

      unitsLabel.setBackground(null);
      nameLabel.setBackground(null);
      fulfillsLabel.setBackground(null);
      prereqLabel.setBackground(null);
      descriptionPane.setBackground(null);
      descriptionScroller.setBackground(null);

      Border infoBoeder = new BevelBorder(BevelBorder.LOWERED);
      unitsLabel.setBorder(infoBoeder);
      nameLabel.setBorder(infoBoeder);
      fulfillsLabel.setBorder(infoBoeder);
      prereqLabel.setBorder(infoBoeder);

      fileMenu.setMnemonic(KeyEvent.VK_F);
      helpMenu.setMnemonic(KeyEvent.VK_H);
      openMenuItem.setMnemonic(KeyEvent.VK_O);
      saveMenuItem.setMnemonic(KeyEvent.VK_S);
      saveAsMenuItem.setMnemonic(KeyEvent.VK_A);
      quitMenuItem.setMnemonic(KeyEvent.VK_Q);

      openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
            InputEvent.CTRL_MASK));
      saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
            InputEvent.CTRL_MASK));
      saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
            InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK));
      quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
            InputEvent.CTRL_MASK));
      aboutMenuItem.setMnemonic(KeyEvent.VK_A);

      fileMenu.add(openMenuItem);
      fileMenu.add(saveMenuItem);
      fileMenu.add(saveAsMenuItem);
      fileMenu.add(quitMenuItem);
      helpMenu.add(aboutMenuItem);
      menuBar.add(fileMenu);
      menuBar.add(helpMenu);
      setJMenuBar(menuBar);
      layoutComponents();
      addButton.setActionCommand("Add Course");
      removeButton.setActionCommand("Remove Course");
      clearButton.setActionCommand("Clear Suggested");
      suggestButton.setActionCommand("Autofill");

   }

   public void setController(SchedulerController controller)
   {
      addButton.addActionListener(controller);
      removeButton.addActionListener(controller);
      clearButton.addActionListener(controller);
      suggestButton.addActionListener(controller);
      requiredList.addListSelectionListener(controller);
      suggestedList.addListSelectionListener(controller);
   }

   /**
    * Returns the Course that is selected in the "Passed" pane
    * 
    * @return Course that is selected in the "Passed" pane
    */
   public Course getSelectedPassed()
   {
      return (Course) passedTable.getValueAt(passedTable.getSelectedRow(), 1);
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
    * Returns the number of units in the units to take box
    * 
    * @return int the number of units in the units to take box
    */
   public int getScheduleUnits()
   {
      return Integer.parseInt(requestedUnitsField.getText());
   }

   //
   // /**
   // * Fills the "Info" section with the info of the parameter Course
   // *
   // * @param Course
   // * course to display
   // */
   public void setInfo(Course c)
   {
      nameLabel.setText(c.getName());
      unitsLabel.setText(Integer.toString(c.getUnits()));
      fulfillsLabel.setText("Need CourseOption");
      prereqLabel.setText(c.getPreRequisitesString());
      descriptionPane.setText(c.getDescription());

   }

   public void setSources(CourseTableModel tableModel, final CourseList coursesRequired, CourseList coursesSuggested)
   {
      passedSorter = new TableRowSorter<CourseTableModel>(tableModel);
      passedSorter.setRowFilter(new RowFilter<CourseTableModel, Integer>()
      {
         public boolean include(
               Entry<? extends CourseTableModel, ? extends Integer> entry)
         {

            return (entry.getStringValue(1).toLowerCase().contains(passedFilter
                  .getText().toLowerCase()));
         }
      });
      passedTable.setRowSorter(passedSorter);

      passedTable.setModel(tableModel);

      requiredList.setModel(coursesRequired);
      requiredFilter.addKeyListener(new KeyAdapter()
      {
         @Override
         public void keyReleased(KeyEvent e)
         {
            coursesRequired.filterList(requiredFilter.getText());
         }
     });

      suggestedList.setModel(coursesSuggested);
   }

}