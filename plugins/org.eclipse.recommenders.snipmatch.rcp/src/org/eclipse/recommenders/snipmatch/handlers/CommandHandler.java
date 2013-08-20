package org.eclipse.recommenders.snipmatch.handlers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.recommenders.snipmatch.core.Snippet;
import org.eclipse.recommenders.snipmatch.core.TemplateProcessor;
import org.eclipse.recommenders.snipmatch.preferences.PreferenceConstants;
import org.eclipse.recommenders.snipmatch.rcp.Activator;
import org.eclipse.recommenders.snipmatch.rcp.UserEnvironment;
import org.eclipse.recommenders.snipmatch.search.SearchBox;
import org.eclipse.recommenders.snipmatch.search.SnipMatchSearchEngine;
import org.eclipse.recommenders.snipmatch.util.SnipMatchMessageDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class CommandHandler extends AbstractHandler {

	private String lastQuery;
	private List<Snippet> searchResult = null;
	private SnipMatchSearchEngine snipMatchSearchEngine;
	private SearchBox searchBox = null;

	/**
	 * The constructor.
	 */
	public CommandHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String localSnippetRepoDirPath = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.LOCAL_SNIPPETS_REPO);
		String indexDirPath = localSnippetRepoDirPath + System.getProperty("file.separator") + "index";

		if (isIndexUpdated(indexDirPath)) {
			IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

			if (editor instanceof JavaEditor) {
				UserEnvironment env = new UserEnvironment(editor);
				searchBox = new SearchBox(env, this);
				searchBox.show();
				snipMatchSearchEngine = new SnipMatchSearchEngine(indexDirPath);
			}
		}

		return null;
	}

	/**
	 * This method handle the type action on the search query text box
	 * 
	 * @param query
	 *            search query
	 */
	public void handleTyping(String query) {

		if (lastQuery != null && lastQuery.trim().equals(query.trim())) {
			return; // If the query has not been changed
		} else {
			lastQuery = query;
			// searchResult = snipMatchSearchEngine.search(query);
			searchResult = snipMatchSearchEngine.luceneSearch(query);
			searchBox.displayResults(searchResult);
		}
	}

	/**
	 * This method insert the code snippet into the editor
	 * 
	 * @param resultIndex
	 *            Index of selected snippet
	 */
	public void selectEntry(int resultIndex) {
		Snippet snippet = searchResult.get(resultIndex);
		System.out.println(snippet.getCode());
		TemplateProcessor templateProcessor = new TemplateProcessor();
		templateProcessor.insertTemplate(snippet);
	}

	/**
	 * This method check whether the index has been updated.
	 * 
	 * @param indexDirPath
	 *            Path to index directory
	 * @return true or false
	 */
	public boolean isIndexUpdated(String indexDirPath) {
		boolean indexExists = false;
		File indexDir = new File(indexDirPath);

		if (!indexDir.isDirectory() || !indexDir.exists()) {

			SnipMatchMessageDialog.openError("Index has not been updated", "Please enter the path and update the SnipMatch search index in preference window.");
			return false;

		} else {

			try {
				indexExists = IndexReader.indexExists(FSDirectory.open(indexDir));
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (indexExists) {
				return true;
			} else {
				SnipMatchMessageDialog.openError("Index has not been updated",
						"Please enter the path and update the SnipMatch search index in preference window.");
				return false;
			}

		}
	}
}
