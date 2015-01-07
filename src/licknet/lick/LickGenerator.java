/*
 * Copyright (C) 2015 Matteo Martelli matteomartelli3@gmail.com
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
import java.util.Collections;
import java.util.Random;
import licknet.graph.NoWeightEdgeException;
import licknet.graph.NoteNode;
import licknet.graph.NotesGraph;
import licknet.graph.WeightEdge;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

/**
 *
 * @author Matteo Martelli matteomartelli3@gmail.com
 */
public class LickGenerator {
	
	private LickGeneratorSettings settings;
	
	private final NotesGraph graph;
	private ArrayList<LickGraphScore> randomLicks;
	private ArrayList<LickGraphScore> bestLicks;
	
	
	public LickGenerator(NotesGraph graph, LickGeneratorSettings settings) {
		this.graph = graph;
		this.settings = settings;
		this.randomLicks = new ArrayList<LickGraphScore>();
		this.bestLicks = new ArrayList<LickGraphScore>();
	}
	
	public void clear() {
		randomLicks.clear();
		bestLicks.clear();
	}
	
	public void generate() throws NoWeightEdgeException {
		clear();
		Random randomGenerator = new Random();
		while(randomLicks.size() < settings.getnRandomLicks()) {
			int idx = randomGenerator.nextInt(graph.getNodeCount());
			Node startNode = graph.getNode(idx);
			randomLicks.add(randomWalk(startNode, randomGenerator));
		}
		
		Collections.sort(randomLicks, new LickGraphScoreComparator());
		
		int n = Math.min(settings.getnBestLicks(), settings.getnRandomLicks());

		for (int i = 0; i < n; i++)
			bestLicks.add(randomLicks.get(i));
	}
	
	private LickGraphScore randomWalk(Node startNode, Random randomGenerator) 
			throws NoWeightEdgeException {		
		
		Lick lick = new Lick();
		float score = 0.0f;
		Node node = startNode;
		int i = 0, oJump = 0;
		int prevOctave = ((NoteNode)node.getAttribute("note")).getOctave();
	
		while (i < settings.getLickMaxNotes() && 
				lick.getDuration() < settings.getLickDuration()) {
			
			if (node.getLeavingEdgeSet().size() > 0) {
				
				NoteNode note = new NoteNode((NoteNode)(node.getAttribute("note")));
				note.setOctave(prevOctave + oJump);
				lick.addNote(note);
				lick.addDuration(note.getTime());

				prevOctave = note.getOctave();
				
				/* Increase the score and decide the next node */
				int idx = randomGenerator.nextInt(node.getLeavingEdgeSet().size());
				Edge e = node.getLeavingEdge(idx);

				WeightEdge we = new WeightEdge(e);
				oJump = we.getRandomOctaveJump(randomGenerator);
				score += (float)(we.getWeight(oJump));
				
				node = e.getTargetNode();
				
				i++;
			} else {
				int idx = randomGenerator.nextInt(graph.getNodeCount());
				node = graph.getNode(idx);
				prevOctave = ((NoteNode)node.getAttribute("note")).getOctave();
				oJump = 0;
			}
		}
		
		LickGraphScore lgs = new LickGraphScore(graph, lick, score);
		lgs.normalizeScore();
		return lgs;
	}

	public LickGeneratorSettings getSettings() {
		return settings;
	}

	public void setSettings(LickGeneratorSettings settings) {
		this.settings = settings;
	}

	public NotesGraph getGraph() {
		return graph;
	}

	public ArrayList<LickGraphScore> getRandomLicks() {
		return randomLicks;
	}

	public ArrayList<LickGraphScore> getBestLicks() {
		return bestLicks;
	}
	
}
