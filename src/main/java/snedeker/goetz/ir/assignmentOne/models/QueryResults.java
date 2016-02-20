package snedeker.goetz.ir.assignmentOne.models;

import java.util.HashMap;
import java.util.Map;

public class QueryResults {

	Integer hits = null;
	Long queryTime = null;
	Map<Integer, String> resultsMap = null;

	public Integer getHits() {
		if (hits == null) {
			hits = 0;
		}
		return hits;
	}

	public void add(EntryDocument document) {
		setHits(getHits() + 1);
		setQueryTime(-1L);
		getResultsMap().put(document.getDocId(), document.getTitle());
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}

	public Long getQueryTime() {
		if (queryTime == null) {
			queryTime = 0L;
		}
		return queryTime;
	}

	public void setQueryTime(Long queryTime) {
		this.queryTime = queryTime;
	}

	public Map<Integer, String> getResultsMap() {
		if (resultsMap == null) {
			resultsMap = new HashMap<>();
		}
		return resultsMap;
	}

	public void setResultsMap(Map<Integer, String> resultsMap) {
		this.resultsMap = resultsMap;
	}

}
