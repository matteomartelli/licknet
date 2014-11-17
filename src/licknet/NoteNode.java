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

import java.util.Iterator;

import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGString;
import org.herac.tuxguitar.song.models.TGTrack;
import org.herac.tuxguitar.song.models.effects.TGEffectBend;
import org.herac.tuxguitar.song.models.effects.TGEffectBend.BendPoint;

/**
 *
 * @author Matteo Martelli matteomartelli3@gmail.com
 */
public class NoteNode {
	
	private int baseNote;
	private int octave;
	private float time;
	boolean isBend, isRestNote;
	private String nodeKey;

	public int tgNoteToMidi(TGTrack track, TGNote note) {
		return track.getOffset() + (note.getValue() + 
			   ((TGString)track.getStrings().get(note.getString() - 1)).getValue());
	}
	
	NoteNode (TGTrack track, TGMeasure measure, TGBeat beat, TGNote note) {
		int midi, transposed;
		TGDuration tgDuration;
		isBend = isRestNote = false;
		
		if (note == null) {
			if (beat.isRestBeat()) {
				this.baseNote = Consts.REST_NOTE;
				this.octave = 0;	
				this.isRestNote = true;
			} else {
				throw new NullPointerException("Found a null note that is not a rest");
			}
		} else {		
			if (note.getEffect().isBend()) {
				this.isBend = true;
				int bendAvgNote;
				/* Get the most played note in a bending.
				 * TODO: Would it be better if I split the bended note in 
				 * more notes? Or better if I consider the baseNote + the bend
				 * in the graph?
				*/
				bendAvgNote = getBendAvg(note.getEffect().getBend(), 
									     note.getValue());
				note.setValue(bendAvgNote);
			}
			
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
	
	/* Calculate the note fret that is most played in a bended note */
	public int getBendAvg(TGEffectBend bend, int baseNote) {
		int max_idx, bendNote;
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

	public boolean isRestNote() {
		return this.isRestNote;
	}
	
	public boolean isBend() {
		return this.isBend;
	}
}

