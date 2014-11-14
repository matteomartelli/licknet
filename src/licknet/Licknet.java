/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package licknet;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.herac.tuxguitar.io.gtp.GP5InputStream;
import org.herac.tuxguitar.io.gtp.GTPSettings;
import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.song.models.TGString;
import org.herac.tuxguitar.song.models.TGTrack;
import org.herac.tuxguitar.song.models.effects.TGEffectBend;
import org.herac.tuxguitar.song.models.effects.TGEffectBend.BendPoint;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

/**
 *
 * @author Matteo Martelli matteomartelli3@gmail.com
 */

class Note {
	
}

class Utils {
	Utils () { }
	
	public int tgNote2Midi(TGTrack track, TGNote note)
	{
		return track.getOffset() + (note.getValue() + 
			   ((TGString)track.getStrings().get(note.getString() - 1)).getValue());
	}
	
	public int bendAvg(TGEffectBend bend, int baseNote) {
		int npoints, max_idx, bendNote;
		int[] notes = new int[24]; /* Java says they're all initialized to 0 */	

		Iterator it = bend.getPoints().iterator();
		while(it.hasNext()){
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
}

public class Licknet {
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws FileNotFoundException, TGFileFormatException {
		// TODO code application logic here
		
		Utils utils = new Utils();
		TGFactory factory = new TGFactory();
		
		FileInputStream file = new FileInputStream("data/road.gp5");
		DataInputStream data = new DataInputStream(file);
		
		GTPSettings gtpsettings = new GTPSettings();
		GP5InputStream gp5 = new GP5InputStream(gtpsettings);
		gp5.init(factory, data);
		
		TGSong song = gp5.readSong();
		
		/* XXX: Trying the graph 
		Graph graph = new SingleGraph("licknet");
		graph.addNode("A");
		graph.addNode("B");
		graph.addNode("C");
		graph.addEdge("AB", "A", "B");
		graph.addEdge("BC", "B", "C");
		graph.addEdge("CA", "C", "A");
		graph.display(); */
		
		/* Assuming we are interested in the 0th track */
		TGTrack track = song.getTrack(0);
		System.out.println("string\tnote\tmidi\tbend");
		for (int i = 0; i < track.countMeasures(); i++) {
			TGMeasure measure = track.getMeasure(i);
			for (int j = 0; j < measure.countBeats(); j++) {
				TGBeat beat = measure.getBeat(j);
				TGNote note = beat.getVoice(0).getNote(0);
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
				
				int midi = utils.tgNote2Midi(track, note);
				System.out.println(note.getString() + "\t" + note.getValue()
							       + "\t" + midi +  "\t" + note.getEffect().isBend());
				
				/* TODO Store the notes in a arraylist and in the graph at the same time */
				
			}
		}
		
		System.exit(0);
	}
	
}
