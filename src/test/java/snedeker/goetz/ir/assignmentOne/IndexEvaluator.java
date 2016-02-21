package snedeker.goetz.ir.assignmentOne;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import snedeker.goetz.ir.assignmentOne.models.QueryResults;
import snedeker.goetz.ir.assignmentOne.utils.BaselineComparison;
import snedeker.goetz.ir.assignmentOne.utils.BaselineQueries;
import snedeker.goetz.ir.assignmentOne.utils.DataSource;

public class IndexEvaluator {

	static Logger log = Logger.getLogger(IndexEvaluator.class);

	public static Double evaluate(DataSource source, String analyzer) throws IOException {

		List<Double> f1Scores = new ArrayList<>();

		SearchAppliance searchApp = new SearchAppliance(analyzer, source);
		AssignmentApplication.createIndex(searchApp);
		AssignmentApplication.indexMetrics(searchApp);

		BaselineComparison comparison = new BaselineComparison(source);
		BaselineQueries standardQueries = new BaselineQueries(source);

		List<Integer> queryIds = standardQueries.getAvailableQueryIds();

		log.debug(listToString(queryIds));

		for (Integer queryId : queryIds) {
			String queryString = standardQueries.getQueryString(queryId);

			QueryResults obtainedResults = searchApp.performQuery(queryString);

			List<Integer> expectedDocuments = comparison.getExpectedDocumentIds(queryId);
			List<Integer> obtainedDocuments = new ArrayList<>(obtainedResults.getResultsMap().keySet());
			Collections.sort(obtainedDocuments);

			log.debug("QUERY: " + queryString);
			log.debug("Excpected:\t" + listToString(expectedDocuments));
			log.debug("Actual:\t" + listToString(obtainedDocuments));

			Double score = getF1(obtainedDocuments, expectedDocuments);
			log.debug("F1: " + score);
			f1Scores.add(score);

			log.debug("\n");
		}

		Double scoreSum = 0.0;
		for (Double score : f1Scores) {
			scoreSum += score;
		}

		Double avg = scoreSum / new Integer(f1Scores.size()).doubleValue();
		log.debug("Average F1 score against baseline set: " + avg);

		AssignmentApplication.releaseIndex(searchApp);
		
		return 0.0;
	}

	public static void main(String[] args) throws IOException {
		Double stdCran = evaluate(DataSource.CRAN, AssignmentApplication.STD_ANALYZER);
		Double engCran = evaluate(DataSource.CRAN, AssignmentApplication.ENG_ANALYZER);
		Double stdMed = evaluate(DataSource.MED, AssignmentApplication.STD_ANALYZER);
		Double engMed = evaluate(DataSource.MED, AssignmentApplication.ENG_ANALYZER);
		
		
		log.debug("Standard Analyzer against Cranfield Dataset: F1 = " + stdCran );
		log.debug("English Analyzer against Cranfield Dataset: F1 = " + engCran );
		log.debug("Standard Analyzer against Medline Dataset: F1 = " + stdMed );
		log.debug("English Analyzer against Medline Dataset: F1 = " + engMed );
	}

	private static Double getF1(List<Integer> obtained, List<Integer> expected) {

		if (obtained.isEmpty()) {
			return 0.0;
		}

		Integer relevantElements = expected.size();

		Integer truePositives = 0;
		for (Integer integer : obtained) {
			if (expected.contains(integer)) {
				truePositives++;
			}
		}

		double precision = truePositives.doubleValue() / new Integer(obtained.size()).doubleValue();
		double recall = truePositives.doubleValue() / relevantElements.doubleValue();

		if ((precision + recall) == 0.0) {
			return 0.0;
		}

		return 2.0 * ((precision * recall) / (precision + recall));
	}

	private static String listToString(List<Integer> ids) {
		String string = "";
		for (Integer integer : ids) {
			if (string.length() != 0) {
				string += ", ";
			}
			string += integer.toString();
		}
		return string;
	}

}
