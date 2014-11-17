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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.herac.tuxguitar.io.gtp.GP5InputStream;
import org.herac.tuxguitar.io.gtp.GTPSettings;
import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.song.models.TGTrack;
import org.herac.tuxguitar.song.models.effects.TGEffectBend;
import org.herac.tuxguitar.song.models.effects.TGEffectBend.BendPoint;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.swingViewer.Viewer;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

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

class Utils {
	Utils () { }
	
	public void resetEdgesWeights(Graph graph) {
		for (Edge e : graph.getEachEdge()) {
			int[] ojumpsBak = e.getAttribute("ojumpsBak");
			int[] ojumps = e.getAttribute("ojumps");
			
			if (!Arrays.equals(ojumpsBak, ojumps))
				System.arraycopy(ojumpsBak, 0, ojumps, 0, ojumpsBak.length);
		}
			
	}
	
	public int bendAvg(TGEffectBend bend, int baseNote) {
		int npoints, max_idx, bendNote;
		int[] notes = new int[Consts.N_GUITAR_FRETS];

		Iterator it = bend.getPoints().iterator();
		while (it.hasNext()) {
			BendPoint point = (BendPoint)it.next();
			bendNote = baseNote + (point.getValue() / 2); 
			notes[bendNote]++;
		}
		
		max_idx = 0;
		for (int i = 0; i < notes.length; i++)
			if (notes[i] > notes[max_idx])
				max_idx = i;
		
		return max_idx;
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
}

class DegreeComparator implements Comparator<Node> {
    @Override
    public int compare(Node o1, Node o2) {
		return o2.getOutDegree() - o1.getOutDegree();
    }
}

public class Licknet {
	
	/**
	 * @param args the command line arguments
	 * @throws Exception 
	 */
	public static void main(String[] args) 
			throws Exception {
		
		int beatCount;
		NoteNode nt, prevNt;
		nt = prevNt = null;
		
		Utils utils = new Utils();
		TGFactory factory = new TGFactory();
		
		/* TODO GP5 appears not to support the key signature info */
		FileInputStream file = new FileInputStream("data/road.gp5");
		DataInputStream data = new DataInputStream(file);
		
		GTPSettings gtpsettings = new GTPSettings();
		GP5InputStream gp5 = new GP5InputStream(gtpsettings);
		gp5.init(factory, data);
		
		TGSong song = gp5.readSong();
		
		ArrayList noteNodes = new ArrayList();
		Graph graph = new SingleGraph("licknet");
		
		/* Assuming we are interested in the 0th track */
		TGTrack track;
		TGMeasure measure;
		
		track = song.getTrack(0);
		beatCount = 0;
		System.out.println("string\tnote\tbend\tbnote\tdura\toctave");
		for (int i = 0; i < track.countMeasures(); i++) {
			measure = track.getMeasure(i);
			
			for (int j = 0; j < measure.countBeats(); j++) {
				TGBeat beat = measure.getBeat(j);
				
				TGNote note = beat.getVoice(0).getNote(0);
				
				if (note != null) {
					if (note.getEffect().isBend()) {

						int bendAvgNote;
						/* Get the most played note in a bending.
						 * TODO: Would it be better if I split the bended note in 
						 * more notes?
						*/
						bendAvgNote = utils.bendAvg(note.getEffect().getBend(), 
									  note.getValue());
						note.setValue(bendAvgNote);
					} 
					System.out.print(note.getString() 
								       + "\t" + note.getValue() 
								       + "\t" + note.getEffect().isBend());
				} else {
					System.out.print("REST\tREST\tREST");
				}
				
				nt = new NoteNode(track, measure, beat, note);
				System.out.printf("\t%d\t%.3f\t%d\t%s\n", 
								  nt.getBaseNote(), 
							      nt.getTime(), 
								  nt.getOctave(),
								  nt.getNodeKey());
				
				/* TODO 
				 * - merge the tied notes */
				
				noteNodes.add(nt);
				if (graph.getNode(nt.getNodeKey()) == null) {
					Node node = graph.addNode(nt.getNodeKey());
					node.addAttribute("note", nt);
				}
				
				if (beatCount > 0) {
					String Ak, Bk, Ek;
					Edge edge;
					int[] ojumps;
					int ojumpId;
					
					prevNt = (NoteNode)noteNodes.get(beatCount - 1);
					Ak = prevNt.getNodeKey();
					Bk = nt.getNodeKey();
					Ek = Ak+"->"+Bk;
					ojumpId = nt.getOctave() - prevNt.getOctave() + 
							  Consts.N_OCTAVES;
					
					/* Create the edge if it doesn't exist yet.
					 * Otherwise get back the edge and modify its weights */
					if (graph.getEdge(Ek) == null) {
						edge = graph.addEdge(Ek, Ak, Bk, true);
						
						/* Create the octave jump weights array for this edge,
						 * increment the first jump weight for this edge
						 * and assign the array to this edge.
						*/
						ojumps = new int[Consts.N_OCTAVES * 2 + 1];
						ojumps[ojumpId]++;
						edge.addAttribute("ojumps", ojumps);
					} else {
						edge = graph.getEdge(Ek);
						ojumps = edge.getAttribute("ojumps");
						ojumps[ojumpId]++;
					}
				}
				beatCount++;
			}
		}
		
		ArrayList startingNodes = new ArrayList();
		float goalDuration, incDuration;
		int nStartNotes, prevOctave, ojumpId, ojump;
		int[] ojumps;
		Node snode, next;
		Edge edge;
		goalDuration = Consts.LICK_DURATION;
		nStartNotes = Consts.N_START_NODES;
		
		for (Node node : graph) {
			node.addAttribute("ui.label", node.getId());
			startingNodes.add(node);
		}
		Collections.sort(startingNodes, new DegreeComparator());
		
		if (startingNodes.size() < nStartNotes)
			nStartNotes = startingNodes.size();
		
		ArrayList licks = new ArrayList();
		
		for (Edge e : graph.getEachEdge()) {
			int[] ojumpsOld = e.getAttribute("ojumps");
			int[] ojumpsBak = new int[ojumpsOld.length];
			System.arraycopy(ojumpsOld, 0, ojumpsBak, 0, ojumpsOld.length);
					
			e.addAttribute("ojumpsBak", ojumpsBak);
		}
		
		/* TODO: Backup the edges and reset them for each startNode */
		
		/* For each node of the greatest degree set perform a visit 
		 * starting from that node */
		for (int i = 0; i < nStartNotes; i++) {
			
			if (i > 0)
				utils.resetEdgesWeights(graph);
			
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
				NodeNext nnext = utils.nextNode(snode);
				if (nnext == null) {
					snode = null;
				} else {
					ojumps = nnext.getEdgeThrough().getAttribute("ojumps");
					ojumpId = nnext.getOjumpId();
					ojumps[ojumpId]--;
					snode = nnext.getNextNode();
				}
			}
			lick.setDuration(incDuration);
			licks.add(lick);
		}
		
		Viewer viewer = graph.display();
		
		System.in.read();
		
		System.exit(0);
	}
	
}
