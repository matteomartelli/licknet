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

/**
 *
 * @author Matteo Martelli matteomartelli3@gmail.com
 */
public class LickGeneratorSettings {
	public static final int DEFAULT_N_RANDOM_LICKS = 500;
	public static final int DEFAULT_N_BEST_LICKS = 5;
	public static final int DEFAULT_LICK_MIN_NOTES = 4;
	public static final int DEFAULT_LICK_MAX_NOTES = 10;
	public static final float DEFAULT_LICK_DURATION = 2.0f;
	
	private int nRandomLicks = DEFAULT_N_RANDOM_LICKS;
	private int nBestLicks = DEFAULT_N_BEST_LICKS;
	private int lickMinNotes = DEFAULT_LICK_MIN_NOTES;
	private int lickMaxNotes = DEFAULT_LICK_MAX_NOTES;
	private float lickDuration = DEFAULT_LICK_DURATION;
	
	
	public LickGeneratorSettings() {}

	public int getnRandomLicks() {
		return nRandomLicks;
	}

	public void setnRandomLicks(int nRandomLicks) {
		this.nRandomLicks = nRandomLicks;
	}

	public int getnBestLicks() {
		return nBestLicks;
	}

	public void setnBestLicks(int nBestLicks) {
		this.nBestLicks = nBestLicks;
	}

	public int getLickMinNotes() {
		return lickMinNotes;
	}

	public void setLickMinNotes(int lickMinNotes) {
		this.lickMinNotes = lickMinNotes;
	}

	public int getLickMaxNotes() {
		return lickMaxNotes;
	}

	public void setLickMaxNotes(int lickMaxNotes) {
		this.lickMaxNotes = lickMaxNotes;
	}

	public float getLickDuration() {
		return lickDuration;
	}

	public void setLickDuration(float lickDuration) {
		this.lickDuration = lickDuration;
	}
	
}
