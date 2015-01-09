package licknet.graph;

import java.util.Random;
import licknet.utils.Consts;

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
			
	/* Return the weight corresponding to the given octave jump */
	public int getWeight(int octaveJump){
		int[] ojumps = edge.getAttribute("ojumps");
		return ojumps[octaveJump + Consts.N_OCTAVES];
	}

	/* Return a non-zero octave jump offset randomly choosed */
	public int getRandomOctaveJump(Random r) throws NoWeightEdgeException{
		int[] ojumps = edge.getAttribute("ojumps");
		int[] reducedOjumps = new int[ojumps.length];
		int rsize = 0;
		for (int i = 0; i < ojumps.length; i++) {
			if (ojumps[i] > 0)
				reducedOjumps[rsize++] = i;
		}
		
		/* reducedOjumps contains only the indexes of the non-zero oJumps */
		if (rsize > 0)
			return reducedOjumps[r.nextInt(rsize)] - Consts.N_OCTAVES;
		else 
			throw new NoWeightEdgeException("All zero ojumps in edge: " + edge.getId());
	}
	
	public Edge getEdge() {
		return edge;
	}

}
