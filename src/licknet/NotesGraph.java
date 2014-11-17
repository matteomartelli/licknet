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
package licknet;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.herac.tuxguitar.io.gtp.GP5InputStream;
import org.herac.tuxguitar.io.gtp.GTPSettings;
import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.song.models.TGTrack;

/**
*
* @author Matteo Martelli matteomartelli3@gmail.com
*/


class NodeNext {
	private final int ojumpId;
	private final Node nodeNext;
	private final Edge edgeThrough;
	
	NodeNext(int ojumpId, Node nextNode, Edge edgeThrough) {
		this.ojumpId = ojumpId;
		this.nodeNext = nextNode;
		this.edgeThrough = edgeThrough;
	}

	public int getOjumpId() {
		return ojumpId;
	}

	public Node getNextNode() {
		return nodeNext;
	}

	public Edge getEdgeThrough() {
		return edgeThrough;
	}
	
}

class DegreeComparator implements Comparator<Node> {
    @Override
    public int compare(Node o1, Node o2) {
		return o2.getOutDegree() - o1.getOutDegree();
    }
}

public class NotesGraph extends SingleGraph {
	private ArrayList<NoteNode> notesSequence;
	private ArrayList<Lick> licks;
	
	public NotesGraph(String id) {
		super(id);
		this.notesSequence = new ArrayList<NoteNode>();
	}
	
	/* Initialize the graph from a tab file.
	 * This destroys the graph if it's not empty.
	 * */
	public void initFromFile(String filePath) 
			throws FileNotFoundException, TGFileFormatException  {
		
		this.clear();
		this.notesSequence.clear();
		addFromFile(filePath);
	}
	
	/* Initialize the graph from a folder containing tab files. 
	 * This destroys the graph if it's not empty.
	 * */
	public void initFromFolder(String folderPath) {
		this.clear();
		
	}
	
	/* Import the graph from a previous stored graph.
	 * This destroys the graph if it's not empty. */
	public void importFromGraphFile(String filePath) {
		this.clear();
		
	}
	
	public void addFromFile(String filePath) 
			throws FileNotFoundException, TGFileFormatException {
		TGFactory factory = new TGFactory();
		
		/* TODO GP5 appears not to support the key signature info */
		FileInputStream file = new FileInputStream(filePath);
		DataInputStream data = new DataInputStream(file);
		
		GTPSettings gtpsettings = new GTPSettings();
		GP5InputStream gp5 = new GP5InputStream(gtpsettings);
		gp5.init(factory, data);
		
		TGSong song = gp5.readSong();
		
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
				
				nt = new NoteNode(track, measure, beat, note);
				
				/* TODO 
				 * - merge the tied notes */
				
				notesSequence.add(nt);
				if (this.getNode(nt.getNodeKey()) == null) {
					Node node = this.addNode(nt.getNodeKey());
					node.addAttribute("note", nt);
				}
				
				if (beatCount > 0) {
					String Ak, Bk, Ek;
					Edge edge;
					int[] ojumps;
					int ojumpId;
					
					prevNt = (NoteNode)notesSequence.get(beatCount - 1);
					Ak = prevNt.getNodeKey();
					Bk = nt.getNodeKey();
					Ek = Ak+"->"+Bk;
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

	/* If the graph is empty this silently do nothing */
	public void findLicks() {
		licks.clear();
		
		ArrayList<Node> startingNodes;
		float goalDuration, incDuration;
		int nStartNotes, prevOctave, ojumpId, ojump;
		int[] ojumps;
		Node snode;
		goalDuration = Consts.LICK_DURATION;
		nStartNotes = Consts.N_START_NODES;
		
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
		
		if (startingNodes.size() < nStartNotes)
			nStartNotes = startingNodes.size();
		
		/* Make a copy of the edges */
		for (Edge e : this.getEachEdge()) {
			int[] ojumpsOld = e.getAttribute("ojumps");
			int[] ojumpsBak = new int[ojumpsOld.length];
			System.arraycopy(ojumpsOld, 0, ojumpsBak, 0, ojumpsOld.length);
					
			e.addAttribute("ojumpsBak", ojumpsBak);
		}
				
		/* For each node of the greatest degree set perform a visit 
		 * starting from that node */
		for (int i = 0; i < nStartNotes; i++) {
			snode = (Node)startingNodes.get(i);
			incDuration = 0;
			Lick lick = new Lick();
			prevOctave = ((NoteNode)(snode.getAttribute("note"))).getOctave();
			ojumpId = Consts.N_OCTAVES; /* The starting note has ojump of 0 */
			while (incDuration < goalDuration && snode != null) {
				
				NoteNode note = (NoteNode)(snode.getAttribute("note"));
				ojump = ojumpId - Consts.N_OCTAVES;
				prevOctave += ojump;
				note.setOctave(prevOctave);
				lick.getNotes().add(note);
				
				incDuration += note.getTime();
				NodeNext nnext = nextNode(snode);
				if (nnext == null) {
					snode = null;
				} else {
					ojumps = nnext.getEdgeThrough().getAttribute("ojumps");
					ojumpId = nnext.getOjumpId();
					ojumps[ojumpId]--;
					snode = nnext.getNextNode();
				}
			}
			
			resetEdgesWeights();
			lick.setDuration(incDuration);
			
			/* TODO check if the lick is present in the notesSequence */
			
			licks.add(lick);
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


	/* Find the max octave jump for an edge. 
	 * Return the id of the maximum 
	 */
	public int maxOctaveJump(int[] ojumps) {
		int max, maxIdx;
		max = maxIdx = 0;
		for (int i = 0; i < ojumps.length; i++) {
				if (ojumps[i] > max) {
					max = ojumps[i];
					maxIdx = i;
				}
		}
		
		return maxIdx;
	}
	
	public NodeNext nextNode(Node node) {
		Edge bestEdge;
		int[] ojumps, maxOjumps;
		int maxW, maxWId, edgeMaxWId;
		
		ojumps = maxOjumps = null;
		maxW = maxWId = edgeMaxWId = 0;
		bestEdge = node.getLeavingEdge(0);
		
		for (Edge edge : node.getEachLeavingEdge()) {
			ojumps = edge.getAttribute("ojumps");
			edgeMaxWId = maxOctaveJump(ojumps);
			if (ojumps[edgeMaxWId] > maxW) {
				maxOjumps = ojumps;
				maxWId = edgeMaxWId;
				maxW = maxOjumps[maxWId];
				bestEdge = edge;
			}
		}
		
		if (bestEdge == null || maxOjumps[maxWId] <= 0)
			return null;
		else 
			return new NodeNext(maxWId, bestEdge.getTargetNode(), bestEdge);
	}

	public ArrayList<NoteNode> getNotesSequence() {
		return notesSequence;
	}

	public ArrayList<Lick> getLicks() {
		return licks;
	}

	
}