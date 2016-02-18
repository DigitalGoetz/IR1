package snedeker.goetz.ir.assignmentOne.services;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import snedeker.goetz.ir.assignmentOne.models.QueryResults;

public class QueryService {

	Logger log = Logger.getLogger(getClass());
	Analyzer analyzer = new StandardAnalyzer();
	String type;
	Directory index;

	public QueryService(String type) {
		this.type = type;
	}

	public QueryResults query(String queryString) throws IOException {
		QueryResults results = new QueryResults();
		File indexDirectory = new File(type + File.separator + "index");
		index = FSDirectory.open(indexDirectory.toPath());

		try {
			long start = System.currentTimeMillis();

			Query q = new QueryParser("content", analyzer).parse(queryString);

			int hitsPerPage = 9999;
			IndexReader reader = DirectoryReader.open(index);
			IndexSearcher searcher = new IndexSearcher(reader);
			TopDocs docs = searcher.search(q, hitsPerPage);

			long finish = System.currentTimeMillis();

			results.setQueryTime(finish - start);

			ScoreDoc[] hits = docs.scoreDocs;

			results.setHits(hits.length);

			for (int i = 0; i < hits.length; ++i) {
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				results.getResultsMap().put(d.get("title"), docId);
			}

		} catch (ParseException e) {
			log.error("Error Parsing Query String", e);
		} catch (IOException e) {
			log.error("Error Reading from Index Directory", e);
		}

		return results;
	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
}
