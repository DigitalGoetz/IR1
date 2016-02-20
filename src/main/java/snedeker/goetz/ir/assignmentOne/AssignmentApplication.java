package snedeker.goetz.ir.assignmentOne;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.en.EnglishAnalyzer;

import snedeker.goetz.ir.assignmentOne.models.QueryResults;
import snedeker.goetz.ir.assignmentOne.utils.LuceneResults;

import snedeker.goetz.ir.assignmentOne.utils.Metrics;

public class AssignmentApplication {

	static Logger log = Logger.getLogger(AssignmentApplication.class);

	static String[] queries = { "test", "properties", "slipstream", "sample", "experiment", "the*ry",
			"to be or not to be" };

	static LuceneResults resultSet = new LuceneResults();

	static void clean(SearchAppliance app) throws IOException {
		FileUtils.deleteDirectory(new File(app.getType() + File.separator + "index"));
		FileUtils.deleteDirectory(new File(app.getType() + File.separator + "documents"));
		FileUtils.deleteDirectory(new File(app.getType()));
	}

	public static void main(String[] args) throws IOException, InterruptedException {

		SearchAppliance standardAnalyzerSearch = new SearchAppliance(STD_ANALYZER);
		clean(standardAnalyzerSearch);
		createIndex(standardAnalyzerSearch);
		indexMetrics(standardAnalyzerSearch);
		performQueries(standardAnalyzerSearch);

		String retrieveDocument = standardAnalyzerSearch.retrieveDocument(1);
		log.debug(retrieveDocument);

		SearchAppliance englishAnalyzerSearch = new SearchAppliance(ENG_ANALYZER);
		clean(englishAnalyzerSearch);
		englishAnalyzerSearch.setAnalyzer(new EnglishAnalyzer());
		createIndex(englishAnalyzerSearch);
		indexMetrics(englishAnalyzerSearch);
		performQueries(englishAnalyzerSearch);

		resultSet.printEvaluation();

	}

	static void performQueries(SearchAppliance app) throws IOException {
		for (String query : queries) {
			QueryResults results = app.performQuery(query);
			log.debug("Query for '" + query + "'...");
			log.debug("Query completed in " + results.getQueryTime() + "ms");
			log.debug("Query obtained " + results.getHits() + " results");
			log.debug("\n");
			if (results.getHits() > 0) {
				log.debug("Hits found for " + query);
				resultSet.insertData(app.getType(), query, results.getQueryTime());
			} else {
				log.debug("Not hits found for " + query);
			}

		}
	}

	static void indexMetrics(SearchAppliance app) {
		int size = Metrics.getFolderSizeOnDisk(new File(app.getIndexPath()));
		int docs = Metrics.getDocumentCount(new File(app.getFileStore()));
		log.debug(app.getType() + " Index currently residing within " + size + " bytes on disk.");
		log.debug(app.getType() + " Index stores " + docs + " documents.");
	}

	static void createIndex(SearchAppliance app) throws IOException {
		log.debug("Creating " + app.getType() + " Index...");
		app.setIndexPath(app.getType() + File.separator + "index");
		app.createIndex();
		log.debug("Index " + app.getType() + " Index Created.");
	}

	static final String STD_ANALYZER = "StandardAnalyzer";
	static final String ENG_ANALYZER = "EnglishAnalyzer";
}
