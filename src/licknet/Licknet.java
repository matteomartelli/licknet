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

import org.graphstream.graph.Node;

/**
 *
 * @author Matteo Martelli matteomartelli3@gmail.com
 */

class Utils {
		
	static public void printNotesSequence(ArrayList<NoteNode> notesSequence) {
		System.out.println("bend\tbnote\tdura\tocta\tnkey");
		
		for (NoteNode nt : notesSequence) {
			if (nt.isRestNote())
				System.out.print("REST");
			else
				System.out.print(nt.isBend());
			
			System.out.printf("\t%d\t%.3f\t%d\t%s\n", 
							  nt.getBaseNote(), 
							  nt.getTime(), 
							  nt.getOctave(),
							  nt.getNodeKey());
		}
		
	}	

	static public void printLicks(ArrayList<Lick> licks) {
		for (Lick lick : licks) {
			System.out.print("[");
			for (int i = 0; i < lick.getNotes().size(); i++) {
				NoteNode note = lick.getNotes().get(i);
				System.out.print(note.getNodeKey() + note.getOctave());
				if (i < lick.getNotes().size() - 1) {
					System.out.println(", ");
				}
			}
			System.out.println("], occures " + lick.getOccurrences() + "times");
		}
	}
}

public class Licknet {
	
	/**
	 * @param args the command line arguments
	 * @throws Exception 
	 */
	public static void main(String[] args) 
			throws Exception {
		
		String filePath = "data/tests/test_tied.gp3";
		
		NotesGraph graph = new NotesGraph("licknet");
		
		graph.initFromFile(filePath);
		
		for (Node node : graph.getEachNode()) {
			node.addAttribute("ui.label", node.getId());
		}
		
		graph.display();
		
		//Utils.printNotesSequence(graph.getNotesSequence());
		Utils.printLicks(graph.getLicks());
		graph.findLicks();
		
		System.in.read();
		
		System.exit(0);
	}
	
}
