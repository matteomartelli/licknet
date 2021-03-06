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
package licknet.graph;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import licknet.io.SongFileLoader;
import licknet.utils.Consts;
import licknet.lick.Lick;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGTrack;

/**
*
* @author Matteo Martelli matteomartelli3@gmail.com
*/
public class NotesGraph extends SingleGraph {
	
	private NotesGraphSettings settings;
	
	/* TODO: this should be freed, too much memory needed */
	private final ArrayList<NoteNode> notesSequence;  
	
	public NotesGraph(String id) {
		super(id);
		this.notesSequence = new ArrayList<NoteNode>();
	}
	
	public void initFromFile(String filePath) throws Exception {
		NotesGraphSettings defaultSettings = new NotesGraphSettings();
		initFromFile(filePath, defaultSettings);
	}

	public void initFromFolder(String folderPath) throws Exception {
		NotesGraphSettings defaultSettings = new NotesGraphSettings();
		initFromFolder(folderPath, defaultSettings);
	}
	
	
	public void initFromSubFolders(String folderPath) throws Exception {
		NotesGraphSettings defaultSettings = new NotesGraphSettings();
		initFromSubFolders(folderPath, defaultSettings);
	}
	
	/* Initialize the graph from a tab file.
	 * This destroys the graph if it's not empty.
	 * */
	public void initFromFile(String filePath, NotesGraphSettings settings) 
			throws Exception  {
		this.settings = settings;
		this.clear();
		this.notesSequence.clear();
		addFromFile(filePath);
	}
	
	/* Initialize the graph from a folder containing tab files. 
	 * This destroys the graph if it's not empty.
	 * */
	public void initFromFolder(String folderPath, NotesGraphSettings settings) 
			throws Exception {
		this.settings = settings;
		this.clear();
		addFromFolder(folderPath);
	}
	
	/* Initialize the graph from from the 1 level sub-folders contained in the given path. 
	 * This destroys the graph if it's not empty.
	 * */
	public void initFromSubFolders(String folderPath, NotesGraphSettings settings) 
			throws Exception {
		this.settings = settings;
		this.clear();
		File dir = new File(folderPath);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				if (child.isDirectory())
					addFromFolder(child.getPath());
			}
		} else {
			throw new Exception("Not a directory");
		}
	}
	
	/* Import the graph from a previous stored graph.
	 * This destroys the graph if it's not empty. */
	public void importFromGraphFile(String filePath) {
		this.clear();
		/* TODO */
	}
	
	public void addFromFile(String filePath) 
			throws IOException, TGFileFormatException {
		
		SongFileLoader songFile = new SongFileLoader(filePath);
		
		/* Assuming we are interested in the 0th track TODO: what about this? */
		TGTrack track = songFile.getSong().getTrack(0);
		
		
		int beatCount;
		NoteNode nt, prevNt;
		nt = prevNt = null;
		
		beatCount = notesSequence.size();
		
		for (int i = 0; i < track.countMeasures(); i++) {
			TGMeasure measure = track.getMeasure(i);
			
			for (int j = 0; j < measure.countBeats(); j++) {
				TGBeat beat = measure.getBeat(j);
				
				TGNote tgNote = beat.getVoice(0).getNote(0);
				
				if (beatCount > 0)
					prevNt = (NoteNode)notesSequence.get(beatCount - 1);
				
				/* If the node is tied, merge it to the previous one */
				if (tgNote != null && prevNt != null && tgNote.isTiedNote()) {
					String PPk, Pk, Nk, Ek;
					Pk = prevNt.getNodeKey();
					
					/* Copy the previous tgNote and merge it with the new one */
					NoteNode newNote = new NoteNode(prevNt);
					newNote.mergeNote(tgNote);
					Nk = newNote.getNodeKey();
					
					/* Remove the previous one from the notes sequence and add 
					 * the new one to it */
					this.notesSequence.remove(this.notesSequence.size() - 1);
					this.notesSequence.add(newNote);
					
					/* If some notes just after the first one are tied. */
					if (beatCount < 2) {
						/* Just remove the first one (if it has degree 0)
						 * and add the new one. */
						if (this.getNode(Pk).getDegree() == 0)
							this.removeNode(Pk);
						
						this.addNode(Nk, newNote);
					} else {
						NoteNode pprevNt = (NoteNode)notesSequence.get(beatCount - 2);
						PPk = pprevNt.getNodeKey();
						Ek = generateEdgeKey(PPk, Pk);
						
						/* Find the octave weight between the pprev and prev */
						int prevOjumpId = prevNt.getOctave() - pprevNt.getOctave() + 
						  Consts.N_OCTAVES;
						
						/* Remove the previously added octave weight from the 
						 * pprev -> prev edge. If the edge becomes with no weight
						 * remove it. */
						Edge edge = this.getEdge(Ek);
						int[] ojumps;
						if (edge != null) {
							ojumps = edge.getAttribute("ojumps");
							if (ojumps[prevOjumpId] > 0) 
								ojumps[prevOjumpId]--;
							else if (ojumps[prevOjumpId] < 0)
								System.err.println("WARNING: Negative weight!");	
							
							int sum = 0;
							for (int k = 0; k < ojumps.length; k++)
								sum += ojumps[k];
							if (sum == 0) {
								this.removeEdge(edge);
								if (this.getNode(Pk).getDegree() == 0)
									this.removeNode(Pk);
							}
							
						}
						
						/* Create a new node if it does not exist */
						if (this.getNode(Nk) == null)
							this.addNode(Nk, newNote);		
							
						/* Create a new pprev -> new edge if it doesn't exists. 
						 * Also add the weight just removed from the other edge.
						 */
						Ek = generateEdgeKey(PPk, Nk);
						if ((edge = this.getEdge(Ek)) == null) {
							edge = this.addEdge(Ek, PPk, Nk, true);
							ojumps = new int[Consts.N_OCTAVES * 2 + 1];
							edge.addAttribute("ojumps", ojumps);
						} else {
							ojumps = edge.getAttribute("ojumps");
						}
						
						ojumps[prevOjumpId]++;
					}	
				} else {
					nt = new NoteNode(track, measure, beat, tgNote, 
							settings.isInfluenceBendings());
					
					/* Skip the repeated notes if specified in the settings */
					if (prevNt != null && !settings.isInfluenceLoopNote() && 
							nt.getNodeKey().equals(prevNt.getNodeKey()))
						continue;

					notesSequence.add(nt);
					if (this.getNode(nt.getNodeKey()) == null)
						this.addNode(nt.getNodeKey(), nt);
					
					/* Add the tgNote to the graph and the edge between this tgNote
					 * and the previous tgNote. */
					if (prevNt != null && beatCount > 0) {
						String Ak, Bk, Ek;
						Edge edge;
						int[] ojumps;
						int ojumpId;
						
						Ak = prevNt.getNodeKey();
						Bk = nt.getNodeKey();
						Ek = generateEdgeKey(Ak, Bk);
						ojumpId = nt.getOctave() - prevNt.getOctave() + 
								  Consts.N_OCTAVES;
						
						/* Create the edge if it doesn't exist yet.
						 * Otherwise get back the edge and modify its weights */
						if ((edge = this.getEdge(Ek)) == null) {
							edge = this.addEdge(Ek, Ak, Bk, true);
							
							/* Create the octave jump weights array for this edge,
							 * increment the first jump weight for this edge
							 * and assign the array to this edge.
							*/
							ojumps = new int[Consts.N_OCTAVES * 2 + 1];
							ojumps[ojumpId]++;
							edge.addAttribute("ojumps", ojumps);
						} else {
							ojumps = edge.getAttribute("ojumps");
							ojumps[ojumpId]++;
						}
					}
					beatCount++;
				}
			}
		}
		
		for (Node n : this.getEachNode()) {
			if (n.getDegree() == 0) {
				this.removeNode(n);
			}
		}
	}

	public void addFromFolder(String folderPath) throws Exception {
		File dir = new File(folderPath);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				if (child.isFile())
					addFromFile(child.getPath());
			}
		} else {
			throw new Exception("Not a directory");
		}
	}
	
	private String generateEdgeKey(String Ak, String Bk) {
		return Ak + "->" + Bk;
	}
	
	/* TODO: implement this creating an hashmap of the notesSequence array 
	 * where each value has key made with the noteKey and 
	 * its pos in the notesSequence. */
	private ArrayList<Integer> getStartIndexes(String key) {		
		ArrayList<Integer> indexes = new ArrayList<Integer>(); 
		
		for (int i = 0; i < this.notesSequence.size(); i++) {
			NoteNode nt = this.notesSequence.get(i);
			if (nt.getNodeKey().equals(key)) {
				indexes.add(i);
			}
		}
		return indexes;
	}
	
	/* Return true if a lick matches in the notesSequcence array starting 
	 * from the gives startIdx. 
	*/
	private float matchLick(Lick lick, int startIdx) {
		int matchedNotes = 0;
		
		if (lick.getNotes().size() == 0)
			return 0;
		
		for (int i = 0, j = startIdx; i < lick.getNotes().size(); i++,j++) {
			if (lick.getNotes().get(i).getNodeKey() == 
					this.notesSequence.get(j).getNodeKey()) {
				matchedNotes++;
			}
		}
		
		return (float)matchedNotes / (float)lick.getNotes().size();
	}
	
	/* Overridden method used to add a NoteNode to a Node during its creation */
	public Node addNode(String key, NoteNode note) {
		Node node = addNode(key);
		node.addAttribute("note", note);
		node.addAttribute("ui.label", node.getId());
		return node;
	}
	
	@Override
	public void clear() {
		notesSequence.clear();
		super.clear();
	}
	
	public Edge getEdge(String Ak, String Bk) {
		return getEdge(generateEdgeKey(Ak, Bk));
	}
	
	public Edge getEdge(Node A, Node B) {
		return getEdge(A.getId(), B.getId());
	}
	
	public ArrayList<NoteNode> getNotesSequence() {
		return notesSequence;
	}
	
	public int getMaxWeight() {
		int max, w;
		WeightEdge wedge;
		max = 0;
		
		for (Edge e : getEachEdge()) {
			wedge = new WeightEdge(e);
			w = wedge.getMaxOctaveJump();
			if (w > max)
				max = w;
		}
		
		return max;
	}
}