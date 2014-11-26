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

import licknet.graph.NoteNode;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import licknet.utils.Consts;
import licknet.lick.Lick;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.herac.tuxguitar.io.gpx.GPXInputStream;
import org.herac.tuxguitar.io.gtp.GP3InputStream;
import org.herac.tuxguitar.io.gtp.GP4InputStream;
import org.herac.tuxguitar.io.gtp.GP5InputStream;
import org.herac.tuxguitar.io.gtp.GTPSettings;
import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.song.models.TGTrack;
import org.herac.tuxguitar.io.tg.TGInputStream;

/**
*
* @author Matteo Martelli matteomartelli3@gmail.com
*/
public class NotesGraph extends SingleGraph {
	
	private NotesGraphSettings settings;
	
	private final ArrayList<NoteNode> notesSequence;
	private final ArrayList<Lick> licks;
	
	public NotesGraph(String id) {
		super(id);
		this.notesSequence = new ArrayList<NoteNode>();
		this.licks = new ArrayList<Lick>();
	}
	
	public void initFromFile(String filePath) throws Exception {
		NotesGraphSettings defaultSettings = new NotesGraphSettings();
		initFromFile(filePath, defaultSettings);
	}

	public void initFromFolder(String folderPath) throws Exception {
		NotesGraphSettings defaultSettings = new NotesGraphSettings();
		initFromFolder(folderPath, defaultSettings);
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
	
	/* Import the graph from a previous stored graph.
	 * This destroys the graph if it's not empty. */
	public void importFromGraphFile(String filePath) {
		this.clear();
		
	}
	
	public void addFromFile(String filePath) 
			throws Exception {
		TGFactory factory = new TGFactory();
		
		
		FileInputStream file = new FileInputStream(filePath);
		DataInputStream data = new DataInputStream(file);
		TGSong song;
		/* WARNING: Unsupported key signature with guitar pro files */
		if (filePath.endsWith(".gp3")) {
			GTPSettings gtpsettings = new GTPSettings();
			GP3InputStream gp3 = new GP3InputStream(gtpsettings);
			gp3.init(factory, data);
			song = gp3.readSong();
		} else if (filePath.endsWith(".gp4")) {
			GTPSettings gtpsettings = new GTPSettings();
			GP4InputStream gp4 = new GP4InputStream(gtpsettings);
			gp4.init(factory, data);
			song = gp4.readSong();
		} else if (filePath.endsWith(".gp5")) {
			GTPSettings gtpsettings = new GTPSettings();
			GP5InputStream gp5 = new GP5InputStream(gtpsettings);
			gp5.init(factory, data);
			song = gp5.readSong();
		} else if (filePath.endsWith(".gpx")) {
			GPXInputStream gpx = new GPXInputStream();
			gpx.init(factory, data);
			song = gpx.readSong();
		}else if (filePath.endsWith(".tg")) {
			TGInputStream tg = new TGInputStream();
			tg.init(factory, data);
			song = tg.readSong();
		} else {
			file.close();
			data.close();
			throw new Exception("Unsupported file format");
		}
		
		/* Assuming we are interested in the 0th track TODO: what about this? */
		TGTrack track = song.getTrack(0);
		
		int beatCount;
		NoteNode nt, prevNt;
		nt = prevNt = null;
		
		beatCount = 0;
		
		for (int i = 0; i < track.countMeasures(); i++) {
			TGMeasure measure = track.getMeasure(i);
			
			for (int j = 0; j < measure.countBeats(); j++) {
				TGBeat beat = measure.getBeat(j);
				
				TGNote note = beat.getVoice(0).getNote(0);
				
				if (beatCount > 0)
					prevNt = (NoteNode)notesSequence.get(beatCount - 1);
				
				/* If the node is tied, merge it to the previous one */
				if (note != null && prevNt != null && note.isTiedNote()) {
					String PPk, Pk, Nk, Ek;
					Pk = prevNt.getNodeKey();
					
					/* Copy the previous note and merge it with the new one */
					NoteNode newNote = new NoteNode(prevNt);
					newNote.mergeNote(note);
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
							if (ojumps[prevOjumpId] > 0) {
								ojumps[prevOjumpId]--;
								int sum = 0;
								for (int k = 0; k < ojumps.length; k++)
									sum += ojumps[k];
								if (sum == 0) {
									this.removeEdge(edge);
									if (this.getNode(Pk).getDegree() == 0)
										this.removeNode(Pk);
								}
							} else {
								ojumps[prevOjumpId] = 0;
								System.err.println("WARNING: Negative weight!");
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
					nt = new NoteNode(track, measure, beat, note, 
							settings.isInfluenceBendings());

					notesSequence.add(nt);
					if (this.getNode(nt.getNodeKey()) == null)
						this.addNode(nt.getNodeKey(), nt);
					
					/* Add the note to the graph and the edge between this note
					 * and the previous note. */
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
		file.close();
		data.close();
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
		Node node = this.addNode(key);
		node.addAttribute("note", note);
		return node;
	}
	
	/* TODO: addEdge(..., int[] ojumps)*/
	
	/* If the graph is empty this silently do nothing.
	 * WARNING: This will erase the previous array of licks */
	public void findLicks() {
		licks.clear();
		
		ArrayList<Node> startingNodes;
		Node snode;
		int nStartNodes = settings.getnStartNodes();
		startingNodes = new ArrayList<Node>();
		
		if (this.nodeCount == 0)
			return;
		
		/* TODO: this can be done copying the array of notes from the graph
		 * if there is a way to get it 
		 */
		for (Node node : this.getEachNode()) {
			startingNodes.add(node);
		}
		
		Collections.sort(startingNodes, new DegreeComparator());
		
		if (startingNodes.size() < nStartNodes)
			nStartNodes = startingNodes.size();
		
		/* Make a copy of the edges */
		for (Edge e : this.getEachEdge()) {
			int[] ojumpsOld = e.getAttribute("ojumps");
			int[] ojumpsBak = new int[ojumpsOld.length];
			System.arraycopy(ojumpsOld, 0, ojumpsBak, 0, ojumpsOld.length);
					
			e.addAttribute("ojumpsBak", ojumpsBak);
		}
				
		snode = (Node)startingNodes.get(0);
		/* For each node of the greatest degree set perform a visit 
		 * starting from that node */
		for (int i = 0; i < nStartNodes && snode != null; i++) {
			snode = (Node)startingNodes.get(i);
			
			for (int j = 0; j < settings.getLickBranching(); j++) {
			
				Lick lick = lickVisit(snode, j);
				
				resetEdgesWeights();
				/* Check if the lick is in the notesSequence */
				//NoteNode fstNode = lick.getNotes().get(0);
				//ArrayList<Integer> startIndexes = getStartIndexes(fstNode.getNodeKey());
				
				/* Brute force match search */
				for (int si = 0; si < (notesSequence.size() - lick.getNotes().size()); si++) {
					if (matchLick(lick, si) > settings.getMinMatchRate())
						lick.incrementOccurrences();
				}
				
				if (lick.getOccurrences() > 0 && 
						lick.getNotes().size() >= settings.getLicksMinNotes())
					licks.add(lick);
			}
		}
	}	

	public void resetEdgesWeights() {
		for (Edge e : this.getEachEdge()) {
			int[] ojumpsBak = e.getAttribute("ojumpsBak");
			int[] ojumps = e.getAttribute("ojumps");
			
			if (!Arrays.equals(ojumpsBak, ojumps))
				System.arraycopy(ojumpsBak, 0, ojumps, 0, ojumpsBak.length);
		}
			
	}

	public Lick lickVisit(Node snode, int weightOrderId){
		int prevOctave, ojumpId, ojump;
		int[] ojumps;
		float goalDuration, incDuration;
		NoteNode note;
		Lick lick = new Lick();
		ArrayList<String> visitedNotes = new ArrayList<String>();

		note = null;
		goalDuration = settings.getLickDuration();
		incDuration = 0;
		prevOctave = ((NoteNode)(snode.getAttribute("note"))).getOctave();
		ojumpId = Consts.N_OCTAVES; /* The starting note has ojump of 0 */
		
		
		while (incDuration < goalDuration && 
				lick.getNotes().size() < settings.getLickMaxNotes() 
				&& snode != null) {
			
			note = new NoteNode((NoteNode)(snode.getAttribute("note")));
			
			if (settings.isInfluenceLoopNote() 
					|| !visitedNotes.contains(note.getNodeKey())) {
				ojump = ojumpId - Consts.N_OCTAVES;
				prevOctave += ojump;
				/* FIXME: Wrong octave with loopnote influence */
				note.setOctave(prevOctave);
				lick.getNotes().add(note);
				visitedNotes.add(note.getNodeKey());
				incDuration += note.getTime();
			} 
			
			/* Copy all the leaving edges of the current node and sort them 
			 * in their weight descending order. */
			ArrayList<Edge> leavingEdges = new ArrayList<Edge>(snode.getLeavingEdgeSet());
			Collections.sort(leavingEdges, new WeightComparator());
			if (leavingEdges.size() == 0) {
				snode = null;
			} else {
				int idx = weightOrderId <= leavingEdges.size() - 1? 
						weightOrderId :
						leavingEdges.size() - 1;
				Edge edgeThrough = leavingEdges.get(idx);
				ojumps = edgeThrough.getAttribute("ojumps");
				ojumpId = WeightComparator.maxOctaveJump(ojumps);
				ojumps[ojumpId]--;
				snode = edgeThrough.getTargetNode();
			}
		}
		
		lick.setDuration(incDuration);
		return lick;
	}
	

	public ArrayList<NoteNode> getNotesSequence() {
		return notesSequence;
	}

	public ArrayList<Lick> getLicks() {
		return licks;
	}

	
}