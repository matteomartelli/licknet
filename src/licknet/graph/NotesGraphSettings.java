package licknet.graph;

public class NotesGraphSettings {
	
	public static final boolean DEFAULT_INFLUENCE_BENDINGS = false;
	public static final boolean DEFAULT_INFLUENCE_LOOPNOTE = true;
	
	private boolean influenceBendings = DEFAULT_INFLUENCE_BENDINGS;
	private boolean influenceLoopNote = DEFAULT_INFLUENCE_LOOPNOTE;
	
	public NotesGraphSettings() {}

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
}
