package edu.calpoly.razsoftware;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * 
 * @author Daniel
 */
public class SchedulerFrame extends JFrame {
    private static final String       FILE_EXTENSION      = ".us";

    private JMenuBar                  menuBar             = new JMenuBar();
    private JMenu                     fileMenu            = new JMenu("File");
    private JMenuItem                 openMenuItem        = new JMenuItem("Open");
    private JMenuItem                 saveMenuItem        = new JMenuItem("Save");
    private JMenuItem                 saveAsMenuItem      = new JMenuItem("Save As");
    private JMenuItem                 quitMenuItem        = new JMenuItem("Quit");
    private JMenu                     helpMenu            = new JMenu("Help");
    private JLabel                    passedLabel         = new JLabel("Courses Passed",
	                                                          SwingConstants.CENTER);
    private JTextField                passedFilter        = new JTextField();
    private DefaultTableModel         passedModel         = new DefaultTableModel(new String[] {
	    "Passed", "Course"                           }, 0) {
	                                                      @Override
	                                                      public Class getColumnClass(int c) {
		                                                  return getValueAt(0, c)
		                                                          .getClass();
	                                                      }
	                                                  };

    TableRowSorter<DefaultTableModel> passedSorter        = new TableRowSorter<DefaultTableModel>(
	                                                          passedModel);
    private JTable                    passedTable         = new JTable(passedModel);
    private JScrollPane               passedScroller      = new JScrollPane(passedTable);
    private JCheckBox                 uscpBox             = new JCheckBox("USCP Met");
    private JCheckBox                 gwrBox              = new JCheckBox("GWR Met");
    private JButton                   generateButton      = new JButton("Generate");
    private JLabel                    requiredLabel       = new JLabel("Courses Required",
	                                                          SwingConstants.CENTER);
    private final String              ALL_FILTER          = "All";
    private final String              PREREQ_MET          = "Pre-Reqs Met";
    private final String              PREREQ__NOT_MET     = "Pre-Reqs Not Met";
    private JComboBox                 requiredComboBox    = new JComboBox(new String[] {
	    ALL_FILTER, PREREQ_MET, PREREQ__NOT_MET      });
    private JTextField                requiredFilter      = new JTextField();
    DefaultListModel                  requiredModel       = new DefaultListModel();
    private JList                     requiredList        = new JList(requiredModel);
    private JScrollPane               requiredScroller    = new JScrollPane(requiredList);
    private static final String       UNITS               = "Units";
    private JLabel                    unitsSelecter       = new JLabel(UNITS);
    private JTextField                unitsField          = new JTextField("TF3");
    private JButton                   addButton           = new JButton(">");
    private JButton                   removeButton        = new JButton("<");
    private final JButton             suggestButton       = new JButton("Suggest Schedule");
    private JLabel                    suggestedLabel      = new JLabel("Suggested Schedule",
	                                                          SwingConstants.CENTER);
    DefaultListModel                  suggestedModel      = new DefaultListModel();
    private JList                     suggestedList       = new JList(suggestedModel);
    private JScrollPane               suggestedScroller   = new JScrollPane(suggestedList);
    private JLabel                    dynamicUnits        = new JLabel("0 " + UNITS);
    private JTextPane                 descriptionPane     = new JTextPane();
    private JScrollPane               descriptionScroller = new JScrollPane(descriptionPane);
    private JLabel                    nameInfo            = new JLabel("Name:");
    private JLabel                    fuffilsInfo         = new JLabel("Pre Req");
    JTextField                        nameLabel           = new JTextField();
    JTextField                        FuffilsLabel        = new JTextField();
    private JLabel                    descriptionInfo     = new JLabel("Description");

    private CourseList                list;
    private UserState                 state               = new UserState();
    private CourseDecider.Result      result;
    private CourseDecider             decider             = new CourseDecider();
    private Flowchart                 flowchart;
    private File                      userStateFile       = null;

    private void loadCourseList() {
	System.out.println(getClass().getResourceAsStream("Cat.json"));
	list = new CourseList(getClass().getResourceAsStream("Cat.json"));
	// list = new CourseList(new File(
	// "C:\\Users\\Daniel\\workspace\\trunk\\src\\edu\\calpoly\\razsoftware\\Cat.json"));
	for (Course c : list.getCatalog()) {
	    passedModel.addRow(new Object[] { Boolean.FALSE, c });
	}

	passedSorter.allRowsChanged();

    }

    /** Creates new form SchedulerFrame */
    public SchedulerFrame() {
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	loadCourseList();
	initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */

    private void initComponents() {
	passedTable.getColumnModel().getColumn(0).setPreferredWidth(40);
	passedTable.getTableHeader().setReorderingAllowed(false);
	passedTable.getTableHeader().setResizingAllowed(false);

	passedSorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
	    public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {

		return (entry.getStringValue(1).toLowerCase().contains(passedFilter.getText()
		        .toLowerCase()));
	    }
	});

	passedTable.setRowSorter(passedSorter);

	passedFilter.addKeyListener(new KeyAdapter() {
	    @Override
	    public void keyReleased(KeyEvent e) {
		passedSorter.allRowsChanged();
	    }
	});
	generateButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		generateLists();
	    }
	});

	requiredComboBox.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		requiredFilter.setText("");
		updateRequiredList();
	    }
	});
	requiredFilter.addKeyListener(new KeyAdapter() {
	    @Override
	    public void keyReleased(KeyEvent e) {
		System.out.println("ACTION");
		updateRequiredList();
	    }
	});

	ListSelectionListener l = new ListSelectionListener() {
	    public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() instanceof JList) {
		    JList selectedList = (JList) e.getSource();
		    if (selectedList.getSelectedValue() instanceof Course) {
			updateInfo((Course) selectedList.getSelectedValue());
		    }
		}
	    }
	};

	requiredList.addListSelectionListener(l);
	suggestedList.addListSelectionListener(l);
	requiredList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	suggestedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	addButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		addButton();
	    }
	});

	removeButton.addActionListener(new ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		removeButton();
	    }
	});

	suggestButton.addActionListener(new ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		suggestSchedule();
	    }
	});

	openMenuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		openUserState();
	    }
	});
	saveMenuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		saveUserState();
	    }
	});
	saveAsMenuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		saveAsUserState();
	    }
	});
	quitMenuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		System.exit(0);
	    }
	});

	fileMenu.setMnemonic(KeyEvent.VK_F);
	helpMenu.setMnemonic(KeyEvent.VK_H);
	openMenuItem.setMnemonic(KeyEvent.VK_O);
	saveMenuItem.setMnemonic(KeyEvent.VK_S);
	saveAsMenuItem.setMnemonic(KeyEvent.VK_A);

	quitMenuItem.setMnemonic(KeyEvent.VK_Q);

	openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
	saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
	saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.SHIFT_MASK
	        | InputEvent.CTRL_MASK));
	quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));

	fileMenu.add(openMenuItem);
	fileMenu.add(saveMenuItem);
	fileMenu.add(saveAsMenuItem);
	fileMenu.add(quitMenuItem);
	menuBar.add(fileMenu);
	menuBar.add(helpMenu);
	setJMenuBar(menuBar);

	GroupLayout layout = new GroupLayout(getContentPane());
	layout.setHorizontalGroup(layout
	        .createParallelGroup(Alignment.LEADING)
	        .addGroup(
	                layout.createSequentialGroup()
	                        .addContainerGap()
	                        .addGroup(
	                                layout.createParallelGroup(Alignment.LEADING)
	                                        .addGroup(
	                                                layout.createSequentialGroup()
	                                                        .addGroup(
	                                                                layout.createParallelGroup(
	                                                                        Alignment.LEADING,
	                                                                        false)
	                                                                        .addComponent(
	                                                                                passedScroller,
	                                                                                GroupLayout.PREFERRED_SIZE,
	                                                                                156,
	                                                                                GroupLayout.PREFERRED_SIZE)
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
	                                                                        .addGroup(
	                                                                                layout.createSequentialGroup()
	                                                                                        .addComponent(
	                                                                                                uscpBox)
	                                                                                        .addPreferredGap(
	                                                                                                ComponentPlacement.UNRELATED)
	                                                                                        .addComponent(
	                                                                                                generateButton)))
	                                                        .addPreferredGap(
	                                                                ComponentPlacement.UNRELATED)
	                                                        .addGroup(
	                                                                layout.createParallelGroup(
	                                                                        Alignment.LEADING)
	                                                                        .addGroup(
	                                                                                layout.createSequentialGroup()
	                                                                                        .addComponent(
	                                                                                                FuffilsLabel,
	                                                                                                GroupLayout.DEFAULT_SIZE,
	                                                                                                241,
	                                                                                                Short.MAX_VALUE)
	                                                                                        .addPreferredGap(
	                                                                                                ComponentPlacement.RELATED))
	                                                                        .addGroup(
	                                                                                layout.createSequentialGroup()
	                                                                                        .addComponent(
	                                                                                                fuffilsInfo)
	                                                                                        .addPreferredGap(
	                                                                                                ComponentPlacement.RELATED))
	                                                                        .addGroup(
	                                                                                layout.createSequentialGroup()
	                                                                                        .addComponent(
	                                                                                                nameLabel,
	                                                                                                GroupLayout.DEFAULT_SIZE,
	                                                                                                241,
	                                                                                                Short.MAX_VALUE)
	                                                                                        .addPreferredGap(
	                                                                                                ComponentPlacement.RELATED))
	                                                                        .addGroup(
	                                                                                layout.createSequentialGroup()
	                                                                                        .addComponent(
	                                                                                                nameInfo)
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
	                                                                                                                                unitsField,
	                                                                                                                                GroupLayout.PREFERRED_SIZE,
	                                                                                                                                30,
	                                                                                                                                GroupLayout.PREFERRED_SIZE)
	                                                                                                                        .addPreferredGap(
	                                                                                                                                ComponentPlacement.RELATED)
	                                                                                                                        .addComponent(
	                                                                                                                                unitsSelecter)
	                                                                                                                        .addPreferredGap(
	                                                                                                                                ComponentPlacement.RELATED)
	                                                                                                                        .addComponent(
	                                                                                                                                suggestButton)
	                                                                                                                        .addGap(2))
	                                                                                                        .addGroup(
	                                                                                                                layout.createSequentialGroup()
	                                                                                                                        .addComponent(
	                                                                                                                                requiredScroller,
	                                                                                                                                GroupLayout.DEFAULT_SIZE,
	                                                                                                                                243,
	                                                                                                                                Short.MAX_VALUE)
	                                                                                                                        .addGap(2)))
	                                                                                        .addGap(0)))
	                                                        .addGroup(
	                                                                layout.createParallelGroup(
	                                                                        Alignment.TRAILING)
	                                                                        .addGroup(
	                                                                                layout.createSequentialGroup()
	                                                                                        .addGroup(
	                                                                                                layout.createParallelGroup(
	                                                                                                        Alignment.TRAILING)
	                                                                                                        .addComponent(
	                                                                                                                removeButton)
	                                                                                                        .addComponent(
	                                                                                                                descriptionInfo)
	                                                                                                        .addComponent(
	                                                                                                                addButton))
	                                                                                        .addPreferredGap(
	                                                                                                ComponentPlacement.UNRELATED)
	                                                                                        .addGroup(
	                                                                                                layout.createParallelGroup(
	                                                                                                        Alignment.LEADING)
	                                                                                                        .addComponent(
	                                                                                                                suggestedLabel,
	                                                                                                                GroupLayout.PREFERRED_SIZE,
	                                                                                                                196,
	                                                                                                                GroupLayout.PREFERRED_SIZE)
	                                                                                                        .addComponent(
	                                                                                                                descriptionScroller,
	                                                                                                                Alignment.TRAILING,
	                                                                                                                GroupLayout.DEFAULT_SIZE,
	                                                                                                                230,
	                                                                                                                Short.MAX_VALUE)
	                                                                                                        .addComponent(
	                                                                                                                suggestedScroller,
	                                                                                                                GroupLayout.DEFAULT_SIZE,
	                                                                                                                230,
	                                                                                                                Short.MAX_VALUE)))
	                                                                        .addComponent(
	                                                                                dynamicUnits)))
	                                        .addComponent(gwrBox)).addContainerGap()));
	layout.setVerticalGroup(layout
	        .createParallelGroup(Alignment.LEADING)
	        .addGroup(
	                layout.createSequentialGroup()
	                        .addContainerGap()
	                        .addGroup(
	                                layout.createParallelGroup(Alignment.BASELINE)
	                                        .addComponent(passedLabel)
	                                        .addComponent(requiredLabel)
	                                        .addComponent(suggestedLabel))
	                        .addPreferredGap(ComponentPlacement.RELATED)
	                        .addGroup(
	                                layout.createParallelGroup(Alignment.BASELINE)
	                                        .addComponent(requiredComboBox,
	                                                GroupLayout.PREFERRED_SIZE,
	                                                GroupLayout.DEFAULT_SIZE,
	                                                GroupLayout.PREFERRED_SIZE)
	                                        .addComponent(requiredFilter,
	                                                GroupLayout.PREFERRED_SIZE,
	                                                GroupLayout.DEFAULT_SIZE,
	                                                GroupLayout.PREFERRED_SIZE)
	                                        .addComponent(passedFilter,
	                                                GroupLayout.PREFERRED_SIZE,
	                                                GroupLayout.DEFAULT_SIZE,
	                                                GroupLayout.PREFERRED_SIZE))
	                        .addPreferredGap(ComponentPlacement.RELATED)
	                        .addGroup(
	                                layout.createParallelGroup(Alignment.TRAILING)
	                                        .addGroup(
	                                                layout.createSequentialGroup()
	                                                        .addGroup(
	                                                                layout.createParallelGroup(
	                                                                        Alignment.TRAILING)
	                                                                        .addComponent(
	                                                                                requiredScroller,
	                                                                                GroupLayout.DEFAULT_SIZE,
	                                                                                212,
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
	                                                                                                                                103,
	                                                                                                                                Short.MAX_VALUE)
	                                                                                                                        .addPreferredGap(
	                                                                                                                                ComponentPlacement.RELATED)
	                                                                                                                        .addComponent(
	                                                                                                                                removeButton,
	                                                                                                                                GroupLayout.DEFAULT_SIZE,
	                                                                                                                                103,
	                                                                                                                                Short.MAX_VALUE))
	                                                                                                        .addComponent(
	                                                                                                                suggestedScroller,
	                                                                                                                GroupLayout.DEFAULT_SIZE,
	                                                                                                                212,
	                                                                                                                Short.MAX_VALUE))
	                                                                                        .addPreferredGap(
	                                                                                                ComponentPlacement.RELATED)))
	                                                        .addGroup(
	                                                                layout.createParallelGroup(
	                                                                        Alignment.LEADING)
	                                                                        .addGroup(
	                                                                                layout.createSequentialGroup()
	                                                                                        .addGap(4)
	                                                                                        .addGroup(
	                                                                                                layout.createParallelGroup(
	                                                                                                        Alignment.BASELINE)
	                                                                                                        .addComponent(
	                                                                                                                suggestButton)
	                                                                                                        .addComponent(
	                                                                                                                unitsSelecter)
	                                                                                                        .addComponent(
	                                                                                                                unitsField,
	                                                                                                                GroupLayout.PREFERRED_SIZE,
	                                                                                                                GroupLayout.DEFAULT_SIZE,
	                                                                                                                GroupLayout.PREFERRED_SIZE)))
	                                                                        .addGroup(
	                                                                                layout.createSequentialGroup()
	                                                                                        .addPreferredGap(
	                                                                                                ComponentPlacement.RELATED)
	                                                                                        .addComponent(
	                                                                                                dynamicUnits)))
	                                                        .addPreferredGap(
	                                                                ComponentPlacement.RELATED)
	                                                        .addGroup(
	                                                                layout.createParallelGroup(
	                                                                        Alignment.LEADING)
	                                                                        .addGroup(
	                                                                                layout.createSequentialGroup()
	                                                                                        .addGroup(
	                                                                                                layout.createParallelGroup(
	                                                                                                        Alignment.BASELINE)
	                                                                                                        .addComponent(
	                                                                                                                descriptionInfo)
	                                                                                                        .addComponent(
	                                                                                                                nameInfo))
	                                                                                        .addPreferredGap(
	                                                                                                ComponentPlacement.RELATED)
	                                                                                        .addComponent(
	                                                                                                nameLabel,
	                                                                                                GroupLayout.PREFERRED_SIZE,
	                                                                                                GroupLayout.DEFAULT_SIZE,
	                                                                                                GroupLayout.PREFERRED_SIZE)
	                                                                                        .addPreferredGap(
	                                                                                                ComponentPlacement.RELATED)
	                                                                                        .addComponent(
	                                                                                                fuffilsInfo))
	                                                                        .addComponent(
	                                                                                descriptionScroller,
	                                                                                GroupLayout.PREFERRED_SIZE,
	                                                                                121,
	                                                                                GroupLayout.PREFERRED_SIZE)))
	                                        .addGroup(
	                                                layout.createSequentialGroup()
	                                                        .addGroup(
	                                                                layout.createParallelGroup(
	                                                                        Alignment.TRAILING)
	                                                                        .addComponent(
	                                                                                FuffilsLabel,
	                                                                                GroupLayout.PREFERRED_SIZE,
	                                                                                GroupLayout.DEFAULT_SIZE,
	                                                                                GroupLayout.PREFERRED_SIZE)
	                                                                        .addComponent(
	                                                                                passedScroller,
	                                                                                GroupLayout.DEFAULT_SIZE,
	                                                                                332,
	                                                                                Short.MAX_VALUE))
	                                                        .addPreferredGap(
	                                                                ComponentPlacement.RELATED)
	                                                        .addGroup(
	                                                                layout.createParallelGroup(
	                                                                        Alignment.BASELINE)
	                                                                        .addComponent(
	                                                                                uscpBox)
	                                                                        .addComponent(
	                                                                                generateButton))
	                                                        .addGap(5)))
	                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(gwrBox)));
	getContentPane().setLayout(layout);

	pack();
	setMinimumSize(getPreferredSize());

    }

    private void addButton() {
	System.out.print(requiredList.getSelectedValue());
	suggestedModel.addElement(requiredList.getSelectedValue());
	requiredModel.removeElementAt(requiredList.getSelectedIndex());
	updateUnitCount();
    }

    private void removeButton() {
	requiredModel.addElement(suggestedList.getSelectedValue());
	suggestedModel.removeElement(suggestedList.getSelectedValue());
	updateUnitCount();
    }

    private void updateUnitCount() {

	int unitCount = 0;

	for (int i = 0; i < suggestedModel.getSize(); i++) {
	    if (suggestedModel.get(i) instanceof Course) {
		unitCount += ((Course) suggestedModel.get(i)).getUnits();
		System.out.println(((Course) suggestedModel.get(i)).getUnits());
	    }
	}
	dynamicUnits.setText(unitCount + " " + UNITS);

    }

    private void updateRequiredList() {
	if (result == null) {
	    return;
	}
	Set<Course> courses;
	System.out.println(requiredComboBox.getSelectedItem());
	if (requiredComboBox.getSelectedItem().equals(ALL_FILTER)) {
	    courses = new HashSet<Course>(both);
	} else if (requiredComboBox.getSelectedItem().equals(PREREQ_MET)) {
	    courses = new HashSet<Course>(metArray);
	} else if (requiredComboBox.getSelectedItem().equals(PREREQ__NOT_MET)) {
	    courses = new HashSet<Course>(noMetArray);
	} else {
	    return;
	}
	requiredModel.clear();
	for (Course c : courses) {
	    System.out.println(c.toString() + "\t"
		    + c.getName().toLowerCase().contains(requiredFilter.getText().toLowerCase()));
	    if (c.toString().toLowerCase().contains(requiredFilter.getText().toLowerCase())) {
		if (suggestedModel.contains(c)) {
		    System.out.println(c + "is already in the suggested list");
		} else {
		    requiredModel.addElement(c);
		}
	    }
	}

    }

    private void updateUserState() {
	state.getTaken().clear();
	for (int i = 0; i < passedModel.getRowCount(); i++) {
	    if ((Boolean) passedModel.getValueAt(i, 0)) {
		state.getTaken().add((Course) passedModel.getValueAt(i, 1));
	    }

	}
    }

    Set<Course> both;
    Set<Course> metArray;
    Set<Course> noMetArray;

    private void generateLists() {
	updateInfo(null);
	if (state == null) {
	    System.out.println("USer state is null");
	}
	updateUserState();
	// result=decider.decideClasses(state, flowchart);
	Set<Course> met = new HashSet<Course>();
	Set<Course> unmet = new HashSet<Course>();
	result = new CourseDecider.Result(met, unmet);
	for (int i = 0; i < 10; i++) {
	    met.add(list.getCatalog().toArray(new Course[0])[i]);
	    unmet.add(list.getCatalog().toArray(new Course[0])[i + 10]);
	}
	both = new HashSet<Course>();
	metArray = result.getPrerequisitesMet();
	noMetArray = result.getPrerequisitesNotMet();
	both.addAll(metArray);
	both.addAll(noMetArray);
	for (Course c : list.getCatalog()) {
	    if (state.getTaken().contains(c)) {
		System.out.println("Is taken");
	    }
	}
	updateRequiredList();
	requiredComboBox.setSelectedIndex(0);
	requiredFilter.setText("");
	suggestedModel.clear();
	updateUnitCount();

    }

    private void updateInfo(Course selCourse) {
	if (selCourse == null) {
	    descriptionPane.setText("");
	    nameLabel.setText("");
	    FuffilsLabel.setText("");
	    return;

	}
	descriptionPane.setText(selCourse.getDescription());
	nameLabel.setText(selCourse.getName());
	if (selCourse.preRecsMet(state.getTaken())) {
	    FuffilsLabel.setText("the list of taken classes fulfills all prereqs");
	} else {
	    FuffilsLabel.setText(selCourse.getPreRequisitesString());

	}
	descriptionPane.setCaretPosition(0);

    }

    FileFilter saveLoadFilter = new FileFilter() {
	                          public boolean accept(File f) {
		                      return (f.getName().endsWith(FILE_EXTENSION) || f
		                              .isDirectory());
	                          }

	                          public String getDescription() {
		                      return "User State (" + FILE_EXTENSION + ")";
	                          }
	                      };

    private void openUserState() {
	JFileChooser chooser = new JFileChooser();
	chooser.setFileFilter(saveLoadFilter);
	int choice = chooser.showOpenDialog(this);
	if (choice == JFileChooser.APPROVE_OPTION) {
	    System.out.println("load" + chooser.getSelectedFile());
	    try {
		state = new UserState(chooser.getSelectedFile(), list);
		userStateFile = chooser.getSelectedFile();
		for (int i = 0; i < passedModel.getRowCount(); i++) {
		    passedModel.setValueAt(
			    state.getTaken().contains((Course) passedModel.getValueAt(i, 1)), i, 0);
		}
		setTitle(userStateFile.getName());
	    } catch (IOException ex) {
		Logger.getLogger(SchedulerFrame.class.getName()).log(Level.SEVERE, null, ex);
	    }
	} else if (choice == JFileChooser.CANCEL_OPTION) {
	} else {
	}
    }

    private void saveAsUserState() {
	System.out.println("Saving");
	System.out.println(userStateFile);
	JFileChooser chooser = new JFileChooser() {
	    @Override
	    public void approveSelection() {
		File SelectedFile = getSelectedFile();

		if (!SelectedFile.getName().endsWith(FILE_EXTENSION)) {
		    try {
			SelectedFile = new File(SelectedFile.getCanonicalPath() + FILE_EXTENSION);
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		}

		if (SelectedFile.exists() && !SelectedFile.equals(userStateFile)) {
		    int overWrite = JOptionPane.showConfirmDialog(this, getSelectedFile().getName()
			    + " already exists.\nDo you want to replace it?", "Confirm Save As",
			    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		    if (overWrite == JOptionPane.OK_OPTION) {
			userStateFile = SelectedFile;
			super.approveSelection();
		    } else if (overWrite == JOptionPane.CANCEL_OPTION) {
			super.cancelSelection();
		    }
		    return;
		} else {
		    userStateFile = SelectedFile;
		    super.approveSelection();
		}

	    }
	};
	chooser.setFileFilter(saveLoadFilter);
	chooser.setSelectedFile(userStateFile);
	int choice = chooser.showSaveDialog(this);
	if (choice == JFileChooser.APPROVE_OPTION) {
	    saveUserState();
	}
    }

    private void suggestSchedule() {

    }

    private void saveUserState() {

	updateUserState();
	if (userStateFile == null) {
	    saveAsUserState();
	}
	try {
	    state.write(userStateFile);
	} catch (IOException ex) {
	    Logger.getLogger(SchedulerFrame.class.getName()).log(Level.SEVERE, null, ex);
	}

    }
}
