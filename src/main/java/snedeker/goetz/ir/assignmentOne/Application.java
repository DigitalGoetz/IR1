package snedeker.goetz.ir.assignmentOne;

import java.io.File;
import java.io.IOException;

import snedeker.goetz.ir.assignmentOne.models.QueryResults;
import snedeker.goetz.ir.assignmentOne.services.IndexerService;
import snedeker.goetz.ir.assignmentOne.services.QueryService;
import snedeker.goetz.ir.assignmentOne.utils.LuceneConstants;
import snedeker.goetz.ir.assignmentOne.utils.Metrics;

public class Application {

	private IndexerService indexerService;
	private QueryService queryService;
	private int indexSize = 0;
	private String indexPath = LuceneConstants.INDEX_PATH;
	private String fileStore = "temp";

	public Application() throws IOException {
		indexerService = new IndexerService();
		queryService = new QueryService();
	}

	public void createIndex() throws IOException {
		indexerService.run();
		setIndexSize(Metrics.getFolderSizeOnDisk(new File(indexPath)));
	}

	public QueryResults performQuery(String query) {
		return queryService.query(query);
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
