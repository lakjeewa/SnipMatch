package org.eclipse.recommenders.snipmatch.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.recommenders.snipmatch.rcp.Activator;
import org.eclipse.recommenders.snipmatch.rcp.UserEnvironment;

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

		UserEnvironment env = new UserEnvironment();

		if (env.isJavaEditor())
			Activator.getDefault().showSearchBox();

		return null;
	}
}
