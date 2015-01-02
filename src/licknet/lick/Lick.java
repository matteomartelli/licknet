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

import java.io.IOException;
import licknet.graph.NoteNode;
import java.util.ArrayList;
import licknet.graph.NotesGraphSettings;
import licknet.io.SongFileLoader;
import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGTrack;

/**
 *
 * @author Matteo Martelli matteomartelli3@gmail.com
 */

public class Lick {
	private float duration;
	private int occurrences;
	private final ArrayList<NoteNode> notes;
	
	public Lick() {
		this.duration = 0;
		this.notes = new ArrayList<NoteNode>();
		this.occurrences = 0;
	}
	
	/* Copy constructor */
	public Lick(Lick lick) {
		this.duration = lick.getDuration();
		this.occurrences = lick.getOccurrences();
		this.notes = new ArrayList<NoteNode>();
		this.notes.addAll(lick.getNotes());
	}
	
	public void importFromFile(String filePath, NotesGraphSettings settings) 
			throws IOException, TGFileFormatException {
		SongFileLoader songFile = new SongFileLoader(filePath);
		
		/* Assuming we are interested in the 0th track TODO: what about this? */
		TGTrack track = songFile.getSong().getTrack(0);
		
		NoteNode nt, prevNt;
		nt = prevNt = null;
		
		for (int i = 0; i < track.countMeasures(); i++) {
			TGMeasure measure = track.getMeasure(i);
			
			for (int j = 0; j < measure.countBeats(); j++) {
				TGBeat beat = measure.getBeat(j);
				
				TGNote tgNote = beat.getVoice(0).getNote(0);
				
				/* If the node is tied, merge it to the previous one */
				if (tgNote != null && prevNt != null && tgNote.isTiedNote()) {
					/* Copy the previous tgNote and merge it with the new one */
					NoteNode newNote = new NoteNode(prevNt);
					newNote.mergeNote(tgNote);
					
					/* Remove the previous one from the notes sequence and add 
					 * the new one to it */
					notes.remove(notes.size() - 1);
					notes.add(newNote);
				} else {
					nt = new NoteNode(track, measure, beat, tgNote, 
							settings.isInfluenceBendings());
					
					/* Skip the repeated notes if specified in the settings */
					if (prevNt != null && settings.isInfluenceLoopNote() && 
							nt.getNodeKey().equals(prevNt.getNodeKey()))
						continue;

					notes.add(nt);
					duration += nt.getTime();
				}
			}
		}
	}
	
	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}
	
	public void addDuration(float duration) {
		this.duration += duration;
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