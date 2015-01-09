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
package licknet.lick;

import java.util.ArrayList;
import licknet.graph.NoteNode;
import licknet.graph.NotesGraph;
import licknet.graph.WeightEdge;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

/**
 *
 * @author Matteo Martelli matteomartelli3@gmail.com
 */

public class LickClassifier {

	private final ArrayList<LickGraphScore> graphs;
	private int bestGraphId;
	private final Lick lick;
	
	public LickClassifier(ArrayList<NotesGraph> graphs, Lick lick) {
		this.graphs = new ArrayList<LickGraphScore>();
		this.lick = lick;
		for (NotesGraph g : graphs) 
			this.graphs.add(new LickGraphScore(g, lick, 0.0f));
		
		this.bestGraphId = -1;
	}
	
	public void clear() {
		for (LickGraphScore sgraph : graphs) {
			sgraph.setScore(0.0f);
			bestGraphId = -1;
		}
	}
	
	public void classify() {
		clear();
		for (LickGraphScore graphscore : graphs) {
			NotesGraph graph = graphscore.getGraph();
			int oJump = 0, maxW = graph.getMaxWeight();
			float score = 0.0f;
			NoteNode lNote, prevLNote;
			Node gNode, prevGNode = null;
			
			for (int i = 0; i < lick.getNotes().size(); i++) {
				lNote = lick.getNotes().get(i);
				
				if (i > 0) {
					/* Calculate octave jump */
					prevLNote = lick.getNotes().get(i - 1);
					oJump = lNote.getOctave() - prevLNote.getOctave();
				}
				
				gNode = graph.getNode(lNote.getNodeKey());
				/* If there is a note in the graph matching the lick note */
				if (gNode != null) {
					/* Mark as the previous note the first matching note 
					 * of the matching lick segment in the graph is found */
					if (prevGNode == null) {
						prevGNode = gNode;
					} else {
						//FIXME: check if it matches all the notes
						Edge e = graph.getEdge(prevGNode, gNode);
						if (e != null) {
							WeightEdge wedge = new WeightEdge(e);
							/* Update the score calculated on the octave jump
							 * corresponding weight */
							int weight = wedge.getWeight(oJump);
							score += (float)weight ;
						}
						prevGNode = gNode;
					}
				} else {
					/* Restart the segment to be matched */
					prevGNode = null;
				}
			}
			/* Normalize with the graph insertions number */
			graphscore.setScore(score);
			graphscore.normalizeScore();
			int currentIdx = graphs.indexOf(graphscore);
			if (bestGraphId == -1)
				bestGraphId = currentIdx;
			else if (graphscore.getScore() > graphs.get(bestGraphId).getScore())
				bestGraphId = currentIdx;
		}
	}

	public ArrayList<LickGraphScore> getGraphs() {
		return graphs;
	}

	public int getBestGraphId() {
		return bestGraphId;
	}

	public Lick getLick() {
		return lick;
	}
	
}
