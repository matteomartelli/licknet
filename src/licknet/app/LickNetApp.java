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
import java.util.ArrayList;
import licknet.graph.NotesGraph;

/**
 *
 * @author Matteo Martelli matteomartelli3@gmail.com
 */
public class LickNetApp {
	private static LickNetApp instance = null;
	
	private static final String DEFAULT_GRAPHS_FOLDER = "data";
	
	private static String graphsFolder = DEFAULT_GRAPHS_FOLDER;
	private static final ArrayList<NotesGraph> notesGraphs = new ArrayList<>();
	
	
	protected LickNetApp() {}
	
	public static LickNetApp getInstance() {
		if (instance == null) {
			instance = new LickNetApp();
		}
		return instance;
	}

	public String getGraphsFolder() {
		return graphsFolder;
	}

	public void setGraphsFolder(String graphsFolder) {
		LickNetApp.graphsFolder = graphsFolder;
	}

	public ArrayList<NotesGraph> getNotesGraphs() {
		return notesGraphs;
	}	
	
	public void createGraphs() throws Exception {
		notesGraphs.clear();
		File dir = new File(graphsFolder);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				if (child.isDirectory()) {
					NotesGraph ng = new NotesGraph(child.getName());
					ng.initFromFolder(child.getPath());
					notesGraphs.add(ng);
				}
			}
		}
	}
}
