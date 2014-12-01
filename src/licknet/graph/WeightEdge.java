package licknet.graph;

import java.util.Arrays;

import org.graphstream.graph.Edge;
public class WeightEdge {

	private final Edge edge;
	
	public WeightEdge(Edge edge) {
		this.edge = edge;
	}
	
    /* Find the max octave jump for an edge. 
	 * Return the id of the maximum 
	 */
	private int getMaxOctaveJumpId() {
		int max, maxIdx;
		max = maxIdx = 0;
		int[] ojumps = edge.getAttribute("ojumps");
		for (int i = 0; i < ojumps.length; i++) {
				if (ojumps[i] > max) {
					max = ojumps[i];
					maxIdx = i;
				}
		}
		
		return maxIdx;
	}
	
	public int getMaxOctaveJump() {
		int[] ojumps = edge.getAttribute("ojumps");
		return ojumps[getMaxOctaveJumpId()];
	}
	
	public void decreaseMaxOctaveJump() {
		int[] ojumps = edge.getAttribute("ojumps");
		int maxId = getMaxOctaveJumpId();
		ojumps[maxId]--;
	}
	
	public void resetWeights() {
		int[] ojumpsBak = edge.getAttribute("ojumpsBak");
		int[] ojumps = edge.getAttribute("ojumps");
		
		System.arraycopy(ojumpsBak, 0, ojumps, 0, ojumpsBak.length);
	}
	

	public Edge getEdge() {
		return edge;
	}

}
