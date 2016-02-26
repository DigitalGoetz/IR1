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

	Logger log = Logger.getLogger(IndexEvaluator.class);

	public Double evaluate(SearchAppliance searchApp) throws IOException {

		List<Double> f1Scores = new ArrayList<>();

		AssignmentApplication.createIndex(searchApp);
		AssignmentApplication.indexMetrics(searchApp);

		BaselineComparison comparison = new BaselineComparison(searchApp.getSource());
		BaselineQueries standardQueries = new BaselineQueries(searchApp.getSource());

		List<Integer> queryIds = standardQueries.getAvailableQueryIds();

		log.debug(listToString(queryIds));

		for (Integer queryId : queryIds) {
			String queryString = standardQueries.getQueryString(queryId);

			QueryResults obtainedResults = searchApp.performQuery(queryString);

			List<Integer> expectedDocuments = comparison.getExpectedDocumentIds(queryId);
			List<Integer> obtainedDocuments = new ArrayList<>(obtainedResults.getResultsMap().keySet());
			Collections.sort(obtainedDocuments);

			log.debug("QUERY: " + queryString);
			log.debug("Baseline:\t" + listToString(expectedDocuments));
			log.debug("Engine Found:\t" + listToString(obtainedDocuments));

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

		return avg;
	}

	public void runEvals() throws IOException {
		SearchAppliance stdCran = new SearchAppliance(AssignmentApplication.STD_ANALYZER, DataSource.CRAN, null);
		SearchAppliance engCran = new SearchAppliance(AssignmentApplication.ENG_ANALYZER, DataSource.CRAN, null);
		SearchAppliance stdApp = new SearchAppliance(AssignmentApplication.STD_ANALYZER, DataSource.MED, null);
		SearchAppliance engApp = new SearchAppliance(AssignmentApplication.ENG_ANALYZER, DataSource.MED, null);

		List<String> stopwords = new ArrayList<>();
		stopwords.add("after");
		stopwords.add("has");
		stopwords.add("been");
		stopwords.add("may");
		stopwords.add("were");
		stopwords.add("than");
		stopwords.add("from");
		stopwords.add("cells");
		stopwords.add("patients");
		stopwords.add("blood");
		stopwords.add("have");
		stopwords.add("which");
		stopwords.add("growth");
		stopwords.add("cases");
		stopwords.add("during");
		stopwords.add("normal");
		stopwords.add("1");
		stopwords.add("10");
		stopwords.add("2");
		stopwords.add("3");
		stopwords.add("4");
		stopwords.add("5");
		stopwords.add("6");
		stopwords.add("b");
		stopwords.add("c");
		stopwords.add("its");
		stopwords.add("m");
		stopwords.add("other");
		stopwords.add("some");
		stopwords.add("had");
		stopwords.add("also");
		stopwords.add("when");
		stopwords.add("one");
		stopwords.add("only");
		stopwords.add("found");
		stopwords.add("between");
		stopwords.add("cell");
		stopwords.add("children");
		stopwords.add("treatment");
		stopwords.add("all");
		stopwords.add("changes");
		stopwords.add("more");
		stopwords.add("disease");
		stopwords.add("dna");
		stopwords.add("effect");
		stopwords.add("hormone");
		stopwords.add("human");
		stopwords.add("increased");
		stopwords.add("rats");
		stopwords.add("results");
		stopwords.add("two");
		stopwords.add("can");
		stopwords.add("most");
		stopwords.add("per");
		stopwords.add("both");
		stopwords.add("increase");

		stopwords.add("associated");
		stopwords.add("body");
		stopwords.add("case");
		stopwords.add("day");
		stopwords.add("days");
		stopwords.add("who");
		stopwords.add("type");
		stopwords.add("three");

		stopwords.add("differ");
		stopwords.add("different");
		stopwords.add("difference");
		stopwords.add("differences");

		SearchAppliance customApp = new SearchAppliance(AssignmentApplication.CUSTOM_STD_ANALYZER, DataSource.MED,
				stopwords);

		Double stdCranf1 = evaluate(stdCran);
		Double engCranf1 = evaluate(engCran);
		Double stdMedf1 = evaluate(stdApp);
		Double engMedf1 = evaluate(engApp);
		Double customMedf1 = evaluate(customApp);

		log.debug("Standard Analyzer against Cranfield Dataset: F1 = " + stdCranf1);
		log.debug("English Analyzer against Cranfield Dataset: F1 = " + engCranf1);
		log.debug("Standard Analyzer against Medline Dataset: F1 = " + stdMedf1);
		log.debug("English Analyzer against Medline Dataset: F1 = " + engMedf1);
		log.debug("Custom Analyzer against Medline Dataset: F1 = " + customMedf1);

		// Metrics.printIndexTerms(customApp);
	}

	private Double getF1(List<Integer> obtained, List<Integer> expected) {

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

	private String listToString(List<Integer> ids) {
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