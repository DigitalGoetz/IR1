package snedeker.goetz.ir.assignmentOne;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import snedeker.goetz.ir.assignmentOne.models.QueryResults;
import snedeker.goetz.ir.assignmentOne.utils.BaselineComparison;
import snedeker.goetz.ir.assignmentOne.utils.BaselineQueries;
import snedeker.goetz.ir.assignmentOne.utils.SetType;

public class IndexEvaluator {

	static Logger log = Logger.getLogger(IndexEvaluator.class);

	public static void main(String[] args) throws IOException {
		SearchAppliance standardAnalyzerSearch = new SearchAppliance(AssignmentApplication.STD_ANALYZER);
		AssignmentApplication.createIndex(standardAnalyzerSearch);
		AssignmentApplication.indexMetrics(standardAnalyzerSearch);

		BaselineComparison comparison = new BaselineComparison(SetType.CRAN);
		BaselineQueries standardQueries = new BaselineQueries(SetType.CRAN);

		List<Integer> queryIds = standardQueries.getAvailableQueryIds();

		log.debug(listToString(queryIds));

		int count = 0;
		
		for (Integer queryId : queryIds) {
			String queryString = standardQueries.getQueryString(queryId);

			QueryResults actualResults = standardAnalyzerSearch.performQuery(queryString);

			List<Integer> expectedDocuments = comparison.getExpectedDocumentIds(queryId);
			List<Integer> actualDocuments = new ArrayList<>(actualResults.getResultsMap().keySet());
			Collections.sort(actualDocuments);

			log.debug("QUERY: " + queryString);
			log.debug("Excpected:\t" + listToString(expectedDocuments));
			log.debug("Actual:\t" + listToString(actualDocuments));
		}

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
