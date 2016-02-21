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
import snedeker.goetz.ir.assignmentOne.utils.Metrics;
import snedeker.goetz.ir.assignmentOne.utils.DataSource;

public class SearchAppliance {

	static Logger log = Logger.getLogger(SearchAppliance.class);

	private IndexerService indexerService;
	private QueryService queryService;
	private int indexSize = 0;
	private String indexPath = "index";
	private String type;

	public SearchAppliance(String type, DataSource source) throws IOException {
		this.type = type;
		indexerService = new IndexerService(type, source);
		queryService = new QueryService(type);
	}

	public void setAnalyzer(Analyzer analyzer) {
		indexerService.setAnalyzer(analyzer);
		queryService.setAnalyzer(analyzer);
	}

	public void createIndex() throws IOException {
		indexerService.createIndex();
		setIndexSize(Metrics.getFolderSizeOnDisk(new File(indexPath)));
	}

	public void releaseIndex() throws IOException {
		indexerService.releaseIndex();
		queryService.releaseIndex();
		FileUtils.deleteDirectory(new File(getIndexPath()));
	}

	public QueryResults performQuery(String query) throws IOException {
		return queryService.query(query);
	}

	public String retrieveDocument(Integer documentId) throws FileNotFoundException, IOException {

		log.debug("Retriving document with ID = " + documentId);

		long start = System.currentTimeMillis();
		File file = new File(type + File.separator + "documents" + File.separator + documentId.toString());

		log.debug(file.getAbsolutePath().toString());

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

	public String getType() {
		return type;
	}

	public String getFileStore() {
		return type + File.separator + "documents";
	}

}
