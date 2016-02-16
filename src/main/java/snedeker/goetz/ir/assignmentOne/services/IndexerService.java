package snedeker.goetz.ir.assignmentOne.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import snedeker.goetz.ir.assignmentOne.models.EntryDocument;
import snedeker.goetz.ir.assignmentOne.utils.LuceneConstants;
import snedeker.goetz.ir.assignmentOne.utils.Metrics;

public class IndexerService {

	Logger log = Logger.getLogger(getClass());
	private StandardAnalyzer analyzer;
	Directory index;

	public IndexerService() throws IOException {
		File indexDirectory = new File(LuceneConstants.INDEX_PATH);

		if (indexDirectory.exists()) {
			log.debug("Index Directory exists.  Clearing directory...");
			FileUtils.deleteDirectory(indexDirectory);
		}

		Path path = indexDirectory.toPath();
		log.debug("Creating Index Directory at: " + path.toString());
		analyzer = new StandardAnalyzer();
		index = FSDirectory.open(path);
	}

	public void run() throws IOException {
		Map<String, Long> postings = new HashMap<>();

		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setOpenMode(OpenMode.CREATE_OR_APPEND);

		ArrayList<EntryDocument> texts = loadTexts();

		try (IndexWriter w = new IndexWriter(index, config)) {
			for (EntryDocument entry : texts) {
				addDoc(w, entry);
			}

			// Obtain details regarding the index here
			IndexReader reader = DirectoryReader.open(w, false);
			Fields fields = MultiFields.getFields(reader);
			for (String field : fields) {
				Terms terms = fields.terms(field);
				TermsEnum iterator = terms.iterator();
				BytesRef byteRef = null;
				while ((byteRef = iterator.next()) != null) {
					String term = byteRef.utf8ToString();
					postings.put(term, new Long(iterator.totalTermFreq()));
				}

			}
		}

		Metrics.printIndexTerms(postings);

	}

	private ArrayList<EntryDocument> loadTexts() throws IOException {
		ArrayList<EntryDocument> texts = new ArrayList<>();

		InputStream stream = IndexerService.class.getClassLoader().getResourceAsStream("cran.all");
		String text = IOUtils.toString(stream);

		// Split into docs
		String[] splitDocs = text.split(".I");

		for (String doc : splitDocs) {
			if (doc != null && !doc.isEmpty()) {
				EntryDocument newEntry = EntryDocument.buildEntry(doc);
				if(newEntry != null){
					texts.add(newEntry);
				}
			}
		}

		return texts;
	}

	private void addDoc(IndexWriter w, EntryDocument entry) throws IOException {
		Document doc = new Document();
		doc.add(new TextField("docId", entry.getDocId().toString(), Field.Store.YES));
		doc.add(new TextField("title", entry.getTitle(), Field.Store.YES));
		doc.add(new TextField("content", entry.getContents(), Field.Store.YES));
		doc.add(new TextField("author", entry.getAuthor(), Field.Store.YES));
		doc.add(new TextField("additional", entry.getAdditional(), Field.Store.YES));
		w.addDocument(doc);
	}
}