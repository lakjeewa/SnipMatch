package org.eclipse.recommenders.snipmatch.util;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * Utility class to create MessageDialog
 * 
 */
public class SnipMatchMessageDialog {

	private static Shell shell = null;

	/**
	 * Create MessageDialog.openInformation dialog
	 * 
	 * @param title
	 *            Title of the dialog
	 * @param message
	 *            Message
	 */
	public static void openInformation(String title, String message) {
		if (shell == null) {
			shell = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.NO_TRIM | SWT.NO_FOCUS | SWT.NO_BACKGROUND);
		}
		MessageDialog.openInformation(shell, title, message);
	}

	/**
	 * Create MessageDialog.openError dialog
	 * 
	 * @param title
	 *            Title of the dialog
	 * @param message
	 *            Message
	 */
	public static void openError(String title, String message) {
		if (shell == null) {
			shell = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.NO_TRIM | SWT.NO_FOCUS | SWT.NO_BACKGROUND);
		}
		MessageDialog.openError(shell, title, message);
	}
}
