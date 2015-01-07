package licknet.graph;

public class NotesGraphSettings {
	/* TODO: remove lick related fields */
	public static final int DEFAULT_LICK_BRANCHING = 3;
	public static final int DEFAULT_N_START_NODES = 8;
	public static final float DEFAULT_LICK_DURATION = 1.0f;
	public static final int DEFAULT_LICK_MIN_NOTES = 3;
	public static final int DEFAULT_LICK_MAX_NOTES = 6;
	public static final float DEFAULT_MIN_MATCH_RATE = 0.5f;
	
	
	public static final boolean DEFAULT_INFLUENCE_BENDINGS = false;
	public static final boolean DEFAULT_INFLUENCE_LOOPNOTE = true;
	
	private int lickBranching = DEFAULT_LICK_BRANCHING;
	private int nStartNodes = DEFAULT_N_START_NODES;
	private float lickDuration = DEFAULT_LICK_DURATION;
	private int licksMinNotes = DEFAULT_LICK_MIN_NOTES;
	private int lickMaxNotes = DEFAULT_LICK_MAX_NOTES;
	private float minMatchRate = DEFAULT_MIN_MATCH_RATE;
	private boolean influenceBendings = DEFAULT_INFLUENCE_BENDINGS;
	private boolean influenceLoopNote = DEFAULT_INFLUENCE_LOOPNOTE;
	
	public NotesGraphSettings() {}


	public int getLickBranching() {
		return lickBranching;
	}


	public void setLickBranching(int lickBranching) {
		this.lickBranching = lickBranching;
	}


	public int getnStartNodes() {
		return nStartNodes;
	}


	public void setnStartNodes(int nStartNodes) {
		this.nStartNodes = nStartNodes;
	}


	public float getLickDuration() {
		return lickDuration;
	}


	public void setLickDuration(float lickDuration) {
		this.lickDuration = lickDuration;
	}


	public int getLicksMinNotes() {
		return licksMinNotes;
	}


	public void setLicksMinNotes(int licksMinNotes) {
		this.licksMinNotes = licksMinNotes;
	}


	public int getLickMaxNotes() {
		return lickMaxNotes;
	}


	public void setLickMaxNotes(int lickMaxNotes) {
		this.lickMaxNotes = lickMaxNotes;
	}


	public boolean isInfluenceBendings() {
		return influenceBendings;
	}


	public void setInfluenceBendings(boolean influenceBendings) {
		this.influenceBendings = influenceBendings;
	}


	public boolean isInfluenceLoopNote() {
		return influenceLoopNote;
	}


	public void setInfluenceLoopNote(boolean influenceLoopNote) {
		this.influenceLoopNote = influenceLoopNote;
	}


	public float getMinMatchRate() {
		return minMatchRate;
	}


	public void setMinMatchRate(float minMatchRate) {
		this.minMatchRate = minMatchRate;
	}
	
}
