package snedeker.goetz.ir.assignmentOne.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import snedeker.goetz.ir.assignmentOne.models.EntryDocument;
import snedeker.goetz.ir.assignmentOne.utils.LuceneConstants;

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
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setOpenMode(OpenMode.CREATE_OR_APPEND);

		ArrayList<EntryDocument> texts = loadTexts();

		try (IndexWriter w = new IndexWriter(index, config)) {
			for (EntryDocument entry : texts) {
				addDoc(w, entry);
			}
		}
	}

	private ArrayList<EntryDocument> loadTexts() throws IOException {
		ArrayList<EntryDocument> texts = new ArrayList<>();

		InputStream stream = IndexerService.class.getClassLoader().getResourceAsStream("cran.all");

		File inputFile = File.createTempFile("IR_AssignmentOne", ".txt");
		inputFile.deleteOnExit();
		FileUtils.copyInputStreamToFile(stream, inputFile);

		String text = FileUtils.readFileToString(inputFile);

		// Split into docs
		String[] splitDocs = text.split(".I");

		for (String doc : splitDocs) {
			EntryDocument newEntry = getEntry(doc);

			if (newEntry != null)
				texts.add(newEntry);
		}

		return texts;
	}

	private EntryDocument getEntry(String doc) {
		EntryDocument entry = new EntryDocument();

		if (!doc.equals("")) {
			// Get ID
			String[] docId = doc.split(".T");
			entry.setDocId(StringUtils.trim(docId[0]));

			// get title
			String[] title = docId[1].split(".A");
			entry.setTitle(StringUtils.trim(title[0]));

			// get author
			String[] author = title[1].split(".B");
			entry.setAuthor(StringUtils.trim(author[0]));

			// get additional
			String[] additional = author[1].split(".W");
			entry.setAdditional(StringUtils.trim(additional[0]));

			// get contents
			entry.setContents(StringUtils.trim(additional[1]));

			return entry;
		}

		return null;
	}

	private void addDoc(IndexWriter w, EntryDocument entry) throws IOException {
		Document doc = new Document();
		doc.add(new TextField("docId", entry.getDocId(), Field.Store.YES));
		doc.add(new TextField("title", entry.getTitle(), Field.Store.YES));
		doc.add(new TextField("content", entry.getContents(), Field.Store.YES));
		doc.add(new TextField("author", entry.getAuthor(), Field.Store.YES));
		doc.add(new TextField("additional", entry.getAdditional(), Field.Store.YES));
		w.addDocument(doc);
	}
}