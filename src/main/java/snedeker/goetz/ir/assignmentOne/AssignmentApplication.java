package snedeker.goetz.ir.assignmentOne;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.en.EnglishAnalyzer;

import snedeker.goetz.ir.assignmentOne.models.QueryResults;
import snedeker.goetz.ir.assignmentOne.utils.DataSource;
import snedeker.goetz.ir.assignmentOne.utils.LuceneResults;

import snedeker.goetz.ir.assignmentOne.utils.Metrics;

public class AssignmentApplication {

	static Logger log = Logger.getLogger(AssignmentApplication.class);

	static String[] queries = { "test", "properties", "slipstream", "sample", "experiment" };

	static LuceneResults resultSet = new LuceneResults();

	public static void eval() throws IOException {
		IndexEvaluator evaluator = new IndexEvaluator();
		evaluator.runEvals();
	}

	public static void stock() throws IOException {
		SearchAppliance standardAnalyzerSearch = new SearchAppliance(STD_ANALYZER, DataSource.CRAN, null);
		createIndex(standardAnalyzerSearch);
		indexMetrics(standardAnalyzerSearch);
		performQueries(standardAnalyzerSearch);

		SearchAppliance englishAnalyzerSearch = new SearchAppliance(ENG_ANALYZER, DataSource.CRAN, null);
		englishAnalyzerSearch.setAnalyzer(new EnglishAnalyzer());
		createIndex(englishAnalyzerSearch);
		indexMetrics(englishAnalyzerSearch);
		performQueries(englishAnalyzerSearch);

		String retrieveDocument = standardAnalyzerSearch.retrieveDocument(1);
		log.debug(retrieveDocument);

		resultSet.printEvaluation();
	}

	public static void query() throws IOException {

		SearchAppliance app = new SearchAppliance(AssignmentApplication.STD_ANALYZER, DataSource.CRAN, null);
		app.createIndex();

		try (Scanner scanner = new Scanner(System.in)) {

			boolean active = true;
			while (active) {
				System.out.println(
						"Query or Retrieve documents from the index (prefix queries with \"q \" and retrievals with \"d \"). x to exit");
				String line = scanner.nextLine();
				if (line.startsWith("d ")) {
					System.out.println("Performing Document Retrieval...");
					String docIdString = line.substring(2).trim();
					if (StringUtils.isNumeric(docIdString)) {
						String documentString = app.retrieveDocument(Integer.parseInt(docIdString) + 1);
						System.out.println(documentString.isEmpty() ? "<Empty>" : documentString);
						continue;
					} else {
						System.out.println("Document retrieval must be via document ID");
						continue;
					}
				} else if (line.startsWith("q ")) {
					System.out.println("Performing Query...");
					String queryString = line.substring(2);
					QueryResults results = app.performQuery(queryString);
					Map<Integer, String> resultsMap = results.getResultsMap();
					for (Integer key : resultsMap.keySet()) {
						String title = resultsMap.get(key);
						if (title.length() > 65) {
							System.out.println(key + ": " + title.substring(0, 65));
						} else {
							System.out.println(key + ": " + title);
						}

					}
					System.out.println("Found " + results.getHits() + " in " + results.getQueryTime() + "ms");
					continue;

				} else if (line.equals("x")) {
					System.out.println("Exitting...");
					System.exit(0);
				} else {

					System.out.println("Invalid syntax");
				}

			}
		}

	}

	public static void main(String[] args) throws IOException, InterruptedException {

		if (args.length < 1) {
			log.error("Application must obtain at least 1 cmd line arg");
			System.exit(1);
		}

		String intent = args[0];

		switch (intent) {
		case "evaluate":
			eval();
			break;
		case "stock":
			stock();
			break;
		case "query":
			query();
			break;
		}

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
		log.debug(app.getIndexPath());
		int size = Metrics.getFolderSizeOnDisk(new File(app.getIndexPath()));
		int docs = Metrics.getDocumentCount(new File(app.getFileStore()));
		log.debug(app.getType() + " Index currently residing within " + size + " bytes on disk.");
		log.debug(app.getType() + " Index stores " + docs + " documents.");
	}

	static void createIndex(SearchAppliance app) throws IOException {
		log.debug("Creating " + app.getType() + " Index...");
		app.setIndexPath(app.getType() + app.getIndexId() + File.separator + "index");
		app.createIndex();
		log.debug("Index " + app.getType() + " Index Created.");
	}

	static final String STD_ANALYZER = "StandardAnalyzer";
	static final String ENG_ANALYZER = "EnglishAnalyzer";
	static final String CUSTOM_STD_ANALYZER = "CustomStd";
}
