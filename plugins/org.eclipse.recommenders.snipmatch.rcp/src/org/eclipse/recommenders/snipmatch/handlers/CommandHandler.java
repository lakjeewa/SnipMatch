package org.eclipse.recommenders.snipmatch.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.recommenders.snipmatch.rcp.UserEnvironment;
import org.eclipse.recommenders.snipmatch.search.SearchBox;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class CommandHandler extends AbstractHandler {
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
			SearchBox searchBox = new SearchBox(env);
			searchBox.show();
		}

		return null;
	}
}
