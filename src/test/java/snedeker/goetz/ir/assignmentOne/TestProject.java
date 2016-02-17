package snedeker.goetz.ir.assignmentOne;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.Test;

import snedeker.goetz.ir.assignmentOne.models.QueryResults;
import snedeker.goetz.ir.assignmentOne.utils.Metrics;

public class TestProject {

	Logger log = Logger.getLogger(getClass());

	String[] queries = { "test", "properties", "slipstream", "sample", "experiment", "the*ry" };

	@Test
	public void testQueryApplication() throws IOException {

		// Create Index
		log.debug("Creating Index...");
		Application testApplication = new Application();
		testApplication.createIndex();
		log.debug("Index Created.");
		// Obtain Index Metrics
		int size = Metrics.getFolderSizeOnDisk(new File(testApplication.getIndexPath()));
		int docs = Metrics.getDocumentCount(new File(testApplication.getFileStore()));
		log.debug("Index currently residing within " + size + " bytes on disk.");
		log.debug("Index stores " + docs + " documents.");

		for (String query : queries) {
			QueryResults results = testApplication.performQuery(query);
			log.debug("Query for '" + query + "'...");
			log.debug("Query completed in " + results.getQueryTime() + "ms");
			log.debug("Query obtained " + results.getHits() + " results");
			log.debug("\n");
		}
	}
}