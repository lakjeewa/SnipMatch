package org.eclipse.recommenders.snipmatch.search;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.eclipse.recommenders.snipmatch.core.Snippet;
import org.eclipse.recommenders.snipmatch.preferences.PreferenceConstants;
import org.eclipse.recommenders.snipmatch.rcp.Activator;
import org.eclipse.recommenders.utils.gson.GsonUtil;

import com.google.gson.reflect.TypeToken;

/**
 * 
 * This is the SnipMatch search engine. This processes the query given by the
 * user.
 * 
 */
public class SnipMatchSearchEngine {
	private String indexFilePath = null;
	private List<IndexRecord> indexRecordsList = null;

	public SnipMatchSearchEngine(String indexFilePath) {
		this.indexFilePath = indexFilePath;
	}

	// /**
	// * Search for the query.
	// *
	// * @param query
	// * query given by the user.
	// * @return search result as a Snippet object list.
	// */
	// public ArrayList<Snippet> search(String query) {
	// ArrayList<Snippet> searchResult = new ArrayList<Snippet>();
	// ArrayList<IndexRecord> indexRecordResult = new ArrayList<IndexRecord>();
	//
	// for (IndexRecord record : indexRecordsList) {
	// if (record.summary.contains(query)) {
	// indexRecordResult.add(record);
	// }
	// }
	//
	// for (IndexRecord record : indexRecordResult) {
	// File jsonFile = new File(record.filePath);
	// if (jsonFile.exists()) {
	// Snippet snippet = GsonUtil.deserialize(jsonFile, Snippet.class);
	// searchResult.add(snippet);
	// }
	//
	// }
	//
	// return searchResult;
	// }
	//
	// /**
	// * Initialize search engine.
	// */
	// public void initialize() {
	// File indexFile = new File(indexFilePath);
	// loadIndexFile(indexFile);
	// }
	//
	// /**
	// * Load index file.
	// *
	// * @param indexFile
	// * path to index file.
	// */
	// private void loadIndexFile(File indexFile) {
	// Type listType = new TypeToken<List<IndexRecord>>() {
	// }.getType();
	// indexRecordsList = GsonUtil.deserialize(indexFile, listType);
	// }

	/**
	 * This method performs the Lucene search for a user given search query
	 * 
	 * @param queryLine
	 *            Search query
	 * @param indexPath
	 *            Path to index directory
	 * @return Search result
	 */
	public ArrayList<Snippet> luceneSearch(String queryLine, String indexPath) {
		ArrayList<Snippet> searchResult = new ArrayList<Snippet>();

		if (queryLine.trim() != "") {
			IndexReader reader = null;
			try {
				reader = IndexReader.open(FSDirectory.open(new File(indexPath)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
			QueryParser parser = new QueryParser(Version.LUCENE_31, "summary", analyzer);
			Query query = null;
			try {
				query = parser.parse(queryLine);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			ScoreDoc[] hits = null;
			try {
				hits = searcher.search(query, null, 100).scoreDocs;
			} catch (IOException e) {
				e.printStackTrace();
			}

			for (int i = 0; i < hits.length; i++) {
				Document doc = null;
				try {
					doc = searcher.doc(hits[i].doc);
				} catch (IOException e) {
					e.printStackTrace();
				}
				String path = doc.get("path");
				File jsonFile = new File(path);
				if (jsonFile.exists()) {
					Snippet snippet = GsonUtil.deserialize(jsonFile, Snippet.class);
					searchResult.add(snippet);
				}
			}

			try {
				searcher.close();
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return searchResult;
	}
}
