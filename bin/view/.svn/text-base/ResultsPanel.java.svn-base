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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import model.ElementaryMode;
import model.Reaction;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import view.popup.FilterFrame;
import controler.Download;
import controler.DownloadContentThread;
import controler.Filter;
import controler.MainControler;
import controler.MyDocumentListener;
import controler.Var;

public class ResultsPanel extends JPanel {

	private String resultsName;

	private MainControler controler;
	private JTextArea log;
	private JTable modeTable;
	private JToolBar toolbar;
	private List<ElementaryMode> allModes = new ArrayList<ElementaryMode>();
	private JComboBox modesCombo;
	private JButton download;
	private JButton addToProject = new JButton(Var.iconaddtoproject);
	private JButton removeFromProject = new JButton(Var.iconremoveproject);
	private JButton filterButton = new JButton(Var.iconfilter);
	private JButton histoButton = new JButton(Var.iconhistogram);
	private JButton scriptButton = new JButton(Var.iconcmd);
	private String[] command;
	private JTextField searchField = new JTextField(8);
	private boolean isAttached;
	private Filter filter = new Filter();

	public ResultsPanel(final MainControler controler, String[] command,
			String resultsName) {

		this.resultsName = resultsName;

		this.controler = controler;
		this.command = command;
		modeTable = new JTable(0, 2);

		DefaultTableModel model = new MyTableModel();

		model.addColumn("Stoichiometry");
		model.addColumn("Enzyme name");
		model.addColumn("Equation");
		modeTable.setModel(model);

		JPanel logPanel = new JPanel(new BorderLayout());
		log = new JTextArea();
		log.setEditable(false);
		log.setBackground(new Color(0, 0, 0));
		log.setForeground(new Color(255, 255, 255));

		logPanel.add(new JLabel("Genereted log", JLabel.CENTER),
				BorderLayout.PAGE_START);
		logPanel.add(new JScrollPane(log), BorderLayout.CENTER);

		addToProject.setName("add");
		addToProject.setToolTipText("Attach this result to the project");
		removeFromProject.setName("remove");
		removeFromProject.setToolTipText("Remove this result from the project");

		removeFromProject.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				removeFromProject();
				updateToolbar();
			}
		});

		addToProject.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				addToProject();
				updateToolbar();
			}
		});

		if (!resultsName.startsWith("saved/")) {
			isAttached = false;
		} else {
			isAttached = true;
		}

		filterButton.setToolTipText("Filter the results");

		filterButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				FilterFrame ff = new FilterFrame(GeneralFrame
						.getInstance(controler), allModes, filter, controler);
				if (ff.okPressed()) {

					displayFrame(ff.getNewModes(),ff.getIndices());
				}
			}
		});
		
		scriptButton.setToolTipText("Download the command as a script");
		scriptButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				
				String scriptString = makeScript();
				new Download(scriptString, "Download the command as a script", "");
				
			}
		});

		download = new JButton(new ImageIcon("src/icons/download.png"));
		download.setToolTipText("Download results");
		
		histoButton.setToolTipText("Statistics");

		searchField.setMaximumSize(new Dimension(20, 30));

		readLog();
		allModes = readResultsFile();
		
		List<Integer> indices = new ArrayList<Integer>();
		for (int i =0;i<allModes.size();i++){
			indices.add((i+1));
		}
		
		displayFrame(allModes,indices);

	}

	

	public void setTable(int nbMode, List<ElementaryMode> modes) {

		ElementaryMode mode = modes.get(nbMode);

		DefaultTableModel model = (DefaultTableModel) modeTable.getModel();

		while (model.getRowCount() > 0) {
			model.removeRow(0);
		}

		Map<Reaction, Double> map = mode.getContent();
		for (Reaction key : map.keySet()) {

			model.addRow(new Object[] { map.get(key), key.getName(),
					key.getStringReaction() });

		}

		final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
				modeTable.getModel());
		modeTable.setRowSorter(sorter);
		modeTable.getTableHeader().setReorderingAllowed(false);

		searchField.getDocument().addDocumentListener(
				new MyDocumentListener(searchField, sorter, 1, 2));

		searchField.setText(searchField.getText());

	}

	// fill the list
	public List<ElementaryMode> readResultsFile() {

		List<ElementaryMode> modes = new ArrayList<ElementaryMode>();

		try {

			BufferedReader in = new BufferedReader(new FileReader(new File(
					"res/" + resultsName + ".res")));
			String input = in.readLine();
			while (input != null) {

				ElementaryMode mode = new ElementaryMode();

				String[] ligne = input.split("\t");

				List<Reaction> reactions = controler.getListReactions();
				String ligneList = "";

				for (int i = 0; i < ligne.length; i++) {

					if (!ligne[i].trim().equals("0.0")) {
						mode.addReaction(reactions.get(i),
								Double.parseDouble(ligne[i].trim()));
					}

				}
				input = in.readLine();
				modes.add(mode);

			}
		} catch (IOException ex) {
		}
		return modes;
	}

	public void readLog() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(
					"res/" + resultsName + ".log")));

			String input = in.readLine();
			String text = "";
			while (input != null) {
				text += input + "\n";
				input = in.readLine();
			}

			log.setText(text);
		}

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	class MyTableModel extends DefaultTableModel {
		public boolean isCellEditable(int iRowIndex, int iColumnIndex) {
			return false;
		}
	}

	public JPanel panel() {
		return this;
	}


	protected void addToProject() {

		isAttached = true;
		String output = resultsName.replace("saved/", "");

		// file are in saved repertory
		File copyRes = new File("res/saved/" + output + ".res");
		File copyLog = new File("res/saved/" + output + ".log");

		new File("res/" + output + ".res").renameTo(copyRes);
		new File("res/" + output + ".log").renameTo(copyLog);

		controler.getProject().addResultsPath(output, command);
		
		try {
			controler.saveProject();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void removeFromProject() {

		isAttached = false;
		String output = resultsName.replace("saved/", "");

		// file are in saved repertory
		File copyRes = new File("res/" + output + ".res");
		File copyLog = new File("res/" + output + ".log");

		new File("res/saved/" + output + ".res").renameTo(copyRes);
		new File("res/saved/" + output + ".log").renameTo(copyLog);

		controler.getProject().removeResultsPath(output);
		
		try {
			controler.saveProject();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateToolbar() {

		for (int i = 0; i < toolbar.getComponentCount(); i++) {
			if (toolbar.getComponentAtIndex(i).getName() != null) {
				if (toolbar.getComponentAtIndex(i).getName().equals("add")) {
					toolbar.remove(i);
					toolbar.add(removeFromProject, i);
					toolbar.revalidate();
					toolbar.repaint();
				} else if (toolbar.getComponentAtIndex(i).getName()
						.equals("remove")) {
					toolbar.remove(i);
					toolbar.add(addToProject, i);
					toolbar.revalidate();
					toolbar.repaint();
				}
			}

		}
	}

	public void displayFrame(final List<ElementaryMode> modes,final List<Integer> indices) {

		this.removeAll();

		DefaultTableModel model = (DefaultTableModel) modeTable.getModel();

		while (model.getRowCount() > 0) {
			model.removeRow(0);
		}

		if (modes.size() > 0) {
			setTable(0, modes);
		}
		JLabel nbMod = new JLabel(modes.size() + " mode(s) found");

		toolbar = new JToolBar();
		// set elements in the toolbar
		toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.LINE_AXIS));
		toolbar.add(nbMod);
		toolbar.setFloatable(false);

		Object[] comboElements = new Object[modes.size()];

		for (int i = 0; i < modes.size(); i++) {
			
			comboElements[i] = "mode " + indices.get(i);
		}

		modesCombo = new JComboBox(comboElements);

		if (modesCombo.getActionListeners().length > 0) {
			modesCombo.removeActionListener(modesCombo.getActionListeners()[0]);
		}
		modesCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTable(modesCombo.getSelectedIndex(), modes);
			}
		});

		if (modes.size() > 0) {
			toolbar.add(modesCombo);
		}
		modesCombo.setMaximumSize(new Dimension(100, 30));
		
		//we put the right size for le comboBox
		int sizeMax = String.valueOf(modes.size()).length();
		
		String stringMax="mode ";
		for(int i = 0;i<sizeMax;i++){
			stringMax+="X";
		}
		
		modesCombo.setPrototypeDisplayValue(stringMax);

		
		

		

		if (download.getActionListeners().length > 0) {
			download.removeActionListener(download.getActionListeners()[0]);
		}
		download.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Thread thread = new DownloadContentThread(modes,controler,command,log.getText());
				thread.start();

			}
		});
		
		if (histoButton.getActionListeners().length > 0) {
			histoButton.removeActionListener(histoButton.getActionListeners()[0]);
		}
		histoButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showHistogram(modes);
				
			}

		});
		
		
		toolbar.add(histoButton);
		toolbar.add(filterButton);
		toolbar.add(download);
		toolbar.add(scriptButton);
		if (!isAttached) {
			toolbar.add(addToProject);
		} else {
			toolbar.add(removeFromProject);
		}
		

		toolbar.add(Box.createHorizontalGlue());
		toolbar.add(searchField);
		toolbar.add(new JLabel(Var.iconsearch));

		JPanel logPanel = new JPanel(new BorderLayout());

		logPanel.add(new JLabel("Generetad log", JLabel.CENTER),
				BorderLayout.PAGE_START);
		logPanel.add(new JScrollPane(log), BorderLayout.CENTER);

		this.setLayout(new BorderLayout());

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				new JScrollPane(modeTable), logPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(500);

		add(toolbar, BorderLayout.PAGE_START);
		this.add(splitPane, BorderLayout.CENTER);

		this.revalidate();
		this.repaint();
	}
	
	public void showHistogram( List<ElementaryMode> modes){
		
		//for the JTable
		DefaultTableModel tableModel = new DefaultTableModel();
		JTable tableResult = new JTable();
		tableResult.setModel(tableModel);
		
		tableModel.addColumn("Reaction");
		tableModel.addColumn("Presence in the modes");
		
		tableResult.setAutoCreateRowSorter(true);
		
		Map <Reaction, Double> stats = new HashMap<Reaction, Double>();
		
		DecimalFormat df = new DecimalFormat("0.00");
		
		for (ElementaryMode em : modes){
			for (Reaction r : em.getContent().keySet()) {
				if (em.getContent().containsKey(r)){
					if (!stats.containsKey(r)){
						stats.put(r, 1.0);
					}
					else {
						Reaction key = r;
						Double value = stats.get(r)+1;
						stats.remove(key);
						stats.put(key, value);
					}
				}
			}
		}
		
		
		for (Reaction r : stats.keySet()){
			tableModel.addRow(new Object[]{r,String.valueOf(df.format(stats.get(r)*100/modes.size()))+"%"});
		}
		
		
		JFrame statisticFrame = new JFrame();
		statisticFrame.add(new JScrollPane(tableResult), BorderLayout.CENTER);
		statisticFrame.setVisible(true);
		statisticFrame.setSize(400,350);
		statisticFrame.setTitle("Representativeness of each reaction");
		statisticFrame.setLocation(500,600);
		
		//histogram
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		Map<Integer,Integer> data = new TreeMap<Integer,Integer>();
		int maxSize=0;
		for (ElementaryMode em : modes){
			int modeLength = em.getContent().size();
			if (modeLength>maxSize){
				maxSize = modeLength;
			}
			if (data.containsKey(modeLength)){
				int value = data.get(modeLength)+1;
				data.put(modeLength, value);
			}
			else {
				data.put(modeLength,1);
			}
		}

		for(int i=1;i<maxSize;i++) {
			if (!data.containsKey(i)){
				data.put(i, 0);
			}
		}
		for (int key : data.keySet()) {
			dataset.addValue(Integer.valueOf((data.get(key))),"test",Integer.valueOf(key));
		}
		
		  String plotTitle = "Number of reactions per elementary mode"; 
	       String xaxis = "Reaction number";
	       String yaxis = "Elementary mode number"; 
	       PlotOrientation orientation = PlotOrientation.VERTICAL; 
	       boolean show = false; 
	       boolean toolTips = false;
	       boolean urls = false; 
	       JFreeChart chart = ChartFactory.createBarChart3D(plotTitle, xaxis, yaxis, 
	                dataset, orientation, show, toolTips, urls);
	       
	       CategoryPlot plot = chart.getCategoryPlot(); 
	       CategoryAxis axis = plot.getDomainAxis(); 
	       
	       plot.getDomainAxis(0).setLabelFont( plot.getDomainAxis().getLabelFont().deriveFont(new Float(11)));
	       
	       ChartFrame frame=new ChartFrame("Elementary modes",chart);
	       frame.setVisible(true);
	       frame.setSize(400,350);
	       frame.setLocation(500,100);
	      
	}
	
	protected String makeScript() {
		String stringCommand = "";
		for (int i = 0; i < command.length; i++) {
			stringCommand += command[i] + " ";
		}
		return stringCommand;
	}

}
