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

import licknet.graph.NotesGraph;
import licknet.view.Frame;
import javax.swing.UIManager;
import licknet.utils.Log;

import org.graphstream.graph.Node;

/**
 *
 * @author Matteo Martelli matteomartelli3@gmail.com
 */

public class Licknet {
	
	/**
	 * @param args the command line arguments
	 * @throws Exception 
	 */
	public static void main(String[] args) 
			throws Exception {
		
		NotesGraph graph = new NotesGraph("licknet");

		/*String filePath = "data/hendrix/Jimi Hendrix - Foxy Lady.tg";
		
		graph.initFromFile(filePath);*/
		
		graph.initFromFolder("data/hendrix");
		
		for (Node node : graph.getEachNode()) {
			node.addAttribute("ui.label", node.getId());
		}
		
		
		Log.printNotesSequence(graph.getNotesSequence());
		
		graph.findLicks();
		
		Log.printLicks(graph.getLicks());

		graph.display();
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		

		new Frame();
	}
	
}
