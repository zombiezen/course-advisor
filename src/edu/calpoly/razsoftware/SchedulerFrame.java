package edu.calpoly.razsoftware;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import com.google.common.collect.ImmutableList;

/**
 * 
 * @author Daniel
 */
public class SchedulerFrame extends JFrame
{
    private class CourseListModel extends AbstractTableModel
    {
        private List<Course> sortedCatalog;

        public CourseListModel(CourseList clist)
        {
            sortedCatalog = ImmutableList.copyOf(clist.getCatalog());
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
                    return String.class;
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

            final Course requestedCourse = sortedCatalog.get(rowIndex);

            switch (columnIndex)
            {
                case 0:
                    return state.getTaken().contains(requestedCourse);
                case 1:
                    return requestedCourse.toString();
                default:
                    return null;
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex)
        {
            if (rowIndex > sortedCatalog.size())
                return;

            final Course requestedCourse = sortedCatalog.get(rowIndex);

            switch (columnIndex)
            {
                case 0:

                    setSaved(false);
                    setTitle();
                    if ((Boolean) aValue)
                    {
                        state.getTaken().add(requestedCourse);
                    }
                    else
                    {
                        state.getTaken().remove(requestedCourse);
                    }
                    generateLists();
                    break;
            }
        }
    }

    private boolean                 saved               = false;
    private CourseList              list;
    private UserState               state               = new UserState();
    // private CourseDecider.Result result;
    private CourseDecider           decider             = new CourseDecider();
    private Flowchart               flowchart;
    private File                    userStateFile;

    private static final String     FILE_EXTENSION      = ".us";
    private static final String     APP_NAME            = "Course Advisor";

    private JMenuBar                menuBar             = new JMenuBar();
    private JMenu                   fileMenu            = new JMenu("File");
    private JMenuItem               openMenuItem        = new JMenuItem("Open");
    private JMenuItem               saveMenuItem        = new JMenuItem("Save");
    private JMenuItem               saveAsMenuItem      = new JMenuItem(
                                                                "Save As");
    private JMenuItem               quitMenuItem        = new JMenuItem("Quit");
    private JMenu                   helpMenu            = new JMenu("Help");
    private JLabel                  passedLabel         =
                                                                new JLabel(
                                                                        "Courses Passed",
                                                                        SwingConstants.CENTER);
    private JTextField              passedFilter        = new JTextField();

    private CourseListModel         passedModel;

    TableRowSorter<CourseListModel> passedSorter;
    private JTable                  passedTable         = new JTable(
                                                                passedModel);
    private JScrollPane             passedScroller      = new JScrollPane(
                                                                passedTable);
    private JLabel                  requiredLabel       =
                                                                new JLabel(
                                                                        "Courses Required",
                                                                        SwingConstants.CENTER);
    private final String            ALL_FILTER          = "All";
    private final String            PREREQ_MET          = "Prerequisites Met";
    private final String            PREREQ__NOT_MET     =
                                                                "Prerequisite Not Met";
    private JComboBox               requiredComboBox    = new JComboBox(
                                                                new String[] {
            ALL_FILTER, PREREQ_MET, PREREQ__NOT_MET            });
    private JTextField              requiredFilter      = new JTextField();
    DefaultListModel                requiredModel       =
                                                                new DefaultListModel();
    private JList                   requiredList        = new JList(
                                                                requiredModel);
    private JScrollPane             requiredScroller    = new JScrollPane(
                                                                requiredList);
    private static final String     UNITS               = "Units";
    private JLabel                  unitsSelecter       = new JLabel(UNITS);
    private JTextField              requestedUnitsField = new JTextField("16");
    private JButton                 addButton           = new JButton(">");
    private JButton                 removeButton        = new JButton("<");

    JButton                         clearButton         = new JButton("Clear");

    private final JButton           suggestButton       = new JButton(
                                                                "Autofill");
    private JLabel                  suggestedLabel      =
                                                                new JLabel(
                                                                        "Suggested Schedule",
                                                                        SwingConstants.CENTER);
    DefaultListModel                suggestedModel      =
                                                                new DefaultListModel();
    private JList                   suggestedList       = new JList(
                                                                suggestedModel);
    private JScrollPane             suggestedScroller   = new JScrollPane(
                                                                suggestedList);
    private JLabel                  dynamicUnits        = new JLabel("0/");
    private JLabel                  nameInfo            = new JLabel("Name:");
    private final JLabel            unitsInfo           = new JLabel("Units:");
    private JLabel                  fulfillsInfo        = new JLabel(
                                                                "Fulfills:");
    private final JLabel            preReqInfo          =
                                                                new JLabel(
                                                                        "Prerequisites:");
    private JLabel                  descriptionInfo     = new JLabel(
                                                                "Description:");
    private final JTextPane         nameLabel           = new JTextPane();
    private final JTextPane         unitsLabel          = new JTextPane();
    private final JTextPane         fuffilsLabel        = new JTextPane();
    private final JTextPane         prereqLabel         = new JTextPane();
    private JTextPane               descriptionPane     = new JTextPane();
    private JScrollPane             descriptionScroller =
                                                                new JScrollPane(
                                                                        descriptionPane);

    void loadCourseList()
    {
        System.out.println(getClass().getResourceAsStream("Cat.json"));
        list = new CourseList(getClass().getResourceAsStream("Cat.json"));

        passedModel = new CourseListModel(list);
        passedTable.setModel(passedModel);

        // for(Course c:list.getCatalog()){
        // System.out.println(c+" "+c.getUnits());
        //
        // }
        flowchart =
                FlowchartReader.readFlowchart(
                        getClass().getResourceAsStream("FlowChart.json"), list);
        generateLists();

    }

    public void setTitle()
    {
        String title = APP_NAME;
        if (userStateFile != null)
        {
            title +="- "+ userStateFile.getName();
        }
        if (!saved)
        {
            title += "*";
        }

        super.setTitle(title);

    }

    /** Creates new form SchedulerFrame */
    public SchedulerFrame()
    {

        setSaved(false);
        prereqLabel.setFocusable(false);
        prereqLabel.setEditable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        loadCourseList();
        initComponents();
    }

    @Override
    public void dispose()
    {
        if (!saved)
        {
            int choice =
                    JOptionPane.showConfirmDialog(this,
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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */

    private void initComponents()
    {
        passedTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        passedTable.getTableHeader().setReorderingAllowed(false);
        passedTable.getTableHeader().setResizingAllowed(false);

        passedSorter = new TableRowSorter<CourseListModel>(passedModel);
        passedSorter.setRowFilter(new RowFilter<CourseListModel, Integer>()
        {
            public boolean include(
                    Entry<? extends CourseListModel, ? extends Integer> entry)
            {

                return (entry.getStringValue(1).toLowerCase()
                        .contains(passedFilter.getText().toLowerCase()));
            }
        });

        passedTable.setRowSorter(passedSorter);

        passedFilter.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                passedSorter.allRowsChanged();
            }
        });
        // generateButton.addActionListener(new ActionListener()
        // {
        // public void actionPerformed(ActionEvent e)
        // {
        // generateLists();
        // }
        // });

        requiredComboBox.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                requiredFilter.setText("");
                updateRequiredList();
            }
        });
        requiredFilter.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                System.out.println("ACTION");
                updateRequiredList();
            }
        });

        ListSelectionListener l = new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                if (e.getSource() instanceof JList)
                {
                    JList selectedList = (JList) e.getSource();
                    if (selectedList.getSelectedValue() instanceof Course)
                    {
                        Course selected =
                                (Course) selectedList.getSelectedValue();
                        CourseOption option = null;
                        for (CourseOption o : CourseOptions)
                        {
                            if (o.getOptions().contains(selected))
                            {
                                option = o;
                            }
                        }
                        updateInfo(selected, option);
                    }
                }
            }
        };

        nameLabel.setEditable(false);
        nameLabel.setFocusable(false);
        fuffilsLabel.setEditable(false);
        fuffilsLabel.setFocusable(false);
        descriptionPane.setFocusable(false);
        descriptionPane.setEditable(true);

        unitsLabel.setBackground(null);
        nameLabel.setBackground(null);
        fuffilsLabel.setBackground(null);
        prereqLabel.setBackground(null);
        descriptionPane.setBackground(null);
        descriptionScroller.setBackground(null);

        unitsLabel.setBorder(descriptionScroller.getBorder());
        nameLabel.setBorder(descriptionScroller.getBorder());
        fuffilsLabel.setBorder(descriptionScroller.getBorder());
        prereqLabel.setBorder(descriptionScroller.getBorder());

        requiredList.addListSelectionListener(l);
        suggestedList.addListSelectionListener(l);
        requiredList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        suggestedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        requestedUnitsField.addKeyListener(new KeyAdapter()
        {

            @Override
            public void keyTyped(KeyEvent keyevent)
            {
                if (!Character.isDigit(keyevent.getKeyChar()))
                {
                    keyevent.consume();
                }

            }

        });

        addButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                addButton();
            }
        });

        removeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                removeButton();
            }
        });

        suggestButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                suggestSchedule();
            }
        });

        openMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                openUserState();
            }
        });
        saveMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                saveUserState();
            }
        });
        saveAsMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                saveAsUserState();
            }
        });
        clearButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent actionevent)
            {
                clearSuggested();

            }
        });
        quitMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });
        helpMenu.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent actionevent)
            {
//                JOptionPane.showMessageDialog(this, obj, s, i)
//                JOptionPane.showMessageDialog(this, ,"About", JOptionPane.PLAIN_MESSAGE);
                
            }
        });

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

        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(quitMenuItem);
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        clearButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent actionevent)
            {}
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setHorizontalGroup(layout
                .createParallelGroup(Alignment.LEADING)
                .addGroup(
                        layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(
                                        layout.createParallelGroup(
                                                Alignment.LEADING)
                                                .addComponent(
                                                        passedLabel,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        156,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addComponent(
                                                        passedFilter,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        156,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addComponent(
                                                        passedScroller,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        156,
                                                        GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addGroup(
                                        layout.createParallelGroup(
                                                Alignment.LEADING)
                                                .addGroup(
                                                        layout.createSequentialGroup()
                                                                .addGroup(
                                                                        layout.createParallelGroup(
                                                                                Alignment.LEADING)
                                                                                .addGroup(
                                                                                        layout.createSequentialGroup()
                                                                                                .addComponent(
                                                                                                        preReqInfo,
                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                        38,
                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                        ComponentPlacement.RELATED))
                                                                                .addGroup(
                                                                                        Alignment.TRAILING,
                                                                                        layout.createSequentialGroup()
                                                                                                .addComponent(
                                                                                                        nameLabel,
                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                        245,
                                                                                                        Short.MAX_VALUE)
                                                                                                .addPreferredGap(
                                                                                                        ComponentPlacement.RELATED))
                                                                                .addGroup(
                                                                                        Alignment.TRAILING,
                                                                                        layout.createSequentialGroup()
                                                                                                .addComponent(
                                                                                                        prereqLabel,
                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                        245,
                                                                                                        Short.MAX_VALUE)
                                                                                                .addPreferredGap(
                                                                                                        ComponentPlacement.RELATED))
                                                                                .addGroup(
                                                                                        Alignment.TRAILING,
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
                                                                                                                                                                        95,
                                                                                                                                                                        Short.MAX_VALUE)
                                                                                                                                                                .addGap(1))
                                                                                                                                                .addComponent(
                                                                                                                                                        requiredLabel,
                                                                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                                                                        239,
                                                                                                                                                        Short.MAX_VALUE))
                                                                                                                                .addGap(6))
                                                                                                                .addGroup(
                                                                                                                        layout.createSequentialGroup()
                                                                                                                                .addComponent(
                                                                                                                                        requiredScroller,
                                                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                                                        243,
                                                                                                                                        Short.MAX_VALUE)
                                                                                                                                .addGap(2)))
                                                                                                .addGap(0))
                                                                                .addGroup(
                                                                                        Alignment.TRAILING,
                                                                                        layout.createSequentialGroup()
                                                                                                .addGroup(
                                                                                                        layout.createParallelGroup(
                                                                                                                Alignment.LEADING)
                                                                                                                .addComponent(
                                                                                                                        unitsInfo,
                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                        29,
                                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(
                                                                                                                        unitsLabel,
                                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                                        98,
                                                                                                                        Short.MAX_VALUE))
                                                                                                .addGap(23)
                                                                                                .addGroup(
                                                                                                        layout.createParallelGroup(
                                                                                                                Alignment.LEADING)
                                                                                                                .addComponent(
                                                                                                                        fulfillsInfo)
                                                                                                                .addComponent(
                                                                                                                        fuffilsLabel,
                                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                                        124,
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
                                                .addComponent(nameInfo))
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addGroup(
                                        layout.createParallelGroup(
                                                Alignment.TRAILING)
                                                .addComponent(
                                                        descriptionScroller,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        244, Short.MAX_VALUE)
                                                .addGroup(
                                                        Alignment.LEADING,
                                                        layout.createSequentialGroup()
                                                                .addComponent(
                                                                        clearButton)
                                                                .addPreferredGap(
                                                                        ComponentPlacement.RELATED)
                                                                .addComponent(
                                                                        dynamicUnits,
                                                                        GroupLayout.DEFAULT_SIZE,
                                                                        39,
                                                                        Short.MAX_VALUE)
                                                                .addPreferredGap(
                                                                        ComponentPlacement.RELATED)
                                                                .addComponent(
                                                                        requestedUnitsField,
                                                                        GroupLayout.PREFERRED_SIZE,
                                                                        30,
                                                                        GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        ComponentPlacement.RELATED)
                                                                .addComponent(
                                                                        unitsSelecter)
                                                                .addGap(11)
                                                                .addComponent(
                                                                        suggestButton))
                                                .addComponent(
                                                        suggestedLabel,
                                                        Alignment.LEADING,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        196,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addComponent(
                                                        suggestedScroller,
                                                        Alignment.LEADING,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        244, Short.MAX_VALUE)
                                                .addComponent(descriptionInfo,
                                                        Alignment.LEADING))
                                .addContainerGap()));
        layout.setVerticalGroup(layout
                .createParallelGroup(Alignment.LEADING)
                .addGroup(
                        layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(
                                        layout.createParallelGroup(
                                                Alignment.LEADING)
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
                                                                                Alignment.TRAILING)
                                                                                .addGroup(
                                                                                        layout.createSequentialGroup()
                                                                                                .addGroup(
                                                                                                        layout.createParallelGroup(
                                                                                                                Alignment.TRAILING)
                                                                                                                .addComponent(
                                                                                                                        requiredScroller,
                                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                                        242,
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
                                                                                                                                                                        118,
                                                                                                                                                                        Short.MAX_VALUE))
                                                                                                                                                .addComponent(
                                                                                                                                                        suggestedScroller,
                                                                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                                                                        242,
                                                                                                                                                        Short.MAX_VALUE))
                                                                                                                                .addPreferredGap(
                                                                                                                                        ComponentPlacement.RELATED)))
                                                                                                .addGroup(
                                                                                                        layout.createParallelGroup(
                                                                                                                Alignment.LEADING)
                                                                                                                .addGroup(
                                                                                                                        layout.createSequentialGroup()
                                                                                                                                .addGap(4)
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
                                                                                                .addGap(36)
                                                                                                .addGroup(
                                                                                                        layout.createParallelGroup(
                                                                                                                Alignment.TRAILING,
                                                                                                                false)
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
                                                                                                                                                                .addGap(1)
                                                                                                                                                                .addComponent(
                                                                                                                                                                        fuffilsLabel,
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
                                                                                                                                        descriptionScroller)
                                                                                                                                .addGap(2))))
                                                                                .addComponent(
                                                                                        passedScroller,
                                                                                        Alignment.LEADING,
                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                        458,
                                                                                        Short.MAX_VALUE))
                                                                .addContainerGap())
                                                .addGroup(
                                                        Alignment.TRAILING,
                                                        layout.createSequentialGroup()
                                                                .addGroup(
                                                                        layout.createParallelGroup(
                                                                                Alignment.BASELINE)
                                                                                .addComponent(
                                                                                        descriptionInfo)
                                                                                .addComponent(
                                                                                        nameInfo))
                                                                .addGap(180)))));
        layout.linkSize(SwingConstants.VERTICAL, new Component[] { nameLabel,
                prereqLabel });
        dynamicUnits.setHorizontalAlignment(SwingConstants.RIGHT);
        getContentPane().setLayout(layout);

        pack();
        setMinimumSize(getPreferredSize());

    }

    private void addButton()
    {
        Course c = (Course) requiredList.getSelectedValue();
        if (c != null)
        {
            int choice = JOptionPane.YES_OPTION;
            if (noMetArray.contains(c))
            {
                // TODO
                choice =
                        JOptionPane
                                .showConfirmDialog(
                                        this,
                                        "The prerequisites are not fulfilled for this course\n are you sure you want to add?",
                                        "Prerequisites not met",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.QUESTION_MESSAGE);
                // JOptionPane.showMessageDialog(this, "NO",
                // "The prereqs are not fulifled for this course",
                // JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE);

            }
            if (choice == JOptionPane.YES_OPTION)
            {
                suggestedModel.addElement(requiredList.getSelectedValue());
                requiredModel.removeElement(requiredList.getSelectedValue());
                updateUnitCount();
                updateInfo(null, null);
            }

        }
    }

    private void removeButton()
    {
        if (suggestedList.getSelectedValue() != null)
        {
            requiredModel.addElement(suggestedList.getSelectedValue());
            suggestedModel.removeElement(suggestedList.getSelectedValue());
            updateUnitCount();
            updateInfo(null, null);
        }
    }

    private int getSuggestedUnits()
    {

        int unitCount = 0;

        for (int i = 0; i < suggestedModel.getSize(); i++)
        {
            if (suggestedModel.get(i) instanceof Course)
            {
                unitCount += ((Course) suggestedModel.get(i)).getUnits();
            }
        }
        return unitCount;
    }

    private void updateUnitCount()
    {
        dynamicUnits.setText(getSuggestedUnits() + "/");

    }

    private void updateRequiredList()
    {
        Set<Course> courses;
        if (requiredComboBox.getSelectedItem().equals(ALL_FILTER))
        {
            courses = new TreeSet<Course>(both);
        }
        else if (requiredComboBox.getSelectedItem().equals(PREREQ_MET))
        {
            courses = new TreeSet<Course>(metArray);
        }
        else if (requiredComboBox.getSelectedItem().equals(PREREQ__NOT_MET))
        {
            courses = new TreeSet<Course>(noMetArray);
        }
        else
        {
            return;
        }
        requiredModel.clear();
        for (Course c : courses)
        {
            if (c.toString().toLowerCase()
                    .contains(requiredFilter.getText().toLowerCase()))
            {
                if (!suggestedModel.contains(c))
                {
                    requiredModel.addElement(c);
                }
            }
        }

    }

    Set<Course>       both       = new TreeSet<Course>();
    Set<Course>       metArray   = new TreeSet<Course>();
    Set<Course>       noMetArray = new TreeSet<Course>();
    Set<CourseOption> CourseOptions;

    private void generateLists()
    {
        updateInfo(null, null);
        CourseOptions = decider.decideClasses(state, flowchart);

        metArray.clear();
        noMetArray.clear();
        for (CourseOption o : CourseOptions)
        {
            for (Course c : o.getOptions())
            {
                if (!state.getTaken().contains(c))
                {
                    if (c.preRecsMet(state.getTaken()))
                    {
                        metArray.add(c);
                    }
                    else
                    {
                        noMetArray.add(c);
                    }
                }
            }
        }
        both = new TreeSet<Course>();
        both.addAll(metArray);
        both.addAll(noMetArray);
        updateRequiredList();
        requiredComboBox.setSelectedIndex(0);
        requiredFilter.setText("");
        suggestedModel.clear();
        updateUnitCount();

    }

    private void updateInfo(Course selCourse, CourseOption option)
    {
        if (selCourse == null && option == null)
        {
            descriptionPane.setText("");
            nameLabel.setText("");
            fuffilsLabel.setText("");
            prereqLabel.setText("");
            unitsLabel.setText("");
            return;

        }
        descriptionPane.setText(selCourse.getDescription());
        nameLabel.setText(selCourse.getName());
        fuffilsLabel.setText(option.getRequirement());
        unitsLabel.setText(selCourse.getUnits() + " Units");
        if (selCourse.preRecsMet(state.getTaken()))
        {
            prereqLabel.setText("All of the prerequisites are met");
        }
        else
        {
            prereqLabel.setText(selCourse.getPreRequisitesString());

        }
        descriptionPane.setCaretPosition(0);

    }

    FileFilter saveLoadFilter = new FileFilter()
                              {
                                  public boolean accept(File f)
                                  {
                                      return (f.getName().endsWith(
                                              FILE_EXTENSION) || f
                                              .isDirectory());
                                  }

                                  public String getDescription()
                                  {
                                      return "User State (" + FILE_EXTENSION
                                              + ")";
                                  }
                              };

    private void openUserState()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(saveLoadFilter);
        int choice = chooser.showOpenDialog(this);
        if (choice == JFileChooser.APPROVE_OPTION)
        {
            System.out.println("load" + chooser.getSelectedFile());
            try
            {
                state = new UserState(chooser.getSelectedFile(), list);
                userStateFile = chooser.getSelectedFile();
                passedModel.fireTableDataChanged();
                generateLists();
                setSaved(true);
            }
            catch (IOException ex)
            {
                Logger.getLogger(SchedulerFrame.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        else if (choice == JFileChooser.CANCEL_OPTION)
        {
        }
        else
        {
        }
    }

    public void setSaved(boolean saved)
    {
        this.saved = saved;
        setTitle();

    }

    private void saveAsUserState()
    {
        System.out.println("Saving");
        System.out.println(userStateFile);
        JFileChooser chooser = new JFileChooser()
        {
            @Override
            public void approveSelection()
            {
                File SelectedFile = getSelectedFile();

                if (!SelectedFile.getName().endsWith(FILE_EXTENSION))
                {
                    try
                    {
                        SelectedFile =
                                new File(SelectedFile.getCanonicalPath()
                                        + FILE_EXTENSION);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }

                if (SelectedFile.exists()
                        && !SelectedFile.equals(userStateFile))
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
                        userStateFile = SelectedFile;
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
                    userStateFile = SelectedFile;
                    super.approveSelection();
                }

            }
        };
        chooser.setFileFilter(saveLoadFilter);
        chooser.setSelectedFile(userStateFile);
        int choice = chooser.showSaveDialog(this);
        if (choice == JFileChooser.APPROVE_OPTION)
        {
            saveUserState();
        }
    }

    private void clearSuggested()
    {

        suggestedModel.removeAllElements();
        updateUnitCount();
        updateRequiredList();

    }

    private void suggestSchedule()
    {
        if (CourseOptions == null || CourseOptions.size() == 0)
        {
            return;
        }
        System.out.println(requestedUnitsField.getText());
        int maxUnits = Integer.parseInt(requestedUnitsField.getText());

        int unitCount = getSuggestedUnits();
        ArrayList<CourseOption> list =
                new ArrayList<CourseOption>(CourseOptions);

        Collections.sort(list);
        // List<Course> suggested = new ArrayList<Course>();

        for (CourseOption co : list)
        {
            if (unitCount > maxUnits)
            {
                break;
            }
            System.out.println(co.getRequirement() + " " + co.getQuarter());

            for (Course c : co.getOptions())
            {

                if (c.preRecsMet(state.getTaken())
                        && !suggestedModel.contains(c)
                        && unitCount + c.getUnits() <= maxUnits)
                {
                    suggestedModel.add(suggestedModel.getSize(), c);
                    unitCount += c.getUnits();
                    break;
                }

            }
        }
        updateUnitCount();
    }

    private void saveUserState()
    {
        if (userStateFile == null)
        {
            saveAsUserState();

        }
        try
        {
            state.write(userStateFile);

            setSaved(true);
            setTitle();
        }
        catch (IOException ex)
        {
            Logger.getLogger(SchedulerFrame.class.getName()).log(Level.SEVERE,
                    null, ex);
        }

    }
}
