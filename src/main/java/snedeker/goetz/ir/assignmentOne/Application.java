package snedeker.goetz.ir.assignmentOne;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;

import snedeker.goetz.ir.assignmentOne.models.QueryResults;
import snedeker.goetz.ir.assignmentOne.services.IndexerService;
import snedeker.goetz.ir.assignmentOne.services.QueryService;
import snedeker.goetz.ir.assignmentOne.utils.LuceneConstants;
import snedeker.goetz.ir.assignmentOne.utils.Metrics;

public class Application {

	static Logger log = Logger.getLogger(Application.class);

	private IndexerService indexerService;
	private QueryService queryService;
	private int indexSize = 0;
	private String indexPath = LuceneConstants.INDEX_PATH;
	private String fileStore = "temp";

	public Application() throws IOException {
		indexerService = new IndexerService();
		queryService = new QueryService();
	}

	public void setAnalyzer(Analyzer analyzer) {
		indexerService.setAnalyzer(analyzer);
		queryService.setAnalyzer(analyzer);
	}

	public void createIndex() throws IOException {
		indexerService.createIndex(fileStore);
		setIndexSize(Metrics.getFolderSizeOnDisk(new File(indexPath)));
	}

	public QueryResults performQuery(String query) {
		return queryService.query(query);
	}

	public String retrieveDocument(Integer documentId) throws FileNotFoundException, IOException {

		log.debug("Retriving document with ID = " + documentId);

		long start = System.currentTimeMillis();
		File file = new File(getFileStore() + File.separator + documentId.toString());

		if (!file.exists()) {
			throw new FileNotFoundException("Could not retrieve the requested Document");
		}

		String document = FileUtils.readFileToString(file);
		long finish = System.currentTimeMillis();

		log.debug("Document Retrieved in " + (finish - start) + "ms.");

		return document;

	}

	public int getIndexSize() {
		return indexSize;
	}

	public void setIndexSize(int indexSize) {
		this.indexSize = indexSize;
	}

	public String getIndexPath() {
		return indexPath;
	}

	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
	}

	public String getFileStore() {
		return fileStore;
	}

	public void setFileStore(String fileStore) {
		this.fileStore = fileStore;
	}
}
