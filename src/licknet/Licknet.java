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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
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

/**
 *
 * @author Matteo Martelli matteomartelli3@gmail.com
 */

class Utils {
	Utils () { }
	
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
	
	public Edge checkBestEdge(Node node) {
		/*TODo */
		return null;
		
	}
}

class DegreeComparator implements Comparator<Node> {
    @Override
    public int compare(Node o1, Node o2) {
		return o2.getDegree() - o1.getDegree();
    }
}

public class Licknet {
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) 
			throws FileNotFoundException, TGFileFormatException, IOException {
		
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
						 * TODO: It would be better if I split the bended note in 
						 * more notes.
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
						edge.changeAttribute(Ek, ojumps);
					}
				}
				beatCount++;
			}
		}
		
		/* Label edges */
		/*for (Edge edge : graph.getEachEdge()){
			
			int[] ojumps = edge.getAttribute("ojumps");
			String ojumpsLabel = "[";
			for (int i = 0; i < ojumps.length; i++){
				ojumpsLabel += ojumps[i];
				if (i != ojumps.length - 1)
					ojumpsLabel += ",";
			}
			ojumpsLabel += "]";
			edge.addAttribute("ui.label", ojumpsLabel);
			
		}*/
		
		ArrayList nodes = new ArrayList();
		float goalDuration, incDuration;
		int nStartNotes;
		Node snode, next;
		Edge edge;
		goalDuration = Consts.LICK_DURATION;
		nStartNotes = Consts.N_START_NODES;
		
		for (Node node : graph) {
			node.addAttribute("ui.label", node.getId());
			nodes.add(node);
		}
		Collections.sort(nodes, new DegreeComparator());
		
		if (nodes.size() < nStartNotes)
			nStartNotes = nodes.size();
		
		/* For each node of the greatest degree set perform a visit 
		 * starting from that node */
		for (int i = 0; i < nStartNotes; i++) {
			snode = (Node)nodes.get(i);
			incDuration = 0;
			
			next = snode;
			while (incDuration < goalDuration) {
				incDuration += ((NoteNode)next.getAttribute("note")).getTime();
				edge = utils.checkBestEdge(next);
				
				next = edge.getTargetNode();
			}
		}
		
		Viewer viewer = graph.display();
		
		
		System.in.read();
		
		System.exit(0);
	}
	
}
