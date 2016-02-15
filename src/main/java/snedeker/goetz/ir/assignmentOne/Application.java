package snedeker.goetz.ir.assignmentOne;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.queryparser.classic.ParseException;

import snedeker.goetz.ir.assignmentOne.services.IndexerService;
import snedeker.goetz.ir.assignmentOne.services.QueryService;
import snedeker.goetz.ir.assignmentOne.utils.LuceneConstants;
import snedeker.goetz.ir.assignmentOne.utils.Metrics;

public class Application {

	static Logger log = Logger.getLogger(Application.class);

	public static void main(String[] args) {

		try {
			IndexerService indexerService = new IndexerService();
			indexerService.run();

			int indexSize = Metrics.getFolderSizeOnDisk(new File(LuceneConstants.INDEX_PATH));
			log.debug("Index: " + indexSize + " bytes on disk.");

		} catch (Exception e) {
			log.error("IndexerService failed to complete", e);
		}

		QueryService queryService;
		try {
			queryService = new QueryService();
			while (true)
				queryService.run();
		} catch (IOException | ParseException e) {
			log.error("QueryService failed to complete", e);
		}
	}
}
