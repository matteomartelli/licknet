/*
 * Copyright (C) 2014 Matteo Martelli matteomartelli3@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package licknet.view;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.graphstream.ui.swingViewer.Viewer;

import licknet.app.LickNetApp;
import licknet.graph.NotesGraph;
import licknet.graph.NotesGraphSettings;
import licknet.lick.LickGeneratorSettings;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Graph;
import org.graphstream.stream.file.FileSink;
import org.graphstream.stream.file.FileSinkGraphML;
import org.graphstream.stream.file.FileSourceGraphML;
import org.herac.tuxguitar.io.base.TGFileFormatException;

/**
 *
 * @author Matteo Martelli matteomartelli3@gmail.com
 */
public class Frame extends javax.swing.JFrame {
	
	
	
	class SharedListSelectionHandler implements ListSelectionListener {
		//FIXME: triggered two times for each selction
		@Override
		public void valueChanged(ListSelectionEvent e) {
			ListSelectionModel lsm = (ListSelectionModel)e.getSource();
			
			if (!lsm.isSelectionEmpty() && !(e.getValueIsAdjusting())) {
				// Find out which indexes are selected.
				int minIndex = lsm.getMinSelectionIndex();
				int maxIndex = lsm.getMaxSelectionIndex();
				for (int i = minIndex; i <= maxIndex; i++) {
					if (lsm.isSelectedIndex(i)) {
						NotesGraph graph = getSelectedGraph();
						drawGraphProperties(graph);
						break;
					}
				}
			}
		}
	}
	
	private static final String GPROP_VCARDINALITY_TEXT = "Vertex Cardinality: ";
	private static final String GPROP_ECARDINALITY_TEXT = "Edges Cardinality: ";
	private static final String GPROP_CLUSTAVG_TEXT = "Clustering AVG: ";
	private static final String GPROP_SHORTESTPATHAVG_TEXT = "Path Length AVG : ";
	private static final String GPROP_CLUSTAVGRND_TEXT = "Rand Clustering AVG: ";
	private static final String GPROP_SHORTESTPATHAVGRND_TEXT = "Rand Path Length AVG : ";

	private static LickNetApp app = LickNetApp.getInstance();

	/**
	 * Creates new form Frame
	 */
	public Frame() {
		setTitle("Licknet");
		initComponents();
		ListSelectionModel listSelectionModel = jListGraphs.getSelectionModel();
		listSelectionModel.addListSelectionListener(
                            new SharedListSelectionHandler());
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jPanelMain = new javax.swing.JPanel();
        jLabelGraphsList = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListGraphs = new javax.swing.JList();
        jButtonDisplayGraph = new javax.swing.JButton();
        jCheckBoxWholeGraph = new javax.swing.JCheckBox();
        jTabbedPaneLicks = new javax.swing.JTabbedPane();
        jPanelGraphsSettings = new javax.swing.JPanel();
        jCheckBoxInfluenceBending = new javax.swing.JCheckBox();
        jCheckBoxInfluenceLoopNote = new javax.swing.JCheckBox();
        jButtonCreateGraphs = new javax.swing.JButton();
        jTextFieldBrowseGraphs = new javax.swing.JTextField();
        jButtonBrowseGraphs = new javax.swing.JButton();
        jLabelBrowseGraphs = new javax.swing.JLabel();
        jPanelLickClassify = new javax.swing.JPanel();
        jLabelBrowseLick = new javax.swing.JLabel();
        jButtonBrowseLick = new javax.swing.JButton();
        jTextFieldBrowseLick = new javax.swing.JTextField();
        jButtonClassifyLick = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableGraphsScores = new javax.swing.JTable();
        jLabelBestGraph = new javax.swing.JLabel();
        jPanelLickGenerate = new javax.swing.JPanel();
        jButtonGenerateLicks = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableGeneratedLicks = new javax.swing.JTable();
        jSpinnerRandomLicksN = new javax.swing.JSpinner();
        jLabelRandomLicksN = new javax.swing.JLabel();
        jSpinnerBestLicksN = new javax.swing.JSpinner();
        jLabelBestLicksN = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSpinnerMinNotesN = new javax.swing.JSpinner();
        jLabelMinNotesN = new javax.swing.JLabel();
        jSpinnerMaxNotesN = new javax.swing.JSpinner();
        jLabelMaxNotesN = new javax.swing.JLabel();
        jLabelLickDuration = new javax.swing.JLabel();
        jTextFieldLickDuration = new javax.swing.JTextField();
        jLabelVCardinality = new javax.swing.JLabel();
        jLabelECardinality = new javax.swing.JLabel();
        jLabelClustAvg = new javax.swing.JLabel();
        jLabelShortestPathLenghtAvg = new javax.swing.JLabel();
        jLabelClustAvgRnd = new javax.swing.JLabel();
        jLabelShortestPathLenghtAvgRnd = new javax.swing.JLabel();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabelGraphsList.setText("Graphs List");

        jListGraphs.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListGraphs.setToolTipText("");
        jScrollPane1.setViewportView(jListGraphs);
        jListGraphs.getAccessibleContext().setAccessibleParent(this);

        jButtonDisplayGraph.setText("Display");
        jButtonDisplayGraph.setToolTipText("");
        jButtonDisplayGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDisplayGraphActionPerformed(evt);
            }
        });

        jCheckBoxWholeGraph.setText("Whole Graph");
        jCheckBoxWholeGraph.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxWholeGraphItemStateChanged(evt);
            }
        });

        jCheckBoxInfluenceBending.setText("Consider Bendings");

        jCheckBoxInfluenceLoopNote.setText("Consider Repeated Notes");

        jButtonCreateGraphs.setText("Regenerate Graphs");
        jButtonCreateGraphs.setToolTipText("Create the graphs from the default folder");
        jButtonCreateGraphs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCreateGraphsActionPerformed(evt);
            }
        });

        jTextFieldBrowseGraphs.setEditable(false);
        jTextFieldBrowseGraphs.setText(app.getGraphsFolderPath());

        jButtonBrowseGraphs.setText("Browse...");
        jButtonBrowseGraphs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBrowseGraphsActionPerformed(evt);
            }
        });

        jLabelBrowseGraphs.setText("Select a different graphs source folder");

        javax.swing.GroupLayout jPanelGraphsSettingsLayout = new javax.swing.GroupLayout(jPanelGraphsSettings);
        jPanelGraphsSettings.setLayout(jPanelGraphsSettingsLayout);
        jPanelGraphsSettingsLayout.setHorizontalGroup(
            jPanelGraphsSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGraphsSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelGraphsSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelGraphsSettingsLayout.createSequentialGroup()
                        .addComponent(jTextFieldBrowseGraphs, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonBrowseGraphs))
                    .addComponent(jCheckBoxInfluenceLoopNote)
                    .addComponent(jButtonCreateGraphs, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelBrowseGraphs)
                    .addComponent(jCheckBoxInfluenceBending))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelGraphsSettingsLayout.setVerticalGroup(
            jPanelGraphsSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGraphsSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelBrowseGraphs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelGraphsSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldBrowseGraphs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonBrowseGraphs))
                .addGap(26, 26, 26)
                .addComponent(jCheckBoxInfluenceBending)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBoxInfluenceLoopNote)
                .addGap(52, 52, 52)
                .addComponent(jButtonCreateGraphs)
                .addContainerGap(227, Short.MAX_VALUE))
        );

        jTabbedPaneLicks.addTab("Graphs Settings", jPanelGraphsSettings);

        jLabelBrowseLick.setText("Select a lick to be classified");

        jButtonBrowseLick.setText("Browse...");
        jButtonBrowseLick.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBrowseLickActionPerformed(evt);
            }
        });

        jTextFieldBrowseLick.setEditable(false);
        jTextFieldBrowseLick.setText(app.getUnknownLickFile());

        jButtonClassifyLick.setText("Classify Lick");
        jButtonClassifyLick.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClassifyLickActionPerformed(evt);
            }
        });

        jTableGraphsScores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTableGraphsScores.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTableGraphsScores);

        javax.swing.GroupLayout jPanelLickClassifyLayout = new javax.swing.GroupLayout(jPanelLickClassify);
        jPanelLickClassify.setLayout(jPanelLickClassifyLayout);
        jPanelLickClassifyLayout.setHorizontalGroup(
            jPanelLickClassifyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLickClassifyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelLickClassifyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLickClassifyLayout.createSequentialGroup()
                        .addComponent(jLabelBrowseLick)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanelLickClassifyLayout.createSequentialGroup()
                        .addGroup(jPanelLickClassifyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelLickClassifyLayout.createSequentialGroup()
                                .addGroup(jPanelLickClassifyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelLickClassifyLayout.createSequentialGroup()
                                        .addComponent(jTextFieldBrowseLick, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButtonBrowseLick))
                                    .addGroup(jPanelLickClassifyLayout.createSequentialGroup()
                                        .addComponent(jButtonClassifyLick)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabelBestGraph)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanelLickClassifyLayout.setVerticalGroup(
            jPanelLickClassifyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLickClassifyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelBrowseLick)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLickClassifyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldBrowseLick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonBrowseLick))
                .addGap(19, 19, 19)
                .addGroup(jPanelLickClassifyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonClassifyLick)
                    .addComponent(jLabelBestGraph))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jTabbedPaneLicks.addTab("Classify Lick", jPanelLickClassify);

        jButtonGenerateLicks.setText("Generate Licks");
        jButtonGenerateLicks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenerateLicksActionPerformed(evt);
            }
        });

        jTableGeneratedLicks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableGeneratedLicks.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTableGeneratedLicks.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane3.setViewportView(jTableGeneratedLicks);

        jSpinnerRandomLicksN.setToolTipText("Set the number of random lick that will be generated");
        jSpinnerRandomLicksN.setValue(LickGeneratorSettings.DEFAULT_N_RANDOM_LICKS);

        jLabelRandomLicksN.setText("Random Licks Number");

        jSpinnerBestLicksN.setValue(LickGeneratorSettings.DEFAULT_N_BEST_LICKS);

        jLabelBestLicksN.setText("Best Licks Number");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSpinnerMinNotesN.setValue(LickGeneratorSettings.DEFAULT_LICK_MIN_NOTES);

        jLabelMinNotesN.setText("Min Notes Number");

        jSpinnerMaxNotesN.setValue(LickGeneratorSettings.DEFAULT_LICK_MAX_NOTES);

        jLabelMaxNotesN.setText("Max Notes Number");

        jLabelLickDuration.setText("Lick Duration");

        jTextFieldLickDuration.setText(""+LickGeneratorSettings.DEFAULT_LICK_DURATION);

        javax.swing.GroupLayout jPanelLickGenerateLayout = new javax.swing.GroupLayout(jPanelLickGenerate);
        jPanelLickGenerate.setLayout(jPanelLickGenerateLayout);
        jPanelLickGenerateLayout.setHorizontalGroup(
            jPanelLickGenerateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLickGenerateLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelLickGenerateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanelLickGenerateLayout.createSequentialGroup()
                        .addGroup(jPanelLickGenerateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelMinNotesN)
                            .addComponent(jLabelMaxNotesN)
                            .addComponent(jLabelLickDuration))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelLickGenerateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jSpinnerMaxNotesN, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSpinnerMinNotesN, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldLickDuration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelLickGenerateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelLickGenerateLayout.createSequentialGroup()
                                .addGroup(jPanelLickGenerateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelRandomLicksN)
                                    .addComponent(jLabelBestLicksN))
                                .addGap(18, 18, 18)
                                .addGroup(jPanelLickGenerateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jSpinnerBestLicksN)
                                    .addComponent(jSpinnerRandomLicksN, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jButtonGenerateLicks))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelLickGenerateLayout.setVerticalGroup(
            jPanelLickGenerateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLickGenerateLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelLickGenerateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLickGenerateLayout.createSequentialGroup()
                        .addGroup(jPanelLickGenerateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jSpinnerMinNotesN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelMinNotesN))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelLickGenerateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jSpinnerMaxNotesN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelMaxNotesN))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelLickGenerateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldLickDuration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelLickDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelLickGenerateLayout.createSequentialGroup()
                        .addGroup(jPanelLickGenerateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jSpinnerRandomLicksN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelRandomLicksN))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelLickGenerateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jSpinnerBestLicksN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelBestLicksN))
                        .addGap(18, 18, 18)
                        .addComponent(jButtonGenerateLicks))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
        );

        jTabbedPaneLicks.addTab("Generate Lick", jPanelLickGenerate);

        jLabelVCardinality.setText(GPROP_VCARDINALITY_TEXT);

        jLabelECardinality.setText(GPROP_ECARDINALITY_TEXT);

        jLabelClustAvg.setText(GPROP_CLUSTAVG_TEXT);

        jLabelShortestPathLenghtAvg.setText(GPROP_SHORTESTPATHAVG_TEXT);

        jLabelClustAvgRnd.setText(GPROP_CLUSTAVGRND_TEXT);

        jLabelShortestPathLenghtAvgRnd.setText(GPROP_SHORTESTPATHAVGRND_TEXT);

        javax.swing.GroupLayout jPanelMainLayout = new javax.swing.GroupLayout(jPanelMain);
        jPanelMain.setLayout(jPanelMainLayout);
        jPanelMainLayout.setHorizontalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jButtonDisplayGraph, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBoxWholeGraph, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                        .addComponent(jLabelGraphsList, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabelVCardinality)
                    .addComponent(jLabelECardinality)
                    .addComponent(jLabelClustAvg)
                    .addComponent(jLabelClustAvgRnd)
                    .addComponent(jLabelShortestPathLenghtAvgRnd)
                    .addComponent(jLabelShortestPathLenghtAvg))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPaneLicks))
        );
        jPanelMainLayout.setVerticalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelMainLayout.createSequentialGroup()
                        .addComponent(jTabbedPaneLicks, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanelMainLayout.createSequentialGroup()
                        .addComponent(jLabelGraphsList, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxWholeGraph)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonDisplayGraph)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelVCardinality)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelECardinality)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelClustAvg)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelClustAvgRnd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelShortestPathLenghtAvg)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelShortestPathLenghtAvgRnd)
                        .addGap(40, 40, 40))))
        );

        getContentPane().add(jPanelMain, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private NotesGraph getSelectedGraph() {
		NotesGraph graph;
		if (app.isUseWholeGraph())
			graph = app.getWholeGraph();
		else 
			graph = (NotesGraph)jListGraphs.getSelectedValue();
		
		return graph;
	}
	
	private void createGraphs() {
		NotesGraphSettings settings = new NotesGraphSettings();
		
		boolean isInfluenceBending = jCheckBoxInfluenceBending.isSelected();
		settings.setInfluenceBendings(isInfluenceBending);
		
		boolean isInfluenceLoopNote = jCheckBoxInfluenceLoopNote.isSelected();
		settings.setInfluenceLoopNote(isInfluenceLoopNote);
		
		try {
			app.createGraphs(settings);
		} catch (Exception ex) {
			Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		jListGraphs.setModel(new javax.swing.AbstractListModel() {
			@Override
            public int getSize() { return app.getNotesGraphs().size(); }
			@Override
            public Object getElementAt(int i) {
					return app.getNotesGraphs().get(i);
			}
        });
	}
	
    private void jButtonDisplayGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDisplayGraphActionPerformed
        NotesGraph graph;
		graph = getSelectedGraph();
		
		if (graph == null) {
			JOptionPane.showMessageDialog(this,
				"Select a graph from the graph list or the whole graph.\n",
				"Warning",
				JOptionPane.WARNING_MESSAGE);
		} else {
			
			Viewer viewer = graph.display();
			viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
		}
    }//GEN-LAST:event_jButtonDisplayGraphActionPerformed

    private void jButtonBrowseLickActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBrowseLickActionPerformed
		JFileChooser c = new JFileChooser(app.getDataFolder());
		if (c.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String path = c.getSelectedFile().getAbsolutePath();
			app.setUnknownLickFile(path);
			jTextFieldBrowseLick.setText(path);
		}
    }//GEN-LAST:event_jButtonBrowseLickActionPerformed

    private void jCheckBoxWholeGraphItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxWholeGraphItemStateChanged
      	if (evt.getStateChange() == ItemEvent.SELECTED) {
			jListGraphs.setEnabled(false);
			app.setUseWholeGraph(true);
			drawGraphProperties(app.getWholeGraph());
		} else {
			jListGraphs.setEnabled(true);
			app.setUseWholeGraph(false);
			resetGraphProperies();
		}
    }//GEN-LAST:event_jCheckBoxWholeGraphItemStateChanged

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
		createGraphs();
    }//GEN-LAST:event_formWindowOpened

    private void jButtonClassifyLickActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClassifyLickActionPerformed
		try {
			app.classifyUnknownLick();
		} catch (IOException | TGFileFormatException ex) {
			Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		Object[][] graphsScores = app.getGraphsScores();
		
		jTableGraphsScores.setModel(new javax.swing.table.DefaultTableModel(
            graphsScores,
            new String [] {
                "Graph", "Score"
            }
        ));
		
		jLabelBestGraph.setText("Best: " + (String)graphsScores[app.getBestGraphId()][0]);
		
    }//GEN-LAST:event_jButtonClassifyLickActionPerformed

    private void jButtonGenerateLicksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenerateLicksActionPerformed
        NotesGraph graph;
		graph = getSelectedGraph();
		
		if (graph == null) {
			JOptionPane.showMessageDialog(this,
				"Select a graph from the graph list or the whole graph.\n",
				"Warning",
				JOptionPane.WARNING_MESSAGE);
		} else {
			LickGeneratorSettings settings = new LickGeneratorSettings();
			
			settings.setnRandomLicks((int)jSpinnerRandomLicksN.getValue());
			settings.setnBestLicks((int)jSpinnerBestLicksN.getValue());
			settings.setLickMaxNotes((int)jSpinnerMaxNotesN.getValue());
			settings.setLickMinNotes((int)jSpinnerMinNotesN.getValue());
			String str = jTextFieldLickDuration.getText();
			if (str.matches("[-+]?[0-9]*\\.?[0-9]+")) { 
				settings.setLickDuration(Float.parseFloat(str));
			} else {
				JOptionPane.showMessageDialog(this,
					"The lick duration must be a number!\n",
					"Warning",
					JOptionPane.WARNING_MESSAGE);
			}
			
			
			Object[][] licksScores = app.generateLicks(graph, settings);
			
			jTableGeneratedLicks.setModel(new javax.swing.table.DefaultTableModel(
				licksScores,
				new String [] {
					"Score", "Lick"
				}
			));
			
			for (int i = 0; i < jTableGeneratedLicks.getColumnCount(); i++) {
				DefaultTableColumnModel colModel = (DefaultTableColumnModel) jTableGeneratedLicks.getColumnModel();
				TableColumn col = colModel.getColumn(i);
				int width = 0;

				TableCellRenderer renderer = col.getHeaderRenderer();
				for (int r = 0; r < jTableGeneratedLicks.getRowCount(); r++) {
				  renderer = jTableGeneratedLicks.getCellRenderer(r, i);
				  Component comp = renderer.getTableCellRendererComponent(jTableGeneratedLicks, jTableGeneratedLicks.getValueAt(r, i),
					  false, false, r, i);
				  width = Math.max(width, comp.getPreferredSize().width);
				}
				col.setPreferredWidth(width + 2);
			  }
		}
    }//GEN-LAST:event_jButtonGenerateLicksActionPerformed

    private void jButtonBrowseGraphsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBrowseGraphsActionPerformed
        JFileChooser c = new JFileChooser();
        c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        c.setAcceptAllFileFilterUsed(false);
        if (c.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = c.getSelectedFile().getAbsolutePath();
            app.setGraphsFolderPath(path);
            jTextFieldBrowseGraphs.setText(path);
        }
    }//GEN-LAST:event_jButtonBrowseGraphsActionPerformed

    private void jButtonCreateGraphsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCreateGraphsActionPerformed
        createGraphs();
		resetGraphProperies();
		
    }//GEN-LAST:event_jButtonCreateGraphsActionPerformed

	public void drawGraphProperties(NotesGraph graph){
		jLabelVCardinality.setText(GPROP_VCARDINALITY_TEXT + 
				graph.getNodeSet().size());
		jLabelECardinality.setText(GPROP_ECARDINALITY_TEXT + 
				graph.getEdgeSet().size());
		
		jLabelClustAvg.setText(GPROP_CLUSTAVG_TEXT + 
				String.format("%.3f", Toolkit.averageClusteringCoefficient(graph)));
		
		jLabelShortestPathLenghtAvg.setText(GPROP_SHORTESTPATHAVG_TEXT + 
				String.format("%.3f", app.getShortestPathLenghtAvg(graph)));
		
		Graph rand = app.getRandomEquivalent(graph);
		
		jLabelClustAvgRnd.setText(GPROP_CLUSTAVGRND_TEXT + 
				String.format("%.3f", Toolkit.averageClusteringCoefficient(rand)));
		
		jLabelShortestPathLenghtAvgRnd.setText(GPROP_SHORTESTPATHAVGRND_TEXT + 
				String.format("%.3f", app.getShortestPathLenghtAvg(rand)));
		
	}
	
	public void resetGraphProperies(){
		jLabelVCardinality.setText(GPROP_VCARDINALITY_TEXT);
		jLabelECardinality.setText(GPROP_ECARDINALITY_TEXT);
		jLabelClustAvg.setText(GPROP_CLUSTAVG_TEXT);
		jLabelShortestPathLenghtAvg.setText(GPROP_CLUSTAVG_TEXT);
		jLabelClustAvgRnd.setText(GPROP_CLUSTAVGRND_TEXT);
		jLabelShortestPathLenghtAvgRnd.setText(GPROP_SHORTESTPATHAVGRND_TEXT);
		if (app.isUseWholeGraph())
			drawGraphProperties(app.getWholeGraph());
	}
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBrowseGraphs;
    private javax.swing.JButton jButtonBrowseLick;
    private javax.swing.JButton jButtonClassifyLick;
    private javax.swing.JButton jButtonCreateGraphs;
    private javax.swing.JButton jButtonDisplayGraph;
    private javax.swing.JButton jButtonGenerateLicks;
    private javax.swing.JCheckBox jCheckBoxInfluenceBending;
    private javax.swing.JCheckBox jCheckBoxInfluenceLoopNote;
    private javax.swing.JCheckBox jCheckBoxWholeGraph;
    private javax.swing.JLabel jLabelBestGraph;
    private javax.swing.JLabel jLabelBestLicksN;
    private javax.swing.JLabel jLabelBrowseGraphs;
    private javax.swing.JLabel jLabelBrowseLick;
    private javax.swing.JLabel jLabelClustAvg;
    private javax.swing.JLabel jLabelClustAvgRnd;
    private javax.swing.JLabel jLabelECardinality;
    private javax.swing.JLabel jLabelGraphsList;
    private javax.swing.JLabel jLabelLickDuration;
    private javax.swing.JLabel jLabelMaxNotesN;
    private javax.swing.JLabel jLabelMinNotesN;
    private javax.swing.JLabel jLabelRandomLicksN;
    private javax.swing.JLabel jLabelShortestPathLenghtAvg;
    private javax.swing.JLabel jLabelShortestPathLenghtAvgRnd;
    private javax.swing.JLabel jLabelVCardinality;
    private javax.swing.JList jListGraphs;
    private javax.swing.JPanel jPanelGraphsSettings;
    private javax.swing.JPanel jPanelLickClassify;
    private javax.swing.JPanel jPanelLickGenerate;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSpinner jSpinnerBestLicksN;
    private javax.swing.JSpinner jSpinnerMaxNotesN;
    private javax.swing.JSpinner jSpinnerMinNotesN;
    private javax.swing.JSpinner jSpinnerRandomLicksN;
    private javax.swing.JTabbedPane jTabbedPaneLicks;
    private javax.swing.JTable jTableGeneratedLicks;
    private javax.swing.JTable jTableGraphsScores;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextFieldBrowseGraphs;
    private javax.swing.JTextField jTextFieldBrowseLick;
    private javax.swing.JTextField jTextFieldLickDuration;
    // End of variables declaration//GEN-END:variables
}
