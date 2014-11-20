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

import java.util.ArrayList;

/**
 *
 * @author Matteo Martelli matteomartelli3@gmail.com
 */

public class Lick {
	private float duration;
	private int occurrences;
	private final ArrayList<NoteNode> notes;
	
	Lick() {
		this.duration = 0;
		this.notes = new ArrayList();
		this.occurrences = 0;
	}
	
	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public void addNote(NoteNode note) {
		this.notes.add(note);
	}
	
	public ArrayList<NoteNode> getNotes() {
		return notes;
	}

	public int getOccurrences() {
		return occurrences;
	}

	public void setOccurrences(int occurrences) {
		this.occurrences = occurrences;
	}
	
	public void incrementOccurrences() {
		this.occurrences++;
	}
}