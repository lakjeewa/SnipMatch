package org.eclipse.recommenders.snipmatch.search;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

/**
 * This Listener dispose the shell when a focus event take place on the active
 * editor.
 * 
 */
public class EditorFocusListener implements Listener {

	private Shell shell = null;

	public EditorFocusListener(Shell s) {
		shell = s;
	}

	@Override
	public void handleEvent(Event event) {
		// if (shell != null &&
		// event.widget.equals(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()))
		// shell.dispose();
		if (shell != null) {
			Widget widget = event.widget;
			for (Control child : shell.getChildren()) {
				if (child == widget)
					return;
			}
		}
		shell.dispose();

	}

}
