package org.eclipse.recommenders.snipmatch.rcp;

import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * This class provide the information about the user current environment.
 * 
 */
public class UserEnvironment {

	private IEditorPart javaEditor;
	private StyledText styledText;

	public UserEnvironment() {
		javaEditor = (JavaEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	}

	/**
	 * This method find the location to place the SnipMatch search box.
	 * 
	 * @return Point coordinate of the location
	 */
	public Point getSearchBoxAnchor() {

		if (javaEditor != null) {
			styledText = (StyledText) javaEditor.getAdapter(Control.class);
			return styledText.toDisplay(styledText.getCaret().getLocation().x, styledText.getCaret().getLocation().y + styledText.getCaret().getSize().y);
		} else {
			return null;
		}
	}

	/**
	 * Check whether the user is working on Java editor.
	 * 
	 * @return true if the Java editor is opened
	 */
	public boolean isJavaEditor() {
		return javaEditor instanceof JavaEditor;
	}

}
