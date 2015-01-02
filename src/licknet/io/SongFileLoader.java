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
package licknet.io;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.activation.UnsupportedDataTypeException;
import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.herac.tuxguitar.io.gpx.GPXInputStream;
import org.herac.tuxguitar.io.gtp.GP3InputStream;
import org.herac.tuxguitar.io.gtp.GP4InputStream;
import org.herac.tuxguitar.io.gtp.GP5InputStream;
import org.herac.tuxguitar.io.gtp.GTPSettings;
import org.herac.tuxguitar.io.tg.TGInputStream;
import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.TGSong;

/**
 *
 * @author Matteo Martelli matteomartelli3@gmail.com
 */
public class SongFileLoader {

	private TGSong song;
	
	public SongFileLoader(String filePath) 
			throws TGFileFormatException, IOException {
		
		TGFactory factory = new TGFactory();
			
		FileInputStream file = new FileInputStream(filePath);
		DataInputStream data = new DataInputStream(file);
		
		/* WARNING: Unsupported key signature with guitar pro files */
		if (filePath.endsWith(".gp3")) {
			GTPSettings gtpsettings = new GTPSettings();
			GP3InputStream gp3 = new GP3InputStream(gtpsettings);
			gp3.init(factory, data);
			song = gp3.readSong();
		} else if (filePath.endsWith(".gp4")) {
			GTPSettings gtpsettings = new GTPSettings();
			GP4InputStream gp4 = new GP4InputStream(gtpsettings);
			gp4.init(factory, data);
			song = gp4.readSong();
		} else if (filePath.endsWith(".gp5")) {
			GTPSettings gtpsettings = new GTPSettings();
			GP5InputStream gp5 = new GP5InputStream(gtpsettings);
			gp5.init(factory, data);
			song = gp5.readSong();
		} else if (filePath.endsWith(".gpx")) {
			GPXInputStream gpx = new GPXInputStream();
			gpx.init(factory, data);
			song = gpx.readSong();
		}else if (filePath.endsWith(".tg")) {
			TGInputStream tg = new TGInputStream();
			tg.init(factory, data);
			song = tg.readSong();
		} else {
			file.close();
			data.close();
			throw new TGFileFormatException("Unsupported file format");
		}
		
		file.close();
		data.close();
	}

	public TGSong getSong() {
		return song;
	}
	
}
