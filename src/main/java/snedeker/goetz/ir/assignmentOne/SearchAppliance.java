package snedeker.goetz.ir.assignmentOne;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;

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
	private String indexId;
	private DataSource source;

	public String getIndexId() {
		return indexId;
	}

	public SearchAppliance(String type, DataSource source, List<String> additionalStopwords) throws IOException {
		this.type = type;
		this.source = source;
		indexId = UUID.randomUUID().toString();

		indexerService = new IndexerService(type, source, indexId);
		queryService = new QueryService(type, indexId);

		if (type.equals(AssignmentApplication.ENG_ANALYZER)) {
			this.setAnalyzer(new EnglishAnalyzer());
		}
		if (type.equals(AssignmentApplication.CUSTOM_STD_ANALYZER)) {
			if (additionalStopwords == null) {
				additionalStopwords = new ArrayList<>();
			}

			List<String> stopwords = new ArrayList<String>();

			for (Object word : StandardAnalyzer.STOP_WORDS_SET) {
				char[] stopword = (char[]) word;
				stopwords.add(new String(stopword));
			}

			for (String word : additionalStopwords) {
				stopwords.add(word.toLowerCase());
			}

			CharArraySet stopwordArray = new CharArraySet(stopwords, true);
			Analyzer custom = new StandardAnalyzer(stopwordArray);
			this.setAnalyzer(custom);
		}

	}

	public IndexerService getIndexer() {
		return indexerService;
	}

	public QueryService getQueryHandler() {
		return queryService;
	}

	public DataSource getSource() {
		return source;
	}

	public void setAnalyzer(Analyzer analyzer) {
		indexerService.setAnalyzer(analyzer);
		queryService.setAnalyzer(analyzer);
	}

	public void createIndex() throws IOException {
		indexerService.createIndex();
		setIndexSize(Metrics.getFolderSizeOnDisk(new File(indexPath)));
	}

	public QueryResults performQuery(String query) throws IOException {
		return queryService.query(query);
	}

	public String retrieveDocument(Integer documentId) throws FileNotFoundException, IOException {

		log.debug("Retriving document with ID = " + documentId);

		long start = System.currentTimeMillis();
		File file = new File(type + indexId + File.separator + "documents" + File.separator + documentId.toString());

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
		return type + indexId + File.separator + "documents";
	}

}
