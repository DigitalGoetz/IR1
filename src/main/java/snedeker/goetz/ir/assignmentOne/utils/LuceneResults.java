package snedeker.goetz.ir.assignmentOne.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class LuceneResults {
	Logger log = Logger.getLogger(getClass());

	private Map<String, List<ResultData>> evaluations;

	public Map<String, List<ResultData>> getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(Map<String, List<ResultData>> evaluations) {
		this.evaluations = evaluations;
	}

	public LuceneResults() {
		evaluations = new HashMap<>();
	}

	public void insertData(String type, String query, Long duration) {
		List<ResultData> list = evaluations.get(query);

		if (list == null || list.isEmpty()) {
			list = new ArrayList<>();
			list.add(new ResultData(type, duration));
			evaluations.put(query, list);
		} else {
			list.add(new ResultData(type, duration));
		}

	}

	public void printEvaluation() {
		log.debug("Evaluations...");
		for (String query : evaluations.keySet()) {
			List<ResultData> data = evaluations.get(query);
			String fastestType = "N/A";
			double sumDuration = 0L;
			long fastestTime = Long.MAX_VALUE;
			for (ResultData resultData : data) {
				if (resultData.isFoundHits()) {
					if (resultData.getTime() < fastestTime) {
						fastestTime = resultData.getTime();
						fastestType = resultData.getType();
					}
					sumDuration += resultData.getTime().doubleValue();
				}

			}

			Double timeAboveAverage = sumDuration / new Integer(data.size()).doubleValue();

			log.debug("Query for: " + query + " had " + fastestType + "leading by " + fastestTime + " which is "
					+ timeAboveAverage.intValue() + "ms above average  ");

		}
	}

}
