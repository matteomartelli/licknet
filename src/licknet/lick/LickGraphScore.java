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

import licknet.graph.NotesGraph;

/**
 *
 * @author Matteo Martelli matteomartelli3@gmail.com
 */
public class LickGraphScore {
	private final NotesGraph graph;
	private final Lick lick;
	private float score;
	private boolean normalized;
	
	public LickGraphScore(NotesGraph graph, Lick lick, float score){
		this.normalized = false;
		this.lick = lick;
		this.graph = graph;
		this.score = score;
	}
		
	public void normalizeScore() {
		if (!normalized) {
			score /= (float)graph.getNotesSequence().size();
			normalized = true;
		}
	}
	
	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
		normalized = false;
	}

	public NotesGraph getGraph() {
		return graph;
	}

	public boolean isNormalized() {
		return normalized;
	}

	public Lick getLick() {
		return lick;
	}
	
}
