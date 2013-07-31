package org.eclipse.recommenders.snipmatch.handlers;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.recommenders.snipmatch.core.Snippet;
import org.eclipse.recommenders.snipmatch.core.TemplateProcessor;
import org.eclipse.recommenders.snipmatch.rcp.UserEnvironment;
import org.eclipse.recommenders.snipmatch.search.SearchBox;
import org.eclipse.recommenders.snipmatch.search.SnipMatchSearchEngine;
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

		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		if (editor instanceof JavaEditor) {
			UserEnvironment env = new UserEnvironment(editor);

			//if (searchBox == null) {
				searchBox = new SearchBox(env, this);
			//}

			searchBox.show();
			snipMatchSearchEngine = new SnipMatchSearchEngine();
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
			return;
		} else {
			lastQuery = query;
			searchResult = snipMatchSearchEngine.search(query);
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
}
