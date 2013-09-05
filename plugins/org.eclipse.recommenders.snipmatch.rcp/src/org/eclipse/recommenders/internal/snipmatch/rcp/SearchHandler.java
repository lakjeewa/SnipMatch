package org.eclipse.recommenders.internal.snipmatch.rcp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.recommenders.internal.snipmatch.search.SearchBox;
import org.eclipse.recommenders.internal.snipmatch.search.SnipMatchSearchEngine;
import org.eclipse.recommenders.internal.snipmatch.search.UserEnvironment;
import org.eclipse.recommenders.snipmatch.Snippet;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

public class SearchHandler extends AbstractHandler {

    private String lastQuery;
    private List<Snippet> searchResult = null;
    private SnipMatchSearchEngine snipMatchSearchEngine;
    private SearchBox searchBox = null;

    /**
     * the command has been executed, so extract extract the needed information from the application context.
     */
    public Object execute(ExecutionEvent event) throws ExecutionException {
        File repodir = new File(SnipmatchActivator.getDefault().getPreferenceStore()
                .getString(Constants.P_LOCAL_SNIPPETS_REPO));
        File indexdir = new File(repodir.getParentFile(), "index");

        // TODO: we should inform the user that the index is not accessible yet if this fails
        // otherwise we hit M1+ENTER a hundred times and complain that the plugin is not working... :)
        if (isValidIndex(indexdir)) {

            IEditorPart editor = HandlerUtil.getActiveEditor(event);
            if (editor instanceof JavaEditor) {
                UserEnvironment env = new UserEnvironment(editor);
                snipMatchSearchEngine = new SnipMatchSearchEngine(indexdir);
                searchBox = new SearchBox(env, this);
                searchBox.show();
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
    // TODO MB: Why is this mediated via the search handler? it feels a bit weird to me when looking at it the firt
    // time.
    public void handleTyping(String query) {
        if (lastQuery != null && lastQuery.trim().equals(query.trim())) {
            return; // If the query has not been changed
        } else {
            lastQuery = query;
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
    // TODO what is this? This callback should not be here.
    public void selectEntry(int resultIndex) {
        Snippet snippet = searchResult.get(resultIndex);
        TemplateProcessor templateProcessor = TemplateProcessor.getInstance();
        templateProcessor.insertTemplate(snippet);
    }

    /**
     * This method check whether the index has been updated.
     */
    public boolean isValidIndex(File indexDir) {
        try {
            FSDirectory index = FSDirectory.open(indexDir);
            return IndexReader.indexExists(index);
        } catch (IOException e) {
            return false;
        }
    }
}
