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

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

/**
 *
 * @author Matteo Martelli matteomartelli3@gmail.com
 */
public class NodeNext {
	private final int ojumpId;
	private final Node nodeNext;
	private final Edge edgeThrough;
	
	NodeNext(int ojumpId, Node nextNode, Edge edgeThrough) {
		this.ojumpId = ojumpId;
		this.nodeNext = nextNode;
		this.edgeThrough = edgeThrough;
	}

	public int getOjumpId() {
		return ojumpId;
	}

	public Node getNextNode() {
		return nodeNext;
	}

	public Edge getEdgeThrough() {
		return edgeThrough;
	}
	
}
