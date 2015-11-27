/*
    This file is part of J-Varo.
    Authors: Elodie Cassan, Christophe Djemiel, Thomas Faux, Aurélie Lelièvre, Lucas Marmiesse

    J-Varo is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    J-Varo is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with J-Varo.  If not, see <http://www.gnu.org/licenses/>.
 */

package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import model.data.Format;
import model.data.MetatoolFormat;
import model.data.ProjectFormat;
import model.data.SBMLFormat;
import view.popup.AboutFrame;
import view.popup.ImportReactionFrame;
import controler.MainControler;
import controler.NewProjectListener;
import controler.Var;
import controler.externalPrograms.RegEfmToolHandler;
import controler.load.MenuOpenListener;
import controler.metabolites.AddMetabListener;
import controler.reactions.AddReacListener;
import controler.rule.AddRuleListener;
import controler.save.ButtonSaveListener;
import controler.save.MenuSaveListener;
import controler.visu.VisuListener;

public class GeneralFrame extends JFrame implements Observer {

	private static GeneralFrame uniqueInstance = null;
	private MainControler controler;

	private JTabbedPane leftPanel;
	private JTabbedPane rightPanel;

	private JMenuItem save;
	private JMenuItem saveAs;
	private JMenuItem addMetab;
	private JMenuItem addReac;
	private JMenuItem addRule;
	private JMenuItem importReaction;
	private JMenuItem runVisu;
	private JMenuItem runReg;

	private JButton buttonsave;
	private JButton buttonvisu;
	private JButton buttonimportJvaro;
	private JButton buttonRegefmtool;
	private List<Integer> pressedKeys = new ArrayList<Integer>();

	private JPanel statusBar;

	private GeneralFrame(MainControler controler) {
		super();

		this.setIconImage(Var.logolittle.getImage());
		this.setExtendedState(this.MAXIMIZED_BOTH);

		this.controler = controler;
		initGF();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setFocusable(true);

		ekans();

	}

	public static GeneralFrame getInstance(MainControler controler) {
		if (uniqueInstance == null) {
			return uniqueInstance = new GeneralFrame(controler);
		}
		return uniqueInstance;
	}

	private void initGF() {

		initmenu();
		initToolbar();
		initpanel();
		initStatusBar();

		this.setTitle("J-VARO");
		this.pack();
		setLocationRelativeTo(null);
		this.setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}

	private void initmenu() {

		// Menu
		JMenuBar menubar = new JMenuBar();
		JMenu filemenu = new JMenu("File");
		JMenuItem fileItem1 = new JMenuItem("New");
		fileItem1.addActionListener(new NewProjectListener(controler));
		KeyStroke keyNew = KeyStroke.getKeyStroke(KeyEvent.VK_N,
				KeyEvent.CTRL_DOWN_MASK);
		fileItem1.setAccelerator(keyNew);

		// subMenu Open
		JMenu fileItem2 = new JMenu("Open");
		JMenuItem openProject = new JMenuItem("Project");
		JMenuItem openSBML = new JMenuItem("SBML file");
		JMenuItem openMETATOOL = new JMenuItem("Metatool file");
		JMenuItem openRegefmtool = new JMenuItem("regEfmTool file");
		fileItem2.add(openProject);
		fileItem2.add(openSBML);
		fileItem2.add(openMETATOOL);
		fileItem2.add(openRegefmtool);

		// subMenu Save
		save = new JMenuItem("Save");
		KeyStroke keySave = KeyStroke.getKeyStroke(KeyEvent.VK_S,
				KeyEvent.CTRL_DOWN_MASK);
		save.setAccelerator(keySave);
		save.setEnabled(controler.hasLoadedProject());

		// subMenu save as
		saveAs = new JMenu("Save as");
		saveAs.setEnabled(controler.hasLoadedProject());
		JMenuItem saveProject = new JMenuItem("Project");
		JMenuItem saveSBML = new JMenuItem("SBML file");
		JMenuItem saveMetatool = new JMenuItem("Metatool file");
		JMenuItem saveRegEfmTool = new JMenuItem("RegEfmTool files");
		JMenuItem saveTlp = new JMenuItem("Tlp file");
		saveAs.add(saveProject);
		saveAs.add(saveSBML);
		saveAs.add(saveMetatool);
		saveAs.add(saveRegEfmTool);
		saveAs.add(saveTlp);

		// Close
		JMenuItem fileItem5 = new JMenuItem("Close");
		KeyStroke keyClose = KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				KeyEvent.CTRL_DOWN_MASK);
		fileItem5.setAccelerator(keyClose);

		fileItem5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(null,
						"Are you sure you want to quit?", "",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
					System.exit(0);
			}
		});

		// Listeners for opening all file formats
		MenuOpenListener openlistener = new MenuOpenListener(controler,
				openProject, openSBML, openMETATOOL, openRegefmtool);
		openProject.addActionListener(openlistener);
		openSBML.addActionListener(openlistener);
		openMETATOOL.addActionListener(openlistener);
		openRegefmtool.addActionListener(openlistener);

		// Listeners for saving all file formats
		MenuSaveListener savelistener = new MenuSaveListener(controler,
				saveProject, saveSBML, saveMetatool, saveRegEfmTool, saveTlp,
				save);
		saveProject.addActionListener(savelistener);
		saveSBML.addActionListener(savelistener);
		saveMetatool.addActionListener(savelistener);
		saveRegEfmTool.addActionListener(savelistener);
		saveTlp.addActionListener(savelistener);
		save.addActionListener(savelistener);

		filemenu.add(fileItem1);
		filemenu.add(fileItem2);
		filemenu.add(save);
		filemenu.add(saveAs);
		filemenu.add(new JSeparator());

		// loading recent files

		// map that makes correspond types to loaders
		final Map<String, Format> map = new HashMap<String, Format>();
		map.put("sbml", new SBMLFormat());
		map.put("prj", new ProjectFormat(controler));
		map.put("met", new MetatoolFormat());

		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(new File("recent.txt"),
					true));
			out.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(new File("recent.txt")));

			String input = in.readLine();

			for (int i = 0; i < 5; i++) {
				if (input != null) {
					final String[] ligne = input.split(" ");
					JMenuItem item = new JMenuItem(ligne[0]);
					item.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent arg0) {
							controler.setLoader(map.get(ligne[1]));

							File files[] = new File[1];
							files[0] = new File(ligne[0]);
							try {
								controler.loadNetwork(files);
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}

						}
					});

					filemenu.add(item);

					input = in.readLine();
				}

			}
			in.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		//
		filemenu.add(new JSeparator());
		filemenu.add(fileItem5);

		JMenu networkmenu = new JMenu("Network");
		addMetab = new JMenuItem("Add a metabolite");
		addReac = new JMenuItem("Add a reaction");
		addRule = new JMenuItem("Add a rule");
		importReaction = new JMenuItem("Import reactions");
		addMetab.setEnabled(controler.hasLoadedProject());
		addReac.setEnabled(controler.hasLoadedProject());
		addRule.setEnabled(controler.hasLoadedProject());
		importReaction.setEnabled(controler.hasLoadedProject());

		addMetab.addActionListener(new AddMetabListener(controler));
		addReac.addActionListener(new AddReacListener(controler));
		addRule.addActionListener(new AddRuleListener(controler));
		importReaction.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new ImportReactionFrame(GeneralFrame.getInstance(controler),
						controler);
			}

		});

		networkmenu.add(addMetab);
		networkmenu.add(addReac);
		networkmenu.add(addRule);
		networkmenu.add(new JSeparator());
		networkmenu.add(importReaction);

		JMenu runMenu = new JMenu("Run");
		runReg = new JMenuItem("RegEfmTool");
		runReg.setEnabled(controler.hasLoadedProject());
		runReg.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new RegEfmToolHandler(controler);
			}

		});

		runVisu = new JMenuItem("Visualization");
		runVisu.setEnabled(controler.hasLoadedProject());
		runVisu.addActionListener(new VisuListener(controler));

		runMenu.add(runReg);
		runMenu.add(runVisu);

		JMenu helpmenu = new JMenu("Help");
		JMenuItem helpitem1 = new JMenuItem("About");
		helpitem1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AboutFrame(frame());
			}
		});
		helpmenu.add(helpitem1);

		menubar.add(filemenu);

		menubar.add(networkmenu);
		menubar.add(runMenu);
		menubar.add(helpmenu);

		this.setJMenuBar(menubar);
	}

	private void initToolbar() {
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);

		// New
		JButton buttonnew = new JButton(Var.iconnew);
		buttonnew.setToolTipText("Create a new project");
		buttonnew.setText("New Project");
		buttonnew.setHorizontalTextPosition(JButton.CENTER);
		buttonnew.setVerticalTextPosition(JButton.BOTTOM);
		buttonnew.addActionListener(new NewProjectListener(controler));

		toolbar.add(buttonnew);

		// Open
		JButton buttonfile = new JButton(Var.iconfile);
		buttonfile.setToolTipText("Open an existing project");
		buttonfile.setText("Open Project");
		buttonfile.setHorizontalTextPosition(JButton.CENTER);
		buttonfile.setVerticalTextPosition(JButton.BOTTOM);
		buttonfile
				.addActionListener(new MenuOpenListener(controler, buttonfile));
		toolbar.add(buttonfile);

		// Save
		buttonsave = new JButton(Var.iconsave);
		buttonsave.setToolTipText("Save current project");
		buttonsave.setText("Save Project");
		buttonsave.setHorizontalTextPosition(JButton.CENTER);
		buttonsave.setVerticalTextPosition(JButton.BOTTOM);
		buttonsave.setEnabled(controler.hasLoadedProject());
		buttonsave.addActionListener(new ButtonSaveListener(controler));
		toolbar.add(buttonsave);

		// import from Jvaro
		buttonimportJvaro = new JButton(Var.iconimport);
		buttonimportJvaro.setToolTipText("Import network from J-Varo");
		buttonimportJvaro.setText("Import reactions");
		buttonimportJvaro.setHorizontalTextPosition(JButton.CENTER);
		buttonimportJvaro.setVerticalTextPosition(JButton.BOTTOM);
		buttonimportJvaro.setEnabled(controler.hasLoadedProject());
		buttonimportJvaro.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new ImportReactionFrame(GeneralFrame.getInstance(controler),
						controler);
			}

		});
		toolbar.add(buttonimportJvaro);

		// space in the toolbar
		toolbar.add(Box.createRigidArea(new Dimension(50, 0)));

		// run regefmtool
		buttonRegefmtool = new JButton(Var.iconrun);
		buttonRegefmtool.setEnabled(controler.hasLoadedProject());
		buttonRegefmtool.setToolTipText("Launch regEfmtool analysis");
		buttonRegefmtool.setText("regEfmtool");
		buttonRegefmtool.setHorizontalTextPosition(JButton.CENTER);
		buttonRegefmtool.setVerticalTextPosition(JButton.BOTTOM);
		buttonRegefmtool.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new RegEfmToolHandler(controler);
			}

		});
		toolbar.add(buttonRegefmtool);

		// visualization
		buttonvisu = new JButton(Var.iconvisu);
		buttonvisu.setEnabled(controler.hasLoadedProject());
		buttonvisu.setToolTipText("Create a visualization");
		buttonvisu.setText("Visualization");
		buttonvisu.setHorizontalTextPosition(JButton.CENTER);
		buttonvisu.setVerticalTextPosition(JButton.BOTTOM);
		buttonvisu.addActionListener(new VisuListener(controler));
		toolbar.add(buttonvisu);

		this.add(toolbar, BorderLayout.NORTH);
	}

	private void initStatusBar() {
		statusBar = new JPanel();
		this.add(statusBar, BorderLayout.PAGE_END);
		statusBar.add(new JLabel("Welcome to J-VARO !"));
	}

	private void initpanel() {
		JSplitPane panel1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				initmetabo(), initpanelright());
		this.add(panel1, BorderLayout.CENTER);
	}

	private JTabbedPane initpanelright() {

		rightPanel = new RightPanel(controler);
		return rightPanel;

	}

	public LeftPanel getLeftPanel() {
		return (LeftPanel) leftPanel;
	}

	private JTabbedPane initmetabo() {

		leftPanel = new LeftPanel(controler);
		return leftPanel;

	}

	public RightPanel getRightPanel() {
		return (RightPanel) rightPanel;
	}

	public JPanel getStatusBar() {
		return statusBar;
	}

	public void setStatusBar() {
		statusBar.removeAll();
		statusBar.add(new JLabel("Metabolites : "
				+ controler.getListMetabos().size()));
		statusBar.add(Box.createRigidArea(new Dimension(50, 0)));
		statusBar.add(new JLabel("Reactions : "
				+ controler.getListReactions().size()));
		statusBar.add(Box.createRigidArea(new Dimension(50, 0)));
		statusBar.add(new JLabel("Rules : " + controler.getRules().size()));
		statusBar.revalidate();
		statusBar.repaint();
	}

	public void update(Observable arg0, Object arg1) {

		save.setEnabled(controler.hasLoadedProjectWithPath());
		saveAs.setEnabled(controler.hasLoadedProject());
		addMetab.setEnabled(controler.hasLoadedProject());
		addReac.setEnabled(controler.hasLoadedProject());
		addRule.setEnabled(controler.hasLoadedProject());
		importReaction.setEnabled(controler.hasLoadedProject());
		runVisu.setEnabled(controler.hasLoadedProject());
		runReg.setEnabled(controler.hasLoadedProject());

		buttonsave.setEnabled(controler.hasLoadedProjectWithPath());
		buttonvisu.setEnabled(controler.hasLoadedProject());
		buttonRegefmtool.setEnabled(controler.hasLoadedProject());
		buttonimportJvaro.setEnabled(controler.hasLoadedProject());
		if (controler.hasLoadedProject()) {
			this.setTitle(controler.getProjectName() + " - J-VARO");
		}

		this.setStatusBar();
		this.setVisible(true);
	}

	public GeneralFrame frame() {
		return this;
	}

	private void ekans() {
		this.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				pressedKeys.add(e.getKeyCode());
				if (pressedKeys.size() > 5) {
					pressedKeys.remove(0);
				}

				if (pressedKeys.size() == 5) {
					if (pressedKeys.get(0) == 83 && pressedKeys.get(1) == 78
							&& pressedKeys.get(2) == 65
							&& pressedKeys.get(3) == 75
							&& pressedKeys.get(4) == 69) {

						try {
							Process p = Runtime.getRuntime().exec(
									"java -jar lib/ekans.jar");
						} catch (IOException e1) {
							e1.printStackTrace();
						}

					}
					else if (pressedKeys.get(0) == 74 && pressedKeys.get(1) == 77
							&& pressedKeys.get(2) == 69
							&& pressedKeys.get(3) == 84
							&& pressedKeys.get(4) == 65) {

						try {
							Process p = Runtime.getRuntime().exec(
									"java -jar lib/atemj.jar");
						} catch (IOException e1) {
							e1.printStackTrace();
						}

					}
				}
			}

			public void keyReleased(KeyEvent e) {

			}
		});
	}

	public void disableSave() {
		save.setEnabled(false);
		buttonsave.setEnabled(false);
	}
	
	public void enableSave() {
		save.setEnabled(true);
		buttonsave.setEnabled(true);
	}

}
