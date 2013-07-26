package org.eclipse.recommenders.snipmatch.search;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
	private String indexFilePath = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.SEARCH_INDEX_FILE);
	private static SnipMatchSearchEngine snipMatchSearchEngine = null;
	private List<IndexRecord> indexRecordsList = null;

	public SnipMatchSearchEngine() {
		initialize();
	}

	/**
	 * Search for the query.
	 * 
	 * @param query
	 *            query given by the user.
	 * @return search result as a Snippet object list.
	 */
	public ArrayList<Snippet> search(String query) {
		ArrayList<Snippet> searchResult = new ArrayList<Snippet>();
		ArrayList<IndexRecord> indexRecordResult = new ArrayList<IndexRecord>();

		for (IndexRecord record : indexRecordsList) {
			if (record.summary.contains(query)) {
				indexRecordResult.add(record);
			}
		}

		for (IndexRecord record : indexRecordResult) {
			File jsonFile = new File(record.filePath);
			if (jsonFile.exists()) {
				Snippet snippet = GsonUtil.deserialize(jsonFile, Snippet.class);
				searchResult.add(snippet);
			}

		}

		return searchResult;
	}

	/**
	 * Initialize search engine.
	 */
	public void initialize() {
		File indexFile = new File(indexFilePath);
		loadIndexFile(indexFile);
	}

	/**
	 * Load index file.
	 * 
	 * @param indexFile
	 *            path to index file.
	 */
	private void loadIndexFile(File indexFile) {
		Type listType = new TypeToken<List<IndexRecord>>() {
		}.getType();
		indexRecordsList = GsonUtil.deserialize(indexFile, listType);
	}

}
