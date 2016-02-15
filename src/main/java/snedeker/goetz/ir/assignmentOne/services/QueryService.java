package snedeker.goetz.ir.assignmentOne.services;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.apache.log4j.Logger;
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

import snedeker.goetz.ir.assignmentOne.utils.LuceneConstants;

public class QueryService {

	Logger log = Logger.getLogger(getClass());
	private StandardAnalyzer analyzer;
	Directory index;

	public QueryService() throws IOException {
		File indexDirectory = new File(LuceneConstants.INDEX_PATH);
		analyzer = new StandardAnalyzer();
		index = FSDirectory.open(indexDirectory.toPath());
	}

	public void run() throws ParseException, IOException {
		System.out.println("Enter your query: ");

		try (Scanner scanner = new Scanner(System.in)) {

			while (scanner.hasNext()) {
				String queryString = scanner.nextLine();
				System.out.println("Your query is " + queryString);

				long start = System.currentTimeMillis();

				Query q = new QueryParser("content", analyzer).parse(queryString);

				int hitsPerPage = 9999;
				IndexReader reader = DirectoryReader.open(index);
				IndexSearcher searcher = new IndexSearcher(reader);
				TopDocs docs = searcher.search(q, hitsPerPage);

				long finish = System.currentTimeMillis();

				log.debug("Query Completed in " + (finish - start) + " milliseconds");

				ScoreDoc[] hits = docs.scoreDocs;

				System.out.println("Found " + hits.length + " hits\n");
				for (int i = 0; i < hits.length; ++i) {
					int docId = hits[i].doc;
					Document d = searcher.doc(docId);
					System.out.println((i + 1) + ". " + d.get("title"));
				}
				System.out.println();
			}
		}
	}
}
