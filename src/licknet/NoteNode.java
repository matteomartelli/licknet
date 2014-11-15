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

import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGString;
import org.herac.tuxguitar.song.models.TGTrack;

/**
 *
 * @author Matteo Martelli matteomartelli3@gmail.com
 */
public class NoteNode {
	
	private int baseNote;
	private int octave;
	private float time;
	private String nodeKey;

	public int tgNoteToMidi(TGTrack track, TGNote note) {
		return track.getOffset() + (note.getValue() + 
			   ((TGString)track.getStrings().get(note.getString() - 1)).getValue());
	}
	
	NoteNode (TGTrack track, TGMeasure measure, TGBeat beat, TGNote note) {
		int keyOffset, midi, transposed;
		TGDuration tgDuration;
				
		if (note == null) {
			if (beat.isRestBeat()) {
				this.baseNote = Consts.REST_NOTE;
				this.octave = 0;
				
			} else {
				throw new NullPointerException("Found a null note that is not a rest");
			}
		} else {		
			 
			midi = tgNoteToMidi(track, note);
			transposed = midi - measure.getKeySignature();
			
			this.octave = (transposed / Consts.N_SEMITONES) - 1;

			this.baseNote = transposed - (Consts.N_SEMITONES * (octave + 1));	
		}
		
		tgDuration = beat.getVoice(0).getDuration();
		this.time = 1.0f / (float)tgDuration.getValue();
		
		if (tgDuration.isDotted())
			this.time += this.time / 2.0f;
		else if (tgDuration.isDoubleDotted())
			this.time += this.time / 4.0f;
		
		if (tgDuration.getDivision().getTimes() > 0)
			this.time *= (float)tgDuration.getDivision().getTimes()
						 / (float)tgDuration.getDivision().getEnters();
		
		nodeKey = String.format("%02d:%.3f", this.baseNote, this.time);
	}
	
	public int getOctave() {
		return octave;
	}

	public void setOctave(int octave) {
		this.octave = octave;
	}

	public int getBaseNote() {
		return baseNote;
	}

	public void setBaseNote(int baseNote) {
		this.baseNote = baseNote;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}
	
	public String getNodeKey() {
		return nodeKey;
	}

	public void setNodeKey(String nodeKey) {
		this.nodeKey = nodeKey;
	}
}

