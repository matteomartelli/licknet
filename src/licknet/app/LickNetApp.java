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
package licknet.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import licknet.graph.NoWeightEdgeException;
import licknet.graph.NoteNode;
import licknet.graph.NotesGraph;
import licknet.graph.NotesGraphSettings;
import licknet.lick.Lick;
import licknet.lick.LickClassifier;
import licknet.lick.LickGenerator;
import licknet.lick.LickGeneratorSettings;
import licknet.lick.LickGraphScore;
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.herac.tuxguitar.io.base.TGFileFormatException;

/**
 *
 * @author Matteo Martelli matteomartelli3@gmail.com
 */
public class LickNetApp {
	private static LickNetApp instance = null;
	
	private final String DEFAULT_DATA_FOLDER = "example_data";
	private final String DEFAULT_GRAPHS_FOLDER_PATH = DEFAULT_DATA_FOLDER + "/tracks";
	private final String DEFAULT_UNKNOWN_LICK_FILE = DEFAULT_DATA_FOLDER + "/licks/unknown_lick.tg";
	
	private String dataFolder = DEFAULT_DATA_FOLDER;
	private String graphsFolderPath = DEFAULT_GRAPHS_FOLDER_PATH;
	private String unknownLickFile = DEFAULT_UNKNOWN_LICK_FILE;
	private final ArrayList<NotesGraph> notesGraphs = new ArrayList<>();
	/* TODO: relate the best licks to each graph */
	private ArrayList<LickGraphScore> currentBestLicks = new ArrayList<>();
	private final NotesGraph wholeGraph = new NotesGraph("Whole");
	private boolean useWholeGraph = false;
	private NotesGraphSettings settings;	
	private LickClassifier lickClassifier;
	
	protected LickNetApp() {}
	
	public static LickNetApp getInstance() {
		if (instance == null) {
			instance = new LickNetApp();
		}
		return instance;
	}

	public String getDataFolder() {
		return dataFolder;
	}

	public void setDataFolder(String dataFolder) {
		this.dataFolder = dataFolder;
	}
	
	public String getGraphsFolderPath() {
		return graphsFolderPath;
	}

	public void setGraphsFolderPath(String graphsFolderPath) {
		this.graphsFolderPath = graphsFolderPath;
	}

	public String getUnknownLickFile() {
		return unknownLickFile;
	}

	public void setUnknownLickFile(String unknownLickFile) {
		this.unknownLickFile = unknownLickFile;
	}
	
	public ArrayList<NotesGraph> getNotesGraphs() {
		return notesGraphs;
	}	
	
	public void createGraphs(NotesGraphSettings settings) throws Exception {
		this.settings = settings;
		
		/* Clear the previous structures, if non-empty */
		for (NotesGraph graph : notesGraphs)
			graph.clear();	
		wholeGraph.clear();
		notesGraphs.clear();
		
		
		File dir = new File(graphsFolderPath);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			/* Create N graphs, where N is the number of subfolders 
			 * of graphsFolderPath. */
			for (File child : directoryListing) {
				if (child.isDirectory()) {
					NotesGraph ng = new NotesGraph(child.getName());
					ng.initFromFolder(child.getPath(), settings);
					if (ng.getNodeCount() > 0)
						notesGraphs.add(ng);
				}
			}
			/* Create the whole graph that includes all the other N graphs. */
			wholeGraph.initFromSubFolders(graphsFolderPath, settings);
		}
	}

	public void classifyUnknownLick() 
			throws IOException, TGFileFormatException {
		Lick uLick = new Lick();
		uLick.importFromFile(unknownLickFile, settings);
		lickClassifier = new LickClassifier(notesGraphs, uLick);
		lickClassifier.classify();	
	}
	
	public Object[][] getGraphsScores() {
		Object[][] graphsScores = new Object[lickClassifier.getGraphs().size()][2];
		
		for (int i = 0; i < lickClassifier.getGraphs().size(); i++) {
			LickGraphScore lgs = lickClassifier.getGraphs().get(i);
			graphsScores[i][0] = lgs.getGraph().getId();
			graphsScores[i][1] = lgs.getScore();
		}
		
		return graphsScores;
	} 
	
	public Object[][] generateLicks(NotesGraph graph, LickGeneratorSettings settings) {
		LickGenerator lickGenerator = new LickGenerator(graph, settings);
		
		try {
			lickGenerator.generate();
		} catch (NoWeightEdgeException ex) {
			/* TODO: delete noweightedge from the graph */
			Logger.getLogger(LickNetApp.class.getName()).log(Level.SEVERE, null, ex);
		}
		currentBestLicks.clear();
		currentBestLicks = lickGenerator.getBestLicks();
		
		Object[][] lickScores = new Object[currentBestLicks.size()][2];
		for (int i = 0; i < currentBestLicks.size(); i++) {
			LickGraphScore lgs = currentBestLicks.get(i);
			lickScores[i][0] = lgs.getScore();
			lickScores[i][1] = "";
			for (int j = 0; j < lgs.getLick().getNotes().size(); j++) {
				NoteNode nt = lgs.getLick().getNotes().get(j);
				lickScores[i][1] += nt.getNodeKey() + ":o" + nt.getOctave() + " ";
			}
		}
		return lickScores;
	}
	
	public Graph getRandomEquivalent(Graph graph) {
		Graph g = new SingleGraph(graph.getId() + "_random");
		
		for (Node n : graph)
			g.addNode(n.getId());
		
		for (int i = 0; i < graph.getEdgeCount(); i++) {
			Node A = Toolkit.randomNode(g);
			Node B = Toolkit.randomNode(g);
			String Ek = A.getId()+B.getId();
			String REk = B.getId()+A.getId();
			if (g.getEdge(Ek) == null && g.getEdge(REk) == null)
				g.addEdge(A.getId()+B.getId(), A, B);
		}
		return g;
	}
	
	public double getShortestPathLenghtAvg(Graph graph) {
		Graph g = new SingleGraph(graph.getId() + "_dijkstra");
		for (Node n : graph) {
			String Ak = n.getId();
			if (g.getNode(Ak) == null)
				g.addNode(Ak);
			for (Edge e : n.getEachLeavingEdge()) {
				String Bk = e.getTargetNode().getId();
				String Ek = Ak + Bk;
				String REk = Bk + Ak;
				if ( g.getEdge(Ek) == null && g.getEdge(REk) == null) {
					if (g.getNode(Bk) == null)
						g.addNode(Bk);
					g.addEdge(Ek, Ak, Bk).addAttribute("length", 1);
				}
			}
		}
		double sum = 0.0;
		
		for (Node i : g) {
			Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "length");
			// Compute the shortest paths in g from A to all nodes
			dijkstra.init(g);
			dijkstra.setSource(i);
			
			dijkstra.compute();
			for (Node j : g) {
				if (i.getId() != j.getId()) 
					sum += dijkstra.getPathLength(j); //dij
			}
		}
		double N = (double)(g.getNodeSet().size());
		double L = sum / (N * (N - 1.0));
		return L;
	}
	
	public ArrayList<LickGraphScore> getCurrentBestLicks() {
		return currentBestLicks;
	}
	
	public int getBestGraphId() {
		return lickClassifier.getBestGraphId();
	}
	
	public NotesGraph getWholeGraph() {
		return wholeGraph;
	}

	public boolean isUseWholeGraph() {
		return useWholeGraph;
	}

	public void setUseWholeGraph(boolean useWholeGraph) {
		this.useWholeGraph = useWholeGraph;
	}
}
