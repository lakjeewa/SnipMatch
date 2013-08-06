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

		Widget widget = event.widget;

		// if (shell != null &&
		// event.widget.equals(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()))
		// shell.dispose();

		// shell.getShell

		if (shell != null) {

			if (widget.equals(shell)) {
				return;
			}
			for (Control child : shell.getChildren()) {
				if (child == widget)
					return;
			}

			Shell[] subShells = shell.getShells();

			if (subShells.length > 0) {
				for (Shell subShell : subShells) {

					if (widget.equals(subShell))
						return;

					for (Control child : subShell.getChildren()) {
						if (child == widget)
							return;
					}
				}
			}

			shell.dispose();
		}

	}
}
