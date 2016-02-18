package snedeker.goetz.ir.assignmentOne.utils;

public class ResultData {
	String type;
	Long time;
	boolean foundHits;

	public boolean isFoundHits() {
		return foundHits;
	}

	public void setFoundHits(boolean foundHits) {
		this.foundHits = foundHits;
	}

	public ResultData(String type, Long time) {
		this.type = type;
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}
}