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
package licknet.graph;

import java.util.Comparator;

import org.graphstream.graph.Edge;

/**
*
* @author Matteo Martelli matteomartelli3@gmail.com
*/
/* Compare the octave jump weight of the nodes in an ArrayList 
 * for descending order sorting */
public class WeightComparator  implements Comparator<Edge> {
    @Override
    public int compare(Edge e1, Edge e2) {
		int[] ojumps1 = e1.getAttribute("ojumps");
		int[] ojumps2 = e2.getAttribute("ojumps");
		
		int weight1 = maxOctaveJump(ojumps1);
		int weight2 = maxOctaveJump(ojumps2);
		
    	return ojumps2[weight2] - ojumps1[weight1];
    }
    
    /* Find the max octave jump for an edge. 
	 * Return the id of the maximum 
	 */
	public static int maxOctaveJump(int[] ojumps) {
		int max, maxIdx;
		max = maxIdx = 0;
		for (int i = 0; i < ojumps.length; i++) {
				if (ojumps[i] > max) {
					max = ojumps[i];
					maxIdx = i;
				}
		}
		
		return maxIdx;
	}
	
}